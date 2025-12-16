package com.zest.turnstile.scanner;

import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MockScannerServer {
    public static void main(String[] args) throws Exception {
        ServerSocket server = new ServerSocket(9000);
        System.out.println("Mock scanner listening on 9000");
        Socket s = server.accept();
        OutputStream out = s.getOutputStream();
        // packet bytes as hex
        byte[] pkt = new byte[] {(byte)0x55,(byte)0xAA,0x33,0x00,0x07,0x00,0x10,0x31,0x32,0x33,0x34,0x35,0x36,(byte)0xDC};
        Thread.sleep(1000);
        out.write(pkt);
        out.flush();
        // keep connection open for extended testing
        Thread.sleep(300000);
    }
}
