package Controller;

import org.example.View;
import Model.InvoiceHeader;
import Model.InvoiceLine;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Controller implements ActionListener {
    InvoiceHeader invoiceHeader;
    InvoiceLine invoiceLine;
    private static String selectedRowsFromHeader = "";
    View view;
    private static String status;
    private static int invoiceNumberForNewInv;
    boolean createNewInvoice = false ;
    public Controller() {}

    public Controller(View view) {
        this.view = view;
        initializeController();
    }

    public void initializeController(){
        view.getInvoiceTbl().addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JTable table = (JTable) e.getSource();
                if(e.getClickCount() == 1 && table.getSelectedRow() != -1){
                    //loading Data
                    getSelectedDataOfLineTabel(Integer.parseInt(table.getValueAt(table.getSelectedRow(),0).toString()));
                    view.getInvoiceNumValueLabel().setText(table.getValueAt(table.getSelectedRow(), 0).toString());
                    view.getInvoiceDateTF().setText(table.getValueAt(table.getSelectedRow(), 1).toString());
                    view.getCustomerNameTF().setText(table.getValueAt(table.getSelectedRow(), 2).toString());
                    view.getinvoiceTotalValueLabel().setText(table.getValueAt(table.getSelectedRow(), 3).toString());

                    view.getCustomerNameTF().setEditable(true);
                    view.getInvoiceDateTF().setEditable(true);
                }

            }
            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        invoiceHeader = new InvoiceHeader();
        invoiceLine = new InvoiceLine();

        ((DefaultTableModel)view.getInvItemsTbl().getModel()).addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                ArrayList<InvoiceLine> arrayList = new ArrayList<>();
                String itemName=null; double itemPrice=0; int count =0;
                JTable table = view.getInvItemsTbl();
                if(table.getSelectedRow() != -1 && e.getColumn() !=4 && e.getColumn() !=-1){

                    for (int x = 0; x < table.getRowCount(); x++) {
                        //skip row has null cells
                        if (table.getValueAt(x, 1) == null
                                || table.getValueAt(x, 2) == null
                                || table.getValueAt(x, 3) == null) {
                            break;
                        }
                        itemPrice = Double.parseDouble(table.getValueAt(x, 2).toString());
                        count = Integer.parseInt(table.getValueAt(x, 3).toString());
                        invoiceLine.setItemTotal(0, itemPrice, count); //
                        table.setValueAt(invoiceLine.getItemTotal(), x, 4);
                    }

                    if (table.getRowCount() > 0
                            && table.getValueAt(table.getRowCount() - 1, 1) != null
                            && table.getValueAt(table.getRowCount() - 1, 2) != null
                            && table.getValueAt(table.getRowCount() - 1, 3) != null) {
                        Object[] objects = {view.getInvoiceNumValueLabel().getText(),null,null,null,"0.0"}; //
                        ((DefaultTableModel)view.getItemTbl().getModel()).addRow(objects); //invoice Number"no" and totalItem is uneditable"user can not insert data"
                    }
                }
            }
        });

        view.getItemTbl().addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JTable table = view.getInvItemsTbl();
                if (table.getRowCount() > 0
                        && table.getValueAt(table.getRowCount() - 1, 1) != null
                        && table.getValueAt(table.getRowCount() - 1, 2) != null
                        && table.getValueAt(table.getRowCount() - 1, 3) != null) {


                    Object[] objects = {view.getInvoiceNumValueLabel().getText(), null, null, null, "0.0"};
                    ((DefaultTableModel) view.getItemTbl().getModel()).addRow(objects); //invoice Number"no" and totalItem is uneditable"user can not insert data"
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }

    protected void getDataOfInvoiceTabel() throws IOException {
        ArrayList<InvoiceHeader> arrayList = new ArrayList<>();
        arrayList = invoiceHeader.readFile();
        DefaultTableModel model = (DefaultTableModel)view.getInvoiceTbl().getModel();
        model.setRowCount(0);   //clear the table from the current rows

        Object[] objects;
        for (int x = 0; x < arrayList.size(); x++) {
            objects = new Object[4];
            objects[0] = arrayList.get(x).getInvoiceNum();
            objects[1] = arrayList.get(x).getInvoiceDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            objects[2] = arrayList.get(x).getCustomerName();
            objects[3] = arrayList.get(x).getTotal();
            model.addRow(objects);
        }
    }

    public void getSelectedDataOfLineTabel(int invoiceNumber) {
        ArrayList<InvoiceLine> arrayList = new ArrayList<>();
        arrayList = invoiceLine.readFile();
        DefaultTableModel model = (DefaultTableModel) view.getInvItemsTbl().getModel();
        model.setRowCount(0);   //clear the table from the current rows
        Object[] objects;

        for (int x = 0; x < arrayList.size(); x++) {
            if (arrayList.get(x).getInvoiceNum() == invoiceNumber) {
                objects = new Object[5];
                objects[0] = arrayList.get(x).getInvoiceNum();
                objects[1] = arrayList.get(x).getItemName();
                objects[2] = arrayList.get(x).getItemPrice();
                objects[3] = arrayList.get(x).getCount();
                objects[4] = arrayList.get(x).getItemTotal();
                model.addRow(objects);
            }
        }
    }

    public void getDataOfLineTabel(){
        ArrayList<InvoiceLine> arrayList = new ArrayList<>();
        arrayList = invoiceLine.readFile();
        DefaultTableModel model = (DefaultTableModel)view.getInvItemsTbl().getModel();

        Object[] objects;
        for(int x=0; x<arrayList.size(); x++) {
            objects = new Object[5];
            objects[0] = arrayList.get(x).getInvoiceNum();
            objects[1] = arrayList.get(x).getItemName();
            objects[2] = arrayList.get(x).getItemPrice();
            objects[3] = arrayList.get(x).getCount();
            objects[4] = arrayList.get(x).getItemTotal();
            model.addRow(objects);
        }
    }

    protected void deleteHeaderSelectedRow() throws IOException {
        ArrayList<InvoiceHeader> arrayList;
        arrayList = invoiceHeader.readFile();
        int rowIndex = 0;
        for (int x = 0; x < arrayList.size(); x++) {
            if (arrayList.get(x).getInvoiceNum() == Integer.parseInt(view.getInvoiceNumValueLabel().getText())){
                rowIndex = x;
                break;
            }
        }

        removeRowFromInvoiceHeader(Integer.parseInt(view.getInvoiceTbl().getValueAt(rowIndex,0).toString()));
        deleteLineRelatedRows(Integer.parseInt(view.getInvoiceTbl().getValueAt(rowIndex,0).toString()));
        getDataOfInvoiceTabel();
        if(view.getInvoiceTbl().getModel().getRowCount() != 0) {
            getSelectedDataOfLineTabel(Integer.parseInt(view.getInvoiceTbl().getValueAt(0,0).toString()));
            view.getInvoiceNumValueLabel().setText(view.getInvoiceTbl().getValueAt(0, 0).toString());
            view.getInvoiceDateTF().setText(view.getInvoiceTbl().getValueAt(0, 1).toString());
            view.getCustomerNameTF().setText(view.getInvoiceTbl().getValueAt(0, 2).toString());
            view.getinvoiceTotalValueLabel().setText(view.getInvoiceTbl().getValueAt(0, 3).toString());
        }else{
            ((DefaultTableModel)view.getItemTbl().getModel()).setRowCount(0);
            ((DefaultTableModel)view.getItemTbl().getModel()).fireTableDataChanged();
        }

    }
    protected void removeRowFromInvoiceHeader(int invoiceNumber) throws IOException {
        ArrayList<InvoiceHeader> arrayList = new ArrayList<>();
        arrayList = invoiceHeader.readFile();

        for (int x = 0; x < arrayList.size(); x++) {
            if (arrayList.get(x).getInvoiceNum() == invoiceNumber){
                arrayList.remove(x);
                break;
            }
        }
        invoiceHeader.writeFile(arrayList , false); //in this case it is delete so false is passed
    }
    protected void deleteLineRelatedRows(int invoiceNumber) throws IOException {
        removeRowFromInvoiceLine(invoiceNumber);
    }
    protected void removeRowFromInvoiceLine(int invoiceNumber) throws IOException {
        ArrayList<InvoiceLine> arrayListToCheck = new ArrayList<>();
        arrayListToCheck = invoiceLine.readFile();

        ArrayList<InvoiceLine> arrayList = new ArrayList<>();
        int invoiceNum= 0, count=0 ;
        String itemName="";
        double itemPrice=0, itemTotal=0;
        for (int x = 0; x < arrayListToCheck.size(); x++){
            if (arrayListToCheck.get(x).getInvoiceNum() != invoiceNumber){
                invoiceNum = arrayListToCheck.get(x).getInvoiceNum();
                itemName = arrayListToCheck.get(x).getItemName();
                itemPrice = arrayListToCheck.get(x).getItemPrice();
                count = arrayListToCheck.get(x).getCount();
                itemTotal = arrayListToCheck.get(x).getItemTotal();
                arrayList.add(new InvoiceLine(invoiceNum, itemName, itemPrice, count, itemTotal));
            }
        }
        invoiceLine.writeInvoiceLineFile(arrayList , false); //in this case it is delete so false is passed
    }

    protected void createNewInvoice(){
        createNewInvoice = true;
        //Set Default for Customer name and Date
        view.getInvoiceDateTF().setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        view.getCustomerNameTF().setText("Customer Name");
        //First Clear both of the two tables
        DefaultTableModel modelItem = (DefaultTableModel)view.getInvItemsTbl().getModel();
        DefaultTableModel modelHeader = (DefaultTableModel)view.getInvoiceTbl().getModel();
        modelItem.setRowCount(0);
        modelHeader.setRowCount(0);

        //Add new row in Items table
        Object[] objects = {null,null,null,null,null};
        modelItem.addRow(objects); //invoice Number"no" and totalItem is uneditable"user can not insert data"
        modelItem.setValueAt((Object) invoiceNumberForNewInv, 0,0);
        modelItem.setValueAt(0.0, 0,4);
    }

    protected void saveData() throws Exception{
        //saving invoice items
        double invoiceTotal = 0;
        int invoiceNum = Integer.parseInt(view.getInvoiceNumValueLabel().getText());
        ArrayList<InvoiceLine> itemsArrayListInitial = invoiceLine.readFile();  //load the whole file to sort the array before writing it
        ArrayList<InvoiceLine> itemsArrayList = new ArrayList<>();
        for (int x = 0; x < itemsArrayListInitial.size(); x++){
            if(itemsArrayListInitial.get(x).getInvoiceNum() != invoiceNum){
                itemsArrayList.add(itemsArrayListInitial.get(x));
            }
        }
        String itemName = null; double itemPrice = 0; int count = 0;
        JTable itemstbl = view.getInvItemsTbl();
        DefaultTableModel itemsTblModel = (DefaultTableModel) view.getInvItemsTbl().getModel();
        for (int x = 0; x < itemstbl.getRowCount(); x++){
            //skip row has null cells
            if (itemstbl.getValueAt( x, 1) == null
                    || itemstbl.getValueAt( x, 2) == null
                    || itemstbl.getValueAt( x, 3) == null){
                break;
            }

            itemName = itemstbl.getValueAt(x, 1).toString();
            itemPrice = Double.parseDouble(itemstbl.getValueAt(x, 2).toString());
            count = Integer.parseInt(itemstbl.getValueAt(x, 3).toString());
            invoiceLine.setItemTotal(0, itemPrice, count); //
            itemsArrayList.add(new InvoiceLine(invoiceNum, itemName, itemPrice, count, invoiceLine.getItemTotal()));
            invoiceTotal += invoiceLine.getItemTotal();
        }

        //saving invoice header
        ArrayList<InvoiceHeader> headerArrayList = new ArrayList<>();
        ArrayList<InvoiceHeader> headerArrayListInitial = invoiceHeader.readFile();  //load the whole file to sort the array before writing it
        for (int x = 0; x < headerArrayListInitial.size(); x++){
            if(headerArrayListInitial.get(x).getInvoiceNum() != invoiceNum){
                headerArrayList.add(headerArrayListInitial.get(x));
            }
        }

        //System.out.println("Date " +view.getInvoiceDateTF().getText());
        headerArrayList.add(new InvoiceHeader(invoiceNum,
                LocalDate.parse(view.getInvoiceDateTF().getText(), DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                view.getCustomerNameTF().getText(),
                invoiceTotal));

        if(createNewInvoice){

        }
        else {
            deleteHeaderSelectedRow();
        }

        System.out.println("");
        //Sorting ArrayLists before saving them
        Collections.sort(headerArrayList, new Comparator<InvoiceHeader>(){
            public int compare(InvoiceHeader o1, InvoiceHeader o2){
                if(o1.getInvoiceNum() == o2.getInvoiceNum())
                    return 0;
                return o1.getInvoiceNum() < o2.getInvoiceNum() ? -1 : 1;
            }
        });
        Collections.sort(itemsArrayList, new Comparator<InvoiceLine>(){
            public int compare(InvoiceLine o1, InvoiceLine o2){
                if(o1.getInvoiceNum() == o2.getInvoiceNum())
                    return 0;
                return o1.getInvoiceNum() < o2.getInvoiceNum() ? -1 : 1;
            }
        });
        System.out.println("");
        invoiceLine.writeInvoiceLineFile(itemsArrayList, false);
        invoiceHeader.writeFile(headerArrayList, false);

        reloadInitialValues();
    }

    protected void reloadInitialValues() throws Exception{
        getDataOfInvoiceTabel();

        view.getInvoiceTbl().setRowSelectionInterval(0, 0);
        getSelectedDataOfLineTabel(Integer.parseInt(view.getInvoiceTbl().getValueAt(view.getInvoiceTbl().getSelectedRow(),0).toString()));

        createNewInvoice = false;
        view.getInvoiceNumValueLabel().setText(view.getInvoiceTbl().getValueAt(view.getInvoiceTbl().getSelectedRow(), 0).toString());
        view.getInvoiceDateTF().setText(view.getInvoiceTbl().getValueAt(view.getInvoiceTbl().getSelectedRow(), 1).toString());
        view.getCustomerNameTF().setText(view.getInvoiceTbl().getValueAt(view.getInvoiceTbl().getSelectedRow(), 2).toString());
        view.getinvoiceTotalValueLabel().setText(view.getInvoiceTbl().getValueAt(view.getInvoiceTbl().getSelectedRow(), 3).toString());
    }

    public static String getStatus() {
        return status;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        switch (actionEvent.getActionCommand()){
            case "loadFile" :
                try {
                    getDataOfInvoiceTabel();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                status = "Loaded";
                break;

            case "saveFile" :
                view.getStautsPaneMessage("File is "+getStatus()+" .");
                break;

            case "createNewInvoiceBtn" :
                try {
                    invoiceHeader.setInvoiceNum(0);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                invoiceNumberForNewInv = invoiceHeader.getInvoiceNum();
                view.getInvoiceNumValueLabel().setText(String.valueOf(invoiceNumberForNewInv));
                view.getInvoiceDateTF().setEditable(true);
                view.getCustomerNameTF().setEditable(true);
                view.getinvoiceTotalValueLabel().setText("0.0");
                createNewInvoice();
                status = "Created";
                break;

            case "customerNameTF" :
                break;

            case "deleteInvoice_btn" :
                try {
                    deleteHeaderSelectedRow();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                status = "Deleted";
                break;

            case "save_btn" :
                try {
                    saveData();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                status = "Saved";
                break;

            case "cancel_btn":
                try {
                    System.out.println("Canceled");
                    reloadInitialValues();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                status = "Canceled";
                break;

        }
    }
}
