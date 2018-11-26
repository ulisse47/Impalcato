package cassone.util;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

import javax.swing.JOptionPane;

import cassone.inputPanel.PannelloConcioCassoneII;
import cassone.inputPanel.PannelloConcioCassone;
import cassone.inputPanel.PannelloConcioDoppioT;
import cassone.inputPanel.PannelloConcioGenerico;
import cassone.inputPanel.PannelloIrrigidimLongCassoneII;
import cassone.inputPanel.PannelloIrrigidimentoLongDoppioT;
import cassone.inputPanel.PannelloIrrigidimLongCassone;
import cassone.inputPanel.PannelloSolettaT;
import cassone.inputPanel.PannelloSolettaType2;
import cassone.model.CombinazioneCarichi;
import cassone.model.CondizioniCarico;
import cassone.model.GiuntoBullonato;
import cassone.model.Progetto;
import cassone.model.Sezione;
import cassone.model.SezioneOutputTensioniFase;
import cassone.model.Sollecitazioni;
import cassone.model.SezioniMetalliche.SezioneMetallica;
import cassone.model.SezioniMetalliche.SezioneMetallicaCassone;
import cassone.model.SezioniMetalliche.SezioneMetallicaCassoneII;
import cassone.model.SezioniMetalliche.SezioneMetallicaDoppioT;
import cassone.model.SezioniMetalliche.SezioneMetallicaGenerica;
import cassone.model.irrigidimenti.Irrigidimento;
import cassone.model.irrigidimenti.Irrigidimento_L;
import cassone.model.irrigidimenti.Irrigidimento_T;
import cassone.model.soletta.Soletta;
import cassone.model.soletta.SolettaT;
import cassone.model.soletta.SolettaType2;
import cassone.model.soletta.SolettaType2_2;
import cassone.view.Giunti.InputGiuntiBullonatiGeneral;
import cassone.view.Giunti.InputGiuntoBullonatoAnima;
import cassone.view.Giunti.InputGiuntoBullonatoInf;
import cassone.view.Giunti.InputGiuntoBullonatoSup;
import cassone.view.Instabilita.InputInstabilita;
import cassone.view.Instabilita.TableTensioniEfficaci;
import cassone.view.VerificaTensioni.InputVerifiche;
import cassone.view.VerificaTensioni.TableCombView;
import cassone.view.VerificaTensioni.TableCondView;
import cassone.view.analisi.InputView;
import cassone.view.table.TableSezModel;

public class Parser {
    
    public static String DEL = ".";
    
    private InputView inputPanel;
    
    static NumberFormat ns;
    
    /**
     *
     * @return
     */
    public static Properties getInput() {
        Properties prop = new Properties();
        Progetto prg = Progetto.getInstance();
        
        double[] n = prg.getN();
        
        prop.put("nome", String.valueOf(prg.getNomeProgetto()));
        
        prop.put("n1", String.valueOf(n[1]));
        prop.put("n2", String.valueOf(n[2]));
        prop.put("n3", String.valueOf(n[3]));
        prop.put("n4", String.valueOf(n[4]));
        
        return prop;
        
    }
    
    /**
     *
     * @return
     */
    public static String[] getColumnNameComb() {
        Progetto prg = Progetto.getInstance();
        CombinazioneCarichi comb;
        int nc = prg.getCombinazioni().size();
        
        String[] colName = new String[nc + 1];
        
        colName[0] = "Condizione";
        
        for (int i = 0; i < nc; i++) {
            comb = prg.getCombinazioni().get(i);
            colName[i + 1] = comb.getName();
        }
        
        return colName;
    }
    
    /**
     *
     * @return
     */
    public static String[] getColumnNameSollecitazioni() {
        String[] colName = new String[7];
        
        colName[0] = "Cond.";
        colName[1] = "n";
        colName[2] = "N (kN)";
        colName[3] = "V (kN)";
        colName[4] = "M (kNm)";
        colName[5] = "σcls(kN)";
        colName[6] = "Mtor(kNm)";
        
        return colName;
    }
    
    /**
     *
     * @return
     */
    public static String[] getColumnNameParametriStatici() {
        String[] colName = new String[6];
        Progetto prg = Progetto.getInstance();
        
        colName[0] = "";
        colName[1] = "n0 = inf.";
        colName[2] = "n1 = " + prg.getN()[0];
        colName[3] = "n2 = " + prg.getN()[1];
        colName[4] = "n3 = " + prg.getN()[2];
        colName[5] = "n4 = " + prg.getN()[3];
        
        return colName;
    }
    
    /**
     *
     * @return
     */
    public static Object[][] getRowParametriStatici() {
        
        int num = 6;
        Object[][] rowData = new Object[8][num];
        
        // setta i nomi delle righe
        setRowNameParametriStatici(rowData);
        for (int i = 1; i < num; i++) {
            setRowParametriStatici(i, rowData);
        }
        return rowData;
    }
    
    /**
     *
     * @return
     */
    public static Object[][] getRowCondizioniCarico() {
        
        Progetto prg = Progetto.getInstance();
        int ncond = prg.getCurrentSezioneVerifica().getSollecitazioni().size();
        
        int num = 7; // 9
        Object[][] rowData = new Object[ncond][num];
        
        // setta i nomi delle righe
        setRowNameCondizioniCarico(rowData);
        for (int i = 0; i < ncond; i++) {
            setRowCondizioniCarico(i, rowData);
        }
        return rowData;
    }
    
    /**
     *
     * @return
     */
    public static Object[][] getRowDataComb() {
        
        Progetto prg = Progetto.getInstance();
        int nCmb = prg.getCombinazioni().size();
        int ncond = prg.getCurrentSezioneVerifica().getSollecitazioni().size();
        
        // numero colonne, numero comb + colonna numero condizione + descrizione
        int num = nCmb + 1; // Cond. + Descr. + combinazioni
        
        Object[][] rowData = new Object[ncond][num];
        
        // setta i nomi delle righe, riga 0
        setRowNameCondizioniCarico(rowData);
        
        // setta le combinazioni
        for (int i = 0; i < ncond; i++) {
            setRowDataComb(i, rowData);
        }
        return rowData;
    }
    
    public static Object[][] getRowDataBulloniInf() {
        
        Progetto prg = Progetto.getInstance();
        Sezione sez = prg.getCurrentSezioneVerifica();
        int nc = sez.getSezioniMetGiunto().size();
        
        Object[][] data = new Object[nc][7];
        
        for (int i = 0; i < nc; ++i) {
            CombinazioneCarichi cmb = prg.getCombinazioni().get(i);
            SezioneMetallica sm = sez.getSezioniMetGiunto().get(i);
            
            data[i][0] = new String(cmb.getName());
            data[i][1] = new Double(sm.getFSd(2) / 1000);
            data[i][2] = new Double(sm.getFbRdCoprig(2) / 1000);
            data[i][3] = new Double(sm.getFbRdPiatp(2) / 1000);
            data[i][4] = new Double(sm.getFvRd(2) / 1000);
            data[i][5] = new Double(sm.getFscrSLU(2) / 1000);
            data[i][6] = new Double(sm.getFscrSLE(2) / 1000);
         }
        return data;

    }
    
    public static Object[][] getRowDataBulloniW() {
        
         Progetto prg = Progetto.getInstance();
        Sezione sez = prg.getCurrentSezioneVerifica();
        int nc = sez.getSezioniMetGiunto().size();
        
        Object[][] data = new Object[nc][7];
        
        for (int i = 0; i < nc; ++i) {
            CombinazioneCarichi cmb = prg.getCombinazioni().get(i);
            SezioneMetallica sm = sez.getSezioniMetGiunto().get(i);
            
            data[i][0] = new String(cmb.getName());
            data[i][1] = new Double(sm.getFSd(1) / 1000);
            data[i][2] = new Double(sm.getFbRdCoprig(1) / 1000);
            data[i][3] = new Double(sm.getFbRdPiatp(1) / 1000);
            data[i][4] = new Double(sm.getFvRd(1) / 1000);
            data[i][5] = new Double(sm.getFscrSLU(1) / 1000);
            data[i][6] = new Double(sm.getFscrSLE(1) / 1000);
         }
        return data;

    }
    
    public static Object[][] getRowDataBulloniSup() {
        
        Progetto prg = Progetto.getInstance();
        Sezione sez = prg.getCurrentSezioneVerifica();
        int nc = sez.getSezioniMetGiunto().size();
        
        Object[][] data = new Object[nc][7];
        
        for (int i = 0; i < nc; ++i) {
            CombinazioneCarichi cmb = prg.getCombinazioni().get(i);
            SezioneMetallica sm = sez.getSezioniMetGiunto().get(i);
            
            data[i][0] = new String(cmb.getName());
            data[i][1] = new Double(sm.getFSd(0) / 1000);
            data[i][2] = new Double(sm.getFbRdCoprig(0) / 1000);
            data[i][3] = new Double(sm.getFbRdPiatp(0) / 1000);
            data[i][4] = new Double(sm.getFvRd(0) / 1000);
            data[i][5] = new Double(sm.getFscrSLU(0) / 1000);
            data[i][6] = new Double(sm.getFscrSLE(0) / 1000);
         }
        return data;
    }
    
