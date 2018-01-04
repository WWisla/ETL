package com.hd.etl;

import java.io.FileOutputStream;
import java.io.IOException;
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

    public void write(String text) throws IOException{
        byte[] bytes = text.getBytes();
        FileOutputStream fileOutputStream = new FileOutputStream(path.toString());

        FileChannel fileChannel = fileOutputStream.getChannel();

        ByteBuffer buffer = ByteBuffer.wrap(bytes);

        fileChannel.write(buffer);

        fileChannel.close();
        fileOutputStream.close();
    }

    public String encode() throws IOException{
        FileChannel fileChannel = FileChannel.open(path);

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int bytesRead = fileChannel.read(buffer);

        StringBuilder text = new StringBuilder();

        String encoder = System.getProperty("file.encoding");

        while (bytesRead != -1) {
            buffer.flip();

            text.append(Charset.forName(encoder).decode(buffer));

            buffer.clear();
            bytesRead = fileChannel.read(buffer);
        }

        fileChannel.close();

        return text.toString();
    }

    public String read() throws IOException{
        FileChannel fileChannel = FileChannel.open(path);

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int bytesRead = fileChannel.read(buffer);

        StringBuilder text = new StringBuilder();

        while (bytesRead != -1) {
            buffer.flip();

            while (buffer.hasRemaining()) {
                text.append((char) buffer.get());
            }

            buffer.clear();
            bytesRead = fileChannel.read(buffer);
        }

        fileChannel.close();

        return text.toString();
    }
}
