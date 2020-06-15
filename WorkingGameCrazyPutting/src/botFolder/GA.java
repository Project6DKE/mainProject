package botFolder;

import readingOfFunctions.*;
import MainProject.PuttingCourse;
import MainProject.PuttingSimulator;
import MainProject.RungeKutta;
import MainProject.Vector2d;

public class GA implements PuttingBot{
	private static int number_of_gen = 150;
    private static double middle;
    private static int size_initpopulation = 200;	
    private static final int param = 2;
    private double mutationrate = 0.1;    
    private double distancefromhole;
    private double max_fit; 
    private int max_elem;
    private static double[][] initpopulation = new double[size_initpopulation][param];
    private static double[] fitness = new double[initpopulation.length];
    private static double[][] actualpopulation = new double[initpopulation.length][param];
    private double maxspeed;
    private double maxangle;
    private double distScore = 0.2;
    private PuttingSimulator PS;
    private Vector2d ballpos;
    private Vector2d backup_ballpos;
    private Vector2d holepos;
    private static double dist;
    
    public GA(PuttingCourse PC){
        PS = new PuttingSimulator(PC, new RungeKutta());
        this.maxspeed = PS.getCourse().get_maximum_velocity();
        this.maxangle = 360 * Math.PI / 180;
        this.holepos = PS.getCourse().get_flag_position();
    }
    public GA(PuttingSimulator PS){
        this.PS = PS;
        this.maxspeed = PS.getCourse().get_maximum_velocity();
        this.maxangle = 360 * Math.PI / 180;
        this.holepos = PS.getCourse().get_flag_position();
    }
    public GA() {
    	
    }
    
    
    public void encoding(){
        for(int i = 0; i<initpopulation.length; i++){
            for(int j = 0; j<initpopulation[i].length; j++){
                if(j == 0){ //speed
                    initpopulation[i][j] = Math.random() * maxspeed;    //generate a number between 0 and the maximum allowed speed of the ball
                    //System.out.println("Speed : " + initpopulation[i][j]);
                }
                if(j == 1){ //angle
                    initpopulation[i][j] = Math.random() * maxangle;    //generate an angle between 0 and the maximum angle possible (probably 360 degrees or 2 pi)
                    //ystem.out.println("Angle : " + initpopulation[i][j]);
                }
            }
        }
    }
    
    public int setFitness(){
        middle = 0;
        max_fit = 0;
        max_elem = -1;
        //System.out.println("The hole x position is at : " + holepos.get_x() + ". The hole y position is at : " + holepos.get_y());
        for(int i = 0; i < initpopulation.length; i++){
        		System.out.println("Take a new shot");
        		ballpos = PS.get_ball_position();
        		backup_ballpos = ballpos;
        		PS.take_angle_shot(initpopulation[i][0], initpopulation[i][1]);
        		double getTheX = PS.get_ball_position().get_x();
        		double getTheY = PS.get_ball_position().get_y();
        		double xshot = holepos.get_x()-getTheX;
        		double yshot = holepos.get_y()-getTheY;
                distancefromhole = Math.sqrt((Math.pow(xshot, 2) + Math.pow(yshot, 2)));
                //System.out.println("Distance from hole is : " + distancefromhole);
                fitness[i] = 1/distancefromhole;
                fitness[i] = fitness[i] * 100;
                if(distancefromhole <= distScore) {
                	return i;
                }
                if (i == 0) {
                	middle = fitness[i];
                	}
                else {
                	middle = (middle + fitness[i])/2;
                }
                if ( max_fit < fitness [i]) {
                	max_fit = fitness[i];
                	max_elem = i;
                }
                //System.out.println("Fitness of the element is : " + fitness[i]);
                PS.set_ball_position(backup_ballpos);
                PS.setShot(0);
                System.out.println("End of the shot");
        }
        return -1;
    }
    
    public void newGen(){
        int n = 0;
        int chosen1;
        while (n<initpopulation.length || n<actualpopulation.length){
        	if(n==0) {
        		chosen1 = max_elem;
        	}
        	else {
        		chosen1 = (int) (Math.random() * fitness.length);
        	}
            int chosen2 = (int) (Math.random() * fitness.length);
            if(fitness[chosen1]>=middle && fitness[chosen2]>=middle){
                actualpopulation[n][0] = crossover_speed(initpopulation[chosen1][0],initpopulation[chosen2][0]);
                actualpopulation[n][1] = crossover_angle(initpopulation[chosen1][1],initpopulation[chosen2][1]);
                n++;
            }
        }
        
        //convert again to the initpopulation
        for(int i = 0; i<actualpopulation.length ||  i < initpopulation.length; i++){
            for(int j = 0 ; j<actualpopulation[i].length || j < initpopulation[i].length; j++){
                initpopulation[i][j] = actualpopulation[i][j];
            }
        }
    }
    
    public double crossover_angle(double angle1, double angle2){
        double newangle = ((6 * angle1) + (4 * angle2))/10;
        return newangle;
    }

    public double crossover_speed(double speed1, double speed2) {
        double newspeed = ((6 * speed1) + (4 * speed2))/10;
        return newspeed;
    }
    
