package cassone.model.SezioniMetalliche;

import java.awt.Graphics2D;
import java.awt.Shape;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Table;

import cassone.model.GiuntoBullonato;
import cassone.model.MaterialeAcciaio;
import cassone.model.ParametriStatici;
import cassone.model.Sezione;
import cassone.model.SezioneOutputTensioniFase;
import cassone.model.irrigidimenti.Irrigidimento;

public interface SezioneMetallica {

	public ParametriStatici getParametriStatici();
	
	//parametrici statici della sezione fino a sotto piattabanda sup
	//metodo per il calcolo dei momenti statici Sn
	public ParametriStatici getParametriStaticiS1();

	//parametrici statici della sezione fino a piattabanda sup
	//metodo per il calcolo dei momenti statici Sn
	public ParametriStatici getParametriStaticiS2();
	
	//calcola l'altezza massima della sezione
	public double getHtot();

	//calcola la larghezza massima della sezione
	public double getBtot();
	
	//calcola la coordinata della piattabanda sup quota intradosso
	public double getYs1();

	//calcola la coordinata della piattabanda inf quota estradosso
	public double getYs2();
	
	//resitiuisce lo spessore dell'anima (somma delle anime)
	public double getTwTot();

	//resitiuisce lo spessore dell'anima 
	public double getTw();
        
	//resitiuisce lo spessore piattab sup
	public double getTsup();

        //resitiuisce lo spessore dell'anima 
	public double getTinf();

	public String getName();
	
	public void setName(String name);
	
	public void calcolaTensioniEfficaci(MaterialeAcciaio mat,SezioneOutputTensioniFase so,
			double passoIrrigidimentiTrasv,boolean rigidEndPost);
	
	ParametriStatici getParametriEfficaci(MaterialeAcciaio mat ,double	passoIrrigidimentiTrasv);
	
	public double getSEff();
	
	public double getIEff();

	public int getClasseSezione(MaterialeAcciaio mat);
	
        //resistenza taglio totale
	public double getVbRd();
        //resistenza taglio (anima)
        public double getVbwRd();
        //resistenza taglio (flangia)
	public double getVbfRd();

       //restituisce l'azione agente sull'anima più sollecitata
	public double getVbsd();
	
	public double getNsez();
	
	public double getMsez();

	public double getVsez();
	
        //resistenza interazione taglio momento
	public double getNuInterM_V();
        
        //Ved/vbrd
        public double getNu3();

	//GIUNTI BULLONATI
	public double getFSd (int nPannello);
	public double getFbRdCoprig(int nPannello);
	public double getFbRdPiatp(int nPannello);
	public double getFvRd(int nPannello);
	public double getFscrSLU(int nPannello);
	public double getFscrSLE(int nPannello);
        public void calcolaBulloni(SezioneOutputTensioniFase so, Sezione sez,MaterialeAcciaio mat, GiuntoBullonato g);

	public String toString();
	
	public Shape getShape();
	
	public Shape getShapeEff();

	public Shape getShapeIrrigidita();

	public void quota(Graphics2D g2d,int ddquote);
	
	public boolean isPiattabandaSupIrrigidita();

	public void setPiattabandaSupIrrigidita(boolean piattabandaSupIrrigidita) ;
	
	public boolean isSezioneChiusa();
	
	//momento inerzia torcente
	public double getJw(boolean faseZero,double n,double spSoletta, double hsoletta);

	//Area interna semispessore
	public double getAreaInternaSemispessore(boolean faseZero, double hsoletta);

	//irrigidimento trasversale: rigidezza minima
	public double [] getIst_Istmin_irrigTrasversale(Irrigidimento irTrasv, 
			double passoIrrig, MaterialeAcciaio mat);
	
	public Table getTabellaInput() throws BadElementException;

	public Table getTabellaIrrigidimenti(double passoTrasv) throws BadElementException;
	
	public Table getTabellaVerificaIrrigidimenti(double passoTrasv, MaterialeAcciaio mat,
			Irrigidimento irrTrasv) throws BadElementException;
	
        public String getStringParametriStatici(Sezione sez,String combo);
        
        public double[] getSigma_W_IrrigTrasversali(Irrigidimento irt,double passo);
 

}
