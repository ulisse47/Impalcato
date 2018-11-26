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

public class PannelloConcioCassoneII extends JPanel {

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
	
	JLabel lbsc = new JLabel();

	JTextField bsc = new MyTextField();

	JLabel ltsc = new JLabel();

	JTextField tsc = new MyTextField();

	JLabel ltwc = new JLabel();

	JTextField twc = new MyTextField();

	JLabel lbic = new JLabel();

	JTextField bic = new MyTextField();
	
	JLabel lteq = new JLabel();

	JTextField teq = new MyTextField();

	JLabel lalfa = new JLabel();

	JTextField alfa = new MyTextField();
	
	TitledBorder titledBorderSezione;
	
	private static PannelloConcioCassoneII inputPanel;
	
	private Hashtable listaTextField = new Hashtable();

	public PannelloConcioCassoneII(){
		try {
			jbInit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	
	void jbInit() throws Exception {
		
		titledBorderSezione = new TitledBorder(new EtchedBorder(
				EtchedBorder.RAISED, Color.white, Color.gray), "CASSONE METALLICO TIPO II");
		
		setLayout(new GridBagLayout());
		setBorder(titledBorderSezione);
		
		// concio
		addComponent("bs", "Larghezza ali superiori: bs (mm)",
				lbs, bs, 0, 0,58,21);
		addComponent("ts", "Spessore ali superiori: ts (mm)",
				lts, ts, 0, 1, 58,21);

		addComponent("tw", "Spessore anime: tw (mm)", ltw, tw,
				0, 2, 58,21);

		addComponent("Htot", "Altezza totlae: H (mm)", lhw, Htot,
				0, 3, 58,21);

		addComponent("bi", "Larghezza ala inferiore: bi (mm)",
				lbi, bi, 0, 4, 58,21);

		addComponent("ti", "Spessore ala inferiore: ti (mm)",
				lti, ti, 0, 5, 58,21);

		addComponent("alfa", "Inclinazione anime: alfa (deg)",
				lalfa, alfa, 0, 6, 58,21);
		
		addComponent("teq", "Spessore equivalente controventi: teq (mm)",
				lteq, teq, 0, 7, 58,21);
		
		addComponent("bsc", "larghezza ali superiori centrali: bsc (mm)",
				lbsc, bsc, 0, 8, 58,21);

		addComponent("tsc", "spessore ali superiori centrali: tsc (mm)",
				ltsc, tsc, 0, 9, 58,21);
		
		addComponent("twc", "spessore anime centrali: twc (mm)",
				ltwc, twc, 0, 10, 58,21);

		addComponent("bic", "distanza anime centrali: bic (mm)",
				lbic, bic, 0, 11, 58,21);
		
		String img = "file:img/CCASSII_alfa.JPG";
		alfa.setToolTipText("<html>SEZIONE CASSONE II<br><img src="+img+"></html>");

		img = "file:img/CCASSII_ts.JPG";
		ts.setToolTipText("<html>SEZIONE CASSONE II<br><img src="+img+"></html>");

		img = "file:img/CCASSII_ti.JPG";
		ti.setToolTipText("<html>SEZIONE CASSONE II<br><img src="+img+"></html>");

		img = "file:img/CCASSII_bi.JPG";
		bi.setToolTipText("<html>SEZIONE CASSONE II<br><img src="+img+"></html>");

		img = "file:img/CCASSII_bs.JPG";
		bs.setToolTipText("<html>SEZIONE CASSONE II<br><img src="+img+"></html>");

		img = "file:img/CCASSII_tw.JPG";
		tw.setToolTipText("<html>SEZIONE CASSONE II<br><img src="+img+"></html>");

		img = "file:img/CCASSII_H.JPG";
		Htot.setToolTipText("<html>SEZIONE CASSONE II<br><img src="+img+"></html>");
		

		img = "file:img/CCASSII_bsc.JPG";
		bsc.setToolTipText("<html>SEZIONE CASSONE II <br><img src="+img+"></html>");

		img = "file:img/CCASSII_tsc.JPG";
		tsc.setToolTipText("<html>SEZIONE CASSONE II <br><img src="+img+"></html>");


		img = "file:img/CCASSII_twc.JPG";
		twc.setToolTipText("<html>SEZIONE CASSONE II <br><img src="+img+"></html>");

		img = "file:img/CCASSII_bic.JPG";
		bic.setToolTipText("<html>SEZIONE CASSONE II <br><img src="+img+"></html>");

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
	public static synchronized PannelloConcioCassoneII getInstance() {
		if (inputPanel == null) {
			inputPanel = new PannelloConcioCassoneII();
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
