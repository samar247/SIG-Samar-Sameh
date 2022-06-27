
package sig.view;

import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JDialog;


public class HeaderDialog extends JDialog {
    private JTextField custNameField;
    private JTextField invDateField;
    private JLabel custNameLbl;
    private JLabel invDateLbl;
    private JButton okBtn;
    private JButton cancelBtn;

    public HeaderDialog(InvoiceFrame frame) {
        custNameLbl = new JLabel("Customer Name:");
        custNameField = new JTextField(20);
        invDateLbl = new JLabel("Date:");
        invDateField = new JTextField(20);
        okBtn = new JButton("OK");
        cancelBtn = new JButton("Cancel");
        
        okBtn.setActionCommand("createInvoiceOK");
        cancelBtn.setActionCommand("createInvoiceCancel");
        
        okBtn.addActionListener(frame.getController());
        cancelBtn.addActionListener(frame.getController());
        setLayout(new GridLayout(3, 2));
        
        add(custNameLbl);
        add(custNameField);
        add(invDateLbl);
        add(invDateField);
        add(okBtn);
        add(cancelBtn);
        
        pack();
        
    }

    
    public JTextField getCustNameField() {
        return custNameField;
    }

    
    public JTextField getInvDateField() {
        return invDateField;
    }
    
}
