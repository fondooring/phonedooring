package com.fonekey.mainpage;
import com.fonekey.R;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onClick(View view) {

                    ArrayList<String> message = new ArrayList<String>();

                    message.add("A");
                    message.add(m_txtUsername.getText().toString());
                    message.add(m_txtPassword.getText().toString());

                    int result = CClient.SendData(message.toString().replace(',', '|'));
                    if (result == 0) {
                        Toast.makeText(getApplicationContext(),"Сообщение отправлено", Toast.LENGTH_SHORT).show();
                        String answer = CClient.ReadData();
                        if(!answer.isEmpty()) {
                            Toast.makeText(getApplicationContext(),"Сообщение получено", Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(),answer, Toast.LENGTH_SHORT).show();
                            /*try {
                                boolean res = m_externalAppDir.createNewFile();
                                if(res)
                                    m_txtRegistrationSuccess.setText("Регистрация пройдена");
                                else
                                    m_txtRegistrationSuccess.setText("Регистрация не пройдена");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }*/
                        } else
                            Toast.makeText(getApplicationContext(),"Сообщение не получено", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(),"Нет связи", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
