package cassone.model;

import cassone.model.irrigidimenti.Irrigidimento_T;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import cassone.model.irrigidimenti.Irrigidimento;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company:
 * </p>
 *
 * @author not attributable
 * @version 1.0
 */

public class PannelloPostCritico {
    
    private double bef1, bef2;
    
    private double roc;
    
    private double Aceff,Acloc,Aceffloc;
    
    // comportamento plate
    double sigP;
    double roPlate;
    double lamdaP;
    double lamdaW;
    
    //instabilita tipo "column"
    double sigC;
    double chiColumn;
    
    double psi,yn;
    
    //bordi anima collaboranti b1 e b2 (b2 verso zona tesa)
    double b1,b2;
    
    double hw, tw ,alfa, ss,ii;
    ArrayList<Irrigidimento> irs;
    double passoIrrTrasv;
    MaterialeAcciaio mat;
    double Dy;
    
   public PannelloPostCritico(){
        this.hw =14;
        this.tw =10;
        this.alfa =20;
        this.ss =0;
        this.ii =0;
        this.irs = null;
        this.passoIrrTrasv=2000;
        this.mat=null;
        this.Dy =500;
      }

    public PannelloPostCritico(double hw, double tw, double alfa, double ss,
            double ii, ArrayList<Irrigidimento> irs, double passoIrrTrasv,
            MaterialeAcciaio mat, double Dy){
        
        this.hw =hw;
        this.tw =tw;
        this.alfa =alfa;
        this.ss =ss;
        this.ii =ii;
        this.irs = irs;
        this.passoIrrTrasv=passoIrrTrasv;
        this.mat=mat;
        this.Dy =Dy;
        
    }
    
    //anime non orizzonatli
    public  Shape getShape(double hw, double tw, double alfa, double ss,
            double ii, ArrayList<Irrigidimento> irs, double passoIrrTrasv,
            MaterialeAcciaio mat, double Dy) {
        
        Area a = new Area();
        
        if(ss<=0 && ii <= 0){
            Rectangle2D.Double r = new Rectangle2D.Double();
            r.setFrame(-tw/2,Dy,tw,hw);
            return new Area(r);
        }
        
        if (irs.size() != 0) {
            double psi = getPsi(ss, ii);
            double yn = getYn(ss, ii, hw);
            double bTeso =hw- yn;
            if(bTeso>hw ) bTeso=hw;
            if(bTeso<0 ) bTeso=0;
            
            
            getParametriStaticiAnimaVert_NonIrrigidita();
            double[] bedge = setBsup_Binf_ReturnBedge();
            double b1 = bedge[0];
            double b2 = bedge[1] + bTeso;
            Rectangle2D.Double r = new Rectangle2D.Double();
            
            if (ss > ii) {
                r.setFrame(-tw / 2, Dy, tw, b1);
                a.add(new Area(r));
                r.setFrame(-tw / 2, hw - b2 + Dy, tw, b2);
                a.add(new Area(r));
            } else {
                r.setFrame(-tw / 2, Dy, tw, b2);
                a.add(new Area(r));
                r.setFrame(-tw / 2, hw - b1 + Dy, tw, b1);
                a.add(new Area(r));
            }
            
            for (int i = 0; i < irs.size(); i++) {
                Irrigidimento ir = irs.get(i);
                Area si =new Area(ir.getShapeEff(tw, roc));
                si.transform(AffineTransform.getTranslateInstance(0, Dy));
                a.add(si);
            }
            
            return a;
        } else {
            getParametriStaticiAnimaVert_NonIrrigidita();
            // if(ss>ii){
            Rectangle2D.Double r = new Rectangle2D.Double();
            r.setFrame(-tw / 2, Dy, tw, bef1);
            a.add(new Area(r));
            r.setFrame(-tw / 2, Dy + hw - bef2, tw, bef2);
            a.add(new Area(r));
                        /*
                         * }else{ Rectangle2D.Double r= new Rectangle2D.Double();
                         * r.setFrame(-tw/2, Dy, tw, bef2); a.add(new Area(r));
                         * r.setFrame(-tw/2, Dy+hw-bef1, tw, bef1); a.add(new Area(r)); }
                         */
            return a;
        }
    }
    