    /**
     *
     * @param col
     * @param rowData
     * @return
     */
    private static Object[] setRowDataComb(int row, Object[][] rowData) {
        // int current_sezione = Common.current_sezione;
        Progetto prg = Progetto.getInstance();
        ArrayList<CombinazioneCarichi> cmb = prg.getCombinazioni();
        
        for (int i = 0; i < cmb.size(); i++) {
            CombinazioneCarichi c = cmb.get(i);
            rowData[row][i + 1] = new Double(c.getC1(row)); // I
        }
        return rowData;
    }
    
    /**
     *
     * @param col
     * @param rowData
     * @return
     */
    // setta la tabella delle sollecitazioni delle condizioni di carico di una
    // sezione
    private static Object[] setRowCondizioniCarico(int row, Object[][] rowData) {
        
        Progetto prg = Progetto.getInstance();
        Sollecitazioni sol = prg.getCurrentSezioneVerifica()
        .getSollecitazioni().get(row);
        
        // tipi sol.
        rowData[row][0] = new String(sol.getCond().getNome()); // n
        rowData[row][1] = new Integer(sol.getCond().getN()); // n
        rowData[row][2] = new Double(sol.getN()); // N
        rowData[row][3] = new Double(sol.getV()); // V
        rowData[row][4] = new Double(sol.getM()); // M
        rowData[row][5] = new Double(sol.getSigCls()); // Nsoletta
        rowData[row][6] = new Double(sol.getMt()); // M torcente
        
        return rowData;
    }
    
    /**
     *
     * @param rowData
     */
    private static void setRowNameCondizioniCarico(Object[][] rowData) {
        Progetto prg = Progetto.getInstance();
        int ns = prg.getCurrentSezioneVerifica().getSollecitazioni().size();
        
        // colonna i-esima
        for (int i = 0; i < ns; i++) {
            CondizioniCarico c = prg.getCurrentSezioneVerifica()
            .getSollecitazioni().get(i).getCond();
            rowData[i][0] = c.getNome();
        }
    }
    
    /**
     *
     * @param rowData
     */
    // setta l'intestazione della tabella di autput sezioni di analisi
    private static void setRowNameParametriStatici(Object[][] rowData) {
        
        // colonna 0-esima
        rowData[0][0] = "A (mm²)";
        rowData[1][0] = "yg (mm)";
        rowData[2][0] = "J (mm4)";
        rowData[3][0] = "Jw (mm4)";
        
        rowData[4][0] = "A(-) (mm²)";
        rowData[5][0] = "yg(-) (mm)";
        rowData[6][0] = "J(-) (mm4)";
        rowData[7][0] = "Jw(-) (mm)";
        
    }
    
    // setta i valori di output delle sezioni di analisi
    private static Object[] setRowParametriStatici(int col, Object[][] rowData) {
        Progetto prg = Progetto.getInstance();
        Sezione sez = prg.getCurrentSezioneAnalisi();
        
        SezioneOutputTensioniFase ps = new SezioneOutputTensioniFase();
        if (sez.getSezioniOutput().size() != 0) {
            ps = (sez.getSezioniOutput()).get(0)[col - 1];
        }
        // ParametriStaticiSezione ps= sez.getParametriOut().get(col-1);
        rowData[0][col] = new Double(ps.getA());
        rowData[1][col] = new Double(ps.getYg());
        rowData[2][col] = new Double(ps.getJy());
        rowData[3][col] = new Double(ps.getJw());
        rowData[4][col] = new Double(ps.getAn());
        rowData[5][col] = new Double(ps.getYgn());
        rowData[6][col] = new Double(ps.getJyn());
        rowData[7][col] = new Double(ps.getJwn());
        
        return rowData;
    }
    
    /**
     * Legge i dati da Common per visualizzarli nella inputView
     */
    
    public static void updateInputView() {
        updateInputAnalisi();
        updateInputVerificaSezioni();
        updateInputVerificaStabilita();
        updateInputGiunto();
    }
    
    public static void updateInputVerificaStabilita() {
        ns = NumberFormat.getInstance();
        ns.setMaximumFractionDigits(1);
        
        InputInstabilita inputPanel = InputInstabilita.getInstance();
        Progetto prg = Progetto.getInstance();
        
        Sezione sez = prg.getCurrentSezioneVerifica();
        SezioneMetallica sm = sez.getSezioneMetallica();
        
        inputPanel.getCbPiattabSupIrrigidita().setSelected(sm.isPiattabandaSupIrrigidita());
        
        inputPanel.resetComboSez(sez, prg.getSezioniVerifica());
        inputPanel.resetComboMat(prg.getMateriale(), prg.getMateriali());
        
        // irrigidimenti trasve
        Irrigidimento_T it = (Irrigidimento_T) sez.getIrrigidimetoTrasversale();
        inputPanel.setValue("passsoTrasv", String.valueOf(it.getPasso()));
        inputPanel.setValue("b1t", String.valueOf(it.getBsl()));
        inputPanel.setValue("h1t", String.valueOf(it.getTsl1()));
        inputPanel.setValue("b2t", String.valueOf(it.getHsl()));
        inputPanel.setValue("h2t", String.valueOf(it.getTsl2()));
        inputPanel.getCkDoppioIrTrasv().setSelected(it.isDoppio());
        inputPanel.getCkRigidEndPost().setSelected(it.isRigidEndPost());
        
        //sezione metallica a doppio T
        if (sm.getClass() == SezioneMetallicaDoppioT.class) {
            SezioneMetallicaDoppioT sdt = (SezioneMetallicaDoppioT) sm;
            ArrayList<Irrigidimento> ir = sdt.getIrsAnima();
            PannelloIrrigidimentoLongDoppioT pn = (PannelloIrrigidimentoLongDoppioT)inputPanel.getPIrrLong();
            
            int nIrrigi = ir.size();
            pn.setSelectedIndexComboIrrigidimenti(nIrrigi);
            
            if (nIrrigi > 0) {
                Irrigidimento_L i1 = (Irrigidimento_L) ir.get(0);
                pn.setValue("b1", String.valueOf(i1.getBsl()));
                pn.setValue("h1", String.valueOf(i1.getTsl1()));
                pn.setValue("y1", String.valueOf(i1.getY()));
                pn.setValue("b2", String.valueOf(i1.getHsl()));
                pn.setValue("h2", String.valueOf(i1.getTsl2()));
                pn.setValue("y1", String.valueOf(i1.getY()));
            }
            if (nIrrigi > 1) {
                Irrigidimento_L i1 = (Irrigidimento_L) ir.get(1);
                pn.setValue("b1", String.valueOf(i1.getBsl()));
                pn.setValue("h1", String.valueOf(i1.getTsl1()));
                pn.setValue("b2", String.valueOf(i1.getHsl()));
                pn.setValue("h2", String.valueOf(i1.getTsl2()));
                pn.setValue("y2", String.valueOf(i1.getY()));
            }
        }
        //sezione metallica a doppio T
        else if (sm.getClass() == SezioneMetallicaCassone.class) {
            SezioneMetallicaCassone sdt = (SezioneMetallicaCassone) sm;
            ArrayList<Irrigidimento> ir = sdt.getIrsAnime();
            ArrayList<Irrigidimento> ire = sdt.getIrsPiattaInf();
            PannelloIrrigidimLongCassone pn = (PannelloIrrigidimLongCassone)inputPanel.getPIrrLong();
           
            int nIrrigi = ir.size();
            int nIrrigiInf = ire.size();

            if (nIrrigi +nIrrigiInf > 0) {
                
               Irrigidimento_L i1 = new Irrigidimento_L();

               if (ir.size()!=0){
                        i1 = (Irrigidimento_L) ir.get(0);}
               else if (ire.size()!=0){
                    i1 = (Irrigidimento_L) ire.get(0);
                }
               
               pn.setValue("b1", String.valueOf(i1.getBsl()));
                pn.setValue("h1", String.valueOf(i1.getTsl1()));
                pn.setValue("b2", String.valueOf(i1.getHsl()));
                pn.setValue("h2", String.valueOf(i1.getTsl2()));
            }
            
            
            if (nIrrigi > 0) {
                Irrigidimento_L i1 = (Irrigidimento_L) ir.get(0);
                 pn.setValue("y1", String.valueOf(i1.getY()));
            }
            if (nIrrigi > 1) {
                Irrigidimento_L i1 = (Irrigidimento_L) ir.get(1);
                pn.setValue("y2", String.valueOf(i1.getY()));
            }
            
            //irrigidimento piattabanda inferiore
            pn.setIndexCNumeroIrrigidimentiPiattaInf(ire.size());
            pn.setIndexcNumeroIrrigidimentiAnima(ir.size());
        }
        
        //sezione metallica a doppio T
        else if (sm.getClass() == SezioneMetallicaCassoneII.class) {
            SezioneMetallicaCassoneII sdt = (SezioneMetallicaCassoneII) sm;
            ArrayList<Irrigidimento> ir = sdt.getIrsAnimeLaterali();
            ArrayList<Irrigidimento> irC = sdt.getIrsAnimeCentrali();
            ArrayList<Irrigidimento> ire = sdt.getIrsPiattaInfLaterali();
            ArrayList<Irrigidimento> ireC = sdt.getIrsPiattaInfCentrali();
            
            PannelloIrrigidimLongCassoneII pn = (PannelloIrrigidimLongCassoneII)inputPanel.getPIrrLong();
            
            int nIrrigi = ir.size();
            int nIrrigiC = irC.size();
            int nIrrigiInf = ire.size();
            int nIrrigiCinf = ireC.size();

            if (nIrrigi+nIrrigiC+nIrrigiInf+nIrrigiCinf> 0) {
               Irrigidimento_L i1 = new Irrigidimento_L();

               if (ir.size()!=0){
                        i1 = (Irrigidimento_L) ir.get(0);}
                else if (irC.size()!=0){
                    i1 = (Irrigidimento_L) irC.get(0);
                }
                else if (ire.size()!=0){
                    i1 = (Irrigidimento_L) ire.get(0);
                }
                else if (ireC.size()!=0) {i1 = (Irrigidimento_L) ireC.get(0);
                }

                pn.setValue("b1", String.valueOf(i1.getBsl()));
                pn.setValue("h1", String.valueOf(i1.getTsl1()));
                pn.setValue("b2", String.valueOf(i1.getHsl()));
                pn.setValue("h2", String.valueOf(i1.getTsl2()));
            }
            
            if (nIrrigi > 0) {
                Irrigidimento_L i1 = (Irrigidimento_L) ir.get(0);
                pn.setValue("y1", String.valueOf(i1.getY()));
            }
            if (nIrrigi > 1) {
                Irrigidimento_L i1 = (Irrigidimento_L) ir.get(1);
                pn.setValue("y2", String.valueOf(i1.getY()));
            }
            if (nIrrigiC > 0) {
                Irrigidimento_L i1 = (Irrigidimento_L) irC.get(0);
                pn.setValue("y1c", String.valueOf(i1.getY()));
            }
            if (nIrrigiC > 1) {
                Irrigidimento_L i1 = (Irrigidimento_L) irC.get(1);
                pn.setValue("y2c", String.valueOf(i1.getY()));
            }
            //irrigidimento piattabanda inferiore
            pn.setIndexCNumeroIrrigidimentiPiattaInf(ire.size());
            pn.setIndexcNumeroIrrigidimentiAnima(ir.size());
            pn.setIndexCNumeroIrrigidimentiPiattaInfCen(ireC.size());
            pn.setIndexcNumeroIrrigidimentiAnimaCen(irC.size());
       }
        // sollecitazioni su trave
        int row = TableTensioniEfficaci.getInstance().getTable()
        .getSelectedRow();
        if (row == -1)
            row = 0;
        
        if(sez.getSezioniMetEfficaci().size() > row){
            SezioneMetallica sme =sez.getSezioniMetEfficaci().get(row);
            inputPanel.setValue("Mconcio", String.valueOf(ns.format(sme.getMsez()/1000000)));
            inputPanel.setValue("Nconcio", String.valueOf(ns.format(sme.getNsez()/1000)));
            inputPanel.setValue("Vconcio", String.valueOf(ns.format(sme.getVsez()/1000)));
        }
        
        
    }
    
