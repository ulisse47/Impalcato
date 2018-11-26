package cassone.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JMenuItem;

import cassone.model.MaterialeAcciaio;
import cassone.model.Progetto;
import cassone.model.Sezione;
import cassone.util.Parser;
import cassone.view.Instabilita.InputInstabilita;
import cassone.view.Instabilita.TableTensioniEfficaci;
import cassone.view.VerificaTensioni.InputVerifiche;
import cassone.view.VerificaTensioni.TableCombView;
import cassone.view.VerificaTensioni.TableCondView;
import cassone.view.VerificaTensioni.TableTensioniView;

public class ControllerVerificaInstabilita implements ActionListener,
		ItemListener {

	public ControllerVerificaInstabilita() {
	}

	public void actionPerformed(ActionEvent e) {

		Class cl = e.getSource().getClass();
		JMenuItem source = null;
		JButton bottomSource = null;
		JComboBox comboSource = null;

		// bottoni
		if (cl == JButton.class) {
			bottomSource = (JButton) e.getSource();
			if (bottomSource.getActionCommand() == "OPEN") {
				open();
			}
			if (bottomSource.getActionCommand() == "SAVE") {
				saveToFile();
			}
			if (bottomSource.getActionCommand() == "ELABORA") {
				elabora();
			}
			if (bottomSource.getActionCommand() == "DISEGNA") {
				disegna();
			}
			if (bottomSource.getActionCommand() == "ADD_IRRIGIDIMENTO") {
				//addIrrigidimento();
			}
			if (bottomSource.getActionCommand() == "REMOVE_IRRIGIDIMENTO") {
				removeIrrigidimento();
			}

			if (bottomSource.getActionCommand() == "STAMPA") {
				stampa();
			}
	

		}
		// combo
		if (cl == JComboBox.class) {
			comboSource = (JComboBox) e.getSource();
			if (comboSource.getActionCommand() == "SET_CONCIO") {
				//setConcio();
			}
			if (comboSource.getActionCommand() == "SET_SEZIONE") {
				setSezione();
			}
			if (comboSource.getActionCommand() == "SET_MATERIALE") {
				setMateriale();
			}
		

		}

	}

	
	private void setMateriale(){
		InputInstabilita inputView = InputInstabilita.getInstance();
		Progetto prg = Progetto.getInstance();
		MaterialeAcciaio selected = (MaterialeAcciaio) inputView.getComboMateriali()
				.getSelectedItem();

		// se problemi..reimposto il corrente
		if (!prg.save()) {
			inputView.getComboMateriali().setSelectedItem(
					prg.getMateriale());
			return;
		}
		
		prg.setMateriale(selected);
		Parser.updateInputView();
		repaint();
	}
	
	/**
	 * 
	 */
	private void setSezione() {
		InputInstabilita inputView = InputInstabilita.getInstance();
		Progetto prg = Progetto.getInstance();
		Sezione selected = (Sezione) inputView.getComboSezione()
				.getSelectedItem();

		// se problemi..reimposto il corrente
		if (!prg.save()) {
			inputView.getComboSezione().setSelectedItem(
					prg.getCurrentSezioneVerifica());
			return;
		}
		// setto la current_sezione = selected
//		prg.setCurrentSezioneVerifica(selected);
		// repaint dell'inputView
		Parser.updateInputView();
		repaint();
	}

	public void removeIrrigidimento() {
		Progetto prg = Progetto.getInstance();
		if (prg.checkInput()) {
			prg.removeIrrigidimento();
			Parser.updateInputView();
			// TableTensioniView.refresh();
			repaint();
		}
	}

	public void addIrrigidimento() {
		Progetto prg =Progetto.getInstance();
		try {
			prg.addIrrigidimento();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void elabora() {
		Progetto prg = Progetto.getInstance();

	    try {
	    	prg.elabora();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void stampa() {
		Progetto prg = Progetto.getInstance();
		elabora();
		prg.writeRFT();
	}

	private void disegna()  {
		Progetto prg = Progetto.getInstance();
		try {
			Parser.validate();
			prg.ridisegna();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	private void repaint() {
		// tab
		TableCondView.refresh();
		TableCombView.refresh();
		TableTensioniView.refresh();
		TableTensioniEfficaci.refresh();
		
		// TableTensIdealiView.refresh();*/
		// TabTensioni.getInstance().repaint();
	}

	/**
	 * Carica i valori del file sulla finestra InputView
	 */
	private void open() {
		Progetto prg = Progetto.getInstance();
		prg.openFile();
		Parser.updateInputView();
		/*
		 * TableCondView.refresh(); TableCombView.refresh();
		 */
		repaint();
	}

	/**
	 * Salva i valori della maschera di input nel modello( Common ) e poi anche
	 * in un file
	 */
	private void saveToFile() {
		// Properties prop;
		Progetto prg = Progetto.getInstance();
		prg.saveToFile();
		// repaint();
	}

	public void itemStateChanged(ItemEvent e) {
		Object source = e.getItemSelectable();
		JCheckBox ck = null;
		InputVerifiche inputPanel = InputVerifiche.getInstance();
		
/*		if (source.getClass() == JCheckBox.class) {
			ck = (JCheckBox) source;
			if (ck.getActionCommand() == "CALCOLO_AUTOMATICO_B") {
				Progetto prg = Progetto.getInstance();
				prg.getCurrentSezioneVerifica().setCalcoloAutomaticoBeff(
						ck.isSelected());
				inputPanel.setEnableTextBoxCalcoloAutomatico(ck.isSelected());	
//				Parser.updateInputView();
			}
			if (ck.getActionCommand() == "CALCOLO_AUTOMATICO_ARMATURA") {
				Progetto prg = Progetto.getInstance();
				prg.getCurrentSezioneVerifica().setCalcolaArmatura(
						ck.isSelected());
				inputPanel.setEnableTextBoxCalcoloArmatura(ck.isSelected());	
				//				Parser.updateInputView();
			}

		}*/

	}

}
