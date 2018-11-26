package cassone.model;

import cassone.model.SezioniMetalliche.SezioneMetallicaDoppioT;

public class GiuntoBullonato {

	//PIATTABANDA SUP
	//direzione della forza
	double e1aSup,e1bSup,p1Sup;
	int nfile1Sup;
	//direzione trasv forza
	double e2aSup,e2bSup, p2Sup;
	int nfile2Sup;
	//bulloni
	int diamBullSup,diamForoSup;
	//coprigiunti
	double tgSup;
	
	//PIATTABANDA INF
	//direzione della forza
	double e1aInf,e1bInf,p1Inf;
	int nfile1Inf;
	//direzione trasv forza
	double e2aInf,e2bInf,p2Inf;
	int nfile2Inf;
	//bulloni
	int diamBullInf,diamForoInf;
	//coprigiunti
	double tgInf;
	
	//ANIMA
	//direzione della forza
	double e1aW,e1bW,p1W;
	int nfile1W;
	//direzione trasv forza
	double e2W,p2W;
	int nfile2W;
	//bulloni
	int diamBullW,diamForoW;
	//coprigiunti
	double tgW;

	//materiale
	double fubulloni,fuPiastre;
	MaterialeBulloni materiale;
	
	//parametri scorromento
	double ks;
	double nu;
	
	public GiuntoBullonato(){
		ks=1;
		nu=0.3;
		fubulloni=980;
		fuPiastre=510;
		e1aSup=50;
		e1bSup=50;
		e2aSup=50;
		p1Sup=87;
		p2Sup=87;
		diamBullSup =27;
		diamForoSup =29;
		nfile1Sup=5;
		nfile2Sup=5;
		tgSup=20;
		
		e1aW=50;
		e1bW=50;
		e2W=50;
		p1W=87;
		p2W=87;
		diamBullW=27;
		diamForoW=29;
		nfile1W=3;
		nfile2W=4;
		tgW=20;
		
		e1aInf=50;
		e1bInf=50;
		e2aInf=50;
		p1Inf=87;
		p2Inf=87;
		diamBullInf=27;
		diamForoInf=29;
		nfile1Inf=4;
		nfile2Inf=4;
		tgInf=20;
	}

	public double getAreaResistente(int diametro){
		
		if(diametro==8) return 38.6;
		if(diametro==10) return 58;
		if(diametro==12) return 84.3;
		if(diametro==14) return 115;
		if(diametro==16) return 157;
		if(diametro==18) return 192;
		if(diametro==20) return 245;
		if(diametro==22) return 303;
		if(diametro==24) return 353;
		if(diametro==27) return 459;
		if(diametro==30) return 581;
		if(diametro==33) return 694;
		if(diametro==36) return 817;
		
		return diametro*diametro*Math.PI/4;
		
	}
	public double getArea(int diametro){
		return diametro*diametro*Math.PI/4;
	}

	public int getDiamBullInf() {
		return diamBullInf;
	}

	public void setDiamBullInf(int diamBullInf) {
		this.diamBullInf = diamBullInf;
	}

	public int getDiamBullSup() {
		return diamBullSup;
	}

	public void setDiamBullSup(int diamBullSup) {
		this.diamBullSup = diamBullSup;
	}

	public int getDiamBullW() {
		return diamBullW;
	}

	public void setDiamBullW(int diamBullW) {
		this.diamBullW = diamBullW;
	}

	public int getDiamForoInf() {
		return diamForoInf;
	}

	public void setDiamForoInf(int diamForoInf) {
		this.diamForoInf = diamForoInf;
	}

	public int getDiamForoSup() {
		return diamForoSup;
	}

	public void setDiamForoSup(int diamForoSup) {
		this.diamForoSup = diamForoSup;
	}

	public int getDiamForoW() {
		return diamForoW;
	}

	public void setDiamForoW(int diamForoW) {
		this.diamForoW = diamForoW;
	}

	public double getE1aInf() {
		return e1aInf;
	}

	public void setE1aInf(double inf) {
		e1aInf = inf;
	}

