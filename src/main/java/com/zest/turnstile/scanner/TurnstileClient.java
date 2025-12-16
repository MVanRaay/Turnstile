package com.zest.turnstile.scanner;

import com.zest.turnstile.config.ScannerProperties;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@Component
public class TurnstileClient {
    private final ScannerProperties props;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

    private volatile Socket socket;
    private volatile InputStream in;
    private volatile OutputStream out;
    private volatile boolean connected = false;

    private Consumer<String> qrCallback;

    public TurnstileClient(ScannerProperties props) {
        this.props = props;
    }

    public void setQrCallback(Consumer<String> qrCallback) {
        this.qrCallback = qrCallback;
    }

    public boolean isConnected() { return connected; }

    @PostConstruct
    public void start() {
        scheduler.execute(this::connectLoop);
        scheduler.scheduleAtFixedRate(this::sendHeartbeat,
                props.getHeartbeatSeconds(),
                props.getHeartbeatSeconds(),
                TimeUnit.SECONDS);
    }

    @PreDestroy
    public void shutdown() {
        scheduler.shutdownNow();
        closeSocket();
    }

    private void connectLoop() {
        while (true) {
            try {
                connect();
                readLoop();
            } catch (Exception e) {
                connected = false;
                closeSocket();
                sleep(props.getReconnectSeconds());
            }
        }
    }

    public void connect() throws IOException {
        socket = new Socket(props.getHost(), props.getPort());
        in = socket.getInputStream();
        out = socket.getOutputStream();
        connected = true;

        sendPacket((byte)0x31, new byte[]{
                (byte)0x01,
                (byte)0x00,
                (byte)0x81,
        });
    }

    public synchronized void sendPacket(byte command, byte[] data) {
        try {
            byte[] packet = buildPacket(command, data);
            out.write(packet);
            out.flush();
        } catch (IOException e) {
            throw  new RuntimeException("Failed to send packet", e);
        }
    }

    private void readLoop() throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int b;
        while ((b = in.read()) != -1) {
            buffer.write(b);
            tryParse(buffer);
        }
        throw new IOException("Disconnected");
//        try {
//
//
//            while (connected && (b = in.read()) != -1) {
//                buffer.write(b);
//
//            }
//        } catch (IOException e) {
//            if (connected) e.printStackTrace();
//        }
    }

    private void tryParse(ByteArrayOutputStream buffer) {
        byte[] arr = buffer.toByteArray();
        if (arr.length < 8) return;
        if ((arr[0] & 0xFF) != 0x55 || (arr[1] & 0xFF) != 0xAA) {
            buffer.reset();
            return;
        }
        int len = (arr[4] & 0xFF) | ((arr[5] & 0xFF) << 8);
        int total = 2 + 1 + 1 + 2 + len + 1;
        if (arr.length < total) return;
        handlePacket(arr);
        buffer.reset();
//        if (arr.length >= 6) {
//            int headerIndex = findHeader(arr);
//            if (headerIndex >= 0 && arr.length >= headerIndex + 6) {
//                int lenLow = arr[headerIndex + 4] & 0xFF;
//                int lenHigh = arr[headerIndex + 5] & 0xFF;
//                int payloadLen = lenLow + (lenHigh << 8);
//                int totalPacketLen = 2 + 1 + 1 + 2 + payloadLen + 1;
//                if (arr.length >= headerIndex + totalPacketLen) {
//                    byte[] packet = Arrays.copyOfRange(arr, headerIndex, headerIndex + totalPacketLen);
//                    handlePacket(packet);
//
//                    buffer.reset();
//                    buffer.write(arr, headerIndex + totalPacketLen, arr.length - (headerIndex + totalPacketLen));
//                }
//            }
//        }
    }

    private void handlePacket(byte[] packet) {
        if (packet[2] == 0x33 && packet[6] == 0x10 && qrCallback != null) {
            String qr = new String(packet, 7, packet.length - 8);
            qrCallback.accept(qr);
        }
    }

//    private void handlePacket(byte[] packet) {
//        byte check = packet[packet.length - 1];
//        byte computed = xorBytes(packet, 0, packet.length - 1);
//        if (computed != check) {
//            System.err.println("Bad checksum");
//            return;
//        }
//        byte cmd = packet[2];
//        if (cmd == 0x33 || cmd == 0x30) {
//            int headerLen = 2 + 1 + 1 + 2;
//            int idByte = packet[headerLen] & 0xFF;
//            if (cmd == 0x33) {
//                int dataFlag = packet[headerLen + 1] & 0xFF;
//                if (dataFlag == 0x10) {
//                    byte[] qrBytes = Arrays.copyOfRange(packet, headerLen + 2, packet.length - 1);
//                    String qr = new String(qrBytes);
//                    qrCallback.accept(qr);
//                }
//            } else {
//                byte[] qrBytes = Arrays.copyOfRange(packet, headerLen, packet.length - 1);
//                String qr = new String(qrBytes);
//                qrCallback.accept(qr);
//            }
//        }
//    }

    private int findHeader(byte[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            if ((arr[i] & 0xFF) == 0x55 && (arr[i + 1] & 0xFF) == 0xAA) return i;
        }
        return -1;
    }

    private void sendHeartbeat() {
        if (connected) {
            sendPacket((byte) 0x00, new byte[0]);
        }
    }

    private byte[] buildPacket(byte command, byte[] data) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(0x55);
        out.write(0xAA);
        out.write(command);
        out.write(0x00);
        out.write(data.length);
        out.write(0x00);
        out.write(data, 0, data.length);
        byte[] noCheck = out.toByteArray();
        byte chk = 0;
        for (byte b: noCheck) chk ^= b;
        out.write(chk);
        return out.toByteArray();
    }

    private void closeSocket() {
        try { if (socket != null) socket.close(); } catch (Exception ignored) {}
    }

    private void sleep(int seconds) {
        try { Thread.sleep(seconds * 1000L); } catch (InterruptedException ignored) {}
    }
}
