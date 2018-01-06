package com.hd.etl;

import org.jsoup.nodes.Document;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Daniel K on 2017-10-28.
 */
public class ETL extends JFrame implements ActionListener{
    //GUI
    private JTextField productID;
    private JTextArea result;
    private JButton extract, transform, load, etl, clearDataBase, exportCSV, exportReviews;
    //etl variables
    private ArrayList<Document> docList = new ArrayList<Document>();
    private ArrayList<Opinia> reviews = new ArrayList<Opinia>();
    private Produkt product;
    private long id;
    //methods pointer
    private ETLMethods etlMethods = new ETLMethods();

    public ETL(){
        //window GUI settings
        setTitle("CENEO.PL ETL Process");
        setLayout(new BorderLayout());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //GUI components
        productID = new JTextField(10);
        result = new JTextArea(15,100);
        extract = new JButton("Extract");
        transform = new JButton("Transform");
        load = new JButton("Load");
        etl = new JButton("ETL");
        clearDataBase = new JButton("Clear Data Base");
        exportCSV = new JButton("Export Data to CSV");
        exportReviews = new JButton("Export reviews to files");

        //GUI panels
        JPanel searchPanel = new JPanel();
        JPanel operationPanel = new JPanel();
        JScrollPane resultPanel = new JScrollPane(result);

        //button listeners
        extract.addActionListener(this);
        transform.addActionListener(this);
        load.addActionListener(this);
        etl.addActionListener(this);
        exportCSV.addActionListener(this);
        exportReviews.addActionListener(this);
        clearDataBase.addActionListener(this);

        //button settings
        transform.setEnabled(false);
        load.setEnabled(false);
        exportCSV.setEnabled(false);
        exportReviews.setEnabled(false);

        //result panel settings
        resultPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        resultPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        //text display settings in result panel
        result.setLineWrap(true);
        result.setWrapStyleWord(true);
        result.setEditable(false);
        DefaultCaret caret = (DefaultCaret) result.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        resultPanel.setViewportView(result);

        //default welcome result text
        result.append("Witaj! \r\n\r\n");
        result.append("Ten program pozwoli Ci pobierać informacje o produkcie oraz ");
        result.append("opinie użytkowników o nim z witryny Ceneo.pl.\r\n\r\n\r\n");
        result.append("UWAGA: pobieranie dużych ilości opinii może potrwać chwilkę lub dwie. Prosimy o cierpliwość :)\r\n");

        //adding GUI components to panels
        searchPanel.add(new JLabel("Product ID:"));
        searchPanel.add(productID);
        operationPanel.add(extract);
        operationPanel.add(transform);
        operationPanel.add(load);
        operationPanel.add(etl);
        operationPanel.add(exportCSV);
        operationPanel.add(exportReviews);
        operationPanel.add(clearDataBase);

        //adding GUI panels to window
        add(searchPanel, BorderLayout.NORTH);
        add(resultPanel, BorderLayout.CENTER);
        add(operationPanel, BorderLayout.SOUTH);

        //default product number - for test purpose only
        productID.setText("47629930");//56435526

        //resize window to used by components size and enable visibility
        pack();
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        //Extract button action
        if(e.getActionCommand().equals(extract.getActionCommand())){
            //display result of extract in GUI
            new Thread(new Runnable() {
                @Override
                public void run() {
                    result.setText(etlMethods.extract());
                }
            }).run();
            repaint();
        }
        //Transform button action
        if(e.getActionCommand().equals(transform.getActionCommand())){
            //display result of transform in GUI
            new Thread(new Runnable() {
                @Override
                public void run() {
                    result.setText(etlMethods.transform());
                }
            }).run();
            repaint();
        }
        //Load button action
        if(e.getActionCommand().equals(load.getActionCommand())){
            //display result of load in GUI
            new Thread(new Runnable() {
                @Override
                public void run() {
                    result.setText(etlMethods.load());
                }
            }).run();
            repaint();
        }
        //ETL button action
        if(e.getActionCommand().equals(etl.getActionCommand())){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    etlMethods.extract();
                    etlMethods.transform();
                    result.setText(etlMethods.load());
                }
            }).run();
        }
        //Export to CSV button action
        if(e.getActionCommand().equals(exportCSV.getActionCommand())){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    etlMethods.exportDataToCSV();
                }
            }).run();
        }
        //Export reviews button action
        if(e.getActionCommand().equals(exportReviews.getActionCommand())){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    etlMethods.exportReviewsToFiles();
                }
            }).run();
        }
        //Clear Data Base action
        if(e.getActionCommand().equals(clearDataBase.getActionCommand())){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    result.append("\r\n" + etlMethods.dropDataBase() + "\r\n");
                }
            }).run();
        }
    }

    public static void main(String[] args) {
        //start GUI in new thread
        new Thread(new Runnable() {
            public void run() {
                new ETL();
            }
        }).run();
    }

    private class ETLMethods{
        public String extract(){
            //create ceneo.pl url with product number to extract reviews
            //"https://www.ceneo.pl/47629930#tab=reviews"
            id = Long.parseLong(productID.getText());
            String url = "https://www.ceneo.pl/" + productID.getText() + "#tab=reviews";

            //extract all review sites for this product number
            try {
                docList = Extract.extractDocuments(url);
            }
            catch (IOException event){
                event.printStackTrace();
            }

            //enable transform
            transform.setEnabled(true);
            load.setEnabled(false);
            return Extract.extractToString();
        }

        public String transform(){
            //get all reviews and product info
            reviews = Transform.transform(docList);
            product = Transform.transform(docList.get(0), id);

            //enable load
            load.setEnabled(true);
            exportCSV.setEnabled(true);
            exportReviews.setEnabled(true);
            return Transform.transformToString();
        }

        public String load(){
            String str = Load.load(product,reviews);

            //disable buttons
            transform.setEnabled(false);
            load.setEnabled(false);
            exportCSV.setEnabled(false);
            exportReviews.setEnabled(false);

            //clear etl variables
            reviews.clear();
            docList.clear();

            return str;
        }

        public String dropDataBase(){
            //clear all database - recreate empty database
            return Load.dropDataBase();
        }

        public void exportDataToCSV(){
            Transform.exportDataToCSV();
        }

        public void exportReviewsToFiles(){
            Transform.reviewToFile();
        }
    }
}

