package cassone.view.VerificaTensioni;

/*
 * Created on 23-mar-2004
 * Andrea Cavalieri
 *
 */

import cassone.model.Sezione;
import cassone.view.grafica.Drawing;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.text.NumberFormat;
import java.util.ArrayList;

import javax.swing.JPanel;

import cassone.model.Campate;
import cassone.model.Progetto;
import java.awt.BorderLayout;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

/**
 * @author Andrea
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

public class CampateViewTensioni extends JPanel {
    
    // trasformazione originale
    AffineTransform atOrigine;
    
    // variabili globali del progetto
    private int l[];
    
    // variabili di util grafica
    double  Bfinestra, Hfinestra;
    
    // numero campate ponte
    int ncamp;
    
    // numero della campata sezione
    int campataSez;
    double xCampata;
    
    int Ltot = 0;
    
    double fy,fx;
    // grafica
    private Color coloreQuote = Color.RED;
    
    
    // utility grafica
    private int ddquote;
    
    private static JPanel container;
    
    /**
     *
     */
    public CampateViewTensioni() {
        super();
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static JPanel getInstance() {
        if (container == null) {
            container = new JPanel();
            container.setLayout(new BorderLayout());
            container.setBorder(new TitledBorder(new EtchedBorder(
                    EtchedBorder.RAISED, Color.white, Color.gray), "CAMPATE"));
            container.setBackground(Color.BLACK);
            container.add( new CampateViewTensioni() );
            
        }
        return container;
    }
    
    /**
     * The paint method provides the real magic. Here we cast the Graphics
     * object to Graphics2D to illustrate that we may use the same old graphics
     * capabilities with Graphics2D that we are used to using with Graphics.
     */
    public void paintComponent(Graphics g) {
        super.paintComponents(g);
        
        try {
            
            // setBackground(Color.white);
            
            Graphics2D g2d = (Graphics2D) g;
            
            // salva la trasformazione originaria
            atOrigine = g2d.getTransform();
            
            // salva le variabili globali in variabili locali
            getVariabiliLocali();
            
            disegnaSezioneTipo(g2d);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    // salva le variabili del progetto in variabili private della
    // classe presente
    void getVariabiliLocali() {
        
        Progetto prg = Progetto.getInstance();
        Campate cp = prg.getCampate();
        
        double[] lc = cp.getLuci();
        
        ncamp = cp.getNCampate();
        l = new int[ncamp];
        Ltot = 0;
        for (int i = 0; i < ncamp; ++i) {
            l[i] = Math.round((float ) (lc[i] * 1000));
            Ltot += l[i];
        }
        
        campataSez = prg.getCurrentSezioneVerifica().getNCampata();
        
    }
    
    // imposta il fattore di scala della classe AffineTransform at
    void setFattoreDiScala(Graphics2D g) {
        
        double W = 0, H = 0;
        double xOrig = 0, Yorig = 0;
        
        setOriginalTrasformation(g);
        
        AffineTransform at = new AffineTransform();
        
        W = getWidth();
        H = getHeight();
        
        fx = Math.min(W / (Ltot * 1.50), H / 5000);
        fy = fx;
        
        Hfinestra = (H / fy);
        
        xOrig = W / 2;
        Yorig = H / 2;
        
        // imposta grafica
        at.setToTranslation(xOrig, Yorig);
        g.transform(at);
        at.setToScale(fx, fy);
        g.transform(at);
        
        // variabili di util grafica
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        ddquote = (int) (screenSize.width / 220 / fy);
        
    }
    
    // disegna il cassone completo
    void disegnaSezioneTipo(Graphics2D g2d) {
        disegnaCampata(g2d);
        quota(g2d);
        
        // disegnaCarpenteriaMetallica(g2d);
        // disegnaSoletta(g2d);
        // quota(g2d);
    }
    
    void disegnaCampata(Graphics2D g2d) {
        
        setFattoreDiScala(g2d);
        AffineTransform at = new AffineTransform();
        at.setToTranslation(0, 0);
        g2d.transform(at);
        g2d.setColor(Color.BLUE);
        
        int a = (-Ltot / 2);
        int b = (-Ltot / 2 + l[0]);
        if(ncamp==1){
            Line2D ln = new Line2D.Double();
            ln.setLine(a, 0, b, 0);
            g2d.setColor(Color.yellow);
            g2d.draw(ln);
        } else{
            for (int i = 1; i < ncamp + 1; ++i) {
                Line2D ln = new Line2D.Double();
                ln.setLine(a, 0, b, 0);
                if (campataSez == i - 1) {
                    g2d.setColor(Color.yellow);
                } else {
                    g2d.setColor(Color.blue);
                }
                g2d.draw(ln);
                a = b;
                if (i != ncamp)
                    b += l[i];
            }
        }
        
        // disegna appoggi
        g2d.setColor(Color.blue);
        int[] x0 = new int[3];
        int[] y0 = new int[3];
        a = -Ltot / 2;
        if(ncamp==1){
            x0[0] = a;
            y0[0] = 0;
            x0[1] = a - ddquote * 2;
            y0[1] = ddquote * 2;
            x0[2] = a + ddquote * 2;
            y0[2] = ddquote * 2;
            Polygon app = new Polygon(x0, y0, 3);
            g2d.fill(app);
            x0[0] = a+Ltot;
            y0[0] = 0;
            x0[1] = a+Ltot - ddquote * 2;
            y0[1] = ddquote * 2;
            x0[2] = a+Ltot + ddquote * 2;
            y0[2] = ddquote * 2;
            app = new Polygon(x0, y0, 3);
            g2d.fill(app);
        } else{
            for (int i = 0; i < ncamp + 1; ++i) {
                x0[0] = a;
                y0[0] = 0;
                x0[1] = a - ddquote * 2;
                y0[1] = ddquote * 2;
                x0[2] = a + ddquote * 2;
                y0[2] = ddquote * 2;
                
                Polygon app = new Polygon(x0, y0, 3);
                g2d.fill(app);
                if (i != ncamp)
                    a += l[i];
            }
        }
        
        ArrayList sezioni = Progetto.getInstance().getSezioniVerifica();
        Font fn = new Font("Arial", 0, (int) (ddquote * 1.8));
        g2d.setFont(fn);
        
        //disegna xSezione
        for(int i =0; i<sezioni.size();++i){
            Sezione sez = (Sezione)sezioni.get(i);
            double xCampata=sez.getXSezione()*1000;
            int nCampata = sez.getNCampata();
            
            g2d.setColor(Color.blue);
            if(sez==Progetto.getInstance().getCurrentSezioneVerifica())g2d.setColor(Color.YELLOW);
            
            int xc = 0;
            for(int j =0; j<nCampata;++j){
                xc +=l[j];
            }
            
            at.setToTranslation(xc+xCampata-Ltot/2,0);
            g2d.transform(at);
            at.setToRotation(-Math.PI/2);
            g2d.transform(at);
//            String s = sez.getName() + ": x = " + Double.toString(xCampata/1000) + "m";
            String s = sez.getName();
//            Drawing.leader(0, 0, 0, 4*ddquote, ddquote, s, g2d);
            Drawing.leader(0, 0, ddquote, 0, ddquote, s, g2d);
            
            at.setToRotation(Math.PI/2);
            g2d.transform(at);
            at.setToTranslation(-xc-xCampata+Ltot/2,0);
            g2d.transform(at);
        }
        
    }
    
    // disegna le quote
    void quota(Graphics2D g2d) {
        
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        
        
        setOriginalTrasformation(g2d);
        setFattoreDiScala(g2d);
        g2d.setColor(coloreQuote);
        
        AffineTransform at = new AffineTransform();
        at.setToTranslation(0, Hfinestra/4);
        g2d.transform(at);
        
        int a = (-Ltot / 2);
        int b = (-Ltot / 2 + l[0]);
        for (int i = 1; i < ncamp + 1; ++i) {
            disegnaQuotaTraDuePunti(a, b, nf.format(l[i - 1] / 1000.00),
                    ddquote, g2d);
            a = b;
            if (i != ncamp)
                b += l[i];
        }
        
    }
    
    // disegna una quota tra due punti x1 e x2
    // se non si mette la stringa della quota calcola la differenza
    // tra x1 e x2
    void disegnaQuotaTraDuePunti(int x1, int x2, String quota, int dd,
            Graphics2D g2d) {
        
        int dx = Math.abs(x2 - x1);
        if (quota == "")
            quota = Integer.toString(dx);
        
        Line2D ln = new Line2D.Double();
        // linea principale
        ln.setLine(x1 - dd, 0, x2 + dd, 0);
        g2d.draw(ln);
        // linea di estensione
        ln.setLine(x1 + dd, dd, x1 - dd, -dd);
        g2d.draw(ln);
        ln.setLine(x2 + dd, dd, x2 - dd, -dd);
        g2d.draw(ln);
        ln.setLine(x1, dd, x1, -dd);
        g2d.draw(ln);
        ln.setLine(x2, dd, x2, -dd);
        g2d.draw(ln);
        
        Font fn = new Font("Arial", 0, (int) (ddquote * 1.8));
        g2d.setFont(fn);
        
        FontMetrics metrics = g2d.getFontMetrics();
        int tW = metrics.stringWidth(quota);
        if (dx > tW) {
            g2d.drawString(quota, x2 / 2 + x1 / 2 - tW / 2, -ddquote / 2);
        } else {
            g2d.drawString(quota, Math.max(x2, x1) + ddquote, 0);
        }
        
    }
    
    void setOriginalTrasformation(Graphics2D g) {
        g.setTransform(atOrigine);
    }
    
    private void jbInit() throws Exception {
        this.setMinimumSize(new Dimension(100, 100));
        this.setVerifyInputWhenFocusTarget(true);
    }
}
