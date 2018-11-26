package cassone.view;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;

import cassone.util.ViewUtil;

public abstract class AbstractDlgInit
    extends JDialog {

  protected int status = 0;

  public static int OK = 1;
  public static int CANCEL = 2;

  protected DlgInit_bOK_actionAdapter okController;
  protected DlgInit_bOpenFile_actionAdapter openController;

  public AbstractDlgInit() {
    super( (Frame)null, "", true);
    openController = new DlgInit_bOpenFile_actionAdapter(this);
    okController = new DlgInit_bOK_actionAdapter(this);
  }


  //Overridden so we can exit when window is closed
  protected void processWindowEvent(WindowEvent e) {
    //super.processWindowEvent(e);
    int option;
    if (e.getID() == WindowEvent.WINDOW_CLOSING) {
      System.exit(0);
    }
  }

   public abstract void jbInit();

  /**
   *
   * @return
   */
  public static int showDlg( Class fr  ) {

    AbstractDlgInit frame = null;
    try {
     frame = ( AbstractDlgInit ) fr.newInstance();
     ViewUtil.launch( frame );
   }
   catch (IllegalAccessException ex) {
     ex.printStackTrace();
   }
   catch (InstantiationException ex) {
     ex.printStackTrace();
   }

    while ( frame.status == 0) {
      continue;
    }
    return frame.status;
  }

  /**
   *
   * @param e
   */
  public abstract void ok(ActionEvent e);

  /**
   *
   * @param e
   */
  public abstract void openFile(ActionEvent e);

}


/**
 *
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
class DlgInit_bOK_actionAdapter
    implements java.awt.event.ActionListener {
  AbstractDlgInit adaptee;

  DlgInit_bOK_actionAdapter(AbstractDlgInit adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.ok(e);
  }
}

/**
 *
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
class DlgInit_bOpenFile_actionAdapter
    implements java.awt.event.ActionListener {
  AbstractDlgInit adaptee;

  DlgInit_bOpenFile_actionAdapter( AbstractDlgInit adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.openFile(e);
  }
}



