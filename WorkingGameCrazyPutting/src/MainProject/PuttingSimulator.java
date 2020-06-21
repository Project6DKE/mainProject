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

	private Vector2d position, velocity, acceleration,temp;

	private ArrayList<Vector2d> ballPath=new ArrayList<Vector2d>();;

	private boolean course_put = false;
	private boolean water_penalty=false;

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

	public void take_shot(Vector2d initial_ball_velocity) {
		take_shot_list(initial_ball_velocity);
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

	public ArrayList<Vector2d> take_shot_euler_list(Vector2d initial_ball_velocity) {
		ArrayList<Vector2d> ballPath= new ArrayList<Vector2d>();
		System.out.println("This is shot #"+(++shot));
		this.velocity=initial_Velocity_Check( initial_ball_velocity);

		temp= position;
		boolean conti=true;
		while(conti) {
			acceleration=course.calculate_acceleration(position, velocity);
			position=engine.solve(position, velocity);
			ballPath.add(position);

			conti=collisionHandler(course.collisionDetector(position));

			velocity=engine.solve(velocity, acceleration);
			if(isStop()) conti=false;

		}

		if(course.is_put(position)) {
			put();
			System.out.println("You have putted, number of shots: "+shot);
		}
		return ballPath;
	}


	public ArrayList<Vector2d> take_shot_RK_list(Vector2d initial_ball_velocity){
		System.out.println("This is shot #"+(++shot)+"using RK4");
		ballPath= new ArrayList<Vector2d>();

		this.velocity=initial_Velocity_Check( initial_ball_velocity);
		temp= position;
		ballPath.add(position);

		boolean conti=true;
		while(conti) {
			Vector2d[] data=engine.solve_RK(position, velocity);
			position=data[0];
			velocity=data[1];
			ballPath.add(position);

			conti=collisionHandler(course.collisionDetector(position));

			if(isStop())conti=false;
		}

		if(course.is_put(position)) {
			put();
		}
		return ballPath;
	}

	public ArrayList<Vector2d> take_shot_ab3_list(Vector2d initial_ball_velocity){
		System.out.println("This is shot #"+(++shot)+" using ab3");
		ballPath= new ArrayList<Vector2d>();

		this.velocity=initial_Velocity_Check( initial_ball_velocity);
		temp= position;

		Vector2d[] initialValues= engine.bootstrap_AB3(position, velocity);
		velocity=engine.get_velocity();
		for(int i=0;i<initialValues.length;i++) {
			position=initialValues[i];
			ballPath.add(position);

			if(!collisionHandler(course.collisionDetector(position))) {return ballPath;}

			if(isStop()) break;
		}

		boolean conti=true;
		while(conti&& !isStop()) {
			Vector2d[] data=engine.solve_AB3(position, velocity);
			position=data[0];
			velocity=data[1];
			ballPath.add(position);

			conti=collisionHandler(course.collisionDetector(position));

			if(isStop()) conti=false;
		}
		if(course.is_put(position)) put();

		return ballPath;
	}


	public ArrayList<Vector2d> take_shot_verlet_list(Vector2d initial_ball_velocity){
		System.out.println("This is shot #"+(++shot)+"using verlet");
		this.velocity = initial_Velocity_Check( initial_ball_velocity);
		ballPath= new ArrayList<Vector2d>();

		temp= position;
		ballPath.add(position);
		boolean conti=true;
//		int count = 0;
		while((conti)/* && (count<pointOfAbandon)*/) {
			Vector2d[] data=engine.solve_Verlet(position, velocity);

			position=data[0];
			velocity=data[1];

			ballPath.add(position);

			conti=collisionHandler(course.collisionDetector(position));
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

	public boolean stopsAtPoint(Vector2d aPoint) {
		if(calculate_acceleration(aPoint, new Vector2d(0,0)).get_scalar()<0.01)
			return true;
		else
			return false;

	}

	public boolean collisionHandler(int type) {
		if(type==0) return waterHandler();
		if(type==2) return treeHandler();
		if(type==3) return wallHandler();
		return true;
	}

	private boolean waterHandler() {
		shot++;
		if(water_penalty) {
			System.out.println("Your ball has gone into water, +1 shot penalty! \nCurrent Score: "+(++shot));
			position=ballPath.get(ballPath.size()-2);
			ballPath.add(position);
			velocity=new Vector2d(0,0);
		}
		else {
			System.out.println("Your ball has gone into water, +1 shot penalty! \nCurrent Score: "+(++shot));
			position=temp;
			ballPath.add(position);
			velocity=new Vector2d(0,0);
		}
		return false;
	}

	private boolean treeHandler() {
		return true;
	}

	private boolean wallHandler() {
		Vector2d pos1, pos2;
		pos1=ballPath.get(ballPath.size()-1);
		pos2=ballPath.get(ballPath.size()-2);

		Wall w= course.find_wall(pos1);
		switch(w.reflectSide(pos1, pos2)) {
			case 0:
				reflect_x();
				break;
			case 1:
				reflect_y();
				break;
		}
		return true;
	}

	private void reflect_x() {
		velocity.set_y(velocity.get_y()*-1);
	}

	private void reflect_y() {
		velocity.set_x(velocity.get_x()*-1);
	}

	public void set_water_penalty(boolean x) {water_penalty=x;}

	public boolean get_water_penalty() {return water_penalty;}

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
	
	public void setSolver(Solver solver) {
		solverChoice = solver;
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
