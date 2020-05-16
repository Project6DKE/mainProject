package readingOfFunctions;

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

    boolean isFunction;
    /*
    So if it's a function it's true. If it's not it aint
    Functions are things like cos(x), sin(), things like that.
     */
    
    /*
     * Alt generator so that operations can be generated with only an int
     * More hardcoding, it's not good but it works
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

        } else if (operType.equalsIgnoreCase("cos")){
            this.operationType = 8;
            this.isFunction = true;

        } else if (operType.equalsIgnoreCase("sin")){
            this.operationType = 9;
            this.isFunction = true;
        } else if (operType.equalsIgnoreCase("tan")){
            this.operationType = 10;
            this.isFunction = true;
        } else if (operType.equalsIgnoreCase("acos")){
            this.operationType = 11;
            this.isFunction = true;
        } else if (operType.equalsIgnoreCase("asin")){
            this.operationType = 12;
            this.isFunction = true;
        } else if (operType.equalsIgnoreCase("atan")){
            this.operationType = 13;
            this.isFunction = true;
        } else if (operType.equalsIgnoreCase("abs")){
            this.operationType = 14;
            this.isFunction = true;
        } else if (operType.equalsIgnoreCase("log")){
            this.operationType = 15;
            this.isFunction = true;
        } else if (operType.equalsIgnoreCase("log10")){
            this.operationType = 16;
            this.isFunction = true;
        } else if (operType.equalsIgnoreCase("cosh")){
            this.operationType = 17;
            this.isFunction = true;
        } else if (operType.equalsIgnoreCase("sinh")){
            this.operationType = 18;
            this.isFunction = true;
        } else if (operType.equalsIgnoreCase("tanh")){
            this.operationType = 19;
            this.isFunction = true;
        } else if (operType.equalsIgnoreCase("sqrt")) {
        	this.operationType = 20;
        	this.isFunction = true;
        }


    }
    
    // Alternate generation method
    // Only uses an int, with each in up till 19 having an equivalent relevant operation
    public Operations (int operValue) {
    	this(createOperation(operValue));    	
    }

    // Creates the relevant string from an int
	static String createOperation(int operValue) {
		if (operValue == 1){
            return "(";
        } else if (operValue == 2){
            return ")";
        } else if (operValue == 3){
            return "+";
        } else if (operValue ==4){
            return "-";
        } else if (operValue==5){
            return "*";
        } else if (operValue==6){
            return "/";
        } else if (operValue==7){
            return "^";
        } else if (operValue==8){
            return "cos";
        } else if (operValue==9){
            return "sin";
        } else if (operValue==10){
            return "tan";
        } else if (operValue==11){
            return "acos";
        } else if (operValue==12){
            return "asin";
        } else if (operValue==13){
            return "atan";
        } else if (operValue==14){
            return "abs";
        } else if (operValue==15){
            return "log";
        } else if (operValue==16){
            return "log10";
        } else if (operValue==17){
            return "cosh";
        } else if (operValue==18){
            return "sinh";
        } else if (operValue==19){
            return "tanh";
        } else if (operValue==20) {
        	return "sqrt";
        }
		
		System.out.println("There's an error here");
		return null;
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
    	// I'm realizing now that this is probably kind of shit ahhhhhh
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
        } else if (this.operationType == 6) {
            return left.solve()/right.solve();
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
            return Math.log(Math.abs(left.solve()));
        } else if (this.operationType == 16){
            return Math.log10(Math.abs(left.solve()));
        } else if (this.operationType == 17){
            return Math.cosh(left.solve());
        } else if (this.operationType == 18){
            return Math.sinh(left.solve());
        } else if (this.operationType == 19) {
            return Math.tanh(left.solve());
        } else if (this.operationType == 20) {
        	return Math.sqrt(Math.abs(left.solve()));
        }

        System.out.println("Invalid function");

        return 0;
    }

    public String toString(){
        return operViso;
    }

}