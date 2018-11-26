package it.ccprogetti.impalcatoGraticcio.nb.installer;

import cassone.model.Progetto;
import cassone.util.Parser;
import javax.swing.JOptionPane;
import org.openide.modules.ModuleInstall;

/**
 * Manages a module's lifecycle. Remember that an installer is optional and
 * often not needed at all.
 */
public class Installer extends ModuleInstall {
    
       
    public void restored() {
        // By default, do nothing.
        // Put your startup code here.
        System.out.println(  System.getProperty("jdk.home") );
        Progetto.getInstance();
        Parser.updateInputView();
    }
    
    public boolean closing(){
        return Progetto.getInstance().close();
    }
    
    
}
