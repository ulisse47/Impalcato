package cassone.model;

public class Concio {
	
	private String name = "C1";
	
	//MISURE IN MILLIMETRI
	//piattabande sup in mm
	private double bs;
	private double ts;

	//anima inf in mm
	private double hw;
	private double tw;
	
	//piattabande inf in mm
	private double bi;
	private double ti;
	
	//caso sezione generica
	private boolean sezioneGenerica;
	private double Jgen,Asgen,yggen,Hgen,Awgen;

	
public Concio(double bs, double ts,double tw, double hw,double bi, double ti){
	this.bs=bs;
	this.ts=ts;
	this.bi=bi;
	this.ti=ti;
	this.hw=hw;
	this.tw=tw;
	name = "C1";
	this.sezioneGenerica=false;
}

//crea sezione generica
public Concio(String name, double Jgen, double Asgen,double yggen,double Hgen,double Awgen){
	this.yggen=yggen;
	this.Jgen=Jgen;
	this.Hgen=Hgen;
	this.Asgen=Asgen;
	this.Awgen=Awgen;
	this.sezioneGenerica=true;
	this.name=name;
	
	//imposta i restanti valori per soli fini
	//rappresentazione grafica
	bs = Hgen/2;
	bi = Hgen/2;
	ti = 20;
	ts = 20;
	tw = 15;
	hw = Hgen-ts-ti;
	
}

public Concio(Concio concio){
	this.bs=concio.getBs();
	this.ts=concio.getTs();
	this.bi=concio.getBi();
	this.ti=concio.getTi();
	this.hw=concio.getHw();
	this.tw=concio.getTw();
	this.name="_" + concio.getName();
	this.sezioneGenerica=concio.isSezioneGenerica();
	
}

public Concio(){
	this.bs=600;
	this.ts=40;
	this.bi=600;
	this.ti=40;
	this.hw=2000;
	this.tw=8;
	this.sezioneGenerica=false;
}

@Override
public String toString() {
	// TODO Auto-generated method stub
	return getName();
}

public double getBi() {
	return bi;
}

public double getHtot() {
	return ti+ts+hw;
}


public String getName() {
	return name;
}

public void setName(String name) {
	this.name = name;
}

public void setBi(double bi) {
	this.bi = bi;
}


public double getBs() {
	return bs;
}


public void setBs(double bs) {
	this.bs = bs;
}


public double getHw() {
	return hw;
}


public void setHw(double hw) {
	this.hw = hw;
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


public double getTw() {
	return tw;
}


public void setTw(double tw) {
	this.tw = tw;
}	
	
public double getArea(){
	if(!isSezioneGenerica()){
	double a = bs*ts+bi*ti+tw*hw;
	return a;}
	else{
		return Asgen;
	}
}

public double[] getAs_Ygs_Js_Jw(){
	double [] v = new double [4]; 
	
	if(!isSezioneGenerica()){
	double Asup = bs * ts;
	double Aw = hw * tw;
	double Ainf = bi * ti;
	double As = Asup + Aw + Ainf;
	double ygsup = ts / 2;
	double ygw = ts + hw / 2;
	double yginf = ts + hw + ti / 2;
	double ygs = 0;
	if (As != 0)
		ygs = (Asup * ygsup + Aw * ygw + Ainf * yginf) / As;
	double Jsup = bs * ts * ts * ts / 12;
	double Jwanima = tw * hw * hw * hw / 12;
	double Jinf = bi * ti * ti * ti / 12;
	double Js = Jsup + Jinf + Jwanima + Asup * (ygs - ygsup) * (ygs - ygsup) + Aw
			* (ygs - ygw) * (ygs - ygw) + Ainf * (ygs - yginf)
			* (ygs - yginf);
	double Jw = (bs * ts * ts * ts + bi * ti * ti * ti + hw * tw * tw * tw)/3;
	v[0]=As;
	v[1]=ygs;
	v[2]=Js;
	v[3]=Jw;
	}
	else{
		v[0]=Asgen;
		v[1]=yggen;
		v[2]=Jgen;
		v[3]=0;
	}
	return v;

}

public double getMplastico(double Nsd, double fy){
	double a = getArea();
	double s = (a/2-bs*ts)/tw;
	double s2 = hw-s;
	double yg1 = (bs*ts*(s+ts/2)+s*tw*s/2)/(bs*ts+s*tw);
	double yg2 = (bi*ti*(s2+ti/2)+s2*tw*s2/2)/(bi*ti+s2*tw);
	double yg = yg1+yg2;
		
	double w=yg*a/2;
	double Mplrd = w*fy;
	double Nrd = fy*a;
	double n1=Nsd/Nrd;
	double n2 = Nsd/(fy*(a-bs*ts-bi*ti));
	
	if (n1 < 0.25 && n2 <0.5) return Mplrd;
	else{
		double aa = Math.min((a-bs*ts-bi*ti)/a,0.5);
		return (Mplrd*(1-n1)/(1-0.5*aa));
	}
	
}

//restituisce il momento resistente della flangie
public double getMFlange(MaterialeAcciaio mat, double bsred, double bired, double Nconcio) {

	double e = mat.gete();

	double bs = Math.min(30 * e * ts + tw, bsred);
	double as = bs * ts;
	double bi = Math.min(30 * e * ti + tw, bired);
	double ai = bi * ti;
	double hf = hw + ti / 2 + ts / 2;
	double Mf = Math.min(hf * as * mat.getFy(ts) / mat.gamma0, hf * ai
			* mat.getFy(ti) / mat.gamma0);

	//momento ridotto per presenza azione assiale
	if(Nconcio>0){
		Mf = Mf * (1 - Nconcio / ((as + ai) * mat.getFy(0) / mat.getGamma0()));
	}

	return Mf;
}

public double getAsgen() {
	return Asgen;
}

public void setAsgen(double asgen) {
	Asgen = asgen;
}

public double getJgen() {
	return Jgen;
}

public void setJgen(double jgen) {
	Jgen = jgen;
}

public boolean isSezioneGenerica() {
	return sezioneGenerica;
}

public void setSezioneGenerica(boolean sezioneGenerica) {
	this.sezioneGenerica = sezioneGenerica;
	if(sezioneGenerica) impostaGeometriaSezioneGenerica(); 
}

public void impostaGeometriaSezioneGenerica() {
	
	//imposta i restanti valori per soli fini
	//rappresentazione grafica
//	bs = Hgen/2;
//	bi = Hgen/2;
//	ti = 20;
//	ts = 20;
//	tw = 15;
	hw = Hgen-ts-ti;
}

public double getYggen() {
	return yggen;
}

public void setYggen(double yggen) {
	this.yggen = yggen;
}

public double getHgen() {
	return Hgen;
}

public void setHgen(double hgen) {
	Hgen = hgen;
}

public double getAwgen() {
	return Awgen;
}

public void setAwgen(double awgen) {
	Awgen = awgen;
}

}
