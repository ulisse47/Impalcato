package cassone.view.grafica;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class Drawing {
  public  Drawing() {
  }

  
  public static void disegnaQuotaTraDuePunti(double x1, double x2, String quota,
          int dd,
          Graphics2D g2d) {
  
	  int x1i = Math.round((float)x1);
	  int x2i = Math.round((float)x2);
	  if (quota == "") {
	      quota = Integer.toString(Math.round((float)(x2-x1)));
	    }

	  disegnaQuotaTraDuePunti(x1i, x2i, quota, dd, g2d);
  }
  
//disegna una quota tra due punti x1 e x2
//se non si mette la stringa della quota calcola la differenza
//tra x1 e x2
  public static void disegnaQuotaTraDuePunti(int x1, int x2, String quota,
                                             int dd,
                                             Graphics2D g2d) {
    int dx = Math.abs(x2 - x1);
    if (quota == "") {
      quota = Integer.toString(dx);

    }
    Line2D ln = new Line2D.Double();
    //linea principale
    ln.setLine(x1 - dd, 0, x2 + dd, 0);
    g2d.draw(ln);
    //linea  di estensione
    ln.setLine(x1 + dd, dd, x1 - dd, -dd);
    g2d.draw(ln);
    ln.setLine(x2 + dd, dd, x2 - dd, -dd);
    g2d.draw(ln);
    ln.setLine(x1, dd, x1, -dd);
    g2d.draw(ln);
    ln.setLine(x2, dd, x2, -dd);
    g2d.draw(ln);

    Font fn = new Font("Arial", 0, (int) (dd * 1.8));
    g2d.setFont(fn);

    FontMetrics metrics = g2d.getFontMetrics();
    int tW = metrics.stringWidth(quota);
    if (dx > tW) {
      g2d.drawString(quota, x2 / 2 + x1 / 2 - tW / 2, -dd / 2);
    }
    else {
      //g2d.drawString(quota, Math.max(x2, x1) + dd, 0);
        g2d.drawString(quota, x2 / 2 + x1 / 2 - tW / 2, -2*dd);
    }
  }

  
  
  
//disegna un "leader" e relativo testo
  public static void leader(int x, int y, int dx,int dy,int dd, String testo,
                            Graphics2D g2d){

    int x2=x+dx;
    int y2=y+dy;
    int x3=x2+dd;
    int y3=y2;


    //linee
    Line2D ln = new Line2D.Double();
   //linea principale
    ln.setLine(x, y, x2 ,y2);
    g2d.draw(ln);
    ln.setLine(x2, y2, x3 ,y3);
    g2d.draw(ln);

    FontMetrics metrics = g2d.getFontMetrics();
    int tW = metrics.stringWidth(testo);
    int tH = (metrics.getMaxAscent()-metrics.getMaxDescent());

    if(dd>=0) g2d.drawString(testo,x3+dd/30,y3+tH/2);
    else g2d.drawString(testo,x3-tW,y3+tH/2);

  }

}