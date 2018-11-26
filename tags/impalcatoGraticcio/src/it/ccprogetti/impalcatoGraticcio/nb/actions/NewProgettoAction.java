package it.ccprogetti.impalcatoGraticcio.nb.actions;

import cassone.controller.ControllerAnalisi;
import cassone.model.Progetto;
import org.netbeans.core.windows.WindowManagerImpl;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;

public final class NewProgettoAction extends CallableSystemAction {
    
    private ControllerAnalisi controller = new ControllerAnalisi();
    
    public void performAction() {
        controller.nuovo();
         WindowManagerImpl.getInstance().setProjectName( Progetto.getInstance().getFileCorrente().getPath() );
    }
    
    public String getName() {
        return NbBundle.getMessage(NewProgettoAction.class, "CTL_NewProgettoAction");
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
