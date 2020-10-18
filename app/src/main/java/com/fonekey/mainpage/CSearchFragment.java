package com.fonekey.mainpage;
import com.fonekey.R;
import com.fonekey.searchpage.CSearchActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class CSearchFragment  extends Fragment {

    //private Socket m_socket;

    /*CSearchFragment(Socket socket) {
        m_socket = socket;
    }*/

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        Button m_btnSearch = (Button) view.findViewById(R.id.btnSearch);
        m_btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CClient.SendData("sad");

                /*try {
                    PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(CMainActivity.m_socket.getOutputStream())),true);
                    out.println("message");
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }*/


                //Intent intent = new Intent(getActivity(), CSearchActivity.class);
                //startActivity(intent);
            }
        });

        return view;
    }
}