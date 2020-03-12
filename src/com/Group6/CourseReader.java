package com.Group6;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class CourseReader {

    private static double gravity;
    private static double mass_of_ball;
    private static double mu; //coefficient of friction
    private static double vmax;
    private static double tol; //distance from hole
    private static double startX;
    private static double startY;
    private static double goalX;
    private static double goalY;
    private static double heightXcoeff;
    private static double heightX2coeff;
    private static double heightYcoeff;
    private static double ballposX;
    private static double ballposY;
    private static int stroke;
    private static boolean autosave;

    public double getMass(){

        return mass_of_ball;
    }
    public double getFriction(){

        return mu;
    }
    public double getVmax(){
        return vmax;
    }
    public double getHoleDistance(){

        return tol;
    }
    public double getStartX(){

        return startX;
    }
    public double getStartY(){

        return startY;
    }
    public double getGoalX(){

        return goalX;
    }
    public double getGoalY(){

        return goalY;
    }
    public double getHeightXcoeff(){

        return heightXcoeff;
    }
    public double getHeightX2coeff(){

        return heightX2coeff;
    }
    public double getHeightYcoeff(){

        return heightYcoeff;
    }
    public double getBallPosX(){
        return ballposX;
    }
    public double getBallPosY(){
        return ballposY;
    }
    public double getStroke(){
        return stroke;
    }
    public boolean isAutosave(){
        return autosave;
    }
    public static void main(String args[]) {
        String file = "Course1.txt"; // change the txt name here
        Scanner in = new Scanner(System.in);
        String[] word = new String[1000];
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String st = " ";
            int i = 0;
            while ((st = br.readLine()) != null) {
                word = st.split(" ");
                switch (i) {
                    case 0:
                        for (int j = 0; j < word.length; j++) {
                            if (word[j].equals("g")) {
                                gravity = Double.parseDouble(word[j + 2]);
                            }
                        }
                        break;
                    case 1:
                        for (int j = 0; j < word.length; j++) {
                            if (word[j].equals("[m/s^2]m")) {
                                mass_of_ball = Double.parseDouble(word[j + 2]);
                            }
                        }
                        break;
                    case 2:
                        for (int l = 0; l<word.length; l++) {
                            if (word[l].equals("mu")) {
                                mu = Double.parseDouble(word[l + 2]);
                            }
                        }
                        break;
                    case 4:
                        for(int j = 0; j<word.length; j++){
                            if(word[j].equals("vmax")){
                                vmax = Double.parseDouble(word[j + 2]);
                            }
                        }
                        break;
                    case 5:
                        for(int j = 0; j<word.length; j++){
                            if(word[j].equals("tol")){
                                tol = Double.parseDouble(word[j + 2]);
                            }
                        }
                        break;
                    case 7:
                        for(int j = 0; j<word.length; j++){
                            if(word[j].equals("start")){
                                startX = Double.parseDouble(word[j + 3]);
                                startY = Double.parseDouble(word[j + 5]);
                            }
                        }
                        break;
                    case 8:
                        for(int j = 0; j<word.length; j++){
                            if(word[j].equals("goal")){
                                goalX = Double.parseDouble(word[j + 3]);
                                goalY = Double.parseDouble(word[j + 5]);
                            }
                        }
                        break;
                    case 10:
                        for(int j = 0; j<word.length; j++){
                            if(word[j].equals("height")){
                                heightXcoeff = Double.parseDouble(word[j + 2]);
                                heightX2coeff = Double.parseDouble(word[j + 6]);
                                heightYcoeff = Double.parseDouble(word[j + 10]);
                            }
                        }
                        break;
                    case 12:
                        for(int j = 0; j<word.length; j++){
                            if(word[j].equals("ballpos")){
                                ballposX = Double.parseDouble(word[j + 3]);
                                ballposY = Double.parseDouble(word[j + 5]);
                            }
                        }
                        break;
                    case 13:
                        for(int j = 0; j<word.length; j++){
                            if(word[j].equals("stroke")){
                                stroke = Integer.parseInt(word[j + 2]);
                            }
                        }
                        break;
                }
                i++;
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        System.out.println(gravity);
        System.out.println(mass_of_ball);
        System.out.println(mu);
        System.out.println(vmax);
        System.out.println(tol);
        System.out.println(startX + "   " + startY);
        System.out.println(goalX + "   " + goalY);
        System.out.println(heightXcoeff + "   " + heightX2coeff + "   " + heightYcoeff);
        System.out.println(ballposX + "   " + ballposY);
        System.out.println(stroke);
        if(stroke==0){
            autosave = false;
        }
        else{
            autosave = true;
        }
    }
}