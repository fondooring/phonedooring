package com.fonekey.searchpage;
import com.fonekey.R;

import com.fonekey.mainpage.CClient;
import com.fonekey.mainpage.CException;
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

import java.util.ArrayList;
import java.io.ByteArrayOutputStream;

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
        m_recyclerViewList.setVisibility(View.INVISIBLE);
        CRecyclerAdapter adapter = (CRecyclerAdapter)m_recyclerViewList.getAdapter();
        if(adapter != null) {
            adapter.onClear();

            ArrayList<byte[]> data = new ArrayList<>();
            data.add("111".getBytes());
            data.add(CSearch.number_person.getBytes());
            //buffer.write(CSearch.date_begin.getBytes());
            data.add("20-10-2020 12:00:00".getBytes());
            //buffer.write(CSearch.date_end.getBytes());
            data.add("20-10-2020 12:00:01".getBytes());

            ByteArrayOutputStream message = CClient.CreateMessage(data, (byte) 0x4C); // "L"
            ArrayList<ArrayList<ByteArrayOutputStream>> answer = CClient.DataExchange(message.toByteArray(), (byte) 0x4C);
            if (answer != null) {
                try {
                    if(!answer.isEmpty()) {
                        for (ArrayList<ByteArrayOutputStream> listValue : answer) {
                            if (listValue.size() == 1) {
                                if (listValue.get(0).toByteArray()[0] == '0') {
                                    if (answer.size() == 1)
                                        throw new CException("Нет результатов");
                                } else
                                    throw new CException("Ошибка сервера");
                            } else {
                                if (listValue.size() == 9)
                                    adapter.onItemArray(listValue);
                                else
                                    throw new CException("Ошибка сервера");
                            }
                        }

                        if(adapter.getItemCount() > 0) {
                            m_textPlugList.setText("");
                            m_recyclerViewList.setVisibility(View.VISIBLE);
                        }
                    } else
                        throw  new CException("Ошибка парсера");
                } catch (CException ex) {
                    m_textPlugList.setText(ex.getMessage());
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
