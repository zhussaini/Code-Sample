import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
/**
 * Calculates escape course.
 */
public class CourseCalculator {
	private final int timePerBlastMove;
	private final ArrayList<Asteroid> asteroids;
	private final int lastAsteroidPosition;
	
	private final int MIN_ACCELERATION = -1;
	private final int MAX_ACCELERATION = 1;
	
	public CourseCalculator(final int timePerBlastMove, final ArrayList<Asteroid> asteroids) {
		this.timePerBlastMove = timePerBlastMove;
		this.asteroids = asteroids;
		this.lastAsteroidPosition = asteroids.size();
	}
	/**
	 * Calculates optimal escape course using breadth first search.
	 * @return escape course
	 */
	public LinkedList<Integer> calculateEscapeCourse() {
		// initialize queue for breadth first search
		LinkedList<SpaceshipState> queue = new LinkedList<SpaceshipState>();
		SpaceshipState initialState = new SpaceshipState(0, 0, 0);
		queue.offer(initialState);
		// initialize hashmap to keep track of each explored state's previous state
		HashMap<SpaceshipState, SpaceshipState> previousStates = 
				new HashMap<SpaceshipState, SpaceshipState>();
		previousStates.put(initialState, null);
		
		// breadth first search
		SpaceshipState finalState = initialState;
		while(queue.size() != 0) {
			SpaceshipState currentState = queue.poll();
			// check to see if spaceship has escaped past the asteroids
			if(currentState.getPosition() > lastAsteroidPosition) {
				finalState = currentState;
				break;
			}
			// calculate states reachable from currentState and add them to the queue
			for(int acceleration = MIN_ACCELERATION; acceleration <= MAX_ACCELERATION; 
					++acceleration) {
				SpaceshipState nextState = currentState.accelerate(acceleration);
				// only add state to queue if it has not already been explored 
				// and if spaceship will not die at this state
				if(!previousStates.containsKey(nextState) && isValidSpaceshipState(nextState)) {
					previousStates.put(nextState, currentState);
					queue.offer(nextState);
				}
			}
		}
		
		// backtrack from finalState and calculate course required to escape
		LinkedList<Integer> course = new LinkedList<Integer>();
		SpaceshipState currentState = finalState;
		SpaceshipState previousState = previousStates.get(currentState);
		while(previousState != null) {
			final int acceleration = currentState.getVelocity() - previousState.getVelocity();
			course.addFirst(acceleration);
			currentState = previousState;
			previousState = previousStates.get(currentState);
		}
		return course;		
	}
	
	/** 
	 * Determines whether spaceship will die at specified state.
	 * @param state current spaceship state
	 * @return whether spaceship survives
	 */
	private boolean isValidSpaceshipState(final SpaceshipState state) {
		// check for positions that are inside or outside asteroid belt
		if(state.getPosition() == 0 || state.getPosition() > lastAsteroidPosition) {
			return true;
		}
		// check for death by hitting the planet
		if(state.getPosition() < 0) {
			return false;
		}
		// check for death by blast
		int currentBlastRadius = state.getTime() / timePerBlastMove;
		if(state.getPosition() < currentBlastRadius) {
			return false;
		}
		// check for death by hitting an asteroid
		int currentAsteroidOrientation = asteroids.get(state.getPosition() - 1)
				.currentOrientation(state.getTime());
		if(currentAsteroidOrientation == 0) {
			return false;
		} else {
			return true;
		}
	}
}