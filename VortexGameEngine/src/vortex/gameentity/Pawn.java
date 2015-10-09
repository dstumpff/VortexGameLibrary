package vortex.gameentity;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.*;

import vortex.Game;
import vortex.GameEntity;
import vortex.collision.*;

/**
 * Pawn is the most basic type of GameEntity.
 * 
 * @author Daniel Stumpff
 *
 */
public class Pawn extends GameEntity{
	
	/**
	 * The shape to draw.
	 */
	protected Shape shape;
	/**
	 * Modifier to draw outline or fill shape.
	 */
	protected boolean renderFilled;
	
	/**
	 * The default constructor. Sets the spawn point to the center of the screen.
	 */
	public Pawn(){
		super();
		renderFilled = true;
	}
	
	/**
	 * A constructor with parameters.
	 * 
	 * @param startPoint The x and y coordinates to spawn at
	 * @param width The width of the entity
	 * @param height The height of the entity.
	 */
	public Pawn(Point startPoint, float width, float height){
		super(startPoint, width, height);
		renderFilled = true;
	}
	
	/**
	 * A constructor with parameters.
	 * 
	 * @param x The x coordinate to spawn at
	 * @param y The y coordinate to spawn at
	 * @param width The width of the entity
	 * @param height The height of the entity
	 */
	public Pawn(float x, float y, float width , float height){
		super(x, y, width, height);
		renderFilled = true;
	}
	
	/**
	 * Tells the pawn what type of shape to draw as. Currently compatible with the following shapes:
	 * 
	 * <ul>
	 * 		<li>Rectangle</li>
	 * 		<li>Ellipse</li>
	 * 		<li>TriangleNorth - A triangle with the point facing north</li>
	 * 		<li>TriangleSouth - A triangle with the point facing south</li>
	 * 		<li>TriangleEast - A triangle with the point facing east</li>
	 * 		<li>TriangleWest - A triangle with the point facing west</li>
	 * </ul>
	 * 
	 * @param shapeID The shape to use
	 */
	public void setShape(String shapeID){
		if(shapeID.equals("Rectangle")){
			shape = new Rectangle(startPoint.getX(), startPoint.getY(), width, height);
			collisionBox.add(new CollisionRectangle(this, startPoint.getX(), startPoint.getY(), width, height));
			Game.collisionShapes.add(collisionBox.get(0));
		}
		else if(shapeID.equals("Ellipse")){
			shape = new Ellipse(startPoint.getX(), startPoint.getY(), width, height);
		}
		else if(shapeID.equals("TriangleNorth")){
			float[] points = new float[6];
			points[0] = startPoint.getX();
			points[1] = startPoint.getY() + height;
			points[2] = startPoint.getX() + width;
			points[3] = startPoint.getY() + height;
			points[4] = startPoint.getX() + (width / 2);
			points[5] = startPoint.getY();
			shape = new Polygon(points);
		}
		else if(shapeID.equals("TriangleSouth")){
			float[] points = new float[6];
			points[0] = startPoint.getX();
			points[1] = startPoint.getY();
			points[2] = startPoint.getX() + width;
			points[3] = startPoint.getY();
			points[4] = startPoint.getX() + (width / 2);
			points[5] = startPoint.getY() + height;
			shape = new Polygon(points);
		}
		else if(shapeID.equals("TriangleEast")){
			float[] points = new float[6];
			points[0] = startPoint.getX();
			points[1] = startPoint.getY();
			points[2] = startPoint.getX();
			points[3] = startPoint.getY() + height;
			points[4] = startPoint.getX() + width;
			points[5] = startPoint.getY() + (height / 2);
			shape = new Polygon(points);
		}
		else if(shapeID.equals("TriangleWest")){
			float[] points = new float[6];
			points[0] = startPoint.getX() + width;
			points[1] = startPoint.getY();
			points[2] = startPoint.getX() + width;
			points[3] = startPoint.getY() + height;
			points[4] = startPoint.getX();
			points[5] = startPoint.getY() + (height / 2);
			shape = new Polygon(points);
		}
	}
	
	/**
	 * Set if the shape should be rendered filled or not.
	 * 
	 * @param renderFilled If the shape should be filled when rendered
	 */
	public void setRenderFilled(boolean renderFilled){
		this.renderFilled = renderFilled;
	}
	
	/**
	 * Get if the shape should be filled when rendered.
	 * @return If the shape should be filled when rendered
	 */
	public boolean isRenderFilled(){
		return renderFilled;
	}
	
	@Override
	public void init(GameContainer gc) {
		
	}

	@Override
	public void render(GameContainer gc, Graphics g) {
		g.setColor(Color.red);
		if(shape != null){	
			if(renderFilled)
				g.fill(shape);
			else
				g.draw(shape);
			
			g.setColor(Color.black);
			g.draw(shape);
		}
		super.render(gc, g);
	}
	
	public void setX(float x){
		super.setX(x);
		
		if(shape != null)
			shape.setX(x);
	}
	
	public void setY(float y){
		super.setY(y);
		if(shape != null)
			shape.setY(y);
	}
}
