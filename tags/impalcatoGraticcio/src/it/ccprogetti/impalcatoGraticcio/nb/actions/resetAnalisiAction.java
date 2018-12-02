package it.ccprogetti.impalcatoGraticcio.nb.actions;

import cassone.controller.ControllerAnalisi;
import cassone.controller.ControllerVerificaSezioni;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;

public final class resetAnalisiAction extends CallableSystemAction {
    
   private ControllerVerificaSezioni controller = new ControllerVerificaSezioni();

    public void performAction() {
        controller.resetAnalisi();
    }
    
    public String getName() {
        return NbBundle.getMessage(resetAnalisiAction.class, "CTL_resetAnalisiAction");
    }
    
    protected void initialize() {
        super.initialize();
        // see org.openide.util.actions.SystemAction.iconResource() javadoc for more details
        putValue("noIconInMenu", Boolean.TRUE);
    }
    
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }
    
    protected boolean asynchronous() {
        return false;
    }
    
}
