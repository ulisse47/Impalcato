package cassone.model.SezioniMetalliche;

import cassone.model.irrigidimenti.Irrigidimento_T;
import cassone.util.MainRTFCreator;
import cassone.view.grafica.Drawing;
import com.lowagie.text.Cell;
import com.lowagie.text.Element;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.text.NumberFormat;
import java.util.ArrayList;


import com.lowagie.text.BadElementException;
import com.lowagie.text.Table;


import cassone.model.PannelloPostCritico;
import cassone.model.GiuntoBullonato;
import cassone.model.MaterialeAcciaio;
import cassone.model.ParametriStatici;
import cassone.model.Sezione;
import cassone.model.SezioneOutputTensioniFase;
import cassone.model.irrigidimenti.Irrigidimento;
import cassone.model.irrigidimenti.Irrigidimento_L;

public class SezioneMetallicaCassoneII implements SezioneMetallica {
    
    String nome;
    double bs,ts,ti,bi,hw,tw,alfa,teq;
    //anime centrali
    double bic,twc, bsc,tsc;
    
    private double ss, ii,i,s;
    
    double wss,wii,Js,A,yg,ygEff;
    
    //anime centrali
    private double ic,sc;
    
    double sEff, iEff;
    
    Area shapeEff;
    
    // sollecitazioni
    double Nsez, Msez, Vsez;
    
    double[] VbwRd = new double[2];
    double[]  VbfRd = new double[2];
    double[]  VbRd= new double[2];
    
    double[]  nuInterM_V= new double[2];
    double[]  nu3= new double[2];
    
    boolean piattabandaSupIrrigidita;
    
    double VtaglioAnima;
    
    PannelloPostCritico pannelloPostAnimaExt;
    PannelloPostCritico pannelloPostAnimaInt;
    PannelloPostCritico pannelloPostPiattabInfLaterali;
    PannelloPostCritico pannelloPostPiattabInfCentrali;
    
    public boolean isPiattabandaSupIrrigidita() {
        return piattabandaSupIrrigidita;
    }
    
    
    public void setPiattabandaSupIrrigidita(boolean piattabandaSupIrrigidita) {
        this.piattabandaSupIrrigidita = piattabandaSupIrrigidita;
    }
    
    
    ArrayList<Irrigidimento> irsAnimeLaterali = new ArrayList<Irrigidimento>();
    ArrayList<Irrigidimento> irsAnimeCentrali= new ArrayList<Irrigidimento>();
    
    ArrayList<Irrigidimento> irsPiattaInfLaterali= new ArrayList<Irrigidimento>();
    ArrayList<Irrigidimento> irsPiattaInfCentrali= new ArrayList<Irrigidimento>();
    
    public ArrayList<Irrigidimento> getIrsAnimeLaterali() {
        return irsAnimeLaterali;
    }
    
    public void setIrsAnimeLaterali(ArrayList<Irrigidimento> irsAnime) {
        this.irsAnimeLaterali = irsAnime;
    }
    
    public ArrayList<Irrigidimento> getIrsPiattaInfLaterali() {
        return irsPiattaInfLaterali;
    }
    
    public void setIrsPiattaInfLaterali(ArrayList<Irrigidimento> irsPiattaInf) {
        this.irsPiattaInfLaterali = irsPiattaInf;
    }
    
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public SezioneMetallicaCassoneII(SezioneMetallicaCassoneII sm,String nome){
        this.nome=nome;
        bs=sm.getBs();
        ts=sm.getTs();
        ti=sm.getTi();
        bi=sm.getBi();
        hw=sm.getHw();
        tw=sm.getTw();
        alfa=sm.getAlfa();
        
        bic=sm.getBic();
        twc=sm.getTwc();
        bsc=sm.getBsc();
        tsc=sm.getTsc();
        
        piattabandaSupIrrigidita=sm.isPiattabandaSupIrrigidita();
        
        int nirr = sm.getIrsAnimeLaterali().size();
        for (int i = 0; i < nirr; i++) {
            Irrigidimento_L ir = (Irrigidimento_L)sm.getIrsAnimeLaterali().get(i);
            Irrigidimento_L ir1 =new Irrigidimento_L(ir);
            this.irsAnimeLaterali.add(ir1);
        }
        
        int nirrL = sm.getIrsAnimeCentrali().size();
        for (int i = 0; i < nirrL; i++) {
            Irrigidimento_L ir = (Irrigidimento_L)sm.getIrsAnimeCentrali().get(i);
            Irrigidimento_L ir1 =new Irrigidimento_L(ir);
            this.irsAnimeCentrali.add(ir1);
        }
        
        int nirrInf = sm.getIrsPiattaInfLaterali().size();
        for (int i = 0; i < nirrInf ; i++) {
            Irrigidimento_L ir = (Irrigidimento_L)sm.getIrsPiattaInfLaterali().get(i);
            Irrigidimento_L ir1 =new Irrigidimento_L(ir);
            this.irsPiattaInfLaterali.add(ir1);
        }
        int nirrInfC = sm.getIrsPiattaInfCentrali().size();
        for (int i = 0; i < nirrInfC ; i++) {
            Irrigidimento_L ir = (Irrigidimento_L)sm.getIrsPiattaInfCentrali().get(i);
            Irrigidimento_L ir1 =new Irrigidimento_L(ir);
            this.irsPiattaInfCentrali.add(ir1);
        }
        
    }
    
    public double getAlfa() {
        return alfa;
    }
    
    public void setAlfa(double alfa) {
        this.alfa = alfa;
    }
    
