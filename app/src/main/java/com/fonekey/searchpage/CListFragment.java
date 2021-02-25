package com.fonekey.searchpage;
import com.fonekey.R;

import com.fonekey.mainpage.CClient;
import com.fonekey.mainpage.CException;
import com.fonekey.mainpage.CMainActivity;
import com.fonekey.mainpage.CRecyclerAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.io.ByteArrayOutputStream;

import ru.yandex.money.android.sdk.Checkout;

public class CListFragment extends Fragment {

    SwipeRefreshLayout m_swipeRefreshLayout;
    RecyclerView m_recyclerViewList;
    TextView m_textPlugList;

    static public final int REQUEST_CODE_TOKENIZE = 33;
    static public final int REQUEST_CODE_SEND = 44;

    boolean m_success = false;

    final CSearch m_search = new CSearch();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        Intent intent = getActivity().getIntent();
        m_search.number_person = intent.getStringExtra("number_person");
        m_search.town = intent.getStringExtra("town");
        m_search.date_begin = intent.getStringExtra("data_begin");
        m_search.date_end = intent.getStringExtra("data_end");

        m_swipeRefreshLayout = view.findViewById(R.id.swipeRefreshList);
        m_recyclerViewList = view.findViewById(R.id.recycle_view);
        m_textPlugList = view.findViewById(R.id.txtPlugList);

        m_recyclerViewList.setHasFixedSize(true);
        m_recyclerViewList.setLayoutManager(new LinearLayoutManager(getActivity()));
        m_recyclerViewList.setAdapter(new CRecyclerAdapter(false, getContext(), this));

        GetListFerms();

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Intent in = getActivity().getIntent();
        if(requestCode == REQUEST_CODE_TOKENIZE && resultCode == Activity.RESULT_OK) {

            ArrayList<byte[]> data = new ArrayList<>();

            data.add(in.getByteArrayExtra("town"));
            data.add(in.getByteArrayExtra("ferm_id"));
            data.add(in.getByteArrayExtra("time_begin"));
            data.add(in.getByteArrayExtra("time_end"));
            data.add(CMainActivity.m_userId.getBytes());
            data.add(Checkout.createTokenizationResult(intent).getPaymentToken().getBytes());

            ByteArrayOutputStream message = CClient.CreateMessage(data, (byte) 0x50); // "P"
            ArrayList<ArrayList<ByteArrayOutputStream>> answer = CClient.DataExchange(message.toByteArray(), (byte) 0x50);
            if (answer != null) {
                try {
                    if (!answer.isEmpty()) {
                        ArrayList<ByteArrayOutputStream> listValue = answer.get(0);
                        if (listValue.size() == 1) {
                            byte[] arrayValue = listValue.get(0).toByteArray();
                            if (arrayValue.length == 1) {
                                if (arrayValue[0] == (byte) 0x31)
                                    throw new CException("Квартира куплена");
                                else
                                    throw new CException("Квартира не куплена");
                            }
                        }
                    }
                    throw new CException("Ошибка парсера");

                } catch (CException exc) {
                    Toast.makeText(getContext(), exc.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else
                Toast.makeText(getContext(), R.string.not_connection, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Токен не создан", Toast.LENGTH_SHORT).show();
            if(resultCode == Activity.RESULT_CANCELED)
                Toast.makeText(getContext(), "RESULT_CANCELED", Toast.LENGTH_SHORT).show();
        }
    }

    // Получения списка квартир
    private void GetListFerms() {

        m_textPlugList.setText(R.string.not_connection);
        m_recyclerViewList.setVisibility(View.INVISIBLE);
        CRecyclerAdapter adapter = (CRecyclerAdapter)m_recyclerViewList.getAdapter();
        if(adapter != null) {
            adapter.onClear();

            ArrayList<byte[]> data = new ArrayList<>();
            data.add(m_search.town.getBytes());
            data.add(m_search.number_person.getBytes());
            data.add(m_search.date_begin.getBytes());
            data.add(m_search.date_end.getBytes());

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
