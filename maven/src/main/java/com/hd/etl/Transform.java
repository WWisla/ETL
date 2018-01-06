package com.hd.etl;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Daniel K on 2017-10-28.
 */
public class Transform {
    private static ArrayList<Opinia> reviews = new ArrayList<Opinia>();
    private static Produkt product;

    public static Produkt transform(Document document, long id){
        //transform product info from html parsed document
        product = new Produkt(document, id);

        return product;
    }

    public static ArrayList<Opinia> transform(ArrayList<Document> docList){
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
                    //System.out.println(element.outerHtml());
                    //System.out.println(i);
                    i++;
                }
            }
        }
        //System.out.println(reviews.size());

        try {
            //create new file for opinions
            String fileName = "temp/reviews.xml";

            FileService fileService = new FileService(fileName);

            StringBuilder xml = new StringBuilder();

            for(Opinia review : reviews){
                //save reviews to file
                xml.append(review.outerHtml());
                if(!review.equals(reviews.get(reviews.size()-1))) {
                    //if its last review not needed to add 2 new lines
                    xml.append("\r\n\r\n");
                }
            }
            fileService.print(xml.toString());
        }
        catch (IOException event){
            event.printStackTrace();
        }
        return reviews;
    }

    public static String transformToString(){
        StringBuilder result = new StringBuilder();

        //save result of transform to file
        String fileName = "temp/transform.xml";

        try{
            FileService fileService = new FileService(fileName);

            StringBuilder xml = new StringBuilder();

            xml.append(product.toString());

            for(Opinia review : reviews){
                xml.append(review.toString());
                if(!review.equals(reviews.get(reviews.size()-1))) {
                    //2 new lines between each html code
                    xml.append("\r\n\r\n\r\n");
                }
            }

            fileService.print(xml.toString());
        }
        catch (IOException e){
            e.printStackTrace();
        }

        try{
            //read from file encoded data
            FileService fileService = new FileService(fileName);

            result.append(fileService.encode());
        }
        catch (IOException e){
            e.printStackTrace();
        }

        return result.toString();
    }

    public static void exportDataToCSV(){
        try {
            String fileName = "temp/" + product.getProductID() +".csv";

            FileService fileService = new FileService(fileName);

            StringBuilder text = new StringBuilder();

            text.append("\"Produkt\"");
            text.append("\n");

            text.append("\"ID produktu\";");
            text.append("\"Rodzaj produktu\";");
            text.append("\"Marka produktu\";");
            text.append("\"Model\";");
            text.append("\"Dodatkowe uwagi\";");
            text.append("\n");

            text.append("\""+ product.getProductID() + "\";");
            text.append("\""+ product.getProductType() + "\";");
            text.append("\""+ product.getBrand() + "\";");
            text.append("\""+ product.getModel() + "\";");
            text.append("\""+ product.getNotes() + "\";");
            text.append("\n\n\n");

            text.append("\"Opinie\";");
            text.append("\n");

            text.append("\"ID opinii\";");
            text.append("\"Autor opinii\";");
            text.append("\"Data wystawienia\";");
            text.append("\"Ocena\";");
            text.append("\"Rekomendacja\";");
            text.append("\"Tresc opinii\";");
            text.append("\"Zalety produktu\";");
            text.append("\"Wady produktu\";");
            text.append("\"Przydatna\";");
            text.append("\"Nieprzydatna\";");
            text.append("\n");

            for(Opinia review : reviews){
                text.append("\""+ review.getReviewID() + "\";");
                text.append("\""+ review.getReviewerName() + "\";");
                text.append("\""+ review.getReviewDate() + "\";");
                text.append("\""+ review.getReviewScore() + "\";");
                text.append("\""+ review.getProductRecommendation() + "\";");
                text.append("\""+ review.getReviewText() + "\";");
                text.append("\""+ review.getProductPros() + "\";");
                text.append("\""+ review.getProductCons() + "\";");
                text.append("\""+ review.getVotesYes() + "\";");
                text.append("\""+ review.getVotesNo() + "\";");
                text.append("\n");
            }

            fileService.print(text.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void reviewToFile(){
        for(Opinia review : reviews){
            try{
                String fileName = "temp/" + review.getReviewID() + ".txt";

                FileService fileService = new FileService(fileName);

                fileService.print(review.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
