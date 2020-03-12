package Physics;

public class PuttingCourse {
	
	private Function2d height;
	private Vector2d flag,start;
	private double friction, maxV, tol, g, mass;
	
	public PuttingCourse(Function2d height,Vector2d flag, Vector2d start) {
		this.height=height;
		this.flag=flag;
		this.start=start;
	}
	
	public PuttingCourse(Function2d height,Vector2d flag, Vector2d start, double friction, double maxV, double tol, double g, double mass) {
		this.height=height;
		this.flag=flag;
		this.start=start;
		this.friction=friction;
		this.maxV=maxV;
		this.tol=tol;
		this.g=g;this.mass=mass;
	}
	
	public Function2d get_height() {return height;}
	
	public Vector2d get_flag_position() {return flag;}
	
	public Vector2d get_start_position() {return start;}
	
	public double get_friction_coefficient() {return friction;}
	
	public double get_maximum_velocity() {return maxV;}
	
	public double get_hole_tolerance() {return tol;}
	
	public double get_ball_mass() {return mass;}
	
	public double get_gravity() {return g;}
	
}
