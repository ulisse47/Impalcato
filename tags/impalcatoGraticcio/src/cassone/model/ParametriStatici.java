package cassone.model;

public class ParametriStatici {

	double a,jy,yg,jw;
	public double getA() {
		return a;
	}
	public void setA(double a) {
		this.a = a;
	}
	public double getJy() {
		return jy;
	}
	public void setJy(double jx) {
		jy = jx;
	}
	public double getYg() {
		return yg;
	}
	public void setYg(double yg) {
		this.yg = yg;
	}
	public ParametriStatici(double A, double Jx,double yg,double jw){
		this.a=A;
		this.jy=Jx;
		this.yg=yg;
		this.jw=jw;
		
	}
	public double getJw() {
		return jw;
	}
	public void setJw(double jw) {
		this.jw = jw;
	}
}
