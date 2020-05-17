package botFolder;

import MainProject.PuttingCourse;
import MainProject.PuttingSimulator;
import MainProject.Vector2d;

public class RandomAI implements PuttingBot {
	private double maxspeed;
	private double maxangle;
	
	public RandomAI(PuttingSimulator PS) {
		this.maxspeed = PS.getCourse().get_maximum_velocity();
        this.maxangle = 360 * Math.PI / 180;
	}
	
	public double [] getShotParam() {
		double[] AngSp = new double [2];
		AngSp[0] = Math.random() * maxspeed;
		AngSp[1] = Math.random() * maxangle;
		return AngSp;
	}
	
	public Vector2d shot_velocity(PuttingCourse course, Vector2d ball_position) {
		double[] myShot = new double[2]; 
    	myShot = getShotParam();
    	double speed = myShot[0];
    	double angle = myShot[1];
    	double x = speed*Math.cos(angle);
		double y = speed*Math.sin(angle);
		Vector2d result = new Vector2d(x,y);
    	return result;
	}
}
