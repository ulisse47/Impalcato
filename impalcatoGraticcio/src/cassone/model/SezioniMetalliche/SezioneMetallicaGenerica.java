package cassone.model.SezioniMetalliche;

import com.lowagie.text.Cell;
import com.lowagie.text.Element;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.GeneralPath;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Table;

import cassone.model.GiuntoBullonato;
import cassone.model.MaterialeAcciaio;
import cassone.model.ParametriStatici;
import cassone.model.Sezione;
import cassone.model.SezioneOutputTensioniFase;
import cassone.model.irrigidimenti.Irrigidimento;
import java.text.NumberFormat;

public class SezioneMetallicaGenerica implements SezioneMetallica {

	private double hgen,asgen,jgen,yggen,awgen;
	private String name="G1";
	
	private double ss, ii,i,s;
	
	double sEff, iEff;

                    double Msez, Nsez ,Vsez ;

	boolean piattabandaSupIrrigidita;
	
	public boolean isPiattabandaSupIrrigidita() {
		return piattabandaSupIrrigidita;
	}


	public void setPiattabandaSupIrrigidita(boolean piattabandaSupIrrigidita) {
		this.piattabandaSupIrrigidita = piattabandaSupIrrigidita;
	}

	

	public SezioneMetallicaGenerica(){
		name="Gen";
		hgen = 2000;
		asgen = 80000;
		jgen=10E11;
		yggen=1000;
		awgen=2000;
		
	}
	public SezioneMetallicaGenerica(String name) {
		this.name= name;
	}

	
	public SezioneMetallicaGenerica(SezioneMetallicaGenerica sm, String nome){
		this.name=nome;
		hgen = sm.getHgen();
		asgen =sm.getAsgen();
		jgen=sm.getJgen();
		yggen=sm.getYggen();
		awgen=sm.getAwgen();
		piattabandaSupIrrigidita=sm.isPiattabandaSupIrrigidita();

	}
	
	
	public SezioneMetallica getCopia() {
		// TODO Auto-generated method stub
		return null;
	}

	public double getHtot() {
		return hgen;
	}

	public ParametriStatici getParametriStatici() {
		
		return new ParametriStatici(asgen,jgen,yggen,0);
	}

	public ParametriStatici getParametriStatici(double yn) {
		return new ParametriStatici(0,0,0,0);
	}

	public ParametriStatici getParametriStaticiS1() {
		return new ParametriStatici(0,0,0,0);
	}

	public ParametriStatici getParametriStaticiS2() {
		return getParametriStatici();
	}

	public double getYs1() {
		return 0;
	}

