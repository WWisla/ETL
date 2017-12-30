package etl;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * Created by Daniel K on 2017-12-29.
 */
public class Opinia{
    private String reviewText;
    private float reviewScore;
    private String reviewerName;
    private String reviewDate;
    private String productRecommendation;
    private int votesYes, votesNo;
    private ArrayList<String> productPros = new ArrayList<>();
    private ArrayList<String> productCons = new ArrayList<>();
    //contain full review html code
    private Element review;

    public Opinia(Element review){
        this.review = review;

        Elements pros = review.select("div[class=pros-cell]").select("li");
        for(Element pro : pros){
            //System.out.println(pro.text() + ",");
            productPros.add(pro.text());
        }

        Elements cons = review.select("div[class=cons-cell]").select("li");
        for(Element con : cons){
            //System.out.println(con.text() + ",");
            productCons.add(con.text());
        }

        reviewText = review.select("p[class=product-review-body]").text();
        String score = review.select("span[class=review-score-count]").text();
        if(score.length()<=3){
            score = score.substring(0,1);
        }
        else {
            score = score.replace(',','.');
            score = score.substring(0,3);
        }
        reviewScore = Float.valueOf(score);
        reviewerName = review.select("div[class=reviewer-name-line]").text();
        reviewDate = review.select("time").attr("datetime");
        productRecommendation = review.select("em[class=product-recommended]").text();
        votesYes = Integer.valueOf(review.select("span[id^=votes-yes]").text());
        votesNo = Integer.valueOf(review.select("span[id^=votes-no]").text());

        System.out.println(reviewerName + " " + reviewDate);
        System.out.println(reviewScore + "/5.0 " + productRecommendation);
        System.out.println("✔ - " + votesYes + "\t X - " + votesNo);
        System.out.println(reviewText);

        if(!productPros.isEmpty()) {
            System.out.println("Zalety:");
        }
        for(String pro : productPros){
            System.out.print(pro);
            if(!pro.equals(productPros.get(productPros.size()-1))) {
                System.out.print(", ");
            }
        }
        if(!productPros.isEmpty()) {
            System.out.println();
        }

        if(!productCons.isEmpty()) {
            System.out.println("Wady:");
        }
        for(String con : productCons){
            System.out.print(con);
            if(!con.equals(productCons.get(productCons.size()-1))){
                System.out.print(", ");
            }
        }
        if(!productCons.isEmpty()) {
            System.out.println();
        }
        System.out.println("**********************************************");
    }

    public String outerHtml(){
        return review.outerHtml();
    }
}
