package cassone.view.Giunti;

/*
 * Created on 23-mar-2004
 * Andrea Cavalieri
 *
 */

/**
 * @author Andrea
 * 
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

/*public class TensioniInstabilitaView extends JPanel {

	// trasformazione originale
	AffineTransform atOrigine;

	// variabili globali del progetto
	private int B, H, b0, b1, b2, b, h,be1,be2;

	private int Btot = 0;
	
	private int bs, ts, tw, hw, bi, ti;

	//tensioni reali
	//private int ssL,sL,iiL,iL;
	//tensioni scalate per il fattore di scala
	private int ss,s,ii,i;
	
	//valori instabilità
	private int ssLred,sLred,iiLred,iLred,ss1Lred,ss2Lred;
	private int ssred,sred,iired,ired,ss1red,ss2red;
	private int bef1,bef2,bsred,bired;
	boolean instabilitaEC3;
	
	//
	String combinazione;
	
	private boolean calcoloBeff;

	private double[] n = new double[5];

	// variabili di utilità
	private int Htot, dxAnima, Hcarp;

	// variabili di utilità grafica
	private int Bfinestra, Hfinestra;

	private int FattoreScalaSpessori;

	// grafica
	private Color coloreSezioneAcciaio = Color.BLUE;

	private Color coloreCopella = Color.darkGray;

	private Color coloreSoletta = Color.lightGray;

	private Color coloreQuote = Color.RED;

	private Color[] coloreAssi = { Color.magenta, Color.cyan, Color.yellow,
			Color.green, Color.ORANGE };

	// utility grafica
	private int ddquote;

	private static TensioniInstabilitaView sezTipo;
	
	private int ns;

	*//**
	 * 
	 *//*
	public TensioniInstabilitaView() {
		super();
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static TensioniInstabilitaView getInstance() {
		if (sezTipo == null) {
			sezTipo = new TensioniInstabilitaView();
		}
		return sezTipo;
	}

	*//**
	 * The paint method provides the real magic. Here we cast the Graphics
	 * object to Graphics2D to illustrate that we may use the same old graphics
	 * capabilities with Graphics2D that we are used to using with Graphics.
	 *//*
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

			disegnaAssiNeutri(g2d);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// salva le variabili del progetto in variabili private della
	// classe presente
	void getVariabiliLocali() {

		Progetto prg = Progetto.getInstance();
		Sezione sezione = prg.getCurrentSezioneVerifica();
		SolettaT sl = (SolettaT)sezione.getSoletta();
		SezioneMetallicaDoppioT concio =(SezioneMetallicaDoppioT) sezione.getSezioneMetallica();

		
		b0 = Math.round((float)(sl.getBo()));

		// piattabanda inferiore
		bi = Math.round((float)(concio.getBi()));
		ti = Math.round((float) (concio.getTi()));

		// anima
		tw =Math.round((float) (concio.getTw()));
		hw = Math.round((float) (concio.getHw()));

		// piattabanda superiore
		bs = Math.round((float) (concio.getBs()));
		ts = Math.round((float) (concio.getTs()));

		// utility
		Hcarp = ti + hw + ts;

		FattoreScalaSpessori = 1;

		//RIGA SELEZIONATA
		int row= 0;
		//nome combinazione selezionata
		combinazione = prg.getCombinazioni().get(row).getName();
	
	     row = TableVerificaGiuntiSup.getInstance().getTable().getSelectedRow();
	     if(row==-1) row=0;
	     
		//tensioni ridotte
		SezioneMetallica ps = sezione.getSezioniMetEfficaci().get(row);
//		ns = sezione.getIrrigidimentiLong().size();

		ssLred = Math.round((float)(ps.getSeff()));
//		sLred=Math.round((float)(ps.getIeff()));
//		iLred = Math.round((float)(ps.getIred()));
		iiLred=Math.round((float)(ps.getIeff()));
//		bsred=Math.round((float)(ps.getBsred()));
//		bired=Math.round((float)(ps.getBired()));

		if(ns==0){
//			ss1Lred = Math.round((float)(ps.getSs1red()));
//			ss2Lred = Math.round((float)(ps.getSs2red()));
				
//			bef1=Math.round((float)(ps.getBef1()));
//			bef2=Math.round((float)(ps.getBef2()));
		}else if(ns==2){
			
		}else if (ns==2) {
			
		}		
	}

	// imposta il fattore di scala della classe AffineTransform at
	void setFattoreDiScala(Graphics2D g) {

		double fx, fy, W = 0, H = 0;
		double xOrig = 0, Yorig = 0;


		setOriginalTrasformation(g);

		AffineTransform at = new AffineTransform();

		W = getWidth();
		H = getHeight();

		Btot=2*Math.max(bi, bs);
		Htot = Hcarp;
		
		fx = Math.min((W/4) / (Btot * 1.2), (H) / (Htot * 1.8));
		fy = fx;

		xOrig = W / 4;
		Yorig = H / 2;

		// imposta grafica
		at.setToTranslation(xOrig, Yorig);
		g.transform(at);
		at.setToScale(fx, fy);
		g.transform(at);

		// variabili di utilità grafica
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		ddquote = (int) (screenSize.width / 220 / fy);

		
		//tensioni
		double fTensioni = Math.abs(2.4*Btot/(ssLred-iiLred));
		
		ssred = (int)(ssLred*fTensioni);
		sred = (int)(sLred*fTensioni);
		ired = (int)(iLred*fTensioni);
		iired = (int)(iiLred*fTensioni);
		ss1red = (int)(ss1Lred*fTensioni);
		ss2red = (int)(ss2Lred*fTensioni);
	
	}

	// disegna il cassone completo
	void disegnaSezioneTipo(Graphics2D g2d) {
		disegnaCarpenteriaMetallica(g2d);
		//disegnaTensioni(g2d);
		//disegnaSoletta(g2d);
		quota(g2d);
		disegnaTensioniRidotte(g2d);
	}

	void disegnaTensioniRidotte (Graphics2D g2d){
		// imposta la scala della finestra;
		setFattoreDiScala(g2d);
	
		AffineTransform at = new AffineTransform();
		at.setToTranslation(2.4*Btot, getYorigine());
		g2d.transform(at);
		g2d.setColor(Color.YELLOW);
	
		if(ns==0)
		{
		//linea verticale
		Line2D ln = new Line2D.Double(0, 0, 0, -Hcarp);
		g2d.draw(ln);
		//ss
		ln = new Line2D.Double(0, -Hcarp, ssred, -Hcarp);
		g2d.draw(ln);
		//s
		ln = new Line2D.Double(ssred, -Hcarp, sred, -Hcarp+ts);
		g2d.draw(ln);
		//s1red
		ln = new Line2D.Double(sred, -Hcarp+ts, ss1red, -Hcarp+ts+bef1);
		g2d.draw(ln);
		ln = new Line2D.Double(ss1red, -Hcarp+ts+bef1, 0, -Hcarp+ts+bef1);
		g2d.draw(ln);
		
		//s2red
		ln = new Line2D.Double(0, -bef2-ti, ss2red, -bef2-ti);
		g2d.draw(ln);
		//i
		ln = new Line2D.Double(ss2red, -bef2-ti, ired, -Hcarp+ts+hw);
		g2d.draw(ln);
		//ii
		ln = new Line2D.Double(ired, -Hcarp+ts+hw, iired, 0);
		g2d.draw(ln);
		//linee orizzontali
		ln = new Line2D.Double(0, -Hcarp+ts, sred, -Hcarp+ts);
		g2d.draw(ln);
		ln = new Line2D.Double(0, -Hcarp+ts+hw, ired, -Hcarp+ts+hw);
		g2d.draw(ln);
		ln = new Line2D.Double(0, 0, iired, 0);
		g2d.draw(ln);
		}else{
			//linea verticale
			Line2D ln = new Line2D.Double(0, 0, 0, -Hcarp);
			g2d.draw(ln);
			//ss
			ln = new Line2D.Double(0, -Hcarp, ssred, -Hcarp);
			g2d.draw(ln);
			//s
			ln = new Line2D.Double(ssred, -Hcarp, sred, -Hcarp+ts);
			g2d.draw(ln);
			//i
			ln = new Line2D.Double(sred, -Hcarp+ts, ired, -Hcarp+ts+hw);
			g2d.draw(ln);
			//ii
			ln = new Line2D.Double(ired, -Hcarp+ts+hw, iired, 0);
			g2d.draw(ln);
			//linee orizzontali
			ln = new Line2D.Double(0, -Hcarp+ts, sred, -Hcarp+ts);
			g2d.draw(ln);
			ln = new Line2D.Double(0, -Hcarp+ts+hw, ired, -Hcarp+ts+hw);
			g2d.draw(ln);
			ln = new Line2D.Double(0, 0, iired, 0);
			g2d.draw(ln);		
		
		}
		
		//scritte
		String str1 = "ss=" + Integer.toString(ssLred) +" MPa" ;
		String str2 = "s=" + Integer.toString(sLred)+" MPa" ;
		String str3 = "i=" + Integer.toString(iLred)+" MPa" ;
		String str4 = "ii=" + Integer.toString(iiLred)+" MPa" ;
		
		Font fn = new Font("Arial", 0, (int) (ddquote * 1.8));
		g2d.setFont(fn);

		FontMetrics metrics = g2d.getFontMetrics();
		int tW = metrics.stringWidth(str1);
		
		g2d.drawString(str1, ssred/2-tW/2, -Hcarp-ddquote);
		g2d.drawString(str2, sred/2-tW/2, -Hcarp+2*ddquote+ts);
		g2d.drawString(str3, ired/2-tW/2, -Hcarp-ddquote+ts+hw);
		g2d.drawString(str4, iired/2-tW/2, 2*ddquote);

		
	}
	
	//disegna le tensioni
	void disegnaTensioni(Graphics2D g2d){
		// imposta la scala della finestra;
		setFattoreDiScala(g2d);

		AffineTransform at = new AffineTransform();
		at.setToTranslation(2.4*Btot, getYorigine());
		g2d.transform(at);
		g2d.setColor(Color.YELLOW);
		
		//linea verticale
		Line2D ln = new Line2D.Double(0, 0, 0, -Hcarp);
		g2d.draw(ln);
		//ss
		ln = new Line2D.Double(0, -Hcarp, ss, -Hcarp);
		g2d.draw(ln);
		//s
		ln = new Line2D.Double(ss, -Hcarp, s, -Hcarp+ts);
		g2d.draw(ln);
		//i
		ln = new Line2D.Double(s, -Hcarp+ts, i, -Hcarp+ts+hw);
		g2d.draw(ln);
		//ii
		ln = new Line2D.Double(i, -Hcarp+ts+hw, ii, 0);
		g2d.draw(ln);
		//linee orizzontali
		ln = new Line2D.Double(0, -Hcarp+ts, s, -Hcarp+ts);
		g2d.draw(ln);
		ln = new Line2D.Double(0, -Hcarp+ts+hw, i, -Hcarp+ts+hw);
		g2d.draw(ln);
		ln = new Line2D.Double(0, 0, ii, 0);
		g2d.draw(ln);
		
		//scritte
		String str1 = "ss=" + Integer.toString(ssL) +" MPa" ;
		String str2 = "s=" + Integer.toString(sL)+" MPa" ;
		String str3 = "i=" + Integer.toString(iL)+" MPa" ;
		String str4 = "ii=" + Integer.toString(iiL)+" MPa" ;
		
		Font fn = new Font("Arial", 0, (int) (ddquote * 1.8));
		g2d.setFont(fn);

		FontMetrics metrics = g2d.getFontMetrics();
		int tW = metrics.stringWidth(str1);
		
		g2d.drawString(str1, ss/2-tW/2, -Hcarp-ddquote);
		g2d.drawString(str2, s/2-tW/2, -Hcarp+2*ddquote+ts);
		g2d.drawString(str3, i/2-tW/2, -Hcarp-ddquote+ts+hw);
		g2d.drawString(str4, ii/2-tW/2, 2*ddquote);

		
	}
	
	// disegna la carpenteria metallica
	void disegnaCarpenteriaMetallica(Graphics2D g2d) {

		// imposta la scala della finestra;
		setFattoreDiScala(g2d);

		AffineTransform at = new AffineTransform();
		at.setToTranslation(0, getYorigine());
		g2d.transform(at);

		// disegna l'ala inferiore
		Rectangle2D sInf = new Rectangle2D.Double(-bi / 2, -ti
				* FattoreScalaSpessori, bi, ti * FattoreScalaSpessori);
		g2d.setColor(coloreSezioneAcciaio);
		g2d.draw(sInf);
			sInf = new Rectangle2D.Double(-bired / 2, -ti
					* FattoreScalaSpessori, bired, ti * FattoreScalaSpessori);
			g2d.setColor(coloreSezioneAcciaio);
		g2d.fill(sInf);


		// disegna le amime
		Rectangle2D sAla = new Rectangle2D.Double(-tw / 2
				* FattoreScalaSpessori, -Hcarp, tw * FattoreScalaSpessori,
				Hcarp);
		Rectangle2D sAla2 = new Rectangle2D.Double();
		
		g2d.setColor(coloreSezioneAcciaio);
		g2d.draw(sAla);
			sAla = new Rectangle2D.Double(-tw / 2
					* FattoreScalaSpessori, -bef2, tw * FattoreScalaSpessori,
					bef2);
			sAla2 = new Rectangle2D.Double(-tw / 2
					* FattoreScalaSpessori, -Hcarp, tw * FattoreScalaSpessori,
					bef1);
		g2d.fill(sAla);
		g2d.fill(sAla2);

		// disegna le ali superiori
		Rectangle2D sSup = new Rectangle2D.Double(-bs / 2, -Hcarp, bs, ts
				* FattoreScalaSpessori);
		g2d.draw(sSup);
			sSup= new Rectangle2D.Double(-bsred / 2, -Hcarp, bsred, ts * FattoreScalaSpessori);
			g2d.setColor(coloreSezioneAcciaio);
		g2d.fill(sSup);

	}

	// disegna la soletta di calcestruzzo
	void disegnaSoletta(Graphics2D g2d) {
		setOriginalTrasformation(g2d);
		setFattoreDiScala(g2d);

		// mi sposto nella sezione all'estradosso carpenteria metallica
		AffineTransform at = new AffineTransform();
		at.setToTranslation(Bfinestra / 2, getYorigine() - Hcarp);
		g2d.transform(at);
		g2d.setColor(coloreSoletta);

		// disegno il primo blocchetto di calcestruzzo bxh
		Rectangle2D ac1 = new Rectangle2D.Double(-b / 2,-h, b, h);
		g2d.fill(ac1);

		//mi sposto in instradosso soletta
		at = new AffineTransform();
		at.setToTranslation(0,-h);
		g2d.transform(at);
		//disegno la soletta superiore
		if (calcoloBeff){
			Rectangle2D ac2 = new Rectangle2D.Double(-b0 / 2-b1,-H, b1+b0+b2, H);
			g2d.draw(ac2);
			ac2 = new Rectangle2D.Double(-b0 / 2-be1,-H, be1+b0+be2, H);
			g2d.draw(ac2);
			g2d.fill(ac2);
		}else{
			Rectangle2D ac2 = new Rectangle2D.Double(-B / 2,-H, B, H);
			g2d.fill(ac2);
		}
		
			
		int vl = Lpi / 2 + dxAnima
				- (int) (FattoreScalaSpessori / 2 * Sai / Math.cos(alfa));
		int vl2 = vl - Lps / 2;
		int vl3 = vl + Lps / 2;
		int dy = Ss2 - Ss1;

		int[] x0 = new int[4];
		int[] y0 = new int[4];
		x0[0] = x0[3] = -vl2 - 80;
		x0[1] = x0[2] = x0[0] + 2 * vl2 + 160;
		y0[0] = y0[1] = 0;
		y0[2] = y0[3] = -Spr;
		Polygon ala = new Polygon(x0, y0, 4);
		g2d.fill(ala);

		int[] x1 = new int[6];
		int[] y1 = new int[6];
		x1[0] = x1[5] = vl3 - 80;
		x1[1] = x1[4] = Beff_i / 2;
		x1[2] = x1[3] = Beff_s / 2;

		y1[0] = y1[1] = 0;
		y1[4] = y1[5] = -Spr;
		y1[2] = -dy;
		y1[3] = -dy - Spr;
		ala = new Polygon(x1, y1, 6);
		g2d.fill(ala);

		int[] x2 = new int[6];
		int[] y2 = new int[6];
		x2[0] = -x1[0];
		x2[1] = -x1[1];
		x2[2] = -x1[2];
		x2[3] = -x1[3];
		x2[4] = -x1[4];
		x2[5] = -x1[5];
		y2[0] = y1[0];
		y2[1] = y1[1];
		y2[2] = y1[2];
		y2[3] = y1[3];
		y2[4] = y1[4];
		y2[5] = y1[5];

		ala = new Polygon(x2, y2, 6);
		g2d.fill(ala);

		int[] x = new int[14];
		int[] y = new int[14];
		x[0] = x1[0];
		x[1] = x1[5];
		x[2] = x1[4];
		x[3] = x1[3];
		x[4] = x1[3];

		y[0] = y1[0];
		y[1] = y1[5];
		y[2] = y1[4];
		y[3] = y1[3];
		y[4] = y1[3] - Ss1;

		y[5] = y[4];
		x[5] = x2[3];

		x[6] = x2[3];
		x[7] = x2[4];
		x[8] = x2[5];
		x[9] = x2[0];

		y[6] = y2[3];
		y[7] = y2[4];
		y[8] = y2[5];
		y[9] = y2[0];

		x[10] = x0[0];
		x[11] = x0[3];
		x[12] = x0[2];
		x[13] = x0[1];

		y[10] = y0[0];
		y[11] = y0[3];
		y[12] = y0[2];
		y[13] = y0[1];

		g2d.setColor(coloreSoletta);
		ala = new Polygon(x, y, 14);
		g2d.fill(ala);

		// area controsoletta
		if (areaControSoletta != 0) {
			setOriginalTrasformation(g2d);
			setFattoreDiScala(g2d);

			at = new AffineTransform();
			at.setToTranslation(Bfinestra / 2, getYorigine() - Spi
					* FattoreScalaSpessori);
			g2d.transform(at);
			g2d.setColor(coloreSoletta);
			Rectangle2D rc = new Rectangle2D.Double(-Lpi / 2, -100, Lpi, 100);
			g2d.fill(rc);

		}
		
	}


	// disegna li assi neutri della sezione per i vari n
	void disegnaAssiNeutri(Graphics2D g2d) {

		// tratteggio
		float[] fl = { 100, 50 };
		BasicStroke str = new BasicStroke(1, BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_BEVEL, 10, fl, 100);
		Stroke orStr = g2d.getStroke();

		int b = (int) (Lpi / 2 + dxAnima);
		int db = (int) ((Beff_s / 2 - b) / 3);
		int nc = 0;

		// per ogni n
		for (int i = 0; i < 4; ++i) {

			// disegna prima n3 di n2
			if (i == 1)
				nc = 1;
			if (i == 2)
				nc = 3;
			if (i == 3)
				nc = 2;
			if (i == 0)
				nc = 0;

			// continua se è zero
			if (hgi[nc] == 0)
				continue;

			// imposta gli assi
			setOriginalTrasformation(g2d);
			setFattoreDiScala(g2d);
			AffineTransform at = new AffineTransform();

			if (i != 0) {
				at.setToTranslation(Bfinestra / 2 + b + db * (i - 1),
						getYorigine() - hgi[nc]);
			} else {
				at.setToTranslation(Bfinestra / 2 - b - db, getYorigine()
						- hgi[nc]);
			}
			g2d.transform(at);
			// imposta colori e penne
			g2d.setStroke(str);

			g2d.setColor(coloreAssi[i]);
			Line2D ln = new Line2D.Double(0, 0, db, 0);
			g2d.draw(ln);

			String ns;
			if (n[i] == Double.MAX_VALUE)
				ns = "\u221E";
			else
				ns = Double.toString(n[nc]);

			String str1 = "n=" + ns; // + "" + Integer.toString(hgi[i]/10);
			FontMetrics metrics = g2d.getFontMetrics();
			int tW = metrics.stringWidth(str1);
			g2d.drawString(str1, tW / 2, -30);

			// quote
			// imposta colori e penne
			g2d.setStroke(orStr);
			at = new AffineTransform();
			at.setToRotation(-Math.PI / 2);
			g2d.transform(at);
			disegnaQuotaTraDuePunti(-hgi[nc], 0, "", ddquote, g2d);

			

		}

	}

	// disegna le quote
	void quota(Graphics2D g2d) {

		//imposto la finestra
		setOriginalTrasformation(g2d);
		setFattoreDiScala(g2d);
		g2d.setColor(coloreQuote);
		AffineTransform at = new AffineTransform();

		at.setToTranslation(3*Btot/2, getYorigine());
		g2d.transform(at);

	
		//TITOLO
		Font fn = new Font("Arial", 0, (int) (ddquote * 2.2));
		g2d.setFont(fn);

		FontMetrics metrics = g2d.getFontMetrics();
		String s = "TENSIONI PRINCIPALI COMBINAZIONE: "+combinazione ;
		int tW = metrics.stringWidth(s);
		g2d.setColor(Color.CYAN);
		g2d.drawString(s, -tW/2, -Hcarp-10*ddquote);
		g2d.setColor(Color.red);
		
			
		
		//quota piattabanda inf
		//riposizioni gli assi
		setFattoreDiScala(g2d);
		at.setToTranslation(Bfinestra / 2, getYorigine() + 4 * ddquote);
		g2d.transform(at);
		disegnaQuotaTraDuePunti(-bi/2, bi/2, "", ddquote, g2d);
		
		//quota piattabanda sup
		//riposizioni gli assi
		setFattoreDiScala(g2d);
		at.setToTranslation(Bfinestra / 2, getYorigine() - Hcarp+5*ddquote);
		g2d.transform(at);
		disegnaQuotaTraDuePunti(-bs/2, bs/2, "", ddquote, g2d);

		//quote verticali
		setFattoreDiScala(g2d);
		at.setToTranslation(-Btot/2-4*ddquote, getYorigine());
		g2d.transform(at);
		at.setToRotation(-Math.PI / 2);
		g2d.transform(at);
		disegnaQuotaTraDuePunti(0, Hcarp, "", ddquote, g2d);
		if(instabilitaEC3){
			at.setToTranslation(0, 4*ddquote);
			g2d.transform(at);
			disegnaQuotaTraDuePunti(ti, ti+bef2, "", ddquote, g2d);
			disegnaQuotaTraDuePunti(ti+bef2, Hcarp-ts-bef1, "", ddquote, g2d);
			disegnaQuotaTraDuePunti(Hcarp-ts-bef1, Hcarp-ts, "", ddquote, g2d);
		}
		
	
		// spessori piattabande
		setFattoreDiScala(g2d);
		at.setToTranslation(Bfinestra / 2 + bi / 4, getYorigine());
		g2d.transform(at);
		// spessore paittabanda inferiore
		Line2D ln = new Line2D.Double(0, 0, ddquote, 3 * ddquote);
		Drawing.leader(0, 0, 0, 2 * ddquote, 2 * ddquote, "sp.="
				+ Integer.toString(ti) + "mm", g2d);
		// anima
		at = new AffineTransform();
		at.setToTranslation(-bi / 4, 0);
		g2d.transform(at);
		ln.setLine(0, -Hcarp / 2, 0 + Hcarp / 7, -Hcarp / 2);
		g2d.draw(ln);
		g2d.drawString("sp.=" + Integer.toString(tw) + " mm", Hcarp / 7,
				-Hcarp / 2);

		//superiore
		setFattoreDiScala(g2d);
		at = new AffineTransform();
		at.setToTranslation(Bfinestra / 2 + bs / 2.5, getYorigine()-Hcarp + ts);
		g2d.transform(at);
		Drawing.leader(0, 0, 0, 2 * ddquote, 2 * ddquote, "sp.="
				+ Integer.toString(ts) + "mm", g2d);

		
		
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

	// restituisce la posizione dell'intradosso piattabanda inferiore rispetto
	// al bordo
	// superiore della finestra
	private int getYorigine() {
		return (int)(0.6*Htot );
	}

	private void jbInit() throws Exception {
		this.setMinimumSize(new Dimension(100, 100));
		this.setVerifyInputWhenFocusTarget(true);
	}
}*/
