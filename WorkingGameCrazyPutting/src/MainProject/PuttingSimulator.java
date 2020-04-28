package MainProject;

import java.io.BufferedReader;
import java.io.FileReader;

public class PuttingSimulator {
	private PuttingCourse course;
	RungeKutta engine;
	Vector2d position, velocity, acceleration;
	boolean course_put=false;
	
	int shot=0;
	private Vector2d stopV= new Vector2d(0.01,0.01);
	
	double maxV;
	
	/*
	 * Old code for the sake of bugtesting, should be deleted soon-ish
	 */
	
	EulerSolver engineOld;
	
	public PuttingSimulator(PuttingCourse course, RungeKutta engine) {
		this.course=course;
		this.engine=engine;
		engine.setCourse(course);
		position=course.get_start_position();
		this.maxV = this.course.get_maximum_velocity();
		}

	public PuttingSimulator(PuttingCourse pC, EulerSolver eulerSolver) {
		this.course = pC;
		this.engineOld = eulerSolver;
		this.maxV = this.course.get_maximum_velocity();
		this.position = course.get_start_position();
		
	}

	public void set_ball_position(Vector2d p) {
		this.position=p;
	}
	
	public Vector2d get_ball_position() {
		return position;
	}
	
	public boolean get_put_state() {
		return course_put;
	}
	
	public void take_shot(Vector2d initial_ball_velocity) {
		System.out.println("This is shot #"+(++shot));
		
		/*
		 * This is a system to make sure that a given shot is always going to be less than maxV
		 * Not hard to implement, kind of weird.
		 * Assumes that the new value should have the same relation between x and y (so if x is twice y then this new value will do that as well)
		 * This is done to give a focus to direction when taking a shot that's too strong.
		 */
		
		if(initial_ball_velocity.get_scalar()> maxV) {
			double temp = initial_ball_velocity.get_x()/initial_ball_velocity.get_y();
			double newX = Math.sqrt( (maxV*maxV)/(temp*temp +1));
			double newY = newX/temp;
			
			initial_ball_velocity.set_x(newX);
			initial_ball_velocity.set_y(newY);
			
		}
		
		this.velocity=initial_ball_velocity;
		Vector2d temp= position;
		boolean conti=true;
		while(conti) {
			acceleration=course.calculate_acceleration(position, velocity);
			position=engine.solve(position, velocity);
			if(course.is_water(position)) {
				System.out.println("Your ball has gone into water, +1 shot penalty! \nCurrent Score: "+(++shot));
				position=temp;
				velocity=new Vector2d(0,0);
				conti=false;
				break;
			}
			
			velocity=engine.solve(velocity, acceleration);
//			if(velocity.get_scalar()<stopV.get_scalar() && acceleration.get_scalar()< course.calculate_acceleration(position, stopV).get_scalar()) conti=false;
			if(velocity.get_scalar()<0.01 && acceleration.get_scalar()< 0.05) conti=false;
			
//			System.out.println("vel: "+velocity.toString());
//			System.out.println("acc: "+acceleration.toString());
//			System.out.println("pos: "+position.toString()+"\n");
		}
		
		if(course.is_put(position)) {
			put();
			System.out.println("You have putted, number of shots: "+shot);
		}
	}
	
	public void take_shot_RK(Vector2d initial_ball_velocity) {
		System.out.println("This is shot #"+(++shot));
		this.velocity=initial_ball_velocity;
		Vector2d temp= position;
		boolean conti=true;
		while(conti) {
			Vector2d[] data=engine.solve_RK(position, velocity);
			position=data[0];
			velocity=data[1];
			if(course.is_water(position)) {
				System.out.println("Your ball has gone into water, +1 shot penalty! \nCurrent Score: "+(++shot));
				position=temp;
				velocity=new Vector2d(0,0);
				conti=false;
				break;
			}
		}
		
		if(course.is_put(position)) {
			put();
			System.out.println("You have putted, number of shots: "+shot);
		}
	}
		
	public void put() {
		course_put=true;
	}
	
	public void take_shot(double x, double y) {
		take_shot(new Vector2d(x,y));
	}
	
	//Angle has to be in radians
	public void take_angle_shot(double speed, double angle) {
		double x = speed*Math.cos(angle);
		double y = speed*Math.sin(angle);
		take_shot(x,y);
	}
	
	public Vector2d calculate_acceleration(Vector2d vv) {
		return calculate_acceleration(position, vv);
	}
	
	public Vector2d calculate_acceleration(Vector2d positionn, Vector2d vv) {
		return course.calculate_acceleration(positionn, vv);
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

	public PuttingCourse getCourse() {
		return this.course;
	}
}
