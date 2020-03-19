package Physics;

import java.io.BufferedReader;
import java.io.FileReader;

public class PuttingSimulator {
	PuttingCourse course; EulerSolver engine;
	Vector2d position, velocity, acceleration;
	int shot=0;
	private Vector2d stopV= new Vector2d(0.01,0.01);
	
	public PuttingSimulator(PuttingCourse course, EulerSolver engine) {this.course=course; this.engine=engine;position=course.get_start_position();}

	public void set_ball_position(Vector2d p) {this.position=p;}
	
	public Vector2d get_ball_position() {return position;}
	
	public void take_shot(Vector2d initial_ball_velocity) {
		System.out.println("This is shot #"+(++shot));
		this.velocity=initial_ball_velocity;
		Vector2d temp= position;
		boolean conti=true;
		while(conti) {
			acceleration=calculate_acceleration(velocity);
			position=engine.solve(position, velocity);
			if(course.is_water(position)) {
				System.out.println("Your ball has gone into water, +1 shot penalty! \nCurrent Score: "+(++shot));
				position=temp;
				//SEND POSITION TO GRAPHICS
				velocity=new Vector2d(0,0);
				conti=false;
				break;
			}
			
			//SEND POSITION TO GRAPHICS
			velocity=engine.solve(velocity, acceleration);
//			if(velocity.get_scalar()<stopV.get_scalar() && acceleration.get_scalar()< calculate_acceleration(stopV).get_scalar()) conti=false;
			if(velocity.get_scalar()<0.01 && acceleration.get_scalar()< 0.05) conti=false;
			
//			System.out.println("vel: "+velocity.toString());
//			System.out.println("acc: "+acceleration.toString());
//			System.out.println("pos: "+position.toString()+"\n");
		}
		
		if(course.is_put(position)) {
			//Activate put sequence
			System.out.println("You have putted, number of shots: "+shot);
		}
	}
	
	//Angle has to be in radians
	public void take_angle_shot(double speed, double angle) {
		double x = speed*Math.cos(angle);
		double y = speed*Math.sin(angle);
		take_shot(new Vector2d(x,y));
	}
	
	
	//Reads shots in the format (x.x, y.y)
	public void read_shots(String path) {
		String script[];

		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
	         String sCurrentLine;
	         while ((sCurrentLine = br.readLine()) != null) {
	        	 int xx= sCurrentLine.indexOf(",");
	        	 double x,y;
	        	 x=Double.valueOf(sCurrentLine.substring(1, xx));
	        	 y=Double.valueOf(sCurrentLine.substring(xx+1, sCurrentLine.length()));
	        	 take_shot(new Vector2d(x,y));
	         }
	      } catch (Exception e) {
	         System.out.println("File not found or error while reading the file.");
	      }
	}
	
	
	
	private Vector2d calculate_acceleration(Vector2d vv){
		double Ax, Ay, mu, g;
		g=course.get_gravity();mu=course.get_friction_coefficient();
		Vector2d gradient=course.get_height().gradient(position);
		
		Ax=(-g*gradient.get_x())-((mu*g*vv.get_x())/vv.get_scalar());
		Ay=(-g*gradient.get_y())-((mu*g*vv.get_y())/vv.get_scalar());
		return new Vector2d(Ax,Ay);
	}
}
