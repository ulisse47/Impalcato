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

public class InputGiuntoBullonatoInf extends JPanel {

	private Hashtable listaTextField = new Hashtable();

	private static InputGiuntoBullonatoInf inputPanel;

	GridBagLayout gridBagLayout1 = new GridBagLayout();


	//piattabanda inf
	JLabel le1aInf = new JLabel();
	MyTextField e1aInf = new MyTextField();

	JLabel le1bInf = new JLabel();
	MyTextField e1bInf = new MyTextField();

	JLabel le2aInf = new JLabel();
	MyTextField e2aInf= new MyTextField();

	JLabel le2bInf = new JLabel();
	MyTextField e2bInf= new MyTextField();

	JLabel lp1Inf = new JLabel();
	MyTextField p1Inf= new MyTextField();

	JLabel lp2Inf = new JLabel();
	MyTextField p2Inf= new MyTextField();
	
	JLabel lnfile1Inf= new JLabel();
	MyTextField nfile1Inf= new MyTextField();
	
	JLabel lnfile2Inf= new JLabel();
	MyTextField nfile2Inf= new MyTextField();
	
	JLabel lDiamBInf = new JLabel();
	JComboBox DiamBInf= new JComboBox();

	JLabel lDiamForoInf = new JLabel();
	MyTextField DiamForoInf =new MyTextField();
	
	JLabel ltgInf= new JLabel();
	MyTextField tgInf= new MyTextField();

	
	private ControllerVerificaGiunti controller = new ControllerVerificaGiunti();

