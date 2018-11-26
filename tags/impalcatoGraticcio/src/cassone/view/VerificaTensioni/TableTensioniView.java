package cassone.view.VerificaTensioni;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableModel;

import cassone.util.Parser;
import cassone.view.table.TableTensModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;



public class TableTensioniView extends JScrollPane{
    BorderLayout borderLayout1 = new BorderLayout();
    
    private static JTable table;
    private static TableTensioniView scPane;
    
    /**
     *
     * @return
     */
    public static TableModel getRowData(){
        getInstance();
        return table.getModel();
    }
    public TableTensioniView(Component arg0) {
        super(arg0);
        
    }
    public static TableTensioniView getInstance() {
        if (scPane == null) {
            TableTensModel myModel = new TableTensModel(Parser.getRowDataTensioni(),
                    Parser.getColumnNameTensioni());
            table = new JTable(myModel);
            //aggiungo listener quando seleziono una riga
            table.getSelectionModel().addListSelectionListener(new RowListener());
            table.setRowSelectionAllowed( true );
            
            //table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
            scPane = new TableTensioniView(table);
            scPane.setPreferredSize(new Dimension(400, 100));
            table.setPreferredScrollableViewportSize(new Dimension(300, 100));
            
            scPane.setBorder(new TitledBorder(new EtchedBorder(
                    EtchedBorder.RAISED, Color.white, Color.gray), "TENSIONI"));
        }
        
        return scPane;
    }
    
    
    private static class RowListener implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent event) {
            if (event.getValueIsAdjusting()) {
                return;
            }
            TensioniVerificaView.getInstance().repaint();
        }
    }
    
    
    
    public static void refresh() {
        if (scPane != null && table != null) {
            table.setModel(new TableTensModel(Parser.getRowDataTensioni(),
                    
                    Parser.getColumnNameTensioni()));
            scPane.repaint();
        }
        
        
    }
    public JTable getTable(){
        return table;
    }
    
}


