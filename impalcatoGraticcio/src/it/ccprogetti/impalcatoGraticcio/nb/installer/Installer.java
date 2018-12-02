package it.ccprogetti.impalcatoGraticcio.nb.installer;

import cassone.model.Progetto;
import cassone.util.Parser;
import it.ccprogetti.impalcatoGraticcio.activation.core.StartUpExt;
import java.awt.Dimension;
import java.awt.Toolkit;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.modules.ModuleInstall;
import org.openide.util.Exceptions;

/**
 * Manages a module's lifecycle. Remember that an installer is optional and
 * often not needed at all.
 */
public class Installer extends ModuleInstall {

    dlgAttivaSoftware dlg;

    public boolean restored2(String code) {
        try {

            //tenta la registrazione online
            int validCode = StartUpExt.getInstance().verifyValidCode(code);
            switch (validCode) {
                case StartUpExt.NOT_REGIGESTERED:
                    int activeRemotly = StartUpExt.getInstance().activateRemotely();
                    switch (activeRemotly) {
                        case StartUpExt.HTTP_ERROR:
                            dlg.setStato("Non attivato. Errore durante il processo di attivazione");
                            return false;
                        case StartUpExt.REGIGESTERED:
                            dlg.setStato("Attivato con successo!");
                            StartUpExt.getInstance().activateLocally();
                            runProgram(false);
                            return true;
                    }
                case StartUpExt.NOT_VALID_CODE:
                    dlg.setStato("Non attivato: il codice inserito non è valido");
                    return false;

                case StartUpExt.HTTP_ERROR:
                    dlg.setStato("Non attivato. Errore durante il processo di attivazione");
                    return false;
                case StartUpExt.DEMO:
                    runProgram(true);
                    return true;
            }

        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
            NotifyDescriptor d = new NotifyDescriptor.Message("Errore nella caricamento del programma", NotifyDescriptor.ERROR_MESSAGE);
            DialogDisplayer.getDefault().notify(d);
            System.exit(0);
        }
        return false;
    }

    public void restored() {
        //verifica attivazione locale
        try {
            int exitValue = StartUpExt.getInstance().verifyLocalActivation();
            switch (exitValue) {
                case StartUpExt.LOCAL_REGIGESTERED:
                    runProgram(false);
                    return;

                case StartUpExt.NOT_REGIGESTERED:
                    if (dlg == null) {
                        dlg = new dlgAttivaSoftware(null, true, this);
                        final Toolkit toolkit = Toolkit.getDefaultToolkit();
                        Dimension screenSize = toolkit.getScreenSize();
                        int x = (screenSize.width - dlg.getWidth()) / 2;
                        int y = (screenSize.height - dlg.getHeight()) / 2;
                        dlg.setLocation(x, y);
                    }
                    String code = dlg.showDialog();

                    if (code == "demo") {
                        runProgram(true);
                    } else if (code == null) {
                        System.exit(0);
                    } else if (code == "findNewMaya") {
                        runProgram(false);
                    } else {
                        boolean everythingDone = restored2(code);
                        if (everythingDone) {
                            dlg.dispose();
                        } else {
                            Toolkit.getDefaultToolkit().beep();
                            restored();
                        }
                    }
            }
        } catch (Exception ex) {
            System.exit(0);
        }
    }

    public static void runProgram(boolean demo) {
        try {
            
           /* if (Progetto.getInstance().getFileCorrente() != null) {

                String title = NbBundle.getMessage(MainWindow.class, "CTL_MainWindow_Title", System.getProperty("netbeans.buildnumber"));
                WindowManagerImpl.getInstance().getMainWindow().setTitle(title + " - " + Progetto.getInstance().getFileCorrente().getPath());
            }
*/
           
           
            if (demo) {
                Progetto.getInstance().setMode(StartUpExt.DEMO);
                System.out.println("programma attivato in modalita' demo");
            }else{
                Progetto.getInstance();
            }
            Parser.updateInputView();

            
//            command.forward(null);*/
        } catch (Exception ex) {

        }

    }

    public boolean closing() {

        //if (Progetto.getInstance().getMode() == StartUpExt.DEMO) {
        //    return true;
        // }
        return Progetto.getInstance().close();

        //  return false;
//        return new .close();
    }

}
