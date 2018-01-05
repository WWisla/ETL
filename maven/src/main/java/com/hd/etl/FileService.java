package com.hd.etl;

import java.io.FileOutputStream;
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

    public void print(String text) throws IOException{
        //write text to file
        PrintWriter printWriter = new PrintWriter(this.path.toString());

        printWriter.print(text);

        printWriter.close();
    }

    public void write(String text) throws IOException{
        //write text to file through file channel
        byte[] bytes = text.getBytes();
        FileOutputStream fileOutputStream = new FileOutputStream(path.toString());

        FileChannel fileChannel = fileOutputStream.getChannel();

        //load bytes to buffer
        ByteBuffer buffer = ByteBuffer.wrap(bytes);

        //write buffer to file
        fileChannel.write(buffer);

        fileChannel.close();
        fileOutputStream.close();
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
