package com.fonekey.fermpage;
import com.fonekey.R;
import com.fonekey.mainpage.CMainActivity;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.ImageButton;
import android.widget.TextView;
import android.view.View.OnClickListener;

import java.util.Random;
import java.util.ArrayList;
import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

public class CFermActivity extends AppCompatActivity {

    static final int GALLERY_REQUEST = 1;

    TextView m_owner;
    TextView m_town;
    TextView m_streetSave;
    TextView m_houseSave;
    TextView m_geo;
    TextView m_ipHouse;
    TextView m_distanceSave;
    TextView m_numberPersonSave;
    TextView m_numberRoomsSave;
    TextView m_priceSave;
    TextView m_description;

    ArrayList<String> m_foto;
    ByteArrayOutputStream m_buffer;
    ArrayList<ByteArrayOutputStream> m_bufferFoto;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_add_ferm);

        m_foto = new ArrayList<>();
        m_bufferFoto = new ArrayList<>();
        m_buffer = new ByteArrayOutputStream();

        Button btnSave = findViewById(R.id.btnSaveFerm);
        ImageButton btnLoadFoto = findViewById(R.id.btnPhotoSave);

        m_owner = findViewById(R.id.txtOwner);
        m_town = findViewById(R.id.txtTown);
        m_streetSave = findViewById(R.id.txtStreetSave);
        m_houseSave = findViewById(R.id.txtHouseSave);
        m_geo = findViewById(R.id.txtGeo);
        m_ipHouse = findViewById(R.id.txtIpHouse);
        m_distanceSave = findViewById(R.id.txtDistanceSave);
        m_numberPersonSave = findViewById(R.id.txtNumberPersonSave);
        m_numberRoomsSave = findViewById(R.id.txtNumberRoomsSave);
        m_priceSave = findViewById(R.id.txtPriceSave);
        m_description = findViewById(R.id.txtDescription);

        m_owner.setText("Иванов Иван Иванович");
        m_town.setText("Москва");
        m_streetSave.setText("Петровская");
        m_houseSave.setText("5");
        m_geo.setText("34.4568 55.6723");
        m_ipHouse.setText("192.168.1.1");
        m_distanceSave.setText("12.5");
        m_numberPersonSave.setText("3");
        m_numberRoomsSave.setText("3");
        m_priceSave.setText("10000");
        m_description.setText("Самая лучша квартира в мире");

        btnSave.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Random random = new Random();
                try {
                    m_buffer.write("S|P|".getBytes());
                    m_buffer.write(m_numberPersonSave.getText().toString().getBytes());
                    m_buffer.write("|D|L|".getBytes());
                    m_buffer.write(m_streetSave.getText().toString().getBytes());
                    m_buffer.write("|".getBytes());
                    m_buffer.write(m_houseSave.getText().toString().getBytes());
                    m_buffer.write("|".getBytes());
                    m_buffer.write((random.nextInt(10) + "." + random.nextInt(10)).getBytes());
                    m_buffer.write("|".getBytes());
                    m_buffer.write(m_distanceSave.getText().toString().getBytes());
                    m_buffer.write("|".getBytes());
                    m_buffer.write(m_numberRoomsSave.getText().toString().getBytes());
                    m_buffer.write("|".getBytes());
                    m_buffer.write(m_priceSave.getText().toString().getBytes());
                    m_buffer.write("|R|".getBytes());
                    m_buffer.write(m_owner.getText().toString().getBytes());
                    m_buffer.write("|".getBytes());
                    m_buffer.write(m_geo.getText().toString().getBytes());
                    m_buffer.write("|".getBytes());
                    m_buffer.write(m_description.getText().toString().getBytes());
                    m_buffer.write("|F|".getBytes());
                    m_buffer.write(String.valueOf(m_bufferFoto.size()).getBytes());
                    m_buffer.write("|".getBytes());
                    for(ByteArrayOutputStream p : m_bufferFoto) {
                        m_buffer.write(p.toByteArray());
                        m_buffer.write("|".getBytes());
                    }
                    m_buffer.write("C|0|I|".getBytes());
                    m_buffer.write(m_ipHouse.getText().toString().getBytes());
                    m_buffer.write("|U|".getBytes());
                    m_buffer.write(CMainActivity.m_userId.getBytes());
                    m_buffer.write("|".getBytes());

                } catch (IOException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent();
                intent.putExtra("message", m_buffer.toByteArray());
                setResult(1889, intent);
                finish();
            }
        });

        btnLoadFoto.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case GALLERY_REQUEST:
                if (resultCode == RESULT_OK) {
                    Uri uri = imageReturnedIntent.getData();
                    if (uri != null) {
                        try {
                            InputStream inputStream = getContentResolver().openInputStream(uri);
                            if(inputStream != null) {

                                int len = 0;
                                byte[] buffer = new byte[1024];
                                ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

                                while ((len = inputStream.read(buffer)) != -1)
                                    byteBuffer.write(buffer, 0, len);

                                m_bufferFoto.add(byteBuffer);
                            }
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    }
                }
        }
    }
}