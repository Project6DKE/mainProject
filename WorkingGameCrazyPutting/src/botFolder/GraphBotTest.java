package botFolder;

import java.util.ArrayList;

import MainProject.Vector2d;


public class GraphBotTest{
	//this is for testing
	 private static ArrayList<Node> nodes = new ArrayList<>();
	 public static void main(String[] args){
		 initNetwork();
	     GraphBot pathFinder = new GraphBot(nodes.get(1), nodes.get(11));
	     pathFinder.reconstructPathToArray(pathFinder.runShortestPath());

	 }
	 public static void initNetwork(){
	     Node n00 = new Node(new Vector2d(0,0),null);
	     Node n01 = new Node(new Vector2d(0,1),null);
	     Node n02 = new Node(new Vector2d(0,2),null);
	     Node n03 = new Node(new Vector2d(0,3),null);
	     Node n04 = new Node(new Vector2d(0,4),null);
	     Node n05 = new Node(new Vector2d(0,5),null);


	     Node n10 = new Node(new Vector2d(1,0),null);
	     Node n11 = new Node(new Vector2d(1,1),null);
	     Node n12 = new Node(new Vector2d(1,2),null);
	     Node n13 = new Node(new Vector2d(1,3),null);
	     Node n14 = new Node(new Vector2d(1,4),null);
	     Node n15 = new Node(new Vector2d(1,5),null);

	     Node n22 = new Node(new Vector2d(2,2),null);
	     Node n23 = new Node(new Vector2d(2,3),null);
	     Node n24 = new Node(new Vector2d(2,4),null);
	     Node n25 = new Node(new Vector2d(2,5),null);

	     nodes.add(n00);
	     nodes.add(n01);
	     nodes.add(n02);
	     nodes.add(n03);
	     nodes.add(n04);
	     nodes.add(n05);
	     nodes.add(n10);
	     nodes.add(n11);
	     nodes.add(n12);
	     nodes.add(n13);
	     nodes.add(n14);
	     nodes.add(n15);
	     nodes.add(n22);
	     nodes.add(n23);
	     nodes.add(n24);
	     nodes.add(n25);
	     n00.connectTo(n10);
	     n00.connectTo(n01);
	     n01.connectTo(n11);
	     n10.connectTo(n11);
	     n11.connectTo(n12);

	     n12.connectTo(n02);
	     n02.connectTo(n03);
	     n03.connectTo(n04);
	     n04.connectTo(n05);
	     n05.connectTo(n15);

	     n12.connectTo(n22);
	     n22.connectTo(n23);
	     n23.connectTo(n13);
	     n13.connectTo(n14);
	     n14.connectTo(n24);
	     n24.connectTo(n25);
	     n25.connectTo(n15);
	}
}