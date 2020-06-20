package MainProject;

public class MathEngine {
	public static double gradient(Vector2d p1, Vector2d p2) {
		return (p1.get_y()-p2.get_y())/(p1.get_x()-p2.get_x());
	}
	
	public static double normal(double gradient) {
		return (-1/gradient);
	}
	
	public static double normal(Vector2d p1, Vector2d p2) {
		return normal(gradient(p1,p2));
	}
	
	public static double angleWithALine(Vector2d v, Vector2d lineVector) {
		double p,q,ap,aq;
		p=v.get_x()*lineVector.get_x();
		q=v.get_y()*lineVector.get_y();
		
		ap=v.get_scalar();
		aq=lineVector.get_scalar();
		
		return ((p+q)/(ap*aq));		
	}
	
	public static Vector2d doubleToVector(double gradient) {
		return new Vector2d(1,gradient);
	}
}
