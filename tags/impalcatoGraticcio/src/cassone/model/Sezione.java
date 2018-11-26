package cassone.model;

import java.util.ArrayList;

import cassone.model.SezioniMetalliche.SezioneMetallica;
import cassone.model.SezioniMetalliche.SezioneMetallicaDoppioT;
import cassone.model.irrigidimenti.Irrigidimento;
import cassone.model.irrigidimenti.Irrigidimento_T;
import cassone.model.soletta.Soletta;
import cassone.model.soletta.SolettaT;


public class Sezione {

	private String name = "S1";

	// coordinata x relativa alla campata
	double xSezione;

	// concio metallico tipo
	SezioneMetallica sezioneMetallica;

	//sezione soletta
	Soletta soletta;
	
	// numero camapata
	int nCampata;

	// armatura longitudinale
	double passoArmatura;

	double diametroArmatura;

	double areaArmatura;

	double ygArmatura; // baricentro armatura (distanza da intradosso soletta
						// bxh)

	// calcola automaticamente l'area dell'armatura?
	boolean calcolaArmatura;
	boolean calcolaBeff;
	

    ArrayList<SezioneOutputTensioniFase[]> sezioniOutput=new ArrayList<SezioneOutputTensioniFase[]>();
    ArrayList<SezioneMetallica> sezioniMetEfficaci=new ArrayList<SezioneMetallica>();
    ArrayList<SezioneMetallica> sezioniMetGiunto=new ArrayList<SezioneMetallica>();
	
	//e una sezione di verifica o di analisi globale
	boolean isSezioneAnalisiGlobale;

	// resistenze materiale
//	double fy;

	// sollecitazioni di calcolo
	ArrayList<Sollecitazioni> sollecitazioni;

	// Irrigidimenti Longitudinali
//	ArrayList<IrrigidimentoLongitudinale> irrigidimentiLong;

	Irrigidimento irrigidimetoTrasversale;

	GiuntoBullonato giunto;
	
	// TensioniSezione tensioni;

	public double getPassoIrrigidimentiTrasv() {
		return irrigidimetoTrasversale.getPasso();
	}
	
	public void aggiungiGiunto(){
		if(giunto == null){
			giunto = new GiuntoBullonato();
		}
		
	}
	public void removeGiunto(){
		if(giunto != null){
			giunto = null;
		}
		
	}

	public void setPassoIrrigidimentiTrasv(double passoIrrigidimentiTrasv) {
		irrigidimetoTrasversale.setPasso(passoIrrigidimentiTrasv);
	}

	public Irrigidimento getIrrigidimetoTrasversale() {
		return irrigidimetoTrasversale;
	}

	public void setIrrigidimetoTrasversale(
			Irrigidimento irrigidimetoTrasversale) {
		this.irrigidimetoTrasversale = irrigidimetoTrasversale;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}
	
	public Sezione(){}
	
	public Sezione(boolean isSezioneAnalisiGlobale, SezioneMetallica concio, Soletta soletta) {
		
		sezioneMetallica=concio;
		
		nCampata = 0;
		xSezione = 0;
		calcolaArmatura = false;
		areaArmatura = 0;
		nCampata = 0;

		passoArmatura = 10; // cm
		diametroArmatura = 20; // mm

		this.isSezioneAnalisiGlobale = isSezioneAnalisiGlobale;

		if (isSezioneAnalisiGlobale)
			name = "SA1";
		if (!isSezioneAnalisiGlobale)
			name = "SV1";

		this.soletta=soletta;
		
		
		sollecitazioni = new ArrayList<Sollecitazioni>();
		// tensioni =new TensioniSezione();

/*		irrigidimentiLong = new ArrayList<IrrigidimentoLongitudinale>();
		IrrigidimentoLongitudinale i1 = new IrrigidimentoLongitudinale();
		IrrigidimentoLongitudinale i2 = new IrrigidimentoLongitudinale();
		i1.getP1().setY(1040);
		i2.getP1().setY(1540);

		irrigidimentiLong.add(i1);
		irrigidimentiLong.add(i2);
*/
		irrigidimetoTrasversale = new Irrigidimento_T();
		irrigidimetoTrasversale.setPasso(2000);
	}

