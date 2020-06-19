package botFolder;

import java.util.ArrayList;
import java.util.List;

import MainProject.PuttingCourse;
import MainProject.Vector2d;
import pathFinding.GridNode;
import pathFinding.GridTraversal;

public class TraversalBot extends MergeAI implements PuttingBot {
	
	List<Vector2d> ballPath;
	
	GridTraversal pathFinder;
	
	// First test it to attempt a hole in one.
	// If it ain't doable, then go backwards and try different shots
	TraversalBot(){
		super();
		this.ballPath = new ArrayList<Vector2d>();
	}
	
	// TODO: Store solutions inside traversal bot somehow so that it's easier for it to respond to a given shot
	@Override
	public Vector2d shot_velocity(PuttingCourse course, Vector2d ball_position) throws Exception {
		
		// First test is to check if it's viable or not to take a shot directly towards the flag
		if(super.findIfShotIsValid(course, ball_position, course.get_flag_position())) {
			return super.shot_velocity(course, ball_position);
		}
		
		GridTraversal traveler = new GridTraversal(course, ball_position);
		
		this.pathFinder = traveler;
		
		
		// TODO: add the auxiliary method that will make my life so much easier
		
		List<GridNode> nodes = traveler.processedSolution();
		
		
		
		for(int i=0; i<nodes.size(); i++) {
			
			GridNode aNode = nodes.get(i);
			
			// TODO: here I want to modify it so that it'll create a subroute of some sort with the necessary shots
			
			if(super.findIfShotIsValid(course, ball_position, aNode.getCenter())) {
				return super.aimedShot(course, ball_position, aNode.getCenter());
			}
			
			
			
		}
		
		
		traveler.createNewPath();
		
		// At this point I'd need to go back and have a way of redoing the travel/grid generation again
		
		
	}
	
	void createValidShotSequenceList(Vector2d ball_position, List<GridNode> listOfNodes, int prevStep) {
		
		for(int i=0; i<prevStep;i++) {
			GridNode aNode = listOfNodes.get(i);
			
			if(aNode.hasBall()) {
				// TODO: some way of saying that there ain't a valid path and life is miserable
			}
			
			if((super.findIfShotIsValid(this.theCourse, ball_position, aNode.getCenter()) )) {
				this.ballPath.add(aNode.getCenter());
				
				this.createValidShotSequenceList(ball_position, listOfNodes, i);
				
				return;
				
			}
			
			
		}
		
		// TODO: Someway of notifying that a new path is needed and life sucks dick
		
		
	}
	
	// TODO: Some kind of loop that'll alternate between going through the solution nodes
	// If no solution is found it'll create a new map and try over again
	
	// TODO: A way of finding all of the relevant shots and storing them somewhere
	// TODO: A way of checking that ALL of the path is doable, not just an individual step
	
}
