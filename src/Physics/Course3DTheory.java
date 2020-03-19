package Physics;

public class Course3DTheory {

    FunctionH aFunction;
    Rect3DInfo[] rectangles;

    int length = 200;
    int width = 200;

    int middlel = 100;
    int middlew = 100;

    /*
    Decimal value is how close each of the points will be for a given array.
    So if it's 10 it means each point will be 0.1 apart, if it's 100 it means they'll be 0.01 apart, etc.
     */

    double decimalValue = 10;

    public Course3DTheory(String aFunction){
        this.aFunction = new FunctionH(aFunction);

        rectangles = new Rect3DInfo[(length-1)*(width-1)];

        this.generate();

    }

    public Course3DTheory(String aFunction, int l, int w){
        this.aFunction = new FunctionH(aFunction);
        this.length = l;
        this.width = w;

        this.middlel = l/2;
        this.middlew = w/2;

        rectangles = new Rect3DInfo[(length-1)*(width-1)];

        this.generate();

    }

    public void generate(){

        int list = 0;

        for(int i=0; i<width-1; i++){
            for(int j=0; j<length-1; j++){
                double x1 = (i-middlew)/decimalValue;
                double y1 = (j-middlel)/decimalValue;

                double z1 = aFunction.evaluate(x1,y1);

                double x2 = (i+1-middlew)/decimalValue;
                double y2 = (j-middlel)/decimalValue;

                double z2 = aFunction.evaluate(x2,y2);

                double x3 = (i-middlew)/decimalValue;
                double y3 = (j+1-middlel)/decimalValue;

                double z3 = aFunction.evaluate(x3,y3);

                double x4 = (i+1-middlew)/decimalValue;
                double y4 = (j+1-middlel)/decimalValue;

                double z4 = aFunction.evaluate(x4,y4);

                int watercount = 0;

                if (z1 <= 0){
                    z1 = 0;
                    watercount++;
                }
                if (z2 <= 0){
                    z2 = 0;
                    watercount++;
                }
                if(z3 <= 0){
                    z3 = 0;
                    watercount++;

                }
                if(z4 <= 0){
                    z4 = 0;
                    watercount++;

                }

                Vector3d v1 = new Vector3d(x1,y1,z1);

                Vector3d v2 = new Vector3d(x2,y2,z2);

                Vector3d v3 = new Vector3d(x3,y3,z3);

                Vector3d v4 = new Vector3d(x4,y4,z4);

                rectangles[list] = new Rect3DInfo(v1,v2,v3,v4);

                /*
                I'm just arbitrarily saying that if two or more of the points for a rectangle are zero than the rectangle is a water rectangle
                Not the best, but it's something I suppose
                 */
                if (watercount>= 2){
                    rectangles[list].setWater(true);
                }

                list++;


            }


        }


    }

/*
    public static void main(String[] args){
        Course3DTheory testCourse = new Course3DTheory("5 + x + y");

        int length = testCourse.rectangles.length;

        System.out.println(testCourse.rectangles[length-1].toString());


    }

 */



}
