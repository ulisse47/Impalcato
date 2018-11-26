package cassone.model;

public class Campate {

	int nCampate;
	double [] luci;
	
	public Campate(int nCampate){
		this.nCampate =nCampate;
		//NB numero massimo campate 50
		luci = new double [50];
	}

	public Campate(){
		nCampate =3;
		luci = new double [50];
		luci[0]=30.00;
		luci[1]=50.00;
		luci[2]=30.00;
		
	}

	public void setLuce(int i, double luce){
		this.luci[i]=luce;	
	}
	public double[] getLuci() {
		return luci;
	}

	public void setLuci(double[] luci) {
		this.luci = luci;
	}

	public int getNCampate() {
		return nCampate;
	}

	public void setNCampate(int campate) {
		nCampate = campate;
	}
}
