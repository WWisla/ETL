package com.hd.etl;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.jsoup.nodes.Document;

/**
 * Created by Daniel K on 2017-10-28.
 */
public class ETL extends JFrame implements ActionListener{
    private JTextField product;
    private JTextArea result;
    private JButton extract, transform, load, etl, clearDataBase;
    private ArrayList<Document> docList = new ArrayList<Document>();
    private ArrayList<Opinia> reviews = new ArrayList<Opinia>();

    public ETL(){
        //window GUI settings
        setTitle("CENEO.PL ETL Process");
        setLayout(new BorderLayout());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //GUI components
        product = new JTextField(10);
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
        searchPanel.add(product);
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
        product.setText("47629930");//56435526

        //resize window to used by components size and enable visibility
        pack();
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        //Extract button action
        if(e.getActionCommand().equals(extract.getActionCommand())){
            //create ceneo.pl url with product number to extract reviews
            //"https://www.ceneo.pl/47629930#tab=reviews"
            String url = "https://www.ceneo.pl/" + product.getText() + "#tab=reviews";

            //extract all review sites for this product number
            try {
                docList = Extract.extractDocuments(url);
            }
            catch (IOException event){
                event.printStackTrace();
            }

            //display result of extract in GUI
            result.setText(Extract.extractToString());

            //enable transform
            transform.setEnabled(true);
        }
        //Transform button action
        if(e.getActionCommand().equals(transform.getActionCommand())){
            reviews = Transform.transform(docList);

            //display result of transform in GUI
            result.setText(Transform.transformToString());

            //enable load
            load.setEnabled(true);
        }
        //Load button action
        if(e.getActionCommand().equals(load.getActionCommand())){
            //TODO
            Load.load();
        }
        //ETL button action
        if(e.getActionCommand().equals(etl.getActionCommand())){
            //TODO
            System.out.println("ETL");
        }
        //Clear Data Base action
        if(e.getActionCommand().equals(clearDataBase.getActionCommand())){
            //TODO
            System.out.println("DROP");
            Load.dropDataBase();
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
}