	public double getE1aSup() {
		return e1aSup;
	}

	public void setE1aSup(double sup) {
		e1aSup = sup;
	}

	public double getE1bInf() {
		return e1bInf;
	}

	public void setE1bInf(double inf) {
		e1bInf = inf;
	}

	public double getE1bSup() {
		return e1bSup;
	}

	public void setE1bSup(double sup) {
		e1bSup = sup;
	}


	public double getE1aW() {
		return e1aW;
	}

	public void setE1aW(double e1aw) {
		e1aW = e1aw;
	}

	public double getE1bW() {
		return e1bW;
	}

	public void setE1bW(double e1bw) {
		e1bW = e1bw;
	}

	public double getE2aInf() {
		return e2aInf;
	}

	public void setE2aInf(double inf) {
		e2aInf = inf;
	}

	public double getE2aSup() {
		return e2aSup;
	}

	public void setE2aSup(double sup) {
		e2aSup = sup;
	}

	public double getE2W() {
		return e2W;
	}

	public void setE2W(double e2w) {
		e2W = e2w;
	}

	public double getFubulloni() {
		return fubulloni;
	}

	public void setFubulloni(double fubulloni) {
		this.fubulloni = fubulloni;
	}

	public double getFuPiastre() {
		return fuPiastre;
	}

	public void setFuPiastre(double fuPiastre) {
		this.fuPiastre = fuPiastre;
	}

	public double getKs() {
		return ks;
	}

	public void setKs(double ks) {
		this.ks = ks;
	}

	public MaterialeBulloni getMateriale() {
		return materiale;
	}

	public void setMateriale(MaterialeBulloni materiale) {
		this.materiale = materiale;
	}

	public int getNfile1Inf() {
		return nfile1Inf;
	}

	public void setNfile1Inf(int nfile1Inf) {
		this.nfile1Inf = nfile1Inf;
	}

	public int getNfile1Sup() {
		return nfile1Sup;
	}

	public void setNfile1Sup(int nfile1Sup) {
		this.nfile1Sup = nfile1Sup;
	}

	public int getNfile1W() {
		return nfile1W;
	}

	public void setNfile1W(int nfile1W) {
		this.nfile1W = nfile1W;
	}

	public int getNfile2Inf() {
		return nfile2Inf;
	}

	public void setNfile2Inf(int nfile2Inf) {
		this.nfile2Inf = nfile2Inf;
	}

	public int getNfile2Sup() {
		return nfile2Sup;
	}

	public void setNfile2Sup(int nfile2Sup) {
		this.nfile2Sup = nfile2Sup;
	}

	public int getNfile2W() {
		return nfile2W;
	}

	public void setNfile2W(int nfile2W) {
		this.nfile2W = nfile2W;
	}

	public double getNu() {
		return nu;
	}

	public void setNu(double nu) {
		this.nu = nu;
	}

	public double getP1Inf() {
		return p1Inf;
	}

	public void setP1Inf(double inf) {
		p1Inf = inf;
	}

	public double getP1Sup() {
		return p1Sup;
	}

	public void setP1Sup(double sup) {
		p1Sup = sup;
	}

	public double getP1W() {
		return p1W;
	}

	public void setP1W(double p1w) {
		p1W = p1w;
	}

	public double getP2Inf() {
		return p2Inf;
	}

	public void setP2Inf(double inf) {
		p2Inf = inf;
	}

	public double getP2Sup() {
		return p2Sup;
	}

	public void setP2Sup(double sup) {
		p2Sup = sup;
	}

	public double getP2W() {
		return p2W;
	}

	public void setP2W(double p2w) {
		p2W = p2w;
	}

	public double getTgInf() {
		return tgInf;
	}

	public void setTgInf(double tgInf) {
		this.tgInf = tgInf;
	}

	public double getTgSup() {
		return tgSup;
	}

	public void setTgSup(double tgSup) {
		this.tgSup = tgSup;
	}

	public double getTgW() {
		return tgW;
	}

