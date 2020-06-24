package readingOfFunctions;

import java.util.*;

import MainProject.Vector2d;

public class FunctionH implements Function2d {

    String function;

    ArrayDeque<Something> themagic;

    Something root;
    
    ArrayList<FunctionH> subfunc= new ArrayList<FunctionH>();
    ArrayList<double[]> ranges = new ArrayList<double[]>();

    public FunctionH(String aFunction) throws Exception{
        this.function = aFunction.trim();
        this.understand();
    }
    
    /*
     * A set of functions used for testing. I created a constructor to make it easier and cleaner to access them.
     * In the comments I mention where the Flag and the Ball should be for the sake of the course
     */
    
    public static FunctionH createInstance(int aValue) throws Exception {
    	
    	FunctionH height;
    	switch (aValue) {
		case 1:
			/*
			 * Flag (0,4)
			 * Ball (0,0)
			 * 
			 * In this function the ball is surrounded by water, with the exit being a very thin line
			 * Used to test how the internal restart method works
			 */
			
			FunctionH func = new FunctionH("2");
			func.add_subfunct("-1", -10000, 10000, -100000, -1);
			func.add_subfunct("-1", 0.0225, 2, 1, 2);
			func.add_subfunct("-1", -2, -0.0225, 1, 2);
			func.add_subfunct("-1", 1, 2, -2, 2);
			func.add_subfunct("-1", -2, -1, -2, 2);
			return func;
		case 2:
			/*
			 * Flag (0,4)
			 * Ball (0,0)
			 * 
			 * Here the ball and water are separated by a really long strip of water
			 * Used to test the internal restart function
			 */
			height = new FunctionH("2");
			height.add_subfunct("-1", -10000, 10000, -100000, -1);
			height.add_subfunct("-1", -10, 10, 1, 2);
			return height;
		case 3:
			/*
			 * Ball (0,0)
			 * 
			 * Flags at:
			 * 	(0,4)
			 * 	(4,4)
			 * 	(4,8)
			 * 	(0,8)
			 * 	(0,12)
			 * 
			 * A short path is created between ball and flag
			 * Done to test speed for backwards vs forward solver
			 * It's a crappy maze
			 * 
			 */
			height = new FunctionH("2");
			height.add_subfunct("-1", -100, 100, -10, -1);
			
			// Left wall
			height.add_subfunct("-1", -2, -1, -1, 15);
			//Middle block
			height.add_subfunct("-1", -1, 3, 5, 7);
			//Lower right block
			height.add_subfunct("-1", 1, 5, -1, 3);
			//Top right block
			height.add_subfunct("-1", 1, 7, 9, 15);
			return height;
		case 4:
			/*
			 * Ball (10.5,1)
			 * 
			 * Flags at: 
			 * 	(4,10)
			 * 	(5, 16)
			 *  (10,20)
			 *  (17,17)
			 *  (10,7)
			 *  (13,5)
			 */
			
			height = new FunctionH("2");
			
			// 1
			height.add_subfunct("-1", 2.875, 6.125, 2.875, 3.125);
			//2
			height.add_subfunct("-1", 2.875, 3.125, 2.875, 18.125);
			// 3
			height.add_subfunct("-1", 2.875, 6.125, 8.875, 9.125);
			// 4
			height.add_subfunct("-1", 2.875, 9.125, 17.875, 18.125);
			// 5
			height.add_subfunct("-1", 2.875, 9.125, 14.875, 15.125);
			//6
			height.add_subfunct("-1", 5.875, 15.125, 11.875, 12.125);
			//7
			height.add_subfunct("-1", 11.875, 12.125, 14.875, 18.125);
			//8
			height.add_subfunct("-1", 11.875, 18.125, 17.875, 18.125);
			//9
			height.add_subfunct("-1", 17.875, 18.125, 2.875, 18.125);
			
			//10
			height.add_subfunct("-1", 11.875, 18.125, 2.875, 3.125);
			// 11
			height.add_subfunct("-1", 11.875, 12.125, 2.875, 9.125);
			
			
			//12
			height.add_subfunct("-1", 5.875, 15.125, 5.875, 6.125);
			//13
			height.add_subfunct("-1", 8.875, 9.125, 5.875, 12.125);
			// 14
			height.add_subfunct("-1", 14.875, 15.125, 8.875, 15.875);
			
			return height;
		default:
			return new FunctionH("50");
			
		
    	}
    	
    	
    	
    }
    
    
    
   
    public void understand() throws Exception{
        /*
        This is where the equation will be transformed into a search tree.
         */
    	
    	// Operator stack is a temporary stack where the operators are store
    	// outputStack is where the results are put in, organized in reverse polish notation
        ArrayDeque<Operations> operatorStack = new ArrayDeque<Operations>();
        ArrayDeque<Something> outputStack = new ArrayDeque<Something>();
        
        String[] elements = function.split(" ");

        for(int i=0; i<elements.length; i++){
            /*
             *  Long if else statements that take care of identifying the elements in the function
             */
        	if (isNumeric(elements[i])){
                outputStack.push(new Constants(elements[i]));
            } else if(elements[i].equalsIgnoreCase("x") ||
                    elements[i].equalsIgnoreCase("y") ||
                    elements[i].equalsIgnoreCase("-x") ||
                    elements[i].equalsIgnoreCase("-y")){
                outputStack.push(new Variables(elements[i]));
            } else if(elements[i].equals("(")){
                operatorStack.push(new Operations(elements[i]));
            } else if(elements[i].equals(")")){
                while(operatorStack.peek().getOperationType() != 1){
                    outputStack.push(operatorStack.pop());
                }
                // This is to remove the last left parenthesis
                operatorStack.pop();

            } else {
            	// The last part assumes that the element has to be an operator or a function
            	// And then uses the Operations creator to generate the reelvant operation
                Operations op = new Operations(elements[i]);

                if(op.isFunction){
                    operatorStack.push(op);
                } else {

                    while ((!operatorStack.isEmpty()) && (
                                (operatorStack.peek().isFunction) ||
                                        (operatorStack.peek().getPrecedence() > op.getPrecedence()) ||
                                        ((operatorStack.peek().getPrecedence() == op.getPrecedence()) && (!op.getLeftAss()))
                    ))
                    {

                        outputStack.push(operatorStack.pop());
                    }

                    operatorStack.push(op);
                }

            }

        }

        while(!operatorStack.isEmpty()){
            outputStack.push(operatorStack.pop());
        }

        this.themagic = outputStack;

        this.createTree();

    }

