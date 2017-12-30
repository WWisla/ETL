package etl;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 *
 * @author lenovo
 */
public class Extract{
    public static Document extractDocument(String url) throws IOException{
        //parse html from url
        Document doc = Jsoup.connect(url)
                .header("Accept-Encoding", "gzip, deflate")
                .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
                .maxBodySize(0)
                .timeout(6000)
                .get();

        return doc;
    }

    public static String convertToXHTML(String html){
        //convert html to xhtml - no more errors in syntax
        Document doc = Jsoup.parse(html);

        doc.outputSettings().syntax(Document.OutputSettings.Syntax.xml);

        return doc.html();
    }
}
