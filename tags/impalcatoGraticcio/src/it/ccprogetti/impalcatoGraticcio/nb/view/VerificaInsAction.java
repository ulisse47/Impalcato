package it.ccprogetti.impalcatoGraticcio.nb.view;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import org.openide.windows.TopComponent;

/**
 * Action which shows VerificaIns component.
 */
public class VerificaInsAction extends AbstractAction {
    
    public VerificaInsAction() {
        super(NbBundle.getMessage(VerificaInsAction.class, "CTL_VerificaInsAction"));
//        putValue(SMALL_ICON, new ImageIcon(Utilities.loadImage(VerificaInsTopComponent.ICON_PATH, true)));
    }
    
    public void actionPerformed(ActionEvent evt) {
        TopComponent win = VerificaInsTopComponent.findInstance();
        win.open();
        win.requestActive();
    }
    
}