	public void setTgW(double tgW) {
		this.tgW = tgW;
	}
	
	public double getFbRd_CoprigiuntoSup(){
		double v1 =e1aSup/(3*diamForoSup);
		double v2 =p1Sup/(3*diamForoSup)-0.25;
		double v3 =fubulloni/fuPiastre;
		double alfa = Math.min(Math.min(v1,v2),Math.min(v3,1));
		double VbRd_piastre = 2.5*alfa*fuPiastre*2*tgSup*diamBullSup/1.25;
		
		if (e2aSup>1.5*diamForoSup && p2Sup>=3.0*diamForoSup) return VbRd_piastre;

		double var1 = e2aSup/(1.2*diamForoSup);
		double var2 = p2Sup/(2.4*diamForoSup);
		double var = Math.min(var1,var2);
		if (var<1) return 0;
		else{
			double coef = (1.0/3.0)*(var-1)/0.25+(2.0/3.0);
			return VbRd_piastre*coef;
		}
	}
	public double getFbRd_PiattabandaSup(Sezione sez, MaterialeAcciaio mat){
		Progetto prg = Progetto.getInstance();
		SezioneMetallicaDoppioT sm =(SezioneMetallicaDoppioT)sez.getSezioneMetallica();
		double ts = sm.getTs();
		double bs = sm.getBs();
		
		double fum = mat.getFu(ts);
		
		double v1 =e1bSup/(3*diamForoSup);
		double v2 =p1Sup/(3*diamForoSup)-0.25;
		double v3 =fubulloni/fum;
		double alfa = Math.min(Math.min(v1,v2),Math.min(v3,1));
		alfa = Math.min(alfa,1);
		double VbRd_pb = 2.5*alfa*fum*ts*diamBullSup/1.25;
		
		double e2 =( bs -(e2bSup/2+(nfile2Sup-1)*p2Sup));
		
		if (e2>1.5*diamForoSup && p2Sup>=3.0*diamForoSup) return VbRd_pb;

		double var1 = e2/(1.2*diamForoSup);
		double var2 = p2Sup/(2.4*diamForoSup);
		double var = Math.min(var1,var2);
		if (var<1) return 0;
		else{
			double coef = (1.0/3.0)*(var-1)/0.25+(2.0/3.0);
			return VbRd_pb*coef;
		}
	}
	
	
	public double getFscr_sup(boolean SLU){
		
		double FpCd = 0.7*fubulloni*getAreaResistente(diamBullSup);
		double Fscr_sup = ks*nu*2*FpCd;
		if(SLU) return Fscr_sup/1.25;
		else return Fscr_sup/1.10;
		
	}
	
	public double getFvb_sup(){
		return 0.6*fubulloni*getArea(diamBullSup)/1.25;		
	}
	
	public double getFbRd_CoprigiuntoInf(){
		double v1 =e1aInf/(3*diamForoInf);
		double v2 =p1Inf/(3*diamForoInf)-0.25;
		double v3 =fubulloni/fuPiastre;
	
		double alfa = Math.min(Math.min(v1,v2),Math.min(v3,1));
		double VbRd_piastre = 2.5*alfa*fuPiastre*2*tgInf*diamBullInf/1.25;
		
		if (e2aInf>1.5*diamForoInf&& p2Inf>=3.0*diamForoInf) return VbRd_piastre;

		double var1 = e2aInf/(1.2*diamForoInf);
		double var2 = p2Inf/(2.4*diamForoInf);
		double var = Math.min(var1,var2);
		if (var<1) return 0;
		else{
			double coef = (1.0/3.0)*(var-1)/0.25+(2.0/3.0);
			return VbRd_piastre*coef;
		}
	}
	public double getFbRd_PiattabandaInf(Sezione sez,MaterialeAcciaio mat){
		Progetto prg = Progetto.getInstance();
		SezioneMetallicaDoppioT sm =(SezioneMetallicaDoppioT)sez.getSezioneMetallica();
		double ti = sm.getTi();
		double bi = sm.getBi();
		
		double fum =mat.getFu(ti);
		
		double v1 =e1bInf/(3*diamForoInf);
		double v2 =p1Inf/(3*diamForoInf)-0.25;
		double v3 =fubulloni/fum;
		double alfa = Math.min(Math.min(v1,v2),Math.min(v3,1));
		double VbRd_pb = 2.5*alfa*fum*ti*diamBullInf/1.25;
		
		double e2 =( bi -(e2bInf/2 +(nfile2Inf-1)*p2Inf));
		
		if (e2>1.5*diamForoInf&& p2Inf>=3.0*diamForoInf) return VbRd_pb;

		double var1 = e2/(1.2*diamForoInf);
		double var2 = p2Inf/(2.4*diamForoInf);
		double var = Math.min(var1,var2);
		if (var<1) return 0;
		else{
			double coef = (1.0/3.0)*(var-1)/0.25+(2.0/3.0);
			return VbRd_pb*coef;
		}
	}
	
	
	public double getFscr_inf(boolean SLU){
		
		double FpCd = 0.7*fubulloni*getAreaResistente(diamBullInf);
		double Fscr_inf = ks*nu*2*FpCd;
		if(SLU) return Fscr_inf/1.25;
		else return Fscr_inf/1.10;
		
	}
	