    // anime orizzontali
    public  Shape getShape(double hw, double tw, double ss,
            ArrayList<Irrigidimento> irs, double passoIrrTrasv,
            MaterialeAcciaio mat, double Dy) {
        
        Area a = new Area();
        
        if(ss<=0) {
            
            Rectangle2D.Double r = new Rectangle2D.Double();
            r.setFrame(-tw / 2, Dy, tw, hw);
            a.add(new Area(r));
            
            return a;
        }
        
        if (irs.size() == 0) {
            double e = mat.gete();
            
            // chek per l'anima NB: compressioni positive
            double bef = 0;
            double psi = 1;
            double ks = 4.0;
            
            double lamdaP = (hw / tw) / (28.4 * e * Math.pow(ks, 0.5));
            double ro = 1;
            if (lamdaP > 0.673) {
                ro = Math.min((lamdaP - 0.055 * (3 + psi))
                / Math.pow(lamdaP, 2), 1);
            }
            
            bef = ro * hw;
            bef1 = 0.5 * bef;
            bef2 = bef1;
            
            Rectangle2D.Double r = new Rectangle2D.Double();
            r.setFrame(-tw / 2, Dy, tw, bef1);
            a.add(new Area(r));
            r.setFrame(-tw / 2, hw - bef2 + Dy, tw, bef2);
            a.add(new Area(r));
            
            return a;
            
        } else {
            
            double psi = getPsi(ss, ss);
            double yn = Double.MAX_VALUE;
            
            double bTeso = Math.max(hw - yn, 0);
            
            getParametriStaticiAnimaVert_NonIrrigidita();
            double[] bedge = setBsup_Binf_ReturnBedge();
            double b1 = bedge[0];
            double b2 = bedge[1] + bTeso;
            
            Rectangle2D.Double r = new Rectangle2D.Double();
            r.setFrame(-tw*roc / 2, Dy, tw*roc, b1);
            a.add(new Area(r));
            r.setFrame(-tw*roc / 2, hw - b2 + Dy, tw*roc, b2);
            a.add(new Area(r));
            
            for (int i = 0; i < irs.size(); i++) {
                Irrigidimento ir = irs.get(i);
                Area si =new Area( ir.getShapeEff(tw, roc));
                si.transform(AffineTransform.getTranslateInstance(0,Dy));
                a.add(new Area(si));
            }
            
            return a;
        }
        
    }
    
    
    private  double getPsi(double ss, double ii) {
        double s1 = Math.max(ss, ii);
        double s2 = Math.min(ss, ii);
        return s2 / s1;
        
    }
    
    
    //caso anima verticale irrigidita
    public ParametriStatici getParametriStaticiAnimaVerticale() {
        
        // anima tesa
        if (ss <= 0 && ii <= 0) {
            lamdaW=0;
            lamdaP=0;
            sigC=Double.MAX_VALUE;
            sigP=Double.MAX_VALUE;
            double hl = hw * Math.cos(alfa);
            return new ParametriStatici(hw * tw, (tw / Math.cos(alfa)) * hl
                    * hl * hl / 12, hl / 2 + Dy, 0);
        }
        
        //nessun irrigidimentto
        if (irs.size() == 0)
            return getParametriStaticiAnimaVert_NonIrrigidita();
        
        psi = getPsi(ss, ii);
        yn = getYn(ss, ii, hw);
        
        double bTeso = Math.max(hw - yn, 0);
        
        double[] bedge = setBsup_Binf_ReturnBedge();
        b1 = bedge[0];
        b2 = bedge[1] + bTeso;
        
        Acloc = getAcLoc(false);
        Aceffloc = getAcLoc(true);
        
        // comportamento plate
        sigP = getSigP(irs, hw, tw, ss, ii, passoIrrTrasv, mat);
        roPlate = getRoPlatebehaviour(sigP, psi, mat, Aceffloc, Acloc);
        
        // comportamento column
        double[] v = getSigC_nIrrig(hw, ss, ii, tw, irs, passoIrrTrasv, mat);
        sigC = v[0];
        int nIrr = (int) v[1];
        chiColumn = getChiColumn(tw, sigC, mat, irs.get(nIrr));
        
        // interazione
        roc = getRocColumnPlateInteraction(roPlate, chiColumn, sigP,
                sigC);
        
        Aceff= Aceffloc*roc+b1*tw;
        
        int ns = irs.size() + 2;
        double[] a = new double[ns];
        double[] y = new double[ns];
        double[] jx = new double[ns];
        double[] jxy = new double[ns];
        
        
        //momenti nella direzione perpendicolare
        double[] ap = new double[ns];
        double[] x = new double[ns];
        double[] xp = new double[ns];
        double[] Jy = new double[ns];
        
        for (int i = 0; i < irs.size(); i++) {
            Irrigidimento ir = irs.get(i);
            ParametriStatici ps = ir.getParametriStaticiEffGlobali(tw, roc);
            ParametriStatici psn = ir.getParametriStaticiEff(tw, roc);
            
            a[i] = ps.getA();
            y[i] = ps.getYg();
            jx[i] = ps.getJy();
            jxy[i] = ps.getJw();
            
            
            ap[i] = psn.getA();
            x[i] = psn.getYg();
            Jy[i] = psn.getJy();
        }
        
        a[ns - 1] = b1 * tw;
        jx[ns - 1] = b1 * b1 * b1 * tw / 12;
        jxy[ns-1]=0;
        if(ss>ii)
            y[ns - 1] = b1 / 2;
        else
            y[ns - 1] = hw - b1 / 2;
        
        ap[ns - 1] = b1 * tw;
        Jy[ns - 1] = tw * tw * tw * b1 / 12;
        x[ns - 1] = 0;
        
        
        a[ns - 2] = b2 * tw;
        jxy[ns-2]=0;
        jx[ns - 2] = b2 * b2 * b2 * tw / 12;
        if(ss>ii)
            y[ns - 2] = hw - b2 / 2;
        else
            y[ns - 2] = b2 / 2;
        
        ap[ns - 2] = b2 * tw;
        Jy[ns - 2] = b2 * tw * tw * tw / 12;
        x[ns - 2] = 0;
        
        double atot = 0;
        double yg = 0;
        double jtot = 0;
        double atotp = 0;
        double xg = 0;
        double jtotp = 0;
        double jxytot = 0;
        // calcolo baricentro
        double axy = 0;
        double axyp = 0;
        
        for (int i = 0; i < ns; i++) {
            atot += a[i];
            axy += a[i] * y[i];
            atotp += ap[i];
            axyp += ap[i] * x[i];
        }
        yg = axy / atot;
        xg = axyp / atotp;
        
        // calcolo momento inerzia
        for (int i = 0; i < ns; i++) {
            jtot += jx[i] + a[i] * (y[i] - yg) * (y[i] - yg);
            jtotp += Jy[i] + ap[i] * (x[i] - xg) * (x[i] - xg);
            jxytot += jxy[i]+a[i]*(y[i] - yg)*(x[i] - xg);
        }
        
        jtot = jtot * Math.cos(alfa)* Math.cos(alfa)+ jtotp* Math.sin(alfa)* Math.sin(alfa)
        -2*jxytot*Math.cos(alfa)* Math.sin(alfa);
        
        yg = yg * Math.cos(alfa)-xg * Math.sin(alfa);
        
        yg += Dy+(tw/2)*Math.sin(alfa);
        
        return new ParametriStatici(atot, jtot, yg, 0);
    }
    
    
    //ordina irrigidimenti per y crescente
    /*private void ordinaIrrigidimenti(){
        ArrayList<Irrigidimento> irsLoc = new ArrayList<Irrigidimento>();

        int nir=irs.size();
        for(int i=0; i<nir;++i)  irsLoc.add(irs.get(i));
        irs.clear();

        while (irsLoc.size()>0){
            nir=irsLoc.size();
            double minY=Double.MAX_VALUE;
            for(int i=0; i<nir;++i){
                double yi=irsLoc.get(i).getY();
                minY=Math.min(minY,yi);
            }
            for(int i=0; i<nir;++i){
                Irrigidimento iri = irsLoc.get(i);
                double yi=iri.getY();
                if(yi==minY){
                    irs.add(iri);
                    irsLoc.remove(iri);
                    i=nir;
                }
            }
        }
        
        
    }
    */
    // parametri statici anime orizzontali
    public ParametriStatici getParametriStaticiAnimaOrizzontale() {
        
        // anima tesa
        if (ss <= 0) {
            lamdaW=0;
            lamdaP=0;
            sigC=Double.MAX_VALUE;
            sigP=Double.MAX_VALUE;
            return new ParametriStatici(hw * tw, tw * tw * tw * hw / 12, tw / 2
                    + Dy, 0);
        }
        
        if (irs.size() == 0)
            return getParametriStaticiAnimaOrizz_NonIrrigid();
        
        
//        ordinaIrrigidimenti();
        
        psi = getPsi(ss, ss);
        yn = Double.MAX_VALUE;
        
        double bTeso = Math.max(hw - yn, 0);
        
        double[] bedge = setBsup_Binf_ReturnBedge();
        b1 = bedge[0];
        b2 = bedge[1] + bTeso;
        
        Acloc = getAcLoc(false);
        Aceffloc = getAcLoc( true);
        
        // comportamento plate
        sigP = getSigP(irs, hw, tw, ss, ss, passoIrrTrasv, mat);
        roPlate = getRoPlatebehaviour(sigP, psi, mat, Aceffloc, Acloc);
        
        // comportamento column
        double[] v = getSigC_nIrrig(hw, ss, ss, tw, irs, passoIrrTrasv, mat);
        sigC = v[0];
        int nIrr = (int) v[1];
        chiColumn = getChiColumn(tw, sigC, mat, irs.get(nIrr));
        
        // interazione
        roc = getRocColumnPlateInteraction(roPlate, chiColumn, sigP,
                sigC);
        
        int ns = irs.size() + 2;
        double[] a = new double[ns];
        double[] y = new double[ns];
        double[] j = new double[ns];
        
        for (int i = 0; i < irs.size(); i++) {
            Irrigidimento ir = irs.get(i);
            ParametriStatici ps = ir.getParametriStaticiEff(tw, roc);
            a[i] = ps.getA();
            y[i] = ps.getYg();
            j[i] = ps.getJy();
        }
        
        a[ns - 1] = b1 * tw;
        j[ns - 1] = tw * tw * tw * b1 / 12;
        y[ns - 1] = 0;
        a[ns - 2] = b2 * tw;
        j[ns - 2] = tw * tw * tw * b2 / 12;
        y[ns - 2] = 0;
        
        double atot = 0;
        double yg = 0;
        double jtot = 0;
        // calcolo baricentro
        double axy = 0;
        for (int i = 0; i < ns; i++) {
            atot += a[i];
            axy += a[i] * y[i];
        }
        yg = axy / atot;
        // calcolo momento inerzia
        for (int i = 0; i < ns; i++) {
            jtot += j[i] + a[i] * (y[i] - yg) * (y[i] - yg);
        }
        
        yg = Dy + tw / 2-yg;
        
        return new ParametriStatici(atot, jtot, yg, 0);
    }
    
