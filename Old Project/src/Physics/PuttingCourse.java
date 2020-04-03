package Physics;

import java.io.FileWriter;
import java.io.IOException;

public class PuttingCourse {
	
	private Function2d height;
	private Vector2d flag,start;
	private double friction, maxV, tol, g, mass;
	
	public PuttingCourse(Function2d height,Vector2d flag, Vector2d start) {
		this.height=height;
		this.flag=flag;
		this.start=start;
	}
	
	public PuttingCourse(Function2d height,Vector2d flag, Vector2d start, double friction, double maxV, double tol, double g, double mass) {
		this.height=height;
		this.flag=flag;
		this.start=start;
		this.friction=friction;
		this.maxV=maxV;
		this.tol=tol;
		this.g=g;this.mass=mass;
	}
	
	public Function2d get_height() {return height;}
	
	public Vector2d get_flag_position() {return flag;}
	
	public Vector2d get_start_position() {return start;}
	
	public double get_friction_coefficient() {return friction;}
	
	public double get_maximum_velocity() {return maxV;}
	
	public double get_hole_tolerance() {return tol;}
	
	public double get_ball_mass() {return mass;}
	
	public double get_gravity() {return g;}
	
	public boolean is_water(Vector2d p) {
		if(height.evaluate(p)<0) return true;
		return false;
	}
	
	public boolean is_put(Vector2d p) {
		double x=p.get_x()-flag.get_x();double y=p.get_y()-flag.get_y();
		if((x*x+y*y)<=(tol*tol)) return true;
		return false;
	}
	
	public String toString() {
		String ff="<html>Course Details<br>";
		ff+=("g = "+g+";<br>");
		ff+=("m = "+mass+";<br>");
		ff+=("mu = "+friction+";<br>");
		ff+=("<br>");
		ff+=("vmax = "+maxV+";<br>");
		ff+=("tol = "+tol+";<br>");
		ff+=("<br>");
		ff+=("start = ("+start.get_x()+", "+start.get_y()+");<br>");
		ff+=("goal = ("+flag.get_x()+", "+flag.get_y()+");<br>");
		ff+=("<br>");
		ff+=("height = "+height.toString()+";</html>");
		
		return ff;
		
	}
	
	public void output_to_file(String path) {
		try {
			FileWriter txtWriter = new FileWriter(path+".txt");
			txtWriter.append("g = "+g+";\n");
			txtWriter.append("m = "+mass+";\n");
			txtWriter.append("mu = "+friction+";\n");
			txtWriter.append("\n");
			txtWriter.append("vmax = "+maxV+";\n");
			txtWriter.append("tol = "+tol+";\n");
			txtWriter.append("\n");
			txtWriter.append("start = ("+start.get_x()+", "+start.get_y()+");\n");
			txtWriter.append("goal = ("+flag.get_x()+", "+flag.get_y()+");\n");
			txtWriter.append("\n");
			txtWriter.append("height = "+height.toString()+";\n");
			txtWriter.flush();
			txtWriter.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
