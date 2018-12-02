package cassone.view.VerificaTensioni;

/*
 * Created on 23-mar-2004
 * Andrea Cavalieri
 *
 */

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import cassone.model.Campate;
import cassone.model.Progetto;
import cassone.model.Sezione;
import cassone.model.SezioneOutputTensioniFase;
import cassone.model.SezioniMetalliche.SezioneMetallica;
import cassone.model.soletta.Soletta;
import cassone.util.dialog.DlgOutputString;
import java.awt.BorderLayout;

import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;




/**
 * @author Andrea
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

public class TensioniVerificaView extends JPanel {
    
    // trasformazione originale
    AffineTransform atOrigine;
    
    // variabili globali del progetto
    private int[] yg = new int[5];
    
    private double[] n = new double[5];
    
    private boolean calcoloBeff;
    
    // variabili di utilit�
    private int Htot, Btot, Hcarp;
    
    // grafica
    private Color coloreSezioneAcciaio = Color.BLUE;
    
    private Color coloreSoletta = Color.lightGray;
    
    private Color coloreQuote = Color.RED;
    
    private double[] originOffset;
    
    private AscMouse ascMouse;
    
    private AscMouseMove ascMouseMove;
    
    private String nome = "SEZIONE 1";
    
    Sezione curSezione;
    
    SezioneOutputTensioniFase[] sezOut;
    
    private Color[] coloreAssi = { Color.magenta, Color.cyan, Color.yellow,
    Color.green, Color.ORANGE };
    
    // utility grafica
    private int ddquote;
    
    private Soletta soletta;
    
    private SezioneMetallica concio;
    
    Shape spSoletta;
    
    Shape[] spSolettaEff;
    
    Shape spConcio;
    
    double[] DXfinestra = new double[2];
    
    double fx;
    
    Campate campate;
    
    double xCampata;
    
    int nCampate;
    
    boolean viewSingolo = false;
    
    double[] ss, ii, sc, scinf, yn;
    
    int curFase = 0;
    
    double Bf1;
    
    double fTensioni;
    
    boolean analizzato;
    
    private static JPanel container;
    
    /**
     *
     */
    public TensioniVerificaView() {
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
            Font fnt = container.getFont();
            container.setBorder(new TitledBorder( new EtchedBorder(
                    EtchedBorder.LOWERED, Color.white, Color.gray), "SEZIONE",0,0,fnt,Color.BLUE));
            container.setBackground(Color.BLACK);
            container.add(new TensioniVerificaView());
            
        }
        return container;
    }
    
    /**
     * The paint method provides the real magic. Here we cast the Graphics
     * object to Graphics2D to illustrate that we may use the same old graphics
     * capabilities with Graphics2D that we are used to using with Graphics.
     */
    public void paintComponent(Graphics g) {
        // super.paintComponents(g);
        super.paintComponent(g);
        
        try {
            
            setBackground(Color.black);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
                    RenderingHints.VALUE_RENDER_QUALITY);
            
            // salva la trasformazione originaria
            atOrigine = g2d.getTransform();
            
            getVariabiliLocali();
            
            // titolo
            Font fn = new Font("Arial", Font.BOLD, (int) (14));
            g2d.setColor(Color.LIGHT_GRAY);
            g2d.setFont(fn);
            // FontMetrics metrics = g2d.getFontMetrics();
            String nm = nome;
            if (viewSingolo) {
                if (curFase != 5)
                    nm += " Fase: " + Integer.toString(curFase);
                else
                    nm += "TOTALE";
                
            }
            g2d.drawString(nm, getWidth() / 40, getHeight() / 2);
            
            // salva le variabili globali in variabili locali
            
            // imposta la fimestra
            setFattoreDiScala(g2d);
            
            if(analizzato)disegnaTensioni(g2d);
            
            disegnaCarpenteriaMetallica(g2d);
            
            disegnaSoletta(g2d);
            
            
            
            // disegnaAssiNeutri(g2d);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    // salva le variabili del progetto in variabili private della
    // classe presente
    void getVariabiliLocali() {
        
        Progetto prg = Progetto.getInstance();
        curSezione = prg.getCurrentSezioneVerifica();
        calcoloBeff = curSezione.isCalcoloAutomaticoBeff();
        
        campate = prg.getCampate();
        nCampate = curSezione.getNCampata();
        xCampata = curSezione.getXSezione();
        
        nome = curSezione.getName();
        
        soletta = curSezione.getSoletta();
        concio = curSezione.getSezioneMetallica();
        
        n[0] = Double.MAX_VALUE;
        n[1] = prg.getN()[0];
        n[2] = prg.getN()[1];
        n[3] = prg.getN()[2];
        n[4] = prg.getN()[3];
        
        // RIGA SELEZIONATA COMBINAZIONI
        int row = TableTensioniView.getInstance().getTable().getSelectedRow();
        if (row == -1)
            row = 0;
        
        // combinazione = prg.getCombinazioni().get(row).getName();
        
        boolean mPos = true;
        if (curSezione.getSezioniOutput().size() != 0) {
            SezioneOutputTensioniFase[] ps = curSezione.getSezioniOutput().get(
                    row);
            for (int i = 0; i < 5; ++i) {
                yn[i] = (ps[i].getYn(curSezione));
                sc[i] = Math.round((float) ps[i].getSc());
                scinf[i] = Math.round((float) ps[i].getScinf());
                ss[i] = Math.round((float) ps[i].getSs());
                ii[i] = Math.round((float) ps[i].getIi());
                mPos = ps[i].isMomPositivo();
                sezOut[i] = ps[i];
            }
            SezioneOutputTensioniFase pss = curSezione.getTensioniTotali(row);
            yn[5] = (pss.getYn(curSezione));
            sc[5] = Math.round((float) pss.getSc());
            scinf[5] = Math.round((float) pss.getScinf());
            ss[5] = Math.round((float) pss.getSs());
            ii[5] = Math.round((float) pss.getIi());
            sezOut[5] = pss;
            
            for (int i = 0; i < 5; i++) {
                spSolettaEff[i] = soletta.getShape(calcoloBeff, campate,
                        nCampate, xCampata, yn[i], mPos);
            }
            spSolettaEff[5] = soletta.getShape(calcoloBeff, campate, nCampate,
                    xCampata, 0, mPos);
        } else{
            spSolettaEff[0] = soletta.getShape(calcoloBeff,campate,nCampate,xCampata,0,false);
            for (int i = 1; i < 6; i++)
                spSolettaEff[i] = soletta.getShape(calcoloBeff,campate,nCampate,xCampata,0,true);
            
        }
        
        spSoletta = soletta.getShape(false, null, 0);
        spConcio = concio.getShape();
        
        analizzato = prg.isAnalizzato();
        // impostazioni grafiche
        Btot = Math
                .round((float) Math.max(soletta.getBtot(), concio.getBtot()));
        Hcarp = (int) concio.getHtot();
        Htot = (int) (soletta.getHsoletta() + Hcarp);
        
    }
    
    // imposta il fattore di scala della classe AffineTransform at
    void setFattoreDiScala(Graphics2D g) {
        
        double W = 0, H = 0;
        
        setOriginalTrasformation(g);
        AffineTransform at = new AffineTransform();
        
        W = getWidth();
        H = getHeight();
        
        Bf1 = Btot * 1.20;
        
        if (!viewSingolo) {
            Bf1 = 5 * Bf1;
            DXfinestra[0] = W / 2 + originOffset[0];
            DXfinestra[1] = H / 2 + originOffset[1] - (Hcarp - Htot / 2) * fx;
        } else {
            double dx = W / 5;
            DXfinestra[0] = W / 2 - 2 * dx + curFase * dx + originOffset[0];
            DXfinestra[1] = H / 2 + originOffset[1] - (Hcarp - Htot / 2) * fx;
        }
        
        fx = Math.min((W) / (Bf1), (H) / (Htot * 1.4));
        
        DXfinestra[0] = W / 2 + originOffset[0];
        DXfinestra[1] = H / 2 + originOffset[1] - (Hcarp - Htot / 2) * fx;
        
        // mi sposto nella sezione all'estradosso carpenteria metallica
        // at.setToTranslation(Bfinestra / 2, getYorigine() - Hcarp);
        // g2d.transform(at);
        
        // imposta grafica
        at.setToTranslation(DXfinestra[0], DXfinestra[1]);
        g.transform(at);
        at.setToScale(fx, fx);
        g.transform(at);
        
        if (viewSingolo){
            double den =Math.max(Math.abs(ss[5] * 1.05), Math.abs(ii[5] * 1.05));
            if(den ==0) den =1;
            fTensioni = Math.abs((Bf1 / 3)/den );
        } else{
            double den =(Math.max(Math.abs(ss[5] * 1.05), Math.abs(ii[5] * 1.05)));
            if(den ==0) den =1;
            fTensioni = Math.abs((Bf1 / 10)/ den);
        }
        // variabili di utilit� grafica
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        ddquote = (int) (screenSize.width / 220 / fx);
        
    }
    
    // imposta il fattore di scala della classe AffineTransform at
    public static Graphics2D setFattoreDiScala(Graphics2D g, double W,
            double H, Sezione sez) {
        
        Soletta soletta = sez.getSoletta();
        SezioneMetallica concio = sez.getSezioneMetallica();
        
        double Btot = Math.round((float) Math.max(soletta.getBtot(), concio
                .getBtot()));
        double Hcarp = (int) concio.getHtot();
        double Htot = (int) (soletta.getHsoletta() + Hcarp);
        
        AffineTransform at = new AffineTransform();
        
        double fx = Math.min((W) / (Btot * 1.20), (H) / (Htot * 1.4));
        
        // imposta grafica
        at.setToTranslation(W / 2, H / 2 - (Hcarp - Htot / 2) * fx);
        g.transform(at);
        at.setToScale(fx, fx);
        g.transform(at);
        
        return g;
        
    }
    
    private Point2D getPuntoAssiLocali(Point2D p) {
        
        double x = (p.getX() - DXfinestra[0]) / fx;
        double y = (p.getY() - DXfinestra[1]) / fx;
        
        return new Point2D.Double(x, y);
    }
    
    // disegna la carpenteria metallica
    void disegnaCarpenteriaMetallica(Graphics2D g2d) {
        
        AffineTransform old = g2d.getTransform();
        
        g2d.setColor(coloreSezioneAcciaio);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        
        if (viewSingolo) {
            g2d.fill(spConcio);
            g2d.draw(spConcio);
            
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_DEFAULT);
            g2d.setColor(coloreQuote);
            if(!analizzato)concio.quota(g2d, ddquote);
            
        } else {
            AffineTransform at = new AffineTransform();
            double W = getWidth();
            at.setToTranslation((-W * 2 / 5) / fx, 0);
            g2d.transform(at);
            for (int i = 0; i < 5; i++) {
                g2d.fill(spConcio);
                g2d.draw(spConcio);
                at.setToTranslation((W / 5) / fx, 0);
                g2d.transform(at);
            }
            
        }
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_DEFAULT);
        
        g2d.setTransform(old);
        
        // g2d.setColor(coloreQuote);
        // concio.quota(g2d, ddquote);
        
    }
    
    // disegna la soletta di calcestruzzo
    void disegnaSoletta(Graphics2D g2d) {
        
        AffineTransform old = g2d.getTransform();
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(coloreSoletta);
        
        if (viewSingolo) {
            g2d.draw(spSoletta);
            g2d.fill(spSolettaEff[curFase]);
            g2d.setColor(coloreQuote);
            
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_DEFAULT);
            if(!analizzato) soletta.quota(g2d, ddquote, calcoloBeff, campate, nCampate,xCampata);
        } else {
            AffineTransform at = new AffineTransform();
            double W = getWidth();
            at.setToTranslation((-W * 2 / 5) / fx + (W / 5) / fx, 0);
            g2d.transform(at);
            for (int i = 1; i < 5; i++) {
                g2d.fill(spSolettaEff[i]);
                g2d.draw(spSoletta);
                at.setToTranslation((W / 5) / fx, 0);
                g2d.transform(at);
            }
            
        }
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_DEFAULT);
        
        g2d.setTransform(old);
        
        // g2d.setColor(coloreQuote);
        // soletta.quota(g2d, ddquote, calcoloBeff,campate,nCampate);
        
    }
    
    // disegna li assi neutri della sezione per i vari n
    void disegnaAssiNeutri(Graphics2D g2d) {
        
        Font fn = new Font("Arial", 0, (int) (ddquote * 1.8));
        g2d.setFont(fn);
        
        float[] fl = { 100, 50 };
        BasicStroke str = new BasicStroke(1, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_BEVEL, 10, fl, 100);
        Stroke orStr = g2d.getStroke();
        
        // int b = (int) (1.5 * 300 / 2);
        // int db = (int) ((Btot / 2 - b) / 3);
        
        g2d.setStroke(str);
        // per ogni n
        int tw = 0;
        for (int i = 0; i < 4; ++i) {
            g2d.setColor(coloreAssi[i]);
            Line2D ln = new Line2D.Double(-Btot / 4 + tw, yg[i], Btot / 20,
                    yg[i]);
            g2d.draw(ln);
            // scritte
            String ns;
            if (n[i] == Double.MAX_VALUE)
                ns = "\u221E";
            else
                ns = Double.toString(n[i]);
            String str1 = "n=" + ns; // + "" + Integer.toString(hgi[i]/10);
            FontMetrics metrics = g2d.getFontMetrics();
            g2d.drawString(str1, -Btot / 4 + tw/* tW / 2 */, yg[i] - 30);
            tw += metrics.stringWidth(str1);
        }
        
        g2d.setStroke(orStr);
        
    }
    
    // disegna le tensioni
    void disegnaTensioni(Graphics2D g2d) {
        // imposta la scala della finestra;
        AffineTransform old = g2d.getTransform();
        g2d.setColor(Color.yellow);
        
//		AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OUT);
//		g2d.setComposite(ac);
        
        
        if (viewSingolo) {
            
            
            Line2D ln = new Line2D.Double(0, -(Htot - Hcarp), 0, Hcarp);
            g2d.draw(ln);
            
            // csup
            ln = new Line2D.Double(0, -Htot + Hcarp, sc[curFase] * fTensioni,
                    -Htot + Hcarp);
            g2d.draw(ln);
            ln = new Line2D.Double(sc[curFase] * fTensioni, -Htot + Hcarp,
                    scinf[curFase] * fTensioni, 0);
            g2d.draw(ln);
            
            ln = new Line2D.Double(scinf[curFase] * fTensioni, 0,
                    0, 0);
            g2d.draw(ln);
            
            
            // ss
            ln = new Line2D.Double(0, 0, ss[curFase] * fTensioni, 0);
            g2d.draw(ln);
            // ii
            ln = new Line2D.Double(ss[curFase] * fTensioni, 0, ii[curFase]
                    * fTensioni, Hcarp);
            g2d.draw(ln);
            ln = new Line2D.Double(ii[curFase] * fTensioni, Hcarp, 0, Hcarp);
            g2d.draw(ln);
            
            String str1 = "ss= " + Double.toString(Math.round(ss[curFase]))
            + " MPa";
            String str4 = "ii= " + Double.toString(Math.round(ii[curFase]))
            + " MPa";
            String str5 = "sc= " + Double.toString(Math.round(sc[curFase]))
            + " MPa";
            
            Font fn = new Font("Arial", 0, (int) (ddquote * 1.8));
            g2d.setFont(fn);
            
            FontMetrics metrics = g2d.getFontMetrics();
            int tW = metrics.stringWidth(str1);
            
            g2d.drawString(str1, (int) (fTensioni * ss[curFase] / 2 - tW / 2),
                    2 * ddquote);
            g2d.drawString(str4, (int) (fTensioni * ii[curFase] / 2 - tW / 2),
                    Hcarp + 2 * ddquote);
            g2d.drawString(str5, (int) (fTensioni * sc[curFase] / 2 - tW / 2),
                    (Hcarp - Htot) - ddquote);
        } else {
            AffineTransform at = new AffineTransform();
            double W = getWidth();
            at.setToTranslation((-W * 2 / 5) / fx, 0);
            g2d.transform(at);
            for (int i = 0; i < 5; i++) {
                Line2D ln = new Line2D.Double(0, -(Htot - Hcarp), 0, Hcarp);
                g2d.draw(ln);
                // csup
                ln = new Line2D.Double(0, -Htot + Hcarp, sc[i] * fTensioni,
                        -Htot + Hcarp);
                g2d.draw(ln);
                ln = new Line2D.Double(sc[i] * fTensioni, -Htot + Hcarp,
                        scinf[i] * fTensioni, 0);
                g2d.draw(ln);
                
                //inf
                ln = new Line2D.Double(scinf[i] * fTensioni, 0,
                        0, 0);
                g2d.draw(ln);
                
                // ss
                ln = new Line2D.Double(0, 0, ss[i] * fTensioni, 0);
                g2d.draw(ln);
                // ii
                ln = new Line2D.Double(ss[i] * fTensioni, 0, ii[i] * fTensioni,
                        Hcarp);
                g2d.draw(ln);
                ln = new Line2D.Double(ii[i] * fTensioni, Hcarp, 0, Hcarp);
                g2d.draw(ln);
                
                String str1 = "ss= " + Double.toString(Math.round(ss[i]))
                + " MPa";
                String str4 = "ii= " + Double.toString(Math.round(ii[i]))
                + " MPa";
                String str5 = "sc= " + Double.toString(Math.round(sc[i]))
                + " MPa";
                
                Font fn = new Font("Arial", 0, (int) (ddquote * 1.8));
                g2d.setFont(fn);
                
                FontMetrics metrics = g2d.getFontMetrics();
                int tW = metrics.stringWidth(str1);
                
                g2d.drawString(str1, (int) (fTensioni * ss[i] / 2 - tW / 2),
                        2 * ddquote);
                g2d.drawString(str4, (int) (fTensioni * ii[i] / 2 - tW / 2),
                        Hcarp + 2 * ddquote);
                g2d.drawString(str5, (int) (fTensioni * sc[i] / 2 - tW / 2),
                        (Hcarp - Htot) - ddquote);
                
                at.setToTranslation((W / 5) / fx, 0);
                g2d.transform(at);
                
            }
            
        }
        
        // scritte
                /*
                 * String str1 = "ss=" + Integer.toString(ssL) +" MPa" ; String str2 =
                 * "s=" + Integer.toString(sL)+" MPa" ; String str3 = "i=" +
                 * Integer.toString(iL)+" MPa" ; String str4 = "ii=" +
                 * Integer.toString(iiL)+" MPa" ;
                 *
                 * Font fn = new Font("Arial", 0, (int) (ddquote * 1.8));
                 * g2d.setFont(fn);
                 *
                 * FontMetrics metrics = g2d.getFontMetrics(); int tW =
                 * metrics.stringWidth(str1);
                 *
                 * g2d.drawString(str1, ss/2-tW/2, -Hcarp-ddquote); g2d.drawString(str2,
                 * s/2-tW/2, -Hcarp+2*ddquote+ts); g2d.drawString(str3, i/2-tW/2,
                 * -Hcarp-ddquote+ts+hw); g2d.drawString(str4, ii/2-tW/2, 2*ddquote);
                 *
                 */
        g2d.setTransform(old);
        
    }
    
    // disegna le tensioni
    public static void disegnaTensioni(Graphics2D g2d, Sezione sez,
            SezioneOutputTensioniFase so, double W, double H) {
        
        // imposta la scala della finestra;
        g2d.setColor(Color.BLACK);
        AffineTransform old = g2d.getTransform();
        
        Soletta soletta = sez.getSoletta();
        SezioneMetallica concio = sez.getSezioneMetallica();
        
        double sc = so.getSc();
        double scinf = so.getScinf();
        double ss = so.getSs();
        double ii = so.getIi();
        
        int Btot = Math.round((float) Math.max(soletta.getBtot(), concio
                .getBtot()));
        int Hcarp = (int) concio.getHtot();
        int Htot = (int) (soletta.getHsoletta() + Hcarp);
        
        double Bf1 = Btot * 1.20;
        double fx = Math.min(W / (Btot * 1.20), (H) / (Htot * 1.4));
        
        double fTensioni = Math.abs((Bf1 / 3)
        / (Math.max(Math.abs(ss * 1.05), Math.abs(ii * 1.05))));
        
        // variabili di utilit� grafica
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int ddquote = (int) (screenSize.width / 220 / fx);
        
        Line2D ln = new Line2D.Double(0, -(Htot - Hcarp), 0, Hcarp);
        // csup
        ln = new Line2D.Double(0, -Htot + Hcarp, sc * fTensioni, -Htot + Hcarp);
        g2d.draw(ln);
        ln = new Line2D.Double(sc * fTensioni, -Htot + Hcarp,
                scinf * fTensioni, 0);
        g2d.draw(ln);
        
        ln = new Line2D.Double(scinf* fTensioni, 0,
                0, 0);
        g2d.draw(ln);
        
        
        // ss
        ln = new Line2D.Double(0, 0, ss * fTensioni, 0);
        g2d.draw(ln);
        // ii
        ln = new Line2D.Double(ss * fTensioni, 0, ii * fTensioni, Hcarp);
        g2d.draw(ln);
        ln = new Line2D.Double(ii * fTensioni, Hcarp, 0, Hcarp);
        g2d.draw(ln);
        
        String str1 = "ss=" + Double.toString(Math.round(ss)) + " MPa";
        String str4 = "ii=" + Double.toString(Math.round(ii)) + " MPa";
        String str5 = "sc=" + Double.toString(Math.round(sc)) + " MPa";
        
        Font fn = new Font("Arial", 0, (int) (ddquote * 1.8));
        g2d.setFont(fn);
        
        FontMetrics metrics = g2d.getFontMetrics();
        int tW = metrics.stringWidth(str1);
        
        g2d.drawString(str1, (int) (fTensioni * ss / 2 - tW / 2), 2 * ddquote);
        g2d.drawString(str4, (int) (fTensioni * ii / 2 - tW / 2), Hcarp + 2
                * ddquote);
        g2d.drawString(str5, (int) (fTensioni * sc / 2 - tW / 2),
                (Hcarp - Htot) - ddquote);
        
        g2d.setTransform(old);
        
    }
    
    void setSezione(boolean next) {
        Progetto prg = Progetto.getInstance();
        if (next && prg.isLastSezioniVerifica())
            return;
        if (!next && prg.isFirstSezioniVerifica())
            return;
        
        double H = getHeight();
        int c;
        if (next)
            c = 1;
        else
            c = -1;
        int count = 0;
        
        while (count < 10) {
            try {
                Thread.sleep(5);
                originOffset[1] += H * c / 10;
                paintComponent(getGraphics());
            } catch (InterruptedException ex) {
                throw new RuntimeException();
            }
            ++count;
        }
        
        if (next)
            prg.setNextSezioniVerifica();
        else
            prg.setPreviousSezioniVerifica();
        
        getVariabiliLocali();
        
        originOffset[1] = -H * c;
        count = 0;
        while (count < 10) {
            try {
                Thread.sleep(5);
                originOffset[1] += H * c / 10;
                paintComponent(getGraphics());
            } catch (InterruptedException ex) {
                throw new RuntimeException();
            }
            ++count;
        }
        
    }
    
    void setOriginalTrasformation(Graphics2D g) {
        g.setTransform(atOrigine);
    }
    
    private void jbInit() throws Exception {
        
        setLayout(null);
        
        originOffset = new double[] { 0, 0 };
        ascMouse = new AscMouse();
        ascMouseMove = new AscMouseMove();
        addMouseListener(ascMouse);
        addMouseWheelListener(ascMouse);
        addMouseMotionListener(ascMouseMove);
        
        spSoletta = new GeneralPath();
        spConcio = new GeneralPath();
        spSolettaEff = new Shape[6];
        for (int i = 0; i < 6; i++) {
            spSolettaEff[i] = new GeneralPath();
        }
        
        yn = new double[6];
        ss = new double[6];
        ii = new double[6];
        sc = new double[6];
        scinf = new double[6];
        
        sezOut = new SezioneOutputTensioniFase[6];
        
        this.setMinimumSize(new Dimension(100, 100));
        this.setVerifyInputWhenFocusTarget(true);
    }
    
    class AscMouse extends MouseAdapter {
        public void mouseWheelMoved(MouseWheelEvent e) {
            int sc = e.getWheelRotation();
            if (!viewSingolo) {
                setSezione(sc > 0);
            } else {
                if (sc > 0)
                    ++curFase;
                else
                    --curFase;
                if (curFase > 5)
                    curFase = 5;
                if (curFase < 0)
                    curFase = 0;
                repaint();
            }
        }
        
        public void mousePressed(MouseEvent e) {
            
        }
        
        public void mouseClicked(MouseEvent e) {
            
            if (viewSingolo) {
                if (analizzato
                        && (spConcio.contains(getPuntoAssiLocali(e.getPoint())) ||
                        spSoletta.contains(getPuntoAssiLocali(e.getPoint())))
                        && viewSingolo && sezOut[curFase] != null) {
                    DlgOutputString dg = new DlgOutputString(sezOut[curFase]
                            .getStringParametriStatici(curSezione, curFase),"CARATTERISTICHE SEZIONE");
                    dg.setVisible(true);
                    return;
                }
            }
            
            if (!viewSingolo) {
                viewSingolo = !viewSingolo;
                paintComponent(getGraphics());
                return;
            }
            
            if (spConcio.contains(getPuntoAssiLocali(e.getPoint()))
            && viewSingolo) {
                Progetto prg = Progetto.getInstance();
                SezioneMetallica sez[] = prg.getArraySezioniMetalliche();
                SezioneMetallica cur = prg.getCurrentSezioneMetallica(false);
                SezioneMetallica sf = (SezioneMetallica) JOptionPane
                        .showInputDialog(null, "Soletta",
                        "Imposta sezione metallica",
                        JOptionPane.INFORMATION_MESSAGE, null, sez, cur);
                try {
                    prg.setSezioneMetallica(sf, false);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                repaint();
                return;
            }
            
            if (spSoletta.contains(getPuntoAssiLocali(e.getPoint()))
            && viewSingolo) {
                Progetto prg = Progetto.getInstance();
                Soletta sez[] = prg.getArraySolette();
                Soletta cur = prg.getCurrentSoletta(false);
                Soletta sf = (Soletta) JOptionPane.showInputDialog(null,
                        "Soletta", "Imposta sezione soletta",
                        JOptionPane.INFORMATION_MESSAGE, null, sez, cur);
                try {
                    prg.setSoletta(sf, false);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                repaint();
                return;
            }
            
            viewSingolo = !viewSingolo;
            paintComponent(getGraphics());
            return;
            
        }
        
    }
    
    class AscMouseMove extends MouseMotionAdapter {
        public void mouseDragged(MouseEvent e) {
            
        }
        
        public void mouseMoved(MouseEvent e) {
            
            if (!viewSingolo) {
                return;
            }
            
            
            if (spConcio.contains(getPuntoAssiLocali(e.getPoint()))) {
                setCursor(new Cursor(Cursor.HAND_CURSOR));
                coloreSezioneAcciaio = Color.yellow;
                
                if (sezOut[curFase] != null && analizzato)
                    setToolTipText(sezOut[curFase].getToolTipParametriStatici(curSezione, curFase));
                
                repaint();
                return;
            } else if (spSoletta.contains(getPuntoAssiLocali(e.getPoint()))) {
                setCursor(new Cursor(Cursor.HAND_CURSOR));
                coloreSoletta = Color.DARK_GRAY;
                
                if (sezOut[curFase] != null && analizzato)
                    setToolTipText(sezOut[curFase].getToolTipParametriStatici(curSezione, curFase));
                
                repaint();
                return;
            } else {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                coloreSoletta = Color.LIGHT_GRAY;
                coloreSezioneAcciaio = Color.BLUE;
                setToolTipText("");
                repaint();
                return;
            }
            
        }
    }
    
}
