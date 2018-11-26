package cassone.view.Instabilita;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import cassone.controller.ControllerVerificaInstabilita;
import cassone.inputPanel.PannelloConcioCassoneII;
import cassone.inputPanel.PannelloConcioDoppioT;
import cassone.inputPanel.PannelloConcioGenerico;
import cassone.inputPanel.PannelloIrrigidimLongCassone;
import cassone.inputPanel.PannelloIrrigidimLongCassoneII;
import cassone.inputPanel.PannelloIrrigidimentoLongDoppioT;
import cassone.inputPanel.PannelloSolettaT;
import cassone.inputPanel.PannelloSolettaType2;
import cassone.model.MaterialeAcciaio;
import cassone.model.Sezione;
import cassone.model.SezioniMetalliche.SezioneMetallica;
import cassone.model.SezioniMetalliche.SezioneMetallicaCassone;
import cassone.model.SezioniMetalliche.SezioneMetallicaCassoneII;
import cassone.model.SezioniMetalliche.SezioneMetallicaDoppioT;
import cassone.model.SezioniMetalliche.SezioneMetallicaGenerica;
import cassone.model.soletta.Soletta;
import cassone.model.soletta.SolettaT;
import cassone.model.soletta.SolettaType2;
import cassone.view.analisi.MyTextField;

public class InputInstabilita extends JPanel {

	private Hashtable listaTextField = new Hashtable();

	private static InputInstabilita inputPanel;

	//GridBagLayout gridBagLayout1 = new GridBagLayout();

//	JLabel lTipoIrrigidimenti = new JLabel();
//	JComboBox cbTipoIrrigidimenti = new JComboBox();

	JLabel lNormative = new JLabel();
	JComboBox cbNormative = new JComboBox();

	JLabel lSezione = new JLabel();
	JComboBox cbSezione = new JComboBox();
	
	JLabel lPiattabSupIrrigidita = new JLabel();
	JCheckBox cbPiattabSupIrrigidita = new JCheckBox();

	JLabel lcbMateriale = new JLabel();
	JComboBox cbMateriale = new JComboBox();

	JLabel lpassoIrrTrasv = new JLabel();
	JTextField passoIrrTrasv = new MyTextField();
	
	JLabel lb1t = new JLabel();
	JTextField b1t = new MyTextField();

	JLabel lh1t = new JLabel();
	JTextField h1t = new MyTextField();

	JLabel lb2t = new JLabel();
	JTextField b2t = new MyTextField();

	JLabel lh2t = new JLabel();
	JTextField h2t = new MyTextField();
	
	JCheckBox ckDoppioIrTrasv = new JCheckBox();
	JLabel lckDoppioIrTrasv= new JLabel();
	
	JCheckBox ckRigidEndPost = new JCheckBox();
	JLabel lckRigidEndPost= new JLabel();
	
	//output
	
	JLabel lNconcio = new JLabel();
	JTextField Nconcio = new MyTextField();
	
	JLabel lMconcio = new JLabel();
	JTextField Mconcio = new MyTextField();
	
	JLabel lVconcio = new JLabel();
	JTextField Vconcio = new MyTextField();
	
	JPanel pIrrLong;
	
        JPanel dxAzioniConcio,dxGeneral;
        
	JPanel container,pAzioniConcio,pIrrTrasv,pGeneral; 
	
	private ControllerVerificaInstabilita controller = new ControllerVerificaInstabilita();

