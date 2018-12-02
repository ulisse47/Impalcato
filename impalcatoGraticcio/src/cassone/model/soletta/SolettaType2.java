package cassone.model.soletta;

import cassone.util.MainRTFCreator;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Cell;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;

import com.lowagie.text.Table;

import cassone.calcolo.Matematica;
import cassone.model.Campate;
import cassone.model.ParametriStatici;
import cassone.view.grafica.Drawing;

public class SolettaType2 implements Soletta {
	
	String name;
	double b,bs,bd,hs1,hs2,hd1,hd2;
	
	public SolettaType2(String nome) {
		this.name= nome;
	}

	//caloloBeff
	double bo;
	
	public double getB() {
		return b;
	}

	public void setB(double b) {
		this.b = b;
	}

	public double getBd() {
		return bd;
	}

	public void setBd(double bd) {
		this.bd = bd;
	}

	public double getBs() {
		return bs;
	}

	public void setBs(double bs) {
		this.bs = bs;
	}

	public double getHd1() {
		return hd1;
	}

	public void setHd1(double hd1) {
		this.hd1 = hd1;
	}

	public double getHd2() {
		return hd2;
	}

	public void setHd2(double hd2) {
		this.hd2 = hd2;
	}

	public double getHs1() {
		return hs1;
	}

	public void setHs1(double hs1) {
		this.hs1 = hs1;
	}

	public double getHs2() {
		return hs2;
	}

	public void setHs2(double hs2) {
		this.hs2 = hs2;
	}

	public SolettaType2(){
		name="Type1";
		b=300;
		bs=1500;
		bd=2000;
		hs1=100;
		hs2=200;
		hd1=200;
		hd2=300;
		
	}
	
	public double getBeffAnalisiGlobale(Campate campate, int campata,boolean calcolaBeff) {
		
            if(calcolaBeff){
                double[] b = get_b1b2_AnalisiGlobale(campate,campata);
                return b[0]+b[1];
            }else{
                return bs+bd+b;
            }
	}

	public double getBeffSezioniVerifica(Campate campate, int nCampata,
			double xSezione,boolean calcolaBeff) {
           if(calcolaBeff){
                double[] b = get_b1b2_VerificaSezioni(campate,nCampata,xSezione);
                return b[0]+b[1];
            }else{
                return bs+bd+b;
            }	}

	public double getBtot() {
		return bs+b+bd;
		}

    public double getHsoletta() {
		return Math.max(hd1+hd2, hs1+hs2);
	}

	public ParametriStatici getParametriAreaA (Area at){
		
		//inizializzo
		double dy = -getHsoletta()/1000;
		double yi=dy;
		double bt = getBtot();
		double A=0,J=0,AxYgi=0;
		double Yg=0;
		Area ril;
		do {
			Rectangle2D.Double ri = new Rectangle2D.Double();
			ri.setFrame(-bt,yi,2*bt,-dy);
			ril = new Area(ri);
			ril.intersect(at);
//			ri = ril.getBounds2D();
//			double ai = ri.getWidth()*ri.getHeight();
			double ygi = yi+ri.getHeight()/2;
			double ai = Matematica.get_Area_Yg(ril);
			AxYgi += ai*ygi;
			J+=ai*ygi*ygi;
			A+=ai;
			yi+=dy;
		} while ((-yi+dy)<getHsoletta() || !ril.isEmpty());
		
		//valori baricentrici
		if(A!=0) Yg = AxYgi/A;
		double Jn = J-A*Yg*Yg;
		
		return new ParametriStatici(A,Jn,Yg,0);
	}
	
	public ParametriStatici getParametriStatici(boolean calcolaBeff,Campate campate, int nCampata) {
		
		Area at = new Area(getShape(calcolaBeff, campate, nCampata));
		return getParametriAreaA(at);

	}

