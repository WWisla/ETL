package etl;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Created by Daniel K on 2017-10-28.
 */
public class ETL extends JFrame implements ActionListener{
    private JTextField product;
    private JTextArea result;
    private JButton extract, transform, load;
    private ArrayList<Document> docList = new ArrayList<>();
    private StringBuilder test = new StringBuilder();

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

        product.setText("47629930");//56435526

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

                while (tempDoc.select("li").hasClass("page-arrow arrow-next")){
                    for (Element element : tempDoc.select("li")) {
                        if (element.hasClass("page-arrow arrow-next")) {
                            Element next = element.select("a").first();
                            try {
                                if(next.attr("href").contains("opinie")) {
                                    tempDoc = Extract.extractDocument("https://www.ceneo.pl" + next.attr("href"));
                                    System.out.println(next.attr("href"));
                                }
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            docList.add(tempDoc);
                        }
                    }
                }
                /**
                for(Document document : docList) {
                    //TODO cant display a lot of signs in JTextArea
                    //result.append(document.html());
                    test.append(document.toString() + "\r\n");
                }

                System.out.println(test.length());
                 */

            } catch (IOException event){
                event.printStackTrace();
            }

            try {
                String fileName = "extract.xml";

                PrintWriter extract = new PrintWriter(fileName);

                for(Document doc : docList){
                    String xml = Extract.convertToXHTML(doc.html());

                    extract.print(xml);
                    if(!doc.equals(docList.get(docList.size()-1))) {
                        extract.print("\r\n\r\n");
                    }
                }

                extract.close();

                System.out.println("Zapisano do pliku " + fileName);
            }
            catch (FileNotFoundException event){
                event.printStackTrace();
            }
            catch (NullPointerException event){
                event.printStackTrace();
            }

            //TODO use FileChannel to append big file
            try {
                Path path = Paths.get("extract.xml");
                FileChannel fileChannel = FileChannel.open(path);

                ByteBuffer buffer = ByteBuffer.allocate(100000);
                int bytesRead = fileChannel.read(buffer);

                while (bytesRead != -1) {
                    buffer.flip();

                    while (buffer.hasRemaining()) {
                        //TODO find way to append buffer to JTextArea
                        synchronized (buffer) {
                            test.append((char) buffer.get());
                        }
                    }
                    System.out.println(fileChannel.position());
                    buffer.clear();
                    bytesRead = fileChannel.read(buffer);
                }
                result.append(test.toString());
                System.out.println("DONE");
                fileChannel.close();
            }
            catch (IOException event){
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
