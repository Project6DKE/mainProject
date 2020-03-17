import java.util.*;

public class aFunction {

    String function;

    ArrayDeque<Something> themagic;

    public aFunction(String aFunction){
        this.function = aFunction;
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
            } else if(elements[i].equalsIgnoreCase("x") || elements[i].equalsIgnoreCase("y")){
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

                while((!operatorStack.isEmpty()) &&
                        operatorStack.peek().getPrecedence() >= op.getPrecedence()) {

                    outputStack.push(operatorStack.pop());
                }

                operatorStack.push(op);

                /*
                if (!operatorStack.isEmpty()) {

                    if ((operatorStack.peek().getPrecedence() > op.getPrecedence()) ||
                            ((operatorStack.peek().getPrecedence() == op.getPrecedence()) && op.getLeftAss())
                    ) {
                        operatorStack.push(op);
                        //outputStack.push(operatorStack.pop());
                        /*
                        SUPER FUCKING IMPORTANT
                        I NEED TO FIND A WAY TO MAKE THESE PRECEDENCE CHECKS AGAIN AND AGAIN, I'M ASSUMING I'LL FIND THE PLACE AT ONCE
                        THIS IS ACTUALLY DUMB, I HATE MYSELF

                    } else if ((operatorStack.peek().getPrecedence() == op.getPrecedence())) {
                        outputStack.push(operatorStack.pop());
                        operatorStack.push(op);
                    }
                } else {
                    operatorStack.push(op);
                }
        */
            }

            this.themagic = outputStack;

        }

        while(!operatorStack.isEmpty()){
            outputStack.push(operatorStack.pop());
        }

        this.themagic = outputStack;

        this.toString();

    }



    public void evaluate(double x, double y){

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

    // Testing
    public static void main(String[] args){

        aFunction test = new aFunction("x ^ 2 + x + 10");
        test.understand();

        System.out.println(test.toString());


    }




}