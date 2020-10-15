package com.fonekey.mainpage;
import com.fonekey.R;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.IOException;

public class CRegistrationActivity extends AppCompatActivity {

    private int m_layuot = R.layout.fragment_usercard;
    File m_externalAppDir;

    TextView m_txtPassword;
    TextView m_txtUsername;
    TextView m_txtRegistrationSuccess;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean isUserRegistered = true;

        m_externalAppDir = new File(getExternalFilesDir(null), "registration");
        if (!m_externalAppDir.exists())
            isUserRegistered = false;

        if (!isUserRegistered)
            m_layuot = R.layout.activity_login;

        setContentView(m_layuot);

        if (!isUserRegistered) {
            m_txtPassword = findViewById(R.id.password);
            m_txtUsername = findViewById(R.id.username);
            m_txtRegistrationSuccess = findViewById(R.id.txtRegistrationSuccess);

            Button m_btnOkReg = findViewById(R.id.login);
            m_btnOkReg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        boolean result = m_externalAppDir.createNewFile();
                        if(result)
                            m_txtRegistrationSuccess.setText("Регистрация пройдена");
                        else
                            m_txtRegistrationSuccess.setText("Регистрация не пройдена");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    String z = m_txtPassword.getText().toString();
                    String y = m_txtUsername.getText().toString();
                }
            });
        }
    }
}
