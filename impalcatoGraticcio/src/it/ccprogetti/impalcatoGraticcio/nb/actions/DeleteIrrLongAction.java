package it.ccprogetti.impalcatoGraticcio.nb.actions;

import cassone.controller.ControllerVerificaInstabilita;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;

public final class DeleteIrrLongAction extends CallableSystemAction {
    
    private ControllerVerificaInstabilita controller = new ControllerVerificaInstabilita();
    
    public void performAction() {
        controller.removeIrrigidimento();
    }
    
    public String getName() {
        return NbBundle.getMessage(DeleteIrrLongAction.class, "CTL_DeleteIrrLongAction");
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
