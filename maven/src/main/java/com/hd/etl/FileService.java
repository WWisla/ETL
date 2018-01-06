package com.hd.etl;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Daniel K on 2018-01-03.
 */
public class FileService {
    private Path path;

    public FileService(String path) throws IOException{
        this.path = Paths.get(path);
    }

    public static String createDir(String path){
        File file = new File(path);
        String str = "";

        if(file.mkdir()){
            str = path + " directory created.";
        }
        else {
            str = "Cannot create temporary directory!";
        }
        return str;
    }

    public static String deleteDir(String path) throws IOException{
        FileUtils.deleteDirectory(new File(path));
        return "Temporary directory has been deleted.";
    }

    public void print(String text) throws IOException{
        //write text to file
        PrintWriter printWriter = new PrintWriter(this.path.toString());

        printWriter.print(text);

        printWriter.close();
    }

    public String encode() throws IOException{
        //read file and encode it to enable eg pl characters
        FileChannel fileChannel = FileChannel.open(path);

        //allocate buffer
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int bytesRead = fileChannel.read(buffer);

        StringBuilder text = new StringBuilder();

        //get default charset
        String encoder = System.getProperty("file.encoding");

        while (bytesRead != -1) {
            //read signs from buffer
            buffer.flip();

            //decode buffer
            text.append(Charset.forName(encoder).decode(buffer));

            buffer.clear();
            bytesRead = fileChannel.read(buffer);
        }

        fileChannel.close();

        return text.toString();
    }

    public String read() throws IOException{
        //read file - enable reading big files without encoding
        FileChannel fileChannel = FileChannel.open(path);

        //allocate buffer
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int bytesRead = fileChannel.read(buffer);

        StringBuilder text = new StringBuilder();

        while (bytesRead != -1) {
            buffer.flip();

            while (buffer.hasRemaining()) {
                //load file sign by sign
                text.append((char) buffer.get());
            }

            buffer.clear();
            bytesRead = fileChannel.read(buffer);
        }

        fileChannel.close();

        return text.toString();
    }
}
