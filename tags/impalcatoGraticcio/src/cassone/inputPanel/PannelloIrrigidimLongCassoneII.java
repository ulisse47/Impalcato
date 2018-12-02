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
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;



import cassone.controller.ControllerVerificaInstabilita;
import cassone.model.Campate;
import cassone.model.Progetto;
import cassone.model.Sezione;
import cassone.model.SezioniMetalliche.SezioneMetallica;
import cassone.model.SezioniMetalliche.SezioneMetallicaCassone;
import cassone.model.SezioniMetalliche.SezioneMetallicaCassoneII;
import cassone.model.SezioniMetalliche.SezioneMetallicaDoppioT;
import cassone.util.Parser;
import cassone.util.dialog.DlgModificaLuci;
import cassone.util.dialog.DlgModificaNomiConci;
import cassone.util.dialog.DlgModificaNomiSezioni;
import cassone.view.Giunti.ViewGiuntoVistaLaterale;
import cassone.view.Instabilita.SezioneIrrigiditaView;
import cassone.view.Instabilita.TableTensioniEfficaci;
import cassone.view.VerificaTensioni.InputVerifiche;
import cassone.view.VerificaTensioni.TabTensioni;
import cassone.view.VerificaTensioni.TableCombView;
import cassone.view.VerificaTensioni.TableCondView;
import cassone.view.VerificaTensioni.TableTensioniView;
import cassone.view.VerificaTensioni.TensioniVerificaView;
import cassone.view.analisi.InputView;
import cassone.view.analisi.MyTextField;
import cassone.view.analisi.TabGeo;
import cassone.view.analisi.TableOutputView;

public class PannelloIrrigidimLongCassoneII extends JPanel {

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

	JLabel ly1C = new JLabel();

	JTextField y1C = new MyTextField();

	JLabel ly2C = new JLabel();

	JTextField y2C = new MyTextField();
	
	
	JComboBox cNumeroIrrigidimentiAnima = new JComboBox();
	JLabel lcNumeroIrrigidimentiAnima = new JLabel();

	JComboBox cNumeroIrrigidimentiPiattaInf = new JComboBox();
	JLabel lcNumeroIrrigidimentiPiattaInf = new JLabel();

	JComboBox cNumeroIrrigidimentiPiattaInfCen = new JComboBox();
	JLabel lcNumeroIrrigidimentiPiattaInfLat = new JLabel();

	JComboBox cNumeroIrrigidimentiAnimaCen = new JComboBox();
	JLabel lcNumeroIrrigidimentiAnimaCen = new JLabel();

	TitledBorder titledBorderSezione;

	private ControllerPannello controller = new ControllerPannello();

	private static PannelloIrrigidimLongCassoneII inputPanel;

	private Hashtable listaTextField = new Hashtable();

	public PannelloIrrigidimLongCassoneII() {
		try {
			jbInit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	void jbInit() throws Exception {

		titledBorderSezione = new TitledBorder(new EtchedBorder(
				EtchedBorder.RAISED, Color.white, Color.gray), "CASSONE TIPO II");

		setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.RAISED,
				Color.white, Color.gray), "IRRIGIDIMENTO LONGITUDINALE"));
		JPanel pIrrLongL = new JPanel(new GridBagLayout());
		JPanel pIrrLongR = new JPanel(new GridBagLayout());
		add(pIrrLongL, BorderLayout.WEST);
		add(pIrrLongR, BorderLayout.CENTER);

		cNumeroIrrigidimentiAnima.setActionCommand("SET_NIRR");
		cNumeroIrrigidimentiAnima.addActionListener(controller);

		cNumeroIrrigidimentiPiattaInf.setActionCommand("SET_NIRR_INF");
		cNumeroIrrigidimentiPiattaInf.addActionListener(controller);
		
		cNumeroIrrigidimentiAnimaCen.setActionCommand("SET_NIRR_C");
		cNumeroIrrigidimentiAnimaCen.addActionListener(controller);

		cNumeroIrrigidimentiPiattaInfCen.setActionCommand("SET_NIRR_INF_C");
		cNumeroIrrigidimentiPiattaInfCen.addActionListener(controller);
		
		
		setBorder(titledBorderSezione);

		b1.setText("150");
		b2.setText("150");
		h2.setText("8");
		h1.setText("10");
		y1.setText("300");
		y2.setText("800");
		y1C.setText("300");
		y2C.setText("800");
		
		
		
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
		
