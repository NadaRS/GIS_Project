package org.example;

import Controller.Controller;
import GeneralFunctions.DatePicker;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class View extends JFrame {

    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenuItem loadFile,saveFile;
    private JPanel leftSidePanel, rightSidePanel, leftBtns, upperLeftPanel, rightUpper,rightDwnBtns, rightCenter;
    private JTable invoiceTbl;
    private JTable invItemsTbl;
    private DefaultTableModel headerModel;
    private DefaultTableModel lineModel;
    private String[] headerInvItemsTbl = {"No.", "Item Name", "Item Price", "Count", "Item Total"} ;
    private String[] headerInvTbl = {"No.", "Date", "Customer", "Total"};
    private String[][] dataInvItemsTbl;
    private String[][] dataInvTbl;
    private JSplitPane paneSplit;
    private JButton createNewInvoiceBtn, deleteInvoiceBtn, saveBtn, cancelBtn;
    private JLabel invoiceNumValueLabel, invoiceTotalValueLabel;
    private JTextField invoiceDateTF, customerNameTF;
    private Controller controller;

    public void setDataInvItemsTbl(String[][] dataInvItemsTbl) {
        this.dataInvItemsTbl = dataInvItemsTbl;
    }
    public JTable getInvItemsTbl() {
        return invItemsTbl;
    }
    public View() {
        super("SIG Application");
        setSize(1100,800);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(1000, 700)); //Setting minimum window/JFrame Size
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //pack();

        //Creating File Menu
        menuBar = new JMenuBar();
        loadFile = new JMenuItem("Load File");
        //loadFile.addActionListener(new Controller(this));
        loadFile.setActionCommand("loadFile");


        saveFile = new JMenuItem("Save File");
        //saveFile.addActionListener(new Controller(this));
        saveFile.setActionCommand("saveFile");

        fileMenu = new JMenu("File");
        fileMenu.add(loadFile);
        fileMenu.add(saveFile);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar); // to display it in the frame

        leftSidePanel = new JPanel();
        leftSidePanel.setPreferredSize(new Dimension(500,700));
        leftSidePanel.setLayout(new BorderLayout());
        leftSidePanel.setBorder(BorderFactory.createLineBorder(Color.GRAY,0, true));
        leftSidePanel.setVisible(true);

        //Components of left side_panel
        ///////////////////////////////Table on the left panel
        upperLeftPanel = new JPanel(new GridLayout(1,2,3,2));
        TitledBorder titledBorder = BorderFactory.createTitledBorder("Invoices Table");
        titledBorder.setTitlePosition(TitledBorder.ABOVE_TOP);
        upperLeftPanel.setBorder(titledBorder);
        try{
            headerModel = new DefaultTableModel(headerInvTbl, 0){ boolean[] canEdit = new boolean [] { false, false, false, false }; public boolean isCellEditable(int rowIndex, int columnIndex) { return canEdit [columnIndex]; }; };
            invoiceTbl = new JTable(headerModel);
        } catch (NullPointerException n){
            System.out.println("NullPointerException");
        }
        upperLeftPanel.add(new JScrollPane(invoiceTbl));
        JLabel jLabel = new JLabel();
        leftSidePanel.add(upperLeftPanel, BorderLayout.PAGE_START);
        ///////////////////////////////buttons on the left panel
        leftBtns = new JPanel(new GridLayout(1,2,3,2));

        createNewInvoiceBtn = new JButton("Create New Invoice");
        createNewInvoiceBtn.setActionCommand("createNewInvoiceBtn");
        leftBtns.add(createNewInvoiceBtn);
        //createNewInvoiceBtn.addActionListener(new Controller()); // Controller plays role of an ActionListener

        deleteInvoiceBtn = new JButton("Delete Invoice");
        leftBtns.add(deleteInvoiceBtn);
        deleteInvoiceBtn.setActionCommand("deleteInvoice_btn"); // as if it is an id instead of -actionEvent.getSource().equals()-
        //deleteInvoiceBtn.addActionListener(new Controller(invoiceTbl));
        leftSidePanel.add(leftBtns, BorderLayout.SOUTH);

        //Right side panel
        rightSidePanel = new JPanel();
        rightSidePanel.setPreferredSize(new Dimension(500,700));
        rightSidePanel.setLayout(new BorderLayout());
        rightSidePanel.setBorder(BorderFactory.createLineBorder(Color.GRAY,0, false));
        rightSidePanel.setVisible(true);

        //Components of right side_panel
        ////////////////////////////////////////////////////////////Right Top
        rightUpper = new JPanel(new GridLayout(4,2,2,3));

        rightUpper.add(new JLabel("Invoice Number"));

        invoiceNumValueLabel = new JLabel("");
        rightUpper.add(invoiceNumValueLabel);

        rightUpper.add(new JLabel("Invoice Date"));

        invoiceDateTF = new JTextField(20);
        invoiceDateTF.setEditable(false);
        invoiceDateTF.setFocusable(false);
        invoiceDateTF.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        invoiceDateTF.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(invoiceDateTF.isEditable()){
                    invoiceDateTF.setText(new DatePicker(View.this).setPickedDate());
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if(invoiceDateTF.isEditable()){
                    invoiceDateTF.setText(new DatePicker(View.this).setPickedDate());
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {}

            @Override
            public void mouseEntered(MouseEvent e) {}

            @Override
            public void mouseExited(MouseEvent e) {}
        });
        rightUpper.add(invoiceDateTF);

        rightUpper.add(new JLabel("Customer Name"));

        customerNameTF = new JTextField(20);
        customerNameTF.setEditable(false);
        customerNameTF.setActionCommand("customerNameTF");
        customerNameTF.setText("Customer Name");
        customerNameTF.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent focusEvent) {

            }

            @Override
            public void focusLost(FocusEvent focusEvent) {
                if (customerNameTF.getText().equals("")){
                    customerNameTF.setText("Customer Name");
                }
            }
        });
        //customerNameTF.addActionListener(new Controller());
        rightUpper.add(customerNameTF);

        rightUpper.add(new JLabel("Invoice Total"));

        invoiceTotalValueLabel = new JLabel("0.0");
        rightUpper.add(invoiceTotalValueLabel);

        rightSidePanel.add(rightUpper, BorderLayout.PAGE_START);
        ////////////////////////////////////////////////////////////Right center
        rightCenter = new JPanel(new GridLayout(0,1,2,3));
        rightCenter.setBorder(BorderFactory.createTitledBorder("Invoice Items"));

        try{
            lineModel = new DefaultTableModel(headerInvItemsTbl, 0){ boolean[] canEdit = new boolean [] { false, true, true, true, false }; public boolean isCellEditable(int rowIndex, int columnIndex) { return canEdit [columnIndex]; } };
            invItemsTbl = new JTable(lineModel);
        } catch (NullPointerException n){
            System.out.println("NullPointerException");
        }

        rightCenter.add(new JScrollPane(invItemsTbl));
        rightSidePanel.add(rightCenter, BorderLayout.CENTER);
        ///////////////////////////////////////////////////////////Right bottom
        rightDwnBtns = new JPanel(new GridLayout(1,2,3,2));

        saveBtn = new JButton("Save");
        rightDwnBtns.add(saveBtn);
        //saveBtn.addActionListener(new Controller());
        saveBtn.setActionCommand("save_btn");

        cancelBtn = new JButton("Cancel");
        rightDwnBtns.add(cancelBtn);
        //cancelBtn.addActionListener(new Controller());
        cancelBtn.setActionCommand("cancel_btn");

        rightSidePanel.add(rightDwnBtns, BorderLayout.SOUTH);

        paneSplit = new JSplitPane();
        paneSplit.setSize(1000,800);
        //paneSplit.setResizeWeight(0.6);
        paneSplit.setDividerSize(8);
        paneSplit.setOneTouchExpandable(true);
        paneSplit.setContinuousLayout(true);
        paneSplit.setDividerLocation(550);
        paneSplit.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        paneSplit.setLeftComponent(leftSidePanel);
        paneSplit.setRightComponent(rightSidePanel);
        getContentPane().add(paneSplit);

        //add Action Listener
        controller = new Controller(this);
        loadFile.addActionListener(controller);
        saveFile.addActionListener(controller);
        createNewInvoiceBtn.addActionListener(controller);
        deleteInvoiceBtn.addActionListener(controller);
        saveBtn.addActionListener(controller);
        cancelBtn.addActionListener(controller);
        customerNameTF.addActionListener(controller);
        customerNameTF.addActionListener(controller);

    }

    public void setInvoiceNumValueLabel(JLabel invoiceNumValueLabel) {
        this.invoiceNumValueLabel = invoiceNumValueLabel;
    }

    public JLabel getinvoiceTotalValueLabel() {
        return invoiceTotalValueLabel;
    }

    public JLabel getInvoiceNumValueLabel() {
        return invoiceNumValueLabel;
    }

    public void setDataInvTbl(String[][] invoiceHData) {
    }

    public JTextField getInvoiceDateTF() {
        return invoiceDateTF;
    }
    public JTextField getCustomerNameTF() {
        return customerNameTF;
    }

    public JTable getInvoiceTbl() {
        return invoiceTbl;
    }
    public JTable getItemTbl() {
        return invItemsTbl;
    }
    public void getStautsPaneMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "File Status", JOptionPane.PLAIN_MESSAGE);
    }
}