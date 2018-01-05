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
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://v-ie.uek.krakow.pl/~s187086/etl_drop.php");
        String str = "";

        try {
            CloseableHttpResponse response = httpClient.execute(httpPost);
            str = EntityUtils.toString(response.getEntity());
            httpClient.close();
        }
        catch (IOException e){
            //e.printStackTrace();
        }
        return str;
    }

    public static String loadProdukty(Produkt product){
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://v-ie.uek.krakow.pl/~s187086/etl_insert_produkty.php");
        String str = "";

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("id_produktu", String.valueOf(product.getProductID())));
        params.add(new BasicNameValuePair("rodzaj", product.getProductType()));
        params.add(new BasicNameValuePair("marka", product.getBrand()));
        params.add(new BasicNameValuePair("model", product.getModel()));
        params.add(new BasicNameValuePair("uwagi", product.getNotes()));
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            CloseableHttpResponse response = httpClient.execute(httpPost);
            str = EntityUtils.toString(response.getEntity());
            httpClient.close();
        }
        catch (IOException e){
            //e.printStackTrace();
        }
        return str;
    }

    public static String loadOpinie(Opinia review){
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://v-ie.uek.krakow.pl/~s187086/etl_insert_opinie.php");
        String str = "";

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("id_opinii", String.valueOf(review.getReviewID())));
        params.add(new BasicNameValuePair("autor", review.getReviewerName()));
        params.add(new BasicNameValuePair("data", "01.01.2000"));
        params.add(new BasicNameValuePair("ocena", String.valueOf(review.getReviewScore())));
        params.add(new BasicNameValuePair("rekomendacja", review.getProductRecommendation()));
        params.add(new BasicNameValuePair("tresc", review.getReviewText()));
        params.add(new BasicNameValuePair("zalety", review.getProductPros()));
        params.add(new BasicNameValuePair("wady", review.getProductCons()));
        params.add(new BasicNameValuePair("przydatna", String.valueOf(review.getVotesYes())));
        params.add(new BasicNameValuePair("nieprzydatna", String.valueOf(review.getVotesNo())));
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            CloseableHttpResponse response = httpClient.execute(httpPost);
            str = EntityUtils.toString(response.getEntity());
            httpClient.close();
        }
        catch (IOException e){
            //e.printStackTrace();
        }
        return str;
    }

    public static String loadProduktyOpinie(Produkt product, Opinia review){
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://v-ie.uek.krakow.pl/~s187086/etl_insert_produkty_opinie.php");
        String str = "";

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("id_produktu", String.valueOf(product.getProductID())));
        params.add(new BasicNameValuePair("id_opinii", String.valueOf(review.getReviewID())));
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            CloseableHttpResponse response = httpClient.execute(httpPost);
            str = EntityUtils.toString(response.getEntity());
            httpClient.close();
        }
        catch (IOException e){
            //e.printStackTrace();
        }
        return str;
    }
}
