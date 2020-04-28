package botFolder;

import java.util.ArrayList;
import java.util.List;

import MainProject.*;
import MainProject.Vector2d;

public class NewAI {
	
	PuttingSimulator theGame;
    Vector2d flag;
    Vector2d lastBallPosition;
    Vector2d currentBallPosition;
    static final Vector2d STOPPING = new Vector2d(0,0);
    static final double STEPSIZE = 10;
    EulerSolver odeSolver;

    public NewAI(PuttingSimulator simulator){
        this.theGame = simulator;
        this.flag = simulator.getCourse().get_flag_position();
        this.currentBallPosition = simulator.get_ball_position();
        this.odeSolver = new EulerSolver();
    }
    
    public void takeShot() {
    	
    	this.currentBallPosition = theGame.get_ball_position();
    	
    	double xdist = flag.getXDistance(currentBallPosition);
    	double ydist = flag.getYDistance(currentBallPosition);
    	
    	Vector2d velocity = STOPPING;
    	
    	//List<Vector2d> posList = new ArrayList<>();
    	
    	double xStep = xdist/STEPSIZE;
    	double yStep = ydist/STEPSIZE;
    	
    	/*
    	 *  The idea behind the method is okay and seems to be working
    	 *  Main bugs are my implementation for finding the acceleration at a specific point
    	 *  And some uncertainty with the points for which the acceleration is being calculated
    	 *  (Not sure if the point where the ball is matters, or whether the point where the flag is should be the start point)
    	 */
    	
    	for(int i=0; i<STEPSIZE+1;i++) {
    		double newX = flag.get_x() + i*xStep;
    		double newY = flag.get_y() + i*yStep;
    		
    		//posList.add(new Vector2d(newX,newY));
    		
    		Vector2d newPos = new Vector2d(newX,newY);
    		
    		Vector2d accelStep = theGame.accelerationAtPoint(velocity, newPos);
    		
    		velocity = odeSolver.solve(velocity, accelStep);
    		
    		
    	}
    	
    	theGame.take_shot(velocity);
    	
    	
    }
        
    
	
}
