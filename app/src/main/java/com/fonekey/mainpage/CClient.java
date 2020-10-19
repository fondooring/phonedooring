package com.fonekey.mainpage;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

public class CClient {

    private static Socket m_socket;
    private static final int SERVERPORT = 3500;
    private static final String SERVER_IP = "192.168.1.5";

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

    static int SendData(String message) {

        int result = 1;

        if(m_socket != null) {
            result = 0;
            try {
                PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(m_socket.getOutputStream())), true);
                out.println(message);
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    static String ReadData() {
        String line = "";

        if(m_socket != null) {

            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(m_socket.getInputStream(), StandardCharsets.UTF_8));
                while ((line = in.readLine()) != null) {
                    //return line;
                }

            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return line;
    }
}
