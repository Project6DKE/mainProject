package readingOfFunctions;

import java.util.ArrayList;

public class Subfunction extends FunctionH{
    ArrayList<FunctionH> subfunc= new ArrayList<FunctionH>();
    ArrayList<double[]> ranges = new ArrayList<double[]>();

    int n = 0;

    // Basic constructor, we send it the main/height function
    public Subfunction(String main_func) throws Exception{
    	super(main_func);
    }

    // Adding subfunctions, we send it the subfunction(string version) and ranges x [1(x_lower) 3(x_upper)] y [2(y_lower) 4(y_upper)]
    public void add_subfunct(String funct, double x_lower, double x_upper, double y_lower, double y_upper ) throws Exception{
        this.add_subfunct(new FunctionH(funct), x_lower, x_upper, y_lower, y_upper);
    }
    
    public void add_subfunct(FunctionH funct, double x_lower, double x_upper, double y_lower, double y_upper ) throws Exception{
        subfunc.add(funct);
        
        double[] values = new double[4];
        
        values[0] = x_lower;
        values[1] = x_upper;
        values[2] = y_lower;
        values[3] = y_upper;
        
        
        ranges.add(values);
        this.n++;
    }

    // find the right subfunction based on X and Y ranges, if none is found it will return the main/height function
    public FunctionH find_subfunc_XY(double wanted_X, double wanted_Y){
        
    	for (int i=0; i<ranges.size();i++){
        	
        	double[] rangeToUse = this.ranges.get(i);
        	
                if ((wanted_X>=rangeToUse[0]) && (wanted_X<=rangeToUse[1]) && (wanted_Y>=rangeToUse[2]) && (wanted_Y<=rangeToUse[3])) {
                	return subfunc.get(i);
                }
                
        }
        return null;
    }
    
    @Override
    public double evaluate(double x, double y) {
    	FunctionH toUse = this.find_subfunc_XY(x, y);
    	if(toUse == null) {
    		return super.evaluate(x, y);
    	}
    	
    	return toUse.evaluate(x,y);
    }

}