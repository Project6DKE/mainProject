package readingOfFunctions;

import java.util.*;

import MainProject.Vector2d;

public class FunctionH implements Function2d {

    String function;

    ArrayDeque<Something> themagic;

    Something root;

    public FunctionH(String aFunction){
        this.function = aFunction.trim();
        this.understand();
    }
    
    /*
     * Exists solely so that RandomishFunction
     * Can extend this function without problems
     */
    public FunctionH(){
    	
    }
    
   
    public void understand(){
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

        Something traverse = this.root;

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

}