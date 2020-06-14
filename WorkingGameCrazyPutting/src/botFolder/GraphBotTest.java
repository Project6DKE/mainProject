package botFolder;

import java.awt.Point;
import MainProject.PuttingCourse;
import MainProject.Vector2d;
import readingOfFunctions.Function2d;
import readingOfFunctions.FunctionH;


public class GraphBotTest{
	//this is for testing
	public static void main(String[] args) throws Exception {
		Point startPoint = new Point(0,0);
		Point hole = new Point(1,1);
		
		Function2d height= new FunctionH("0");
		Vector2d flag = new Vector2d(0,3);
		Vector2d startVector = new Vector2d(0,0);
		double g,m,mu,vmax,tol;
		g=9.81;m=45.93/1000;mu=0.131;vmax=3;tol=0.2;
		PuttingCourse course = new PuttingCourse(height,flag, startVector, mu, vmax,tol,g,m );
		
		Grid grid = new Grid(course);
		
		GraphBot.search(grid, startPoint, hole);
	}
}