package cassone.model;

public class Pannello {

	double b;
	double h;
	double alfa;
	double y;
	
	
	public Pannello(){
		b=150;
		h=8;
		alfa = 90/180*3.14;
	}
	
	public Pannello(double b,double h, double alfa){
		this.b=b;
		this.h=h;
		this.alfa = alfa;
	
	}
	public double [] getAsJs(double yrif){
		double v[] = new double [2];
		double Jmax,Jmin,Jl,J,A;
		double dy=y-yrif;
		
		Jmax = b*h*h*h/12;
		Jmin = h*b*b*b/12;
		Jl = Jmin*Math.cos(alfa)+Jmax*Math.sin(alfa);
		A = b*h;
		J = b*h*dy*dy + Jl;
		
		v[0]=A;
		v[1]=J;
		
		return v;
}
	public double getAlfa() {
		return alfa;
	}
	public void setAlfa(double alfa) {
		this.alfa = alfa;
	}
	public double getB() {
		return b;
	}
	public void setB(double b) {
		this.b = b;
	}
	public double getH() {
		return h;
	}
	public void setH(double h) {
		this.h = h;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	
}
