package cassone.view.analisi;

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
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import cassone.model.Campate;
import cassone.model.Progetto;
import cassone.model.Sezione;
import cassone.model.SezioneOutputTensioniFase;
import cassone.model.SezioniMetalliche.SezioneMetallica;
import cassone.model.soletta.Soletta;
import java.awt.BorderLayout;

import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;




/**
 * @author Andrea
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

public class SezioneTipoView extends JPanel {
    
    // trasformazione originale
    AffineTransform atOrigine;
    
    // variabili globali del progetto
    private int[] yg = new int[5];
    
    private double[] n = new double[5];
    
    private boolean calcoloBeff;
    
    // variabili di utilit�
    private int Htot, Btot,Hcarp;
    
    // grafica
    private Color coloreSezioneAcciaio = Color.BLUE;
    
    private Color coloreSoletta = Color.lightGray;
    
    private Color coloreQuote = Color.RED;
    
    private double[] originOffset;
    
    private AscMouse ascMouse;
    
    private AscMouseMove ascMouseMove;
    
    private String nome = "SEZIONE 1";
    
    private Color[] coloreAssi = { Color.magenta, Color.cyan, Color.yellow,
    Color.green, Color.ORANGE };
    
    // utility grafica
    private int ddquote;
    
    private Soletta soletta;
    
    private SezioneMetallica concio;
    
    Shape spSoletta;
    Shape spSolettaEff;
    
    Shape spConcio;
    
    double[] DXfinestra = new double[2];
    
    double fx;
    
    double fatZoom=1;
    double Xp[] = {0,0};
    Rectangle2D recNomeSez = new Rectangle2D.Double();
    
    Campate campate;
    int nCampate;
    
    private static JPanel container;
    
    /**
     *
     */
    public SezioneTipoView() {
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
            container.setBorder(new TitledBorder(new EtchedBorder(
                    EtchedBorder.LOWERED, Color.white, Color.gray), "SEZIONE", 0, 0, fnt, Color.BLUE));
            container.setBackground(Color.BLACK);
            container.add(new SezioneTipoView());
            
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
            
            // titolo
            Font fn = new Font("Arial", Font.BOLD, (int) (14));
            g2d.setColor(Color.LIGHT_GRAY);
            g2d.setFont(fn);
//			FontMetrics metrics = g2d.getFontMetrics();
            g2d.drawString(nome, getWidth() / 40 , getHeight()/2);
            recNomeSez.setFrame(getWidth() / 40-25,getHeight()/2-25,50,50);
            
            // salva le variabili globali in variabili locali
            getVariabiliLocali();
            
            //imposta la fimestra
            setFattoreDiScala(g2d);
            
            disegnaCarpenteriaMetallica(g2d);
            
            disegnaSoletta(g2d);
            
            disegnaAssiNeutri(g2d);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    // salva le variabili del progetto in variabili private della
    // classe presente
    void getVariabiliLocali() {
        
        Progetto prg = Progetto.getInstance();
        Sezione sezione = prg.getCurrentSezioneAnalisi();
        calcoloBeff = sezione.isCalcoloAutomaticoBeff();
        
        campate = prg.getCampate();
        nCampate=sezione.getNCampata();
        
        nome = sezione.getName();
        
        soletta = sezione.getSoletta();
        concio = sezione.getSezioneMetallica();
        spSoletta = soletta.getShape(false,null,0);
        spSolettaEff = soletta.getShape(calcoloBeff,campate,nCampate);
        
        spConcio = concio.getShape();
        
        n[0] = Double.MAX_VALUE;
        n[1] = prg.getN()[0];
        n[2] = prg.getN()[1];
        n[3] = prg.getN()[2];
        n[4] = prg.getN()[3];
        
        if(sezione.getSezioniOutput().size()!=0){
            SezioneOutputTensioniFase[] ps = sezione.getSezioniOutput().get(0);
            for (int i=0;i<5;++i){
                yg[i] = Math.round((float)(ps[i].getYg()));
            }
        }
        
        //impostazioni grafiche
        Btot=Math.round((float) Math.max(soletta.getBtot(),concio.getBtot()));
        Hcarp=(int) concio.getHtot();
        Htot= (int) (soletta.getHsoletta()+Hcarp);
        
    }
    
    // imposta il fattore di scala della classe AffineTransform at
    void setFattoreDiScala(Graphics2D g) {
        
        double W = 0, H = 0;
        
        setOriginalTrasformation(g);
        AffineTransform at = new AffineTransform();
        
        W = getWidth();
        H = getHeight();
        
        fx = Math.min((W) / (Btot * 1.20), (H) / (Htot * 1.4));
        fx*=fatZoom;
        
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
        
        // variabili di utilit� grafica
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        ddquote = (int) (screenSize.width / 220 / fx);
        
    }
    
    public static int getDdQuote(Graphics2D g,double W,double H, Sezione sez) {
        Soletta soletta = sez.getSoletta();
        SezioneMetallica concio = sez.getSezioneMetallica();
        
        double Btot=Math.round((float) Math.max(soletta.getBtot(),concio.getBtot()));
        double Hcarp=(int) concio.getHtot();
        double Htot= (int) (soletta.getHsoletta()+Hcarp);
        
        double fx = Math.min((W) / (Btot * 1.20), (H) / (Htot * 1.4));
        
        // variabili di utilit� grafica
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        return (int) (screenSize.width / 220 / fx);
        
    }
    
    
    // imposta il fattore di scala della classe AffineTransform at
    public static Graphics2D setFattoreDiScala(Graphics2D g,double W,double H, Sezione sez) {
        
        Soletta soletta = sez.getSoletta();
        SezioneMetallica concio = sez.getSezioneMetallica();
        
        double Btot=Math.round((float) Math.max(soletta.getBtot(),concio.getBtot()));
        double Hcarp=(int) concio.getHtot();
        double Htot= (int) (soletta.getHsoletta()+Hcarp);
        
        AffineTransform at = new AffineTransform();
        
        double fx = Math.min((W) / (Btot * 1.20), (H) / (Htot * 1.5));
        
        // imposta grafica
        at.setToTranslation(W/2, H / 2  - (Hcarp - Htot / 2) * fx);
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
    
    // disegna il cassone completo
    void disegnaSezioneTipo(Graphics2D g2d) {
        
        disegnaCarpenteriaMetallica(g2d);
        disegnaSoletta(g2d);
        // disegnaAssiNeutri(g2d);
        // quota(g2d);
    }
    
    // disegna la carpenteria metallica
    void disegnaCarpenteriaMetallica(Graphics2D g2d) {
        
        g2d.setColor(coloreSezioneAcciaio);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        Shape pat = concio.getShape();
        g2d.fill(pat);
        g2d.draw(pat);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_DEFAULT);
        
        g2d.setColor(coloreQuote);
        concio.quota(g2d, ddquote);
        
    }
    
    // disegna la soletta di calcestruzzo
    void disegnaSoletta(Graphics2D g2d) {
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(coloreSoletta);
        g2d.draw(spSoletta);
        g2d.fill(spSolettaEff);
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_DEFAULT);
        
        
        g2d.setColor(coloreQuote);
        soletta.quota(g2d, ddquote, calcoloBeff,campate,nCampate);
        
    }
    
    // disegna li assi neutri della sezione per i vari n
    void disegnaAssiNeutri(Graphics2D g2d) {
        
        Font fn = new Font("Arial", 0, (int) (ddquote * 1.8));
        g2d.setFont(fn);
        
        float[] fl = { 100, 50 };
        BasicStroke str = new BasicStroke(1, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_BEVEL, 10, fl, 100);
        Stroke orStr = g2d.getStroke();
        
//		int b = (int) (1.5 * 300 / 2);
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
    
    // disegna le quote
    void quota(Graphics2D g2d) {
/*
                // imposto la finestra
                setOriginalTrasformation(g2d);
                setFattoreDiScala(g2d);
                g2d.setColor(coloreQuote);
 
                // quota soletta
                // prima fila orizzonate di quote superiori
                AffineTransform at = new AffineTransform();
                at.setToTranslation(Bfinestra / 2, getYorigine() - Htot - 8 * ddquote);
                g2d.transform(at);
                // quota soletta superiore
                if (calcoloBeff) {
                        disegnaQuotaTraDuePunti(-b0 / 2 - be1, b0 / 2 + be2, "Beff: "
                                        + Integer.toString(b0 + be1 + be2), ddquote, g2d);
                        at.setToTranslation(0, 3 * ddquote);
                        g2d.transform(at);
                        disegnaQuotaTraDuePunti(-b0 / 2 - be1, -b0 / 2, "be1: "
                                        + Integer.toString(be1), ddquote, g2d);
                        disegnaQuotaTraDuePunti(-b0 / 2, b0 / 2, "b0: "
                                        + Integer.toString(b0), ddquote, g2d);
                        disegnaQuotaTraDuePunti(b0 / 2, b0 / 2 + be2, "be2: "
                                        + Integer.toString(be2), ddquote, g2d);
 
                } else {
                        disegnaQuotaTraDuePunti(-B / 2, B / 2, "", ddquote, g2d);
                }
 
                // quota piattabanda inf
                // riposizioni gli assi
                setFattoreDiScala(g2d);
                at.setToTranslation(Bfinestra / 2, getYorigine() + 4 * ddquote);
                g2d.transform(at);
                // disegnaQuotaTraDuePunti(-bi / 2, bi / 2, "", ddquote, g2d);
 
                // quota piattabanda sup
                // riposizioni gli assi
                setFattoreDiScala(g2d);
                at.setToTranslation(Bfinestra / 2, getYorigine() - Hcarp + 5 * ddquote);
                g2d.transform(at);
                // disegnaQuotaTraDuePunti(-bs / 2, bs / 2, "", ddquote, g2d);
 
                // quote verticali
                setFattoreDiScala(g2d);
                at.setToTranslation(Bfinestra / 2 + Btot / 2 + 8 * ddquote,
                                getYorigine());
                g2d.transform(at);
                at.setToRotation(-Math.PI / 2);
                g2d.transform(at);
                disegnaQuotaTraDuePunti(0, Hcarp, "", ddquote, g2d);
                disegnaQuotaTraDuePunti(Hcarp, Hcarp + h + H, "", ddquote, g2d);
 
                if (h != 0) {
                        at.setToTranslation(0, -4 * ddquote);
                        g2d.transform(at);
                        disegnaQuotaTraDuePunti(Hcarp, Hcarp + h, "", ddquote, g2d);
                }
                // totale
                at.setToTranslation(0, 3 * ddquote);
                g2d.transform(at);
                disegnaQuotaTraDuePunti(0, Hcarp + h + H, "", ddquote, g2d);
 
                // spessori piattabande
                setFattoreDiScala(g2d);
                at.setToTranslation(Bfinestra / 2 + bi / 4, getYorigine());
                g2d.transform(at);
                // spessore paittabanda inferiore
                Line2D ln = new Line2D.Double(0, 0, ddquote, 3 * ddquote);
                Drawing.leader(0, 0, 0, 2 * ddquote, 2 * ddquote, Integer.toString(bi)
                                + "X" + Integer.toString(ti) + "mm", g2d);
                // anima
                at = new AffineTransform();
                at.setToTranslation(-bi / 4, 0);
                g2d.transform(at);
                ln.setLine(0, -Hcarp / 2, 0 + Hcarp / 7, -Hcarp / 2);
                g2d.draw(ln);
                g2d.drawString(Integer.toString(hw) + "X" + Integer.toString(tw)
                                + " mm", Hcarp / 7, -Hcarp / 2);
 
                // superiore
                setFattoreDiScala(g2d);
                at = new AffineTransform();
                at.setToTranslation(Bfinestra / 2 + bs / 2.5, getYorigine() - Hcarp
                                + ts);
                g2d.transform(at);
                Drawing.leader(0, 0, 0, 2 * ddquote, 2 * ddquote, Integer.toString(bs)
                                + "X" + Integer.toString(ts) + "mm", g2d);
 */
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
    
    void setSezione(boolean next) {
        Progetto prg = Progetto.getInstance();
        if (next && prg.isLastSezioniAnalisi())
            return;
        if (!next && prg.isFirstSezioniAnalisi())
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
            prg.setNextSezioniAnalisi();
        else
            prg.setPreviousSezioniAnalisi();
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
        
        this.setMinimumSize(new Dimension(100, 100));
        this.setVerifyInputWhenFocusTarget(true);
    }
    
    class AscMouse extends MouseAdapter {
        public void mouseWheelMoved(MouseWheelEvent e) {
            int sc = e.getWheelRotation();
            
            //se � all'interno del nome sezione cambia sezione'
            if(recNomeSez.contains(e.getPoint())){
               setSezione(sc > 0);
            } else{
                fatZoom=fatZoom-fatZoom*sc/3;
                repaint();
            }
            
        }
        
        public void mousePressed(MouseEvent e) {
            Xp[0] =e.getX();
            Xp[1]=e.getY();
        }
        
        public void mouseClicked(MouseEvent e) {
            
            if(e.getButton()==2 && e.getClickCount()==2){
                fatZoom=1;
                originOffset[0]=0;
                originOffset[1]=0;
                
                repaint();
            }
            
            
            if (spConcio.contains(getPuntoAssiLocali(e.getPoint()))) {
                Progetto prg = Progetto.getInstance();
                SezioneMetallica sez[] =prg.getArraySezioniMetalliche();
                SezioneMetallica cur = prg.getCurrentSezioneMetallica(true);
                SezioneMetallica sf = (SezioneMetallica) JOptionPane.showInputDialog(null,"Soletta",
                        "Imposta sezione metallica",JOptionPane.INFORMATION_MESSAGE,
                        null,sez,cur);
                try {
                    prg.setSezioneMetallica(sf, true);
                } catch (Exception ex) {
                    // TODO: handle exception
                }
                repaint();
            } else if (spSoletta.contains(getPuntoAssiLocali(e.getPoint()))) {
                Progetto prg = Progetto.getInstance();
                Soletta sez[] =prg.getArraySolette();
                Soletta cur = prg.getCurrentSoletta(true);
                Soletta sf = (Soletta) JOptionPane.showInputDialog(null,"Soletta",
                        "Imposta sezione soletta",JOptionPane.INFORMATION_MESSAGE,
                        null,sez,cur);
                try {
                    prg.setSoletta(sf, true);
                } catch (Exception ex) {
                    // TODO: handle exception
                }
                repaint();
            }
        }
        
    }
    
    class AscMouseMove extends MouseMotionAdapter {
        public void mouseDragged(MouseEvent e) {
            setCursor(new Cursor(Cursor.MOVE_CURSOR));
            double dx = Xp[0]-e.getX();
            double dy = Xp[1]-e.getY();
            originOffset[0]-=dx;
            originOffset[1]-=dy;
            Xp[0]=e.getX();
            Xp[1]=e.getY();
            repaint();
        }
        
        public void mouseMoved(MouseEvent e) {
            
            
            if (spConcio.contains(getPuntoAssiLocali(e.getPoint()))) {
                setCursor(new Cursor(Cursor.HAND_CURSOR));
                coloreSezioneAcciaio=Color.yellow;
                repaint();
            } else if (spSoletta.contains(getPuntoAssiLocali(e.getPoint()))) {
                setCursor(new Cursor(Cursor.HAND_CURSOR));
                coloreSoletta=Color.DARK_GRAY;
                repaint();
            }else if(recNomeSez.contains(e.getPoint())){
                setCursor(new Cursor(Cursor.N_RESIZE_CURSOR));
            }             
            else {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                coloreSoletta=Color.LIGHT_GRAY;
                coloreSezioneAcciaio=Color.BLUE;
                repaint();
            }
            
        }
        
    }
    
}
