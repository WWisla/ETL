package com.hd.etl;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel K on 2017-10-28.
 */
public class Load {
    public static String dropDataBase(){
        //clear database
        CloseableHttpClient httpClient = HttpClients.createDefault();

        //connection url
        HttpPost httpPost = new HttpPost("http://v-ie.uek.krakow.pl/~s187086/etl_drop.php");

        String str = "";

        try {
            //connect to url
            CloseableHttpResponse response = httpClient.execute(httpPost);

            //get response from url
            str = EntityUtils.toString(response.getEntity());

            httpClient.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return str;
    }

    public static String loadProdukt(Produkt product){
        //load product
        CloseableHttpClient httpClient = HttpClients.createDefault();

        //connection url
        HttpPost httpPost = new HttpPost("http://v-ie.uek.krakow.pl/~s187086/etl_insert_produkty.php");

        String str = "";

        //create POST parameters list
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        params.add(new BasicNameValuePair("id_produktu", String.valueOf(product.getProductID())));
        params.add(new BasicNameValuePair("rodzaj", product.getProductType()));
        params.add(new BasicNameValuePair("marka", product.getBrand()));
        params.add(new BasicNameValuePair("model", product.getModel()));
        params.add(new BasicNameValuePair("uwagi", product.getNotes()));

        try {
            //set params
            httpPost.setEntity(new UrlEncodedFormEntity(params));

            //connect to url with post method and params
            CloseableHttpResponse response = httpClient.execute(httpPost);

            //get response from url
            str = EntityUtils.toString(response.getEntity());

            httpClient.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return str;
    }

    public static String loadOpinia(Opinia review){
        //load single review
        CloseableHttpClient httpClient = HttpClients.createDefault();

        //connection url
        HttpPost httpPost = new HttpPost("http://v-ie.uek.krakow.pl/~s187086/etl_insert_opinie.php");

        String str = "";

        //create list of params
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        params.add(new BasicNameValuePair("id_opinii", String.valueOf(review.getReviewID())));
        params.add(new BasicNameValuePair("autor", review.getReviewerName()));
        params.add(new BasicNameValuePair("data", review.getReviewDate().substring(0,10)));
        params.add(new BasicNameValuePair("godzina", review.getReviewDate().substring(11,19)));
        params.add(new BasicNameValuePair("ocena", String.valueOf(review.getReviewScore())));
        params.add(new BasicNameValuePair("rekomendacja", review.getProductRecommendation()));
        params.add(new BasicNameValuePair("tresc", review.getReviewText()));
        params.add(new BasicNameValuePair("zalety", review.getProductPros()));
        params.add(new BasicNameValuePair("wady", review.getProductCons()));
        params.add(new BasicNameValuePair("przydatna", String.valueOf(review.getVotesYes())));
        params.add(new BasicNameValuePair("nieprzydatna", String.valueOf(review.getVotesNo())));

        try {
            //set params
            httpPost.setEntity(new UrlEncodedFormEntity(params));

            //connect to url
            CloseableHttpResponse response = httpClient.execute(httpPost);

            //get response from url
            str = EntityUtils.toString(response.getEntity());

            if(str.equals("Eh znowu :(")){
                str = " TEN SIĘ ZEPSUŁ (UŻYŁBYM INNEGO SŁOWA)\r\n" + review.toString();
            }

            httpClient.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return str;
    }

    public static String loadProduktOpinia(Produkt product, Opinia review){
        //load relation between product-review
        CloseableHttpClient httpClient = HttpClients.createDefault();

        //connection url
        HttpPost httpPost = new HttpPost("http://v-ie.uek.krakow.pl/~s187086/etl_insert_produkty_opinie.php");

        String str = "";

        //create params
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        params.add(new BasicNameValuePair("id_produktu", String.valueOf(product.getProductID())));
        params.add(new BasicNameValuePair("id_opinii", String.valueOf(review.getReviewID())));

        try {
            //set params
            httpPost.setEntity(new UrlEncodedFormEntity(params));

            //connect to url
            CloseableHttpResponse response = httpClient.execute(httpPost);

            //get response
            str = EntityUtils.toString(response.getEntity());

            httpClient.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return str;
    }

    public static String load(Produkt product, ArrayList<Opinia> reviews){
        String str = "Dodawanie do bazy danych zakończone.\r\n\r\n";
        //Try load product to database
        if(Load.loadProdukt(product).equals("Brawo :)")){
            str += "Dodano nowy wiersz do tabeli etl_produkty.\r\n";
        }
        else{
            str += "Nie udało dodać się wiersza do tabeli etl_produkty.\r\n";
            str += "Sprawdź czy produkt już nie istnieje w bazie danych.\r\n\r\n";
        }

        int addedReviews = 0;
        int addedRelations = 0;
        int notAddedReviews = 0;
        int notAddedRelations = 0;

        for(Opinia review : reviews){
            if(Load.loadOpinia(review).equals("Brawo :)")){
                addedReviews++;
            }
            else {
                notAddedReviews++;
            }
            //try load relation between product-review to database
            if(Load.loadProduktOpinia(product, review).equals("Brawo :)")){
                addedRelations++;
            }
            else {
                notAddedRelations++;
            }
        }

        if(notAddedReviews == 0) {
            str += "Dodano " + addedReviews + " wierszy do tabeli etl_opinie.\r\n";
        }
        else {
            str += "Dodano " + addedReviews + " wierszy do tabeli etl_opinie.\r\n";
            str+= "Nie dodano " + notAddedReviews + " wierszy do tabeli etl_opinie.\r\n\r\n";
        }

        if (notAddedRelations == 0) {
            str += "Dodano " + addedRelations + " wierszy do tabeli etl_produkty_opinie.\r\n\r\n";
        }
        else {
            str += "Dodano " + addedRelations + " wierszy do tabeli etl_produkty_opinie.\r\n";
            str+= "Nie dodano " + notAddedRelations + " wierszy do tabeli etl_produkty_opinie.\r\n\r\n";
        }

        try {
            FileService.deleteDir("temp");
            str += "Usunięto pliki tymczasowe.\r\n";
        } catch (IOException e) {
            e.printStackTrace();
        }

        return str;
    }
}
