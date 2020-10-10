package com.fonekey.mainpage;
import com.fonekey.R;

import android.os.Bundle;
import android.os.Environment;
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

        //String path_dir = Environment.getExternalStorageDirectory() + "/Android/data/" + requireContext().getPackageName();
        String path_dir = Environment.getExternalStorageDirectory() + "/Android/data/" + getPackageName();

        //SharedPreferences prefs = requireContext().getSharedPreferences("fonkeySettings", MODE_PRIVATE);
        //boolean isUserRegistered = prefs.getBoolean("userRegistered", false); //False is a default value

        m_externalAppDir = new File(path_dir);
        if (!m_externalAppDir.exists())
            m_externalAppDir.mkdir();

        File externalAppFile = new File(path_dir + "/registration");
        if (!externalAppFile.exists())
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
                    File file = new File(m_externalAppDir , "registration");
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    String z = m_txtPassword.getText().toString();
                    String y = m_txtUsername.getText().toString();

                    m_txtRegistrationSuccess.setText("Регистрация пройдена");

                    // SharedPreferences.Editor editor = getContext().getSharedPreferences("fonkeySettings", MODE_PRIVATE).edit();
                    // editor.putBoolean("userRegistered", true);
                    // editor.apply();
                }
            });
        }


    }
}
