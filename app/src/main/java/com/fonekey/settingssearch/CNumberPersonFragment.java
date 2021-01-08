package com.fonekey.settingssearch;
import com.fonekey.R;
import com.fonekey.mainpage.CSearchFragment;
import com.fonekey.searchpage.CSearch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.os.Bundle;
import android.view.View;
import android.app.Fragment;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.EditText;

public class CNumberPersonFragment extends Fragment {

    EditText m_txtPerson;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_numberperson, container, false);

        m_txtPerson = view.findViewById(R.id.txtNumberPersonSearch);
        Button btnSave = view.findViewById(R.id.btnSaveNumberPerson);
        Button btnPlus = view.findViewById(R.id.btnPlus);
        Button btnMinus = view.findViewById(R.id.btnMinus);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CSearch.number_person = m_txtPerson.getText().toString();
                CSearchFragment.RefreshValue();
            }
        });

        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = Integer.parseInt(m_txtPerson.getText().toString());
                count++;
                m_txtPerson.setText(String.valueOf(count));
            }
        });

        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = Integer.parseInt(m_txtPerson.getText().toString());
                if(count > 1)
                    count--;
                m_txtPerson.setText(String.valueOf(count));
            }
        });

        return view;
    }
}
