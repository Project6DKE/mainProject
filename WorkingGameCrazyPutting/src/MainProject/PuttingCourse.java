package MainProject;

import readingOfFunctions.Function2d;
import java.io.*;
import java.util.ArrayList;

public class PuttingCourse {
	
	private Function2d height;
	private Vector2d flag,start;
	private double friction, maxV, tol, g, mass;
	private ArrayList<Wall> walls= new ArrayList<Wall>();
	private ArrayList<SandPit> sandPits= new ArrayList<SandPit>();
	private ArrayList<Tree> trees= new ArrayList<Tree>();
	
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
	
	public void add_tree(Tree tree) {
		trees.add(tree);
	}
	
	public ArrayList<Tree> get_trees(){
		return trees;
	}

	public void add_sandPit(SandPit sand) {
		sandPits.add(sand);
	}
	
	public ArrayList<SandPit> get_sandPits(){
		return sandPits;
	}
	
	public void add_wall(Wall wall) {
		walls.add(wall);
	}
	
	public ArrayList<Wall> get_walls(){
		return walls;
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
				Ax=(-g*gradient.get_x())-((find_sand(position).getSandFriction()*g*vv.get_x())/vv.get_scalar());
				Ay=(-g*gradient.get_y())-((find_sand(position).getSandFriction()*g*vv.get_y())/vv.get_scalar());
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
		if(this.find_sand(p)==null)return false;
		return true;
	}
	
	public boolean is_tree(Vector2d p) {
		if(this.find_tree(p)==null)return false;
		return true;
	}
	
	public boolean is_wall(Vector2d p) {
		if(this.find_wall(p)==null)return false;
		return true;
	}
	
	public int collisionDetector(Vector2d p) {
		if(is_water(p))return 0;
		if(is_sand(p))return 1;
		if(is_tree(p))return 2;
		if(is_wall(p))return 3;
		return -1;
	}
	
	public Wall find_wall(Vector2d p) {
		for(Wall w: walls) {
			if (w.inWallBounds(p)) return w;
		}
		return null;
	}
	
	public Tree find_tree(Vector2d p) {
		for(Tree t: trees) {
			if (t.inTree(p)) return t;
		}
		return null;
	}
	
	public SandPit find_sand(Vector2d p) {
		for(SandPit s: sandPits) {
			if (s.isSand(p)) return s;
		}
		return null;
	}
	
	public boolean is_traversable(Vector2d p) {
		return collisionDetector(p)==-1 || collisionDetector(p)==1;
	}
	
	public boolean stopsAtPoint(Vector2d aPoint) {
		if(calculate_acceleration(aPoint, new Vector2d(0,0)).get_scalar()<0.01)
			return true;
		else
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
