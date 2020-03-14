public class CourseGenerator {


    // Not sure how important these might be, as they're not really used by the generator
    protected double startX;
    protected double startY;
    protected double goalX;
    protected double goalY;

    protected double heightX;
    protected double heightX2;
    protected double heightY;


    protected boolean defaultSize;

    /*
    I'm taking each line to be equal to 0.1 units with the square function they give
    This is the part that probably needs to be played around with most

     */
    protected static int defaultWidth = 200;
    protected static int defaultLength = 200;


    /*
    Decimal value is how close each of the points will be for a given array.
    So if it's 10 it means each point will be 0.1 apart, if it's 100 it means they'll be 0.01 apart, etc.
     */
    protected static final int decimalValue = 10;


    // It's 3D because fuck me, but the first two dimensions are the X and Y coordinates, while the third one contains the height as well as the ground info
    protected double[][][] course;

    protected int length;
    protected int width;

    boolean testing = true;

    protected double lowest = 100000;
    protected double highest = -100000;


    public CourseGenerator(String filename){

        //CourseReader reader = new CourseReader();

        com.Group6.CourseReader reader = new com.Group6.CourseReader(filename);
        this.heightX = reader.getHeightXcoeff();
        this.heightX2 = reader.getHeightX2coeff();
        this.heightY = reader.getHeightYcoeff();




        if(testing){
            heightX = -0.01;
            heightX2 = 0.003;
            heightY = 0.04;
            width = defaultWidth;
            length = defaultLength;
        }



        // Use coursereader and get the relevant info

        // If a size is specified change defaultSize to false, else it's true

    }

    public void create(){
        /*
        Something here to generate length and height of the course
         */

        course = new double[width][length][2];

        double zeroX = width/2;
        double zeroY = length/2;

        for(int i=0; i<width; i++){
            for(int j=0; j<length; j++){

                /*
                Having the decimals here is more than all for readability and to make things easier to understand, at least it helped me out.
                 */
                double decimalX = (i-zeroX)/decimalValue;
                double decimalY = (j-zeroY)/decimalValue;

                double currentheight = heightX*(decimalX) + (Math.pow(decimalX,2)*heightX2) + heightY*(decimalY);

                /*
                1= land
                This is more than all future proofing, as making it so that specific locations have a specific height would not be that difficult overall.
                 */

                course[i][j][0] = currentheight;
                course[i][j][1] = 1;

                if(testing){
                    System.out.println("For point [" + (decimalX) + " , " + (decimalY)+ "] the height is " + course[i][j][0]);

                    lowest = Math.min(course[i][j][0], lowest);
                    highest = Math.max(course[i][j][0], highest);

                }

            }

        }

        if (testing){
            System.out.println("this was the highest " + highest);
            System.out.println("This was the lowest " + lowest);


        }

    }

    /*
    public static void main(String[] args){
        CourseGenerator test = new CourseGenerator();
        test.create();


    }

     */


}
