package etl;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * Created by Daniel K on 2017-10-28.
 */
public class Transform {
    public static void transform(ArrayList<Document> docList){
        int i = 1;
        for (Document doc : docList) {
            Elements opinia = doc.select("li");

            for (Element element : opinia) {
                if (element.hasClass("review-box js_product-review")) {
                    System.out.println(element.outerHtml());
                    System.out.println(i);
                    i++;
                }
            }
        }
    }
}
