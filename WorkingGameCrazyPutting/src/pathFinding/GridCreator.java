package pathFinding;

import java.util.ArrayList;
import java.util.List;

import MainProject.PuttingCourse;
import MainProject.Vector2d;

public class GridCreator {
	GridNode[][] nodeList;
	
	PuttingCourse theCourse;
	
	// The distance between the centerpoint of each node
	double nodeDistance;
	double nodeDistMultiplier;
	
	double gridLengthMultiplier;
	
	Vector2d ballPosition;
	Vector2d flagPosition;
	
	GridNode startNode;
	GridNode endNode;
	
	static int specialNodeCount = 0;
	
	// I'll need an extra constructor in case I'm giving it a separate ball position
	// Blah, to do later
	
	// This is just a really handy way of keeping some information for nodes and grid nodes
	// I can probably change it to instead be like a long ass static method.
	
	// TODO: Modify GridCreator to be the main class that interacts with/explores all of the nodes
	// TODO: Update the piece of shit so that it can create a new map with smaller nodes in case a solution isn't found.
	
	
	// I'll modify things to have a default constructor, because the existence of createGrid makes things a bit more annoying for me
	// Means I'll have to add a boolean check at the end of the super, but it ain't a biggie
	public GridCreator(PuttingCourse aCourse){
		this.theCourse = aCourse;
		this.ballPosition = aCourse.get_start_position();
		this.flagPosition = aCourse.get_flag_position();
		
		this.gridLengthMultiplier = 4.0;
		
		this.setNodeDistance();
		this.createGrid();
		
	}
	
	
	// TODO: Modify the gridCreator to be a much better constructed constructor
	// This will be the default contructor, as take shot gives a ball position always
	
	// TODO: Add a way of generating a new map that'll double the gridLength multiplier and half the nodeSize
	public GridCreator(PuttingCourse aCourse, Vector2d ball_position) {
		this.theCourse = aCourse;
		this.ballPosition = ball_position;
		this.flagPosition = aCourse.get_flag_position();
		
		this.gridLengthMultiplier = 4.0;
		
		this.setNodeDistance();
		this.createGrid();
	}
	
	// This method will recreate the grid but twice as large
	void recreateGrid() {
		this.gridLengthMultiplier = this.gridLengthMultiplier*2;
		System.out.println("We need to go deeper");
		this.createGrid();
	}
	
	
	// There needs to be a way of increasing the grid size in case no path can be found
	// Let's do that and decrease nodeSize at the same time because fuck it
	// Initial gridSize is dist from flag to ball * 2 rounded up
	// Divided by the nodeDistance, rounded.
	void createGrid() {
		
		// TODO: Think of a fucking better way to make the gridLength method nicer
		double totalGridLength = Math.abs(((ballPosition.get_distance(flagPosition)*this.gridLengthMultiplier)));
		
		int nodeGridSize = (int)(totalGridLength/this.nodeDistance);
		this.nodeList = new GridNode[nodeGridSize][nodeGridSize];
		
		double startX = (-totalGridLength/2)+this.ballPosition.get_x();
		double startY = (-totalGridLength/2)+this.ballPosition.get_y();
		
		// With the current values
		// when i close to j, the ball should be around there.
		// Extra check there for that so that I can store where the ball is/the start node
		for(int i=0; i<nodeList.length; i++) {
			for(int j=0;j<nodeList[0].length;j++) {
				
				
				double valX = startX + i*this.nodeDistance;
				double valY = startY + j*this.nodeDistance;
				Vector2d createdPoint = new Vector2d(valX, valY);
				boolean traversable = this.checkIfTraversable(createdPoint);
				
				GridNode newNode = new GridNode(createdPoint, this.nodeDistance, traversable, this.flagPosition);
				
				this.nodeList[i][j] = newNode;
				
				if (newNode.checkIfLocationIsContained(flagPosition)) {
					newNode.setFlag(true);
					newNode.setCenter(flagPosition);
					this.endNode = newNode;
					this.specialNodeCount++;
				}
				
				// Shitty ways of marking start and end node
				// Don't care if they suck, they workkkk
				
				// I can do an extra check, because I know that start node has to be in the exact middle
				// To do if the program sucks dick
				if (newNode.checkIfLocationIsContained(ballPosition)) {
					
					// TODO: Fix this garbage so that the node has the ball in its center or something so that this shit's natural
					newNode.setBall(true);
					this.startNode = newNode;
					double distToBall = newNode.findDistanceToCenter(ballPosition);
					newNode.setBallDistance(distToBall);
					
					//startNodeCount is just there for bug testing purposes
					// It's an easy AF way for me to know if multiple nodes have the ball
					this.specialNodeCount++;
				}
				
				
			}
			
		}
		
		
	}
	
