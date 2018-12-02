package cassone.model.SezioniMetalliche;

import cassone.model.irrigidimenti.Irrigidimento_T;
import cassone.view.grafica.Drawing;
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
import com.lowagie.text.Cell;
import com.lowagie.text.Element;
import com.lowagie.text.Table;


import cassone.model.PannelloPostCritico;
import cassone.model.GiuntoBullonato;
import cassone.model.MaterialeAcciaio;
import cassone.model.ParametriStatici;
import cassone.model.Sezione;
import cassone.model.SezioneOutputTensioniFase;
import cassone.model.irrigidimenti.Irrigidimento;
import cassone.model.irrigidimenti.Irrigidimento_L;
import cassone.util.MainRTFCreator;

public class SezioneMetallicaCassone implements SezioneMetallica {
    
    String nome;
    double bs,ts,ti,bi,hw,tw,alfa,teq;
    
    //parametri statici efficaci
    double wss,wii,Js,A,yg,ygEff;
    
    private double ss, ii,i,s;
    
    double sEff, iEff;
    
    Area shapeEff;
    
    // sollecitazioni
    double Nsez, Msez, Vsez,Mtor;
    double Vtorsione;
    
    double VbwRd, VbfRd,VbRd;
    double nuInterM_V,nu3;
    
    boolean piattabandaSupIrrigidita;
    
    PannelloPostCritico pannelloPostPiattaInf;
    PannelloPostCritico pannelloPostAnima;
    
    public boolean isPiattabandaSupIrrigidita() {
        return piattabandaSupIrrigidita;
    }
    
    
    public void setPiattabandaSupIrrigidita(boolean piattabandaSupIrrigidita) {
        this.piattabandaSupIrrigidita = piattabandaSupIrrigidita;
    }
    
    
    ArrayList<Irrigidimento> irsAnime = new ArrayList<Irrigidimento>();
    ArrayList<Irrigidimento> irsPiattaInf= new ArrayList<Irrigidimento>();
    
    public ArrayList<Irrigidimento> getIrsAnime() {
        return irsAnime;
    }
    
    public void setIrsAnime(ArrayList<Irrigidimento> irsAnime) {
        this.irsAnime = irsAnime;
    }
    
    public ArrayList<Irrigidimento> getIrsPiattaInf() {
        return irsPiattaInf;
    }
    
