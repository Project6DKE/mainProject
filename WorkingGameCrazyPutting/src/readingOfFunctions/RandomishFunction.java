package readingOfFunctions;

public class RandomishFunction extends FunctionH {
	
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
		boolean openParenthesis = false;
		
		boolean lastConstant = false;
		
		// Divided by 100 so that it's somewhat flat
		elements[0] = generateX() + " +";
		elements[1] = generateY();
		
		
		for(int i=2; i<randomSize-1; i++) {
			double generate = Math.random();
			
			
			
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
		double num = Math.random()/100;
		while (num == 0 ) {
			num = Math.random()/100;
		}
		
		return Double.toString(num) + " * x";
	}
	
	static String generateY() {
		double num = Math.random()/100;
		while (num == 0 ) {
			num = Math.random()/100;
		}
		
		return Double.toString(num) + " * y";
	}
	
	static String flatify(String str) {
		double num = Math.random()/100;
		while (num == 0 ) {
			num = Math.random()/100;
		}
		
		return Double.toString(num) + " * " + str;
	}
	
	public static void main(String[] args) {
		RandomishFunction ran1 = new RandomishFunction();
		RandomishFunction ran2 = new RandomishFunction();
		
	}

}
