package MainProject;

import java.util.ArrayList;

public class Subfunction{
    ArrayList<String> subfunc= new ArrayList<String>();
    ArrayList<ArrayList<Double>> ranges = new ArrayList<ArrayList<Double>>();

    public static int n = 0;
    String mainFunc;

    // Basic constructor, we send it the main/height function
    public Subfunction(String main_func){
        mainFunc=main_func;

    }

    // Adding subfunctions, we send it the subfunction(string version) and ranges x [1(x_lower) 3(x_upper)] y [2(y_lower) 4(y_upper)]
    public void add_subfunct(String funct, double x_lower, double x_upper, double y_lower, double y_upper ){
        ArrayList<Double> test = new ArrayList<Double>();
        subfunc.add(n,funct);

        test.add(0,x_lower);
        test.add(1,x_upper);
        test.add(2,y_lower);
        test.add(3,y_upper);
        ranges.add(n,test);
        n++;
    }

    // find the right subfunction based on X and Y ranges, if none is found it will return the main/height function
    public String find_subfunc_XY( double wanted_X, double wanted_Y){
        for (int i=0; i<subfunc.size();i++){
            for (int j= 0; j<ranges.get(i).size()-1;j++)
                if (wanted_X>ranges.get(i).get(0) && wanted_X<ranges.get(i).get(1) && wanted_Y>ranges.get(i).get(2) && wanted_Y<ranges.get(i).get(3))
                    return subfunc.get(i);
        }
        return mainFunc;
    }


    // find the right subfunction based on X range, if none is found it will return the main/height function
    public String find_subfunc_X( double wanted_X){
        for (int i=0; i<subfunc.size();i++){
            if (wanted_X>ranges.get(i).get(0) && wanted_X<ranges.get(i).get(1))
                return subfunc.get(i);
        }
        return mainFunc;
    }
    // find the right subfunction based on Y range, if none is found it will return the main/height function
    public String find_subfunc_Y(double wanted_Y){
        for (int i=0; i<subfunc.size();i++){
            if (wanted_Y<ranges.get(i).get(2) && wanted_Y<ranges.get(i).get(3))
                return subfunc.get(i);
        }
        return mainFunc;
    }

}