		addComponent(pIrrLongL, "y1c", "Quota irrig. 1: anime centr.", ly1C, y1C, 0, 5,
				50, 20);
		addComponent(pIrrLongL, "y2c", "Quota irrig. 2: anime centr.", ly2C, y2C, 0, 6,
				50, 20);

		addComponent(pIrrLongL, "nIrr", "Num irrigidimenti Anima",
				lcNumeroIrrigidimentiAnima, cNumeroIrrigidimentiAnima, 0, 7, 50, 20);

		addComponent(pIrrLongL, "nIrrCen", "Num irrig. anime centrali",
				lcNumeroIrrigidimentiAnimaCen, cNumeroIrrigidimentiAnimaCen, 0, 8, 50, 20);

		addComponent(pIrrLongL, "nIrrInf", "Num irrig. piattab. inf. laterali",
				lcNumeroIrrigidimentiPiattaInf, cNumeroIrrigidimentiPiattaInf, 0, 9, 50, 20);
		
		addComponent(pIrrLongL, "nIrrInfCen", "Num irrig. piatt. inf. centrale",
				lcNumeroIrrigidimentiPiattaInfLat, cNumeroIrrigidimentiPiattaInfCen, 0, 10, 50, 20);

		// immagine
		ImageIcon p1 = loadImage("/images/P1.gif");
		addComponent(pIrrLongR, "", "", new JLabel(p1), null, 0, 1, p1
				.getIconHeight(), 50);
		
