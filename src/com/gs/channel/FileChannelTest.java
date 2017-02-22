package com.gs.channel;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

/**
 * FileChannel可用于FileInputStream, FileOutputStream, RandomAccessFile类
 * Created by WangGenshen on 10/31/16.
 */
public class FileChannelTest {

    public static void main(String... args) throws IOException {
        FileInputStream in = new FileInputStream("test.txt");
        FileChannel fileChannel = in.getChannel();
        ByteBuffer buf = ByteBuffer.allocate(1024);
        fileChannel.read(buf);
        buf.flip(); // 写出buffer前调用，从写模式转换到读模式
        Charset  cs = Charset.defaultCharset();
        System.out.println(cs.decode(buf));
    }

}
