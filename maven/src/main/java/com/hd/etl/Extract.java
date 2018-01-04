package com.hd.etl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
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
        ArrayList<Document> docList = new ArrayList<Document>();
        html = new StringBuilder();

        //parse html site
        Document doc = Extract.extractDocument(url); //"https://www.ceneo.pl/47629930#tab=reviews"
        System.out.println(url);

        //save parsed site to temporary document
        Document tempDoc = doc;

        //add to list of documents from this extract
        docList.add(doc);

        String fileName = "extract.xml";

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

        try{
            FileService fileService = new FileService(fileName);

            StringBuilder xml = new StringBuilder();

            for(Document document: docList){
                xml.append(convertToXHTML(document.html()));
            }
            fileService.print(xml.toString());
        }
        catch (IOException event){
            event.printStackTrace();
        }

        try {
            FileService fileService = new FileService(fileName);

            html.append(fileService.read());
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

