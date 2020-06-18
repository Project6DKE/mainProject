package MainProject;

public class CircleSand implements SandPit{

	private double friction, r;
	private Vector2d center;
	
	public CircleSand(Vector2d center, double r, double friction) {
		this.center=center;
		this.r=r;
		this.friction=friction;
	}
	
	@Override
	public double getSandFriction() {
		return friction;
	}

	@Override
	public boolean isSand(Vector2d p) {
		double x=p.get_x()-center.get_x();
		double y=p.get_y()-center.get_y();
		if((x*x+y*y)<=(r*r)) return true;
		return false;
	}

}
