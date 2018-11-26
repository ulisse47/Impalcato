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

public class DlgModificaLuci extends JDialog {
  private TabellaLuci jTable1;
  private JButton jButtonOk = new JButton();
  private JButton jButtonCancel = new JButton();
//  private JButton jButtonAdd = new JButton();
  
//  private Struttura str;

  public DlgModificaLuci() throws HeadlessException {
//    progettoView = mPrg;
 //   grafica = gr;
 //   str = (Struttura)progettoView.getStrutturaCorrente().clone();

//    if (progettoView.getRunned()) jButtonOk.setEnabled(false);

    this.setModal(true);
    jTable1 =  new TabellaLuci();
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  
  private void jbInit() throws Exception {
    this.getContentPane().setLayout(null);
    this.setSize(300,250);
    this.setLocation(200,200);
    jTable1.setDebugGraphicsOptions(DebugGraphics.FLASH_OPTION);
    jTable1.setVerifyInputWhenFocusTarget(false);
    jTable1.setBounds(new Rectangle(20, 30, 100, 30));
    jButtonOk.setBounds(new Rectangle(180, 90, 85, 20));
    jButtonCancel.setBounds(new Rectangle(180, 115, 85, 20));
//    jButtonAdd.setBounds(new Rectangle(180, 115, 85, 20));
    jButtonOk.setText("OK");
    jButtonOk.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jButtonOk_actionPerformed(e);
      }
    });
    jButtonCancel.setText("CANCEL");
 //   jButtonAdd.setText("AGGIUNGI");
    
    jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jButtonCancel_actionPerformed(e);
      }
    });
    
 /*   jButtonAdd.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(ActionEvent e) {
          jButtonAdd_actionPerformed(e);
        }
      });
*/
    this.getContentPane().add(jTable1, null);
    this.getContentPane().add(jButtonCancel, null);
 //   this.getContentPane().add(jButtonAdd, null);
    this.getContentPane().add(jButtonOk, null);
    
    jTable1.setVisible(true);
  }


  private class TabellaLuci extends JTable{
    private Object[][] data;
    private int nC;

    public TabellaLuci() {
    	Progetto prg = Progetto.getInstance();
    	
      final String[] names = {"CAMPATA", "LUCI"};
      Campate cam = prg.getCampate();
      nC = cam.getNCampate();
      data = new Object[nC][2];
      for(int i = 0; i < nC; i++){
        data[i][0] = new Integer(i+1);
       data[i][1] = new Double(cam.getLuci()[i]);
      }
      // Create a model of the data.
      TableModel dataModel = new AbstractTableModel() {
        // These methods always need to be implemented.
        public int getColumnCount() { return names.length; }
        public int getRowCount() { return data.length;}
        public Object getValueAt(int row, int col) {return data[row][col];}

        // The default implementations of these methods in
        // AbstractTableModel would work, but we can refine them.
        public String getColumnName(int column) {return names[column];}
        public Class getColumnClass(int c) {return getValueAt(0, c).getClass();}
        public boolean isCellEditable(int row, int col) {
            return (col==1);}
        public void setValueAt(Object aValue, int row, int column) {
          try{
            data[row][column] = aValue;
          }catch(Exception ex){
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "inserisci valori numerici",
                "Warning", JOptionPane.ERROR_MESSAGE);
          }
        }
        public void aggiornaTabella(){
          	Progetto prg = Progetto.getInstance();
        	final String[] names = {"CAMPATA", "LUCI"};
            Campate cam = prg.getCampate();
            nC = cam.getNCampate();
            data = new Object[nC][2];
            for(int i = 0; i < nC; i++){
              data[i][0] = new Integer(i+1);
             data[i][1] = new Double(cam.getLuci()[i]);
            }
    }
        
      };
      // Create the table
      JTable tableView = new JTable(dataModel);
      // Turn off auto-resizing so that we can set column sizes programmatically.
      // In this mode, all columns will get their preferred widths, as set blow.
      tableView.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

      // Finish setting up the table.
      JScrollPane scrollpane = new JScrollPane(tableView);
      scrollpane.setBorder(new BevelBorder(BevelBorder.LOWERED));
      scrollpane.setPreferredSize(new Dimension(170, 160));
      getContentPane().add(scrollpane);
      pack();
    }

  }

  void jButtonOk_actionPerformed(ActionEvent e) {
 
	  
	  Double[] dataTmp = new Double[100];
    try{
    	Progetto prg = Progetto.getInstance();
    	Campate c = prg.getCampate();
      for(int i =0; i < jTable1.nC; i++){
        dataTmp[i] = (Double)jTable1.data[i][1];
        c.setLuce(i,dataTmp[i]);
      }
      this.dispose();
      }catch(Exception ex){
        JOptionPane.showMessageDialog(null, "non è stato possibile modificare le luci",
                "Warning", JOptionPane.ERROR_MESSAGE);
      }
  }

  void jButtonCancel_actionPerformed(ActionEvent e) {
    this.dispose();
  }
  void jButtonAdd_actionPerformed(ActionEvent e) {
	    
	  Progetto prg = Progetto.getInstance();
	  Campate cam = prg.getCampate();
	  cam.setNCampate(cam.getNCampate()+1);
//	  jTable1.();
//	  this.repaint();
  }
}

