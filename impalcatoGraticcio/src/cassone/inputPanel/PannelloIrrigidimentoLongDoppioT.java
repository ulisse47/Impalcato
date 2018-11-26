package cassone.inputPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Hashtable;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;


import cassone.model.Progetto;
import cassone.model.Sezione;
import cassone.model.SezioniMetalliche.SezioneMetallicaDoppioT;
import cassone.util.Parser;
import cassone.view.analisi.MyTextField;

public class PannelloIrrigidimentoLongDoppioT extends JPanel {

	JLabel lb1 = new JLabel();

	JTextField b1 = new MyTextField();

	JLabel lh1 = new JLabel();

	JTextField h1 = new MyTextField();

	JLabel lb2 = new JLabel();

	JTextField b2 = new MyTextField();

	JLabel lh2 = new JLabel();

	JTextField h2 = new MyTextField();

	JLabel ly1 = new JLabel();

	JTextField y1 = new MyTextField();

	JLabel ly2 = new JLabel();

	JTextField y2 = new MyTextField();

	JComboBox cNumeroIrrigidimenti = new JComboBox();

	JLabel lcNumeroIrrigidimenti = new JLabel();

	TitledBorder titledBorderSezione;

	private ControllerPannello controller = new ControllerPannello();

	private static PannelloIrrigidimentoLongDoppioT inputPanel;

	private Hashtable listaTextField = new Hashtable();

     public void setSelectedIndexComboIrrigidimenti(int anIndex) {

         cNumeroIrrigidimenti.removeActionListener(controller);
         cNumeroIrrigidimenti.setSelectedIndex(anIndex);
         cNumeroIrrigidimenti.addActionListener(controller);
         
    }
        public PannelloIrrigidimentoLongDoppioT() {
		try {
			jbInit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	void jbInit() throws Exception {

//		titledBorderSezione = new TitledBorder(new EtchedBorder(
//				EtchedBorder.RAISED, Color.white, Color.gray), "IRRIGIDIMEMNTI LONG. TRAVE DOPPIO T");

                this.setLayout(new BorderLayout());
		
                this.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.RAISED,
				Color.white, Color.gray), "IRRIGIDIMEMNTI LONG. TRAVE DOPPIO T"));
		
                JPanel pIrrLongL = new JPanel(new GridBagLayout());
		JPanel pIrrLongR = new JPanel(new GridBagLayout());
		add(pIrrLongL, BorderLayout.WEST);
		add(pIrrLongR, BorderLayout.CENTER);

 		cNumeroIrrigidimenti.setActionCommand("SET_NIRR");
		cNumeroIrrigidimenti.addActionListener(controller);

//		setBorder(titledBorderSezione);

		b1.setText("150");
		b2.setText("150");
		h2.setText("8");
		h1.setText("10");
		y1.setText("300");
		y2.setText("600");
		
		
		
		addComponent(pIrrLongL, "b1", "Pannello1: b1xh1 (mmxmm)", lb1, b1, 0,
				1, 50, 20);
		addComponent(pIrrLongL, "h1", "", lh1, h1, 1, 1, 50, 20);
		addComponent(pIrrLongL, "b2", "Pannello 2: b2xh2 (mmxmm)", lb2, b2, 0,
				2, 50, 20);
		addComponent(pIrrLongL, "h2", "", lh2, h2, 1, 2, 50, 20);
		addComponent(pIrrLongL, "y1", "Quota irrigidimento 1", ly1, y1, 0, 3,
				50, 20);
		addComponent(pIrrLongL, "y2", "Quota irrigidimento 2", ly2, y2, 0, 4,
				50, 20);
		addComponent(pIrrLongL, "nIrr", "Numero irrigidimenti Anima",
				lcNumeroIrrigidimenti, cNumeroIrrigidimenti, 0, 5, 50, 20);
		// immagine
		ImageIcon p1 = loadImage("/images/P1.gif");
		addComponent(pIrrLongR, "", "", new JLabel(p1), null, 0, 1, p1
				.getIconHeight(), 50);
		
