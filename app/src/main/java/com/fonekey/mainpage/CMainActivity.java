package com.fonekey.mainpage;
import com.fonekey.R;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.app.FragmentTransaction;

import androidx.navigation.Navigation;
import androidx.navigation.NavController;
import androidx.navigation.ui.NavigationUI;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.ui.AppBarConfiguration;

import com.fonekey.searchpage.CSearch;
import com.fonekey.settingssearch.CDateFragment;
import com.fonekey.settingssearch.CTownFragment;
import com.fonekey.settingssearch.CNumberPersonFragment;
import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;

public class CMainActivity extends AppCompatActivity {

    private static final int m_layuot = R.layout.activity_main;
    private CTownFragment m_townFragment;
    private CDateFragment m_dateFragment;
    private CNumberPersonFragment m_numberPersonFragment;
    private AppBarConfiguration mAppBarConfiguration;

    public static CClient m_client;
    public CSearch m_search;
    public static String m_userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(m_layuot);

        m_search = new CSearch();
        m_client = new CClient();
        m_townFragment = new CTownFragment();
        m_dateFragment = new CDateFragment();
        m_numberPersonFragment = new CNumberPersonFragment();

        DrawerLayout drawer = findViewById(R.id.drawerLayout);
        NavigationView navigationView = findViewById(R.id.navigationView);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.menu_search).setDrawerLayout(drawer).build();

        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        LoadUserId();
    }

    public void onClick(View v) {
        FragmentTransaction m_fTrans = getFragmentManager().beginTransaction();
        switch (v.getId()) {
            case R.id.btnTown:
                m_fTrans.replace(R.id.frgmCont, m_townFragment);
                break;
            case R.id.btnDate:
                m_fTrans.replace(R.id.frgmCont, m_dateFragment);
                break;
            case R.id.btnNumberPerson:
                m_fTrans.replace(R.id.frgmCont, m_numberPersonFragment);
            default:
                break;
        }
        m_fTrans.commit();
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