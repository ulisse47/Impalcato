package cassone.model.irrigidimenti;

import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;

import cassone.model.MaterialeAcciaio;
import cassone.model.ParametriStatici;

public class Irrigidimento_L implements Irrigidimento {
    
    double tsl1, bsl, tsl2, hsl;
    
    // posizione rispetto bordo sup anima
    double y;
    
    // variabile per irrigidimenti trasversali
    double passo;
    
    double bsup, binf, bsupEff, binfEff;
    
    boolean rigidEndPost;
    
    // irrigidimento simmetrico
    boolean doppio;
    
    public Irrigidimento_L() {
        this.tsl1 = 10;
        this.tsl2 = 8;
        this.bsl = 200;
        this.hsl = 150;
        this.y = 1000;
    }
    
    public Irrigidimento_L(Irrigidimento_L il) {
        this.tsl1 = il.tsl1;
        this.tsl2 = il.tsl2;
        this.bsl = il.bsl;
        this.hsl = il.hsl;
        this.y = il.y;
    }
    
    public double getBinf() {
        return binf;
    }
    
    public void setBinf(double binf) {
        this.binf = binf;
    }
    
    public double getBinfEff() {
        return binfEff;
    }
    
    public void setBinfEff(double binfEff) {
        this.binfEff = binfEff;
    }
    
    public double getBsl() {
        return bsl;
    }
    
    public void setBsl(double bsl) {
        this.bsl = bsl;
    }
    
    public double getBsup() {
        return bsup;
    }
    
    public void setBsup(double bsup) {
        this.bsup = bsup;
    }
    
    public double getBsupEff() {
        return bsupEff;
    }
    
    public void setBsupEff(double bsupEff) {
        this.bsupEff = bsupEff;
    }
    
    public double getHsl() {
        return hsl;
    }
    
    public void setHsl(double hsl) {
        this.hsl = hsl;
    }
    
    public double getTsl1() {
        return tsl1;
    }
    
    public void setTsl1(double tsl1) {
        this.tsl1 = tsl1;
    }
    
    public double getTsl2() {
        return tsl2;
    }
    
    public void setTsl2(double tsl2) {
        this.tsl2 = tsl2;
    }
    
    public double getT() {
        return tsl1;
        
    }
    
    public double getY() {
        return y;
    }
    
    // parametri statici rispetto baricentro anima tw
    public ParametriStatici getParametriStatici(double bsup, double binf,
            double tw, double ro) {
        
        double b1=binf + bsup + tsl1;
        double a1 = b1*tw*ro;
        double j1=(b1 * Math.pow(tw*ro, 3)) / 12.00;
        double x1=0;
        
        double a2 = bsl*tsl1*ro;
        double x2 = tw / 2 + bsl / 2;
        double j2 = (Math.pow(bsl, 3) * tsl1*ro) / 12.00;

        double a3 = (hsl - tsl1)*tsl2*ro;
        double x3 = tw/2+bsl-tsl2/2;
        double j3 = ((hsl - tsl1)*Math.pow(tsl2*ro,3))/12.00;
        
        
        double Asl = a1+a2+a3;
        double xsl = (a1*x1+a2*x2+a3*x3)/ Asl;
        double Isl = j1+j2+j3+a1*(x1-xsl)*(x1-xsl)+a2*(x2-xsl)*(x2-xsl)+a3*(x3-xsl)*(x3-xsl);
        
        return new ParametriStatici(Asl, Isl, xsl, 0);
    }
    
    public double getArea() {
        return bsl*tsl1+(hsl-tsl1)*tsl2;
    }
    
    public double getAreaEff() {
        return bsl*tsl1+(hsl-tsl1)*tsl2;
    }
    
    public void setBsupEff_BinfEff(double[] beff) {
        bsupEff = beff[0];
        binfEff = beff[1];
    }
    
    public void setBsup_Binf(double[] b) {
        bsup = b[0];
        binf = b[1];
    }
    
