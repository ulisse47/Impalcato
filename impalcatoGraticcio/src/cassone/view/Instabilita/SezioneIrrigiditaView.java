package cassone.view.Instabilita;

/*
 * Created on 23-mar-2004
 * Andrea Cavalieri
 *
 */

import cassone.util.dialog.DlgOutputString;
import java.awt.AlphaComposite;
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

public class SezioneIrrigiditaView extends JPanel {
    
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
    
    double fTensioni;
    
    double ss,ii;
    
    double y1,y2;
    
    boolean analizzato;
    
    private Color[] coloreAssi = { Color.magenta, Color.cyan, Color.yellow,
    Color.green, Color.ORANGE };
    
    // utility grafica
    private int ddquote;
    
    private Soletta soletta;
    private Sezione sezione;
    private SezioneMetallica concio;
    
    Shape spSoletta;
    Shape spSolettaEff;
    
    Shape spConcio;
    Shape spConcioEff;
    
    String combo;
    SezioneMetallica smEfficace;
    double[] DXfinestra = new double[2];
    
    double fx;
    
    Campate campate;
    int nCampate;
    
    private static JPanel container;
    
    /**
     *
     */
    public SezioneIrrigiditaView() {
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
            container.add(new SezioneIrrigiditaView());
            
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
// FontMetrics metrics = g2d.getFontMetrics();
            g2d.drawString(nome, getWidth() / 40 , getHeight()/2);
            
            // salva le variabili globali in variabili locali
            getVariabiliLocali();
            
            // imposta la fimestra
            setFattoreDiScala(g2d);
            
            if(analizzato) disegnaTensioni(g2d);
            
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
        sezione = prg.getCurrentSezioneVerifica();
        
        nome = sezione.getName();
        
        soletta = sezione.getSoletta();
        concio = sezione.getSezioneMetallica();
        spSoletta = soletta.getShape(false,null,0);
        
        // RIGA SELEZIONATA COMBINAZIONI
        int row = TableTensioniEfficaci.getInstance().getTable().getSelectedRow();
        if(row==-1) row=0;
        combo= prg.getCombinazioni().get(row).getName();
        
        if(prg.isAnalizzato() && sezione.getSezioniMetEfficaci().size()!=0){
            smEfficace = sezione.getSezioniMetEfficaci().get(row);
            spConcioEff = smEfficace.getShapeEff();
            spConcio = concio.getShapeIrrigidita();
            ss=smEfficace.getSEff();
            ii=smEfficace.getIEff();
        }else{
            spConcio = concio.getShapeIrrigidita();
            spConcioEff = spConcio;
            ss=0;
            ii=0;
        }
        if(spConcio==null)spConcio=new GeneralPath();
        if(spConcioEff==null)spConcioEff=new GeneralPath();
        
        
        // impostazioni grafiche
        Btot=Math.round((float) Math.max(soletta.getBtot(),concio.getBtot()));
        Hcarp=(int) concio.getHtot();
        Htot= (int) (soletta.getHsoletta()+Hcarp);
        
        y1 = concio.getYs1()/2;
        y2 = Hcarp -(Hcarp- concio.getYs2())/2;
        
        analizzato=prg.isAnalizzato();
    }
    
    // imposta il fattore di scala della classe AffineTransform at
    void setFattoreDiScala(Graphics2D g) {
        
        double W = 0, H = 0;
        
        setOriginalTrasformation(g);
        AffineTransform at = new AffineTransform();
        
        W = getWidth();
        H = getHeight();
        
        fx = Math.min((W) / (Btot * 1.20), (H) / (Htot * 1.4));
        
        DXfinestra[0] = W / 2 + originOffset[0];
        DXfinestra[1] = H / 2 + originOffset[1] - (Hcarp - Htot / 2) * fx;
        
        // imposta grafica
        at.setToTranslation(DXfinestra[0], DXfinestra[1]);
        g.transform(at);
        at.setToScale(fx, fx);
        g.transform(at);
        
        double den = Math.max(Math.abs(ss*1.05), Math.abs(ii*1.05));
        if(den==0) den=1;
        fTensioni = Math.abs((Btot/1.6)/den);
        
        // variabili di utilit� grafica
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        ddquote = (int) (screenSize.width / 220 / fx);
        
    }
    
    
    // imposta il fattore di scala della classe AffineTransform at
    public static Graphics2D setFattoreDiScala(Graphics2D g, double W, double H,Sezione sez) {
        
        AffineTransform at = new AffineTransform();
        
        Progetto prg = Progetto.getInstance();
        Sezione sezione = prg.getCurrentSezioneVerifica();
        SezioneMetallica concio = sezione.getSezioneMetallica();
        Soletta soletta = sez.getSoletta();
        
        // impostazioni grafiche
        double Btot=Math.round((float) Math.max(soletta.getBtot(),concio.getBtot()));
        double Hcarp=(int) concio.getHtot();
        double Htot= (int) (soletta.getHsoletta()+Hcarp);
        
        
        double fx = Math.min((W) / (Btot * 1.20), (H) / (Htot * 1.4));
        
        // imposta grafica
        at.setToTranslation(W / 2 ,H / 2  - (Hcarp - Htot / 2) * fx);
        g.transform(at);
        at.setToScale(fx, fx);
        g.transform(at);
        
        return g;
        
    }
    
