package cassone.view;

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

public class DlgInit
    extends AbstractDlgInit {


  JPanel pCenter = new JPanel();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  JLabel lTitolo1 = new JLabel();
  JTextField tTitolo1 = new JTextField();
  JLabel lNumeroConci = new JLabel();
  JTextField tNumConci = new JTextField();
  JPanel pButton = new JPanel();
  JButton bOpenFile = new JButton();
  JButton bOK = new JButton();


  public DlgInit() {
    super();
    jbInit();
  }

  public void jbInit() {
    pCenter.setLayout(gridBagLayout1);
    lTitolo1.setFont(new java.awt.Font("Dialog", 0, 11));
    lTitolo1.setForeground(Color.black);
    lTitolo1.setPreferredSize(new Dimension(100, 15));
    lTitolo1.setHorizontalAlignment(SwingConstants.RIGHT);
    lTitolo1.setText("nome progetto");
    tTitolo1.setText("nome");
    tTitolo1.setSelectionStart(11);
    tTitolo1.setPreferredSize(new Dimension(150, 21));
    lNumeroConci.setFont(new java.awt.Font("Dialog", 0, 11));
    lNumeroConci.setForeground(Color.black);
    lNumeroConci.setPreferredSize(new Dimension(100, 15));
    lNumeroConci.setHorizontalAlignment(SwingConstants.RIGHT);
    lNumeroConci.setText("numero conci");
    tNumConci.setPreferredSize(new Dimension(30, 21));
    tNumConci.setText("1");
    this.setResizable(true);
    this.setTitle("");
    bOpenFile.setHorizontalTextPosition(SwingConstants.TRAILING);
    bOpenFile.setText("Open File");
    bOpenFile.addActionListener( openController );
    bOK.setText("OK");
    bOK.addActionListener( okController );
    pCenter.setAlignmentY( (float) 0.5);
    this.getContentPane().add(pCenter, BorderLayout.NORTH);
    pCenter.add(tNumConci, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.WEST,
                                                  GridBagConstraints.NONE,
                                                  new Insets(0, 0, 5, 5), 5, 5));
    pCenter.add(lNumeroConci, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.NONE,
        new Insets(0, 0, 0, 10), 5, 5));
    pCenter.add(lTitolo1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
                                                 , GridBagConstraints.CENTER,
                                                 GridBagConstraints.NONE,
                                                 new Insets(0, 0, 0, 10), 5, 5));
    pCenter.add(tTitolo1, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
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
    int nConci;
    String titolo = null;
    try {
      nConci = Integer.parseInt(tNumConci.getText());
    }
    catch (Exception ex) {
      JOptionPane.showMessageDialog(null, "Numero dei conci non valido!",
                                    "Warning!", JOptionPane.WARNING_MESSAGE);
      return;
    }
    if (tTitolo1.getText() == null) {
      titolo = "";
    }
    else {
      titolo = tTitolo1.getText();
    }

    //ok
    dispose();
    Progetto prg = Progetto.getInstance();
    //prg.init(titolo, nConci);
    Parser.updateInputView();
    status = OK;
  }



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

