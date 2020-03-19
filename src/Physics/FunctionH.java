package Physics;

import javax.script.*;
import java.util.*;

public class FunctionH implements Function2d {

    String function;

    ArrayDeque<Something> themagic;

    Something root;

    public FunctionH(String aFunction){
        this.function = aFunction.trim();
        this.understand();
    }

    public void understand(){
        /*
        This is where the equation will be transformed into a search tree.
         */

        ArrayDeque<Operations> operatorStack = new ArrayDeque<Operations>();
        //ArrayDeque<Something> functionStack = new ArrayDeque<Something>();
        ArrayDeque<Something> outputStack = new ArrayDeque<Something>();

        String[] elements = function.split(" ");

        for(int i=0; i<elements.length; i++){
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
            System.out.println("buggs");
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
        String res = "";
        for(Something elem : themagic){
            res += (elem.toString() + " ");
        }
        return res;

    }

    /* Testing
    public static void main(String[] args){
        aFunction test = new aFunction(" 10 * cos ( x + y ) + 5");
        System.out.println(test.evaluate(3.14,3.14));
    }
     */
}