package cassone.inputPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.Hashtable;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import cassone.view.analisi.InputView;
import cassone.view.analisi.MyTextField;

public class PannelloSolettaType2 extends JPanel {

	public JTextField b = new MyTextField();

	JLabel lb = new JLabel();

	public JTextField bo = new MyTextField();

	JLabel lbo = new JLabel();

	public JTextField bs = new MyTextField();

	JLabel lbs = new JLabel();

	JLabel lhs1 = new JLabel();

	public JTextField hs1 = new MyTextField();

	JLabel lhs2 = new JLabel();

	public JTextField hs2 = new MyTextField();

	JLabel lbd = new JLabel();

	public JTextField bd = new MyTextField();
	
	JLabel lhd1 = new JLabel();

	public JTextField hd1 = new MyTextField();

	JLabel lhd2 = new JLabel();

	public JTextField hd2 = new MyTextField();
	
	
	
	TitledBorder titledBorderSezione;
	
	private static PannelloSolettaType2 inputPanel;
	
	private Hashtable listaTextField = new Hashtable();

	public PannelloSolettaType2(){
		try {
			jbInit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	
	void jbInit() throws Exception {
		
		titledBorderSezione = new TitledBorder(new EtchedBorder(
				EtchedBorder.RAISED, Color.white, Color.gray), "SOLETTE TIPO 2 E 3");
		
		setLayout(new GridBagLayout());
		setBorder(titledBorderSezione);

		addComponent("b", "Larghezza inferiore: b(mm)",
				lb, b, 0, 1,58,21);

		addComponent("bs", "larghezza sx: bs (mm)", lbs, bs, 0, 2,58,21);

		addComponent("bd", "larghezza dx: bd (mm)", lbd, bd, 0, 3,58,21);

		addComponent("hs1", "dy di sinistra: hs1 (mm)", lhs1, hs1, 0, 4,58,21);

		addComponent("hs2", "spessore di sinistra: hs2 (mm)", lhs2, hs2, 0, 5,58,21);

		addComponent("hd1", "dy di destra: hd1 (mm)", lhd1, hd1, 0, 6,58,21);

		addComponent("hd2", "spessore di destra: hd2 (mm)", lhd2, hd2, 0, 7,58,21);
		
		addComponent("bo", "Larghezza pioli: bo (mm)", lbo, bo, 0, 8, 58,21);


		String imageNameb = "file:img/STYPE2_b.JPG";
		b.setToolTipText("<html>SEZIONE TIPO 2 <br><img src="+imageNameb+"></html>");
	
		imageNameb = "file:img/STYPE2_bd.JPG";
		bd.setToolTipText("<html>SEZIONE TIPO 2<br><img src="+imageNameb+"></html>");

		imageNameb = "file:img/STYPE2_bs.JPG";
		bs.setToolTipText("<html>SEZIONE TIPO 2<br><img src="+imageNameb+"></html>");
	
		imageNameb = "file:img/STYPE2_hs1.JPG";
		hs1.setToolTipText("<html>SEZIONE TIPO 2<br><img src="+imageNameb+"></html>");

		imageNameb = "file:img/STYPE2_hs2.JPG";
		hs2.setToolTipText("<html>SEZIONE TIPO 2<br><img src="+imageNameb+"></html>");

		imageNameb = "file:img/STYPE2_hd1.JPG";
		hd1.setToolTipText("<html>SEZIONE TIPO 2<br><img src="+imageNameb+"></html>");

		imageNameb = "file:img/STYPE2_hd2.JPG";
		hd2.setToolTipText("<html>SEZIONE TIPO 2<br><img src="+imageNameb+"></html>");
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
	public static synchronized PannelloSolettaType2 getInstance() {
		if (inputPanel == null) {
			inputPanel = new PannelloSolettaType2();
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
