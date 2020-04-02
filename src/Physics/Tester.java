package Physics;

import java.io.IOException;

public class Tester {
	public static void main(String[] args) throws IOException {
<<<<<<< HEAD
		long startT = System.currentTimeMillis();
		String path="C:\\Users\\husam\\git\\mainProject\\Speed_velocity3.txt";
		
=======
		/*
		String path="Course1.txt";
		// "C:\\Users\\husam\\git\\mainProject\\TestCourse.txt"

>>>>>>> da9e7f9c1024b79de99eedbcabc3b459b5ea2f56
		FunctionReader xh= new FunctionReader(path);

		PuttingCourse course1 = xh.get_Course();
		System.out.println("dd"+course1.get_height().toString());

		 */
		
//		String path="C:\\Users\\husam\\git\\mainProject\\course1.txt";
//		
//		CourseFileReader xh= new CourseFileReader(path);
//		
//		PuttingCourse course1 = xh.readFile();
//		System.out.println(course1.get_height().toString());
		
		Function2d height= new FunctionH("0");
		
		Vector2d flag = new Vector2d(0,3);
		Vector2d start = new Vector2d(0,0);
		
		double g,m,mu,vmax,tol;
		g=9.81;m=45.93/1000;mu=0.131;vmax=3;tol=0.02;
		
		PuttingCourse course = new PuttingCourse(height,flag, start, mu, vmax,tol,g,m );
		
		EulerSolver engine= new EulerSolver();
		
		PuttingSimulator s= new PuttingSimulator(course,engine);
		
		Vector2d initial_ball_velocity= new Vector2d(1.5,0);
		
		s.take_shot(initial_ball_velocity);
		
		System.out.println(s.get_ball_position().toString());
		
<<<<<<< HEAD
		long endT = System.currentTimeMillis();
		System.out.println(" " + ((endT - startT) / 1000.) + " seconds ");
		
=======
>>>>>>> da9e7f9c1024b79de99eedbcabc3b459b5ea2f56
		//course.output_to_file("C:\\Users\\husam\\git\\mainProject\\course");
	}
}
