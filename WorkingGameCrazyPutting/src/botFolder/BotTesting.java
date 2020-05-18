package botFolder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

import MainProject.PuttingCourse;
import MainProject.PuttingSimulator;
import MainProject.RungeKutta;
import MainProject.Vector2d;
import readingOfFunctions.Function2d;
import readingOfFunctions.FunctionH;
import readingOfFunctions.LessRandomFunctions;
import readingOfFunctions.RandomishFunction;

public class BotTesting {
	
	static int numberOfTests = 1000;
	
	public static void main(String[] args) {
		
		try(PrintWriter writer = new PrintWriter(new File("botresults.csv"))){
			
			StringBuilder sb = new StringBuilder();
			
			sb.append("Function");
			sb.append(',');
			sb.append("InitDistanceToFlag");
			sb.append(',');
			sb.append("BotType");
			sb.append(',');
			sb.append("ShotsTaken");
			sb.append(',');
			sb.append("Put");
			sb.append(',');
			sb.append("DistanceAfterFirstShot");
			sb.append(',');
			sb.append("FinalDistance");
			sb.append(',');
			sb.append("CalculationTimeAveragePerShot");
			sb.append('\n');
			
			writer.write(sb.toString());
			
			for(int i=0; i<numberOfTests;i++) {
				
				boolean hasFunc = false;
				
				Function2d height = null;
				
				while(!hasFunc) {
					
					// There needs to be some testing to make sure the generated function is valid
					// A way that's basic enough to deal with it in a try catch block
					
					
					try {
						
						int size = (int)((Math.random()*5)+2);
						height = new LessRandomFunctions(size);
						//height = new FunctionH("2");
						hasFunc = true;
					} catch (Exception e) {
						hasFunc = false;
					}
				}
				
				// We take the values given in the project manual as the default due to lazyness
				// Only one that's modified is vmax, done so that the focus is on the bot's accuracy nothing else
				double g,m,mu,vmax,tol;
				g=9.81;m=45.93/1000;mu=0.131;vmax=10000;tol=0.02;
				
				Vector2d flag = new Vector2d(randomPosNeg(10),randomPosNeg(10));
				Vector2d start = new Vector2d(randomPosNeg(10),randomPosNeg(10));
				
				PuttingCourse course = new PuttingCourse(height,flag, start, mu, vmax,tol,g,m );
				
				/*
				 * Add the AI to an arrayList here
				 */
				
				
				
				ArrayList<PuttingBot> aiList = new ArrayList<PuttingBot>();
				
				aiList.add(new BasicAI());
				aiList.add(new NewAI());
				//aiList.add(new GA());
				aiList.add(new RandomAI());
				
				PuttingSimulator s = new PuttingSimulator(course, new RungeKutta());
				double distToFlag = s.distToFlag();
				
				for(PuttingBot bot: aiList) {
					
					
					int shotsTaken = 0;
					double distAfter1Shot = 0;
					double finalDist = 0;
					double avgTime = 0;
					boolean put = false;
					
					
					// Just taking 10 shots as an abritrary value
					for(int j=0; j<10;j++) {
						
						long startTime =System.nanoTime();
						Vector2d shot = bot.shot_velocity(course, s.get_ball_position());
						long endTime = System.nanoTime();
						
						s.take_shot(shot);
						
						if(j==0) {
							distAfter1Shot = s.distToFlag();
						}
						
						shotsTaken = j+1;
						double runTime = endTime-startTime;
						avgTime = avgTime + runTime;
					
						
						if(s.get_put_state()) {
							put = true;
							break;
						}
						
					}
					
					finalDist =s.distToFlag();
					avgTime = avgTime/shotsTaken;
					
					String results = height.toString() + "," + distToFlag + "," 
							+bot.toString() + "," + shotsTaken + "," + put + "," + distAfter1Shot 
							+ "," + finalDist + "," + avgTime + '\n';
					
					sb.append(results);
					//sb.append('\n');
					
					s.restart_simulation();
					
					writer.write(results);
					
				}
				
			}
			
			
			
			writer.write(sb.toString());
			System.out.println("done!");
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	// Randomizes the value between a bound
	// The abs(resulting value)<abs(bound)
	static double randomPosNeg(int bound) {
		double num = (Math.random()*bound);

		if(takeChance(50)) {
			return num;
		} else {
			return -num;
		}
	}
	
	static boolean takeChance(double chance) {
		double num = Math.random()*100.0;
		
		if(num<chance) {
			return true;
		} else {
			return false;
		}
		
	}
	
}
