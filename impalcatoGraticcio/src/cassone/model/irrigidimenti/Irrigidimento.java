package cassone.model.irrigidimenti;

import java.awt.Shape;

import cassone.model.MaterialeAcciaio;
import cassone.model.ParametriStatici;

public interface Irrigidimento {

	//restituisce la larghezza della zona ancorata
	public double getT();
	
	//posizione dell'irrigidimento
	public double getY();
	
	public ParametriStatici getParametriStatici(double bsup, double binf,double tw, double ro);

	//calcola i parametri static dell'irrigidimento più la porzione di anima
	public ParametriStatici getParametriStatici(double tw, double ro);

	//calcola i parametri static dell'irrigidimento più la porzione EFFICACE di anima
	public ParametriStatici getParametriStaticiEff(double tw, double ro);

	public double getArea();
	
	public double getAreaEff();
	
	public void setBsup_Binf(double[] b);
	
	public void setBsupEff_BinfEff(double[] beff);
	
	public ParametriStatici getParametriStaticiEffGlobali(double tw,double roc);
	
	public Shape getShape(double tw);

	public Shape getShape(double tw,double ro);

	public Shape getShapeEff(double tw,double ro);
	
	public double getPasso();
	
	public void setPasso(double a);
	
	public boolean isDoppio();


	public void setDoppio(boolean doppio);
	
	public boolean isRigidEndPost();


	public void setRigidEndPost(boolean rigidEndPost);
	
	public double getTorsionalConstantSVenant();
	public double getPolarSecondMoment();
	public double getWarpingCostant(double tw);
	
	public double getSigCriticoTorsionale( MaterialeAcciaio mat,double h,double tw);

	
}
