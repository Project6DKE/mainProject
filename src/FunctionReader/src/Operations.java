public class Operations implements Something {

    /*
    Use operation Type to properly define what operation is being used, a pain in the ass but fuck it.
     */
    int operationType;

    String operViso;

    Something left;
    Something right;

    int precedence;
    /*
    trigonometric functions (weird shit) are precedence 5

    Exponent is precedence 4
    Multiplication and division are 3
    addition and substraction are 2
    () are 1
     */

    boolean leftAss;
    /*
    If it's false, it's right associative
    If it's true, it's left associative
    As far as I'm aware, it's only the exponent that is true for this case
     */

    public Operations(String operType) {
        /*
        Something annoying to create what kind of operation type it will be
        Probably a lot of annoying if else statements.
         */

        this.operViso = operType;

        if (operType.equals("(")){
            this.operationType = 1;
            /*
            magic
             */

            this.precedence = 1;

        } else if (operType.equals(")")){
            this.operationType = 2;
            /*
            Magic
             */

            this.precedence = 1;

        } else if (operType.equals("+")){
            this.operationType = 3;
            this.precedence = 2;
            leftAss = false;

        } else if (operType.equals("*")){
            this.operationType = 5;
            this.precedence = 3;
            leftAss = false;

        } else if (operType.equals("^")){
            this.operationType = 7;
            this.precedence = 4;
            leftAss = true;


        }


    }

    public Operations(String operType, Something aLeft, Something aRight){

        this(operType);

        this.left = aLeft;
        this.right = aRight;

    }

    public int getOperationType(){
        return operationType;
    }

    public boolean getLeftAss(){
        return leftAss;
    }

    public int getPrecedence(){
        return precedence;
    }



    @Override
    public double solve() {
        return Operate(left, right);
    }

    public double Operate(Something left, Something right){

        /*
        This is where the eternally long if else loop would be used in order to process the type of operator that's being used.
         */

        return 0;
    }

    public String toString(){
        return operViso;
    }

}