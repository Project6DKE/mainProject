package MainProject;

import readingOfFunctions.*;
import botFolder.*;

public class Tester {
	public static void main(String[] args) throws Exception {
		
		//String path="C:\\Users\\husam\\git\\mainProject\\Speed_velocity3.txt";
		
		//FunctionReader xh= new FunctionReader(path);
		
		//PuttingCourse course1 = xh.get_Course();
		//System.out.println("dd"+course1.get_height().toString());
		
//		String path="C:\\Users\\husam\\git\\mainProject\\course1.txt";
//		
//		CourseFileReader xh= new CourseFileReader(path);
//		
//		PuttingCourse course1 = xh.readFile();
//		System.out.println(course1.get_height().toString());
		
		Function2d height= new FunctionH("2");
		//Function2d height = new FunctionH("-0.01 * x + 0.003 * x ^ 2 + 0.04 * y");
//		Function2d height = new FunctionH("( 7 * x * y ) / ( exp ( x ^ 2 + y ^ 2 ) )");
		
		Vector2d flag = new Vector2d(3,0);
		Vector2d start = new Vector2d(0,2);
		
		double g,m,mu,vmax,tol;
		g=9.81;m=45.93/1000;mu=0.131;vmax=10000;tol=0.03;
		
		PuttingCourse course = new PuttingCourse(height,flag, start, mu, vmax,tol,g,m );
		
		RungeKutta engine = new RungeKutta();
		
		PuttingSimulator s= new PuttingSimulator(course,engine);
		
		//BasicAI ai = new BasicAI(s);
		NewAI ai = new NewAI(s);
		//BackwardsAI ai = new BackwardsAI(s);
		
//		while(!s.course_put) {
//			Vector2d speed = ai.shot_velocity(course, s.get_ball_position());
//			s.take_shot(speed);
//			System.out.println(s.get_ball_position().toString());
//			System.out.println("The distance to flag is " + s.distToFlag());
//			
//		}
		
		Vector2d initial_ball_velocity= new Vector2d(0,2);
		
//		s.take_shot(0,2);
		s.take_angle_shot(1,90);
		
//		
		System.out.println(s.get_ball_position().toString());
//		System.out.println("game is over");
//		
		//course.output_to_file("C:\\Users\\husam\\git\\mainProject\\course");
	}
}
