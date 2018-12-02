package cassone.view.analisi;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import cassone.controller.ControllerAnalisi;
import cassone.util.Parser;
import cassone.view.table.TableGeoModel;
import java.awt.Color;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class TableOutputView {
  BorderLayout borderLayout1 = new BorderLayout();
  private static JTable table;
  private static JScrollPane scPane;
  private static JDialog frm;

  public static JScrollPane getInstance() {
    if (scPane == null) {
      TableGeoModel myModel = new TableGeoModel(Parser.getRowParametriStatici(),
                                              Parser.getColumnNameParametriStatici());
      table = new JTable(myModel);
      //table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
      scPane = new JScrollPane(table);
      //scPane.setPreferredSize(new Dimension(400, 600));
      table.setPreferredScrollableViewportSize(new Dimension( 300, 50 ));
      
      scPane.setBorder(new TitledBorder(new EtchedBorder(
    			EtchedBorder.RAISED, Color.white, Color.gray), "CARATTERISTICHE SEZIONI"));      
    }

    return scPane;
  }

  public static void refresh(){
   if ( scPane != null && table != null ) {
      table.setModel( new TableGeoModel(Parser.getRowParametriStatici(),
                                       Parser.getColumnNameParametriStatici()));
      scPane.repaint();

    }
  }


  public static void showTableResult() {

    if (frm != null) {
      frm.dispose();
    }

    frm = new JDialog();
    frm.setTitle("Risultati del problema");
    frm.setResizable(false);
    //frm.setSize( new Dimension(  450,600) );
    frm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    TableGeoModel myModel = new TableGeoModel(Parser.getRowParametriStatici(),
                                            Parser.getColumnNameParametriStatici());
    table = new JTable(myModel);
    //table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    scPane = new JScrollPane(table);

    //scPane.setPreferredSize(new Dimension(400, 600));
    table.setPreferredScrollableViewportSize(new Dimension(500, 350));
    frm.getContentPane().add(scPane, BorderLayout.CENTER);
    frm.getContentPane().add(createButtons(), BorderLayout.SOUTH);
    frm.pack();
    frm.setVisible( true );
  }

  private static JPanel createButtons() {
    JPanel pB = new JPanel();
    JButton bStampa = new JButton();
    bStampa.setPreferredSize(new Dimension(60, 25));
    bStampa.setMargin(new Insets(0, 2, 2, 2));
    bStampa.setText("STAMPA");
    bStampa.setActionCommand("STAMPA");
    bStampa.addActionListener(new ControllerAnalisi());
    pB.add(bStampa, null);
    return pB;
  }

}