    private static void updateInputGiunto() {
        
        InputGiuntoBullonatoSup ipSup = InputGiuntoBullonatoSup.getInstance();
        InputGiuntoBullonatoInf ipInf = InputGiuntoBullonatoInf.getInstance();
        InputGiuntoBullonatoAnima ipAnima = InputGiuntoBullonatoAnima
                .getInstance();
        InputGiuntiBullonatiGeneral ipGen = InputGiuntiBullonatiGeneral
                .getInstance();
        
        Progetto prg = Progetto.getInstance();
        
        Sezione sez = prg.getCurrentSezioneVerifica();

        
        ipGen.resetComboSez(sez, prg.getSezioniVerifica());
        GiuntoBullonato g = sez.getGiunto();
        if(sez.getSezioneMetallica().getClass()!= SezioneMetallicaDoppioT.class) g=null;
        
        ipSup.setTextBoxEnable(g != null);
        ipInf.setTextBoxEnable(g != null);
        ipAnima.setTextBoxEnable(g != null);
        ipGen.setTextBoxEnable(g != null);

        if (g == null)
            return;
        
        ipSup.setValue("e1aSup", String.valueOf(g.getE1aSup()));
        ipSup.setValue("e1bSup", String.valueOf(g.getE1bSup()));
        ipSup.setValue("e2aSup", String.valueOf(g.getE2aSup()));
        ipSup.setValue("e2bSup", String.valueOf(g.getE2bSup()));
        ipSup.setValue("p1Sup", String.valueOf(g.getP1Sup()));
        ipSup.setValue("p2Sup", String.valueOf(g.getP2Sup()));
        ipSup.setValue("nFile1Sup", String.valueOf(g.getNfile1Sup()));
        ipSup.setValue("nFile2Sup", String.valueOf(g.getNfile2Sup()));
        ipSup.setDiametroBullone(g.getDiamBullSup());
        ipSup.setValue("diamForoSup", String.valueOf(g.getDiamForoSup()));
        ipSup.setValue("tgSup", String.valueOf(g.getTgSup()));
        
        ipInf.setValue("e1aInf", String.valueOf(g.getE1aInf()));
        ipInf.setValue("e1bInf", String.valueOf(g.getE1bInf()));
        ipInf.setValue("e2aInf", String.valueOf(g.getE2aInf()));
        ipInf.setValue("e2bInf", String.valueOf(g.getE2bInf()));
        ipInf.setValue("p1Inf", String.valueOf(g.getP1Inf()));
        ipInf.setValue("p2Inf", String.valueOf(g.getP2Inf()));
        ipInf.setValue("nFile1Inf", String.valueOf(g.getNfile1Inf()));
        ipInf.setValue("nFile2Inf", String.valueOf(g.getNfile2Inf()));
        ipInf.setDiametroBullone(g.getDiamBullInf());
        ipInf.setValue("diamForoInf", String.valueOf(g.getDiamForoInf()));
        ipInf.setValue("tgInf", String.valueOf(g.getTgInf()));
        
        ipAnima.setValue("e1aW", String.valueOf(g.getE1aW()));
        ipAnima.setValue("e1bW", String.valueOf(g.getE1bW()));
        ipAnima.setValue("e2W", String.valueOf(g.getE2W()));
        ipAnima.setValue("p1W", String.valueOf(g.getP1W()));
        ipAnima.setValue("p2W", String.valueOf(g.getP2W()));
        ipAnima.setValue("nFile1W", String.valueOf(g.getNfile1W()));
        ipAnima.setValue("nFile2W", String.valueOf(g.getNfile2W()));
        ipAnima.setDiametroBullone(g.getDiamBullW());
        ipAnima.setValue("diamForoW", String.valueOf(g.getDiamForoW()));
        ipAnima.setValue("tgW", String.valueOf(g.getTgW()));
        
        ipGen.setValue("ks", String.valueOf(g.getKs()));
        ipGen.setValue("nu", String.valueOf(g.getNu()));
        ipGen.setValue("fuBulloni", String.valueOf(g.getFubulloni()));
        ipGen.setValue("fuCoprig", String.valueOf(g.getFuPiastre()));
        
    }
    
