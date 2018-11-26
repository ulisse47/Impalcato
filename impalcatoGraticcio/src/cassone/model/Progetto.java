package cassone.model;

import cassone.util.dialog.DlgProgressBar;
import cassone.util.dialog.DlgVerifiche;
import cassone.view.Giunti.TableVerificaGiuntiInf;
import cassone.view.Giunti.TableVerificaGiuntiSup;
import cassone.view.Giunti.TableVerificaGiuntiW;
import cassone.view.Giunti.ViewGiuntoPiantaInferiore;
import cassone.view.Giunti.ViewGiuntoPiantaSup;
import cassone.view.VerificaTensioni.CampateViewTensioni;
import cassone.view.VerificaTensioni.InputVerifiche;
import cassone.view.analisi.CampateView;
import com.thoughtworks.xstream.XStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import cassone.model.SezioniMetalliche.SezioneMetallica;
import cassone.model.SezioniMetalliche.SezioneMetallicaCassone;
import cassone.model.SezioniMetalliche.SezioneMetallicaCassoneII;
import cassone.model.SezioniMetalliche.SezioneMetallicaDoppioT;
import cassone.model.SezioniMetalliche.SezioneMetallicaGenerica;
import cassone.model.irrigidimenti.Irrigidimento_L;
import cassone.model.soletta.Soletta;
import cassone.model.soletta.SolettaT;
import cassone.model.soletta.SolettaType2;
import cassone.model.soletta.SolettaType2_2;
import cassone.util.FileChooser;
import cassone.util.MainRTFCreator;
import cassone.util.Parser;
import cassone.view.Giunti.ViewGiuntoVistaLaterale;
import cassone.view.Instabilita.InputInstabilita;
import cassone.view.Instabilita.SezioneIrrigiditaView;
import cassone.view.Instabilita.TableTensioniEfficaci;
import cassone.view.VerificaTensioni.TableCombView;
import cassone.view.VerificaTensioni.TableCondView;
import cassone.view.VerificaTensioni.TableTensioniView;
import cassone.view.VerificaTensioni.TensioniVerificaView;
import cassone.view.analisi.InputView;
import cassone.view.analisi.TabGeo;
import cassone.view.analisi.TableOutputView;
import it.ccprogetti.impalcatoGraticcio.activation.core.StartUpExt;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

public class Progetto {
    
    private static Progetto prg;
    
    private transient File fileCorrente;
    
    private ArrayList<Sezione> sezioniAnalisiGlobale;
    
    private ArrayList<Sezione> sezioniVerifica;
    
    private ArrayList<SezioneMetallica> conci;
    
    private ArrayList<Soletta> solette;
    
    private ArrayList<CombinazioneCarichi> combinazioni;
    
    private Campate campate;
    
    private Sezione currentSezioneAnalisi;
    
    private Sezione currentSezioneVerifica;
    
    // coefficienti di omogeneizzazione
    double n[];
    
    String nomeProgetto;
    
    // materiale
    MaterialeAcciaio materiale;
    
    ArrayList<MaterialeAcciaio> materiali;
    
    boolean analizzato=false;
    
