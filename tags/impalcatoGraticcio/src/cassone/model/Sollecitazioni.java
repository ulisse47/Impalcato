package cassone.model;

public class Sollecitazioni {

	CondizioniCarico cond;
	double M;//momento - positivo tende fibre inferiori
	double V;//taglio
	double N;//azione assiale - positiva compressione
	double sigCls; //tensione sul cls dovuta a ritiro o effetti termici
	double Mt;//momento torcente
	
	
	public double getMt() {
		return Mt;
	}

	public void setMt(double mt) {
		Mt = mt;
	}

	public Sollecitazioni(CondizioniCarico cond ){
		M = 0;
		V = 0;
		N = 0;
		Mt = 0;
		sigCls = 0;
		this.cond=cond;
	}

	public Sollecitazioni(){
		M = 0;
		V = 0;
		N = 0;
		Mt = 0;
		sigCls = 0;
	}

	public CondizioniCarico getCond() {
		return cond;
	}

	public void setCond(CondizioniCarico cond) {
		this.cond = cond;
	}

	public double getM() {
		return M;
	}

	public void setM(double m) {
		M = m;
	}

	public double getN() {
		return N;
	}

	public void setN(double n) {
		N = n;
	}

	public double getSigCls() {
		return sigCls;
	}

	public void setSigCls(double sigCls) {
		this.sigCls = sigCls;
	}

	public double getV() {
		return V;
	}

	public void setV(double v) {
		V = v;
	}

}
