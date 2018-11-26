package it.ccprogetti.impalcatoGraticcio.nb.actions;

import cassone.controller.ControllerAnalisi;
import cassone.controller.ControllerVerificaGiunti;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;

public final class ElaboraAction extends CallableSystemAction {
    
    private ControllerAnalisi controller = new ControllerAnalisi();
    private ControllerVerificaGiunti c_giunti = new ControllerVerificaGiunti();
    
    public void performAction() {
        controller.elabora();
//        c_giunti.elabora();
    }
    
    public String getName() {
        return NbBundle.getMessage(ElaboraAction.class, "CTL_ElaboraAction");
    }
    
    protected void initialize() {
        super.initialize();
        // see org.openide.util.actions.SystemAction.iconResource() javadoc for more details
        //putValue("noIconInMenu", Boolean.TRUE);
    }
    
     
     protected String iconResource() {
        return "it/ccprogetti/impalcatoGraticcio/nb/actions/images/elabora.gif";
    }
    
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }
    
    protected boolean asynchronous() {
        return false;
    }
    
}
