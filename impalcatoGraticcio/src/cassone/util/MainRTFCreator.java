package cassone.util;

import cassone.model.SezioniMetalliche.SezioneMetallicaCassoneII;
import cassone.model.SezioniMetalliche.SezioneMetallicaGenerica;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.NumberFormat;
import java.util.ArrayList;

import cassone.model.CombinazioneCarichi;
import cassone.model.CondizioniCarico;
import cassone.model.MaterialeAcciaio;
import cassone.model.ParametriStatici;
import cassone.model.Progetto;
import cassone.model.Sezione;
import cassone.model.SezioneOutputTensioniFase;
import cassone.model.Sollecitazioni;
import cassone.model.SezioniMetalliche.SezioneMetallica;
import cassone.model.soletta.Soletta;
import cassone.view.Instabilita.SezioneIrrigiditaView;
import cassone.view.VerificaTensioni.TensioniVerificaView;
import cassone.view.analisi.SezioneTipoView;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Jpeg;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Table;
import com.lowagie.text.rtf.RtfWriter;
public class MainRTFCreator {
    
    // private static Font fontTitleTable = new Font(Font.COURIER, 12,
    // Font.NORMAL);
    
    private static Font title1 = new Font(Font.TIMES_ROMAN, 14, Font.BOLD);
    
    private static Font title2 = new Font(Font.TIMES_ROMAN, 14, Font.BOLD);
    
    private static Font title3 = new Font(Font.TIMES_ROMAN, 12, Font.BOLDITALIC);
    
    private static Font corpo = new Font(Font.TIMES_ROMAN, 11, Font.NORMAL);
    
    public static String NEW_LINE = "\n";
    
    private static NumberFormat nf;
    
    static {
        nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
    }
    
    public static Document creaRTFDocument(String fileName) throws Exception {
        // step 1: creation of a document-object
        Document document = new Document();
        
        RtfWriter.getInstance(document, new FileOutputStream(fileName));
        document.open();
        document.setMargins(40, 40, 40, 40);
        // main
        scriviAnalisiSollecitazioni(/* fileName, */document);
        scriviVerificheTensionali(/* fileName, */document);
        scriviVerificheStabilita(/* fileName, */document);
        
        document.close();
        
        return document;
    }
    
    public static Document scriviVerificheTensionali(Document document)
    throws Exception {
        
        Progetto prg = Progetto.getInstance();
        ArrayList<Sezione> sezioniVerifica = prg.getSezioniVerifica();
        
        Paragraph par = new Paragraph("PARTE 2: VERIFICICA TENSIONALE", title2);
        document.add(par);
        document.add(new Phrase(NEW_LINE));
        
        for (int i = 0; i < sezioniVerifica.size(); i++) {
            Sezione sez = sezioniVerifica.get(i);
            scriviSezioneVerificaTensioni(document, sez);
            document.add(new Phrase(NEW_LINE));
        }
        
        return document;
    }
    
    public static Document scriviVerificheStabilita(Document document)
    throws Exception {
        
        Progetto prg = Progetto.getInstance();
        ArrayList<Sezione> sezioniVerifica = prg.getSezioniVerifica();
        
        Paragraph par = new Paragraph("PARTE 3: VERIFICA STABILITA' LOCALE",
                title2);
        document.add(par);
        document.add(new Phrase(NEW_LINE));
        
        par = new Paragraph("GENERALITA'", title3);
        document.add(par);
        
        par = new Paragraph("Materiale acciaio", corpo);
        document.add(par);
        Table tab = getTabellaMateriale(prg.getMateriale());
        document.add(tab);
        
        for (int i = 0; i < sezioniVerifica.size(); i++) {
            Sezione sez = sezioniVerifica.get(i);
            
            if(sez.getSezioneMetallica().getClass() == SezioneMetallicaGenerica.class) continue;
            scriviSezioneVerificaStabilita(document, sez);
            document.add(new Phrase(NEW_LINE));
        }
        
        return document;
    }
    
    public static Document scriviAnalisiSollecitazioni(Document document)
    throws Exception {
        
        Progetto prg = Progetto.getInstance();
        ArrayList<Sezione> sezioniAnalisi = prg.getSezioniAnalisiGlobale();
        
        Paragraph par1 = new Paragraph(prg.getNomeProgetto(), title1);
        par1.setAlignment(Paragraph.ALIGN_LEFT);
        document.add(par1);
        document.add(new Phrase(NEW_LINE));
        
        Paragraph par = new Paragraph(
                "PARTE 1: CARATTERISTICHE INERZIALI ANALISI GLOBALE", title2);
        document.add(par);
        document.add(new Phrase(NEW_LINE));
        
        for (int i = 0; i < sezioniAnalisi.size(); i++) {
            Sezione sez = sezioniAnalisi.get(i);
            scriviSezioneAnalisi(document, sez);
            document.add(new Phrase(NEW_LINE));
        }
        
        return document;
    }
    
    private static Document scriviSezioneAnalisi(Document document, Sezione sez)
    throws Exception {
        
        SezioneMetallica sm = sez.getSezioneMetallica();
        Soletta sl = sez.getSoletta();
        
        Paragraph par = new Paragraph("SEZIONE: " + sez.getName(), title3);
        document.add(par);
        
        // ImageIcon p1 = new ImageIcon("prova.jpg");
        salvaImmagine(sez);
        Image img2;
        img2 = new Jpeg("sez.jpg");
        img2.setAlignment(Paragraph.ALIGN_CENTER);
        par = new Paragraph();
        par.add(img2);
        par.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(par);
        
        par = new Paragraph("Caratteristiche concio metallico:", corpo);
        document.add(par);
        Table tbsm = sm.getTabellaInput();
        document.add(tbsm);
        
        par = new Paragraph("Caratteristiche soletta in calcestruzzo:", corpo);
        document.add(par);
        Table tbsl = sl.getTabellaInput();
        document.add(tbsl);
        
        par = new Paragraph("Armatura soletta", corpo);
        document.add(par);
        Table tbAr = getTabellaArmatura(sez);
        document.add(tbAr);
        
        par = new Paragraph(
                "Parametri statici per l'analisi delle sollecitazioni:", corpo);
        document.add(par);
        
        Table tab = getTabellaParametriAnalisiGlobale(sez);
        document.add(tab);
        
        return document;
    }
    
