package cassone.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JMenuItem;

import cassone.model.Progetto;
import cassone.model.Sezione;
import cassone.view.Giunti.InputGiuntiBullonatiGeneral;

public class ControllerVerificaGiunti implements ActionListener{
    
    public ControllerVerificaGiunti() {
    }
    
    public void actionPerformed(ActionEvent e) {
        
        Class cl = e.getSource().getClass();
        JMenuItem source = null;
        JButton bottomSource = null;
        JComboBox comboSource = null;
        
        // bottoni
        if (cl == JButton.class) {
            bottomSource = (JButton) e.getSource();
            if (bottomSource.getActionCommand() == "ADD_GIUNTO") {
                addGiunto();
            }
            if (bottomSource.getActionCommand() == "REMOVE_GIUNTO") {
                removeGiunto();
            }
            
            
        }
        // combo
        if (cl == JComboBox.class) {
            comboSource = (JComboBox) e.getSource();
            if (comboSource.getActionCommand() == "SET_SEZIONE") {
                setSezione();
            }
            
        }
        
    }
    
    
    /**
     *
     */
    private void setSezione() {
        InputGiuntiBullonatiGeneral inputView = InputGiuntiBullonatiGeneral.getInstance();
        Progetto prg = Progetto.getInstance();
        Sezione selected = (Sezione) inputView.getComboSezione()
        .getSelectedItem();
        
        try {
            prg.setSezioneCorrente(selected, false);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    public void removeGiunto() {
        Progetto prg = Progetto.getInstance();
        if (prg.checkInput()) {
            prg.removeGiunto(prg.getCurrentSezioneVerifica());
      try {
            prg.ridisegna();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        }
    }
    
    public void addGiunto() {
        Progetto prg = Progetto.getInstance();
        if (prg.checkInput()) {
            prg.aggiungiGiunto(prg.getCurrentSezioneVerifica());
       try {
            prg.ridisegna();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        }
    }
    
    
    
}