    int mode;

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }
    
    private transient File currentPath;
    
    public boolean isAnalizzato() {
        return analizzato;
    }
    
    public void setAnalizzato(boolean analizzato) {
        this.analizzato = analizzato;
    }
    
    public Progetto() {
    }
    
    public File getFileCorrente(){
        return fileCorrente;
    }
    
    private void init() {
        
        nomeProgetto = "Nuovo progetto";
        
        conci = new ArrayList<SezioneMetallica>();
        SezioneMetallicaDoppioT primoConcio = new SezioneMetallicaDoppioT();
        conci.add(primoConcio);
        
        solette = new ArrayList<Soletta>();
        SolettaT primaSoletta = new SolettaT(3500, 0, 300, 0, 0);
        solette.add(primaSoletta);
        
        sezioniAnalisiGlobale = new ArrayList<Sezione>();
        Sezione sezioneAnalisi = new Sezione(true, primoConcio, primaSoletta);
        sezioniAnalisiGlobale.add(sezioneAnalisi);
        
        sezioniVerifica = new ArrayList<Sezione>();
        Sezione sezioneVerifica = new Sezione(false, primoConcio, primaSoletta);
        sezioniVerifica.add(sezioneVerifica);
        
        setCurrentSezioneAnalisi(sezioneAnalisi);
        setCurrentSezioneVerifica(sezioneVerifica);
        
        campate = new Campate();
        
        CondizioniCarico condizioni = new CondizioniCarico("c1", 1);
        Sollecitazioni sol = new Sollecitazioni(condizioni);
        currentSezioneVerifica.addSollecitazione(sol);
        
        CombinazioneCarichi cmb = new CombinazioneCarichi("cmb1");
        combinazioni = new ArrayList<CombinazioneCarichi>();
        combinazioni.add(cmb);
        
        n = new double[4];
        n[0] = 20;
        n[1] = 6;
        n[2] = 14.3;
        n[3] = 16.0;
        
        materiale = new MaterialeAcciaio("S235");
        materiali = new ArrayList<MaterialeAcciaio>();
        materiali.add(materiale);
        materiali.add(new MaterialeAcciaio("S275"));
        materiali.add(new MaterialeAcciaio("S355"));
        
    }
    
    public static synchronized Progetto getInstance() {
        
        if (prg == null) {
            prg = new Progetto();
            prg.init();
        }
        
        return prg;
    }
    
    public static synchronized Progetto getNewInstance() {
        
        prg = new Progetto();
        prg.init();
        return prg;
        
    }
    
    /**
     *
     * @param concio
     * @throws Exception
     */
    public boolean deleteConcio(SezioneMetallica concio) throws Exception {
        
        if (!checkInput()) {
            return false;
        }
        
        if (conci.size() == 1) {
            JOptionPane.showMessageDialog(null,
                    "E' stato definito un solo concio!", "Elimina Concio",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        conci.remove(concio);
        
        if(currentSezioneAnalisi.getSezioneMetallica()==concio) setSezioneMetallica(conci.get(0),true);
        for (int j = 0; j < sezioniAnalisiGlobale.size(); ++j) {
            Sezione sez = sezioniAnalisiGlobale.get(j);
            if(sez.getSezioneMetallica()== concio) sez.setSezioneMetallica(conci.get(0));
        }
        
        if(currentSezioneVerifica.getSezioneMetallica()==concio) setSezioneMetallica(conci.get(0),false);
        for (int j = 0; j < sezioniVerifica.size(); ++j) {
            Sezione sez = sezioniVerifica.get(j);
            if(sez.getSezioneMetallica()== concio) sez.setSezioneMetallica(conci.get(0));
        }
        
        ridisegna();
        return true;
    }
    
    public boolean deleteSoletta(Soletta soletta) throws Exception {
        
        if (!checkInput()) {
            return false;
        }
        
        if (solette.size() == 1) {
            JOptionPane.showMessageDialog(null,
                    "E' stato definita una sola soletta, impossibile eliminare", "Elimina Soletta",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        solette.remove(soletta);
        if(currentSezioneAnalisi.getSoletta()==soletta) setSoletta(solette.get(0),true);
        for (int j = 0; j < sezioniAnalisiGlobale.size(); ++j) {
            Sezione sez = sezioniAnalisiGlobale.get(j);
            if(sez.getSoletta()== soletta) sez.setSoletta(solette.get(0));
        }
        
        if(currentSezioneVerifica.getSoletta()==soletta) setSoletta(solette.get(0),false);
        for (int j = 0; j < sezioniVerifica.size(); ++j) {
            Sezione sez = sezioniVerifica.get(j);
            if(sez.getSoletta()== soletta) sez.setSoletta(solette.get(0));
        }
        
        ridisegna();
        return true;
    }
    
    /**
     *
     * @param
     * @throws Exception
     */
    public boolean deleteSezione(Sezione sez,boolean sezAnalisi) throws Exception {
        
        if (!checkInput()) {
            return false;
        }
        
        
        if(sezAnalisi){
            if (sezioniAnalisiGlobale.size() == 1) {
                JOptionPane.showMessageDialog(null,"E' stata definita una sola sezione, impossibile eliminare",
                        "Elimina Sezione", JOptionPane.WARNING_MESSAGE);
                return false;
            }else
                sezioniAnalisiGlobale.remove(sez);
            setSezioneCorrente(sezioniAnalisiGlobale.get(0), true);
        } else{
            if (sezioniVerifica.size() == 1) {
                JOptionPane.showMessageDialog(null,"E' stata definita una sola sezione, impossibile eliminare",
                        "Elimina Sezione", JOptionPane.WARNING_MESSAGE);
                return false;
            } else
                sezioniVerifica.remove(sez);
            setSezioneCorrente(sezioniVerifica.get(0), false);
        }
        
        ridisegna();
        return true;
    }
    
    
    
    public void removeGiunto(Sezione sez) {
        sez.removeGiunto();
    }
    
    public void setSoletta(Soletta sm, boolean sezioneAnalisi) throws Exception {
        
//                if (!checkInput()) {
//			return ;
//		}
        
        int i = solette.indexOf(sm);
        if (i < 0)
            return;
        if (sezioneAnalisi) {
            currentSezioneAnalisi.setSoletta(sm);
            InputView.getInstance().aggiornaPannelli(currentSezioneAnalisi);
        } else
            currentSezioneVerifica.setSoletta(sm);
        ridisegna();
    }
    
    public void setSezioneMetallica(SezioneMetallica sm, boolean sezioneAnalisi)
    throws Exception {
        
        
        int i = conci.indexOf(sm);
        if (i < 0)
            return;
        if (sezioneAnalisi) {
            currentSezioneAnalisi.setSezioneMetallica(sm);
            InputView.getInstance().aggiornaPannelli(currentSezioneAnalisi);
        } else{
            currentSezioneVerifica.setSezioneMetallica(sm);
            InputInstabilita.getInstance().aggiornaPannelli(currentSezioneVerifica);
        }
        ridisegna();
    }
    
    public Soletta getCurrentSoletta(boolean analisiGlobale) {
        if (analisiGlobale)
            return getCurrentSezioneAnalisi().getSoletta();
        else
            return getCurrentSezioneVerifica().getSoletta();
        
    }
    
    public void aggiungiGiunto(Sezione sez) {
        if (sez.getSezioneMetallica().getClass() != SezioneMetallicaGenerica.class)
            sez.aggiungiGiunto();
    }
    
    public boolean removeIrrigidimento() {
        return false;
        
//		int ni = currentSezioneVerifica.getIrrigidimentiLong().size();
/*		if (ni == 0) {
                        JOptionPane.showMessageDialog(null,
                                        "Non � stato definito alcun irrigidimento longitudnale",
                                        "Elimina irrigidimento longitudinale",
                                        JOptionPane.WARNING_MESSAGE);
                        return false;
                }
 
                int returnVal = JOptionPane.showConfirmDialog(null,
                                "Vuoi eliminare un irrigidimento longitudinale?",
                                "Elimina irrigidimento longitudinale",
                                JOptionPane.YES_NO_OPTION);
 
                if (returnVal == JOptionPane.YES_OPTION) {
                        currentSezioneVerifica.getIrrigidimentiLong().remove(ni - 1);
                }
 
                // Parser.deleteSezione(i);
 
                return true;
 */
    }
    
    public void aggiungiCampata() {
        int nl = campate.getNCampate();
        campate.setNCampate(nl + 1);
        campate.setLuce(nl, 30);
        
    }
    
    public void rimuoviCampata() {
        
        int nl = campate.getNCampate();
        if (nl < 2) {
            JOptionPane
                    .showMessageDialog(
                    null,
                    "Il numero di campate deve essere maggiore di uno, impossibile eliminare",
                    "Elimina Campata", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (nl > 1)
            campate.setNCampate(nl - 1);
    }
    
    
    /**
     *
     * @param fileName
     * @throws IOException
     */
    public void openRTFDocument(String fileName) throws IOException {
        Process echoProcess = null;
        try {
            echoProcess = Runtime.getRuntime().exec(
                    "cmd /C  \"" + fileName + "\""); // crea un Thread di
            // tipo Process
        } catch (IOException ioEx) {
            throw new IOException("Could not retrieve env variables "
                    + ioEx.toString());
        }
    }
    
    
    
    
    /**
     *
     */
    public boolean elabora() throws Exception {
        
        
        // controlla i dati di input: valori numerici?
        if (!checkInput()) {
            return false;
        }
        
        // controlla i dati input: valori consistenti?
        if (!checkAnalisi()) {
            return false;
        }
        
        DlgProgressBar dlg = new DlgProgressBar(true);
        dlg.setVisible(true);
        
        ridisegna();
        
        if(analizzato)   return true;
        else return false;
        
    }
    
    
    
    public boolean isMomentoPositivo(Sollecitazioni[] sol, Sezione sez){
        double m=0,n=0;
        for (int k = 0; k < 5; k++) {
            m+=sol[k].getM()*1000000;
            n+=sol[k].getN()*1000;
        }
        SezioneOutputTensioniFase so = new SezioneOutputTensioniFase();
        so.calcolaParametriStatici(false,prg.getN()[0],prg.getCampate(),sez);
        double j = so.getJy();
        double  yg = so.getYg();
        double a = so.getA();
        double sc = m*(yg+sez.getSoletta().getHsoletta())/j + n/a;
        
        return (sc>0);
        
    }
    
    // resistituisce una classe sollecitazioni con i valori di azione M.N,V,scls
    // relativi ad una fase specifica
    public static Sollecitazioni getSollecitazioniFase(int fase, Sezione sez,
            CombinazioneCarichi cmb) {
        Sollecitazioni sol = new Sollecitazioni();
        int ncond = sez.getSollecitazioni().size();
        
        double M = 0;
        double Mt = 0;
        double V = 0;
        double N = 0;
        double sigc = 0;
        
        for (int i = 0; i < ncond; ++i) {
            Sollecitazioni s = sez.getSollecitazioni().get(i);
            int f = s.getCond().getN();
            if (f == fase){
                M += s.getM() * cmb.getC1(i);
                V += s.getV() * cmb.getC1(i);
                N += s.getN() * cmb.getC1(i);
                sigc += s.getSigCls() * cmb.getC1(i);
                Mt += s.getMt() * cmb.getC1(i);
                
            }
        }
        
        sol.setM(M);
        sol.setV(V);
        sol.setN(N);
        sol.setSigCls(sigc);
        sol.setMt(Mt);
        
        return sol;
    }
    
    public void disegna() {
        if (!checkInput()) {
            return;
        }
    }
    
    /**
     *
     */
    public boolean addSezioneMetallica(Class sezioneTpo,
            boolean isSezioneAnalisi) throws Exception {
        
        if (!checkInput()) {
            return false;
        }
        
        SezioneMetallica newConcio = null;
        
        // String nome = "C"+Integer.toString(conci.size()+1);
        if (sezioneTpo == SezioneMetallicaDoppioT.class) {
            // newConcio = new SezioneMetallicaDoppioT(
            // (SezioneMetallicaDoppioT) getCurrentSezioneMetallica(true),nome);
            newConcio = new SezioneMetallicaDoppioT();
        } else if (sezioneTpo == SezioneMetallicaGenerica.class) {
            newConcio = new SezioneMetallicaGenerica();
        } else if (sezioneTpo == SezioneMetallicaCassone.class) {
            newConcio = new SezioneMetallicaCassone();
        }else if (sezioneTpo == SezioneMetallicaCassoneII.class) {
            newConcio = new SezioneMetallicaCassoneII();
        }
        
        
        conci.add(newConcio);
        int ncon = conci.size();
        newConcio.setName("C"+ncon);
        setSezioneMetallica(newConcio, isSezioneAnalisi);
        
        // ridisegna();
        return true;
    }
    
    public boolean addSoletta(Class sezioneTpo, boolean sezioneAnalisi)
    throws Exception {
        
        if (!checkInput()) {
            return false;
        }
        
        Soletta sl = null;
        
        if (sezioneTpo == SolettaT.class) {
            // newConcio = new SezioneMetallicaDoppioT(
            // (SezioneMetallicaDoppioT) getCurrentSezioneMetallica(true),nome);
            sl = new SolettaT();
        } else if (sezioneTpo == SolettaType2.class) {
            sl = new SolettaType2();
        } else if (sezioneTpo == SolettaType2_2.class) {
            sl = new SolettaType2_2();
        }
        
        solette.add(sl);
        int nsol = solette.size();
        sl.setName("S"+nsol);
        prg.setSoletta(sl, sezioneAnalisi);
        
        ridisegna();
        return true;
    }
    
    /**
     *
     */
    public boolean addSezioneVerifica() throws Exception {
        if (!checkInput()) {
            return false;
        }
        
        String nome = "SV" + Integer.toString(sezioniVerifica.size() + 1);
        Sezione sez = getCurrentSezioneVerifica();
        Sezione newSez = new Sezione(sez, nome);
        sezioniVerifica.add(newSez);
        setSezioneCorrente(newSez, false);
        return true;
    }
    
    /**
     * Parsa tutti i valori della maschera cercando di salvarli nel modello, in
     * caso di errore mostra un pannello.
     *
     * @return
     */
    public boolean checkInput() {
        try {
            Parser.validate();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Valori di input non validi!",
                    "Warning!:" + ex.getMessage(), JOptionPane.WARNING_MESSAGE);
            return false;
        }
    }
    
    public boolean saveAsToFile() {
        
        // se comunque non esiste il file
        if (fileCorrente == null || !fileCorrente.exists()) {
            return saveToFile();
        }
        // se esiste gi provo a salvare prima
        else {
            if (saveToFile()) {
                currentPath = fileCorrente;
                fileCorrente = null;
                return saveToFile();
            } else {
                // se non riesco a salvare
                return false;
            }
        }
        
    }
    
    public boolean close() {
        
        int option;
        
        if (Progetto.getInstance().getMode() == StartUpExt.DEMO) {
          return true;
        } 

        
        option = JOptionPane.showConfirmDialog(null,
                "salvare prima di chiudere?", "Message", 1);
        if (option == JOptionPane.YES_OPTION) {
            Progetto prg = Progetto.getInstance();
            return prg.saveToFile();
        } else if (option == JOptionPane.NO_OPTION) {
            System.exit(0);
            return true;
        }
        return false;
    }
    
    
    /**
     *
     */
    public boolean saveToFile() {
        String fileName = null;
        
        // caso file nuovo
        if (fileCorrente == null || !fileCorrente.exists()) {
            // scegli file
            FileChooser chooser = new FileChooser(JFileChooser.SAVE_DIALOG,
                    "cass", "CASS");
            
            chooser.setSelectedFile(currentPath);
            
            int returnVal;
            returnVal = chooser.showSaveDialog(null);
            if (returnVal == FileChooser.APPROVE_OPTION) {
                fileName = chooser.getAbsoluteFileName();
                fileCorrente = new File(fileName);
            }
            if (returnVal == FileChooser.CANCEL_OPTION) {
                fileCorrente=currentPath;
                return false;
            }
        } else {
            fileName = fileCorrente.getAbsolutePath();
        }
        
        try {
            if (!checkInput()) {
                return false;
            }
            saveToFile(fileName);
            return true;
        } catch (Exception io) {
            JOptionPane.showMessageDialog(null,
                    "Non è stato possibile salvare il file " + fileName,
                    "Warning!", JOptionPane.WARNING_MESSAGE);
            io.printStackTrace();
            return false;
        }
        
    }
    
    /**
     *
     */
    public boolean openFile() {
        
        /* START - ANDREA MODIFICHE 15-03-2007 */
        
        String fileName = null;
        // scegli file
        FileChooser chooser = new FileChooser(JFileChooser.OPEN_DIALOG, "cass",
                "CASS");
        chooser.setTipo(FileChooser.OPEN);
        try {
            chooser.setSelectedFile(prg.fileCorrente);
        } catch (Exception e) {
            
        }
        
        int returnVal = chooser.showOpenDialog(null);
        if (returnVal == FileChooser.APPROVE_OPTION) {
            fileName = chooser.getAbsoluteFileName();
            try {
                
                /*
                XMLDecoder e = new XMLDecoder(new BufferedInputStream(
                        new FileInputStream(fileName)));
                prg = (Progetto) e.readObject();
                e.close();
                 */
                
                BufferedReader in = new BufferedReader(new FileReader( fileName ));
                StringBuffer xml = new StringBuffer();
                String text = null;
                while (( text = in.readLine()) != null) {
                    xml.append( text );
                }
                in.close();
                
                XStream xstream = new XStream();
                 prg = ( Progetto )xstream.fromXML( xml.toString() );
                
                
                prg.fileCorrente = new File(fileName);
                InputView.getInstance().aggiornaPannelli(prg.getCurrentSezioneAnalisi());
                InputInstabilita.getInstance().aggiornaPannelli(prg.getCurrentSezioneVerifica());
                prg.resetAnalisi();
                prg.setAnalizzato(false);
                ridisegna();
                return true;
                
            } catch (Exception io) {
                io.printStackTrace();
                JOptionPane.showMessageDialog(null, io.getMessage(),
                        "Errore nell'apertura del file " + fileName,
                        JOptionPane.ERROR_MESSAGE);
            }
            
        }
        return false;
        
        /* END */
        
                /*
                 * Properties prop = null; String fileName = null; //scegli file
                 * FileChooser chooser = new FileChooser(JFileChooser.OPEN_DIALOG,
                 * "cass", "CASS"); chooser.setTipo(FileChooser.OPEN); int returnVal =
                 * chooser.showOpenDialog(null); if (returnVal ==
                 * FileChooser.APPROVE_OPTION) { fileName =
                 * chooser.getAbsoluteFileName(); try { prop = openFile(fileName);
                 * fileCorrente = new File(fileName); } catch (Exception io) {
                 * JOptionPane.showMessageDialog(null, io.getMessage(), "Errore
                 * nell'apertura del file " + fileName, JOptionPane.ERROR_MESSAGE); }
                 * try { Parser.loadFile(prop);
                 *
                 *
                 *
                 * //Parser.updateInputView(); //Parser.loadValues(prop); } catch
                 * (Exception ex) { ex.printStackTrace(); return false; }
                 *
                 * return true; checkInput(); } return false;
                 */
        
    }
    
    public boolean openFile( String fileName ) {
        
                
        // scegli file
        try {
                
                /*
                XMLDecoder e = new XMLDecoder(new BufferedInputStream(
                        new FileInputStream(fileName)));
                prg = (Progetto) e.readObject();
                e.close();
                 */
                
                BufferedReader in = new BufferedReader(new FileReader( fileName ));
                StringBuffer xml = new StringBuffer();
                String text = null;
                while (( text = in.readLine()) != null) {
                    xml.append( text );
                }
                in.close();
                
                XStream xstream = new XStream();
                prg = ( Progetto )xstream.fromXML( xml.toString() );
                
                
                prg.fileCorrente = new File(fileName);
                InputView.getInstance().aggiornaPannelli(prg.getCurrentSezioneAnalisi());
                InputInstabilita.getInstance().aggiornaPannelli(prg.getCurrentSezioneVerifica());
                prg.resetAnalisi();
                prg.setAnalizzato(false);
                ridisegna();
                return true;
                
            } catch (Exception io) {
                io.printStackTrace();
                JOptionPane.showMessageDialog(null, io.getMessage(),
                "Errore nell'apertura del file " + fileName,
                JOptionPane.ERROR_MESSAGE);
            }            
        return false;
        }
        
    
    
    
    
    /**
     *
     *
     */
    public boolean save() {
        return checkInput();
    }
    
    /**
     *
     * @param fileName
     * @throws java.lang.Exception
     */
    private void saveToFile(String fileName) throws Exception {
                /*
                 * FileOutputStream fout = new FileOutputStream(fileName);
                 * BufferedOutputStream bout = new BufferedOutputStream(fout);
                 * Properties prop; prop = Parser.getInput(); prop.store(bout,
                 * fileName); bout.close(); fout.close();
                 */
        
        /*XMLEncoder e = new XMLEncoder(new BufferedOutputStream(
                new FileOutputStream(fileName)));
        e.writeObject(getInstance());
        e.close();*/
        
        XStream xstream = new XStream();
        
        xstream.omitField(Sezione.class, "sezioniOutput");
        xstream.omitField(Sezione.class, "sezioniMetEfficaci");
 //       xstream.omitField(Progetto.class, "sezioniOutput");
                
  /*      xstream.omitField(SezioneMetallicaDoppioT.class, "shapeEff");
        xstream.omitField(SezioneMetallicaCassone.class, "shapeEff");
        xstream.omitField(SezioneMetallicaCassoneII.class, "shapeEff");
        xstream.omitField(SezioneMetallicaGenerica.class, "shapeEff");
*/
        String xml =  xstream.toXML(getInstance());
        
        BufferedWriter out = new BufferedWriter(new FileWriter( fileName ));
        out.write( xml );
        out.close();
        
    }
    
    /**
     *
     */
/*	public void writeRFT_tensioni() {
 
                // salva
                if (!saveToFile()) {
                        return;
                }
 
                String fileName = null;
                // scegli file
                FileChooser chooser = new FileChooser(JFileChooser.SAVE_DIALOG, "rtf",
                                "");
                // chooser.setDialogType( JFileChooser.SAVE_DIALOG );
                chooser.setTipo(FileChooser.SAVE_AS);
                chooser.setCurrentDirectory(fileCorrente);
                int returnVal = chooser.showDialog(null, "Seleziona");
                if (returnVal == FileChooser.APPROVE_OPTION) {
                        fileName = chooser.getAbsoluteFileName();
 
                        try {
                                // Document doc = MainRTFCreator.creaRTFDocument(fileName);
                                openRTFDocument(fileName );
                        } catch (Exception io) {
                                io.printStackTrace();
                        }
                }
 
        }
 */
    /**
     *
     */
    public void writeRFT() {
        
        // salva
        if (!saveToFile()) {
            return;
        }
        
        String fileName = null;
        // scegli file
        FileChooser chooser = new FileChooser(JFileChooser.SAVE_DIALOG, "rtf",
                "");
        // chooser.setDialogType( JFileChooser.SAVE_DIALOG );
        chooser.setTipo(FileChooser.SAVE_AS);
        chooser.setCurrentDirectory(fileCorrente);
        int returnVal = chooser.showDialog(null, "Seleziona");
        if (returnVal == FileChooser.APPROVE_OPTION) {
            fileName = chooser.getAbsoluteFileName();
            
            try {
                MainRTFCreator mc = new MainRTFCreator();
                mc.creaRTFDocument(fileName);
                openRTFDocument(fileName /* "c:/rtf.rtf" */);
            } catch (Exception io) {
                io.printStackTrace();
            }
        }
        
    }
    
    /**
     *
     * @return
     */
    public boolean addCondizione() {
        if (!checkInput()) {
            return false;
        }
        int nc = getCurrentSezioneVerifica().getSollecitazioni().size();
        
        CondizioniCarico cond = new CondizioniCarico("carico" + nc, 0);
        for (int i = 0; i < sezioniVerifica.size(); ++i) {
            Sollecitazioni sol = new Sollecitazioni(cond);
            sezioniVerifica.get(i).addSollecitazione(sol);
        }
        
        return true;
    }
    
    public ArrayList<CondizioniCarico> getCondizioni() {
        
        ArrayList<Sollecitazioni> sol = currentSezioneVerifica.getSollecitazioni();
        ArrayList<CondizioniCarico> cond = new ArrayList<CondizioniCarico>();
        
        for (int i = 0; i < sol.size(); i++) {
            cond.add(sol.get(i).getCond());
        }
        
        return cond;
    }
    
    
    /**
     *
     * @return
     */
    public boolean inportaCondizioni() {
        
                /*
                 * FileParser parser = null; String fileName = null; //scegli file
                 *
                 * FileChooser chooser = new FileChooser(JFileChooser.OPEN_DIALOG,
                 * "cond", "cond"); chooser.setTipo(FileChooser.OPEN); int returnVal =
                 * chooser.showOpenDialog(null); if (returnVal ==
                 * FileChooser.APPROVE_OPTION) { fileName =
                 * chooser.getAbsoluteFileName(); try { parser = new FileParser(
                 * fileName ); Iterator sezioni = parser.getSezioni(); Iterator
                 * condizioni; Set condSet; Condizione cond; int sez = 0; int cnd = 0;
                 *
                 * //elimina tutte le condizioni e sezioni if ( sezioni.hasNext() ){
                 * Common.ncond = 0;//numero di condizioni Common.sezioni = 0;//numero
                 * sezioni Common.current_sezione = 0; Common.ncond =
                 * parser.getNumeroCondizioni(); }
                 *
                 * //per ogni sezione while ( sezioni.hasNext() ){
                 *
                 * condSet = ( Set )sezioni.next(); condizioni = condSet.iterator(); cnd =
                 * 0; //per ogni condizione della sezione while ( condizioni.hasNext() ){
                 * cond = ( Condizione )condizioni.next(); Parser.addCondizione(
                 * sez,cnd, cond.id, cond.descrizione ,
                 * cond.N,cond.V,cond.M,cond.Nsoletta); cnd ++; } sez ++; Common.sezioni =
                 * sez; } } catch (Exception io) { JOptionPane.showMessageDialog(null,
                 * io.getMessage(), "Errore nel parsing del file " + fileName,
                 * JOptionPane.ERROR_MESSAGE); }
                 *
                 * return true; }
                 */
        return false;
    }
    
    /**
     *
     * @return
     */
    public boolean addCombinazione() {
        if (!checkInput()) {
            return false;
        }
        int n = combinazioni.size() + 1;
        CombinazioneCarichi cmb = new CombinazioneCarichi("cmb" + n);
        
        combinazioni.add(cmb);
        // Parser.setNewCombinazione();
        return true;
    }
    
    /**
     *
     * @return
     * @throws Exception
     */
    public boolean deleteCondizione(CondizioniCarico cond) throws Exception {
        
        if(!checkInput()) return false;
        
        int ns = getCurrentSezioneVerifica().getSollecitazioni().size();
        
        if (ns == 1) {
            JOptionPane
                    .showMessageDialog(
                    null,
                    "E' stata definita una sola condizione di carico, impossibile eliminare",
                    "Elimina condizione di carico",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        for (int i = 0; i < sezioniVerifica.size(); ++i) {
            ArrayList<Sollecitazioni> sol = sezioniVerifica.get(i).getSollecitazioni();
            for (int j = 0; j < sol.size(); ++j) {
                Sollecitazioni s =sol.get(j);
                if(s.getCond()==cond) sol.remove(s);
            }
            
            
        }
        ridisegna();
        
        return false;
    }
    
    /**
     *
     * @return
     * @throws Exception
     */
    public boolean deleteCombinazione(CombinazioneCarichi cmb) throws Exception {
        
        if(!checkInput()) return false;
        
        int ns = getCombinazioni().size();
        
        if (ns == 1) {
            JOptionPane
                    .showMessageDialog(
                    null,
                    "E' stata definita una sola combinazione di carico, impossibile eliminare",
                    "Elimina combinazione di carico",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        combinazioni.remove(cmb);
        ridisegna();
        return false;
    }
    
    public void verifica(){
        
        if(!analizzato){
            int r = JOptionPane.showConfirmDialog(null,"Analisi non ancora effettuata." +
                    " Effettuare ora?","ATTENZIONE",JOptionPane.OK_CANCEL_OPTION);
            
            if(r==JOptionPane.OK_OPTION){
                
                DlgProgressBar dlg = new DlgProgressBar(true);
                dlg.setVisible(true);
                InputVerifiche inputPanel = InputVerifiche.getInstance();
                
            } else return;
        }
        
        DlgVerifiche dlgv = new DlgVerifiche(false);
        dlgv.setAlwaysOnTop(true);
        dlgv.setVisible(true);
        
        
        
    }
    
    public ArrayList<SezioneMetallica> getConci() {
        return conci;
    }
    
    public SezioneMetallica[] getSezioniMetalliche() {
        int nc = conci.size();
        
        SezioneMetallica s[] = new SezioneMetallica[nc];
        for (int i = 0; i < nc; i++) {
            s[i]=conci.get(i);
        }
        return s;
    }
    
    public Sezione[] getArraySezioneAnalisi() {
        int nc = sezioniAnalisiGlobale.size();
        
        Sezione s[] = new Sezione[nc];
        for (int i = 0; i < nc; i++) {
            s[i]=sezioniAnalisiGlobale.get(i);
        }
        return s;
    }
    
    public CondizioniCarico[] getArrayCondizioniCarico() {
        int nc = getCurrentSezioneVerifica().getSollecitazioni().size();
        
        CondizioniCarico s[] = new CondizioniCarico[nc];
        for (int i = 0; i < nc; i++) {
            s[i]=getCurrentSezioneVerifica().getSollecitazioni().get(i).getCond();
        }
        return s;
    }
    
    public Sezione[] getArraySezioneVerifica() {
        int nc = sezioniVerifica.size();
        
        Sezione s[] = new Sezione[nc];
        for (int i = 0; i < nc; i++) {
            s[i]=sezioniVerifica.get(i);
        }
        return s;
    }
    public void setSezioniMetalliche(ArrayList<SezioneMetallica> conci) {
        this.conci = conci;
    }
    
    public boolean verificaCoordinataX(int ncampata, double xs) {
        double lc = getCampate().luci[ncampata];
        if (xs < 0 || xs > lc)
            return false;
        
        return true;
    }
    
    public Sezione getCurrentSezioneVerifica() {
        if(currentSezioneVerifica==null)
            currentSezioneVerifica=sezioniVerifica.get(0);
        
        return currentSezioneVerifica;
        
    }
    
        /*
         * public void setCurrentSezioneAnalisi(Sezione currentSezioneAnalisi) {
         * this.currentSezioneAnalisi = currentSezioneAnalisi;
         * Parser.updateInputView(); }
         */
        /*
         * public void setCurrentSezioneVerifica(Sezione currentSezioneVerifica) {
         * this.currentSezioneVerifica = currentSezioneVerifica; }
         */
    public ArrayList<Sezione> getSezioniAnalisiGlobale() {
        return sezioniAnalisiGlobale;
    }
    
    public void setSezioniAnalisiGlobale(
            ArrayList<Sezione> sezioniAnalisiGlobale) {
        this.sezioniAnalisiGlobale = sezioniAnalisiGlobale;
    }
    
    public ArrayList<Sezione> getSezioniVerifica() {
        return sezioniVerifica;
    }
    
    public void setSezioniVerifica(ArrayList<Sezione> sezioniVerifica) {
        this.sezioniVerifica = sezioniVerifica;
    }
    public boolean isLastSezioniAnalisi() {
        int i = sezioniAnalisiGlobale.indexOf(currentSezioneAnalisi);
        if (i == sezioniAnalisiGlobale.size() - 1)
            return true;
        else
            return false;
    }
    
    public boolean isFirstSezioniAnalisi() {
        int i = sezioniAnalisiGlobale.indexOf(currentSezioneAnalisi);
        if (i == 0)
            return true;
        else
            return false;
    }
    
    public void setNextSezioniAnalisi() {
        int i = sezioniAnalisiGlobale.indexOf(currentSezioneAnalisi);
        int ns = sezioniAnalisiGlobale.size();
        
        if (ns == 1)
            return;
        if (i < ns - 1)
            ++i;
        else if (i == ns - 1)
            i = 0;
        try {
            setSezioneCorrente(sezioniAnalisiGlobale.get(i), true);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void setPreviousSezioniVerifica() {
        int i = sezioniVerifica.indexOf(currentSezioneVerifica);
        int ns = sezioniVerifica.size();
        
        if (ns == 1)
            return;
        if (i > 0)
            --i;
        else if (i == 0)
            i = ns - 1;
        try {
            setSezioneCorrente(sezioniVerifica.get(i), false);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public boolean isLastSezioniVerifica() {
        int i = sezioniVerifica.indexOf(currentSezioneVerifica);
        if (i == sezioniVerifica.size() - 1)
            return true;
        else
            return false;
    }
    
    public boolean isFirstSezioniVerifica() {
        int i = sezioniVerifica.indexOf(currentSezioneVerifica);
        if (i == 0)
            return true;
        else
            return false;
    }
    
    public void setNextSezioniVerifica() {
        int i = sezioniVerifica.indexOf(currentSezioneVerifica);
        int ns = sezioniVerifica.size();
        
        if (ns == 1)
            return;
        if (i < ns - 1)
            ++i;
        else if (i == ns - 1)
            i = 0;
        try {
            setSezioneCorrente(sezioniVerifica.get(i), false);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void setPreviousSezioniAnalisi() {
        int i = sezioniAnalisiGlobale.indexOf(currentSezioneAnalisi);
        int ns = sezioniAnalisiGlobale.size();
        
        if (ns == 1)
            return;
        if (i > 0)
            --i;
        else if (i == 0)
            i = ns - 1;
        try {
            setSezioneCorrente(sezioniAnalisiGlobale.get(i), true);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public String getNomeProgetto() {
        return nomeProgetto;
    }
    
    public void setNomeProgetto(String nomeProgetto) {
        this.nomeProgetto = nomeProgetto;
    }
    
    public double[] getN() {
        return n;
    }
    
    public void setN(double[] n) {
        this.n = n;
    }
    
    public Sezione getCurrentSezioneAnalisi() {
        
        if(currentSezioneAnalisi==null)
            currentSezioneAnalisi= sezioniAnalisiGlobale.get(0);
        return currentSezioneAnalisi;
    }
    
    public SezioneMetallica getCurrentSezioneMetallica(boolean analisi) {
        SezioneMetallica sez;
        if (analisi) {
            sez = getCurrentSezioneAnalisi().getSezioneMetallica();
        } else {
            sez = getCurrentSezioneVerifica().getSezioneMetallica();
        }
        return sez;
    }
    
    public boolean addSezioneAnalisi() throws Exception {
        if (!checkInput()) {
            return false;
        }
        
        String nome = "SA" + Integer.toString(sezioniAnalisiGlobale.size() + 1);
        Sezione sez = new Sezione(getCurrentSezioneAnalisi(), nome);
        sezioniAnalisiGlobale.add(sez);
        setSezioneCorrente(sez, true);
        return true;
    }
    
    public void setSezioneCorrente(Sezione sez, boolean isSezioneAnalisi)
    throws Exception {
        
        if (!checkInput()) {
            return ;
        }
        
        if (isSezioneAnalisi){
            currentSezioneAnalisi = sez;
        } else{
            currentSezioneVerifica=sez;
        }
        InputView.getInstance().aggiornaPannelli(currentSezioneAnalisi);
        InputInstabilita.getInstance().aggiornaPannelli(currentSezioneVerifica);
        ridisegna();
    }
    
    public Campate getCampate() {
        return campate;
    }
    
    public void setCampate(Campate campate) {
        this.campate = campate;
    }
    
        /*
         * public Concio getConcio(int nc){
         *
         * return conci.get(nc); }
         */
    public ArrayList<CombinazioneCarichi> getCombinazioni() {
        return combinazioni;
    }
    
    public void setCombinazioni(ArrayList<CombinazioneCarichi> combinazioni) {
        this.combinazioni = combinazioni;
    }
    
    public MaterialeAcciaio getMateriale() {
        return materiale;
    }
    
    public void setMateriale(MaterialeAcciaio materiale) {
        this.materiale = materiale;
    }
    
    public void addIrrigidimento() throws Exception {
        
        SezioneMetallicaDoppioT sm = (SezioneMetallicaDoppioT)currentSezioneVerifica.getSezioneMetallica();
        if (sm.getIrsAnima().size() > 1) {
            JOptionPane.showMessageDialog(null,
                    "Numero massimo irrigidimenti uguale a 2",
                    "Impossibile aggiungere irrigidimento",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Irrigidimento_L irr = new Irrigidimento_L();
        sm.getIrsAnima().add(irr);
        
        ridisegna();
    }
    
    public ArrayList<MaterialeAcciaio> getMateriali() {
        return materiali;
    }
    
    public void setMateriali(ArrayList<MaterialeAcciaio> materiali) {
        this.materiali = materiali;
    }
    
    // fa un chek sui dati salvati per verificare la possibilit di effettuare
    // l'analisi
    public boolean checkAnalisi() {
        
        // coefficienti di omogeneizzazione >0?
        double nmin = Math.min(Math.min(n[0], n[1]), Math.min(n[2], n[3]));
        if (nmin <= 0) {
            JOptionPane
                    .showMessageDialog(
                    null,
                    "I coefficineti di omogeneizzazione n non possono essere minori di 0",
                    "ATTENZIONE", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        return true;
    }
    
    public ArrayList<Soletta> getSolette() {
        return solette;
    }
    
    public void setSolette(ArrayList<Soletta> solette) {
        this.solette = solette;
    }
    
    // restituisce un array tipo SezioneMetallica[] con i conci correnti
    public SezioneMetallica[] getArraySezioniMetalliche() {
        SezioneMetallica[] sm = new SezioneMetallica[conci.size()];
        for (int i = 0; i < conci.size(); i++) {
            sm[i] = conci.get(i);
        }
        return sm;
    }
    
    // restituisce un array tipo Solette[] con le solette
    public Soletta[] getArraySolette() {
        Soletta[] sm = new Soletta[solette.size()];
        for (int i = 0; i < solette.size(); i++) {
            sm[i] = solette.get(i);
        }
        return sm;
    }
    
    // restituisce un array tipo Solette[] con le solette
    public CombinazioneCarichi[] getArrayCombinazioni() {
        CombinazioneCarichi[] sm = new CombinazioneCarichi[combinazioni.size()];
        for (int i = 0; i < combinazioni.size(); i++) {
            sm[i] = combinazioni.get(i);
        }
        return sm;
    }
    
        /*
         * public void setConcioCorrente(SezioneMetallica sez, boolean
         * sezioneAnalisi) {
         *
         * int i = conci.indexOf(sez); if(i>=0 && sezioneAnalisi){
         * currentSezioneAnalisi.setSezioneMetallica(sez); } else if(i>=0 &&
         * !sezioneAnalisi){ currentSezioneVerifica.setSezioneMetallica(sez); } else
         * return; }
         *
         */
    
  /*      public void aggiornaPannelli(){
                InputView.getInstance().aggiornaPannelli(getCurrentSezioneAnalisi());
                InputInstabilita.getInstance().aggiornaPannelli(getCurrentSezioneVerifica());
        }
   */
public void ridisegna() throws Exception {
        
        
        Parser.updateInputView();
        
        TabGeo.getInstance().repaint();
        TableOutputView.refresh();
        
        TableCondView.refresh();
        TableCombView.refresh();
        TableTensioniView.refresh();
        TableTensioniEfficaci.refresh();
        TableVerificaGiuntiInf.refresh();
        TableVerificaGiuntiSup.refresh();
        TableVerificaGiuntiW.refresh();
        
        
        ViewGiuntoVistaLaterale.getInstance().repaint();
        ViewGiuntoPiantaInferiore.getInstance().repaint();
        ViewGiuntoPiantaSup.getInstance().repaint();
        
        SezioneIrrigiditaView.getInstance().repaint();
        TensioniVerificaView.getInstance().repaint();
        CampateViewTensioni.getInstance().repaint();
        CampateView.getInstance().repaint();
//		TensioniInstabilitaView.getInstance().repaint();
        
    }
    
    // resistituisce le calsse valide per sezionemetallica
    public Object[] getAvailableClassSezioniMetalliche() {
        
        Object[] s = new Object[4];
        s[0] = new SezioneMetallicaDoppioT("Trave doppio T");
        s[1] = new SezioneMetallicaCassone("Cassone");
        s[2] = new SezioneMetallicaCassoneII("Cassone tipo 2");
        s[3] = new SezioneMetallicaGenerica("Sezione generica");
        
        return s;
        
    }
    
    // resistituisce le calsse valide per soletta
    public Object[] getAvailableClassSezioniSoletta() {
        
        Object[] s = new Object[3];
        s[0] = new SolettaT("Soletta a T");
        s[1] = new SolettaType2("Soletta tipo 2");
        s[2] = new SolettaType2_2("Soletta tipo 3");
        return s;
        
    }
    
    public void setConci(ArrayList<SezioneMetallica> conci) {
        this.conci = conci;
    }
    
    public void setCurrentSezioneAnalisi(Sezione currentSezioneAnalisi) {
        this.currentSezioneAnalisi = currentSezioneAnalisi;
    }
    
    public void setCurrentSezioneVerifica(Sezione currentSezioneVerifica) {
        this.currentSezioneVerifica = currentSezioneVerifica;
    }
    
    public void resetAnalisi(){
        
        analizzato=false;
        for (int i= 0; i < sezioniAnalisiGlobale.size();++i) {
            sezioniAnalisiGlobale.get(i).getSezioniOutput().clear();
        }
        for (int i= 0; i < sezioniVerifica.size();++i) {
            sezioniVerifica.get(i).getSezioniOutput().clear();
            sezioniVerifica.get(i).getSezioniMetEfficaci().clear();
            sezioniVerifica.get(i).getSezioniMetGiunto().clear();
        }
        
        
        try {
            ridisegna();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
