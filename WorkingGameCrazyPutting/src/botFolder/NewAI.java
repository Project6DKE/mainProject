package botFolder;

import MainProject.PuttingSimulator;
import MainProject.RungeKutta;
import MainProject.Vector2d;

public class NewAI {
	
	PuttingSimulator theGame;
    Vector2d flag;
    Vector2d lastBallPosition;
    Vector2d currentBallPosition;
    static final Vector2d STOPPING = new Vector2d(0.0000001,0.0000001);
    static final Vector2d NEGSTOP = new Vector2d(-0.0000001,-0.0000001);
    
    boolean positiveStop = true;
    
    static final double STEPSIZE = 1000;
    //EulerSolver odeSolver;
    RungeKutta odeSolver;

    public NewAI(PuttingSimulator simulator){
        this.theGame = simulator;
        this.flag = simulator.getCourse().get_flag_position();
        this.currentBallPosition = simulator.get_ball_position();
        //this.odeSolver = new EulerSolver();
        this.odeSolver = new RungeKutta();
    }
    
    public void takeShot() {
    	
    	this.currentBallPosition = theGame.get_ball_position();
    	
    	double xdist = flag.getXDistance(currentBallPosition);
    	double ydist = flag.getYDistance(currentBallPosition);
    	
    	Vector2d velocity;
    	
    	if (positiveStop) {
    		velocity = STOPPING;
    	} else {
    		velocity = NEGSTOP;
    	}
    	
    	//List<Vector2d> posList = new ArrayList<>();
    	
    	double xStep = xdist/STEPSIZE;
    	double yStep = ydist/STEPSIZE;
    	
    	/*
    	 *  The idea behind the method is okay and seems to be working
    	 *  Main bugs are my implementation for finding the acceleration at a specific point
    	 *  And some uncertainty with the points for which the acceleration is being calculated
    	 *  (Not sure if the point where the ball is matters, or whether the point where the flag is should be the start point)
    	 */
    	
    	/*
    	 * There's a fundamental issue with how the acceleration is being calculated, the shot being done is completely wrong
    	 * The issue is related to how V is calculated, I'll just deal with it later
    	 */
    	
    	for(int i=0; i<STEPSIZE;i++) {
    		double newX = flag.get_x()+i*xStep;
    		double newY = flag.get_y()+i*yStep;
    		
    		/*
    		 * The reason newX and newY had the i*zStep was to calculate the locations
    		 * where the accel would change.
    		 * 
    		 * It makes sense that there would be 2 variables-ish, one of them the dist
    		 * and one of them the location of the new accel
    		 */
    		
    		//posList.add(new Vector2d(newX,newY));
    		
    		Vector2d newDist = new Vector2d(-xStep,-yStep);
    		
    		Vector2d newPos = new Vector2d(newX,newY);
    		
    		//Vector2d accelStep = new Vector2d(-0.90871,-0.90871);
    		Vector2d accelStep = theGame.calculate_acceleration(newPos, velocity);
    		
    		velocity = findV0(velocity, accelStep, newDist);
    				//odeSolver.solve(velocity, accelStep);
    		
    		positiveStop = !positiveStop;
    		
    	}
    	
    	theGame.take_shot(velocity);
    	
    	
    }
    
    public static Vector2d findV0(Vector2d vf, Vector2d accel, Vector2d dist) {
    	
    	/*
    	 *  Equation is based off kinematic equations, Vf^2 = Vo^2 + 2ad
    	 *  Modified the variables so that it's Vo what's being looked for
    	 *  Additional change so that it can take into account to what direction the velocity is going
    	 */
    	double tempx = Math.pow(vf.get_x(), 2)-2*accel.get_x()*dist.get_x();
    	double tempy = Math.pow(vf.get_y(), 2)-2*accel.get_y()*dist.get_y();
    	
    	double vx,vy;
    	
    	if (tempx<0) {
    		vx = -Math.sqrt(-tempx);
    	} else {
    		vx = Math.sqrt(tempx);
    	}
    	
    	if (tempy<0) {
    		vy = -Math.sqrt(-tempy);
    	} else {
    		vy = Math.sqrt(tempy);
    	}
    	
    	return new Vector2d(vx,vy);
    	
    }
    
	
}