    public ParametriStatici getParametriStatici(double tw, double ro) {
        
/*        double tsl1 = this.tsl1*ro;
        tw = tw*ro;
        double tsl2 = this.tsl2*ro;
        
        double AslI = (binf + bsup + tsl1) * tw + bsl * tsl1 + (hsl - tsl1)
        * tsl2;
        double xslI = ((hsl - tsl1) * tsl2 * (tw / 2 + bsl - tsl2 / 2) + bsl
                * tsl1 * (tw / 2 + bsl / 2))
                / AslI;
        double IslI = ((binf + bsup + tsl1) * Math.pow(tw, 3)) / 12
                + Math.pow(bsl, 3) * tsl1 / 12+((hsl-tsl1)*Math.pow(tsl2,3))/12;
        IslI += (binf + bsup + tsl1) * tw * xslI * xslI + (hsl - tsl1) * tsl2
                * Math.pow(bsl + tw / 2 - tsl2 / 2 - xslI, 2);
        IslI += bsl * tsl1 * Math.pow((tw / 2 + bsl / 2 - xslI), 2);
  */      
         
        double b1=binf + bsup + tsl1;
        double a1 = b1*tw*ro;
        double j1=(b1 * Math.pow(tw*ro, 3)) / 12.00;
        double x1=0;
        
        double a2 = bsl*tsl1*ro;
        double x2 = tw / 2 + bsl / 2;
        double j2 = (Math.pow(bsl, 3) * tsl1*ro) / 12.00;

        double a3 = (hsl - tsl1)*tsl2*ro;
        double x3 = tw/2+bsl-tsl2/2;
        double j3 = ((hsl - tsl1)*Math.pow(tsl2*ro,3))/12.00;
        
        
        double Asl = a1+a2+a3;
        double xsl = (a1*x1+a2*x2+a3*x3)/ Asl;
        double Isl = j1+j2+j3+a1*(x1-xsl)*(x1-xsl)+a2*(x2-xsl)*(x2-xsl)+a3*(x3-xsl)*(x3-xsl);
 
        
        return new ParametriStatici(Asl, Isl, xsl, 0);
    }
    
    public ParametriStatici getParametriStaticiEff(double tw, double ro) {
        
        
        
        double b1=binfEff + bsupEff + tsl1;
        double twnet=tw;
        double tsl2net=tsl2;
        double tsl1net=tsl1;

        tw=tw*ro;
        double tsl1=this.tsl1*ro;
        double tsl2=this.tsl2*ro;
        
        double a1=b1 * tw;
        double x1 = 0;
        double y1 = binfEff+tsl1net/2-b1/2;
        double J1=b1 * Math.pow(tw, 3) / 12;
        
        double a2=bsl * tsl1;
        double x2=twnet / 2 + bsl / 2;
        double y2=0;
        double J2=Math.pow(bsl, 3) * tsl1 / 12;
        
        double a3=(hsl - tsl1net) * tsl2;
        double x3= (twnet / 2 + bsl - tsl2net / 2);
        double y3=-tsl1net/2-(hsl-tsl1net)/2;
        double J3=((hsl-tsl1net)*Math.pow(tsl2,3))/12;
        
//        double tsl1 = this.tsl1*ro;
 //       tw = tw*ro;
//        double tsl2 = this.tsl2*ro;
        
        
        double AslI = a1+ a2+ a3;
        double xslI = (a3 *x3+ a2 * x2)	/ AslI;
        double IslI = J1+ J2+J3+ a1* xslI * xslI + a3*Math.pow(x3 - xslI, 2);
        IslI += a2 * Math.pow(x2 - xslI, 2);
        
//        double yslI = (a1*y1+a3 *y3+ a2 * y2)	/ AslI;
//        double Jxy= a1*(x1-xslI)*(y1-yslI)+a2*(x2-xslI)*(y2-yslI)+a3*(x3-xslI)*(y3-yslI);
        
        return new ParametriStatici(AslI, IslI, xslI, 0);
    }
    
