package com.hd.etl;

import org.jsoup.nodes.Document;

/**
 * Created by Daniel K on 2018-01-04.
 */
public class Produkt {
    private long productID;
    private String productType;
    private String brand;
    private String model;
    private String notes;
    //contain full element
    private Document product;

    public Produkt(Document product){
        this.product = product;

        productType = product.select("dl[data-gacategoryname]").attr("data-gacategoryname");
        productType = productType.substring(1);
        productType = productType.replace("/",", ");

        brand = product.select("meta[property=og:brand]").attr("content");

        model = product.select("strong[class=js_searchInGoogleTooltip]").first().text();

        notes = product.select("div[class=ProductSublineTags]").text();

        System.out.println(productType);
        System.out.println(brand);
        System.out.println(model);
        System.out.println(notes);
    }
}
