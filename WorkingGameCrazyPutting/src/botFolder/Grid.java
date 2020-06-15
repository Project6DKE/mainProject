package botFolder;

import java.util.ArrayList;
import MainProject.PuttingCourse;
import MainProject.Vector2d;

public class Grid {
	private int width, height;
    private PuttingCourse PC;

    private static Vector2d[] dirs = {new Vector2d(-1,1),new Vector2d(0,1), new Vector2d(1,1), new Vector2d(-1,0),
                                   new Vector2d(1,0), new Vector2d(-1,-1), new Vector2d(0,-1), new Vector2d(1,-1)};

    public Grid(PuttingCourse PC){
        this.width = 10;
        this.height = 10;
        this.PC = PC;
        
        }
    
    private boolean inBounds(Vector2d id){
        return 0 <= id.get_x() && id.get_x() <= width && 0 <= id.get_y() && id.get_y() <= height;
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
        	if(PC.collisionDetector(neighbours.get(i).getLocation())==0) {
        		neighbours.remove(i);
        	}
        }
        return neighbours;
    }
}
