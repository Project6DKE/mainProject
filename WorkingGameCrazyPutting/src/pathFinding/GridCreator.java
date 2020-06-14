package pathFinding;

import MainProject.PuttingCourse;
import MainProject.Vector2d;

public class GridCreator {
	GridNode[][] nodeList;
	
	PuttingCourse theCourse;
	
	// The distance between the centerpoint of each node
	double nodeDistance;
	
	Vector2d ballPosition;
	Vector2d flagPosition;
	
	// I'll need an extra constructor in case I'm giving it a separate ball position
	// Blah, to do later
	GridCreator(PuttingCourse aCourse){
		this.theCourse = aCourse;
		this.ballPosition = aCourse.get_start_position();
		this.flagPosition = aCourse.get_flag_position();
		this.setNodeDistance();
		
	}
	
	// There needs to be a way of increasing the grid size in case no path can be found
	// Let's do that and decrease nodeSize at the same time because fuck it
	// Initial gridSize is dist from flag to ball * 2 rounded up
	// Divided by the nodeDistance, rounded.
	void createGrid() {
		int gridSize = (int) Math.round(((ballPosition.get_distance(flagPosition)*2)/this.nodeDistance));
		this.nodeList = new GridNode[gridSize][gridSize];
		
	}
	
	// Setting initial distance on which is smaller between vMax and distance between ball and flag
	// And just dividing it by 100
	void setNodeDistance() {
		
		
		double ballFlagDist = ballPosition.get_distance(flagPosition);
		double vMax = this.theCourse.get_maximum_velocity();
		
		double nodeSize;
		
		if (ballFlagDist > vMax) {
			nodeSize = vMax;
		} else {
			nodeSize = ballFlagDist;
		}
		
		this.nodeDistance = nodeSize/100.0;
		
		
	}
	
}
