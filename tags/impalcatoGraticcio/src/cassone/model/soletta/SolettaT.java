package cassone.model.soletta;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;

//import javax.swing.plaf.basic.DragRecognitionSupport.BeforeDrag;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Cell;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PRAcroForm;

import cassone.exception.XsezioneNonValida;
import cassone.model.Campate;
import cassone.model.ParametriStatici;
import cassone.util.MainRTFCreator;
import cassone.view.grafica.Drawing;

public class SolettaT implements Soletta {

	String name = "S1";

	double b, binf, h, hinf;

	double  bo;

	public SolettaT() {
		this.bo = 0;
		this.b = 4000;
		this.binf = 0;
		this.h = 300;
		this.hinf = 0;
	}

	public SolettaT(String nome) {
		this.name= nome;
	}

	
	
	public SolettaT(double b, double binf, double h, double hinf, 
			double bc) {
		this.bo = bc;
		this.b = b;
		this.binf = binf;
		this.h = h;
		this.hinf = hinf;
		
	}

	public ParametriStatici getParametriStatici(boolean calcolaBeff,Campate campate, int nCampata) {

		double Ac1, Ac2, Acls;
		double Jc1, Jc2, Jc = 0, jw = 0;
		double ygc1, ygc2, ygc = 0;
                
                double b = getBeffAnalisiGlobale(campate,nCampata,calcolaBeff);
                
		Ac1 = b * h;
		Ac2 = binf * hinf;
		Acls = Ac1 + Ac2;
		Jc1 = b * h * h * h / 12;
		Jc2 = binf * hinf * hinf * hinf / 12;
		ygc1 = -(hinf + h / 2);
		ygc2 = -hinf / 2;
		ygc = 0;
		if (Acls != 0)
			ygc = (Ac1 * ygc1 + Ac2 * ygc2) / Acls;

		Jc = Jc1 + Jc2 + Ac1 * (ygc - ygc1) * (ygc - ygc1) + Ac2 * (ygc - ygc2)
				* (ygc - ygc2);
		
		jw = (b*h*h*h)/6;
		
		return new ParametriStatici(Acls, Jc, ygc, jw);

	}

	public double getHsoletta() {
		return hinf + h;
	}

	public ParametriStatici getParametriStatici(boolean calcolaBeff,Campate campate, int nCampata,
			double xCampata,double yn, boolean Mpos) {


                double B = b;
		double b = binf;
		double h = hinf;
		double H = this.h;

            if(calcolaBeff){
                double []beff = get_b1b2_VerificaSezioni(campate,nCampata,xCampata);
                B = beff[0]+beff[1];
            }
		//attenzione implementare calcolo automatico beff
		double yp = -yn;
		double Ac1, Ac2, Acls;
		double Jc1, Jc2, Jc = 0;
		double ygc1, ygc2, ygc = 0;

		// il momento non  positivo
		if (!Mpos) {
			B = 0;
			b = 0;
		}

                
                		// tutta reagente
			Ac1 = B * H;
			Ac2 = h * b;
			Acls = Ac1 + Ac2;
			if (Acls != 0)
				ygc = (B * H * (h + H / 2) + b * h * (h / 2)) / Acls;
			Jc1 = B * H * H * H / 12;
			Jc2 = b * h * h * h / 12;
			ygc1 = H / 2 + h;
			ygc2 = h / 2;
			Jc = Jc1 + Jc2 + Ac1 * Math.pow(ygc1 - ygc, 2) + Ac2
					* Math.pow(ygc2 - ygc, 2);
            
		// tutta reagente
/*		if (yp <= 0) {
			Ac1 = B * H;
			Ac2 = h * b;
			Acls = Ac1 + Ac2;
			if (Acls != 0)
				ygc = (B * H * (h + H / 2) + b * h * (h / 2)) / Acls;
			Jc1 = B * H * H * H / 12;
			Jc2 = b * h * h * h / 12;
			ygc1 = H / 2 + h;
			ygc2 = h / 2;
			Jc = Jc1 + Jc2 + Ac1 * Math.pow(ygc1 - ygc, 2) + Ac2
					* Math.pow(ygc2 - ygc, 2);
		}
		// in primo tratto bxh
		else if (yp > 0 && yp < h) {
			Ac1 = B * H;
			Ac2 = (h - yp) * b;
			Acls = Ac1 + Ac2;
			if (Acls != 0)
				ygc = (B * H * (h + H / 2) + b * (h - yp) * (yp / 2 + h / 2))
						/ Acls;
			Jc1 = B * H * H * H / 12;
			Jc2 = b * (h - yp) * (h - yp) * (h - yp) / 12;
			ygc1 = H / 2 + h;
			ygc2 = yp / 2 + h / 2;
			Jc = Jc1 + Jc2 + Ac1 * Math.pow(ygc1 - ygc, 2) + Ac2
					* Math.pow(ygc2 - ygc, 2);

		}
		// secondo tratto BxH
		else if (yp >= h && yp < h + H) {
			double dy = yp - h;
			Ac1 = B * (H - dy);
			Ac2 = 0;
			Acls = Ac1 + Ac2;
			if (Acls != 0)
				ygc = (H - dy) / 2 + yp;
			Jc1 = B * (H - dy) * (H - dy) * (H - dy) / 12;
			Jc2 = 0;
			ygc1 = ygc;
			Jc = Jc1;

		} else {
			Ac1 = 0;
			Ac2 = 0;
			Acls = 0;
			ygc = 0;
			Jc = 0;
		}

		ygc = -1 * ygc;

*/		return new ParametriStatici(Acls, Jc, -ygc, 0);
	}

