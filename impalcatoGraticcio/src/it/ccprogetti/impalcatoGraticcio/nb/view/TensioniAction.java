package it.ccprogetti.impalcatoGraticcio.nb.view;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;

/**
 * Action which shows Tensioni component.
 */
public class TensioniAction extends AbstractAction {
    
    public TensioniAction() {
        super(NbBundle.getMessage(TensioniAction.class, "CTL_TensioniAction"));
//        putValue(SMALL_ICON, new ImageIcon(Utilities.loadImage(TensioniTopComponent.ICON_PATH, true)));
    }
    
    public void actionPerformed(ActionEvent evt) {
       open();
    }
    
    public static void open(){
        TopComponent win = TensioniTopComponent.findInstance();
        win.open();
        win.requestActive();
    }
    
}