	public InputInstabilita() {
		super();
		try {
			jbInit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public JCheckBox getCkDoppioIrTrasv() {
		return ckDoppioIrTrasv;
	}
	
	public JCheckBox getCkRigidEndPost() {
		return ckRigidEndPost;
	}
	
	
	public JComboBox getComboSezione() {
		return cbSezione;
	}
	
	public JComboBox getComboMateriali(){
		return cbMateriale;
	}

/*	public JComboBox getComboConcio() {
		return cbTipoIrrigidimenti;
	}
*/
	public JComboBox getComboNormative() {
		return cbNormative;
	}

	/**
	 * @return
	 */
	public static synchronized InputInstabilita getInstance() {
		if (inputPanel == null) {
			inputPanel = new InputInstabilita();
		}
		return inputPanel;
	}

	public Iterator getListaFieldNames() {
		return listaTextField.keySet().iterator();
	}

	public String getValue(String name) {
		JTextField field = (JTextField) listaTextField.get(name);
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
			boolean conciogenerico = sez.getSezioneMetallica().getClass()==SezioneMetallicaGenerica.class;
			if(!conciogenerico) cbSezione.addItem(nSezioni.get(i));
		}
		cbSezione.setSelectedItem(current_sezione);
		cbSezione.addActionListener(controller);
	}

	public void resetComboMat(Object current_mat,
			ArrayList<MaterialeAcciaio> materiali) {
		cbMateriale.removeActionListener(controller);
		cbMateriale.removeAllItems();
		for (int i = 0; i < materiali.size(); i++) {
			cbMateriale.addItem(materiali.get(i));
		}
		cbMateriale.setSelectedItem(current_mat);
		cbMateriale.addActionListener(controller);
	}
	
	
	/**
	 * 
	 */
	private void addComponents() {
		
		pGeneral = new JPanel( new GridBagLayout() );
                
                dxGeneral = new JPanel( new BorderLayout());
                dxGeneral.add(pGeneral,BorderLayout.WEST );
		dxGeneral.setBorder( new TitledBorder( new EtchedBorder(
			EtchedBorder.RAISED, Color.white, Color.gray ), "DATI GENERALI") );
                
        	pIrrLong = new PannelloIrrigidimentoLongDoppioT();
		
		pIrrTrasv = new JPanel( new BorderLayout() );
		pIrrTrasv.setBorder( new TitledBorder( new EtchedBorder(
			EtchedBorder.RAISED, Color.white, Color.gray ), "IRRIGIDIMENTO TRASVERSALE") );
		
		JPanel pIrrTrasvL = new JPanel( new GridBagLayout() );
		JPanel pIrrTrasvR = new JPanel( new GridBagLayout() );
		pIrrTrasv.add( pIrrTrasvL, BorderLayout.WEST );
		pIrrTrasv.add( pIrrTrasvR, BorderLayout.CENTER );
		
		pAzioniConcio = new JPanel( new GridBagLayout() );
		dxAzioniConcio =  new JPanel( new BorderLayout());
                dxAzioniConcio.setBorder( new TitledBorder( new EtchedBorder(
			EtchedBorder.RAISED, Color.white, Color.gray ), "AZIONI SU CONCIO METALLICO") );
                dxAzioniConcio.add(pAzioniConcio,BorderLayout.WEST );

					
		container = new JPanel();
		container.setLayout( new BoxLayout( container, BoxLayout.Y_AXIS ) );
		container.add( dxGeneral );
		container.add( pIrrLong );
		container.add( pIrrTrasv );
		container.add( dxAzioniConcio );
				
		JScrollPane pane = new JScrollPane();
		//pane.setWheelScrollingEnabled( false );		
		//pane.setHorizontalScrollBarPolicy( ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );
                pane.getViewport().add( container , null);		
		
		this.add( pane );		
		
		int wTitle = 100;
		int wBox = 50;
		int h = 20;
		
		//DATI GENERALI
		addComponentForm(pGeneral, "sezione", "Sezione", lSezione, cbSezione, h, wBox + 40, 0, 0);
		addComponentForm(pGeneral, "normativa", "Normativa", lNormative, cbNormative,h, wBox + 40, 0, 1);
		addComponentForm(pGeneral, "cbMateriale", "Acciaio", lcbMateriale, cbMateriale, h, wBox + 40,0, 2);
		addComponentForm(pGeneral, "cbPSupIrrigidita", "Piattabanda sup irrigidita", lPiattabSupIrrigidita, cbPiattabSupIrrigidita, h, wBox + 40,0, 3);

		addComponentForm(pIrrTrasvL, "passsoTrasv", "Interasse irrigid. trasv.", lpassoIrrTrasv, passoIrrTrasv, h, wBox,0, 1);
		addComponentForm(pIrrTrasvL, "b1t", "Pannello1: b1xh1 (mmxmm)", lb1t, b1t, h, wBox,0, 2 );
		addComponentForm(pIrrTrasvL, "h1t", "", lh1t, h1t,h, wBox, 1, 2);
		addComponentForm(pIrrTrasvL, "b2t", "Pannello 2: b2xh2 (mmxmm)", lb2t, b2t, h, wBox,0, 3);
		addComponentForm(pIrrTrasvL, "h2t", "", lh2t, h2t,h, wBox,1, 3);
		addComponentForm(pIrrTrasvL, "ckSoppioTrasv", "Irrigidimento doppio", lckDoppioIrTrasv, ckDoppioIrTrasv, h, wBox,0, 4);
		addComponentForm(pIrrTrasvL, "ckRigidEndPost", "Rigid End Post", lckRigidEndPost, ckRigidEndPost, h, wBox,0, 5);
		
		//immagine
		ImageIcon p2 =  loadImage( "/images/P2.gif" );
		addComponentForm(pIrrTrasvR, "", "", new JLabel( p2 ), null, p2.getIconHeight(), wBox, 0, 1);		
					
		addComponentForm(pAzioniConcio, "Mconcio", "Momento trave acciaio: kNm", lMconcio, Mconcio,h, wBox + 30 , 0, 1);
		addComponentForm(pAzioniConcio, "Nconcio", "Azione assiale trave acciaio: kN", lNconcio, Nconcio,h, wBox + 30, 0, 2);
		addComponentForm(pAzioniConcio, "Vconcio", "Taglio trave acciaio: kN", lVconcio, Vconcio, h, wBox + 30,0, 3);
		
	}
	
	private ImageIcon loadImage( String localUrl ){
		
		try {
			 BufferedImage bf = ImageIO.read( getClass().getResourceAsStream( localUrl ) );
			 ImageIcon img = new ImageIcon( bf );
			 return img;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * @param title
	 * @param lBs1
	 * @param bs1
	 * @param height TODO
	 * @param gridx
	 * @param gridy
	 */
	private void addComponentForm(JPanel pInput, String fieldName, String title,
			JLabel lBs1, JComponent bs1, int height, int witdthBox,  int gridx, int gridy) {
		
		
		
		if ( bs1 != null )  {
			listaTextField.put(fieldName, bs1);
			bs1.setPreferredSize( new Dimension( witdthBox , height ) );
			pInput.add(bs1, new GridBagConstraints( gridx + 1, gridy, 1, 1, 0.0,
					0.0, GridBagConstraints.LINE_START, GridBagConstraints.NONE,
					new Insets(1, 1, 0, 0), 0, 0));			
		}
		
		lBs1.setHorizontalAlignment( SwingConstants.LEFT );
		lBs1.setText(title);
		
		pInput.add(lBs1, new GridBagConstraints(gridx, gridy, 1, 1, 0.0, 0.0,
				GridBagConstraints.LINE_START , GridBagConstraints.NONE, new Insets(
						0, 0, 0, 0), 0, 0));	

	}

	/**
	 * 
	 * @throws java.lang.Exception
	 */
	void jbInit() throws Exception {
		
		this.setLayout( new BorderLayout() );
		
		addComponents();

		Mconcio.setEditable(false);
		Nconcio.setEditable(false);
		Vconcio.setEditable(false);
		
		// combo sezione
		cbSezione.addActionListener(controller);
		cbSezione.setActionCommand("SET_SEZIONE");
		
		cbMateriale.addActionListener(controller);
		cbMateriale.setActionCommand("SET_MATERIALE");

		
		ckDoppioIrTrasv.setActionCommand("SET_DOPPIO_IRR_TRASV");
		ckDoppioIrTrasv.addActionListener(controller);


		// combo concio
		cbNormative.setActionCommand("SET_NORMATIVA");
		cbNormative.addActionListener(controller);

		fillComboNormative();

	}

	private void fillComboNormative() {
		
		if(cbNormative ==null) cbNormative = new JComboBox();

		cbNormative.removeActionListener(controller);
		cbNormative.removeAllItems();
		cbNormative.addItem("EN 1993-1-5");
//		cbNormative.addItem("CNR 10030/87");
		cbNormative.setSelectedIndex(0);
		cbNormative.addActionListener(controller);

	}

	
	void imposta() throws Exception {
		
		
		container.removeAll();
		container.add( dxGeneral );
		container.add( pIrrLong );
		container.add( pIrrTrasv );
		container.add( dxAzioniConcio );
				
		this.repaint();
		
	}
	
	public void aggiornaPannelli(Sezione sez ){
		JPanel pn=null;
		SezioneMetallica sm = sez.getSezioneMetallica();
		if(sm.getClass()==SezioneMetallicaDoppioT.class){
			pn = new PannelloIrrigidimentoLongDoppioT();
		}
		else if(sm.getClass()==SezioneMetallicaCassone.class){
			pn = new PannelloIrrigidimLongCassone();
		}

		else if(sm.getClass()==SezioneMetallicaCassoneII.class){
			pn = new PannelloIrrigidimLongCassoneII();
		}
		else if(sm.getClass()==SezioneMetallicaGenerica.class){
			pn = new JPanel();
		}
                
		setPIrrLong(pn);
	
	}
	
	public JPanel getPIrrLong() {
		return pIrrLong;
	}

	public void setPIrrLong(JPanel irrLong) {
			this.pIrrLong = irrLong;
			try {
				imposta();
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	public JCheckBox getCbPiattabSupIrrigidita() {
		return cbPiattabSupIrrigidita;
	}

	public void setCbPiattabSupIrrigidita(JCheckBox cbPiattabSupIrrigidita) {
		this.cbPiattabSupIrrigidita = cbPiattabSupIrrigidita;
	}

}
