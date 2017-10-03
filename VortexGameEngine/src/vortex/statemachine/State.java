package vortex.statemachine;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import vortex.event.StateEdgeCondition;
import vortex.event.StateEvent;
import vortex.event.StateListener;
import vortex.exception.StateException;

public class State {
	private static int nextId = 0;
	private List<StateListener> eventHandlers;
	private List<StateEdge> edges;
	private int stateId;
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((edges == null) ? 0 : edges.hashCode());
		result = prime * result + ((eventHandlers == null) ? 0 : eventHandlers.hashCode());
		result = prime * result + stateId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		State other = (State) obj;
		if (edges == null) {
			if (other.edges != null)
				return false;
		} else if (!edges.equals(other.edges))
			return false;
		if (eventHandlers == null) {
			if (other.eventHandlers != null)
				return false;
		} else if (!eventHandlers.equals(other.eventHandlers))
			return false;
		if (stateId != other.stateId)
			return false;
		return true;
	}

	public State() {
		stateId = nextId++;
		eventHandlers = new ArrayList<StateListener>();
		edges = new ArrayList<StateEdge>();
	}
	
	public State(List<StateEdge> edges) {
		stateId = nextId++;
		eventHandlers = new ArrayList<StateListener>();
		Iterator<StateEdge> edgeIterator = edges.iterator();
		edges = new ArrayList<StateEdge>();
		while(edgeIterator.hasNext()) {
			edges.add(edgeIterator.next());
		}
	}
	
	public void addEdge(State to, StateEdgeCondition condition) throws StateException{
		try {
			StateEdge newEdge = new StateEdge(this, to);
			newEdge.addCondition(condition);
			edges.add(newEdge);
		}catch(NullPointerException e) {
			throw new StateException("The state the edge is connecting to cannot be null");
		}
	}
	
	public int getStateId() {
		return stateId;
	}
	
	public List<StateEdge> getEdges(){
		return edges;
	}
	
	public void performStateAction() {
		fireStateEvent();
	}
	
	private synchronized void fireStateEvent() {
		Iterator<StateListener> it = eventHandlers.iterator();
		StateEvent event = new StateEvent(this);
		
		while(it.hasNext()) {
			it.next().stateEvent(event);
		}
	}
	
	public synchronized void addStateListener(StateListener listener) {
		eventHandlers.add(listener);
	}
	
	public synchronized void removeStateListener(StateListener listener) {
		eventHandlers.remove(listener);
	}
}
