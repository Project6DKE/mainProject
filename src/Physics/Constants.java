package Physics;

public class Constants implements Something {

    double value;

    public Constants(String aConstant){
        this.value = Double.parseDouble(aConstant);
    }

    public double solve(){
        return value;
    }

    public String toString(){
        return Double.toString(value);
    }

}