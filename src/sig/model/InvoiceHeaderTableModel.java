
package sig.model;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

public class InvoiceHeaderTableModel extends AbstractTableModel {
    private ArrayList<InvoiceHeader> header;
    private String headerColumns[] = {"Number", "Date", "Customer", "Total"};
    
    
    public InvoiceHeaderTableModel(ArrayList<InvoiceHeader> invoices) {
        this.header = invoices;
    }
    
    
    @Override
    public int getRowCount() {
        return header.size();
    }

    
    @Override
    public int getColumnCount() {
        return headerColumns.length;
    }

    
    @Override
    public String getColumnName(int column) {
        return headerColumns[column];
    }
    
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        InvoiceHeader invoice = header.get(rowIndex);
        
        switch (columnIndex) {
            case 0: return invoice.getNum();
            case 1: return invoice.getDate();
            case 2: return invoice.getCustomer();
            case 3: return invoice.getInvoiceTotal();
            default : return "";
        }
    }
}