    //parametri static anima non irrigidita verticale
    private ParametriStatici getParametriStaticiAnimaVert_NonIrrigidita() {
        
        bef1 = 0;
        bef2 = 0;
        
        double Aw, Jw, ygw;
        
        double e = mat.gete();
        
        // chek per l'anima NB: compressioni positive
        double bef = 0;
        psi = getPsi(ss,ii);
        double ks = 0;
        
        // valuto se è il filo superiore od inferiore che è soggetto
        // a tensioni di compressione
        boolean fibreSupCompresse = ss > ii;
        ks = getKsig(psi, true);
        
        lamdaP = (hw / tw) / (28.4 * e * Math.pow(ks, 0.5));
        sigP = mat.getFy(tw)/(lamdaP*lamdaP);
        sigC = Math.PI*Math.PI*mat.getE()*tw*tw/(12*(1-mat.getNi()*mat.getNi())*Math.pow(passoIrrTrasv,2));
        
        double ro = 1;
        if (lamdaP > 0.673) {
            ro = Math
                    .min((lamdaP - 0.055 * (3 + psi)) / Math.pow(lamdaP, 2), 1);
        }
        
        if (psi == 1) {
            bef = ro * hw;
            bef1 = 0.5 * bef;
            bef2 = bef1;
        } else if (psi > 0 && psi <= 1) {
            bef = ro * hw;
            if (fibreSupCompresse) {
                bef1 = bef * 2 / (5 - psi);
                bef2 = bef - bef1;
            } else {
                bef2 = bef * 2 / (5 - psi);
                bef1 = bef - bef2;
            }
        } else if (psi < 0) {
            double bc = hw / (1 - psi);
            bef = bc * ro;
            if (fibreSupCompresse) {
                bef1 = 0.4 * bef;
                bef2 = hw - bc + 0.6 * bef;
            } else {
                bef2 = 0.4 * bef;
                bef1 = hw - bc + 0.6 * bef;
            }
        }
        
        Aw = tw * (bef1 + bef2);
        double ygwtm = (bef1 * tw * (bef1 / 2) + bef2 * tw * (hw - bef2 / 2))
        / Aw;
        ygw = ygwtm;
        Jw = Math.pow(bef1, 3) * tw / 12 + Math.pow(bef2, 3) * tw / 12 + bef1
                * tw * Math.pow(ygwtm - bef1 / 2, 2) + bef2 * tw
                * Math.pow(hw - bef2 / 2 - ygwtm, 2);
        
        Jw = Jw * Math.cos(alfa) * Math.cos(alfa);
        ygw =ygw  * Math.cos(alfa)+ Dy;
        
        Aceff=bef*tw;
        return new ParametriStatici(Aw, Jw, ygw, 0);
        
    }
    
    // parametri static anima orizzontale non irrigidita
    private ParametriStatici getParametriStaticiAnimaOrizz_NonIrrigid() {
        
        double Aw, Jw, ygw;
        
        double e = mat.gete();
        
        // chek per l'anima NB: compressioni positive
        double bef = 0;
        psi = 1;
        double ks = 4.0;
        
        lamdaP = (hw / tw) / (28.4 * e * Math.pow(ks, 0.5));
        sigP = mat.getFy(tw)/lamdaP*lamdaP;
        sigC = Math.PI*Math.PI*mat.getE()*tw*tw/(12*(1-mat.getNi()*mat.getNi())*passoIrrTrasv*passoIrrTrasv);
        
        double ro = 1;
        if (lamdaP > 0.673) {
            ro = Math
                    .min((lamdaP - 0.055 * (3 + psi)) / Math.pow(lamdaP, 2), 1);
        }
        
        bef = ro * hw;
        bef1 = 0.5 * bef;
        bef2 = bef1;
        
        Aw = tw * bef;
        Jw = tw*tw*tw * bef / 12;
        ygw = tw / 2 + Dy;
        
        Aceff=Aw;
        return new ParametriStatici(Aw, Jw, ygw, 0);
        
    }
    
    private double[] getLarghezzaSottopanelli(double ss, double ii,
            double hw, ArrayList<Irrigidimento> irs, double yn) {
        
        yn = Math.min(yn,hw);
        
        if (irs.size() == 1) {
            double b1, b2c;
            
            // double psi = 0;
            Irrigidimento i1 = irs.get(0);
            // calcolo b1,b2: posizione irrigidimento rispetto bordo teso anima
            // momenti positivi..
            if (ss >= ii) {
                b1 = i1.getY();
                b2c = yn - i1.getY();
            } else {
                b1 = hw - i1.getY();
                b2c = yn-b1;
            }
            
            return new double[] { b1, b2c };
            
        } else if (irs.size() == 2) {
            Irrigidimento il1 = irs.get(0);
            Irrigidimento il2 = irs.get(1);
            
            double b1, b2, b3c;
            // double ts = concio.getTs();
            // double ti = concio.getTi();
            
            double psi = 0;
            Irrigidimento i1; // identifica l'irrigidimento vicino al
            // bordo
            Irrigidimento i2; // identifica l'irrigidimento interno
            
            // calcolo b1,b2: posizione irrigidimento rispetto bordo teso anima
            // momenti positivi..
            if (ss >= ii) {
                double a1 = il1.getY();
                double a2 = il2.getY();
                b1 = Math.min(a1, a2);
                b2 = Math.max(a1, a2) - b1;
                b3c = yn - b1 - b2;
                if (a1 < a2) {
                    i1 = il1;
                    i2 = il2;
                } else {
                    i1 = il2;
                    i2 = il1;
                }
            } else {
                double a1 = hw - il1.getY();
                double a2 = hw - il2.getY();
                b1 = Math.min(a1, a2);
                b2 = Math.max(a1, a2) - b1;
                b3c = yn - b1 - b2;
                if (a1 < a2) {
                    i1 = il1;
                    i2 = il2;
                } else {
                    i1 = il2;
                    i2 = il1;
                }
            }
            return new double[] { b1, b2, b3c };
            
        }
        
        return null;
    }
    
    private double getAcLoc( boolean areaEfficace) {
        
        double acLoc = 0;
        ParametriStatici ps;
        for (int i = 0; i < irs.size(); i++) {
            Irrigidimento ir = irs.get(i);
            if (!areaEfficace)
                ps = ir.getParametriStatici(tw, 1);
            else
                ps = ir.getParametriStaticiEff(tw, 1);
            
            double all = ps.getA();
            acLoc += all;
        }
        return acLoc;
    }
    
