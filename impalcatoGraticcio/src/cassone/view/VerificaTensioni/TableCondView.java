package cassone.view.VerificaTensioni;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import cassone.util.Parser;
import cassone.view.table.MyComboBoxEditor;
import cassone.view.table.MyComboBoxRenderer;
import cassone.view.table.MyTable;
import cassone.view.table.TableSezModel;

public class TableCondView {
  BorderLayout borderLayout1 = new BorderLayout();

  private static JTable table;
  private static JScrollPane scPane;

  /**
   *
   * @return
   */
  public static TableModel getRowData() {
    getInstance();
    return table.getModel();
  }

  public static JScrollPane getInstance() {
    if (table == null) {

      TableSezModel myModel = new TableSezModel(Parser.getRowCondizioniCarico(),
                                                Parser.getColumnNameSollecitazioni());
      table = new MyTable(myModel);
      setComboColumn();
      table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
      scPane = new JScrollPane(table);
      //scPane.setPreferredSize(new Dimension(400, 600));
      table.setPreferredScrollableViewportSize(new Dimension(300, 200));
      scPane.setBorder(new TitledBorder(new EtchedBorder(
  			EtchedBorder.RAISED, Color.white, Color.gray), "CONDIZIONI DI CARICO"));    
     }
    return scPane;
  }

  /**
   *
   */
  private static void setComboColumn() {

    // These are the combobox values
    Integer[] values = new Integer[5];
    for (int i = 0; i < 5; i++) {
      values[i] = new Integer(i);
    }

    // Set the combobox editor on the 1st visible column
    int vColIndex = 1;
    TableColumn col = table.getColumnModel().getColumn(vColIndex);
    col.setCellEditor(new MyComboBoxEditor(values));
 
    // If the cell should appear like a combobox in its
    // non-editing state, also set the combobox renderer
    col.setCellRenderer(new MyComboBoxRenderer(values));
  }
  

  public static void refresh() {
    if ( scPane != null && table != null ) {
      //getInstance();
     table.setModel(new TableSezModel(Parser.getRowCondizioniCarico(),
                                    Parser.getColumnNameSollecitazioni()));
      setComboColumn();
          
      
      scPane.repaint();

    }
  }
}


