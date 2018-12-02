package cassone.model;

public class TensioniSezione {

	double ss,s,i,ii,ts,ti,sc,sf;
	
	public TensioniSezione(){
		ss=0;
		s=0;
		ii=0;
		i=0;
		ts=0;
		ti=0;
		sc=0;
		sf=0;
		
	}

	public double getI() {
		return i;
	}

	public void setI(double i) {
		this.i = i;
	}

	public double getIi() {
		return ii;
	}

	public void setIi(double ii) {
		this.ii = ii;
	}

	public double getS() {
		return s;
	}

	public void setS(double s) {
		this.s = s;
	}

	public double getSc() {
		return sc;
	}

	public void setSc(double sc) {
		this.sc = sc;
	}

	public double getSf() {
		return sf;
	}

	public void setSf(double sf) {
		this.sf = sf;
	}

	public double getSs() {
		return ss;
	}

	public void setSs(double ss) {
		this.ss = ss;
	}

	public double getTi() {
		return ti;
	}

	public void setTi(double ti) {
		this.ti = ti;
	}

	public double getTs() {
		return ts;
	}

	public void setTs(double ts) {
		this.ts = ts;
	}
	
	
}
