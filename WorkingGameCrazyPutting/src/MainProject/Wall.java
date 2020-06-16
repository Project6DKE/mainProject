package MainProject;

import java.util.ArrayList;
import java.util.List;

public class Wall {
	private Vector2d start, end;
	
	/* 
	 * @param start	lower left corner of the wall
	 * @param end 	upper right corner of the wall
	 */
	public Wall(Vector2d start, Vector2d end) {
		this.start=start;
		this.end=end;
	}
	
	public Vector2d getStart() {
		return start;
	}
	
	public Vector2d getEnd() {
		return end;
	}

	public void setStart(Vector2d start) {
		this.start=start;
	}
	
	public void setEnd(Vector2d end) {
		this.end=end;
		}

	public boolean inWallBounds(Vector2d p) {
		double x,y;
		x=p.get_x();
		y=p.get_y();
		
		if(x>=start.get_x() && x<=end.get_x()) {
			if(y>=start.get_y() && y<=end.get_y()) return true;
			else if(y<=start.get_y() && y>=end.get_y()) return true;
		}
		else if(x<=start.get_x() && x>=end.get_x()) {
			if(y>=start.get_y() && y<=end.get_y()) return true;
			else if(y<=start.get_y() && y>=end.get_y()) return true;			
		}
		
		return false;
	}

	public List<Vector2d> getBounds(){
		ArrayList<Vector2d> bounds= new ArrayList<Vector2d>();
		bounds.add(start);
		bounds.add(end);
		return bounds;
	}
	
}
