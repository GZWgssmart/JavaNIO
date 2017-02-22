package com.gs.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * Created by WangGenshen on 10/31/16.
 */
public class NIOServer {


    public static void main(String[] args) {
        try {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress(8898));
            serverSocketChannel.configureBlocking(false);
            Selector selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            while (true) {
                if (selector.select() > 0) {
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        SelectionKey selectionKey = iterator.next();
                        iterator.remove();
                        if (selectionKey.isAcceptable()) {
                            ServerSocketChannel serverSocketChannel1 = (ServerSocketChannel) selectionKey.channel();
                            SocketChannel socketChannel = serverSocketChannel1.accept();
                            socketChannel.configureBlocking(false);
                            socketChannel.register(selector, SelectionKey.OP_READ);
                            socketChannel.write(ByteBuffer.wrap(new String("向客户端发送了一条信息").getBytes()));
                        } else if (selectionKey.isReadable()) {
                            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                            ByteBuffer buffer = ByteBuffer.allocate(10);
                            int total = socketChannel.read(buffer);
                            if (total > 0) {
                                byte[] data = buffer.array();
                                String msg = new String(data).trim();
                                System.out.println("服务端收到信息：" + msg);
                                ByteBuffer outBuffer = ByteBuffer.wrap(msg.getBytes());
                                socketChannel.write(outBuffer);
                            }
                            selectionKey.interestOps(SelectionKey.OP_READ);
                            socketChannel.register(selector, SelectionKey.OP_WRITE);
                        } else if (selectionKey.isWritable()) {
                            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                            socketChannel.write(ByteBuffer.wrap(new String("aaa").getBytes()));
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
