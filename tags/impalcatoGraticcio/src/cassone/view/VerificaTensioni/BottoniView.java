package cassone.view.VerificaTensioni;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JPanel;

import cassone.controller.ControllerVerificaSezioni;

public class BottoniView extends JPanel {
//	JButton jBOpen = new JButton();

//	JButton jBSaveFile = new JButton();

	JButton btDeleteSez = new JButton();

//	JButton btElabora = new JButton();

	JButton btAddSezione = new JButton();

	JButton btRinominaSezioni = new JButton();

	JButton btResetAnalisi= new JButton();

	JButton bStampa = new JButton();

	JButton jBDisegna = new JButton();

	JPanel pButton = new JPanel();

	ControllerVerificaSezioni controller = new ControllerVerificaSezioni();

	BorderLayout borderLayout1 = new BorderLayout();

	JButton btAddCond = new JButton();

	private static BottoniView bottoniView;

	JButton btDeleteCond = new JButton();

	JButton btAddComb = new JButton();

	JButton btDeleteComb = new JButton();

	JButton btRenameCombs = new JButton();
	
	JButton btImportaCond = new JButton();

	public static synchronized BottoniView getInstance() {

		if (bottoniView == null) {
			bottoniView = new BottoniView();
		}
		return bottoniView;
	}

