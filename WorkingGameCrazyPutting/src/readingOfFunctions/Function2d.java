package readingOfFunctions;

import MainProject.Vector2d;

public interface Function2d {
		public double evaluate(Vector2d p);
		public Vector2d gradient(Vector2d p);
		public void add_subfunct(String string, double x_lower, double x_upper, double y_lower, double y_upper) throws Exception;
}
