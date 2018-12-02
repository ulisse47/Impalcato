package it.ccprogetti.impalcatoGraticcio.nb.actions;

import cassone.controller.ControllerAnalisi;
import it.ccprogetti.impalcatoGraticcio.nb.view.GeometriaAction;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;

public final class AddSezioneAction extends CallableSystemAction {
    
    private ControllerAnalisi controller = new ControllerAnalisi();
    
    public void performAction() {
        controller.addSezioneAnalisi();
         GeometriaAction.open();
    }
    
        
    public String getName() {
        return NbBundle.getMessage(AddSezioneAction.class, "CTL_AddSezioneAction");
    }
    
    protected void initialize() {
        super.initialize();
        // see org.openide.util.actions.SystemAction.iconResource() javadoc for more details
        //putValue("noIconInMenu", Boolean.TRUE);
    }
    
     protected String iconResource() {
        return "it/ccprogetti/impalcatoGraticcio/nb/actions/images/sezione_analisi.gif";
    }
       
    
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }
    
    protected boolean asynchronous() {
        return false;
    }
    
}
