package com.fonekey.mainpage;
import com.fonekey.R;
import com.fonekey.searchpage.CSearchActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class CSearchFragment  extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        Button m_btnSearch = (Button) view.findViewById(R.id.btnSearch);
        m_btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CSearchActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}