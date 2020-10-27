package com.fonekey.fermpage;
import com.fonekey.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.TextView;
import android.view.View.OnClickListener;

public class CFermActivity extends AppCompatActivity {

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
    TextView m_photo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_add_ferm);

        Button btnSave = (Button) findViewById(R.id.btnSaveFerm);

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
        m_photo = findViewById(R.id.txtPhoto);

        m_owner					.setText("Иванов Иван Иванович");
        m_town                  .setText("Москва");
        m_streetSave            .setText("Петровская");
        m_houseSave             .setText("5");
        m_geo                   .setText("34.4568 55.6723");
        m_ipHouse               .setText("192.168.1.1");
        m_distanceSave          .setText("12.5");
        m_numberPersonSave      .setText("3");
        m_numberRoomsSave       .setText("3");
        m_priceSave             .setText("10000");
        m_description           .setText("Самая лучша квартира в мире");
        m_photo                 .setText("");

        btnSave.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("owner", m_owner.getText().toString());
                intent.putExtra("town", m_town.getText().toString());
                intent.putExtra("streetSave", m_streetSave.getText().toString());
                intent.putExtra("houseSave", m_houseSave.getText().toString());
                intent.putExtra("geo", m_geo.getText().toString());
                intent.putExtra("ipHouse", m_ipHouse.getText().toString());
                intent.putExtra("distanceSave", m_distanceSave.getText().toString());
                intent.putExtra("numberPersonSave", m_numberPersonSave.getText().toString());
                intent.putExtra("numberRoomsSave", m_numberRoomsSave.getText().toString());
                intent.putExtra("priceSave", m_priceSave.getText().toString());
                intent.putExtra("description", m_description.getText().toString());
                intent.putExtra("photo", m_photo.getText().toString());
                setResult(1889, intent);
                finish();
            }
        });
    }
}