	public SolettaT getCopia() {
		return new SolettaT(b, binf, h, hinf,  bo);
	}

	public double getB() {
		return b;
	}

	public void setB(double b) {
		this.b = b;
	}

	public double getBo() {
		return bo;
	}

	public void setBo(double bc) {
		this.bo = bc;
	}

	public double getBinf() {
		return binf;
	}

	public void setBinf(double binf) {
		this.binf = binf;
	}

	public double getH() {
		return h;
	}

	public void setH(double h) {
		this.h = h;
	}

	public double getHinf() {
		return hinf;
	}

	public void setHinf(double hinf) {
		this.hinf = hinf;
	}

	public double getBeffAnalisiGlobale(Campate campate, int nCampata,boolean calcoloBeff) {

		if (!calcoloBeff)
			return b;

		double[] l = campate.getLuci();
		int nTotc = campate.getNCampate();
		double Le, be1, be2;
		double b1 = b/2-bo/2;
		double b2 = b/2-bo/2;
		
		// se la campata non esiste restituisce 0
		if (nCampata > nTotc - 1)
			return 0;

		// caso campate esterne
		if (nCampata == 0 || nCampata == nTotc - 1) {
			Le = 0.8 * l[nCampata] * 1000;
			be1 = Math.min(Le / 8, b1);
			be2 = Math.min(Le / 8, b2);
		} else// caso campate interne
		{
			Le = 0.7 * l[nCampata] * 1000;
			be1 = Math.min(Le / 8, b1);
			be2 = Math.min(Le / 8, b2);
		}

		return bo + be1 + be2;
	}

