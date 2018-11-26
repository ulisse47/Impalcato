package cassone.inputPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Hashtable;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import cassone.view.analisi.MyTextField;

public class PannelloSolettaT extends JPanel {
    
    JLabel lB = new JLabel();
    
    public JTextField B = new MyTextField();
    
    public JTextField b0 = new MyTextField();
    
    JLabel lb0 = new JLabel();
    
    JLabel lH = new JLabel();
    
    public JTextField H = new MyTextField();
    
    JLabel lh = new JLabel();
    
    public JTextField h = new MyTextField();
    
    JLabel lb = new JLabel();
    
    public JTextField b = new MyTextField();
    
    TitledBorder titledBorderSezione;
    
    private static PannelloSolettaT inputPanel;
    
    private Hashtable listaTextField = new Hashtable();
    
    public PannelloSolettaT(){
        try {
            jbInit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    
    void jbInit() throws Exception {
        
        titledBorderSezione = new TitledBorder(new EtchedBorder(
                EtchedBorder.RAISED, Color.white, Color.gray), "SOLETTA A T");
        
        setLayout(new GridBagLayout());
        setBorder(titledBorderSezione);
        
        addComponent("B", "Larghezza collaborante soletta in cls: Beff (mm)",
                lB, B, 0, 1,58,21);
        
        addComponent("H", "Spessore soletta in cls: H (mm)", lH, H, 0, 2,58,21);
        
        addComponent("h", "Spessore soletta in cls: h (mm)", lh, h, 0, 3,58,21);
        
        addComponent("b", "Larghezza soletta in cls: b (mm)", lb, b, 0, 4,58,21);
        
        addComponent("b0", "Larghezza pioli: b0 (mm)", lb0, b0, 0, 5, 58,21);
        
        String imageNameB = "file:img/SDOPPIOT_B.JPG";
        B.setToolTipText("<html>SEZIONE A T <br><img src="+imageNameB+"></html>");
        
        String imageNameb = "file:img/SDOPPIOT_binf.JPG";
        b.setToolTipText("<html>SEZIONE A T <br><img src="+imageNameb+"></html>");
        
        String imageNameH = "file:img/SDOPPIOT_H.JPG";
        H.setToolTipText("<html>SEZIONE A T <br><img src="+imageNameH+"></html>");
        
        String imageNameh = "file:img/SDOPPIOT_hinf.JPG";
        h.setToolTipText("<html>SEZIONE A T <br><img src="+imageNameh+"></html>");
        
    }
    
    private void addComponent(String fieldName, String title, JLabel lBs1,
            JTextField bs1, int gridx, int gridy, int width, int height ) {
        
        listaTextField.put(fieldName, bs1);
        
        lBs1.setHorizontalAlignment(SwingConstants.LEFT);
        lBs1.setText(title);
        lBs1.setPreferredSize(new Dimension(230, 15));
        
        bs1.setPreferredSize( new Dimension( width , height ) );
        bs1.setHorizontalAlignment(SwingConstants.RIGHT);
        
        add(lBs1, new GridBagConstraints(gridx, gridy, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(
                0, 0, 0, 0), 9, 0));
        add(bs1, new GridBagConstraints(gridx + 1, gridy, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(
                0, 0, 0, 0), 0, 0));
    }
    
    
    /**
     * @return
     */
    public static synchronized PannelloSolettaT getInstance() {
        if (inputPanel == null) {
            inputPanel = new PannelloSolettaT();
        }
        return inputPanel;
    }
    
    public String getValue(String name) {
        JTextField field = (JTextField) listaTextField.get(name);
        
        if(name == "B" || name == "H" || name == "h" ||name == "b" ||name == "b0" ){
            try {
                Double.parseDouble(field.getText());
                field.setBackground(Color.WHITE);
            } catch (Exception e) {
                field.setBackground(Color.RED);
            }
        
        }
        return field.getText();
    }
    
    public void setValue(String name, String value) {
        JTextField field = (JTextField) listaTextField.get(name);
        field.setText(value);
    }
    
}
