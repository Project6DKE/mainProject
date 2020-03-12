import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class CourseReader {
    
    private static double gravity;
    private static double mass_of_ball;
    private static double mu; //coefficient of friction
    private static double vmax; //Maximum initial ball speed
    private static double tol; //distance from hole
    private static double startX;
    private static double startY;
    private static double goalX;
    private static double goalY;
    private static double heightXcoeff;
    private static double heightX2coeff;
    private static double heightYcoeff;

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
                    case 1:
                        for (int j = 0; j < word.length; j++) {
                            if (word[j].equals("m")) {
                                mass_of_ball = Double.parseDouble(word[j + 2]);
                            }
                        }

                    case 2:
                        for (int j = 0; j < word.length; j++) {
                            if (word[j].equals("mu")) {
                                mu = Double.parseDouble(word[j + 2]);
                            }
                        }

                    case 3:
                        for (int j = 0; j < word.length; j++) {
                            if (word[j].equals("vmax")) {
                                vmax = Double.parseDouble(word[j + 2]);
                            }
                        }
                    case 4:
                        for (int j = 0; j < word.length; j++) {
                            if (word[j].equals("tol")) {
                                tol = Double.parseDouble(word[j + 2]);
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
    }

}