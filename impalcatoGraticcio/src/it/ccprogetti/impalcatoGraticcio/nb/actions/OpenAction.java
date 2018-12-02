package it.ccprogetti.impalcatoGraticcio.nb.actions;

import cassone.controller.ControllerAnalisi;
import cassone.model.Progetto;
import org.netbeans.core.windows.WindowManagerImpl;
import org.netbeans.core.windows.view.ui.MainWindow;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;

public final class OpenAction extends CallableSystemAction {
    
    private ControllerAnalisi controller = new ControllerAnalisi();
    
    public void performAction() {
        controller.open();
        if ( Progetto.getInstance().getFileCorrente() != null ){
          
            String title = NbBundle.getMessage(MainWindow.class, "CTL_MainWindow_Title", System.getProperty("netbeans.buildnumber"));
            WindowManagerImpl.getInstance().getMainWindow().setTitle( title + " - " + Progetto.getInstance().getFileCorrente().getPath() );
         }
        
    }
    
    public void performAction( String fileName ) {
        controller.open( fileName );
        //WindowManagerImpl.getInstance().getMainWindow().setTitle( Progetto.getInstance().getFileCorrente().getPath() );
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
