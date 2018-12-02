package cassone.view.analisi;

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

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import cassone.controller.ControllerAnalisi;
import cassone.inputPanel.PannelloConcioCassoneII;
import cassone.inputPanel.PannelloConcioCassone;
import cassone.inputPanel.PannelloConcioDoppioT;
import cassone.inputPanel.PannelloConcioGenerico;
import cassone.inputPanel.PannelloSolettaT;
import cassone.inputPanel.PannelloSolettaType2;
import cassone.model.Campate;
import cassone.model.Progetto;
import cassone.model.Sezione;
import cassone.model.SezioniMetalliche.SezioneMetallica;
import cassone.model.SezioniMetalliche.SezioneMetallicaCassone;
import cassone.model.SezioniMetalliche.SezioneMetallicaCassoneII;
import cassone.model.SezioniMetalliche.SezioneMetallicaDoppioT;
import cassone.model.SezioniMetalliche.SezioneMetallicaGenerica;
import cassone.model.soletta.Soletta;
import cassone.model.soletta.SolettaT;
import cassone.model.soletta.SolettaType2;
import cassone.model.soletta.SolettaType2_2;

public class InputView extends JPanel implements ItemListener{
    
    private Hashtable listaTextField = new Hashtable();
    
    private static InputView inputPanel;
    
    BorderLayout borderLayout1 = new BorderLayout();
    
    private JPanel pDati = new JPanel();
    
    private JPanel pSezioni = new JPanel();
    
    private JPanel pInput = new JPanel();
    
    private JScrollPane pTot = new JScrollPane();
    
    private JPanel pUtil = new JPanel();
    
    private JPanel pTitolo = new JPanel();
    
    private JPanel jSPinput = new JPanel();
    
    // private JScrollPane jSPsezioni = new JScrollPane();
    
    BorderLayout borderLayout2 = new BorderLayout();
    
    JTextField tbTitoloOpera = new MyTextField();
    
    GridBagLayout gridBagLayout6 = new GridBagLayout();
    
    TitledBorder titledBorderTitolo;
    
    TitledBorder titledBorderSezione;
    
    TitledBorder titledBorderPropConcio;
    
    TitledBorder titledBorderUtility;
    
    GridBagLayout gbInput = new GridBagLayout();
    
    GridBagLayout gbSezione = new GridBagLayout();
    
    JCheckBox ckCalcoloBeff = new JCheckBox();
    
    JComboBox cbCampata = new JComboBox();
    
    JLabel lCbCampata = new JLabel();
    
    JLabel lAf = new JLabel();
    
    JTextField Af = new MyTextField();
    
    JComboBox cbConcio = new JComboBox();
    
    JLabel lCbConcio = new JLabel();
    
    JComboBox cbSezioneAnalisi = new JComboBox();
    
    JLabel lcbSezioneSoletta = new JLabel();
    
    JComboBox cbSezioneSoletta = new JComboBox();
    
    JLabel lCbSezAnalisi = new JLabel();
    
    JLabel ldiametro = new JLabel();
    
    JTextField diametro = new MyTextField();
    
    JLabel lpassoBarre = new JLabel();
    
    JTextField passoBarre = new MyTextField();
    
    JCheckBox ckCalcoloArmatura = new JCheckBox();
    
    JLabel lckCalcoloArmatura = new JLabel();
    
    JPanel soletta=null ;
    
    JPanel concio=null;
    
    private ControllerAnalisi controllerAnalisi;
    
    JLabel lN1 = new JLabel();
    
    JTextField tN1 = new MyTextField();
    
    JLabel lN2 = new JLabel();
    
    JTextField tN2 = new MyTextField();
    
    JLabel lN3 = new JLabel();
    
    JTextField tN3 = new MyTextField();
    
    JLabel lN4 = new JLabel();
    
    JTextField tN4 = new MyTextField();
    
    JButton bStampa = new JButton();
    
    JButton btDeleteConcio = new JButton();
    
    JButton btDeleteSezioneAnalisi = new JButton();
    
    JButton btModificaCampate = new JButton();
    
