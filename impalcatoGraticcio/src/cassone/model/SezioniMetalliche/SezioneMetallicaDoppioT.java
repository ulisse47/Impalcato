package cassone.model.SezioniMetalliche;

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
import cassone.model.irrigidimenti.Irrigidimento_T;
import cassone.util.MainRTFCreator;
import cassone.view.grafica.Drawing;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

public class SezioneMetallicaDoppioT implements SezioneMetallica {
    
    private String name = "C1";
    
    // MISURE IN MILLIMETRI
    // piattabande sup in mm
    private double bs;
    
    private double ts;
    
    // anima inf in mm
    private double hw;
    
    private double tw;
    
    // piattabande inf in mm
    private double bi;
    
    private double ti;
    
    private ArrayList<Irrigidimento> irsAnima = new ArrayList<Irrigidimento>();
    
    @XStreamOmitField
    private double ss, ii, i, s;
    
   @XStreamOmitField
     private double sEff, iEff;
    
    //parametri statici efficaci
     @XStreamOmitField
     double wss, wii, Js, A, yg, ygEff;
    
    @XStreamOmitField
    private Area shapeEff=new Area();
    
    boolean piattabandaSupIrrigidita;
    
    // sollecitazioni
    double Nsez, Msez, Vsez;
    
    // resistenza a taglio
    double VbwRd, VbfRd, VbRd;
    
    double nuInterM_V,nu3;
    
    //bulloni
    double FSd_sup,FSd_inf,FSd_w;
    //resistenze bulloni
    double 	FbRdPiatp_sup,FbRdCoprig_sup,FvRd_sup;
    double FscrSLU_sup,FscrSLE_sup,FbRdPiatp_inf ,FbRdCoprig_inf;
    double FvRd_inf,FscrSLU_inf,FscrSLE_inf;
    double	FbRdPiatp_w ,FbRdCoprig_w,FvRd_w;
    double FscrSLU_w,FscrSLE_w;
    
    PannelloPostCritico pannelloPost;
    
    // private double b1eff, b2eff;
/*
    public double getYgEff(){return ygEff;}
    public double getJs(){return Js;}
    public double getA(){return A;}
 */
    
    public boolean isPiattabandaSupIrrigidita() {
        return piattabandaSupIrrigidita;
    }
    
    public void setPiattabandaSupIrrigidita(boolean piattabandaSupIrrigidita) {
        this.piattabandaSupIrrigidita = piattabandaSupIrrigidita;
    }
    
    public double getIEff() {
        return iEff;
    }
    
    public void setIEff(double eff) {
        iEff = eff;
    }
    
    public double getSEff() {
        return sEff;
    }
    
    public void setSEff(double eff) {
        sEff = eff;
    }
    
    public SezioneMetallicaDoppioT(String name) {
        this.name = name;
    }
    
    
    public SezioneMetallicaDoppioT() {
        this.name = "C1";
        this.bs = 800;
        this.bi = 700;
        this.ts = 30;
        this.ti = 40;
        this.hw = 2500-ts-ti;
        this.tw = 14;
        
    }
    
    public SezioneMetallicaDoppioT(double bs, double ts, double hw, double tw,
            double bi, double ti) {
        this.bs = bs;
        this.bi = bi;
        this.ts = ts;
        this.ti = ti;
        this.hw = hw;
        this.tw = tw;
    }
    
    public SezioneMetallicaDoppioT(SezioneMetallicaDoppioT sm, String name) {
        this.name = name;
        this.bs = sm.getBs();
        this.bi = sm.getBi();
        this.ts = sm.getTs();
        this.ti = sm.getTi();
        this.hw = sm.getHw();
        this.tw = sm.getTw();
        piattabandaSupIrrigidita = sm.isPiattabandaSupIrrigidita();
        
        int nirr = sm.getIrsAnima().size();
        for (int i = 0; i < nirr; i++) {
            Irrigidimento_L ir = (Irrigidimento_L) sm.getIrsAnima().get(i);
            Irrigidimento_L ir1 = new Irrigidimento_L(ir);
            this.irsAnima.add(ir1);
        }
        
    }
    
