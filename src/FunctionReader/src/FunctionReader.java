package FunctionReader.src;
import Physics.FunctionH;
import Physics.PuttingCourse;
import Physics.Vector2d;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class FunctionReader {

    private double gravity;
    private double mass;
    private double mu;
    private double ballspeed;
    private double holeDistance;
    private double startX;
    private double startY;
    private double goalX;
    private double goalY;
    private String height;
    private double ballposX;
    private double ballposY;
    private double stroke;
    private double shotnr;
    private double velocity;
    private double direction;


    public String getHeight() {
        return height;
    }

    public double getShotnr() {
        return shotnr;
    }
    public double getVelocity(){
        return velocity;
    }
    public double getDirection(){
        return direction;
    }

    public double getBallposX() {
        return ballposX;
    }

    public double getBallposY() {
        return ballposY;
    }

    public double getBallspeed() {
        return ballspeed;
    }

    public double getGoalX() {
        return goalX;
    }

    public double getGoalY() {
        return goalY;
    }

    public double getGravity() {
        return gravity;
    }

    public double getHoleDistance() {
        return holeDistance;
    }

    public double getMass() {
        return mass;
    }

    public double getMu() {
        return mu;
    }

    public double getStartX() {
        return startX;
    }

    public double getStartY() {
        return startY;
    }

    public double getStroke() {
        return stroke;
    }

    public FunctionReader(String filename) throws IOException {
        String file = filename; // change the txt name here
        Scanner in = new Scanner(System.in);
        ArrayList<String> variable = new ArrayList<>();
        ArrayList<Double> value = new ArrayList<>();

        BufferedReader br = new BufferedReader(new FileReader(file));
        String st = " ";

        while ((st = br.readLine()) != null) {
            String[] word = st.split("=");
            if (word[0].equals("height ")) {
                height = word[1];
            } else {
                variable.add(word[0]);
                value.add(Double.parseDouble(word[1]));
            }
        }

        for (int i = 0; i < variable.size(); i++) {
            if (variable.get(i).equalsIgnoreCase("g "))
                gravity = value.get(i);
            if (variable.get(i).equalsIgnoreCase("m ") || variable.get(i).equalsIgnoreCase("[m/s^2]m "))
                mass = value.get(i);
            if (variable.get(i).equalsIgnoreCase("mu "))
                mu = value.get(i);
            if (variable.get(i).equalsIgnoreCase("vmax "))
                ballspeed = value.get(i);
            if (variable.get(i).equalsIgnoreCase("tol "))
                holeDistance = value.get(i);
            if (variable.get(i).equalsIgnoreCase("startX "))
                startX = value.get(i);
            if (variable.get(i).equalsIgnoreCase("startY "))
                startY = value.get(i);
            if (variable.get(i).equalsIgnoreCase("goalX "))
                goalX = value.get(i);
            if (variable.get(i).equalsIgnoreCase("goalY "))
                goalY = value.get(i);
            if (variable.get(i).equalsIgnoreCase("ballposX "))
                ballposX = value.get(i);
            if (variable.get(i).equalsIgnoreCase("ballposY "))
                ballposY = value.get(i);
            if (variable.get(i).equalsIgnoreCase("stroke "))
                stroke = value.get(i);
            if (variable.get(i).equalsIgnoreCase("shot "))
                shotnr = value.get(i);
            if (variable.get(i).equalsIgnoreCase("velocity "))
                velocity = value.get(i);
            if (variable.get(i).equalsIgnoreCase("direction  "))
                direction = value.get(i);

        }
        PuttingCourse newCourse= new PuttingCourse(new FunctionH(),new Vector2d(startX, startY),new Vector2d(goalX, goalY),mu,ballspeed,holeDistance,gravity,mass );
    }
}