    private static Document scriviSezioneVerificaStabilita(Document document,
            Sezione sez) throws Exception {
        
        Progetto prg = Progetto.getInstance();
        SezioneMetallica sml = sez.getSezioneMetallica();
        // Soletta sl = sez.getSoletta();
        
        Paragraph par = new Paragraph("SEZIONE: " + sez.getName(), title3);
        document.add(par);
        
        // ImageIcon p1 = new ImageIcon("prova.jpg");
        salvaImmagineConcioIrrigidito(sez);
        Image img2;
        img2 = new Jpeg("sez.jpg");
        img2.setAlignment(Paragraph.ALIGN_CENTER);
        par = new Paragraph();
        par.add(img2);
        par.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(par);
        
        par = new Paragraph("Irrigidimenti longitudinali e trasversali", corpo);
        document.add(par);
        Table tb = sml.getTabellaIrrigidimenti(sez.getPassoIrrigidimentiTrasv());
        document.add(tb);
        
        
        if (sml.isPiattabandaSupIrrigidita()) {
            String s = "Nota: le piattabande superiori sono considerate irrigidite dalla presenza della"
                    + "soletta in cemento armato";
            par = new Paragraph(s, corpo);
            document.add(par);
        }
        
        // risultati fasi combinazioni
        ArrayList<CombinazioneCarichi> cmb = prg.getCombinazioni();
        int ncombo = cmb.size();
        for (int combo = 0; combo < ncombo; combo++) {
            SezioneMetallica sm = sez.getSezioniMetEfficaci().get(combo);
            
            par = new Paragraph("COMBINAZIONE " + cmb.get(combo).getName(), corpo);
            document.add(par);
            // immagine tensioni fase i
            salvaImmagineSezioneEffica(sez, combo);
            Image img3;
            img3 = new Jpeg("sez.jpg");
            img3.setAlignment(Paragraph.ALIGN_CENTER);
            par = new Paragraph();
            par.add(img3);
            par.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(par);
            // tabella caratteristiche tensioni
            par = new Paragraph("Dettaglio sollecitazioni", corpo);
            document.add(par);
            Table tabSol = getOutputStabilita(sm,prg.getMateriale(), combo,sez.getPassoIrrigidimentiTrasv());
            document.add(tabSol);
            
            // Irrigidimenti
            par = new Paragraph("Verifica irrigidimenti", corpo);
            document.add(par);
            Table tabVI= sm.getTabellaVerificaIrrigidimenti(sez.getPassoIrrigidimentiTrasv()
            , prg.getMateriale(), sez.getIrrigidimetoTrasversale());
            document.add(tabVI);
            
            
        }
        
        return document;
    }
    
    private static Document scriviSezioneVerificaTensioni(Document document,
            Sezione sez) throws Exception {
        
        Progetto prg = Progetto.getInstance();
        SezioneMetallica sm = sez.getSezioneMetallica();
        Soletta sl = sez.getSoletta();
        
        Paragraph par = new Paragraph("SEZIONE: " + sez.getName(), title3);
        document.add(par);
        
        // ImageIcon p1 = new ImageIcon("prova.jpg");
        salvaImmagine(sez);
        Image img2;
        img2 = new Jpeg("sez.jpg");
        img2.setAlignment(Paragraph.ALIGN_CENTER);
        par = new Paragraph();
        par.add(img2);
        par.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(par);
        
        // concio metallico
        par = new Paragraph("Caratteristiche concio metallico:", corpo);
        document.add(par);
        Table tbsm = sm.getTabellaInput();
        document.add(tbsm);
        
        // soletta
        par = new Paragraph("Caratteristiche soletta in calcestruzzo:", corpo);
        document.add(par);
        Table tbsl = sl.getTabellaInput();
        document.add(tbsl);
        
        // armatura
        par = new Paragraph("Armatura soletta", corpo);
        document.add(par);
        Table tbAr = getTabellaArmatura(sez);
        document.add(tbAr);
        
        // condizioni di carico
        par = new Paragraph("Sollecitazioni", corpo);
        document.add(par);
        Table tab = getTabellaSollecitazioni(sez);
        document.add(tab);
        
        // combinazioni
        par = new Paragraph("Combinazioni di carico", corpo);
        document.add(par);
        Table tabCmb = getCombinazioni();
        document.add(tabCmb);
        
        // risultati fasi combinazioni
        ArrayList<CombinazioneCarichi> cmb = prg.getCombinazioni();
        int ncombo = cmb.size();
        for (int combo = 0; combo < ncombo; combo++) {
            for (int fase = 0; fase < 6; fase++) {
                if (fase != 5)
                    par = new Paragraph("COMBINAZIONE "
                            + cmb.get(combo).getName() + " - fase " + fase,
                            corpo);
                else
                    par = new Paragraph("COMBINAZIONE "
                            + cmb.get(combo).getName() + " TOTALE", corpo);
                document.add(par);
                // immagine tensioni fase i
                salvaImmagineFaseI(sez, fase, combo);
                Image img3;
                img3 = new Jpeg("sez.jpg");
                img3.setAlignment(Paragraph.ALIGN_CENTER);
                par = new Paragraph();
                par.add(img3);
                par.setAlignment(Paragraph.ALIGN_CENTER);
                document.add(par);
                // tabella caratteristiche tensioni
                par = new Paragraph("Dettaglio sollecitazioni", corpo);
                document.add(par);
                Table tabSol = getOutputFase(sez, fase, combo);
                document.add(tabSol);
            }
        }
        
        return document;
    }
    
