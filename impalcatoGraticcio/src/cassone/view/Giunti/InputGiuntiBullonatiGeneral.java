package cassone.view.Giunti;

import cassone.model.SezioniMetalliche.SezioneMetallicaDoppioT;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
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
import cassone.model.Sezione;
import cassone.view.analisi.MyTextField;

public class InputGiuntiBullonatiGeneral extends JPanel {

	private Hashtable listaTextField = new Hashtable();

	private static InputGiuntiBullonatiGeneral inputPanel;
	
	JLabel lSezione = new JLabel();
	JComboBox cbSezione = new JComboBox();


	JLabel lnu = new JLabel();
	MyTextField nu = new MyTextField();
	
	JLabel lks = new JLabel();
	MyTextField ks = new MyTextField();
	
	JLabel lfuCoprig = new JLabel();
	MyTextField fuCoprig = new MyTextField();
	
	JLabel lfuBulloni = new JLabel();
	MyTextField fuBulloni = new MyTextField();
	

	private ControllerVerificaGiunti controller = new ControllerVerificaGiunti();

	public InputGiuntiBullonatiGeneral() {
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
	public static synchronized InputGiuntiBullonatiGeneral getInstance() {
		if (inputPanel == null) {
			inputPanel = new InputGiuntiBullonatiGeneral();
		}
		return inputPanel;
	}

	public Iterator getListaFieldNames() {
		return listaTextField.keySet().iterator();
	}

	public String getValue(String name) {
		JTextField field = (JTextField) listaTextField.get(name);
        if(name == "nu" || name == "ks" || name == "fuBulloni" ||name == "fuCoprig" ){
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

	public void resetComboSez(Object current_sezione,
		ArrayList<Sezione> nSezioni) {
		cbSezione.removeActionListener(controller);
		cbSezione.removeAllItems();
		for (int i = 0; i < nSezioni.size(); i++) {
			Sezione sez = (Sezione)nSezioni.get(i);
			if(sez.getSezioneMetallica().getClass()== SezioneMetallicaDoppioT.class)
                            cbSezione.addItem(sez);
		}
		cbSezione.setSelectedItem(current_sezione);
		cbSezione.addActionListener(controller);
	}

	public JComboBox getComboSezione(){
		return cbSezione;
	}
	
	/**
	 * 
	 */
	private void addComponents() {
		
		
		addComponent(this, "sezione", "Sezione", lSezione, cbSezione, 0, 0);
		addComponent(this, "nu", "Coefficiente di attrito nu", lnu, nu, 0, 1);
		addComponent(this, "ks", "Coefficiente ks", lks, ks, 0, 2);
		addComponent(this, "fuCoprig", "Resistenza ultima coprigiunti: MPa", lfuCoprig, fuCoprig, 0, 3);
		addComponent(this, "fuBulloni", "Resistenza ultima bulloni: MPa", lfuBulloni, fuBulloni, 0, 4);
		
//		this.add( pGeneral );
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
                 
                pInput.add(lBs1, new GridBagConstraints(gridx, gridy, 1, 1, 0.0, 0.0,
                                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(
                                                0, 0, 0, 0), 12, 0));
                pInput.add(bs1, new GridBagConstraints(gridx + 1, gridy, 1, 1, 0.0,
                                0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
                                new Insets(0, 3, 0, 0), 20, 0));     

	}

	/**
	 * 
	 * @throws java.lang.Exception
	 */
	void jbInit() throws Exception {
		
            
 //           	JPanel pGeneral = new JPanel( new GridBagLayout());
//		pGeneral.setBorder( new TitledBorder(new EtchedBorder(
//				EtchedBorder.RAISED, Color.white, Color.gray), "DATI GENERALI SEZIONE" ));

                 
                this.setLayout(new GridBagLayout());
				
		this.setBorder( new TitledBorder(new EtchedBorder(
				EtchedBorder.RAISED, Color.white, Color.gray), "DATI GENERALI SEZIONE" ));

                addComponents();

  
		// combo sezione
		cbSezione.addActionListener(controller);
		cbSezione.setActionCommand("SET_SEZIONE");
		
	}
	
	public void setTextBoxEnable(boolean eneble){
		
		nu.setEnabled(eneble);
		ks.setEnabled(eneble);
		fuBulloni.setEditable(eneble);
		fuCoprig.setEditable(eneble);
		
		
	}


}
