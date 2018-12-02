/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.ccprogetti.impalcatoGraticcio.nb.actions;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "Help",
        id = "it.ccprogetti.impalcatoGraticcio.nb.actions.RichiediAssistenzaAction"
)
@ActionRegistration(
        displayName = "#CTL_RichiediAssistenzaAction"
)
@ActionReference(path = "Menu/Help", position = 662, separatorBefore = 593, separatorAfter = 731)
@Messages("CTL_RichiediAssistenzaAction=Richiedi assistenza")
public final class RichiediAssistenzaAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            java.awt.Desktop desk;
            desk = Desktop.getDesktop();
            desk.browse(  new URI ("https://download.ccprogetti.it/index.php/assistenza") );
     } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }
    }
}
