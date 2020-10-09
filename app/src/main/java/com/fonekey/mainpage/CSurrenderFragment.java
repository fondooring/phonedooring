package com.fonekey.mainpage;
import com.fonekey.R;

import com.fonekey.fermpage.CFermActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.LayoutInflater;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class CSurrenderFragment extends Fragment {

    SwipeRefreshLayout m_swipeRefreshLayout;
    RecyclerView m_recyclerViewSurrender;
    TextView m_textPlugSurrender;

    int m_count = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_surrender, container, false);

        m_swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshSurrender);
        m_recyclerViewSurrender = (RecyclerView) view.findViewById(R.id.recyclerViewSurrender);
        m_textPlugSurrender = (TextView) view.findViewById(R.id.txtPlugSurrender);
        m_textPlugSurrender.setText("У вас нет арендованных квартир");

        m_recyclerViewSurrender.setVisibility(View.INVISIBLE);
        m_recyclerViewSurrender.setHasFixedSize(true);
        m_recyclerViewSurrender.setLayoutManager(new LinearLayoutManager(getActivity()));
        m_recyclerViewSurrender.setAdapter(new CSliderFermRecyclerAdapter(new ArrayList<CFermSlider>()));

        return view;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        m_swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                m_count++;
                ((CSliderFermRecyclerAdapter)m_recyclerViewSurrender.getAdapter()).onClear();

                for(int i = 0; i < m_count; i++)
                     ((CSliderFermRecyclerAdapter)m_recyclerViewSurrender.getAdapter()).onItemAdd(Integer.toString(10000 * i));

                m_textPlugSurrender.setText("");
                m_recyclerViewSurrender.setVisibility(View.VISIBLE);
                m_swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}