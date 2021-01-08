package com.fonekey.mainpage;
import com.fonekey.R;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class CRegistrationActivity extends AppCompatActivity {

    private int m_layuot = R.layout.fragment_usercard;
    File m_externalAppDir;

    Button m_btnOkReg;
    TextView m_txtPassword;
    TextView m_txtUsername;
    TextView m_txtRegistrationSuccess;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean isUserRegistered = true;

        m_externalAppDir = new File(getExternalFilesDir(null), "registration.txt");
        if (!m_externalAppDir.exists())
            isUserRegistered = false;

        if (!isUserRegistered)
            m_layuot = R.layout.activity_login;

        setContentView(m_layuot);

        if (!isUserRegistered) {
            m_txtPassword = findViewById(R.id.password);
            m_txtUsername = findViewById(R.id.username);
            m_txtRegistrationSuccess = findViewById(R.id.txtRegistrationSuccess);

            m_btnOkReg = findViewById(R.id.login);
            m_btnOkReg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ArrayList<byte[]> data = new ArrayList<>();
                    data.add(m_txtUsername.getText().toString().getBytes());
                    data.add(m_txtPassword.getText().toString().getBytes());

                    ByteArrayOutputStream message = CClient.CreateMessage(data, (byte)0x41); // "A"
                    ArrayList<ArrayList<ByteArrayOutputStream>> answer = CClient.DataExchange(message.toByteArray(), (byte)0x41);
                    if(answer != null) {
                        try {

                            if(!answer.isEmpty()) {
                                ArrayList<ByteArrayOutputStream> listValue = answer.get(0);
                                if(listValue.size() == 2) {
                                    if(listValue.get(0).toByteArray()[0] != '0')
                                        throw new CException("Ошибка сервера");

                                    try {
                                        String id = listValue.get(1).toString();
                                        if(m_externalAppDir.createNewFile()) {
                                            try {
                                                FileWriter myWriter = new FileWriter(m_externalAppDir);
                                                myWriter.write(id);
                                                myWriter.close();
                                                CMainActivity.m_userId = id;
                                                m_btnOkReg.setEnabled(false);
                                                m_txtRegistrationSuccess.setText("Регистрация пройдена");
                                            } catch (IOException e) {
                                                m_txtRegistrationSuccess.setText("Не сохранена регистрация");
                                                e.printStackTrace();
                                            }
                                        } else
                                            throw new IOException();
                                    } catch (IOException e) {
                                        m_txtRegistrationSuccess.setText("Файл регистрации не создан");
                                        e.printStackTrace();
                                    }

                                } else
                                    throw new CException("Ошибка парсера");
                            } else
                                throw new CException("Ошибка парсера");
                        } catch (CException error) {
                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else
                        Toast.makeText(getApplicationContext(), R.string.not_connection, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
