package cassone.view.table;

import java.awt.Component;

import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class MyComboBoxRenderer  extends JComboBox
    implements TableCellRenderer {
  public MyComboBoxRenderer( Object[] items ) {
    super(items);
  }

  public Component getTableCellRendererComponent(JTable table, Object value,
                                                 boolean isSelected,
                                                 boolean hasFocus, int row,
                                                 int column) {
    if (isSelected) {
      setForeground(table.getSelectionForeground());
      super.setBackground(table.getSelectionBackground());
    }
    else {
      setForeground(table.getForeground());
      setBackground(table.getBackground());
    }

    // Select the current value
    int val;
    if ( value instanceof Double  ){
      val = (( Double )value).intValue();
      setSelectedItem( new Integer( val ) );
      return this;
    }
    if ( value instanceof Integer  ){
      setSelectedItem( value  );
      return this;
    }
    setSelectedItem( value );
    return this;
  }
}
