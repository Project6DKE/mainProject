package Physics;

public class EulerSolver implements PhysicsEngine {
	private double h,g,mu;
	
	
	public EulerSolver() {this.h=1/30;}
	
	public EulerSolver(double x) {this.h=x;}
	
	public void set_step_size(double h) {this.h=h;}
	
	public Vector2d solve(Vector2d start, Vector2d velocity ) {
		double Sx,Sy,Vx,Vy,Ax,Ay,g,mu;
		Sx=start.get_x();
		Sy=start.get_y();
		Vx=velocity.get_x();
		Vy=velocity.get_y();
		boolean move=true;
		while (move) {
			Ax=cal_A(Vx,Vy);
			Ay=cal_A(Vx,Vy);
		}
		
		
		return new Vector2d(Sx,Sy);
	}
	
	//I reflects the plane we are calculating on, j is the other one
	private double cal_A(double Vi, double Vj) {
		
		return 0.0;
	}
}
