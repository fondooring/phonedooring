package com.fonekey.mainpage;

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

public class CClient extends Thread {

    private static Socket m_socket;
    private static final int SERVERPORT = 3500;
    private static final String SERVER_IP = "192.168.1.5";

    private static String m_buffer = "";

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
                dos.writeInt(message.length);
                dos.write(message,0,message.length);
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

        Thread thrRead = new Thread(new CClient.ReadThread());
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
}
