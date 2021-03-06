package botFolder;

import MainProject.PuttingCourse;
import MainProject.PuttingSimulator;
import MainProject.RungeKutta;
import MainProject.Vector2d;

public class NewAI implements PuttingBot {
	
	PuttingSimulator theGame;
	PuttingCourse theCourse;
    Vector2d flag;
    //Vector2d modFlag;
    
    Vector2d lastBallPosition;
    
    static final double STOP0 = 0.00000000000001;
    
    
    //static final Vector2d STOPPING = new Vector2d(0,0);
    //static final Vector2d NEGSTOP = new Vector2d(0,0);
    
    
    
    static final double STEPSIZE = 50;
    //EulerSolver odeSolver;
    RungeKutta odeSolver;

    public NewAI(PuttingSimulator simulator){
        this.theGame = simulator;
        this.theCourse = simulator.getCourse();
        this.flag = simulator.getCourse().get_flag_position();
        //this.odeSolver = new EulerSolver();
        this.odeSolver = new RungeKutta();
    }
    
    public NewAI() {
    	
    }
    
    void takeShot() {
    	
    	Vector2d initBallPosition = theGame.get_ball_position();
    	
    	
    	//List<Vector2d> posList = new ArrayList<>();
    	
    	//Vector2d modFlag = new Vector2d(this.flag.get_x()-velocity.get_x(),this.flag.get_x()-velocity.get_y());
    	
    	
    	double xdist = flag.getXDistance(initBallPosition);
    	double ydist = flag.getYDistance(initBallPosition);
    	
    	/*
    	 * If one of the dist is 0 it causes problems
    	 */
    	
    	double vx0 = Math.signum(-xdist)*STOP0;
    	double vy0 = Math.signum(-ydist)*STOP0;
    	
    	Vector2d velocity = new Vector2d(vx0, vy0);
    	
    	xdist = xdist + vx0;
    	ydist = ydist + vy0;
    	
    	
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
    		
    		/*
    		 * Because I'm talking dist from flag to ball, when calculating from ball to flag
    		 * I need to swap around the signs
    		 */
    		
    		
    		Vector2d newDist = new Vector2d(-xStep,-yStep);
    		
    		Vector2d newPos = new Vector2d(newX,newY);
    		
    		//Vector2d accelStep = new Vector2d(-0.90871,-0.90871);
    		Vector2d accelStep = theGame.calculate_acceleration(newPos, velocity);
    		
    		velocity = findV0(velocity, accelStep, newDist);

    		
    		
    		
    	}
    	
