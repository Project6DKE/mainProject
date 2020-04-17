package MainProject;

public class Vector2d {
	double x,y;
	public Vector2d(double x, double y) {this.x=x;this.y=y;}
	public double get_x(){return x;}
	public double get_y(){return y;}
	public void set_x(double x) {this.x=x;}
	public void set_y(double y) {this.y=y;}
	
	public double get_scalar(){
		return Math.abs(Math.sqrt(x*x+y*y));
	}
	
	public double get_distance(Vector2d aVec){
		return Math.sqrt(Math.pow((this.x-aVec.x),2)+Math.pow((this.y-aVec.y),2));
	}
	
	public double getXDistance(Vector2d aVec) {
		return (aVec.x-this.x);
	}
	
	public double getYDistance(Vector2d aVec) {
		return (aVec.y-this.y);
	}

	// I think the angle is in radians, but I need to test it out later
	public double get_angle(Vector2d aVec){
		double firstOp = this.get_dotProduct(aVec)/(this.get_scalar()*aVec.get_scalar());
		return Math.acos(firstOp);
	}

	public double get_dotProduct(Vector2d aVec){
		return this.x*aVec.get_x()+this.y*aVec.get_y();
	}
	
	public String toString() {
		return "X: "+x+"\t Y: "+y+"\t Scalar: "+get_scalar();
	}
}
