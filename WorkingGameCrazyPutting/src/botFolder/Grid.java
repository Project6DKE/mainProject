package botFolder;

import java.awt.Point;
import javafx.geometry.Point2D;
import java.util.HashSet;
import java.util.ArrayList;
import javafx.scene.shape.Rectangle;
import java.util.List;

import MainProject.PuttingCourse;

import java.awt.geom.Line2D;


public class Grid {
	private int width, height;
    private HashSet<Point> obstacles = new HashSet<>();
    private PuttingCourse PC;

    private static Point[] dirs = {new Point(-1,1),new Point(0,1), new Point(1,1), new Point(-1,0),
                                   new Point(1,0), new Point(-1,-1), new Point(0,-1), new Point(1,-1)};

    public Grid(PuttingCourse PC){
        this.width = 10;
        this.height = 10;
        this.PC = PC;
        
        }
    
    //name explains
    private boolean inBounds(Point id){
        return 0 <= id.x && id.x <= width && 0 <= id.y && id.y <= height;
    }

    private boolean passable(Point id){
        for(int i = id.x-10; i < id.x+10; i++){
            for(int j = id.y-10; j < id.y+10;j++){
                if(obstacles.contains(new Point(i,j))){return false;}

                if(!inBounds(new Point(i,j))){
                    return false;
                }

            }
        }
        return true;
    }
    
    //name explains
    public double cost(Point a, Point b){return 1;}

    //name explains
    public ArrayList<Point> neighbours(Point id){
        ArrayList<Point> neighbours = new ArrayList<>();

        for(Point p : dirs){
            Point neighbour = new Point(id.x + p.x, id.y +p.y);
            if(passable(neighbour)){
                neighbours.add(neighbour);
            }
        }
        return neighbours;
    }
}
