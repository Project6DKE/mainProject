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
	Vector2d[] popDistToFlag = new Vector2d[popNumber];
	Vector2d bestShot;
	Vector2d initialBallPosition;
	
	final int totalTestShots;
	
	// Because the GA will be generated based off final distance to flag
	// It's a good idea to play around with how the shot should be modified relative to the distance
	// With an amplificationFactor of 2 meaning the shot will vary twice as much in X and Y values
	final double amplificationFactor = 1;
	
	// Epsilon to decide accuracy when aiming a shot towards a specific spot different than the flag
	final double epsilon = 0.04;
	
	public MergeAI(){
		super();
		this.totalTestShots = 10;
	}
	
	public MergeAI(int val) {
		super();
		this.totalTestShots = val;
	}
	
	// TODO: Add a method to check if a shot is doable or not, setting a max amount of tries
	
	@Override
	public Vector2d shot_velocity(PuttingCourse course, Vector2d ball_position) throws Exception {
		/*
		 * PlaceHolder, I'll add the method and make everything better in a sec
		 */
		
		this.initialBallPosition = ball_position;
		this.sim = new PuttingSimulator(course, new RungeKutta());
		Vector2d initShot = super.shot_velocity(course, ball_position);
		Vector2d margin = this.simulateShot(initShot);
		
		// Add an extra check here in case the original shot is already perfect
		
		
		this.generatePopulation(initShot, margin);
		
		
		
		// In best the 0 is the index of the shot
		// The 1 is the fitness of the shot
		double[] best = this.findBestCurrentShot();
		
		// Check to make sure the best fit is = 0
		while(best[1] != 0.0) {
			Vector2d newError = this.simulateShot(bestShot);
			this.generatePopulation(bestShot, newError);
			best = this.findBestCurrentShot();
			
		}
		
		
		return this.bestShot;
		
	}
	
	// It's literally just findAimedShot but checking a max of 30 times
	// TODO: Bugtest this shit
	public boolean findIfShotIsValid(PuttingCourse course, Vector2d ball_position, Vector2d ballObjective) {
		this.initialBallPosition = ball_position;
		Vector2d initShot = super.aimedShot(course, ball_position, ballObjective);
		this.sim = new PuttingSimulator(course, new RungeKutta());
		this.sim.set_ball_position(ball_position);
		boolean validShot = false;
		
		Vector2d margin = this.simulateAimedShot(initShot, ballObjective);
		
		
		this.generatePopulationAimed(initShot, margin, ballObjective);
		
		double[] best = this.findBestCurrentShot();
		
		int count = 0;
		while((best[1] > epsilon) && (count < totalTestShots))  {
			int bestLocation = (int)best[0];
			Vector2d currentError = this.popDistToFlag[bestLocation];
			this.generatePopulationAimed(bestShot, currentError, ballObjective);
			
			best = this.findBestCurrentShot();
			count++;
			
			if(best[1] < epsilon) {
				validShot = true;
				//System.out.println("Shot is valido!");
				break;
			}
			
			
		}
		
		if(best[1]<epsilon) {
			validShot = true;
			//System.out.println("Shot is valido");
		}
		
		
		return validShot;
		
		
		
	}
	
	// TODO: Bug test this shit cause it seems like it shouldn't work
	public Vector2d findAimedShot(PuttingCourse course, Vector2d ball_position, Vector2d ballObjective) {
	
		this.initialBallPosition = ball_position;
		Vector2d initShot = super.aimedShot(course, ball_position, ballObjective);
		this.sim = new PuttingSimulator(course, new RungeKutta());
		this.sim.set_ball_position(ball_position);
		
		Vector2d margin = this.simulateAimedShot(initShot, ballObjective);
		
		
		this.generatePopulationAimed(initShot, margin, ballObjective);
		
		double[] best = this.findBestCurrentShot();
		
		while(best[1] > epsilon) {
			int bestLocation = (int)best[0];
			Vector2d currentError = this.popDistToFlag[bestLocation];
			this.generatePopulationAimed(bestShot, currentError, ballObjective);
			best = this.findBestCurrentShot();
			
		}
		
		return this.bestShot;
		
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
			
			/*
			 * So there's no crossover, but it currently works by generating a shot, randomly generating a new set of shots based off it
			 * Under the idea that it will go on pinpointing towards the most accurate location as time goes on
			 */
			this.generatePopulation(this.population[(int)bestShotInfo[0]], this.popDistToFlag[(int)bestShotInfo[0]]);
			bestShotInfo = this.findBestCurrentShot();
			this.bestShot= this.population[(int)bestShotInfo[0]];
		}
		
		return this.population[(int) bestShotInfo[0]];
		
	}
	
	/*
	 * Resets the sim and changes the ball position to be = to the initial given position
	 */
	public void resetSimWithDifferentBallPosition() {
		this.sim.restart_simulation();
		this.sim.set_ball_position(initialBallPosition);
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
		this.bestShot= this.population[(int)bestShotnFit[0]];
		
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
			double randomizedY = amplificationFactor*generateRandomNumber(distToFlag.get_y()) + shot.get_y();
			
			Vector2d individual = new Vector2d(randomizedX,randomizedY);
			this.population[i] = individual;
			this.popDistToFlag[i] = this.simulateShot(individual);
			this.fitnessOfPopulation[i] = this.popDistToFlag[i].get_scalar();
			
		}
	}
	
	public void generatePopulationAimed(Vector2d shot, Vector2d margin, Vector2d objective) {
		for(int i=0; i<this.population.length;i++) {
			double randomizedX = amplificationFactor*generateRandomNumber(margin.get_x()) + shot.get_x();
			double randomizedY = amplificationFactor*generateRandomNumber(margin.get_y()) + shot.get_y();
			Vector2d individual = new Vector2d(randomizedX, randomizedY);
			
			this.population[i] = individual;
			this.popDistToFlag[i] = this.simulateAimedShot(shot, objective);
			this.fitnessOfPopulation[i] = this.popDistToFlag[i].get_scalar();
			
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
		
		this.resetSimWithDifferentBallPosition();
		return res;
	}
	
	// Aiming at something different than the flag
	public Vector2d simulateAimedShot(Vector2d shot, Vector2d objective) {
		this.sim.take_shot(shot);
		
		Vector2d res = sim.get_ball_position().getVectDist(objective);
		this.resetSimWithDifferentBallPosition();
		
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
	
	/*
	 * Generates a random value between 0-100
	 * If rvalue < chance then return true
	 * Else return false
	 */
	static boolean takeChance(double chance) {
		double num = Math.random()*100.0;
		
		if(num<chance) {
			return true;
		} else {
			return false;
		}
		
	}
	
}
