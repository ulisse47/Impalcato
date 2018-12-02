package cassone.view.table;

import java.awt.Component;

import javax.swing.DefaultCellEditor;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;

public class TextFieldEditor
    extends DefaultCellEditor
    implements TableCellEditor {

// This is the component that will handle the editing of the cell value
  JComponent component = new JTextField();

  public TextFieldEditor() {
    super(new JTextField());
  }

// This method is called when a cell value is edited by the user.
  public Component getTableCellEditorComponent(JTable table, Object value,
                                               boolean isSelected,
                                               int rowIndex,
                                               int vColIndex) {
// 'value' is value contained in the cell located at (rowIndex, vColIndex)

    if (isSelected) {

      ( (JTextField) component).setText("");
      return component;
      // cell (and perhaps other cells) are selected
    }

// Configure the component with the specified value
    else {
      return super.getTableCellEditorComponent(table, value,
                                               isSelected,
                                               rowIndex,
                                               vColIndex);
    }

// Return the configured component
    //return component;
  }

// This method is called when editing is completed.
// It must return the new value to be stored in the cell.
  public Object getCellEditorValue() {
    //return ( (JTextField) component).getText();
    String value =    ( (JTextField) component).getText();
    try {
      return new Double(Double.parseDouble(value));
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    return super.getCellEditorValue();
  }

}