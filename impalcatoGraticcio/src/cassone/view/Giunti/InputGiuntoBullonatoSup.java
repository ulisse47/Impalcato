package cassone.view.Giunti;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Hashtable;
import java.util.Iterator;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import cassone.controller.ControllerVerificaGiunti;
import cassone.view.analisi.MyTextField;

public class InputGiuntoBullonatoSup extends JPanel {
    
    private Hashtable listaTextField = new Hashtable();
    
    private static InputGiuntoBullonatoSup inputPanel;
    
    GridBagLayout gridBagLayout1 = new GridBagLayout();
    
    JLabel le1aSup = new JLabel();
    MyTextField e1aSup = new MyTextField();
    
    JLabel le1bSup = new JLabel();
    MyTextField e1bSup = new MyTextField();
    
    JLabel le2aSup = new JLabel();
    MyTextField e2aSup = new MyTextField();
    
    JLabel le2bSup = new JLabel();
    MyTextField e2bSup = new MyTextField();
    
    JLabel lp1Sup = new JLabel();
    MyTextField p1Sup = new MyTextField();
    
    JLabel lp2Sup = new JLabel();
    MyTextField p2Sup = new MyTextField();
    
    JLabel lnfile1Sup = new JLabel();
    MyTextField nfile1Sup = new MyTextField();
    
    JLabel lnfile2Sup = new JLabel();
    MyTextField nfile2Sup = new MyTextField();
    
    JLabel lDiamBsup = new JLabel();
    JComboBox DiamBsup = new JComboBox();
    
    JLabel lDiamForoSup = new JLabel();
    MyTextField DiamForoSup = new MyTextField();
    
    JLabel ltgSup = new JLabel();
    MyTextField tgSup = new MyTextField();
    
    private ControllerVerificaGiunti controller = new ControllerVerificaGiunti();
    
