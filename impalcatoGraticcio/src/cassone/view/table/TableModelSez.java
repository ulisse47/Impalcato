package cassone.view.table;

import javax.swing.JOptionPane;

public class TableModelSez
    extends MyTableModel {

  /**
   *
   * @param rowData
   * @param header
   */
  public TableModelSez(Object[][] rowData, String[] header) {
    super(rowData, header);
  }

  /**
   *
   * @param aValue
   * @param row
   * @param column
   */
  public void setValueAt(Object aValue, int row, int column) {
    try {
       rowData[row][column] = aValue;
    }
    catch (Exception ex) {
      ex.printStackTrace();
      JOptionPane.showMessageDialog(null, "inserisci valori numerici",
                                    "Warning", JOptionPane.ERROR_MESSAGE);
    }
  }

  /**
   *
   * @param row
   * @param col
   * @return
   */
  public boolean isCellEditable(int row, int col) {
    if (col != 0) {
      return true;
    }
    else {
      return false;
    }
  }

  /**
   *
   * @param rowIndex
   * @param columnIndex
   * @return
   */
  public double getDoubleValueAt(int rowIndex, int columnIndex) {
    Object obj = getValueAt( rowIndex, columnIndex );
    Double d = ( Double )obj;
    return d.doubleValue();
  }

}











