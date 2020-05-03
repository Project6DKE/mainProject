package botFolder;

import MainProject.PuttingSimulator;
import MainProject.Vector2d;

public class BasicAI {

    /*
    Basics of the AI are in place, all that's needed now is for it to play the game until the shot has been put
     */

    PuttingSimulator theGame;
    Vector2d flag;
    Vector2d lastBallPosition;
    Vector2d currentBallPosition;

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
        
        Vector2d speed = new Vector2d(0.0000001,0.0000001);
        
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
    	double temp = -2*accel*dist;
    	
    	if (temp<0) {
    		return -Math.sqrt(-temp);
    	} else {
    		return Math.sqrt(temp);
    	}
    	
    }

}