package com.fonekey.mainpage;
import com.fonekey.R;
import com.fonekey.fermpage.CFermActivity;
import com.fonekey.searchpage.CSearchActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import java.util.ArrayList;

public class CRentFragment extends Fragment {

    /*CRentFragment(Socket socket) {
        super(socket);
    }*/

    SwipeRefreshLayout m_swipeRefreshLayout;
    RecyclerView m_recyclerViewRent;
    TextView m_textPlugRent;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data == null)
            return;

        if( resultCode == 1889 ) {
            ((CRecyclerAdapter)m_recyclerViewRent.getAdapter()).onItemAdd(data.getStringExtra("definition"));
            m_textPlugRent.setText("");
            m_recyclerViewRent.setVisibility(View.VISIBLE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rent, container, false);

        m_swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshRent);
        m_recyclerViewRent = (RecyclerView) view.findViewById(R.id.recyclerViewRent);
        m_textPlugRent = (TextView) view.findViewById(R.id.txtPlugRent);

        // получения списка квартир

        if(true)
            m_textPlugRent.setText(R.string.not_connection);

        m_recyclerViewRent.setVisibility(View.INVISIBLE);
        m_recyclerViewRent.setHasFixedSize(true);
        m_recyclerViewRent.setLayoutManager(new LinearLayoutManager(getActivity()));
        m_recyclerViewRent.setAdapter(new CRecyclerAdapter(new ArrayList<CFerm>(), getActivity()));

        for(int i = 0; i < 10; i++)
            ((CRecyclerAdapter)m_recyclerViewRent.getAdapter()).onItemAdd(Integer.toString(10000 * i));

        FloatingActionButton btnAddFerm = (FloatingActionButton) view.findViewById(R.id.btnAddFerm);
        btnAddFerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(CRentFragment.this.getActivity(), CFermActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        return view;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        m_swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                m_textPlugRent.setText("");
                m_recyclerViewRent.setVisibility(View.VISIBLE);
                m_swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}
