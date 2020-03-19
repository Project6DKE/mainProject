package Physics;

import java.io.IOException;

public class Tester {
	public static void main(String[] args) throws IOException {
		
		String path="C:\\Users\\husam\\git\\mainProject\\Speed_velocity3.txt";
		
		FunctionReader xh= new FunctionReader(path);
		
		PuttingCourse course1 = xh.get_Course();
		System.out.println(course1.get_height().toString());
		
//		String path="C:\\Users\\husam\\git\\mainProject\\course1.txt";
//		
//		CourseFileReader xh= new CourseFileReader(path);
//		
//		PuttingCourse course1 = xh.readFile();
//		System.out.println(course1.get_height().toString());
		
		Function2d height= new FunctionH("-0.01 * x + 0.003 * x ^ 2 + 0.04 * y");
		
		Vector2d flag = new Vector2d(0,3);
		Vector2d start = new Vector2d(0,0);
		
		double g,m,mu,vmax,tol;
		g=9.81;m=45.93/1000;mu=0.131;vmax=3;tol=0.02;
		
		PuttingCourse course = new PuttingCourse(height,flag, start, mu, vmax,tol,g,m );
		
		EulerSolver engine= new EulerSolver();
		
		PuttingSimulator s= new PuttingSimulator(course,engine);
		
		Vector2d initial_ball_velocity= new Vector2d(2,2);
		
		s.take_shot(initial_ball_velocity);
		
		System.out.println(s.get_ball_position().toString());
		
		course.output_to_file("C:\\Users\\husam\\git\\mainProject\\course");
	}
}
