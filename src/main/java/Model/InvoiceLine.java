package Model;

import com.opencsv.CSVWriter;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.nio.file.DirectoryNotEmptyException;
import java.util.ArrayList;

public class InvoiceLine {
    private int invoiceNum;
    private String itemName;
    private double itemPrice;
    private int count; //number of items purchased
    private double itemTotal;
    private static boolean isFileRead = false;
    private static File fileItems = null;
    private static File csv = null;
    private static boolean isFileWritten = false;
    //private static int invoicenumberCount;

    public InvoiceLine() {
    }

    public InvoiceLine(int invoiceNum, String itemName, double itemPrice, int count, double itemTotal) {
        this.invoiceNum = invoiceNum;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.count = count;
        this.itemTotal = itemTotal;
    }

    public int getInvoiceNum() {
        return invoiceNum;
    }

    public void setInvoiceNum(int invoiceNum) {
        if(invoiceNum == 0){
            this.invoiceNum = countInvoiceNumber();
        }
    }
    public int countInvoiceNumber() {
        ArrayList<InvoiceLine> arrayList = new ArrayList<>();
        InvoiceLine invoiceLine = new InvoiceLine();
        arrayList = invoiceLine.readFile();
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
    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getItemTotal() {
        return itemTotal;
    }

    public void setItemTotal(double itemTotal, double itemPrice, double count) {
        if(itemTotal == 0){
            calculateItemTotal(itemPrice, count);
        }
        else{
            this.itemTotal = itemTotal;
        }
    }
    private double calculateItemTotal(double itemPrice, double count){
        return this.itemTotal= itemPrice * count;
    }

    //Read Line File
    public ArrayList<InvoiceLine> readFile() {
        ArrayList<InvoiceLine> arrayList=null;
        try {
            if (isFileRead == false) {
                JFileChooser jFileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                jFileChooser.setCurrentDirectory(new File(".").getAbsoluteFile());
                jFileChooser.setDialogTitle("Select Items File");
                int returnValue = jFileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    fileItems = jFileChooser.getSelectedFile();
                    csv = fileItems;
                    isFileRead = true;
                }
            }
            FileReader fileReader = new FileReader(fileItems);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            arrayList = readData(bufferedReader);
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFoundException");
        }catch (IOException e) {
            System.out.println("FileNotFoundException");
        }
        return arrayList;
    }
    public ArrayList<InvoiceLine> readData(BufferedReader bufferedReader) throws IOException {
        ArrayList<InvoiceLine> arraylist = new ArrayList<>();
        while (bufferedReader.ready()) {
            arraylist.add(readInvoice(bufferedReader));
        }
        return arraylist;
    }

    public InvoiceLine readInvoice (BufferedReader bufferedReader) throws IOException {
        String line = bufferedReader.readLine();
        int invoiceNum = Integer.parseInt(line.split(",")[0]);
        String itemName = line.split(",")[1];
        double itemPrice = Double.parseDouble(line.split(",")[2]);
        int count = Integer.parseInt(line.split(",")[3]);
        double itemTotal = Double.parseDouble(line.split(",")[4]);

        return new InvoiceLine(invoiceNum , itemName, itemPrice, count, itemTotal);
    }
    //Write Invoice Line
    public void writeInvoiceLineFile(ArrayList<InvoiceLine> a , boolean AddOrDelete) {
        try{
            if (csv == null) {
                JFileChooser jFileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                jFileChooser.setCurrentDirectory(new File(".").getAbsoluteFile());
                jFileChooser.setDialogTitle("Select Items/Line File To Save");
                int returnValue = jFileChooser.showSaveDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    csv = jFileChooser.getSelectedFile();
                }
            }

            CSVWriter csvWriter = new CSVWriter(new FileWriter(csv , AddOrDelete), ',', CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.NO_ESCAPE_CHARACTER, "");

            for (int x = 0; x < a.size(); x++) {
                int invoiceNumber = a.get(x).getInvoiceNum();
                String itemName = a.get(x).getItemName();
                double itemPrice = a.get(x).getItemPrice();
                int count = a.get(x).getCount();
                double itemTotal = a.get(x).getItemTotal();
                String[] dataToWrite = {String.valueOf(invoiceNumber), itemName, String.valueOf(itemPrice), String.valueOf(count), String.valueOf(itemTotal)};
                String[] newLine = {"\n"};

                if(AddOrDelete) {
                    csvWriter.writeNext(newLine, false); //to add new line after each invoice except for the last one

                }
                csvWriter.writeNext(dataToWrite, false);

                if(x < a.size()-1 && !AddOrDelete){
                    csvWriter.writeNext(newLine, false); //to add new line after each invoice except for the last one
                }
                //csvWriter.writeNext(newLine, false);
                csvWriter.flush();
            }
            csvWriter.close();
        }
        catch (FileNotFoundException e){
            System.out.println("FileNotFoundException");
        }
        catch (DirectoryNotEmptyException e){
            System.out.println("DirectoryNotFoundException");
        }
        catch (IOException e){
            System.out.println("IOException -- RuntimeException --");
        }
    }
}
