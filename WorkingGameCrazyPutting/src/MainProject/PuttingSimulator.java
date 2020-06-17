package MainProject;

import java.util.ArrayList;
import java.io.*;

enum Solver {
	EULER("euler"),
	RUNGE_KUTTA4("runge kutta"),
	ADAMS_BASHFORTH("adams-bashforth"),
	VERLET("verlet");

	public String name;

	Solver(String name) {
		this.name = name;
	}};

public class PuttingSimulator {
	private PuttingCourse course;
	private RungeKutta engine;

	private Vector2d position, velocity, acceleration;

	private boolean course_put = false;

	int shot = 0;
	private Vector2d stopV = new Vector2d(0.01,0.01);
	final int pointOfAbandon = 1000000;

	private Solver solverChoice = Solver.VERLET;

	double maxV;

	/*
	 * Old code for the sake of bug testing, should be deleted soon-ish
	 */

	EulerSolver engineOld;

	public PuttingSimulator(PuttingCourse course, RungeKutta engine) {
		this.course = course;
		this.engine = engine;
		engine.setCourse(course);
		position = course.get_start_position();
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
		switch (solver) {
			case "euler":
				solverChoice = Solver.EULER;
				break;
			case "rk4":
				solverChoice = Solver.RUNGE_KUTTA4;;
				break;
			case "ab3":
				solverChoice = Solver.ADAMS_BASHFORTH;
				break;
			case "verlet":
				solverChoice = Solver.VERLET;
				break;
			default:
				System.out.println("Solver not recognized");
				break;
		}
	}

	public String getSolver() {
		return solverChoice.name;
	}

	public void take_shot(Vector2d initial_ball_velocity) {
		switch(solverChoice) {
			case EULER:
				take_shot_euler(initial_ball_velocity);
				break;
			case RUNGE_KUTTA4:
				take_shot_RK(initial_ball_velocity);
				break;
			case ADAMS_BASHFORTH:
				take_shot_ab3_list(initial_ball_velocity);
				break;
			case VERLET:
				take_shot_verlet(initial_ball_velocity);
				break;
		}
	}

	public ArrayList<Vector2d> take_shot_list(Vector2d initial_ball_velocity){
		switch(solverChoice) {
			case EULER:
				return take_shot_euler_list(initial_ball_velocity);
			case RUNGE_KUTTA4:
				return take_shot_RK_list(initial_ball_velocity);
			case ADAMS_BASHFORTH:
				return take_shot_ab3_list(initial_ball_velocity);
			case VERLET:
				return take_shot_verlet_list(initial_ball_velocity);
		}
		return null;
	}

	public void take_shot_euler(Vector2d initial_ball_velocity) {
		System.out.println("This is shot #"+(++shot)+"using euler");
		this.velocity = initial_Velocity_Check( initial_ball_velocity);

		Vector2d temp= position;
		boolean conti=true;

		int count = 0;
		while((conti) && (count<pointOfAbandon)) {
			acceleration=course.calculate_acceleration(position, velocity);
			position=engine.solve(position, velocity);

			if (course.is_water(position)) {
				System.out.println("Your ball has gone into water, +1 shot penalty! \nCurrent Score: "+(++shot));
				position = temp;
				velocity = new Vector2d(0,0);
				conti = false;
				break;
			}

			velocity=engine.solve(velocity, acceleration);
			count++;

			if(isStop()) conti=false;
		}

		if(course.is_put(position)) {
			put();
		}
	}

	public ArrayList<Vector2d> take_shot_euler_list(Vector2d initial_ball_velocity) {
		ArrayList<Vector2d> ballPath= new ArrayList<Vector2d>();
		System.out.println("This is shot #"+(++shot));
		this.velocity=initial_Velocity_Check( initial_ball_velocity);

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

	/*
	 * This is a system to make sure that a given shot is always going to be less than maxV
	 * Not hard to implement, kind of weird.
	 * Assumes that the new value should have the same relation between x and y (so if x is twice y then this new value will do that as well)
	 * This is done to give a focus to direction when taking a shot that's too strong.
	 */
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
		System.out.println("This is shot #"+(++shot)+"using RK4");

		this.velocity=initial_Velocity_Check( initial_ball_velocity);
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
		}

