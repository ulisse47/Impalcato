package cassone.util.dialog;

import cassone.model.CombinazioneCarichi;
import cassone.model.MaterialeAcciaio;
import cassone.model.Sezione;
import cassone.model.SezioneOutputTensioniFase;
import cassone.model.SezioniMetalliche.SezioneMetallica;
import cassone.model.irrigidimenti.Irrigidimento;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import cassone.model.Progetto;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author Andrea Cavalieri
 * @version 1.0
 */

public class DlgVerifiche extends JDialog implements ActionListener{
    private JTextArea txtArea = new JTextArea();
    
    private JButton startButton;
    
    JProgressBar progressBar;
    JTextArea taskOutput;
    JPanel panel;
    JTable table ;
    public DlgVerifiche(boolean modal) throws HeadlessException {
        
        this.setModal(modal);
        
        try {
            jbInit();
            this.setTitle("VERIFICHE");
            //     Progetto prg = Progetto.getInstance();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    private void jbInit() throws Exception {
        setLayout(new BorderLayout());
        this.setSize(1000,400);
        
        Dimension scr = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((int)(scr.getWidth()/2- getWidth()/2),(int)(scr.getHeight()/2-getHeight()/2));
        
        //Create the demo's UI.
        startButton = new JButton("Ricalcola");
        startButton.setActionCommand("Ricalcola");
        startButton.addActionListener(this);
        
        table = new JTable(new MyTableModel());
        table.setPreferredScrollableViewportSize(new Dimension(1000, 250));
        table.setFillsViewportHeight(true);
        table.setDefaultRenderer(JComponent.class,new DateRenderer());
        table.setSelectionBackground(Color.WHITE);
        table.setSelectionForeground(Color.WHITE);
        
        progressBar = new JProgressBar(0,table.getRowCount());
        MyTableModel model = (MyTableModel) table.getModel();
        progressBar.setValue(model.getVerificati());
        progressBar.setStringPainted(true);
        
        
        panel = new JPanel();
        panel.add(progressBar);
        add(panel, BorderLayout.PAGE_START);
        add(startButton, BorderLayout.PAGE_END);
        add(new JScrollPane(table), BorderLayout.CENTER);
        //   this.getContentPane().setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        
    }
    
    public Object[][] getDatiVerifica() {
        Progetto prg = Progetto.getInstance();
        
        ArrayList<Sezione> sezioniVerifica =prg.getSezioniVerifica();
        ArrayList<Sezione> sezioniAnalisiGlobale =prg.getSezioniAnalisiGlobale();
        ArrayList<CombinazioneCarichi> combinazioni =prg.getCombinazioni();
        MaterialeAcciaio mat = prg.getMateriale();
        
        NumberFormat nf = NumberFormat.getInstance();
        NumberFormat nf2 = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        nf2.setMaximumFractionDigits(2);
        
        int ntask = sezioniVerifica.size()*combinazioni.size()*11;
/*        ntask+= sezioniVerifica.size()*combinazioni.size();
        ntask+= sezioniVerifica.size()*combinazioni.size();
 */
        
        Object[][] data = new Object[ntask][6];
        int cont=0;
        for (int i = 0; i < sezioniVerifica.size(); i++) {
            Sezione sez = sezioniVerifica.get(i);
            String nomesez = sez.getName();
            for (int j = 0; j < combinazioni.size(); j++) {
                SezioneOutputTensioniFase so = sez.getTensioniTotali(j);
                SezioneMetallica sm = sez.getSezioniMetEfficaci().get(j);
                
                /*
                 *       String[] names = { "cond.", "σss (MPa)", "σii (MPa)", "σssred (MPa)", "σiired (MPa)",
        "VbSd (N)","VbwRd (N)","VbfRd (N)","VbRd TOT (N)", "coeff. verifica taglio", "coeff. verifica V+M+N" };
*/
                //calcolo sigma irrigidimenti trasv
                 Irrigidimento irrTrasv = sez.getIrrigidimetoTrasversale();
                 double passoTrasv = sez.getPassoIrrigidimentiTrasv();
                 double scr= irrTrasv.getSigCriticoTorsionale( mat, passoTrasv,sm.getTw());        
                 double[] sig_w = sm.getSigma_W_IrrigTrasversali(irrTrasv, passoTrasv);
                 double sc = 6*sig_w[0];

                
                data[cont][0]=new JLabel(nomesez);
                data[cont][1]=new JLabel(combinazioni.get(j).getName());
                data[cont][2]=new JLabel("σss (MPa)");
                data[cont][3]=new JLabel(nf.format(Math.abs(so.getSs())));
                data[cont][4]=new JLabel(nf.format(mat.getFy(sm.getTsup())));
                data[cont][5]=new JCheckBox("",Math.abs(so.getSs())<mat.getFy(sm.getTsup()));
//                data[cont][6]=new JButton("modifica");
                nomesez="";
                
                ++cont;
                data[cont][0]=new JLabel(nomesez);
                data[cont][1]=new JLabel("");
                data[cont][2]=new JLabel("σii (MPa)");
                data[cont][3]=new JLabel(nf.format(Math.abs(so.getIi())));
                data[cont][4]=new JLabel(nf.format(mat.getFy(sm.getTinf())));
                data[cont][5]=new JCheckBox("",Math.abs(so.getIi())<mat.getFy(sm.getTinf()));
//                data[cont][6]=new JButton("modifica");
                
                ++cont;
                double sid = Math.max(Math.abs(so.getSid1()),Math.abs(so.getSid2()));
                data[cont][0]=new JLabel(nomesez);
                data[cont][1]=new JLabel("");
                data[cont][2]=new JLabel("σid_max (MPA)");
                data[cont][3]=new JLabel(nf.format(sid));
                data[cont][4]=new JLabel(nf.format(mat.getFy(sm.getTw())));
                data[cont][5]=new JCheckBox("",sid <mat.getFy(sm.getTw()));
 //               data[cont][6]=new JButton("modifica");
                
                ++cont;
                data[cont][0]=new JLabel(nomesez);
                data[cont][1]=new JLabel("");
                data[cont][2]=new JLabel("σssred (MPa)");
                data[cont][3]=new JLabel(nf.format(sm.getSEff()));
                data[cont][4]=new JLabel(nf.format(mat.getFy(sm.getTsup())));
                 data[cont][5]=new JCheckBox("",Math.abs(sm.getSEff())<mat.getFy(sm.getTsup()));

                 ++cont;
                data[cont][0]=new JLabel(nomesez);
                data[cont][1]=new JLabel("");
                data[cont][2]=new JLabel("σiired (MPa)");
                data[cont][3]=new JLabel(nf.format(sm.getIEff()));
                data[cont][4]=new JLabel(nf.format(mat.getFy(sm.getTinf())));
                data[cont][5]=new JCheckBox("",Math.abs(sm.getIEff())<mat.getFy(sm.getTinf()));

                 ++cont;
                data[cont][0]=new JLabel(nomesez);
                data[cont][1]=new JLabel("");
                data[cont][2]=new JLabel("coeff. verifica taglio");
                data[cont][3]=new JLabel(nf.format(sm.getNu3()));
                data[cont][4]=new JLabel("1.0");
                data[cont][5]=new JCheckBox("",sm.getNu3()<1.00);

                 ++cont;
                data[cont][0]=new JLabel(nomesez);
                data[cont][1]=new JLabel("");
                data[cont][2]=new JLabel("coeff. verifica N-V");
                data[cont][3]=new JLabel(nf.format(sm.getNuInterM_V()));
                data[cont][4]=new JLabel("1.0");
                data[cont][5]=new JCheckBox("",sm.getNuInterM_V()<1.00);

                ++cont;
                data[cont][0]=new JLabel(nomesez);
                data[cont][1]=new JLabel("");
                data[cont][2]=new JLabel("σ cr irrigid. trasversali(Mpa)");
                data[cont][3]=new JLabel(nf.format(scr));
                data[cont][4]=new JLabel(nf.format(sc));
                data[cont][5]=new JCheckBox("",scr>sc);
                
                 ++cont;
                double[] Ist = sm.getIst_Istmin_irrigTrasversale(irrTrasv, passoTrasv, mat);
                data[cont][0]=new JLabel(nomesez);
                data[cont][1]=new JLabel("");
                data[cont][2]=new JLabel("Ist rigid. trasversali(mm4)");
                data[cont][3]=new JLabel(nf.format(Ist[0]));
                data[cont][4]=new JLabel(nf.format(Ist[1]));
                data[cont][5]=new JCheckBox("",Ist[0]>Ist[1]);
                
                 ++cont;
                double fy = mat.getFy(0)/mat.getGamma1();
//                double[] sig_w = sm.getSigma_W_IrrigTrasversali(irrTrasv, passoTrasv);
                data[cont][0]=new JLabel(nomesez);
                data[cont][1]=new JLabel("");
                data[cont][2]=new JLabel("σ max irrigidimenti trasversali (MPa)");
                data[cont][3]=new JLabel(nf.format(sig_w[0]));
                data[cont][4]=new JLabel(nf.format(fy));
                data[cont][5]=new JCheckBox("",fy>sig_w[0]);
                
                 ++cont;
                data[cont][0]=new JLabel(nomesez);
                data[cont][1]=new JLabel("");
                data[cont][2]=new JLabel("W0 freccia irrigidimenti trasversali (MPa)");
                data[cont][3]=new JLabel(nf.format(sig_w[1]));
                data[cont][4]=new JLabel(nf.format(sig_w[2]));
                data[cont][5]=new JCheckBox("",sig_w[2]>sig_w[1]);
                
         
                ++cont;
                
            }
        }
        
        
        
        return data;
    }
    
    
    public void actionPerformed(ActionEvent e) {
        
        Object s = e.getSource();
        if(s.getClass()== JButton.class){
            JButton jb = (JButton)s;
            if(jb.getActionCommand()=="Ricalcola"){
                try {
                    this.setVisible(false);
                    Progetto.getInstance().elabora();
//			DefaultTableModel fm=new DefaultTableModel(getDatiVerifica(),titolo);
//			table.setModel(fm);
                    table.setModel(new MyTableModel());
                    MyTableModel model = (MyTableModel)table.getModel();
                    model.fireTableDataChanged();

                    progressBar.setMaximum(table.getRowCount());
                    progressBar.setValue(model.getVerificati());
                    
                    this.setVisible(true);
                    
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                
            }
        }
        
    }
    
    
    class MyTableModel extends AbstractTableModel {
        
        private String[] columnNames = {"SEZIONE","Combinazione",
        "Tipo verifica",
        "Valore di calcolo",
        "Valore limite di progetto",
        "Verificato"};
        private Object[][] data;
        
        public MyTableModel(){
            data = getDatiVerifica();
        }
        
        public int getColumnCount() {
            return columnNames.length;
        }
        
        public int getRowCount() {
            return data.length;
        }
        
        public String getColumnName(int col) {
            return columnNames[col];
        }
        
        
        public Object getValueAt(int row, int col) {
            return data[row][col];
        }
        
        /*
         * JTable uses this method to determine the default renderer/
         * editor for each cell.  If we didn't implement this method,
         * then the last column would contain text ("true"/"false"),
         * rather than a check box.
         */
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }
        
        /*
         * Don't need to implement this method unless your table's
         * editable.
         */
        public boolean isCellEditable(int row, int col) {
            //Note that the data/cell address is constant,
            //no matter where the cell appears onscreen.
            return false;
        }
        
        /*
         * Don't need to implement this method unless your table's
         * data can change.
         */
        public void setValueAt(Object value, int row, int col) {
            
            data[row][col] = value;
            fireTableCellUpdated(row, col);
        }
        
        public int getVerificati() {
            
            int ncont = 0;
            for (int i = 0; i < data.length; i++) {
                JCheckBox ck = (JCheckBox)data[i][5];
                if(ck.isSelected())
                    ++ncont;
            }
            return ncont;
        }
        
        
        private void printDebugData() {
            int numRows = getRowCount();
            int numCols = getColumnCount();
            
        }
    }
    
    static class DateRenderer extends DefaultTableCellRenderer {
        DateFormat formatter;
        String scur;
        int ncol;
        Color[] cl;
        
        
        public DateRenderer() {
            super();
            cl = new Color[2];
            cl[0]=new Color(255,255,255);
            cl[1]=new Color(255,255,100);
            ncol=0;
            scur ="";
            
        }
        
        public void setValue(Object value) {
            if (formatter==null) {
                formatter = DateFormat.getDateInstance();
            }
            setText((value == null) ? "" : formatter.format(value));
        }
        
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int col) {
            
            
            boolean verificato = ((JCheckBox)table.getValueAt(row,5)).isSelected();
            String sez = ((JLabel)table.getValueAt(row,0)).getText();
            
            if(sez!= "")
                ncol=1;
            else
                ncol=0;
            
            if(value.getClass()==JLabel.class){
                
                String label = ((JLabel)table.getValueAt(row,col)).getText();
                JLabel testLabel = new JLabel(label, SwingConstants.CENTER);
                testLabel.setOpaque(true);
                testLabel.setBackground(cl[ncol]);
                if(!verificato){
                    testLabel.setForeground(Color.RED);
                }
                return testLabel;
                
            }else if (value.getClass()==JButton.class){
                if(!verificato){
                    JButton testb= new JButton("Modifica");
                    testb.setOpaque(true);
                    testb.setBackground(cl[ncol]);
                    return testb;
                }else {
                    JLabel lbl = new JLabel("ok");
                    lbl.setOpaque(true);
                    lbl.setBackground(cl[ncol]);
                    return lbl;
                }
            } else if (value.getClass()==JCheckBox.class){
                JCheckBox testb= new JCheckBox("",verificato);
                testb.setOpaque(true);
                testb.setBackground(cl[ncol]);
                return testb;
            }
            
            return null;
        }
        
        
    }
    
    
    
}




