package cassone.model;

import java.text.NumberFormat;

import cassone.model.SezioniMetalliche.SezioneMetallica;
import cassone.model.SezioniMetalliche.SezioneMetallicaGenerica;
import cassone.model.soletta.Soletta;

public class SezioneOutputTensioniFase {

	//parametri statici sezione
	private double a, yg,jy,jw;
        
        //baricentro armatura
        private double ygf;
	
	//parametri statici sezione cls non collaborante
	private double an, ygn,jyn,jwn;

	//area interna semispessore
	private double awTor;
	
	//scorrimento unioni
	private double sscor;
	//momenti statici azioni taglianti
	private double sns,sni;
	
	//valori tensionali cls
	private double sc,scinf;
	//armatura
	private double sf;
	//acciaio carpenteria
	private double ss, s,i,ii,sid1,sid2,ts,ti;
	//scorrimento collegamenti a taglio
	private double vPioli;
	//momento positivo?
	private boolean momPositivo;
	
	private double Mflettente;
	private double Mtor;
	private double Vtaglio;
        //azione di scorrimento sull'anima'
	private double Vtorsione;
	private double Nassiale;
	private double Scls_impedito;
	
	public double getMflettente() {
		return Mflettente;
	}

        public double getVtorsione() {
		return Vtorsione;
	}

        public void setVtaglioAnima(double VtaglioAnima) {
		this.Vtorsione=VtaglioAnima;
	}

        public double getMtor() {
		return Mtor;
	}

	public void setMflettente(double mflettente) {
		Mflettente = mflettente;
	}

	public double getNassiale() {
		return Nassiale;
	}

	public void setNassiale(double nassiale) {
		Nassiale = nassiale;
	}

	public double getScls_impedito() {
		return Scls_impedito;
	}

	public void setScls_impedito(double scls_impedito) {
		Scls_impedito = scls_impedito;
	}

	public double getVtaglio() {
		return Vtaglio;
	}

	public void setVtaglio(double vtaglio) {
		Vtaglio = vtaglio;
	}

	public boolean isMomPositivo() {
		return momPositivo;
	}

	public void setMomPositivo(boolean momPositivo) {
		this.momPositivo = momPositivo;
	}

	public SezioneOutputTensioniFase(){
		}
	
	public void calcolaTensioni(boolean isFaseZero,double n,Sezione sez, 
			Sollecitazioni sol, boolean sezioneChiusa){

		//sollecitazioni
		// attenzione sollecitazioni sono in kN e kNm
		Mflettente = sol.getM() * 1000000;
		Mtor = sol.getMt() * 1000000;
		Nassiale = sol.getN() * 1000;
		Vtaglio = sol.getV() * 1000;
		Scls_impedito= sol.getSigCls();
		

		double M = Mflettente;
		double Mt = Mtor;
		double N = Nassiale;
		double V = Vtaglio;
		double sigc = Scls_impedito;

		//calcestruzzo
		double Hc = sez.getHsoletta();
		
		//sezione metallica
		SezioneMetallica sm = sez.getSezioneMetallica();
		double Hs = sm.getHtot();
		double ys1 = sm.getYs1();
		double ys2 = sm.getYs2();
		
		//armatura
//		ygf = sez.getYgArmatura();

		//calcolo parametri statici sezione
		calcolaParametriStatici(isFaseZero, n, sez, sol, momPositivo);
	
		double wc = (jy / (yg + Hc));
		double wcinf = (jy / (yg));
		
		// armatura
		double wf = jy / (yg - ygf);
		// carpenteria metallica
		double wss = jy / yg;
		double ws = jy / (yg - ys1);
		double wi = jy / (yg - ys2);
		double wii = jy / (yg - Hs);
	
		sc = (M / wc + N / a )/ n + sigc;
		scinf = (M / wcinf + N / a )/ n + sigc;
		
		// armatura
		sf = M / wf + N / a;
		// carpenteria metallica
		ss = M / wss + N / a;
		s = M / ws + N / a;
		i = M / wi + N / a;
		ii = M / wii + N / a;

		// taglio
		double twt = sm.getTwTot();
		ts = V * sns / (jy * twt);
		ti = V * sni / (jy * twt);
		
		//torsione
                Vtorsione=0;
                if(sezioneChiusa){
                    Vtorsione=Mt/(2*awTor);
                    double tw = sm.getTw();
                    double tauTor = Mt/(2*awTor*tw);
                    ts+=tauTor;
                    ti+=tauTor;
                }
                
		//tensioni ideali anime
		sid1 = Math.pow(s * s + 3 * ts * ts, 0.5);
		sid2 = Math.pow(i * i + 3 * ti * ti, 0.5);

		// Scorrimento
		vPioli = V * sscor ;
	}
	