    // calcola i valori di bsup, binf (anche efficaci) dei panelli e gli
    // memorizza nelle
    // variabili degli irrigidimenti. Restituisce inoltre i valori di Bedge
    private  double[] setBsup_Binf_ReturnBedge() {
        
        double[] bSottPann = getLarghezzaSottopanelli(ss, ii, hw, irs, yn);
        // coefficiente csi materiale
        double e = mat.gete();
        
        double ro1, ro2, ro3;
        double b1edge, b1inf, b1eff, b1edgeeff, b1infeff;
        double b2sup, b2inf, b2eff, b2supeff, b2infeff, b2edge, b2edgeeff;
        double b3sup, b3edge, b3eff, b3supeff, b3edgeeff;
        
        if (irs.size() == 1) {
            double b1 = bSottPann[0];
            double b2c = bSottPann[1];
            
            Irrigidimento i1 = irs.get(0);
            
            double tws1 = i1.getT();
            double ys1 = i1.getY();
            
            // pannello 1
            double b1s = b1 - tws1 / 2;
            double psi1 = (yn - b1) / yn;
            double ks1 = getKsig(psi1, true);
            double lamda1 = (b1s / tw) / (28.4 * e * Math.pow(ks1, 0.5));
            ro1 = getro(psi1, lamda1, true);
            b1edge = (2 / (5 - psi1)) * b1s;
            b1inf = ((3 - psi1) / (5 - psi1)) * b1s;
            b1eff = ro1 * b1s;
            b1edgeeff = (2 / (5 - psi1)) * b1eff;
            b1infeff = ((3 - psi1) / (5 - psi1)) * b1eff;
            // x1eff = b1s - b1eff;
            
            // pannello 2
            double b2cs = b2c - tws1 / 2;
            double btot2;
            if(hw>yn)btot2=hw-yn+b2cs;
            else btot2=b2cs;
            
            double psi2 = (-hw+yn)/(yn-b1);
            //double psi2 = (yn - b1 - b2c) / (yn );
            double ks2 = getKsig(psi2, true);
            double lamda2 = (btot2 / tw) / (28.4 * e * Math.pow(ks2, 0.5));
            ro2 = getro(psi2, lamda2, true);
            
            if(yn<hw){
                b2sup = 0.4 * b2cs;
                b2edge = 0.6 * b2cs;
                b2eff = ro2 * b2cs;
                b2supeff = 0.4 * b2eff;
                b2edgeeff = 0.6 * b2eff;
                
            }else{
                b2sup = (2 / (5 - psi2)) * b2cs;
                b2edge = ((3 - psi2) / (5 - psi2)) * b2cs;
                b2eff = ro2 * b2cs;
                b2supeff = (2 / (5 - psi2)) * b2eff;
                b2edgeeff = ((3 - psi2) / (5 - psi2)) * b2eff;
            }
            
            // setta i valori negli irrigidimenti
            if(ss>ii){
                i1.setBsup_Binf(new double[] { b2sup, b1inf });
                i1.setBsupEff_BinfEff(new double[] { b2supeff, b1infeff });
            }else{
                i1.setBsup_Binf(new double[] { b1inf,b2sup });
                i1.setBsupEff_BinfEff(new double[] {  b1infeff ,b2supeff});
            }
            return new double[] { b1edgeeff, b2edgeeff };
            
        }
        if (irs.size() == 2) {
            Irrigidimento i1 = irs.get(0);
            Irrigidimento i2 = irs.get(1);
            
            double b1 = bSottPann[0];
            double b2 = bSottPann[1];
            double b3c = bSottPann[2];
            
            double tws1 = i1.getT();
            double ys1 = i1.getY();
            
            double tws2 = i2.getT();
            double ys2 = i2.getY();
            
            // double a = passoIrrigidimentiTrasversali;
            // double E = mat.getE();
            // double ni = mat.getNi();
            
            // pannello 1
            double b1s = b1 - tws1 / 2;
            double psi1 = (yn - b1) / yn;
            double ks1 = getKsig(psi1, true);
            double lamda1 = (b1s / tw) / (28.4 * e * Math.pow(ks1, 0.5));
            ro1 = getro(psi1, lamda1, true);
            b1edge = (2 / (5 - psi1)) * b1s;
            b1inf = ((3 - psi1) / (5 - psi1)) * b1s;
            b1eff = ro1 * b1s;
            b1edgeeff = (2 / (5 - psi1)) * b1eff;
            b1infeff = ((3 - psi1) / (5 - psi1)) * b1eff;
            // x1eff = b1s - b1eff;
            
            // pannello 2
            double b2s = (b2) - tws1 / 2 - tws2 / 2;
            double psi2 = (yn - b1 - b2) / (yn - b1);
            double ks2 = getKsig(psi2, true);
            double lamda2 = (b2s / tw) / (28.4 * e * Math.pow(ks2, 0.5));
            ro2 = getro(psi2, lamda2, true);
            b2sup = (2 / (5 - psi2)) * b2s;
            b2inf = ((3 - psi2) / (5 - psi2)) * b2s;
            b2eff = ro2 * b2s;
            b2supeff = (2 / (5 - psi2)) * b2eff;
            b2infeff = ((3 - psi2) / (5 - psi2)) * b2eff;
            // x2eff = b2s - b2eff;
            
            // pannello 3
            double b3cs = b3c - tws2 / 2;
            double psi3 = (-hw+yn)/(yn-b1-b2);
            double btot3;
            if(hw>yn)btot3=hw-yn+b3cs;
            else btot3=b3cs;
            
            double ks3 = getKsig(psi3, true);
            double lamda3 = (btot3 / tw) / (28.4 * e * Math.pow(ks3, 0.5));
            ro3 = getro(psi3, lamda3, true);
            
            if(yn<hw){
                b3sup = 0.4 * b3cs;
                b3edge = 0.6 * b3cs;
                b3eff = ro3 * b3cs;
                b3supeff = 0.4 * b3eff;
                b3edgeeff = 0.6 * b3eff;
            }else{
                b3sup = (2 / (5 - psi3)) * b3cs;
                b3edge = ((3 - psi3) / (5 - psi3)) * b3cs;
                b3eff = ro3 * b3cs;
                b3supeff = (2 / (5 - psi3)) * b3eff;
                b3edgeeff = ((3 - psi3) / (5 - psi3)) * b3eff;
            }
            
            // setta i valori negli irrigidimenti
            if(ss>ii){
                i1.setBsup_Binf(new double[] { b2sup, b1inf });
                i1.setBsupEff_BinfEff(new double[] { b2supeff, b1infeff });
                
                i2.setBsup_Binf(new double[] { b3sup, b2inf });
                i2.setBsupEff_BinfEff(new double[] { b3supeff, b2infeff });
            } else{
                i1.setBsup_Binf(new double[] { b1inf,b2sup  });
                i1.setBsupEff_BinfEff(new double[] { b1infeff ,b2supeff});
                
                i2.setBsup_Binf(new double[] {  b2inf ,b3sup});
                i2.setBsupEff_BinfEff(new double[] { b2infeff,b3supeff });
            }
            
            return new double[] { b1edgeeff, b3edgeeff };
            
        }
        // nb solo caso ss = cost. e irrigigidimenti equidistanti
        // e irrigidimenti uguali
        if (irs.size() > 2) {
            int nIrr = irs.size();
            Irrigidimento i1 = irs.get(0);
            
            double b1 = hw / (nIrr + 1);
            double tws1 = i1.getT();
            
            // pannelli
            double b1s = b1 - tws1;
            double psi1 = 1;
            double ks1 = getKsig(psi1, true);
            double lamda1 = (b1s / tw) / (28.4 * e * Math.pow(ks1, 0.5));
            ro1 = getro(psi1, lamda1, true);
            double b1sEff = b1s * ro1;
            
            // setta i valori negli irrigidimenti
            for (int i = 0; i < nIrr; i++) {
                Irrigidimento irrl = irs.get(i);
                irrl.setBsup_Binf(new double[] { b1s / 2, b1s / 2 });
                irrl.setBsupEff_BinfEff(new double[] { b1sEff / 2,b1sEff / 2 });
            }
            
            return new double[] { b1*ro1 / 2,  b1*ro1 / 2};
            
        }
        return null;
    }
    
