package botFolder;

import MainProject.PuttingSimulator;
import MainProject.RungeKutta;
import MainProject.Vector2d;

public class BackwardsAI {
	
	PuttingSimulator theGame;
    Vector2d flag;
    //Vector2d modFlag;
    
    Vector2d lastBallPosition;
    Vector2d currentBallPosition;
    
    static final double STOP0 = 0.00000000000001;
    
    static final double STEPSIZE = 10;
    //EulerSolver odeSolver;
    RungeKutta odeSolver;
    
	
	public BackwardsAI(PuttingSimulator simulator){
        this.theGame = simulator;
        this.flag = simulator.getCourse().get_flag_position();
        this.currentBallPosition = simulator.get_ball_position();
        //this.odeSolver = new EulerSolver();
        this.odeSolver = new RungeKutta();
    }
	
	public void takeShot() {
		this.currentBallPosition = theGame.get_ball_position();
		
		boolean cont = true;
		
		double xdist = flag.getXDistance(currentBallPosition);
    	double ydist = flag.getYDistance(currentBallPosition);
		
    	Vector2d velocity = new Vector2d(Math.signum(-xdist)*STOP0, Math.signum(-ydist)*STOP0);
		
    	Vector2d[] motionValues = this.odeSolver.solveBack(flag, velocity);
    	
		while(cont) {
			
			// Change to close enough
			if (theGame.getCourse().is_start(motionValues[0])) {
				cont = false;
				break;
			}
			
			motionValues = this.odeSolver.solveBack(motionValues[0], motionValues[1]);
			
		}
		
		theGame.take_shot(motionValues[0]);
		
		
	}

}
