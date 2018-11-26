/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.ccprogetti.impalcatoGraticcio.nb.actions;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "Help",
        id = "it.ccprogetti.impalcatoGraticcio.nb.actions.GuidaUtenteAction"
)
@ActionRegistration(
        displayName = "#CTL_GuidaUtenteAction"
)
@ActionReference(path = "Menu/Help", position = 100, separatorBefore = 50, separatorAfter = 150)
@Messages("CTL_GuidaUtenteAction=Guida utente")
public final class GuidaUtenteAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {

//        File guida = new File(System.getProperty("user.dir") + "\\Help\\Help.html");
        try {
            java.awt.Desktop desk;
            desk = Desktop.getDesktop();
         File guida;
            guida = new File(System.getProperty("user.dir") + "\\help\\Help.html");
            desk.open(guida);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

    }
}
