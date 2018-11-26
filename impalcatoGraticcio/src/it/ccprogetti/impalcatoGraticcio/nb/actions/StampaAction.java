package it.ccprogetti.impalcatoGraticcio.nb.actions;

import cassone.controller.ControllerAnalisi;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;

public final class StampaAction extends CallableSystemAction {
    
     
    private ControllerAnalisi controller = new ControllerAnalisi();
    
    public void performAction() {
        controller.stampa();
    }
    
    public String getName() {
        return NbBundle.getMessage(StampaAction.class, "CTL_StampaAction");
    }
    
    protected String iconResource() {
        return "it/ccprogetti/impalcatoGraticcio/nb/actions/print.png";
    }
    
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }
    
    protected boolean asynchronous() {
        return false;
    }
    
}
