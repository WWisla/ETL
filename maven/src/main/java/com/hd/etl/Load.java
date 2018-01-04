package com.hd.etl;

import org.apache.http.HttpEntity;
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
    public static void dropDataBase(){
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://v-ie.uek.krakow.pl/~s187086/etl_drop.php");

        try {
            CloseableHttpResponse response = httpClient.execute(httpPost);
            String str = EntityUtils.toString(response.getEntity());
            System.out.println(str);
            httpClient.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void test() throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://v-ie.uek.krakow.pl/~s187086/etl_insert_produkty.php");

        try {
            CloseableHttpResponse response = httpClient.execute(httpPost);
            String str = EntityUtils.toString(response.getEntity());
            System.out.println(str);
            httpClient.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void loadProdukty(){
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://v-ie.uek.krakow.pl/~s187086/etl_insert_produkty.php");

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("id_produktu", "101"));
        params.add(new BasicNameValuePair("rodzaj", "101"));
        params.add(new BasicNameValuePair("marka", "101"));
        params.add(new BasicNameValuePair("model", "101"));
        params.add(new BasicNameValuePair("uwagi", "101"));
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            CloseableHttpResponse response = httpClient.execute(httpPost);
            String str = EntityUtils.toString(response.getEntity());
            System.out.println(str);
            httpClient.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void loadOpinie(){
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://v-ie.uek.krakow.pl/~s187086/etl_insert_produkty.php");

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("id_opinii", "101"));
        params.add(new BasicNameValuePair("autor", "101"));
        params.add(new BasicNameValuePair("data", "101"));
        params.add(new BasicNameValuePair("ocena", "101"));
        params.add(new BasicNameValuePair("rekomendacja", "101"));
        params.add(new BasicNameValuePair("tresc", "101"));
        params.add(new BasicNameValuePair("zalety", "101"));
        params.add(new BasicNameValuePair("wady", "101"));
        params.add(new BasicNameValuePair("przydatna", "101"));
        params.add(new BasicNameValuePair("nieprzydatna", "101"));
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            CloseableHttpResponse response = httpClient.execute(httpPost);
            String str = EntityUtils.toString(response.getEntity());
            System.out.println(str);
            httpClient.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void loadProduktyOpinie(){
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://v-ie.uek.krakow.pl/~s187086/etl_insert_produkty.php");

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("id_produktu", "101"));
        params.add(new BasicNameValuePair("id_opinii", "101"));
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            CloseableHttpResponse response = httpClient.execute(httpPost);
            String str = EntityUtils.toString(response.getEntity());
            System.out.println(str);
            httpClient.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
