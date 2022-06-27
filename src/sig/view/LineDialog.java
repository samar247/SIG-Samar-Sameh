
package sig.view;

import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JDialog;


public class LineDialog extends JDialog{
    private JTextField itemNameField;
    private JTextField itemPriceField;
    private JTextField itemCountField;
    private JLabel itemNameLbl;
    private JLabel itemPriceLbl;
    private JLabel itemCountLbl;
    private JButton okBtn;
    private JButton cancelBtn;
    
    public LineDialog(InvoiceFrame frame) {
        itemNameField = new JTextField(20);
        itemNameLbl = new JLabel("Item Name");
        
        itemPriceField = new JTextField(20);
        itemPriceLbl = new JLabel("Item Price");
        
        itemCountField = new JTextField(20);
        itemCountLbl = new JLabel("Item Count");
        
        okBtn = new JButton("OK");
        cancelBtn = new JButton("Cancel");
        
        okBtn.setActionCommand("createLineOK");
        cancelBtn.setActionCommand("createLineCancel");
        
        okBtn.addActionListener(frame.getController());
        cancelBtn.addActionListener(frame.getController());
        
        setLayout(new GridLayout(4, 2));
        
        add(itemNameLbl);
        add(itemNameField);
         add(itemPriceLbl);
        add(itemPriceField);
        add(itemCountLbl);
        add(itemCountField);
        add(okBtn);
        add(cancelBtn);
        
        pack();
    }

    
    public JTextField getItemNameField() {
        return itemNameField;
    }

    public JTextField getItemCountField() {
        return itemCountField;
    }

    public JTextField getItemPriceField() {
        return itemPriceField;
    }
}
