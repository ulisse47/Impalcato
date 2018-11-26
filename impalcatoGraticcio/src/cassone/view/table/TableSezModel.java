package cassone.view.table;

import javax.swing.JOptionPane;

public class TableSezModel
    extends TableGeoModel {

  /**
   *
   * @param rowData
   * @param header
   */
  public TableSezModel(Object[][] rowData, String[] header) {
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

  public Object getValueAt(int rowIndex, int columnIndex) {
    return rowData[rowIndex][columnIndex];
  }


  /**
   *
   * @param row
   * @param col
   * @return
   */
  public boolean isCellEditable(int row, int col) {
//    if (col == 0) 
 //     return false;
   // }
    //else {
      return true;
   // 
  }

  /**
   *
   * @param rowIndex
   * @param columnIndex
   * @return
   */
  public double getDoubleValueAt(int rowIndex, int columnIndex) {
    Double d;
    Integer i;
    Object obj = getValueAt(rowIndex, columnIndex);

    if (obj instanceof Integer) {
      i = (Integer) obj;
      return (double) i.intValue();
    }


    d = (Double) obj;
    return d.doubleValue();
  }

}
