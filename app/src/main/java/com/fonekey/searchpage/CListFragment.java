package com.fonekey.searchpage;
import com.fonekey.R;

import com.fonekey.mainpage.CClient;
import com.fonekey.mainpage.CRecyclerAdapter;

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

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class CListFragment extends Fragment {

    SwipeRefreshLayout m_swipeRefreshLayout;
    RecyclerView m_recyclerViewList;
    TextView m_textPlugList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        m_swipeRefreshLayout = view.findViewById(R.id.swipeRefreshList);
        m_recyclerViewList = view.findViewById(R.id.recycle_view);
        m_textPlugList = view.findViewById(R.id.txtPlugList);

        m_recyclerViewList.setHasFixedSize(true);
        m_recyclerViewList.setLayoutManager(new LinearLayoutManager(getActivity()));
        m_recyclerViewList.setAdapter(new CRecyclerAdapter(false, getActivity()));

        GetListFerms();

        return view;
    }

    // Получения списка квартир
    private void GetListFerms() {
        m_textPlugList.setText(R.string.not_connection);
        ((CRecyclerAdapter) m_recyclerViewList.getAdapter()).onClear();
        m_recyclerViewList.setVisibility(View.INVISIBLE);
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        try {
            buffer.write(new byte[]{(byte) 0xFA, (byte) 0xFB, (byte) 0x4C});
            buffer.write("111".getBytes());
            buffer.write(new byte[]{(byte) 0xFA, (byte) 0xFB, (byte) 0x60});
            buffer.write(CSearch.number_person.getBytes());
            buffer.write(new byte[]{(byte) 0xFA, (byte) 0xFB, (byte) 0x60});
            //buffer.write(CSearch.date_begin.getBytes());
            buffer.write("20-10-2020 12:00:00".getBytes());
            buffer.write(new byte[]{(byte) 0xFA, (byte) 0xFB, (byte) 0x60});
            //buffer.write(CSearch.date_end.getBytes());
            buffer.write("20-10-2020 12:00:01".getBytes());
            buffer.write(new byte[]{(byte) 0xFA, (byte) 0xFB, (byte) 0xFF});

        } catch (IOException e) {
            e.printStackTrace();
        }

        int result = CClient.SendArray(buffer.toByteArray());
        if(result == 0) {
            result = CClient.ReadData();
            if(result == 0) {

                byte[] answerArray = CClient.GetBufferArray();
                int sizeArray = answerArray.length;

                if(sizeArray != 0) {
                    ArrayList<ArrayList<ByteArrayOutputStream>> parse = CClient.Parse(answerArray, sizeArray, (byte) 0x4C);
                    if (!parse.isEmpty()) {
                        for (ArrayList<ByteArrayOutputStream> p : parse) {
                            ((CRecyclerAdapter) m_recyclerViewList.getAdapter()).onItemArray(p);
                        }
                    }
                }

                int size = m_recyclerViewList.getAdapter().getItemCount();
                if(size > 0) {
                    m_textPlugList.setText("");
                    m_recyclerViewList.setVisibility(View.VISIBLE);
                } else {
                    m_textPlugList.setText("Нет результатов");
                    m_recyclerViewList.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    public void onViewCreated(@NotNull View view, Bundle savedInstanceState) {
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