    public ParametriStatici getParametriStaticiEffGlobali(double tw, double roc) {
        // pannello 1
        double a1 = roc * tsl1 * bsl;
        double y1 = y;
        double x1=tw/2+bsl/2;
        double j1 = bsl * Math.pow(tsl1 * roc, 3) / 12;
        
        // pannello 2
        double b = hsl - tsl1;
        double a2 = tsl2 * roc * b;
        double y2 = y1 + tsl1 / 2 + b / 2;
        double x2=tw/2+bsl-tsl2/2;
        double j2 = tsl2 * roc * Math.pow(b, 3) / 12;
        
        // anima
        double bw = (bsupEff + binfEff + tsl1);
        double a3 = tw * roc * bw;
        double y3 = y1 - tsl1 / 2 - binfEff + bw / 2;
        double x3=0;
        double j3 = tw * roc * Math.pow(bw, 3) / 12;
        
        double at = a1 + a2 + a3;
        double yg = (a1 * y1 + a2 * y2 + a3 * y3) / at;
        double xg = (a1 * x1 + a2 * x2 + a3 * x3) / at;
        
        double jt = j1 + j2 + j3 + a1 * (y1 - yg) * (y1 - yg) + a2 * (y2 - yg)
        * (y2 - yg) + a3 * (y3 - yg) * (y3 - yg);
        
        
        double Jxy = a1*(x1-xg)*(y1-yg)+a2*(y2-yg)*(x2-xg)+a3*(y3-yg)*(x3-xg);
        
        return new ParametriStatici(at, jt, yg, Jxy);
        
        
        
        
    }
    
    public Shape getShape(double tw) {
        GeneralPath gp = new GeneralPath();
        
        gp.moveTo(tw / 2, y - tsl1 / 2);
        gp.lineTo(tw / 2 + bsl, y - tsl1 / 2);
        gp.lineTo(tw / 2 + bsl, y - tsl1 / 2 + hsl);
        gp.lineTo(tw / 2 + bsl - tsl2, y - tsl1 / 2 + hsl);
        gp.lineTo(tw / 2 + bsl - tsl2, y + tsl1 / 2);
        gp.lineTo(tw / 2, y + tsl1 / 2);
        gp.closePath();
        
        return gp;
        
    }
    
    public Shape getShape(double tw, double ro) {
        
        
        double tsl1 = this.tsl1*ro;
        tw = tw*ro;
        double tsl2 = this.tsl2*ro;
        
        
        GeneralPath gp = new GeneralPath();
        
        gp.moveTo(-tw / 2, y - tsl1 / 2 - binf);
        gp.lineTo(tw / 2, y - tsl1 / 2 - binf);
        gp.lineTo(tw / 2, y - tsl1 / 2);
        gp.lineTo(tw / 2 + bsl, y - tsl1 / 2);
        gp.lineTo(tw / 2 + bsl, y - tsl1 / 2 + hsl);
        gp.lineTo(tw / 2 + bsl - tsl2, y - tsl1 / 2 + hsl);
        gp.lineTo(tw / 2 + bsl - tsl2, y - tsl1 / 2 + tsl1);
        gp.lineTo(tw / 2, y + tsl1 / 2);
        gp.lineTo(tw / 2, y + tsl1 / 2 + bsup);
        gp.lineTo(-tw / 2, y + tsl1 / 2 + bsup);
        gp.closePath();
        
        return gp;
    }
    
