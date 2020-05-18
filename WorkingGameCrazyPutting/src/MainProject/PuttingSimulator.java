package MainProject;

import java.util.ArrayList;
import java.io.*;

public class PuttingSimulator {
	private PuttingCourse course;
	RungeKutta engine;

	Vector2d position;
	Vector2d velocity;
	Vector2d acceleration;

	boolean course_put = false;
	
	int shot = 0;
	private Vector2d stopV = new Vector2d(0.01,0.01);
	final int pointOfAbandon = 1000000;

	private int solverChoice = 2; //0 for euler, 1 for RK4, 2 for AB3

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
	
	public void setSolver(String solver) {
		solver=solver.toLowerCase();
		if(solver.equals("euler")) solverChoice=0;
		else if(solver.equals("rk4")) solverChoice=1;
		else if(solver.equals("ab3")) solverChoice=2;
		else System.out.println("Solver not recognized");
	}
	
	public String getSolver() {
		switch(solverChoice) {
		case 0:
			return "Euler";
		case 1:
			return "Runge Kutta";
		case 2:
			return "Adams Bashforth";
		}
		return "";
	}
	
	public void take_shot(Vector2d initial_ball_velocity) {
		switch(solverChoice) {
		case 0:
			take_shot_euler(initial_ball_velocity);
			break;
		case 1:
			take_shot_RK(initial_ball_velocity);
			break;
		case 2:
			take_shot_ab3(initial_ball_velocity);
			break;
		}
	}
	
	public void take_shot_euler(Vector2d initial_ball_velocity) {
		System.out.println("This is shot #"+(++shot));
		
		/*
		 * This is a system to make sure that a given shot is always going to be less than maxV
		 * Not hard to implement, kind of weird.
		 * Assumes that the new value should have the same relation between x and y (so if x is twice y then this new value will do that as well)
		 * This is done to give a focus to direction when taking a shot that's too strong.
		 */
		
		this.velocity = initial_Velocity_Check( initial_ball_velocity);
		
		//this.velocity=initial_ball_velocity;
		Vector2d temp= position;
		boolean conti=true;
		int count = 0;
		while((conti) && (count<pointOfAbandon)) {
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
			count++;
			
			if(isStop()) conti=false;
			
			
//			System.out.println("vel: "+velocity.toString());
//			System.out.println("acc: "+acceleration.toString());
//			System.out.println("pos: "+position.toString()+"\n");
		}
		
		if(course.is_put(position)) {
			put();
			System.out.println("You have putted, number of shots: "+shot);
		}
	}
	
	public ArrayList<Vector2d> take_shot_list(Vector2d initial_ball_velocity) {
		ArrayList<Vector2d> ballPath= new ArrayList<Vector2d>();
		System.out.println("This is shot #"+(++shot));
		
		/*
		 * This is a system to make sure that a given shot is always going to be less than maxV
		 * Not hard to implement, kind of weird.
		 * Assumes that the new value should have the same relation between x and y (so if x is twice y then this new value will do that as well)
		 * This is done to give a focus to direction when taking a shot that's too strong.
		 */
		
		initial_Velocity_Check( initial_ball_velocity);
		
		this.velocity=initial_ball_velocity;
		Vector2d temp= position;
		boolean conti=true;
		while(conti) {
			acceleration=course.calculate_acceleration(position, velocity);
			position=engine.solve(position, velocity);
			ballPath.add(position);
			if(course.is_water(position)) {
				System.out.println("Your ball has gone into water, +1 shot penalty! \nCurrent Score: "+(++shot));
				position=temp;
				ballPath.add(position);
				velocity=new Vector2d(0,0);
				conti=false;
				break;
			}
			
			velocity=engine.solve(velocity, acceleration);
			if(isStop()) conti=false;
			
		}
		
		if(course.is_put(position)) {
			put();
			System.out.println("You have putted, number of shots: "+shot);
		}
		return ballPath;
	}
	
	public Vector2d initial_Velocity_Check(Vector2d ballVelocity) {
		double newX = ballVelocity.get_x();
		double newY = ballVelocity.get_y();
		
		if(ballVelocity.get_scalar()> maxV) {
			double temp = ballVelocity.get_x()/ballVelocity.get_y();
			newY = NegativeRoot((maxV*maxV)/(temp*temp+1));
			newX = newY*temp;
			
			
		}
		
		return new Vector2d(newX,newY);
		
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
			if(isStop())conti=false;
//			System.out.println("failed to stop at "+position);
		}
		
		if(course.is_put(position)) {
			put();
			System.out.println("You have putted, number of shots: "+shot);
		}
	}
		
	public ArrayList<Vector2d> take_shot_ab3(Vector2d initial_ball_velocity){
		System.out.println("This is shot #"+(++shot)+" using ab3");
		ArrayList<Vector2d> ballPath= new ArrayList<Vector2d>();
		initial_Velocity_Check( initial_ball_velocity);
		Vector2d temp= position;
		velocity=initial_ball_velocity;
		
		Vector2d[] initialValues= engine.bootstrap_AB3(position, velocity);
		velocity=engine.get_velocity();
		for(int i=0;i<initialValues.length;i++) {
			position=initialValues[i];
			ballPath.add(position);
			if(course.is_water(position)) {
				System.out.println("Your ball has gone into water, +1 shot penalty! \nCurrent Score: "+(++shot));
				position=temp;
				ballPath.add(position);
				velocity=new Vector2d(0,0);
				return ballPath;
			}
			if(isStop()) {break;}
			else {System.out.println("failed to stop at "+position);}
			
		}
		
		
		boolean conti=true;
		while(conti&& ! isStop()) {
			Vector2d[] data=engine.solve_AB3(position, velocity);
			position=data[0];
			velocity=data[1];
			ballPath.add(position);
			if(course.is_water(position)) {
				System.out.println("Your ball has gone into water, +1 shot penalty! \nCurrent Score: "+(++shot));
				position=temp;
				ballPath.add(position);
				velocity=new Vector2d(0,0);
				conti=false;
				return ballPath;
			}
			
			if(isStop()) {conti=false;}
			else {System.out.println("failed to stop at "+position);}
			conti=false;
		}
		System.out.println("This is shot #"+(++shot)+" using ab3");
		
		if(course.is_put(position)) {
			put();
			System.out.println("You have putted, number of shots: "+shot);
		}
		
		return ballPath;
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
	
	public boolean isStop() {
		if(velocity.get_scalar()<stopV.get_scalar() && calculate_acceleration(position,new Vector2d(0,0)).get_scalar()< 0.01) 
			return true;
		else return false;
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

	public static double NegativeRoot(double num) {
		if (num<0) {
			return -Math.sqrt(-num);
		} else {
			return Math.sqrt(num);
		}
	}
	
	public double distToFlag() {
		return this.position.get_distance(course.get_flag_position());
	}

	public PuttingCourse getCourse() {
		return this.course;
	}
	
	public void setShot(int nbr) {
		shot = nbr;
	}
	
	public void restart_simulation() {
		this.position=course.get_start_position();
		this.velocity=new Vector2d(0,0);
		this.shot=0;
		this.course_put = false;
	}
}