    private static Table getOutputFase(Sezione sez, int fase, int combo)
    throws BadElementException {
        
        ArrayList<SezioneOutputTensioniFase[]> soS = sez.getSezioniOutput();
        SezioneOutputTensioniFase[] so = soS.get(combo);
        SezioneOutputTensioniFase s;
        if (fase < 5)
            s = so[fase];
        else
            s = sez.getTensioniTotali(combo);
        
        nf.setMaximumFractionDigits(0);
        
        Cell c;
        Table table = new Table(2, 16);
        table.setBorder(Table.NO_BORDER);
        table.setPadding(2);
        table.setSpacing(2);
        int nc = 0;
        
        // prima riga
        c = getCellaTipo("N assiale(kNm)", Cell.ALIGN_LEFT);
        table.addCell(c, nc, 0);
        c = getCellaTipo(nf.format(s.getNassiale() / 1000), Cell.ALIGN_RIGHT);
        table.addCell(c, nc, 1);
        ++nc;
        
        c = getCellaTipo("M flettente(kN)", Cell.ALIGN_LEFT);
        table.addCell(c, nc, 0);
        c = getCellaTipo(nf.format(s.getMflettente() / 1000000),
                Cell.ALIGN_RIGHT);
        table.addCell(c, nc, 1);
        ++nc;
        
        c = getCellaTipo("V taglio(kN)", Cell.ALIGN_LEFT);
        table.addCell(c, nc, 0);
        c = getCellaTipo(nf.format(s.getVtaglio() / 1000), Cell.ALIGN_RIGHT);
        table.addCell(c, nc, 1);
        ++nc;
        
        c = getCellaTipo("Tensione ritiro impedito soletta(MPa)",
                Cell.ALIGN_LEFT);
        table.addCell(c, nc, 0);
        c = getCellaTipo(nf.format(s.getScls_impedito()), Cell.ALIGN_RIGHT);
        table.addCell(c, nc, 1);
        ++nc;
        
        c = getCellaTipo("sc (MPa): tensione estradosso soletta",
                Cell.ALIGN_LEFT);
        table.addCell(c, nc, 0);
        c = getCellaTipo(nf.format(s.getSc()), Cell.ALIGN_RIGHT);
        table.addCell(c, nc, 1);
        ++nc;
        
        c = getCellaTipo("sc inf (MPa): tensione intradosso soletta",
                Cell.ALIGN_LEFT);
        table.addCell(c, nc, 0);
        c = getCellaTipo(nf.format(s.getScinf()), Cell.ALIGN_RIGHT);
        table.addCell(c, nc, 1);
        ++nc;
        
        c = getCellaTipo("sf (MPa): tensione armature", Cell.ALIGN_LEFT);
        table.addCell(c, nc, 0);
        c = getCellaTipo(nf.format(s.getSf()), Cell.ALIGN_RIGHT);
        table.addCell(c, nc, 1);
        ++nc;
        
        c = getCellaTipo("ss (MPa): tensione estradosso sezione metallica",
                Cell.ALIGN_LEFT);
        table.addCell(c, nc, 0);
        c = getCellaTipo(nf.format(s.getSs()), Cell.ALIGN_RIGHT);
        table.addCell(c, nc, 1);
        ++nc;
        
        c = getCellaTipo("s (MPa): tensione estradosso anima metallica",
                Cell.ALIGN_LEFT);
        table.addCell(c, nc, 0);
        c = getCellaTipo(nf.format(s.getS()), Cell.ALIGN_RIGHT);
        table.addCell(c, nc, 1);
        ++nc;
        
        c = getCellaTipo("i (MPa: tensione intradosso anima metallica)",
                Cell.ALIGN_LEFT);
        table.addCell(c, nc, 0);
        c = getCellaTipo(nf.format(s.getI()), Cell.ALIGN_RIGHT);
        table.addCell(c, nc, 1);
        ++nc;
        
        c = getCellaTipo("ii(MPa: tensione intradosso sezione metallica)",
                Cell.ALIGN_LEFT);
        table.addCell(c, nc, 0);
        c = getCellaTipo(nf.format(s.getIi()), Cell.ALIGN_RIGHT);
        table.addCell(c, nc, 1);
        ++nc;
        
        c = getCellaTipo("ts(MPa: taglio estradosso anima metallica)",
                Cell.ALIGN_LEFT);
        table.addCell(c, nc, 0);
        c = getCellaTipo(nf.format(s.getTs()), Cell.ALIGN_RIGHT);
        table.addCell(c, nc, 1);
        ++nc;
        
        c = getCellaTipo("ti(MPa: taglio estradosso anima metallica)",
                Cell.ALIGN_LEFT);
        table.addCell(c, nc, 0);
        c = getCellaTipo(nf.format(s.getTi()), Cell.ALIGN_RIGHT);
        table.addCell(c, nc, 1);
        ++nc;
        
        c = getCellaTipo("sid1(MPa): tensioni ideali estradosso anima",
                Cell.ALIGN_LEFT);
        table.addCell(c, nc, 0);
        c = getCellaTipo(nf.format(s.getSid1()), Cell.ALIGN_RIGHT);
        table.addCell(c, nc, 1);
        ++nc;
        
        c = getCellaTipo("sid2(MPa): tensioni ideali intradosso anima",
                Cell.ALIGN_LEFT);
        table.addCell(c, nc, 0);
        c = getCellaTipo(nf.format(s.getSid2()), Cell.ALIGN_RIGHT);
        table.addCell(c, nc, 1);
        ++nc;
        
        c = getCellaTipo(
                "V pioli(kN/ml): azione tagliante sistema di collegamento acciaio-soletta",
                Cell.ALIGN_LEFT);
        table.addCell(c, nc, 0);
        c = getCellaTipo(nf.format(s.getVPioli() / 1000), Cell.ALIGN_RIGHT);
        table.addCell(c, nc, 1);
        ++nc;
        
        return table;
    }
    
