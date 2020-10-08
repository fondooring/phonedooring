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
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import java.util.ArrayList;
import java.util.List;

public class CSurrenderFragment extends Fragment {

    private List<CFerm> m_lstFerm;
    RecyclerView m_recyclerViewFerm;
    TextView m_plag_surrender;

    SwipeRefreshLayout m_swipeRefreshLayout;
    int m_count = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_surrender, container, false);

        m_swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh_surrender);
        m_lstFerm = new ArrayList<>();
        m_plag_surrender = (TextView) view.findViewById(R.id.text_plug_surrender);
        m_plag_surrender.setText("У вас нет арендованных квартир");

        FloatingActionButton btnAddFerm;
        btnAddFerm = (FloatingActionButton) view.findViewById(R.id.btnAddFerm);
        btnAddFerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                // Удаление
                //((CRecyclerAdapter)m_recyclerViewFerm.getAdapter()).onItemDel(0);

                // Добавление
                // ((CRecyclerAdapter)m_recyclerViewFerm.getAdapter()).onItemAdd();

                Intent intent = new Intent(CSurrenderFragment.this.getActivity(), CFermActivity.class);
                startActivityForResult(intent, 1);
                ((CRecyclerAdapter)m_recyclerViewFerm.getAdapter()).onItemAdd("Новый фрейм");
            }

            /*@Override
            protected void onActivityResult(int requestCode, int resultCode, Intent data) {

                DialogFragment fr = new DialogFragment();
                fr.showNow(getFragmentManager(), "onActivityResult");

                if (data == null) {
                    return;
                }

                ((CRecyclerAdapter)m_recyclerViewFerm.getAdapter()).onItemAdd("ffff234");

                //String name = data.getStringExtra("name");
                //tvName.setText("Your name is " + name);
            }*/


            /*@Override
            protected void onActivityResult(int requestCode, int resultCode, Intent data) {
                super.onActivityResult(requestCode, resultCode, data);
            }*/

        });

        m_recyclerViewFerm = (RecyclerView) view.findViewById(R.id.recycle_view);
        m_recyclerViewFerm.setVisibility(View.INVISIBLE);


        m_recyclerViewFerm.setHasFixedSize(true);
        m_recyclerViewFerm.setLayoutManager(new LinearLayoutManager(getActivity()));

        m_recyclerViewFerm.setAdapter(new CRecyclerAdapter(m_lstFerm));

        return view;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        m_swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                m_count++;
                ((CRecyclerAdapter)m_recyclerViewFerm.getAdapter()).onClear();

                for(int i = 0; i < m_count; i++) {
                    ((CRecyclerAdapter)m_recyclerViewFerm.getAdapter()).onItemAdd(Integer.toString(10000 * i));
                }

                if(m_lstFerm.size() > 0) {
                    m_recyclerViewFerm.setVisibility(View.VISIBLE);
                    m_plag_surrender.setText("");
                }

                m_swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}