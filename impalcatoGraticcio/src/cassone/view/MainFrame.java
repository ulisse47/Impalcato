package cassone.view;

import cassone.util.Parser;
import java.awt.BorderLayout;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import cassone.model.Progetto;
import cassone.view.Giunti.TabGiunti;
import cassone.view.Instabilita.TabInstabilita;
import cassone.view.VerificaTensioni.TabTensioni;
import cassone.view.analisi.TabGeo;
import it.ccprogetti.impalcatoGraticcio.activation.core.StartUpExt;

public class MainFrame extends JFrame {
  JTabbedPane tab = new JTabbedPane();
  BorderLayout borderLayout1 = new BorderLayout();

  public MainFrame() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }

  }
  private void jbInit() throws Exception {
    this.getContentPane().setLayout(borderLayout1);
    this.setTitle("IMALCATO A GRATICCIO");
    this.getContentPane().add(tab, BorderLayout.CENTER);
    
    tab.add( TabGeo.getInstance() , "geometria");
    tab.add( TabTensioni.getInstance() , "tensioni");
    tab.add( TabInstabilita.getInstance(), "VerificaInstabilità");
    tab.add(TabGiunti.getInstance(),"Verifica giunti bullonati");
    Parser.updateInputView();
   
  }


  //Overridden so we can exit when window is closed
  protected void processWindowEvent(WindowEvent e) {
    //super.processWindowEvent(e);
    int option;
    if (e.getID() == WindowEvent.WINDOW_CLOSING) {
        
       if (Progetto.getInstance().getMode() == StartUpExt.DEMO) {
          System.exit(0);
        } 
 
        
      option = JOptionPane.showConfirmDialog(null,
                                             "vuoi salvare i dati di input?",
                                             "Message", 1);
      if (option == JOptionPane.YES_OPTION) {
        Progetto prg = Progetto.getInstance();
        if (prg.saveToFile()) {
          System.exit(0);
        }
      }
      else if (option == JOptionPane.NO_OPTION) {
        System.exit(0);
      }

    }
  }


}