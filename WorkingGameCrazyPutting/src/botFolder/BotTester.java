package botFolder;

import MainProject.PuttingCourse;
import MainProject.PuttingSimulator;
import MainProject.RungeKutta;
import MainProject.Vector2d;
import readingOfFunctions.Function2d;
import readingOfFunctions.FunctionH;

public class BotTester {
	
	public static void main(String[] args) throws Exception {
		
		// Three nice functions for testing
		Function2d height= new FunctionH("2");
		//Function2d height = new FunctionH("-0.01 * x + 0.003 * x ^ 2 + 0.04 * y");
		//Function2d height = new FunctionH("( 7 * x * y ) / ( exp ( x ^ 2 + y ^ 2 ) )");
		
		Vector2d flag = new Vector2d(4,0);
		Vector2d start = new Vector2d(0,4);
		
		double g,m,mu,vmax,tol;
		g=9.81;m=45.93/1000;mu=0.131;vmax=10000;tol=0.03;
		
		PuttingCourse course = new PuttingCourse(height,flag, start, mu, vmax,tol,g,m );
		
		RungeKutta engine = new RungeKutta();
		
		PuttingSimulator s= new PuttingSimulator(course,engine);
		
		//BasicAI ai = new BasicAI(s);
		//NewAI ai = new NewAI(s);
		MergeAI ai = new MergeAI();
		
		Vector2d stopGap = new Vector2d(2,2);
		
		Vector2d aimedShot = ai.findAimedShot(course, s.get_ball_position(), stopGap);
		//Vector2d aimedShot = ai.aimedShot(course, s.get_ball_position(), stopGap);
		
		//Vector2d shot = ai.shot_velocity(course, s.get_ball_position());
		
		s.take_shot(aimedShot);
		
		System.out.println(s.get_ball_position());
		
		Vector2d fShot = ai.findAimedShot(course, s.get_ball_position(), flag);
		
		s.take_shot(fShot);
		
		System.out.println(s.get_ball_position());
		
	}
	
}
