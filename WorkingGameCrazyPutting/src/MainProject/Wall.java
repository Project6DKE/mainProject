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
		this.start = start;
		this.end = end;
	}

	public Vector2d getMidpoint() {
		double midpointX = (start.x + end.x) / 2;
		double midpointY = (start.y + end.y) / 2;

		return new Vector2d(midpointX, midpointY);
	}
	
	public int reflectSide(Vector2d pos1, Vector2d pos2) {
		double x1,y1,x2,y2,sx,sy,ex,ey;
		x1=pos1.get_x();
		y1=pos1.get_y();
		
		x2=pos2.get_x();
		y2=pos2.get_y();

		sx=start.get_x();
		sy=start.get_y();

		ex=end.get_x();
		ey=end.get_y();
		
		if(x2<x1) {
			if(x2<sx && sx<x1)return 1;
		}
		else {
			if(x1<ex && ex<x2)return 1;
		}
		
		if(y2<y1) {
			if(y1<sy && sy<y2)return 0;
		}
		else {
			if(y2<ey && ey<y1)return 0;
		}
		
		return -1;
	}

	public double getWidth() {
		return start.get_distance(end);
	}

	public double getRotationInDegrees() {
		double differenceX = start.x - end.x;
		double differenceY = start.y - end.y;

		return Math.atan2(differenceY, differenceX) * 90 / Math.PI;
	}

	public Vector2d getStart() {
		return start;
	}
	
	public Vector2d getEnd() {
		return end;
	}

	public void setStart(Vector2d start) {
		this.start = start;
	}
	
	public void setEnd(Vector2d end) {
		this.end = end;
		}

	public boolean inWallBounds(Vector2d p) {
		double x,y;
		x = p.get_x();
		y = p.get_y();
		
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