    // imposta il fattore di scala della classe AffineTransform at
    public static int getDdQuote(Graphics2D g, double W, double H,Sezione sez) {
        
        Progetto prg = Progetto.getInstance();
        Sezione sezione = prg.getCurrentSezioneVerifica();
        SezioneMetallica concio = sezione.getSezioneMetallica();
        Soletta soletta = sez.getSoletta();
        
        // impostazioni grafiche
        double Btot=Math.round((float) Math.max(soletta.getBtot(),concio.getBtot()));
        double Hcarp=(int) concio.getHtot();
        double Htot= (int) (soletta.getHsoletta()+Hcarp);
        
        double fx = Math.min((W) / (Btot * 1.20), (H) / (Htot * 1.4));
        
        // variabili di utilit� grafica
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int ddquote = (int) (screenSize.width / 220 / fx);
        
        return ddquote;
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
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.draw(spConcio);
        
        g2d.setColor(coloreSezioneAcciaio);
        g2d.fill(spConcioEff);
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_DEFAULT);
        
//		g2d.setColor(coloreQuote);
//		concio.quota(g2d, ddquote);
        
    }
    
    // disegna la soletta di calcestruzzo
    void disegnaSoletta(Graphics2D g2d) {
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(coloreSoletta);
        g2d.draw(spSoletta);
// g2d.fill(spSolettaEff);
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_DEFAULT);
        
        
//		g2d.setColor(coloreQuote);
//		soletta.quota(g2d, ddquote, calcoloBeff,campate,nCampate);
        
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
    void disegnaTensioni(Graphics2D g2d){
        // imposta la scala della finestra;
        AffineTransform old = g2d.getTransform();
        AlphaComposite acOld = (AlphaComposite)g2d.getComposite();
        
        g2d.setColor(Color.yellow);
        
        GeneralPath pt = new GeneralPath();
        
        pt.moveTo(0,y1);
        pt.lineTo(ss*fTensioni,y1);
        pt.lineTo(ii*fTensioni,y2);
        pt.lineTo(0,y2);
//		pt.closePath();
        g2d.draw(pt);
        
//		AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OUT,0.5f);
//		g2d.setComposite(ac);
        
        g2d.drawLine(0, (int)y1, 0, (int)y2);
        g2d.setComposite(acOld);
        
        
        String str1 = "ssEff=" + Double.toString(Math.round(ss)) +" MPa" ;
        String str4 = "iiEff=" + Double.toString(Math.round(ii))+" MPa" ;
        
        Font fn = new Font("Arial", 0, (int) (ddquote * 1.8));
        g2d.setFont(fn);
        
        FontMetrics metrics = g2d.getFontMetrics();
        int tW = metrics.stringWidth(str1);
        
        g2d.drawString(str1, (int) (fTensioni*ss/2-tW/2), 2*ddquote);
        g2d.drawString(str4, (int) (fTensioni*ii/2-tW/2), Hcarp+2*ddquote);
        
        g2d.setTransform(old);
        
    }
    
    
    // disegna le tensioni
    public static void disegnaTensioni(Graphics2D g2d, double W, double H, Sezione sez,int combo){
        // imposta la scala della finestra;
        
        Progetto prg = Progetto.getInstance();
        SezioneMetallica sm = sez.getSezioniMetEfficaci().get(combo);
        Soletta soletta = sez.getSoletta();
        
        // impostazioni grafiche
        double Btot=Math.round((float) Math.max(soletta.getBtot(),sm.getBtot()));
        int Hcarp=(int) sm.getHtot();
        int Htot= (int) (soletta.getHsoletta()+Hcarp);
        double fx = Math.min((W) / (Btot * 1.20), (H) / (Htot * 1.4));
        
        AffineTransform old = g2d.getTransform();
        AlphaComposite acOld = (AlphaComposite)g2d.getComposite();
        
        g2d.setColor(Color.darkGray);
        
        GeneralPath pt = new GeneralPath();
        // impostazioni grafiche
        
        double ss = sm.getSEff();
        double ii = sm.getIEff();
        double y1 = sm.getYs1()/2;
        double y2 = Hcarp -(Hcarp- sm.getYs2())/2;
        
        double fTensioni = Math.abs((Btot/1.6)/(Math.max(Math.abs(ss*1.05), Math.abs(ii*1.05))));
        // variabili di utilit� grafica
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int ddquote = (int) (screenSize.width / 220 / fx);
        
        pt.moveTo(0,sm.getYs1());
        pt.lineTo(ss*fTensioni,sm.getYs1());
        pt.lineTo(ii*fTensioni,sm.getYs2());
        pt.lineTo(0,sm.getYs2());
        g2d.draw(pt);
        
        AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.5f);
        g2d.setComposite(ac);
        g2d.drawLine(0, (int)y1, 0, (int)y2);
        g2d.setComposite(acOld);
        
        String str1 = "ssEff=" + Double.toString(Math.round(ss)) +" MPa" ;
        String str4 = "iiEff=" + Double.toString(Math.round(ii))+" MPa" ;
        
        Font fn = new Font("Arial", 0, (int) (ddquote * 1.8));
        g2d.setFont(fn);
        
        FontMetrics metrics = g2d.getFontMetrics();
        int tW = metrics.stringWidth(str1);
        
        g2d.drawString(str1, (int) (fTensioni*ss/2-tW/2), 2*ddquote);
        g2d.drawString(str4, (int) (fTensioni*ii/2-tW/2), Hcarp+2*ddquote);
        
        g2d.setTransform(old);
        
        
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
        
