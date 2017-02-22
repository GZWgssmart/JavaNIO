package com.gs.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * Created by WangGenshen on 11/3/16.
 */
public class NIOClient {

    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        Selector selector = Selector.open();
        socketChannel.connect(new InetSocketAddress("localhost", 8898));
        socketChannel.register(selector, SelectionKey.OP_CONNECT);
        while (true) {
            selector.select();
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                iterator.remove();
                SocketChannel socketChannel1 = (SocketChannel) selectionKey.channel();
                if (selectionKey.isConnectable()) {
                    if (socketChannel1.isConnectionPending()) {
                        socketChannel1.finishConnect();
                    }
                    socketChannel1.configureBlocking(false);
                    socketChannel1.write(ByteBuffer.wrap("client test".getBytes()));
                    socketChannel1.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                } else if (selectionKey.isReadable()) {
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    int total = socketChannel1.read(buffer);
                    if (total > 0) {
                        byte[] data = buffer.array();
                        String msg = new String(data).trim();
                        System.out.println("客户端收到信息：" + msg);
                    }
                } else if (selectionKey.isWritable()) {
                    socketChannel1.write(ByteBuffer.wrap("client test".getBytes()));
                }
            }
        }
    }
}
