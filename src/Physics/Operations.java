package Physics;

public class Operations implements Something {

    /*
    GUIDE FOR WHOEVER HAS TO FINISH HARDCODING THIS:
    -Information about things that are functions:
        -If it's a function only assign it a left, don't assign both
        -Functions start from operation type 7 onwards
        -Operation type is more than all used to categorize the operations, just add go in increasing order for them
        -For functions precedence and leftass don't matter

    -Operations (AKA the Operate function):
        -These should just do whatever the operation does
        -So the + operation will sum the left and right, etc.
        -FOR THE FUNCTIONS ONLY OPERATE THEM ON THE LEFT SOMETHING
        -So f(x) = f(solve.left) <-- Visualize it like that if it makes it easier for you
        -Cos is done, that should work as an example of what's expected for functions
        -DOING IT WITH solve.right WILL BREAK EVERYTHING. DON'T
        -https://docs.oracle.com/javase/8/docs/api/java/lang/Math.html
        -For the trigonometric it should work relatively easy, there might be some weirder implementation for some of the other functions (or we might need to specify something)
            -For example, for absolute value doing it with the |x| is a pain, so just do Abs ( x ), or something like that
            -For these weird guys make sure to add how you're reading them into the readme file included.

     */



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

    boolean isFunction;
    /*
    So if it's a function it's true. If it's not it aint
    Functions are things like cos(x), sin(), things like that.

     */


    public Operations(String operType) {
        /*
        Something annoying to create what kind of operation type it will be
        Probably a lot of annoying if else statements.
         */

        this.operViso = operType;

        if (operType.equals("(")){
            this.operationType = 1;
            this.leftAss = false;
            this.precedence = 1;
            this.isFunction = false;

        } else if (operType.equals(")")){
            this.operationType = 2;
            this.precedence = 1;
            this.isFunction = false;
            this.leftAss = false;

        } else if (operType.equals("+")){
            this.operationType = 3;
            this.precedence = 2;
            this.leftAss = false;
            this.isFunction = false;

        } else if (operType.equals("-")){
            this.operationType = 4;
            this.precedence = 2;
            this.leftAss = false;
            this.isFunction = false;


        } else if (operType.equals("*")){
            this.operationType = 5;
            this.precedence = 3;
            this.leftAss = false;
            this.isFunction = false;

        } else if (operType.equals("/")){
            this.operationType = 6;
            this.precedence = 3;
            this.leftAss = false;

        } else if (operType.equals("^")){
            this.operationType = 7;
            this.precedence = 4;
            this.leftAss = true;
            this.isFunction = false;

        } else if (operType.equals("cos")){
            this.operationType = 8;
            this.isFunction = true;

        }


    }

    public boolean getIsFunction(){
        return isFunction;
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

    public void setLeft(Something aThing){
        this.left = aThing;
    }

    public void setRight(Something aThing){
        this.right = aThing;
    }


    public double solve() {
        return Operate(left, right);
    }


    public double Operate(Something left, Something right){

        /*
        This is where the eternally long if else loop would be used in order to process the type of operator that's being used.
         */

        /*
        The gist of operate is that it's going to do the relevant operation for what's on the left and what's on the right
         */
        if (this.operationType == 3){
            return left.solve() + right.solve();
        } else if (this.operationType == 4){
            return left.solve() - right.solve();
        } else if (this.operationType == 5){
            return left.solve()*right.solve();
        } else if (this.operationType == 7){
            return Math.pow(left.solve(), right.solve());
        } else if (this.operationType == 8){
            return Math.cos(left.solve());
        }

        return 0;
    }

    public String toString(){
        return operViso;
    }
    
    public void createSubstrings(String operType) {
        leftOperString = operViso.substring(0, operationAtIndex);
        rightOperString = operViso.substring(operationAtIndex + 1);
    }
    
    
    public int firstOccurrence(String operType) {
        // Later on we should replace cos, sin, tan...
        // With a distinct letter with the replace function

        // For cos and sin make sure opertype only contains one letter


        char operTypeChar = operType.charAt(0);
        if (operType == "end") {
            return -1;
        }

        for (int i = 0; i < operViso.length(); i++) {
            if (operViso.charAt(i) == operTypeChar) {
                return i;
            }
        }

        return -1;
    }

    public String findOperType() {
        //The operType array needs to contain all the possible operation types

        String[] operTypeArray = {"^", "end"};

        int i = 0;
        while (firstOccurrence(operTypeArray[i]) == -1) {
            i++;
        }

        operationAtIndex = firstOccurrence(operTypeArray[i]);
        return operTypeArray[i];
    }

}
