  package cassone.view.Instabilita;

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

public class TableTensioniEfficaci extends JScrollPane{
  BorderLayout borderLayout1 = new BorderLayout();

  private static JTable table;
  private static TableTensioniEfficaci scPane;

  /**
   *
   * @return
   */
  public static TableModel getRowData(){
    getInstance();
    return table.getModel();
  }

  public TableTensioniEfficaci(Component arg0) {
	super(arg0);
	
}

public JTable getTable(){
	  return table;
  }
  
  public static TableTensioniEfficaci getInstance() {
    if (scPane == null) {
      TableTensModel myModel = new TableTensModel(Parser.getRowDataTensioniEfficaci(),
                                                Parser.getColumnNameTensioniEfficaci());
      table = new JTable(myModel);
      table.getSelectionModel().addListSelectionListener(new RowListener());
      table.setRowSelectionAllowed( true );
      
      //table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
      scPane = new TableTensioniEfficaci(table);
      //scPane.setPreferredSize(new Dimension(400, 600));
      table.setPreferredScrollableViewportSize(new Dimension(300, 100));
      scPane.setBorder(new TitledBorder(new EtchedBorder(
    			EtchedBorder.RAISED, Color.white, Color.gray), "TENSIONI EFFICACI"));   
    }

    return scPane;
  }
  
  
   private static class RowListener implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent event) {
            if (event.getValueIsAdjusting()) {
                return;
            }
            Parser.updateInputVerificaStabilita();
            //TensioniInstabilitaView.getInstance().repaint();
            SezioneIrrigiditaView.getInstance().repaint();
        }
    }

  public static void refresh() {
    if (scPane != null && table != null) {
      table.setModel(new TableTensModel(Parser.getRowDataTensioniEfficaci(),

                                        Parser.getColumnNameTensioniEfficaci()));
      scPane.repaint();
    }


  }
}


