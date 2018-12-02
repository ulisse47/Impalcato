package it.ccprogetti.impalcatoGraticcio.nb.actions;

import cassone.controller.ControllerAnalisi;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;

public final class modificaConciAction extends CallableSystemAction {
    
   private ControllerAnalisi controller = new ControllerAnalisi();

    public void performAction() {
        controller.modificaConci();
    }
    
    public String getName() {
        return NbBundle.getMessage(modificaConciAction.class, "CTL_modificaConciAction");
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
