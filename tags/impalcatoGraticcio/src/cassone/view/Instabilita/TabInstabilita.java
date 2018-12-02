package cassone.view.Instabilita;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowEvent;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import cassone.model.Progetto;

public class TabInstabilita
        extends /*JInternalFrame*/ JPanel {

    private static TabInstabilita mainFrame;
    BorderLayout borderLayout1 = new BorderLayout();

    public static synchronized TabInstabilita getInstance() {
        if (mainFrame == null) {
            mainFrame = new TabInstabilita();
        }
        return mainFrame;
    }

    //Construct the frame
    private TabInstabilita() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {

        //JPanel contentPane;
        InputInstabilita inputPanel = InputInstabilita.getInstance();
//        BottoniView bottoni = BottoniView.getInstance();
        JPanel impalcatoView = SezioneIrrigiditaView.getInstance();
    
        JSplitPane spPaneRight = new JSplitPane();
        JSplitPane spPaneLeftBottom = new JSplitPane();
        JSplitPane spPaneLeft = new JSplitPane();
        JSplitPane spPaneMain = new JSplitPane();

        JSplitPane spPaneCenter = new JSplitPane();

        spPaneMain.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        spPaneRight.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        spPaneLeft.setOrientation(JSplitPane.VERTICAL_SPLIT);
        spPaneLeftBottom.setOrientation(JSplitPane.VERTICAL_SPLIT);
        spPaneCenter.setOrientation(JSplitPane.VERTICAL_SPLIT);

        //setta le dimensioni dei pannelli
        //inputPanel.setPreferredSize(new Dimension(400, 400));
        inputPanel.setMinimumSize(new Dimension(350, 30));
        //inputPanel.setMaximumSize(new Dimension(350, 300));
//        bottoni.setPreferredSize(new Dimension(700, 80));

        impalcatoView.setPreferredSize(new Dimension(350, 200));
        impalcatoView.setMinimumSize(new Dimension(350, 30));
        impalcatoView.setBackground(Color.black);

        spPaneCenter.setTopComponent(spPaneMain);
        spPaneCenter.setBottomComponent(TableTensioniEfficaci.getInstance());

        spPaneMain.setLeftComponent(inputPanel);
        spPaneMain.setRightComponent(impalcatoView);
        spPaneMain.setBackground(Color.black);
        setLayout(borderLayout1);
        add(spPaneCenter);
        //contentPane.add( TableTensioniView.getInstance(), BorderLayout.CENTER);
//        add(bottoni, BorderLayout.SOUTH);
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
