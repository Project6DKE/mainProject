package pathFinding;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

import MainProject.PuttingCourse;
import MainProject.PuttingSimulator;
import MainProject.RungeKutta;
import MainProject.Vector2d;
import botFolder.BasicAI;
import botFolder.NewAI;
import botFolder.PuttingBot;
import botFolder.RandomAI;
import botFolder.TraversalBot;
import readingOfFunctions.Function2d;
import readingOfFunctions.FunctionH;
import readingOfFunctions.LessRandomFunctions;

public class FinalTesting {
	
	static int numberOfTests = 10;
	
	public static void main(String[] args) throws Exception {
		
		try(PrintWriter writer = new PrintWriter(new File("botCompetition2.csv"))){
			
			StringBuilder sb = new StringBuilder();
			
			sb.append("CourseInfo");
			sb.append(',');
			sb.append("BotType");
			sb.append(',');
			sb.append("ShotsTaken");
			sb.append(',');
			sb.append("TimetoSolve");
			sb.append('\n');
			
			writer.write(sb.toString());
			
			Vector2d[] starts = {new Vector2d(0,0), new Vector2d(0,0), new Vector2d(0,0), 
					new Vector2d(0,0), new Vector2d(0,0), new Vector2d(0,0), new Vector2d(0,0), new Vector2d(10.5,1),
					new Vector2d(10.5,1), new Vector2d(10.5,1), new Vector2d(10.5,1), new Vector2d(10.5,1), new Vector2d(10.5,1)};
			Vector2d[] flags = {new Vector2d(0,4), new Vector2d(0,4), new Vector2d(0,4), new Vector2d(4,4),
					new Vector2d(4,8), new Vector2d(0,8), new Vector2d(0,12), new Vector2d(4,10),
					new Vector2d(5,16), new Vector2d(10,20), new Vector2d(17,17), new Vector2d(10,7),
					new Vector2d(13,5)};
			int[] courses = {1,2,3,3,3,3,3,4,4,4,4,4,4};
			
			for(int i=7; i<flags.length;i++) {
				
				int val = courses[i];
				Function2d height = FunctionH.createInstance(val);
				
				// We take the values given in the project manual as the default due to lazyness
				// Only one that's modified is vmax, done so that the focus is on the bot's accuracy nothing else
				double g,m,mu,vmax,tol;
				g=9.81;m=45.93/1000;mu=0.131;vmax=10000;tol=0.02;
				
				Vector2d flag = flags[i];
				Vector2d start = starts[i];
				
				PuttingCourse course = new PuttingCourse(height,flag, start, mu, vmax,tol,g,m );
				
				
				
				// I gotta time these babes as soon as I create em
				
				
				long start1 = System.nanoTime();
				TraversalBot ai1 = new TraversalBot(true);
				ai1.shot_velocity(course, start);
				long end1 = System.nanoTime();
				
				
				
				long start2 = System.nanoTime();
				TraversalBot ai2 = new TraversalBot(false);
				ai2.shot_velocity(course, start);
				long end2 = System.nanoTime();
				
				
				long delt1 = end1-start1;
				
				long delt2 = end2-start2;
				
				String res1 = val + "," + ai1.toString() + ", " + delt1 + "\n";
				String res2 = val + "," + ai2.toString() + "," + delt2 + "\n";
				
				/*sb.append(res1);
				sb.append(res2);*/
				
				writer.write(res1);
				writer.write(res2);	
				
				writer.flush();
				
				System.out.println("flushed");
			}
			
			
			
			//writer.write(sb.toString());
			System.out.println("done!");
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
}
