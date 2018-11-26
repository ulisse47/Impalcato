package cassone.util;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;

import javax.swing.UIManager;

public class ViewUtil {

  public static void setLook() {

    try {
      UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
      //UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
      System.setProperty("file.encoding", "UTF-8" );
      System.out.println( System.getProperty("file.encoding" ) );
      
      //OutputStreamWriter out = new OutputStreamWriter(new ByteArrayOutputStream());
      //System.out.println("encoding: " + out.getEncoding());
    }
    catch (Exception e) {
      e.printStackTrace();
    }

  }


  public static void launch( Window frame ) {

      frame.pack();

     //Center the window
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension frameSize = frame.getSize();
    if (frameSize.height > screenSize.height) {
      frameSize.height = screenSize.height;
    }
    if (frameSize.width > screenSize.width) {
      frameSize.width = screenSize.width;
    }
    frame.setLocation( (screenSize.width - frameSize.width) / 2,
                      (screenSize.height - frameSize.height) / 2);
   frame.setVisible( true );

  }
}
  
