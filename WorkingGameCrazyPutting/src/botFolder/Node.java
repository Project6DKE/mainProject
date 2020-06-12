package botFolder;

import java.util.ArrayList;

//import com.sun.javafx.geom.Vec3d;

import MainProject.Vector2d;

public class Node {
	
	private Vector2d pos;
	private ArrayList<Node> cameFrom;
	private ArrayList<Node> neighbours;
	
	public Node(Vector2d pos, ArrayList<Node> cameFrom) {
		this.pos = pos;
		this.cameFrom = cameFrom;
		this.neighbours = new ArrayList<>();
		if(cameFrom == null) {
			this.cameFrom = new ArrayList<>();
		}
	}
	
	public double distanceTo(Node nodeB) {
		return this.pos.get_distance(nodeB.getPos());
	}
	
	public void addNeighbour(Node node) {
		this.neighbours.add(node);
	}
	
	public void addToPath(Node node) {
		this.cameFrom.add(node);
		
	}
	
	public ArrayList<Node> getNeighbours() {
		return this.neighbours;
	}
	
	public Vector2d getPos() {
		return this.pos;
	}
	
	public ArrayList<Node> getPath(){
		return this.cameFrom;
	}
	
	
}
