package readingOfFunctions;

public class RandomishFunction extends FunctionH implements Function2d {
	
	static int randomSize = 3;
	
	/*
	 * I can add in some arguments in the super in the first line
	 * Keep it in mind while doing the random generator
	 */
	public RandomishFunction() {
		this(generateRandom());
	}
	
	public RandomishFunction(String aString) {
		super(aString);
	}
	
	static String generateRandom() {
		/*
		 * I think 5 elements is a good start for a random ish function
		 * Note that each multiple must have a constant multiplying it to make the function more flat
		 * This does not count as an extra element
		 * Length is +1 because the last element is plus a value so that there's little to no water
		 * 
		 */
		
		String[] elements = new String[randomSize];
		
		// Done to make sure there's a closure of parenthesis
		int openParenthesis = 0;
		
		// Done to avoid having constants side by side
		boolean lastConstant = false;
		
		/*
		 * I'm generating these two elements to make sure there's an X and Y value
		 * Should be modified later some way, doesn't matter for now
		 */
		elements[0] = generateX() + " +";
		elements[1] = generateY();
		
		
		/*
		 * If a variable was generated, the next element has to be an operator
		 * If an operator was generated, the next element has to be a variable or a function
		 * If a function was generated, next element has to be an variable, and it must close
		 * 		
		 *  When generating a function include add an open parenthesis
		 *  IE not log x
		 *  But log ( x )
		 * 
		 * This could be redundant
		 * 	0.02 * cos ( 0.02 * x )
		 * 
		 * This is easier to do
		 * 	0.02 * sin ( x )
		 * 
		 * Make sure to not end on an operator or a function
		 * IE you cant have x + + or x + cos ( )
		 * So if the last value is a operator or a function, add a variable somewhere
		 * 
		 * If you open a function or add a parenthesis, do openparenthesis++
		 * 
		 */
		
		
		for(int i=2; i<randomSize-1; i++) {
			double generate = Math.random();
			
			
			
			if (generate <= 0.33) {
				//Generate a new operation
				
				
			} else if (generate <= 0.66) {
				//Generate a new function
				
				
			} else {
				// Generate a new variable
				// 50/50 chance of generating an x or a y
				lastConstant = true;
				
			}
			
			
		}
		
		if (openParenthesis != 0) {
			/*
			 * add whoever many parenthesis needed at end so that it's equal to 0
			 */
		}
		
		elements[randomSize-1] = "+ 5";
		
		return addSpace(elements);
	}
	
	// Exists so that I can be lazy and construct the elements individually
	// Just adds a space inbetween each element
	// Goes linearly through the elements in the array
	static String addSpace(String[] stel) {
		
		String res = "";
		
		for(int i=0; i<stel.length; i++) {
			res = res + " " + stel[i];
		}
		
		return res.trim();
	}
	
	// Done so that the generated x is flat enough
	static String generateX() {
		
		/*
		 * Also needs to be negative, currently only generates positive values
		 */
		double num = Math.random()/100;
		while (num == 0 ) {
			num = Math.random()/100;
		}
		
		if(takeChance(50)) {
			num = -num;
		}
		
		
		int power = 1;
		double chance = 2;
		
		while(true) {
			double opportunity = 100*(1.0/chance);			
			
			if ((takeChance(opportunity)) || (power > 6)) {
				
				return Double.toString(num) + " * x ^ " + power;
				
			} else {
				chance = chance *2;
				power++;
			}
			
		}
	}
	
	static String generateY() {
		
		//Also needs to be negative, currently only generates positive values
		
		double num = Math.random()/100;
		while (num == 0 ) {
			num = Math.random()/100;
		}
		
		if(takeChance(50)) {
			num = -num;
		}
		
		
		int power = 1;
		double chance = 2;
		
		while(true) {
			double opportunity = 100*(1.0/chance);
			if ((takeChance(opportunity)) || (power > 6)) {
				return Double.toString(num) + " * y ^ " + power;
				
			} else {
				chance = chance *2;
				power++;
			}
			
		}
		
	}
	
	static String flatify(String str) {
		
		//Also needs to be negative, currently only generates positive values
		double num = Math.random()/100;
		while (num == 0 ) {
			num = Math.random()/100;
		}
		
		return Double.toString(num) + " * " + str;
	}
	
	// Chance has to be 0<x<100, to represent a chance of something happening
	// Generate a Math.random() double, if the double*100 < chance
	// Then the boolean returns a true, if not it returns a false
	static boolean takeChance(double chance) {
		double num = Math.random()*100;
		
		if(num<chance) {
			return true;
		} else {
			return false;
		}
		
	}
	
	public static void main(String[] args) {
		RandomishFunction ran1 = new RandomishFunction();
		RandomishFunction ran2 = new RandomishFunction();
		
	}

}