		if(course.is_put(position)) {
			put();
		}
	}

	public ArrayList<Vector2d> take_shot_RK_list(Vector2d initial_ball_velocity){
		System.out.println("This is shot #"+(++shot)+"using RK4");
		ArrayList<Vector2d> ballPath= new ArrayList<Vector2d>();

		this.velocity=initial_Velocity_Check( initial_ball_velocity);
		Vector2d temp= position;
		ballPath.add(position);

		boolean conti=true;
		while(conti) {
			Vector2d[] data=engine.solve_RK(position, velocity);
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
			if(isStop())conti=false;
		}

		if(course.is_put(position)) {
			put();
		}
		return ballPath;
	}

	public ArrayList<Vector2d> take_shot_ab3_list(Vector2d initial_ball_velocity){
		System.out.println("This is shot #"+(++shot)+" using ab3");
		ArrayList<Vector2d> ballPath= new ArrayList<Vector2d>();

		this.velocity=initial_Velocity_Check( initial_ball_velocity);
		Vector2d temp= position;

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
			if(isStop()) break;
		}

		boolean conti=true;
		while(conti&& !isStop()) {
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

			if(isStop()) conti=false;
		}
		if(course.is_put(position)) put();

		return ballPath;
	}

	public void take_shot_verlet(Vector2d initial_ball_velocity) {
		System.out.println("This is shot #"+(++shot)+"using verlet");
		this.velocity = initial_Velocity_Check( initial_ball_velocity);

		Vector2d temp= position;
		boolean conti=true;
//		int count = 0;
		while((conti)/* && (count<pointOfAbandon)*/) {
			Vector2d[] data=engine.solve_Verlet(position, velocity);

			position=data[0];
			velocity=data[1];

			if(course.is_water(position)) {
				System.out.println("Your ball has gone into water, +1 shot penalty! \nCurrent Score: "+(++shot));
				position=temp;
				velocity=new Vector2d(0,0);
				conti=false;
				break;
			}
			//			count++;
			if(isStop()) conti=false;
		}

		if(course.is_put(position)) {
			put();
		}
	}

	public ArrayList<Vector2d> take_shot_verlet_list(Vector2d initial_ball_velocity){
		System.out.println("This is shot #"+(++shot)+"using verlet");
		this.velocity = initial_Velocity_Check( initial_ball_velocity);
		ArrayList<Vector2d> ballPath= new ArrayList<Vector2d>();

		Vector2d temp = position;
		ballPath.add(position);
		boolean conti=true;
//		int count = 0;
		while((conti)/* && (count<pointOfAbandon)*/) {
			Vector2d[] data=engine.solve_Verlet(position, velocity);

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
//			count++;
			if(isStop()) conti=false;
		}

		if(course.is_put(position)) {
			put();
		}
		return ballPath;
	}

	public void put() {
		course_put=true;
		System.out.println("You have putted, number of shots: "+shot);
	}

	public void take_shot(double x, double y) {
		take_shot(new Vector2d(x,y));
	}

	public ArrayList<Vector2d> take_shot_list(double x, double y) {
		return take_shot_list(new Vector2d(x,y));
	}

	//Angle has to be in degrees
	public void take_angle_shot(double speed, double angle) {
		angle=angle*Math.PI/180;
		double x = speed*Math.cos(angle);
		double y = speed*Math.sin(angle);
		x = Math.round(x*1000000000)/1000000000;
		y = Math.round(y*1000000000)/1000000000;
//		System.out.println("angle in rad is:"+ angle+"x: "+x+" y:"+y);
		take_shot(x,y);
	}

	public ArrayList<Vector2d> take_angle_shot_list(double speed, double angle) {
		angle=angle*Math.PI/180;
		double x = speed*Math.cos(angle);
		double y = speed*Math.sin(angle);
		x = Math.round(x*1000000000)/1000000000;
		y = Math.round(y*1000000000)/1000000000;
		return take_shot_list(x,y);
	}

	public Vector2d calculate_acceleration(Vector2d vv) {
		return calculate_acceleration(position, vv);
	}

	public Vector2d calculate_acceleration(Vector2d positionn, Vector2d vv) {
		return course.calculate_acceleration(positionn, vv);
	}

	public boolean isStop() {
		if(velocity.get_scalar()<stopV.get_scalar() && calculate_acceleration(position,new Vector2d(0,0)).get_scalar()< 0.01) return true;
		else return false;
	}

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
		boolean negative = num < 0;

		if (negative) {
			return -Math.sqrt(-num);
		} else {
			return Math.sqrt(num);
		}
	}

	public double distToFlag() {
		return this.position.get_distance(course.get_flag_position());
	}

	public Vector2d distToFlagVector() {
		return this.position.getVectDist(course.get_flag_position());
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

	public void setSolver(int x) {
		boolean invalidIndex = x < 0 || x > 3;
		if (invalidIndex) return;

		switch (x) {
			case 0:
				solverChoice = Solver.EULER;
				break;
			case 1:
				solverChoice = Solver.RUNGE_KUTTA4;
			case 2:
				solverChoice = Solver.ADAMS_BASHFORTH;
			case 3:
				solverChoice = Solver.VERLET;
		}
	}
}
