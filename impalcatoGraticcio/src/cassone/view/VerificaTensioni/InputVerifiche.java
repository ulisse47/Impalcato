package cassone.view.VerificaTensioni;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import cassone.controller.ControllerVerificaSezioni;
import cassone.model.Campate;
import cassone.model.Progetto;
import cassone.model.Sezione;
import cassone.model.SezioniMetalliche.SezioneMetallica;
import cassone.model.soletta.Soletta;
import cassone.view.analisi.MyTextField;

public class InputVerifiche
        extends JPanel implements ItemListener{
    
    private Hashtable listaTextField = new Hashtable();
    
    private static InputVerifiche inputPanel;
    
    private JPanel pDati = new JPanel();
    private JPanel pSezioni = new JPanel();
    private JPanel pUtil = new JPanel();
    private JScrollPane jSPinput = new JScrollPane();
    
    GridBagLayout gridBagLayout1 = new GridBagLayout();
    
    JLabel lX = new JLabel();
    MyTextField x = new MyTextField();
    
    JLabel lConcio = new JLabel();
    JComboBox cbConcio = new JComboBox();
    
    JLabel lSoletta = new JLabel();
    JComboBox cbSoletta = new JComboBox();
    
    JLabel lSezione = new JLabel();
    JComboBox cbSezione = new JComboBox();
    
    JCheckBox ckCalcoloBeff = new JCheckBox();
    JLabel lckCalcoloBeff= new JLabel();
    
    JCheckBox ckCalcoloArmatura = new JCheckBox();
    JLabel lckCalcoloArmatura= new JLabel();
    
    JComboBox cbCampata = new JComboBox();
    JLabel lCbCampata = new JLabel();
    
    JLabel lAf = new JLabel();
    MyTextField Af = new MyTextField();
    
    JLabel ldiametro = new JLabel();
    MyTextField diametro = new MyTextField();
    
    JLabel lpassoBarre = new JLabel();
    MyTextField passoBarre = new MyTextField();
    
    
    private ControllerVerificaSezioni controller = new ControllerVerificaSezioni();
    
    public InputVerifiche() {
        super();
        try {
            jbInit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public JComboBox getComboSezione() {
        return cbSezione;
    }
    
    public JComboBox getComboConcio() {
        return cbConcio;
    }
    
    public JComboBox getComboSoletta() {
        return cbSoletta;
    }
    
    public JCheckBox getCkCalcoloAutomatico() {
        return ckCalcoloBeff;
    }
    
    public JCheckBox getCkCalcoloArmatura() {
        return ckCalcoloArmatura;
    }
    
    // public JCheckBox getCkInstabilitaEc3() {
//	    return ckInstabilitaEc3;
//	  }
    
    public JComboBox getComboCampata(){
        return cbCampata;
        
    }
    
    public void setEnableTextBoxCalcoloAutomatico(boolean automatico){
        
        cbCampata.setEnabled(automatico);
    }
    
    
    
    //calcolo automatico armatura
    public void setEnableTextBoxCalcoloArmatura(boolean automatico){
        
        Af.setEnabled(!automatico);
        passoBarre.setEnabled(automatico);
        diametro.setEnabled(automatico);
    }
    
    /**
     * @return
     */
    public static synchronized InputVerifiche getInstance() {
        if (inputPanel == null) {
            inputPanel = new InputVerifiche();
        }
        return inputPanel;
    }
    
    public Iterator getListaFieldNames() {
        return listaTextField.keySet().iterator();
    }
    
    public String getValue(String name) {
        MyTextField field = (MyTextField) listaTextField.get(name);
        if(name == "x" || name == "Af" || name == "DiametroArmatura" ||name == "PassoArmatura" ){
            try {
                Double.parseDouble(field.getText());
                field.setBackground(Color.WHITE);
            } catch (Exception e) {
                field.setBackground(Color.RED);
            }
            
        }
        return field.getText();
    }
    
    public void setValue(String name, String value) {
        Object obj = listaTextField.get(name);
        if (obj instanceof MyTextField) {
            MyTextField field = (MyTextField) listaTextField.get(name);
            field.setText(value);
        }
    }
    
    
  /*
   *
   * @param current_concio
   * @param nConci
   */
    public void resetCombo(JComboBox cb, Object current, int n) {
        if ( n == 0 ) return;
        cb.removeActionListener(controller);
        cb.removeAllItems();
        for (int i = 0; i < n; i++) {
            cb.addItem(new Integer(i + 1));
        }
        cb.setSelectedItem(current);
        cb.addActionListener(controller);
    }
    
    public void resetComboSez( Object current_sezione, ArrayList<Sezione> nSezioni ){
        cbSezione.removeActionListener( controller );
        cbSezione.removeAllItems();
        for ( int i = 0; i<nSezioni.size(); i++ ){
            cbSezione.addItem( nSezioni.get(i) );
        }
        cbSezione.setSelectedItem( current_sezione );
        cbSezione.addActionListener( controller );
        
    }
    
    
    public void resetComboConcio( Object current_concio, ArrayList<SezioneMetallica>nConci ){
        cbConcio.removeActionListener( controller );
        cbConcio.removeAllItems();
        for ( int i = 0; i<nConci.size(); i++ ){
            cbConcio.addItem( nConci.get(i) );
        }
        cbConcio.setSelectedItem( current_concio );
        cbConcio.addActionListener( controller );
    }
    
    public void resetComboSoletta( Object curSol, ArrayList<Soletta>sol){
        cbSoletta.removeActionListener( controller );
        cbSoletta.removeAllItems();
        for ( int i = 0; i<sol.size(); i++ ){
            cbSoletta.addItem( sol.get(i) );
        }
        cbSoletta.setSelectedItem( curSol );
        cbSoletta.addActionListener( controller );
    }
    
    
    /**
     *
     */
    
    /**
     *
     */
    private void addComponents() {
        
        addComponent( pSezioni, "sezione", "Sezione", lSezione, cbSezione, 0, 0);
        addComponent( pSezioni , "concio", "Concio metallico", lConcio, cbConcio, 0, 1);
        addComponent( pSezioni , "soletta", "Soletta in c.a.", lSoletta, cbSoletta, 0, 2);
        addComponent( pSezioni, "x", "Ascissa di verifica sezione: x(m)", lX,x, 0, 3);
        addComponent(pSezioni, "Af", "Area armatura: A(mm²)", lAf, Af, 0, 4);
        
        //CHEK CALCOLO AUTOMATICO DI BEFF
        addComponent(pUtil, "CalcoloAutomatico", "Calcolo automatico di Beff", lckCalcoloBeff, ckCalcoloBeff, 0,
                0);
        //   addComponent(pUtil, "b0", "Larghezza zona ancorata: b0(mm) ", lb0, b0, 0,
        //           1);
        //   addComponent(pUtil, "b1", "Larghezza ala sx: b1(mm)", lb1, b1, 0,
        //           2);
        //   addComponent(pUtil, "b2", "Larghezza ala dx: b2(mm)", lb2, b2, 0,
        //           3);
        addComponent(pUtil, "campata", "Campata numero", lCbCampata, cbCampata, 0,
                1);
        //CHEK CALCOLO AUTOMATICO DI ARMATURA
        
        addComponent(pUtil, "CalcoloArmatura", "Calcolo automatico armatura", lckCalcoloArmatura, ckCalcoloArmatura, 0,
                2);
        addComponent(pUtil, "DiametroArmatura", "Diametro barre: D(mm)", ldiametro, diametro, 0,
                3);
        addComponent(pUtil, "PassoArmatura", "Passo barre: p(cm)", lpassoBarre, passoBarre, 0,
                4);
        
    }
    
    
    
    
    /**
     * @param title
     * @param lBs1
     * @param bs1
     * @param gridx
     * @param gridy
     */
    private void addComponent(JPanel pInput, String fieldName, String title,
            JLabel lBs1,
            JComponent bs1,
            int gridx, int gridy) {
        
        listaTextField.put(fieldName, bs1);
        
        lBs1.setHorizontalAlignment(SwingConstants.LEFT);
        lBs1.setText(title);
        //lBs1.setPreferredSize(new Dimension(280, 15));
        
        bs1.setPreferredSize(new Dimension(70, 21));
        
        
        pInput.add(lBs1, new GridBagConstraints(gridx, gridy, 1, 1, 0.0, 0.0
                , GridBagConstraints.LINE_START,
                GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 9, 0));
        pInput.add(bs1, new GridBagConstraints(gridx + 1, gridy, 1, 1, 0.0, 0.0
                , GridBagConstraints.LINE_START,
                GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
    }
    
    /**
     *
     * @throws java.lang.Exception
     */
    void jbInit() throws Exception {
        
        TitledBorder titledBorderSezioni = new TitledBorder(new EtchedBorder(
                EtchedBorder.RAISED, Color.white, Color.gray), "DATI SEZIONE VERIFICA");
        
        TitledBorder titledBorderUtil = new TitledBorder(new EtchedBorder(
                EtchedBorder.RAISED, Color.white, Color.gray), "UTILITY");
        
        this.setLayout( new BorderLayout() );
        
        pSezioni.setLayout( new GridBagLayout() );
        pSezioni.setBorder( titledBorderSezioni );
        
        pUtil.setLayout(new GridBagLayout());
        pUtil.setBorder( titledBorderUtil );
        
        
        addComponents();
        
        pDati.setLayout( new BorderLayout() );
        pDati.add(pSezioni, BorderLayout.NORTH );
        pDati.add(pUtil, BorderLayout.CENTER );
        
        jSPinput.getViewport().add(pDati, null);
        jSPinput.setWheelScrollingEnabled(true);
        
        
        this.add(jSPinput, BorderLayout.CENTER);
        
        //combo sezione
        cbSezione.addActionListener(controller);
        cbSezione.setActionCommand( "SET_SEZIONE");
        
        //combo concio
        cbConcio.setActionCommand("SET_CONCIO");
        cbConcio.addActionListener(controller);
        
        //combo soletta
        cbSoletta.setActionCommand("SET_SOLETTA");
        cbSoletta.addActionListener(controller);
        
        ckCalcoloBeff.addItemListener( this );
        ckCalcoloBeff.setActionCommand("CALCOLO_AUTOMATICO_B");
        
        ckCalcoloArmatura.addItemListener( this);
        ckCalcoloArmatura.setActionCommand("CALCOLO_AUTOMATICO_ARMATURA");
        
    }
    
    /**
     *
     * @param
     * @param
     */
    public void resetComboCampate(){
        Progetto prg = Progetto.getInstance();
        Campate cm = prg.getCampate();
        Sezione sez = prg.getCurrentSezioneVerifica();
        
        cbCampata.removeActionListener( controller );
        cbCampata.removeAllItems();
        
        for ( int i = 0; i<cm.getNCampate(); i++ ){
            cbCampata.addItem( new Integer( i+1 ) );
        }
        
        cbCampata.setSelectedIndex(sez.getNCampata());
        cbCampata.addActionListener( controller );
        
    }
    
    public void itemStateChanged(ItemEvent e) {
        
        Object source = e.getItemSelectable();
        JCheckBox ck = null;
        
        if (source.getClass() == JCheckBox.class ) {
            ck = (JCheckBox)source;
            if ( ck.getActionCommand() == "CALCOLO_AUTOMATICO_B") {
                setEnableTextBoxCalcoloAutomatico(ck.isSelected());
            }
        }
        if ( ck.getActionCommand() == "CALCOLO_AUTOMATICO_ARMATURA") {
            setEnableTextBoxCalcoloArmatura(ck.isSelected());
            
        }
    }
}
