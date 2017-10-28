/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etl;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author lenovo
 */
public class ETL {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        Document doc = Jsoup.connect("https://www.ceneo.pl/47629930/opinie-2")
                .header("Accept-Encoding", "gzip, deflate")
                .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
                .maxBodySize(0)
                .timeout(600000)
                .get();

        Elements html = doc.getAllElements();

        for (Element e : html){
            System.out.println(e.html());
        }
    }
    
}
