package Physics;

public class PuttingSimulator {
	PuttingCourse course; EulerSolver engine;
	Vector2d position, velocity, acceleration;
	
	public PuttingSimulator(PuttingCourse course, EulerSolver engine) {this.course=course; this.engine=engine;position=course.get_start_position();}

	public void set_ball_position(Vector2d p) {this.position=p;}
	
	public void get_ball_position() {}
	
	public void take_shot(Vector2d initial_ball_velocity) {
		this.velocity=initial_ball_velocity;
		Vector2d temp= position;
		boolean conti=true;
		while(conti) {
			acceleration=calculate_acceleration();
			position=calculate_displacement();
			if(course.is_water(position)) {
				position=temp;
				//SEND POSITION TO GRAPHICS
				velocity=new Vector2d(0,0);
				break;
			}
			//SEND POSITION TO GRAPHICS
			velocity=calculate_velocity();
			if(velocity.get_scalar()==0 && acceleration.get_scalar()==0) conti=false;
		}
	}
	
	private Vector2d calculate_acceleration(){
		double Ax, Ay, mu, g;
		g=course.get_gravity();mu=course.get_friction_coefficient();
		Vector2d gradient=course.get_height().gradient(position);
		
		Ax=(-g*gradient.get_x())-( (mu*g*velocity.get_x())/velocity.get_scalar());
		Ay=(-g*gradient.get_y())-( (mu*g*velocity.get_y())/velocity.get_scalar());
		return new Vector2d(Ax,Ay);
	}
	
	private Vector2d calculate_displacement() {
		double h=engine.get_step_size();
		double Sx= position.get_x()+h*velocity.get_x();
		double Sy= position.get_y()+h*velocity.get_y();
		return new Vector2d(Sx,Sy);
	}
	
	private Vector2d calculate_velocity() {
		double h=engine.get_step_size();
		double Vx= velocity.get_x()+h*acceleration.get_x();
		double Vy= velocity.get_y()+h*acceleration.get_y();
		return new Vector2d(Vx,Vy);
	}
}
