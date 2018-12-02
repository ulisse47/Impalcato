package it.ccprogetti.impalcatoGraticcio.nb.actions;

import cassone.controller.ControllerVerificaGiunti;
import cassone.model.Progetto;
import it.ccprogetti.impalcatoGraticcio.nb.view.VerificaGiuntiAction;
import java.awt.event.ActionEvent;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.BooleanStateAction;

public final class GiuntoBullonatoAction extends BooleanStateAction {
    
    private ControllerVerificaGiunti controller = new ControllerVerificaGiunti();
    
    
       
    
    public void actionPerformed(ActionEvent actionEvent) {
        
        if ( getBooleanState() ){
            controller.removeGiunto();
        }
        else {
            controller.addGiunto();
        }
        
        
        super.actionPerformed(actionEvent);
        VerificaGiuntiAction.open();
   }

   

    public boolean getBooleanState() {
        
         //se non ha il giuntoi bullonato
        if ( Progetto.getInstance().getCurrentSezioneVerifica() != null ){
            if ( Progetto.getInstance().getCurrentSezioneVerifica().getGiunto() == null ){
                //false
                //controller.addGiunto();
                return false;
            }
            
            else {
                //controller.removeGiunto();
                return true;
            }
        }
        else return false;
    
    }
    
    

    
    
    protected void initialize() {
        super.initialize();
        //setBooleanState( false );        
    }

     
    

    
    public String getName() {
        return NbBundle.getMessage(GiuntoBullonatoAction.class, "CTL_GiuntoBullonatoAction");
    }
    
    /*protected String iconResource() {
        return "it/ccprogetti/impalcatoGraticcio/nb/actions/save.png";
    }*/
    
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }
    
    protected boolean asynchronous() {
        return false;
    }
    
}
