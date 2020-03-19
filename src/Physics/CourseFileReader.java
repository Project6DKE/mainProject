package Physics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Scanner;

public class CourseFileReader {
	
	private String path;
	
	public CourseFileReader(String path) {this.path=path;}
	
	public PuttingCourse readFile() {
		Scanner in= new Scanner(System.in);
		
		int xx;
		double g,m,mu,vmax,tol,x,y;
		Vector2d start, goal;
		FunctionH height;
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(path))); 
	         String sCurrentLine;
	         String[] line;
	         while ((sCurrentLine = br.readLine()) != null) {
	        	 sCurrentLine=sCurrentLine.replaceAll(";", "");
	        	 sCurrentLine=sCurrentLine.replaceAll(" ", "");
	        	 
	        	 line=sCurrentLine.split("=");
	        	 
	        	 switch(line[0]) {
	        	 case "g":
	        		 g= Double.valueOf(line[1]);
	        		 System.out.println("I read g = "+g);
	        		 break;
	        	
	        	 case "m":
	        		 m= Double.valueOf(line[1]);
	        		 System.out.println("I read m = "+m);
	        		 break;
	        	 
	        	 case "mu":
	        		 mu= Double.valueOf(line[1]);
	        		 System.out.println("I read mu = "+mu);
	        		 break;
	        	 
	        	 case "vmax":
	        		 vmax= Double.valueOf(line[1]);
	        		 System.out.println("I read vmax = "+vmax);
	        		 break;
	        	 
	        	 case "tol":
	        		 tol= Double.valueOf(line[1]);
	        		 System.out.println("I read tol = "+tol);
	        		 break;
	        	 
	        	 case "start":
	        		 xx= line[1].indexOf(",");
		        	 x=Double.valueOf(line[1].substring(1, xx));
		        	 y=Double.valueOf(line[1].substring(xx+1, line[1].length()));
		        	 System.out.println("I read start = ("+x+", "+y+")");
		        	 start=new Vector2d(x,y);
	        		 break;
	        	 
	        	 case "goal":
	        		 xx= line[1].indexOf(",");
	        		 x=Double.valueOf(line[1].substring(1, xx));
		        	 y=Double.valueOf(line[1].substring(xx+1, line[1].length()));
		        	 System.out.println("I read goal = ("+x+", "+y+")");
		        	 goal=new Vector2d(x,y);
		        	 break;
	        	
	        	 case "height":
	        		 String f=createFormula(line[1]);
	        		 System.out.println("I read height = "+f); 
	        		 height= new FunctionH(f);
	        		 break;
	        	
	        		
	        	 
	        	 
	        	 }
	        	 
//	        	 return new PuttingCourse(height, goal, start, mu, vmax, tol, g, m);
	         }
	      } catch (Exception e) {
	         System.out.println("File not found or error while reading the file.");
	         return null;
	      }
//		return null;
		return new PuttingCourse(height, goal, start, mu, vmax, tol, g, m);
	}
	
	private String createFormula(String f) {
		String function="";
		char[] chars= f.toCharArray();
		String data = "+-*/^";
		char[] compare = data.toCharArray();
		function+=chars[0];
		for(int i=1;i<chars.length;i++) {
			
			for(int j=0;j<compare.length;j++) {
				if(chars[i]==compare[j]) function+=" ";
				function+=chars[i];
				if(chars[i]==compare[j]) function+=" ";
				
					}
			
		}
		
		
		return function;
	}
}
