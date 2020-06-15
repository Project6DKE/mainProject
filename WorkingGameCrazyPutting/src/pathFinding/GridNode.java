package pathFinding;

import MainProject.PuttingCourse;
import MainProject.Vector2d;

// I'm not delimiting the size of the grid node here, it's probably for the best
// But that means I'll have to do an extra check elsewhere for if the grid has the flag
// Blaaaahh
// And it's also good for checking if the node is traversable, as the check can be generalized
public class GridNode {
	
	
	
	GridNode comesFrom;
	
		
	boolean hasFlag;
	boolean hasBall;
	
	double distToFlag;
	double distToBall;
	
	double univDistScore;
	
	// If true then this point can be walked through
	// If false it can't
	boolean traversable;
	
	// Centerpoint, used more than all for when the AI has to take a shot
	Vector2d centerPoint;
	
	double length;
	
	boolean explored;
	
	// This is a way of making the exploration easier,
	// It only makes sense to check the values that have been updated in their distToBall
	// So by adding this little extra boolean I should be able to hurry up the search a bit
	// Aaah or maybe not.
	// Because I'd have to go through all of the nodes either way, which sounds like absolute shit
	// aahhhh
	boolean toCheck;
	
	
	// GOOD CONSTRUCTOR USE ME
	GridNode(Vector2d aPoint, double length, boolean traverse, Vector2d flagPos){
		this.centerPoint = aPoint;
		this.distToFlag = aPoint.get_distance(flagPos);
		
		// The check for if a point is traversable shouldn't be done in the gridNode
		// The gridNode would need the course, and having multiple gridNodes share that reference doesn't sound good
		this.traversable = traverse;
		
		// Length should always be positive and this check should be redundant
		// But I don't want to take chances
		this.length = Math.abs(length);
		
		this.hasFlag = this.checkIfLocationIsContained(flagPos);
		
		// God this is is such a shit way of dealing with not being able to have null values
		// But if fucking works with what I need, so screw it
		this.distToBall = Double.MAX_VALUE;
		this.explored = false;
		
	}
	
	boolean hasFlag() {
		return this.hasFlag;
	}
	
	void setFlag(boolean contains) {
		this.hasFlag = contains;
	}
	
	void setBall(boolean contains) {
		this.hasBall = contains;
	}
	
	void setBallDistance(double newDist) {
		if (this.distToBall > newDist) {
			this.distToBall = newDist;
			this.updateTotalDistance();
		}
	}
	
	// THIS IS THE GOOD METHOD FOR SETTING THE BALL DISTANCE
	void setBallDistance(GridNode aNode) {
		double newBallDistance = aNode.getCenterPoint().get_distance(centerPoint);
		
		if(this.distToBall > newBallDistance) {
			this.distToBall = newBallDistance;
			this.updateTotalDistance();

			this.comesFrom = aNode;
			
		}
		
		
	}
	
	double getTotalDistance() {
		return this.univDistScore;
	}
	
	Vector2d getCenterPoint() {
		return this.centerPoint;
	}
	
	void updateTotalDistance() {
		this.univDistScore = this.distToBall + this.distToFlag;
	}
	
	double findDistanceToCenter(Vector2d position) {
		return this.centerPoint.get_distance(position);
	}
	
	void setExplored(boolean exploredState) {
		this.explored = exploredState;
	}
	
	// Only works because the nodes are meant to be squares
	// So it checks if something is in the square's area
	public boolean checkIfLocationIsContained(Vector2d objectLocation) {
		Vector2d temp = this.centerPoint.getVectDist(objectLocation);
		
		if ((Math.abs(temp.get_x()) <=this.length/2.0)
				&& (Math.abs(temp.get_y())<= this.length/2.0)) {
			return true;
		} else {
			return false;
		}
		
	}
	
	
	public String toString() {
		String a = this.centerPoint.toString() + " distFlag " + this.distToFlag + " distBall " + this.distToBall + " totalScore " + this.univDistScore;
		String b = " hasBall " + this.hasBall + " hasFlag " + this.hasFlag();
		
		return (a + b);
	}


}
