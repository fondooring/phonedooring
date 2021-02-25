package com.fonekey.mainpage;
import com.fonekey.R;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.Navigation;
import androidx.navigation.NavController;
import androidx.navigation.ui.NavigationUI;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.ui.AppBarConfiguration;

import android.os.Bundle;
import android.view.Menu;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;

public class CMainActivity extends AppCompatActivity {

    private static final int m_layuot = R.layout.activity_main;
    private AppBarConfiguration mAppBarConfiguration;

    public static String m_userId;
    public static CClient m_client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(m_layuot);

        m_userId = "0000000000";
        m_client = new CClient();

        DrawerLayout drawer = findViewById(R.id.drawerLayout);
        NavigationView navigationView = findViewById(R.id.navigationView);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.menu_search).setDrawerLayout(drawer).build();

        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        LoadUserId();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        m_client.Close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }

    private void LoadUserId() {
        File file = new File(getExternalFilesDir(null), "registration.txt");
        if (file.exists()) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                m_userId = br.readLine();
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}