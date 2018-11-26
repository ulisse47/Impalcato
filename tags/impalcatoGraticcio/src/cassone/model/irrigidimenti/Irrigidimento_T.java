package cassone.model.irrigidimenti;


import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;

import cassone.model.MaterialeAcciaio;
import cassone.model.ParametriStatici;

public class Irrigidimento_T implements Irrigidimento {
    
    double tsl1,bsl,tsl2,hsl;
    
    //posizione rispetto bordo sup anima
    double y;
    
    //
    double passo;
    
    boolean rigidEndPost;
    boolean doppio;
    
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
    
    
    double bsup,binf,bsupEff,binfEff;
    
    public Irrigidimento_T(){
        this.tsl1=10;
        this.tsl2=8;
        this.bsl=200;
        this.hsl=150;
        this.y=1000;
    }
    
    
    public Irrigidimento_T(Irrigidimento_T il){
        this.tsl1=il.tsl1;
        this.tsl2=il.tsl2;
        this.bsl=il.bsl;
        this.hsl=il.hsl;
        this.y=il.y;
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
        return 	tsl1;
        
    }
    
    public double getY() {
        return y;
    }
    
    
    //parametri statici rispetto baricentro anima tw
    public ParametriStatici getParametriStatici(double bsup, double binf,double tw, double ro) {
        
        double Asl;
        double xsl;
        double Isl;
        if(!doppio){
            // parametri statici rispetto baricentro anima tw
            
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
            
            
            Asl = a1+a2+a3;
            xsl = (a1*x1+a2*x2+a3*x3)/ Asl;
            Isl = j1+j2+j3+a1*(x1-xsl)*(x1-xsl)+a2*(x2-xsl)*(x2-xsl)+a3*(x3-xsl)*(x3-xsl);
 
        }else{
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
            
            
            Asl = a1+2*a2+2*a3;
            xsl = 0;
            Isl = j1+2*j2+2*j3+a1*(x1-xsl)*(x1-xsl)+2*a2*(x2-xsl)*(x2-xsl)+2*a3*(x3-xsl)*(x3-xsl);
        }
        return new ParametriStatici(Asl,Isl,xsl,0);
    }
    
    public double getArea() {
        // TODO Auto-generated method stub
        return 0;
    }
    
    public double getAreaEff() {
        // TODO Auto-generated method stub
        return 0;
    }
    
    public void setBsupEff_BinfEff(double[] beff) {
        bsupEff=beff[0];
        binfEff=beff[1];
    }
    
    public void setBsup_Binf(double[] b) {
        bsup=b[0];
        binf=b[1];
    }
    
    public ParametriStatici getParametriStatici(double tw, double ro) {
        double AslI = (binf + bsup + tsl1) * tw + bsl * tsl1 + (hsl - tsl1) * tsl2;
        double xslI = ((hsl - tsl1) * tsl2 * (tw / 2 + bsl - tsl2 / 2) + bsl * tsl1
                * (tw / 2 + bsl / 2))/ AslI;
        double 	IslI = ((binf + bsup + tsl1) * Math.pow(tw, 3)) / 12
                + Math.pow(bsl, 3) * tsl1 / 12;
        IslI += (binf + bsup + tsl1) * tw * xslI * xslI + (hsl - tsl1) * tsl2
                * Math.pow(bsl + tw / 2 - tsl2 / 2 - xslI, 2);
        IslI += bsl * tsl1 * Math.pow((tw / 2 + bsl / 2 - xslI), 2);
        
        return new ParametriStatici(AslI,IslI,xslI,0);
    }
    
    public ParametriStatici getParametriStaticiEff(double tw, double ro) {
        double AslI = (binfEff + bsupEff + tsl1) * tw + bsl * tsl1 + (hsl - tsl1) * tsl2;
        double xslI = ((hsl - tsl1) * tsl2 * (tw / 2 + bsl - tsl2 / 2) + bsl * tsl1
                * (tw / 2 + bsl / 2))/ AslI;
        double 	IslI = ((binfEff + bsupEff + tsl1) * Math.pow(tw, 3)) / 12
                + Math.pow(bsl, 3) * tsl1 / 12;
        IslI += (binfEff + bsupEff + tsl1) * tw * xslI * xslI + (hsl - tsl1) * tsl2
                * Math.pow(bsl + tw / 2 - tsl2 / 2 - xslI, 2);
        IslI += bsl * tsl1 * Math.pow((tw / 2 + bsl / 2 - xslI), 2);
        
        return new ParametriStatici(AslI,IslI,xslI,0);
    }
    
