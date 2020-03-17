package Physics;

import javax.script.*;

public class FunctionH implements Function2d{

	public static void main(String[] args) throws Exception {
		// create a script engine manager
	    ScriptEngineManager factory = new ScriptEngineManager();
	    // create a JavaScript engine
	    ScriptEngine engine = factory.getEngineByName("JavaScript");
	    // evaluate JavaScript code from String
	    Object obj = engine.eval("2x");
	    System.out.println( obj );
	}
	
	@Override
	public double evaluate(Vector2d p) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Vector2d gradient(Vector2d p) {
		// TODO Auto-generated method stub
		return null;
	}

}