    public InputGiuntoBullonatoSup() {
        
        try {
            jbInit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    
    /**
     * @return
     */
    public static synchronized InputGiuntoBullonatoSup getInstance() {
        if (inputPanel == null) {
            inputPanel = new InputGiuntoBullonatoSup();
        }
        return inputPanel;
    }
    
    public Iterator getListaFieldNames() {
        return listaTextField.keySet().iterator();
    }
    
    public String getValue(String name) {
        JTextField field = (JTextField) listaTextField.get(name);
        if(name == "e1aSup" || name == "e1bSup" || name == "e2aSup" ||name == "e2bSup"||
                name == "p1Sup"||name == "p2Sup" ||name == "tgSup" ){
            try {
                Double.parseDouble(field.getText());
                field.setBackground(Color.WHITE);
            } catch (Exception e) {
                field.setBackground(Color.RED);
            }
        }
        if(name == "nFile1Sup" || name == "nFile2Sup" || name == "diamForoSup" ){
            try {
                Integer.parseInt(field.getText());
                field.setBackground(Color.WHITE);
            } catch (Exception e) {
                field.setBackground(Color.RED);
            }
        }
        
        return field.getText();
        
    }
    
    public void setValue(String name, String value) {
        Object obj = listaTextField.get(name);
        if (obj instanceof JTextField) {
            JTextField field = (JTextField) listaTextField.get(name);
            field.setText(value);
        }
    }
    public int getDiametroBullone(){
        return (Integer)DiamBsup.getSelectedItem();
    }
        /*
         *
         * @param current_concio @param nConci
         */
    public void resetCombo(JComboBox cb, String _current) {
        
        int current;
        try {
            current = Integer.parseInt(_current);
        } catch (NumberFormatException ex) {
            current = 0;
        }
        cb.removeActionListener(controller);
        cb.removeAllItems();
        cb.setSelectedIndex(current);
        cb.addActionListener(controller);
    }
    
    
    /**
     *
     */
    private void addComponents() {
        
        //JPanel pAlaSup = new JPanel( new GridBagLayout() );
        this.setBorder( new TitledBorder(new EtchedBorder(
                EtchedBorder.RAISED, Color.white, Color.gray), "ALA SUPERIORE" ));
        
//		addComponent(this, "sezione", "Sezione", lSezione, cbSezione, 0, 0);
        addComponent(this, "e1aSup", "Distanza dal bordo e1a mm", le1aSup, e1aSup, 0, 1);
        addComponent(this, "e1bSup", "Distanza dal bordo e1b mm", le1bSup, e1bSup, 0, 2);
        addComponent(this, "e2aSup", "Distanza dal bordo e2a mm", le2aSup, e2aSup, 0, 3);
        addComponent(this, "e2bSup", "Interasse e2b mm", le2bSup, e2bSup, 0, 4);
        addComponent(this, "p1Sup", "Interasse p1 mm", lp1Sup, p1Sup, 0, 5);
        addComponent(this, "p2Sup", "Interasse p2 mm", lp2Sup, p2Sup, 0, 6);
        addComponent(this, "nFile1Sup", "numero file 1", lnfile1Sup, nfile1Sup, 0, 7);
        addComponent(this, "nFile2Sup", "Numer file 2", lnfile2Sup, nfile2Sup, 0, 8);
        addComponent(this, "diamBsup", "Diametro bulloni mm", lDiamBsup, DiamBsup, 0, 9);
        addComponent(this, "diamForoSup", "Diametro fori", lDiamForoSup, DiamForoSup, 0, 10);
        addComponent(this, "tgSup", "Spessore compribande mm", ltgSup, tgSup, 0, 11);
        
    }
    
    /**
     * @param title
     * @param lBs1
     * @param bs1
     * @param gridx
     * @param gridy
     */
    private void addComponent(JPanel pInput, String fieldName, String title,
            JLabel lBs1, JComponent bs1, int gridx, int gridy) {
        
        listaTextField.put(fieldName, bs1);
        
        lBs1.setHorizontalAlignment(SwingConstants.LEFT);
        lBs1.setText(title);
        lBs1.setPreferredSize(new Dimension(150, 15));
        
        bs1.setPreferredSize(new Dimension(40, 21));
        bs1.setMinimumSize(new Dimension(40, 21));
        bs1.setMaximumSize(new Dimension(40, 21));
        
        
        
        pInput.add(lBs1, new GridBagConstraints(gridx, gridy, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(
                0, 0, 0, 0), 9, 0));
        pInput.add(bs1, new GridBagConstraints(gridx + 1, gridy, 1, 1, 0.0,
                0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 3, 0, 0), 20, 0));
        
    }
    public void setDiametroBullone(int b){
        DiamBsup.setSelectedItem(b);
    }
    
    
    public void resetComboDiametri(){
//		Integer c = (Integer)DiamBW.getSelectedItem();
        DiamBsup.removeActionListener( controller );
        DiamBsup.removeAllItems();
        
        DiamBsup.addItem( new Integer(8));
        DiamBsup.addItem( new Integer(10));
        DiamBsup.addItem( new Integer(12));
        DiamBsup.addItem( new Integer(14));
        DiamBsup.addItem( new Integer(16));
        DiamBsup.addItem( new Integer(18));
        DiamBsup.addItem( new Integer(20));
        DiamBsup.addItem( new Integer(22));
        DiamBsup.addItem( new Integer(24));
        DiamBsup.addItem( new Integer(27));
        DiamBsup.addItem( new Integer(30));
        DiamBsup.addItem( new Integer(33));
        DiamBsup.addItem( new Integer(36));
        
        DiamBsup.setSelectedIndex(0);
        DiamBsup.addActionListener( controller);
    }
    
    /**
     *
     * @throws java.lang.Exception
     */
    void jbInit() throws Exception {
        this.setLayout(gridBagLayout1);
        resetComboDiametri();
        addComponents();
        
        String img = "file:img/e1aPB.JPG";
        e1aSup.setToolTipText("<html>e1a<br><img src="+img+"></html>");
        
        img = "file:img/e1bPB.JPG";
        e1bSup.setToolTipText("<html>e1b<br><img src="+img+"></html>");
        
        img = "file:img/e2aPB.JPG";
        e2aSup.setToolTipText("<html>e2a<br><img src="+img+"></html>");
        
        img = "file:img/e2bPB.JPG";
        e2bSup.setToolTipText("<html>e2b<br><img src="+img+"></html>");
        
        img = "file:img/p1PB.JPG";
        p1Sup.setToolTipText("<html>p1<br><img src="+img+"></html>");
        
        img = "file:img/p2PB.JPG";
        p2Sup.setToolTipText("<html>p2<br><img src="+img+"></html>");
        
    }
    
    public void setTextBoxEnable(boolean eneble){
        
        e1aSup.setEnabled(eneble);
        e1bSup.setEnabled(eneble);
        e2aSup.setEnabled(eneble);
        e2bSup.setEnabled(eneble);
        p1Sup.setEnabled(eneble);
        p2Sup.setEnabled(eneble);
        nfile1Sup.setEnabled(eneble);
        nfile2Sup.setEnabled(eneble);
        DiamBsup.setEnabled(eneble);
        DiamForoSup.setEnabled(eneble);
        tgSup.setEnabled(eneble);
    }
    
    
}
