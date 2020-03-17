package Physics;

import javax.script.*;

public class FunctionH implements Function2d{
	
	@Override
	public double evaluate(Vector2d p) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Vector2d gradient(Vector2d p) {
		double del=0.001;
		double initial=evaluate(p);
		double x =(evaluate((new Vector2d(p.get_x()+del,p.get_y())))-initial)/del;
		double y =(evaluate((new Vector2d(p.get_x(),p.get_y()+del)))-initial)/del;
		return new Vector2d(x,y);
	}

}
