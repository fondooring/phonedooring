package com.fonekey.searchpage;
import com.fonekey.R;
import com.fonekey.mainpage.CFerm;
import com.fonekey.mainpage.CClient;
import com.fonekey.mainpage.CRecyclerAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.LayoutInflater;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

        m_swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshList);
        m_recyclerViewList = (RecyclerView) view.findViewById(R.id.recycle_view);
        m_textPlugList = (TextView) view.findViewById(R.id.txtPlugList);

        m_recyclerViewList.setHasFixedSize(true);
        m_recyclerViewList.setLayoutManager(new LinearLayoutManager(getActivity()));
        m_recyclerViewList.setAdapter(new CRecyclerAdapter(new ArrayList<CFerm>(), getActivity()));

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
                            m_textPlugList.setText("");
                            m_recyclerViewList.setVisibility(View.VISIBLE);
                            ((CRecyclerAdapter) m_recyclerViewList.getAdapter()).onItemAdd(array);
                            array.clear();
                        }
                    }
                }
            }
        }
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
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