    private static void updateInputAnalisi() {
        ns = NumberFormat.getInstance();
        ns.setMaximumFractionDigits(0);
        
        InputView inputPanel = InputView.getInstance();
        Progetto prg = Progetto.getInstance();
        
        Sezione sezAnalsi = prg.getCurrentSezioneAnalisi();
        SezioneMetallica concio = (SezioneMetallica) prg
                .getCurrentSezioneMetallica(true);
        double n[] = prg.getN();
        
        // DATI GENERALI SEZIONE
        inputPanel.setTitolo(prg.getNomeProgetto());
        inputPanel.resetComboConcio(concio, prg.getConci());
        inputPanel.resetComboCampate();
        inputPanel.resetComboSoletta(sezAnalsi.getSoletta(), prg.getSolette());
        inputPanel.resetComboSezioneAnalisi(prg.getCurrentSezioneAnalisi(), prg
                .getSezioniAnalisiGlobale());
        // COEFFI. OMOGENEIZZAZIONE
        inputPanel.setN1(String.valueOf(n[0]));
        inputPanel.setN2(String.valueOf(n[1]));
        inputPanel.setN3(String.valueOf(n[2]));
        inputPanel.setN4(String.valueOf(n[3]));
        
        // SOLETTA
        Soletta sl = sezAnalsi.getSoletta();
        updateSoletta(sl);
        
        // CONCIO METALLICO
        SezioneMetallica sm = prg.getCurrentSezioneMetallica(true);
        updateConcio(sm);
        
        // UTILITY
        boolean isCalcoloArmatura = sezAnalsi.isCalcolaArmatura();
        boolean isCalcoloBeff = sezAnalsi.isCalcoloAutomaticoBeff();
        inputPanel.getCkCalcoloArmautr().setSelected(isCalcoloArmatura);
        double Beff = sezAnalsi.getBeff(prg.getCampate());
        double af = sezAnalsi.getAreaArmatura(Beff);
        if (!isCalcoloArmatura) {
            inputPanel.setValue("Af", String.valueOf(af));
        } else {
            inputPanel.setValue("Af", ns.format(af));
            inputPanel.setValue("PassoArmatura", String.valueOf(sezAnalsi
                    .getPassoArmatura()));
            inputPanel.setValue("DiametroArmatura", String.valueOf(sezAnalsi
                    .getDiametroArmatura()));
        }
        inputPanel.getCkCalcoloBeff().setSelected(isCalcoloBeff);
        
 //       inputPanel.setEnableTextBoxCalcoloAutomatico(isCalcoloBeff);
//        inputPanel.getComboCampata().setEnabled(isCalcoloBeff);
        inputPanel.getCkCalcoloBeff().setSelected(isCalcoloBeff);
//        inputPanel.setEnableTextBoxCalcoloArmatura(isCalcoloArmatura);
        
    }
    
    /**
     *
     */
    private static void updateInputVerificaSezioni() {
        ns = NumberFormat.getInstance();
        ns.setMaximumFractionDigits(0);
        
        InputVerifiche inputPanel = InputVerifiche.getInstance();
        Progetto prg = Progetto.getInstance();
        
        Sezione sezVerifica = prg.getCurrentSezioneVerifica();
        SezioneMetallica sm = sezVerifica.getSezioneMetallica();
        Soletta sol = sezVerifica.getSoletta();
        
        inputPanel.resetComboSez(sezVerifica, prg.getSezioniVerifica());
        inputPanel.resetComboConcio(sm, prg.getConci());
        inputPanel.resetComboCampate();
        inputPanel.resetComboSoletta(sol, prg.getSolette());
       
        inputPanel.setValue("x", String.valueOf(sezVerifica.getXSezione()));
        double Beff = 0;
        Beff = sezVerifica.getBeff(prg.getCampate());
        inputPanel.setValue("l1", String.valueOf(Beff));
        
        boolean isCalcoloAutomatico = sezVerifica.isCalcoloAutomaticoBeff();
        inputPanel.setEnableTextBoxCalcoloAutomatico(isCalcoloAutomatico);
        inputPanel.getCkCalcoloAutomatico().setSelected(isCalcoloAutomatico);
        
        boolean isCalcoloAutomaticoArmatura = sezVerifica.isCalcolaArmatura();
        inputPanel.setEnableTextBoxCalcoloArmatura(isCalcoloAutomaticoArmatura);
        inputPanel.getCkCalcoloArmatura().setSelected(
                isCalcoloAutomaticoArmatura);
        
        double areaArm = sezVerifica.getAreaArmatura(Beff);
        if (!isCalcoloAutomaticoArmatura) {
            inputPanel.setValue("Af", String.valueOf(areaArm));
        } else {
            inputPanel.setValue("Af", ns.format(areaArm));
            inputPanel.setValue("PassoArmatura", String.valueOf(sezVerifica
                    .getPassoArmatura()));
            inputPanel.setValue("DiametroArmatura", String.valueOf(sezVerifica
                    .getDiametroArmatura()));
        }
        
        inputPanel.getCkCalcoloAutomatico().setSelected(isCalcoloAutomatico);
        
//        inputPanel.setEnableTextBoxCalcoloAutomatico(isCalcoloAutomatico);
        inputPanel.getComboCampata().setEnabled(isCalcoloAutomatico);
        inputPanel.getCkCalcoloAutomatico().setSelected(isCalcoloAutomatico);
        inputPanel.setEnableTextBoxCalcoloArmatura(isCalcoloAutomaticoArmatura);
 
    }
    
      
    /**
     *
     * @param prop
     */
    public static void loadValuesSez(Properties prop) {
        
        InputVerifiche inputPanel = InputVerifiche.getInstance();
        inputPanel.setValue("x", prop.getProperty("x"));
        inputPanel.setValue("l1", prop.getProperty("l1"));
        inputPanel.setValue("l2", prop.getProperty("l2"));
        
        // comboConcio
        int nConci = Integer.parseInt(prop.getProperty("nconci"));
        // concio selezionato per la prima sezione
        int concio = Integer.parseInt(prop.getProperty("sez.0.1"));
        
        // inputPanel.resetComboConcio(0, nConci);
        
    }
    
    /**
     * @param prop
     */
    public static void loadValues(Properties prop, int nConcio) {
        
        InputView inputPanel = InputView.getInstance();
        
        Iterator keys = inputPanel.getListaFieldNames();
        String key;
        
        // setto tutti i textField nella inputView
        while (keys.hasNext()) {
            key = (String) keys.next();
            inputPanel.setValue(key, prop.getProperty(key + DEL + nConcio));
        }
    }
    
    /**
     * controlla tutti i valori della maschera Parser - View -----> Model
     *
     * @throws java.lang.Exception
     */
    public static void validate() throws Exception {
        
        validateInputAnalisi();
        validateVerificaSezioni();
        validateVerificaInstabilita();
        validateVerificaGiunti();
        
    }
    