	private BottoniView() {
		super();

		try {
			jbInit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	void jbInit() throws Exception {
		pButton.setRequestFocusEnabled(true);
		pButton.setPreferredSize(new Dimension(380, 80));
		pButton.setFont(new java.awt.Font("Dialog", 0, 11));
		pButton.setMaximumSize(new Dimension(32767, 32767));
		jBDisegna.setText("disegna");
		jBDisegna.setMargin(new Insets(0, 2, 2, 2));
		jBDisegna.setActionCommand("DISEGNA");
		jBDisegna.setFont(new java.awt.Font("SansSerif", 0, 13));
		jBDisegna.setPreferredSize(new Dimension(60, 25));
		bStampa.setFont(new java.awt.Font("SansSerif", 0, 13));
		bStampa.setPreferredSize(new Dimension(60, 25));
		bStampa.setActionCommand("STAMPA");
		bStampa.setMargin(new Insets(0, 2, 2, 2));
		bStampa.setText("stampa");
		btAddSezione.setMargin(new Insets(0, 2, 2, 2));
		btAddSezione.setActionCommand("ADD_SEZIONE");
		btAddSezione.setFocusPainted(true);
		btAddSezione.setFont(new java.awt.Font("SansSerif", 0, 13));
		btAddSezione.setPreferredSize(new Dimension(100, 25));
		btAddSezione.setToolTipText("");
		btAddSezione.setText("nuova sezione");
		btRinominaSezioni.setMargin(new Insets(0, 2, 2, 2));
		btRinominaSezioni.setActionCommand("RINOMINA_SEZIONI");
		btRinominaSezioni.setFocusPainted(true);
		btRinominaSezioni.setFont(new java.awt.Font("SansSerif", 0, 13));
		btRinominaSezioni.setPreferredSize(new Dimension(100, 25));
		btRinominaSezioni.setToolTipText("");
		btRinominaSezioni.setText("Rinomina sezioni");

		btResetAnalisi.setMargin(new Insets(0, 2, 2, 2));
		btResetAnalisi.setActionCommand("RESET_ANALISI");
		btResetAnalisi.setFocusPainted(true);
		btResetAnalisi.setFont(new java.awt.Font("SansSerif", 0, 13));
		btResetAnalisi.setPreferredSize(new Dimension(100, 25));
		btResetAnalisi.setToolTipText("");
		btResetAnalisi.setText("Reset analisi");
		
/*		btElabora.setText("elabora");
		btElabora.setMargin(new Insets(0, 2, 2, 2));
		btElabora.setFont(new java.awt.Font("SansSerif", 0, 13));
		btElabora.setPreferredSize(new Dimension(60, 25));
		btElabora.setActionCommand("ELABORA");
*/		btDeleteSez.setText("elimina sezione ");
		btDeleteSez.setFont(new java.awt.Font("SansSerif", 0, 13));
		btDeleteSez.setPreferredSize(new Dimension(110, 25));
		btDeleteSez.setToolTipText("");
		btDeleteSez.setActionCommand("DELETE_SEZIONE");
		btDeleteSez.setMargin(new Insets(0, 2, 2, 2));
/*		jBSaveFile.setText("salva");
		jBSaveFile.setMargin(new Insets(0, 2, 2, 2));
		jBSaveFile.setFont(new java.awt.Font("SansSerif", 0, 13));
		jBSaveFile.setPreferredSize(new Dimension(60, 25));
		jBSaveFile.setActionCommand("SAVE");
		jBOpen.setText("apri");
		jBOpen.setMargin(new Insets(0, 2, 2, 2));
		jBOpen.setFont(new java.awt.Font("SansSerif", 0, 13));
		jBOpen.setForeground(Color.black);
		jBOpen.setPreferredSize(new Dimension(60, 25));
		jBOpen.setActionCommand("OPEN");
*/	
		this.setLayout(borderLayout1);
		btAddCond.setText("nuova condizione");
		btAddCond.setToolTipText("");
		btAddCond.setFont(new java.awt.Font("SansSerif", 0, 13));
		btAddCond.setPreferredSize(new Dimension(110, 25));
		btAddCond.setActionCommand("ADD_CONDIZIONE");
		btAddCond.setMargin(new Insets(0, 2, 2, 2));
		btDeleteCond.setMargin(new Insets(0, 2, 2, 2));
		btDeleteCond.setActionCommand("DELETE_CONDIZIONE");
		btDeleteCond.setToolTipText("");
		btDeleteCond.setFont(new java.awt.Font("SansSerif", 0, 13));
		btDeleteCond.setPreferredSize(new Dimension(120, 25));
		btDeleteCond.setText("elimina condizione");
		btAddComb.addActionListener(controller);
		btAddComb.setMargin(new Insets(0, 2, 2, 2));
		btAddComb.setActionCommand("ADD_COMBINAZIONE");
		btAddComb.setEnabled(true);
		btAddComb.setFont(new java.awt.Font("SansSerif", 0, 13));
		btAddComb.setPreferredSize(new Dimension(130, 25));
		btAddComb.setToolTipText("");
		btAddComb.setText("nuova combinazione");
		btDeleteComb.addActionListener(controller);
		btDeleteComb.setText("elimina combinazione");
		btDeleteComb.setFont(new java.awt.Font("SansSerif", 0, 13));
		btDeleteComb.setOpaque(true);
		btDeleteComb.setPreferredSize(new Dimension(135, 25));
		btDeleteComb.setToolTipText("");
		btDeleteComb.setActionCommand("DELETE_COMBINAZIONE");
		btDeleteComb.setMargin(new Insets(0, 2, 2, 2));
		btDeleteComb.setRolloverEnabled(false);
		btRenameCombs.addActionListener(controller);
		btRenameCombs.setText("Rinomina combinazioni");
		btRenameCombs.setFont(new java.awt.Font("SansSerif", 0, 13));
		btRenameCombs.setOpaque(true);
		btRenameCombs.setPreferredSize(new Dimension(135, 25));
		btRenameCombs.setToolTipText("");
		btRenameCombs.setActionCommand("RINOMINA_COMBINAZIONI");
		btRenameCombs.setMargin(new Insets(0, 2, 2, 2));
		btRenameCombs.setRolloverEnabled(false);
		
		this.setPreferredSize(new Dimension(380, 100));
		btImportaCond.setRolloverEnabled(false);
		btImportaCond.setMargin(new Insets(0, 2, 2, 2));
		btImportaCond.setActionCommand("INPORT_CONDIZIONI");
		btImportaCond.setToolTipText("");
		btImportaCond.setPreferredSize(new Dimension(135, 25));
		btImportaCond.setOpaque(true);
		btImportaCond.setFont(new java.awt.Font("SansSerif", 0, 13));
		btImportaCond.setText("importa condizioni");
		btImportaCond.addActionListener(controller);
//		pButton.add(jBOpen, null);
//		pButton.add(jBSaveFile, null);
		pButton.add(jBDisegna, null);
//		pButton.add(btElabora, null);
		pButton.add(bStampa, null);
		pButton.add(btAddSezione, null);
		pButton.add(btDeleteSez, null);
		pButton.add(btAddCond, null);
		pButton.add(btDeleteCond, null);
		pButton.add(btAddComb, null);
		pButton.add(btDeleteComb, null);
		pButton.add(btRenameCombs, null);
		pButton.add(btImportaCond, null);
		pButton.add(btRinominaSezioni,null);
		pButton.add(btResetAnalisi,null);
		this.add(pButton, BorderLayout.CENTER);

//		jBOpen.addActionListener(controller);
		jBDisegna.addActionListener(controller);
//		jBSaveFile.addActionListener(controller);
		btAddSezione.addActionListener(controller);
		btRinominaSezioni.addActionListener(controller);
		btResetAnalisi.addActionListener(controller);
		btDeleteSez.addActionListener(controller);
//		btElabora.addActionListener(controller);
		btAddCond.addActionListener(controller);
		btDeleteCond.addActionListener(controller);
		bStampa.addActionListener(controller);

	}
}