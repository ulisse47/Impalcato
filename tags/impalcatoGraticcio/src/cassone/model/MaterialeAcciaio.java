package cassone.model;

public class MaterialeAcciaio {

	String nome="s235";
	double fy1;
	double fu1;
	double fu2;
	double sp1;
	double fy2;
	double sp2;
	double E=210000;
	double ni=0.3; //coefficiente poisson
	
	double gamma1,gamma0,gamma2;
	//per verifica instabilità taglio
	double nutaglio;
	double Betaw;

	public MaterialeAcciaio(){}
	
	public MaterialeAcciaio(String s){
		
		if(s=="S355"){
			nome=s;
			fy1=355;
			fy2=335;
			fu1=510;
			fu2=490;
			nutaglio=1.2;
			gamma1=1.1;
			gamma0=1.0;
			gamma2=1.25;
			sp1 = 40;
			sp2=100;
			
		}else if(s=="S235"){
			nome=s;
			fy1=235;
			fy2=215;
			fu1=360;
			fu2=340;
			
			nutaglio=1.2;
			gamma1=1.1;
			gamma0=1.0;
			gamma2=1.25;
			sp1 = 40; 
			sp2=100;

	}else if(s=="S275"){
		nome=s;
		fy1=275;
		fy2=255;
		fu1=430;
		fu2=410;
		
		nutaglio=1.2;
		gamma1=1.1;
		gamma0=1.0;
		gamma2=1.25;
		sp1 = 40; 
		sp2=100;

	}
		
		else{
			
			nome=s;
			fy1=235;
			fy2=235;
			fu1=235;
			fu2=235;
			
			nutaglio=1.2;
			gamma1=1.1;
			gamma0=1.0;
			gamma2=1.25;
			sp1 = 40; 	
			sp2 = 40;
		}
		
		
	}

	@Override
	public String toString() {
		return nome;
	}

	
	public double getFy(double spessore) {
		if (spessore<=sp1) return fy1;
		if (spessore>sp1 && spessore<=sp2) return fy2;
		else return 0;
	}
	public double getFu(double spessore) {
		if (spessore<=sp1) return fu1;
		if (spessore>sp1 && spessore<=sp2) return fu2;
		else return 0;
	}
	public double getG() {
		return E/(2*(1+ni));
	}

	
	public double gete(){
		
		return Math.pow(235/fy1, 0.5);
	}

	public double getBetaw() {
		return Betaw;
	}

	public void setBetaw(double betaw) {
		Betaw = betaw;
	}

	public double getE() {
		return E;
	}

	public void setE(double e) {
		E = e;
	}

	public double getFu1() {
		return fu1;
	}

	public void setFu1(double fu1) {
		this.fu1 = fu1;
	}

	public double getFu2() {
		return fu2;
	}

	public void setFu2(double fu2) {
		this.fu2 = fu2;
	}

	public double getFy1() {
		return fy1;
	}

	public void setFy1(double fy1) {
		this.fy1 = fy1;
	}

	public double getFy2() {
		return fy2;
	}

	public void setFy2(double fy2) {
		this.fy2 = fy2;
	}

	public double getGamma0() {
		return gamma0;
	}

	public void setGamma0(double gamma0) {
		this.gamma0 = gamma0;
	}

	public double getGamma1() {
		return gamma1;
	}

	public void setGamma1(double gamma1) {
		this.gamma1 = gamma1;
	}

	public double getGamma2() {
		return gamma2;
	}

	public void setGamma2(double gamma2) {
		this.gamma2 = gamma2;
	}

	public double getNi() {
		return ni;
	}

	public void setNi(double ni) {
		this.ni = ni;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public double getNutaglio() {
		return nutaglio;
	}

	public void setNutaglio(double nutaglio) {
		this.nutaglio = nutaglio;
	}

	public double getSp1() {
		return sp1;
	}

	public void setSp1(double sp1) {
		this.sp1 = sp1;
	}

	public double getSp2() {
		return sp2;
	}

	public void setSp2(double sp2) {
		this.sp2 = sp2;
	}

	
}
