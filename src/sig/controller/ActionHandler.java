package sig.controller;

import sig.model.InvoiceHeader;
import sig.model.InvoiceHeaderTableModel;
import sig.model.InvoiceLine;
import sig.model.InvoiceLineTableModel;
import java.util.ArrayList;
import sig.view.InvoiceFrame;
import sig.view.HeaderDialog;
import sig.view.LineDialog;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import java.text.ParseException;


public class ActionHandler implements ActionListener, ListSelectionListener {

    private InvoiceFrame frame;
    private HeaderDialog headerDialog;
    private LineDialog lineDialog;

    public ActionHandler(InvoiceFrame frame) {
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();
        System.out.println("Action: " + actionCommand);
        switch (actionCommand) {
            case "Load File":
                loadFile();
                break;
            case "Save File":
                saveFile();
                break;
            case "Create New Invoice":
                createNewInvoice();
                break;
            case "Delete Invoice":
                deleteInvoice();
                break;
            case "Create New Item":
                createNewItem();
                break;
            case "Delete Item":
                deleteItem();
                break;
            case "createInvoiceOK":
                createInvoiceOK();
                break;

            case "createInvoiceCancel":
                createInvoiceCancel();
                break;
            case "createLineOK":
                createLineOK();
                break;
            case "createLineCancel":
                createLineCancel();
                break;
        }
    }

    
    @Override
    public void valueChanged(ListSelectionEvent e) {
        int selected = frame.getInvoiceTable().getSelectedRow();
        if (selected != -1) {
            System.out.println("You have selected row: " + selected);
            InvoiceHeader currentInvoice = frame.getInvoices().get(selected);
            frame.getInvoiceNumLabel().setText("" + currentInvoice.getNum());
            frame.getCustomerNameLabel().setText(currentInvoice.getCustomer());
            frame.getInvoiceDateLabel().setText(currentInvoice.getDate());
            frame.getInvoiceTotalLabel().setText("" + currentInvoice.getInvoiceTotal());
            InvoiceLineTableModel lineTableModel = new InvoiceLineTableModel(currentInvoice.getLines());
            frame.getLineTable().setModel(lineTableModel);
            lineTableModel.fireTableDataChanged();
        }
    }

    
    private void loadFile() {
        JFileChooser chooser = new JFileChooser();
        try {
            int r = chooser.showOpenDialog(frame);
            if (r == JFileChooser.APPROVE_OPTION) {
                File headerFile = chooser.getSelectedFile();
                Path headerPath = Paths.get(headerFile.getAbsolutePath());
                List<String> headerLines = Files.readAllLines(headerPath);
            
                ArrayList<InvoiceHeader> invoiceArray = new ArrayList<>();
                for (String headerLine : headerLines) {
                    try {
                        String headers[] = headerLine.split(",");
                        int invoiceNumber = Integer.parseInt(headers[0]);
                        String invoiceDate = headers[1];
                        String customerName = headers[2];

                        InvoiceHeader header = new InvoiceHeader(invoiceNumber, invoiceDate, customerName);
                        invoiceArray.add(header);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(frame, "Error in line format", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                
                r = chooser.showOpenDialog(frame);
                if (r == JFileChooser.APPROVE_OPTION) {
                    File lineFile = chooser.getSelectedFile();
                    Path linePath = Paths.get(lineFile.getAbsolutePath());
                    List<String> lineLines = Files.readAllLines(linePath);
                    System.out.println("Lines have been read");
                    for (String lineLine : lineLines) {
                        try {
                            String lines[] = lineLine.split(",");
                            int invoiceNum = Integer.parseInt(lines[0]);
                            String itemName = lines[1];
                            double itemPrice = Double.parseDouble(lines[2]);
                            int count = Integer.parseInt(lines[3]);
                            InvoiceHeader inv = null;
                            for (InvoiceHeader invoice : invoiceArray) {
                                if (invoice.getNum() == invoiceNum) {
                                    inv = invoice;
                                    break;
                                }
                            }

                            InvoiceLine line = new InvoiceLine(itemName, itemPrice, count, inv);
                            inv.getLines().add(line);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(frame, "Error in line format", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
                
                frame.setInvoices(invoiceArray);
                InvoiceHeaderTableModel invoicesTableModel = new InvoiceHeaderTableModel(invoiceArray);
                frame.setInvoicesTableModel(invoicesTableModel);
                frame.getInvoiceTable().setModel(invoicesTableModel);
                frame.getInvoicesTableModel().fireTableDataChanged();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Cannot read file", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    
    private void saveFile() {
        ArrayList<InvoiceHeader> header = frame.getInvoices();
        String headers = "";
        String lines = "";
        for (InvoiceHeader invoice : header) {
            String invCSV = invoice.getAsCSV();
            headers += invCSV;
            headers += "\n";
            for (InvoiceLine line : invoice.getLines()) {
                String lineCSV = line.getAsCSV();
                lines += lineCSV;
                lines += "\n";
            }
        }
        try {
            JFileChooser chooser = new JFileChooser();
            int result = chooser.showSaveDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                File headerFile = chooser.getSelectedFile();
                FileWriter headerWriter = new FileWriter(headerFile);
                headerWriter.write(headers);
                headerWriter.flush();
                headerWriter.close();
                result = chooser.showSaveDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File lineFile = chooser.getSelectedFile();
                    FileWriter lineWriter = new FileWriter(lineFile);
                    lineWriter.write(lines);
                    lineWriter.flush();
                    lineWriter.close();
                }
            }
        } catch (Exception ex) {

        }
    }

    
    private void createNewInvoice() {
        headerDialog = new HeaderDialog(frame);
        headerDialog.setVisible(true);
    }

    
    private void deleteInvoice() {
        int selectedRow = frame.getInvoiceTable().getSelectedRow();
        if (selectedRow!=-1) {
            frame.getInvoices().remove(selectedRow);
            frame.getInvoicesTableModel().fireTableDataChanged();
        }
    }

    
    private void createNewItem() {
        lineDialog = new LineDialog(frame);
        lineDialog.setVisible(true);
    }

    
    private void deleteItem() {
        int selectedRow = frame.getLineTable().getSelectedRow();

        if (selectedRow!=-1) {
            InvoiceLineTableModel lineTableModel = (InvoiceLineTableModel) frame.getLineTable().getModel();
            lineTableModel.getLines().remove(selectedRow);
            lineTableModel.fireTableDataChanged();
            frame.getInvoicesTableModel().fireTableDataChanged();
        }
    }
    
     private void createInvoiceOK() {
        String date = headerDialog.getInvDateField().getText();
        String customer = headerDialog.getCustNameField().getText();
        int num = frame.getNextInvoiceNum();
        try {
            String Parts[] = date.split("-"); 
            if (Parts.length < 3) {
                JOptionPane.showMessageDialog(frame, "Wrong date format", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                int day = Integer.parseInt(Parts[0]);
                int month = Integer.parseInt(Parts[1]);
                int year = Integer.parseInt(Parts[2]);
                if (day > 31 || month > 12) {
                    JOptionPane.showMessageDialog(frame, "Wrong date format", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    InvoiceHeader invoice = new InvoiceHeader(num, date, customer);
                    frame.getInvoices().add(invoice);
                    frame.getInvoicesTableModel().fireTableDataChanged();
                    headerDialog.setVisible(false);
                    headerDialog.dispose();
                    headerDialog = null;
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Wrong date format", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

     
    private void createInvoiceCancel() {
        headerDialog.setVisible(false);
        headerDialog.dispose();
        headerDialog = null;
    }

   

    private void createLineOK() {
        String item = lineDialog.getItemNameField().getText();
        String priceStr = lineDialog.getItemPriceField().getText();
        String countStr = lineDialog.getItemCountField().getText();
        int count = Integer.parseInt(countStr);
        double price = Double.parseDouble(priceStr);
        int selectedInvoice = frame.getInvoiceTable().getSelectedRow();
        if (selectedInvoice!=-1) {
            InvoiceHeader invoice = frame.getInvoices().get(selectedInvoice);
            InvoiceLine line = new InvoiceLine(item, price, count, invoice);
            invoice.getLines().add(line);
            InvoiceLineTableModel linesTableModel = (InvoiceLineTableModel) frame.getLineTable().getModel();
            linesTableModel.fireTableDataChanged();
            frame.getInvoicesTableModel().fireTableDataChanged();
        }
        lineDialog.setVisible(false);
        lineDialog.dispose();
        lineDialog = null;
    }

    
    private void createLineCancel() {
        lineDialog.setVisible(false);
        lineDialog.dispose();
        lineDialog = null;
    }

}