		resetComboIrrigidimenti();
		setJTxtEnableAnimeLat(cNumeroIrrigidimentiAnima.getSelectedIndex(),true);

	}

	@SuppressWarnings("unchecked") 
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
	public static synchronized PannelloIrrigidimLongCassoneII getInstance() {
		if (inputPanel == null) {
			inputPanel = new PannelloIrrigidimLongCassoneII();
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
			return null;
		}
	}

	public String getValue(String name) {
		JTextField field = (JTextField) listaTextField.get(name);
		return field.getText();
	}

	public void setValue(String name, String value) {
		JTextField field = (JTextField) listaTextField.get(name);
		field.setText(value);
	}

	public void setJTxtEnableAnimeLat(int nIrr, boolean nn){
			h1.setEnabled(nn);
			h2.setEnabled(nn);
			b1.setEnabled(nn);
			b2.setEnabled(nn);
			y1.setEnabled(nIrr>0);
			y2.setEnabled(nIrr>1);
		}
		
	public void setJTxtEnableAnimeCen(int nIrr, boolean nn){
		h1.setEnabled(nn);
		h2.setEnabled(nn);
		b1.setEnabled(nn);
		b2.setEnabled(nn);
		y1C.setEnabled(nIrr>0);
		y2C.setEnabled(nIrr>1);
	}
	
	public void resetComboIrrigidimenti(){
		Progetto prg = Progetto.getInstance();
		Sezione sez= prg.getCurrentSezioneVerifica();
		SezioneMetallicaCassoneII sm = (SezioneMetallicaCassoneII)sez.getSezioneMetallica();
		int nIc = sm.getIrsAnimeCentrali().size();
		int nIl = sm.getIrsAnimeLaterali().size();
		int nIinf = sm.getIrsPiattaInfCentrali().size();
		int nIinfL = sm.getIrsPiattaInfLaterali().size();
		
		
		cNumeroIrrigidimentiAnima.removeActionListener(controller);
		cNumeroIrrigidimentiPiattaInf.removeActionListener(controller);
		cNumeroIrrigidimentiPiattaInfCen.removeActionListener(controller);
		cNumeroIrrigidimentiAnimaCen.removeActionListener(controller);
		

		cNumeroIrrigidimentiAnima.removeAllItems();
		cNumeroIrrigidimentiPiattaInf.removeAllItems();
		cNumeroIrrigidimentiAnimaCen.removeAllItems();
		cNumeroIrrigidimentiPiattaInfCen.removeAllItems();

		for (int i = 0; i < 3; i++) {
			cNumeroIrrigidimentiAnima.addItem(new Integer(i));
			cNumeroIrrigidimentiAnimaCen.addItem(new Integer(i));
		}

		for (int i = 0; i < 6; i++) {
			cNumeroIrrigidimentiPiattaInf.addItem(new Integer(i));
			cNumeroIrrigidimentiPiattaInfCen.addItem(new Integer(i));
		}

		cNumeroIrrigidimentiAnima.setSelectedItem(new Integer(nIl));
		cNumeroIrrigidimentiPiattaInf.setSelectedItem(new Integer(nIinfL));
		cNumeroIrrigidimentiAnimaCen.setSelectedItem(new Integer(nIc));
		cNumeroIrrigidimentiPiattaInfCen.setSelectedItem(new Integer(nIinf));

		cNumeroIrrigidimentiAnima.addActionListener(controller);
		cNumeroIrrigidimentiPiattaInf.addActionListener(controller);
		cNumeroIrrigidimentiAnimaCen.addActionListener(controller);
		cNumeroIrrigidimentiPiattaInfCen.addActionListener(controller);
	
	}

	class ControllerPannello implements ActionListener, ItemListener {

	public ControllerPannello() {
	}

	public void actionPerformed(ActionEvent e) {

		Class cl = e.getSource().getClass();
		JComboBox comboSource = null;

		if (cl == JComboBox.class) {
			comboSource = (JComboBox) e.getSource();
			if (comboSource.getActionCommand() == "SET_NIRR") {
				int ni = (Integer) cNumeroIrrigidimentiAnima.getSelectedItem();
				int ni2 = (Integer) cNumeroIrrigidimentiAnimaCen.getSelectedItem();
				int ni3 = (Integer) cNumeroIrrigidimentiPiattaInf.getSelectedItem();
				int ni4 = (Integer) cNumeroIrrigidimentiPiattaInfCen.getSelectedItem();
				
				setJTxtEnableAnimeLat(ni,ni+ni2+ni3+ni4>0);
				try {
					Parser.validate();
					Progetto.getInstance().ridisegna();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				}
			else if (comboSource.getActionCommand() == "SET_NIRR_INF" || 
					comboSource.getActionCommand() == "SET_NIRR_INF_C") {
				int ni2 = (Integer) cNumeroIrrigidimentiAnima.getSelectedItem();
				int ni = (Integer) cNumeroIrrigidimentiAnimaCen.getSelectedItem();
				int ni3 = (Integer) cNumeroIrrigidimentiPiattaInf.getSelectedItem();
				int ni4 = (Integer) cNumeroIrrigidimentiPiattaInfCen.getSelectedItem();
				
				setJTxtEnableAnimeCen(ni,ni+ni2+ni3+ni4>0);

                            try {
					Parser.validate();
					Progetto.getInstance().ridisegna();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				}
			else if (comboSource.getActionCommand() == "SET_NIRR_C") {
				int ni2 = (Integer) cNumeroIrrigidimentiAnima.getSelectedItem();
				int ni = (Integer) cNumeroIrrigidimentiAnimaCen.getSelectedItem();
				int ni3 = (Integer) cNumeroIrrigidimentiPiattaInf.getSelectedItem();
				int ni4 = (Integer) cNumeroIrrigidimentiPiattaInfCen.getSelectedItem();
				
				setJTxtEnableAnimeCen(ni,ni+ni2+ni3+ni4>0);
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
		return cNumeroIrrigidimentiAnima;
	}


	public JComboBox getCNumeroIrrigidimentiPiattaInf() {
		return cNumeroIrrigidimentiPiattaInf;
	}

	public JComboBox getCNumeroIrrigidimentiAnimaCen() {
		return cNumeroIrrigidimentiAnimaCen;
	}


	public JComboBox getCNumeroIrrigidimentiPiattaInfCen() {
		return cNumeroIrrigidimentiPiattaInfCen;
	}
        
                public void setIndexCNumeroIrrigidimentiPiattaInf(int indx) {
		cNumeroIrrigidimentiPiattaInf.removeActionListener(controller);
                cNumeroIrrigidimentiPiattaInf.setSelectedIndex(indx);
                cNumeroIrrigidimentiPiattaInf.addActionListener(controller);
	}
        public void setIndexcNumeroIrrigidimentiAnima(int indx) {
		cNumeroIrrigidimentiAnima.removeActionListener(controller);
                cNumeroIrrigidimentiAnima.setSelectedIndex(indx);
                cNumeroIrrigidimentiAnima.addActionListener(controller);
	}
        public void setIndexCNumeroIrrigidimentiPiattaInfCen(int indx) {
		cNumeroIrrigidimentiPiattaInfCen.removeActionListener(controller);
                cNumeroIrrigidimentiPiattaInfCen.setSelectedIndex(indx);
                cNumeroIrrigidimentiPiattaInfCen.addActionListener(controller);
	}
        public void setIndexcNumeroIrrigidimentiAnimaCen(int indx) {
		cNumeroIrrigidimentiAnimaCen.removeActionListener(controller);
                cNumeroIrrigidimentiAnimaCen.setSelectedIndex(indx);
                cNumeroIrrigidimentiAnimaCen.addActionListener(controller);
	}

}