	public void calcolaParametriStatici(boolean isFaseZero, double n,Sezione sez,Sollecitazioni sol, boolean momentoPositivo){
	
		Progetto prg = Progetto.getInstance();
		Campate campate = prg.getCampate();
		int ncam = sez.getNCampata();
		double xcam = sez.getXSezione();
		
		//sollecitazioni
		double M = sol.getM() * 1000000;
		double N = sol.getN() * 1000;
	
		//caratteristiche sezione metallica
		SezioneMetallica sm = sez.getSezioneMetallica();
		ParametriStatici ps = sm.getParametriStatici();
		double as = ps.getA();
		double ygs = ps.getYg();
		double jxs = ps.getJy();
		double jws = ps.getJw();
		
		//caratteristiche soletta (NB: primo tentativo soletta sempre reagente)
		Soletta sl = sez.getSoletta();
		ps = sl.getParametriStatici(sez.isCalcoloAutomaticoBeff(),campate,ncam,xcam,0,true);
                
		double ac = ps.getA();
		double ygc = ps.getYg();
		double jxc = ps.getJy();
                
		//memorizzo caratteristiche calcetruzzo reagente
		double acreag = ps.getA();
		double ygcreag = ps.getYg();
	
/*                if(!momentoPositivo) {
			ac=0;
			jxc=0;
			} 
*/
		//caratteristiche armatura
		double af = sez.getAreaArmatura();
                //stesso baricentro della soletta
                ygf = ps.getYg();
		if (isFaseZero)	af = 0;

		// caratteristiche statiche di primo tentativo
		a = ac/ n + as + af;
		double ygloc = (ac*ygc/n+af*ygf+as*ygs)/a;
		yg=ygloc;
		jy = jxs+jxc/n + as*(yg-ygs)*(yg-ygs)+af*(yg-ygf)*(yg-ygf)+ac*(yg-ygc)*(yg-ygc)/n;
		//memorizzo baricentro sezione con cls reagente (per calcolo Ssc)
		double ygreag =yg; 
		double jreag = jy;
		
		// iter per il calcolo dell'asse neutro
		double yIter = 0; //distanza baricentro asse neutro primo tentativo
		double yn=0; //posizione asse neutro
		int j=0;
		do {
			ygloc=yg;
			//posizione asse neutro di tentativo yn
			if(M==0 && N>0) yIter = Double.MIN_VALUE;
			else if(M==0 && N<0) yIter = Double.MAX_VALUE;
			else if(M==0 && N==0) yIter = 0;
			else  yIter= -N*jy/(a*M);
 			yn = yg-yIter;
			
			//parametri statici soletta ridotta
			ps = sl.getParametriStatici(sez.isCalcoloAutomaticoBeff(),campate,ncam,xcam,yn,momentoPositivo);
			ac = ps.getA();
			ygc = ps.getYg();
			jxc = ps.getJy();
		
			//nuove caratteristiche
			a = ac/ n + as + af;
			yg = (ac*ygc/n+af*ygf+as*ygs)/a;
			jy = jxs+jxc/n + as*(yg-ygs)*(yg-ygs)+af*(yg-ygf)*(yg-ygf)+ac*(yg-ygc)*(yg-ygc)/n;
			
			++j;
		}
		while(Math.pow((ygloc-yg),2) > 0.1 && j<100);

		// taglio NB:CALCESTRUZZO NON REAGENTE A TRAZIONE
		if(sm.getClass()!= SezioneMetallicaGenerica.class){
			ParametriStatici ps1 = sm.getParametriStaticiS1();
			ParametriStatici ps2 = sm.getParametriStaticiS2();
			sns = (yg-ygc)*ac/n + af*(yg-ygf)+ ps1.getA()*(yg-ps1.getYg());
			sni = (yg-ygc)*ac/n + af*(yg-ygf)+ ps2.getA()*(yg-ps2.getYg());
		}else{
			SezioneMetallicaGenerica smg = (SezioneMetallicaGenerica)sm;
			sns = jy*1/smg.getAwgen();
			sni = sns;
		}
		
		// scorrimento NB:CALCESTRUZZO REAGENTE A TRAZIONE=S/Jy
		sscor = (acreag*(ygreag-ygcreag)/n + af*(ygreag - ygf))/jreag;
		
		
		//momento torcente
		if(sm.isSezioneChiusa()){
			if(isFaseZero){
				awTor = sm.getAreaInternaSemispessore(isFaseZero,0);
			}else{
				awTor = sm.getAreaInternaSemispessore(isFaseZero, sl.getHcacloloJw());
			}
		}else awTor=Double.MAX_VALUE;
		
	}
	
