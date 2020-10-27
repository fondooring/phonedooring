package com.fonekey.mainpage;
import com.fonekey.searchpage.CSearch;
import com.fonekey.searchpage.CSearchActivity;
import com.fonekey.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.annotation.SuppressLint;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import androidx.fragment.app.Fragment;

public class CSearchFragment  extends Fragment {

    private static Button m_numberPerson;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_search, container, false);

        m_numberPerson = view.findViewById(R.id.btnNumberPerson);

        Button m_btnSearch = (Button) view.findViewById(R.id.btnSearch);
        m_btnSearch.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("CutPasteId")
            @Override
            public void onClick(View v) {

                //CSearch.town = ((Button) view.findViewById(R.id.btnTown)).getText().toString();
                //CSearch.number_person = ((Button) view.findViewById(R.id.btnNumberPerson)).getText().toString();
                //CSearch.date_begin = ((Button) view.findViewById(R.id.btnDate)).getText().toString();

                CSearch.town = "moscow";
                CSearch.date_begin = "0123456789";
                CSearch.date_end = "9876543210";

                Intent intent = new Intent(getActivity(), CSearchActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    static public void RefreshValue() {
        m_numberPerson.setText(CSearch.number_person);
    }
}