package com.hd.etl;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Daniel K on 2018-01-03.
 */
public class FileService {
    private Path path;
    private FileChannel fileChannel;
    private FileOutputStream fileOutputStream;

    public FileService(String path) throws IOException{
        this.path = Paths.get(path);

        fileOutputStream = new FileOutputStream(this.path.toString());
    }

    public void write(String text) throws IOException{
        fileChannel = fileOutputStream.getChannel();

        byte[] bytes = text.getBytes();

        ByteBuffer buffer = ByteBuffer.wrap(bytes);

        fileChannel.write(buffer);
    }

    public void read(){

    }

    public void close() throws IOException{
        fileChannel.close();
        fileOutputStream.close();
    }
}