    private static Table getOutputStabilita(SezioneMetallica sm,MaterialeAcciaio mat,
            int combo, double passoIrrigidimentiTrasv)
            throws BadElementException {
        
        nf.setMaximumFractionDigits(0);
        ParametriStatici ps = sm.getParametriEfficaci(mat, passoIrrigidimentiTrasv);
        ParametriStatici psLord = sm.getParametriStatici();
        
        Cell c;
        int ncol = 17;
        if(sm.getClass()==SezioneMetallicaCassoneII.class) ncol+=6;
        Table table = new Table(2, ncol);
        table.setBorder(Table.NO_BORDER);
        table.setPadding(2);
        table.setSpacing(2);
        int nc = 0;
        
        // prima riga
        c = getCellaTipo("N assiale sezione metallica(kN)", Cell.ALIGN_LEFT);
        table.addCell(c, nc, 0);
        c = getCellaTipo(nf.format(sm.getNsez() / 1000), Cell.ALIGN_RIGHT);
        table.addCell(c, nc, 1);
        ++nc;
        
        c = getCellaTipo("M flettente sezione metallica (kNm)", Cell.ALIGN_LEFT);
        table.addCell(c, nc, 0);
        c = getCellaTipo(nf.format(sm.getMsez() / 1000000),
                Cell.ALIGN_RIGHT);
        table.addCell(c, nc, 1);
        ++nc;
        
        c = getCellaTipo("V taglio sezione metallica(kN)", Cell.ALIGN_LEFT);
        table.addCell(c, nc, 0);
        c = getCellaTipo(nf.format(sm.getVsez() / 1000), Cell.ALIGN_RIGHT);
        table.addCell(c, nc, 1);
        ++nc;
        
        c = getCellaTipo("Mt momento torcente (kNm)", Cell.ALIGN_LEFT);
        table.addCell(c, nc, 0);
        c = getCellaTipo(nf.format(sm.getVsez() / 1000), Cell.ALIGN_RIGHT);
        table.addCell(c, nc, 1);
        ++nc;
        
        c = getCellaTipo("Area efficace(mmq)", Cell.ALIGN_LEFT);
        table.addCell(c, nc, 0);
        c = getCellaTipo(nf.format(ps.getA()), Cell.ALIGN_RIGHT);
        table.addCell(c, nc, 1);
        ++nc;
        
        c = getCellaTipo("Jy,eff momento di inerzia efficace(mm4)", Cell.ALIGN_LEFT);
        table.addCell(c, nc, 0);
        c = getCellaTipo(nf.format(ps.getJy()), Cell.ALIGN_RIGHT);
        table.addCell(c, nc, 1);
        ++nc;
        
        c = getCellaTipo("yg,eff baricentro sezione efficace (mm)", Cell.ALIGN_LEFT);
        table.addCell(c, nc, 0);
        c = getCellaTipo(nf.format(ps.getJy()), Cell.ALIGN_RIGHT);
        table.addCell(c, nc, 1);
        ++nc;
        
        c = getCellaTipo("yg baricentro sezione lorda (mm)", Cell.ALIGN_LEFT);
        table.addCell(c, nc, 0);
        c = getCellaTipo(nf.format(psLord.getYg()), Cell.ALIGN_RIGHT);
        table.addCell(c, nc, 1);
        ++nc;
        
        double DM = sm.getNsez() * (+ps.getYg() - psLord.getYg());
        c = getCellaTipo("DM=NxDyg variazione momento efficace (kNm)", Cell.ALIGN_LEFT);
        table.addCell(c, nc, 0);
        c = getCellaTipo(nf.format(DM), Cell.ALIGN_RIGHT);
        table.addCell(c, nc, 1);
        ++nc;
        
        c = getCellaTipo("s,eff tensione efficace piattabanda superiore (MPa)", Cell.ALIGN_LEFT);
        table.addCell(c, nc, 0);
        c = getCellaTipo(nf.format(sm.getSEff()), Cell.ALIGN_RIGHT);
        table.addCell(c, nc, 1);
        ++nc;
        
        c = getCellaTipo("i,eff tensione efficace piattabanda inferiore (MPa)", Cell.ALIGN_LEFT);
        table.addCell(c, nc, 0);
        c = getCellaTipo(nf.format(sm.getIEff()), Cell.ALIGN_RIGHT);
        table.addCell(c, nc, 1);
        ++nc;
        
        if(sm.getClass()!= SezioneMetallicaCassoneII.class){
            c = getCellaTipo("Vbsd azione tagliante sull'anima (kN)", Cell.ALIGN_LEFT);
            table.addCell(c, nc, 0);
            c = getCellaTipo(nf.format(sm.getVbsd()), Cell.ALIGN_RIGHT);
            table.addCell(c, nc, 1);
            ++nc;
            
            c = getCellaTipo("VbwRd contributo anima alla resistenza a taglio (kN)", Cell.ALIGN_LEFT);
            table.addCell(c, nc, 0);
            c = getCellaTipo(nf.format(sm.getVbwRd()), Cell.ALIGN_RIGHT);
            table.addCell(c, nc, 1);
            ++nc;
            
            c = getCellaTipo("VbfRd contributo delle ali alla resistenza a taglio (kN)", Cell.ALIGN_LEFT);
            table.addCell(c, nc, 0);
            c = getCellaTipo(nf.format(sm.getVbfRd()), Cell.ALIGN_RIGHT);
            table.addCell(c, nc, 1);
            ++nc;
            
            c = getCellaTipo("VbRd resistenza a taglio totale (kN)", Cell.ALIGN_LEFT);
            table.addCell(c, nc, 0);
            c = getCellaTipo(nf.format(sm.getVbRd()), Cell.ALIGN_RIGHT);
            table.addCell(c, nc, 1);
            ++nc;
            
            nf.setMaximumFractionDigits(3);
            c = getCellaTipo("nu3 coefficiente di verifica a taglio: Vbsd/Vbrd", Cell.ALIGN_LEFT);
            table.addCell(c, nc, 0);
            c = getCellaTipo(nf.format(sm.getNu3()), Cell.ALIGN_RIGHT);
            table.addCell(c, nc, 1);
            ++nc;
            
            c = getCellaTipo("nu1 coefficiente di verifica azioni combinate N,M,V", Cell.ALIGN_LEFT);
            table.addCell(c, nc, 0);
            c = getCellaTipo(nf.format(sm.getNuInterM_V()), Cell.ALIGN_RIGHT);
            table.addCell(c, nc, 1);
            ++nc;
            nf.setMaximumFractionDigits(0);
        }else {
            SezioneMetallicaCassoneII smc = (SezioneMetallicaCassoneII)sm;
            c = getCellaTipo("Vbsd,lat azione tagliante sulle anime laterali (kN) - solo azione torcente Mt) ", Cell.ALIGN_LEFT);
            table.addCell(c, nc, 0);
            c = getCellaTipo(nf.format(smc.getVbsdLaterali()), Cell.ALIGN_RIGHT);
            table.addCell(c, nc, 1);
            ++nc;
            
            c = getCellaTipo("VbwRd,lat contributo anima resistenza a taglio (kN)", Cell.ALIGN_LEFT);
            table.addCell(c, nc, 0);
            c = getCellaTipo(nf.format(smc.getVbwRdlat()), Cell.ALIGN_RIGHT);
            table.addCell(c, nc, 1);
            ++nc;
            
            c = getCellaTipo("VbfRd,lat contributo ali resistenza a taglio (kN)", Cell.ALIGN_LEFT);
            table.addCell(c, nc, 0);
            c = getCellaTipo(nf.format(smc.getVbfRdLat()), Cell.ALIGN_RIGHT);
            table.addCell(c, nc, 1);
            ++nc;
            
            c = getCellaTipo("VbRd,lat resistenza a taglio totale anime laterali(kN)", Cell.ALIGN_LEFT);
            table.addCell(c, nc, 0);
            c = getCellaTipo(nf.format(smc.getVbRdLat()), Cell.ALIGN_RIGHT);
            table.addCell(c, nc, 1);
            ++nc;
            
            nf.setMaximumFractionDigits(3);
            c = getCellaTipo("nu3 coefficiente di verifica a taglio anime laterali: Vbsd,lat/Vbrd,lat", Cell.ALIGN_LEFT);
            table.addCell(c, nc, 0);
            c = getCellaTipo(nf.format(smc.getNu3Lat()), Cell.ALIGN_RIGHT);
            table.addCell(c, nc, 1);
            ++nc;
            
            c = getCellaTipo("nu1 coefficiente di verifica azioni combinate N,M,V anime laterali", Cell.ALIGN_LEFT);
            table.addCell(c, nc, 0);
            c = getCellaTipo(nf.format(smc.getNuInterM_VLat()), Cell.ALIGN_RIGHT);
            table.addCell(c, nc, 1);
            ++nc;
            nf.setMaximumFractionDigits(0);
            
            // anime centrali
            c = getCellaTipo("Vbsd,cen azione tagliante sulle anime centrali (kN) - solo azioni taglianti V) ", Cell.ALIGN_LEFT);
            table.addCell(c, nc, 0);
            c = getCellaTipo(nf.format(smc.getVbsdCentrale()), Cell.ALIGN_RIGHT);
            table.addCell(c, nc, 1);
            ++nc;
            
            c = getCellaTipo("VbwRd,cen contributo anima resistenza a taglio (kN)", Cell.ALIGN_LEFT);
            table.addCell(c, nc, 0);
            c = getCellaTipo(nf.format(smc.getVbwRdCen()), Cell.ALIGN_RIGHT);
            table.addCell(c, nc, 1);
            ++nc;
            
            c = getCellaTipo("VbfRd,cen contributo ali resistenza a taglio (kN)", Cell.ALIGN_LEFT);
            table.addCell(c, nc, 0);
            c = getCellaTipo(nf.format(smc.getVbfRdCen()), Cell.ALIGN_RIGHT);
            table.addCell(c, nc, 1);
            ++nc;
            
            c = getCellaTipo("VbRd,cen resistenza a taglio totale anime ecntrali(kN)", Cell.ALIGN_LEFT);
            table.addCell(c, nc, 0);
            c = getCellaTipo(nf.format(smc.getVbRdCen()), Cell.ALIGN_RIGHT);
            table.addCell(c, nc, 1);
            ++nc;
            
            nf.setMaximumFractionDigits(3);
            c = getCellaTipo("nu3 coefficiente di verifica a taglio anime centrali: Vbsd,cen/Vbrd,cen", Cell.ALIGN_LEFT);
            table.addCell(c, nc, 0);
            c = getCellaTipo(nf.format(smc.getNu3Cen()), Cell.ALIGN_RIGHT);
            table.addCell(c, nc, 1);
            ++nc;
            
            c = getCellaTipo("nu1 coefficiente di verifica azioni combinate N,M,V anime centrali", Cell.ALIGN_LEFT);
            table.addCell(c, nc, 0);
            c = getCellaTipo(nf.format(smc.getNuInterM_VLat()), Cell.ALIGN_RIGHT);
            table.addCell(c, nc, 1);
            ++nc;
            nf.setMaximumFractionDigits(0);
            
        }
        
        return table;
    }
    
