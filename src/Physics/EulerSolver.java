package Physics;

public class EulerSolver implements PhysicsEngine {
	private double h;
	public void set_step_size(double h) {this.h=h;}
	
	public Vector2d solve(Vector2d vector) {
		double x=vector.get_x();
		double y=vector.get_y();
		
		return new Vector2d(x,y);
	}
	}
