package vortex.statemachine;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import vortex.event.StateEdgeCondition;

public class StateEdge {
	private State to, from;
	private List<StateEdgeCondition> conditions;
	
	public StateEdge(State from, State to) {
		this.to = to;
		this.from = from;
		conditions = new ArrayList<StateEdgeCondition>();
	}
	
	public State getTo() {
		return to;
	}
	
	public State getFrom() {
		return from;
	}
	
	public synchronized boolean checkConditions() {
		boolean conditionsMet = true;
		Iterator<StateEdgeCondition> iterator = conditions.iterator();
		while(iterator.hasNext()) {
			if(!iterator.next().condition()) {
				conditionsMet = false;
			}
		}
		
		return conditionsMet;
	}
	
	public synchronized void addCondition(StateEdgeCondition condition) {
		conditions.add(condition);
	}
	
	public synchronized void removeCondition(StateEdgeCondition condition) {
		conditions.remove(condition);
	}
}
