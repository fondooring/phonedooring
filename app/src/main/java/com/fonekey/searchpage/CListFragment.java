package com.fonekey.searchpage;
import com.fonekey.R;

import com.fonekey.mainpage.CClient;
import com.fonekey.mainpage.CRecyclerAdapter;
import com.fonekey.mainpage.CSliderFermRecyclerAdapter;

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
import java.util.StringTokenizer;

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

        String answer = "";
        ArrayList<String> array = new ArrayList<>();
        ArrayList<String> message = new ArrayList<>();
        message.add("L");
        message.add(CSearch.town);
        message.add(CSearch.number_person);
        message.add(CSearch.date_begin);
        message.add(CSearch.date_end);

        String str = message.toString();
        int result = CClient.SendData(str.substring(1, str.length() - 1).replace(", ", "|"));
        if(result == 0) {
            result = CClient.ReadData();
            if(result == 0) {
                answer = CClient.GetBuffer();
                if(!answer.isEmpty()) {
                    ((CRecyclerAdapter) m_recyclerViewList.getAdapter()).onClear();
                    StringTokenizer list = new StringTokenizer(answer, "#");
                    while (list.hasMoreTokens()) {
                        StringTokenizer item = new StringTokenizer(list.nextToken(), "|");
                        while (item.hasMoreTokens()) {
                            str = item.nextToken();
                            if(str.equals("L"))
                                continue;
                            array.add(str);
                        }
                        if(!array.isEmpty()) {
                            ((CRecyclerAdapter) m_recyclerViewList.getAdapter()).onItemAdd(array);
                            array.clear();
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
