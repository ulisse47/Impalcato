package cassone.inputPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Hashtable;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import cassone.view.analisi.MyTextField;

public class PannelloConcioDoppioT extends JPanel {

	JTextField bs = new MyTextField();

	JLabel lbs = new JLabel();

	JTextField ts = new MyTextField();

	JLabel lts = new JLabel();

	JLabel ltw = new JLabel();

	JTextField tw = new MyTextField();

	JLabel lhw = new JLabel();

	JTextField hw = new MyTextField();

	JLabel lbi = new JLabel();

	JTextField bi = new MyTextField();

	JLabel lti = new JLabel();

	JTextField ti = new MyTextField();

	TitledBorder titledBorderSezione;
	
	private static PannelloConcioDoppioT inputPanel;
	
	private Hashtable listaTextField = new Hashtable();

	public PannelloConcioDoppioT(){
		try {
			jbInit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	
	void jbInit() throws Exception {
		
		titledBorderSezione = new TitledBorder(new EtchedBorder(
				EtchedBorder.RAISED, Color.white, Color.gray), "TRAVE METALLICA A DOPPIO T");
		
		setLayout(new GridBagLayout());
		setBorder(titledBorderSezione);
		
		// concio
		addComponent("bs", "Larghezza ala superiore: bs (mm)",
				lbs, bs, 0, 0,58,21);
		addComponent("ts", "Spessore ala superiore: ts (mm)",
				lts, ts, 0, 1, 58,21);

		addComponent("tw", "Spessore anima trave: tw (mm)", ltw, tw,
				0, 2, 58,21);

		addComponent("hw", "Altezza totale trave: H (mm)", lhw, hw,
				0, 3, 58,21);

		addComponent("bi", "Larghezza ala inferiore trave in acciaio: bi (mm)",
				lbi, bi, 0, 4, 58,21);

		addComponent("ti", "Spessore ala inferiore trave in acciaio: ti (mm)",
				lti, ti, 0, 5, 58,21);

		String img = "file:img/CDOPPIOT_ts.JPG";
		ts.setToolTipText("<html>SEZIONE DOPPIO T <br><img src="+img+"></html>");

		img = "file:img/CDOPPIOT_bs.JPG";
		bs.setToolTipText("<html>SEZIONE DOPPIO T <br><img src="+img+"></html>");

		
		img = "file:img/CDOPPIOT_tw.JPG";
		tw.setToolTipText("<html>SEZIONE DOPPIO T <br><img src="+img+"></html>");

		img = "file:img/CDOPPIOT_H.JPG";
		hw.setToolTipText("<html>SEZIONE DOPPIO T <br><img src="+img+"></html>");

		img = "file:img/CDOPPIOT_ti.JPG";
		ti.setToolTipText("<html>SEZIONE DOPPIO T <br><img src="+img+"></html>");

		img = "file:img/CDOPPIOT_bi.JPG";
		bi.setToolTipText("<html>SEZIONE DOPPIO T <br><img src="+img+"></html>");

//		setToolTipText("<html> <v:imagedata> prova.jpg </v:imagedata></html>");
		
	}
	
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
	public static synchronized PannelloConcioDoppioT getInstance() {
		if (inputPanel == null) {
			inputPanel = new PannelloConcioDoppioT();
		}
		return inputPanel;
	}

	public String getValue(String name) {
		JTextField field = (JTextField) listaTextField.get(name);
	
            if(name == "bs" || name == "ts" || name == "tw" ||name == "bi" ||name == "ti" ||name == "hw"){
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
}
