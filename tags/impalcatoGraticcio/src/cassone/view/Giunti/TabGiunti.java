package cassone.view.Giunti;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.WindowEvent;
import javax.swing.BoxLayout;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import cassone.model.Progetto;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

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
        JPanel vistaLaterale = ViewGiuntoVistaLaterale.getInstance();
        JPanel vistaSuperiore = ViewGiuntoPiantaSup.getInstance();
        JPanel vistaInferiore= ViewGiuntoPiantaInferiore.getInstance();
        
        
        TableVerificaGiuntiSup tabs = TableVerificaGiuntiSup.getInstance();
        TableVerificaGiuntiW tabw = TableVerificaGiuntiW.getInstance();
        TableVerificaGiuntiInf tabi= TableVerificaGiuntiInf.getInstance();
        
        JSplitPane spPaneMain = new JSplitPane();
        JSplitPane spPaneSup = new JSplitPane();
        JSplitPane spPaneInf = new JSplitPane();
        JSplitPane spPaneTableSup = new JSplitPane();
        JSplitPane spPaneTableInf= new JSplitPane();
        
        JSplitPane spPaneVista1 = new JSplitPane();
        JSplitPane spPaneVista2= new JSplitPane();


        spPaneMain.setOrientation(JSplitPane.VERTICAL_SPLIT);
        spPaneSup.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        spPaneInf.setOrientation(JSplitPane.VERTICAL_SPLIT);
        spPaneTableInf.setOrientation(JSplitPane.VERTICAL_SPLIT);
        spPaneTableSup.setOrientation(JSplitPane.VERTICAL_SPLIT);
        
        spPaneVista1.setOrientation(JSplitPane.VERTICAL_SPLIT);
        spPaneVista2.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        
        JPanel paneInput = new JPanel();
        paneInput.setLayout( new BoxLayout(paneInput, BoxLayout.Y_AXIS ) );
        
        paneInput.add(inputPanelGen);
        paneInput.add(inputPanel);
        paneInput.add(inputPanel3);
        paneInput.add(inputPanel2);

        JScrollPane jSPinput = new JScrollPane(paneInput);
	jSPinput.setWheelScrollingEnabled(true);
  
        spPaneVista1.setTopComponent(vistaSuperiore);
        spPaneVista1.setBottomComponent(vistaInferiore);

        spPaneVista2.setLeftComponent(vistaLaterale);
        spPaneVista2.setRightComponent(spPaneVista1);
        
        spPaneVista1.setBorder(new LineBorder(Color.DARK_GRAY));
        spPaneVista2.setBorder(new LineBorder(Color.DARK_GRAY));
        
        spPaneSup.setLeftComponent(jSPinput);
        spPaneSup.setRightComponent(spPaneVista2);
        spPaneSup.setBackground(Color.black);
        
        spPaneTableSup.setTopComponent(tabs);
        spPaneTableSup.setBottomComponent(tabw);
        spPaneInf.setTopComponent(spPaneTableSup);
        spPaneInf.setBottomComponent(tabi);
        spPaneInf.setBackground(Color.black);
        
        spPaneMain.setTopComponent(spPaneSup);
        spPaneMain.setBottomComponent(spPaneInf);
  
        int dx=250;
        int dy=260;
        
        inputPanel.setMinimumSize(new Dimension(dx, dy));
        inputPanel.setPreferredSize(new Dimension(dx, dy));
        inputPanel.setMaximumSize(new Dimension(dx, dy));
        
        inputPanel2.setMinimumSize(new Dimension(dx, dy));
        inputPanel2.setPreferredSize(new Dimension(dx, dy));
        inputPanel2.setMaximumSize(new Dimension(dx, dy));
        
        inputPanel3.setMinimumSize(new Dimension(dx, dy));
        inputPanel3.setPreferredSize(new Dimension(dx, dy));
        inputPanel3.setMaximumSize(new Dimension(dx, dy));
       
        inputPanelGen.setMinimumSize(new Dimension(dx, 140));
        inputPanelGen.setPreferredSize(new Dimension(dx, 140));
        inputPanelGen.setMaximumSize(new Dimension(dx, 140));
        
        jSPinput.setMinimumSize(new Dimension(280, dy));
                
        
        tabi.setPreferredSize(new Dimension(350, 35));
        tabw.setPreferredSize(new Dimension(350, 35));
        tabs.setPreferredSize(new Dimension(350, 35));
        tabi.setMinimumSize(new Dimension(350, 1));
        tabw.setMinimumSize(new Dimension(350, 1));
        tabs.setMinimumSize(new Dimension(350, 1));
      
        setLayout(borderLayout1);
        add(spPaneMain, BorderLayout.CENTER);
       
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