	public double getBeffSezioniVerifica(Campate campate, int nCampata,
			double xSezione,boolean calcoloBeff) {

		if (!calcoloBeff)
			return b;

		double b1 = b/2-bo/2;
		double b2 = b/2-bo/2;
		double[] l = campate.getLuci();
		int nTotc = campate.getNCampate();
		double Le, be1, be2, beff0, beff1, beff2, beta1, beta2;

		// caso campata 1
		if (nCampata == 0) {
			Le = 0.8 * l[0] * 1000;
			be1 = Math.min(Le / 8, b1);
			be2 = Math.min(Le / 8, b2);

			beta1 = Math.min((0.55 + 0.025 * Le / b1), 1);
			beta2 = Math.min((0.55 + 0.025 * Le / b2), 1);

			beff0 = bo + beta1 * be1 + beta2 * be2;
			beff1 = bo + be1 + be2;
			// se non è una unica campata calcola beff2
			if (nTotc > 1) {
				Le = 0.25 * (l[0] + l[1]) * 1000;
				be1 = Math.min(Le / 8, b1);
				be2 = Math.min(Le / 8, b2);
				beff2 = bo + be1 + be2;
			} else {
				beff2 = beff0;
			}
			double dx = xSezione / l[0];
			// interpolo lineramente per valori intermedi di xsezione
			if (dx >= 0.25 && dx <= 0.75)
				return beff1;
			if (dx < 0.25)
				return xSezione * (beff1 - beff0) / (l[0] / 4) + beff0;
			if (dx > 0.75)
				return beff1 + (dx - 0.75) * (beff2 - beff1) / 0.25;

		}
		// caso campata ultima
		if (nCampata == nTotc - 1) {
			Le = 0.8 * l[nCampata] * 1000;
			be1 = Math.min(Le / 8, b1);
			be2 = Math.min(Le / 8, b2);

			beta1 = Math.min((0.55 + 0.025 * Le / b1), 1);
			beta2 = Math.min((0.55 + 0.025 * Le / b2), 1);

			beff0 = bo + beta1 * be1 + beta2 * be2;
			beff1 = bo + be1 + be2;
			// se non è una unica campata calcola beff2
			if (nTotc > 1) {
				Le = 0.25 * (l[nCampata] + l[nCampata - 1]) * 1000;
				be1 = Math.min(Le / 8, b1);
				be2 = Math.min(Le / 8, b2);
				beff2 = bo + be1 + be2;
			} else {
				beff2 = beff0;
			}
			// interpolo lineramente per valori intermedi di xsezione
			double dx = xSezione / l[nCampata];
			if (dx >= 0.25 && dx <= 0.75)
				return beff1;
			if (dx < 0.25)
				return dx * (beff1 - beff2) / 0.25 + beff2;
			if (dx > 0.75)
				return beff1 + (beff0 - beff1) / 0.25 * (dx - 0.75);
		}

		// caso campate interne
		if (nCampata > 0 && nCampata < nTotc - 1) {
			Le = 0.7 * l[nCampata] * 1000;
			be1 = Math.min(Le / 8, b1);
			be2 = Math.min(Le / 8, b2);

			beff1 = bo + be1 + be2;
			// se non  una unica campata calcola beff2
			Le = 0.25 * (l[nCampata] + l[nCampata - 1]) * 1000;
			be1 = Math.min(Le / 8, b1);
			be2 = Math.min(Le / 8, b2);
			double beff2a = bo + be1 + be2;
			Le = 0.25 * (l[nCampata] + l[nCampata + 1]) * 1000;
			be1 = Math.min(Le / 8, b1);
			be2 = Math.min(Le / 8, b2);
			double beff2b = bo + be1 + be2;

			// interpolo lineramente per valori intermedi di xsezione
			double dx = xSezione / l[nCampata];
			if (dx >= 0.25 && dx <= 0.75)
				return beff1;
			if (dx < 0.25)
				return dx * (beff1 - beff2a) / 0.25 + beff2a;
			if (dx > 0.75)
				return beff1 + (beff2b - beff1) / 0.25 * (dx - 0.75);
		}

		return 0;
	}

