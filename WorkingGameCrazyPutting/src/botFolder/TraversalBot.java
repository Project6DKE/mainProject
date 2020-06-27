package botFolder;

import java.util.ArrayList;
import java.util.List;

import MainProject.PuttingCourse;
import MainProject.Vector2d;
import pathFinding.GridNode;
import pathFinding.GridTraversal;

public class TraversalBot extends MergeAI implements PuttingBot {
	
	// TODO: Add a way of saving the ballPath and quickly retrieving it
	// So that if the shot_velocity method is called again it'll just take one more shot and that's it
	List<Vector2d> ballPath;
	
	List<Vector2d> shotSequence;
	
	GridTraversal pathFinder;
	
	int loopsTried;
	
	boolean pathFound;
	
	boolean forward;
	
	
	PuttingCourse theCourse;
	
	// First test it to attempt a hole in one.
	// If it ain't doable, then go backwards and try different shots
	public TraversalBot(){
		this(50);
	}
	
	public TraversalBot(int val) {
		super(val);
		this.ballPath = new ArrayList<Vector2d>();
		this.shotSequence = new ArrayList<Vector2d>();
		
		// TODO: Add info to loops tried so that it will try a maximum amount of times for a given shot
		this.loopsTried = 0;
		this.pathFound = false;
	}
	
	public TraversalBot(boolean bool) {
		this(50);
		this.forward=bool;
	}
	
	// TODO: Store solutions inside traversal bot somehow so that it's easier for it to respond to a given shot
	@Override
	public Vector2d shot_velocity(PuttingCourse course, Vector2d ball_position) throws Exception {
		
		// The gist of this call is to check if the course already exists inside of the code
		// If it does instead of having to go through every step it'll just find the relevant step and return that
		if(course == this.theCourse) {
			int pos = this.findClosestLocation(ball_position);
			
			// Due to how the shot sequence and the ball path are created
			// Ball position (i) is reached by taking shot (i-1)
			if(pos > 0) {
				return this.shotSequence.get(pos-1);
			}
			
		}
		
		this.theCourse = course;
		
		// First test is to check if it's viable or not to take a shot directly towards the flag
		if(super.findIfShotIsValid(course, ball_position, course.get_flag_position())) {
			this.ballPath.add(ball_position);
			this.ballPath.add(course.get_flag_position());
			this.createShotSequence();
			return super.shot_velocity(course, ball_position);
		}
		
		
		GridTraversal traveler = new GridTraversal(course, ball_position);
		
		this.pathFinder = traveler;
		
		
		
		// TODO: add the auxiliary method that will make my life so much easier
		
		List<GridNode> nodes = traveler.processedSolution();
		
		while(!this.pathFound) {
			//this.createForwardShotSequenceList(nodes);
			if(this.forward) {
				this.createForwardShotSequenceList(nodes);
			} else {
				this.createBackwardsShotSequenceList(nodes);
			}
			// This should be the first call so that the loop works properly
			// But my mind isn't working a wy to properly organize it as such
			//  temporary solution
			
			if(!this.pathFound) {
				this.getNewPathFinding();
			}
			
			// TODO: add a loop that'll limit the amount of total tries
			
		}
		
		this.createShotSequence();
		
		
		Vector2d returnShot = this.shotSequence.get(0);
		
		return returnShot;
		
		
	}
	
	
	/**
	 * @param aVec the vector that will be checked internally
	 * @return pos  the position of the closest vector in the ballpath
	 * 				if no vector exists, returns -1
	 */
	public int findClosestLocation(Vector2d aVec) {
		
		for(int i=0; i<this.ballPath.size(); i++) {
			Vector2d compareVec = this.ballPath.get(i);
			
			if(aVec.nearlyEquals(compareVec)) {
				return i;
			}
			
		}
		
		return -1;
		
	}
	
	public List<Vector2d> getBallPath(){
		return this.ballPath;
	}
	
	public List<Vector2d> getShotSequence(){
		return this.shotSequence;
	}
	
	// Sequence is done under the assumption that ballPath.get(0) = ball, ballPath.get(last) = flag
	void createShotSequence() {
		
		for(int i=1; i<this.ballPath.size();i++) {
			Vector2d aimedShot = super.aimedShot(theCourse, this.ballPath.get(i-1), this.ballPath.get(i));
			this.shotSequence.add(aimedShot);
			
		}
		
		
	}
	
	void createForwardShotSequenceList(List<GridNode> listOfNodes) throws Exception {
		
		GridNode comparisonNode = listOfNodes.get(listOfNodes.size()-1);
		
		this.ballPath.add(listOfNodes.get(listOfNodes.size()-1).getCenter());
		
		// I'm using the fact that i=0 is the ball as a cheat
		// TODO: Bugtest this 'cause I'm not used to backwards for loops
		for(int i=listOfNodes.size()-2; i>=0; i--) {
			GridNode aNode = listOfNodes.get(i);
			
			boolean validShot = super.findIfShotIsValid(theCourse, comparisonNode.getCenter(), aNode.getCenter());
			
			if(!validShot){
				i++;
				
				GridNode newSavedNode = listOfNodes.get(i);
				
				this.ballPath.add(newSavedNode.getCenter());
				
				comparisonNode = newSavedNode;
				
				// TODO: Add a check in case the comparisonNode hasn't changed
				// It'll work as a way of notifying the program that the course is not deemed valid

				
			} else {
			
			
			// Idea is that it's a valid shot, and that it's a valid shot towards the flag
			// So it has to be the last possible shot, and as such should be saved
				if(aNode.hasFlag()) {
					this.ballPath.add(aNode.getCenter());
					this.pathFound = true;
					break;
				}
				
				
			}
			
			
		}
		
		
		
	}
	
	
	void createBackwardsShotSequenceList(List<GridNode> listOfNodes) throws Exception {
		
		GridNode comparisonNode = listOfNodes.get(listOfNodes.size()-1);
		this.ballPath.add(listOfNodes.get(listOfNodes.size()-1).getCenter());
		int validSearch = listOfNodes.size()-1;
		
		for(int i=0; i<validSearch;i++) {
			
			GridNode aNode = listOfNodes.get(i);
			
			if((super.findIfShotIsValid(this.theCourse, comparisonNode.getCenter(), aNode.getCenter()) )) {
				this.ballPath.add(aNode.getCenter());
				
				if(aNode.hasFlag()) {
					this.pathFound = true;
					return;
				}
				
				comparisonNode = aNode;
				validSearch = i;
				
				// Crappy solution, but it's to make sure the loop restarts properly
				i=-1;
				
			}
			
			
		}
		
		System.out.println("It ain't working chief");
		
	}
	
	
	List<GridNode> getNewPathFinding() throws Exception {
		this.pathFinder.createNewPath();
		this.ballPath = new ArrayList<Vector2d>();
		this.pathFound = false;
		return this.pathFinder.processedSolution();
	}
	
	public String toString() {
		String forw;
		if(this.forward) {
			forw = "forward";
		} else {
			forw = "backward";
		}
		
		String shot = "," + this.shotSequence.size();
		
		return forw + shot;
	}
	
}