    private static Table getCombinazioni() throws BadElementException {
        
        Progetto prg = Progetto.getInstance();
        ArrayList<CombinazioneCarichi> cmb = prg.getCombinazioni();
        ArrayList<CondizioniCarico> cond = prg.getCondizioni();
        
        nf.setMaximumFractionDigits(0);
        int nComb = cmb.size();
        int nCond = cond.size();
        
        Cell c;
        Table table = new Table(nComb + 1, nCond + 1);
        table.setBorder(Table.NO_BORDER);
        table.setPadding(2);
        table.setSpacing(2);
        int nc = 0;
        
        // prima riga
        c = getCellaTipo("Condizione di carico", Cell.ALIGN_CENTER);
        table.addCell(c, nc, 0);
        int nr = 1;
        for (int i = 0; i < nComb; i++) {
            CombinazioneCarichi cm = cmb.get(i);
            c = getCellaTipo(cm.getName(), Cell.ALIGN_CENTER);
            table.addCell(c, nc, nr);
            ++nr;
        }
        ++nc;
        
        // altre righe
        for (int i = 0; i < nCond; i++) {
            CondizioniCarico cd = cond.get(i);
            c = getCellaTipo(cd.getNome(), Cell.ALIGN_CENTER);
            table.addCell(c, nc, 0);
            nr = 1;
            for (int j = 0; j < nComb; j++) {
                CombinazioneCarichi cm = cmb.get(j);
                c = getCellaTipo(Double.toString(cm.getC1(i)),
                        Cell.ALIGN_CENTER);
                table.addCell(c, nc, nr);
                ++nr;
            }
            ++nc;
        }
        
        return table;
    }
    
    private static Table getTabellaArmatura(Sezione sez)
    throws BadElementException {
        
        Progetto prg = Progetto.getInstance();
        double beff = sez.getBeff(prg.getCampate());
        Table table = new Table(1, 1);
        
        Cell c;
        int nc = 0;
        
        if (sez.isCalcolaArmatura()) {
            table = new Table(2, 3);
            table.setBorder(Table.NO_BORDER);
            table.setPadding(2);
            table.setSpacing(2);
            
            c = getCellaTipo("Passo armature(mm)", Cell.ALIGN_LEFT);
            table.addCell(c, nc, 0);
            c = getCellaTipo(nf.format(sez.getPassoArmatura()),
                    Cell.ALIGN_RIGHT);
            table.addCell(c, nc, 1);
            ++nc;
            
            c = getCellaTipo("Diametro barre(mmq)", Cell.ALIGN_LEFT);
            table.addCell(c, nc, 0);
            c = getCellaTipo(nf.format(sez.getDiametroArmatura()),
                    Cell.ALIGN_RIGHT);
            table.addCell(c, nc, 1);
            ++nc;
            
            c = getCellaTipo("As(mmq)", Cell.ALIGN_LEFT);
            table.addCell(c, nc, 0);
            c = getCellaTipo(nf.format(sez.getAreaArmatura(beff)),
                    Cell.ALIGN_RIGHT);
            table.addCell(c, nc, 1);
            
        } else {
            table = new Table(2, 1);
            table.setBorder(Table.NO_BORDER);
            table.setPadding(2);
            table.setSpacing(2);
            
            c = getCellaTipo("As(mmq)", Cell.ALIGN_LEFT);
            table.addCell(c, nc, 0);
            c = getCellaTipo(nf.format(sez.getAreaArmatura(beff)),
                    Cell.ALIGN_RIGHT);
            table.addCell(c, nc, 1);
            
        }
        
        return table;
        
    }
    
