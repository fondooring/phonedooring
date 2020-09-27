package com.fonekey.mainpage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.fonekey.R;

import java.io.File;

public class CRegistrationActivity extends Activity {

    Button m_btnOkReg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        m_btnOkReg = (Button) findViewById(R.id.login);
        m_btnOkReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CRegistrationActivity.this, CMainActivity.class);
                startActivity(intent);
                finish();
            }
        });


        File root = new File(Environment.getExternalStorageDirectory(), "registration");
        if (!root.exists()) {
            root.mkdirs();
        } else {
            Intent intent = new Intent(CRegistrationActivity.this, CMainActivity.class);
            startActivity(intent);
            finish();
        }

    }
}
