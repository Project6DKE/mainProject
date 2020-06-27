package readingOfFunctions;

import java.util.ArrayList;
import java.util.List;

public class LessRandomFunctions extends FunctionH {

	static int randomSize = 5;
	
	public LessRandomFunctions(String aFunction) throws Exception {
		super(aFunction);
	}
	
	public LessRandomFunctions() throws Exception {
		this(generateRandom());
	}
	
	public LessRandomFunctions(int size) throws Exception{
		this(generateRandom(size));
	}
	
	static String generateRandom(int size) {
		randomSize = size;
		return (generateRandom());
	}
	
	static String generateRandom() {
		/*
		 * I think 5 elements is a good start for a random ish function
		 * Note that each multiple must have a constant multiplying it to make the function more flat
		 * This does not count as an extra element
		 * Length is +1 because the last element is plus a value so that there's little to no water
		 * 
		 */
		
		String[] elements = new String[randomSize*5];
		
		// Done to make sure there's a closure of parenthesis
		// I only open parenthesis when adding a function

		int parenthesis = 0;
		
		// Done to avoid having constants, variables or operators that clash
		// true = lastElement is a variable
		boolean lastVar = false;
		
		// There needs to be a variable no matter what
		boolean hasFunc = false;
		
		/*
		 * I'm generating these two elements to make sure there's an X and Y value
		 * Should be modified later some way, doesn't matter for now
		 */
		
		//elements[0] = generateX() + " +";
		//elements[1] = generateY();
		
		
		// 50/50 chance of the first element being a variable or a function
		// This can be removed and updated to work only on the inside of the function
		
		int count = 0;
		
		while(count<=randomSize) {
			
			// Last element is a variable
			if (lastVar) {
				elements[count] = generateOperator();
				lastVar = false;
				
			} else {
				// Last element is not a variable
				
				if(!hasFunc) {
				
					if(takeChance(50)) {
					
						String var = generateVar();
					
						if (parenthesis>0) {
							if(takeChance(50)) {
							// For practical purposes closing parenthesis has the same requirements as a normal variable
								var = var + " )";
								parenthesis--;
							}
						}
					
						elements[count] = var;
					
						lastVar = true;					
					} else {
						String func = generateFunction();
					
						if(parenthesis == 0) {
							elements[count] = flatify(func);
						
						} else {
							elements[count] = func;
						}
					
						lastVar = false;
						parenthesis++;
						
						hasFunc = true;
					
					}
				} else {
					String var = generateVar();
					
					if (parenthesis>0) {
						if(takeChance(50)) {
						// For practical purposes closing parenthesis has the same requirements as a normal variable
							var = var + " )";
							parenthesis--;
						}
					}
				
					elements[count] = var;
				
					lastVar = true;
				}
				
			}
			count++;
			
		}
		
		if(!lastVar) {
			elements[count] = generateVar();
			count++;
		}
		
		while (parenthesis != 0) {
			elements[count] = ")";
			count++;
			parenthesis--;
		}
		
		elements[count] = "+ 10";
		
		List<String> list = removeNull(elements);
		
		elements = list.toArray(new String[list.size()]);
		
		return addSpace(elements);
	}
	
	// Exists so that I can  construct the elements individually
	// Just adds a space inbetween each element
	// Goes linearly through the elements in the array
	static String addSpace(String[] stel) {
		
		String res = "";
		
		for(int i=0; i<stel.length; i++) {
			res = res + " " + stel[i];
		}
		
		return res.trim();
	}
	
	static String generateVar() {
		if(takeChance(50)) {
			return generateX().trim();
		} else {
			return generateY().trim();
		}
		
	}
	
	// Done so that the generated x is flat enough
	// Can also be negative, and can also be exponential
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
		double chance = 50.0;
		double opportunity = Math.random()*100.0;
		
		/*
		 * There's a 50% chance of it being x ^ 1
		 * 25% for x ^ 2
		 * 12.5% for x ^ 3
		 * So on and so forth
		 */
		while(true) {
			if ((opportunity>chance) || (power > 6)) {
				return Double.toString(num) + " * x ^ " + power;
				
			} else {
				chance = chance/2.0;
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
		double chance = 50.0;
		double opportunity = Math.random()*100.0;
		
		while(true) {
			if ((opportunity>chance) || (power > 6)) {
				return Double.toString(num) + " * y ^ " + power;
				
			} else {
				chance = chance/2.0;
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
		
		return (Double.toString(num) + " * " + str).trim();
	}
	
	static String generateFunction() {
		// It
		int temp = (int)((Math.random()*13)+8);
		
		// Done because asin and acos can end up with NaN
		// Didn't feel like working around them for the moment, so just ignore them
		while ((temp == 11) || (temp == 12)) {
			temp = (int)((Math.random()*13)+8);
		}
		
		return (Operations.createOperation(temp) + " (").trim();
		
	}
	
	static String generateOperator() {
		int temp = (int)(Math.random()*4);
		
		switch(temp) {
		case 0: 
			return "+";
		case 1:
			return "-";
		case 2:
			return "*";
		case 3:
			/*
			 * Exponentials can have some weird effects, so to reduce them
			 * We make sure that it's always an int exponential
			 * And that there's an operator afterwards
			 * All done to make life easier
			 */
			
			int power = 2;
			double chance = 50.0;
			double opportunity = Math.random()*100.0;
			boolean stop = true;
			
			while(stop) {
				
				while(true) {
					if (opportunity>chance) {
						int newtemp = (int)Math.random()*3;
						String op = null;
						
						if (newtemp ==0) {
							op = "+";
						} else if (newtemp ==1) {
							op = "-";
						} else if (newtemp==2) {
							op="*";
						}
						
						return "^ " + power + " " + op;
						
						
					} else {
						chance = chance/2.0;
						power++;
					}
					
				}
				
			}
			break;
			default: 
				System.out.println("Error generating operator");
				break;
			
		}
		
		System.out.println("No operator was generated");
		return null;
		
		
	}
	
	// Chance has to be 0<x<100, to represent a chance of something happening
	// Generate a Math.random() double, if the double*100 < chance
	// Then the boolean returns a true, if not it returns a false
	static boolean takeChance(double chance) {
		double num = Math.random()*100.0;
		
		if(num<chance) {
			return true;
		} else {
			return false;
		}
		
	}
	
	static List<String> removeNull(String[] arr) {
		ArrayList<String> list = new ArrayList<String>();
		for(int i=0; i<arr.length; i++) {
			if(arr[i] != null) {
				list.add(arr[i]);
			}
		}
		return list;
		
	}
	
	public static void main(String[] args) throws Exception {
		
		LessRandomFunctions ran;
		
		for(int i=0; i<50; i++) {
			ran = new LessRandomFunctions();
			System.out.println(ran.toString());
		}
		
	}
	

}
