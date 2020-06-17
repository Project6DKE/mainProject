package botFolder;

import java.util.ArrayList;

import MainProject.Vector2d;

public class Node {

    private Vector2d location;
    private ArrayList<Node> cameFrom;
    private ArrayList<Node> neighbours;

    public Node(Vector2d location, ArrayList<Node> cameFrom){
        this.location = location;
        this.cameFrom = cameFrom;
        this.neighbours = new ArrayList<>();
        if(cameFrom == null)
        {
            this.cameFrom = new ArrayList<>();
        }
    }

    public double distanceTo(Node to){
        return this.location.get_distance(to.location);
    }

    public void addNeighbour(Node n) {
    	this.neighbours.add(n);
    }
    
    public static void addNeighbour(Node n, Node m){
        n.neighbours.add(m);
        //m.neighbours.add(n);
    }

    public ArrayList<Node>getNeighbours(){
        return this.neighbours;
    }

    public Vector2d getLocation() {
        return location;
    }

    public void addPrevious(Node n){
        cameFrom.add(n);
    }

    public ArrayList<Node> getCameFrom() {
        return cameFrom;
    }
    
    public String toString() {
    	return location.toString();
    }
}