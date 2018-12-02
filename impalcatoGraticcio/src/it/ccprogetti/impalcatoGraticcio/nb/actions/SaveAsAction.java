package it.ccprogetti.impalcatoGraticcio.nb.actions;

import cassone.controller.ControllerAnalisi;
import cassone.model.Progetto;
import it.ccprogetti.impalcatoGraticcio.activation.core.StartUpExt;
import org.netbeans.core.windows.WindowManagerImpl;
import org.netbeans.core.windows.view.ui.MainWindow;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;

public final class SaveAsAction extends CallableSystemAction {

    private ControllerAnalisi controller = new ControllerAnalisi();

    public void performAction() {
        
           if (Progetto.getInstance().getMode() == StartUpExt.DEMO) {
            NotifyDescriptor d = new NotifyDescriptor.Message("Il salvataggio del progetto è consentito solo alla versione registrata del programma", NotifyDescriptor.WARNING_MESSAGE);
            DialogDisplayer.getDefault().notify(d);
            return;
        } 

        
        controller.saveAs();
        if (Progetto.getInstance().getFileCorrente() != null) {

            String title = NbBundle.getMessage(MainWindow.class, "CTL_MainWindow_Title", System.getProperty("netbeans.buildnumber"));
            WindowManagerImpl.getInstance().getMainWindow().setTitle(title + " - " + Progetto.getInstance().getFileCorrente().getPath());
        }
    //MainWindow main = ( MainWindow )org.netbeans.core.windows.WindowManagerImpl.getDefault().getMainWindow();
    // WindowManagerImpl.getInstance().getMainWindow().setTitle(  Progetto.getInstance().getFileCorrente().getPath() );
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