    public String getN1() {
    
        try {
            Double.parseDouble(tN1.getText());
            tN1.setBackground(Color.WHITE);
        } catch (Exception e) {
            tN1.setBackground(Color.RED);
        }

        return tN1.getText();
    }
    
      
    public String getN2() {
        try {
            Double.parseDouble(tN2.getText());
            tN2.setBackground(Color.WHITE);
        } catch (Exception e) {
            tN2.setBackground(Color.RED);
        }
        return tN2.getText();
    }
    
    public String getN3() {
        try {
            Double.parseDouble(tN3.getText());
            tN3.setBackground(Color.WHITE);
        } catch (Exception e) {
            tN3.setBackground(Color.RED);
        }
        
        return tN3.getText();
    }
    
    public String getN4() {
        try {
            Double.parseDouble(tN4.getText());
            tN4.setBackground(Color.WHITE);
        } catch (Exception e) {
            tN4.setBackground(Color.RED);
        }
        return tN4.getText();
    }
    
    public void setN1(String text) {
        tN1.setText(text);
    }
    
    public void setN2(String text) {
        tN2.setText(text);
    }
    
    public void setN3(String text) {
        tN3.setText(text);
    }
    
    public void setN4(String text) {
        tN4.setText(text);
    }
    /**
     *
     */
    private InputView() {
        try {
            jbInit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public JComboBox getComboConcio() {
        return cbConcio;
    }
    
    public JComboBox getComboCampata() {
        return cbCampata;
    }
    
    public JCheckBox getCkCalcoloBeff() {
        return ckCalcoloBeff;
    }
    
    
    public JCheckBox getCkCalcoloArmautr() {
        return ckCalcoloArmatura;
    }
    
    public JComboBox getComboSezione() {
        return cbSezioneAnalisi;
    }
    
    public JComboBox cbSezioneSoletta() {
        return cbSezioneAnalisi;
    }
    
    /**
     *
     * @param current_concio
     * @param nConci
     */
    public void resetComboConcio(Object current_concio, ArrayList<SezioneMetallica> nConci) {
        cbConcio.removeActionListener(controllerAnalisi);
        cbConcio.removeAllItems();
        for (int i = 0; i < nConci.size(); i++) {
            cbConcio.addItem(nConci.get(i));
        }
        cbConcio.setSelectedItem(current_concio);
        cbConcio.addActionListener(controllerAnalisi);
        
    }
    
    /**
     *
     * @param current_concio
     * @param nConci
     */
    public void resetComboSoletta(Object current_soletta, ArrayList<Soletta> nsoletta) {
        cbSezioneSoletta.removeActionListener(controllerAnalisi);
        cbSezioneSoletta.removeAllItems();
        for (int i = 0; i < nsoletta.size(); i++) {
            cbSezioneSoletta.addItem(nsoletta.get(i));
        }
        cbSezioneSoletta.setSelectedItem(current_soletta);
        cbSezioneSoletta.addActionListener(controllerAnalisi);
        
    }
    
    
    /**
     *
     * @param
     * @param
     */
    public void resetComboCampate() {
        Progetto prg = Progetto.getInstance();
        Campate cm = prg.getCampate();
        Sezione sez = prg.getCurrentSezioneAnalisi();
        
        cbCampata.removeActionListener(controllerAnalisi);
        cbCampata.removeAllItems();
        
        for (int i = 0; i < cm.getNCampate(); i++) {
            cbCampata.addItem(new Integer(i + 1));
        }
        
        cbCampata.setSelectedIndex(sez.getNCampata());
        cbCampata.addActionListener(controllerAnalisi);
        
    }
    
    /**
     *
     * @param current_concio
     * @param nConci
     */
    public void resetComboSezioneAnalisi(Object current_sezione,
            ArrayList<Sezione> nSezioni) {
        cbSezioneAnalisi.removeActionListener(controllerAnalisi);
        cbSezioneAnalisi.removeAllItems();
        for (int i = 0; i < nSezioni.size(); i++) {
            cbSezioneAnalisi.addItem(nSezioni.get(i));
        }
        cbSezioneAnalisi.setSelectedItem(current_sezione);
        cbSezioneAnalisi.addActionListener(controllerAnalisi);
        
    }
    
    public void reset() {
        Iterator iter = listaTextField.values().iterator();
        while (iter.hasNext()) {
            ((JTextField) iter.next()).setText("");
        }
    }
    
    public void setTitolo(String titolo) {
        tbTitoloOpera.setText(titolo);
    }
    
    /**
     * @return
     */
    public static synchronized InputView getInstance() {
        if (inputPanel == null) {
            inputPanel = new InputView();
        }
        return inputPanel;
    }
    
    void jbInit() throws Exception {
        controllerAnalisi = new ControllerAnalisi();
        
        titledBorderTitolo = new TitledBorder(new EtchedBorder(
                EtchedBorder.RAISED, Color.white, Color.gray), "DATI GENERALI");
        
        titledBorderSezione = new TitledBorder(new EtchedBorder(
                EtchedBorder.RAISED, Color.white, Color.gray), "DATI SEZIONE");
        
        titledBorderUtility = new TitledBorder(new EtchedBorder(
                EtchedBorder.RAISED, Color.white, Color.gray), "UTILITY");
        
        titledBorderPropConcio = new TitledBorder(new EtchedBorder(
                EtchedBorder.RAISED, Color.white, Color.gray),
                "PROPRIETA' CONCIO METALLICO");
        
        this.setLayout(borderLayout1);
        
        pDati.setLayout(borderLayout2);
        pDati.setFont(new java.awt.Font("Dialog", 0, 10));
        // pDati.setPreferredSize(new Dimension(294, 200));
        
        pTitolo.setBorder(titledBorderTitolo);
        // pTitolo.setPreferredSize(new Dimension(393, 80));
        pTitolo.setLayout(gridBagLayout6);
        
        pInput.setLayout(gbInput);
        pInput.setBorder(titledBorderPropConcio);
        
        pSezioni.setLayout(gbSezione);
        pSezioni.setBorder(titledBorderSezione);
        pSezioni.setMinimumSize(new Dimension(100,300));
        
        pUtil.setLayout(new GridBagLayout());
        pUtil.setBorder(titledBorderUtility);
        
        // CheckBox
        ckCalcoloBeff.addItemListener(this);
        ckCalcoloBeff.setActionCommand("CALCOLO_AUTOMATICO_B");
        
        ckCalcoloArmatura.addItemListener(this);
        ckCalcoloArmatura.setActionCommand("CALCOLO_AUTOMATICO_ARMATURA");
        
        // COMBO
        cbConcio.setActionCommand("SET_CONCIO");
        cbConcio.addActionListener(controllerAnalisi);
        
        cbSezioneAnalisi.setActionCommand("SET_SEZIONE_ANALISI");
        cbSezioneAnalisi.addActionListener(controllerAnalisi);
        
        cbSezioneSoletta.setActionCommand("SET_SOLETTA");
        cbSezioneSoletta.addActionListener(controllerAnalisi);
        
        
        // cbConcio.setMaximumSize(new Dimension( 80, 20));
        int w = 20;
        int h = 20;
        
        tbTitoloOpera.setBorder(BorderFactory.createEtchedBorder());
        tbTitoloOpera.setMinimumSize(new Dimension(120, h ));
        tbTitoloOpera.setPreferredSize(new Dimension(200, h));
        tbTitoloOpera.setText("");
        
        lN1.setMinimumSize(new Dimension(w, h));
        lN1.setPreferredSize(new Dimension(w, h));
        lN1.setText("n1");
        tN1.setMinimumSize(new Dimension(w, w));
        tN1.setPreferredSize(new Dimension(w, h));
        tN1.setSelectionStart(11);
        tN1.setText("22");
        lN2.setMinimumSize(new Dimension(w, h));
        lN2.setPreferredSize(new Dimension(w, h));
        lN2.setText("n2");
        tN2.setMinimumSize(new Dimension(w, h));
        tN2.setPreferredSize(new Dimension(w, h));
        tN2.setText("6.24");
//		tN2.addActionListener(new InputView_tN2_actionAdapter(this));
        lN3.setMinimumSize(new Dimension(w, h));
        lN3.setPreferredSize(new Dimension(w, h));
        lN3.setText("n3");
        tN3.setMinimumSize(new Dimension(w, h));
        tN3.setPreferredSize(new Dimension(w, h));
        tN3.setText("14.2");
        tN4.setMinimumSize(new Dimension(w, h));
        tN4.setPreferredSize(new Dimension(w, h));
        tN4.setText("16.0");
        lN4.setMinimumSize(new Dimension(w, h));
        lN4.setPreferredSize(new Dimension(w, h));
        lN4.setText("n4");
        
        JLabel lTitoloOpera = new JLabel();
        lTitoloOpera.setText("TITOLO");
        lTitoloOpera.setMinimumSize(new Dimension( 150 , h ) );
        
        pTitolo.add(lTitoloOpera, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(
                0, 0, 0, 0), 5, 0));
        
        pTitolo.add(tbTitoloOpera, new GridBagConstraints(1, 0, 8, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(
                5, 0, 5, 20), 10, 0));
        
        pTitolo.add(lN1, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(
                0, 0, 0, 5), 0, 0));
        pTitolo.add(tN1, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
                0, 0, 20), 10, 0));
        pTitolo.add(lN2, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(
                0, 0, 0, 5), 0, 0));
        pTitolo.add(tN2, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(
                0, 0, 0, 20), 10, 0));
        pTitolo.add(lN3, new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 5), 0, 0));
        pTitolo.add(tN3, new GridBagConstraints(5, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,0, 0, 20), 10, 0));
        pTitolo.add(lN4, new GridBagConstraints(6, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 5), 0, 0));
        pTitolo.add(tN4, new GridBagConstraints(7, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,0, 0, 20), 10, 0));
        
        addComponents();
        soletta = new PannelloSolettaT();
        concio = new PannelloConcioDoppioT();
        
        pDati.add(soletta, BorderLayout.NORTH);
        pDati.add(concio, BorderLayout.CENTER);
        pDati.add(pUtil, BorderLayout.SOUTH);
        
        
        jSPinput.setLayout(new BorderLayout());
        
        jSPinput.add(pTitolo, BorderLayout.NORTH);
        jSPinput.add(pSezioni, BorderLayout.CENTER);
        jSPinput.add(pDati, BorderLayout.SOUTH);
