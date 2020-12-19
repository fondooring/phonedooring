package com.fonekey.mainpage;
import com.fonekey.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.LayoutInflater;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class CSurrenderFragment extends Fragment {

    TextView m_textPlugSurrender;
    RecyclerView m_recyclerViewSurrender;
    SwipeRefreshLayout m_swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_surrender, container, false);

        m_textPlugSurrender = view.findViewById(R.id.txtPlugSurrender);
        m_swipeRefreshLayout = view.findViewById(R.id.swipeRefreshSurrender);
        m_recyclerViewSurrender = view.findViewById(R.id.recyclerViewSurrender);

        m_recyclerViewSurrender.setHasFixedSize(true);
        m_recyclerViewSurrender.setLayoutManager(new LinearLayoutManager(getActivity()));
        m_recyclerViewSurrender.setAdapter(new CSliderFermRecyclerAdapter(getActivity()));

        GetListFerms();

        return view;
    }

    // Получения списка квартир
    private void GetListFerms() {
        m_textPlugSurrender.setText(R.string.not_connection);
        m_recyclerViewSurrender.setVisibility(View.INVISIBLE);

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            buffer.write(new byte[]{(byte) 0xFA, (byte) 0xFB, (byte) 0x4B});
            buffer.write(CMainActivity.m_userId.getBytes());
            buffer.write(new byte[]{(byte) 0xFA, (byte) 0xFB, (byte) 0xFF});

        } catch (IOException e) {
            e.printStackTrace();
        }

        CClient.SetBufferArray(buffer.toByteArray());
        int result = CClient.SendData2();
        if(result == 0) {
            result = CClient.ReadData();
            if(result == 0) {
                byte[] answerArray = CClient.GetBufferArray();
                int sizeArray = answerArray.length;

                if(sizeArray != 0) {
                    ArrayList<ArrayList<ByteArrayOutputStream>> parse = CClient.Parse(answerArray, sizeArray, (byte) 0x4B);
                    if (!parse.isEmpty()) {
                        for (ArrayList<ByteArrayOutputStream> p : parse) {
                            ((CSliderFermRecyclerAdapter) m_recyclerViewSurrender.getAdapter()).onItemAdd(p);
                        }
                    }
                }

                int size = m_recyclerViewSurrender.getAdapter().getItemCount();
                if(size > 0) {
                    m_textPlugSurrender.setText("");
                    m_recyclerViewSurrender.setVisibility(View.VISIBLE);
                } else {
                    m_textPlugSurrender.setText("У вас нет арендованных квартир");
                    m_recyclerViewSurrender.setVisibility(View.INVISIBLE);
                }

            }
        }
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        m_swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                GetListFerms();
                m_swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}