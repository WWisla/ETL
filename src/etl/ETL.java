package etl;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Daniel K on 2017-10-28.
 */
public class ETL extends JFrame implements ActionListener{
    private JTextField product;
    private JTextArea result;
    private JButton extract, transform, load;

    public ETL(){
        setTitle("CENEO.PL ETL Process");
        setLayout(new BorderLayout());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        product = new JTextField(10);
        result = new JTextArea(15,100);
        extract = new JButton("Extract");
        transform = new JButton("Transform");
        load = new JButton("Load");

        JPanel searchPanel = new JPanel();
        JPanel operationPanel = new JPanel();
        JScrollPane resultPanel = new JScrollPane(result);

        extract.addActionListener(this);
        transform.addActionListener(this);
        load.addActionListener(this);

        resultPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        resultPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        result.setLineWrap(true);
        result.setWrapStyleWord(true);
        result.setEditable(false);
        DefaultCaret caret = (DefaultCaret) result.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        resultPanel.setViewportView(result);

        searchPanel.add(new JLabel("Product ID:"));
        searchPanel.add(product);
        operationPanel.add(extract);
        operationPanel.add(transform);
        operationPanel.add(load);

        add(searchPanel, BorderLayout.NORTH);
        add(resultPanel, BorderLayout.CENTER);
        add(operationPanel, BorderLayout.SOUTH);

        pack();
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Extract")){
            String url = "https://www.ceneo.pl/" + product.getText() + "#tab=reviews";
            String html = "";

            try {
                html = Extract.extract(url); //"https://www.ceneo.pl/47629930#tab=reviews"

                result.setText(html);
            } catch (IOException event){
                event.printStackTrace();
            }

            try {
                PrintWriter extract = new PrintWriter("extract.xml");

                String xml = Extract.convertToXHTML(html);
                
                extract.print(xml);

                extract.close();
            }
            catch (FileNotFoundException event){
                event.printStackTrace();
            }
        }
        if(e.getActionCommand().equals("Transform")){
            //TODO
        }
        if(e.getActionCommand().equals("Load")){
            //TODO
        }
    }

    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                new ETL();
            }
        }).run();
    }
}
