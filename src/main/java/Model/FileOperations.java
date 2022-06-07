package Model;

import com.opencsv.CSVWriter;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
public class FileOperations {
    public static ArrayList<InvoiceHeader> readFile() throws IOException {
        JFileChooser jFileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        int returnValue = jFileChooser.showOpenDialog(null);
        File file = null;
        if(returnValue == JFileChooser.APPROVE_OPTION){
            file = jFileChooser.getSelectedFile();
        }
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        ArrayList<InvoiceHeader> arrayList;
        arrayList = readData(bufferedReader);
        bufferedReader.close();
        return arrayList;
    }
    //Read Invoice Header
    public static ArrayList<InvoiceHeader> readData(BufferedReader bufferedReader) throws IOException {
        ArrayList<InvoiceHeader> arraylist = new ArrayList<>();
        while (bufferedReader.ready()) {
            arraylist.add(readInvoice(bufferedReader));
        }
        return arraylist;
    }

    public static InvoiceHeader readInvoice (BufferedReader bufferedReader) throws IOException {
        String line = bufferedReader.readLine();
        int invoiceNum = Integer.parseInt(line.split(",")[0]);
        LocalDate date = LocalDate.parse(line.split(",")[1], DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        String customerName = line.split(",")[2];
        double total = Double.parseDouble(line.split(",")[3]);
        return new InvoiceHeader(invoiceNum , date, customerName, total);
    }
    //Write Invoice Header
    static void writeInvoiceHeaderFile(ArrayList<InvoiceHeader> a) {
        InvoiceHeader invoiceHeader = new InvoiceHeader();
        try{
            JFileChooser jFileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
            int returnValue = jFileChooser.showOpenDialog(null);
            File csv = null;
            if(returnValue == JFileChooser.APPROVE_OPTION){
                csv = jFileChooser.getSelectedFile();
            }
            //String csv = "InvoiceHeader.csv";
            CSVWriter csvWriter = new CSVWriter(new FileWriter(csv , true), ',', CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.NO_ESCAPE_CHARACTER, "");
            int invoiceNumber= a.get(0).getInvoiceNum();
            LocalDate date = a.get(0).getInvoiceDate();
            String customerName = a.get(0).getCustomerName();
            double total = a.get(0).getTotal();
            invoiceHeader.setTotal(total);
            total = invoiceHeader.getTotal();
            String[] dataToWrite = {String.valueOf(invoiceNumber), date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")), customerName, String.valueOf(total)};
            String[] newLine = {"\n"};
            csvWriter.writeNext(newLine, false);
            csvWriter.writeNext(dataToWrite, false);
            csvWriter.flush();
            csvWriter.close();
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
   static public ArrayList<InvoiceLine> readFileLine() throws IOException {
       JFileChooser jFileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
       int returnValue = jFileChooser.showOpenDialog(null);
       File file = null;
       if(returnValue == JFileChooser.APPROVE_OPTION){
           file = jFileChooser.getSelectedFile();
       }
       FileReader fileReader = new FileReader(file);
       //FileReader fileReader = new FileReader(new File("InvoiceLine.csv"));
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        ArrayList<InvoiceLine> arrayList;
        arrayList = readDataLine(bufferedReader);

        bufferedReader.close();
        return arrayList;
    }
    static public ArrayList<InvoiceLine> readDataLine(BufferedReader bufferedReader) throws IOException {
        ArrayList<InvoiceLine> arraylist = new ArrayList<>();
        while (bufferedReader.ready()) {
            arraylist.add(readInvoiceLine(bufferedReader));
        }
        return arraylist;
    }

    static public InvoiceLine readInvoiceLine (BufferedReader bufferedReader) throws IOException {
        String line = bufferedReader.readLine();
        int invoiceNum = Integer.parseInt(line.split(",")[0]);
        String itemName = line.split(",")[1];
        double itemPrice = Double.parseDouble(line.split(",")[2]);
        int count = Integer.parseInt(line.split(",")[3]);
        double itemTotal = Double.parseDouble(line.split(",")[4]);;

        return new InvoiceLine(invoiceNum , itemName, itemPrice, count, itemTotal);
    }
     //Write Invoice Line
    static void writeInvoiceLineFile(ArrayList<InvoiceLine> a) {
        try{
            JFileChooser jFileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
            int returnValue = jFileChooser.showOpenDialog(null);
            File csv = null;
            if(returnValue == JFileChooser.APPROVE_OPTION){
                csv = jFileChooser.getSelectedFile();
            }
            //String csv = "InvoiceLine.csv";
            CSVWriter csvWriter = new CSVWriter(new FileWriter(csv , true), ',', CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.NO_ESCAPE_CHARACTER, "");
            int invoiceNumber= a.get(0).getInvoiceNum();
            String itemName = a.get(0).getItemName();
            double itemPrice = a.get(0).getItemPrice();
            int count = a.get(0).getCount();
            double itemTotal = a.get(0).getItemTotal();
            String[] dataToWrite = {String.valueOf(invoiceNumber), itemName, String.valueOf(itemPrice), String.valueOf(count), String.valueOf(itemTotal)};
            String[] newLine = {"\n"};
            csvWriter.writeNext(newLine, false);
            csvWriter.writeNext(dataToWrite, false);
            csvWriter.flush();
            csvWriter.close();
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws IOException {
        ArrayList<InvoiceHeader> arrayList = FileOperations.readFile();
        ArrayList<InvoiceLine> arrayListLine = FileOperations.readFileLine();

        writeInvoiceHeaderFile(arrayList);

        for (int i=0; i<arrayList.size() ; i++){

            System.out.println(arrayList.get(i).getInvoiceNum() + "\n" +
                    "{\n" +
                    "("+arrayList.get(i).getInvoiceDate()+"), "+arrayList.get(i).getCustomerName());

            for (int j=0; j<arrayListLine.size() ; j++) {
                if (arrayList.get(i).getInvoiceNum() == arrayListLine.get(j).getInvoiceNum()) {
                    System.out.println(arrayListLine.get(j).getItemName()+", "+arrayListLine.get(j).getItemPrice()+", "+arrayListLine.get(j).getCount() );
                }
            }
            System.out.println("}\n\n");
        }

//        ArrayList<InvoiceHeader> arrayList = FileOperations.readFile();
//        for (int i=0; i<arrayList.size() ; i++){
//            System.out.println(arrayList.get(i).getInvoiceDate());
//        }
//        //writing in file
//        ArrayList<InvoiceHeader> arrayListToWrite = new ArrayList<>();
//        arrayListToWrite.add(new InvoiceHeader(3,LocalDate.parse("2007-12-03"), "Hassan",Double.parseDouble("0")));
//        FileOperations.writeFile(arrayListToWrite);
//
//        arrayList = FileOperations.readFile();
//        for (int i=0; i<arrayList.size() ; i++){
//            System.out.println(arrayList.get(i).getInvoiceDate());
        }

//        ArrayList<InvoiceLine> arrayList = FileOperations.readFile();
//        for (int i=0; i<arrayList.size() ; i++){
//               System.out.println(arrayList.get(i).getItemName());
//            }
//        ArrayList<InvoiceLine> arrayListToWrite = new ArrayList<>();
//        arrayListToWrite.add(new InvoiceLine(3, "Laptop", 5500, 2, 0));
//        FileOperations.writeFile(arrayListToWrite);
//        arrayList = FileOperations.readFile();
//        for (int i=0; i<arrayList.size() ; i++){
//            System.out.println(arrayList.get(i).getItemName());
//        }

    }

