package botFolder;

import MainProject.PuttingCourse;
import MainProject.PuttingSimulator;
import MainProject.RungeKutta;
import MainProject.Vector2d;

public class MergeAI extends NewAI implements PuttingBot {
	
	PuttingSimulator sim;
	final int popNumber = 10;
	private Vector2d[] population = new Vector2d[popNumber];
	double[] fitnessOfPopulation = new double[popNumber];
	//Vector2d bestShot;
	
	// Because the GA will be generated based off final distance to flag
	// It's a good idea to play around with how the shot should be modified relative to the distance
	// With an amplificationFactor of 2 meaning the shot will vary twice as much in X and Y values
	final double amplificationFactor = 1.5;
	
	MergeAI(){
		super();
	}
	
	@Override
	public Vector2d shot_velocity(PuttingCourse course, Vector2d ball_position) {
		/*
		 * PlaceHolder, I'll add the method and make everything better in a sec
		 */
		
		this.sim = new PuttingSimulator(course, new RungeKutta());
		Vector2d initShot = super.shot_velocity(course, ball_position);
		Vector2d margin = this.simulateShot(initShot);
		
		// Add an extra check here in case the original shot is already perfect
		
		/*
		 * Method here will take care of finding a shot that is perfect
		 */
		this.generatePopulation(initShot, margin);
		
		/*
		 * It's a place holder, as it stands it just gets the best shot out of the first 10 generated shots
		 */
		double[] best = this.findBestCurrentShot();
		return this.population[(int) best[0]];
		
	}
	
	public double getFitness(Vector2d shot) {
		Vector2d result= this.simulateShot(shot);
		return result.get_scalar();
	}
	
	public Vector2d createBestShot() {
		double[] bestShotInfo = this.findBestCurrentShot();
		
		// This while loop might give problems with the scalar of best shot being = 0
		// But it requiring 0.0. Place to bugtest later
		while(bestShotInfo[1] != 0.0) {
			
			//this.generatePopulation(shot, distToFlag);
			
			
			bestShotInfo = this.findBestCurrentShot();
		}
		
		return this.population[(int) bestShotInfo[0]];
		
	}
	
	public void crossover() {
		/*
		 * I'll make half of the fittest individuals cross over
		 */
		
		int[] sortedShots = this.sortShotsByFit();
		
		for(int i=0; i<this.population.length/2;i++) {
			int indv1 = (int) Math.random()*this.popNumber/2;
			int indv2 = (int) Math.random()*this.popNumber/2;
			
		}
		
	}
	
	double[] findBestCurrentShot() {
		double[] bestShotnFit = new double[2];
		
		// First value is the location, second is the fitness
		bestShotnFit[0] = 0;
		bestShotnFit[1] = 10000.0;
		
		for(int i=0; i<fitnessOfPopulation.length;i++) {
			if (fitnessOfPopulation[i] < bestShotnFit[1]) {
				bestShotnFit[1] = fitnessOfPopulation[i];
				bestShotnFit[0] = i;
			}
		}
		
		return bestShotnFit;
		
	}
	
	// Results will be such that int[0] is the fittest result, and int[populationsize-1] is the worst
	int[] sortShotsByFit() {
		int[] sorted = new int[this.popNumber];
		
		
		// Initializing the values for the comparison
		for(int i=0; i<sorted.length;i++) {
			sorted[i]=i;
			
		}
		
		// It's bubble sort, it's shit but it's good enough for the small size
		
		for(int i=0; i<sorted.length;i++) {
			for(int j=1; j<sorted.length-i;j++) {
				
				// Idea is sound, but I feel a bug will come out from here
				// Because I'm not really sorting the original list, this means I have to do an extra comparison at various points
				// So there's weird shit here, bug test later
				if(this.fitnessOfPopulation[sorted[j-1]]>this.fitnessOfPopulation[sorted[j]]) {
					int temp = sorted[j-1];
					sorted[j-1] = sorted[j];
					sorted[j] = temp;
					
				}
				
			}
			
		}
		
		return sorted;
		
	}
	
	public void generatePopulation(Vector2d shot, Vector2d distToFlag) {
		for(int i=0; i<this.population.length; i++) {
			double randomizedX = amplificationFactor*generateRandomNumber(distToFlag.get_x()) + shot.get_x();
			double randomizedY = amplificationFactor*generateRandomNumber(distToFlag.get_y()) + shot.get_x();
			
			Vector2d individual = new Vector2d(randomizedX,randomizedY);
			this.population[i] = individual;
			this.fitnessOfPopulation[i] = this.getFitness(individual);
			
		}
	}
	
	/*
	 * Simulatoes the shot with the internal PuttingSimulator, without modifying the simulation
	 */
	public Vector2d simulateShot(Vector2d shot) {
		this.sim.take_shot(shot);
		Vector2d res;
		
		if(sim.get_put_state()) {
			res = new Vector2d(0,0);
		} else {
			res = sim.distToFlagVector();
		}
		
		sim.restart_simulation();
		return res;
	}
	
	// Generates a random double such that 
	// abs(result) <= abs(value)
	// Means this can be negative
	static double generateRandomNumber(double value) {
		value = Math.abs(value);
		boolean neg = takeChance(50.0);
		
		if (neg) {
			value = -value;
		}
		
		return Math.random()*value;
		
		
	}
	
	static boolean takeChance(double chance) {
		double num = Math.random()*100.0;
		
		if(num<chance) {
			return true;
		} else {
			return false;
		}
		
	}
	
}
