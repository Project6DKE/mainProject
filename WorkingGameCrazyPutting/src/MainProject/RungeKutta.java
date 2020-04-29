package MainProject;

import java.util.ArrayList;

public class RungeKutta extends EulerSolver{
	private PuttingCourse c;
	private double RKStep=0.1;
	
	private Vector2d p,v;
	private Vector2d[] vs= new Vector2d[3];
	private Vector2d[] as= new Vector2d[3];

	public RungeKutta() {
		super();
	}
	
	public RungeKutta(PuttingCourse c) {
		this();
		this.c=c;
	}
	
	public void setCourse(PuttingCourse c) {
		this.c=c;
	}
	
	public void set_RK_Step(double x) {
		this.RKStep=x;
	}
	
	public Vector2d[] solve_RK(Vector2d p1, Vector2d v1) {
		 Vector2d k1,k2,k3,k4,l1,l2,l3,l4,a1,a2,a3,a4,p2,p3,p4,v2,v3,v4;
		 
		 a1=c.calculate_acceleration(p1, v1);
		 k1=new Vector2d(RKStep*a1.get_x(),RKStep*a1.get_y());
		 l1=new Vector2d(RKStep*v1.get_x(),RKStep*v1.get_y());
		 
		 p2= new Vector2d(( p1.get_x()+(l1.get_x())/2 ),( p1.get_y()+(l1.get_y())/2 ));
		 v2= new Vector2d(( v1.get_x()+(k1.get_x())/2 ),( v1.get_y()+(k1.get_y())/2 ));
		 a2=c.calculate_acceleration(p2, v2);
		 k2=new Vector2d(RKStep*a2.get_x(),RKStep*a2.get_y());
		 l2=new Vector2d(RKStep*v2.get_x(),RKStep*v2.get_y());
		 
		 p3= new Vector2d(( p.get_x()+(l2.get_x())/2 ),( p.get_y()+(l2.get_y())/2 ));
		 v3= new Vector2d(( v.get_x()+(k2.get_x())/2 ),( v.get_y()+(k2.get_y())/2 ));
		 a3=c.calculate_acceleration(p3, v3);
		 k3=new Vector2d(RKStep*a3.get_x(),RKStep*a3.get_y());
		 l3=new Vector2d(RKStep*v3.get_x(),RKStep*v3.get_y());
		 
		 p4= new Vector2d(( p.get_x()+(l3.get_x()) ),( p.get_y()+(l3.get_y()) ));
		 v4= new Vector2d(( v.get_x()+(k3.get_x()) ),( v.get_y()+(k3.get_y()) ));
		 a4=c.calculate_acceleration(p4, v4);
		 k4=new Vector2d(RKStep*a4.get_x(),RKStep*a4.get_y());
		 l4=new Vector2d(RKStep*v4.get_x(),RKStep*v4.get_y());
		 
		 double vX,vY,pX,pY;
		 vX=v.get_x()+(k1.get_x()+2*(k2.get_x()+k3.get_x())+k4.get_x())/6;
		 vY=v.get_y()+(k1.get_y()+2*(k2.get_y()+k3.get_y())+k4.get_y())/6;
		 pX=p.get_x()+(l1.get_x()+2*(l2.get_x()+l3.get_x())+l4.get_x())/6;
		 pY=p.get_y()+(l1.get_y()+2*(l2.get_y()+l3.get_y())+l4.get_y())/6;
		 
		 v= new Vector2d(vX,vY);
		 p= new Vector2d(pX,pY);
		 
		 Vector2d[] result = new Vector2d[2];
		 result[0]=p;
		 result[1]=v;
		 return result;
	}	

	/*This returns a list of length 3
	where i=0,1,2 stand for x0,x1,x2 respectively
	*/
	public Vector2d[] bootstrap_AB3(Vector2d p1, Vector2d v1) {
		Vector2d[] xs= new Vector2d[3];
		xs[0]=p1;
		vs[0]=v1;
		
		double tempp=RKStep;
		RKStep=this.get_step_size();
		
		Vector2d[] boot_values= solve_RK(xs[0],vs[0]);
		xs[1]=boot_values[0];
		vs[1]=boot_values[1];
		
		boot_values= solve_RK(xs[1],vs[1]);
		
		xs[2]=boot_values[0];
		vs[2]=boot_values[1];
		
		for(int i=0;i<3;i++) {
			as[i]=c.calculate_acceleration(xs[i], vs[i]);
		}
		
		RKStep=tempp;
		return xs;
	}
	
	public Vector2d[] solve_AB3(Vector2d p1, Vector2d v1){
		p=ab3_body(p1,vs);
		v=ab3_body(v1,as);
		
		as[0]=as[1];
		as[1]=as[2];
		as[2]=c.calculate_acceleration(p, v);
		
		vs[0]=vs[1];
		vs[1]=vs[2];
		vs[2]=v;
		
		Vector2d[] result = new Vector2d[2];
		 result[0]=p;
		 result[1]=v;
		 return result;
	}
	
	private Vector2d ab3_body(Vector2d ws, Vector2d[] fs) {
		double Nx,Ny;
		
		Nx=ws.get_x()+(1/12)*(this.get_step_size())*(23*(fs[2].get_x())-16*(fs[1].get_x())+5*(fs[0].get_x()));
		Ny=ws.get_y()+(1/12)*(this.get_step_size())*(23*(fs[2].get_y())-16*(fs[1].get_y())+5*(fs[0].get_y()));
		return new Vector2d(Nx,Ny);
	}
}
