package com.fonekey.mainpage;
import com.fonekey.R;
import com.fonekey.searchpage.CSearchActivity;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class CRentFragment extends Fragment {

    TextView m_txtMessage;
    TextView m_txtData;
    private Socket m_socket;
    private static final int SERVERPORT = 3500;
    private static final String SERVER_IP = "45.134.60.232";

    public CRentFragment() {
    };

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rent, container, false);

        Button btnSend = (Button) view.findViewById(R.id.btnSend);
        m_txtMessage = (TextView) view.findViewById(R.id.txtTown);
        m_txtData = (TextView) view.findViewById(R.id.txtDate);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "onClick:", Toast.LENGTH_SHORT).show();
                SendData(m_txtMessage.getText().toString());
            }
        });

        new Thread(new ClientThread()).start();

        return view;
    }

    public void SendData(String message) {
        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(m_socket.getOutputStream())),true);
            out.println(message);

            /*String response = new String();
            BufferedReader in = new BufferedReader(new InputStreamReader(m_socket.getInputStream(), "UTF-8"));

            Toast.makeText(this.getActivity(), "start", Toast.LENGTH_SHORT).show();

            while (in.ready()) {
                response = in.readLine();
                Toast.makeText(this.getActivity(), response, Toast.LENGTH_SHORT).show();
            }

            //while(in.ready() && (response = in.readLine()) != null) {
              //Toast.makeText(this.getActivity(), response, Toast.LENGTH_SHORT).show();
            //}

            //while ((response = in.readLine()) != null) {
              //  Toast.makeText(this.getActivity(), response, Toast.LENGTH_SHORT).show();
            //}

            Toast.makeText(this.getActivity(), "stop", Toast.LENGTH_SHORT).show();*/

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}