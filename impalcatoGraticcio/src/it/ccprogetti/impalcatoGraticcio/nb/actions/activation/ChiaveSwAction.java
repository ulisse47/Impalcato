/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.ccprogetti.impalcatoGraticcio.nb.actions.activation;

import it.ccprogetti.impalcatoGraticcio.activation.core.StartUpExt;
import javax.swing.JOptionPane;
import org.openide.util.Exceptions;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;

public final class ChiaveSwAction extends CallableSystemAction {

    public void performAction() {
        try {
            StartUpExt.getInstance().togliChiaveSW();
            JOptionPane.showMessageDialog(null, "Programma ora in modalità demo");
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
             JOptionPane.showMessageDialog(null, "Qualche errore è occorso");
       }
    }

    public String getName() {
        return NbBundle.getMessage(ChiaveSwAction.class, "CTL_TogliChiaveAction");
    }

    @Override
    protected void initialize() {
        super.initialize();
        // see org.openide.util.actions.SystemAction.iconResource() Javadoc for more details
        putValue("noIconInMenu", Boolean.TRUE);
    }

    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }
}
