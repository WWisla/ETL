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
    private static ArrayList<Opinia> opinie = new ArrayList<>();

    public static void transform(ArrayList<Document> docList){
        //clear list before transform
        opinie.clear();
        int i = 1;
        for (Document doc : docList) {
            //search for reviews only
            Elements opinia = doc.select("li");

            for (Element element : opinia) {
                if (element.hasClass("review-box js_product-review")) {
                    //add review to list
                    opinie.add(new Opinia(element));
                    System.out.println(element.outerHtml());
                    System.out.println(i);
                    i++;
                }
            }
        }
        //System.out.println(opinie.size());

        try {
            //create new file for opinions
            String fileName = "reviews.xml";

            PrintWriter transform = new PrintWriter(fileName);

            for(Opinia opinia : opinie){
                //save reviews to file
                transform.print(opinia.outerHtml());
                if(!opinia.equals(opinie.get(opinie.size()-1))) {
                    //if its last review not needed to add 2 new lines
                    transform.print("\r\n\r\n");
                }
            }

            //close file
            transform.close();

            System.out.println("Zapisano do pliku " + fileName);
        }
        catch (FileNotFoundException event){
            event.printStackTrace();
        }
    }
}