    private static void validateVerificaInstabilita() throws Exception {
        
        InputInstabilita inputPanel = InputInstabilita.getInstance();
        Progetto prg = Progetto.getInstance();
        Sezione sez = prg.getCurrentSezioneVerifica();
        SezioneMetallica sm = sez.getSezioneMetallica();
        
        if (sez == null)
            return;
        
        sm.setPiattabandaSupIrrigidita(inputPanel.getCbPiattabSupIrrigidita().isSelected());
        
        // irrigidimenti trasve
        Irrigidimento_T it = (Irrigidimento_T) sez.getIrrigidimetoTrasversale();
        it.setPasso(Double.parseDouble(inputPanel.getValue("passsoTrasv")));
        it.setBsl(Double.parseDouble(inputPanel.getValue("b1t")));
        it.setTsl1(Double.parseDouble(inputPanel.getValue("h1t")));
        it.setHsl(Double.parseDouble(inputPanel.getValue("b2t")));
        it.setTsl2(Double.parseDouble(inputPanel.getValue("h2t")));
        it.setDoppio(inputPanel.getCkDoppioIrTrasv().isSelected());
        it.setRigidEndPost(inputPanel.getCkRigidEndPost().isSelected());
        
        // sezione metallica a doppio T
        if (sm.getClass() == SezioneMetallicaDoppioT.class) {
            SezioneMetallicaDoppioT sd = (SezioneMetallicaDoppioT) sm;
            ArrayList<Irrigidimento> ir = sd.getIrsAnima();
            PannelloIrrigidimentoLongDoppioT pn =(PannelloIrrigidimentoLongDoppioT)inputPanel.getPIrrLong();
            int nIrrigi = (Integer)pn.getCNumeroIrrigidimentiAnima().getSelectedItem();
            
            ir.clear();
            if (nIrrigi >0) {
                Irrigidimento_L i1 = new Irrigidimento_L();
                i1.setTsl1(Double.parseDouble(pn.getValue("h1")));
                i1.setTsl2(Double.parseDouble(pn.getValue("h2")));
                i1.setBsl(Double.parseDouble(pn.getValue("b1")));
                i1.setHsl(Double.parseDouble(pn.getValue("b2")));
                i1.setY(Double.parseDouble(pn.getValue("y1")));
                ir.add(i1);
                
            }
            if (nIrrigi > 1) {
                Irrigidimento_L i2 = new Irrigidimento_L();
                i2.setTsl1(Double.parseDouble(pn.getValue("h1")));
                i2.setTsl2(Double.parseDouble(pn.getValue("h2")));
                i2.setBsl(Double.parseDouble(pn.getValue("b1")));
                i2.setHsl(Double.parseDouble(pn.getValue("b2")));
                i2.setY(Double.parseDouble(pn.getValue("y2")));
                ir.add(i2);
            }
        }
        // sezione metallica a cassone
        // sezione metallica a doppio T
        else if (sm.getClass() == SezioneMetallicaCassone.class) {
            SezioneMetallicaCassone sd = (SezioneMetallicaCassone) sm;
            ArrayList<Irrigidimento> ir = sd.getIrsAnime();
            ArrayList<Irrigidimento> irInf = sd.getIrsPiattaInf();
            
            PannelloIrrigidimLongCassone pn =(PannelloIrrigidimLongCassone)inputPanel.getPIrrLong();
            int nIrrigi = (Integer)pn.getCNumeroIrrigidimentiAnima().getSelectedItem();
            int nIrrigiInf = (Integer)pn.getCNumeroIrrigidimentiPiattaInf().getSelectedItem();
            
            ir.clear();
            irInf.clear();
            
            //irrigidimenti anime
            if (nIrrigi >0) {
                Irrigidimento_L i1 = new Irrigidimento_L();
                i1.setTsl1(Double.parseDouble(pn.getValue("h1")));
                i1.setTsl2(Double.parseDouble(pn.getValue("h2")));
                i1.setBsl(Double.parseDouble(pn.getValue("b1")));
                i1.setHsl(Double.parseDouble(pn.getValue("b2")));
                i1.setY(Double.parseDouble(pn.getValue("y1")));
                ir.add(i1);
                
            }
            if (nIrrigi > 1) {
                Irrigidimento_L i2 = new Irrigidimento_L();
                i2.setTsl1(Double.parseDouble(pn.getValue("h1")));
                i2.setTsl2(Double.parseDouble(pn.getValue("h2")));
                i2.setBsl(Double.parseDouble(pn.getValue("b1")));
                i2.setHsl(Double.parseDouble(pn.getValue("b2")));
                i2.setY(Double.parseDouble(pn.getValue("y2")));
                ir.add(i2);
            }
            //irrigidimenti piatta inf
            double bloc = sd.getBi()/(nIrrigiInf+1);
            double iter = bloc;
            for(int i=0;i<nIrrigiInf; ++i){
                Irrigidimento_L i1 = new Irrigidimento_L();
                i1.setTsl1(Double.parseDouble(pn.getValue("h1")));
                i1.setTsl2(Double.parseDouble(pn.getValue("h2")));
                i1.setBsl(Double.parseDouble(pn.getValue("b1")));
                i1.setHsl(Double.parseDouble(pn.getValue("b2")));
                i1.setY(iter);
                irInf.add(i1);
                iter+=bloc;
            }
            
        } else if (sm.getClass() == SezioneMetallicaCassoneII.class) {
            SezioneMetallicaCassoneII sd = (SezioneMetallicaCassoneII) sm;
            ArrayList<Irrigidimento> ir = sd.getIrsAnimeLaterali();
            ArrayList<Irrigidimento> irC = sd.getIrsAnimeCentrali();
            
            ArrayList<Irrigidimento> irInf = sd.getIrsPiattaInfLaterali();
            ArrayList<Irrigidimento> irInfC = sd.getIrsPiattaInfCentrali();
            
            PannelloIrrigidimLongCassoneII pn =(PannelloIrrigidimLongCassoneII)inputPanel.getPIrrLong();
            int nIrrigi = (Integer)pn.getCNumeroIrrigidimentiAnima().getSelectedItem();
            int nIrrigiInf = (Integer)pn.getCNumeroIrrigidimentiPiattaInf().getSelectedItem();
            
            int nIrrigiC = (Integer)pn.getCNumeroIrrigidimentiAnimaCen().getSelectedItem();
            int nIrrigiInfC = (Integer)pn.getCNumeroIrrigidimentiPiattaInfCen().getSelectedItem();
            
            ir.clear();
            irInf.clear();
            irC.clear();
            irInfC.clear();
            

            //irrigidimenti anime
            if (nIrrigi >0) {
                Irrigidimento_L i1 = new Irrigidimento_L();
                i1.setTsl1(Double.parseDouble(pn.getValue("h1")));
                i1.setTsl2(Double.parseDouble(pn.getValue("h2")));
                i1.setBsl(Double.parseDouble(pn.getValue("b1")));
                i1.setHsl(Double.parseDouble(pn.getValue("b2")));
                i1.setY(Double.parseDouble(pn.getValue("y1")));
                ir.add(i1);
            }
            
            if (nIrrigi > 1) {
                Irrigidimento_L i2 = new Irrigidimento_L();
                i2.setTsl1(Double.parseDouble(pn.getValue("h1")));
                i2.setTsl2(Double.parseDouble(pn.getValue("h2")));
                i2.setBsl(Double.parseDouble(pn.getValue("b1")));
                i2.setHsl(Double.parseDouble(pn.getValue("b2")));
                i2.setY(Double.parseDouble(pn.getValue("y2")));
                ir.add(i2);
            }
            //irrigidimenti anime centrali
            if (nIrrigiC >0) {
                Irrigidimento_L i1 = new Irrigidimento_L();
                i1.setTsl1(Double.parseDouble(pn.getValue("h1")));
                i1.setTsl2(Double.parseDouble(pn.getValue("h2")));
                i1.setBsl(Double.parseDouble(pn.getValue("b1")));
                i1.setHsl(Double.parseDouble(pn.getValue("b2")));
                i1.setY(Double.parseDouble(pn.getValue("y1c")));
                irC.add(i1);
                
            }
            if (nIrrigiC > 1) {
                Irrigidimento_L i2 = new Irrigidimento_L();
                i2.setTsl1(Double.parseDouble(pn.getValue("h1")));
                i2.setTsl2(Double.parseDouble(pn.getValue("h2")));
                i2.setBsl(Double.parseDouble(pn.getValue("b1")));
                i2.setHsl(Double.parseDouble(pn.getValue("b2")));
                i2.setY(Double.parseDouble(pn.getValue("y2c")));
                irC.add(i2);
            }
                       
           
            //irrigidimenti piatta inf laterali
            double bloc = (sd.getBi()-sd.getBic())/2/(nIrrigiInf+1);
            double iter = bloc;
            for(int i=0;i<nIrrigiInf; ++i){
                Irrigidimento_L i1 = new Irrigidimento_L();
                i1.setTsl1(Double.parseDouble(pn.getValue("h1")));
                i1.setTsl2(Double.parseDouble(pn.getValue("h2")));
                i1.setBsl(Double.parseDouble(pn.getValue("b1")));
                i1.setHsl(Double.parseDouble(pn.getValue("b2")));
                i1.setY(iter);
                irInf.add(i1);
                iter+=bloc;
            }
            
            //irrigidimenti piatta inf laterali
            bloc = sd.getBic()/(nIrrigiInfC+1);
            iter = bloc;
            for(int i=0;i<nIrrigiInfC; ++i){
                Irrigidimento_L i1 = new Irrigidimento_L();
                i1.setTsl1(Double.parseDouble(pn.getValue("h1")));
                i1.setTsl2(Double.parseDouble(pn.getValue("h2")));
                i1.setBsl(Double.parseDouble(pn.getValue("b1")));
                i1.setHsl(Double.parseDouble(pn.getValue("b2")));
                i1.setY(iter);
                irInfC.add(i1);
                iter+=bloc;
            }
        }
        
    }
    
