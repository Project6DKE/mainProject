package botFolder;

import MainProject.PuttingCourse;
import MainProject.PuttingSimulator;
import MainProject.Vector2d;

public class BasicAI implements PuttingBot {

    /*
    Basics of the AI are in place, all that's needed now is for it to play the game until the shot has been put
     */

    PuttingSimulator theGame;
    Vector2d flag;
    Vector2d lastBallPosition;
    Vector2d currentBallPosition;
    
    static final double STOP0 = 0.00000000000001;

    public BasicAI() {
    	
    }
    
    public BasicAI(PuttingSimulator simulator){
        this.theGame = simulator;
        this.flag = simulator.getCourse().get_flag_position();
        this.currentBallPosition = simulator.get_ball_position();
    }

    public void takeShot(){
        /*
        For the basic version of the AI it will take the field as if it was flat so that it can do something at the start
        The better versions of process field should be able to give info into whether a shot will just go up and back down a mountain or something like that
        Eventually it should be able to deal with a maze, but this is the start
         */
    	currentBallPosition = theGame.get_ball_position();

        //double distance = currentBallPosition.get_distance(flag);
        double xdist = currentBallPosition.getXDistance(flag);
        double ydist = currentBallPosition.getYDistance(flag);
        
        double vx0 = Math.signum(xdist)*STOP0;
    	double vy0 = Math.signum(ydist)*STOP0;
    	
    	Vector2d speed = new Vector2d(vx0, vy0);
        
        
        Vector2d acceleration = theGame.calculate_acceleration(speed);

        //double maxV = theGame.course.get_maximum_velocity();
        
        double reqVelX = getVel(acceleration.get_x(),xdist);
        double reqVelY = getVel(acceleration.get_y(),ydist);
        
        //double angle = currentBallPosition.get_angle(flag);

        /*if(reqVelX>maxV){
            reqVelY = maxV;
        }*/
        
        /*
        At this point I would return requiredVel somehow to take the shot, I'll update that after work
         */

        //theGame.take_angle_shot(requiredVel,angle);
        theGame.take_shot(reqVelX, reqVelY);

    }
    
    public static double getVel(double accel, double dist) {
    	double temp = -2*accel*Math.abs(dist);
    	
    	if (temp<0) {
    		return -Math.sqrt(-temp);
    	} else {
    		return Math.sqrt(temp);
    	}
    	
    }

	@Override
	public Vector2d shot_velocity(PuttingCourse course, Vector2d ball_position) {
		
		this.currentBallPosition = ball_position;
		this.flag = course.get_flag_position();
		
		double xdist = currentBallPosition.getXDistance(flag);
        double ydist = currentBallPosition.getYDistance(flag);
        
        double vx0 = Math.signum(xdist)*STOP0;
    	double vy0 = Math.signum(ydist)*STOP0;
    	
    	Vector2d speed = new Vector2d(vx0, vy0);
        
        
        Vector2d acceleration = course.calculate_acceleration(currentBallPosition,speed);

        //double maxV = theGame.course.get_maximum_velocity();
        
        double reqVelX = getVel(acceleration.get_x(),xdist);
        double reqVelY = getVel(acceleration.get_y(),ydist);
        
        //double angle = currentBallPosition.get_angle(flag);

        /*if(reqVelX>maxV){
            reqVelY = maxV;
        }*/
        
        /*
        At this point I would return requiredVel somehow to take the shot, I'll update that after work
         */

        //theGame.take_angle_shot(requiredVel,angle);
        return new Vector2d(reqVelX, reqVelY);
	}
	
	public String toString() {
		return "BasicAI";
	}

}