package it.ccprogetti.impalcatoGraticcio.nb.view;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;

/**
 * Action which shows Geometria component.
 */
public class GeometriaAction extends AbstractAction {
    
    public GeometriaAction() {
        super(NbBundle.getMessage(GeometriaAction.class, "CTL_GeometriaAction"));
//        putValue(SMALL_ICON, new ImageIcon(Utilities.loadImage(GeometriaTopComponent.ICON_PATH, true)));
    }
    
    public void actionPerformed(ActionEvent evt) {
       open();
    }
    
    public static void open(){
        TopComponent win = GeometriaTopComponent.findInstance();
        win.open();
        win.requestActive();
    }
    
}