    private static Table getTabellaMateriale(MaterialeAcciaio mat)
    throws BadElementException {
        
        Cell c;
        int nc = 0;
        
        Table table = new Table(2, 7);
        table.setBorder(Table.NO_BORDER);
        table.setPadding(2);
        table.setSpacing(2);
        
        c = getCellaTipo("Acciaio tipo", Cell.ALIGN_LEFT);
        table.addCell(c, nc, 0);
        c = getCellaTipo(mat.getNome(), Cell.ALIGN_RIGHT);
        table.addCell(c, nc, 1);
        ++nc;
        
        c = getCellaTipo("fy (MPa)t < " + mat.getSp1() + "", Cell.ALIGN_LEFT);
        table.addCell(c, nc, 0);
        c = getCellaTipo(Double.toString(mat.getFy1()), Cell.ALIGN_RIGHT);
        table.addCell(c, nc, 1);
        ++nc;
        
        c = getCellaTipo("fy (MPa) " + mat.getSp1() + "mm < t < "
                + mat.getSp2(), Cell.ALIGN_LEFT);
        table.addCell(c, nc, 0);
        c = getCellaTipo(Double.toString(mat.getFy2()), Cell.ALIGN_RIGHT);
        table.addCell(c, nc, 1);
        ++nc;
        
        c = getCellaTipo("E (MPa)", Cell.ALIGN_LEFT);
        table.addCell(c, nc, 0);
        c = getCellaTipo(Double.toString(mat.getE()), Cell.ALIGN_RIGHT);
        table.addCell(c, nc, 1);
        ++nc;
        
        c = getCellaTipo("G (MPa)", Cell.ALIGN_LEFT);
        table.addCell(c, nc, 0);
        c = getCellaTipo(Double.toString(mat.getG()), Cell.ALIGN_RIGHT);
        table.addCell(c, nc, 1);
        ++nc;
        
        c = getCellaTipo("v", Cell.ALIGN_LEFT);
        table.addCell(c, nc, 0);
        c = getCellaTipo(Double.toString(mat.getNi()), Cell.ALIGN_RIGHT);
        table.addCell(c, nc, 1);
        ++nc;
        
        c = getCellaTipo("coefficiente ni EC3-1-5", Cell.ALIGN_LEFT);
        table.addCell(c, nc, 0);
        c = getCellaTipo(Double.toString(mat.getNutaglio()), Cell.ALIGN_RIGHT);
        table.addCell(c, nc, 1);
        ++nc;
        
        return table;
        
    }
    
    private static Table getTabellaParametriAnalisiGlobale(Sezione sez)
    throws BadElementException {
        
        Progetto prg = Progetto.getInstance();
        SezioneOutputTensioniFase[] so = sez.getSezioniOutput().get(0);
        nf.setMaximumFractionDigits(0);
        
        double[] n = prg.getN();
        Cell c;
        Table table = new Table(6, 8);
        table.setBorder(Table.NO_BORDER);
        table.setPadding(2);
        table.setSpacing(2);
        int nc = 0;
        
        c = getCellaTipo(" ", Cell.ALIGN_CENTER);
        table.addCell(c, nc, 0);
        c = getCellaTipo("n = infinito", Cell.ALIGN_CENTER);
        table.addCell(c, nc, 1);
        c = getCellaTipo("n = " + Double.toString(n[0]), Cell.ALIGN_CENTER);
        table.addCell(c, nc, 2);
        c = getCellaTipo("n = " + Double.toString(n[1]), Cell.ALIGN_CENTER);
        table.addCell(c, nc, 3);
        c = getCellaTipo("n = " + Double.toString(n[2]), Cell.ALIGN_CENTER);
        table.addCell(c, nc, 4);
        c = getCellaTipo("n = " + Double.toString(n[3]), Cell.ALIGN_CENTER);
        table.addCell(c, nc, 5);
        ++nc;
        
        c = getCellaTipo("A (mmq)", Cell.ALIGN_CENTER);
        table.addCell(c, nc, 0);
        c = getCellaTipo(nf.format(so[0].getA()), Cell.ALIGN_CENTER);
        table.addCell(c, nc, 1);
        c = getCellaTipo(nf.format(so[1].getA()), Cell.ALIGN_CENTER);
        table.addCell(c, nc, 2);
        c = getCellaTipo(nf.format(so[2].getA()), Cell.ALIGN_CENTER);
        table.addCell(c, nc, 3);
        c = getCellaTipo(nf.format(so[3].getA()), Cell.ALIGN_CENTER);
        table.addCell(c, nc, 4);
        c = getCellaTipo(nf.format(so[4].getA()), Cell.ALIGN_CENTER);
        table.addCell(c, nc, 5);
        ++nc;
        
        c = getCellaTipo("Jy (mm4)", Cell.ALIGN_CENTER);
        table.addCell(c, nc, 0);
        c = getCellaTipo(nf.format(so[0].getJy()), Cell.ALIGN_CENTER);
        table.addCell(c, nc, 1);
        c = getCellaTipo(nf.format(so[1].getJy()), Cell.ALIGN_CENTER);
        table.addCell(c, nc, 2);
        c = getCellaTipo(nf.format(so[2].getJy()), Cell.ALIGN_CENTER);
        table.addCell(c, nc, 3);
        c = getCellaTipo(nf.format(so[3].getJy()), Cell.ALIGN_CENTER);
        table.addCell(c, nc, 4);
        c = getCellaTipo(nf.format(so[4].getJy()), Cell.ALIGN_CENTER);
        table.addCell(c, nc, 5);
        ++nc;
        
        c = getCellaTipo("yg (mm)", Cell.ALIGN_CENTER);
        table.addCell(c, nc, 0);
        c = getCellaTipo(nf.format(so[0].getYg()), Cell.ALIGN_CENTER);
        table.addCell(c, nc, 1);
        c = getCellaTipo(nf.format(so[1].getYg()), Cell.ALIGN_CENTER);
        table.addCell(c, nc, 2);
        c = getCellaTipo(nf.format(so[2].getYg()), Cell.ALIGN_CENTER);
        table.addCell(c, nc, 3);
        c = getCellaTipo(nf.format(so[3].getYg()), Cell.ALIGN_CENTER);
        table.addCell(c, nc, 4);
        c = getCellaTipo(nf.format(so[4].getYg()), Cell.ALIGN_CENTER);
        table.addCell(c, nc, 5);
        ++nc;
        
        c = getCellaTipo("A(-) (mmq)", Cell.ALIGN_CENTER);
        table.addCell(c, nc, 0);
        c = getCellaTipo(nf.format(so[0].getAn()), Cell.ALIGN_CENTER);
        table.addCell(c, nc, 1);
        c = getCellaTipo(nf.format(so[1].getAn()), Cell.ALIGN_CENTER);
        table.addCell(c, nc, 2);
        c = getCellaTipo(nf.format(so[2].getAn()), Cell.ALIGN_CENTER);
        table.addCell(c, nc, 3);
        c = getCellaTipo(nf.format(so[3].getAn()), Cell.ALIGN_CENTER);
        table.addCell(c, nc, 4);
        c = getCellaTipo(nf.format(so[4].getAn()), Cell.ALIGN_CENTER);
        table.addCell(c, nc, 5);
        ++nc;
        
        c = getCellaTipo("Jy(-) (mmq)", Cell.ALIGN_CENTER);
        table.addCell(c, nc, 0);
        c = getCellaTipo(nf.format(so[0].getJyn()), Cell.ALIGN_CENTER);
        table.addCell(c, nc, 1);
        c = getCellaTipo(nf.format(so[1].getJyn()), Cell.ALIGN_CENTER);
        table.addCell(c, nc, 2);
        c = getCellaTipo(nf.format(so[2].getJyn()), Cell.ALIGN_CENTER);
        table.addCell(c, nc, 3);
        c = getCellaTipo(nf.format(so[3].getJyn()), Cell.ALIGN_CENTER);
        table.addCell(c, nc, 4);
        c = getCellaTipo(nf.format(so[4].getJyn()), Cell.ALIGN_CENTER);
        table.addCell(c, nc, 5);
        ++nc;
        
        c = getCellaTipo("yg(-) (mmq)", Cell.ALIGN_CENTER);
        table.addCell(c, nc, 0);
        c = getCellaTipo(nf.format(so[0].getYgn()), Cell.ALIGN_CENTER);
        table.addCell(c, nc, 1);
        c = getCellaTipo(nf.format(so[1].getYgn()), Cell.ALIGN_CENTER);
        table.addCell(c, nc, 2);
        c = getCellaTipo(nf.format(so[2].getYgn()), Cell.ALIGN_CENTER);
        table.addCell(c, nc, 3);
        c = getCellaTipo(nf.format(so[3].getYgn()), Cell.ALIGN_CENTER);
        table.addCell(c, nc, 4);
        c = getCellaTipo(nf.format(so[4].getYgn()), Cell.ALIGN_CENTER);
        table.addCell(c, nc, 5);
        ++nc;
        
        c = getCellaTipo("Jw(-) (mmq)", Cell.ALIGN_CENTER);
        table.addCell(c, nc, 0);
        c = getCellaTipo(nf.format(so[0].getJw()), Cell.ALIGN_CENTER);
        table.addCell(c, nc, 1);
        c = getCellaTipo(nf.format(so[1].getJw()), Cell.ALIGN_CENTER);
        table.addCell(c, nc, 2);
        c = getCellaTipo(nf.format(so[2].getJw()), Cell.ALIGN_CENTER);
        table.addCell(c, nc, 3);
        c = getCellaTipo(nf.format(so[3].getJw()), Cell.ALIGN_CENTER);
        table.addCell(c, nc, 4);
        c = getCellaTipo(nf.format(so[4].getJw()), Cell.ALIGN_CENTER);
        table.addCell(c, nc, 5);
        ++nc;
        
        return table;
    }
    
