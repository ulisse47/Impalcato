package cassone.view.VerificaTensioni;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import cassone.model.Progetto;
import cassone.util.Parser;
import cassone.view.AbstractDlgInit;

public class DlgInitSezione
    extends AbstractDlgInit {
  JPanel pCenter = new JPanel();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  JLabel lNumSez = new JLabel();
  JTextField tNumSez = new JTextField();
  JLabel lNumeroCondi = new JLabel();
  JTextField tNumCond = new JTextField();
  JPanel pButton = new JPanel();
  JButton bOpenFile = new JButton();
  JButton bOK = new JButton();



  public DlgInitSezione() {
    super();
    jbInit();

  }

  public void jbInit() {
    pCenter.setLayout(gridBagLayout1);
    lNumSez.setFont(new java.awt.Font("Dialog", 0, 11));
    lNumSez.setForeground(Color.black);
    lNumSez.setPreferredSize(new Dimension(100, 15));
    lNumSez.setHorizontalAlignment(SwingConstants.RIGHT);
    lNumSez.setText("numero sezioni");
    tNumSez.setText("1");
    tNumSez.setSelectionStart(11);
    tNumSez.setPreferredSize(new Dimension(150, 21));
    lNumeroCondi.setFont(new java.awt.Font("Dialog", 0, 11));
    lNumeroCondi.setForeground(Color.black);
    lNumeroCondi.setPreferredSize(new Dimension(100, 15));
    lNumeroCondi.setHorizontalAlignment(SwingConstants.RIGHT);
    lNumeroCondi.setText("numero cond. carico");
    tNumCond.setPreferredSize(new Dimension(30, 21));
    tNumCond.setText("1");
    this.setResizable(true);
    this.setTitle("");
    bOpenFile.setHorizontalTextPosition(SwingConstants.TRAILING);
    bOpenFile.setText("Open File");
    bOpenFile.addActionListener( openController );
    bOK.setText("OK");
    bOK.addActionListener( okController );
    pCenter.setAlignmentY( (float) 0.5);
    this.getContentPane().add(pCenter, BorderLayout.NORTH);
    pCenter.add(tNumCond, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.WEST,
                                                  GridBagConstraints.NONE,
                                                  new Insets(0, 0, 5, 5), 5, 5));
    pCenter.add(lNumeroCondi, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.NONE,
        new Insets(0, 0, 0, 10), 5, 5));
    pCenter.add(lNumSez, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
                                                 , GridBagConstraints.CENTER,
                                                 GridBagConstraints.NONE,
                                                 new Insets(0, 0, 0, 10), 5, 5));
    pCenter.add(tNumSez, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
                                                 , GridBagConstraints.EAST,
                                                 GridBagConstraints.NONE,
                                                 new Insets(1, 0, 0, 0), 5, 5));
    this.getContentPane().add(pButton, BorderLayout.CENTER);
    pButton.add(bOK, null);
    pButton.add(bOpenFile, null);
  }


  /**
   *
   * @param e
   */
  public void ok(ActionEvent e) {
    int nSez;
    int nCond;

    try {
      nSez = Integer.parseInt(tNumSez.getText());
      nCond = Integer.parseInt(tNumCond.getText());
    }
    catch (Exception ex) {
      JOptionPane.showMessageDialog(null, "Numero delle sezioni o delle condizioni non valido!",
                                    "Warning!", JOptionPane.WARNING_MESSAGE);
      return;
    }

    //ok
    dispose();
    Progetto prg = Progetto.getInstance();
    //prg.initSezioniCalcolo( nSez , nCond );
    Parser.updateInputView();
    status = OK;
  }


  /**
   *
   * @param e
   */
  public void openFile(ActionEvent e) {
    //ok
    Progetto prg = Progetto.getInstance();
    if( prg.openFile() ){
      dispose();
      Parser.updateInputView();
      status = OK;
    }
  }
}

