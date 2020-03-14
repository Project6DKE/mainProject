package com.Group6;

import java.io.*;
import java.util.Scanner;

public class CourseReaderTwo {

    private static int shotNr;
    private static double velocity; // m/s
    private static double direction;// degrees

    public double getShotNr(){

        return shotNr;
    }
    public double getVelocity(){

        return velocity;
    }
    public double getDirection(){

        return direction;
    }
    public static void main(String args[]) {

        String file = "Speed_velocity1.txt"; // change the txt name here
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
                            if (word[j].equals("Shot")) {
                                shotNr = Integer.parseInt(word[j+1]);
                            }
                        }
                        break;
                    case 1:
                        for (int j = 0; j < word.length; j++) {
                            if (word[j].equals("Velocity")) {
                                velocity = Double.parseDouble(word[j + 2]);
                            }
                        }
                        break;
                    case 2:
                        for (int l = 0; l<word.length; l++) {
                            if (word[l].equals("Direction")) {
                                direction = Double.parseDouble(word[l + 2]);
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
        System.out.println(shotNr);
        System.out.println(velocity);
        System.out.println(direction);

    }
}