package com.hd.etl;

/**
 * Created by Daniel K on 2018-01-05.
 */
public class Encoder {
    public static String replaceAllSymbols(String text) {
        text = text.replace("\"","");
        text = text.replace("~","");
        text = text.replace("@","");
        text = text.replace("#","");
        text = text.replace("$","");
        text = text.replace("%","");
        text = text.replace("^","");
        text = text.replace("&","");
        text = text.replace("*","");
        text = text.replace("(","");
        text = text.replace(")","");
        text = text.replace("-","");
        text = text.replace("_","");
        text = text.replace("=","");
        text = text.replace("+","");
        text = text.replace("[","");
        text = text.replace("]","");
        text = text.replace("{","");
        text = text.replace("}","");
        text = text.replace(";","");
        text = text.replace(":","");
        text = text.replace("\'","");
        text = text.replace("\\","");
        text = text.replace("|","");
        text = text.replace("<","");
        text = text.replace(">","");
        text = text.replace("/","");
        text = text.replace("`","");
        text = text.replace("ą","a");
        text = text.replace("Ą","A");
        text = text.replace("ć","c");
        text = text.replace("Ć","C");
        text = text.replace("ę","e");
        text = text.replace("Ę","E");
        text = text.replace("ł","l");
        text = text.replace("Ł","L");
        text = text.replace("ń","n");
        text = text.replace("Ń","N");
        text = text.replace("ó","o");
        text = text.replace("Ó","O");
        text = text.replace("ś","s");
        text = text.replace("Ś","S");
        text = text.replace("ź","z");
        text = text.replace("Ź","Z");
        text = text.replace("ż","z");
        text = text.replace("Ż","z");

        return text;
    }
}