    private static void validateVerificaGiunti() throws Exception {
        
        InputGiuntoBullonatoSup ipSup = InputGiuntoBullonatoSup.getInstance();
        InputGiuntoBullonatoInf ipInf = InputGiuntoBullonatoInf.getInstance();
        InputGiuntoBullonatoAnima ipAnima = InputGiuntoBullonatoAnima
                .getInstance();
        InputGiuntiBullonatiGeneral ipGen = InputGiuntiBullonatiGeneral
                .getInstance();
        Progetto prg = Progetto.getInstance();
        Sezione sez = prg.getCurrentSezioneVerifica();
        
        if(sez.getSezioneMetallica().getClass()!= SezioneMetallicaDoppioT.class) return;
        
        if (sez == null)
            return;
        if (sez.getGiunto() == null)
            return;
        
        
        GiuntoBullonato giunto = sez.getGiunto();
        
        
        giunto.setE1aSup(Double.parseDouble(ipSup.getValue("e1aSup")));
        giunto.setE1bSup(Double.parseDouble(ipSup.getValue("e1bSup")));
        giunto.setE2aSup(Double.parseDouble(ipSup.getValue("e2aSup")));
        giunto.setE2bSup(Double.parseDouble(ipSup.getValue("e2bSup")));
        giunto.setP1Sup(Double.parseDouble(ipSup.getValue("p1Sup")));
        giunto.setP2Sup(Double.parseDouble(ipSup.getValue("p2Sup")));
        giunto.setNfile1Sup(Integer.parseInt(ipSup.getValue("nFile1Sup")));
        giunto.setNfile2Sup(Integer.parseInt(ipSup.getValue("nFile2Sup")));
        giunto.setDiamBullSup(ipSup.getDiametroBullone());
        giunto.setDiamForoSup(Integer.parseInt(ipSup.getValue("diamForoSup")));
        giunto.setTgSup(Double.parseDouble(ipSup.getValue("tgSup")));
        
        // inf
        giunto.setE1aInf(Double.parseDouble(ipInf.getValue("e1aInf")));
        giunto.setE1bInf(Double.parseDouble(ipInf.getValue("e1bInf")));
        giunto.setE2aInf(Double.parseDouble(ipInf.getValue("e2aInf")));
        giunto.setE2bInf(Double.parseDouble(ipInf.getValue("e2bInf")));
        giunto.setP1Inf(Double.parseDouble(ipInf.getValue("p1Inf")));
        giunto.setP2Inf(Double.parseDouble(ipInf.getValue("p2Inf")));
        giunto.setNfile1Inf(Integer.parseInt(ipInf.getValue("nFile1Inf")));
        giunto.setNfile2Inf(Integer.parseInt(ipInf.getValue("nFile2Inf")));
        giunto.setDiamBullInf(ipInf.getDiametroBullone());
        giunto.setDiamForoInf(Integer.parseInt(ipInf.getValue("diamForoInf")));
        giunto.setTgInf(Double.parseDouble(ipInf.getValue("tgInf")));
        // anima
        giunto.setE1aW(Double.parseDouble(ipAnima.getValue("e1aW")));
        giunto.setE1bW(Double.parseDouble(ipAnima.getValue("e1bW")));
        giunto.setE2W(Double.parseDouble(ipAnima.getValue("e2W")));
        giunto.setP1W(Double.parseDouble(ipAnima.getValue("p1W")));
        giunto.setP2W(Double.parseDouble(ipAnima.getValue("p2W")));
        giunto.setNfile1W(Integer.parseInt(ipAnima.getValue("nFile1W")));
        giunto.setNfile2W(Integer.parseInt(ipAnima.getValue("nFile2W")));
        giunto.setDiamBullW(ipAnima.getDiametroBullone());
        giunto.setDiamForoW(Integer.parseInt(ipAnima.getValue("diamForoW")));
        giunto.setTgW(Double.parseDouble(ipAnima.getValue("tgW")));
        
        giunto.setNu(Double.parseDouble((ipGen.getValue("nu"))));
        giunto.setKs(Double.parseDouble((ipGen.getValue("ks"))));
        giunto.setFubulloni(Double.parseDouble((ipGen.getValue("fuBulloni"))));
        giunto.setFuPiastre(Double.parseDouble((ipGen.getValue("fuCoprig"))));
        
    }
    
    private static void validateInputAnalisi() throws Exception {
        
        InputView inputPanel = InputView.getInstance();
        Progetto prg = Progetto.getInstance();
        Sezione sezione = prg.getCurrentSezioneAnalisi();
        
        // DATI GENERALI
        double[] n = new double[4];
        
        n[0] = Double.parseDouble(inputPanel.getN1());
        n[1] = Double.parseDouble(inputPanel.getN2());
        n[2] = Double.parseDouble(inputPanel.getN3());
        n[3] = Double.parseDouble(inputPanel.getN4());
        prg.setN(n);
        prg.setNomeProgetto(inputPanel.getNomeOpera());
        
        sezione.setSezioneMetallica((SezioneMetallica) inputPanel
                .getComboConcio().getSelectedItem());
        sezione.setNCampata(inputPanel.getComboCampata().getSelectedIndex());
//		sezione.setCalcoloAutomaticoBeff(inputPanel.getCkCalcoloBeff()
//				.isSelected());
        boolean calcoloArmatura = inputPanel.getCkCalcoloArmautr().isSelected();
        sezione.setCalcolaArmatura(calcoloArmatura);
        
        boolean calcoloAutomaticoBeff = inputPanel.getCkCalcoloBeff()
        .isSelected();
        sezione.setCalcoloAutomaticoBeff(calcoloAutomaticoBeff);
        
        if (!calcoloArmatura) {
            sezione.setAreaArmatura(Double.parseDouble(inputPanel
                    .getValue("Af")));
        } else {
            sezione.setDiametroArmatura(Double.parseDouble(inputPanel
                    .getValue("DiametroArmatura")));
            sezione.setPassoArmatura(Double.parseDouble(inputPanel
                    .getValue("PassoArmatura")));
            double aarm = sezione.getAreaArmatura(sezione.getBeff(prg.getCampate()));
            sezione.setAreaArmatura(aarm);
            
        }
        
        validateConcio(sezione.getSezioneMetallica());
        validateSoletta(sezione.getSoletta());
        
    }
    
    /**
     *
     */
    private static void validateVerificaSezioni() throws Exception {
        InputVerifiche inputPanel = InputVerifiche.getInstance();
        
        Progetto prg = Progetto.getInstance();
        Sezione sez = prg.getCurrentSezioneVerifica();
        if (sez == null)
            return;
        
        Soletta sl = sez.getSoletta();
        
        double xs = Double.parseDouble(inputPanel.getValue("x"));
        if (xs<0) xs=0;
        
        sez.setSezioneMetallica((SezioneMetallica) inputPanel.getComboConcio()
        .getSelectedItem());
        sez.setSoletta((Soletta) inputPanel.getComboSoletta()
        .getSelectedItem());
        
        if (!inputPanel.getCkCalcoloAutomatico().isSelected()) {
            sez.setXSezione(Double.parseDouble(inputPanel.getValue("x")));
        } else {
            sez.setCalcoloAutomaticoBeff(inputPanel.getCkCalcoloAutomatico()
            .isSelected());
            sez.setNCampata(inputPanel.getComboCampata().getSelectedIndex());
            // verifica la coordinata x
            if (!prg.verificaCoordinataX(sez.getNCampata(), xs)) {
                JOptionPane
                        .showMessageDialog(
                        null,
                        "La coordinata x della sezione non si trova all'interno "
                        + "della campata seleziona. Si pone x uguale alla lunghezza della " +
                        "campata",
                        "Modifica coordinata x",
                        JOptionPane.WARNING_MESSAGE);
                      
                sez.setXSezione(prg.getCampate().getLuci()[sez.getNCampata()]);
                inputPanel.setValue("x", String.valueOf(sez.getXSezione()));
            } else {
                sez.setXSezione(Double.parseDouble(inputPanel.getValue("x")));
            }
        }
        // armatura
        boolean calcoloArmatura = inputPanel.getCkCalcoloArmatura()
        .isSelected();
        sez.setCalcolaArmatura(calcoloArmatura);
        if (!calcoloArmatura) {
            sez.setAreaArmatura(Double.parseDouble(inputPanel.getValue("Af")));
        } else {
            sez.setAreaArmatura(sez.getAreaArmatura(sez.getBeff(prg.getCampate())));
            sez.setDiametroArmatura(Double.parseDouble(inputPanel
                    .getValue("DiametroArmatura")));
            sez.setPassoArmatura(Double.parseDouble(inputPanel
                    .getValue("PassoArmatura")));
        }
        
        // sollecitazioni
        int ncond = sez.getSollecitazioni().size();
        TableSezModel model = (TableSezModel) TableCondView.getRowData();
        for (int row = 0; row < ncond; row++) {
            Sollecitazioni sol = sez.getSollecitazioni().get(row);
            sol.getCond().setNome((String) model.getValueAt(row, 0));
            sol.getCond().setN((int) model.getDoubleValueAt(row, 1)); // n
            sol.setN(model.getDoubleValueAt(row, 2)); // N
            sol.setV(model.getDoubleValueAt(row, 3)); // V
            sol.setM(model.getDoubleValueAt(row, 4)); // M
            sol.setSigCls(model.getDoubleValueAt(row, 5)); // Nsoletta
            sol.setMt(model.getDoubleValueAt(row, 6)); // M torcente
        } // combinazioni
        int ncomb = prg.getCombinazioni().size();
        model = (TableSezModel) TableCombView.getRowData(); // ciclo
        // combianzioni
        
        for (int j = 0; j < ncomb; ++j) {
            double[] c1 = prg.getCombinazioni().get(j).getC1();
            CombinazioneCarichi cmb = prg.getCombinazioni().get(j); // ciclo
            // condizioni
            for (int row = 0; row < ncond; row++) {
                c1[row] = model.getDoubleValueAt(row, j + 1);
            }
            cmb.setC1(c1);
        }
        
    }
    
