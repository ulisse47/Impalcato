package cassone;

import cassone.util.ViewUtil;
import cassone.view.MainFrame;

public class Main {

  private static Main viewManager;
  private  MainFrame mainF;
  public static synchronized Main getInstance(){
    if ( viewManager ==  null ){
      viewManager = new Main();
    }
    return viewManager;
  }

  /**
   *
   */
  private Main() {
    ViewUtil.setLook();
    //DlgInit.showDlg( DlgInit.class );
    
    mainF = new MainFrame();
    ViewUtil.launch( mainF );
  }

  /**
   *
   * @param args
   */
  public static void main(String[] args) {
    Main viewManager1 = Main.getInstance();
  }

}