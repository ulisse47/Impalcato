package cassone.view.VerificaTensioni;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableModel;

import cassone.util.Parser;
import cassone.view.table.MyTable;
import cassone.view.table.TableSezModel;

public class TableCombView {
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

      TableSezModel myModel = new TableSezModel(Parser.getRowDataComb(),
                                                Parser.getColumnNameComb());
      table = new MyTable(myModel);
 //     setComboColumn();
      table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
      scPane = new JScrollPane(table);
      table.setPreferredScrollableViewportSize(new Dimension(300, 100));
      scPane.setBorder(new TitledBorder(new EtchedBorder(
  			EtchedBorder.RAISED, Color.white, Color.gray), "COMBINAZIONI DI CARICO"));    
    }
    return scPane;
  }

  /**
   *
   */
  private static void setComboColumn() {
   /*
    // These are the combobox values
    Integer[] values = new Integer[8];
    for (int i = 0; i < 8; i++) {
      values[i] = new Integer(i + 1);
    }

    // Set the combobox editor on the 1st visible column
    int vColIndex = 3;
    TableColumn col = table.getColumnModel().getColumn(vColIndex);
    col.setCellEditor(new MyComboBoxEditor(values));

    // If the cell should appear like a combobox in its
    // non-editing state, also set the combobox renderer
    col.setCellRenderer(new MyComboBoxRenderer(values));*/
  }

  public static void refresh() {
    if (table != null) {
      //getInstance();
      table.setModel(new TableSezModel(Parser.getRowDataComb(),
                                       Parser.getColumnNameComb()));
      //setComboColumn();
      scPane.repaint();

    }
  }
  
}