	public ParametriStatici getParametriStatici(boolean calcolaBeff,Campate campate,
			int nCampata,double xCampata,double yn, boolean Mpos) {
		
			Area a = new Area(getShape(calcolaBeff, campate, nCampata, xCampata, yn, Mpos));
			Rectangle2D r = a.getBounds2D();
			Rectangle2D.Double ry = new Rectangle2D.Double();
			ry.setFrame(-r.getWidth(),yn,2*r.getWidth(),-yn);
			a.subtract(new Area(ry));
			
			return getParametriAreaA(a);
		
		}

	public double[] get_b1b2_AnalisiGlobale(Campate campate, int nCampata) {
		
		double[] l = campate.getLuci();
		int nTotc = campate.getNCampate();
		double Le, be1, be2;
		double b1 = bs+b/2-bo/2;
		double b2 = bd+b/2-bo/2;
	
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
		nd[0] = be1+bo/2;
		nd[1] = be2+bo/2;
		return nd;
	}
	
	public Shape getShape(boolean calcolaBeff,Campate campate, int nCampata) {
		GeneralPath gp=new GeneralPath();
		
			gp.moveTo(-b/2,0);
			gp.lineTo(-b/2-bs,-hs1);
			gp.lineTo(-b/2-bs,-hs1-hs2);
			gp.lineTo(b/2+bd,-hd1-hd2);
			gp.lineTo(b/2+bd,-hd1);
			gp.lineTo(b/2,0);
			gp.closePath();

			if(calcolaBeff){
				double[]bl =get_b1b2_AnalisiGlobale(campate, nCampata);
				double b1 = bl[0];
				double b2 = bl[1];
				double h=getHsoletta();
				Rectangle2D.Double rec = new Rectangle2D.Double();
				rec.setFrame(-bs-b/2,-h,bs+b/2-b1,h);
				Rectangle2D.Double rec2 = new Rectangle2D.Double();
				rec2.setFrame(b2,-h,bd+b/2-b2,h);
				Area agp = new Area(gp);
				agp.subtract(new Area(rec));
				agp.subtract(new Area(rec2));
				return agp;
			}

			return gp;

			}


	public void quota(Graphics2D g, int dd, boolean calcoloBeff,Campate campate, int nCampate) {
		
		AffineTransform old = g.getTransform();

		AffineTransform at = new AffineTransform();
		at.setToTranslation(0, -getHsoletta() - 5 * dd);
		g.transform(at);
		
		// quota soletta superiore
		if (calcoloBeff) {
			double b[] = get_b1b2_AnalisiGlobale(campate, nCampate);
				double b1 = b[0]-bo/2;
				double b2 = b[1]-bo/2;

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
			Drawing.disegnaQuotaTraDuePunti(-b / 2-bs, +b / 2+bd, "", dd, g);
			if(b!=0){
				g.setTransform(old);
				at.setToTranslation(0, -getHsoletta() - 2 * dd);
				g.transform(at);
				Drawing.disegnaQuotaTraDuePunti(-b / 2-bs , -b/ 2,"", dd, g);
				Drawing.disegnaQuotaTraDuePunti(-b/2, b/ 2,"", dd, g);
				Drawing.disegnaQuotaTraDuePunti(b/2, b/ 2+bd,"", dd, g);
			}
		}
		
		// quote verticali
		g.setTransform(old);
		at.setToTranslation(b/2+bd + 6 * dd,0);
		g.transform(at);
		at.setToRotation(-Math.PI / 2);
		g.transform(at);
		Drawing.disegnaQuotaTraDuePunti(0, hd1, "", dd, g);
		Drawing.disegnaQuotaTraDuePunti(hd1, hd1+hd2, "", dd, g);

		g.setTransform(old);
		at.setToTranslation(-b/2-bs - 6 * dd,0);
		g.transform(at);
		at.setToRotation(-Math.PI / 2);
		g.transform(at);
		Drawing.disegnaQuotaTraDuePunti(0, hs1, "", dd, g);
		Drawing.disegnaQuotaTraDuePunti(hs1, hs1+hs2, "", dd, g);
		
		
		g.setTransform(old);

	}


