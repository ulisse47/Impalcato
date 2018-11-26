/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.ccprogetti.impalcatoGraticcio.nb.actions;

import it.ccprogetti.impalcatoGraticcio.nb.installer.dlgAbout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "Help",
        id = "it.ccprogetti.impalcatoGraticcio.nb.actions.MyAboutAction"
)
@ActionRegistration(
        displayName = "#CTL_MyAboutAction"
)
@ActionReference(path = "Menu/Help", position = 800, separatorBefore = 750, separatorAfter = 850)
@Messages("CTL_MyAboutAction=About")
public final class MyAboutAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
            try {
         dlgAbout dlg = new dlgAbout(null, true);
         dlg.setVisible(true);
     } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }
 
    }
}
