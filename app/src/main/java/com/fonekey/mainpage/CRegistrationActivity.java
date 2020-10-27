package com.fonekey.mainpage;
import com.fonekey.R;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileWriter;
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

            Button m_btnOkReg = findViewById(R.id.login);
            m_btnOkReg.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onClick(View view) {

                    ArrayList<String> message = new ArrayList<>();

                    message.add("A");
                    message.add(m_txtUsername.getText().toString());
                    message.add(m_txtPassword.getText().toString());

                    String str = message.toString();
                    int result = CClient.SendData(str.substring(1, str.length() - 1).replace(", ", "|"));
                    if (result == 0) {

                        Toast.makeText(getApplicationContext(),"Сообщение отправлено", Toast.LENGTH_SHORT).show();

                        Thread thrRead = new Thread(new CClient.ReadThread());
                        thrRead.start();
                        try {
                            thrRead.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        str = CClient.GetBuffer();
                        if(!str.equals("")) {
                            Toast.makeText(getApplicationContext(),"Сообщение получено", Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();

                            char[] answer = str.toCharArray();
                            int sizeAnswer = str.length();
                            if(answer[0] != 'A')
                               return;
                            if(answer[1] != '|')
                                return;
                            if(answer[2] != '1')
                                return;
                            if(answer[3] != '|')
                                return;

                            str = "";
                            for(int i = 4; i < sizeAnswer; i++)
                                str += answer[i];

                            try {
                                boolean res = m_externalAppDir.createNewFile();
                                if(res) {
                                    try {
                                        FileWriter myWriter = new FileWriter(m_externalAppDir);
                                        myWriter.write(str);
                                        myWriter.close();
										CMainActivity.m_userId = str;
                                        m_txtRegistrationSuccess.setText("Регистрация пройдена");
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                } else
                                    m_txtRegistrationSuccess.setText("Регистрация не пройдена");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else
                            Toast.makeText(getApplicationContext(),"Сообщение не получено", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(getApplicationContext(),"Нет связи", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
