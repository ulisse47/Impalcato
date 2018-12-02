package it.ccprogetti.impalcatoGraticcio.nb.view;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;

/**
 * Action which shows VerificaGiunti component.
 */
public class VerificaGiuntiAction extends AbstractAction {
    
    public VerificaGiuntiAction() {
        super(NbBundle.getMessage(VerificaGiuntiAction.class, "CTL_VerificaGiuntiAction"));
//        putValue(SMALL_ICON, new ImageIcon(Utilities.loadImage(VerificaGiuntiTopComponent.ICON_PATH, true)));
    }
    
    public void actionPerformed(ActionEvent evt) {
       open();
    }
    
     public static void open(){
         TopComponent win = VerificaGiuntiTopComponent.findInstance();
        win.open();
        win.requestActive();
    }
    
}