    public Shape getShapeEff(double tw, double ro) {
        double tsl1 = this.tsl1*ro;
        tw = tw*ro;
        double tsl2 = this.tsl2*ro;
        
        GeneralPath gp = new GeneralPath();
        
        gp.moveTo(-tw / 2, y - tsl1 / 2 - binfEff);
        gp.lineTo(tw / 2, y - tsl1 / 2 - binfEff);
        gp.lineTo(tw / 2, y - tsl1 / 2);
        gp.lineTo(tw / 2 + bsl, y - tsl1 / 2);
        gp.lineTo(tw / 2 + bsl, y - tsl1 / 2 + hsl);
        gp.lineTo(tw / 2 + bsl - tsl2, y - tsl1 / 2 + hsl);
        gp.lineTo(tw / 2 + bsl - tsl2, y - tsl1 / 2 + tsl1);
        gp.lineTo(tw / 2, y + tsl1 / 2);
        gp.lineTo(tw / 2, y + tsl1 / 2 + bsupEff);
        gp.lineTo(-tw / 2, y + tsl1 / 2 + bsupEff);
        gp.closePath();
        
        return gp;
        
    }
    
    public void setY(double y) {
        this.y = y;
    }
    
    public double getPasso() {
        return passo;
    }
    
    public void setPasso(double passo) {
        this.passo = passo;
    }
    
    public boolean isDoppio() {
        return doppio;
    }
    
    public void setDoppio(boolean doppio) {
        this.doppio = doppio;
    }
    
    public boolean isRigidEndPost() {
        return rigidEndPost;
    }
    
    public void setRigidEndPost(boolean rigidEndPost) {
        this.rigidEndPost = rigidEndPost;
    }
    
    public double getTorsionalConstantSVenant() {
        return (bsl * Math.pow(tsl1, 3) + (hsl - tsl1) * Math.pow(tsl2, 3)) / 3;
    }
    
    public double getPolarSecondMoment() {
        
        double Iy = tsl1 * Math.pow(bsl, 3) / 3 + Math.pow(tsl2, 3)
        * (hsl - tsl1) / 12 + tsl2 * (hsl - tsl1)
        * Math.pow(bsl - tsl2, 2);
        double Iz = bsl * Math.pow(tsl1, 3) / 12 + Math.pow(hsl - tsl1, 3)
        * tsl2 / 12 + tsl2 * (hsl - tsl1) * Math.pow(hsl / 2, 2);
        
        return Iz + Iy;
    }
    
    public double getWarpingCostant(double tw) {
        
        return (2 / 3) * (tsl1 * (bsl - tsl2 / 2) * hsl * hsl / 2)
        * (bsl - tsl2 / 2) * hsl;
    }
    
    public double getSigCriticoTorsionale(MaterialeAcciaio mat,double h,double tw) {
        double E = mat.getE();
        double G = mat.getG();
        double It = getTorsionalConstantSVenant();
        double Ip = getPolarSecondMoment();
        double Iw = getWarpingCostant(tw);
        
        return (G * It + Math.pow(Math.PI, 2) * E * Iw / Math.pow(h, 2)) / Ip;
        
    }
    
}

// rigidezza
/*
 * private double getRigidezzaIslVerificheTaglio(ArrayList<Irrigidimento> ir,
 * double e, double tw) { int ni = ir.size(); double[] As = new double[ni];
 * double[] Js = new double[ni]; double[] xs = new double[ni]; double jt = 0;
 *
 * for (int i = 0; i < ni; ++i) { Irrigidimento l = ir.get(i); double ts1 =
 * l.getP1().getH(); double bsl = l.getP1().getB(); double hsl =
 * l.getP2().getB(); double ts2 = l.getP2().getH(); As[i] = (30 * e * tw + ts1) *
 * tw + l.getArea(); xs[i] = (l.getXgDaAnimaTrave(tw) * l.getArea()) / As[i];
 * Js[i] = (30 * e * tw + ts1) * tw * tw * tw / 12 + (hsl - ts1) * ts2 ts2 * ts2 /
 * 12 + ts1 * bsl * bsl * bsl / 12 + (30 * e * tw + ts1) * tw * xs[i] * xs[i] +
 * (hsl - ts1) ts2 * Math.pow(bsl + tw / 2 - ts2 / 2 - xs[i], 2) + bsl ts1 *
 * Math.pow(tw / 2 + bsl / 2 - xs[i], 2); } for (int i = 0; i < ni; ++i) { jt +=
 * Js[i]; }
 *
 * return jt; }
 */

