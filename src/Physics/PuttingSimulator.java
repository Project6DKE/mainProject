package Physics;

public class PuttingSimulator {
	PuttingCourse course; EulerSolver engine;
	Vector2d position, velocity, acceleration;
	
	public PuttingSimulator(PuttingCourse course, EulerSolver engine) {this.course=course; this.engine=engine;position=course.get_start_position();}

	public void set_ball_position(Vector2d p) {this.position=p;}
	
	public Vector2d get_ball_position() {return position;}
	
	public void take_shot(Vector2d initial_ball_velocity) {
		this.velocity=initial_ball_velocity;
		Vector2d temp= position;
		boolean conti=true;
		while(conti) {
			acceleration=calculate_acceleration();
			position=engine.solve(position, velocity);
			if(course.is_water(position)) {
				position=temp;
				//SEND POSITION TO GRAPHICS
				velocity=new Vector2d(0,0);
				conti=false;
				break;
			}
			else if(course.is_put(position)) {
				//Activate put sequence
				conti=false;
				break;
			}
			//SEND POSITION TO GRAPHICS
			velocity=engine.solve(velocity, acceleration);
			if(velocity.get_scalar()<0.05 && velocity.get_scalar()>-0.05) conti=false;
			
			System.out.println("v "+velocity.toString());
			System.out.println("acc "+acceleration.toString());
			System.out.println(position.toString());
			//conti=false;
		}
	}
	
	private Vector2d calculate_acceleration(){
		double Ax, Ay, mu, g;
		g=course.get_gravity();mu=course.get_friction_coefficient();
		Vector2d gradient=course.get_height().gradient(position);
		
		Ax=(-g*gradient.get_x())-((mu*g*velocity.get_x())/velocity.get_scalar());
		Ay=(-g*gradient.get_y())-((mu*g*velocity.get_y())/velocity.get_scalar());
		return new Vector2d(Ax,Ay);
	}
}