    public void createTree(){
        ArrayDeque<Something> solvedTree = new ArrayDeque<Something>();

        ArrayDeque<Something> invertedList = new ArrayDeque<Something>();

        while(!this.themagic.isEmpty()){
            invertedList.push(this.themagic.pop());
        }

        while(!invertedList.isEmpty()){
            if(!(invertedList.peek() instanceof Operations)){
                solvedTree.push(invertedList.pop());
            } else {
                /*
                I need an extra check in case it's a function, but the function only needs one child
                Still, it's a change that's gotta be done.
                 */
                Operations op = (Operations)invertedList.pop();

                if (op.getIsFunction()){
                    op.setLeft(solvedTree.pop());
                } else {
                    op.setRight(solvedTree.pop());
                    op.setLeft(solvedTree.pop());
                }

                solvedTree.push(op);

            }

        }

        if(solvedTree.size() != 1){
            System.out.println("error, look at later");
        }

        this.root = solvedTree.pop();

    }

    public double evaluate(double x, double y){
    	
    	FunctionH subFunc = this.find_subfunc_XY(x, y);
    	
    	Something traverse;
    	Something root;
    	
    	if (subFunc == null) {
    		traverse = this.root;
    		root = this.root;
    	} else {
    		traverse = subFunc.root;
    		root = subFunc.root;
    		
    	}
    	
        assign(x,y,traverse);

        return root.solve();
    }

    public double evaluate(Vector2d p){
        double x, y;
        x=p.get_x();
        y=p.get_y();

        return evaluate(x, y);

    }
    public Vector2d gradient(Vector2d p){
        double del=0.001;
        double initial=evaluate(p);
        double x =(evaluate((new Vector2d(p.get_x()+del,p.get_y())))-initial)/del;
        double y =(evaluate((new Vector2d(p.get_x(),p.get_y()+del)))-initial)/del;
        return new Vector2d(x,y);
    }


    /*
    I can probably modify the assign class to work better and automatically do the calculations, it wouldn't be all that difficult
     */

    public void assign(double x, double y, Something aThing){

        if(aThing instanceof Operations){
            Operations op = (Operations)(aThing);

            assign(x,y, op.left);
            assign(x,y,op.right);

        } else if (aThing instanceof Variables){
            Variables var = (Variables) aThing;


            if (var.isitX){
                var.setValue(x);
            } else {
                var.setValue(y);
            }


        }
    }

    private static boolean isNumeric(String a) {
        try{
            Double.parseDouble(a);
            return true;
        } catch (NumberFormatException e){
            return false;
        }
    }

    public String toString(){
    	return function;
    }
    
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

}