	// Setting initial distance on which is smaller between vMax and distance between ball and flag
	// And just dividing it by 100
	void setNodeDistance() {
		this.nodeDistance = 0.25/this.gridLengthMultiplier;
	}
	
	
		List<GridNode> findSurroundingNodes(GridNode aNode) throws Exception {
		
			List<GridNode> list = new ArrayList<>();
		
		int xPos = 0;
		int yPos = 0;
		
		boolean confirmAllWorks = false;
		
		outer: for(int i=0; i<this.nodeList.length; i++) {
			for(int j=0; j<this.nodeList[0].length;j++) {
				if (this.nodeList[i][j] == aNode) {
					xPos = i;
					yPos = j;
					
					confirmAllWorks = true;
					
					break outer;
					
				}
				
			}
			
		}
		
		// Exists only for bug-testing!
		if(confirmAllWorks == false) {
			throw new Exception();
		}
		
		// The idea is to check the 8 surrounding objects
		for(int i=-1; i<2; i++) {
			for(int j=-1; j<2; j++) {
				try {
					
					
					
					if((i != j) || (i != 0)) {
						
						if(this.nodeList[xPos+i][yPos+j].isTraversable()) {
							list.add(this.nodeList[xPos+i][yPos+j]);
						}
						
					}
					
				} catch(ArrayIndexOutOfBoundsException e) {
					
				}
				
				
			}
			
		}
		
		return list;
		
	}
	
	public GridNode[][] getGrid(){
		return this.nodeList;
	}
	
	public GridNode getEndNode() {
		return this.endNode;
	}
	
	public GridNode getStartNode() {
		return this.startNode;
	}
	
	// Currently only takes water as untraversable
	// Everything else is traversable
	
	// TODO: Update method to check for other types of untraversable
	// TODO: Update method to check various points, not just the center
	// Well, guess that update would go to the method that uses checkIfTraversable, not this method specifically
	boolean checkIfTraversable(Vector2d point) {
		
		// BUGTEST EXTRA METHOD
		/*
		if ((Math.abs(point.get_x()) < 2) && (Math.abs(point.get_y()-0.5) < 0.05)) {
			System.out.println("The bad boi of a point is " + point.toString());
			return false;
		}*/
		
		// Checks at the center point, and at the four corners of each node
		// Assumes that the given point is the center point
		
		double pointDist = this.nodeDistance/2;
		
		// I'm checking five points and storing them in the array
		Vector2d[] points = getCornersAndCenter(point, pointDist);
		
		for(Vector2d aPoint : points) {
			
			// TODO: Update the method to check all kinds of traversable
			// Should also do an extra update to make sure the point doesn't have too high a gradient
			if(this.theCourse.is_water(aPoint)) {
				return false;
			}
			
		}
		
		return true;
		
		
	}
	
	public String toString() {
		int count = 0;
		int countTraverse = 0;
		for(GridNode[] nodeArr : this.nodeList) {
			for(GridNode node : nodeArr) {
				if (node.explored) {
					count++;
				} else if (!node.traversable) {
					countTraverse++;
				}
				
				
				
				
			}
			
			
		}
		
		int totalArea = this.nodeList.length*this.nodeList[0].length;
		
		return ("Explored:" + count + ", untraversable:" + countTraverse + ", Total:" + totalArea);
		
	}
	
	// Assumes the given point is the center
	// Hardcoded and not visually nice, but it's good enough for what's needed
	static Vector2d[] getCornersAndCenter(Vector2d aPoint, double dist) {
		Vector2d[] points = new Vector2d[5];
		
		points[0] = aPoint;
		
		points[1] = new Vector2d(aPoint.get_x()+dist,aPoint.get_y()+dist);
		
		points[2] = new Vector2d(aPoint.get_x()+dist,aPoint.get_y()-dist);
		
		points[3] = new Vector2d(aPoint.get_x()-dist,aPoint.get_y()+dist);
		
		points[4] = new Vector2d(aPoint.get_x()-dist,aPoint.get_y()-dist);
		
		return points;
		
	}
	
}
