package com.joebrothers.chapter04;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class PlainOioServer {
    public void serve(int port) throws IOException {
        final ServerSocket socket = new ServerSocket(port);
        try {
            for (; ; ) {
                final Socket clientSocket = socket.accept();
                System.out.println("Accepted connection from" + clientSocket);

                new Thread(() -> {
                    final OutputStream out;
                    try {
                        out = clientSocket.getOutputStream();
                        out.write("Hi!\r\n".getBytes(StandardCharsets.UTF_8));
                        out.flush();
                        clientSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            clientSocket.close();
                        } catch (IOException ex) {
                            // ignore
                        }
                    }
                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
