package com.fonekey.fermpage;
import com.fonekey.R;
import com.fonekey.mainpage.CFerm;
import com.fonekey.searchpage.CSearchActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class CFermActivity extends AppCompatActivity {

    Button btnSave;
    TextView m_street;
    TextView m_definitionFerm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_add_ferm);

        btnSave = (Button) findViewById(R.id.btnSaveFerm);
        m_street = (TextView) findViewById(R.id.txtStreetFerm);
        m_definitionFerm = (TextView) findViewById(R.id.txtDefinitionFerm);

        btnSave.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent();
                intent.putExtra("street", m_street.getText().toString());
                intent.putExtra("definition", m_definitionFerm.getText().toString());
                setResult(1889, intent);
                finish();
            }
        });

    }
}