	public String toString() {
		return name;
	}

	public double getBo() {
		return bo;
	}

	public void setBo(double bo) {
		this.bo = bo;
	}

	public double[] get_b1b2_VerificaSezioni(Campate campate, int nCampata,double xSezione){
		
		double[] l = campate.getLuci();
		int nTotc = campate.getNCampate();
		double Le, be1, be2;
		double b1 = bs+b/2-bo/2;
		double b2 = bd+b/2-bo/2;

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
			// se non è una unica campata calcola beff2
			if (nTotc > 1) {
				Le = 0.25 * (l[nCampata] + l[nCampata - 1]) * 1000;
				be1 = Math.min(Le / 8, b1);
				be2 = Math.min(Le / 8, b2);
				beff2_s = bo + be1;
				beff2_d = bo + be2;
				
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
			
			// se non è una unica campata calcola beff2
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
	
	public Shape getShape(boolean calcolaBeff, Campate campate, int nCampata, double xCampata, double yn, boolean momentoPositivo) {
		
		if(!momentoPositivo) return new GeneralPath();
	
		GeneralPath gp = new GeneralPath();
		Area a;
		
                gp.moveTo(-b/2,0);
                gp.lineTo(-b/2-bs,-hs1);
                gp.lineTo(-b/2-bs,-hs1-hs2);
                gp.lineTo(b/2+bd,-hd1-hd2);
                gp.lineTo(b/2+bd,-hd1);
                gp.lineTo(b/2,0);
                gp.closePath();
                a =new Area(gp);
	
		if(calcolaBeff){
			double[]bl =get_b1b2_VerificaSezioni(campate, nCampata,xCampata);
			double b1 = bl[0];
			double b2 = bl[1];
			double h=getHsoletta();
			Rectangle2D.Double rec = new Rectangle2D.Double();
			rec.setFrame(-bs-b/2,-h,bs+b/2-b1,h);
			Rectangle2D.Double rec2 = new Rectangle2D.Double();
			rec2.setFrame(b2,-h,bd+b/2-b2,h);
			a = new Area(gp);
			a.subtract(new Area(rec));
			a.subtract(new Area(rec2));

		}
		
/*                if(momentoPositivo){
                    //sottrae area non reagente
                    Rectangle2D.Double rec = new Rectangle2D.Double();
                    rec.setFrame(-getBtot(),yn,2*getBtot(),-yn);
                    a.subtract(new Area(rec));
                }else{
                    if(yn>-getHsoletta() && yn< 0){
                    Rectangle2D.Double rec = new Rectangle2D.Double();
                    rec.setFrame(-getBtot(),-getHsoletta(),2*getBtot(),-getHsoletta()+yn);
                    a.subtract(new Area(rec));
                    }else if(yn >0){
                        return new GeneralPath();
                    }
                }
  */              
		return a;
		
	}

	public double getHcacloloJw() {
		return ((hs1+hs2/2)+(hd1+hd2/2))/2;
	}

	public double getSpMedioCalcoloJw() {
		return hs2/2+hd2/2;
	}

	public Table getTabellaInput() throws BadElementException {
	
		Cell c;
		int nc=0;
		Table table = new Table(2, 8);
		table.setBorder(Table.NO_BORDER);
		table.setPadding(2);
		table.setSpacing(2);
		
		
		c = MainRTFCreator.getCellaTipo("Larghezza totale: B(mm)", Cell.ALIGN_LEFT);
		table.addCell(c, nc, 0);
		c = MainRTFCreator.getCellaTipo(Double.toString(getBtot()), Cell.ALIGN_RIGHT);
		table.addCell(c, nc, 1);
		++nc;

		c = MainRTFCreator.getCellaTipo("Larghezza tratto orizzontale inferiore: b(mm)", Cell.ALIGN_LEFT);
		table.addCell(c, nc, 0);
		c = MainRTFCreator.getCellaTipo(Double.toString(b), Cell.ALIGN_RIGHT);
		table.addCell(c, nc, 1);
		++nc;

                c = MainRTFCreator.getCellaTipo("Larghezza ala sinistra: bs(mm)", Cell.ALIGN_LEFT);
		table.addCell(c, nc, 0);
		c = MainRTFCreator.getCellaTipo(Double.toString(bs), Cell.ALIGN_RIGHT);
		table.addCell(c, nc, 1);
		++nc;

		c = MainRTFCreator.getCellaTipo("Quota bordo inferiore sinistro: hs1(mm)", Cell.ALIGN_LEFT);
		table.addCell(c, nc, 0);
		c = MainRTFCreator.getCellaTipo(Double.toString(hs1), Cell.ALIGN_RIGHT);
		table.addCell(c, nc, 1);
		++nc;

		c = MainRTFCreator.getCellaTipo("Spessore soletta bordo sinistro: hs2(mm)", Cell.ALIGN_LEFT);
		table.addCell(c, nc, 0);
		c = MainRTFCreator.getCellaTipo(Double.toString(hs2), Cell.ALIGN_RIGHT);
		table.addCell(c, nc, 1);
		++nc;

                c = MainRTFCreator.getCellaTipo("Larghezza ala destra: bd(mm)", Cell.ALIGN_LEFT);
		table.addCell(c, nc, 0);
		c = MainRTFCreator.getCellaTipo(Double.toString(bd), Cell.ALIGN_RIGHT);
		table.addCell(c, nc, 1);
		++nc;

                c = MainRTFCreator.getCellaTipo("Quota bordo inferiore destro: hd1(mm)", Cell.ALIGN_LEFT);
		table.addCell(c, nc, 0);
		c = MainRTFCreator.getCellaTipo(Double.toString(hs1), Cell.ALIGN_RIGHT);
		table.addCell(c, nc, 1);
		++nc;

		c = MainRTFCreator.getCellaTipo("Spessore soletta bordo destro: hd2(mm)", Cell.ALIGN_LEFT);
		table.addCell(c, nc, 0);
		c = MainRTFCreator.getCellaTipo(Double.toString(hs2), Cell.ALIGN_RIGHT);
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
		at.setToTranslation(0, -getHsoletta() - 5 * dd);
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
			Drawing.disegnaQuotaTraDuePunti(-b / 2-bs, +b / 2+bd, "", dd, g);
			if(b!=0){
				g.setTransform(old);
				at.setToTranslation(0, -getHsoletta() - 2 * dd);
				g.transform(at);
				Drawing.disegnaQuotaTraDuePunti(-b / 2-bs , -b/ 2,"", dd, g);
				Drawing.disegnaQuotaTraDuePunti(-b/2, b/ 2,"", dd, g);
				Drawing.disegnaQuotaTraDuePunti(b/2, b/ 2+bd,"", dd, g);
			}
		}
		
		// quote verticali
		g.setTransform(old);
		at.setToTranslation(b/2+bd + 6 * dd,0);
		g.transform(at);
		at.setToRotation(-Math.PI / 2);
		g.transform(at);
		Drawing.disegnaQuotaTraDuePunti(0, hd1, "", dd, g);
		Drawing.disegnaQuotaTraDuePunti(hd1, hd1+hd2, "", dd, g);

		g.setTransform(old);
		at.setToTranslation(-b/2-bs - 6 * dd,0);
		g.transform(at);
		at.setToRotation(-Math.PI / 2);
		g.transform(at);
		Drawing.disegnaQuotaTraDuePunti(0, hs1, "", dd, g);
		Drawing.disegnaQuotaTraDuePunti(hs1, hs1+hs2, "", dd, g);
		
		
		g.setTransform(old);		
	}

}
