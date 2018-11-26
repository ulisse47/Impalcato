package it.ccprogetti.impalcatoGraticcio.nb.actions;

import cassone.controller.ControllerAnalisi;
import cassone.model.Progetto;
import org.netbeans.core.windows.WindowManagerImpl;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;

public final class OpenAction extends CallableSystemAction {
    
    private ControllerAnalisi controller = new ControllerAnalisi();
    
    public void performAction() {
        controller.open();
         WindowManagerImpl.getInstance().setProjectName( Progetto.getInstance().getFileCorrente().getPath() );
    }
    
    public String getName() {
        return NbBundle.getMessage(OpenAction.class, "CTL_OpenAction");
    }
    
    protected String iconResource() {
        return "it/ccprogetti/impalcatoGraticcio/nb/actions/openProject.gif";
    }
    
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }
    
    protected boolean asynchronous() {
        return false;
    }
    
}
