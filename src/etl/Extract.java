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
public class Extract{
    public static String extract(String url) throws IOException{
        Document doc = Jsoup.connect(url)
                .header("Accept-Encoding", "gzip, deflate")
                .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
                .maxBodySize(0)
                .timeout(600000)
                .get();

        Elements html = doc.getAllElements();

        String result = "";

        for (Element e : html){
            result += e.html();
        }

        return result;
    }
}
