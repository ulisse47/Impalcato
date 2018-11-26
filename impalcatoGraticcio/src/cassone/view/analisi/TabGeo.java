package cassone.view.analisi;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowEvent;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import cassone.model.Progetto;

public class TabGeo extends JPanel {

	private static TabGeo mainFrame;

	BorderLayout borderLayout1 = new BorderLayout();

	public static synchronized TabGeo getInstance() {
		if (mainFrame == null) {
			mainFrame = new TabGeo();
		}
		return mainFrame;
	}

	// Construct the frame
	private TabGeo() {
		super();
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception {

		InputView inputPanel = InputView.getInstance();
		JScrollPane jimSc = new JScrollPane(inputPanel);
		JPanel sezioneTipoView = SezioneTipoView.getInstance();
		JPanel campateView = CampateView.getInstance();
                JScrollPane to = TableOutputView.getInstance();
		
		
		JSplitPane spPane = new JSplitPane();
		JSplitPane spPane_bottom = new JSplitPane();
		JSplitPane spPaneCenter = new JSplitPane();
			
		spPaneCenter.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		spPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		spPane_bottom.setOrientation(JSplitPane.VERTICAL_SPLIT);

		// setta le dimensioni dei pannelli
		//inputPanel.setPreferredSize(new Dimension(380, 200));
		inputPanel.setMinimumSize(new Dimension(300, 200));
		//inputPanel.setMaximumSize(new Dimension(380, 200));
		sezioneTipoView.setPreferredSize(new Dimension(400, 200));
		sezioneTipoView.setMinimumSize(new Dimension(50, 30));
		campateView.setPreferredSize(new Dimension(400, 80));
		campateView.setMinimumSize(new Dimension(50, 20));
                to.setPreferredSize(new Dimension(400, 100));
		
		spPane.setBackground(Color.BLACK);
		spPane.setTopComponent(sezioneTipoView);
		spPane.setBottomComponent(campateView);
				
		spPane_bottom.setTopComponent( spPane );
		spPane_bottom.setBottomComponent(to);
		spPane_bottom.setBackground(Color.BLACK);
			
 //                jimSc.add(inputPanel);
		spPaneCenter.setLeftComponent(jimSc);
		spPaneCenter.setRightComponent(spPane_bottom);

		setLayout( borderLayout1 );
		add(spPaneCenter);

	}

	// Overridden so we can exit when window is closed
	protected void processWindowEvent(WindowEvent e) {
		// super.processWindowEvent(e);
		int option;
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			option = JOptionPane.showConfirmDialog(null,
					"vuoi salvare i dati di input?", "Message", 1);
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
