package com.fonekey.searchpage;
import com.fonekey.R;

import com.fonekey.mainpage.CClient;
import com.fonekey.mainpage.CMainActivity;
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
import java.util.StringTokenizer;

public class CListFragment extends Fragment {

    SwipeRefreshLayout m_swipeRefreshLayout;
    RecyclerView m_recyclerViewList;
    TextView m_textPlugList;

    ArrayList<ByteArrayOutputStream> m_listMessage = new ArrayList<>();

    ByteArrayOutputStream m_buffer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        m_swipeRefreshLayout = view.findViewById(R.id.swipeRefreshList);
        m_recyclerViewList = view.findViewById(R.id.recycle_view);
        m_textPlugList = view.findViewById(R.id.txtPlugList);

        m_buffer = new ByteArrayOutputStream();

        m_recyclerViewList.setHasFixedSize(true);
        m_recyclerViewList.setLayoutManager(new LinearLayoutManager(getActivity()));
        m_recyclerViewList.setAdapter(new CRecyclerAdapter(false, getActivity()));

        GetListFerms();

        return view;
    }

    // Получения списка квартир
    private void GetListFerms() {
        m_textPlugList.setText(R.string.not_connection);
        m_recyclerViewList.setVisibility(View.INVISIBLE);

        try {
            m_buffer.write("L|T|".getBytes());
            m_buffer.write("111".getBytes());
            m_buffer.write("|P|".getBytes());
            m_buffer.write(CSearch.number_person.getBytes());
            m_buffer.write("|D|".getBytes());
            //m_buffer.write(CSearch.date_begin.getBytes());
            m_buffer.write("20-10-2020 12:00:00".getBytes());
            m_buffer.write("|".getBytes());
            //m_buffer.write(CSearch.date_end.getBytes());
            m_buffer.write("20-10-2020 12:00:01".getBytes());
            m_buffer.write("|".getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        }

        String answer = "";
        ArrayList<String> array = new ArrayList<>();

        int result = CClient.SendArray(m_buffer.toByteArray());
        if(result == 0) {
            result = CClient.ReadData();
            if(result == 0) {

                byte[] answerArray = CClient.GetBufferArray();
                int sizeAnswerArray = CClient.GetSizeBufferArray();
                if(sizeAnswerArray != 0) {

                    ByteArrayOutputStream fillerFerst = new ByteArrayOutputStream();

                    for(int i = 0; i < sizeAnswerArray; i++) {

                        if(answerArray[i] == '#' || i == sizeAnswerArray - 1) {
                            byte[] item = fillerFerst.toByteArray();
                            int sizeItem = fillerFerst.size();
                            fillerFerst = new ByteArrayOutputStream();

                            if(sizeItem != 0) {

                                ByteArrayOutputStream fillerSecond = new ByteArrayOutputStream();

                                for(int j = 0; j < sizeItem; j++) {

                                    if(item[j] == '|') {
                                        m_listMessage.add(fillerSecond);
                                        fillerSecond = new ByteArrayOutputStream();
                                    } else
                                        fillerSecond.write(item[j]);
                                }
                            }

                        } else
                            fillerFerst.write(answerArray[i]);
                    }
                }

                if(m_listMessage.size() > 0) {
                    ((CRecyclerAdapter) m_recyclerViewList.getAdapter()).onItemArray(m_listMessage);
                    m_listMessage.clear();
                }

                int size = m_recyclerViewList.getAdapter().getItemCount();
                if(size > 0) {
                    m_textPlugList.setText("");
                    m_recyclerViewList.setVisibility(View.VISIBLE);
                } else {
                    m_textPlugList.setText("Нет результатов");
                    m_recyclerViewList.setVisibility(View.INVISIBLE);
                }

                //answer = CClient.GetBuffer();
                /*if(!answer.isEmpty()) {
                    ((CRecyclerAdapter) m_recyclerViewList.getAdapter()).onClear();
                    StringTokenizer list = new StringTokenizer(answer, "#");
                    while (list.hasMoreTokens()) {
                        StringTokenizer item = new StringTokenizer(list.nextToken(), "|");
                        while (item.hasMoreTokens()) {
                            String str = item.nextToken();
                            if(str.equals("L")) {
                                str = item.nextToken();
                                if(!str.equals("0"))
                                    return;
                                else
                                    break;
                            }
                            array.add(str);
                        }
                        if(!array.isEmpty()) {
                            ((CRecyclerAdapter) m_recyclerViewList.getAdapter()).onItemAdd(array);
                            array.clear();
                        }
                    }
                }*/
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
