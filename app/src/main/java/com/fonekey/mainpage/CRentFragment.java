package com.fonekey.mainpage;
import com.fonekey.R;
import com.fonekey.fermpage.CFermActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.View;
import android.os.Bundle;
import android.content.Intent;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.LayoutInflater;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class CRentFragment extends Fragment {

    SwipeRefreshLayout m_swipeRefreshLayout;
    RecyclerView m_recyclerViewRent;
    TextView m_textPlugRent;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data != null) {
            if(resultCode == 1889) {
                m_textPlugRent.setText(R.string.not_connection);

                String answer = "";
                ArrayList<String> array = new ArrayList<>();
                String str = data.getStringExtra("message");
                byte[] ans = data.getByteArrayExtra("message");
                int result = CClient.SendArray(ans);
                if(result == 0) {
                    result = CClient.ReadData();
                    if(result == 0) {
                        answer = CClient.GetBuffer();
                        if(!answer.isEmpty()) {
                            ((CRecyclerAdapter) m_recyclerViewRent.getAdapter()).onClear();
                            StringTokenizer list = new StringTokenizer(answer, "#");
                            while (list.hasMoreTokens()) {
                                StringTokenizer item = new StringTokenizer(list.nextToken(), "|");
                                while (item.hasMoreTokens()) {
                                    str = item.nextToken();
                                    if(str.equals("S"))
                                        continue;
                                    array.add(str);
                                }
                            }
                            if(!array.isEmpty()) {
                                if(str.equals("1"))
                                    m_textPlugRent.setText("Квартира создана");
                                else
                                    m_textPlugRent.setText("Квартира не создана");
                            }
                        }
                    }
                }
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rent, container, false);

        m_swipeRefreshLayout = view.findViewById(R.id.swipeRefreshRent);
        m_recyclerViewRent = view.findViewById(R.id.recyclerViewRent);
        m_textPlugRent = view.findViewById(R.id.txtPlugRent);

        m_recyclerViewRent.setHasFixedSize(true);
        m_recyclerViewRent.setLayoutManager(new LinearLayoutManager(getActivity()));
        m_recyclerViewRent.setAdapter(new CRecyclerAdapter(true, getActivity()));

        FloatingActionButton btnAddFerm = view.findViewById(R.id.btnAddFerm);
        btnAddFerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(CRentFragment.this.getActivity(), CFermActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        GetListFerms();

        return view;
    }

    // Получения списка квартир
    private void GetListFerms() {
        m_textPlugRent.setText(R.string.not_connection);
        m_recyclerViewRent.setVisibility(View.INVISIBLE);

        String answer = "";
        ArrayList<String> array = new ArrayList<>();
        ArrayList<String> message = new ArrayList<>();
        message.add("O");
        message.add(CMainActivity.m_userId + "|");

        String str = message.toString();
        int result = CClient.SendData(str.substring(1, str.length() - 1).replace(", ", "|"));
        if(result == 0) {
            result = CClient.ReadData();
            if(result == 0) {
                answer = CClient.GetBuffer();
                if(!answer.isEmpty()) {
                    ((CRecyclerAdapter) m_recyclerViewRent.getAdapter()).onClear();
                    StringTokenizer list = new StringTokenizer(answer, "#");
                    while (list.hasMoreTokens()) {
                        StringTokenizer item = new StringTokenizer(list.nextToken(), "|");
                        while (item.hasMoreTokens()) {
                            str = item.nextToken();
                            if(str.equals("O"))
                                continue;
                            array.add(str);
                        }
                        if(!array.isEmpty()) {
                            ((CRecyclerAdapter) m_recyclerViewRent.getAdapter()).onItemAdd(array);
                            array.clear();
                        }
                    }

                    int size = m_recyclerViewRent.getAdapter().getItemCount();
                    if(size > 0) {
                        m_textPlugRent.setText("");
                        m_recyclerViewRent.setVisibility(View.VISIBLE);
                    } else {
                        m_textPlugRent.setText("Вы не сдаете квартиры");
                        m_recyclerViewRent.setVisibility(View.INVISIBLE);
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
