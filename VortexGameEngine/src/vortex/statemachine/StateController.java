package vortex.statemachine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import vortex.event.StateEdgeCondition;
import vortex.exception.StateException;
import vortex.exception.stateexception.IllegalModificationException;
import vortex.exception.stateexception.StateControllerNotRunningException;

public class StateController {
	private State curState;
	private List<State> states;
	private HashMap<Integer, State> idLookupList;
	private boolean running = false;
	
	public StateController() {
		states = new ArrayList<State>();
		idLookupList = new HashMap<Integer, State>();
	}
	
	public void setStartState(State startState) {
		curState = startState;
	}
	
	public void addState(State state) throws StateException, IllegalModificationException{
		if(running) {
			throw new IllegalModificationException("The state machine cannot be modified while running");
		}
		try {
			states.add(state);
			idLookupList.put(state.getStateId(), state);
		}catch(NullPointerException e) {
			throw new StateException("state was null");
		}
	}
	
	public void addEdge(int from, int to, StateEdgeCondition condition) throws StateException, IllegalModificationException {
		if(running) {
			throw new IllegalModificationException("The state machine cannot be modified while running");
		}
		State fromState = idLookupList.get(from);
		State toState = idLookupList.get(to);
		fromState.addEdge(toState, condition);
	}
	
	public void start() {
		running = true;
	}
	
	public void update() throws StateControllerNotRunningException{
		if(!running) {
			throw new StateControllerNotRunningException("The controller was not started");
		}
		Iterator<StateEdge> edgeIterator = curState.getEdges().iterator();
		while(edgeIterator.hasNext()) {
			StateEdge curEdge = edgeIterator.next();
			if(curEdge.checkConditions()) {
				curState = curEdge.getTo();
				curState.performStateAction();
				break;
			}
		}
		
	}
}
