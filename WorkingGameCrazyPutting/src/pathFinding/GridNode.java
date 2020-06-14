package pathFinding;

import MainProject.PuttingCourse;
import MainProject.Vector2d;

// I'm not delimiting the size of the grid node here, it's probably for the best
// But that means I'll have to do an extra check elsewhere for if the grid has the flag
// Blaaaahh
// And it's also good for checking if the node is traversable, as the check can be generalized
public class GridNode {
	
	
	
	
	
	PuttingCourse theCourse;
	
	boolean hasFlag;
	boolean hasBall;
	
	double distToFlag;
	double distToBall;
	
	// If true then this point can be walked through
	// If false it can't
	boolean traversable;
	
	// Centerpoint, used more than all for when the AI has to take a shot
	Vector2d centerPoint;
	
	double length;
	
	// Initializer only works for a course that hasn't been played
	// If it has then other elements need to be updated
	// This initializer should be ignored under all conditions, it's got too many extra methods
	// Will eventually simplify it somehow
	GridNode(PuttingCourse aCourse, Vector2d aPoint){
		this.theCourse = aCourse;
		this.centerPoint = aPoint;
		
		this.distToFlag = theCourse.get_flag_position().get_distance(centerPoint);
		this.distToBall = theCourse.get_flag_position().get_distance(centerPoint);
		
		this.checkTraversable();
		
	}
	
	GridNode(Vector2d aPoint, double length, boolean traverse, Vector2d flagPos){
		this.centerPoint = aPoint;
		this.distToFlag = aPoint.get_distance(flagPos);
		this.traversable = traverse;
		
		// Length should always be positive and this check should be redundant
		// But I don't want to take chances
		this.length = Math.abs(length);
		
		this.hasFlag = this.checkIfLocationIsContained(flagPos);
		
	}
	
	// Only works because the nodes are meant to be squares
	public boolean checkIfLocationIsContained(Vector2d objectLocation) {
		Vector2d temp = this.centerPoint.getVectDist(objectLocation);
		
		if ((Math.abs(temp.get_x()) <=this.length/2.0)
				&& (Math.abs(temp.get_x())<= this.length/2.0)) {
			return true;
		} else {
			return false;
		}
		
		
		
	}
	
	
	// A method to check if for the given putting course the position is traversable
	// For now I'll just take water as untraversable
	// Eventually it should read for obstacles
	// And take the gradient into account somehow
	// But that's for later yooo
	public void checkTraversable() {
		if(this.theCourse.is_water(centerPoint)){
			this.traversable = false;
		} else {
			this.traversable = true;
		}
		
	}

}
