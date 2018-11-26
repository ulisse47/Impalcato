/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cassone.util.dialog;

import cassone.model.Progetto;
import cassone.model.Sezione;
import java.awt.Color;
import java.awt.Component;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Marco
 */
public class DlgRiepilogoSezioni extends javax.swing.JDialog {

    /**
     * Creates new form DlgRiepilogoSezioni
     */
    public DlgRiepilogoSezioni(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        // jtTabella = new javax.swing.JTable(new MyTableModel());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jtTabella = new JTable(new MyTableModel());

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jtTabella.setModel(new MyTableModel());
        jScrollPane1.setViewportView(jtTabella);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 572, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 345, Short.MAX_VALUE)
                .addGap(40, 40, 40))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public Object[][] getDatiRiepilogo() {
        Progetto prg = Progetto.getInstance();

        ArrayList<Sezione> sezioniVerifica = prg.getSezioniVerifica();
        ArrayList<Sezione> sezioniAnalisiGlobale = prg.getSezioniAnalisiGlobale();

//        MaterialeAcciaio mat = prg.getMateriale();
        NumberFormat nf = NumberFormat.getInstance();
        NumberFormat nf2 = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        nf2.setMaximumFractionDigits(2);

        int ntask = sezioniVerifica.size() + sezioniAnalisiGlobale.size();

        Object[][] data = new Object[ntask][4];

        for (int i = 0; i < sezioniAnalisiGlobale.size(); i++) {
            Sezione sA = sezioniAnalisiGlobale.get(i);

            data[i][0] = sA.getName();
            data[i][1] = "Analisi";

            JComboBox jcSoletta = new JComboBox();
            jcSoletta.setModel(new DefaultComboBoxModel(prg.getSolette().toArray()));
            jcSoletta.setSelectedItem(sA.getSoletta());
            data[i][2] = jcSoletta;

            JComboBox jcConcio = new JComboBox();
            jcConcio.setModel(new DefaultComboBoxModel(prg.getConci().toArray()));
            jcConcio.setSelectedItem(sA.getSezioneMetallica());
            data[i][3] = jcConcio;
        }
        
        int nA = sezioniAnalisiGlobale.size();
        for (int i = 0; i < sezioniVerifica.size(); i++) {
            Sezione sA = sezioniVerifica.get(i);

            data[i+nA][0] = sA.getName();
            data[i+nA][1] = "Verifica";

            JComboBox jcSoletta = new JComboBox();
            jcSoletta.setModel(new DefaultComboBoxModel(prg.getSolette().toArray()));
            jcSoletta.setSelectedItem(sA.getSoletta());
            data[i+nA][2] = jcSoletta;

            JComboBox jcConcio = new JComboBox();
            jcConcio.setModel(new DefaultComboBoxModel(prg.getConci().toArray()));
            jcConcio.setSelectedItem(sA.getSezioneMetallica());
            data[i+nA][3] = jcConcio;
        }

        return data;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(DlgRiepilogoSezioni.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DlgRiepilogoSezioni.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DlgRiepilogoSezioni.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DlgRiepilogoSezioni.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                DlgRiepilogoSezioni dialog = new DlgRiepilogoSezioni(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jtTabella;
    // End of variables declaration//GEN-END:variables

    class MyTableModel extends AbstractTableModel {

        private String[] columnNames = {"SEZIONE", "TIPO",
            "SOLETTA",
            "CONCIO"};
        private Object[][] data;

        public MyTableModel() {
            data = getDatiRiepilogo();
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
                JCheckBox ck = (JCheckBox) data[i][5];
                if (ck.isSelected()) {
                    ++ncont;
                }
            }
            return ncont;
        }

        /*    private void printDebugData() {
            int numRows = getRowCount();
            int numCols = getColumnCount();
            
        }*/
    }

    static class DateRenderer extends DefaultTableCellRenderer {

        DateFormat formatter;
        String scur;
        int ncol;
        Color[] cl;

        public DateRenderer() {
            super();
            cl = new Color[2];
            cl[0] = new Color(255, 255, 255);
            cl[1] = new Color(255, 255, 100);
            ncol = 0;
            scur = "";

        }

        public void setValue(Object value) {
            if (formatter == null) {
                formatter = DateFormat.getDateInstance();
            }
            setText((value == null) ? "" : formatter.format(value));
        }

        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int col) {

            boolean verificato = ((JCheckBox) table.getValueAt(row, 5)).isSelected();
            String sez = ((JLabel) table.getValueAt(row, 0)).getText();

            if (sez != "") {
                ncol = 1;
            } else {
                ncol = 0;
            }

            if (value.getClass() == JLabel.class) {

                String label = ((JLabel) table.getValueAt(row, col)).getText();
                JLabel testLabel = new JLabel(label, SwingConstants.CENTER);
                testLabel.setOpaque(true);
                testLabel.setBackground(cl[ncol]);
                if (!verificato) {
                    testLabel.setForeground(Color.RED);
                }
                return testLabel;

            } else if (value.getClass() == JButton.class) {
                if (!verificato) {
                    JButton testb = new JButton("Modifica");
                    testb.setOpaque(true);
                    testb.setBackground(cl[ncol]);
                    return testb;
                } else {
                    JLabel lbl = new JLabel("ok");
                    lbl.setOpaque(true);
                    lbl.setBackground(cl[ncol]);
                    return lbl;
                }
            } else if (value.getClass() == JCheckBox.class) {
                JCheckBox testb = new JCheckBox("", verificato);
                testb.setOpaque(true);
                testb.setBackground(cl[ncol]);
                return testb;
            }

            return null;
        }

    }
}
