package com.hd.etl;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * Created by Daniel K on 2017-12-29.
 */
public class Opinia {
    private long reviewID;
    private String reviewText;
    private float reviewScore;
    private String reviewerName;
    private String reviewDate;
    private String productRecommendation;
    private int votesYes, votesNo;
    private ArrayList<String> productPros = new ArrayList<String>();
    private ArrayList<String> productCons = new ArrayList<String>();
    //contain full review html code
    private Element review;
    private StringBuilder result = new StringBuilder();

    public Opinia(Element review){
        this.review = review;

        //get review id
        reviewID = Long.parseLong(review.select("button[data-review-id]").attr("data-review-id"));

        //get product pros from review
        Elements pros = review.select("div[class=pros-cell]").select("li");
        for(Element pro : pros){
            productPros.add(pro.text());
        }

        //get product cons from review
        Elements cons = review.select("div[class=cons-cell]").select("li");
        for(Element con : cons){
            productCons.add(con.text());
        }

        //get review body
        reviewText = review.select("p[class=product-review-body]").text();
        //get review score
        String score = review.select("span[class=review-score-count]").text();
        if(score.length()<=3){
            //for getting int score numbers
            score = score.substring(0,1);
        }
        else {
            //for getting float score numbers
            score = score.replace(',','.');
            score = score.substring(0,3);
        }
        //get score
        reviewScore = Float.valueOf(score);

        //get reviewer nickname
        reviewerName = review.select("div[class=reviewer-name-line]").text();

        //get review date and time
        reviewDate = review.select("time").attr("datetime");

        //recommending/not recommending product
        productRecommendation = review.select("em[class=product-recommended]").text();

        //get review votes (usefull/useless review)
        votesYes = Integer.valueOf(review.select("span[id^=votes-yes]").text());
        votesNo = Integer.valueOf(review.select("span[id^=votes-no]").text());

        //clear variable capturing object toString
        result.setLength(0);

        //creating toString result for this object
        result.append(reviewerName + " " + reviewDate + "\r\n");
        result.append(reviewScore + "/5.0 " + productRecommendation + "\r\n");
        result.append("✔ - " + votesYes + "\t X - " + votesNo + "\r\n");
        result.append(reviewText + "\r\n");
    }

    public String outerHtml(){
        //return full review html code
        return review.outerHtml();
    }

    public String toString(){

        if(!productPros.isEmpty()) {
            result.append("Zalety:\r\n");
        }
        for(String pro : productPros){
            result.append(pro);
            if(!pro.equals(productPros.get(productPros.size()-1))) {
                result.append(", ");
            }
        }
        if(!productPros.isEmpty()) {
            result.append("\r\n");
        }

        if(!productCons.isEmpty()) {
            result.append("Wady:\r\n");
        }
        for(String con : productCons){
            result.append(con);
            if(!con.equals(productCons.get(productCons.size()-1))){
                result.append(", ");
            }
        }
        if(!productCons.isEmpty()) {
            result.append("\r\n");
        }

        //String delimiter = "********************************************";
        //result.append("\r\n"+ delimiter + delimiter + delimiter + delimiter + delimiter +"\r\n");

        return result.toString();
    }

    public long getReviewID() {
        return reviewID;
    }

    public String getReviewText() {
        return Encoder.replaceAllSymbols(reviewText);
    }

    public float getReviewScore() {
        return reviewScore;
    }

    public String getReviewerName() {
        return Encoder.replaceAllSymbols(reviewerName);
    }

    public String getReviewDate() {
        return reviewDate;
    }

    public String getProductRecommendation() {
        return productRecommendation;
    }

    public int getVotesYes() {
        return votesYes;
    }

    public int getVotesNo() {
        return votesNo;
    }

    public String getProductPros() {
        //get pros as string
        String plus = "";
        if(!productPros.isEmpty()) {
            for (String pros : productPros) {
                plus += pros + ", ";
            }
        }
        else {
            plus = "-";
        }
        plus = Encoder.replaceAllSymbols(plus);
        return plus;
    }

    public String getProductCons() {
        //get cons as string
        String minus = "";
        if (!productCons.isEmpty()) {
            for (String cons : productCons) {
                minus += cons + ", ";
            }
        }
        else {
            minus = "-";
        }
        minus = Encoder.replaceAllSymbols(minus);
        return minus;
    }
}