//		jSPinput.setWheelScrollingEnabled(true);
        
        this.add(jSPinput);

        ckCalcoloArmatura.setSelected(false);
        ckCalcoloBeff.setSelected(false);
        setEnableTextBoxCalcoloArmatura(false);
        setEnableTextBoxCalcoloAutomaticoBeff(false);

    }
    
    void imposta() throws Exception {
        
        pDati.removeAll();
//		jSPinput.removeAll();
        
        pDati.add(soletta, BorderLayout.NORTH);
        pDati.add(concio, BorderLayout.CENTER);
        pDati.add(pUtil, BorderLayout.SOUTH);
        
        jSPinput.add(pTitolo, BorderLayout.NORTH);
        jSPinput.add(pSezioni, BorderLayout.CENTER);
        jSPinput.add(pDati, BorderLayout.SOUTH);
//		jSPinput.setWheelScrollingEnabled(true);
        
        this.add(jSPinput);
        
/*		pDati.add(soletta, BorderLayout.NORTH);
                pDati.add(concio, BorderLayout.CENTER);
                pDati.add(pUtil, BorderLayout.SOUTH);
 
                jSPinput.getViewport().add(pDati, null);
                jSPinput.setWheelScrollingEnabled(true);
 
                this.add(pTitolo, BorderLayout.NORTH);
                this.add(pSezioni, BorderLayout.CENTER);
                this.add(jSPinput, BorderLayout.SOUTH);
 */
        this.repaint();
        
    }
    
    
    
    /**
     *
     */
    private void addComponents() {
        
        // /COMBO SEZIONE
        addComponent( "sezione", "Sezione", lCbSezAnalisi, cbSezioneAnalisi, 0,
                0, pSezioni /*100, 21*/ );
        
        // COMBO CONCIO
        addComponent("Concio_metallico", "Concio metallico", lCbConcio,
                cbConcio, 0, 1, pSezioni);
        
        addComponent("Soletta", "Soletta", lcbSezioneSoletta,
                cbSezioneSoletta, 0, 2, pSezioni);
        
        addComponent("Af", "Area barre in acciaio: Af (mm²)", lAf, Af, 0, 3,
                pSezioni);
        
        // ///UTILITY
        // CHEK CALCOLO AUTOMATICO DI BEFF
        addComponent("beff", "Calcolo automatico di Beff", new JLabel(),
                ckCalcoloBeff, 0, 0, pUtil);
        
        // combo campata calcolo automatico
        addComponent("campata", "Campata", lCbCampata, cbCampata, 0, 1, pUtil);
        
        // cbCalcoloArmatura
        addComponent("calcolo_automatico_armatura",
                "Calcolo automatico armatura", new JLabel(), ckCalcoloArmatura,
                0, 2, pUtil);
        
        addComponent("DiametroArmatura", "Diametro barre: D (mm)", ldiametro,
                diametro, 0, 3, pUtil);
        
        addComponent("PassoArmatura", "Passo barre: p (cm)", lpassoBarre,
                passoBarre, 0, 4, pUtil);
    }
    
    private void addComponent(String fieldName, String title, JLabel lBs1,
            JTextField bs1, int gridx, int gridy, JPanel panel, int width, int height ) {
        
        listaTextField.put(fieldName, bs1);
        
        lBs1.setHorizontalAlignment(SwingConstants.LEFT);
        lBs1.setText(title);
        lBs1.setPreferredSize(new Dimension(230, 15));
        
        bs1.setPreferredSize( new Dimension( width , height ) );
        bs1.setHorizontalAlignment(SwingConstants.RIGHT);
        
        panel.add(lBs1, new GridBagConstraints(gridx, gridy, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(
                0, 0, 0, 0), 9, 0));
        panel.add(bs1, new GridBagConstraints(gridx + 1, gridy, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(
                0, 0, 0, 0), 0, 0));
    }
    
    private void addComponent( String fieldName, String title, JLabel lBs1,
            JCheckBox bs1, int gridx, int gridy, JPanel panel, int width, int height  ) {
        
        listaTextField.put(fieldName, bs1);
        
        lBs1.setHorizontalAlignment(SwingConstants.LEFT);
        lBs1.setText(title);
        lBs1.setPreferredSize(new Dimension(230, 15));
        
        bs1.setPreferredSize(new Dimension( width, height ));
        bs1.setHorizontalAlignment(SwingConstants.RIGHT);
        
        panel.add(lBs1, new GridBagConstraints(gridx, gridy, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(
                0, 0, 0, 0), 9, 0));
        panel.add(bs1, new GridBagConstraints(gridx + 1, gridy, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(
                0, 0, 0, 0), 0, 0));
    }
    
    
    private void addComponent(String fieldName, String title, JLabel lBs1,
            JComboBox bs1, int gridx, int gridy, JPanel panel, int width, int height ) {
        
        listaTextField.put(fieldName, bs1);
        
        lBs1.setHorizontalAlignment(SwingConstants.LEFT);
        lBs1.setText(title);
        lBs1.setPreferredSize(new Dimension(230, 15));
        
        bs1.setPreferredSize(new Dimension( width, height ));
        // bs1.setHorizontalAlignment(SwingConstants.RIGHT);
        
        panel.add(lBs1, new GridBagConstraints(gridx, gridy, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(
                0, 0, 0, 0), 9, 0));
        panel.add(bs1, new GridBagConstraints(gridx + 1, gridy, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(
                0, 0, 0, 0), 0, 0));
    }
    
    
    /**
     * @param title
     * @param lBs1
     * @param bs1
     * @param gridx
     * @param gridy
     */
    private void addComponent(String fieldName, String title, JLabel lBs1,
            JTextField bs1, int gridx, int gridy, JPanel panel) {
        
        addComponent( fieldName, title, lBs1, bs1, gridx, gridy, panel, 58, 21 );
        
    }
    
    private void addComponent(String fieldName, String title, JLabel lBs1,
            JCheckBox bs1, int gridx, int gridy, JPanel panel ) {
        
        addComponent( fieldName, title, lBs1, bs1, gridx, gridy, panel, 58, 21 );
    }
    
    private void addComponent(String fieldName, String title, JLabel lBs1,
            JComboBox bs1, int gridx, int gridy, JPanel panel) {
        
        addComponent( fieldName, title, lBs1, bs1, gridx, gridy, panel, 58, 21 );
        
    }
    
    public Iterator getListaFieldNames() {
        return listaTextField.keySet().iterator();
    }
    
    public String getValue(String name) {
        JTextField field = (JTextField) listaTextField.get(name);

        if(name=="Af" || name=="DiametroArmatura" || name=="PassoArmatura"){
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
        JTextField field = (JTextField) listaTextField.get(name);
        field.setText(value);
    }
    
    public void setEnableTextBoxCalcoloAutomaticoBeff(boolean automatico) {

        
        if(soletta.getClass()==PannelloSolettaT.class){
            PannelloSolettaT st = (PannelloSolettaT)soletta;
            st.b0.setEnabled(automatico);
        }
        if(soletta.getClass()==PannelloSolettaType2.class){
            PannelloSolettaType2 st = (PannelloSolettaType2)soletta;
            st.bo.setEditable(automatico);
        }
        cbCampata.setEnabled(automatico);
        
    }
    
    // calcolo automatico armatura
    public void setEnableTextBoxCalcoloArmatura(boolean automatico) {
        
         Af.setEnabled(!automatico);
            passoBarre.setEnabled(automatico);
            diametro.setEnabled(automatico);
    }
    
    public void setNomeOpera(String nome) {
        tbTitoloOpera.setText(nome);
    }
    
    public String getNomeOpera() {
        return tbTitoloOpera.getText();
    }
    
    
    
//	public void repaint(){
    //	solet
//	}
    
    public void aggiornaPannelli(Sezione sez ){
        JPanel pn=null;
        Soletta sm = sez.getSoletta();
        if(sm.getClass()==SolettaT.class){
            pn = new PannelloSolettaT();
        } else if (sm.getClass()==SolettaType2.class){
            pn = new PannelloSolettaType2();
        } else if (sm.getClass()==SolettaType2_2.class){
            pn = new PannelloSolettaType2();
        }
        setSoletta(pn);
        
        JPanel pn2=null;
        SezioneMetallica sc=sez.getSezioneMetallica();
        if(sc.getClass()==SezioneMetallicaDoppioT.class){
            pn2 = new PannelloConcioDoppioT();
        } else if (sc.getClass()==SezioneMetallicaCassone.class){
            pn2 = new PannelloConcioCassone();
        } else if (sc.getClass()==SezioneMetallicaCassoneII.class){
            pn2 = new PannelloConcioCassoneII();
        } else if (sc.getClass()==SezioneMetallicaGenerica.class){
            pn2 = new PannelloConcioGenerico();
        }
        setConcio(pn2);
        
    }
    
    public JPanel getConcio() {
        return concio;
    }
    
    public void setConcio(JPanel concio) {
        this.concio = concio;
        try {
            imposta();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public JPanel getSoletta() {
        return soletta;
    }
    
    public void setSoletta(JPanel soletta) {
        this.soletta = soletta;
        try {
            imposta();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    public void itemStateChanged(ItemEvent e) {
        
        Object source = e.getItemSelectable();
        JCheckBox ck = null;
        
        if (source.getClass() == JCheckBox.class ) {
            ck = (JCheckBox)source;
            if ( ck.getActionCommand() == "CALCOLO_AUTOMATICO_B") {
                setEnableTextBoxCalcoloAutomaticoBeff(ck.isSelected());
            }
        }
        if ( ck.getActionCommand() == "CALCOLO_AUTOMATICO_ARMATURA") {
             setEnableTextBoxCalcoloArmatura(ck.isSelected());
            
        }
    }
}
