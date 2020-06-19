package MainProject;

public class SandPit {
	
	private double friction, startX, startY, endX, endY;
	
	public SandPit(double friction, double x1, double y1, double x2, double y2) {
		this.friction=friction;
		this.startX=x1;
		this.startY=y1;
		this.endX=x2;
		this.endY=y2;
	}
	
	public double getSandFriction() {
		return friction;
	}
	
	public boolean isSand(Vector2d p) {
		double x,y;
		x=p.get_x();
		y=p.get_y();
		return ((x>=startX)&&(x<=endX)) && ((y>=startY)&&(y<=endY));
	}
}
