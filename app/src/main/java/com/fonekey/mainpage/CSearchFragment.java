package com.fonekey.mainpage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.fonekey.R;

public class CSearchFragment  extends Fragment {

    SwipeRefreshLayout mSwipeRefreshLayout;
    private ListView mListView;
    private ArrayAdapter<String> mListAdapter;
    int m_count = 1;
    TextView m_text;

    final String[] CHEESES = {
        "Abbaye de Belloc", "Abbaye du Mont des Cats", "Abertam", "Abondance", "Ackawi",
        "Acorn", "Adelost", "Affidelice au Chablis", "Afuega'l Pitu", "Airag", "Airedale",
        "Aisy Cendre", "Allgauer Emmentaler", "Alverca" };

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        m_text = (TextView) view.findViewById(R.id.textTest);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);

        return view;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                m_text.setText(CHEESES[m_count++]);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}
