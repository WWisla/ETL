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
    private static ArrayList<Opinia> reviews = new ArrayList<>();

    public static void transform(ArrayList<Document> docList){
        //clear list before transform
        reviews.clear();
        int i = 1;
        for (Document doc : docList) {
            //search for reviews only
            Elements review = doc.select("li");

            for (Element element : review) {
                if (element.hasClass("review-box js_product-review")) {
                    //add review to list
                    reviews.add(new Opinia(element));
                    System.out.println(element.outerHtml());
                    System.out.println(i);
                    i++;
                }
            }
        }
        //System.out.println(reviews.size());

        try {
            //create new file for opinions
            String fileName = "reviews.xml";

            PrintWriter transform = new PrintWriter(fileName);

            for(Opinia review : reviews){
                //save reviews to file
                transform.print(review.outerHtml());
                if(!review.equals(reviews.get(reviews.size()-1))) {
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