    private static Table getTabellaSollecitazioni(Sezione sez)
    throws BadElementException {
        
        // Progetto prg = Progetto.getInstance();
        // SezioneOutputTensioniFase[] so = sez.getSezioniOutput().get(0);
        nf.setMaximumFractionDigits(0);
        ArrayList<Sollecitazioni> sol = sez.getSollecitazioni();
        int ncond = sol.size();
        
        Table table = new Table(7, ncond + 1);
        table.setBorder(Table.NO_BORDER);
        table.setPadding(2);
        table.setSpacing(2);
        int nc = 0;
        Cell c;
        
        c = getCellaTipo("Condizione", Cell.ALIGN_CENTER);
        table.addCell(c, nc, 0);
        c = getCellaTipo("Fase", Cell.ALIGN_CENTER);
        table.addCell(c, nc, 1);
        c = getCellaTipo("N assiale (kN)", Cell.ALIGN_CENTER);
        table.addCell(c, nc, 2);
        c = getCellaTipo("M flettente (kNm)", Cell.ALIGN_CENTER);
        table.addCell(c, nc, 3);
        c = getCellaTipo("V azione tagliante (kN)", Cell.ALIGN_CENTER);
        table.addCell(c, nc, 4);
        c = getCellaTipo("Tensione ritiro impedito (Mpa)", Cell.ALIGN_CENTER);
        table.addCell(c, nc, 5);
        c = getCellaTipo("M torcente (kNm)", Cell.ALIGN_CENTER);
        table.addCell(c, nc, 6);
        ++nc;
        
        for (int i = 0; i < ncond; ++i) {
            Sollecitazioni s = sol.get(i);
            
            c = getCellaTipo(s.getCond().getNome(), Cell.ALIGN_CENTER);
            table.addCell(c, nc, 0);
            c = getCellaTipo("fase " + s.getCond().getN(), Cell.ALIGN_CENTER);
            table.addCell(c, nc, 1);
            c = getCellaTipo(nf.format(s.getN()), Cell.ALIGN_CENTER);
            table.addCell(c, nc, 2);
            c = getCellaTipo(nf.format(s.getM()), Cell.ALIGN_CENTER);
            table.addCell(c, nc, 3);
            c = getCellaTipo(nf.format(s.getV()), Cell.ALIGN_CENTER);
            table.addCell(c, nc, 4);
            c = getCellaTipo(nf.format(s.getSigCls()), Cell.ALIGN_CENTER);
            table.addCell(c, nc, 5);
            c = getCellaTipo(nf.format(s.getMt()), Cell.ALIGN_CENTER);
            table.addCell(c, nc, 6);
            ++nc;
        }
        
        return table;
    }
    
