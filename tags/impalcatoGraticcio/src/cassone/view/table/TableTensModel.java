package cassone.view.table;

import java.text.NumberFormat;

import javax.swing.table.AbstractTableModel;


public class TableTensModel extends AbstractTableModel {

  protected Object[][] rowData;
  protected String[] header;
  protected NumberFormat nf;

  public TableTensModel( Object[][] rowData, String[] header ) {
    this.rowData = rowData;
    this.header = header;
    nf = NumberFormat.getInstance();
    nf.setMaximumFractionDigits(2);
  }
 

  public Object[][] getRowData(){ return rowData; }

  public boolean isCellEditable(int row, int col) {
    return false;
  }

  public int getRowCount() {
    return rowData.length;
  }
  public int getColumnCount() {
   return header.length;
  }

  public Object getValueAt(int rowIndex, int columnIndex) {
	    Object obj = rowData[rowIndex][columnIndex];
	    if ( obj == null ){ return "null";}

	    double val;
	    if ( obj instanceof Double ){
	      val = ( ( Double )obj ).doubleValue();
	      if ( Double.isNaN( val ) ){
	        return "not valid";
	      }
	      return new Double ( val );
	    }
	    return rowData[rowIndex][columnIndex];
	  }
  
  
 /* public Object getValueAt(int rowIndex, int columnIndex) {
    Object obj = rowData[rowIndex][columnIndex];
    if ( obj == null ){ return "null";}

    double val;
    if ( obj instanceof Double ){
      return  new Double ( val );
      if ( Double.isNaN( val ) ){
        return "not valid";
      }
      return new Double ( val );
    }
    return rowData[rowIndex][columnIndex];
  }
*/
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
  
  /**
   *
   * @param c
   * @return
   */
  public Class getColumnClass(int c) {
      return getValueAt(0, c).getClass();
  }

  public String getColumnName(int column) {
    return " " + header[ column ];
  }

}

