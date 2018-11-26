package it.ccprogetti.impalcatoGraticcio.nb.actions;

import cassone.controller.ControllerVerificaSezioni;
import it.ccprogetti.impalcatoGraticcio.nb.view.TensioniAction;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;

public final class DeleteSezioneVerificaAction extends CallableSystemAction {
    
    private ControllerVerificaSezioni controller = new ControllerVerificaSezioni();
    
    public void performAction() {
        controller.deleteSezione();
        TensioniAction.open();
    }
    
    public String getName() {
        return NbBundle.getMessage(DeleteSezioneVerificaAction.class, "CTL_DeleteSezioneVerificaAction");
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
