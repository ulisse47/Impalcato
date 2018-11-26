package cassone.model.soletta;

import java.awt.Graphics2D;
import java.awt.Shape;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Table;

import cassone.model.Campate;
import cassone.model.ParametriStatici;

public interface Soletta {
	

	//resistuisce A,yg,Jx della soletta
	public ParametriStatici getParametriStatici(boolean calcolaBeff,Campate campata,int ncampata);
	
	//resistuisce A,yg,Jx della soletta posta sopra l'asse neutro yn
	public ParametriStatici getParametriStatici(boolean calcolaBeff,Campate campata,int ncampata,
			double xCampata, double yn,boolean Mpos);
	
	//resistituisce l'altezza massima della soletta
	public double getHsoletta();
	
	public void setName(String name);
	
//	public Soletta getCopia();
	
	public double getBeffAnalisiGlobale(Campate campate, int campata,boolean calcolaBeff);

//	public double[] get_b1b2_AnalisiGlobale(Campate campate, int nCampata,boolean calcolaBeff);

	public double getBeffSezioniVerifica(Campate campate, int nCampata,double xSezione,boolean calcolaBeff);
	
	public String toString();

	public Shape getShape(boolean calcolaBeff,Campate campate, int nCampata);
	
	public Shape getShape(boolean calcolaBeff,Campate campate, int nCampata,
			double xCampata, double yn, boolean momentoPositivo);
	
	public void quota(Graphics2D g,int dd, boolean calcoloBeff,Campate campate, int nCampata);

	public void quota(Graphics2D g,int dd, boolean calcoloBeff,Campate campate, int nCampata,double xCampata);

	public double getBtot();
	
	//spessore per il calcolo della rigidezza torsionale
	public double getSpMedioCalcoloJw();
	
	//altezza per il calcolo della rigidezza torsionale
	public double getHcacloloJw();
	
	public Table getTabellaInput() throws BadElementException;
	
}
