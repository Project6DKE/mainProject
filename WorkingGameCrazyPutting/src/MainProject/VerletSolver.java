package MainProject;

public class VerletSolver {
    private PuttingCourse putCourse;
    private EulerSolver eulSol;
    private Vector2d velocity;
    private Vector2d acceleration;
    private Vector2d position;
    private Vector2d oldAcc;
    private double mu;
    private double g;
    private double velX;
    private double velY;
    private double posX;
    private double posY;
    private double accX;
    private double accY;
    private double dt;

    public VerletSolver(PuttingCourse putCourse, EulerSolver eulSol){
         this.putCourse=putCourse;
         this.eulSol=eulSol;
         dt= eulSol.get_step_size();
         this.mu=putCourse.get_friction_coefficient();
         this.g=putCourse.get_gravity();
         velX=velocity.get_x();
         velY=velocity.get_y();
         posX=position.get_x();
         posY=position.get_y();

    }
    public Vector2d calculateVelocity(){
        velX += (oldAcc.get_x()+acceleration.get_x()*(dt/2));
        velY += (oldAcc.get_y()+acceleration.get_y()*(dt/2));
        return new Vector2d(velX,velY);
    }
    public Vector2d calculatePosition(){
        posX+=velocity.get_x()*dt+(acceleration.get_x()*dt*dt)/2;
        posY+=velocity.get_y()*dt+(acceleration.get_y()*dt*dt)/2;
        return new Vector2d(posX,posY);
    }
    public Vector2d calculateAcceleration(Vector2d vel){
        Vector2d gradient=putCourse.get_height().gradient(position);
        accX= (-g*gradient.get_x())-(mu*g*vel.get_x()/vel.get_scalar());
        accY= (-g*gradient.get_y())-(mu*g*vel.get_y()/vel.get_scalar());
        return new Vector2d(accX,accY);

    }
    public void takeShot(Vector2d initVel){
        this.velocity=initVel;
        oldAcc= new Vector2d(0,0);
        acceleration = new Vector2d(0,0);
        Vector2d stopV = new Vector2d(0.01,0.01);
        boolean cont = true;
        while(cont) {
            Vector2d tempAcc = acceleration;
            acceleration = calculateAcceleration(velocity);
            oldAcc = tempAcc;
            position = calculatePosition();
            velocity = calculateVelocity();
            if (velocity.get_scalar() < stopV.get_scalar() && acceleration.get_scalar() < calculateAcceleration(stopV).get_scalar()) {
                cont = false;
            }
        }
    }
}
