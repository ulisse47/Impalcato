package it.ccprogetti.impalcatoGraticcio.nb.actions;

import cassone.controller.ControllerVerificaGiunti;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;

public final class DeleteGiuntoAction extends CallableSystemAction {
    
   private ControllerVerificaGiunti controller = new ControllerVerificaGiunti();
    
    public void performAction() {
        controller.removeGiunto();
    }
    
    
    public String getName() {
        return NbBundle.getMessage(DeleteGiuntoAction.class, "CTL_DeleteGiuntoAction");
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
