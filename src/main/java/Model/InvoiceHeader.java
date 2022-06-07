package Model;

import com.opencsv.CSVWriter;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class InvoiceHeader {
    private int invoiceNum = 0;
    private LocalDate invoiceDate;
    private String customerName;
    private double total;
    private static boolean isFileRead = false;
    private static File fileHeader = null;
    private static boolean isFileWrite = false;
    private static File csv = null;
    private boolean isFileWritten = false;
    private ArrayList<InvoiceHeader> aL;

    private String[] headerInvTbl = {"No.", "Date", "Customer", "Total"};

    public InvoiceHeader() {}

    public InvoiceHeader(int invoiceNum, LocalDate invoiceDate, String customerName, double total) {
        this.invoiceNum = invoiceNum;
        this.invoiceDate = invoiceDate;
        this.customerName = customerName;
        this.total = total;
    }

    public int getInvoiceNum() {
        return invoiceNum;
    }

    public void setInvoiceNum(int invoiceNum) throws IOException {
        if(invoiceNum == 0){
            this.invoiceNum = countInvoiceNumber();
        }
    }

    private int countInvoiceNumber() throws IOException {
        ArrayList<InvoiceHeader> arrayList;
        InvoiceHeader invoiceHeader = new InvoiceHeader();
        arrayList = invoiceHeader.readFile();
        int max = arrayList.get(0).getInvoiceNum();

        if (arrayList.size() > 0){
            for (int i = 1; i < arrayList.size(); i++) {
                if (arrayList.get(i).getInvoiceNum() > max) {
                    max = arrayList.get(i).getInvoiceNum();
                }
            }
        }
        else{
            max = 0;
        }
        return (max+1);//max is the largest invoice number so we can add one to it to get new invoice number
    }
    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate =  invoiceDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) throws IOException {
        if(total == 0){
            this.total = calculateTotalCost();
        }
        else {
            this.total = total;
        }
    }
    private double calculateTotalCost() throws IOException {
        double totalCost=0;
        ArrayList<InvoiceHeader> arrayList = new InvoiceHeader().readFile();
        ArrayList<InvoiceLine> arrayListLine = new InvoiceLine().readFile();
        for (int i=0; i<arrayList.size() ; i++){
            for (int j=0; j<arrayListLine.size() ; j++) {
                if (arrayList.get(i).getInvoiceNum() == arrayListLine.get(j).getInvoiceNum()) {
                    totalCost += arrayListLine.get(j).getItemTotal();
                }
            }
        }
        return totalCost;
    }
    public String[] getHeaderInvTbl() {
        return headerInvTbl;
    }

    //Read Invoice Header
    public ArrayList<InvoiceHeader> readFile() throws IOException {
        if (isFileRead == false) {
            JFileChooser jFileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
            jFileChooser.setCurrentDirectory(new File(".").getAbsoluteFile());
            jFileChooser.setDialogTitle("Select Header File");
            int returnValue = jFileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                fileHeader = jFileChooser.getSelectedFile();
                csv = fileHeader;
                isFileRead = true;
            }
        }
        FileReader fileReader = new FileReader(fileHeader);
        System.out.println("fileHeader "+fileHeader);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        ArrayList<InvoiceHeader> arrayList;
        arrayList = readData(bufferedReader);
        bufferedReader.close();
        return arrayList;
    }
    public ArrayList<InvoiceHeader> readData(BufferedReader bufferedReader) throws IOException {
        ArrayList<InvoiceHeader> arraylist = new ArrayList<>();
        while (bufferedReader.ready()) {
            arraylist.add(readInvoice(bufferedReader));
        }
        return arraylist;
    }

    public InvoiceHeader readInvoice (BufferedReader bufferedReader) throws IOException {
        String line = bufferedReader.readLine();
        int invoiceNum = Integer.parseInt(line.split(",")[0]);
        LocalDate date = LocalDate.parse(line.split(",")[1], DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        String customerName = line.split(",")[2];
        double total = Double.parseDouble(line.split(",")[3]);

        return new InvoiceHeader(invoiceNum , date, customerName, total);
    }
    //Write Invoice Header
    public void writeFile(ArrayList<InvoiceHeader> a , boolean AddOrDelete) {
        try{
            if (csv == null) {
                JFileChooser jFileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                jFileChooser.setCurrentDirectory(new File(".").getAbsoluteFile());
                jFileChooser.setDialogTitle("Select Header File To Save");
                int returnValue = jFileChooser.showSaveDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    csv = jFileChooser.getSelectedFile();
                }
            }

            //String csv = "InvoiceHeader.csv";
            CSVWriter csvWriter = new CSVWriter(new FileWriter(csv , AddOrDelete), ',', CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.NO_ESCAPE_CHARACTER, "");
            for (int x = 0; x < a.size(); x++) {
                System.out.println(" new index a "+a.size());
                int invoiceNumber = a.get(x).getInvoiceNum();
                LocalDate date = a.get(x).getInvoiceDate();
                String customerName = a.get(x).getCustomerName();
                double total = a.get(x).getTotal();
                setTotal(total);
                total = getTotal();
                String[] dataToWrite = {String.valueOf(invoiceNumber), date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")), customerName, String.valueOf(total)};
                String[] newLine = {"\n"};
                if(AddOrDelete) csvWriter.writeNext(newLine, false); //to add new line after each invoice except for the last one
                csvWriter.writeNext(dataToWrite, false);
                if(x < a.size()-1 && !AddOrDelete) csvWriter.writeNext(newLine, false); //to add new line after each invoice except for the last one
                csvWriter.flush();
            }
            //csvWriter.flush();
            csvWriter.close();
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}