    public void setIrsPiattaInf(ArrayList<Irrigidimento> irsPiattaInf) {
        this.irsPiattaInf = irsPiattaInf;
    }
    
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public SezioneMetallicaCassone(SezioneMetallicaCassone sm,String nome){
        this.nome=nome;
        bs=sm.getBs();
        ts=sm.getTs();
        ti=sm.getTi();
        bi=sm.getBi();
        hw=sm.getHw();
        tw=sm.getTw();
        alfa=sm.getAlfa();
        
        piattabandaSupIrrigidita=sm.isPiattabandaSupIrrigidita();
        
        int nirr = sm.getIrsAnime().size();
        for (int i = 0; i < nirr; i++) {
            Irrigidimento_L ir = (Irrigidimento_L)sm.getIrsAnime().get(i);
            Irrigidimento_L ir1 =new Irrigidimento_L(ir);
            this.irsAnime.add(ir1);
        }
        
        int nirrInf = sm.getIrsPiattaInf().size();
        for (int i = 0; i < nirrInf ; i++) {
            Irrigidimento_L ir = (Irrigidimento_L)sm.getIrsPiattaInf().get(i);
            Irrigidimento_L ir1 =new Irrigidimento_L(ir);
            this.irsPiattaInf.add(ir1);
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
    public SezioneMetallicaCassone(String name) {
        this.nome = name;
    }
    
    
    public SezioneMetallicaCassone(){
        bs=400;
        ts=40;
        hw=2500;
        tw=20;
        ti=30;
        bi=2500;
        alfa=30*Math.PI/180;
        nome="CAS1";
    }
    
    public void calcolaTensioniEfficaci(MaterialeAcciaio mat, SezioneOutputTensioniFase so,
            double passoIrrigidimentiTrasv,boolean rigidEndPost) {
        
        ss=so.getSs();
        ii=so.getIi();
        s=so.getS();
        i=so.getI();
        Vtorsione=so.getVtorsione();
        
        
        pannelloPostPiattaInf = new PannelloPostCritico(bi,ti,0,ii/2+i/2,ii/2+i/2,irsPiattaInf,
                passoIrrigidimentiTrasv,mat,ts+hw*Math.cos(alfa));
        
        ArrayList<Irrigidimento>irs =getIrrigidimentiCompressi(s,i);
        pannelloPostAnima = new PannelloPostCritico(hw,tw,alfa,s,i,irs,passoIrrigidimentiTrasv,mat,ts);
        
        
        ParametriStatici psEff= getParametriEfficaci(mat,passoIrrigidimentiTrasv);
        ParametriStatici ps= getParametriStatici();
        
        
        double[] M  = getM_N_SezioneMetallica();
        
        Msez = M[0];
        Nsez = M[1];
        Vsez = so.getVtaglio();
        Mtor=so.getMtor();
        
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
        
        double bsredDx = bs/2-tw/Math.cos(alfa)/2;
        double bsredSx = bsredDx;
        
        double biredDx = bi/2-tw/Math.cos(alfa);
        double biredSx = 0;
        
        double Ved = getVbsd();
        
        double[] Vloc = pannelloPostAnima.getVbwRd_VbfRd_VbRd_nuInter_N_M(irsAnime, mat, hw, tw,
                alfa, bsredDx, bsredSx, biredDx, biredSx, ts, ti, passoIrrig, rigidEndPost, ss, ii, Ved);
        
        VbwRd=Vloc[0];
        VbfRd=Vloc[1];
        VbRd=Vloc[2];
        nu3=Vloc[3];
        nuInterM_V=Vloc[4];
    }
    
    public ParametriStatici getParametriEfficaci(MaterialeAcciaio mat, double passoIrrigidimentiTrasv){
        
        
        //piattabanda sub
        double bsred = 2*getBsRed(mat.gete());
        double Asup = bsred * ts;
        double ygsup = ts / 2;
        double Jsup = bsred * ts * ts * ts / 12;
        
        //paiattabanda inf
        //              pannelloPostPiattaInf = new PannelloPostCritico(bi,ti,0,ii/2+i/2,ii/2+i/2,irsPiattaInf,
//                        passoIrrigidimentiTrasv,mat,ts+hw*Math.cos(alfa));
        ParametriStatici pi=pannelloPostPiattaInf.getParametriStaticiAnimaOrizzontale();
        
        double Jinf = pi.getJy();
        double Ainf = pi.getA();
        double yginf = pi.getYg();
        
        
        //anima
        ParametriStatici pw=getParametriStaticiAnima(mat,s,i,passoIrrigidimentiTrasv);
        double Jwanima = 2*pw.getJy();
        double Aw = 2*pw.getA();
        double ygw = pw.getYg();
        
        //totale
        double As = Asup + Aw + Ainf;
        double ygs = 0;
        if (As != 0)
            ygs = (Asup * ygsup + Aw * ygw + Ainf * yginf) / As;
        double Js = Jsup + Jinf + Jwanima + Asup * (ygs - ygsup) * (ygs - ygsup) + Aw
                * (ygs - ygw) * (ygs - ygw) + Ainf * (ygs - yginf)
                * (ygs - yginf);
        
        return new ParametriStatici(As,Js,ygs,0);
        
    }
    
    private double getHwl(){
        return  hw*Math.cos(alfa);
        
    }
    
    private ParametriStatici getParametriStaticiAnima(MaterialeAcciaio mat,double s, double i,
            double passoIrrigidimentiTrasv){
        
//		ArrayList<Irrigidimento>irs =getIrrigidimentiCompressi(s,i);
        
//                pannelloPostAnima = new PannelloPostCritico(hw,tw,alfa,s,i,irs,passoIrrigidimentiTrasv,mat,ts);
        return pannelloPostAnima.getParametriStaticiAnimaVerticale();
        
    }
    
    private ArrayList<Irrigidimento> getIrrigidimentiCompressi(double s, double i){
        ArrayList<Irrigidimento>irs =new ArrayList<Irrigidimento>();
        
        double yn;
        double yi=0;
        if(s>i){
            yn=s*hw/(s-i);
            for (int j = 0; j < irsAnime.size(); j++) {
                Irrigidimento ir = irsAnime.get(j);
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
            for (int j = 0; j < irsAnime.size(); j++) {
                Irrigidimento ir = irsAnime.get(j);
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
    
    
    private double getBsRed(double e){
        
        if(piattabandaSupIrrigidita) return bs;
        
        double s1 = ss/2+s/2;
        if (s1 > 0) {
            double ks = 0.43;
            double lamdaP = ((bs / 2) / ts) / (28.4 * e * Math.pow(ks, 0.5));
            double ro = Math.min((lamdaP - 0.188) / (lamdaP * lamdaP), 1);
            return ro * bs;
        }else return bs;
        
    }
    
    private double getBiRed(double e){
        // calcolo per la piattab. inf
        
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
        return nuInterM_V;
    }
    
    public ParametriStatici getParametriStatici() {
        
        double hwl=hw*Math.cos(alfa);
        double bwl=hw*Math.sin(alfa);
        double bsup = bi+2*bwl;
        double twl=tw/Math.cos(alfa);
        
        
        double Asup = 2*bs * ts;
        double Aw = 2*hw * tw;
        double Ainf = bi * ti;
        double As = Asup + Aw + Ainf;
        double ygsup = ts / 2;
        double ygw = ts + hwl / 2;
        double yginf = ts + hwl  + ti / 2;
        double ygs = 0;
        if (As != 0)
            ygs = (Asup * ygsup + Aw * ygw + Ainf * yginf) / As;
        double Jsup = 2*bs * ts * ts * ts / 12;
        double Jwanima = 2*(twl) * hwl * hwl * hwl / 12;
        double Jinf = bi * ti * ti * ti / 12;
        double Js = Jsup + Jinf + Jwanima + Asup * (ygs - ygsup) * (ygs - ygsup) + Aw
                * (ygs - ygw) * (ygs - ygw) + Ainf * (ygs - yginf)
                * (ygs - yginf);
//		double Jw = (bs * ts * ts * ts + bi * ti * ti * ti + hw * tw * tw * tw)/3;
        
//        double awt = getAreaInternaSemispessore(0);
//        double a_s = bi/ti+bsup/teq+2*hw/tw;
        double jw = getJw(true, Double.MAX_VALUE,0,0);
        
        return new ParametriStatici(As,Js,ygs,jw);
        
    }
    
    public ParametriStatici getParametriStaticiS1() {
        
        double Asup = 2*bs * ts;
        double ygsup = ts / 2;
        double Jsup = 2*bs * ts * ts * ts / 12;
        
        return new ParametriStatici(Asup,Jsup,ygsup,0);
        
    }
    
    public ParametriStatici getParametriStaticiS2() {
        double hwl=hw*Math.cos(alfa);
        double twl=tw/Math.cos(alfa);
        
        
        double Asup = 2*bs * ts;
        double Aw = 2*hw * tw;
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
        Area b = new Area(rec2);
        Area c = new Area(rec3);
        Area d = new Area(p4);
        Area e = new Area(p5);
        
        a.add(b);
        a.add(c);
        a.add(d);
        a.add(e);
        
        return a;
    }
    
    public double getVbRd() {
        return VbRd;
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
        return 2*tw/Math.cos(alfa);
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
        
        for (int i = 0; i < irsAnime.size(); i++) {
            Irrigidimento ir = irsAnime.get(i);
            Area shIrDx = new Area(ir.getShape(tw));
            Area shIrSx = new Area(ir.getShape(tw));
            
            shIrDx.transform(AffineTransform.getRotateInstance(alfa, 0, 0));
            shIrDx.transform(AffineTransform.getTranslateInstance(bi/2+bwl, ts));
            
            shIrSx.transform(AffineTransform.getRotateInstance(-alfa, 0, 0));
            shIrSx.transform(AffineTransform.getTranslateInstance(-bi/2-bwl, ts));
            
            a.add(shIrDx);
            a.add(shIrSx);
        }
        
        for (int i = 0; i < irsPiattaInf.size(); i++) {
            Irrigidimento ir = irsPiattaInf.get(i);
            Area shIr= new Area(ir.getShape(tw));
            shIr.transform(AffineTransform.getRotateInstance(-Math.toRadians(90), 0, 0));
            shIr.transform(AffineTransform.getTranslateInstance(-bi/2, ts+hwl+ti/2));
            a.add(shIr);
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
    public void creashapeEff(MaterialeAcciaio mat,double passoIrrigidimentiTrasv ){
        
        double hwl = hw*Math.cos(alfa);
        double bwl = hw*Math.sin(alfa);
        double spw = tw/Math.cos(alfa);
        double bsred = getBsRed(mat.gete());
        double bired = getBiRed(mat.gete());
        
        
        shapeEff = new Area();
        
        ArrayList<Irrigidimento> irs = getIrrigidimentiCompressi(ss, ii);
        
        Area anDx= new Area(pannelloPostAnima.getShape(hw, tw, alfa, s, i, irs, passoIrrigidimentiTrasv, mat, ts));
        Area anSx= new Area(anDx);
        anDx.transform(AffineTransform.getRotateInstance(-alfa, 0, ts));
        anDx.transform(AffineTransform.getTranslateInstance(-bi/2-bwl, 0));
        
        anSx.transform(AffineTransform.getRotateInstance(alfa, 0, ts));
        anSx.transform(AffineTransform.getTranslateInstance(bi/2+bwl/*+spw/2*/, 0));
        
        shapeEff.add(new Area(anSx));
        shapeEff.add(new Area(anDx));
        
        Rectangle2D.Double r= new Rectangle2D.Double();
        r.setFrame(-bsred/2-bi/2-bwl,0,bsred,ts);
        shapeEff.add(new Area(r));
        
        r.setFrame(-bsred/2+bi/2+bwl,0,bsred,ts);
        shapeEff.add(new Area(r));
        
        
        Area anInf= new Area(pannelloPostPiattaInf.getShape(bi, ti, ii/2+i/2, irsPiattaInf, passoIrrigidimentiTrasv, mat, ts+hwl+ti/2));
        anInf.transform(AffineTransform.getRotateInstance(-Math.toRadians(90), 0, ts+hwl+ti/2));
        anInf.transform(AffineTransform.getTranslateInstance(-bi/2, 0));
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
    
    
    public double getJw(boolean faseZero, double n, double spSoletta, double hsoletta) {
        
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
    
    public double getAreaInternaSemispessore(boolean faseZero, double hsoletta) {
        
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
    
    public double getBtot() {
        return bi+2*hw*Math.sin(alfa)+bs;
    }
    
    
    public double[] getIst_Istmin_irrigTrasversale(Irrigidimento irTrasv,
            double passoIrrig, MaterialeAcciaio mat) {
        
        
        double e = mat.gete();
        ParametriStatici psTw= irTrasv.getParametriStatici(15*e*tw, 15*e*tw, tw, 1);
        ParametriStatici psBi= irTrasv.getParametriStatici(15*e*ti, 15*e*ti, ti, 1);
        
        double Itras1 = psTw.getJy();
        double Itras2 = psBi.getJy();
        
        //INERZIA MINIMA
        double Imin2=0;
        double Imin1=0;
        
        //anima tesa
        if(sEff<0 && iEff <0) {
            Imin1=0.000000000000000001;
        }
        //anima compressa
        else{
            if (passoIrrig / hw < Math.pow(2, 0.5)) {
                Imin1= 1.5 * Math.pow(hw, 3) * Math.pow(tw, 3) / Math.pow(passoIrrig, 2);
            } else {
                Imin1= 0.75 * hw * Math.pow(tw, 3);
            }
        }
        
        //piattabanda inf
        //tesa
        if(iEff <0) {
            Imin2=0.00000000000001;
        } else{
            if (passoIrrig / bi < Math.pow(2, 0.5)) {
                Imin2= 1.5 * Math.pow(bi, 3) * Math.pow(ti, 3) / Math.pow(passoIrrig, 2);
            } else {
                Imin2= 0.75 * bi * Math.pow(ti, 3);
            }
        }
        
        if(Itras1/Imin1<Itras2/Imin2)
            return new double[] {Itras1,Imin1};
        else
            return new double[] {Itras2,Imin2};
        
    }
    
    
    public Table getTabellaInput() throws BadElementException {
        Table table = new Table(2, 7);
        table.setBorder(Table.NO_BORDER);
        table.setPadding(2);
        table.setSpacing(2);
//		table.setDefaultHorizontalAlignment(Element.ALIGN_RIGHT);
        
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        int nc=0;
        
        Cell c = new Cell("Larghezza piattabande superiori: bs (mm)");
        c.setHorizontalAlignment(Element.ALIGN_LEFT);
        c.setBorder(Cell.NO_BORDER);
        table.addCell(c, nc, 0);
        
        c = new Cell(Double.toString(bs));
        c.setHorizontalAlignment(Element.ALIGN_RIGHT);
        c.setBorder(Cell.NO_BORDER);
        table.addCell(c, nc, 1);
        nc++;
        
        c = new Cell("Spessore piattabande superiori: ts (mm)");
        c.setBorder(Cell.NO_BORDER);
        c.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(c, nc, 0);
        
        c = new Cell(Double.toString(ts));
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
        
        c = new Cell("Spessore anima: tw (mm)");
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
        Table table = new Table(2, 3+irsAnime.size()+irsPiattaInf.size());
        table.setBorder(Table.NO_BORDER);
        table.setPadding(2);
        table.setSpacing(2);
        int nc = 0;
        
        c = MainRTFCreator.getCellaTipo("numero irrigidimenti anime", Cell.ALIGN_LEFT);
        table.addCell(c, nc, 0);
        c = MainRTFCreator.getCellaTipo(Integer.toString(irsAnime.size()), Cell.ALIGN_RIGHT);
        table.addCell(c, nc, 1);
        ++nc;
        for (int i = 0; i < irsAnime.size(); i++) {
            Irrigidimento ir = irsAnime.get(i);
            c = MainRTFCreator.getCellaTipo("Distanza irrigidimento estradosso anima", Cell.ALIGN_LEFT);
            table.addCell(c, nc, 0);
            c = MainRTFCreator.getCellaTipo(Double.toString(ir.getY()), Cell.ALIGN_RIGHT);
            table.addCell(c, nc, 1);
            ++nc;
        }
        c = MainRTFCreator.getCellaTipo("numero irrigidimenti piattabanda inferiore", Cell.ALIGN_LEFT);
        table.addCell(c, nc, 0);
        c = MainRTFCreator.getCellaTipo(Integer.toString(irsPiattaInf.size()), Cell.ALIGN_RIGHT);
        table.addCell(c, nc, 1);
        ++nc;
        for (int i = 0; i < irsPiattaInf.size(); i++) {
            Irrigidimento ir = irsPiattaInf.get(i);
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
        
        return table;
    }
    
    
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
        return nu3;
    }
    
    
    public Table getTabellaVerificaIrrigidimenti(double passoTrasv, MaterialeAcciaio mat,
            Irrigidimento irrTrasv) throws BadElementException {
        
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        
        
        Cell c;
        int ncol = 4;
        if(irsAnime.size()!=0 || irsPiattaInf.size()!=0) ncol+=2;
        Table table = new Table(2, ncol);
        table.setBorder(Table.NO_BORDER);
        table.setPadding(2);
        table.setSpacing(2);
        int nc = 0;
        
        
        if(irsAnime.size()!=0 || irsPiattaInf.size()!=0){
            Irrigidimento ir;
            if(irsAnime.size()!=0)ir = irsAnime.get(0);
            else ir = irsPiattaInf.get(0);
            
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
        c = MainRTFCreator.getCellaTipo("Sig,critico: tensione critica torsionale irrigidimenti trasversali(Mpa)", Cell.ALIGN_LEFT);
        table.addCell(c, nc, 0);
        c = MainRTFCreator.getCellaTipo(nf.format(irrTrasv.getSigCriticoTorsionale( mat, passoTrasv,tw)), Cell.ALIGN_RIGHT);
        table.addCell(c, nc, 1);
        ++nc;
        
        c = MainRTFCreator.getCellaTipo("Tensione ammissibile di confronto 6fy irrigidimenti trasversali(MPa)", Cell.ALIGN_LEFT);
        table.addCell(c, nc, 0);
        double sc = 6*mat.getFy(0);
        c = MainRTFCreator.getCellaTipo(nf.format(sc), Cell.ALIGN_RIGHT);
        table.addCell(c, nc, 1);
        ++nc;
        
        double[] Ist = getIst_Istmin_irrigTrasversale(irrTrasv, passoTrasv, mat);
        c = MainRTFCreator.getCellaTipo("Ist: rigidezza irrigidimenti trasversali piu porzione efficace di anima (mm4)", Cell.ALIGN_LEFT);
        table.addCell(c, nc, 0);
        c = MainRTFCreator.getCellaTipo(nf.format(Ist[0]), Cell.ALIGN_RIGHT);
        table.addCell(c, nc, 1);
        ++nc;
        
        c = MainRTFCreator.getCellaTipo("Ist,min: rigidezza minima richiesta irrigidimenti trasversali(mm4)", Cell.ALIGN_LEFT);
        table.addCell(c, nc, 0);
        c = MainRTFCreator.getCellaTipo(nf.format(Ist[1]), Cell.ALIGN_RIGHT);
        table.addCell(c, nc, 1);
        ++nc;
        
        double[] ws = getSigma_W_IrrigTrasversaliAnima(irrTrasv,passoTrasv);
        c = MainRTFCreator.getCellaTipo("Sigma,max: tensione massima di verifica irrigidimento trasversali (anime)", Cell.ALIGN_LEFT);
        table.addCell(c, nc, 0);
        c = MainRTFCreator.getCellaTipo(nf.format(ws[0]), Cell.ALIGN_RIGHT);
        table.addCell(c, nc, 1);
        ++nc;
        
        c = MainRTFCreator.getCellaTipo("W0: freccia massima irrigidimento trasversale(anime)", Cell.ALIGN_LEFT);
        table.addCell(c, nc, 0);
        c = MainRTFCreator.getCellaTipo(nf.format(ws[1]), Cell.ALIGN_RIGHT);
        table.addCell(c, nc, 1);
        ++nc;
        
        
        ws = getSigma_W_IrrigTrasversaliPiattaInf(irrTrasv,passoTrasv);
        c = MainRTFCreator.getCellaTipo("Sigma,max: tensione massima di verifica irrigidimento trasversali (piattabanda inf)", Cell.ALIGN_LEFT);
        table.addCell(c, nc, 0);
        c = MainRTFCreator.getCellaTipo(nf.format(ws[0]), Cell.ALIGN_RIGHT);
        table.addCell(c, nc, 1);
        ++nc;
        
        c = MainRTFCreator.getCellaTipo("W0: freccia massima irrigidimento trasversale (piattabanda inf)", Cell.ALIGN_LEFT);
        table.addCell(c, nc, 0);
        c = MainRTFCreator.getCellaTipo(nf.format(ws[1]), Cell.ALIGN_RIGHT);
        table.addCell(c, nc, 1);
        ++nc;
        
        return table;
    }
    
    public double getVbsd() {
        double Vv = Math.abs((Vsez/Math.cos(alfa))/2);
        double Vt = Math.abs(Vtorsione*hw);
        return Vt+Vv;
    }
    
    public double getVpiattaInf() {
        double Vt = Math.abs(Vtorsione*bi);
        return Vt;
    }
    private double[] getSigma_W_IrrigTrasversaliAnima(Irrigidimento irTrasv,double passo){
        double wl =Math.max(passo,hw)/300;
        double[] sm=pannelloPostAnima.getSigma_W_IrrigidTrasv(Math.max(sEff,iEff),getVbsd(),(Irrigidimento_T)irTrasv);
        return  new double[]{sm[0],sm[1],wl};
    }
    
    private double[]  getSigma_W_IrrigTrasversaliPiattaInf(Irrigidimento irTrasv,double passo){
        double wl =Math.max(passo,bi)/300;
        double[] sm= pannelloPostPiattaInf.getSigma_W_IrrigidTrasv(iEff,getVpiattaInf(),(Irrigidimento_T)irTrasv);
        return  new double[]{sm[0],sm[1],wl};
    }
    
    public double[] getSigma_W_IrrigTrasversali(Irrigidimento irt,double passo){
        
        double w1[]= getSigma_W_IrrigTrasversaliAnima(irt,passo);
        double w2[]= getSigma_W_IrrigTrasversaliPiattaInf(irt,passo);
        
        double sigma1 = w1[0];
        double sigma2 = w2[0];
        
        double wd1 = w1[1];
        double wd2 =w2[1];
        
        double wl1 = w1[2];
        double wl2 =w2[2];
        
 //       double wl = Math.max(w1[2],w2[2]);
        
        if(wl1>wd1) return new double[] {sigma1,wd1,wl1};
        else if (wl2>wd2) return new double[] {sigma2,wd2,wl2};
        else if (sigma1>sigma2)return new double[] {sigma1,wd1,wl1};
        else return new double[] {sigma2,wd2,wl2};
        
    }
    
    public double getVbwRd() {
        return VbwRd;
    }
    
    public double getVbfRd() {
        return VbfRd;
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
        
        //to Do
        
    }
    
    public double getTsup() {
        
        return ts;
        
    }
    
    public double getTinf() {
        
        return ti;
    }
}
