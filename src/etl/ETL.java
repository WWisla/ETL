package etl;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Created by Daniel K on 2017-10-28.
 */
public class ETL extends JFrame implements ActionListener{
    private JTextField product;
    private JTextArea result;
    private JButton extract, transform, load;
    private ArrayList<Document> docList = new ArrayList<>();

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

        product.setText("56435526");//56435526

        pack();
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Extract")){
            String url = "https://www.ceneo.pl/" + product.getText() + "#tab=reviews";
            //String html = "";
            Document tempDoc;

            try {
                //html = Extract.extract(url); //"https://www.ceneo.pl/47629930#tab=reviews"

                Document doc = Extract.extractDocument(url);

                tempDoc = doc;

                docList.add(doc);

                int i = 1;

                while (tempDoc.select("li").hasClass("page-arrow arrow-next")){

                    i++;

                    for (Element element : tempDoc.select("li")) {
                        if (element.hasClass("page-arrow arrow-next")) {
                            Element next = element.select("a").first();
                            try {
                                tempDoc = Extract.extractDocument("https://www.ceneo.pl" + next.attr("href"));
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            System.out.println(next.attr("href"));
                            docList.add(tempDoc);
                        }
                    }
                            System.out.println(i);
                }
                for(Document document : docList) {
                    result.append(document.html());
                }
            } catch (IOException event){
                event.printStackTrace();
            }

            try {
                PrintWriter extract = new PrintWriter("extract.xml");

                for(Document doc : docList){
                    String xml = Extract.convertToXHTML(doc.html());

                    extract.print(xml);
                    extract.print("\r\n");
                }

                extract.close();
            }
            catch (FileNotFoundException event){
                event.printStackTrace();
            }
            catch (NullPointerException event){
                event.printStackTrace();
            }
        }
        if(e.getActionCommand().equals("Transform")){
            Transform.transform(docList);
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