    private  double getYirrigidimento(Irrigidimento ir,
            boolean Mpositivi, double hw) {
        
        if (Mpositivi) {
            return ir.getY();
        } else {
            return hw - ir.getY();
        }
    }
    
    //posizione asse neutro rispetto bordo compresso
    private double getYn(double ss, double ii, double hw) {
        double yn;
        if (ss > ii) {
            yn = ss * hw / (ss - ii);
        } else {
            yn = ii * hw / (ii - ss);
        }
        return yn;
    }
    
    // caso almeno 3 irrigidimenti equidistanti
    private double getSigP_EqallySpaced(ArrayList<Irrigidimento> irs,
            double hw, double tw, double ss, double ii,
            double passoIrrigidimentiTrasv, MaterialeAcciaio mat) {
        
        int nIrr = irs.size();
        double PiG = Math.PI;
        double ni = mat.getNi();
        double sigE = 190000 * (tw / hw) * (tw / hw);
        double Ip = hw * tw * tw * tw / (12*(1-ni*ni));
        double ap = tw * hw;
        double[] Isl = new double [nIrr+1];
        double[] ygl = new double [nIrr+1];
        double[] asl = new double [nIrr+1];
        double Airr=0;
        
        //calcolo area As
        for (int i = 0; i < nIrr; i++) {
            Irrigidimento ir = irs.get(i);
              Airr += ir.getArea();
        }
        
        //calcolo Isl;
        for (int i = 0; i < nIrr; i++) {
            Irrigidimento ir = irs.get(i);
            ParametriStatici ps =ir.getParametriStatici(0,0,tw,1);
            Isl[i] = ps.getJy();
            ygl[i]=ps.getYg();
            asl[i]=ps.getA();
        }
        //parte anima
        double tsl1 = irs.get(0).getT();
        double b = hw-nIrr*tsl1;
        Isl[nIrr] = b*tw*tw*tw/12;
        ygl[nIrr]= 0;
        asl[nIrr]=b*tw;

        double atot=0;
        double atotYg=0;
        double Jsl=0;
        for (int i = 0; i <= nIrr; i++) {
            atot+=asl[i];
            atotYg+=asl[i]*ygl[i];
        }
        double yg = atotYg/atot;
        for (int i = 0; i <= nIrr; i++) {
            Jsl+=Isl[i]+asl[i]*(ygl[i]-yg)*(ygl[i]-yg);
        }
    
        
        double sig1 = Math.max(ss, ii);
        double sig2 = Math.min(ss, ii);
        
        double gamma = Jsl / Ip;
        double delta = Airr / ap;
        double alfa = passoIrrigidimentiTrasv / hw;
        double psi = Math.max(sig2 / sig1, 0.5);
        
        double ksp;
        if (Math.pow(gamma, 1 / 4) >= alfa) {
            ksp = 2 * ((1 + alfa * alfa) * (1 + alfa * alfa) + gamma - 1);
            ksp = ksp / (alfa * alfa * (psi + 1) * (1 + delta));
        } else {
            ksp = 4 * (1 + Math.pow(gamma, 0.5));
            ksp = ksp / ((psi + 1) * (1 + delta));
        }
        
        return sigE * ksp;
    }
    
    private  double getSigP(ArrayList<Irrigidimento> irs, double hw,
            double tw, double ss, double ii, double passoIrrigidimentiTrasv,
            MaterialeAcciaio mat) {
        
        int nIrr = irs.size();
        
        if (nIrr > 2)
            return getSigP_EqallySpaced(irs, hw, tw, ss, ii,
                    passoIrrigidimentiTrasv, mat);
        
        Irrigidimento i1, i2 = null;
        double y1 = 0, y2 = 0;
        double yn = getYn(ss, ii, hw);
        
        double a = passoIrrigidimentiTrasv;
        
        double AslI, xslI, IslI;
        double AslII = 0, xslII = 0, IslII = 0;
        
        double E = mat.getE();
        double e = mat.gete();
        double ni = mat.getNi();
        
        i1 = irs.get(0);
        // rigidezze primo irrigidimento
        ParametriStatici ps1 = i1.getParametriStatici(tw, 1);
        AslI = ps1.getA();
        xslI = ps1.getYg();
        IslI = ps1.getJy();
        y1 = getYirrigidimento(i1, ss >= ii, hw);
        
        // rigidezza secondo irrigidimento
        if (irs.size() == 2) {
            i2 = irs.get(1);
            ParametriStatici ps2 = i2.getParametriStatici(tw, 1);
            AslII = ps2.getA();
            xslII = ps2.getYg();
            IslII = ps2.getJy();
            y2 = getYirrigidimento(i2, ss >= ii, hw);
        }
        
        if (irs.size() == 2) {
            // caso instabilizzazione irrigidimento 1
            double scrslI;
            double bI1 = y1;
            double bI2 = y2 - y1;
            double BI = bI1 + bI2;
            double acI = 4.33 * Math.pow((IslI * bI1 * bI1 * bI2 * bI2)
            / (Math.pow(tw, 3) * BI), 0.25);
            if (acI > a) {
                scrslI = (Math.PI*Math.PI * E * IslI)
                / (AslI * a * a)
                + (E * Math.pow(tw, 3) * BI * a * a)
                / (4 * Math.PI*Math.PI * (1 - ni * ni) * AslI * bI1 * bI1 * bI2 * bI2);
            } else {
                scrslI = Math.pow(IslI * tw * tw * tw * (BI), 0.5)
                * 1.05 * E / (AslI * bI1 * bI2);
            }
            
            
            double scrpI ;
            if(ss!=ii)
                scrpI = scrslI * (yn / (yn - y1));
            else
                scrpI = scrslI;
            
            // caso instabilità irrigidimento 2
            double scrslII;
            double bII1 = y2-y1;
            double bII2 = hw - y2;
            double BII = bII1 + bII2;
            double acII = 4.33 * Math.pow((IslII * bII1 * bII1 * bII2 * bII2)
            / (Math.pow(tw, 3) * BII), 0.25);
            if (acII > a) {
                scrslII = (Math.PI*Math.PI * E * IslII)
                / (AslII * a * a)
                + (E * Math.pow(tw, 3) * BII * a * a)
                / (4 * Math.PI*Math.PI * (1 - ni * ni) * AslII * bII1 * bII1
                        * bII2 * bII2);
            } else {
                scrslII = Math.pow(IslII * tw * tw * tw * (BII), 0.5)
                * 1.05 * E / (AslII * bII1 * bII2);
            }
            double scrpII;
            if(ss!=ii)
                scrpII = scrslII * (yn / (yn - y2));
            else
                scrpII = scrslII;
            
            // caso instabilità lumped
            
            // combinazione I e II
            double sI = getSigmaAtY(i1.getY(), ss, ii, hw);
            double sII = getSigmaAtY(i2.getY(), ss, ii, hw);
            double Aslumped = AslI + AslII;
            double Isllumped = IslI + IslII;
            double FslI = AslI * sI;
            double FslII = AslII * sII;
            double hwlumped = (y2 - y1) * FslII / (FslI + FslII) + y1;
            
            double scrslIII;
            double bIII1 = hwlumped;
            double bIII2 = hw - hwlumped;
            double BIII = hw;
            double acIII = 4.33 * Math.pow(
                    (Isllumped * bIII1 * bIII1 * bIII2 * bIII2)
                    / (Math.pow(tw, 3) * BIII), 0.25);
            if (acIII > a) {
                scrslIII = (Math.PI*Math.PI * E * Isllumped)
                / (Aslumped * a * a)
                + (E * Math.pow(tw, 3) * BIII * a * a)
                / (4 * Math.PI*Math.PI * (1 - ni * ni) * Aslumped * bIII1
                        * bIII1 * bIII2 * bIII2);
            } else {
                scrslIII = Math.pow(Isllumped * tw * tw * tw * (BIII),
                        0.5)
                        * 1.05 * E / (Aslumped * bIII1 * bIII2);
            }
            
            double scrpIII;
            if(ss!=ii)
                scrpIII = scrslIII * (yn / (yn - hwlumped));
            else
                scrpIII = scrslIII;
            
            return Math.min(scrpI, Math.min(scrpII, scrpIII));
        }
        
        else if (irs.size() == 1) {
            
            double scrslI;
            double bI1 = y1;
            double bI2 = hw - y1;
            double BI = bI1 + bI2;
            double acI = 4.33 * Math.pow((IslI * bI1 * bI1 * bI2 * bI2)
            / (Math.pow(tw, 3) * BI), 0.25);
            if (acI > a) {
                scrslI = ( Math.PI* Math.PI * E * IslI)
                / (AslI * a * a)
                + (E * Math.pow(tw, 3) * BI * a * a)
                / (4 * Math.PI* Math.PI * (1 - ni * ni) * AslI * bI1 * bI1 * bI2 * bI2);
            } else {
                scrslI = Math.pow(IslI * tw * tw * tw * (bI1 + bI2), 0.5)
                * 1.05 * E / (AslI * bI1 * bI2);
            }
            
            if(ss!=ii)
                return scrslI * (yn / (yn - y1));
            else
                return scrslI;
            
            
        }
        
        return 0;
        
    }
    
