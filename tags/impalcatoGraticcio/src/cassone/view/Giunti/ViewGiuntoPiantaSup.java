package cassone.view.Giunti;

/*
 * Created on 23-mar-2004
 * Andrea Cavalieri
 *
 */

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;


import cassone.model.Concio;
import cassone.model.GiuntoBullonato;
import cassone.model.Progetto;
import cassone.model.Sezione;
import cassone.model.SezioniMetalliche.SezioneMetallicaDoppioT;

import java.awt.BorderLayout;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

/**
 * @author Andrea
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

public class ViewGiuntoPiantaSup extends JPanel {
    
    // trasformazione originale
    AffineTransform atOrigine;
    
    // variabili globali del progetto
//	private int B, H, b0, b, h,be1,be2;
    
    private int e1asup,e1bsup,e2asup,e2bsup,p1sup,p2sup, nfile1sup,nfile2sup;
    private int diametroSup;
    private int e1ainf,e1binf,e2ainf,e2binf,p1inf,p2inf, nfile1inf,nfile2inf;
    private int diametroinf;
    private int e1aw,e1bw,e2w,p1w,p2w, nfile1w,nfile2w;
    private int diametrow;
    
    private int byw,bxw,bmax ;
    private int byi,bxi;
    private int bys,bxs;
    private int tgw,tginf,tgsup;
    
    private double v1,v2,v3;
    
    private GiuntoBullonato giunto;
    
    private int Btot = 0;
    
    private int[] yg = new int[4];
    
    private int bs, ts, tw, hw, bi, ti;
    
    private boolean calcoloBeff;
    
    private double[] n = new double[5];
    
    // variabili di utilità
    private int Htot, dxAnima, Hcarp;
    
    // variabili di utilità grafica
    private double Bfinestra, Hfinestra;
    
    //valori instabilità
    private int ssLred,sLred,iiLred,iLred,ss1Lred,ss2Lred;
    private int ssred,sred,iired,ired,ss1red,ss2red;
    private int bef1,bef2,bsred,bired;
    
    private int FattoreScalaSpessori;
    
    // grafica
    private Color coloreSezioneAcciaio = Color.BLUE;
    
    private Color coloreGiunzioni = Color.yellow;
    
    private Color coloreSoletta = Color.lightGray;
    
    private Color coloreQuote = Color.RED;
    
    private Color[] coloreAssi = { Color.magenta, Color.cyan, Color.yellow,
    Color.green, Color.ORANGE };
    
    // utility grafica
    private int ddquote;
    //irrigidimenti
    private int b1,h1,b2,h2,h3,b3,h4,b4;
    
    private int y1,y2,y3,y4;
    private int nir;
    
    private static JPanel container;
    private int ns;
    
    /**
     *
     */
    public ViewGiuntoPiantaSup() {
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
                    EtchedBorder.RAISED, Color.white, Color.gray), "PIANTA SUPERIORE"));
            container.setBackground(Color.BLACK);
            container.add( new ViewGiuntoPiantaSup() );
            
        }
        return container;
    }
    
    /**
     * The paint method provides the real magic. Here we cast the Graphics
     * object to Graphics2D to illustrate that we may use the same old graphics
     * capabilities with Graphics2D that we are used to using with Graphics.
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        try {
            setBackground(Color.BLACK);
            Progetto prg = Progetto.getInstance();
            Sezione sezione = prg.getCurrentSezioneVerifica();
            if(sezione.getSezioneMetallica().getClass()!= SezioneMetallicaDoppioT.class)
                return;
            
            Graphics2D g2d = (Graphics2D) g;
            
            // salva la trasformazione originaria
            atOrigine = g2d.getTransform();
            
            // salva le variabili globali in variabili locali
            getVariabiliLocali();
            
            disegna(g2d);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    // salva le variabili del progetto in variabili private della
    // classe presente
    void getVariabiliLocali() {
        
        Progetto prg = Progetto.getInstance();
        Sezione sezione = prg.getCurrentSezioneVerifica();
        SezioneMetallicaDoppioT concio =(SezioneMetallicaDoppioT) sezione.getSezioneMetallica();
        
        
        // piattabanda inferiore
        bi = Math.round((float) (concio.getBi()));
        ti = Math.round((float)  (concio.getTi()));
        
        // anima
        tw = Math.round((float)  (concio.getTw()));
        hw = Math.round((float)  (concio.getHw()));
        
        // piattabanda superiore
        bs = Math.round((float)  (concio.getBs()));
        ts = Math.round((float)  (concio.getTs()));
        
        // utility
        Hcarp = ti + hw + ts;
        
        Htot = Hcarp;
        FattoreScalaSpessori = 1;
        
        giunto=sezione.getGiunto();
        if(giunto==null) return;
        
        e1ainf=Math.round((float) giunto.getE1aInf());
        e1binf=Math.round((float) giunto.getE1bInf());
        e2ainf=Math.round((float) giunto.getE2aInf());
        e2binf=Math.round((float) giunto.getE2bInf());
        p1inf=Math.round((float) giunto.getP1Inf());
        p2inf=Math.round((float) giunto.getP2Inf());
        nfile1inf=Math.round((float) giunto.getNfile1Inf());
        nfile2inf=Math.round((float) giunto.getNfile2Inf());
        diametroinf=Math.round((float) giunto.getDiamBullInf());
        tginf=Math.round((float) giunto.getTgInf());
        
        bxi = (e1ainf+e1binf+p1inf*(nfile1inf-1))*2;
        byi = (e2ainf+e2binf/2+ p2inf*(nfile2inf-1))*2;
        
        
        e1asup=Math.round((float) giunto.getE1aSup());
        e1bsup=Math.round((float) giunto.getE1bSup());
        e2asup=Math.round((float) giunto.getE2aSup());
        e2bsup=Math.round((float) giunto.getE2bSup());
        p1sup=Math.round((float) giunto.getP1Sup());
        p2sup=Math.round((float) giunto.getP2Sup());
        nfile1sup=Math.round((float) giunto.getNfile1Sup());
        nfile2sup=Math.round((float) giunto.getNfile2Sup());
        diametroSup=Math.round((float) giunto.getDiamBullSup());
        tgsup=Math.round((float) giunto.getTgSup());
        
        
        bxs = (e1asup+e1bsup+p1sup*(nfile1sup-1))*2;
        bys = (e2asup+e2bsup/2+ p2sup*(nfile2sup-1))*2;
        
        
        e1aw=Math.round((float) giunto.getE1aW());
        e1bw=Math.round((float) giunto.getE1bW());
        e2w=Math.round((float) giunto.getE2W());
        p1w=Math.round((float) giunto.getP1W());
        p2w=Math.round((float) giunto.getP2W());
        nfile1w=Math.round((float) giunto.getNfile1W());
        nfile2w=Math.round((float) giunto.getNfile2W());
        diametrow=Math.round((float) giunto.getDiamBullW());
        tgw=Math.round((float) giunto.getTgW());
        
        bxw = (e1aw+e1bw+p1w*(nfile1w-1))*2;
        byw = 2*e2w+p2w*(nfile2w-1);
        
    }
    
    
    // imposta il fattore di scala della classe AffineTransform at
    void setFattoreDiScala(Graphics2D g) {
        
        double fx, W = 0, H = 0;
        double xOrig = 0, Yorig = 0;
        
        
        setOriginalTrasformation(g);
        
        AffineTransform at = new AffineTransform();
        
        W = getWidth();
        H = getHeight();
        
//        Htot = (int)(1.5*(Hcarp+bs+bi));
        Htot = (int)(1.4*bs);
        
        bmax = Math.max(bxi,Math.max(bxs,bxw));
        Btot = (int)(bmax*1.4);
        
        fx = Math.min((W) / (Btot), (H) / (Htot));
        
        xOrig = W / 2;
        Yorig = H / 2;
        
        Bfinestra= W/fx;
        Hfinestra=H/fx;
        
        double a = Hcarp;
        double b = bs;
        double c = bi;
        double d =Hcarp+bs+bi;
        v1 = a/d;
        v2 = b/d;
        v3 = c/d;
        
        // imposta grafica
        at.setToTranslation(xOrig, Yorig);
        g.transform(at);
        at.setToScale(fx, fx);
        g.transform(at);
        
        // variabili di utilità grafica
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        ddquote = (int) (screenSize.width / 250/ fx);
        
    }
    
    // disegna il cassone completo
    void disegna(Graphics2D g2d) {
        
        if(giunto==null) return;
        
//        disegnaProspetto(g2d);
        disegnaPiantaSup(g2d);
//        disegnaPiantaInf(g2d);
        
//		disegnaIrrigidimenti(g2d);
//		disegnaTensioniRidotte(g2d);
        //	disegnaSoletta(g2d);
        quota(g2d);
    }
    
    void disegnaPiantaSup(Graphics2D g2d){
        
        // imposta la scala della finestra;
        setFattoreDiScala(g2d);
        
//        AffineTransform at = new AffineTransform();
//        at.setToTranslation(0, -Hfinestra/2+Hfinestra*(v1+v2/2));
        
  //      g2d.transform(at);
        
        double lt = bmax/2+8*ddquote;
        Line2D ln = new Line2D.Double();
        Rectangle2D rec = new Rectangle2D.Double();
        Ellipse2D el = new Ellipse2D.Double();
        g2d.setColor(coloreSezioneAcciaio);
        
        ln.setLine(-lt, -bs/2, lt, -bs/2);g2d.draw(ln);
        ln.setLine(-lt, bs/2, lt, bs/2);g2d.draw(ln);
        ln.setLine(0, bs/2, 0, -bs/2);g2d.draw(ln);
        
        //linee di chiusura conci
        float[] fl = { 50, 25};
        BasicStroke str = new BasicStroke(1, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_BEVEL, 10, fl, 100);
        Stroke orStr = g2d.getStroke();
        g2d.setStroke(str);g2d.setColor(Color.gray);
        ln.setLine(-lt, -bs/2*1.1, -lt, bs/2*1.10);g2d.draw(ln);
        ln.setLine(lt, -bs/2*1.1, lt, bs/2*1.10);g2d.draw(ln);
        g2d.setStroke(orStr);
        
        g2d.setColor(coloreGiunzioni);
        rec.setFrame(-bxs/2, -bys/2, bxs, bys);g2d.draw(rec);
        //linee assi bulloni
        int y=e2bsup/2;
        for(int i=0;i<nfile2sup;++i){
            g2d.setStroke(str);g2d.setColor(Color.gray);
            ln.setLine(-bxs/2*1.10,y,bxs/2*1.10,y);g2d.draw(ln);
            int x=-bxs/2+e1asup;
            for(int j=0;j<nfile1sup;++j){
                g2d.setStroke(orStr);g2d.setColor(coloreGiunzioni);
                rec.setFrame(x-diametroSup/2,y-diametroSup/2,diametroSup,diametroSup);
                el.setFrame(rec);g2d.draw(el);
                rec.setFrame(x-diametroSup/2,-y-diametroSup/2,diametroSup,diametroSup);
                el.setFrame(rec);g2d.draw(el);
                rec.setFrame(-x-diametroSup/2,y-diametroSup/2,diametroSup,diametroSup);
                el.setFrame(rec);g2d.draw(el);
                rec.setFrame(-x-diametroSup/2,-y-diametroSup/2,diametroSup,diametroSup);
                el.setFrame(rec);g2d.draw(el);
                x+=p1sup;
            }
            
            y+=p2sup;
        }
        
    }
    
    void disegnaPiantaInf(Graphics2D g2d){
        
        // imposta la scala della finestra;
        setFattoreDiScala(g2d);
        
        AffineTransform at = new AffineTransform();
        at.setToTranslation(0, +Hfinestra/2-Hfinestra*v3/2);
        
        g2d.transform(at);
        
        double lt = bmax/2+8*ddquote;
        Line2D ln = new Line2D.Double();
        Rectangle2D rec = new Rectangle2D.Double();
        Ellipse2D el = new Ellipse2D.Double();
        g2d.setColor(coloreSezioneAcciaio);
        
        ln.setLine(-lt, -bi/2, lt, -bi/2);g2d.draw(ln);
        ln.setLine(-lt, bi/2, lt, bi/2);g2d.draw(ln);
        ln.setLine(0, bi/2, 0, -bi/2);g2d.draw(ln);
        
        //linee di chiusura conci
        float[] fl = { 50, 25};
        BasicStroke str = new BasicStroke(1, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_BEVEL, 10, fl, 100);
        Stroke orStr = g2d.getStroke();
        g2d.setStroke(str);g2d.setColor(Color.gray);
        ln.setLine(-lt, -bi/2*1.1, -lt, bi/2*1.10);g2d.draw(ln);
        ln.setLine(lt, -bi/2*1.1, lt, bi/2*1.10);g2d.draw(ln);
        g2d.setStroke(orStr);
        
        g2d.setColor(coloreGiunzioni);
        rec.setFrame(-bxi/2, -byi/2, bxi, byi);g2d.draw(rec);
        //linee assi bulloni
        int y=e2binf/2;
        for(int i=0;i<nfile2inf;++i){
            g2d.setStroke(str);g2d.setColor(Color.gray);
            ln.setLine(-bxi/2*1.10,y,bxi/2*1.10,y);g2d.draw(ln);
            int x=-bxi/2+e1ainf;
            for(int j=0;j<nfile1inf;++j){
                g2d.setStroke(orStr);g2d.setColor(coloreGiunzioni);
                rec.setFrame(x-diametroinf/2,y-diametroinf/2,diametroinf,diametroinf);
                el.setFrame(rec);g2d.draw(el);
                rec.setFrame(x-diametroinf/2,-y-diametroinf/2,diametroinf,diametroinf);
                el.setFrame(rec);g2d.draw(el);
                rec.setFrame(-x-diametroinf/2,y-diametroinf/2,diametroinf,diametroinf);
                el.setFrame(rec);g2d.draw(el);
                rec.setFrame(-x-diametroinf/2,-y-diametroinf/2,diametroinf,diametroinf);
                el.setFrame(rec);g2d.draw(el);
                x+=p1inf;
            }
            
            y+=p2inf;
        }
        
    }
    void disegnaProspetto(Graphics2D g2d){
        
        // imposta la scala della finestra;
        setFattoreDiScala(g2d);
        
//        AffineTransform at = new AffineTransform();
//        at.setToTranslation(0, -Hfinestra/2+Hfinestra*v1*0.5);
//        g2d.transform(at);
        
        //		titolo
        double lt = bmax/2+8*ddquote;
//		g2d.drawString("PROSPETTO",(float)(-lt) , (float)(-Hcarp/ 2));
//		g2d.drawString("PROSPETTO", (float)(-lt), (float)(-Hcarp/2));
        
        Line2D ln = new Line2D.Double();
        Rectangle2D rec = new Rectangle2D.Double();
        Ellipse2D el = new Ellipse2D.Double();
        g2d.setColor(coloreSezioneAcciaio);
        
        ln.setLine(-lt, -Hcarp/2, lt, -Hcarp/2);g2d.draw(ln);
        ln.setLine(-lt, -Hcarp/2+ts*FattoreScalaSpessori, lt, -Hcarp/2+ts*FattoreScalaSpessori);g2d.draw(ln);
        ln.setLine(-lt, Hcarp/2-ti*FattoreScalaSpessori, lt,Hcarp/2 -ti*FattoreScalaSpessori);g2d.draw(ln);
        ln.setLine(-lt, Hcarp/2, lt, Hcarp/2);g2d.draw(ln);
        //linea di separazione conci
        ln.setLine(0, Hcarp/2, 0, -Hcarp/2);g2d.draw(ln);
        
        //linee di chiusura conci
        float[] fl = { 50, 25};
        BasicStroke str = new BasicStroke(1, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_BEVEL, 10, fl, 100);
        Stroke orStr = g2d.getStroke();
        g2d.setStroke(str);g2d.setColor(Color.gray);
        ln.setLine(-lt, -Hcarp/2*1.1, -lt, Hcarp/2*1.10);g2d.draw(ln);
        ln.setLine(lt, -Hcarp/2*1.1, lt, Hcarp/2*1.10);g2d.draw(ln);
        g2d.setStroke(orStr);
        
        //piattabanda giunto d'anima
        g2d.setColor(coloreGiunzioni);
        rec.setFrame(-bxw/2, -byw/2, bxw, byw);g2d.draw(rec);
        //linee assi bulloni
        int y=-byw/2+e2w;
        for(int i=0;i<nfile2w;++i){
            g2d.setStroke(str);g2d.setColor(Color.gray);
            ln.setLine(-bxw/2*1.10,y,bxw/2*1.10,y);g2d.draw(ln);
            int x=-bxw/2+e1aw;
            for(int j=0;j<nfile1w;++j){
                g2d.setStroke(orStr);g2d.setColor(coloreGiunzioni);
                rec.setFrame(x-diametrow/2,y-diametrow/2,diametrow,diametrow);
                el.setFrame(rec);g2d.draw(el);
                rec.setFrame(-x-diametrow/2,y-diametrow/2,diametrow,diametrow);
                el.setFrame(rec);g2d.draw(el);
                x+=p1w;
            }
            
            y+=p2w;
        }
        
        //piattabande sup
        g2d.setColor(coloreGiunzioni);
        rec.setFrame(-bxs/2, -Hcarp/2-tgsup, bxs, tgsup);g2d.draw(rec);
        rec.setFrame(-bxs/2, -Hcarp/2+ts*FattoreScalaSpessori, bxs, tgsup);g2d.draw(rec);
        int x=-bxs/2+e1asup;
        g2d.setStroke(str);g2d.setColor(Color.gray);
        for(int i=0;i<nfile1sup;++i){
            ln.setLine(x,-Hcarp/2-tgsup-ddquote,x,-Hcarp/2+tgsup+ts*FattoreScalaSpessori+ddquote);g2d.draw(ln);
            ln.setLine(-x,-Hcarp/2-tgsup-ddquote,-x,-Hcarp/2+tgsup+ts*FattoreScalaSpessori+ddquote);g2d.draw(ln);
            x+=p1sup;
        }
        //piattabande inf
        g2d.setStroke(orStr);
        g2d.setColor(coloreGiunzioni);
        rec.setFrame(-bxi/2, +Hcarp/2-tginf-ti*FattoreScalaSpessori, bxi, tginf);g2d.draw(rec);
        rec.setFrame(-bxi/2, +Hcarp/2, bxi, tginf);g2d.draw(rec);
        x=-bxi/2+e1ainf;
        g2d.setStroke(str);g2d.setColor(Color.gray);
        for(int i=0;i<nfile1inf;++i){
            ln.setLine(x,Hcarp/2-ti-tginf -ddquote ,x,Hcarp/2+tginf+ddquote);g2d.draw(ln);
            ln.setLine(-x,Hcarp/2-ti-tginf -ddquote ,-x,Hcarp/2+tginf+ddquote);g2d.draw(ln);
            
            x+=p1inf;
        }
        
        
        g2d.setStroke(orStr);
        
    }
    
    
// disegna le quote
    void quota(Graphics2D g2d) {
        
        //imposto la finestra
        setOriginalTrasformation(g2d);
        setFattoreDiScala(g2d);
        g2d.setColor(coloreQuote);
        int dx=e1asup;
        int dy=e2w;

        
        //PROSPETTO
        //giunto sup
        AffineTransform at = new AffineTransform();
 /*       at.setToTranslation(0, -Hcarp/2-tgsup-3*ddquote);
        g2d.transform(at);
        disegnaQuotaTraDuePunti(-bxs/2, -bxs/2 +e1asup, "", ddquote, g2d);
        disegnaQuotaTraDuePunti(bxs/2 -e1asup,bxs/2,  "", ddquote, g2d);
        int dx=e1asup;
        for(int i =0;i<nfile1sup-1;++i){
            disegnaQuotaTraDuePunti(-bxs/2+ dx, -bxs/2+dx+p1sup, "", ddquote, g2d);
            disegnaQuotaTraDuePunti(bxs/2-dx-p1sup,bxs/2- dx, "", ddquote, g2d);
            dx+=p1sup;
        }
        disegnaQuotaTraDuePunti(-e1bsup, 0, "", ddquote, g2d);
        disegnaQuotaTraDuePunti( 0, e1bsup,"", ddquote, g2d);
        
        //anima
        //...quote orizzontali
        setFattoreDiScala(g2d);

        at.setToTranslation(0, -byw/2- 1.5*ddquote);
        g2d.transform(at);
        disegnaQuotaTraDuePunti(-bxw/2, -bxw/2 +e1aw, "", ddquote, g2d);
        disegnaQuotaTraDuePunti(bxw/2 -e1aw,bxw/2,  "", ddquote, g2d);
        dx=e1aw;
        for(int i =0;i<nfile1w-1;++i){
            disegnaQuotaTraDuePunti(-bxw/2+ dx, -bxw/2+dx+p1w, "", ddquote, g2d);
            disegnaQuotaTraDuePunti(bxw/2-dx-p1w,bxw/2- dx, "", ddquote, g2d);
            dx+=p1w;
        }
        disegnaQuotaTraDuePunti(-e1bw, 0, "", ddquote, g2d);
        disegnaQuotaTraDuePunti( 0, e1bw,"", ddquote, g2d);
        //...quote verticali
        setFattoreDiScala(g2d);
        at.setToTranslation(-bxw/2-3*ddquote, +byw/2);
        g2d.transform(at);
        at.setToRotation(-3.14/2);
        g2d.transform(at);
        disegnaQuotaTraDuePunti(0, e2w, "", ddquote, g2d);
        int dy=e2w;
        for(int i =0;i<nfile2w-1;++i){
            disegnaQuotaTraDuePunti(dy, dy+p2w, "", ddquote, g2d);
            dy+=p2w;
        }
        disegnaQuotaTraDuePunti(byw-e2w, byw, "", ddquote, g2d);
        
        //giunto inf
        setFattoreDiScala(g2d);
        at.setToTranslation(0,Hcarp/2+tgsup+5*ddquote);
        g2d.transform(at);
        disegnaQuotaTraDuePunti(-bxi/2, -bxi/2 +e1ainf, "", ddquote, g2d);
        disegnaQuotaTraDuePunti(bxi/2 -e1ainf,bxi/2,  "", ddquote, g2d);
        dx=e1ainf;
        for(int i =0;i<nfile1inf-1;++i){
            disegnaQuotaTraDuePunti(-bxi/2+ dx, -bxi/2+dx+p1inf, "", ddquote, g2d);
            disegnaQuotaTraDuePunti( bxi/2-dx-p1inf, bxi/2- dx,"", ddquote, g2d);
            dx+=p1inf;
        }
        disegnaQuotaTraDuePunti(-e1binf, 0, "", ddquote, g2d);
        disegnaQuotaTraDuePunti(0, e1binf, "", ddquote, g2d);
*/        
        //PIATTABANDA SUP
        setFattoreDiScala(g2d);
        at.setToTranslation(0,-bs/2-3*ddquote);
        g2d.transform(at);
        disegnaQuotaTraDuePunti(-bxs/2, -bxs/2 +e1asup, "", ddquote, g2d);
        disegnaQuotaTraDuePunti(bxs/2 -e1asup,bxs/2,  "", ddquote, g2d);
        dx=e1asup;
        for(int i =0;i<nfile1sup-1;++i){
            disegnaQuotaTraDuePunti(-bxs/2+ dx, -bxs/2+dx+p1sup, "", ddquote, g2d);
            disegnaQuotaTraDuePunti(bxs/2-dx-p1sup,bxs/2- dx, "", ddquote, g2d);
            dx+=p1sup;
        }
        disegnaQuotaTraDuePunti(-e1bsup, 0, "", ddquote, g2d);
        disegnaQuotaTraDuePunti( 0, e1bsup,"", ddquote, g2d);
        //...quote verticali
        setFattoreDiScala(g2d);
        at.setToTranslation(-bxs/2-3*ddquote,+ bys/2);
        g2d.transform(at);
        at.setToRotation(-3.14/2);
        g2d.transform(at);
        disegnaQuotaTraDuePunti(0, e2asup, "", ddquote, g2d);
        disegnaQuotaTraDuePunti(bys-e2asup, bys, "", ddquote, g2d);
        dy=e2asup;
        for(int i =0;i<nfile2sup-1;++i){
            disegnaQuotaTraDuePunti(dy, dy+p2sup, "", ddquote, g2d);
            disegnaQuotaTraDuePunti(bys-dy-p2sup, bys-dy, "", ddquote, g2d);
            dy+=p2sup;
        }
        disegnaQuotaTraDuePunti(bys/2-e2bsup/2, bys/2+e2bsup/2, "", ddquote, g2d);
        disegnaQuotaTraDuePunti(bys-e2asup, bys, "", ddquote, g2d);

        
 /*       
        //PIATTABANDA INF
        setFattoreDiScala(g2d);
        at.setToTranslation(0,Hfinestra/2-Hfinestra*v3/2-bi/2-3*ddquote);
        g2d.transform(at);
        disegnaQuotaTraDuePunti(-bxi/2, -bxi/2 +e1ainf, "", ddquote, g2d);
        disegnaQuotaTraDuePunti(bxi/2 -e1ainf,bxi/2,  "", ddquote, g2d);
        dx=e1ainf;
        for(int i =0;i<nfile1inf-1;++i){
            disegnaQuotaTraDuePunti(-bxi/2+ dx, -bxi/2+dx+p1inf, "", ddquote, g2d);
            disegnaQuotaTraDuePunti(bxi/2-dx-p1inf,bxi/2- dx, "", ddquote, g2d);
            dx+=p1inf;
        }
        disegnaQuotaTraDuePunti(-e1binf, 0, "", ddquote, g2d);
        disegnaQuotaTraDuePunti( 0, e1binf,"", ddquote, g2d);
        //...quote verticali
        setFattoreDiScala(g2d);
        at.setToTranslation(-bxi/2-3*ddquote,+Hfinestra/2-Hfinestra*v3/2 + byi/2);
        g2d.transform(at);
        at.setToRotation(-3.14/2);
        g2d.transform(at);
        disegnaQuotaTraDuePunti(0, e2ainf, "", ddquote, g2d);
        dy=e2ainf;
        for(int i =0;i<nfile2inf-1;++i){
            disegnaQuotaTraDuePunti(dy, dy+p2inf, "", ddquote, g2d);
            disegnaQuotaTraDuePunti(byi-dy-p2inf, byi-dy, "", ddquote, g2d);
            dy+=p2inf;
        }
        disegnaQuotaTraDuePunti(byi/2-e2binf/2, byi/2+e2binf/2, "", ddquote, g2d);
        
        disegnaQuotaTraDuePunti(byi-e2ainf, byi, "", ddquote, g2d);
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
        int tH = (int)(ddquote*1.8);
        if (dx > tW) {
            g2d.drawString(quota, x2 / 2 + x1 / 2 - tW / 2, -ddquote / 2);
        } else {
//			g2d.drawString(quota, Math.max(x2, x1) + ddquote, 0);
            g2d.drawString(quota, x2 / 2 + x1 / 2 - tW / 2,(int) (-tH*1.3));
        }
        
    }
    
    void setOriginalTrasformation(Graphics2D g) {
        g.setTransform(atOrigine);
    }
    
    // restituisce la posizione dell'intradosso piattabanda inferiore rispetto
    // al bordo
    // superiore della finestra
/*	private int getYorigine() {
                return (int)(0.5*Htot );
        }
 */
    private void jbInit() throws Exception {
        this.setMinimumSize(new Dimension(100, 100));
        this.setVerifyInputWhenFocusTarget(true);
    }
}
