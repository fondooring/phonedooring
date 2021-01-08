package com.fonekey.mainpage;
import com.fonekey.R;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.ArrayAdapter;
import android.graphics.BitmapFactory;

import java.util.ArrayList;

public class CViewFermActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewferm);

        final ImageView imgMain = findViewById(R.id.imgMain);
        ImageView imgFoto0 = findViewById(R.id.imgFerm0);
        ImageView imgFoto1 = findViewById(R.id.imgFerm1);
        ImageView imgFoto2 = findViewById(R.id.imgFerm2);
        ImageView imgFoto3 = findViewById(R.id.imgFerm3);
        ImageView imgFoto4 = findViewById(R.id.imgFerm4);

        TextView txtTown = findViewById(R.id.txtTownView);
        TextView txtStreet = findViewById(R.id.txtStreetView);
        TextView txtHouse = findViewById(R.id.txtHouseView);
        TextView txtOwner = findViewById(R.id.txtOwnerView);
        TextView txtRating = findViewById(R.id.txtRatingView);
        TextView txtDistance = findViewById(R.id.txtDistanceView);
        TextView txtNumberRooms = findViewById(R.id.txtNumberRoomsView);
        TextView txtGeo = findViewById(R.id.txtGeoView);
        TextView txtDescription = findViewById(R.id.txtDescriptionView);
        ListView lstComments = findViewById(R.id.lstCommentsView);

        Intent intent = getIntent();

        txtTown.setText(intent.getStringExtra("town"));
        txtStreet.setText(intent.getStringExtra("street"));
        txtHouse.setText(intent.getStringExtra("house"));
        txtOwner.setText(intent.getStringExtra("owner"));
        txtRating.setText(intent.getStringExtra("rating"));
        txtDistance.setText(intent.getStringExtra("distance"));
        txtNumberRooms.setText(intent.getStringExtra("number_rooms"));
        txtGeo.setText(intent.getStringExtra("geo"));
        txtDescription.setText(intent.getStringExtra("description"));

        byte[] tempFoto;
        int sizeFoto = 0;

        tempFoto = intent.getByteArrayExtra("foto_0");
        sizeFoto = tempFoto.length;
        imgMain.setImageBitmap(BitmapFactory.decodeByteArray(tempFoto, 0, sizeFoto));
        imgFoto0.setImageBitmap(BitmapFactory.decodeByteArray(tempFoto, 0, sizeFoto));

        tempFoto = intent.getByteArrayExtra("foto_1");
        sizeFoto = tempFoto.length;
        imgFoto1.setImageBitmap(BitmapFactory.decodeByteArray(tempFoto, 0, sizeFoto));

        tempFoto = intent.getByteArrayExtra("foto_2");
        sizeFoto = tempFoto.length;
        imgFoto2.setImageBitmap(BitmapFactory.decodeByteArray(tempFoto, 0, sizeFoto));

        tempFoto = intent.getByteArrayExtra("foto_3");
        sizeFoto = tempFoto.length;
        imgFoto3.setImageBitmap(BitmapFactory.decodeByteArray(tempFoto, 0, sizeFoto));

        tempFoto = intent.getByteArrayExtra("foto_4");
        sizeFoto = tempFoto.length;
        imgFoto4.setImageBitmap(BitmapFactory.decodeByteArray(tempFoto, 0, sizeFoto));

        ArrayList<String> comments = new ArrayList<>();

        int number_comments = intent.getIntExtra("number_comments", 0);
        if (number_comments != 0) {
            for (int i = 0; i < number_comments; i++)
                comments.add(intent.getStringExtra("comments_" + i));
        } else {
            comments.add("Нет комментариев");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, comments);
        lstComments.setAdapter(adapter);

        View.OnClickListener SwitchImage = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgMain.setImageDrawable(((ImageView)findViewById(v.getId())).getDrawable());
            }
        };

        imgFoto0.setOnClickListener(SwitchImage);
        imgFoto1.setOnClickListener(SwitchImage);
        imgFoto2.setOnClickListener(SwitchImage);
        imgFoto3.setOnClickListener(SwitchImage);
        imgFoto4.setOnClickListener(SwitchImage);
    }
}
