
package sig.model;

import java.util.ArrayList;

public class InvoiceHeader {
    private int number;
    private String date;
    private String customer;
    private ArrayList<InvoiceLine> lines;
    
    public InvoiceHeader() {
    }

    
    public InvoiceHeader(int number, String date, String customer) {
        this.number = number;
        this.date = date;
        this.customer = customer;
    }
    
    public ArrayList<InvoiceLine> getLines() {
        if (lines == null) {
            lines = new ArrayList<>();
        }
        return lines;
    }


    public double getInvoiceTotal() {
        double total=0.0;
        for (InvoiceLine line : getLines()) {
            total += line.getLineTotal();
        }
        return total;
    }
    
    
    public String getCustomer() {
        return customer;
    }

    
    public void setCustomer(String customer) {
        this.customer = customer;
    }

    
    public int getNum() {
        return number;
    }

    
    public void setNum(int number) {
        this.number = number;
    }

    
    public String getDate() {
        return date;
    }

    
    public void setDate(String date) {
        this.date = date;
    }

    
    @Override
    public String toString() {
        return "Invoice{" + "num=" + number + ", date=" + date + ", customer=" + customer + '}';
    }
    
    
    public String getAsCSV() {
        return number + "," + date + "," + customer;
    }
    
}
