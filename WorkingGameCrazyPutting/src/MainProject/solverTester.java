package MainProject;

import readingOfFunctions.Function2d;
import readingOfFunctions.FunctionH;

public class solverTester {
	public static void main(String[] args) throws Exception{
//		Function2d height= new FunctionH("2");
//		Function2d height = new FunctionH("-0.01 * x + 0.003 * x ^ 2 + 0.04 * y");
		Function2d height = new FunctionH("( 7 * x * y ) / ( exp ( x ^ 2 + y ^ 2 ) )");
	
		Vector2d flag = new Vector2d(3,0);
		Vector2d start = new Vector2d(0,2);
		
		double g,m,mu,vmax,tol;
		g=9.81;m=45.93/1000;mu=0.131;vmax=10000;tol=0.03;
		
		PuttingCourse course = new PuttingCourse(height,flag, start, mu, vmax,tol,g,m );
		
		RungeKutta engine = new RungeKutta();
		
		PuttingSimulator s= new PuttingSimulator(course,engine);
		
		String[] solvers= {"Euler", "RK4","AB3","Verlet"};
		
		Vector2d initial_ball_velocity= new Vector2d(2000,20000);
		
		long startT, endT;
		long[] avgTime=new long[4];
		for(int i=0;i<4;i++) {
			avgTime[i]=0;
			for(int j=0;j<1;j++) {
				s.restart_simulation();
				s.setSolver(i);
				
				startT = System.currentTimeMillis();
				s.take_shot(initial_ball_velocity);
				endT = System.currentTimeMillis();
				
				avgTime[i]+=(endT-startT);
			}
			
		}
		
		for(int i=0;i<4;i++) {
			System.out.println("average time for "+solvers[i]+" was: "+(avgTime[i])/1.+" for course: "+height.toString());
			System.out.println(avgTime[i]);
		}
	}
}