	public double getFvb_inf(){
		return 0.6*fubulloni*getArea(diamBullInf)/1.25;		
	}
	
	public double getFbRd_CoprigiuntoW(MaterialeAcciaio mat){
		double v1 =e1aW/(3*diamForoW);
		double v2 =p1W/(3*diamForoW)-0.25;
		double v3 =fubulloni/fuPiastre;
		double alfa = Math.min(Math.min(v1,v2),Math.min(v3,1));
		double VbRd_piastre = 2.5*alfa*fuPiastre*2*tgW*diamBullW/1.25;
		
		if (e2W>1.5*diamForoW&& p2W>=3.0*diamForoW) return VbRd_piastre;

		double var1 = e2W/(1.2*diamForoW);
		double var2 = p2W/(2.4*diamForoW);
		double var = Math.min(var1,var2);
		if (var<1) return 0;
		else{
			double coef = (1.0/3.0)*(var-1)/0.25+(2.0/3.0);
			return VbRd_piastre*coef;
		}
	}
	public double getFbRd_PiattabandaW(Sezione sez, MaterialeAcciaio mat){
		Progetto prg = Progetto.getInstance();
		SezioneMetallicaDoppioT sm =(SezioneMetallicaDoppioT)sez.getSezioneMetallica();
		double tw = sm.getTw();
		double hw = sm.getHw();
		
		double fum = prg.getMateriale().getFu(tw);
		
		double v1 =e1bW/(3*diamForoW);
		double v2 =p1W/(3*diamForoW)-0.25;
		double v3 =fubulloni/fum;
		double alfa = Math.min(Math.min(v1,v2),Math.min(v3,1));
		double VbRd_pb = 2.5*alfa*fum*tw*diamBullW/1.25;

		if (p2W>=3.0*diamForoW) return VbRd_pb;

		double var = p2W/(2.4*diamForoW);
		if (var<1) return 0;
		else{
			double coef = (1.0/3.0)*(var-1)/0.25+(2.0/3.0);
			return VbRd_pb*coef;
		}
		
	}
	
	
	public double getFscr_w(boolean SLU){
		
		double FpCd = 0.7*fubulloni*getAreaResistente(diamBullW);
		double Fscr_inf = ks*nu*2*FpCd;
		if(SLU) return Fscr_inf/1.25;
		else return Fscr_inf/1.10;
		
	}
	
	public double getFvb_w(){
		return 0.6*fubulloni*getArea(diamBullW)/1.25;		
	}

	public double getE2bSup() {
		return e2bSup;
	}

	public void setE2bSup(double sup) {
		e2bSup = sup;
	}

	public double getE2bInf() {
		return e2bInf;
	}

	public void setE2bInf(double inf) {
		e2bInf = inf;
	}
	}