    private double getKsig(double psi, boolean elementoInterno) {
        if (elementoInterno) {
            if (psi <= 1 && psi > 0) {
                return 8.2 / (1.05 + psi);
            }
            if (psi <= 0 && psi > -1) {
                return 7.81 - 6.29 * psi + 9.78 * psi * psi;
            }
            if (psi <= -1 && psi > -3) {
                return 5.98 * (1 - psi) * (1 - psi);
            }
            if (psi <= -3) {
                return 95.68;
            }
            return Double.MAX_VALUE;
        }
        if (!elementoInterno) {
            return 0.0;
        }
        return 0.0;
        
    }
    
    private  double getro(double psi, double lamda,
            boolean elementoInterno) {
        
        if (elementoInterno) {
            if(lamda<=0.673) return 1;
            else return Math.min((lamda - 0.055 * (3 + psi)) / (lamda * lamda), 1);
        } else {
            if(lamda<=0.673) return 1;
            return Math.min((lamda - 0.188) / (lamda * lamda), 1);
        }
    }
    
    // calcola ro plate behaviour
    private  double getRoPlatebehaviour(double sigcrp, double psi,
            MaterialeAcciaio mat, double Aceffloc, double Acloc) {
        
        double fy = mat.getFy(0);
        
        double betaAC = Aceffloc / Acloc;
        double lamdaP = Math.pow(betaAC * fy / sigcrp, 0.5);
        
        return getro(psi, lamdaP, true);
        
    }
    
    private double[] getSigC_nIrrig(double hw, double ss, double ii,
            double tw, ArrayList<Irrigidimento> irs, double passoIrrTrasv,
            MaterialeAcciaio mat) {
        
        double sigcrc = Double.MAX_VALUE;
        int nm = 0;
        for (int i = 0; i < irs.size(); i++) {
            double sigCl = getSigC(hw, ss, ii, tw, irs.get(i), passoIrrTrasv,
                    mat);
            if (sigcrc > sigCl) {
                sigcrc = sigCl;
                nm = i;
            }
        }
        return new double[] { sigcrc, nm };
    }
    
    private double getChiColumn(double tw, double sigcrc,
            MaterialeAcciaio mat, Irrigidimento i1) {
        
        double fy = mat.getFy(0);
        
        // primo Irrigidimento
        ParametriStatici psEff = i1.getParametriStaticiEff(tw, 1);
        double Asleff = psEff.getA();
        
        ParametriStatici psIrr = i1.getParametriStatici(0, 0, 0, 1);
        double eslIrr = psIrr.getYg();
        
        ParametriStatici ps = i1.getParametriStatici(tw, 1);
        double Asl = ps.getA();
        double Isl = ps.getJy();
        double esl = ps.getYg();
        
        double betaAC = Asleff / Asl;
        
        double lamdaC = Math.pow(betaAC * fy / sigcrc, 0.5);
        double i = Math.pow(Isl / Asl, 0.5);
        double e2 = esl;
        double e1 = eslIrr + tw / 2 - e2;
        double e = Math.max(e1, e2);
        double alfa = 0.49;
        double alfaE = alfa + 0.09 / (i / e);
        double fi = 0.5 * (1 + alfaE * (lamdaC - 0.2) + lamdaC * lamdaC);
        double chi = Math.pow((fi + Math.pow(fi * fi - lamdaC * lamdaC, 0.5)),
                -1);
        return Math.min(chi, 1);
        
    }
    
    private double getSigC(double hw, double ss, double ii, double tw,
            Irrigidimento ir, double passoIrrTrasv, MaterialeAcciaio mat) {
        
        double yn = getYn(ss, ii, hw);
        
        double y1 = getYirrigidimento(ir, ss > ii, hw);
        
        ParametriStatici ps = ir.getParametriStatici(tw, 1);
        double Asl = ps.getA();
        double Isl = ps.getJy();
        
        double sigcrsl = Math.PI * Math.PI* mat.getE() * Isl
                / (Asl * passoIrrTrasv * passoIrrTrasv);
        
        if(ss!=ii)
            return yn / (yn - y1) * sigcrsl;
        else
            return sigcrsl;
    }
    
    private double getRocColumnPlateInteraction(double roPlate,
            double chiColumn, double sigcrp, double sigcrc) {
        
        double teta = sigcrp / sigcrc - 1;
        if (teta < 0)
            teta = 0;
        if (teta > 1)
            teta = 1;
        return (roPlate - chiColumn) * teta * (2 - teta) + chiColumn;
        
    }
    
    private double getSigmaAtY(double y, double ss, double ii, double hw) {
        double sigy = (ss - ii) / hw * (hw - y) + ii;
        return sigy;
    }
    