    public static void salvaImmagine(Sezione sez) {
        
        Progetto prg = Progetto.getInstance();
        BufferedImage img = new BufferedImage(700, 350,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D gImg2d = (Graphics2D) img.getGraphics();
        gImg2d.setColor(Color.white);
        gImg2d.fill(new Rectangle2D.Double(0, 0, 700, 350));
        gImg2d = SezioneTipoView.setFattoreDiScala(gImg2d, img.getWidth(), img
                .getHeight(), sez);
        int ddQ = SezioneTipoView.getDdQuote(gImg2d, img.getWidth(), img
                .getHeight(), sez);
        
        SezioneMetallica sm = sez.getSezioneMetallica();
        Soletta sl = sez.getSoletta();
        Shape shSm = sm.getShape();
        Shape shSl = sl.getShape(sez.isCalcoloAutomaticoBeff(), prg
                .getCampate(), sez.getNCampata());
        
        gImg2d.setColor(Color.black);
        gImg2d.fill(shSm);
        gImg2d.setColor(Color.lightGray);
        gImg2d.fill(shSl);
        
        gImg2d.setColor(Color.black);
        sm.quota(gImg2d, ddQ);
        sl.quota(gImg2d, ddQ, sez.isCalcoloAutomaticoBeff(), prg.getCampate(),
                sez.getNCampata());
        
        try {
            OutputStream out = new FileOutputStream("sez.jpg");
            com.sun.image.codec.jpeg.JPEGImageEncoder encoder = com.sun.image.codec.jpeg.JPEGCodec.createJPEGEncoder(out);
            encoder.encode(img);
            out.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public static void salvaImmagineConcioIrrigidito(Sezione sez) {
        
        Progetto prg = Progetto.getInstance();
        
        BufferedImage img = new BufferedImage(700, 350,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D gImg2d = (Graphics2D) img.getGraphics();
        gImg2d.setColor(Color.white);
        
        gImg2d.fill(new Rectangle2D.Double(0, 0, 700, 350));
        gImg2d = SezioneIrrigiditaView.setFattoreDiScala(gImg2d,
                img.getWidth(), img.getHeight(), sez);
        int ddQ = SezioneIrrigiditaView.getDdQuote(gImg2d, img.getWidth(), img
                .getHeight(), sez);
        
        SezioneMetallica sm = sez.getSezioneMetallica();
        Soletta sl = sez.getSoletta();
        Shape shSm = sm.getShapeIrrigidita();
        Shape shSl = sl.getShape(sez.isCalcoloAutomaticoBeff(), prg
                .getCampate(), sez.getNCampata());
        
        gImg2d.setColor(Color.black);
        gImg2d.fill(shSm);
        sm.quota(gImg2d, ddQ);
        gImg2d.setColor(Color.lightGray);
        gImg2d.draw(shSl);
        
        try {
            OutputStream out = new FileOutputStream("sez.jpg");
            com.sun.image.codec.jpeg.JPEGImageEncoder encoder = com.sun.image.codec.jpeg.JPEGCodec
                    .createJPEGEncoder(out);
            encoder.encode(img);
            out.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public static void salvaImmagineSezioneEffica(Sezione sez, int combo) {
        
        SezioneMetallica concio = sez.getSezioniMetEfficaci().get(combo);
        
        BufferedImage img = new BufferedImage(700, 350,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D gImg2d = (Graphics2D) img.getGraphics();
        gImg2d.setColor(Color.white);
        gImg2d.fill(new Rectangle2D.Double(0, 0, 700, 350));
        gImg2d = TensioniVerificaView.setFattoreDiScala(gImg2d, img.getWidth(),
                img.getHeight(), sez);
        
        Shape spConcio = concio.getShapeIrrigidita();
        Shape spConcioEff = concio.getShapeEff();
        
        gImg2d.setColor(Color.lightGray);
        gImg2d.draw(spConcio);
        gImg2d.setColor(Color.black);
        gImg2d.fill(spConcioEff);
        
        SezioneIrrigiditaView.disegnaTensioni(gImg2d, img.getWidth(), img
                .getHeight(), sez,combo);
        
        try {
            OutputStream out = new FileOutputStream("sez.jpg");
            com.sun.image.codec.jpeg.JPEGImageEncoder encoder = com.sun.image.codec.jpeg.JPEGCodec
                    .createJPEGEncoder(out);
            encoder.encode(img);
            out.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public static void salvaImmagineFaseI(Sezione sez, int fase, int combo) {
        
        Progetto prg = Progetto.getInstance();
        Soletta soletta = sez.getSoletta();
        SezioneMetallica concio = sez.getSezioneMetallica();
        ArrayList<SezioneOutputTensioniFase[]> soS = sez.getSezioniOutput();
        SezioneOutputTensioniFase[] so = soS.get(combo);
        boolean mpos = so[0].isMomPositivo();
        SezioneOutputTensioniFase s;
        if (fase < 5)
            s = so[fase];
        else
            s = sez.getTensioniTotali(combo);
        
        BufferedImage img = new BufferedImage(400, 150,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D gImg2d = (Graphics2D) img.getGraphics();
        gImg2d.setColor(Color.white);
        gImg2d.fill(new Rectangle2D.Double(0, 0, 400, 150));
        gImg2d = TensioniVerificaView.setFattoreDiScala(gImg2d, img.getWidth(),
                img.getHeight(), sez);
        // int ddQ = TensioniVerificaView.getDdQuote(gImg2d, img.getWidth(), img
        // .getHeight(), sez);
        Shape spSolettaEff;
        if (fase != 5)
            spSolettaEff = soletta.getShape(sez.isCalcoloAutomaticoBeff(), prg
                    .getCampate(), sez.getNCampata(), sez.getXSezione(), s
                    .getYn(sez), mpos);
        else
            spSolettaEff = soletta.getShape(sez.isCalcoloAutomaticoBeff(), prg
                    .getCampate(), sez.getNCampata(), sez.getXSezione(), 0,
                    mpos);
        
        Shape spSoletta = soletta.getShape(false, null, 0);
        Shape spConcio = concio.getShape();
        
        // sm.quota(gImg2d, ddQ);
        // sl.quota(gImg2d, ddQ, sez.isCalcoloAutomaticoBeff(),
        // prg.getCampate(),
        // sez.getNCampata());
        gImg2d.setColor(Color.lightGray);
        if (fase != 0)
            gImg2d.fill(spSolettaEff);
        gImg2d.draw(spSoletta);
        
        gImg2d.setColor(Color.black);
        gImg2d.fill(spConcio);
        
        TensioniVerificaView.disegnaTensioni(gImg2d, sez, s, img.getWidth(),
                img.getHeight());
        
        try {
            OutputStream out = new FileOutputStream("sez.jpg");
            com.sun.image.codec.jpeg.JPEGImageEncoder encoder = com.sun.image.codec.jpeg.JPEGCodec
                    .createJPEGEncoder(out);
            encoder.encode(img);
            out.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public static void salvaImmagineVerifica(Sezione sez, double yn,
            boolean mPos) {
        
        Progetto prg = Progetto.getInstance();
        BufferedImage img = new BufferedImage(700, 350,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D gImg2d = (Graphics2D) img.getGraphics();
        gImg2d.setColor(Color.white);
        gImg2d.fill(new Rectangle2D.Double(0, 0, 700, 350));
        gImg2d = SezioneTipoView.setFattoreDiScala(gImg2d, img.getWidth(), img
                .getHeight(), sez);
        int ddQ = SezioneTipoView.getDdQuote(gImg2d, img.getWidth(), img
                .getHeight(), sez);
        
        SezioneMetallica sm = sez.getSezioneMetallica();
        Soletta sl = sez.getSoletta();
        Shape shSm = sm.getShape();
        Shape shSl = sl.getShape(sez.isCalcoloAutomaticoBeff(), prg
                .getCampate(), sez.getNCampata(), sez.getXSezione(), yn, mPos);
        
        gImg2d.setColor(Color.black);
        gImg2d.fill(shSm);
        sm.quota(gImg2d, ddQ);
        sl.quota(gImg2d, ddQ, sez.isCalcoloAutomaticoBeff(), prg.getCampate(),
                sez.getNCampata());
        gImg2d.setColor(Color.lightGray);
        gImg2d.fill(shSl);
        
        try {
            OutputStream out = new FileOutputStream("sez.jpg");
            com.sun.image.codec.jpeg.JPEGImageEncoder encoder = com.sun.image.codec.jpeg.JPEGCodec
                    .createJPEGEncoder(out);
            encoder.encode(img);
            out.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public static Cell getCellaTipo(String st, int allineamento) {
        Cell c = new Cell(st);
        c.setBorder(Cell.NO_BORDER);
        c.setHorizontalAlignment(allineamento);
        
        return c;
    }
    
}
