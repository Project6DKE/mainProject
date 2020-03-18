public class Variables implements Something {

    double currentValue;

    boolean isitX;

    boolean negative;

    public Variables(String aString){


        if(aString.charAt(0) == '-'){
            negative = true;
        }


        if (aString.contains("x")){
            this.isitX = true;
        } else {
            this.isitX = false;
        }
    }

    public void setValue(double aValue){
        if (negative){
            this.currentValue = -aValue;
        } else {
            this.currentValue = aValue;
        }
    }

    public double solve() {
        return currentValue;
    }

    public String toString(){
        String a = "";

        if (negative){
            a+= "-";
        }

        if(isitX){
            return a+"x";
        } else {
            return a+"y";
        }
    }

}
