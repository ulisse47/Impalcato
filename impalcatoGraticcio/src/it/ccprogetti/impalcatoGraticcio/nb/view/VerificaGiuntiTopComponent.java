package it.ccprogetti.impalcatoGraticcio.nb.view;

import cassone.view.Giunti.TabGiunti;
import java.io.Serializable;
import org.openide.ErrorManager;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 * Top component which displays something.
 */
final class VerificaGiuntiTopComponent extends TopComponent {
    
    private static VerificaGiuntiTopComponent instance;
    /** path to the icon used by the component and its open action */
//    static final String ICON_PATH = "SET/PATH/TO/ICON/HERE";
    
    private static final String PREFERRED_ID = "VerificaGiuntiTopComponent";
    
    private VerificaGiuntiTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(VerificaGiuntiTopComponent.class, "CTL_VerificaGiuntiTopComponent"));
        setToolTipText(NbBundle.getMessage(VerificaGiuntiTopComponent.class, "HINT_VerificaGiuntiTopComponent"));
//        setIcon(Utilities.loadImage(ICON_PATH, true));
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.BorderLayout());

    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    
    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only,
     * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
     * To obtain the singleton instance, use {@link findInstance}.
     */
    public static synchronized VerificaGiuntiTopComponent getDefault() {
        if (instance == null) {
            instance = new VerificaGiuntiTopComponent();
        }
        return instance;
    }
    
    /**
     * Obtain the VerificaGiuntiTopComponent instance. Never call {@link #getDefault} directly!
     */
    public static synchronized VerificaGiuntiTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            ErrorManager.getDefault().log(ErrorManager.WARNING, "Cannot find VerificaGiunti component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof VerificaGiuntiTopComponent) {
            return (VerificaGiuntiTopComponent)win;
        }
        ErrorManager.getDefault().log(ErrorManager.WARNING, "There seem to be multiple components with the '" + PREFERRED_ID + "' ID. That is a potential source of errors and unexpected behavior.");
        return getDefault();
    }
    
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_ALWAYS;
    }
    
    public void componentOpened() {
        add( TabGiunti.getInstance() );
    }
    
    public void componentClosed() {
        // TODO add custom code on component closing
    }
    
    /** replaces this in object stream */
    public Object writeReplace() {
        return new ResolvableHelper();
    }
    
    protected String preferredID() {
        return PREFERRED_ID;
    }
    
    final static class ResolvableHelper implements Serializable {
        private static final long serialVersionUID = 1L;
        public Object readResolve() {
            return VerificaGiuntiTopComponent.getDefault();
        }
    }
    
}
