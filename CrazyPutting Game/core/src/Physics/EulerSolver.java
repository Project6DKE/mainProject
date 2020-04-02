package Physics;

public class EulerSolver implements PhysicsEngine {
	private double h;
	
	public EulerSolver() {this.h=0.01;}
	
	public EulerSolver(double x) {this.h=x;}
	
	public void set_step_size(double h) {this.h=h;}
	
	public double get_step_size() {return h;}
	
	public Vector2d solve(Vector2d a, Vector2d b) {
		double Rx= a.get_x()+h*b.get_x();
		double Ry= a.get_y()+h*b.get_y();
		return new Vector2d(Rx,Ry);
	}
}