	public Sezione(Sezione sezione,String nome) {
		
		Soletta s = sezione.getSoletta();
//		if(s.getClass()== SolettaT.class){
//			soletta = ((SolettaT)s).getCopia();
//		} 
		soletta = s;
		nCampata = sezione.getNCampata();

	//	areaArmatura = sezione.getAreaArmatura(b);
		xSezione = sezione.getXSezione();
		
		sezioneMetallica= sezione.getSezioneMetallica();
//		if(sm.getClass()== SezioneMetallicaDoppioT.class){
//			sezioneMetallica = ((SezioneMetallicaDoppioT)sm).getCopia();
//		} 
		
		name = sezione.getName()+"_temp";
		sollecitazioni = new ArrayList<Sollecitazioni>();
//		irrigidimentiLong = new ArrayList<IrrigidimentoLongitudinale>();
		for (int i = 0; i < sezione.getSollecitazioni().size(); ++i) {
			Sollecitazioni sol = sezione.getSollecitazioni().get(i);
			CondizioniCarico c = sol.getCond();
			Sollecitazioni news = new Sollecitazioni(c);
			sollecitazioni.add(news);
		}

		this.name=nome;
		this.isSezioneAnalisiGlobale = sezione.isSezioneAnalisiGlobale;
		this.calcolaArmatura=sezione.calcolaArmatura;

		this.diametroArmatura=sezione.diametroArmatura;
		this.passoArmatura=sezione.passoArmatura;
		
		irrigidimetoTrasversale = new Irrigidimento_T();
		irrigidimetoTrasversale.setPasso(2000);
	}

	public void setAreaArmatura(double areaArmatura) {
		this.areaArmatura = areaArmatura;
	}

	public double getDiametroArmatura() {
		return diametroArmatura;
	}

	public void setDiametroArmatura(double diametroArmatura) {
		this.diametroArmatura = diametroArmatura;
	}

	public int getNCampata() {
		return nCampata;
	}

	public void setNCampata(int campata) {
		nCampata = campata;
	}

	public double getPassoArmatura() {
		return passoArmatura;
	}

	public void setPassoArmatura(double passoArmatura) {
		this.passoArmatura = passoArmatura;
	}

	public ArrayList<Sollecitazioni> getSollecitazioni() {
		return sollecitazioni;
	}

	public void setSollecitazioni(ArrayList<Sollecitazioni> sollecitazioni) {
		this.sollecitazioni = sollecitazioni;
	}

	public double getXSezione() {
		return xSezione;
	}

	public void setXSezione(double sezione) {
		xSezione = sezione;
	}

	public double getYgArmatura() {
		return ygArmatura;
	}

	public void setYgArmatura(double ygArmatura) {
		this.ygArmatura = ygArmatura;
	}

	public boolean isCalcoloAutomaticoBeff() {
		return calcolaBeff;
	}

	public void setCalcoloAutomaticoBeff(boolean calcoloAutomaticoBeff) {
		calcolaBeff= calcoloAutomaticoBeff;
	}

	

	public double getBeff(Campate campate) {
		// CASO SEZIONE ANALISI GLOBALE
		if (isSezioneAnalisiGlobale) {
			return soletta.getBeffAnalisiGlobale(campate, nCampata,calcolaBeff);
		} else {
			return soletta.getBeffSezioniVerifica(campate, nCampata, xSezione,calcolaBeff);
		}

	}

	/*
	 * public ParametriStaticiSezione getParametriStaticiAnalisi(boolean
	 * isFaseZero,double n, Campate campate){ ParametriStaticiSezione ps= new
	 * ParametriStaticiSezione();
	 * 
	 * ps.calcolaParametriStaticiAnalisi(isFaseZero, n, concio, campate, this);
	 * return ps;
	 *  }
	 */
	public double getAreaArmatura(double Beff) {
		double af;
		if (calcolaArmatura == true) {
			double nbarre = 100 / passoArmatura;
			double Ab = diametroArmatura * diametroArmatura * Math.PI / 4;
			af = Ab * nbarre * Beff / 1000;
			return af;
		} else {
			return areaArmatura;
		}
	}

	public void addSollecitazione(Sollecitazioni sol) {
		sollecitazioni.add(sol);

	}

