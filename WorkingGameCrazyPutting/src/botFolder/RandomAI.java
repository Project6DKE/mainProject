package botFolder;

import MainProject.PuttingCourse;
import MainProject.PuttingSimulator;
import MainProject.RungeKutta;
import MainProject.Vector2d;
import readingOfFunctions.Function2d;
import readingOfFunctions.FunctionH;

public class RandomAI implements PuttingBot {
	private double maxspeed;
	private double maxangle;
	
	public RandomAI(PuttingSimulator PS) {
		this.maxspeed = PS.getCourse().get_maximum_velocity();
        this.maxangle = 360 * Math.PI / 180;
	}
	
	public RandomAI() {
		this.maxangle = 360* Math.PI / 180;
	}
	
	//this returns the parameter to run PS.takeAngleShot()
	public double [] getShotParam() {
		double[] AngSp = new double [2];
		AngSp[0] = Math.random() * maxspeed;
		AngSp[1] = Math.random() * maxangle;
		return AngSp;
	}
	
	public Vector2d shot_velocity(PuttingCourse course, Vector2d ball_position) {
		this.maxspeed = course.get_maximum_velocity();
		double[] myShot = new double[2]; 
    	myShot = getShotParam();
    	double speed = myShot[0];
    	double angle = myShot[1];
    	double x = speed*Math.cos(angle);
		double y = speed*Math.sin(angle);
		Vector2d result = new Vector2d(x,y);
    	return result;
	}
	
	public String toString() {
		String name = "Random";
		return name;
	}

	//this is just for testing
	public static void main(String[] args) throws Exception {
		Function2d height= new FunctionH("0");
		
		Vector2d flag = new Vector2d(0,3);
		Vector2d start = new Vector2d(0,0);
		
		double g,m,mu,vmax,tol;
		g=9.81;m=45.93/1000;mu=0.131;vmax=3;tol=0.2;
		
		PuttingCourse course = new PuttingCourse(height,flag, start, mu, vmax,tol,g,m );
		PuttingSimulator putSim = new PuttingSimulator(course, new RungeKutta());
		
		RandomAI rand = new RandomAI(putSim);
		
		System.out.println("The ball before the shot : + " + putSim.get_ball_position().toString());
		
		double[] testparam = new double[2];
		testparam = rand.getShotParam();
		putSim.take_angle_shot(testparam[0], testparam[1]);
		
		System.out.println("The ball after the shot : + " + putSim.get_ball_position().toString());
		
	}
	
}
