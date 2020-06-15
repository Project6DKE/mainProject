package pathFinding;

import java.util.ArrayList;
import java.util.List;

import MainProject.PuttingCourse;

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
	
	// Having the map elsewhere might be useless
	
	// TODO: Update GridTraversal to only focus on the abstract exploration, let GridCreator take care of the raw implementation.
	GridTraversal(GridCreator gridCreator) throws Exception{
		this.startNode = gridCreator.startNode;
		this.endNode = gridCreator.endNode;
		this.map = gridCreator.nodeList;
		this.gridInfo = gridCreator;
		this.foundSolution = false;
		this.createPath();
		
	}
	
	GridTraversal(PuttingCourse aCourse) throws Exception{
		this(new GridCreator(aCourse));
		
	}
	
	void createPath() throws Exception {
		
		GridNode exploreNode = this.startNode;
		
		this.updateNearbyNodes(exploreNode);
		
		while(!this.foundSolution) {
			exploreNode = findBestNode(this.toExploreList);
			this.toExploreList.remove(exploreNode);
			
			exploreNode.setExplored(true);
			this.updateNearbyNodes(exploreNode);
			
		}
		
		
		
		
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
	
	List<GridNode> processedSolution() {
		List<GridNode> list = new ArrayList<>();
		
		list.add(endNode);
		
		GridNode curNode = this.endNode;
		
		while(curNode != this.startNode) {
			curNode = curNode.comesFrom;
			list.add(curNode);
		}
		
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