	public InputGiuntoBullonatoInf() {
		super();
		try {
			jbInit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}



	/**
	 * @return
	 */
	public static synchronized InputGiuntoBullonatoInf getInstance() {
		if (inputPanel == null) {
			inputPanel = new InputGiuntoBullonatoInf();
		}
		return inputPanel;
	}

	public Iterator getListaFieldNames() {
		return listaTextField.keySet().iterator();
	}

	public String getValue(String name) {
		JTextField field = (JTextField) listaTextField.get(name);
        if(name == "e1aInf" || name == "e1bInf" || name == "e2aInf" ||name == "e2bInf"||
                name == "p1Inf"||name == "p2Inf" ||name == "tgInf" ){
            try {
                Double.parseDouble(field.getText());
                field.setBackground(Color.WHITE);
            } catch (Exception e) {
                field.setBackground(Color.RED);
            }
        }
        if(name == "nFile1Inf" || name == "nFile2Inf" || name == "diamForoInf" ){
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

	/*
	 * 
	 * @param current_concio @param nConci
	 */
	public void resetCombo(JComboBox cb, Object current, int n) {
		if (n == 0)
			return;
		cb.removeActionListener(controller);
		cb.removeAllItems();
		for (int i = 0; i < n; i++) {
			cb.addItem(new Integer(i + 1));
		}
		cb.setSelectedItem(current);
		cb.addActionListener(controller);
	}

	
	/**
	 * 
	 */
	private void addComponents() {
				
		
		//inferiore
		addComponent(this, "e1aInf", "Distanza dal bordo e1a mm", le1aInf, e1aInf, 0, 12);
		addComponent(this, "e1bInf", "Distanza dal bordo e1b mm", le1bInf, e1bInf, 0, 13);
		addComponent(this, "e2aInf", "Distanza dal bordo e2a mm", le2aInf, e2aInf, 0, 14);
		addComponent(this, "e2bInf", "Distanza dal bordo e2b mm", le2bInf, e2bInf, 0, 15);
		addComponent(this, "p1Inf", "Interasse p1 mm", lp1Inf, p1Inf, 0, 16);
		addComponent(this, "p2Inf", "Interasse p2 mm", lp2Inf, p2Inf, 0, 17);
		addComponent(this, "nFile1Inf", "numero file 1", lnfile1Inf, nfile1Inf, 0, 18);
		addComponent(this, "nFile2Inf", "Numer file 2", lnfile2Inf, nfile2Inf, 0, 19);
		addComponent(this, "diamBInf", "Diametro bulloni mm", lDiamBInf, DiamBInf, 0, 20);
		addComponent(this, "diamForoInf", "Diametro fori", lDiamForoInf, DiamForoInf, 0, 21);
		addComponent(this, "tgInf", "Spessore compribande mm", ltgInf, tgInf, 0, 22);

	
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

	/**
	 * 
	 * @throws java.lang.Exception
	 */
	void jbInit() throws Exception {
		this.setLayout(gridBagLayout1);
		this.setBorder( new TitledBorder(new EtchedBorder(
				EtchedBorder.RAISED, Color.white, Color.gray), "ALA INFERIORE" ));
		resetComboDiametri();
		addComponents();
                
                               
                String img = "file:img/e1aPB.JPG";
		e1aInf.setToolTipText("<html>e1a<br><img src="+img+"></html>");

                img = "file:img/e1bPB.JPG";
		e1bInf.setToolTipText("<html>e1b<br><img src="+img+"></html>");

                img = "file:img/e2aPB.JPG";
		e2aInf.setToolTipText("<html>e2a<br><img src="+img+"></html>");

                img = "file:img/e2bPB.JPG";
		e2bInf.setToolTipText("<html>e2b<br><img src="+img+"></html>");
                
                img = "file:img/p1PB.JPG";
		p1Inf.setToolTipText("<html>p1<br><img src="+img+"></html>");

                img = "file:img/p2PB.JPG";
		p2Inf.setToolTipText("<html>p2<br><img src="+img+"></html>");
                
 	
	}
	public int getDiametroBullone(){
		return (Integer)DiamBInf.getSelectedItem();
	}
	public void setDiametroBullone(int b){
		DiamBInf.setSelectedItem(b);
	}
	
	
	public void resetComboDiametri(){
//		Integer c = (Integer)DiamBW.getSelectedItem();	    
		DiamBInf.removeActionListener( controller );
		DiamBInf.removeAllItems();
			  
			    DiamBInf.addItem( new Integer(8));
			    DiamBInf.addItem( new Integer(10));
			    DiamBInf.addItem( new Integer(12));
			    DiamBInf.addItem( new Integer(14));
			    DiamBInf.addItem( new Integer(16));
			    DiamBInf.addItem( new Integer(18));
			    DiamBInf.addItem( new Integer(20));
			    DiamBInf.addItem( new Integer(22));
			    DiamBInf.addItem( new Integer(24));
			    DiamBInf.addItem( new Integer(27));
			    DiamBInf.addItem( new Integer(30));
			    DiamBInf.addItem( new Integer(33));
			    DiamBInf.addItem( new Integer(36));
			    
			    DiamBInf.setSelectedIndex(0);
			    DiamBInf.addActionListener( controller);
	}
	
	public void setTextBoxEnable(boolean eneble){
		
/*		e1aSup.setEnabled(eneble);
		e1bSup.setEnabled(eneble);
		e2Sup.setEnabled(eneble);
		e1aSup.setEnabled(eneble);
		p1Sup.setEnabled(eneble);
		p2Sup.setEnabled(eneble);
		nfile1Sup.setEnabled(eneble);
		nfile2Sup.setEnabled(eneble);
		DiamBsup.setEnabled(eneble);
		DiamForoSup.setEnabled(eneble);
		tgSup.setEnabled(eneble);
		nu.setEnabled(eneble);
		ks.setEnabled(eneble);
*/
		e1aInf.setEnabled(eneble);
		e1bInf.setEnabled(eneble);
		e2aInf.setEnabled(eneble);
		e2bInf.setEnabled(eneble);
		p1Inf.setEnabled(eneble);
		p2Inf.setEnabled(eneble);
		nfile1Inf.setEnabled(eneble);

		nfile2Inf.setEnabled(eneble);
		DiamBInf.setEnabled(eneble);
		DiamForoInf.setEnabled(eneble);
		tgInf.setEnabled(eneble);
/*		e1aW.setEnabled(eneble);
		e1bW.setEnabled(eneble);
		e2W.setEnabled(eneble);
		p1W.setEnabled(eneble);
		p2W.setEnabled(eneble);
		nfile1W.setEnabled(eneble);
		nfile2W.setEnabled(eneble);
		DiamBW.setEnabled(eneble);
		DiamForoW.setEnabled(eneble);
		tgW.setEnabled(eneble);
*/
		
		
	}


}
