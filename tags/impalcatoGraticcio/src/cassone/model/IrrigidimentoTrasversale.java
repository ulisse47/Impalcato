package cassone.model;

public class IrrigidimentoTrasversale {

	Pannello p1 = new Pannello();
	Pannello p2 = new Pannello();
	
	boolean doppio;
	double passo;
	boolean rigidEndPost;

	public boolean isRigidEndPost() {
		return rigidEndPost;
	}

	public void setRigidEndPost(boolean rigidEndPost) {
		this.rigidEndPost = rigidEndPost;
	}

	public IrrigidimentoTrasversale(){
		p1.setB(120);
		p1.setH(8);
		p2.setB(80);
		p2.setH(8);
		doppio = false;
		passo = 2000;
	}

	public boolean isDoppio() {
		return doppio;
	}

	public void setDoppio(boolean doppio) {
		this.doppio = doppio;
	}

	public void setPannello1(double b, double h){
		p1.setB(b);
		p1.setH(h);
	}
	public void setPannello2(double b, double h){
		p2.setB(b);
		p2.setH(h);
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

	public double getPasso() {
		return passo;
	}

	public void setPasso(double passo) {
		this.passo = passo;
	}
	
	public double getAst(){
		
		double bst = p2.getB();
		double tstf = p2.getH();
		double hst = p1.getB();
		double tstw = p1.getH();
		
		return hst*tstw+bst*tstf;
	}

	//baricentro rispetto borto attaccato all'ala
	public double gete1(){
		
		double bst = p2.getB();
		double tstf = p2.getH();
		double hst = p1.getB();
		double tstw = p1.getH();
		double e1 = (tstw*hst*hst/2+bst*tstf*(hst+tstf/2))/getAst();
		
		return e1;
	}
	//baricentro rispetto borto esterno ala
	public double gete2(){
		
		double bst = p2.getB();
		double tstf = p2.getH();
		double hst = p1.getB();
		double tstw = p1.getH();
		double e1 = (tstw*hst*hst/2+bst*tstf*(hst+tstf/2))/getAst();
		
		return hst+tstf-e1;
	}

	public double getSecondMomentIy(){
		
		double bst = p2.getB();
		double tstf = p2.getH();
		double hst = p1.getB();
		double tstw = p1.getH();
		double e1 = gete1();
		double e2 = gete2();
		
		double Iy = Math.pow(hst,3)*tstw/12+bst*Math.pow(tstf,3)/12+hst*tstw*Math.pow((e1-hst/2),2)
			+bst*tstf*Math.pow(e2-tstf/2,2)+getAst()*e1*e1;
		
		return Iy;
	}
	
	public double getSecondMomentIz(){
		
		double bst = p2.getB();
		double tstf = p2.getH();
		double hst = p1.getB();
		double tstw = p1.getH();
		double Iz = hst*Math.pow(tstw,3)/12+Math.pow(bst,3)*tstf/12;
		
		return Iz;
	}
	
	public double getPolarSecondMoment(){
	
		return getSecondMomentIy()+getSecondMomentIz();
	}

	public double getWarpingCostant(){
		double bst = p2.getB();
		double tstf = p2.getH();
		double hst = p1.getB();
	
		return Math.pow(bst, 3)*tstf/12*Math.pow(hst+tstf/2,2);
	}
	public double getTorsionalConstantSVenant(){
		double bst = p2.getB();
		double tstf = p2.getH();
		double hst = p1.getB();
		double tstw = p1.getH();
	
		return (hst*Math.pow(tstw, 3)+bst*Math.pow(tstf, 3))/3;
	}
	
	
}
