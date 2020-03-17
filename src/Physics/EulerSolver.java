package Physics;

public class EulerSolver implements PhysicsEngine {
	private double h;
	
	public EulerSolver() {this.h=1/30;}
	
	public EulerSolver(double x) {this.h=x;}
	
	public void set_step_size(double h) {this.h=h;}
	
	public double get_step_size() {return h;}
}
