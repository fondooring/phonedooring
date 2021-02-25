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

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Iterator;

public class CRentFragment extends Fragment {

    SwipeRefreshLayout m_swipeRefreshLayout;
    RecyclerView m_recyclerViewRent;
    TextView m_textPlugRent;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data != null) {
            if(resultCode == 1889) {
                m_textPlugRent.setText(R.string.not_connection);
                m_recyclerViewRent.setVisibility(View.INVISIBLE);
                CRecyclerAdapter adapter = (CRecyclerAdapter)m_recyclerViewRent.getAdapter();
                if(adapter != null) {
                    adapter.onClear();

                    ArrayList<ArrayList<ByteArrayOutputStream>> answer = CClient.DataExchange(data.getByteArrayExtra("message"), (byte)0x53);
                    if(answer != null) {
                        try {
                            if(!answer.isEmpty()) {
                                ArrayList<ByteArrayOutputStream> listValue = answer.get(0);
                                if(listValue.size() == 1) {
                                    if(listValue.get(0).toByteArray()[0] == '0')
                                        throw new CException("Квартира создана.\nОбновите список");
                                    else
                                        throw new CException("Квартира не создана");
                                } else
                                    throw new CException("Ошибка парсера");
                            } else
                                throw new CException("Ошибка парсера");
                        } catch (CException error) {
                            m_textPlugRent.setText(error.getMessage());
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
        m_recyclerViewRent.setAdapter(new CRecyclerAdapter(true, getActivity(), this));

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
        CRecyclerAdapter adapter = (CRecyclerAdapter)m_recyclerViewRent.getAdapter();
        if(adapter != null) {
            adapter.onClear();
            ArrayList<byte[]> data = new ArrayList<>();
            data.add(CMainActivity.m_userId.getBytes());
            data.add("111".getBytes());

            ByteArrayOutputStream message = CClient.CreateMessage(data, (byte) 0x4F); // "O"
            ArrayList<ArrayList<ByteArrayOutputStream>> answer = CClient.DataExchange(message.toByteArray(), (byte) 0x4F);
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
                                    m_textPlugRent.setText("У вас нет сдаваемых квартир");
                                    return;
                                }
                            } else {
                                if (listValue.size() == 9) {
                                    adapter.onItemArray(listValue);
                                    countFerm++;
                                } else
                                    throw new CException("Ошибка парсера");
                            }
                        }

                        if (countFerm != numberFerm)
                            throw new CException("Ошибка парсера");

                        m_recyclerViewRent.setVisibility(View.VISIBLE);
                        m_textPlugRent.setText("");
                    } else {
                        throw new CException("Ошибка парсера");
                    }
                } catch (CException error) {
                    m_textPlugRent.setText(error.getMessage());
                    // Toast.makeText(m_context, "Ошибка парсера", Toast.LENGTH_SHORT).show();
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
