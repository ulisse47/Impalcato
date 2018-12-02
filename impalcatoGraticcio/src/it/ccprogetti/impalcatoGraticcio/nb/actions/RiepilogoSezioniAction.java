/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.ccprogetti.impalcatoGraticcio.nb.actions;

import cassone.util.dialog.DlgRiepilogoSezioni;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "Edit",
        id = "it.ccprogetti.impalcatoGraticcio.nb.actions.RiepilogoSezioniAction"
)
@ActionRegistration(
        displayName = "#CTL_RiepilogoSezioniAction"
)
@ActionReference(path = "Menu/Sezioni", position = 3333, separatorBefore = 3283, separatorAfter = 3383)
@Messages("CTL_RiepilogoSezioniAction=Riepilogo sezioni")
public final class RiepilogoSezioniAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        DlgRiepilogoSezioni dlg = new DlgRiepilogoSezioni(null, true);
        dlg.setVisible(true);
    }
}
