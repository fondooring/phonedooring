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

import java.util.ArrayList;
import java.util.Iterator;
import java.io.ByteArrayOutputStream;

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

    // Получения списка ключей
    private void GetListFerms() {
        m_textPlugSurrender.setText("Ошибка программы.\nПерезагрузите её");
        m_recyclerViewSurrender.setVisibility(View.INVISIBLE);
        CSliderFermRecyclerAdapter adapter = (CSliderFermRecyclerAdapter)m_recyclerViewSurrender.getAdapter();
        if(adapter != null) {
            adapter.onClear();
            m_textPlugSurrender.setText(R.string.not_connection);

            ArrayList<byte[]> data = new ArrayList<>();
            data.add(CMainActivity.m_userId.getBytes());

            ByteArrayOutputStream message = CClient.CreateMessage(data, (byte) 0x4B); // "K"
            ArrayList<ArrayList<ByteArrayOutputStream>> answer = CClient.DataExchange(message.toByteArray(), (byte) 0x4B);
            if (answer != null) {
                try {
                    if (!answer.isEmpty()) {
                        Iterator<ArrayList<ByteArrayOutputStream>> itr = answer.iterator();
                        int countFerm = 0;
                        int numberFerm = 0;
                        while (itr.hasNext()) {
                            ArrayList<ByteArrayOutputStream> listValue = itr.next();
                            if (listValue.size() == 2) {
                                if (listValue.get(0).toByteArray()[0] != '0')
                                    throw new CException("Ошибка сервера");

                                numberFerm = Integer.parseInt(listValue.get(1).toString());
                                if (numberFerm == 0) {
                                    m_textPlugSurrender.setText("У вас нет ключей от квартир");
                                    return;
                                }
                            } else {
                                if (listValue.size() == 4) {
                                    adapter.onItemAdd(listValue);
                                    countFerm++;
                                } else
                                    throw new CException("Ошибка парсера");
                            }
                        }

                        if (countFerm != numberFerm)
                            throw new CException("Ошибка парсера");

                        m_recyclerViewSurrender.setVisibility(View.VISIBLE);
                        m_textPlugSurrender.setText("");

                    } else {
                        throw new CException("Ошибка парсера");
                    }
                } catch (CException error) {
                    m_textPlugSurrender.setText(error.getMessage());
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