    public double getShearBucklingCoeffKt(double hw, double tw,
            double passoIrrig, int nLongStiff, double Isl) {
        
        double ksl1 = 9.00 * Math.pow(hw / passoIrrig, 2)
        * Math.pow(Isl / (tw * tw * tw * hw), 3.00 / 4.00);
        double ksLimit = (2.1 / tw) * Math.pow(Isl / hw, 0.33333333);
        double ksl;
        if (nLongStiff != 0)
            ksl = Math.max(ksl1, ksLimit);
        else
            ksl = 0;
        
        if ((nLongStiff == 0 || nLongStiff > 2)) {
            if (passoIrrig / hw >= 1)
                return 5.34 + 4.00 * Math.pow(hw / passoIrrig, 2) + ksl;
            else
                return 4.00 + 5.34 * Math.pow(hw / passoIrrig, 2) + ksl;
        }
        if ((nLongStiff == 1 || nLongStiff == 2)) {
            if (passoIrrig / hw >= 3) {
                if (passoIrrig / hw >= 1)
                    return 5.34 + 4.00 * Math.pow(hw / passoIrrig, 2) + ksl;
                else
                    return 4.00 + 5.34 * Math.pow(hw / passoIrrig, 2) + ksl;
            } else {
                return 4.1 + (6.3 + 0.18 * Isl / (tw * tw * tw * hw))
                / Math.pow(passoIrrig / hw, 2) + 2.2
                        * Math.pow(Isl / (tw * tw * tw * hw), 1.00/3.00);
            }
        }
        return 0;
    }
    
    public double getChiW(double nu, double lamdaW, boolean rigidEndPost) {
        
        if (lamdaW < 0.83 / nu)
            return nu;
        if (lamdaW >= 0.83 / nu && lamdaW < 1.08)
            return 0.83 / lamdaW;
        if (lamdaW >= 1.08)
            if (!rigidEndPost)
                return 0.83 / lamdaW;
            else
                return 1.37 / (0.7 + lamdaW);
        return 0;
    }
    
    public double getLamdaW(/* boolean irrigidTrasv, */double kt,
            double hw, double tw, double e) {
        
        // if(irrigidTrasv) return hw/(86.4*tw*e);
        
        /* else */return hw / (37.4 * tw * e * Math.pow(kt, 0.5));
        
    }
    
    // resitituisce il valore minimo di LamdaWi dei sottopannelli
    public double getLamdaWi(double hw, double tw, double e,
            double passoIrrig, ArrayList<Irrigidimento> ir) {
        
        double hwmax;
        double a1, a2, a3;
        int nLongStiff = ir.size();
        
        if (nLongStiff == 1) {
            Irrigidimento ilo = ir.get(0);
            double tp1 = ilo.getT();
            a1 = hw - ilo.getY() - tp1 / 2;
            a2 = hw - a1 - tp1;
            hwmax = Math.max(a1, a2);
            double kti = getShearBucklingCoeffKt(hwmax, tw, passoIrrig, 0, 0);
            return getLamdaW(kti, hwmax, tw, e);
        }
        if (nLongStiff == 2) {
            Irrigidimento ilo = ir.get(0);
            Irrigidimento il1 = ir.get(1);
            if (ilo.getY() < il1.getY()) {
                a1 = ilo.getY() - ilo.getT() / 2;
                a2 = il1.getY() - ilo.getY() - ilo.getT() / 2 - il1.getT() / 2;
                a3 = hw - il1.getY() - il1.getT() / 2;
            } else {
                a1 = il1.getY() - il1.getT() / 2;
                a2 = ilo.getY() - il1.getY() - il1.getT() / 2 - ilo.getT() / 2;
                a3 = hw - ilo.getY() - ilo.getT() / 2;
            }
            hwmax = Math.max(a1, Math.max(a2, a3));
            double kti = getShearBucklingCoeffKt(hwmax, tw, passoIrrig, 0, 0);
            return getLamdaW(kti, hwmax, tw, e);
        }
        return 0;
    }
    
    // RESISTENZA A TAGLIO: contributo delle flangie
    public  double getVbfRd(double hw, double tw, double alfa,
            double bsredDx, double bsredSx, double biredDx, double biredSx,
            double ts, double ti, double Mf, MaterialeAcciaio mat,
            double passoIrr, double Mflett) {
        
        double gamma0 = mat.getGamma0();
        double e = mat.gete();
        double twl = tw / Math.cos(alfa);
        double hwl = hw * Math.cos(alfa);
        
        double bslDx = Math.min(15 * e * ts, bsredDx);
        double bslSx = Math.min(15 * e * ts, bsredSx);
        double bsl = bslDx + twl + bslSx;
        
        double bilDx = Math.min(15 * e * ti, biredDx);
        double bilSx = Math.min(15 * e * ti, biredSx);
        double bil = bilDx + twl + bilSx;
        
        double as = bsl * ts;
        double ai = bil * ti;
        
        double bf = 0, tf = 0;
        if (as * mat.getFy(ts) / gamma0 < ai * mat.getFy(ti) / gamma0) {
            bf = bsl;
            tf = ts;
        } else {
            bf = bil;
            tf = ti;
        }
        
        
        double c = passoIrr
                * (0.25 + (1.6 * bf * tf * tf * mat.getFy(tf) / (twl * hwl
                * hwl * mat.getFy(tw))));
        
        double Vb = bf * tf * tf * mat.getFy(tf) / (c * mat.getGamma1())
        * (1 - Math.pow(Mflett / Mf, 2));
        
        if (Math.abs(Mf) < Math.abs(Mflett))
            return 0;
        else
            return Vb;
        
    }
    
    public double getInterazioneMomentoTaglio(double Mf, double Mpl,
            double Msez, double nu3b) {
        
        if (nu3b<0.5) return nu3b;
        // interazione
        double nu1bs = Math.abs(Msez / Mpl);
        
        double nuInterM_V = nu1bs + (1 - Mf / Mpl) * Math.pow(2 * nu3b - 1, 2);
        
        return nuInterM_V;
    }
    
    // CONTROLLARE
    public  double getMplastico(double Nsd, double fy, double hw,
            double tw, double bs, double ts, double bi, double ti, double alfa) {
        
        double a = bs * ts + bi * ti + hw * tw;
        double hwl = hw * Math.cos(alfa);
        double twl = tw / Math.cos(alfa);
        
        double s = (a / 2 - bs * ts) / twl;
        double s2 = hwl - s;
        double yg1 = (bs * ts * (s + ts / 2) + s * twl * s / 2)
        / (bs * ts + s * twl);
        double yg2 = (bi * ti * (s2 + ti / 2) + s2 * twl * s2 / 2)
        / (bi * ti + s2 * twl);
        double yg = yg1 + yg2;
        
        double w = yg * a / 2;
        double Mplrd = w * fy;
        double Nrd = fy * a;
        double n1 = Nsd / Nrd;
        double n2 = Nsd / (fy * (a - bs * ts - bi * ti));
        
        if (n1 < 0.25 && n2 < 0.5)
            return Mplrd;
        else if(n1>=1){
            return 0;
        } else {
            double aa = Math.min((a - bs * ts - bi * ti) / a, 0.5);
            return (Mplrd * (1 - n1) / (1 - 0.5 * aa));
        }
        
    }
    
    // rigidezza degli iirr. longitudinali dell'anima rispetto baricentro
    // anima
    public double getRigidezzaIslVerificheTaglio(
            ArrayList<Irrigidimento> ir, double e, double tw) {
        
        int ni = ir.size();
        double jt = 0;
        
        for (int i = 0; i < ni; ++i) {
            Irrigidimento l = ir.get(i);
            double b = 15 * e * tw;
            ParametriStatici ps = l.getParametriStatici(b, b, tw, 1);
            jt += ps.getJy();
        }
        
        return jt;
    }
    