	private double[] get_b1b2_AnalisiGlobale(Campate campate, int nCampata) {

		double[] l = campate.getLuci();
		int nTotc = campate.getNCampate();
		double Le, be1, be2;
		double b1 = b/2-bo/2;
		double b2 = b/2-bo/2;
	
		// caso campate esterne
		if (nCampata == 0 || nCampata == nTotc - 1) {
			Le = 0.8 * l[nCampata] * 1000;
			be1 = Math.min(Le / 8, b1);
			be2 = Math.min(Le / 8, b2);
		} else// caso campate interne
		{
			Le = 0.7 * l[nCampata] * 1000;
			be1 = Math.min(Le / 8, b1);
			be2 = Math.min(Le / 8, b2);
		}

		double[] nd = new double[2];
		nd[0] = be1;
		nd[1] = be2;
		return nd;
	}
	public double[] get_b1b2_VerificaSezioni(Campate campate, int nCampata,double xSezione){
		
		double[] l = campate.getLuci();
		int nTotc = campate.getNCampate();
		double Le, be1, be2;
		double b1 = b/2-bo/2;
		double b2 = b/2-bo/2;

		double beff0_s,beff0_d, beff1_s, beff1_d, beff2_s,beff2_d, beta1, beta2;

		// caso campata 1
		if (nCampata == 0) {
			Le = 0.8 * l[0] * 1000;
			be1 = Math.min(Le / 8, b1);
			be2 = Math.min(Le / 8, b2);

			beta1 = Math.min((0.55 + 0.025 * Le / b1), 1);
			beta2 = Math.min((0.55 + 0.025 * Le / b2), 1);

			beff0_s = bo/2 + beta1 * be1;
			beff0_d = bo/2 + beta2 * be2;
			
			beff1_s = bo/2 + be1;
			beff1_d = bo/2 + be2;
			// se non è una unica campata calcola beff2
			if (nTotc > 1) {
				Le = 0.25 * (l[0] + l[1]) * 1000;
				be1 = Math.min(Le / 8, b1);
				be2 = Math.min(Le / 8, b2);
				beff2_s = bo/2 + be1 ;
				beff2_d = bo/2 + be2;
			} else {
				beff2_s = beff0_s;
				beff2_d = beff0_d;
			}
			double dx = xSezione / l[0];
			// interpolo lineramente per valori intermedi di xsezione
			if (dx >= 0.25 && dx <= 0.75)
				return new double[] {beff1_s,beff1_d};
			if (dx < 0.25)
				return new double[] {xSezione * (beff1_s - beff0_s) / (l[0] / 4) + beff0_s
					,xSezione * (beff1_d - beff0_d) / (l[0] / 4) + beff0_d};
			if (dx > 0.75)
				return new double[] {beff1_s + (dx - 0.75) * (beff2_s - beff1_s) / 0.25
					,beff1_d + (dx - 0.75) * (beff2_d - beff1_d) / 0.25};
		}
		// caso campata ultima
		if (nCampata == nTotc - 1) {
			Le = 0.8 * l[nCampata] * 1000;
			be1 = Math.min(Le / 8, b1);
			be2 = Math.min(Le / 8, b2);

			beta1 = Math.min((0.55 + 0.025 * Le / b1), 1);
			beta2 = Math.min((0.55 + 0.025 * Le / b2), 1);

			beff0_s = bo/2 + beta1 * be1 ;
			beff0_d = bo/2 + beta2 * be2;
			
			beff1_s = bo/2 + be1 ;
			beff1_d = bo/2 +  be2;
			// se non  una unica campata calcola beff2
			if (nTotc > 1) {
				Le = 0.25 * (l[nCampata] + l[nCampata - 1]) * 1000;
				be1 = Math.min(Le / 8, b1);
				be2 = Math.min(Le / 8, b2);
				beff2_s = bo/2 + be1;
				beff2_d = bo/2 + be2;
				
			} else {
				beff2_s = beff0_s;
				beff2_d = beff0_d;
			}
			// interpolo lineramente per valori intermedi di xsezione
			double dx = xSezione / l[nCampata];
			if (dx >= 0.25 && dx <= 0.75)
				return new double[] {beff1_s,beff1_d};
			if (dx < 0.25)
				return new double[] {dx * (beff1_s - beff2_s) / 0.25 + beff2_s,
					dx * (beff1_d - beff2_d) / 0.25 + beff2_d};
			if (dx > 0.75)
				return new double[] {beff1_s + (beff0_s - beff1_s) / 0.25 * (dx - 0.75),
					beff1_d + (beff0_d - beff1_d) / 0.25 * (dx - 0.75)};
		}

		// caso campate interne
		if (nCampata > 0 && nCampata < nTotc - 1) {
			Le = 0.7 * l[nCampata] * 1000;
			be1 = Math.min(Le / 8, b1);
			be2 = Math.min(Le / 8, b2);

			beff1_s = bo/2 + be1 ;
			beff1_d = bo/2 + be2;
			
			// se non  una unica campata calcola beff2
			Le = 0.25 * (l[nCampata] + l[nCampata - 1]) * 1000;
			be1 = Math.min(Le / 8, b1);
			be2 = Math.min(Le / 8, b2);
			double beff2a_s = bo/2 + be1 ;
			double beff2a_d = bo/2 + be2;
			Le = 0.25 * (l[nCampata] + l[nCampata + 1]) * 1000;
			be1 = Math.min(Le / 8, b1);
			be2 = Math.min(Le / 8, b2);
			double beff2b_s = bo/2 + be1 ;
			double beff2b_d = bo/2 +  be2;

			// interpolo lineramente per valori intermedi di xsezione
			double dx = xSezione / l[nCampata];
			if (dx >= 0.25 && dx <= 0.75)
				return new double []{beff1_s,beff1_d};
			if (dx < 0.25)
				return new double []{dx * (beff1_s - beff2a_s) / 0.25 + beff2a_s,
					dx * (beff1_d - beff2a_d) / 0.25 + beff2a_d};
			if (dx > 0.75)
				return new double []{beff1_s + (beff2b_s - beff1_s) / 0.25 * (dx - 0.75),
					beff1_d + (beff2b_d - beff1_d) / 0.25 * (dx - 0.75)};
		}

		return new double []{0,0};
	
	}
	