	public void calcolaParametriStatici(boolean isFaseZero, double n,
			 Campate campate, Sezione sez) {

		//caratteristiche sezione metallica
		SezioneMetallica sm = sez.getSezioneMetallica();
		ParametriStatici ps = sm.getParametriStatici();
		double as = ps.getA();
		double ygs = ps.getYg();
		double jxs = ps.getJy();
		double jws = ps.getJw();
		
		
		//caratteristiche soletta
		Soletta sl = sez.getSoletta();
		ps = sl.getParametriStatici(sez.isCalcoloAutomaticoBeff(),campate,sez.getNCampata());
		double ac = ps.getA();
		double ygc = ps.getYg();
		double jxc = ps.getJy();
		double jwc = ps.getJw();
		if (isFaseZero)	ac = 0;
		if (isFaseZero)	jxc = 0;
	
		//caratteristiche armatura
		double af = sez.getAreaArmatura();
		ygf = ygc;
		if (isFaseZero)	af = 0;

		// caratteristiche statiche soletta collaborante
		a = ac/ n + as + af;
		yg = (ac*ygc/n+af*ygf+as*ygs)/a;
		jy = jxs+jxc/n + as*(yg-ygs)*(yg-ygs)+af*(yg-ygf)*(yg-ygf)+ac*(yg-ygc)*(yg-ygc)/n;
		
		an = as + af;
		ygn = (af*ygf+as*ygs)/an;
		jyn = jxs+ as*(ygn-ygs)*(ygn-ygs)+af*(ygn-ygf)*(ygn-ygf);

		if(!sm.isSezioneChiusa()){
			jw = jws+jwc/n;
			jwn = jw;
		}else{
                        jwn = sm.getJw(isFaseZero, n, sl.getSpMedioCalcoloJw(), sl.getHcacloloJw());
                        jw = jwn;
		}
		
		
	}

	public String getToolTipParametriStatici(Sezione sez, int fase){
		String s;
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);

		s = "<html>SEZIONE " + sez.getName() + ": fase " + fase;
		s+="<br>";
		s+="A (mmq)= " + nf.format(a);
		s+="<br>";
		s+="yg (mm)= " + nf.format(yg);
		s+="<br>";
		s+="yn (mm)= " + nf.format(getYn(sez));
		s+="<br>";
		s+="Jy (mm4)= " + nf.format(jy);
		s+="</html>";

		return s;
		
	}
	
	public String getStringParametriStatici(Sezione sez, int fase){
		String s;

//		private static NumberFormat nf;

		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		
		s = "SEZIONE " + sez.getName() + ": fase " + fase;
		s+="\n";
		s+="A (mmq)= " + nf.format(a);
		s+="\n";
		s+="yg (mm)= " + nf.format(yg);
		s+="\n";
		s+="yn (mm)= " + nf.format(getYn(sez));
		s+="\n";
		s+="Jy (mm4)= " + nf.format(jy);
		s+="\n";

		s+="M (kNm)= " + nf.format(Mflettente/1000000);
		s+="\n";
		s+="V (kN)= " + nf.format(Vtaglio/1000);
		s+="\n";
		s+="N(kN)= " + nf.format(Nassiale/1000);
		s+="\n";
		s+="sig cls(Mpa)= " + nf.format(Scls_impedito);
		s+="\n";
		
		
		return s;
		
	}
	
	//restituisce quota asse neutro
	public double getYn(Sezione sez){
		
		SezioneMetallica sm = sez.getSezioneMetallica();
		if(ss-ii == 0) return 0;
		else return ss*sm.getHtot()/(ss-ii);
		
	}
	
	public double getA() {
		return a;
	}

	public void setA(double a) {
		this.a = a;
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

	public double getJy() {
		return jy;
	}

	public void setJy(double jy) {
		this.jy = jy;
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

	public double getScinf() {
		return scinf;
	}

	public void setScinf(double scinf) {
		this.scinf = scinf;
	}

	public double getSf() {
		return sf;
	}

	public void setSf(double sf) {
		this.sf = sf;
	}

	public double getSid1() {
		return sid1;
	}

	public void setSid1(double sid1) {
		this.sid1 = sid1;
	}

	public double getSid2() {
		return sid2;
	}

	public void setSid2(double sid2) {
		this.sid2 = sid2;
	}

	public double getSni() {
		return sni;
	}

	public void setSni(double sni) {
		this.sni = sni;
	}

	public double getSns() {
		return sns;
	}

	public void setSns(double sns) {
		this.sns = sns;
	}

	public double getSs() {
		return ss;
	}

	public void setSs(double ss) {
		this.ss = ss;
	}

	public double getSscor() {
		return sscor;
	}

	public void setSscor(double sscor) {
		this.sscor = sscor;
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

	public double getVPioli() {
		return vPioli;
	}

	public void setVPioli(double pioli) {
		vPioli = pioli;
	}

	public double getYg() {
		return yg;
	}

	public void setYg(double yg) {
		this.yg = yg;
	}

	public double getAn() {
		return an;
	}

	public void setAn(double an) {
		this.an = an;
	}

	public double getJw() {
		return jw;
	}

	public void setJw(double jw) {
		this.jw = jw;
	}

	public double getJwn() {
		return jwn;
	}

	public void setJwn(double jwn) {
		this.jwn = jwn;
	}

	public double getJyn() {
		return jyn;
	}

	public void setJyn(double jyn) {
		this.jyn = jyn;
	}

	public double getYgn() {
		return ygn;
	}

	public void setYgn(double ygn) {
		this.ygn = ygn;
	}

	
}
