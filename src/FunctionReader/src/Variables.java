public class Variables implements Something {

    double currentValue;

    boolean isitX;

    public Variables(String aString){
        if (aString.equalsIgnoreCase("x")){
            this.isitX = true;
        } else {
            this.isitX = false;
        }
    }

    public void setValue(double aValue){
        this.currentValue = aValue;
    }

    public double solve() {
        return currentValue;
    }

    public String toString(){
        if(isitX){
            return "x";
        } else {
            return "y";
        }
    }

}
