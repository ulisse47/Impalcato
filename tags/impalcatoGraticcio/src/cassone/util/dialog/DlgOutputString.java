package cassone.util.dialog;

import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;

import javax.swing.DebugGraphics;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import cassone.model.Campate;
import cassone.model.Progetto;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author Andrea Cavalieri
 * @version 1.0
 */

public class DlgOutputString extends JDialog {
  private JTextArea txtArea = new JTextArea();

  public DlgOutputString(String s, String titolo) throws HeadlessException {

    this.setModal(true);
 
    try {
      jbInit();
      txtArea.setText(s);
      this.setTitle(titolo);
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  
  private void jbInit() throws Exception {
    this.getContentPane().setLayout(null);
    this.setSize(300,250);
    this.setLocation(200,200);
    
    txtArea.setSize(new Dimension(getWidth(),getHeight()));
    this.getContentPane().add(txtArea, null);
    
   }
  
}

