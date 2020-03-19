package Physics;

public class Rect3DInfo {

    Vector3d v1;
    Vector3d v2;
    Vector3d v3;
    Vector3d v4;

    // If it's true than the rectangle is water.
    boolean isWater;

    public Rect3DInfo(Vector3d v1, Vector3d v2, Vector3d v3, Vector3d v4){
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        this.v4 = v4;
    }

    public boolean getisWater(){
        return isWater;
    }

    public void setWater(boolean water){
        this.isWater = water;
    }

    public String toString(){
        String a = "";
        a+= v1.toString() + "\t" + v2.toString() + "\t" + v3.toString() + "\t" + v4.toString() + "\t";
        if (isWater){
            a += " It's water";
        }

        return a;

    }


}
