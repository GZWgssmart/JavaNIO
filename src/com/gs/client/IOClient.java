package com.gs.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by WangGenshen on 11/6/16.
 */
public class IOClient {

    public static void main(String[] args) throws IOException {
        while (true) {
            Socket socket = new Socket("localhost", 8898);
            OutputStream out = socket.getOutputStream();
            out.write("你好".getBytes());
            InputStream in = socket.getInputStream();
            byte[] bytes = new byte[2014];
            in.read(bytes);
        }
    }

}
