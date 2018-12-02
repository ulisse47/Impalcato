package cassone.view.VerificaTensioni;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowEvent;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import cassone.model.Progetto;

public class TabTensioni
        extends /*JInternalFrame versione eclipse*/ JPanel {

    private static TabTensioni mainFrame;
    BorderLayout borderLayout1 = new BorderLayout();

    public static synchronized TabTensioni getInstance() {
        if (mainFrame == null) {
            mainFrame = new TabTensioni();
        }
        return mainFrame;
    }

    //Construct the frame
    private TabTensioni() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {

       // JPanel contentPane;
        InputVerifiche inputPanel = InputVerifiche.getInstance();
        BottoniView bottoni = BottoniView.getInstance();
        JPanel tverifcaView = TensioniVerificaView.getInstance();

        JSplitPane spPaneCenterBottom = new JSplitPane();
        JSplitPane spPaneMain = new JSplitPane();
        JSplitPane spPaneCenter = new JSplitPane();

        spPaneMain.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        spPaneCenterBottom.setOrientation( JSplitPane.HORIZONTAL_SPLIT );
        spPaneCenter.setOrientation(JSplitPane.VERTICAL_SPLIT);

        //setta le dimensioni dei pannelli
        //inputPanel.setPreferredSize(new Dimension(200, 400));
        inputPanel.setMinimumSize( new Dimension(250, 340) );
        //inputPanel.setMaximumSize(new Dimension(200, 500));
        bottoni.setPreferredSize(new Dimension(700, 80));

 
        //tabella delle condizioni
        JScrollPane scPaneCond = TableCondView.getInstance();
        scPaneCond.setPreferredSize(new Dimension(380, 200));
        scPaneCond.setMinimumSize(new Dimension(380, 50));


        //tabella delle combinazioni
        JScrollPane scPaneComb = TableCombView.getInstance();
        scPaneComb.setPreferredSize(new Dimension(380, 200));
        scPaneComb.setMinimumSize(new Dimension(380, 50));
                            
        
        spPaneCenterBottom.setLeftComponent( scPaneCond );
        spPaneCenterBottom.setRightComponent( scPaneComb );
          
        spPaneMain.setLeftComponent( /*spPaneLeft*/inputPanel );
        spPaneMain.setRightComponent( /*spPaneLeftBottom*/tverifcaView  );
        spPaneMain.setBackground(Color.black);
        
        spPaneCenter.setTopComponent( spPaneMain );
        spPaneCenter.setBottomComponent( spPaneCenterBottom/*TableTensioniView.getInstance()*/ );
        
        setLayout(borderLayout1);
        
        add( spPaneCenter, BorderLayout.NORTH );
        add( /*spPaneCenterBottom*/TableTensioniView.getInstance(), BorderLayout.CENTER );
        add( bottoni, BorderLayout.SOUTH);
    }


    //Overridden so we can exit when window is closed
    protected void processWindowEvent(WindowEvent e) {
        //super.processWindowEvent(e);
        int option;
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            option = JOptionPane.showConfirmDialog(null,
                    "vuoi salvare i dati di input?",
                    "Message", 1);
            if (option == JOptionPane.YES_OPTION) {
                Progetto prg = Progetto.getInstance();
                if (prg.saveToFile()) {
                    System.exit(0);
                }
            } else if (option == JOptionPane.NO_OPTION) {
                System.exit(0);
            }

        }
    }
}
