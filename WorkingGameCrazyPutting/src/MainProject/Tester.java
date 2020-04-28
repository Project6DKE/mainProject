package MainProject;

import java.io.IOException;

import botFolder.BasicAI;
import botFolder.NewAI;
import readingOfFunctions.Function2d;
import readingOfFunctions.FunctionH;

public class Tester {
	public static void main(String[] args) throws IOException {
		
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
		
		Function2d height= new FunctionH("x / 4");
		
		Vector2d flag = new Vector2d(2,2);
		Vector2d start = new Vector2d(1,1);
		
		double g,m,mu,vmax,tol;
		g=9.81;m=45.93/1000;mu=0.131;vmax=4;tol=0.02;
		
		PuttingCourse course = new PuttingCourse(height,flag, start, mu, vmax,tol,g,m );
		
		EulerSolver engine= new EulerSolver();
		
		PuttingSimulator s= new PuttingSimulator(course,engine);
		
		//BasicAI ai = new BasicAI(s);
		NewAI ai = new NewAI(s);
		
		while(!s.course_put) {
			ai.takeShot();
			System.out.println(s.get_ball_position().toString());
			
		}
		
		//Vector2d initial_ball_velocity= new Vector2d(2,2);
		
		//s.take_shot(initial_ball_velocity);
		
		System.out.println(s.get_ball_position().toString());
		System.out.println("game is over");
		
		//course.output_to_file("C:\\Users\\husam\\git\\mainProject\\course");
	}
}
