package com.fonekey.mainpage;

import java.io.InputStream;
import java.io.IOException;
import java.io.DataOutputStream;
import java.io.ByteArrayOutputStream;

import java.net.Socket;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import java.util.ArrayList;

public class CClient extends Thread {

    //private static int m_result;
    private static Socket m_socket;

    private static final String SERVER_IP = "192.168.1.66";
    private static final int SERVERPORT = 4000;
    private static final int SIZE_BUFF = 64000;

    //private static final int UNCNOWN_HOST_EXCEPTION = 2;
    //private static final int IO_EXCEPTION = 1;
    //private static final int SUCCESS = 0;
    private static final int SEC_TIMEOUT = 2000;

    private static ByteArrayOutputStream m_bufferArrayStream;

    private static class ClientThreadInit implements Runnable {

        @Override
        public void run() {
            try {
                m_bufferArrayStream = new ByteArrayOutputStream();

                m_socket = new Socket();
                m_socket.setSoTimeout(SEC_TIMEOUT);
                m_socket.connect(new InetSocketAddress(SERVER_IP, SERVERPORT), SEC_TIMEOUT);

                // m_result = SUCCESS;
            } catch (UnknownHostException e1) {
                e1.printStackTrace();
                //m_result = UNCNOWN_HOST_EXCEPTION;
            } catch (IOException e1) {
                e1.printStackTrace();
                //m_result = IO_EXCEPTION;
            }
        }
    }

    private static class ClientThreadRead implements Runnable {

        @Override
        public void run() {

            if(m_socket != null) {

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                try {
                    InputStream stream = m_socket.getInputStream();

                    int sizeRead = 0;
                    int sizeCount = 0;
                    byte[] buffer = new byte[1460];

                    while (sizeCount < SIZE_BUFF) {
                        sizeRead = stream.read(buffer);
                        m_bufferArrayStream.write(buffer, 0, sizeRead);
                        sizeCount += sizeRead;
                    }

                    //m_result = SUCCESS;
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                    //m_result = UNCNOWN_HOST_EXCEPTION;
                } catch (IOException e) {
                    e.printStackTrace();
                    //m_result = IO_EXCEPTION;
                }
            }
        }
    }

    private static class ClientThreadSend implements Runnable {

        @Override
        public void run() {

            byte[] message = m_bufferArrayStream.toByteArray();
            int size = message.length;

            if(m_socket != null && size != 0) {
                try {
                    DataOutputStream dos = new DataOutputStream(m_socket.getOutputStream());
                    dos.writeInt(size);
                    dos.write(message,0, size);
                    dos.flush();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static int StartThread(Thread thread) {
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return 1;
        }
        return 0;
    }

    public CClient() {
        StartThread(new Thread(new ClientThreadInit()));
    }

    public static ArrayList<ArrayList<ByteArrayOutputStream>> DataExchange(byte[] data, byte command)
    {
        m_bufferArrayStream.reset();
        int result = Send(data);
        if(result == 0) {
            m_bufferArrayStream.reset();
            byte[] receive = Read();
            int size = receive.length;
            if(size != 0)
                return ParseMessage(receive, size, command);
            else
                return  null;
        } else
            return null;
    }

    public static ByteArrayOutputStream CreateMessage(ArrayList<byte[]> message, byte command) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        try {

            buffer.write(new byte[]{ (byte) 0xFA, (byte) 0xFB, command });

            for(byte[] p : message) {
                buffer.write(p);
                buffer.write(new byte[]{(byte) 0xFA, (byte) 0xFB, (byte) 0x60});
            }

            buffer.write(new byte[]{(byte) 0xFA, (byte) 0xFB, (byte) 0xFF});

        } catch (IOException e) {
            e.printStackTrace();
        }

        return buffer;
    }

    private static int Send(byte[] buffer) {

        try {
            m_bufferArrayStream.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
            return 1;
        }

        return StartThread(new Thread(new ClientThreadSend()));
    }

    private static byte[] Read() {
        StartThread(new Thread(new ClientThreadRead()));
        return m_bufferArrayStream.toByteArray();
    }

    private static ArrayList<ArrayList<ByteArrayOutputStream>> ParseMessage(byte[] mesasge, int sizeMesasge, byte command) {

        ArrayList<ArrayList<ByteArrayOutputStream>> parse_mesasge = new ArrayList<>();
        ByteArrayOutputStream temp = new ByteArrayOutputStream();

        //  Защита от мусора
        int startPosition = -1;
        for(int i = 2; i < sizeMesasge; i++) {
            if(mesasge[i - 2] == (byte) 0xFA && mesasge[i - 1] == (byte) 0xFB && mesasge[i] == command) {
                startPosition = i + 1;
                break;
            }
        }
        if(startPosition == -1)
            return parse_mesasge;

        for(int position = startPosition; position < sizeMesasge; position++) {

            if(mesasge[position] == (byte) 0xFA && mesasge[position + 1] == (byte) 0xFB) {

                switch(mesasge[position + 2]) {

                    case (byte) 0x22 : {
                        position += 2;
                        if(temp.toByteArray()[0] == '0') {
                            temp.reset();
                            break;
                        } else
                            return parse_mesasge;
                    }

                    case (byte) 0x45 : {
                        position += 2;

                        int size = (temp.toByteArray()[0] << 8) | temp.toByteArray()[1];
                        if(size <= 64000) {
                            temp.reset();
                            break;
                        } else
                            return parse_mesasge;
                    }

                    case (byte) 0x78: {
                        position += 2;
                        parse_mesasge.add(new ArrayList<ByteArrayOutputStream>());
                        temp.reset();
                        break;
                    }

                    case (byte) 0x60: {
                        position += 2;
                        parse_mesasge.get(parse_mesasge.size() - 1).add(temp);
                        temp = new ByteArrayOutputStream();
                        break;
                    }

                    case (byte) 0xFF: {
                        return parse_mesasge;
                    }

                    default:
                }
            } else
                temp.write(mesasge[position]);
        }
        return parse_mesasge;
    }
}
