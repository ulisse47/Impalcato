  package cassone.view.Giunti;

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

public class TableVerificaGiuntiInf extends JScrollPane{
  BorderLayout borderLayout1 = new BorderLayout();

  private static JTable table;
  private static TableVerificaGiuntiInf scPane;

  /**
   *
   * @return
   */
  public static TableModel getRowData(){
    getInstance();
    return table.getModel();
  }

  public TableVerificaGiuntiInf(Component arg0) {
	super(arg0);
	
}

public JTable getTable(){
	  return table;
  }
  
  public static TableVerificaGiuntiInf getInstance() {
    if (scPane == null) {
      TableTensModel myModel = new TableTensModel(Parser.getRowDataBulloniInf(),
                                                Parser.getColumnNameVerificaBulloni());
      table = new JTable(myModel);
      //table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
      scPane = new TableVerificaGiuntiInf(table);
      //scPane.setPreferredSize(new Dimension(400, 600));
      table.setPreferredScrollableViewportSize(new Dimension(300, 100));
      scPane.setBorder(new TitledBorder(new EtchedBorder(
    			EtchedBorder.RAISED, Color.white, Color.gray), "TENSIONI ALA INFERIORE"));  
    }

    return scPane;
  }

  public static void refresh() {
    if (scPane != null && table != null) {
      table.setModel(new TableTensModel(Parser.getRowDataBulloniInf(),
              Parser.getColumnNameVerificaBulloni()));
      scPane.repaint();
    }


  }
}


