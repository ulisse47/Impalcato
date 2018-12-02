package cassone.controller;

import cassone.Main;
import cassone.util.dialog.DlgModificaConci;
import cassone.util.dialog.DlgProgressBar;
import cassone.util.dialog.DlgVerifiche;
import cassone.view.DlgInit;
import cassone.view.Instabilita.InputInstabilita;
import cassone.view.MainFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import cassone.model.Concio;
import cassone.model.Progetto;
import cassone.model.Sezione;
import cassone.model.SezioniMetalliche.SezioneMetallica;
import cassone.model.soletta.Soletta;
import cassone.util.Parser;
import cassone.util.dialog.DlgModificaLuci;
import cassone.util.dialog.DlgModificaNomiConci;
import cassone.util.dialog.DlgModificaNomiSezioni;
import cassone.util.dialog.DlgModificaNomiSolette;
import cassone.view.Giunti.ViewGiuntoVistaLaterale;
import cassone.view.Instabilita.SezioneIrrigiditaView;
import cassone.view.Instabilita.TableTensioniEfficaci;
import cassone.view.VerificaTensioni.InputVerifiche;
import cassone.view.VerificaTensioni.TabTensioni;
import cassone.view.VerificaTensioni.TableCombView;
import cassone.view.VerificaTensioni.TableCondView;
import cassone.view.VerificaTensioni.TableTensioniView;
import cassone.view.VerificaTensioni.TensioniVerificaView;
import cassone.view.analisi.InputView;
import cassone.view.analisi.TabGeo;
import cassone.view.analisi.TableOutputView;

