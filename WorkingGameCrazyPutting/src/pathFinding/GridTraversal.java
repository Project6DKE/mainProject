package pathFinding;

import java.util.ArrayList;
import java.util.HashSet;
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
	//List<GridNode> toExploreList = new ArrayList<>();
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
		
		//List<GridNode> firstBatch = this.updateNearbyNodes(exploreNode);
		
		HashSet<GridNode> toExplore = new HashSet<>();
		
		toExplore.addAll(this.updateNearbyNodes(exploreNode));
		
		//List<GridNode> toExplore = new ArrayList<GridNode>();
		
		while(!this.foundSolution) {
			exploreNode = findBestNode(toExplore);
			
			toExplore.remove(exploreNode);
			
			exploreNode.setExplored(true);
			
			//exploreNode.setExplored(true);
			
			List<GridNode> surroundingNodes = this.updateNearbyNodes(exploreNode);
			
			toExplore.addAll(surroundingNodes);
			
			if(toExplore.size() == 0) {
				this.restartInternalInfo();
				exploreNode = this.startNode;
				toExplore.addAll(this.updateNearbyNodes(exploreNode));
				
			}
			
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
	/*void cleanToExploreList() {
		for(GridNode aNode : this.toExploreList) {
			if(aNode.explored) {
				this.toExploreList.remove(aNode);
			}
			
		}
		
	}*/
	
	
	// Given node is the node I'm exploring
	
	// TODO: Bug test the updates for stoppable vs traversable
	
	List<GridNode> updateNearbyNodes(GridNode givenNode) throws Exception {
		
		//givenNode.setExplored(true);
		
		List<GridNode> toUpdate = this.gridInfo.findSurroundingNodes(givenNode);
		
		List<GridNode> updated = new ArrayList<GridNode>();
		
		for(GridNode aNode : toUpdate) {
			
			if(aNode.hasFlag) {
				
				aNode.setBallDistance(givenNode);
				// Here I need to do something to stop everything, cause I should've gotten the result node
				// TODO: Update the method to update the solution in some manner
				
				this.foundSolution = true;
				
				updated.add(aNode);
				
				System.out.println("I found the flag fucko");
				break;
				
				
			}
			
			if((aNode.isTraversable()) && (!aNode.explored)) {
				aNode.setBallDistance(givenNode);
				
				updated.add(aNode);
			}
			
		}
		
		
		return updated;
		
		
	}
	
	/*GridNode getBestNode() {
		int place = 0;
		double exploreValue = Double.MAX_VALUE;
		GridNode bestNode = null;
		
		for(int i=0; i<this.toExploreList.size(); i++) {
			GridNode aNode = this.toExploreList.get(i);
			
			if(aNode.getTotalDistance() < exploreValue) {
				place = i;
				exploreValue = aNode.getTotalDistance();
				bestNode = aNode;
				
			}
			
		}
		
		bestNode.setExplored(true);
		this.toExploreList.remove(place);
		
		return bestNode;
		
	}*/
	
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
	static GridNode findBestNode(Iterable<GridNode> aList) {
		
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