		resetComboIrrigidimenti();
		setJTxtEnable(cNumeroIrrigidimenti.getSelectedIndex());

	}

	private void addComponent(JPanel pInput, String fieldName, String title,
			JLabel lBs1, JComponent bs1, int gridx, int gridy, int witdthBox,
			int height) {

		if (bs1 != null) {
			listaTextField.put(fieldName, bs1);
			bs1.setPreferredSize(new Dimension(witdthBox, height));
			pInput.add(bs1, new GridBagConstraints(gridx + 1, gridy, 1, 1, 0.0,
					0.0, GridBagConstraints.LINE_START,
					GridBagConstraints.NONE, new Insets(1, 1, 0, 0), 0, 0));
		}

		lBs1.setHorizontalAlignment(SwingConstants.LEFT);
		lBs1.setText(title);

		pInput.add(lBs1, new GridBagConstraints(gridx, gridy, 1, 1, 0.0, 0.0,
				GridBagConstraints.LINE_START, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));

	}

	/**
	 * @return
	 */
	public static synchronized PannelloIrrigidimentoLongDoppioT getInstance() {
		if (inputPanel == null) {
			inputPanel = new PannelloIrrigidimentoLongDoppioT();
		}
		return inputPanel;
	}
        
        
	private ImageIcon loadImage(String localUrl) {

		try {
			BufferedImage bf = ImageIO.read(getClass().getResourceAsStream(
					localUrl));
			ImageIcon img = new ImageIcon(bf);
			return img;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ImageIcon img = new ImageIcon();
                        return img;
		}
	}

	public String getValue(String name) {
		JTextField field = (JTextField) listaTextField.get(name);
        if(name == "h1" || name == "h2" || name == "b1" ||name == "b2"||name == "y1"||name == "y2"  ){
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

	public void setJTxtEnable(int nIrr){
			h1.setEnabled(nIrr>0);
			h2.setEnabled(nIrr>0);
			b1.setEnabled(nIrr>0);
			b2.setEnabled(nIrr>0);
			y1.setEnabled(nIrr>0);
			y2.setEnabled(nIrr>1);
		}
		
	public void resetComboIrrigidimenti(){
		Progetto prg = Progetto.getInstance();
		Sezione sez= prg.getCurrentSezioneVerifica();
		SezioneMetallicaDoppioT sm = (SezioneMetallicaDoppioT)sez.getSezioneMetallica();
		int nI = sm.getIrsAnima().size();
		
		cNumeroIrrigidimenti.removeActionListener(controller);
		cNumeroIrrigidimenti.removeAllItems();

		for (int i = 0; i < 3; i++) {
			cNumeroIrrigidimenti.addItem(new Integer(i));
		}

		cNumeroIrrigidimenti.setSelectedIndex(new Integer(nI));
		cNumeroIrrigidimenti.addActionListener(controller);
	
	}

	class ControllerPannello implements ActionListener, ItemListener {

	public ControllerPannello() {
	}

	public void actionPerformed(ActionEvent e) {

		Class cl = e.getSource().getClass();
		JButton bottomSource = null;
		JComboBox comboSource = null;

		if (cl == JComboBox.class) {
			comboSource = (JComboBox) e.getSource();
			if (comboSource.getActionCommand() == "SET_NIRR") {
				int ni = (Integer) comboSource.getSelectedItem();
				setJTxtEnable(ni);
				try {
					Parser.validate();
					Progetto.getInstance().ridisegna();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				}
				
			}
		}


	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub
		
	}
 }

	public JComboBox getCNumeroIrrigidimentiAnima() {
		return cNumeroIrrigidimenti;
	}

	public void setCNumeroIrrigidimenti(JComboBox numeroIrrigidimenti) {
		cNumeroIrrigidimenti = numeroIrrigidimenti;
	}
}