    public void mutation(){
        for(int i = 0; i < actualpopulation.length; i++){
            double mut = Math.random();
            if(mut<mutationrate){
                if(initpopulation[i][0]>(maxspeed * 0.9)){
                    initpopulation[i][0] = initpopulation[i][0]*0.9;
                }
                else{
                    initpopulation[i][0] = initpopulation[i][0]*1.1;
                }
                if(initpopulation[i][1]>(maxangle * 0.9)){
                    initpopulation[i][1] = initpopulation[i][1]*0.9;
                }
                else{
                    initpopulation[i][1] = initpopulation[i][1]*1.1;
                }
            }
        }
    }
    
    public int bestElement(){
        int i = -1;
        double max_fit = -1;
        for(int j = 0; j<fitness.length; j++){
            if(fitness[j] > max_fit){
                max_fit = fitness[j];
                i = j;
            }
        }
            return i;
        
    }
    
    public double[] runGA(Vector2d ball_position) {
    	encoding();
    	int nbr_gen = 0;
    	int check = -1;
    	int best = 0;
    	boolean flag  = true;
    	while(nbr_gen<number_of_gen) {
    		System.out.println("NUMBER OF GEN + " + nbr_gen);
    		check = setFitness();
    		if(check != -1){
    			best = check;
    			flag = false;
    			//break;
    		}
    		if(flag){
    			newGen();
        		mutation();
        		nbr_gen++;
    		}
    		else {
    			//System.out.println("WE BREAK");
    			break;
    		}
    	}
    	System.out.println("Here");
    	//setFitness();
    	if(flag) {
    		best = bestElement();
    	}
    	dist = 1/(fitness[best])*100;
    	System.out.println("Best element has fitness : " + fitness[best] + " with a distance to the hole of : " + dist);   
    	double gaShot[] = new double[2];	//gaShot[0] is speed, gaShot[1] is angle
    	gaShot[0] = initpopulation[best][0];
    	gaShot[1] = initpopulation[best][1];
    	return gaShot;
    	
    }
    
    public static double getDistance() {
    	return dist;
    }
    
    public Vector2d shot_velocity(PuttingCourse course, Vector2d ball_position) {
    	this.maxspeed = course.get_maximum_velocity();
        this.maxangle = 360 * Math.PI / 180;
        this.holepos = course.get_flag_position();
        this.PS = new PuttingSimulator(course, new RungeKutta());
    	double[] myShot = new double[2]; 
    	myShot = runGA(ball_position);
    	double speed = myShot[0];
    	double angle = myShot[1];
    	double x = speed*Math.cos(angle);
		double y = speed*Math.sin(angle);
		Vector2d result = new Vector2d(x,y);
    	return result;
    	
    }
    
    public static void main(String[] args) throws Exception{
    	Function2d height= new FunctionH("0");
    	//Function2d height= new FunctionH(" 0.04 * x ^ 2 + 0.001 * y");
    	//Function2d height= new FunctionH(" -0.01 * x + 0.003 * x ^ 2 + 0.04 * y");
    	//Function2d height = new FunctionH("-0.01 * x + 0.003 * x ^ 2 + 0.04 * y + 1");
    	//Function2d height = new FunctionH("( 7 * x * y ) / ( exp ( x ^ 2 + y ^ 2 ) )");
		Vector2d flag = new Vector2d(0,3);
		Vector2d start = new Vector2d(0,0);
		
		double g,m,mu,vmax,tol;
		g=9.81;m=45.93/1000;mu=0.131;vmax=3;tol=0.2;
		
		PuttingCourse course = new PuttingCourse(height,flag, start, mu, vmax,tol,g,m );
		PuttingSimulator putSim = new PuttingSimulator(course, new RungeKutta());
        GA test = new GA(putSim); 
        test.runGA(putSim.get_ball_position());
//        int nbr_max = 60;
//    	int temp = 0;
//    	double distAvg = 0;
//    	long startT = System.currentTimeMillis();   	 
//    	while(temp<nbr_max) {
//    		test.runGA(putSim.get_ball_position());
//    		if(temp == 0) {
//    			distAvg += getDistance();
//    		}
//    		else {
//    			distAvg = (getDistance() + distAvg) /2;
//    		}
//    		temp++;
//    	}
//    	long endT = System.currentTimeMillis();
//    	System.out.println("Algorithm ran for " + ((endT - startT) / 1000.) + " seconds ");
//    	System.out.println("So ran for " + (((endT - startT) / 1000.) / nbr_max) + " seconds per shots  ");
//    	System.out.println("Dist average is of : " + distAvg + " after " + nbr_max + " shots");
//		GA test = new GA();
//		test.shot_velocity(course, start);
//        int nbr_gen = 0;
//       test.encoding();
//        while(nbr_gen<number_of_gen){
//        	test.setFitness();
//        	test.newGen();
//        	test.mutation();
//            System.out.println(nbr_gen);
//            nbr_gen++;
//       }
//       test.setFitness();
//       int best = test.bestElement();
//       double dist = 1/(fitness[best])*100;
//       System.out.println("Best element has fitness : " + fitness[best] + " with a distance to the hole of : " + dist);    
        System.out.println("Over");
    }
    
    public String toString() {
    	String name = "Genetic Algorithm";
    	return name;
    }
    
}

