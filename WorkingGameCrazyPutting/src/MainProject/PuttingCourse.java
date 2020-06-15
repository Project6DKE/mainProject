package MainProject;

import readingOfFunctions.Function2d;
import java.io.*;

public class PuttingCourse {
	
	private Function2d height;
	private Vector2d flag,start;
	private double friction, maxV, tol, g, mass, sandFriction;
	private SandPit SandPit;
	
	public PuttingCourse(Function2d height,Vector2d flag, Vector2d start) {
		this.height = height;
		this.flag = flag;
		this.start = start;
	}
	
	public PuttingCourse(Function2d height,Vector2d flag, Vector2d start, double friction, double maxV, double tol, double g, double mass) {
		this.height = height;
		this.flag = flag;
		this.start = start;
		this.friction = friction;
		this.maxV = maxV;
		this.tol = tol;
		this.g = g;
		this.mass = mass;
	}
	
	public Function2d get_height() {
		return height;
	}
	
	public Vector2d get_flag_position() {
		return flag;
	}
	
	public Vector2d get_start_position() {
		return start;
	}
	
	public double get_friction_coefficient() {
		return friction;
	}
	
	public double get_maximum_velocity() {
		return maxV;
	}
	
	public double get_hole_tolerance() {
		return tol;
	}
	
	public double get_ball_mass() {
		return mass;
	}
	
	public double get_gravity() {
		return g;
	}

	public Vector2d calculate_acceleration(Vector2d position, Vector2d vv) {
		double Ax, Ay;
		Vector2d gradient=height.gradient(position);
		if(vv.get_scalar()==0) {
			Ax=(-g*gradient.get_x());
			Ay=(-g*gradient.get_y());
		}
		else {
			if(is_sand(position)) {
				Ax=(-g*gradient.get_x())-((sandFriction*g*vv.get_x())/vv.get_scalar());
				Ay=(-g*gradient.get_y())-((sandFriction*g*vv.get_y())/vv.get_scalar());
			}
			else {
				Ax=(-g*gradient.get_x())-((friction*g*vv.get_x())/vv.get_scalar());
				Ay=(-g*gradient.get_y())-((friction*g*vv.get_y())/vv.get_scalar());
			}
		}
		return new Vector2d(Ax,Ay);
	}
	
	public boolean is_water(Vector2d p) {
		boolean heightBelowZero = height.evaluate(p) < 0;

		if (heightBelowZero) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean is_put(Vector2d p) {
		double x=p.get_x()-flag.get_x();
		double y=p.get_y()-flag.get_y();
		if((x*x+y*y)<=(tol*tol)) return true;
		return false;
	}
	
	public boolean is_start(Vector2d p) {
		double stol=tol/10;
		double x=p.get_x()-start.get_x();double y=p.get_y()-start.get_y();
		if((x*x+y*y)<=(stol*stol)) return true;
		return false;
	}
	
	public boolean is_sand(Vector2d p) {
		return SandPit.isSand(p);
	}
	
	public boolean is_tree(Vector2d p) {
		return false;
	}
	
	public int collisionDetector(Vector2d p) {
		if(is_water(p))return 0;
		if(is_sand(p))return 1;
		if(is_tree(p))return 2;
		return -1;
	}
	
	public boolean is_traversable(Vector2d p) {
		return is_water(p);
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
