package com.hd.etl;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel K on 2017-10-28.
 */
public class Load {
    public static void dropDataBase(){
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://v-ie.uek.krakow.pl/~s187086/drop_etl.php");

        try {
            CloseableHttpResponse response = httpClient.execute(httpPost);
            //assertThat(response.getStatusLine().getStatusCode(), equalTo(200));
            System.out.println("DROPED TABLES");
            httpClient.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void load(){
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://v-ie.uek.krakow.pl/~s187086/insert_produkty.php");

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("id", "101"));
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            CloseableHttpResponse response = httpClient.execute(httpPost);
            //assertThat(response.getStatusLine().getStatusCode(), equalTo(200));
            httpClient.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