    public ParametriStatici getParametriStatici() {
        
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
        double Js = Jsup + Jinf + Jwanima + Asup * (ygs - ygsup)
        * (ygs - ygsup) + Aw * (ygs - ygw) * (ygs - ygw) + Ainf
                * (ygs - yginf) * (ygs - yginf);
        double Jw = (bs * ts * ts * ts + bi * ti * ti * ti + hw * tw * tw * tw) / 3;
        
        return new ParametriStatici(As, Js, ygs, Jw);
        
    }
    
    // parametrici statici della sezione fino a sotto piattabanda sup
    // metodo per il calcolo dei momenti statici Sn
    public ParametriStatici getParametriStaticiS1() {
        double as = bs * ts;
        double yg = ts / 2;
        double jx = bs * ts * ts * ts / 12;
        ParametriStatici s = new ParametriStatici(as, jx, yg, 0);
        return s;
    }
    
    // parametrici statici della sezione fino a piattabanda sup
    // metodo per il calcolo dei momenti statici Sn
    public ParametriStatici getParametriStaticiS2() {
        double a1 = bs * ts;
        double a2 = tw * hw;
        double yg1 = ts / 2;
        double yg2 = ts + hw / 2;
        double as = a1 + a2;
        double yg = (a1 * yg1 + a2 * yg2) / as;
        double jx = bs * ts * ts * ts / 12 + tw * hw * hw * hw / 12 + a1
                * (yg - yg1) * (yg - yg1) + a2 * (yg - yg2) * (yg - yg2);
        ParametriStatici s = new ParametriStatici(as, jx, yg, 0);
        return s;
    }
    
    public double getHtot() {
        return ts + ti + hw;
    }
    
    public double getYs1() {
        return ts;
    }
    
    public double getYs2() {
        return ts + hw;
    }
    
    public void setBi(double bi) {
        this.bi = bi;
    }
    
    public void setBs(double bs) {
        this.bs = bs;
    }
    
