package cassone.model;

public class Impalcato {

	int numeroCampate;
	
	double lCampate[];
	
	public Impalcato(int numeroCampate){
		lCampate = new double [30];
		this.numeroCampate=numeroCampate;
				
	}

	public double[] getLCampate() {
		return lCampate;
	}

	public void setLCampate(double[] campate) {
		lCampate = campate;
	}

	public int getNumeroCampate() {
		return numeroCampate;
	}

	public void setNumeroCampate(int numeroCampate) {
		this.numeroCampate = numeroCampate;
	}
	
}
