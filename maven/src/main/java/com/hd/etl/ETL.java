package com.hd.etl;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.DefaultStyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.jsoup.nodes.Document;

/**
 * Created by Daniel K on 2017-10-28.
 */
public class ETL extends JFrame implements ActionListener{
    private JTextField productID;
    private JTextArea result;
    private JButton extract, transform, load, etl, clearDataBase;
    private ArrayList<Document> docList = new ArrayList<Document>();
    private ArrayList<Opinia> reviews = new ArrayList<Opinia>();
    private Produkt product;
    private long id;
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

        //GUI panels
        JPanel searchPanel = new JPanel();
        JPanel operationPanel = new JPanel();
        JScrollPane resultPanel = new JScrollPane(result);

        //button listeners
        extract.addActionListener(this);
        transform.addActionListener(this);
        load.addActionListener(this);
        etl.addActionListener(this);
        clearDataBase.addActionListener(this);

        //button settings
        transform.setEnabled(false);
        load.setEnabled(false);

        //result panel settings
        resultPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        resultPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        result.setLineWrap(true);
        result.setWrapStyleWord(true);
        result.setEditable(false);
        DefaultCaret caret = (DefaultCaret) result.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        resultPanel.setViewportView(result);

        //adding GUI components to panels
        searchPanel.add(new JLabel("Product ID:"));
        searchPanel.add(productID);
        operationPanel.add(extract);
        operationPanel.add(transform);
        operationPanel.add(load);
        operationPanel.add(etl);
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
            //TODO find error in setText -> blocking gui after second use
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
                    etlMethods.load();
                }
            }).run();
        }
        //Clear Data Base action
        if(e.getActionCommand().equals(clearDataBase.getActionCommand())){
            //TODO finish this method
            new Thread(new Runnable() {
                @Override
                public void run() {
                    result.append(etlMethods.dropDataBase());
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
            reviews = Transform.transform(docList);
            product = Transform.transform(docList.get(0), id);

            //enable load
            load.setEnabled(true);
            return Transform.transformToString();
        }

        public String load(){
            System.out.println(Load.loadProdukty(product));

            int i = 1;
            for(Opinia review : reviews){
                System.out.println("Opinia: " + Load.loadOpinie(review));
                System.out.println("Produkt-Opinia: " + Load.loadProduktyOpinie(product, review));
                System.out.println(i);
                i++;
            }

            //deleting files after load
            File reviewsXML = new File("reviews.xml");
            File extractXML = new File("extract.xml");
            File transformXML = new File("transform.xml");

            if(reviewsXML.delete() && extractXML.delete() && transformXML.delete()){
                System.out.println("TEMPORARY FILES HAS BEEN DELETED");
            }
            else {
                System.out.println("ERROR");
            }

            transform.setEnabled(false);
            load.setEnabled(false);

            reviews.clear();
            docList.clear();

            return "Zrobione";
        }
        public String dropDataBase(){
            return Load.dropDataBase();
        }
    }
}

