package cassone.inputPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Hashtable;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import cassone.view.analisi.MyTextField;

public class PannelloConcioGenerico extends JPanel {

	
	JTextField Asgen = new MyTextField();

	JLabel lAsgen = new JLabel();

	JTextField Jsgen = new MyTextField();

	JLabel lJsgen = new JLabel();

	JLabel lYggen = new JLabel();

	JTextField Yggen = new MyTextField();

	JLabel lHsgen = new JLabel();

	JTextField Hsgen = new MyTextField();

	JLabel lAwgen = new JLabel();

	JTextField Awgen = new MyTextField();


	TitledBorder titledBorderSezione;
	
	private static PannelloConcioGenerico inputPanel;
	
	private Hashtable listaTextField = new Hashtable();

	public PannelloConcioGenerico(){
		try {
			jbInit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	
	void jbInit() throws Exception {
		
		titledBorderSezione = new TitledBorder(new EtchedBorder(
				EtchedBorder.RAISED, Color.white, Color.gray), "SEZIONE METALLICA GENERICA");
		
		setLayout(new GridBagLayout());
		setBorder(titledBorderSezione);
		
		
		addComponent("Asgen", "Area concio: As (mm)", lAsgen, Asgen, 0, 1,58,21);
		addComponent("Jsgen", "Momento d'inerzia concio: Js (mm4)", lJsgen,
				Jsgen, 0, 2,58,21);
		addComponent("Yggen", "Baricentro concio: Yg (mm)", lYggen, Yggen, 0,
				3,58,21);
		addComponent("Hsgen", "Altezza totale: Hs (mm)", lHsgen, Hsgen, 0, 4,58,21);
		addComponent("Awgen", "Area resistente a taglio: Aw (mm)", lAwgen,
		Awgen, 0, 5,58,21);

		
	}
	
        @SuppressWarnings("unchecked") 
	 private void addComponent(String fieldName, String title, JLabel lBs1,
				JTextField bs1, int gridx, int gridy, int width, int height ) {
	
		 	listaTextField.put(fieldName, bs1);

			lBs1.setHorizontalAlignment(SwingConstants.LEFT);
			lBs1.setText(title);
			lBs1.setPreferredSize(new Dimension(230, 15));

			bs1.setPreferredSize( new Dimension( width , height ) );
			bs1.setHorizontalAlignment(SwingConstants.RIGHT);

			add(lBs1, new GridBagConstraints(gridx, gridy, 1, 1, 0.0, 0.0,
					GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(
							0, 0, 0, 0), 9, 0));
			add(bs1, new GridBagConstraints(gridx + 1, gridy, 1, 1, 0.0, 0.0,
					GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(
							0, 0, 0, 0), 0, 0));		 
	 }

	
	/**
	 * @return
	 */
	public static synchronized PannelloConcioGenerico getInstance() {
		if (inputPanel == null) {
			inputPanel = new PannelloConcioGenerico();
		}
		return inputPanel;
	}

	public String getValue(String name) {
		JTextField field = (JTextField) listaTextField.get(name);
		return field.getText();
	}

	public void setValue(String name, String value) {
		JTextField field = (JTextField) listaTextField.get(name);
		field.setText(value);
	}
}
