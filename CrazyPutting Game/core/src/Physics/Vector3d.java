package Physics;

public class Vector3d {

    double x,y,z;
    public Vector3d(double x, double y, double z) {this.x=x;this.y=y; this.z = z;}
    public double get_x(){return x;}
    public double get_y(){return y;}
    double get_z(){return z;}
    public void set_x(double x) {this.x=x;}
    public void set_y(double y) {this.y=y;}
    public void set_z(double z) {this.z = z;}
    public double get_scalar(){
        return Math.abs(Math.sqrt(x*x+y*y + z*z));
    }

    public String toString() {
        return "X: "+x+"\t Y: "+y+"\t Z: "+z+"\t Scalar: "+get_scalar();
    }

}
