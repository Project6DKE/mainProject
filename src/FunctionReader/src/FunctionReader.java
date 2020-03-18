

import java.io.*;
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
        ArrayList<String> value = new ArrayList<>();

        BufferedReader br = new BufferedReader(new FileReader(file));
        String st = " ";

        while ((st = br.readLine()) != null) {
            String[] word = st.split("=", 2);
            if (word[0].equals("height ")) {
                height = word[1];
            } else {
                variable.add(word[0]);
                value.add(word[1]);
            }
        }

        for (int i = 0; i < variable.size(); i++) {
            if (variable.get(i).equalsIgnoreCase("g "))
                gravity = Double.parseDouble(value.get(i));
            if (variable.get(i).equalsIgnoreCase("m ") || variable.get(i).equalsIgnoreCase("[m/s^2]m "))
                mass = Double.parseDouble(value.get(i));
            if (variable.get(i).equalsIgnoreCase("mu "))
                mu = Double.parseDouble(value.get(i));
            if (variable.get(i).equalsIgnoreCase("vmax "))
                ballspeed = Double.parseDouble(value.get(i));
            if (variable.get(i).equalsIgnoreCase("tol "))
                holeDistance = Double.parseDouble(value.get(i));
            if (variable.get(i).equalsIgnoreCase("startX "))
                startX = Double.parseDouble(value.get(i));
            if (variable.get(i).equalsIgnoreCase("startY "))
                startY = Double.parseDouble(value.get(i));
            if (variable.get(i).equalsIgnoreCase("goalX "))
                goalX = Double.parseDouble(value.get(i));
            if (variable.get(i).equalsIgnoreCase("goalY "))
                goalY = Double.parseDouble(value.get(i));
            if (variable.get(i).equalsIgnoreCase("ballposX "))
                ballposX = Double.parseDouble(value.get(i));
            if (variable.get(i).equalsIgnoreCase("ballposY "))
                ballposY = Double.parseDouble(value.get(i));
            if (variable.get(i).equalsIgnoreCase("stroke "))
                stroke = Double.parseDouble(value.get(i));
            if (variable.get(i).equalsIgnoreCase("shot "))
                shotnr = Double.parseDouble(value.get(i));
            if (variable.get(i).equalsIgnoreCase("velocity "))
                velocity = Double.parseDouble(value.get(i));
            if (variable.get(i).equalsIgnoreCase("direction  "))
                direction = Double.parseDouble(value.get(i));

        }
        //PuttingCourse newCourse= new PuttingCourse(xx,xx,xx,mu,ballspeed,holeDistance,gravity,mass );
    }
}