	public  String toString() {
			return name;
		}
	
	public Shape getShape(boolean calcoloBeff,Campate campate, int nCampata) {
		GeneralPath pat = new GeneralPath();
		double b1 = b/2-bo/2;
		double b2 = b/2-bo/2;
	
		if (!calcoloBeff) {
			pat.moveTo(-binf / 2, 0);
			pat.lineTo(-binf / 2, -hinf);
			pat.lineTo(-b / 2, -hinf);
			pat.lineTo(-b / 2, -hinf - h);
			pat.lineTo(+b / 2, -hinf - h);
			pat.lineTo(+b / 2, -hinf);
			pat.lineTo(+binf / 2, -hinf);
			pat.lineTo(+binf / 2, 0);
			pat.closePath();
		}else{
			double[] b = get_b1b2_AnalisiGlobale(campate, nCampata);
			b1=b[0];b2=b[1];
			pat.moveTo(-binf / 2, 0);
			pat.lineTo(-binf / 2, -hinf);
			pat.lineTo(-b1 -bo/2, -hinf);
			pat.lineTo(-b1 -bo/2, -hinf - h);
			pat.lineTo(+b2 +bo/2, -hinf - h);
			pat.lineTo(+b2 +bo/2, -hinf);
			pat.lineTo(+binf / 2, -hinf);
			pat.lineTo(+binf / 2, 0);
			pat.closePath();
		}

		return pat;
	}


