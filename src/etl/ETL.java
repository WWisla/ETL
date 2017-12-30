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
import java.nio.ByteBuffer;
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
    private StringBuilder test = new StringBuilder();;

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

        //log panel settings
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
            String url = "https://www.ceneo.pl/" + product.getText() + "#tab=reviews";

            //temporary doc to keep html code in it
            Document tempDoc;

            //clear list for extracted html docs
            docList.clear();

            //clear text buffer needed to display text in GUI
            test.setLength(0);

            try {
                //parse html site
                Document doc = Extract.extractDocument(url); //"https://www.ceneo.pl/47629930#tab=reviews"

                //save parsed site to temporary document
                tempDoc = doc;

                //add to list of documents from this extract
                docList.add(doc);

                //search in temp doc for next review site link
                while (tempDoc.select("li").hasClass("page-arrow arrow-next")){
                    for (Element element : tempDoc.select("li")) {
                        //if next site link exist catch it to temporary element
                        if (element.hasClass("page-arrow arrow-next")) {
                            Element next = element.select("a").first();
                            try {
                                //choose this links which contains reviews only
                                if(next.attr("href").contains("opinie")) {
                                    //extract next site of reviews
                                    tempDoc = Extract.extractDocument("https://www.ceneo.pl" + next.attr("href"));
                                    System.out.println(next.attr("href"));
                                }
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            //add extracted site to list and check for next ones
                            docList.add(tempDoc);
                        }
                    }
                }
            } catch (IOException event){
                event.printStackTrace();
            }

            try {
                //create new file named below
                String fileName = "extract.xml";

                PrintWriter extract = new PrintWriter(fileName);

                for(Document doc : docList){
                    //convert html of each doc to xhtml and save to file
                    String xml = Extract.convertToXHTML(doc.html());

                    extract.print(xml);
                    //if its last parsed document not needed to add 2 new lines
                    if(!doc.equals(docList.get(docList.size()-1))) {
                        //2 new lines between each html code
                        extract.print("\r\n\r\n");
                    }
                }

                //close file
                extract.close();

                System.out.println("Zapisano do pliku " + fileName);
            }
            catch (FileNotFoundException event){
                event.printStackTrace();
            }

            try {
                //connect file channel to enable fast load big files or containing
                //a lot of signs to display it on GUI - without it loading is never ending story
                Path path = Paths.get("extract.xml");
                FileChannel fileChannel = FileChannel.open(path);

                //allocate buffer - number of bytes in memory
                ByteBuffer buffer = ByteBuffer.allocate(100000);
                int bytesRead = fileChannel.read(buffer);

                //read file until end - end throw exception (-1)
                while (bytesRead != -1) {
                    buffer.flip();

                    //load signs one by one
                    while (buffer.hasRemaining()) {
                        synchronized (buffer) {
                            //load sign to string builder
                            test.append((char) buffer.get());
                        }
                    }
                    System.out.println(fileChannel.position());
                    //clear buffer - again allocated number and load again
                    buffer.clear();
                    bytesRead = fileChannel.read(buffer);
                }
                //display loaded file in GUI
                result.append(test.toString());
                System.out.println("DONE");

                //close file channel
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
        //strat GUI in new thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                new ETL();
            }
        }).run();
    }
}
