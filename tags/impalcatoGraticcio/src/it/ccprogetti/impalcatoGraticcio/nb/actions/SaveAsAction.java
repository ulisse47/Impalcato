package it.ccprogetti.impalcatoGraticcio.nb.actions;

import cassone.controller.ControllerAnalisi;
import cassone.model.Progetto;
import org.netbeans.core.windows.WindowManagerImpl;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;

public final class SaveAsAction extends CallableSystemAction {
    
    private ControllerAnalisi controller = new ControllerAnalisi();
    
    public void performAction() {
        controller.saveAs();
       //MainWindow main = ( MainWindow )org.netbeans.core.windows.WindowManagerImpl.getDefault().getMainWindow();
        WindowManagerImpl.getInstance().setProjectName( Progetto.getInstance().getFileCorrente().getPath() );
       //main.setProjectName( "porca troia" );
        //WindowManager.getDefault().getMainWindow().setTitle("porca troia");
    }
    
    public String getName() {
        return NbBundle.getMessage(SaveAsAction.class, "CTL_SaveAsAction");
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
