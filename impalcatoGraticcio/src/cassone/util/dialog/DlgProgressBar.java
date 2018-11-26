package cassone.util.dialog;

import cassone.model.CombinazioneCarichi;
import cassone.model.MaterialeAcciaio;
import cassone.model.Sezione;
import cassone.model.SezioneOutputTensioniFase;
import cassone.model.SezioniMetalliche.SezioneMetallica;
import cassone.model.SezioniMetalliche.SezioneMetallicaCassone;
import cassone.model.SezioniMetalliche.SezioneMetallicaCassoneII;
import cassone.model.SezioniMetalliche.SezioneMetallicaDoppioT;
import cassone.model.SezioniMetalliche.SezioneMetallicaGenerica;
import cassone.model.Sollecitazioni;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import cassone.model.Campate;
import cassone.model.Progetto;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author Andrea Cavalieri
 * @version 1.0
 */

public class DlgProgressBar extends JDialog implements ActionListener, Runnable{
    private JTextArea txtArea = new JTextArea();
    
    int nTask;
    private JButton startButton;
    
    JProgressBar progressBar;
    JTextArea taskOutput;
    JPanel panel;
    public DlgProgressBar(boolean modal) throws HeadlessException {
        
        this.setModal(modal);
        this.nTask=nTask;
        
        try {
            this.setTitle("ELABORA....");
            jbInit();
            //     Progetto prg = Progetto.getInstance();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    private void jbInit() throws Exception {
        setLayout(new BorderLayout());
        this.setSize(600,300);
        Dimension scr = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((int)(scr.getWidth()/2- getWidth()/2),(int)(scr.getHeight()/2-getHeight()/2));

          //Create the demo's UI.
        startButton = new JButton("Start");
        startButton.setActionCommand("start");
        startButton.addActionListener(this);
        
        
        progressBar = new JProgressBar(0,100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        
        taskOutput = new JTextArea();
        taskOutput.setMargin(new Insets(5,5,5,5));
        taskOutput.setEditable(false);
        
        panel = new JPanel();
        panel.add(progressBar);
        add(panel, BorderLayout.PAGE_START);
        add(startButton, BorderLayout.PAGE_END);
        
        add(new JScrollPane(taskOutput), BorderLayout.CENTER);
        //   this.getContentPane().setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        avviaAnalisi();
        
    }
    
    public void addTask(String task){
        taskOutput.append(task+"\n");
        taskOutput.setCaretPosition(taskOutput.getText().length());
    }
    public void addTaskProgressBar(){
        progressBar.setValue(progressBar.getValue()+1);
    }
    
    public void run(){
        Progetto prg = Progetto.getInstance();
        int wait=5;
        
        ArrayList<Sezione> sezioniVerifica =prg.getSezioniVerifica();
        ArrayList<Sezione> sezioniAnalisiGlobale =prg.getSezioniAnalisiGlobale();
        ArrayList<CombinazioneCarichi> combinazioni =prg.getCombinazioni();
        Campate campate = prg.getCampate();
        double[] n = prg.getN();
        MaterialeAcciaio materiale = prg.getMateriale();
        
        
        int ntask = sezioniAnalisiGlobale.size()*5;
        ntask+= sezioniVerifica.size()*combinazioni.size()*5;
        ntask+= sezioniVerifica.size()*combinazioni.size();
        ntask+= sezioniVerifica.size()*combinazioni.size();
        
            progressBar.setMaximum(ntask);
        
        
        // analisi globale
        for (int i = 0; i < sezioniAnalisiGlobale.size(); ++i) {
            
            SezioneOutputTensioniFase[] ta = new SezioneOutputTensioniFase[5];
            Sezione sezAn = sezioniAnalisiGlobale.get(i);
            ArrayList<SezioneOutputTensioniFase[]> sezioniOutAnalisi = sezAn
                    .getSezioniOutput();
            sezioniOutAnalisi.clear();
            //progress bar
            addTask("SEZIONE ANALISI: " + sezAn.getName());
            for (int j = 0; j < 5; ++j) {
                ta[j] = new SezioneOutputTensioniFase();
                if (j == 0)
                    ta[j].calcolaParametriStatici(j == 0, Double.MAX_VALUE,
                            campate, sezAn);
                else
                    ta[j].calcolaParametriStatici(j == 0, n[j - 1], campate,
                            sezAn);
                addTask("fase: " + j +".......");
                addTaskProgressBar();
          
                
            }
            sezioniOutAnalisi.add(ta);
        }
        
        // verifica tensioni
        for (int i = 0; i < sezioniVerifica.size(); ++i) {
            Sezione sezV = sezioniVerifica.get(i);
            SezioneMetallica sm = sezV.getSezioneMetallica();
            ArrayList<SezioneOutputTensioniFase[]> sezioniOutput = sezV
                    .getSezioniOutput();
            sezioniOutput.clear();
            
            addTask("SEZIONE VERIFICA: " + sezV.getName());
            for (int j = 0; j < combinazioni.size(); j++) {
                SezioneOutputTensioniFase[] tf = new SezioneOutputTensioniFase[5];
                Sollecitazioni[] sol = new Sollecitazioni[5];
                CombinazioneCarichi cmb = combinazioni.get(j);
                addTask("   combinazione " + cmb.getName());
                
                for (int k = 0; k < 5; k++) {
                    sol[k] = prg.getSollecitazioniFase(k, sezV, cmb);
                }
                
                boolean mPos = prg.isMomentoPositivo(sol,sezV);
                for (int k = 0; k < 5; k++) {
                    
                    tf[k] = new SezioneOutputTensioniFase();
                    tf[k].setMomPositivo(mPos);
                    if (k == 0)
                        tf[k].calcolaTensioni(k == 0, Double.MAX_VALUE , sezV, sol[k],sm.isSezioneChiusa());
                    else
                        tf[k].calcolaTensioni(k == 0, n[k - 1], sezV, sol[k],sm.isSezioneChiusa());
                    
                   
                    try {
                        Thread.sleep(wait);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    
                    addTask("      fase " + k+ " .......");
                    addTaskProgressBar();
                }
                sezioniOutput.add(tf);
            }
        }
        
        // verifica tensioni efficaci
        for (int i = 0; i < sezioniVerifica.size(); ++i) {
            Sezione sezTE = sezioniVerifica.get(i);
            SezioneMetallica sm = sezTE.getSezioneMetallica();
            double passoTras = sezTE.getIrrigidimetoTrasversale().getPasso();
            boolean rigidEndPost = sezTE.getIrrigidimetoTrasversale().isRigidEndPost();
            ArrayList<SezioneMetallica> smeff = sezTE.getSezioniMetEfficaci();
            smeff.clear();
            
            addTask("VERIFICA STABILITA SEZIONE :" + sezTE.getName());
            for (int j = 0; j < combinazioni.size(); j++) {
                SezioneMetallica seff = null;
                if (sm.getClass() == SezioneMetallicaDoppioT.class) {
                    seff = new SezioneMetallicaDoppioT(
                            (SezioneMetallicaDoppioT) sm, "");
                } else if (sm.getClass() == SezioneMetallicaGenerica.class) {
                    seff = new SezioneMetallicaGenerica((SezioneMetallicaGenerica) sm, "");
                } else if (sm.getClass() == SezioneMetallicaCassone.class) {
                    seff = new SezioneMetallicaCassone((SezioneMetallicaCassone)sm,"");
                } else if (sm.getClass() == SezioneMetallicaCassoneII.class) {
                    seff = new SezioneMetallicaCassoneII((SezioneMetallicaCassoneII)sm,"");
                }
                SezioneOutputTensioniFase so= sezTE.getTensioniTotali(j);
                seff.calcolaTensioniEfficaci(materiale, so,passoTras,rigidEndPost);
                smeff.add(seff);
                try {
                    
                    Thread.sleep(wait);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                addTask("   combianzione " + combinazioni.get(j).getName()+ " .......");
                addTaskProgressBar();
                
                
            }
        }
        
        // giunti
        for (int i = 0; i < sezioniVerifica.size(); ++i) {
            Sezione sezTE = sezioniVerifica.get(i);
            if(sezTE.getGiunto()== null) continue;
            SezioneMetallica sm = sezTE.getSezioneMetallica();
            ArrayList<SezioneMetallica> smG = sezTE.getSezioniMetGiunto();
            smG.clear();
            addTask("VERIFICA GIUNTI BULLONATI SEZIONE " + sezTE.getName());
            
            for (int j = 0; j < combinazioni.size(); j++) {
                SezioneMetallica seff = null;
                if (sm.getClass() == SezioneMetallicaDoppioT.class) {
                    seff = new SezioneMetallicaDoppioT(
                            (SezioneMetallicaDoppioT) sm, "");
                    SezioneOutputTensioniFase so= sezTE.getTensioniTotali(j);
                    seff.calcolaBulloni(so,sezTE,materiale, sezTE.getGiunto());
                    smG.add(seff);
                }
                
                try {
                    Thread.sleep(wait);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                addTask("   combianzione " + combinazioni.get(j).getName()+ " .......");
                addTaskProgressBar();
                
            }
        }
        
        comleteTaskProgressBar();
        
        addTask(" ");
        addTask("ANALALISI COMPLETATA CON SUCCESSO");
        prg.setAnalizzato(true);
        
        
    }
    
    
    public void comleteTaskProgressBar(){
        progressBar.setValue(progressBar.getMaximum());
    }
    /**
     * Invoked when the user presses the start button.
     */
    
    public void avviaAnalisi() {
    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        startButton.setEnabled(false);
       Thread runner = null;
        boolean loop;
        
        if(runner == null) {
            runner = new Thread(this);
            loop = true;
            runner.start();
            
        }
        
        
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
       startButton.setEnabled(true);
        startButton.setText("Chiudi");
    }
    
    public void actionPerformed(ActionEvent evt) {
    dispose();
        /*      Object s= evt.getSource();
        
        if(s.getClass()== JButton.class){
            JButton jb = (JButton)s;
            if(jb.getText()=="Start") avviaAnalisi();
            if(jb.getText()=="Chiudi") this.dispose();
        }
    */    
        
    }
}




