package it.ccprogetti.impalcatoGraticcio.nb.actions;

import cassone.controller.ControllerAnalisi;
import cassone.model.Progetto;
import org.netbeans.core.windows.WindowManagerImpl;
import org.netbeans.core.windows.view.ui.MainWindow;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;

public final class NewProgettoAction extends CallableSystemAction {

    private ControllerAnalisi controller = new ControllerAnalisi();

    public void performAction() {
        controller.nuovo();
        if (Progetto.getInstance().getFileCorrente() != null) {

            String title = NbBundle.getMessage(MainWindow.class, "CTL_MainWindow_Title", System.getProperty("netbeans.buildnumber"));
            WindowManagerImpl.getInstance().getMainWindow().setTitle(title + " - " + Progetto.getInstance().getFileCorrente().getPath());
        }

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
