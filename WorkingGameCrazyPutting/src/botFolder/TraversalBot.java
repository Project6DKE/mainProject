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
	
	// First test it to attempt a hole in one.
	// If it ain't doable, then go backwards and try different shots
	public TraversalBot(){
		super(5);
		this.ballPath = new ArrayList<Vector2d>();
		this.shotSequence = new ArrayList<Vector2d>();
		
		// TODO: Add info to loops tried so that it will try a maximum amount of times for a given shot
		this.loopsTried = 0;
		this.pathFound = false;
	}
	
	// TODO: Store solutions inside traversal bot somehow so that it's easier for it to respond to a given shot
	@Override
	public Vector2d shot_velocity(PuttingCourse course, Vector2d ball_position) throws Exception {
		
		// First test is to check if it's viable or not to take a shot directly towards the flag
		if(super.findIfShotIsValid(course, ball_position, course.get_flag_position())) {
			this.ballPath.add(course.get_flag_position());
			return super.shot_velocity(course, ball_position);
		}
		
		GridTraversal traveler = new GridTraversal(course, ball_position);
		
		this.pathFinder = traveler;
		
		
		// TODO: add the auxiliary method that will make my life so much easier
		
		List<GridNode> nodes = traveler.processedSolution();
		
		while(!this.pathFound) {
			this.createForwardShotSequenceList(nodes);
			
			// This should be the first call so that the loop works properly
			// But my mind isn't working a wy to properly organize it as such
			// So screw it, temporary solution
			
			if(!this.pathFound) {
				this.getNewPathFinding();
			}
			
			// TODO: add a loop that'll limit the amount of total tries
			
		}
		
		this.createShotSequence();
		
		return this.shotSequence.get(0);
		
		
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
				// And that life sucks
				
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
				i=0;
				
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
	
}