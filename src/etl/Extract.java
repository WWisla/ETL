package etl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 *
 * @author lenovo
 */
public class Extract{
    private static StringBuilder html;
    public static Document extractDocument(String url) throws IOException{
        //parse html from url
        Document doc = Jsoup.connect(url)
                .header("Accept-Encoding", "gzip, deflate")
                .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
                .maxBodySize(0)
                .timeout(15000)
                .get();

        return doc;
    }

    public static ArrayList<Document> extractDocuments(String url) throws IOException{
        ArrayList<Document> docList = new ArrayList<>();
        html = new StringBuilder();

        //parse html site
        Document doc = Extract.extractDocument(url); //"https://www.ceneo.pl/47629930#tab=reviews"
        System.out.println(url);

        //save parsed site to temporary document
        Document tempDoc = doc;

        //add to list of documents from this extract
        docList.add(doc);

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
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //add extracted site to list and check for next ones
                    docList.add(tempDoc);
                }
            }
        }

        try {
            //create new file named below
            String fileName = "extract.xml";

            PrintWriter printWriter = new PrintWriter(fileName);

            for(Document document : docList){
                //convert html of each doc to xhtml and save to file
                String xml = Extract.convertToXHTML(document.html());

                printWriter.print(xml);
                //if its last parsed document not needed to add 2 new lines
                if(!document.equals(docList.get(docList.size()-1))) {
                    //2 new lines between each html code
                    printWriter.print("\r\n\r\n");
                }
            }

            //close file
            printWriter.close();

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
                        html.append((char) buffer.get());
                        //CharBuffer cb = Charset.defaultCharset().decode(buffer);
                        //html.append(cb.toString());
                    }
                }
                //System.out.println(fileChannel.position());
                //clear buffer - again allocated number and load again
                buffer.clear();
                bytesRead = fileChannel.read(buffer);
            }
            System.out.println("Extract:DONE");

            //close file channel
            fileChannel.close();
        }
        catch (IOException event){
            event.printStackTrace();
        }

        return docList;
    }

    public static String extractToString(){
        return html.toString();
    }

    public static String convertToXHTML(String html){
        //convert html to xhtml - no more errors in syntax
        Document doc = Jsoup.parse(html);

        doc.outputSettings().syntax(Document.OutputSettings.Syntax.xml);

        return doc.html();
    }
}
