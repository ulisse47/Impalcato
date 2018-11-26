package cassone.view.table;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;

public class MyTable extends JTable {

        public MyTable( TableModel model ){
                super( model );
        }
        public Component prepareEditor(TableCellEditor editor, int row, int col)
        {
          Component result = super.prepareEditor(editor, row, col);
          if (result instanceof JTextField)
          {
                ((JTextField)result).selectAll();
                ((JTextField)result).requestFocus();
          }
          return result;
        }


}