	public boolean isCalcolaArmatura() {
		return calcolaArmatura;
	}

	public void setCalcolaArmatura(boolean calcolaArmatura) {
		this.calcolaArmatura = calcolaArmatura;
	}


	public GiuntoBullonato getGiunto() {
		return giunto;
	}

	public void setGiunto(GiuntoBullonato giunto) {
		this.giunto = giunto;
	}

	public boolean isSezioneAnalisiGlobale() {
		return isSezioneAnalisiGlobale;
	}

	public void setSezioneAnalisiGlobale(boolean isSezioneAnalisiGlobale) {
		this.isSezioneAnalisiGlobale = isSezioneAnalisiGlobale;
	}

	public double getAreaArmatura() {
		return areaArmatura;
	}

	public SezioneMetallica getSezioneMetallica() {
		return sezioneMetallica;
	}

	public void setSezioneMetallica(SezioneMetallica sezioneMetallica) {
		this.sezioneMetallica = sezioneMetallica;
	}

	public Soletta getSoletta() {
		return soletta;
	}

	public void setSoletta(Soletta soletta) {
		this.soletta = soletta;
	}

	public double getHsoletta(){
		return soletta.getHsoletta();
	}

/*	public double[] get_b1b2_AnalisiGlobale(Campate campate) {
		return soletta.get_b1b2_AnalisiGlobale(campate, nCampata);
	}
*/
	public ArrayList<SezioneOutputTensioniFase[]> getSezioniOutput() {
		return sezioniOutput;
	}

	public void setSezioniOutput(
			ArrayList<SezioneOutputTensioniFase[]> sezioniOutput) {
		this.sezioniOutput = sezioniOutput;
	}
	
	public SezioneOutputTensioniFase getTensioniTotali(int combo){
		
		SezioneOutputTensioniFase[] sfo = sezioniOutput.get(combo);
		
		double ss=0,s=0,i=0,ii=0,ts=0,ti=0,sc=0,scinf=0,sf=0,vPioli=0,VtaglioAnima=0;
		double sid1, sid2;
		double Mfl=0,N=0,V=0,Scls_imp=0;
		
		SezioneOutputTensioniFase st = new SezioneOutputTensioniFase();
		for (int k = 0; k < sfo.length; k++) {
			SezioneOutputTensioniFase sl=sfo[k];
			ss += sl.getSs();
			s += sl.getS();
			i += sl.getI();
			ii += sl.getIi();
			sc += sl.getSc();
			scinf += sl.getScinf();
			sf += sl.getSf();
			ts+=sl.getTs();
			ti+=sl.getTi();
			vPioli += sl.getVPioli();
			Mfl+=sl.getMflettente();
			N+=sl.getNassiale();
			V+=sl.getVtaglio();
			Scls_imp+=sl.getScls_impedito();
			VtaglioAnima+=sl.getVtorsione();
                        
		}
		
		sid1 = Math.pow(s*s+3*ts*ts, 0.5);
		sid2 = Math.pow(i*i+3*ti*ti, 0.5);
		
		st.setSs(ss);
		st.setS(s);
		st.setI(i);
		st.setIi(ii);

		st.setSc(sc);
		st.setScinf(scinf);
		
		st.setSf(sf);

		st.setTs(ts);
		st.setTi(ti);

		st.setSid1(sid1);
		st.setSid2(sid2);
		
		st.setVPioli(vPioli);
		
		st.setMflettente(Mfl);
		st.setNassiale(N);
		st.setVtaglio(V);
		st.setScls_impedito(Scls_imp);
                
                st.setVtaglioAnima(VtaglioAnima);
		return st;
	}

	public ArrayList<SezioneMetallica> getSezioniMetEfficaci() {
		return sezioniMetEfficaci;
	}

	public void setSezioniMetEfficaci(ArrayList<SezioneMetallica> sezioniMetEfficaci) {
		this.sezioniMetEfficaci = sezioniMetEfficaci;
	}
        
	public ArrayList<SezioneMetallica> getSezioniMetGiunto() {
		return sezioniMetGiunto;
	}

	public void setSezioniMetGiunto(ArrayList<SezioneMetallica> sezioniMetGiunto) {
		this.sezioniMetGiunto = sezioniMetGiunto;
	}
	
}
