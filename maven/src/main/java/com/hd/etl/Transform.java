package com.hd.etl;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by Daniel K on 2017-10-28.
 */
public class Transform {
    private static ArrayList<Opinia> reviews = new ArrayList<Opinia>();

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
            String fileName = "reviews.xml";

            PrintWriter printWriter = new PrintWriter(fileName);

            for(Opinia review : reviews){
                //save reviews to file
                printWriter.print(review.outerHtml());
                if(!review.equals(reviews.get(reviews.size()-1))) {
                    //if its last review not needed to add 2 new lines
                    printWriter.print("\r\n\r\n");
                }
            }

            //close file
            printWriter.close();

            System.out.println("Zapisano do pliku " + fileName);
        }
        catch (FileNotFoundException event){
            event.printStackTrace();
        }
        return reviews;
    }

    public static String transformToString() throws UnsupportedEncodingException{
        //StringBuilder result = new StringBuilder();
        StringBuilder result2 = new StringBuilder();

        //save result to file
        try {
            String fileName = "transform.xml";

            PrintWriter printWriter = new PrintWriter(fileName);

            for(Opinia review : reviews){
                printWriter.print(review.toString());
                //
                result2.append(review.toString());
                if(!review.equals(reviews.get(reviews.size()-1))) {
                    //2 new lines between each html code
                    //
                    result2.append("\r\n\r\n\r\n");
                    printWriter.print("\r\n\r\n\r\n");
                }
            }

            printWriter.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        /**
        try {
            //connect file channel to enable fast load big files or containing
            //a lot of signs to display it on GUI - without it loading is never ending story
            Path path = Paths.get("transform.xml");
            FileChannel fileChannel = FileChannel.open(path);

            //allocate buffer - number of bytes in memory
            ByteBuffer buffer = ByteBuffer.allocate(120000);
            int bytesRead = fileChannel.read(buffer);

            //read file until end - end throw exception (-1)
            while (bytesRead != -1) {
                buffer.flip();

                //load signs one by one
                while (buffer.hasRemaining()) {
                    synchronized (buffer) {
                        //load sign to string builder
                        result.append((char) buffer.get());
                        //CharBuffer cb = Charset.defaultCharset().decode(buffer);
                        //result.append(cb.toString());
                    }
                }
                //System.out.println(fileChannel.position());
                //clear buffer - again allocated number and load again
                buffer.clear();
                bytesRead = fileChannel.read(buffer);
            }
            System.out.println("Transform:DONE");

            //close file channel
            fileChannel.close();
        }
        catch (IOException event){
            event.printStackTrace();
        }
        */
        return result2.toString();
    }
}