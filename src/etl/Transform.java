package etl;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Created by Daniel K on 2017-10-28.
 */
public class Transform {
    private static ArrayList<Opinia> opinie;

    public static void transform(ArrayList<Document> docList){
        opinie = new ArrayList<>();
        int i = 1;
        for (Document doc : docList) {
            Elements opinia = doc.select("li");

            for (Element element : opinia) {
                if (element.hasClass("review-box js_product-review")) {
                    opinie.add(new Opinia(element));
                    System.out.println(element.outerHtml());
                    System.out.println("********************************************");
                    i++;
                }
            }
        }
        //System.out.println(opinie.size());

        try {
            String fileName = "transform.xml";

            PrintWriter transform = new PrintWriter(fileName);

            for(Opinia opinia : opinie){
                transform.print(opinia.outerHtml());
                if(!opinia.equals(opinie.get(opinie.size()-1))) {
                    transform.print("\r\n\r\n");
                }
            }

            transform.close();

            System.out.println("Zapisano do pliku " + fileName);
        }
        catch (FileNotFoundException event){
            event.printStackTrace();
        }
    }
}