    public static Object[][] getRowDataTensioniEfficaci() {
        
        Progetto prg = Progetto.getInstance();
        // int nc = prg.getCombinazioni().size();
        Sezione sez = prg.getCurrentSezioneVerifica();
        int nc = sez.getSezioniOutput().size();
        
        Object[][] data = new Object[nc][11];
        
        for (int i = 0; i < nc; ++i) {
            CombinazioneCarichi cmb = prg.getCombinazioni().get(i);
            SezioneOutputTensioniFase ps = sez.getTensioniTotali(i);
            SezioneMetallica seff = sez.getSezioniMetEfficaci().get(i);
            
  //          String classe = "Classe: "
  //                  + seff.getClasseSezione(prg.getMateriale());
            
            data[i][0] = new String(cmb.getName());
//            data[i][1] = new String(classe);
            data[i][1] = new Double(ps.getSs());
            data[i][2] = new Double(ps.getIi());
            data[i][3] = new Double(seff.getSEff());
            data[i][4] = new Double(seff.getIEff());
            data[i][5] = new Double(seff.getVbsd());
            data[i][6] = new Double(seff.getVbwRd());
            data[i][7] = new Double(seff.getVbfRd());
            data[i][8] = new Double(seff.getVbRd());
            data[i][9] = new Double(seff.getNu3());
            data[i][10] = new Double(seff.getNuInterM_V());
        }
        return data;
    }
    
    /**
     *
     * @return
     */
    public static Object[][] getRowDataTensioni() {
        
        Progetto prg = Progetto.getInstance();
        Sezione sez = prg.getCurrentSezioneVerifica();
        int nc = sez.getSezioniOutput().size();
        
        Object[][] data = new Object[nc][12];
        
        for (int i = 0; i < nc; ++i) {
            SezioneOutputTensioniFase ps = sez.getTensioniTotali(i);
            CombinazioneCarichi cmb = prg.getCombinazioni().get(i);
            // ParametriStaticiSezione ps = sez.getParametriOut().get(i);
            // getTensioniSezione(sez, cmb);
            
            data[i][0] = new String(cmb.getName());
            data[i][1] = new Double(ps.getSc());
            data[i][2] = new Double(ps.getSf());
            data[i][3] = new Double(ps.getSs());
            data[i][4] = new Double(ps.getS());
            data[i][5] = new Double(ps.getI());
            data[i][6] = new Double(ps.getIi());
            data[i][7] = new Double(ps.getTs());
            data[i][8] = new Double(ps.getTi());
            data[i][9] = new Double(ps.getSid1());
            data[i][10] = new Double(ps.getSid2());
            data[i][11] = new Double(ps.getVPioli());
        }
        
        return data;
    }
    
    public static String[] getColumnNameVerificaBulloni() {
        String[] names = { "combo", "VSd", "VbRd coprig", "VbRd flangia",
        "VvRd", "VsRd SLU", "VsRd SLE" };
        return names;
    }
    
    public static String[] getColumnNameTensioni() {
        String[] names = { "cond.", "σc (MPA)", "σf (MPA)", "σss  (MPA)", "σs (MPA)", "σi (MPA)", "σii (MPA)", "ts (MPA)",
        "ti (MPA)", "σids (MPA)", "σidi (MPA)", "Fsc (kN/ml)" };
        return names;
    }
    
    public static String[] getColumnNameTensioniEfficaci() {
        String[] names = { "cond.", "σss (MPa)", "σii (MPa)", "σssred (MPa)", "σiired (MPa)",
        "VbSd (N)","VbwRd (N)","VbfRd (N)","VbRd TOT (N)", "coeff. verifica taglio", "coeff. verifica V+M+N" };
        return names;
        
    }
    
    private static void validateSoletta(Soletta sol) throws Exception {
        InputView iv = InputView.getInstance();
        
        if (sol.getClass() == SolettaT.class) {
            SolettaT sl = (SolettaT) sol;
            PannelloSolettaT inputPanel = (PannelloSolettaT) iv.getSoletta();
            sl.setB(Double.parseDouble(inputPanel.getValue("B")));
            sl.setH(Double.parseDouble(inputPanel.getValue("H")));
            sl.setHinf(Double.parseDouble(inputPanel.getValue("h")));
            sl.setBinf(Double.parseDouble(inputPanel.getValue("b")));
            sl.setBo(Double.parseDouble(inputPanel.getValue("b0")));
        } else if (sol.getClass() == SolettaType2.class) {
            SolettaType2 sl = (SolettaType2) sol;
            PannelloSolettaType2 inputPanel = (PannelloSolettaType2) iv
                    .getSoletta();
            sl.setB(Double.parseDouble(inputPanel.getValue("b")));
            sl.setBs(Double.parseDouble(inputPanel.getValue("bs")));
            sl.setBd(Double.parseDouble(inputPanel.getValue("bd")));
            sl.setHs1(Double.parseDouble(inputPanel.getValue("hs1")));
            sl.setHs2(Double.parseDouble(inputPanel.getValue("hs2")));
            sl.setHd1(Double.parseDouble(inputPanel.getValue("hd1")));
            sl.setHd2(Double.parseDouble(inputPanel.getValue("hd2")));
            sl.setBo(Double.parseDouble(inputPanel.getValue("bo")));
        } else if (sol.getClass() == SolettaType2_2.class) {
            SolettaType2_2 sl = (SolettaType2_2) sol;
            PannelloSolettaType2 inputPanel = (PannelloSolettaType2) iv
                    .getSoletta();
            sl.setB(Double.parseDouble(inputPanel.getValue("b")));
            sl.setBs(Double.parseDouble(inputPanel.getValue("bs")));
            sl.setBd(Double.parseDouble(inputPanel.getValue("bd")));
            sl.setHs1(Double.parseDouble(inputPanel.getValue("hs1")));
            sl.setHs2(Double.parseDouble(inputPanel.getValue("hs2")));
            sl.setHd1(Double.parseDouble(inputPanel.getValue("hd1")));
            sl.setHd2(Double.parseDouble(inputPanel.getValue("hd2")));
            sl.setBo(Double.parseDouble(inputPanel.getValue("bo")));
        }
    }
    
    private static void validateConcio(SezioneMetallica sezM) throws Exception {
        
        if (sezM.getClass() == SezioneMetallicaDoppioT.class) {
            SezioneMetallicaDoppioT sm = (SezioneMetallicaDoppioT) sezM;
            InputView iv = InputView.getInstance();
            PannelloConcioDoppioT inputPanel = (PannelloConcioDoppioT) iv
                    .getConcio();
            sm.setBs(Double.parseDouble(inputPanel.getValue("bs")));
            sm.setTs(Double.parseDouble(inputPanel.getValue("ts")));
            sm.setTw(Double.parseDouble(inputPanel.getValue("tw")));
            sm.setBi(Double.parseDouble(inputPanel.getValue("bi")));
            sm.setTi(Double.parseDouble(inputPanel.getValue("ti")));
            double dt = sm.getTs()+sm.getTi();
            sm.setHw(Double.parseDouble(inputPanel.getValue("hw"))-dt);
            
        } else if (sezM.getClass() == SezioneMetallicaCassone.class) {
            SezioneMetallicaCassone sm = (SezioneMetallicaCassone) sezM;
            InputView iv = InputView.getInstance();
            PannelloConcioCassone inputPanel = (PannelloConcioCassone) iv
                    .getConcio();
            sm.setBs(Double.parseDouble(inputPanel.getValue("bs")));
            sm.setTs(Double.parseDouble(inputPanel.getValue("ts")));
            sm.setTw(Double.parseDouble(inputPanel.getValue("tw")));
            sm.setBi(Double.parseDouble(inputPanel.getValue("bi")));
            sm.setTi(Double.parseDouble(inputPanel.getValue("ti")));
            sm.setTeq(Double.parseDouble(inputPanel.getValue("teq")));
            double Htot = Double.parseDouble(inputPanel.getValue("Htot"))-sm.getTs()-sm.getTi();
            double alfa = (Math.PI* Double.parseDouble(inputPanel.getValue("alfa")) / 180);
            sm.setAlfa(alfa);
            double hw = Htot/Math.cos(alfa);
            sm.setHw(hw);
            
        } else if (sezM.getClass() == SezioneMetallicaGenerica.class) {
            SezioneMetallicaGenerica sm = (SezioneMetallicaGenerica) sezM;
            InputView iv = InputView.getInstance();
            PannelloConcioGenerico inputPanel = (PannelloConcioGenerico) iv
                    .getConcio();
            sm.setAsgen(Double.parseDouble(inputPanel.getValue("Asgen")));
            sm.setHgen(Double.parseDouble(inputPanel.getValue("Hsgen")));
            sm.setJgen(Double.parseDouble(inputPanel.getValue("Jsgen")));
            sm.setYggen(Double.parseDouble(inputPanel.getValue("Yggen")));
            sm.setAwgen(Double.parseDouble(inputPanel.getValue("Awgen")));
            
        } else if (sezM.getClass() == SezioneMetallicaCassoneII.class) {
            SezioneMetallicaCassoneII sm = (SezioneMetallicaCassoneII) sezM;
            InputView iv = InputView.getInstance();
            PannelloConcioCassoneII inputPanel = (PannelloConcioCassoneII) iv
                    .getConcio();
            sm.setBs(Double.parseDouble(inputPanel.getValue("bs")));
            sm.setTs(Double.parseDouble(inputPanel.getValue("ts")));
            sm.setTw(Double.parseDouble(inputPanel.getValue("tw")));
            sm.setBi(Double.parseDouble(inputPanel.getValue("bi")));
            sm.setTi(Double.parseDouble(inputPanel.getValue("ti")));
            
            double Htot = Double.parseDouble(inputPanel.getValue("Htot"))-sm.getTs()-sm.getTi();
            double alfa = (Math.PI* Double.parseDouble(inputPanel.getValue("alfa")) / 180);
            sm.setAlfa(alfa);
            double hw = Htot/Math.cos(alfa);
            sm.setHw(hw);
            
            sm.setTeq(Double.parseDouble(inputPanel.getValue("teq")));
            sm.setTsc(Double.parseDouble(inputPanel.getValue("tsc")));
            sm.setBsc(Double.parseDouble(inputPanel.getValue("bsc")));
            sm.setTwc(Double.parseDouble(inputPanel.getValue("twc")));
            sm.setBic(Double.parseDouble(inputPanel.getValue("bic")));
            
            
        }
    }
    
