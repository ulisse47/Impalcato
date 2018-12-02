package cassone.model;

public class IrrigidimentoLongitudinale {
	
	Pannello p1=new Pannello();
	Pannello p2=new Pannello();

	public IrrigidimentoLongitudinale(){
		p1.setB(120);
		p1.setH(8);
		p2.setB(80);
		p2.setH(8);
			}
	
	public void setPannello1(double b, double h, double alfa,double yg){
		p1.setY(yg);
		p1.setAlfa(alfa);
		p1.setB(b);
		p1.setH(h);
	}
	public void setPannello2(double b, double h, double alfa,double yg){
		p2.setY(yg);
		p2.setAlfa(alfa);
		p2.setB(b);
		p2.setH(h);
	}

	public double getXgDaAnimaTrave(double tw){
		double bsl=p1.getB();
		double hsl=p2.getB();
		double tsl1=p1.getH();
		double tsl2=p2.getH();
		
		return ((bsl*tsl1)*(tw/2+bsl/2)+(hsl-tsl1)*tsl2*(tw/2+bsl-tsl2/2))/getArea();
		
	}

	public double getArea(){
		double A = p1.getB()*p1.getH()+(p2.getB()-p1.getH())*p2.getH();
		return A;
	}
	public double getAreaEff(){
		double A = p1.getB()*p1.getH()+(p2.getB()-p1.getH())*p2.getH();
		return A;
	}
	
	public Pannello getP1() {
		return p1;
	}

	public void setP1(Pannello p1) {
		this.p1 = p1;
	}

	public Pannello getP2() {
		return p2;
	}

	public void setP2(Pannello p2) {
		this.p2 = p2;
	}

	public double getY() {
		return  p1.getY();
	}
	
	public double getTorsionalConstantSVenant(){
		double bsl = p1.getB();
		double hsl = p2.getB();
		double tsl1 = p1.getH();
		double tsl2 = p2.getH();
		
		return (bsl*Math.pow(tsl1,3)+(hsl-tsl1)*Math.pow(tsl2,3))/3;
	}
	public double getPolarSecondMoment(){
		double bsl = p1.getB();
		double hsl = p2.getB();
		double tsl1 = p1.getH();
		double tsl2 = p2.getH();
		
		double Iy = tsl1*Math.pow(bsl,3)/3+Math.pow(tsl2,3)*(hsl-tsl1)/12+tsl2*(hsl-tsl1)*Math.pow(bsl-tsl2,2);
		double Iz = bsl*Math.pow(tsl1,3)/12+Math.pow(hsl-tsl1,3)*tsl2/12+tsl2*(hsl-tsl1)*Math.pow(hsl/2,2);
		
		
		return Iz+Iy;
	}
	public double getWarpingCostant(){
		double bsl = p1.getB();
		double hsl = p2.getB();
		double tsl1 = p1.getH();
		double tsl2 = p2.getH();
	
		return (2/3)*(tsl1*(bsl-tsl2/2)*hsl*hsl/2)*(bsl-tsl2/2)*hsl;
	}
	

	
}
