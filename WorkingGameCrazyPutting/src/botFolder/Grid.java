package botFolder;

import java.util.ArrayList;
import MainProject.PuttingCourse;
import MainProject.Vector2d;

public class Grid {
	private static final int HEIGHT;
	private static final int WIDTH = HEIGHT = 10;
	private static ArrayList<Node> nodes = new ArrayList<>();
    private PuttingCourse PC;

    private static Vector2d[] dirs = {new Vector2d(-1,1),new Vector2d(0,1), new Vector2d(1,1), new Vector2d(-1,0),
                                   new Vector2d(1,0), new Vector2d(-1,-1), new Vector2d(0,-1), new Vector2d(1,-1)};

    public Grid(PuttingCourse PC){
        this.PC = PC;   
    }
    
    private boolean inBounds(Vector2d id){
        return 0 <= id.get_x() && id.get_x() <= WIDTH && 0 <= id.get_y() && id.get_y() <= HEIGHT;
    }
    
    private boolean passable(Node neighbour){
    	if(PC.collisionDetector(neighbour.getLocation()) == 0) 
    		return false;
    	
    	else 
    		return true;
    }
    

    public ArrayList<Node> neighbours(Node node){
        ArrayList<Node> neighbours = node.getNeighbours();
        for(int i = 0; i < neighbours.size(); i++) {
        	if(passable(neighbours.get(i))) {
        		neighbours.remove(i);
        	}
        }
        return neighbours;
    }
    
    public static void createGraph() {
		 for(int i = 0; i < HEIGHT; i++) {
			 for(int j = 0; j < WIDTH; j++) {
			 nodes.add(new Node(new Vector2d(i,j), null));
			 }
		 }
		 for(int i = 0; i < nodes.size(); i++) {
			 if(i%(HEIGHT-1)!=0 || i==0) {
				 //nodes.get(i).connectTo(nodes.get(i+1));
				 //nodes.get(i+1).connectTo(nodes.get(i));
				 Node.addNeighbour(nodes.get(i), nodes.get(i+1));
				 //Node.addNeighbour(nodes.get(i+1), nodes.get(i));
			 }
			 if(i+HEIGHT<nodes.size()) {
				 //nodes.get(i).connectTo(nodes.get(i+HEIGHT));
				 //nodes.get(i+HEIGHT).connectTo(nodes.get(i));
				 Node.addNeighbour(nodes.get(i), nodes.get(i+HEIGHT));
				 //Node.addNeighbour(nodes.get(i+HEIGHT), nodes.get(i));
			 }
		 }
	 }
    
	 public static void main(String[] args){
		 createGraph();
	     GraphBot pathFinder = new GraphBot(nodes.get(0), nodes.get(18));
	     pathFinder.reconstructPathToArray(pathFinder.runShortestPath());

	 }
	 
}