    public double getBi() {
        return bi;
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
    
    public void setTw(double tw) {
        this.tw = tw;
    }
    
    public SezioneMetallicaCassoneII(String name) {
        this.nome = name;
    }
    
    public SezioneMetallicaCassoneII(){
        bs=400;
        ts=40;
        hw=2500;
        tw=20;
        ti=30;
        bi=4000;
        bic = 1500;
        twc = 20;
        bsc = 400;
        tsc = 40;
        
        alfa=30*Math.PI/180;
        nome="CAS1";
    }
    
    public void calcolaTensioniEfficaci(MaterialeAcciaio mat, SezioneOutputTensioniFase so,
            double passoIrrigidimentiTrasv,boolean rigidEndPost) {
        
        ss=so.getSs();
        ii=so.getIi();
        s=so.getS();
        i=so.getI();
        ic = i;//da fare
        sc = s;//da fare
        
        VtaglioAnima=so.getVtorsione();
        
        double biLat = (bi-bic)/2;
        pannelloPostPiattabInfLaterali=new PannelloPostCritico(biLat, ti, 0,ii/2+i/2,ii/2+i/2, irsPiattaInfLaterali, passoIrrigidimentiTrasv,
                mat, ts+hw*Math.cos(alfa));
        
        pannelloPostPiattabInfCentrali=new PannelloPostCritico(bic, ti, 0,ii/2+i/2,ii/2+i/2, irsPiattaInfCentrali, passoIrrigidimentiTrasv,
                mat, ts+hw*Math.cos(alfa));
        
        ArrayList<Irrigidimento>irs =getIrrigidimentiCompressiLaterali(s,i);
        pannelloPostAnimaExt = new PannelloPostCritico(hw,tw,alfa,s,i,irs,passoIrrigidimentiTrasv,mat,ts);
        
        ArrayList<Irrigidimento>irs2 =getIrrigidimentiCompressiCentrali(s,i);
        double hwc = getHtot()-tsc-ti;
        pannelloPostAnimaInt = new PannelloPostCritico(hwc,twc,0,s,i,irs2,passoIrrigidimentiTrasv,mat,tsc);
        
        
        
        ParametriStatici psEff= getParametriEfficaci(mat,passoIrrigidimentiTrasv);
        ParametriStatici ps= getParametriStatici();
        
        
        double[] M  = getM_N_SezioneMetallica();
        
        Msez = M[0];
        Nsez = M[1];
        Vsez = so.getVtaglio();
        
        
        Js = psEff.getJy();
        A = psEff.getA();
        ygEff=psEff.getYg();
        yg=ps.getYg();
        
        wss = Js / (ygEff-ts/2);
        wii = Js / (ygEff- getHtot() + ti/2);
        
        // momento aggiuntivo causato dalla nuova eccentricit�
        double DM = M[1] * (+ygEff - yg);
        double Mv = M[0] + DM;
        
        // calcolo le nuove tensioni
        sEff = Mv / wss + M[1] / A;
        iEff= Mv / wii + M[1] / A;
        
        // resistenza a taglio
        calcolaResistenzaTaglio(passoIrrigidimentiTrasv, mat, rigidEndPost);
        
        creashapeEff(mat,passoIrrigidimentiTrasv);
        
    }
    
    public double[] getM_N_SezioneMetallica(){
        
        ParametriStatici ps =getParametriStatici();
        double As = ps.getA();
        double ygs=ps.getYg();
        double Js = ps.getJy();
        
        double Htot = getHtot();
        
        // calcolo le tensioni in corrispondenza della sezione baric.
        double sigYg = (Htot - ygs) * (ss - ii) / Htot + ii;
        double Nconcio = sigYg * As;
        double ssC = ss - sigYg;
        double wssa = Js/(ygs);
        double Mconcio = ssC * wssa;
        
        return new double[]{Mconcio,Nconcio};
    }
    
    
    private void calcolaResistenzaTaglio(double passoIrrig,
            MaterialeAcciaio mat, boolean rigidEndPost) {
        
        double e = mat.gete();
        
        
        //anime laterali
        double bsredDx = bs/2-tw/Math.cos(alfa)/2;
        double bsredSx = bsredDx;
        
        double biredDx = (bi-bic)/4-tw/Math.cos(alfa);
        double biredSx = 0;
        
        double Ved = getVbsdLaterali();
        double[] Vloc = pannelloPostAnimaExt.getVbwRd_VbfRd_VbRd_nuInter_N_M(irsAnimeLaterali, mat, hw, tw,
                alfa, bsredDx, bsredSx, biredDx, biredSx, ts, ti, passoIrrig, rigidEndPost, ss, ii, Ved);
        
        
        VbwRd[0]=Vloc[0];
        VbfRd[0]=Vloc[1];
        VbRd[0]=Vloc[2];
        nu3[0]=Vloc[3];
        nuInterM_V[0]=Vloc[4];
        
        //anime centrali
        bsredDx = bsc/2-twc/2;
        bsredSx = bsredDx;
        biredDx = bic/2-twc/2;
        biredSx = (bi-bic)/4-twc/2;
        
        Ved = getVbsdCentrale();
        Vloc = pannelloPostAnimaInt.getVbwRd_VbfRd_VbRd_nuInter_N_M(irsAnimeCentrali, mat, getHtot()-tsc-ti, twc,
                0, bsredDx, bsredSx, biredDx, biredSx, tsc, ti, passoIrrig, rigidEndPost, ss, ii, Ved);
        VbwRd[1]=Vloc[0];
        VbfRd[1]=Vloc[1];
        VbRd[1]=Vloc[2];
        nu3[1]=Vloc[3];
        nuInterM_V[1]=Vloc[4];
    }
    
    public ParametriStatici getParametriEfficaci(MaterialeAcciaio mat, 	double passoIrrigidimentiTrasv){
        
        //piattabanda sub laterali
        double bsredLat = 2*getBsRedLat(mat.gete());
        double Asup = bsredLat * ts;
        double ygsup = ts / 2;
        double Jsup = bsredLat * ts * ts * ts / 12;
        
        //piattabanda sup centrali
        double bsredCen = 2*getBsRedCentr(mat.gete());
        double AsupC = bsredCen * tsc;
        double ygsupC = tsc / 2;
        double JsupC = bsredCen * tsc * tsc * tsc / 12;
        
        //paiattabande inf laterali
        double biLat = (bi-bic)/2;
//        pannelloPostPiattabInfLaterali=new PannelloPostCritico(biLat, ti, 0,ii/2+i/2,ii/2+i/2, irsPiattaInfLaterali, passoIrrigidimentiTrasv,
//                mat, ts+hw*Math.cos(alfa));
        
        ParametriStatici pi=pannelloPostPiattabInfLaterali.getParametriStaticiAnimaOrizzontale();
        
        double Jinf = 2*pi.getJy();
        double Ainf = 2*pi.getA();
        double yginf = pi.getYg();
        
        //paiattabande inf centrale
//        pannelloPostPiattabInfCentrali=new PannelloPostCritico(bic, ti, 0,ii/2+i/2,ii/2+i/2, irsPiattaInfCentrali, passoIrrigidimentiTrasv,
//                mat, ts+hw*Math.cos(alfa));
        
        ParametriStatici piC=pannelloPostPiattabInfCentrali.getParametriStaticiAnimaOrizzontale();
        
        double JinfC = piC.getJy();
        double AinfC = piC.getA();
        double yginfC = piC.getYg();
        
        //anime laterali
        ParametriStatici pw=getParametriStaticiAnima(mat,s,i,passoIrrigidimentiTrasv);
        double Jwanima = 2*pw.getJy();
        double Aw = 2*pw.getA();
        double ygw = pw.getYg();
        
        //anime centrali
        ParametriStatici pwC=getParametriStaticiAnimaCentrale(mat, sc, ic, passoIrrigidimentiTrasv);
        double JwanimaC = 2*pwC.getJy();
        double AwC = 2*pwC.getA();
        double ygwC = pwC.getYg();
        
        //totale
        double As = Asup+AsupC + Aw + Ainf+AinfC+AwC;
        double ygs = 0;
        if (As != 0)
            ygs = (Asup * ygsup+AsupC * ygsupC + Aw * ygw + Ainf * yginf+ AinfC * yginfC+ AwC * ygwC) / As;
        double Js = Jsup +JsupC+ Jinf + Jwanima+ JwanimaC+JinfC+ Asup * (ygs - ygsup) * (ygs - ygsup) + Aw
                * (ygs - ygw) * (ygs - ygw) + Ainf * (ygs - yginf)* (ygs - yginf)
                + AinfC * (ygs - yginfC)* (ygs - yginfC)+ AwC * (ygs - ygwC)* (ygs - ygwC)
                +AsupC * (ygs - ygsupC) * (ygs - ygsupC);
        
        return new ParametriStatici(As,Js,ygs,0);
        
    }
    
    private ParametriStatici getParametriStaticiAnima(MaterialeAcciaio mat,double s, double i,
            double passoIrrigidimentiTrasv){
        
//        ArrayList<Irrigidimento>irs =getIrrigidimentiCompressiLaterali(s,i);
//        pannelloPostAnimaExt = new PannelloPostCritico(hw,tw,alfa,s,i,irs,passoIrrigidimentiTrasv,mat,ts);
        
        return pannelloPostAnimaExt.getParametriStaticiAnimaVerticale();
        
    }
    
    private ParametriStatici getParametriStaticiAnimaCentrale(MaterialeAcciaio mat,double s, double i,
            double passoIrrigidimentiTrasv){
        
 /*       ArrayList<Irrigidimento>irs =getIrrigidimentiCompressiCentrali(s,i);
        double hwc = getHtot()-tsc-ti;
  
        pannelloPostAnimaInt = new PannelloPostCritico(hwc,twc,0,s,i,irs,passoIrrigidimentiTrasv,mat,tsc);
  */
        return pannelloPostAnimaInt.getParametriStaticiAnimaVerticale();
        
    }
    
    private ArrayList<Irrigidimento> getIrrigidimentiCompressiLaterali(double s, double i){
        ArrayList<Irrigidimento>irs =new ArrayList<Irrigidimento>();
        
        double yn;
        double yi=0;
        if(s>i){
            yn=s*hw/(s-i);
            for (int j = 0; j < irsAnimeLaterali.size(); j++) {
                Irrigidimento ir = irsAnimeLaterali.get(j);
                yi = ir.getY()*Math.cos(alfa);
                if(yi<yn){
                    if(irs.size()!=0){
                        Irrigidimento il = irs.get(0);
                        if(il.getY()<ir.getY())	irs.add(ir);
                        else irs.add(0, ir);
                    } else{
                        irs.add(ir);
                    }
                }
            }
            
            
        }else{
            yn=i*hw/(i-s);
            double hn=hw-yn;
            for (int j = 0; j < irsAnimeLaterali.size(); j++) {
                Irrigidimento ir = irsAnimeLaterali.get(j);
                yi = ir.getY()*Math.cos(alfa);
                if(yi>hn){
                    if(irs.size()!=0){
                        Irrigidimento il = irs.get(0);
                        if(il.getY()>ir.getY())	irs.add(ir);
                        else irs.add(0, ir);
                    } else{
                        irs.add(ir);
                    }
                }
            }
        }
        
        
        return irs;
    }
    
    private ArrayList<Irrigidimento> getIrrigidimentiCompressiCentrali(double s, double i){
        ArrayList<Irrigidimento>irs =new ArrayList<Irrigidimento>();
        
        double yn;
        double yi=0;
        double hwc = getHtot()-tsc-ti;
        if(s>i){
            yn=s*hwc/(s-i);
            for (int j = 0; j < irsAnimeCentrali.size(); j++) {
                Irrigidimento ir = irsAnimeCentrali.get(j);
                yi = ir.getY();
                if(yi<yn){
                    if(irs.size()!=0){
                        Irrigidimento il = irs.get(0);
                        if(il.getY()<ir.getY())	irs.add(ir);
                        else irs.add(0, ir);
                    } else{
                        irs.add(ir);
                    }
                }
            }
            
            
        }else{
            yn=i*hwc/(i-s);
            double hn=hwc-yn;
            for (int j = 0; j < irsAnimeCentrali.size(); j++) {
                Irrigidimento ir = irsAnimeCentrali.get(j);
                yi = ir.getY();
                if(yi>hn){
                    if(irs.size()!=0){
                        Irrigidimento il = irs.get(0);
                        if(il.getY()>ir.getY())	irs.add(ir);
                        else irs.add(0, ir);
                    } else{
                        irs.add(ir);
                    }
                }
            }
        }
        
        
        return irs;
    }
    
    private double getBsRedLat(double e){
        
        if(piattabandaSupIrrigidita) return bs;
        
        double s1 = ss/2+s/2;
        if (s1 > 0) {
            double ks = 0.43;
            double lamdaP = ((bs / 2) / ts) / (28.4 * e * Math.pow(ks, 0.5));
            double ro = Math.min((lamdaP - 0.188) / (lamdaP * lamdaP), 1);
            return ro * bs;
        }else return bs;
        
    }
    
    private double getBsRedCentr(double e){
        
        if(piattabandaSupIrrigidita) return bsc;
        
        double s1 = ss/2+s/2;
        if (s1 > 0) {
            double ks = 0.43;
            double lamdaP = ((bsc / 2) / tsc) / (28.4 * e * Math.pow(ks, 0.5));
            double ro = Math.min((lamdaP - 0.188) / (lamdaP * lamdaP), 1);
            return ro * bsc;
        }else return bsc;
        
    }
    
    private double getBiRedLat(double e){
        // calcolo per la piattab. inf
        
        double bi = this.bi/2-bic/2;
        double s1 = ii/2+i/2;
        if (s1 > 0) {
            double ks = 4;
            double lamdaP = (bi  / ti) / (28.4 * e * Math.pow(ks, 0.5));
            double ro = Math.min((lamdaP - 0.055*4) / (lamdaP * lamdaP), 1);
            return ro * bi;
        }else return bi;
        
    }
    
    private double getBiRedCentr(double e){
        // calcolo per la piattab. inf
        
        double bi = this.bic;
        double s1 = ii/2+i/2;
        if (s1 > 0) {
            double ks = 4;
            double lamdaP = (bi  / ti) / (28.4 * e * Math.pow(ks, 0.5));
            double ro = Math.min((lamdaP - 0.055*4) / (lamdaP * lamdaP), 1);
            return ro * bi;
        }else return bi;
        
    }
    public int getClasseSezione(MaterialeAcciaio mat) {
        // TODO Auto-generated method stub
        return 0;
    }
    
    
    public double getFSd(int nPannello) {
        // TODO Auto-generated method stub
        return 0;
    }
    
    public double getFbRdCoprig(int nPannello) {
        // TODO Auto-generated method stub
        return 0;
    }
    
    public double getFbRdPiatp(int nPannello) {
        // TODO Auto-generated method stub
        return 0;
    }
    
    public double getFscrSLE(int nPannello) {
        // TODO Auto-generated method stub
        return 0;
    }
    
    public double getFscrSLU(int nPannello) {
        // TODO Auto-generated method stub
        return 0;
    }
    
    public double getFvRd(int nPannello) {
        // TODO Auto-generated method stub
        return 0;
    }
    
    public double getHtot() {
        return ts+ti+hw*Math.cos(alfa);
    }
    
    public double getIEff() {
        return iEff;
    }
    
    public String getName() {
        return nome;
    }
    
    public double getNuInterM_V() {
        return nuInterM_V[1];
    }
    public double getNuInterM_VLat() {
        return nuInterM_V[0];
    }
    public double getNuInterM_VCen() {
        return nuInterM_V[1];
    }
    
    
    public ParametriStatici getParametriStatici() {
        
        double hwl=hw*Math.cos(alfa);
        double hwc = getHtot()-tsc-ti;
        double bwl=hw*Math.sin(alfa);
        double bsup = bi+2*bwl;
        double twl=tw/Math.cos(alfa);
        
        
        double Asup = 2*bs * ts;
        double AsupC = 2*bsc * tsc;
        double Aw = 2*hw * tw;
        double AwC = 2*hwc * twc;
        double Ainf = (bi-bic) * ti;
        double AinfC = bic * ti;
        double As = Asup + Aw + Ainf+AsupC+AinfC+AwC;
        
        double ygsup = ts / 2;
        double ygsupC = tsc / 2;
        double ygw = ts + hwl / 2;
        double ygwC = tsc + hwc / 2;
        double yginf = ts + hwl  + ti / 2;
        double yginfC = tsc + hwc  + ti / 2;
        double ygs = 0;
        if (As != 0)
            ygs = (Asup * ygsup +AsupC * ygsupC + Aw * ygw +AwC * ygwC + Ainf * yginf+AinfC * yginfC) / As;
        
        double Jsup = 2*bs * ts * ts * ts / 12;
        double JsupC = 2*bsc * tsc * tsc * tsc / 12;
        double Jwanima = 2*(twl) * hwl * hwl * hwl / 12;
        double JwanimaC = 2*(twc) * hwc * hwc * hwc / 12;
        double Jinf = (bi-bic) * ti * ti * ti / 12;
        double JinfC = bic * ti * ti * ti / 12;
        double Js = Jsup+JsupC + Jinf +JinfC+ Jwanima +JwanimaC + Asup * (ygs - ygsup) * (ygs - ygsup)
        +AsupC * (ygs - ygsupC) * (ygs - ygsupC)+ Aw	* (ygs - ygw) * (ygs - ygw)
        +AwC	* (ygs - ygwC) * (ygs - ygwC) + Ainf * (ygs - yginf)* (ygs - yginf)
        +AinfC * (ygs - yginfC)* (ygs - yginfC);
        
//        double awt = (bi+bsup)*(hwl+ti/2+ts)/2;
//        double a_s = bi/ti+bsup/teq+2*hw/tw;
        double jw = getJw(true,Double.MAX_VALUE,0,0);
        
        return new ParametriStatici(As,Js,ygs,jw);
        
    }
    
    public ParametriStatici getParametriStaticiS1() {
        double tsc=0;
        if(this.tsc>ts){
            tsc = this.ts;
        }else{
            tsc = this.tsc;
        }
        
        double Asup = bs * ts;
        double ygsup = ts / 2;
        double Jsup = bs * ts * ts * ts / 12;
        
        double AsupC = bsc * tsc;
        double ygsupC = tsc / 2;
        double JsupC = bsc * tsc * tsc * tsc / 12;
        
        double At = Asup+AsupC;
        double yg = (Asup*ygsup+AsupC*ygsupC)/At;
        double Jt = Jsup+JsupC+Asup*(yg-ygsup)*(yg-ygsup)+
                AsupC*(yg-ygsupC)*(yg-ygsupC);
        
        return new ParametriStatici(At,Jt,yg,0);
        
    }
    
    
    ///da fare!!!!!!!!!!!!!!!!!!!!!!!
    public ParametriStatici getParametriStaticiS2() {
        double hwl=hw*Math.cos(alfa);
        double twl=tw/Math.cos(alfa);
        
        
        double Asup = 2*bs * ts;
        double Aw = hw * tw;
        double Ainf = 0;
        double As = Asup + Aw + Ainf;
        double ygsup = ts / 2;
        double ygw = ts + hwl / 2;
        double yginf = ts + hwl  + ti / 2;
        double ygs = 0;
        if (As != 0)
            ygs = (Asup * ygsup + Aw * ygw + Ainf * yginf) / As;
        double Jsup = 2*bs * ts * ts * ts / 12;
        double Jwanima = (twl) * hwl * hwl * hwl / 12;
        double Jinf = 0;
        double Js = Jsup + Jinf + Jwanima + Asup * (ygs - ygsup) * (ygs - ygsup) + Aw
                * (ygs - ygw) * (ygs - ygw) + Ainf * (ygs - yginf)
                * (ygs - yginf);
        
        return new ParametriStatici(As,Js,ygs,0);
    }
    
    public double getSEff() {
        return sEff;
    }
    
    public Shape getShape() {
        
        double hwl = hw*Math.cos(alfa);
        double bwl = hw*Math.sin(alfa);
        double spw=tw/Math.cos(alfa);
        
        Rectangle2D.Double rec1 = new Rectangle2D.Double();
        rec1.setFrame(-bi/2-bwl-bs/2,0,bs,ts);
        Rectangle2D.Double rec2 = new Rectangle2D.Double();
        rec2.setFrame(bi/2+bwl-bs/2,0,bs,ts);
        Rectangle2D.Double rec3 = new Rectangle2D.Double();
        rec3.setFrame(-bi/2,ts+hwl,bi,ti);
        
        GeneralPath p4 = new GeneralPath();
        p4.moveTo(-bi/2-bwl-spw/2, ts);
        p4.lineTo(-bi/2-bwl+spw/2, ts);
        p4.lineTo(-bi/2+spw/2, ts+hwl);
        p4.lineTo(-bi/2-spw/2, ts+hwl);
        p4.closePath();
        
        GeneralPath p5 = new GeneralPath();
        p5.moveTo(bi/2+bwl-spw/2, ts);
        p5.lineTo(bi/2+bwl+spw/2, ts);
        p5.lineTo(bi/2+spw/2, ts+hwl);
        p5.lineTo(bi/2-spw/2, ts+hwl);
        p5.closePath();
        
        
        GeneralPath gp= new GeneralPath();
        gp.moveTo( -bi/2,getHtot());
        gp.lineTo(-bwl-bi/2, -hwl);
        gp.lineTo(-bwl-bs/2-bi/2, -hwl);
        gp.lineTo(-bwl-bs/2-bi/2, -hwl-ts);
        gp.lineTo(-bwl+bs/2-bi/2, -hwl-ts);
        gp.lineTo(-bwl+bs/2-bi/2, -hwl);
        gp.lineTo(-bwl+bs/2-bi/2, -hwl);
        
        Area a = new Area(rec1);
        a.add(new Area(rec2));
        a.add(new Area(rec3));
        a.add(new Area(p4));
        a.add(new Area(p5));
        
        //anime interne
        double hwc = getHtot()-tsc-ti;
        Rectangle2D.Double r1 = new Rectangle2D.Double();
        
        r1.setFrame(-bic/2-twc/2,tsc,twc,hwc);
        a.add(new Area(r1));
        r1.setFrame(+bic/2-twc/2,tsc,twc,hwc);
        a.add(new Area(r1));
        
        r1.setFrame(-bic/2-bsc/2,0,bsc,tsc);
        a.add(new Area(r1));
        r1.setFrame(+bic/2-bsc/2,0,bsc,tsc);
        a.add(new Area(r1));
        
        return a;
    }
    
    public double getVbRd() {
        return VbRd[1];
    }
    public double getVbRdLat() {
        return VbRd[0];
    }
    public double getVbRdCen() {
        return VbRd[1];
    }
    
    public double getYs1() {
        return ts;
    }
    
    public double getYs2() {
        return getHtot()-ti;
    }
    
    public double getTw() {
        return tw;
    }
    
    public double getTwTot() {
        return 2*tw/Math.cos(alfa)+2*twc;
    }
    
    public void quota(Graphics2D g2d, int ddquote) {
        AffineTransform old = g2d.getTransform();
        
        double ht = getHtot();
        double bw = hw*Math.sin(alfa);
        double btot = getBtot();
        
        // quote verticali
        AffineTransform at = new AffineTransform();
        at.setToTranslation(-btot/2 - 8 * ddquote, ht);
        g2d.transform(at);
        at.setToRotation(-Math.PI / 2);
        g2d.transform(at);
        Drawing.disegnaQuotaTraDuePunti(0, ht, "", ddquote, g2d);
        
        // quote orizzontali
        g2d.setTransform(old);
        at.setToTranslation(-bi/2-bw, ht+ 5 * ddquote);
        g2d.transform(at);
        Drawing.disegnaQuotaTraDuePunti(0, bw, "", ddquote, g2d);
        Drawing.disegnaQuotaTraDuePunti(bw, bw+bi, "", ddquote, g2d);
        Drawing.disegnaQuotaTraDuePunti(bw+bi, bw+bi+bw, "", ddquote, g2d);
        
        // spessori piattabande inferiore
        g2d.setTransform(old);
        at.setToTranslation(bi / 4, ht);
        g2d.transform(at);
        Line2D ln = new Line2D.Double(0, 0, ddquote, 3 * ddquote);
        Drawing.leader(0, 0, 0, 2 * ddquote, 2 * ddquote, Double.toString(bi)
        + "X" + Double.toString(ti) + "mm", g2d);
        // anima
        g2d.setTransform(old);
        ln.setLine(bi / 2+bw/2, ht / 2, bi / 2+bw, ht / 2);
        g2d.draw(ln);
        g2d.drawString(Double.toString(Math.round(hw)) + "X" + Double.toString(tw) + " mm",
                (int) (bi / 2+bw), (int) (ht / 2));
        
        // superiore
        g2d.setTransform(old);
        at.setToTranslation(bi/2+bw+bs / 2.5, ts);
        g2d.transform(at);
        Drawing.leader(0, 0, 0, 2 * ddquote, 2 * ddquote, Double.toString(bs)
        + "X" + Double.toString(ts) + "mm", g2d);
        
        
        g2d.setTransform(old);
        
    }
    
    public void setName(String name) {
        this.nome=name;
    }
    public String toString() {
        return nome;
    }
    
    public Shape getShapeEff() {
        return shapeEff;
    }
    
    
    public Shape getShapeIrrigidita() {
        
        double hwl = hw*Math.cos(alfa);
        double bwl = hw*Math.sin(alfa);
        
        Area a=new  Area(getShape());
        
        //anime laterali
        for (int i = 0; i < irsAnimeLaterali.size(); i++) {
            Irrigidimento ir = irsAnimeLaterali.get(i);
            Area shIrDx = new Area(ir.getShape(tw));
            Area shIrSx = new Area(ir.getShape(tw));
//			double yi = ir.getY();
            shIrDx.transform(AffineTransform.getRotateInstance(alfa, 0, 0));
            shIrDx.transform(AffineTransform.getTranslateInstance(bi/2+bwl, ts));
            
            shIrSx.transform(AffineTransform.getRotateInstance(-alfa, 0, 0));
            shIrSx.transform(AffineTransform.getTranslateInstance(-bi/2-bwl, ts));
            
            a.add(shIrDx);
            a.add(shIrSx);
        }
        
        //anime centrali
        for (int i = 0; i < irsAnimeCentrali.size(); i++) {
            Irrigidimento ir = irsAnimeCentrali.get(i);
            Area shIrDx = new Area(ir.getShape(tw));
            Area shIrSx = new Area(ir.getShape(tw));
//			double yi = ir.getY();
//			shIrDx.transform(AffineTransform.getRotateInstance(0, 0, ts));
            shIrDx.transform(AffineTransform.getTranslateInstance(-bic/2, tsc));
            shIrSx.transform(AffineTransform.getTranslateInstance(bic/2, tsc));
            a.add(shIrDx);
            a.add(shIrSx);
        }
        
        //piattab inf laterali
        for (int i = 0; i < irsPiattaInfLaterali.size(); i++) {
            Irrigidimento ir = irsPiattaInfLaterali.get(i);
            Area shIrSx= new Area(ir.getShape(tw));
            Area shIrDx= new Area(ir.getShape(tw));
            shIrSx.transform(AffineTransform.getRotateInstance(-Math.toRadians(90), 0, 0));
            shIrSx.transform(AffineTransform.getTranslateInstance(-bi/2, ts+hwl+ti/2));
            a.add(shIrSx);
            shIrDx.transform(AffineTransform.getRotateInstance(-Math.toRadians(90), 0, 0));
            shIrDx.transform(AffineTransform.getTranslateInstance(bic/2, ts+hwl+ti/2));
            a.add(shIrDx);
        }
        
        //piattab inf laterali
        for (int i = 0; i < irsPiattaInfCentrali.size(); i++) {
            Irrigidimento ir = irsPiattaInfCentrali.get(i);
            Area shIrSx= new Area(ir.getShape(tw));
            shIrSx.transform(AffineTransform.getRotateInstance(-Math.toRadians(90), 0, 0));
            shIrSx.transform(AffineTransform.getTranslateInstance(-bic/2, ts+hwl+ti/2));
            a.add(shIrSx);
        }
        return a;
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
    
    public double getSs() {
        return ss;
    }
    
    public void setSs(double ss) {
        this.ss = ss;
    }
    
    //da fare!!!!!!!!!
    public void creashapeEff(MaterialeAcciaio mat,double passoIrrigidimentiTrasv ){
        
        double hwl = hw*Math.cos(alfa);
        double bwl = hw*Math.sin(alfa);
        double hwc = getHtot()-tsc-ti;
        double bsred = getBsRedLat(mat.gete());
        double bsredC = getBsRedCentr(mat.gete());
        
        
        
        shapeEff = new Area();
        
        ArrayList<Irrigidimento> irs = getIrrigidimentiCompressiLaterali(ss, ii);
        ArrayList<Irrigidimento> irsC = getIrrigidimentiCompressiCentrali(ss, ii);
        
        //anime laterali
        Area anDx= new Area(pannelloPostAnimaExt.getShape(hw, tw, alfa, s, i, irs, passoIrrigidimentiTrasv, mat, ts));
        Area anSx= new Area(anDx);
        anDx.transform(AffineTransform.getRotateInstance(-alfa, 0, ts));
        anDx.transform(AffineTransform.getTranslateInstance(-bi/2-bwl, 0));
        
        anSx.transform(AffineTransform.getRotateInstance(alfa, 0, ts));
        anSx.transform(AffineTransform.getTranslateInstance(bi/2+bwl/*+spw/2*/, 0));
        
        shapeEff.add(new Area(anSx));
        shapeEff.add(new Area(anDx));
        
        //anime centrali
        anDx= new Area(pannelloPostAnimaInt.getShape(hwc, twc, 0, sc, ic, irsC, passoIrrigidimentiTrasv, mat, tsc));
        anSx= new Area(anDx);
        
        anDx.transform(AffineTransform.getTranslateInstance(-bic/2, 0));
        anSx.transform(AffineTransform.getTranslateInstance(+bic/2, 0));
        
        shapeEff.add(new Area(anSx));
        shapeEff.add(new Area(anDx));
        
        //piattabda sup laterali
        Rectangle2D.Double r= new Rectangle2D.Double();
        r.setFrame(-bsred/2-bi/2-bwl,0,bsred,ts);
        shapeEff.add(new Area(r));
        r.setFrame(-bsred/2+bi/2+bwl,0,bsred,ts);
        shapeEff.add(new Area(r));
        
        //piattabda sup centrali
        r.setFrame(-bsredC/2-bic/2,0,bsredC,tsc);
        shapeEff.add(new Area(r));
        r.setFrame(-bsredC/2+bic/2,0,bsredC,tsc);
        shapeEff.add(new Area(r));
        
        //piattabande inf laterali
        Area anInfSx= new Area(pannelloPostPiattabInfLaterali.getShape(bi/2-bic/2, ti, ii/2+i/2, irsPiattaInfLaterali, passoIrrigidimentiTrasv,
                mat, ts+hwl+ti/2));
        Area anInfDx= new Area(anInfSx);
        anInfSx.transform(AffineTransform.getRotateInstance(-Math.toRadians(90), 0, ts+hwl+ti/2));
        anInfSx.transform(AffineTransform.getTranslateInstance(-bi/2, 0));
        anInfDx.transform(AffineTransform.getRotateInstance(-Math.toRadians(90), 0, ts+hwl+ti/2));
        anInfDx.transform(AffineTransform.getTranslateInstance(bic/2, 0));
        shapeEff.add(new Area(anInfSx));
        shapeEff.add(new Area(anInfDx));
        
        //piattabande inf centrale
        Area anInf= new Area(pannelloPostPiattabInfCentrali.getShape(bic, ti, ii/2+i/2, irsPiattaInfCentrali, passoIrrigidimentiTrasv,
                mat, ts+hwl+ti/2));
        anInf.transform(AffineTransform.getRotateInstance(-Math.toRadians(90), 0, ts+hwl+ti/2));
        anInf.transform(AffineTransform.getTranslateInstance(-bic/2, 0));
        
        shapeEff.add(new Area(anInf));
        
        
    }
    
    
    public double getTeq() {
        return teq;
    }
    
    
    public void setTeq(double teq) {
        this.teq = teq;
    }
    
    
    public boolean isSezioneChiusa() {
        return true;
    }
    
    
    public double getJw(boolean faseZero,double n, double spSoletta, double hsoletta) {
        
         double binf = bi-tw/Math.cos(alfa)-2*Math.tan(alfa)*(ti/2);
        double htot;
        
        if(faseZero)
            htot = ti/2+hw*Math.cos(alfa)+ts/2;
        else
            htot = ti/2+hw*Math.cos(alfa)+ts;
        
        double bsup = binf + 2*htot*Math.tan(alfa);
        double a_s;
        if(faseZero)
            a_s = binf/ti+2*hw/tw+bsup/teq;
        else
            a_s = binf/ti+2*hw/tw+bsup/(spSoletta/n);
        
        double awt = getAreaInternaSemispessore(faseZero, hsoletta);
        double jw = 4*awt*awt/a_s;
        
        return jw;
        
    }
    
    public double getAreaInternaSemispessore(boolean faseZero,double hsoletta) {
        double binf = bi-tw/Math.cos(alfa)-2*Math.tan(alfa)*(ti/2);
        double htot;
        if(faseZero){
            htot = ti/2+hw*Math.cos(alfa)+ts/2;
            hsoletta=0;
        }
        else
            htot = ti/2+hw*Math.cos(alfa)+ts;
        
        double bsup = binf + 2*htot*Math.tan(alfa);
        double awt = (bsup+binf)*htot/2 + hsoletta*bsup;
        
        return awt ;
    
    }
    
    public double getBic() {
        return bic;
    }
    
    
    public void setBic(double bic) {
        this.bic = bic;
    }
    
    
    public double getBsc() {
        return bsc;
    }
    
    
    public void setBsc(double bsc) {
        this.bsc = bsc;
    }
    
    
    
    public double getTsc() {
        return tsc;
    }
    
    
    public void setTsc(double tsc) {
        this.tsc = tsc;
    }
    
    
    public double getTwc() {
        return twc;
    }
    
    
    public void setTwc(double twc) {
        this.twc = twc;
    }
    
    
    public ArrayList<Irrigidimento> getIrsAnimeCentrali() {
        return irsAnimeCentrali;
    }
    
    
    public void setIrsAnimeCentrali(ArrayList<Irrigidimento> irsAnimeCentrali) {
        this.irsAnimeCentrali = irsAnimeCentrali;
    }
    
    
    public ArrayList<Irrigidimento> getIrsPiattaInfCentrali() {
        return irsPiattaInfCentrali;
    }
    
    
    public void setIrsPiattaInfCentrali(
            ArrayList<Irrigidimento> irsPiattaInfCentrali) {
        this.irsPiattaInfCentrali = irsPiattaInfCentrali;
    }
    
    
    public double getBtot() {
        return bi+2*hw*Math.sin(alfa)+bs;
    }
    
    
    public double[] getIst_Istmin_irrigTrasversale(Irrigidimento irTrasv,
            double passoIrrig, MaterialeAcciaio mat) {
        
        double e = mat.gete();
        double hwc = getHtot()-tsc-ti;
        
        ParametriStatici psTw= irTrasv.getParametriStatici(15*e*tw, 15*e*tw, tw, 1);
        ParametriStatici psBi= irTrasv.getParametriStatici(15*e*ti, 15*e*ti, ti, 1);
        ParametriStatici psTwc= irTrasv.getParametriStatici(15*e*twc, 15*e*twc, twc, 1);
        
        double ItrasW = psTw.getJy();
        double ItrasBi = psBi.getJy();
        double ItrasWc = psTwc.getJy();
        
        //INERZIA MINIMA
        double IminW=0;
        double IminBl=0;
        double IminBc=0;
        double IminWc=0;
        
        
        if(iEff <0 && sEff<0) {
            IminW=0;
            IminWc=0;
        } else{
            
            //anime laterali
            if (passoIrrig / hw < Math.pow(2, 0.5)) {
                IminW= 1.5 * Math.pow(hw, 3) * Math.pow(tw, 3) / Math.pow(passoIrrig, 2);
            } else {
                IminW= 0.75 * hw * Math.pow(tw, 3);
            }
            
            
            //anime centrali
            if (passoIrrig / hwc < Math.pow(2, 0.5)) {
                IminWc= 1.5 * Math.pow(hwc, 3) * Math.pow(twc, 3) / Math.pow(passoIrrig, 2);
            } else {
                IminWc= 0.75 * hwc * Math.pow(twc, 3);
            }
        }
        
        
        
        //piattabanda inf centrale
        if(iEff <0 ) {
            IminBc=0;
            IminBl=0;
        } else{
            
            if (passoIrrig / bic < Math.pow(2, 0.5)) {
                IminBc= 1.5 * Math.pow(bic, 3) * Math.pow(ti, 3) / Math.pow(passoIrrig, 2);
            } else {
                IminBc= 0.75 * bic * Math.pow(ti, 3);
            }
            
            //piattabanda inf lat
            double blat = (bi-bic)/2;
            if (passoIrrig / blat < Math.pow(2, 0.5)) {
                IminBl= 1.5 * Math.pow(blat, 3) * Math.pow(ti, 3) / Math.pow(passoIrrig, 2);
            } else {
                IminBl= 0.75 * blat * Math.pow(ti, 3);
            }
        }
        double IminBi=Math.max(IminBc,IminBl);
        
        if(ItrasW/IminW<ItrasWc/IminWc &&  ItrasW/IminW<ItrasBi/IminBi)
            return new double[] {ItrasW,IminW};
        else if (ItrasWc/IminWc<ItrasBi/IminBi)
            return new double[] {ItrasWc,IminWc};
        else
            return new double[] {ItrasBi,IminBi};
        
    }
    
    
    public Table getTabellaInput() throws BadElementException {
        Table table = new Table(2, 11);
        table.setBorder(Table.NO_BORDER);
        table.setPadding(2);
        table.setSpacing(2);
//		table.setDefaultHorizontalAlignment(Element.ALIGN_RIGHT);
        
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        int nc=0;
        
        Cell c = new Cell("Larghezza piattabande superiori laterali: bs (mm)");
        c.setHorizontalAlignment(Element.ALIGN_LEFT);
        c.setBorder(Cell.NO_BORDER);
        table.addCell(c, nc, 0);
        
        c = new Cell(Double.toString(bs));
        c.setHorizontalAlignment(Element.ALIGN_RIGHT);
        c.setBorder(Cell.NO_BORDER);
        table.addCell(c, nc, 1);
        nc++;
        
        c = new Cell("Spessore piattabande superiori laterali: ts (mm)");
        c.setBorder(Cell.NO_BORDER);
        c.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(c, nc, 0);
        
        c = new Cell(Double.toString(ts));
        c.setHorizontalAlignment(Element.ALIGN_RIGHT);
        c.setBorder(Cell.NO_BORDER);
        table.addCell(c, nc, 1);
        nc++;
        
        
        c = new Cell("Larghezza piattabande superiori centrali: bsc (mm)");
        c.setHorizontalAlignment(Element.ALIGN_LEFT);
        c.setBorder(Cell.NO_BORDER);
        table.addCell(c, nc, 0);
        
        c = new Cell(Double.toString(bsc));
        c.setHorizontalAlignment(Element.ALIGN_RIGHT);
        c.setBorder(Cell.NO_BORDER);
        table.addCell(c, nc, 1);
        nc++;
        
        c = new Cell("Spessore piattabande superiori centrali: tsc (mm)");
        c.setBorder(Cell.NO_BORDER);
        c.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(c, nc, 0);
        
        c = new Cell(Double.toString(tsc));
        c.setBorder(Cell.NO_BORDER);
        c.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(c, nc, 1);
        nc++;
        
        
        c = new Cell("Altezza totale cassone: Htot (mm)");
        c.setBorder(Cell.NO_BORDER);
        c.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(c, nc, 0);
        
        
        c = new Cell(Double.toString(getHtot()));
        c.setBorder(Cell.NO_BORDER);
        c.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(c, nc, 1);
        nc++;
        
        c = new Cell("Spessore anime laterali: tw (mm)");
        c.setBorder(Cell.NO_BORDER);
        c.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(c, nc, 0);
        
        c = new Cell(Double.toString(tw));
        c.setBorder(Cell.NO_BORDER);
        c.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(c, nc, 1);
        nc++;
        
        c = new Cell("Inclinazione anime rispetto l'orizzontale: alfa (deg)");
        c.setBorder(Cell.NO_BORDER);
        c.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(c, nc, 0);
        
        c = new Cell(nf.format(Math.toDegrees(alfa)));
        c.setBorder(Cell.NO_BORDER);
        c.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(c, nc, 1);
        nc++;
        
        c = new Cell("Larghezza piattabanda inferiore: bi (mm)");
        c.setBorder(Cell.NO_BORDER);
        c.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(c, nc, 0);
        
        c = new Cell(Double.toString(bi));
        c.setBorder(Cell.NO_BORDER);
        c.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(c, nc, 1);
        nc++;
        
        c = new Cell("Interasse anime centrali: bic (mm)");
        c.setBorder(Cell.NO_BORDER);
        c.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(c, nc, 0);
        
        c = new Cell(Double.toString(bic));
        c.setBorder(Cell.NO_BORDER);
        c.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(c, nc, 1);
        nc++;
        
        c = new Cell("Spessore anime centrali: twc (mm)");
        c.setBorder(Cell.NO_BORDER);
        c.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(c, nc, 0);
        
        c = new Cell(Double.toString(twc));
        c.setBorder(Cell.NO_BORDER);
        c.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(c, nc, 1);
        nc++;
        
        c = new Cell("Spessore piattabanda inferiore: ti (mm)");
        c.setBorder(Cell.NO_BORDER);
        c.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(c, nc, 0);
        
        c = new Cell(Double.toString(ti));
        c.setBorder(Cell.NO_BORDER);
        c.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(c, nc, 1);
        
        
        
        return table;	}
    
    
    public Table getTabellaIrrigidimenti(double passoTrasv) throws BadElementException {
        
        Cell c;
        Table table = new Table(2, 5+irsAnimeCentrali.size()
        +irsPiattaInfCentrali.size()+irsPiattaInfLaterali.size()+irsAnimeLaterali.size());
        table.setBorder(Table.NO_BORDER);
        table.setPadding(2);
        table.setSpacing(2);
        int nc = 0;
        
        c = MainRTFCreator.getCellaTipo("numero irrigidimenti anime laterali", Cell.ALIGN_LEFT);
        table.addCell(c, nc, 0);
        c = MainRTFCreator.getCellaTipo(Integer.toString(irsAnimeLaterali.size()), Cell.ALIGN_RIGHT);
        table.addCell(c, nc, 1);
        ++nc;
        for (int i = 0; i < irsAnimeLaterali.size(); i++) {
            Irrigidimento ir = irsAnimeLaterali.get(i);
            c = MainRTFCreator.getCellaTipo("Distanza irrigidimento estradosso anima", Cell.ALIGN_LEFT);
            table.addCell(c, nc, 0);
            c = MainRTFCreator.getCellaTipo(Double.toString(ir.getY()), Cell.ALIGN_RIGHT);
            table.addCell(c, nc, 1);
            ++nc;
        }
        
        c = MainRTFCreator.getCellaTipo("numero irrigidimenti anime centrali", Cell.ALIGN_LEFT);
        table.addCell(c, nc, 0);
        c = MainRTFCreator.getCellaTipo(Integer.toString(irsAnimeCentrali.size()), Cell.ALIGN_RIGHT);
        table.addCell(c, nc, 1);
        ++nc;
        for (int i = 0; i < irsAnimeCentrali.size(); i++) {
            Irrigidimento ir = irsAnimeCentrali.get(i);
            c = MainRTFCreator.getCellaTipo("Distanza irrigidimento estradosso anima", Cell.ALIGN_LEFT);
            table.addCell(c, nc, 0);
            c = MainRTFCreator.getCellaTipo(Double.toString(ir.getY()), Cell.ALIGN_RIGHT);
            table.addCell(c, nc, 1);
            ++nc;
        }
        
        c = MainRTFCreator.getCellaTipo("numero irrigidimenti piattabanda inferiore centrale", Cell.ALIGN_LEFT);
        table.addCell(c, nc, 0);
        c = MainRTFCreator.getCellaTipo(Integer.toString(irsPiattaInfCentrali.size()), Cell.ALIGN_RIGHT);
        table.addCell(c, nc, 1);
        ++nc;
        for (int i = 0; i < irsPiattaInfCentrali.size(); i++) {
            Irrigidimento ir = irsPiattaInfCentrali.get(i);
            c = MainRTFCreator.getCellaTipo("Distanza irrigidimento bordo sinistro piattabanda", Cell.ALIGN_LEFT);
            table.addCell(c, nc, 0);
            c = MainRTFCreator.getCellaTipo(Double.toString(ir.getY()), Cell.ALIGN_RIGHT);
            table.addCell(c, nc, 1);
            ++nc;
        }
        
        c = MainRTFCreator.getCellaTipo("numero irrigidimenti piattabande inferiori laterali", Cell.ALIGN_LEFT);
        table.addCell(c, nc, 0);
        c = MainRTFCreator.getCellaTipo(Integer.toString(irsPiattaInfLaterali.size()), Cell.ALIGN_RIGHT);
        table.addCell(c, nc, 1);
        ++nc;
        for (int i = 0; i < irsPiattaInfLaterali.size(); i++) {
            Irrigidimento ir = irsPiattaInfLaterali.get(i);
            c = MainRTFCreator.getCellaTipo("Distanza irrigidimento bordo sinistro piattabanda", Cell.ALIGN_LEFT);
            table.addCell(c, nc, 0);
            c = MainRTFCreator.getCellaTipo(Double.toString(ir.getY()), Cell.ALIGN_RIGHT);
            table.addCell(c, nc, 1);
            ++nc;
        }
        
        c = MainRTFCreator.getCellaTipo("passo irrigidimenti trasversali", Cell.ALIGN_LEFT);
        table.addCell(c, nc, 0);
        c = MainRTFCreator.getCellaTipo(Double.toString(passoTrasv), Cell.ALIGN_RIGHT);
        table.addCell(c, nc, 1);
        ++nc;
        
        
        return table;	}
    
    
    public double getMsez() {
        return Msez;
    }
    
    
    public double getNsez() {
        return Nsez;
    }
    
    
    public double getVsez() {
        return Vsez;
    }
    
    public double getNu3() {
        return nu3[1];
    }
    public double getNu3Lat() {
        return nu3[0];
    }
    public double getNu3Cen() {
        return nu3[1];
    }
    
    public Table getTabellaVerificaIrrigidimenti(double passoTrasv, MaterialeAcciaio mat,
            Irrigidimento irrTrasv) throws BadElementException {
        
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        
        
        Cell c;
        int ncol = 4;
        if(irsAnimeCentrali.size()!=0 || irsPiattaInfCentrali.size()!=0
                ||irsAnimeLaterali.size()!=0 || irsPiattaInfLaterali.size()!=0) ncol+=2;
        Table table = new Table(2, ncol);
        table.setBorder(Table.NO_BORDER);
        table.setPadding(2);
        table.setSpacing(2);
        int nc = 0;
        
        
        if(irsAnimeCentrali.size()!=0 || irsPiattaInfCentrali.size()!=0
                ||irsAnimeLaterali.size()!=0 || irsPiattaInfLaterali.size()!=0){
            Irrigidimento ir;
            if(irsAnimeCentrali.size()!=0) ir = irsAnimeCentrali.get(0);
            else if (irsAnimeLaterali.size()!=0) ir = irsAnimeLaterali.get(0);
            else if (irsPiattaInfCentrali.size()!=0) ir = irsPiattaInfCentrali.get(0);
            else  ir = irsPiattaInfLaterali.get(0);
            
            c = MainRTFCreator.getCellaTipo("Sig,critico: tensione critica torsionale irrigidimenti longitudinali(MPa)", Cell.ALIGN_LEFT);
            table.addCell(c, nc, 0);
            c = MainRTFCreator.getCellaTipo(nf.format(ir.getSigCriticoTorsionale( mat, passoTrasv,tw)), Cell.ALIGN_RIGHT);
            table.addCell(c, nc, 1);
            ++nc;
            
            c = MainRTFCreator.getCellaTipo("Tensione ammissibile di confronto 6fy irrigidimenti longitudinali(MPa)", Cell.ALIGN_LEFT);
            table.addCell(c, nc, 0);
            double sc = 6*mat.getFy(0);
            c = MainRTFCreator.getCellaTipo(nf.format(sc), Cell.ALIGN_RIGHT);
            table.addCell(c, nc, 1);
            ++nc;
            
        }
        double hwc = getHtot()-tsc-ti;
        double sig1 = irrTrasv.getSigCriticoTorsionale( mat, hw,tw);
        double sig2 = irrTrasv.getSigCriticoTorsionale( mat, hwc,twc);
        c = MainRTFCreator.getCellaTipo("Sig,critico: tensione critica torsionale irrigidimenti trasversali(Mpa)", Cell.ALIGN_LEFT);
        table.addCell(c, nc, 0);
        c = MainRTFCreator.getCellaTipo(nf.format(Math.min(sig1,sig2)), Cell.ALIGN_RIGHT);
        table.addCell(c, nc, 1);
        ++nc;
        
        c = MainRTFCreator.getCellaTipo("Tensione ammissibile di confronto 6fy irrigidimenti trasversali(MPa)", Cell.ALIGN_LEFT);
        table.addCell(c, nc, 0);
        double sc = 6*mat.getFy(0);
        c = MainRTFCreator.getCellaTipo(nf.format(sc), Cell.ALIGN_RIGHT);
        table.addCell(c, nc, 1);
        ++nc;
        
        double[] Ist = getIst_Istmin_irrigTrasversale(irrTrasv, passoTrasv, mat);
        c = MainRTFCreator.getCellaTipo("Ist: rigidezza irrigidimenti trasversali più porzione efficace di anima (mm4)", Cell.ALIGN_LEFT);
        table.addCell(c, nc, 0);
        c = MainRTFCreator.getCellaTipo(nf.format(Ist[0]), Cell.ALIGN_RIGHT);
        table.addCell(c, nc, 1);
        ++nc;
        
        c = MainRTFCreator.getCellaTipo("Ist,min: rigidezza minima richiesta irrigidimenti trasversali(mm4)", Cell.ALIGN_LEFT);
        table.addCell(c, nc, 0);
        c = MainRTFCreator.getCellaTipo(nf.format(Ist[1]), Cell.ALIGN_RIGHT);
        table.addCell(c, nc, 1);
        ++nc;
        
        double[] ws = getSigma_W_IrrigTrasversaliAnimaLat(irrTrasv,passoTrasv);
        c = MainRTFCreator.getCellaTipo("Sigma,max: tensione massima di verifica irrigidimento trasversali (anime laterali)", Cell.ALIGN_LEFT);
        table.addCell(c, nc, 0);
        c = MainRTFCreator.getCellaTipo(nf.format(ws[0]), Cell.ALIGN_RIGHT);
        table.addCell(c, nc, 1);
        ++nc;
        
        c = MainRTFCreator.getCellaTipo("W0: freccia massima irrigidimento trasversale(anime laterali)", Cell.ALIGN_LEFT);
        table.addCell(c, nc, 0);
        c = MainRTFCreator.getCellaTipo(nf.format(ws[1]), Cell.ALIGN_RIGHT);
        table.addCell(c, nc, 1);
        ++nc;
        
        
        ws = getSigma_W_IrrigTrasversaliAnimaCen(irrTrasv,passoTrasv);
        c = MainRTFCreator.getCellaTipo("Sigma,max: tensione massima di verifica irrigidimento trasversali (anime centrali)", Cell.ALIGN_LEFT);
        table.addCell(c, nc, 0);
        c = MainRTFCreator.getCellaTipo(nf.format(ws[0]), Cell.ALIGN_RIGHT);
        table.addCell(c, nc, 1);
        ++nc;
        
        c = MainRTFCreator.getCellaTipo("W0: freccia massima irrigidimento trasversale (anime centrali)", Cell.ALIGN_LEFT);
        table.addCell(c, nc, 0);
        c = MainRTFCreator.getCellaTipo(nf.format(ws[1]), Cell.ALIGN_RIGHT);
        table.addCell(c, nc, 1);
        ++nc;
        
        ws = getSigma_W_IrrigTrasversaliPiattaInfLat(irrTrasv,passoTrasv);
        c = MainRTFCreator.getCellaTipo("Sigma,max: tensione massima di verifica irrigidimento trasversali (piattabande inferiori laterali)", Cell.ALIGN_LEFT);
        table.addCell(c, nc, 0);
        c = MainRTFCreator.getCellaTipo(nf.format(ws[0]), Cell.ALIGN_RIGHT);
        table.addCell(c, nc, 1);
        ++nc;
        
        c = MainRTFCreator.getCellaTipo("W0: freccia massima irrigidimento trasversale(piattabande inferiori laterali)", Cell.ALIGN_LEFT);
        table.addCell(c, nc, 0);
        c = MainRTFCreator.getCellaTipo(nf.format(ws[1]), Cell.ALIGN_RIGHT);
        table.addCell(c, nc, 1);
        ++nc;
        
        
        ws = getSigma_W_IrrigTrasversaliPiattaInfCent(irrTrasv,passoTrasv);
        c = MainRTFCreator.getCellaTipo("Sigma,max: tensione massima di verifica irrigidimento trasversali (piattabanda onferiore centrale)", Cell.ALIGN_LEFT);
        table.addCell(c, nc, 0);
        c = MainRTFCreator.getCellaTipo(nf.format(ws[0]), Cell.ALIGN_RIGHT);
        table.addCell(c, nc, 1);
        ++nc;
        
        c = MainRTFCreator.getCellaTipo("W0: freccia massima irrigidimento trasversale (piattabanda onferiore centrale)", Cell.ALIGN_LEFT);
        table.addCell(c, nc, 0);
        c = MainRTFCreator.getCellaTipo(nf.format(ws[1]), Cell.ALIGN_RIGHT);
        table.addCell(c, nc, 1);
        ++nc;
        
        return table;
    }
    
    private double[] getSigma_W_IrrigTrasversaliAnimaLat(Irrigidimento irt,double passo){
        double wl =Math.max(passo,hw)/300;
        double sm[]= pannelloPostAnimaExt.getSigma_W_IrrigidTrasv(Math.max(sEff,iEff),getVbsdLaterali(),(Irrigidimento_T)irt);
        return  new double[]{sm[0],sm[1],wl};
    }
    private double[] getSigma_W_IrrigTrasversaliAnimaCen(Irrigidimento irt,double passo){
        double hwc = getHtot()-tsc-ti;
        double wl =Math.max(passo,hwc)/300;
        double sm[]= pannelloPostAnimaInt.getSigma_W_IrrigidTrasv(Math.max(sEff,iEff),getVbsdCentrale(),(Irrigidimento_T)irt);
        return  new double[]{sm[0],sm[1],wl};
        
    }
    private double[] getSigma_W_IrrigTrasversaliPiattaInfLat(Irrigidimento irt,double passo){
        double wl =Math.max(passo,bi/2-bic/2)/300;
        double sm[]= pannelloPostPiattabInfLaterali.getSigma_W_IrrigidTrasv(iEff,getVbsdInfLaterali(),(Irrigidimento_T)irt);
        return  new double[]{sm[0],sm[1],wl};
        
    }
    private double[] getSigma_W_IrrigTrasversaliPiattaInfCent(Irrigidimento irt,double passo){
        double wl =Math.max(passo,bic)/300;
        double sm[]= pannelloPostPiattabInfCentrali.getSigma_W_IrrigidTrasv(iEff,getVbsdInfCentrali(),(Irrigidimento_T)irt);
        return  new double[]{sm[0],sm[1],wl};
    }
    
    public double[] getSigma_W_IrrigTrasversali(Irrigidimento irt,double passo){
        
        double w1[]= getSigma_W_IrrigTrasversaliAnimaCen(irt,passo);
        double w2[]= getSigma_W_IrrigTrasversaliAnimaLat(irt,passo);
        double w3[]= getSigma_W_IrrigTrasversaliPiattaInfCent(irt,passo);
        double w4[]= getSigma_W_IrrigTrasversaliPiattaInfLat(irt,passo);
        
        double sigma1 =w1[0];
        double sigma2 =w2[0];
        double sigma3 =w3[0];
        double sigma4 =w4[0];
        
        double wd1 =w1[1];
        double wd2 =w2[1];
        double wd3 =w3[1];
        double wd4 =w4[1];
        
        double wl1 =w1[2];
        double wl2 =w2[2];
        double wl3 =w3[2];
        double wl4 =w4[2];
        
        if(wl1>wd1) return new double[] {sigma1,wd1,wl1};
        else if (wl2>wd2) return new double[] {sigma2,wd2,wl2};
        else if (wl3>wd3) return new double[] {sigma3,wd3,wl3};
        else if (wl4>wd4) return new double[] {sigma4,wd4,wl4};
        else if (sigma1>sigma2 && sigma1>sigma3 && sigma1>sigma4) return new double[] {sigma1,wd1,wl1};
        else if (sigma2>sigma1 && sigma2>sigma3 && sigma2>sigma4) return new double[] {sigma2,wd2,wl2};
        else if (sigma3>sigma1 && sigma3>sigma2 && sigma3>sigma4) return new double[] {sigma3,wd3,wl3};
        else return new double[] {sigma4,wd4,wl4};
        
    }
    
    public double getVbwRd() {
        return VbwRd[1];
    }
    
    public double getVbwRdlat() {
        return VbwRd[0];
    }
    public double getVbwRdCen() {
        return VbwRd[1];
    }
    
    public double getVbfRd() {
        return VbfRd[1];
    }
    
    public double getVbfRdLat() {
        return VbfRd[0];
    }
    public double getVbfRdCen() {
        return VbfRd[1];
    }
    
    public double getVbsdCentrale() {
        
        double Vv = Math.abs(Vsez/2);
        return Vv;
        
    }
    
    public double getVbsdLaterali() {
        
        
        double Vt = Math.abs(VtaglioAnima*hw);
        return Vt;
        
    }
    
    public double getVbsdInfCentrali() {
        
        return Math.abs(VtaglioAnima*bic);
        
    }
    public double getVbsdInfLaterali() {
        
        double b = (bi/2-bic/2);
        return Math.abs(VtaglioAnima*b);
        
    }
    
    public double getVbsd() {
        return getVbsdCentrale();
    }
    
    public String getStringParametriStatici(Sezione sez, String combo) {
        String s;
        
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        
        s = "SEZIONE " + sez.getName() + ": combinazione " + combo;
        s+="\n";
        s+="Aeff (mmq)= " + nf.format(A);
        s+="\n";
        s+="ygeff (mm)= " + nf.format(yg);
        s+="\n";
        s+="Jy (mm4)= " + nf.format(Js);
        s+="\n";
        s+="M (kNm)= " + nf.format(Msez/1000000);
        s+="\n";
        s+="V (kN)= " + nf.format(Vsez/1000);
        s+="\n";
        s+="N(kN)= " + nf.format(Nsez/1000);
        s+="\n";
        
        return s;
        
    }
    public void calcolaBulloni(SezioneOutputTensioniFase so, Sezione sez,MaterialeAcciaio mat, GiuntoBullonato g){
        
        //to do
        
    }
    public double getTsup() {
        
        return Math.max(ts,tsc);
        
    }
    
    public double getTinf() {
        
        return ti;
    }
}
