package cassone.util.dialog;

import cassone.controller.ControllerAnalisi;
import cassone.model.SezioniMetalliche.SezioneMetallica;
import cassone.view.analisi.InputView;
import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;

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

public class DlgModificaConci extends JDialog {
    private JButton jButtonOk = new JButton();
    private JButton jButtonCancel = new JButton();
//    private JComboBox cbConcio = new JComboBox();
    private JComboBox cbConcio;
    private JPanel panelConcio;

    ControllerAnalisi controllerAnalisi = new ControllerAnalisi();
    
    public DlgModificaConci() throws HeadlessException {
        
        this.setModal(true);
        try {
            jbInit();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    private void jbInit() throws Exception {
        this.getContentPane().setLayout(new BorderLayout());
        this.setSize(300,250);
        this.setLocation(200,200);
        
        cbConcio = InputView.getInstance().getComboConcio();
        panelConcio = InputView.getInstance().getConcio();
//        cbConcio.setActionCommand("SET_CONCIO");
//        cbConcio.addActionListener(controllerAnalisi);
        Progetto prg = Progetto.getInstance();
        ArrayList<SezioneMetallica> sm = prg.getConci();
        resetComboConcio(sm.get(0),sm );
        this.getContentPane().add(cbConcio, BorderLayout.NORTH);
        this.getContentPane().add(panelConcio, BorderLayout.CENTER);
        this.getContentPane().add(jButtonCancel, BorderLayout.SOUTH);
//    this.getContentPane().add(jButtonOk, null);
        
    }
    
    void jButtonOk_actionPerformed(ActionEvent e) {
        
        
    }
    
    public void resetComboConcio(Object current_concio, ArrayList<SezioneMetallica> nConci) {
        cbConcio.removeActionListener(controllerAnalisi);
        cbConcio.removeAllItems();
        for (int i = 0; i < nConci.size(); i++) {
            cbConcio.addItem(nConci.get(i));
        }
        cbConcio.setSelectedItem(current_concio);
        cbConcio.addActionListener(controllerAnalisi);
        
    }
    
    
    void jButtonCancel_actionPerformed(ActionEvent e) {
        this.dispose();
    }
    void jButtonAdd_actionPerformed(ActionEvent e) {
        
        Progetto prg = Progetto.getInstance();
        Campate cam = prg.getCampate();
        cam.setNCampate(cam.getNCampate()+1);
//	  jTable1.();
//	  this.repaint();
    }
}

