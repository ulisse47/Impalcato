package cassone.view.Giunti;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowEvent;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import cassone.model.Progetto;

public class TabGiunti
        extends /*JInternalFrame*/ JPanel {

    private static TabGiunti mainFrame;
    BorderLayout borderLayout1 = new BorderLayout();

    public static synchronized TabGiunti getInstance() {
        if (mainFrame == null) {
            mainFrame = new TabGiunti();
        }
        return mainFrame;
    }

    //Construct the frame
    private TabGiunti() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {

        //JPanel contentPane;
        InputGiuntoBullonatoSup inputPanel = InputGiuntoBullonatoSup.getInstance();
        InputGiuntoBullonatoInf inputPanel2 = InputGiuntoBullonatoInf.getInstance();
        InputGiuntoBullonatoAnima inputPanel3 = InputGiuntoBullonatoAnima.getInstance();
        InputGiuntiBullonatiGeneral inputPanelGen = InputGiuntiBullonatiGeneral.getInstance();
        BottoniView bottoni = BottoniView.getInstance();
        
        JPanel impalcatoView = SezioneGiunto.getInstance();
        
        TableVerificaGiuntiSup tabs = TableVerificaGiuntiSup.getInstance();
        TableVerificaGiuntiW tabw = TableVerificaGiuntiW.getInstance();
        TableVerificaGiuntiInf tabi= TableVerificaGiuntiInf.getInstance();
        

        JSplitPane spPaneMain = new JSplitPane();
        JSplitPane spPaneInput= new JSplitPane();
        JSplitPane spPaneInputGiunti= new JSplitPane();
        JSplitPane spPaneInputGiuntiLeft= new JSplitPane();
        JSplitPane spPaneLeft = new JSplitPane();
        JSplitPane spPaneTableSup = new JSplitPane();
        JSplitPane spPaneTableInf= new JSplitPane();


        spPaneMain.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        spPaneInput.setOrientation(JSplitPane.VERTICAL_SPLIT);
        spPaneInputGiunti.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        spPaneInputGiuntiLeft.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        spPaneLeft.setOrientation(JSplitPane.VERTICAL_SPLIT);
        spPaneTableInf.setOrientation(JSplitPane.VERTICAL_SPLIT);
        spPaneTableSup.setOrientation(JSplitPane.VERTICAL_SPLIT);
        
        spPaneInputGiuntiLeft.setLeftComponent(inputPanel);
        spPaneInputGiuntiLeft.setRightComponent(inputPanel3);
  
        spPaneInputGiunti.setLeftComponent(spPaneInputGiuntiLeft);
        spPaneInputGiunti.setRightComponent(inputPanel2);
  
        spPaneInput.setTopComponent(inputPanelGen);
        spPaneInput.setBottomComponent(spPaneInputGiunti);
        
        spPaneTableSup.setTopComponent(tabs);
        spPaneTableSup.setBottomComponent(tabw);
        
        spPaneTableInf.setTopComponent(spPaneTableSup);
        spPaneTableInf.setBottomComponent(tabi);
        
        spPaneLeft.setTopComponent(spPaneInput);
        spPaneLeft.setBottomComponent(spPaneTableInf);
        
        spPaneMain.setLeftComponent(spPaneLeft);
        spPaneMain.setRightComponent(impalcatoView);
        spPaneMain.setBackground(Color.black);
  
        //dimensioni
//        inputPanel.setPreferredSize(new Dimension(430, 400));
        int dx=250;
        int dy=230;
        inputPanel.setMinimumSize(new Dimension(dx, dy));
        //inputPanel.setMaximumSize(new Dimension(dx, dy));
        inputPanel2.setMinimumSize(new Dimension(dx, dy));
        //inputPanel2.setMaximumSize(new Dimension(dx, dy));
        inputPanel3.setMinimumSize(new Dimension(dx, dy));
        //inputPanel3.setMaximumSize(new Dimension(dx, dy));
       
        inputPanelGen.setMinimumSize(new Dimension(100, 110));
        //inputPanelGen.setMaximumSize(new Dimension(100, 110));
        
        bottoni.setPreferredSize(new Dimension(700, 80));

        impalcatoView.setPreferredSize(new Dimension(350, 200));
        impalcatoView.setMinimumSize(new Dimension(350, 200));
        impalcatoView.setBackground(Color.black);

        setLayout(borderLayout1);
        add(spPaneMain, BorderLayout.CENTER);
        add(bottoni, BorderLayout.SOUTH);
        
        tabi.setPreferredSize(new Dimension(350, 80));
        tabw.setPreferredSize(new Dimension(350, 80));
        tabs.setPreferredSize(new Dimension(350, 80));
        
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
