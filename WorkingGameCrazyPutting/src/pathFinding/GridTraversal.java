package pathFinding;

import java.util.ArrayList;
import java.util.List;

import MainProject.PuttingCourse;
import MainProject.Vector2d;

public class GridTraversal {
	/*
	 * I'm going to eventually need a way of checking whether the grid needs to be created again
	 * Or if the grid needs to be updated in some manner
	 * Especially if the ball moves around
	 * Or if no solution is found
	 * TODO LATER
	 */
	
	GridNode startNode;
	GridNode endNode;
	GridNode[][] map;
	List<GridNode> toExploreList = new ArrayList<>();
	GridCreator gridInfo;
	boolean foundSolution;
	List<GridNode> solution;
	
	// Having the map elsewhere might be useless
	
	// TODO: Update the method so that it can receive a different ball location, in case the traversal needs to be updated
	GridTraversal(GridCreator gridCreator) throws Exception{
		this.startNode = gridCreator.startNode;
		this.endNode = gridCreator.endNode;
		this.map = gridCreator.nodeList;
		this.gridInfo = gridCreator;
		this.foundSolution = false;
		this.createPath();
	}
	
	public GridTraversal(PuttingCourse aCourse) throws Exception{
		this(new GridCreator(aCourse));
	}
	
	public GridTraversal(PuttingCourse aCourse, Vector2d ball_position) throws Exception {
		this(new GridCreator(aCourse, ball_position));
	}
	
	void createPath() throws Exception {
		
		GridNode exploreNode = this.startNode;
		
		this.updateNearbyNodes(exploreNode);
		
		while(!this.foundSolution) {
			exploreNode = findBestNode(this.toExploreList);
			this.toExploreList.remove(exploreNode);
			
			exploreNode.setExplored(true);
			this.updateNearbyNodes(exploreNode);
			
			if(this.toExploreList.size() == 0) {
				this.restartInternalInfo();
				break;
			}
			
		}
		
		if(!this.foundSolution) {
			this.createPath();
		}
		
	}
	
	public void createNewPath() throws Exception {
		this.restartInternalInfo();
		this.createPath();
		
	}
	
	// Restarts the information it has, based off the current gridInfo
	void restartInternalInfo() {
		this.gridInfo.recreateGrid();
		this.startNode = this.gridInfo.startNode;
		this.endNode = this.gridInfo.endNode;
		this.map = this.gridInfo.nodeList;
		this.foundSolution = false;
		
	}
	
	// Extra method
	// It's probably too freaking long, so I'll try to make it nicer somehow
	void cleanToExploreList() {
		for(GridNode aNode : this.toExploreList) {
			if(aNode.explored) {
				this.toExploreList.remove(aNode);
			}
			
		}
		
	}
	
	
	// Given node is the node I'm exploring
	void updateNearbyNodes(GridNode givenNode) throws Exception {
		
		givenNode.setExplored(true);
		
		List<GridNode> toUpdate = this.gridInfo.findSurroundingNodes(givenNode);
		
		for(GridNode aNode : toUpdate) {
			
			if(aNode.hasFlag) {
				
				aNode.setBallDistance(givenNode);
				// Here I need to do something to stop everything, cause I should've gotten the result node
				// TODO: Update the method to update the solution in some manner
				
				this.foundSolution = true;
				
				System.out.println("I found the flag fucko");
				
				
				
			}
			
			if((aNode.traversable) && (!aNode.explored)) {
				aNode.setBallDistance(givenNode);
				
				toExploreList.add(aNode);
			}
			
		}
		
		
		
		
		
	}
	
	public List<GridNode> processedSolution() {
		
		
		List<GridNode> list = new ArrayList<>();
		
		list.add(endNode);
		
		GridNode curNode = this.endNode;
		
		while(curNode != this.startNode) {
			curNode = curNode.comesFrom;
			list.add(curNode);
		}
		
		this.solution = list;
		
		return list;
		
	}
	
	// Finds the node with the lowest traverse value
	// List has to be non-empty
	static GridNode findBestNode(List<GridNode> aList) {
		
		// Just initializing to a really high value blah
		double exploreValue = Double.MAX_VALUE;
		GridNode bestNode = null;
		
		for(GridNode aNode : aList) {
			if(aNode.getTotalDistance() < exploreValue) {
				exploreValue = aNode.getTotalDistance();
				bestNode = aNode;
				
				
			}
			
			
		}
		
		if(bestNode == null) {
			System.out.println("FUCK UP THIS LIST WAS EMPTY");
		}
		
		return bestNode;
		
	}
	
	
	
}