    	theGame.take_shot(velocity);
    	
    	
    }
    
    @Override
	public Vector2d shot_velocity(PuttingCourse course, Vector2d ball_position) throws Exception {
		
		this.flag = course.get_flag_position();
		this.theCourse = course;
    	  	
    	
    	double xdist = flag.getXDistance(ball_position);
    	double ydist = flag.getYDistance(ball_position);
    	
    	/*
    	 * If one of the dist is 0 it causes problems
    	 */
    	
    	double vx0 = Math.signum(-xdist)*STOP0;
    	double vy0 = Math.signum(-ydist)*STOP0;
    	
    	Vector2d velocity = new Vector2d(vx0, vy0);
    	
    	// Slightly modifiying the target so that it in theory becomes more accurate
    	xdist = xdist + vx0;
    	ydist = ydist + vy0;
    	
    	
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
    	
    	//Vector2d movement = new Vector2d(xStep, yStep);
    	
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
    		
    		/*
    		 * Because I'm talking dist from flag to ball, when calculating from ball to flag
    		 * I need to swap around the signs
    		 */
    		
    		
    		Vector2d newDist = new Vector2d(-xStep,-yStep);
    		
    		Vector2d newPos = new Vector2d(newX,newY);
    		
    		Vector2d accelStep = course.calculate_acceleration(newPos, velocity);
    		
    		Vector2d vf= velocity;
    		
    		velocity = findV0(velocity, accelStep, newDist);
    		
    		//movement = findNewD(vf,velocity,accelStep);
    		
    		
    	}
    	
    	return velocity;
		
		
	}
    
    public Vector2d aimedShot(PuttingCourse course, Vector2d ball_position, Vector2d objective) {
    	this.theCourse = course;
    	
    	double xdist = objective.getXDistance(ball_position);
    	double ydist = objective.getYDistance(ball_position);
    	
    	double vx0 = Math.signum(-xdist)*STOP0;
    	double vy0 = Math.signum(-ydist)*STOP0;
    	
    	Vector2d velocity = new Vector2d(vx0, vy0);
    	
    	double xStep = xdist/STEPSIZE;
    	double yStep = ydist/STEPSIZE;
    	
    	for(int i=0; i<STEPSIZE;i++) {
    		double newX = objective.get_x()+i*xStep;
    		double newY = objective.get_y()+i*yStep;
    		
    		
    		
    		Vector2d newDist = new Vector2d(-xStep,-yStep);
    		
    		Vector2d newPos = new Vector2d(newX,newY);
    		
    		Vector2d accelStep = course.calculate_acceleration(newPos, velocity);
    		
    		Vector2d vf= velocity;
    		
    		velocity = findV0(velocity, accelStep, newDist);
    		
    		//movement = findNewD(vf,velocity,accelStep);
    		
    		
    	}
    	
    	return velocity;
    	
    }

    
    public static Vector2d findV0(Vector2d vf, Vector2d accel, Vector2d dist) {
    	
    	/*
    	 *  Equation is based off kinematic equations, Vf^2 = Vo^2 + 2ad
    	 *  Modified the variables so that it's Vo what's being looked for
    	 *  Additional change so that it can take into account to what direction the velocity is going
    	 */
    	double tempx = Math.signum(vf.get_x())*Math.pow(vf.get_x(), 2)-2.0*accel.get_x()*Math.abs(dist.get_x());
    	double tempy = Math.signum(vf.get_y())*Math.pow(vf.get_y(), 2)-2.0*accel.get_y()*Math.abs(dist.get_y());
    	
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
    
    public static Vector2d findNewD(Vector2d vf, Vector2d vo, Vector2d accel) {
    	double resX = ((vf.get_x()*vf.get_x())-(vo.get_x()*vo.get_x()))/(2.0*accel.get_x());
    	double resY = ((vf.get_y()*vf.get_y())-(vo.get_y()*vo.get_y()))/(2.0*accel.get_y());
    	
    	return new Vector2d(-resX,-resY);
    	
    }
    
    public static Vector2d newFindV0(Vector2d vf, Vector2d accel, Vector2d dist) {
    	
    	Vector2d time = quadraticTime(vf,accel,dist);
    	
    	double vx = basicFindV0(vf.get_x(),accel.get_x(),time.get_x());
    	double vy = basicFindV0(vf.get_y(),accel.get_y(),time.get_y());
    	
    	return new Vector2d(vx,vy);
    	
    }
    
    /*
     * Solves equations of the form a*x^2+b*x+c = 0
     * Will return results organized with the biggest one first
     * Also assumes there's only real roots, no imaginary roots
     */
    public static double[] basicQuadratic(double a, double b, double c) {
    	double temp = Math.sqrt(b*b-4*a*c);
    	
    	double res1=(-b+temp)/2*a;
    	double res2=(-b-temp)/2*a;
    	
    	// In this order to make the biggest result the first one
    	// For convenience when interacting with class
    	if (res1 >= res2) {
    		return new double[] {res1,res2};
    	} else {
    		return new double[] {res2,res1};
    	}
    	
    }
    
    /*
     * Testing class for mathematical assumption that for a given Vf, A, and X
     * There will be at least one Time in common between both results
     */
    
    public static Vector2d quadraticTime(Vector2d v, Vector2d accel, Vector2d dist) {
    	
    	double[] tx = basicQuadratic(accel.get_x()/2,-v.get_x(),dist.get_x());
    	double[] ty = basicQuadratic(accel.get_y()/2,-v.get_y(),dist.get_y());
    	
    	/*
    	 * TODO: add some way of checking which T to return
    	 * Currently only the largest t is returned
    	 */
    	
    	return new Vector2d(tx[0],ty[0]);
    	
    }
    
    public static double basicFindV0(double vf, double accel, double time) {
    	double v = vf-accel*time;
    	return v;
    }
    
    
	
}