public class ControllerAnalisi
        implements ActionListener , ItemListener  {
    
    public ControllerAnalisi() {
    }
    
    public void actionPerformed(ActionEvent e) {
        
        Class cl = e.getSource().getClass();
        JMenuItem source = null;
        JButton bottomSource = null;
        JComboBox comboSource = null;
        
        //bottoni
        if (cl == JButton.class) {
            bottomSource = (JButton) e.getSource();
            if (bottomSource.getActionCommand() == "OPEN") {
                open();
            }
            if (bottomSource.getActionCommand() == "SAVE") {
                save();
            }
            if (bottomSource.getActionCommand() == "ELABORA") {
                elabora();
            }
            if (bottomSource.getActionCommand() == "DISEGNA") {
                disegna();
            }
            if (bottomSource.getActionCommand() == "ADD_CONCIO") {
                addConcio();
            }
            if (bottomSource.getActionCommand() == "ADD_SEZIONE_ANALISI") {
                addSezioneAnalisi();
            }
            if (bottomSource.getActionCommand() == "STAMPA") {
                stampa();
            }
            if (bottomSource.getActionCommand() == "DELETE_CONCIO") {
                deleteConcio();
            }
            if (bottomSource.getActionCommand() == "DELETE_SEZIONE_ANALISI") {
                deleteSezioneAnalisi();
            }
            
            if (bottomSource.getActionCommand() == "ADD_CAMPATA") {
                aggiungiCampata();
            }
            if (bottomSource.getActionCommand() == "REMOVE_CAMPATA") {
                rimuoviCampata();
            }
            if (bottomSource.getActionCommand() == "MODIFICA_CAMPATA") {
                modificaCampata();
            }
            if (bottomSource.getActionCommand() == "RINOMINA_SEZIONI") {
                rinominaSezioni();
            }
            if (bottomSource.getActionCommand() == "RINOMINA_CONCI") {
                rinominaConci();
            }
            if (bottomSource.getActionCommand() == "ADD_SOLETTA") {
                aggiungiSoletta();
            }
            if (bottomSource.getActionCommand() == "RINOMINA_SOLETTE") {
                rinominaSoletta();
            }
            if (bottomSource.getActionCommand() == "ELIMINA_SOLETTA") {
                eliminaSoletta();
            }
            
            
        }
        
        //combo
        if (cl == JComboBox.class) {
            comboSource = (JComboBox) e.getSource();
            if (comboSource.getActionCommand() == "SET_CONCIO") {
                setConcio((SezioneMetallica)comboSource.getSelectedItem());
            }
            if (comboSource.getActionCommand() == "SET_SEZIONE_ANALISI") {
                setSezioniAnalisi();
            }
            
            if (comboSource.getActionCommand() == "SET_SEZIONE") {
                setSezione();
            }
            if (comboSource.getActionCommand() == "SET_SOLETTA") {
                setSoletta((Soletta)comboSource.getSelectedItem());
            }
            
            
        }
        
    }
    
    public void eliminaSoletta() {
        Progetto prg = Progetto.getInstance();
        Soletta s[] = prg.getArraySolette();
        Object cl = (Object) JOptionPane.showInputDialog(null,"Sezione",
                "Elimina soletta",JOptionPane.INFORMATION_MESSAGE,
                null,s,s[0]);
        
        if(cl!= null){
            try {
                prg.deleteSoletta((Soletta)cl);
            } catch (Exception e) {
                e.printStackTrace();
            }
            eliminaSoletta();
        }
    }
    
    public void rinominaSoletta(){
        Progetto prg = Progetto.getInstance();
        DlgModificaNomiSolette dlg = new DlgModificaNomiSolette(prg.getSolette());
        dlg.setTitle("RINOMINA SOLETTE");
        dlg.setVisible( true );
        try {
            prg.ridisegna();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void modificaConci(){
        DlgModificaConci dlg = new DlgModificaConci();
        dlg.setTitle("MODIFICA CONCI METALLCII");
        dlg.setVisible( true );
     
    }
    
    
    public void aggiungiSoletta(){
        
        Progetto prg = Progetto.getInstance();
        
        Object s[] = prg.getAvailableClassSezioniSoletta();
        Object cl = (Object) JOptionPane.showInputDialog(null,"Sezione tipo",
                "Tipo sezioni soletta",JOptionPane.INFORMATION_MESSAGE,
                null,s,s[0]);
        if(cl!= null){
            try {
                prg.addSoletta(cl.getClass(),true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
       
        
    }
    
    public void rinominaSezioni(){
        Progetto prg = Progetto.getInstance();
        DlgModificaNomiSezioni dlg = new DlgModificaNomiSezioni(prg.getSezioniAnalisiGlobale());
        dlg.setTitle("RINOMINA SEZIONI ANALISI");
        dlg.setVisible( true );
        Parser.updateInputView();
    }
    
    public void rinominaConci(){
        Progetto prg = Progetto.getInstance();
        DlgModificaNomiConci dlg = new DlgModificaNomiConci(prg.getConci());
        dlg.setTitle("RINOMINA CONCI METALLICI");
        dlg.setVisible( true );
        Parser.updateInputView();
    }
    
    public void modificaCampata(){
        
        DlgModificaLuci dlg = new DlgModificaLuci();
        dlg.setTitle("MODIFICA LUCI CAMPATE");
        dlg.setVisible( true );
        Parser.updateInputView();
        try {
            Progetto.getInstance().ridisegna();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
 
    }
    
    public void aggiungiCampata(){
        Progetto prg = Progetto.getInstance();
        prg.aggiungiCampata();
        try {
            prg.ridisegna();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public void rimuoviCampata(){
        Progetto prg = Progetto.getInstance();
        prg.rimuoviCampata();
        try {
            prg.ridisegna();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void deleteConcio() {
        Progetto prg = Progetto.getInstance();
        SezioneMetallica s[] = prg.getSezioniMetalliche();
        Object cl = (Object) JOptionPane.showInputDialog(null,"Sezione",
                "Elimina sezione metallica",JOptionPane.INFORMATION_MESSAGE,
                null,s,s[0]);
        
        if(cl!= null){
            try {
                prg.deleteConcio((SezioneMetallica)cl);
            } catch (Exception e) {
                e.printStackTrace();
            }
            deleteConcio();
        }
    }
    
    public void deleteSezioneAnalisi() {
        Progetto prg = Progetto.getInstance();
        Sezione s[] = prg.getArraySezioneAnalisi();
        Object cl = (Object) JOptionPane.showInputDialog(null,"Sezione",
                "Elimina sezione analisi",JOptionPane.INFORMATION_MESSAGE,
                null,s,s[0]);
        
        if(cl!= null){
            try {
                prg.deleteSezione((Sezione)cl,true);
            } catch (Exception e) {
                e.printStackTrace();
            }
         deleteSezioneAnalisi();

        }
    }
    
    
    private void setConcio(SezioneMetallica concio) {
        Progetto prg = Progetto.getInstance();
        try {
            prg.setSezioneMetallica(concio, true);
            
        } catch (Exception e) {
        }
        
    }
    
    private void setSoletta(Soletta soletta) {
        Progetto prg = Progetto.getInstance();
        try {
            prg.setSoletta(soletta, true);
            
        } catch (Exception e) {
        }
        
    }
    private void setSezioniAnalisi() {
        InputView inputView = InputView.getInstance();
        Progetto prg = Progetto.getInstance();
        
        Sezione selected = (Sezione)inputView.getComboSezione().getSelectedItem();
        
        //salva tutto prima di cambiare sezione
 /*   if ( !prg.save() ) {
        inputView.getComboSezione().setSelectedItem( prg.getCurrentSezioneAnalisi());
        return;
    }
  */
        try {
            prg.setSezioneCorrente(selected, true);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    
    
    
    
    /**
     *
     */
    private void setSezione() {
        InputVerifiche inputView = InputVerifiche.getInstance();
        Progetto prg = Progetto.getInstance();
        
        if ( !prg.save() ) return;
        
        int selected = inputView.getComboSezione().getSelectedIndex();
        //setto la current_sezione = selected
        //prg.setSezione(selected);
        //repaint dell'inputView
        Parser.updateInputView();
        repaint2();
    }
    
    public void elabora() {
        
        Progetto prg = Progetto.getInstance();
        try {
            prg.elabora();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void verifica() {
        
     Progetto.getInstance().verifica();
//        DlgModificaConci dlg = new DlgModificaConci();
//        dlg.setVisible(true);
        
    }
    
    public void stampa() {
        Progetto prg = Progetto.getInstance();
        if(!prg.isAnalizzato()){
            int r = JOptionPane.showConfirmDialog(null,"Analisi non ancora effettuata." +
                    " Effettuare ora?","ATTENZIONE",JOptionPane.OK_CANCEL_OPTION);
            
            if(r==JOptionPane.OK_OPTION){
                DlgProgressBar dlg = new DlgProgressBar(true);
                dlg.setVisible(true);
                try {
                    prg.ridisegna();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else return;
        }
        prg.writeRFT();
    }
    
    public void disegna() {
        
        Progetto prg = Progetto.getInstance();
        
        try {
            Parser.validate();
            prg.ridisegna();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    /**
     *
     */
    public void addConcio() {
        Progetto prg = Progetto.getInstance();
        
        Object s[] = prg.getAvailableClassSezioniMetalliche();
        Object cl = (Object) JOptionPane.showInputDialog(null,"Sezione tipo",
                "Tipo sezioni metalliche",JOptionPane.INFORMATION_MESSAGE,
                null,s,s[0]);
        if(cl!= null){
            try {
                prg.addSezioneMetallica(cl.getClass(),true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
    }
    
    /**
     *
     */
    public void addSezioneAnalisi() {
        Progetto prg = Progetto.getInstance();
//        TabGeo.getInstance().requestFocus();
        try {
            prg.addSezioneAnalisi();
//            TabGeo.getInstance().v;
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    /**
     *
     */
/*  private void repaint() {
      TabGeo.getInstance().repaint();
      TableOutputView.refresh();
 
      TableCondView.refresh();
      TableCombView.refresh();
      TableTensioniView.refresh();
      TableTensioniEfficaci.refresh();
 
      SezioneGiunto.getInstance().repaint();
      SezioneIrrigiditaView.getInstance().repaint();
      TensioniVerificaView.getInstance().repaint();
 //     TensioniInstabilitaView.getInstance().repaint();
  }
 */
    /**
     *
     */
    private void repaint2() {
        TabTensioni.getInstance().repaint();
        TableCondView.refresh();
    }
    
    /**
     * Carica i valori del file sulla finestra InputView
     */
    public void open() {
        Progetto prg = Progetto.getInstance();
        prg.openFile();
        try {
            //           prg.aggiornaPannelli();
            prg.ridisegna();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * Carica i valori del file sulla finestra InputView
     */
    public void nuovo() {
        Progetto.getInstance().close();
        Progetto prg = Progetto.getNewInstance();
        InputView.getInstance().aggiornaPannelli(prg.getCurrentSezioneAnalisi());
        InputInstabilita.getInstance().aggiornaPannelli(prg.getCurrentSezioneVerifica());
        Parser.updateInputView();
        try {
            prg.ridisegna();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    
    
    
    /**
     * Salva i valori della maschera di input nel modello( Common )
     * e poi anche in un file
     */
    public void save() {
        //Properties prop;
        Progetto prg = Progetto.getInstance();
        prg.saveToFile();
        try {
            prg.ridisegna();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
    }
    
    
    public void saveAs() {
        //Properties prop;
        Progetto prg = Progetto.getInstance();
        prg.saveAsToFile();
        try {
            prg.ridisegna();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    
  /*public void save() {
    //Properties prop;
    Progetto prg = Progetto.getInstance();
    prg.save();
    repaint();
  }*/
    
    
    public void itemStateChanged( ItemEvent e ) {
        Object source = e.getItemSelectable();
        JCheckBox ck = null;
        InputView inputPanel = InputView.getInstance();
        
        if (source.getClass() == JCheckBox.class ) {
            ck = (JCheckBox)source;
            if ( ck.getActionCommand() == "CALCOLO_AUTOMATICO_B") {
                Progetto.getInstance().getCurrentSezioneAnalisi().setCalcoloAutomaticoBeff(ck.isSelected());
                Parser.updateInputView();
                //     	Progetto prg=Progetto.getInstance();
                //    	prg.getCurrentSezioneAnalisi().setCalcoloAutomaticoBeff(ck.isSelected());
                // 		inputPanel.setEnableTextBoxCalcoloAutomatico(ck.isSelected());
                // 		inputPanel.getComboCampata().setEnabled(ck.isSelected());
                
            }
            if ( ck.getActionCommand() == "CALCOLO_AUTOMATICO_ARMATURA") {
                Progetto.getInstance().getCurrentSezioneAnalisi().setCalcolaArmatura(ck.isSelected());
                Parser.updateInputView();
//        	Progetto prg=Progetto.getInstance();
                //       	prg.getCurrentSezioneAnalisi().setCalcolaArmatura(ck.isSelected());
                //  		inputPanel.setEnableTextBoxCalcoloArmatura(ck.isSelected());
                //Parser.updateInputView();
            }
            
        }
        
    }
    
    
    
}
