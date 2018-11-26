package it.ccprogetti.impalcatoGraticcio.nb.actions;

import cassone.controller.ControllerAnalisi;
import cassone.model.Progetto;
import it.ccprogetti.impalcatoGraticcio.nb.view.VerificaInsTopComponent;
import org.netbeans.core.windows.WindowManagerImpl;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;
import org.openide.windows.TopComponent;

public final class SaveAction extends CallableSystemAction {
    
    private ControllerAnalisi controller = new ControllerAnalisi();
    
    public void performAction() {
        controller.save();
        WindowManagerImpl.getInstance().setProjectName( Progetto.getInstance().getFileCorrente().getPath() );
        /*TopComponent win = VerificaInsTopComponent.findInstance();
        win.open();
        win.requestActive();*/
        //new VerificaInsAction().actionPerformed( null );
        
    }  

    
    public String getName() {
        return NbBundle.getMessage(SaveAction.class, "CTL_SaveAction");
    }
    
    protected String iconResource() {
        return "it/ccprogetti/impalcatoGraticcio/nb/actions/save.png";
    }
    
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }
    
    protected boolean asynchronous() {
        return false;
    }
    
}
