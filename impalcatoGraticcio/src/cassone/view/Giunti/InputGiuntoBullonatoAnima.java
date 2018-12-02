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

public class InputGiuntoBullonatoAnima extends JPanel {

	private Hashtable listaTextField = new Hashtable();

	private static InputGiuntoBullonatoAnima inputPanel;

	GridBagLayout gridBagLayout1 = new GridBagLayout();

        //anima
	//piattabanda inf
	JLabel le1aW= new JLabel();
	MyTextField e1aW= new MyTextField();

	JLabel le1bW = new JLabel();
	MyTextField e1bW= new MyTextField();

	JLabel le2W= new JLabel();
	MyTextField e2W= new MyTextField();

	JLabel lp1W= new JLabel();
	MyTextField p1W= new MyTextField();

	JLabel lp2W= new JLabel();
	MyTextField p2W= new MyTextField();
	
	JLabel lnfile1W= new JLabel();
	MyTextField nfile1W= new MyTextField();
	
	JLabel lnfile2W= new JLabel();
	MyTextField nfile2W= new MyTextField();
	
	JLabel lDiamBW = new JLabel();
	JComboBox DiamBW= new JComboBox();

	JLabel lDiamForoW= new JLabel();
	MyTextField DiamForoW= new MyTextField();

	JLabel ltgW= new JLabel();
	MyTextField tgW= new MyTextField();
	
	
	private ControllerVerificaGiunti controller = new ControllerVerificaGiunti();

	public InputGiuntoBullonatoAnima() {
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
	public static synchronized InputGiuntoBullonatoAnima getInstance() {
		if (inputPanel == null) {
			inputPanel = new InputGiuntoBullonatoAnima();
		}
		return inputPanel;
	}

	public int getDiametroBullone(){
		return (Integer)DiamBW.getSelectedItem();
	}
	
	public void setDiametroBullone(int b){
		DiamBW.setSelectedItem(b);
	}
	
	
	public void resetComboDiametri(){
//		Integer c = (Integer)DiamBW.getSelectedItem();	    
		DiamBW.removeActionListener( controller );
			    DiamBW.removeAllItems();
			  
			    DiamBW.addItem( new Integer(8));
			    DiamBW.addItem( new Integer(10));
			    DiamBW.addItem( new Integer(12));
			    DiamBW.addItem( new Integer(14));
			    DiamBW.addItem( new Integer(16));
			    DiamBW.addItem( new Integer(18));
			    DiamBW.addItem( new Integer(20));
			    DiamBW.addItem( new Integer(22));
			    DiamBW.addItem( new Integer(24));
			    DiamBW.addItem( new Integer(27));
			    DiamBW.addItem( new Integer(30));
			    DiamBW.addItem( new Integer(33));
			    DiamBW.addItem( new Integer(36));
			    
			    DiamBW.setSelectedIndex(0);
			    DiamBW.addActionListener( controller);
	}
	
	public Iterator getListaFieldNames() {
		return listaTextField.keySet().iterator();
	}

	public String getValue(String name) {
		JTextField field = (JTextField) listaTextField.get(name);
        if(name == "e1aW" || name == "e1bW" || name == "e2W" ||name == "e2W"||
                name == "p1W"||name == "p2W" ||name == "tgW" ){
            try {
                Double.parseDouble(field.getText());
                field.setBackground(Color.WHITE);
            } catch (Exception e) {
                field.setBackground(Color.RED);
            }
        }
        if(name == "nFile1W" || name == "nFile2W" || name == "diamForoW" ){
            try {
                Integer.parseInt(field.getText());
                field.setBackground(Color.WHITE);
            } catch (Exception e) {
                field.setBackground(Color.RED);
            }
        }
                /*
                 *        giunto.setE1aW(Double.parseDouble(ipAnima.getValue("e1aW")));
        giunto.setE1bW(Double.parseDouble(ipAnima.getValue("e1bW")));
        giunto.setE2W(Double.parseDouble(ipAnima.getValue("e2W")));
        giunto.setP1W(Double.parseDouble(ipAnima.getValue("p1W")));
        giunto.setP2W(Double.parseDouble(ipAnima.getValue("p2W")));
        giunto.setNfile1W(Integer.parseInt(ipAnima.getValue("nFile1W")));
        giunto.setNfile2W(Integer.parseInt(ipAnima.getValue("nFile2W")));
        giunto.setDiamBullW(ipAnima.getDiametroBullone());
        giunto.setDiamForoW(Integer.parseInt(ipAnima.getValue("diamForoW")));
        giunto.setTgW(Double.parseDouble(ipAnima.getValue("tgW")));

                 *
                 **/
                
                
                
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
	
		//anima
		addComponent(this, "e1aW", "Distanza dal bordo e1a mm", le1aW, e1aW, 0, 21);
		addComponent(this, "e1bW", "Distanza dal bordo e1b mm", le1bW, e1bW, 0, 22);
		addComponent(this, "e2W", "Distanza dal bordo e2 mm", le2W, e2W, 0, 23);
		addComponent(this, "p1W", "Interasse p1 mm", lp1W, p1W, 0, 24);
		addComponent(this, "p2W", "Interasse p2 mm", lp2W, p2W, 0, 25);
		addComponent(this, "nFile1W", "numero file 1", lnfile1W, nfile1W, 0, 26);
		addComponent(this, "nFile2W", "Numer file 2", lnfile2W, nfile2W, 0, 27);
		addComponent(this, "diamBW", "Diametro bulloni mm", lDiamBW, DiamBW, 0, 28);
		addComponent(this, "diamForoW", "Diametro fori", lDiamForoW, DiamForoW, 0, 29);
		addComponent(this, "tgW", "Spessore compribande mm", ltgW, tgW, 0, 30);

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
				EtchedBorder.RAISED, Color.white, Color.gray), "ANIMA" ));
		
		resetComboDiametri();
		addComponents();
              
                String img = "file:img/e1aA.JPG";
		e1aW.setToolTipText("<html>e1a<br><img src="+img+"></html>");

                img = "file:img/e1bA.JPG";
		e1bW.setToolTipText("<html>e1b<br><img src="+img+"></html>");

                img = "file:img/e2A.JPG";
		e2W.setToolTipText("<html>e2<br><img src="+img+"></html>");

                img = "file:img/p1A.JPG";
		p1W.setToolTipText("<html>p1<br><img src="+img+"></html>");

                img = "file:img/p2A.JPG";
		p2W.setToolTipText("<html>p2<br><img src="+img+"></html>");

	
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
		e1aInf.setEnabled(eneble);
		e1bInf.setEnabled(eneble);
		e2Inf.setEnabled(eneble);
		p1Inf.setEnabled(eneble);
		p2Inf.setEnabled(eneble);
		nfile1Inf.setEnabled(eneble);

		nfile2Inf.setEnabled(eneble);
		DiamBInf.setEnabled(eneble);
		DiamForoInf.setEnabled(eneble);
		tgInf.setEnabled(eneble);
*/		e1aW.setEnabled(eneble);
		e1bW.setEnabled(eneble);
		e2W.setEnabled(eneble);
		p1W.setEnabled(eneble);
		p2W.setEnabled(eneble);
		nfile1W.setEnabled(eneble);
		nfile2W.setEnabled(eneble);
		DiamBW.setEnabled(eneble);
		DiamForoW.setEnabled(eneble);
		tgW.setEnabled(eneble);

		
		
	}


}
