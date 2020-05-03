package MainProject;

import readingOfFunctions.Function2d;
import readingOfFunctions.FunctionH;

public class GA {
	private static int number_of_gen = 20;
    private static double middle;
    private static int size_initpopulation = 250;	
    private static final int param = 2;
    private int stroke;
    private double mutationrate = 0.01;    
    private double distancefromhole;
    private double max_fit;
    private int max_elem;
    private static double[][] initpopulation = new double[size_initpopulation][param];
    private static double[] fitness = new double[initpopulation.length];
    private static double[][] actualpopulation = new double[initpopulation.length][param];
    private double maxspeed;
    private double maxangle;
    private PuttingSimulator PS;
    private Vector2d ballpos;
    private Vector2d backup_ballpos;
    private Vector2d holepos;
    
    public GA(PuttingCourse PC){
        PS = new PuttingSimulator(PC, new RungeKutta());
        this.maxspeed = PS.getCourse().get_maximum_velocity();
        this.maxangle = 360 * Math.PI / 180;
        this.holepos = PS.getCourse().get_flag_position();
    }
    
    public void encoding(){
        for(int i = 0; i<initpopulation.length; i++){
            for(int j = 0; j<initpopulation[i].length; j++){
                if(j == 0){ //speed
                    initpopulation[i][j] = Math.random() * maxspeed;    //generate a number between 0 and the amximum allowed speed of the ball
                    //System.out.println("Speed : " + initpopulation[i][j]);
                }
                if(j == 1){ //angle
                    initpopulation[i][j] = Math.random() * maxangle;    //generate an angle between 0 and the maximum angle possible (probably 360 degrees or 2 pi)
                    //ystem.out.println("Angle : " + initpopulation[i][j]);
                }
            }
        }
    }
    
    public void setFitness(){
        middle = 0;
        max_fit = 0;
        max_elem = -1;
        System.out.println("The hole x position is at : " + holepos.get_x() + ". The hole y position is at : " + holepos.get_y());
        for(int i = 0; i < initpopulation.length; i++){
        		ballpos = PS.get_ball_position();
        		backup_ballpos = ballpos;
        		PS.take_angle_shot(initpopulation[i][0], initpopulation[i][1]);
        		double getTheX = PS.get_ball_position().get_x();
        		double getTheY = PS.get_ball_position().get_y();
        		double xshot = holepos.get_x()-getTheX;
        		double yshot = holepos.get_y()-getTheY;
                distancefromhole = Math.sqrt((Math.pow(xshot, 2) + Math.pow(yshot, 2)));
                System.out.println("Distance from hole is : " + distancefromhole);
                fitness[i] = 1/distancefromhole;
                fitness[i] = fitness[i] * 100;
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
                System.out.println("Fitness of the element is : " + fitness[i]);
                PS.set_ball_position(backup_ballpos);
        }
    }
    
    public void newgen(){
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
        double newangle = ((4 * angle1) + (6 * angle2))/10;
        return newangle;
    }

    public double crossover_speed(double speed1, double speed2) {
        double newspeed = ((4 * speed1) + (6 * speed2))/10;
        return newspeed;
    }
    
    public void mutation(){
        for(int i = 0; i < actualpopulation.length; i++){
            double mut = Math.random();
            if(mut<mutationrate){
                if(actualpopulation[i][0]>(maxspeed * 0.9)){
                    actualpopulation[i][0] = actualpopulation[i][0]*0.9;
                }
                else{
                    actualpopulation[i][0] = actualpopulation[i][0]*1.1;
                }
                if(actualpopulation[i][1]>(maxangle * 0.9)){
                    actualpopulation[i][1] = actualpopulation[i][1]*0.9;
                }
                else{
                    actualpopulation[i][1] = actualpopulation[i][1]*1.1;
                }
            }
        }
    }
    
    public int bestelement(){
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
    
    public static void main(String[] args){
    	Function2d height= new FunctionH("0");
		
		Vector2d flag = new Vector2d(0,3);
		Vector2d start = new Vector2d(0,0);
		
		double g,m,mu,vmax,tol;
		g=9.81;m=45.93/1000;mu=0.131;vmax=3;tol=0.02;
		
		PuttingCourse course = new PuttingCourse(height,flag, start, mu, vmax,tol,g,m );
        GA test = new GA(course);        
        int nbr_gen = 0;
       test.encoding();
        while(nbr_gen<number_of_gen){
        	test.setFitness();
        	test.newgen();
        	test.mutation();
            System.out.println(nbr_gen);
            nbr_gen++;
       }
       test.setFitness();
       int best = test.bestelement();
       double dist = 1/(fitness[best])*100;
       System.out.println("Best element has fitness : " + fitness[best] + " with a distance to the hole of : " + dist);    
    }
    
}

