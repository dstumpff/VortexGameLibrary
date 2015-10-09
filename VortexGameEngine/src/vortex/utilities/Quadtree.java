package vortex.utilities;

import java.util.ArrayList;

import org.newdawn.slick.geom.Rectangle;

import vortex.collision.CollisionShape;

/**
 * A data structure much like a binary tree except it splits into 4 rather than 2. This data structure is particularly useful
 * for optimizing collision checks. It can help minimize the number of collision checks done. A naive approach to collision 
 * checks results in a O(n^2) algorithm. With this data structure one can reduce it to around O(nlog(n)).
 * 
 * @author Daniel Stumpff - special thanks to Steven Lambert for his Quadtree example
 *
 */
public class Quadtree {
	/**
	 * The max number of objects allowed for each level.
	 */
	private int MAX_OBJECTS = 10;
	
	/**
	 * The current level of the node.
	 */
	private int level;
	/**
	 * The list of objects in this node.
	 */
	private ArrayList<CollisionShape> objects;
	/**
	 * The bounds of this node.
	 */
	private Rectangle bounds;
	/**
	 * The child nodes.
	 */
	private Quadtree[] nodes;
	
	/**
	 * The constructor.
	 * 
	 * @param pLevel The level of this node
	 * @param pBounds The bounds of this node
	 */
	public Quadtree(int pLevel, Rectangle pBounds){
		level = pLevel;
		objects = new ArrayList<CollisionShape>();
		bounds = pBounds;
		nodes = new Quadtree[4];
	}
	
	/**
	 * Clears the entire tree recursively. This can be expensive so my API doesn't use this method.
	 */
	public void clear(){
		objects.clear();
		
		for(int i = 0; i < nodes.length; i++){
			if(nodes[i] != null){
				nodes[i].clear();
				nodes[i] = null;
			}
		}
	}
	
	/**
	 * Splits the current node into 4 smaller nodes.
	 */
	public void split(){
		int subWidth = (int)(bounds.getWidth() / 2);
		int subHeight = (int)(bounds.getWidth() / 2);
		int x = (int)bounds.getX();
		int y = (int)bounds.getY();
		
		nodes[0] = new Quadtree(level + 1, new Rectangle(x + subWidth, y, subWidth, subHeight));
		nodes[1] = new Quadtree(level + 1, new Rectangle(x, y, subWidth, subHeight));
		nodes[2] = new Quadtree(level + 1, new Rectangle(x, y + subHeight, subWidth, subHeight));
		nodes[3] = new Quadtree(level + 1, new Rectangle(x + subWidth, y + subHeight, subWidth, subHeight));
	}
	
	/**
	 * @param pShape The shape to get index of
	 * @return The node pShape is in as an integer
	 */
	public int getIndex(CollisionShape pShape){
		int index = -1;
		float verticalMidpoint = bounds.getX() + (bounds.getWidth() / 2);
		float horizontalMidpoint = bounds.getY() + (bounds.getHeight() / 2);
		
		boolean topQuadrant = (pShape.getY() < horizontalMidpoint && pShape.getY() + pShape.getHeight() < horizontalMidpoint);
		boolean bottomQuadrant = (pShape.getY() > horizontalMidpoint);
		
		if(pShape.getX() < verticalMidpoint && pShape.getX() + pShape.getWidth() < verticalMidpoint){
			if(topQuadrant){
				index = 1;
			}
			else if(bottomQuadrant){
				index = 2;
			}
		}
		else if(pShape.getX() > verticalMidpoint){
			if(topQuadrant){
				index = 0;
			}
			else if(bottomQuadrant){
				index = 3;
			}
		}
		
		return index;
	}
	
	/**
	 * Inserts a collision object into the tree.
	 * 
	 * @param pShape The object to be added
	 */
	public void insert(CollisionShape pShape){
		if(nodes[0] != null){
			int index = getIndex(pShape);
			
			if(index != -1){
				nodes[index].insert(pShape);
				
				return;
			}
		}
		
		objects.add(pShape);
		
		if(objects.size() > MAX_OBJECTS){
			if(nodes[0] == null){
				split();
			}
			
			int i = 0;
			while(i < objects.size()){
				int index = getIndex(objects.get(i));
				if(index != -1){
					nodes[index].insert(objects.get(i));
					objects.remove(i);
				}
				else{
					i++;
				}
			}
		}
	}
	
	/**
	 * Mainly used for debugging purposes.
	 * @param returnNodes The list of nodes.
	 * @return A list of all nodes in the tree
	 */
	public ArrayList<Rectangle> getNodes(ArrayList<Rectangle> returnNodes){
		
		if(nodes[0] != null){
			nodes[0].getNodes(returnNodes);
		}
		if(nodes[1] != null){
			nodes[1].getNodes(returnNodes);
		}
		if(nodes[2] != null){
			nodes[2].getNodes(returnNodes);
		}
		if(nodes[3] != null){
			nodes[2].getNodes(returnNodes);
		}
		
		returnNodes.add(bounds);
		
		return returnNodes;
	}
	
	/**
	 * Retrieves all collision objects that may hit pShape.
	 * 
	 * @param returnObjects The list to be given potential collision objects
	 * @param pShape The shape to check 
	 * @return The list of potential collision objects
	 */
	public ArrayList<CollisionShape> retrieve(ArrayList<CollisionShape> returnObjects, CollisionShape pShape){
		if(nodes[0] != null){
			int index = getIndex(pShape);
			if(index != -1 && nodes[0] != null){
				nodes[index].retrieve(returnObjects, pShape);
			}
			else{
				for(int i = 0; i < nodes.length; i++){
					nodes[i].retrieve(returnObjects, pShape);
				}
			}
		}
		
		returnObjects.addAll(objects);
		
		return returnObjects;
	}
}
