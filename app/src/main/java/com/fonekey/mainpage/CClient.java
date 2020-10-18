package com.fonekey.mainpage;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class CClient {

    private static Socket m_socket;
    private static final int SERVERPORT = 3500;
    private static final String SERVER_IP = "192.168.1.4";

    CClient() {
        new Thread(new ClientThread()).start();
    }

    class ClientThread implements Runnable {

        @Override
        public void run() {
            try {
                InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
                m_socket = new Socket(serverAddr, SERVERPORT);

            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    static void SendData(String message) {
        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(m_socket.getOutputStream())),true);
            out.println(message);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
