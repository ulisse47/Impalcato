package cassone.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import cassone.model.CombinazioneCarichi;
import cassone.model.CondizioniCarico;
import cassone.model.Progetto;
import cassone.model.Sezione;
import cassone.model.SezioniMetalliche.SezioneMetallica;
import cassone.model.soletta.Soletta;
import cassone.util.dialog.DlgModificaNomiCombinazioni;
import cassone.util.dialog.DlgModificaNomiSezioni;
import cassone.util.Parser;
import cassone.view.VerificaTensioni.InputVerifiche;
import cassone.view.VerificaTensioni.TableCombView;
import cassone.view.VerificaTensioni.TableCondView;
import cassone.view.VerificaTensioni.TableTensioniView;



public class ControllerVerificaSezioni
        implements ActionListener , ItemListener  {
    
    public ControllerVerificaSezioni() {
    }
    
    public void actionPerformed(ActionEvent e) {
        
        Class cl = e.getSource().getClass();
        JMenuItem source = null;
        JButton bottomSource = null;
        JComboBox comboSource = null;
        
        //bottoni
        if (cl == JButton.class) {
            bottomSource = (JButton) e.getSource();
            
   /*      if (bottomSource.getActionCommand() == "OPEN") {
        open();
     } */
            if (bottomSource.getActionCommand() == "SAVE") {
                //       saveToFile();
            }
            if (bottomSource.getActionCommand() == "ELABORA") {
                //       elabora();
            }
            if (bottomSource.getActionCommand() == "DISEGNA") {
                disegna();
            }
            if (bottomSource.getActionCommand() == "ADD_SEZIONE") {
                addSezione();
            }
            if (bottomSource.getActionCommand() == "STAMPA") {
                stampa();
            }
            if (bottomSource.getActionCommand() == "ADD_CONDIZIONE") {
                addCondizione();
            }
            if (bottomSource.getActionCommand() == "ADD_COMBINAZIONE") {
                addCombinazione();
            }
            if (bottomSource.getActionCommand() == "DELETE_SEZIONE") {
                deleteSezione();
            }
            
            if (bottomSource.getActionCommand() == "DELETE_CONDIZIONE") {
                deleteCondizione();
            }
            
            if (bottomSource.getActionCommand() == "DELETE_COMBINAZIONE") {
                deleteCombinazione();
            }
            if (bottomSource.getActionCommand() == "RINOMINA_COMBINAZIONI") {
                rinominaCombinazioni();
            }
            
            if (bottomSource.getActionCommand() == "INPORT_CONDIZIONI") {
                // inportaCondizioni();
            }
            if (bottomSource.getActionCommand() == "RINOMINA_SEZIONI") {
                rinominaSezioni();
            }
            if (bottomSource.getActionCommand() == "RESET_ANALISI") {
                resetAnalisi();
            }
        }
        
        //combo
        if (cl == JComboBox.class) {
            comboSource = (JComboBox) e.getSource();
            if ( comboSource.getActionCommand() == "SET_CONCIO") {
                setConcio();
            }
            if ( comboSource.getActionCommand() == "SET_SEZIONE") {
                setSezione();
            }
            if ( comboSource.getActionCommand() == "SET_SOLETTA") {
                setSoletta();
            }
            
        }
        
    }
    
    public void resetAnalisi(){
        Progetto.getInstance().resetAnalisi();
    }
    
    public void rinominaCombinazioni(){
        Progetto prg = Progetto.getInstance();
        DlgModificaNomiCombinazioni dlg = new DlgModificaNomiCombinazioni(prg.getCombinazioni());
        dlg.setTitle("RINOMINA COMBINAZIONI");
        dlg.setVisible( true );
        Parser.updateInputView();
    }
    
    public void rinominaSezioni(){
        Progetto prg = Progetto.getInstance();
        DlgModificaNomiSezioni dlg = new DlgModificaNomiSezioni(prg.getSezioniVerifica());
        dlg.setTitle("RINOMINA SEZIONI VERIFICA");
        dlg.setVisible( true );
        Parser.updateInputView();
    }
    /**
     *
     */
    public void deleteSezione(){
        Progetto prg = Progetto.getInstance();
        Sezione s[] = prg.getArraySezioneVerifica();
        Object cl = (Object) JOptionPane.showInputDialog(null,"Sezione",
                "Elimina sezione analisi",JOptionPane.INFORMATION_MESSAGE,
                null,s,s[0]);
        
        if(cl!= null){
            try {
                prg.deleteSezione((Sezione)cl,false);
            } catch (Exception e) {
                e.printStackTrace();
            }
            deleteSezione();
            
        }
    }
    
    private void setConcio() {
        Progetto prg=Progetto.getInstance();
        InputVerifiche im = InputVerifiche.getInstance();
        SezioneMetallica sm = (SezioneMetallica)im.getComboConcio().getSelectedItem();
        
        
        try {
            prg.setSezioneMetallica(sm, false);
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
        Sezione selected = (Sezione)inputView.getComboSezione().getSelectedItem();
        
        try {
            prg.setSezioneCorrente(selected,false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void setSoletta() {
        InputVerifiche inputView = InputVerifiche.getInstance();
        Progetto prg = Progetto.getInstance();
        Soletta selected = (Soletta)inputView.getComboSoletta().getSelectedItem();
        
        try {
            prg.setSoletta(selected,false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
/*  private void elabora() {
    Progetto prg = Progetto.getInstance();
    try {
        prg.elabora();
        } catch (Exception e) {
                e.printStackTrace();
        }
 
  }
 */
    private void stampa(){
        Progetto prg = Progetto.getInstance();
        try {
            prg.elabora();
            prg.writeRFT();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void disegna() {
        Progetto prg = Progetto.getInstance();
        try {
            Parser.validate();
            prg.ridisegna();
        } catch (Exception e) {
            e.printStackTrace();
        }
//    repaint();
    }
    
    /**
     *
     */
    public void addSezione() {
        Progetto prg = Progetto.getInstance();
        
        try {
            prg.addSezioneVerifica();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     *
     */
    public void addCondizione() {
        Progetto prg = Progetto.getInstance();
        
        if (prg.addCondizione()) {
            Parser.updateInputView();
            repaint();
        }
    }
    
    /**
     *
     */
    public void inportaCondizioni() {
        
    }
    
    
    
    
    
    /**
     *
     */
    public void addCombinazione() {
        Progetto prg = Progetto.getInstance();
        
        if (prg.addCombinazione()) {
            //   TableCombView.refresh();
//Parser.updateInputView();
            repaint();
        }
    }
    
    /**
     *
     */
    private void repaint() {
        //tab
        TableCondView.refresh();
        TableCombView.refresh();
        TableTensioniView.refresh();
        //TableTensIdealiView.refresh();*/
        //   TabTensioni.getInstance().repaint();
    }
    
    
    /**
     * Carica i valori del file sulla finestra InputView
     */
 /* private void open() {
    Progetto prg = Progetto.getInstance();
    prg.openFile();
    Parser.updateInputView();
    TableCondView.refresh();
    TableCombView.refresh();
    repaint();
  }*/
    
    
    public void deleteCondizione() {
        
        Progetto prg = Progetto.getInstance();
        CondizioniCarico s[] = prg.getArrayCondizioniCarico();
        Object cl = (Object) JOptionPane.showInputDialog(null,"Condizione",
                "Elimina condizione",JOptionPane.INFORMATION_MESSAGE,
                null,s,s[0]);
        
        if(cl!= null){
            try {
                prg.deleteCondizione((CondizioniCarico)cl);
            } catch (Exception e) {
                e.printStackTrace();
            }
            deleteCondizione();
        }
    }
    
    public void deleteCombinazione() {
        Progetto prg = Progetto.getInstance();
        CombinazioneCarichi s[] = prg.getArrayCombinazioni();
        Object cl = (Object) JOptionPane.showInputDialog(null,"Combinazioni",
                "Elimina combinazioni",JOptionPane.INFORMATION_MESSAGE,
                null,s,s[0]);
        
        if(cl!= null){
            try {
                prg.deleteCombinazione((CombinazioneCarichi)cl);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    
    
    /**
     * Salva i valori della maschera di input nel modello( Common )
     * e poi anche in un file
     */
/*  private void saveToFile() {
    //Properties prop;
    Progetto prg = Progetto.getInstance();
    prg.saveToFile();
    //repaint();
  }*/
    
    public void itemStateChanged(ItemEvent e) {
        Object source = e.getItemSelectable();
        JCheckBox ck = null;
        InputVerifiche inputPanel = InputVerifiche.getInstance();
        
        if (source.getClass() == JCheckBox.class) {
            ck = (JCheckBox) source;
            if (ck.getActionCommand() == "CALCOLO_AUTOMATICO_B") {
                Progetto.getInstance().getCurrentSezioneVerifica().setCalcoloAutomaticoBeff(
                        ck.isSelected());
                Parser.updateInputView();
//				inputPanel.setEnableTextBoxCalcoloAutomatico(ck.isSelected());
//				Parser.updateInputView();
            }
            if (ck.getActionCommand() == "CALCOLO_AUTOMATICO_ARMATURA") {
                Progetto.getInstance().getCurrentSezioneVerifica().setCalcolaArmatura(
                        ck.isSelected());
                Parser.updateInputView();
//				inputPanel.setEnableTextBoxCalcoloArmatura(ck.isSelected());
                //				Parser.updateInputView();
            }
            
        }
        
    }
    
}
