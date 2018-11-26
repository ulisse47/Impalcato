package cassone.view.analisi;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JPanel;

import cassone.controller.ControllerAnalisi;

public class BottoniView extends JPanel{
  JButton jBOpen = new JButton();
  JButton jBSaveFile = new JButton();
  JButton btDeleteConcio = new JButton();
  JButton btDeleteSezioneAnalisi = new JButton();
  JButton jBOk = new JButton();
  JButton btAddConcio = new JButton();
  JButton btAddSezioneAnalisi = new JButton();
  JButton btAddCampata= new JButton();
  JButton btRemoveCampata = new JButton();
  JButton btModificaCampata = new JButton();
  JButton btRinominaSezioni = new JButton();
  JButton btRinominaConci = new JButton();
  JButton btAddSoletta= new JButton();
  JButton btRinominaSoletta= new JButton();
  JButton btEliminaSoletta= new JButton();
   
  
  
  
    JButton jBDisegna = new JButton();
  JPanel pButton = new JPanel();

  private ControllerAnalisi controllerAnalisi = new ControllerAnalisi();

  private static BottoniView bottoniView;
  BorderLayout borderLayout1 = new BorderLayout();

  private BottoniView() {
    super();
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public static synchronized BottoniView getInstance() {

        if (bottoniView == null) {
            bottoniView = new BottoniView();
        }
        return bottoniView;
    }


  private void jbInit() throws Exception {
    pButton.setRequestFocusEnabled(true);
    pButton.setPreferredSize(new Dimension(380, 70));
    pButton.setFont(new java.awt.Font("Dialog", 0, 11));
    jBDisegna.setText("disegna");
    jBDisegna.setMargin(new Insets(0, 2, 2, 2));
    jBDisegna.setActionCommand("DISEGNA");
    jBDisegna.setFont(new java.awt.Font("SansSerif", 0, 13));
    jBDisegna.setPreferredSize(new Dimension(60, 25));
    jBDisegna.addActionListener( controllerAnalisi );

    /*bStampa.setFont(new java.awt.Font("SansSerif", 0, 13));
    bStampa.setPreferredSize(new Dimension(60, 25));
    bStampa.setActionCommand("STAMPA");
    bStampa.setMargin(new Insets(0, 2, 2, 2));
    bStampa.setText("stampa");
    bStampa.addActionListener( controller );*/

    btAddConcio.setMargin(new Insets(0, 2, 2, 2));
    btAddConcio.setActionCommand("ADD_CONCIO");
    btAddConcio.setFont(new java.awt.Font("SansSerif", 0, 13));
    btAddConcio.setPreferredSize(new Dimension(100, 25));
    btAddConcio.setText("nuovo concio");
    btAddConcio.addActionListener( controllerAnalisi );
    
    btAddSezioneAnalisi.setMargin(new Insets(0, 2, 2, 2));
    btAddSezioneAnalisi.setActionCommand("ADD_SEZIONE_ANALISI");
    btAddSezioneAnalisi.setFont(new java.awt.Font("SansSerif", 0, 13));
    btAddSezioneAnalisi.setPreferredSize(new Dimension(100, 25));
    btAddSezioneAnalisi.setText("nuova sezione");
    btAddSezioneAnalisi.addActionListener( controllerAnalisi );
    
    
    btAddCampata.setMargin(new Insets(0, 2, 2, 2));
    btAddCampata.setActionCommand("ADD_CAMPATA");
    btAddCampata.setFont(new java.awt.Font("SansSerif", 0, 13));
    btAddCampata.setPreferredSize(new Dimension(100, 25));
    btAddCampata.setText("Aggiungi campata");
    btAddCampata.addActionListener( controllerAnalisi );
   
    btRemoveCampata.setMargin(new Insets(0, 2, 2, 2));
    btRemoveCampata.setActionCommand("REMOVE_CAMPATA");
    btRemoveCampata.setFont(new java.awt.Font("SansSerif", 0, 13));
    btRemoveCampata.setPreferredSize(new Dimension(100, 25));
    btRemoveCampata.setText("Rimuovi campata");
    btRemoveCampata.addActionListener( controllerAnalisi );
    
    btModificaCampata.setMargin(new Insets(0, 2, 2, 2));
    btModificaCampata.setActionCommand("MODIFICA_CAMPATA");
    btModificaCampata.setFont(new java.awt.Font("SansSerif", 0, 13));
    btModificaCampata.setPreferredSize(new Dimension(100, 25));
    btModificaCampata.setText("Modifica luci");
    btModificaCampata.addActionListener( controllerAnalisi );

    btRinominaSezioni.setMargin(new Insets(0, 2, 2, 2));
    btRinominaSezioni.setActionCommand("RINOMINA_SEZIONI");
    btRinominaSezioni.setFont(new java.awt.Font("SansSerif", 0, 13));
    btRinominaSezioni.setPreferredSize(new Dimension(100, 25));
    btRinominaSezioni.setText("Rinomina sezioni");
    btRinominaSezioni.addActionListener( controllerAnalisi );

    
    btRinominaConci.setMargin(new Insets(0, 2, 2, 2));
    btRinominaConci.setActionCommand("RINOMINA_CONCI");
    btRinominaConci.setFont(new java.awt.Font("SansSerif", 0, 13));
    btRinominaConci.setPreferredSize(new Dimension(100, 25));
    btRinominaConci.setText("Rinomina conci");
    btRinominaConci.addActionListener( controllerAnalisi );
    
    
    btAddSoletta.setMargin(new Insets(0, 2, 2, 2));
    btAddSoletta.setActionCommand("ADD_SOLETTA");
    btAddSoletta.setFont(new java.awt.Font("SansSerif", 0, 13));
    btAddSoletta.setPreferredSize(new Dimension(100, 25));
    btAddSoletta.setText("Aggiungi soletta");
    btAddSoletta.addActionListener( controllerAnalisi );
    
    
    btRinominaSoletta.setMargin(new Insets(0, 2, 2, 2));
    btRinominaSoletta.setActionCommand("RINOMINA_SOLETTE");
    btRinominaSoletta.setFont(new java.awt.Font("SansSerif", 0, 13));
    btRinominaSoletta.setPreferredSize(new Dimension(100, 25));
    btRinominaSoletta.setText("Rinomina solette");
    btRinominaSoletta.addActionListener( controllerAnalisi );
    
    btEliminaSoletta.setMargin(new Insets(0, 2, 2, 2));
    btEliminaSoletta.setActionCommand("ELIMINA_SOLETTA");
    btEliminaSoletta.setFont(new java.awt.Font("SansSerif", 0, 13));
    btEliminaSoletta.setPreferredSize(new Dimension(100, 25));
    btEliminaSoletta.setText("Elimina solette");
    btEliminaSoletta.addActionListener( controllerAnalisi );
    
    
    jBOk.setText("elabora");
    jBOk.setMargin(new Insets(0, 2, 2, 2));
    jBOk.setFont(new java.awt.Font("SansSerif", 0, 13));
    jBOk.setPreferredSize(new Dimension(60, 25));
    jBOk.setActionCommand("ELABORA");
    jBOk.addActionListener( controllerAnalisi );

    btDeleteConcio.setText("elimina concio");
    btDeleteConcio.setFont(new java.awt.Font("SansSerif", 0, 13));
    btDeleteConcio.setPreferredSize(new Dimension(100, 25));
    btDeleteConcio.setActionCommand("DELETE_CONCIO");
    btDeleteConcio.setMargin(new Insets(0, 2, 2, 2));
    btDeleteConcio.addActionListener( controllerAnalisi );

    btDeleteSezioneAnalisi.setText("elimina sezione analisi");
    btDeleteSezioneAnalisi.setFont(new java.awt.Font("SansSerif", 0, 13));
    btDeleteSezioneAnalisi.setPreferredSize(new Dimension(100, 25));
    btDeleteSezioneAnalisi.setActionCommand("DELETE_SEZIONE_ANALISI");
    btDeleteSezioneAnalisi.setMargin(new Insets(0, 2, 2, 2));
    btDeleteSezioneAnalisi.addActionListener( controllerAnalisi );
    
    
    
    jBSaveFile.setText("salva");
    jBSaveFile.setMargin(new Insets(0, 2, 2, 2));
    jBSaveFile.setFont(new java.awt.Font("SansSerif", 0, 13));
    jBSaveFile.setPreferredSize(new Dimension(60, 25));
    jBSaveFile.setActionCommand("SAVE");
    jBSaveFile.addActionListener( controllerAnalisi );

    jBOpen.setText("apri");
    jBOpen.setMargin(new Insets(0, 2, 2, 2));
    jBOpen.setFont(new java.awt.Font("SansSerif", 0, 13));
    jBOpen.setPreferredSize(new Dimension(60, 25));
    jBOpen.setActionCommand("OPEN");
    jBOpen.addActionListener( controllerAnalisi );

    this.setLayout(borderLayout1);
    pButton.add(jBOpen, null);
    pButton.add(jBSaveFile, null);
    pButton.add(jBDisegna, null);
    pButton.add(jBOk, null);
    //pButton.add(bStampa, null);
    pButton.add(btAddConcio, null);
    pButton.add(btAddSezioneAnalisi, null);
    pButton.add(btAddCampata,null);
    pButton.add(btRemoveCampata,null);
    pButton.add(btModificaCampata,null);
    pButton.add(btRinominaSezioni,null);
    pButton.add(btRinominaConci,null);
    pButton.add(btAddSoletta,null);
    pButton.add(btRinominaSoletta,null);
    pButton.add(btEliminaSoletta,null);
    
    pButton.add(btDeleteConcio, null);
    pButton.add(btDeleteSezioneAnalisi, null);

    this.add(pButton,  BorderLayout.CENTER);

  }


}