	public double getYs2() {
		return hgen;
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

	//restitruisce uno spessore fittizio unitario
	public double getTw() {
		return 1;
	}
	public double getTwTot() {
		return 1;
	}

	public double getAsgen() {
		return asgen;
	}

	public void setAsgen(double asgen) {
		this.asgen = asgen;
	}

	public double getAwgen() {
		return awgen;
	}

	public void setAwgen(double awgen) {
		this.awgen = awgen;
	}

	public double getJgen() {
		return jgen;
	}

	public void setJgen(double jgen) {
		this.jgen = jgen;
	}

	public double getYggen() {
		return yggen;
	}

	public void setYggen(double yggen) {
		this.yggen = yggen;
	}

	public double getHgen() {
		return hgen;
	}

	public void setHgen(double hgen) {
		this.hgen = hgen;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name=name;
		
	}

	public void calcolaTensioniEfficaci(MaterialeAcciaio mat, SezioneOutputTensioniFase so,
			double passoIrrigidimentiTrasv,boolean rigidEndPost) {
                        
            ss=so.getSs();
            ii=so.getIi();
            
            sEff=so.getSs();
            iEff=so.getIi();
           
            double[] M = getM_N_SezioneMetallica();
		Msez = M[0];
		Nsez = M[1];
		Vsez = so.getVtaglio();

	}

	public int getClasseSezione(MaterialeAcciaio mat) {
		// TODO Auto-generated method stub
		return 0;
	}

	public double getIEff() {
		return iEff;
	}

	public double getNuInterM_V() {
		// TODO Auto-generated method stub
		return 0;
	}

	public double getSEff() {
		return sEff ;
	}

	public double getVbRd() {
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

	public String toString() {
		return name;
	}

	public Shape getShape() {
		GeneralPath pat = new GeneralPath();
		double bs=500;
		double ts=40;
		double tw=40;
		double bi=500;
		double ti=40;
		double hw=hgen-ts-ti;
		
		pat.moveTo(-bs/2, 0);
		pat.lineTo(-bs/2, ts);
		pat.lineTo(-tw/2, ts);
		pat.lineTo(-tw/2, ts+hw);
		pat.lineTo(-bi/2, ts+hw);
		pat.lineTo(-bi/2, ts+hw+ti);
		pat.lineTo(bi/2, ts+hw+ti);
		pat.lineTo(bi/2, ts+hw);
		pat.lineTo(tw/2, ts+hw);
		pat.lineTo(tw/2, ts);
		pat.lineTo(bs/2, ts);
		pat.lineTo(bs/2, 0);
		pat.closePath();
		return pat;	
	}

	public void quota(Graphics2D g2d, int ddquote) {
		// TODO Auto-generated method stub
		
	}

	public Shape getShapeEff() {
		return getShape();
	}

	public double getPassoIrrigidimentiTrasv() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Shape getShapeIrrigidita() {
		return getShape();
	}


	public boolean isSezioneChiusa() {
		return false;
	}


	public double getJw(boolean faseZero,double n, double spSoletta,double hsoletta) {
		return 0;
	}


	public double getBtot() {
		return 500;
	}


	public double getAreaInternaSemispessore(boolean faseZero, double hsoletta) {
		return 0;
	}


	public double[] getIst_Istmin_irrigTrasversale(Irrigidimento irTrasv, double passoIrrig, MaterialeAcciaio mat) {
		return new double []{0,0};
	}


	public Table getTabellaInput() throws BadElementException {
		Table table = new Table(2, 5);
		table.setBorder(Table.NO_BORDER);
		table.setPadding(2);
		table.setSpacing(2);
//		table.setDefaultHorizontalAlignment(Element.ALIGN_RIGHT);

//		nf.setMinimumFractionDigits(2);
		int nc=0;
		
		Cell c = new Cell("Area sezione: As (mmq)");
		c.setHorizontalAlignment(Element.ALIGN_LEFT);
		c.setBorder(Cell.NO_BORDER);
		table.addCell(c, nc, 0);
		
		c = new Cell(Double.toString(asgen));
		c.setHorizontalAlignment(Element.ALIGN_RIGHT);
		c.setBorder(Cell.NO_BORDER);
		table.addCell(c, nc, 1);
		nc++;

		c = new Cell("Momento di inerzia: Jy (mm4)");
		c.setBorder(Cell.NO_BORDER);
		c.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(c, nc, 0);

		c = new Cell(Double.toString(jgen));
		c.setBorder(Cell.NO_BORDER);
		c.setHorizontalAlignment(Element.ALIGN_RIGHT);
		table.addCell(c, nc, 1);
		nc++;

		
		c = new Cell("Altezza totale sezione: Htot (mm)");
		c.setBorder(Cell.NO_BORDER);
		c.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(c, nc, 0);
		

		c = new Cell(Double.toString(hgen));
		c.setBorder(Cell.NO_BORDER);
                c.setHorizontalAlignment(Element.ALIGN_RIGHT);
		table.addCell(c, nc, 1);
		nc++;

		c = new Cell("Quota baricentro (da estradosso sezione): yg (mm)");
		c.setBorder(Cell.NO_BORDER);
		c.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(c, nc, 0);

		c = new Cell(Double.toString(yggen));
		c.setBorder(Cell.NO_BORDER);
		c.setHorizontalAlignment(Element.ALIGN_RIGHT);
		table.addCell(c, nc, 1);
		nc++;

		
		c = new Cell("Area resistente a taglio: Aw (mmq)");
		c.setBorder(Cell.NO_BORDER);
		c.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(c, nc, 0);

		c = new Cell(Double.toString(awgen));
		c.setBorder(Cell.NO_BORDER);
		c.setHorizontalAlignment(Element.ALIGN_RIGHT);
		table.addCell(c, nc, 1);
		nc++;
		
		return table;
        }


	public Table getTabellaIrrigidimenti(double passoTrasv) throws BadElementException {
		return new Table(1, 1);
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


	public ParametriStatici getParametriEfficaci(MaterialeAcciaio mat, double passoIrrigidimentiTrasv) {
		return new ParametriStatici(0,0,0,0);
	}


	public Table getTabellaVerificaIrrigidimenti(double passoTrasv, MaterialeAcciaio mat,
			Irrigidimento irrTrasv) throws BadElementException {
	return new Table(1, 1);
	}

    public double getNu3() {
        return 0;
    }

    public double getVbwRd() {
        return 0;
    }

    public double getVbfRd() {
        return 0;
    }

    public double getVbsd() {
        return 0;
    }
    
  public String getStringParametriStatici(Sezione sez, String combo) {
   		String s;

		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		
		s = "SEZIONE " + sez.getName() + ": combinazione " + combo;
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
      //to DO
        
    }
      public double getTsup() {
        
        return 0;
        
    }

    public double getTinf() {

        return 0;
    }

    public double[] getSigma_W_IrrigTrasversali(Irrigidimento irt, double passo) {
        return new double []{0,0,0.0000001};
    }
}
