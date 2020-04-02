package Physics;

public class BasicAI {

    /*
    Basics of the AI are in place, all that's needed now is for it to play the game until the shot has been put
     */

    PuttingSimulator theGame;
    Vector2d flag;
    Vector2d lastBallPosition;
    Vector2d currentBallPosition;

    BasicAI(PuttingSimulator simulator){
        this.theGame = simulator;
        this.flag = simulator.course.get_flag_position();
        this.currentBallPosition = simulator.get_ball_position();
    }

    public void takeShot(){
        /*
        For the basic version of the AI it will take the field as if it was flat so that it can do something at the start
        The better versions of process field should be able to give info into whether a shot will just go up and back down a mountain or something like that
        Eventually it should be able to deal with a maze, but this is the start
         */

        double distance = currentBallPosition.get_distance(flag);
        double acceleration = theGame.calculate_acceleration(currentBallPosition).get_scalar();

        double maxV = theGame.course.get_maximum_velocity();
        double requiredVel = Math.sqrt(2*acceleration*distance);
        double angle = currentBallPosition.get_angle(flag);

        if(requiredVel>maxV){
            requiredVel = maxV;
        }
        /*
        At this point I would return requiredVel somehow to take the shot, I'll update that after work
         */

        theGame.take_angle_shot(requiredVel,angle);

    }

}
