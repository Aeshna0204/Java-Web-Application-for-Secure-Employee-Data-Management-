package com.resource.management.hr.pl.ui;

import com.resource.management.hr.pl.model.*;
import com.resource.management.hr.bl.exceptions.*;
import com.resource.management.hr.bl.interfaces.pojo.*;
import com.resource.management.hr.bl.pojo.*;
import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

public class DesignationUI extends JFrame implements DocumentListener,ListSelectionListener {

    private JLabel titleLabel;
    private JLabel searchErrorLabel;
    private JLabel searchLabel;
    private JTextField searchTextField;
    private JButton clearSearchTextFieldButton;
    private JTable designationTable;
    private DesignationModel designationModel;
    private Container container;
    private JScrollPane scrollPane;
    private DesignationPanel designationPanel;
    private enum MODE{VIEW,ADD,EDIT,DELETE,EXPORT_TO_PDF};
    private MODE mode;
    

    public DesignationUI() {
        initComponents();
        addListeners();
        setAppearance();
        setViewMode();
    }

    

    private void initComponents() {

        designationModel = new DesignationModel();
        titleLabel = new JLabel("Designations");
        searchLabel = new JLabel("Search");
        searchTextField = new JTextField();
        clearSearchTextFieldButton = new JButton("X"); // done

        searchErrorLabel = new JLabel("");
        designationTable = new JTable(designationModel);
        scrollPane = new JScrollPane(designationTable, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        container = getContentPane();
    }

    private void setAppearance() {
        Font titleFont = new Font("Verdana", Font.BOLD, 18);
        Font captionFont = new Font("Verdana", Font.BOLD, 16);
        Font dataFont = new Font("Verdana", Font.PLAIN, 16);
        Font columnHeaderFont = new Font("Verdana", Font.BOLD, 16);
        Font columFont = new Font("Verdana", Font.BOLD, 16);
        Font searchErrorFont = new Font("Verdana", Font.BOLD, 12);
        titleLabel.setFont(titleFont);
        searchLabel.setFont(captionFont);
        searchTextField.setFont(dataFont);
        searchErrorLabel.setFont(searchErrorFont);
        searchErrorLabel.setForeground(Color.red);
        designationTable.setFont(dataFont);
        designationTable.setRowHeight(35);
        designationTable.getColumnModel().getColumn(0).setPreferredWidth(20);
        designationTable.getColumnModel().getColumn(1).setPreferredWidth(400);
        JTableHeader header = designationTable.getTableHeader();
        header.setFont(columnHeaderFont);
        header.setReorderingAllowed(false);
        header.setResizingAllowed(false);
        designationTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        designationPanel = new DesignationPanel();
        container.setLayout(null);
        int lm = 0, tm = 0;
        titleLabel.setBounds(lm + 10, tm + 10, 200, 40);
        searchLabel.setBounds(lm + 10, tm + 60, 100, 30);
        searchErrorLabel.setBounds(lm + 420, tm + 30, 100, 20);
        searchTextField.setBounds(lm + 100, tm + 60, 400, 30);
        clearSearchTextFieldButton.setBounds(lm + 505, tm + 60, 30, 30);
        scrollPane.setBounds(lm + 10, tm + 100, 565, 340);
        designationPanel.setBounds(lm + 10, tm + 420, 565, 200);
        container.add(titleLabel);
        container.add(searchLabel);
        container.add(searchErrorLabel);
        container.add(searchTextField);
        container.add(clearSearchTextFieldButton);
        container.add(scrollPane);
        container.add(designationPanel);
        int w = 600, h = 680;
        setSize(w, h);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((d.width / 2) - (w / 2), (d.height / 2) - (h / 2));

    }

    private void addListeners() {

        searchTextField.getDocument().addDocumentListener(this);
        clearSearchTextFieldButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                searchTextField.setText("");
                searchTextField.requestFocus();
            }
        });

        designationTable.getSelectionModel().addListSelectionListener(this);


    }

    private void searchDesignation() {
        searchErrorLabel.setText("");
        String title = searchTextField.getText().trim();
        if (title.length() == 0)
            return;
        int rowIndex;
        try {
            rowIndex = designationModel.indexOfTitle(title, true);
        } catch (BLException ble) {
            searchErrorLabel.setText("NotFound");
            return;
        }
        designationTable.setRowSelectionInterval(rowIndex, rowIndex);
        // to get the rectangle of matching row
        Rectangle rectangle = designationTable.getCellRect(rowIndex, 0, true);
        designationTable.scrollRectToVisible(rectangle);

    }

    public void removeUpdate(DocumentEvent ev) {
        searchDesignation();
    }

    public void insertUpdate(DocumentEvent ev) {
        searchDesignation();
    }

    public void changedUpdate(DocumentEvent ev) {
        searchDesignation();
    }

    public void valueChanged(ListSelectionEvent ev){
        int selectedRowIndex=designationTable.getSelectedRow();
        try{
            DesignationInterface designation=designationModel.getDesignationAt(selectedRowIndex);
            designationPanel.setDesignation(designation);

        }catch(BLException ble){
            designationPanel.clearDesignation();
        }
    }

    private void setViewMode(){
        this.mode=MODE.VIEW;

        if(designationModel.getRowCount()==0)
    }





    // inner class

    public class DesignationPanel extends JPanel {
        private JLabel titleCaptionLabel;
        private JLabel titleLabel;
        private JTextField titleTextField;
        private JButton clearTextFieldButton;
        private JButton addButton;
        private JButton deleteButton;
        private JButton editButton;
        private JButton cancelButton;
        private JButton exportToPDFButton;
        private JPanel buttonsPanel;
        private DesignationInterface designation;

        DesignationPanel() {
            setBorder(BorderFactory.createLineBorder(new Color(170, 170, 170)));
            initComponents();
            setAppearance();
            addListeners();
        }
        // --------------------------------------------------------------------------------

        public void setDesignation(DesignationInterface designation) {
        this.designation = designation;
        titleLabel.setText(designation.getTitle());
        }

    // *******************************************************************************
        public void clearDesignation() {
             this.designation = null;
             titleLabel.setText("");
        }

        private void initComponents() {
            designation = null;
            titleCaptionLabel = new JLabel("Designation");
            titleLabel = new JLabel("");
            titleTextField = new JTextField();
            clearTextFieldButton = new JButton("X");
            buttonsPanel = new JPanel();
            addButton = new JButton("A");
            editButton = new JButton("E");
            deleteButton = new JButton("D");
            cancelButton = new JButton("C");
            exportToPDFButton = new JButton("P");
        }

        private void setAppearance() {
            Font captionFont = new Font("Verdana", Font.BOLD, 16);
            Font dataFont = new Font("Verdana", Font.PLAIN, 16);
            titleCaptionLabel.setFont(captionFont);
            titleLabel.setFont(dataFont);
            titleTextField.setFont(dataFont);
            setLayout(null);
            int lm = 0, tm = 0;
            titleCaptionLabel.setBounds(lm + 10, tm + 20, 110, 30);
            titleLabel.setBounds(lm + 125, tm + 20, 400, 30);
            titleTextField.setBounds(lm + 125, tm + 20, 350, 30);
            clearTextFieldButton.setBounds(lm + 480, tm + 20, 30, 30);
            buttonsPanel.setBounds(50, tm + 80, 465, 75);
            buttonsPanel.setBorder(BorderFactory.createLineBorder(new Color(165, 165, 165)));
            addButton.setBounds(70, 12, 50, 50);
            editButton.setBounds(140, 12, 50, 50);
            cancelButton.setBounds(210, 12, 50, 50);
            deleteButton.setBounds(280, 12, 50, 50);
            exportToPDFButton.setBounds(350, 12, 50, 50);
            buttonsPanel.setLayout(null);
            buttonsPanel.add(addButton);
            buttonsPanel.add(editButton);
            buttonsPanel.add(cancelButton);
            buttonsPanel.add(deleteButton);
            buttonsPanel.add(exportToPDFButton);
            add(titleCaptionLabel);
            add(titleTextField);
            add(titleLabel);
            add(clearTextFieldButton);
            add(buttonsPanel);
        }

    }
}
