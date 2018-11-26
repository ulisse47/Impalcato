package cassone.model.soletta;

import cassone.util.MainRTFCreator;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Cell;
import com.lowagie.text.Table;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;

import cassone.model.Campate;
import cassone.model.ParametriStatici;

public class SolettaType2_2 extends SolettaType2 implements Soletta {

	
	public SolettaType2_2(String nome) {
		this.name= nome;
	}
	public SolettaType2_2() {
		super () ;
	}

	
	public double getBeffAnalisiGlobale(Campate campate, int campata,
			boolean calcolaBeff) {
		double b = super.getBeffAnalisiGlobale(campate,campata,calcolaBeff);
		return 2*b;
	}

	public double getBeffSezioniVerifica(Campate campate, int nCampata,
			double xSezione, boolean calcolaBeff) {
		double b = super.getBeffSezioniVerifica(campate,nCampata,xSezione,calcolaBeff);
		return 2*b;
	}

	public double getBtot() {
		return 2 * (bs + b + bd);
	}


	public double getHsoletta() {
		return Math.max(hd1 + hd2, hs1 + hs2);
	}

	public ParametriStatici getParametriStatici(boolean calcolaBeff,
			Campate campate, int nCampata) {

		Area a = new Area(super.getShape(calcolaBeff, campate, nCampata));
		ParametriStatici ps= getParametriAreaA(a);
		
		return new ParametriStatici(ps.getA() * 2, ps.getJy() * 2, ps.getYg(),
				0);
	}

	public ParametriStatici getParametriStatici(boolean calcolaBeff,
			Campate campate, int nCampata,double xCampata, double yn, boolean Mpos) {
		
//		ParametriStatici ps = super.getParametriStatici(calcolaBeff, campate,
//				nCampata,xCampata,yn,Mpos);

		
		Area a = new Area(super.getShape(calcolaBeff, campate, nCampata, xCampata, yn, Mpos));
		ParametriStatici ps= getParametriAreaA(a);
		
		return new ParametriStatici(ps.getA() * 2, ps.getJy() * 2, ps.getYg(),
				0);
		
	}

	public Shape getShape(boolean calcolaBeff, Campate campate, int nCampata) {

		
		Area gp = new Area(super.getShape(calcolaBeff, campate, nCampata));
	

		Area gpm = gp.createTransformedArea(AffineTransform.getScaleInstance(-1,
				1));

		gp.transform(AffineTransform.getTranslateInstance(-getdx(), 0));
		gpm.transform(AffineTransform.getTranslateInstance(getdx(), 0));
		gp.add(gpm);

		return gp;

	}

	public void quota(Graphics2D g, int dd, boolean calcoloBeff,
			Campate campate, int nCampate) {
		
		double dx = getdx();
		AffineTransform old = g.getTransform();
		AffineTransform at = new AffineTransform();

		at.setToTranslation(-dx, 0);
		g.transform(at);
		super.quota(g, dd, calcoloBeff, campate, nCampate);
		
/*		at.setToTranslation(2*dx, 0);
		g.transform(at);
		at.setToScale(-1, 1);
		g.transform(at);
		super.quota(g, dd, calcoloBeff, campate, nCampate);
*/		
		g.setTransform(old);
		
	}

        public void quota(Graphics2D g, int dd, boolean calcoloBeff, Campate campate, int nCampata, double xCampata) {
		
		double dx = getdx();
		AffineTransform old = g.getTransform();
		AffineTransform at = new AffineTransform();

		at.setToTranslation(-dx, 0);
		g.transform(at);
		super.quota(g,dd,calcoloBeff,campate,nCampata,xCampata);
		
/*		at.setToTranslation(2*dx, 0);
		g.transform(at);
		at.setToScale(-1, 1);
		g.transform(at);
		super.quota(g, dd, calcoloBeff, campate, nCampata);
*/		
		g.setTransform(old);
		
	}
	private double getdx(){
		return  b / 2 + bd;
	}
	

	public Shape getShape(boolean calcolaBeff, Campate campate, int nCampata,
			double xCampata, double yn, boolean momentoPositivo) {

		
		Area gp = new Area(super.getShape(calcolaBeff, campate, nCampata, xCampata, yn, momentoPositivo));
	

		Area gpm = gp.createTransformedArea(AffineTransform.getScaleInstance(-1,
				1));

		gp.transform(AffineTransform.getTranslateInstance(-getdx(), 0));
		gpm.transform(AffineTransform.getTranslateInstance(getdx(), 0));
		gp.add(gpm);

		return gp;
	}
	
            
	public Table getTabellaInput() throws BadElementException {
	
		Cell c;
		int nc=0;
		Table table = new Table(2, 8);
		table.setBorder(Table.NO_BORDER);
		table.setPadding(2);
		table.setSpacing(2);
		
		
		c = MainRTFCreator.getCellaTipo("Semi larghezza sezione: B(mm)", Cell.ALIGN_LEFT);
		table.addCell(c, nc, 0);
		c = MainRTFCreator.getCellaTipo(Double.toString(getBtot()), Cell.ALIGN_RIGHT);
		table.addCell(c, nc, 1);
		++nc;

		c = MainRTFCreator.getCellaTipo("Larghezza tratti orizzontali inferiori: b(mm)", Cell.ALIGN_LEFT);
		table.addCell(c, nc, 0);
		c = MainRTFCreator.getCellaTipo(Double.toString(b), Cell.ALIGN_RIGHT);
		table.addCell(c, nc, 1);
		++nc;

                c = MainRTFCreator.getCellaTipo("Larghezza ali esterne: bs(mm)", Cell.ALIGN_LEFT);
		table.addCell(c, nc, 0);
		c = MainRTFCreator.getCellaTipo(Double.toString(bs), Cell.ALIGN_RIGHT);
		table.addCell(c, nc, 1);
		++nc;

		c = MainRTFCreator.getCellaTipo("Quota bordi esterni inferiore : hs1(mm)", Cell.ALIGN_LEFT);
		table.addCell(c, nc, 0);
		c = MainRTFCreator.getCellaTipo(Double.toString(hs1), Cell.ALIGN_RIGHT);
		table.addCell(c, nc, 1);
		++nc;

		c = MainRTFCreator.getCellaTipo("Spessore soletta bordi estreni: hs2(mm)", Cell.ALIGN_LEFT);
		table.addCell(c, nc, 0);
		c = MainRTFCreator.getCellaTipo(Double.toString(hs2), Cell.ALIGN_RIGHT);
		table.addCell(c, nc, 1);
		++nc;

                c = MainRTFCreator.getCellaTipo("Larghezza ali centrali: bd(mm)", Cell.ALIGN_LEFT);
		table.addCell(c, nc, 0);
		c = MainRTFCreator.getCellaTipo(Double.toString(bd), Cell.ALIGN_RIGHT);
		table.addCell(c, nc, 1);
		++nc;

                c = MainRTFCreator.getCellaTipo("Quota bordo inferiore in mezzeria: hd1(mm)", Cell.ALIGN_LEFT);
		table.addCell(c, nc, 0);
		c = MainRTFCreator.getCellaTipo(Double.toString(hs1), Cell.ALIGN_RIGHT);
		table.addCell(c, nc, 1);
		++nc;

		c = MainRTFCreator.getCellaTipo("Spessore soletta inmezzeria: hd2(mm)", Cell.ALIGN_LEFT);
		table.addCell(c, nc, 0);
		c = MainRTFCreator.getCellaTipo(Double.toString(hs2), Cell.ALIGN_RIGHT);
		table.addCell(c, nc, 1);
		++nc;
		return table;
        }

}