    public void setHw(double hw) {
        this.hw = hw;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setTi(double ti) {
        this.ti = ti;
    }
    
    public void setTs(double ts) {
        this.ts = ts;
    }
    
    public void setTw(double tw) {
        this.tw = tw;
    }
    
    public SezioneMetallica getCopia() {
        return new SezioneMetallicaDoppioT(bs, ts, hw, tw, bi, ti);
    }
    
    public double getBi() {
        return bi;
    }
    
    public double getBs() {
        return bs;
    }
    
    public double getHw() {
        return hw;
    }
    
    public String getName() {
        return name;
    }
    
    public double getTi() {
        return ti;
    }
    
    public double getTs() {
        return ts;
    }
    
    public double getTw() {
        return tw;
    }
    public double getTwTot() {
        return tw;
    }
    
    public void calcolaTensioniEfficaci(MaterialeAcciaio mat,
            SezioneOutputTensioniFase so, double passoIrrigidimentiTrasv,
            boolean rigidEndPost) {
        
        
        ss = so.getSs();
        ii = so.getIi();
        s = so.getS();
        i = so.getI();
        
        ArrayList<Irrigidimento> irs = getIrrigidimentiCompressi(s, i);
        pannelloPost = new PannelloPostCritico(hw, tw, 0, s, i, irs,
                passoIrrigidimentiTrasv, mat, ts);
        
        
        ParametriStatici psEff = getParametriEfficaci(mat,passoIrrigidimentiTrasv);
        ParametriStatici ps = getParametriStatici();
        
        double[] M = getM_N_SezioneMetallica();
        Msez = M[0];
        Nsez = M[1];
        Vsez = so.getVtaglio();
        
        Js = psEff.getJy();
        A = psEff.getA();
        ygEff = psEff.getYg();
        yg = ps.getYg();
        
        wss = Js / (ygEff - ts / 2);
        wii = Js / (ygEff - ts - hw - ti / 2);
        
        // momento aggiuntivo causato dalla nuova eccentricit�
        double DM = Nsez * (+ygEff - yg);
        double Mv = Msez + DM;
        
        // calcolo le nuove tensioni
        sEff = Mv / wss + Nsez / A;
        iEff = Mv / wii + Nsez / A;
        
        // resistenza a taglio
        calcolaResistenzaTaglio(passoIrrigidimentiTrasv, mat, rigidEndPost);
        
        creashapeEff(mat, passoIrrigidimentiTrasv);
        
    }
    
    private void calcolaResistenzaTaglio(double passoIrrig,
            MaterialeAcciaio mat, boolean rigidEndPost) {
        
        double e = mat.gete();
        
        double bsredDx = bs/2-tw/2;
        double bsredSx = bsredDx;
        
        double biredDx = bi/2-tw/2;
        double biredSx = biredDx;
        
        double[] Vloc = pannelloPost.getVbwRd_VbfRd_VbRd_nuInter_N_M(irsAnima, mat, hw, tw,
                0, bsredDx, bsredSx, biredDx, biredSx, ts, ti, passoIrrig, rigidEndPost, ss, ii, Math.abs(Vsez));
        
        VbwRd=Vloc[0];
        VbfRd=Vloc[1];
        VbRd=Vloc[2];
        nu3=Vloc[3];
        nuInterM_V=Vloc[4];
    }
    
    
    public double[] getM_N_SezioneMetallica() {
        
        ParametriStatici ps = getParametriStatici();
        double As = ps.getA();
        double ygs = ps.getYg();
        double Js = ps.getJy();
        
        double Htot = getHtot();
        
        // calcolo le tensioni in corrispondenza della sezione baric.
        double sigYg = (Htot - ygs) * (ss - ii) / Htot + ii;
        double Nconcio = sigYg * As;
        double ssC = ss - sigYg;
        double wssa = Js / (ygs);
        double Mconcio = ssC * wssa;
        
        return new double[] { Mconcio, Nconcio };
    }
    
    public ParametriStatici getParametriEfficaci(MaterialeAcciaio mat, double passoIrrigidimentiTrasv) {
        
        // piattabanda sub (mai instabilizzata!)
        double bsred = getBsRed(mat.gete());
        double Asup = bsred * ts;
        double ygsup = ts / 2;
        double Jsup = bsred * ts * ts * ts / 12;
        
        // paiattabanda inf
        double bired = getBiRed(mat.gete());
        double Ainf = bired * ti;
        double yginf = ts + hw + ti / 2;
        double Jinf = bired * ti * ti * ti / 12;
        
        // anima
        ParametriStatici pw = getParametriStaticiAnima(mat, s, i,
                passoIrrigidimentiTrasv);
        double Jwanima = pw.getJy();
        double Aw = pw.getA();
        double ygw = pw.getYg();
        
        // totale
        double As = Asup + Aw + Ainf;
        double ygs = 0;
        if (As != 0)
            ygs = (Asup * ygsup + Aw * ygw + Ainf * yginf) / As;
        double Js = Jsup + Jinf + Jwanima + Asup * (ygs - ygsup)
        * (ygs - ygsup) + Aw * (ygs - ygw) * (ygs - ygw) + Ainf
                * (ygs - yginf) * (ygs - yginf);
        
        return new ParametriStatici(As, Js, ygs, 0);
        
    }
    
    private ParametriStatici getParametriStaticiAnima(MaterialeAcciaio mat,
            double s, double i, double passoIrrigidimentiTrasv) {
        
//        ArrayList<Irrigidimento> irs = getIrrigidimentiCompressi(s, i);
        
/*        pannelloPost = new PannelloPostCritico(hw, tw, 0, s, i, irs,
                passoIrrigidimentiTrasv, mat, ts);
 */
        return pannelloPost.getParametriStaticiAnimaVerticale();
        
    }
    
    private double getSigmaAtY(double y) {
        double sigy = (ss - ii) / getHtot() * (getHtot() - y) + ii;
        return sigy;
    }
    
    private ArrayList<Irrigidimento> getIrrigidimentiCompressi(double s,
            double i) {
        ArrayList<Irrigidimento> irs = new ArrayList<Irrigidimento>();
        
        double yn;
        
        if (s > i) {
            yn = s * hw / (s - i);
            for (int j = 0; j < irsAnima.size(); j++) {
                Irrigidimento ir = irsAnima.get(j);
                if (ir.getY() < yn) {
                    if (irs.size() != 0) {
                        Irrigidimento il = irs.get(0);
                        if (il.getY() < ir.getY())
                            irs.add(ir);
                        else
                            irs.add(0, ir);
                    } else {
                        irs.add(ir);
                    }
                }
            }
            
        } else {
            yn = i * hw / (i - s);
            double hn = hw - yn;
            for (int j = 0; j < irsAnima.size(); j++) {
                Irrigidimento ir = irsAnima.get(j);
                if (ir.getY() > hn) {
                    if (irs.size() != 0) {
                        Irrigidimento il = irs.get(0);
                        if (il.getY() > ir.getY())
                            irs.add(ir);
                        else
                            irs.add(0, ir);
                    } else {
                        irs.add(ir);
                    }
                }
            }
        }
        
        return irs;
    }
    
    private double getBsRed(double e) {
        
        if (piattabandaSupIrrigidita)
            return bs;
        
        double s1 = ss / 2 + s / 2;
        if (s1 > 0) {
            double ks = 0.43;
            double lamdaP = ((bs / 2) / ts) / (28.4 * e * Math.pow(ks, 0.5));
            double ro = Math.min((lamdaP - 0.188) / (lamdaP * lamdaP), 1);
            return ro * bs;
        } else
            return bs;
        
    }
    
    private double getBiRed(double e) {
        // calcolo per la piattab. inf
        
        double s1 = ii / 2 + i / 2;
        if (s1 > 0) {
            double ks = 0.43;
            double lamdaP = ((bi / 2) / ti) / (28.4 * e * Math.pow(ks, 0.5));
            double ro = Math.min((lamdaP - 0.188) / (lamdaP * lamdaP), 1);
            return ro * bi;
        } else
            return bi;
        
    }
    
    public int getClasseSezione(MaterialeAcciaio mat) {
        // TODO Auto-generated method stub
        return 0;
    }
    
    public double getIeff() {
        
        return iEff;
    }
    
    public double getNuInterM_V() {
        return nuInterM_V;
    }
    
    public double getSeff() {
        return sEff;
    }
    
    public double getVbRd() {
        return VbRd;
    }
    
    public double getFSd(int nPannello) {
        switch (nPannello){
            case 0:
                return FSd_sup;
            case 1:
                return FSd_w;
            case 2:
                return FSd_inf;
        }
        
        return 0;
    }
    
    
    public double getFbRdCoprig(int nPannello) {
        switch (nPannello){
            case 0:
                return FbRdCoprig_sup;
            case 1:
                return FbRdCoprig_w;
            case 2:
                return FbRdCoprig_inf;
        }
        
        return 0;
    }
    
    public double getFbRdPiatp(int nPannello) {
        switch (nPannello){
            case 0:
                return FbRdPiatp_sup;
            case 1:
                return FbRdPiatp_w;
            case 2:
                return FbRdPiatp_inf;
        }
        
        return 0;
    }
    
    public double getFscrSLE(int nPannello) {
        switch (nPannello){
            case 0:
                return FscrSLE_sup;
            case 1:
                return FscrSLE_w;
            case 2:
                return FscrSLE_inf;
        }
        
        return 0;
    }
    
    public double getFscrSLU(int nPannello) {
        switch (nPannello){
            case 0:
                return FscrSLU_sup;
            case 1:
                return FscrSLU_w;
            case 2:
                return FscrSLU_inf;
        }
        return 0;
    }
    
    public double getFvRd(int nPannello) {
        switch (nPannello){
            case 0:
                return FvRd_sup;
            case 1:
                return FvRd_w;
            case 2:
                return FvRd_inf;
        }
        return 0;
        
    }
    
    public String toString() {
        return name;
    }
    
    public Shape getShape() {
        
        GeneralPath pat = new GeneralPath();
        pat.moveTo(-bs / 2, 0);
        pat.lineTo(-bs / 2, ts);
        pat.lineTo(-tw / 2, ts);
        pat.lineTo(-tw / 2, ts + hw);
        pat.lineTo(-bi / 2, ts + hw);
        pat.lineTo(-bi / 2, ts + hw + ti);
        pat.lineTo(bi / 2, ts + hw + ti);
        pat.lineTo(bi / 2, ts + hw);
        pat.lineTo(tw / 2, ts + hw);
        pat.lineTo(tw / 2, ts);
        pat.lineTo(bs / 2, ts);
        pat.lineTo(bs / 2, 0);
        pat.closePath();
        return pat;
    }
    
    public void quota(Graphics2D g2d, int ddquote) {
        AffineTransform old = g2d.getTransform();
        
        double ht = ts + hw + ti;
        
        // quote verticali
        AffineTransform at = new AffineTransform();
        at.setToTranslation(Math.min(-bs / 2, -bi / 2) - 8 * ddquote, ht);
        g2d.transform(at);
        at.setToRotation(-Math.PI / 2);
        g2d.transform(at);
        Drawing.disegnaQuotaTraDuePunti(0, ht, "", ddquote, g2d);
        
        // spessori piattabande inferiore
        g2d.setTransform(old);
        at.setToTranslation(bi / 4, ht);
        g2d.transform(at);
        Line2D ln = new Line2D.Double(0, 0, ddquote, 3 * ddquote);
        Drawing.leader(0, 0, 0, 2 * ddquote, 2 * ddquote, Double.toString(bi)
        + "X" + Double.toString(ti) + "mm", g2d);
        // anima
        ln.setLine(-bi / 4, -ht / 2, 0 + ht / 7, -ht / 2);
        g2d.draw(ln);
        g2d.drawString(Double.toString(hw) + "X" + Double.toString(tw) + " mm",
                (int) (ht / 7), (int) (-ht / 2));
        
        // superiore
        g2d.setTransform(old);
        at.setToTranslation(bs / 2.5, ts);
        g2d.transform(at);
        Drawing.leader(0, 0, 0, 2 * ddquote, 2 * ddquote, Double.toString(bs)
        + "X" + Double.toString(ts) + "mm", g2d);
        
        g2d.setTransform(old);
        
    }
    
    public ArrayList<Irrigidimento> getIrsAnima() {
        return irsAnima;
    }
    
    public void setIrsAnima(ArrayList<Irrigidimento> irsAnima) {
        this.irsAnima = irsAnima;
    }
    
    public void creashapeEff(MaterialeAcciaio mat,
            double passoIrrigidimentiTrasv) {
        
        double bsred, bired;
        shapeEff = new Area();
        
        ArrayList<Irrigidimento> irs = getIrrigidimentiCompressi(ss, ii);
        
        Shape an = pannelloPost.getShape(hw, tw, 0, s, i, irs,
                passoIrrigidimentiTrasv, mat, ts);
        shapeEff.add(new Area(an));
        
        bired = getBiRed(mat.gete());
        bsred = getBsRed(mat.gete());
        
        Rectangle2D.Double r = new Rectangle2D.Double();
        r.setFrame(-bsred / 2, 0, bsred, ts);
        shapeEff.add(new Area(r));
        
        r.setFrame(-bired / 2, ts + hw, bired, ti);
        shapeEff.add(new Area(r));
        
    }
    
    public Shape getShapeEff() {
        return shapeEff;
        
    }
    
    public Shape getShapeIrrigidita() {
        
        Area a = new Area(getShape());
        
        for (int i = 0; i < irsAnima.size(); i++) {
            Irrigidimento ir = irsAnima.get(i);
            Area sh = new Area(ir.getShape(tw));
            sh.transform(AffineTransform.getTranslateInstance(0,ts));
            a.add(new Area(sh));
        }
        
        return a;
    }
    
    public boolean isSezioneChiusa() {
        return false;
    }
    
    public double getJw(boolean faseZero,double n, double spSoletta,double hsoletta) {
        return 0;
    }
    
    public double getBtot() {
        return Math.max(bs, bi);
    }
    
    public double getAreaInternaSemispessore(boolean faseZero,double hsoletta) {
        return 0;
    }
    
    
    public double[] getIst_Istmin_irrigTrasversale(Irrigidimento irTrasv,
            double passoIrrig, MaterialeAcciaio mat) {
        
        double e = mat.gete();
        ParametriStatici psTw= irTrasv.getParametriStatici(15*e*tw, 15*e*tw, tw, 1);
        
        double Itras = psTw.getJy();
        
        //INERZIA MINIMA
        double Imin=0;
        
        if(iEff <0 && sEff<0) {
            Imin=0;
        } else{
            if (passoIrrig / hw < Math.pow(2, 0.5)) {
                Imin= 1.5 * Math.pow(hw, 3) * Math.pow(tw, 3) / Math.pow(passoIrrig, 2);
            } else {
                Imin= 0.75 * hw * Math.pow(tw, 3);
            }
        }
        
        
        return new double[] {Itras,Imin};
    }
    
    public double getSigCriticoTorsionale(Irrigidimento ir, MaterialeAcciaio mat,double h,double tw) {
        double E = mat.getE();
        double G = mat.getG();
        double It = ir.getTorsionalConstantSVenant();
        double Ip = ir.getPolarSecondMoment();
        double Iw = ir.getWarpingCostant(tw);
        
        return (G * It + Math.pow(Math.PI, 2) * E * Iw / Math.pow(hw, 2)) / Ip;
        
    }
    
    public Table getTabellaInput() throws BadElementException {
        
        Table table = new Table(2, 6);
        table.setBorder(Table.NO_BORDER);
        table.setPadding(2);
        table.setSpacing(2);
//		table.setDefaultHorizontalAlignment(Element.ALIGN_RIGHT);
        
//		nf.setMinimumFractionDigits(2);
        int nc=0;
        
        Cell c = new Cell("Larghezza piattabanda superiore: bs (mm)");
        c.setHorizontalAlignment(Element.ALIGN_LEFT);
        c.setBorder(Cell.NO_BORDER);
        table.addCell(c, nc, 0);
        
        c = new Cell(Double.toString(bs));
        c.setHorizontalAlignment(Element.ALIGN_RIGHT);
        c.setBorder(Cell.NO_BORDER);
        table.addCell(c, nc, 1);
        nc++;
        
        c = new Cell("Spessore piattabanda superiore: ts (mm)");
        c.setBorder(Cell.NO_BORDER);
        c.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(c, nc, 0);
        
        c = new Cell(Double.toString(ts));
        c.setBorder(Cell.NO_BORDER);
        c.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(c, nc, 1);
        nc++;
        
        
        c = new Cell("Altezza anima: hw (mm)");
        c.setBorder(Cell.NO_BORDER);
        c.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(c, nc, 0);
        
        
        c = new Cell(Double.toString(hw));
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
        
        return table;
    }
    
    public Table getTabellaIrrigidimenti(double passoTrasv) throws BadElementException {
        
        Cell c;
        Table table = new Table(2, 2+irsAnima.size());
        table.setBorder(Table.NO_BORDER);
        table.setPadding(2);
        table.setSpacing(2);
        int nc = 0;
        
        c = MainRTFCreator.getCellaTipo("numero irrigidimenti longitudinali anime", Cell.ALIGN_LEFT);
        table.addCell(c, nc, 0);
        c = MainRTFCreator.getCellaTipo(Integer.toString(irsAnima.size()), Cell.ALIGN_RIGHT);
        table.addCell(c, nc, 1);
        ++nc;
        for (int i = 0; i < irsAnima.size(); i++) {
            Irrigidimento ir = irsAnima.get(i);
            c = MainRTFCreator.getCellaTipo("Distanza irrigidimento estradosso anima", Cell.ALIGN_LEFT);
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
    
    public void setMsez(double msez) {
        Msez = msez;
    }
    
    public double getNsez() {
        return Nsez;
    }
    
    public void setNsez(double nsez) {
        Nsez = nsez;
    }
    
    public double getVsez() {
        return Vsez;
    }
    
    public void setVsez(double vsez) {
        Vsez = vsez;
    }
    public double getNu3() {
        return nu3;
    }
    
    public Table getTabellaVerificaIrrigidimenti(double passoTrasv, MaterialeAcciaio mat,
            Irrigidimento irrTrasv) throws BadElementException {
        
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        
        
        Cell c;
        int ncol=4;
        if(irsAnima.size()!=0) ncol+=2;
        Table table = new Table(2, ncol);
        table.setBorder(Table.NO_BORDER);
        table.setPadding(2);
        table.setSpacing(2);
        int nc = 0;
        
        if(irsAnima.size()!=0){
            Irrigidimento ir = irsAnima.get(0);
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
        c = MainRTFCreator.getCellaTipo(nf.format(irrTrasv.getSigCriticoTorsionale( mat, hw,tw)), Cell.ALIGN_RIGHT);
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
        
        double[] ws = getSigma_W_IrrigTrasversali(irrTrasv,passoTrasv);
        c = MainRTFCreator.getCellaTipo("Sigma,max: tensione massima di verifica irrigidimento trasversali", Cell.ALIGN_LEFT);
        table.addCell(c, nc, 0);
        c = MainRTFCreator.getCellaTipo(nf.format(ws[0]), Cell.ALIGN_RIGHT);
        table.addCell(c, nc, 1);
        ++nc;
        
        c = MainRTFCreator.getCellaTipo("W0: freccia massima irrigidimento trasversale", Cell.ALIGN_LEFT);
        table.addCell(c, nc, 0);
        c = MainRTFCreator.getCellaTipo(nf.format(ws[1]), Cell.ALIGN_RIGHT);
        table.addCell(c, nc, 1);
        ++nc;
        
        c = MainRTFCreator.getCellaTipo("Wlim: freccia limite irrigidimento trasversale", Cell.ALIGN_LEFT);
        table.addCell(c, nc, 0);
        c = MainRTFCreator.getCellaTipo(nf.format(ws[2]), Cell.ALIGN_RIGHT);
        table.addCell(c, nc, 1);
        ++nc;
        
        
        return table;
    }
    
    public double getVbsd() {
        return Vsez;
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
    
    //giunti bullonati
    private double getFSd_inf(GiuntoBullonato giunto, SezioneOutputTensioniFase so) {
        
        double ii = so.getIi();
        double i = so.getI();
        
        int nb = giunto.getNfile1Inf()*giunto.getNfile2Inf()*2;
        double Fs = bi*ti*(ii+i)/2;
        return Math.abs(Fs/nb);
    }
    
    private double getFSd_sup(GiuntoBullonato giunto, SezioneOutputTensioniFase so) {
        double ss = so.getSs();
        double s = so.getS();
        
        int nb = giunto.getNfile1Sup()*2*giunto.getNfile2Sup();
        double Fs = bs*ts*(ss+s)/2;
        return Math.abs(Fs/nb);
    }
    
    //sollecitazioni massime bulloni
    private double getFSd_w(GiuntoBullonato giunto, SezioneOutputTensioniFase so) {
        
        double s = so.getS();
        double i = so.getI();
        
        double sN = (s+i)/2;
        double sM = (s-i)/2;
        double N = sN*tw*hw;
        double M = sM*tw*hw*hw/6;
        double V =so.getVtaglio();
        double p2 =giunto.getP2W();
        double h = (giunto.getNfile2W()-1)*giunto.getP2W();
        double y = h/2;
        double yq =0;
        do {
            yq+=y*y;
            y-=p2;
        } while (y>0);
        
        yq = 2*yq *giunto.getNfile1W();
        int nb = giunto.getNfile1W()*giunto.getNfile2W();
        double Fmax = Math.abs(N/nb)+Math.abs(M*(h/2)/(yq));
        double Vmax = V/nb;
        return Math.pow(Fmax*Fmax+Vmax*Vmax,0.5);
    }
    
    public void calcolaBulloni(SezioneOutputTensioniFase so, Sezione sez,MaterialeAcciaio mat, GiuntoBullonato g){
        
        
        FSd_sup=getFSd_sup(g,so);
        FSd_inf=getFSd_inf(g,so);
        FSd_w=getFSd_w(g,so);
        
        //resistenze bulloni
        FbRdPiatp_sup = g.getFbRd_PiattabandaSup(sez, mat);
        FbRdCoprig_sup=g.getFbRd_CoprigiuntoSup();
        FvRd_sup=g.getFvb_sup();
        FscrSLU_sup=g.getFscr_sup(true);
        FscrSLE_sup=g.getFscr_sup(false);
        
        FbRdPiatp_inf = g.getFbRd_PiattabandaInf(sez,mat);
        FbRdCoprig_inf=g.getFbRd_CoprigiuntoInf();
        FvRd_inf=g.getFvb_inf();
        FscrSLU_inf=g.getFscr_inf(true);
        FscrSLE_inf=g.getFscr_inf(false);
        
        FbRdPiatp_w = g.getFbRd_PiattabandaW(sez, mat);
        FbRdCoprig_w=g.getFbRd_CoprigiuntoW(mat);
        FvRd_w=g.getFvb_w();
        FscrSLU_w=g.getFscr_w(true);
        FscrSLE_w=g.getFscr_w(false);
        
    }
    public double getTsup() {
        
        return ts;
        
    }
    
    public double getTinf() {
        
        return ti;
    }
    
    public double[] getSigma_W_IrrigTrasversali(Irrigidimento irTrasv,double passo){
        
        double wl =Math.max(passo,hw)/300;
        double sm[]=pannelloPost.getSigma_W_IrrigidTrasv(Math.max(sEff,iEff),Vsez,(Irrigidimento_T)irTrasv);
        return  new double[]{sm[0],sm[1],wl};
    }
    
    
}
