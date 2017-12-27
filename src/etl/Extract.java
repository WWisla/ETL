package etl;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 *
 * @author lenovo
 */
public class Extract{
    public static String extract(String url) throws IOException{
        Document doc = Jsoup.connect(url)
                .header("Accept-Encoding", "gzip, deflate")
                .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
                .maxBodySize(0)
                .timeout(6000)
                .get();
        /**
        Elements html = doc.getAllElements();

        StringBuilder result = new StringBuilder();


        for (Element e : html){
            result.append(e.html());
        }*/

        return doc.html();
    }

    public static Document extractDocument(String url) throws IOException{

        Document doc = Jsoup.connect(url)
                .header("Accept-Encoding", "gzip, deflate")
                .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
                .maxBodySize(0)
                .timeout(6000)
                .get();

        return doc;
    }

    public static String convertToXHTML(String html){
        Document doc = Jsoup.parse(html);

        doc.outputSettings().syntax(Document.OutputSettings.Syntax.xml);

        return doc.html();
    }
}
