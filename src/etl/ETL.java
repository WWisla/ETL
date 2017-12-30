package etl;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.nodes.Document;

/**
 * Created by Daniel K on 2017-10-28.
 */
public class ETL extends JFrame implements ActionListener{
    private JTextField product;
    private JTextArea result;
    private JButton extract, transform, load;
    private ArrayList<Document> docList = new ArrayList<>();

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

        //GUI panels
        JPanel searchPanel = new JPanel();
        JPanel operationPanel = new JPanel();
        JScrollPane resultPanel = new JScrollPane(result);

        //button listeners
        extract.addActionListener(this);
        transform.addActionListener(this);
        load.addActionListener(this);

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

    @Override
    public void actionPerformed(ActionEvent e) {
        //Extract button action
        if(e.getActionCommand().equals("Extract")){
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
        }
        //Transform button action
        if(e.getActionCommand().equals("Transform")){
            Transform.transform(docList);

            result.setText(Transform.transformToString());
        }
        //Load button action
        if(e.getActionCommand().equals("Load")){
            //TODO
        }
    }

    public static void main(String[] args) {
        //start GUI in new thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                new ETL();
            }
        }).run();
    }
}
