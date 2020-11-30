package com.fonekey.mainpage;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.Socket;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class CClient extends Thread {

    private static final int SIZE_BUFF = 64000;

    private static Socket m_socket;
    private static final int SERVERPORT = 4000;
    private static final String SERVER_IP = "192.168.1.66";

    private static String m_buffer = "";

    private static ByteArrayOutputStream m_bufferArrayStream;

    CClient() {
        new Thread(new ClientThread()).start();
    }

    static class ClientThread implements Runnable {

        @Override
        public void run() {
            try {
                InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
                m_socket = new Socket(serverAddr, SERVERPORT);
                m_socket.setSoTimeout(2000);

            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    static class ReadThread implements Runnable {

        @Override
        public void run() {

            m_buffer = "";
            if(m_socket != null) {

                try {
                    BufferedReader in = new BufferedReader(new InputStreamReader(m_socket.getInputStream(), "UTF-8"));
                    m_buffer = in.readLine();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class ReadThreadArray implements Runnable {

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
                    m_bufferArrayStream = new ByteArrayOutputStream();

                    int sizeRead = 0;
                    int sizeCount = 0;
                    byte[] buffer = new byte[1460];

                    while (sizeCount < SIZE_BUFF) {
                        sizeRead = stream.read(buffer);
                        m_bufferArrayStream.write(buffer, 0, sizeRead);
                        sizeCount += sizeRead;
                    }

                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static int SendData(String message) {

        int result = 1;

        if(m_socket != null) {
            try {
                PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(m_socket.getOutputStream())), true);
                out.println(message);
                result = 0;
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public static int SendArray(byte[] message) {
        int result = 1;

        if(m_socket != null) {
            try {
                DataOutputStream dos = new DataOutputStream(m_socket.getOutputStream());
                dos.writeInt(message.length + 3);
                dos.write(message,0, message.length);
                dos.flush();
                result = 0;
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public static int ReadData() {

        Thread thrRead = new Thread(new CClient.ReadThreadArray());
        thrRead.start();
        try {
            thrRead.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return 1;
        }

        return 0;
    }

    public static String GetBuffer() {
        return  m_buffer;
    }

    public static byte[] GetBufferArray() {
        return m_bufferArrayStream.toByteArray();
    }

    public static ArrayList<ArrayList<ByteArrayOutputStream>> Parse(byte[] mesasge, int sizeMesasge, byte command)
    {
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
                        parse_mesasge.get(parse_mesasge.size() - 1).add(temp);
                        return parse_mesasge;
                    }
                }
            } else
                temp.write(mesasge[position]);
        }
        return parse_mesasge;
    }
}
