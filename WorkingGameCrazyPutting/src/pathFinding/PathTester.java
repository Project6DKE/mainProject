package pathFinding;

import java.util.Arrays;
import java.util.List;

import MainProject.PuttingCourse;
import MainProject.PuttingSimulator;
import MainProject.RungeKutta;
import MainProject.Vector2d;
import botFolder.MergeAI;
import botFolder.TraversalBot;
import readingOfFunctions.Function2d;
import readingOfFunctions.FunctionH;

public class PathTester {
	
	public static void main(String[] args) throws Exception {
		
		// Three nice functions for testing
		//Function2d height = new FunctionH("2");
		//Function2d height= new FunctionH("5 * sin ( x / 2 ) * cos ( y / 16 ) + 6");
		//Function2d height = new FunctionH("-0.01 * x + 0.003 * x ^ 2 + 0.04 * y");
		//Function2d height = new FunctionH("( ( 3 * x * y ) / ( exp ( x ^ 2 + y ^ 2 ) ) ) + 0.1");
		//Function2d height = new FunctionH("( 1 / ( ( x ^ 2 ) + 1 ) ) + ( 1 / ( ( y ^ 2 ) + 1 ) ) + 1");
		
		//height.add_subfunct("sqrt ( ( x ^ 2 ) + ( y ^ 2 ) ) + 5", -4, 4, -4, 4);
		
		//Vector2d flag = new Vector2d(-5,-5);
		//Vector2d start = new Vector2d(5,5);
		
		Vector2d start = new Vector2d(10.5,1);
		Vector2d flag = new Vector2d(13,4);
		
		Function2d height = FunctionH.createInstance(4);
		
		
		
		
		
		double g,m,mu,vmax,tol;
		g=9.81;m=45.93/1000;mu=0.131;vmax=10000;tol=0.04;
		
		PuttingCourse course = new PuttingCourse(height,flag, start, mu, vmax,tol,g,m );
		
		RungeKutta engine = new RungeKutta();
		
		PuttingSimulator s= new PuttingSimulator(course,engine);
		
		
		/*GridCreator grid = new GridCreator(course);
		
		GridNode[][] values = grid.getGrid();
		
		
		
		
		
		System.out.println("I have this number of special spots " + grid.specialNodeCount);
		
		System.out.println("My start node is " + grid.getStartNode());
		
		System.out.println("My end node is " + grid.getEndNode());
		
		System.out.println(values.length*values[0].length);
		*/
		
		
		
		
		
		
		GridTraversal travel = new GridTraversal(course, false);
		
		List<GridNode> sol = travel.processedSolution();
		
		
		
		System.out.println("This is the solution fucko");
		
		for(GridNode aNode : sol) {
			System.out.println(aNode.toString());
		}
		
		
		
		
		/*
		TraversalBot ai = new TraversalBot(50);
		//MergeAI ai = new MergeAI();
		
		ai.shot_velocity(course, start);
		
		System.out.println("Now comes the ball path");
		
		List<Vector2d> ballSeq = ai.getBallPath();
		
		for(Vector2d aVec : ballSeq) {
			System.out.println(aVec.toString());
		}
		
		System.out.println("now comes the shot list");
		
		List<Vector2d> shotSeq = ai.getShotSequence();
		
		for(Vector2d aVec : shotSeq) {
			System.out.println(aVec.toString());
		}
		/*
		System.out.println("let's play a bit");
		
		for(Vector2d aVec: shotSeq) {
			s.take_shot(aVec);
			System.out.println(s.get_ball_position());
		}
		
		System.out.println("that was the game-ish");
		
		Vector2d lastShot =ai.aimedShot(course, s.get_ball_position(), flag);
		
		s.take_shot(lastShot);
		
		System.out.println(s.get_ball_position());
		*/
	}
	
}
