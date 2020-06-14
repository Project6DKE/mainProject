package botFolder;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

public class GraphBot {
    //Instantiate the needed variables
    private ArrayList<Node> closedSet = new ArrayList<>();
    private HashMap<Node, Double> gScore = new HashMap<>();
    private HashMap<Node, Double> fScore = new HashMap<>();
    private HashMap<Node, Node> cameFrom = new HashMap<>();
    private final int initAmount = 200;
    private Node goal;
    private Node start;
    private PriorityQueue<Node> openSet;

    public GraphBot(Node start, Node goal) {
        this.start = start;
        this.goal = goal;
        Comparator<Node> comparator = new Comparator<Node>() {
            @Override
            public int compare(Node node1, Node node2) {
                if (node1.distanceTo(goal) > node2.distanceTo(node2)) {
                    return -1;
                } else if (node1 == node2) {
                    return 0;
                } else {
                    return 1;
                }
            }
        };
        openSet = new PriorityQueue<>(initAmount,comparator);
        openSet.add(start);
        fScore.put(start, heuristicEstimate(start, goal));
        gScore.put(start, start.distanceTo(start));
    }

    public String getShortestPath() {
        while (openSet.isEmpty() != true) {
            Node pointer = openSet.poll();


            if (pointer == goal) {
            	System.out.println("GOAL");
            	System.out.println("The pointer to string : " + pointer.toString());
                return reconstruct_path(pointer);
            }

            closedSet.add(pointer);
            ArrayList<Node> neighbours = pointer.getNeighbours();
            for (Node neighbour : neighbours) {
                if (closedSet.contains(neighbour)) {
                    continue;
                } else {
                    openSet.offer(neighbour);
                }
                gScore.put(neighbour, start.distanceTo(neighbour));
                double optionalDST = gScore.get(pointer) + pointer.getLocation().get_distance(neighbour.getLocation());
                cameFrom.put(neighbour,pointer);
                gScore.put(neighbour, optionalDST);
                fScore.put(neighbour, gScore.get(neighbour) + neighbour.distanceTo(goal));

            }
        }
        return null;
    }

    public String reconstruct_path(Node pointer) {
       String path = new String();
       path.concat(pointer.toString());
       while(cameFrom.containsKey(pointer)){
    	   System.out.println(pointer.toString());
           pointer = cameFrom.get(pointer);
            path.concat(pointer.toString());
            System.out.println("path is : " + path);
        }
        return path;
    }



   //Linear distance heuristic
    public double heuristicEstimate(Node from, Node to) {
        return from.distanceTo(to);
    }

}