    public ParametriStatici getParametriStaticiEffGlobali(double tw, double roc) {
        // pannello 1
        double a1 = roc * tsl1 * bsl;
        double y1 = y;
        double j1 = bsl * Math.pow(tsl1 * roc, 3) / 12;
        
        // pannello 2
        double b = hsl - tsl1;
        double a2 = tsl2 * roc * b;
        double y2 = y1 + tsl1 / 2 + b / 2;
        double j2 = tsl2 * roc * Math.pow(b, 3) / 12;
        
        //anima
        double bw = (bsupEff+binfEff+tsl1);
        double a3 = tw * roc * bw;
        double y3 = y1 - tsl1 / 2 -binfEff + bw / 2;
        double j3 = tsl2 * roc * Math.pow(bw, 3) / 12;
        
        double at = a1+a2+a3;
        double yg = (a1*y1+a2*y2+a3*y3)/at;
        double jt = j1+j2+j3+a1*(y1-yg)*(y1-yg)+a2*(y2-yg)*(y2-yg)+a3*(y3-yg)*(y3-yg);
        
        return new ParametriStatici(at,jt,yg,0);
        
    }
    
    public Shape getShape(double tw) {
        GeneralPath gp=new GeneralPath();
        
        gp.moveTo(tw/2,y-tsl1/2);
        gp.lineTo(tw/2+bsl,y-tsl1/2);
        gp.lineTo(tw/2+bsl,y-tsl1/2+hsl);
        gp.lineTo(tw/2+bsl-tsl2,y-tsl1/2+hsl);
        gp.lineTo(tw/2+bsl-tsl2,y+tsl1/2);
        gp.lineTo(tw/2,y+tsl1/2);
        gp.closePath();
        
        return gp;
        
    }
    
    public Shape getShape(double tw, double ro) {
        
        GeneralPath gp=new GeneralPath();
        
        gp.moveTo(-tw/2,y-tsl1/2-binf);
        gp.lineTo(tw/2, y-tsl1/2-binf);
        gp.lineTo(tw/2, y-tsl1/2);
        gp.lineTo(tw/2+bsl, y-tsl1/2);
        gp.lineTo(tw/2+bsl, y-tsl1/2+hsl);
        gp.lineTo(tw/2+bsl-tsl2, y-tsl1/2+hsl);
        gp.lineTo(tw/2+bsl-tsl2, y-tsl1/2+tsl1);
        gp.lineTo(tw/2, y+tsl1/2);
        gp.lineTo(tw/2, y+tsl1/2+bsup);
        gp.lineTo(-tw/2, y+tsl1/2+bsup);
        gp.closePath();
        
        return gp;
    }
    
    public Shape getShapeEff(double tw, double ro) {
        GeneralPath gp=new GeneralPath();
        
        gp.moveTo(-tw/2,y-tsl1/2-binfEff);
        gp.lineTo(tw/2, y-tsl1/2-binfEff);
        gp.lineTo(tw/2, y-tsl1/2);
        gp.lineTo(tw/2+bsl, y-tsl1/2);
        gp.lineTo(tw/2+bsl, y-tsl1/2+hsl);
        gp.lineTo(tw/2+bsl-tsl2, y-tsl1/2+hsl);
        gp.lineTo(tw/2+bsl-tsl2, y-tsl1/2+tsl1);
        gp.lineTo(tw/2, y+tsl1/2);
        gp.lineTo(tw/2, y+tsl1/2+bsupEff);
        gp.lineTo(-tw/2, y+tsl1/2+bsupEff);
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
    
    
    public double getTorsionalConstantSVenant() {
 
        double It = ((bsl-tsl2) * Math.pow(tsl1, 3) + (hsl) * Math.pow(tsl2, 3)) / 3.00;
        if(!doppio) return It;
        else return 2*It;
        
            
        
    }
    
    public double getPolarSecondMoment() {
        
        double h1 = bsl-tsl2;
        double a1 = h1*tsl1;
        double y1 = h1/2;
        double j1 = tsl1*Math.pow(h1,3)/12.00;
        
        double h2 = hsl;
        double a2 = h2*tsl2;
        double y2 = bsl-tsl2/2;
        double j2 = hsl*Math.pow(tsl2,3)/12.00;
        
        double at = a1+a2;
        double y = (a1*y1+a2*y2)/at;
        double Iy = j1+j2+a1*(y1-y)*(y1-y)+a2*(y2-y)*(y2-y)+at*y*y;
        
        double Iz = (bsl-tsl2) * Math.pow(tsl1, 3) / 12.00 + Math.pow(hsl, 3)
        * tsl2 / 12.00;
        
        if(!doppio)
            return Iz + Iy;
        else return 2*(Iz + Iy);
    }
    
    public double getWarpingCostant(double tw) {
        
        if(!doppio)
            return hsl*hsl*hsl*tsl2/12.00*(bsl-tsl2/2)*(bsl-tsl2/2);
        else{
            double h = bsl*2+tw - tsl2;
            return (tsl2*(h*h)*(hsl*hsl*hsl))/24.00;
        }
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