	public void quota(Graphics2D g2d, int dd, boolean calcoloBeff,Campate campate, int nCampate) {
		AffineTransform old = g2d.getTransform();

		AffineTransform at = new AffineTransform();
		at.setToTranslation(0, -h - hinf - 5 * dd);
		g2d.transform(at);
		
		// quota soletta superiore
		if (calcoloBeff) {
			double b[] = get_b1b2_AnalisiGlobale(campate, nCampate);
			double b1 = Math.round(b[0]);
			double b2 = Math.round(b[1]);

			Drawing.disegnaQuotaTraDuePunti(-bo / 2 - b1, bo / 2 + b2, "Beff: "
					+ Double.toString(bo + b1 + b2), dd, g2d);
			at.setToTranslation(0, 3 * dd);
			g2d.transform(at);
			Drawing.disegnaQuotaTraDuePunti(-bo / 2 - b1, -bo / 2, "be1: "
					+ Double.toString(b1), dd, g2d);
			Drawing.disegnaQuotaTraDuePunti(-bo / 2, bo / 2, "b0: "
					+ Double.toString(bo), dd, g2d);
			Drawing.disegnaQuotaTraDuePunti(bo / 2, bo / 2 + b2, "be2: "
					+ Double.toString(b2), dd, g2d);


		} else {
			Drawing.disegnaQuotaTraDuePunti(-b / 2, b / 2, "", dd, g2d);
			if(binf!=0){
				g2d.setTransform(old);
				at.setToTranslation(0, -h - hinf - 2 * dd);
				g2d.transform(at);
				Drawing.disegnaQuotaTraDuePunti(-b/2 , -binf/ 2,"", dd, g2d);
				Drawing.disegnaQuotaTraDuePunti(-binf/2, binf/ 2,"", dd, g2d);
				Drawing.disegnaQuotaTraDuePunti(binf/2, b/ 2,"", dd, g2d);
			}
		}
		
		// quote verticali
		g2d.setTransform(old);
//		if(calcoloBeff)	at.setToTranslation(b2+bo/2 + 6 * dd,0);
		/*else*/ at.setToTranslation(b/2 + 6 * dd,0);
		g2d.transform(at);
		at.setToRotation(-Math.PI / 2);
		g2d.transform(at);
		Drawing.disegnaQuotaTraDuePunti(0, h+hinf, "", dd, g2d);
		if(hinf!=0){
			at.setToTranslation(0,-3 * dd);			
			g2d.transform(at);
			Drawing.disegnaQuotaTraDuePunti(0 ,hinf,"", dd, g2d);
			Drawing.disegnaQuotaTraDuePunti(hinf ,hinf+h,"", dd, g2d);
		}

		g2d.setTransform(old);
	}

	public double getBtot() {
		return b;
}

	public Shape getShape(boolean calcolaBeff, Campate campate, int nCampata, double xCampata, double yn, boolean momentoPositivo) {
		GeneralPath pat = new GeneralPath();
		double b1 = b/2-bo/2;
		double b2 = b/2-bo/2;
	
		if(!momentoPositivo) return new GeneralPath();
		
		if (!calcolaBeff) {
			pat.moveTo(-binf / 2, 0);
			pat.lineTo(-binf / 2, -hinf);
			pat.lineTo(-b / 2, -hinf);
			pat.lineTo(-b / 2, -hinf - h);
			pat.lineTo(+b / 2, -hinf - h);
			pat.lineTo(+b / 2, -hinf);
			pat.lineTo(+binf / 2, -hinf);
			pat.lineTo(+binf / 2, 0);
			pat.closePath();
		}else{
			double[] b = get_b1b2_VerificaSezioni(campate, nCampata, xCampata);
			b1=b[0]-bo/2;
                        b2=b[1]-bo/2;
			pat.moveTo(-binf / 2, 0);
			pat.lineTo(-binf / 2, -hinf);
			pat.lineTo(-b1 -bo/2, -hinf);
			pat.lineTo(-b1 -bo/2, -hinf - h);
			pat.lineTo(+b2 +bo/2, -hinf - h);
			pat.lineTo(+b2 +bo/2, -hinf);
			pat.lineTo(+binf / 2, -hinf);
			pat.lineTo(+binf / 2, 0);
			pat.closePath();
		}
/*
		if(yn<0){
			Rectangle2D.Double rec = new Rectangle2D.Double();
			rec.setFrame(-getBtot()/2,yn,getBtot(),-yn);
			Area apat = new Area(pat);
			apat.subtract(new Area(rec));
			return apat;
		}
*/		
		return pat;
	}

	public double getHcacloloJw() {
		return h/2+hinf;
	}

	public double getSpMedioCalcoloJw() {
		return h;
	}

