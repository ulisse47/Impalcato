package it.ccprogetti.impalcatoGraticcio.nb.actions;

import cassone.controller.ControllerVerificaGiunti;
import it.ccprogetti.impalcatoGraticcio.nb.view.VerificaGiuntiAction;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;

public final class AddGiuntoAction extends CallableSystemAction {
    
     
    private ControllerVerificaGiunti controller = new ControllerVerificaGiunti();
    
    public void performAction() {
        controller.addGiunto();
        VerificaGiuntiAction.open();
    }
    
     
    public String getName() {
        return NbBundle.getMessage(AddGiuntoAction.class, "CTL_AddGiuntoAction");
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
