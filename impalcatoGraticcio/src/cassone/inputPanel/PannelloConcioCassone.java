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

public class PannelloConcioCassone extends JPanel {

	JTextField bs = new MyTextField();

	JLabel lbs = new JLabel();

	JTextField ts = new MyTextField();

	JLabel lts = new JLabel();

	JLabel ltw = new JLabel();

	JTextField tw = new MyTextField();

	JLabel lhw = new JLabel();

	JTextField Htot = new MyTextField();

	JLabel lbi = new JLabel();

	JTextField bi = new MyTextField();

	JLabel lti = new JLabel();

	JTextField ti = new MyTextField();
	
	JLabel lteq = new JLabel();

	JTextField teq = new MyTextField();

	JLabel lalfa = new JLabel();

	JTextField alfa = new MyTextField();
	
	TitledBorder titledBorderSezione;
	
	private static PannelloConcioCassone inputPanel;
	
	private Hashtable listaTextField = new Hashtable();

	public PannelloConcioCassone(){
		try {
			jbInit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	
	void jbInit() throws Exception {
		
		titledBorderSezione = new TitledBorder(new EtchedBorder(
				EtchedBorder.RAISED, Color.white, Color.gray), "CASSONE METALLICO");
		
		setLayout(new GridBagLayout());
		setBorder(titledBorderSezione);
		
		// concio
		addComponent("bs", "Larghezza ali superiori: bs (mm)",
				lbs, bs, 0, 0,58,21);
		addComponent("ts", "Spessore ali superiori: ts (mm)",
				lts, ts, 0, 1, 58,21);

		addComponent("tw", "Spessore anime: tw (mm)", ltw, tw,
				0, 2, 58,21);

		addComponent("Htot", "Altezza totale: H (mm)", lhw, Htot,
				0, 3, 58,21);

		addComponent("bi", "Larghezza ala inferiore: bi (mm)",
				lbi, bi, 0, 4, 58,21);

		addComponent("ti", "Spessore ala inferiore: ti (mm)",
				lti, ti, 0, 5, 58,21);

		addComponent("alfa", "Inclinazione anime: alfa (deg)",
				lalfa, alfa, 0, 6, 58,21);
		
		addComponent("teq", "Spessore equivalente controventi: teq (mm)",
				lteq, teq, 0, 7, 58,21);
		
		
		
		String img = "file:CCASS_alfa.JPG";
		alfa.setToolTipText("<html>SEZIONE CASSONE<br><img src="+img+"></html>");

		img = "file:img/CCASS_ts.JPG";
		ts.setToolTipText("<html>SEZIONE CASSONE<br><img src="+img+"></html>");

		img = "file:img/CCASS_ti.JPG";
		ti.setToolTipText("<html>SEZIONE CASSONE<br><img src="+img+"></html>");

		img = "file:img/CCASS_bi.JPG";
		bi.setToolTipText("<html>SEZIONE CASSONE<br><img src="+img+"></html>");

		img = "file:img/CCASS_bs.JPG";
		bs.setToolTipText("<html>SEZIONE CASSONE<br><img src="+img+"></html>");

		img = "file:img/CCASS_tw.JPG";
		tw.setToolTipText("<html>SEZIONE CASSONE<br><img src="+img+"></html>");

		img = "file:img/CCASS_H.JPG";
		Htot.setToolTipText("<html>SEZIONE CASSONE<br><img src="+img+"></html>");

		
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
	public static synchronized PannelloConcioCassone getInstance() {
		if (inputPanel == null) {
			inputPanel = new PannelloConcioCassone();
		}
		return inputPanel;
	}

	public String getValue(String name) {
		JTextField field = (JTextField) listaTextField.get(name);

                if(name == "bs" || name == "ts" || name == "tw" ||name == "bi" ||name == "ti" ||name == "teq"
                        ||name == "Htot"||name == "alfa"){
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
