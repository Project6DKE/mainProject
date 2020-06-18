package MainProject;

public class Tree {
	
	private double r;
	private Vector2d center;
	
	public Tree(double r, Vector2d center) {
		this.r=r;
		this.center=center;
	}
	
	public void setRadius(double r) {this.r=r;}
	
	public void setCenter(Vector2d center) {this.center=center;}
	
	public double getRadies() {return r;}
	
	public Vector2d getCenter() {return center;}
	
	public boolean inTree(Vector2d p) {
		double x=p.get_x()-center.get_x();
		double y=p.get_y()-center.get_y();
		if((x*x+y*y)<=(r*r)) return true;
		return false;
	}
}
