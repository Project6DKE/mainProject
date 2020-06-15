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
	// TODO: 
	GridCreator(PuttingCourse aCourse){
		this.theCourse = aCourse;
		this.ballPosition = aCourse.get_start_position();
		this.flagPosition = aCourse.get_flag_position();
		this.setNodeDistance();
		this.createGrid();
		
	}
	
	// There needs to be a way of increasing the grid size in case no path can be found
	// Let's do that and decrease nodeSize at the same time because fuck it
	// Initial gridSize is dist from flag to ball * 2 rounded up
	// Divided by the nodeDistance, rounded.
	void createGrid() {
		
		// TODO: Think of a fucking better way to make the gridLength method nicer
		double totalGridLength = Math.abs(((ballPosition.get_distance(flagPosition)*2.5)));
		
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
		
		/*
		double ballFlagDist = ballPosition.get_distance(flagPosition);
		double vMax = this.theCourse.get_maximum_velocity();
		
		double nodeSize;
		
		if (ballFlagDist > vMax) {
			nodeSize = vMax;
		} else {
			nodeSize = ballFlagDist;
		}
		*/
		
		// This is me modifying nodeSize to make it easier for me to think about stuff
		// I should definitely refine it, but fuck it works for me
		
		
		
		this.nodeDistance = 0.1;
		//= Math.abs(nodeSize)/100.0;
		
		
	}
	
	List<GridNode> findSurroundingNodes(GridNode aNode) throws Exception{
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
		
		// The idea is to check the 9 surrounding objects
		// TODO: Bugtest this for loop
		for(int i=-1; i<2; i++) {
			for(int j=-1; j<2; j++) {
				try {
					// A check to not return the same value again
					if((i != 0) && (j != 0)) {
						list.add(this.nodeList[xPos+i][yPos+j]);
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
		if ((Math.abs(point.get_x()) < 2) && (Math.abs(point.get_y()-0.5) < 0.05)) {
			System.out.println("The bad boi of a point is " + point.toString());
			return false;
		}
		
		
		
		if(this.theCourse.is_water(point)) {
			return false;
		}else {
			return true;
		}
		
		
	}
	
}