    private static void updateSoletta(Soletta sl) {
        InputView iv = InputView.getInstance();
        
        if (sl.getClass() == SolettaT.class) {
            SolettaT sol = (SolettaT) sl;
            PannelloSolettaT inputPanel = (PannelloSolettaT) iv.getSoletta();
            inputPanel.setValue("B", String.valueOf(sol.getB()));
            inputPanel.setValue("H", String.valueOf(sol.getH()));
            inputPanel.setValue("b0", String.valueOf(sol.getBo()));
            inputPanel.setValue("b", String.valueOf(sol.getBinf()));
            inputPanel.setValue("h", String.valueOf(sol.getHinf()));
        } else if (sl.getClass() == SolettaType2.class) {
            SolettaType2 sol = (SolettaType2) sl;
            PannelloSolettaType2 inputPanel = (PannelloSolettaType2) iv
                    .getSoletta();
            inputPanel.setValue("b", String.valueOf(sol.getB()));
            inputPanel.setValue("bs", String.valueOf(sol.getBs()));
            inputPanel.setValue("bd", String.valueOf(sol.getBd()));
            inputPanel.setValue("hs1", String.valueOf(sol.getHs1()));
            inputPanel.setValue("hs2", String.valueOf(sol.getHs2()));
            inputPanel.setValue("hd1", String.valueOf(sol.getHd1()));
            inputPanel.setValue("hd2", String.valueOf(sol.getHd2()));
            inputPanel.setValue("bo", String.valueOf(sol.getBo()));
        } else if (sl.getClass() == SolettaType2_2.class) {
            SolettaType2_2 sol = (SolettaType2_2) sl;
            PannelloSolettaType2 inputPanel = (PannelloSolettaType2) iv
                    .getSoletta();
            inputPanel.setValue("b", String.valueOf(sol.getB()));
            inputPanel.setValue("bs", String.valueOf(sol.getBs()));
            inputPanel.setValue("bd", String.valueOf(sol.getBd()));
            inputPanel.setValue("hs1", String.valueOf(sol.getHs1()));
            inputPanel.setValue("hs2", String.valueOf(sol.getHs2()));
            inputPanel.setValue("hd1", String.valueOf(sol.getHd1()));
            inputPanel.setValue("hd2", String.valueOf(sol.getHd2()));
            inputPanel.setValue("bo", String.valueOf(sol.getBo()));
        }
        
    }
    
    private static void updateConcio(SezioneMetallica sezM) {
        
        if (sezM.getClass() == SezioneMetallicaDoppioT.class) {
            SezioneMetallicaDoppioT sm = (SezioneMetallicaDoppioT) sezM;
            InputView iv = InputView.getInstance();
            PannelloConcioDoppioT inputPanel = (PannelloConcioDoppioT) iv
                    .getConcio();
            inputPanel.setValue("bs", String.valueOf(sm.getBs()));
            inputPanel.setValue("ts", String.valueOf(sm.getTs()));
            inputPanel.setValue("tw", String.valueOf(sm.getTw()));
            inputPanel.setValue("bi", String.valueOf(sm.getBi()));
            inputPanel.setValue("ti", String.valueOf(sm.getTi()));
            double dt = sm.getTi()+sm.getTs();
            inputPanel.setValue("hw", String.valueOf(sm.getHw()+dt));
            
        } else if (sezM.getClass() == SezioneMetallicaCassone.class) {
            SezioneMetallicaCassone sm = (SezioneMetallicaCassone) sezM;
            InputView iv = InputView.getInstance();
            PannelloConcioCassone inputPanel = (PannelloConcioCassone) iv
                    .getConcio();
            inputPanel.setValue("bs", String.valueOf(sm.getBs()));
            inputPanel.setValue("ts", String.valueOf(sm.getTs()));
            inputPanel.setValue("tw", String.valueOf(sm.getTw()));
            inputPanel.setValue("bi", String.valueOf(sm.getBi()));
            inputPanel.setValue("ti", String.valueOf(sm.getTi()));
            double H =sm.getHw()*Math.cos(sm.getAlfa())+sm.getTi()+sm.getTs();
            inputPanel.setValue("Htot", String.valueOf(Math.round(H)));
            inputPanel.setValue("alfa", String.valueOf(ns.format(sm.getAlfa()
            * 180 / Math.PI)));
            inputPanel.setValue("teq", String.valueOf(sm.getTeq()));
            
        } else if (sezM.getClass() == SezioneMetallicaGenerica.class) {
            SezioneMetallicaGenerica sm = (SezioneMetallicaGenerica) sezM;
            InputView iv = InputView.getInstance();
            PannelloConcioGenerico inputPanel = (PannelloConcioGenerico) iv
                    .getConcio();
            inputPanel.setValue("Asgen", String.valueOf(sm.getAsgen()));
            inputPanel.setValue("Jsgen", String.valueOf(sm.getJgen()));
            inputPanel.setValue("Yggen", String.valueOf(sm.getYggen()));
            inputPanel.setValue("Hsgen", String.valueOf(sm.getHgen()));
            inputPanel.setValue("Awgen", String.valueOf(sm.getAwgen()));
        } else if (sezM.getClass() == SezioneMetallicaCassoneII.class) {
            SezioneMetallicaCassoneII sm = (SezioneMetallicaCassoneII) sezM;
            InputView iv = InputView.getInstance();
            PannelloConcioCassoneII inputPanel = (PannelloConcioCassoneII) iv
                    .getConcio();
            inputPanel.setValue("bs", String.valueOf(sm.getBs()));
            inputPanel.setValue("ts", String.valueOf(sm.getTs()));
            inputPanel.setValue("tw", String.valueOf(sm.getTw()));
            inputPanel.setValue("bi", String.valueOf(sm.getBi()));
            inputPanel.setValue("ti", String.valueOf(sm.getTi()));
            inputPanel.setValue("teq", String.valueOf(sm.getTeq()));
            
            double H =sm.getHw()*Math.cos(sm.getAlfa())+sm.getTi()+sm.getTs();
            inputPanel.setValue("Htot", String.valueOf(Math.round(H)));
            inputPanel.setValue("alfa", String.valueOf(ns.format(sm.getAlfa()
            * 180 / Math.PI)));
            
            inputPanel.setValue("tsc", String.valueOf(sm.getTsc()));
            inputPanel.setValue("twc", String.valueOf(sm.getTwc()));
            inputPanel.setValue("bic", String.valueOf(sm.getBic()));
            inputPanel.setValue("bsc", String.valueOf(sm.getBsc()));
            
        }
        
    }
}
