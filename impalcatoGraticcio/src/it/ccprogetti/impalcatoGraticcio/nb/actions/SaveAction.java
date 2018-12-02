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

public final class SaveAction extends CallableSystemAction {

    private ControllerAnalisi controller = new ControllerAnalisi();

    public void performAction() {
        
       
        if (Progetto.getInstance().getMode() == StartUpExt.DEMO) {
            NotifyDescriptor d = new NotifyDescriptor.Message("Il salvataggio del progetto è consentito solo alla versione registrata del programma", NotifyDescriptor.WARNING_MESSAGE);
            DialogDisplayer.getDefault().notify(d);
            return;
        } 
        
        controller.save();
        if (Progetto.getInstance().getFileCorrente() != null) {

            String title = NbBundle.getMessage(MainWindow.class, "CTL_MainWindow_Title", System.getProperty("netbeans.buildnumber"));
            WindowManagerImpl.getInstance().getMainWindow().setTitle(title + " - " + Progetto.getInstance().getFileCorrente().getPath());
        }
    /*if ( Progetto.getInstance().getFileCorrente() != null ){
    WindowManagerImpl.getInstance().getMainWindow().setTitle( Progetto.getInstance().getFileCorrente().getPath() );  
    }*/
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
