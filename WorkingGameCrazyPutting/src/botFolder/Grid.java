package botFolder;

import java.awt.Point;
import java.util.HashSet;
import java.util.ArrayList;
import javafx.scene.shape.Rectangle;
import java.util.List;

import MainProject.PuttingCourse;
import MainProject.Vector2d;

import java.awt.geom.Line2D;


public class Grid {
	private int width, height;
    private HashSet<Vector2d> obstacles = new HashSet<>();
    private PuttingCourse PC;

    private static Vector2d[] dirs = {new Vector2d(-1,1),new Vector2d(0,1), new Vector2d(1,1), new Vector2d(-1,0),
                                   new Vector2d(1,0), new Vector2d(-1,-1), new Vector2d(0,-1), new Vector2d(1,-1)};

    public Grid(PuttingCourse PC){
        this.width = 10;
        this.height = 10;
        this.PC = PC;
        
        }
    
    //name explains
    private boolean inBounds(Vector2d id){
        return 0 <= id.get_x() && id.get_x() <= width && 0 <= id.get_y() && id.get_y() <= height;
    }

    private boolean passable(Vector2d neighbour){
        for(double i = neighbour.get_x()-10; i < neighbour.get_x()+10; i++){
            for(double j = neighbour.get_y()-10; j < neighbour.get_y()+10;j++){
                if(obstacles.contains(new Vector2d(i,j))){return false;}

                if(!inBounds(new Vector2d(i,j))){
                    return false;
                }
            }
        }
        return true;
    }
    
    //name explains
    public double cost(Vector2d a, Vector2d b){return 1;}

    //name explains
    public ArrayList<Vector2d> neighbours(Vector2d id){
        ArrayList<Vector2d> neighbours = new ArrayList<>();

        for(Vector2d p : dirs){
            Vector2d neighbour = new Vector2d(id.get_x() + p.get_x(), id.get_y() +p.get_y());
            if(passable(neighbour)){
                neighbours.add(neighbour);
            }
        }
        return neighbours;
    }
}