    public  double[] getSigma_W_IrrigidTrasv(double ssEffmax,double Ved,Irrigidimento_T irt) {
        
        boolean doppio = irt.isDoppio();
        double smax;
        double w;
        double emax,e0=0;
        double qm=0;
        double Ast;
        ParametriStatici psi = irt.getParametriStatici(15*mat.gete()*tw,15*mat.gete()*tw,tw,1);
        double xg1 = psi.getYg();
        double Ist = psi.getJy();
        Ast = psi.getA();
        
        if(!doppio){
            double e1 = xg1+tw/2;
            double e2 = irt.getBsl()+tw -e1;
            if(e1>e2){
                emax = e1;
                e0 = xg1;
            }else{
                emax = e2;
                e0 = e1-tw/2;
            }
        }else{
            emax=irt.getBsl()+tw/2;
        }
        
        double wo = Math.max(passoIrrTrasv/300,hw/300);
        double NstEd = Ved-hw*tw*mat.getFy(tw)/(lamdaW*lamdaW*mat.getGamma1()*Math.pow(3,0.5));
        
        double Ned = (ss/2+ii/2)*tw*hw;
        Ned = Math.max(Ned,ssEffmax*Aceff/2);
        
        double sigc_sigp = sigC/sigP;
        if(sigc_sigp>1) sigc_sigp=1;
        
        double sm = sigc_sigp*Ned*(2/passoIrrTrasv)/hw;
//        double u = Math.PI*Math.PI*mat.getE()*emax/(mat.getFy(tw)*300*hw/mat.getGamma1());
//        u = Math.max(u,1);
        
        if(NstEd<=0){
            //caso nessuna azione assiale
            w = wo/(Ist*Math.pow((Math.PI/hw),4)*mat.getE()/sm-1);
            smax=(wo+w)*sm*hw*hw*emax/(Math.PI*Math.PI*Ist);
            return new double[] {smax,w};
        }else{
            double DNstEd = sm*hw*hw/(Math.PI*Math.PI);
            double Ncr = (Math.PI*Math.PI)*mat.getE()*Ist/(hw*hw);
            double SNstEd = NstEd+DNstEd;
            if(doppio){
                w = wo/(Ncr/SNstEd-1);
                smax=NstEd/Ast+(SNstEd*emax*wo/Ist)*(1/(1-SNstEd/Ncr));
                return new double[] {smax,w};
            }else{
                qm = NstEd*e0/(SNstEd*wo);
                smax=NstEd/Ast+SNstEd*emax*wo/Ist*(1/(1-SNstEd/Ncr))*(1+1.11*qm);
                w = wo*(1/(Ncr/SNstEd-1))*(1+1.25*qm);
                return new double[] {smax,w};
            }
        }
        
    }
    
    public double[] getVbwRd_VbfRd_VbRd_nuInter_N_M(
            ArrayList<Irrigidimento> irs, MaterialeAcciaio mat, double hw,
            double tw, double alfa, double bsredDx, double bsredSx,
            double biredDx, double biredSx, double ts, double ti,
            double passoIrrig, boolean rigidEndPost,
            double ss, double ii, double Ved) {
        
        
        double bs = bsredDx+bsredSx+tw/Math.cos(alfa);
        double bi = biredDx+biredSx+tw/Math.cos(alfa);
        
        int nLongStiff = irs.size();
        double e = mat.gete();
        
        // calcolo kt pannello intero
        double Isl = getRigidezzaIslVerificheTaglio(irs, e, tw);
        double kt = getShearBucklingCoeffKt(hw, tw, passoIrrig, nLongStiff, Isl);
        double lamdaWe = getLamdaW(kt, hw, tw, e);
        
        // calcolo di kt sottopannello
        double lamdaWi = getLamdaWi(hw, tw, e, passoIrrig, irs);
        
        lamdaW = Math.max(lamdaWe, lamdaWi);
        
        double chiW = getChiW(mat.getNutaglio(), lamdaW, rigidEndPost);
        
        // resistenza anima
        double VbwRd = chiW * mat.getFy(tw) * hw * tw
                / (mat.getGamma1() * Math.pow(3, 0.5));
        
//		VbwRd *= Math.cos(alfa);
        
        double[] M = getM_NTraveT(ss, ii, bs, ts, bi, ti, hw, tw, alfa);
        
        // contributo delle ali
        double Mf = getMFlange(mat, hw, tw, alfa, bsredDx, bsredSx, biredDx,
                biredSx, ts, ti, M[1]);
        
        double VbfRd = getVbfRd(hw, tw, alfa, bsredDx, bsredSx, biredDx,
                biredSx, ts, ti, Mf, mat, passoIrrig, M[0]);
        
        // contributo totale
        double VbRd = Math.min(VbwRd + VbfRd, mat.getNutaglio() * mat.getFy(0)
        * hw * tw / (mat.getGamma1() * Math.pow(3, 0.5)));
        
        double nu3b = Ved / VbRd;
        double Mpl = getMplastico(M[1], mat.getFy(0), hw, tw, bs, ts, bi, ti, alfa);
        
        double nuInter_N_M = getInterazioneMomentoTaglio(Mf, Mpl, M[0], nu3b);
        
        return new double[] { VbwRd, VbfRd, VbRd, nu3b,nuInter_N_M };
        
    }
    
    
    private double[] getM_NTraveT(double ss, double ii, double bs,
            double ts, double bi, double ti, double hw, double tw, double alfa) {
        
        double twl = tw / Math.cos(alfa);
        double hwl = hw * Math.cos(alfa);
        
        ParametriStatici ps = getParametriStaticiTraveDoppioT(bs, ts, bi, ti,
                hwl, twl);
        double As = ps.getA();
        double ygs = ps.getYg();
        double Js = ps.getJy();
        
        double Htot = hwl + ts + ti;
        
        // calcolo le tensioni in corrispondenza della sezione baric.
        double sigYg = (Htot - ygs) * (ss - ii) / Htot + ii;
        double Nconcio = sigYg * As;
        double ssC = ss - sigYg;
        double wssa = Js / (ygs);
        double Mconcio = ssC * wssa;
        
        return new double[] { Mconcio, Nconcio };
        
    }
    
    public ParametriStatici getParametriStaticiTraveDoppioT(double bs,
            double ts, double bi, double ti, double hw, double tw) {
        
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
    
    // restituisce il momento resistente della flangie
    private  double getMFlange(MaterialeAcciaio mat, double hw,
            double tw, double alfa, double bsredDx, double bsredSx,
            double biredDx, double biredSx, double ts, double ti, double Nsez) {
        
        double gamma0 = mat.getGamma0();
        double e = mat.gete();
        double twl = tw / Math.cos(alfa);
        double hwl = hw * Math.cos(alfa);
        
        double asTot=(bsredDx+bsredSx+twl)*ts;
        double aiTot=(biredDx+biredSx+twl)*ti;
        
        double bslDx = Math.min(15 * e * ts, bsredDx);
        double bslSx = Math.min(15 * e * ts, bsredSx);
        double bsl = bslDx + twl + bslSx;
        
        double bilDx = Math.min(15 * e * ti, biredDx);
        double bilSx = Math.min(15 * e * ti, biredSx);
        double bil = bilDx + twl + bilSx;
        
        double as = bsl * ts;
        double ai = bil * ti;
        double hf = hwl + ti / 2 + ts / 2;
        double Mf = Math.min(hf * as * mat.getFy(ts) / gamma0, hf * ai
                * mat.getFy(ti) / gamma0);
        
        // momento ridotto per presenza azione assiale
        double Nm=Math.abs(Nsez);
        if (Nm > 0) {
            double fac =Math.max(1 - Nm / ((asTot + aiTot) * mat.getFy(0) / mat.getGamma0()),0);
            Mf = Mf * fac;
        }
        
        return Mf;
    }
    
}