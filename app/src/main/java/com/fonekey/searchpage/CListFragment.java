package com.fonekey.searchpage;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.fonekey.R;
import com.fonekey.mainpage.CFerm;
import com.fonekey.mainpage.CRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

public class CListFragment extends Fragment {

    SwipeRefreshLayout m_swipeRefreshLayout;
    RecyclerView m_recyclerViewList;
    TextView m_textPlugList;
    int m_count = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        m_swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshList);
        m_recyclerViewList = (RecyclerView) view.findViewById(R.id.recycle_view);
        m_textPlugList = (TextView) view.findViewById(R.id.txtPlugList);

        // получения списка квартир

        if(true)
            m_textPlugList.setText(R.string.not_connection);

        m_recyclerViewList.setVisibility(View.INVISIBLE);
        m_recyclerViewList.setHasFixedSize(true);
        m_recyclerViewList.setLayoutManager(new LinearLayoutManager(getActivity()));
        m_recyclerViewList.setAdapter(new CRecyclerAdapter(new ArrayList<CFerm>(), getActivity()));

        for(int i = 0; i < 10; i++)
            ((CRecyclerAdapter)m_recyclerViewList.getAdapter()).onItemAdd(Integer.toString(10000 * i));

        return view;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        m_swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                m_textPlugList.setText("");
                m_recyclerViewList.setVisibility(View.VISIBLE);
                m_swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}