	public Table getTabellaInput() throws BadElementException {
	
		Cell c;
		int nc=0;
		Table table = new Table(2, 4);
		table.setBorder(Table.NO_BORDER);
		table.setPadding(2);
		table.setSpacing(2);
		
		
		c = MainRTFCreator.getCellaTipo("Larghezza totale: B(mm)", Cell.ALIGN_LEFT);
		table.addCell(c, nc, 0);
		c = MainRTFCreator.getCellaTipo(Double.toString(b), Cell.ALIGN_RIGHT);
		table.addCell(c, nc, 1);
		++nc;

		c = MainRTFCreator.getCellaTipo("Larghezza anime: b(mm)", Cell.ALIGN_LEFT);
		table.addCell(c, nc, 0);
		c = MainRTFCreator.getCellaTipo(Double.toString(binf), Cell.ALIGN_RIGHT);
		table.addCell(c, nc, 1);
		++nc;

		c = MainRTFCreator.getCellaTipo("Altezza soletta: H(mm)", Cell.ALIGN_LEFT);
		table.addCell(c, nc, 0);
		c = MainRTFCreator.getCellaTipo(Double.toString(h), Cell.ALIGN_RIGHT);
		table.addCell(c, nc, 1);
		++nc;

		c = MainRTFCreator.getCellaTipo("Altezza anima: h(mm)", Cell.ALIGN_LEFT);
		table.addCell(c, nc, 0);
		c = MainRTFCreator.getCellaTipo(Double.toString(h), Cell.ALIGN_RIGHT);
		table.addCell(c, nc, 1);
		++nc;

		return table;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void quota(Graphics2D g, int dd, boolean calcoloBeff, Campate campate, int nCampata, double xCampata) {
		AffineTransform old = g.getTransform();

		AffineTransform at = new AffineTransform();
		at.setToTranslation(0, -h - hinf - 5 * dd);
		g.transform(at);
		
		// quota soletta superiore
		if (calcoloBeff) {
			double b[] = get_b1b2_VerificaSezioni(campate, nCampata, xCampata);
				double b1 = Math.round(b[0]-bo/2);
				double b2 = Math.round(b[1]-bo/2);

			Drawing.disegnaQuotaTraDuePunti(-bo / 2 - b1, bo / 2 + b2, "Beff: "
					+ Double.toString(bo + b1 + b2), dd, g);
			at.setToTranslation(0, 3 * dd);
			g.transform(at);
			Drawing.disegnaQuotaTraDuePunti(-bo / 2 - b1, -bo / 2, "be1: "
					+ Double.toString(b1), dd, g);
			Drawing.disegnaQuotaTraDuePunti(-bo / 2, bo / 2, "b0: "
					+ Double.toString(bo), dd, g);
			Drawing.disegnaQuotaTraDuePunti(bo / 2, bo / 2 + b2, "be2: "
					+ Double.toString(b2), dd, g);


		} else {
			Drawing.disegnaQuotaTraDuePunti(-b / 2, b / 2, "", dd, g);
			if(binf!=0){
				g.setTransform(old);
				at.setToTranslation(0, -h - hinf - 2 * dd);
				g.transform(at);
				Drawing.disegnaQuotaTraDuePunti(-b/2 , -binf/ 2,"", dd, g);
				Drawing.disegnaQuotaTraDuePunti(-binf/2, binf/ 2,"", dd, g);
				Drawing.disegnaQuotaTraDuePunti(binf/2, b/ 2,"", dd, g);
			}
		}
		
		// quote verticali
		g.setTransform(old);
//		if(calcoloBeff)	at.setToTranslation(b2+bo/2 + 6 * dd,0);
		/*else*/ at.setToTranslation(b/2 + 6 * dd,0);
		g.transform(at);
		at.setToRotation(-Math.PI / 2);
		g.transform(at);
		Drawing.disegnaQuotaTraDuePunti(0, h+hinf, "", dd, g);
		if(hinf!=0){
			at.setToTranslation(0,-3 * dd);			
			g.transform(at);
			Drawing.disegnaQuotaTraDuePunti(0 ,hinf,"", dd, g);
			Drawing.disegnaQuotaTraDuePunti(hinf ,hinf+h,"", dd, g);
		}

		g.setTransform(old);		
	}

	
	
}
