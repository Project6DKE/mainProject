package Physics

import jdk.dynalink.Operation;

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
    int operationAtIndex;

    String operViso;


    Something left;
    Something right;

    int precedence;
    /*
    deg and rad have precendence 6
    
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


    String leftOperString;
    String rightOperString;



    public Operations(String operType) {
        /*
        Something annoying to create what kind of operation type it will be
        Probably a lot of annoying if else statements.
         */


        // Find the first ^ and split
            //Assign left and right

        operViso = operType;
        format();
        operType = findOperType();
        createSubstrings(operType);


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

        } else if (operType.equals("c")){ // c =  cos
            this.operationType = 8;
            this.precedence = 5;
            this.isFunction = true;

        } else if (operType.equals("s")){ // s =  sin
            this.operationType = 9;
            this.precedence = 5;
            this.isFunction = true;

        } else if (operType.equals("t")){ // t =  tan
            this.operationType = 10;
            this.precedence = 5;
            this.isFunction = true;
        } else if (operType.equals("C")){ // C =  acos
            this.operationType = 11;
            this.precedence = 5;
            this.isFunction = true;

        } else if (operType.equals("S")){ // S =  asin
            this.operationType = 12;
            this.precedence = 5;
            this.isFunction = true;

        } else if (operType.equals("T")){ // T =  atan
            this.operationType = 13;
            this.precedence = 5;
            this.isFunction = true;
        } else if (operType.equals("a")){ // a =  absolute value
            this.operationType = 14;
            this.precedence = 5;
            this.isFunction = true; // I'm not sure about this
        } else if (operType.equals("l")){ // l = log
            this.operationType = 15;
            this.precedence = 5;
            this.isFunction = true;
        } else if (operType.equals("L")){ // L = log10
            this.operationType = 16;
            this.precedence = 5;
            this.isFunction = true;
        } else if (operType.equals("r")){ // r = square root
            this.operationType = 17;
            this.precedence = 4;
            this.isFunction = true;
        } else if (operType.equals("R")){ // R = cube root
            this.operationType = 18;
            this.precedence = 4;
            this.isFunction = true;
        } else if (operType.equals("d")){ // d = degrees
            this.operationType = 19;
            this.precedence = 6;
            this.isFunction = true;
        } else if (operType.equals("D")){ // D = convert to radians
            this.operationType = 20;
            this.precedence = 6;
            this.isFunction = true;
        }




        operate(this.left, this.right);


    }

    public boolean getIsFunction(){
        return isFunction;
    }

    public Operations(String operType, Something aLeft, Something aRight){
        this(operType);

        this.left = aLeft;
        this.right = aRight;

        operate(this.left, this.right);
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

    public void setLeft(E aThing){
        // Still don't know how to connect this
        this.left = (Something) aThing;
    }

    public void setRight(E aThing){
        // Still don't know how to connect this
        this.right = (Something) aThing;
    }


    public double solve() {
        return operate(left, right);
    }


    public double operate(Something left, Something right){

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
            return left.solve() * right.solve();
        } else if (this.operationType == 7){
            return Math.pow(left.solve(), right.solve());
        } else if (this.operationType == 8){
            return Math.cos(left.solve());
        } else if (this.operationType == 9){
            return Math.sin(left.solve());
        } else if (this.operationType == 10){
            return Math.tan(left.solve());
        } else if (this.operationType == 11){
            return Math.acos(left.solve());
        } else if (this.operationType == 12){
            return Math.asin(left.solve());
        } else if (this.operationType == 13){
            return Math.atan(left.solve());
        } else if (this.operationType == 14){
            return Math.abs(left.solve());
        } else if (this.operationType == 15){
            return Math.log(left.solve());
        } else if (this.operationType == 16){
            return Math.log10(left.solve());
        } else if (this.operationType == 17){
            return Math.sqrt(left.solve());
        } else if (this.operationType == 18){
            return Math.cbrt(left.solve());
        } else if (this.operationType == 19){
            return Math.toDegrees(left.solve());
        } else if (this.operationType == 20){
            return Math.toRadians(left.solve());
        }

        // Maybe it should return 1 instead
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
        if (operType == "other_type") {
            return -3;
        }

        // There may be a more efficient way

        for (int i = 0; i < operViso.length(); i++) {
            if (operViso.charAt(i) == operTypeChar) {
                return i;
            }
        }

        return -1;
    }

    public String findOperType() {
        //The operType array needs to contain all the possible operation type
        // The sequence of the operTypeArray is also the sequence
        // In which the operations are identified
        // We can use this in our advantage

        // I replaced cos, sin, tan, with a single but distinct letter
        // To simplify the search

        String[] operTypeArray = {"d", "D", "a", "C", "S", "T", "c", "s", "t", "l", "L", "^", "r", "R", "*", "/", "+", "-", "other_type"};

        int i = 0;
        while (firstOccurrence(operTypeArray[i]) == -1) {
            i++;
        }

        operationAtIndex = firstOccurrence(operTypeArray[i]);
        return operTypeArray[i];
    }

    public void format() {
        operViso.toLowerCase();
        operViso.replaceAll("abs", "a"); //Absolute value

        operViso.replaceAll("cos", "c");
        operViso.replaceAll("sin", "s");
        operViso.replaceAll("tan", "t");

        operViso.replaceAll("acos", "C");
        operViso.replaceAll("asin", "S");
        operViso.replaceAll("atan", "T");
        operViso.replaceAll("pi", "3.1415926536");
        operViso.replaceAll("e", "2.7182818285");
    }

}