/*
 * cmbConcio = new JComboBox(); cmbConcio.addActionListener(acs);
 * cmbConcio.setActionCommand("SET_CONCIO"); cmbConcio.setVisible(false);
 * cmbConcio.setBackground(Color.red); cmbConcio.setForeground(Color.BLUE);
 * add(cmbConcio);
 *
 * cmbSoletta = new JComboBox(); cmbSoletta.addActionListener(acs);
 * cmbSoletta.setActionCommand("SET_CONCIO"); cmbSoletta.setVisible(false);
 * cmbSoletta.setBackground(Color.red); cmbSoletta.setForeground(Color.BLUE);
 * add(cmbSoletta);
 */
        this.setMinimumSize(new Dimension(100, 100));
        this.setVerifyInputWhenFocusTarget(true);
    }
    
    class AscMouse extends MouseAdapter {
        public void mouseWheelMoved(MouseWheelEvent e) {
            int sc = e.getWheelRotation();
            setSezione(sc > 0);
        }
        
        public void mousePressed(MouseEvent e) {
            
        }
        
        public void mouseClicked(MouseEvent e) {
            if (analizzato && spConcio.contains(getPuntoAssiLocali(e.getPoint()))) {
                DlgOutputString dg = new DlgOutputString(
                        smEfficace.getStringParametriStatici(sezione, combo),"CARATTERISTICHE SEZIONE EFFICACE");
                dg.setVisible(true);
                return;
            }
            
            
            if (spConcio.contains(getPuntoAssiLocali(e.getPoint()))) {
                Progetto prg = Progetto.getInstance();
                SezioneMetallica sez[] =prg.getArraySezioniMetalliche();
                SezioneMetallica cur = prg.getCurrentSezioneMetallica(false);
                SezioneMetallica sf = (SezioneMetallica) JOptionPane.showInputDialog(null,"Soletta",
                        "Imposta sezione metallica",JOptionPane.INFORMATION_MESSAGE,
                        null,sez,cur);
                try {
                    prg.setSezioneMetallica(sf, false);
                } catch (Exception ex) {
                    // TODO: handle exception
                }
                repaint();
            } else if (spSoletta.contains(getPuntoAssiLocali(e.getPoint()))) {
                Progetto prg = Progetto.getInstance();
                Soletta sez[] =prg.getArraySolette();
                Soletta cur = prg.getCurrentSoletta(false);
                Soletta sf = (Soletta) JOptionPane.showInputDialog(null,"Soletta",
                        "Imposta sezione soletta",JOptionPane.INFORMATION_MESSAGE,
                        null,sez,cur);
                try {
                    prg.setSoletta(sf, false);
                } catch (Exception ex) {
                    // TODO: handle exception
                }
                repaint();
            }
        }
        
    }
    
    class AscMouseMove extends MouseMotionAdapter {
        public void mouseDragged(MouseEvent e) {
            
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
            } else {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                coloreSoletta=Color.LIGHT_GRAY;
                coloreSezioneAcciaio=Color.BLUE;
                repaint();
            }
        }
        
    }
    
/*
 * class AscButton implements ActionListener {
 *
 * public void actionPerformed(ActionEvent e) { JComboBox comboSource;
 * comboSource = (JComboBox) e.getSource();
 *
 * if (comboSource.getActionCommand() == "SET_CONCIO") { SezioneMetallica concio =
 * (SezioneMetallica) comboSource .getSelectedItem(); Progetto prg =
 * Progetto.getInstance(); try { prg.setConcio(concio, true);
 *  } catch (Exception ex) { } comboSource.setVisible(false);
 *
 *  // repaint dell'inputView Parser.updateInputView();
 *  }
 *
 * if (comboSource.getActionCommand() == "SET_SEZIONE_ANALISI") { //
 * setSezioniAnalisi(); }
 *
 *
 * if (comboSource.getActionCommand() == "SET_SEZIONE") { // setSezione(); } }
 *  }
 */
}
