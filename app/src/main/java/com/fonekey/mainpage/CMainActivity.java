package com.fonekey.mainpage;
import com.fonekey.R;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import androidx.navigation.Navigation;
import androidx.navigation.NavController;
import androidx.navigation.ui.NavigationUI;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.ui.AppBarConfiguration;

import com.fonekey.settingssearch.CDateFragment;
import com.fonekey.settingssearch.CNumberPersonFragment;
import com.fonekey.settingssearch.CTownFragment;
import com.google.android.material.navigation.NavigationView;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class CMainActivity extends AppCompatActivity {

    private static  final int m_layuot = R.layout.activity_main;

    // private DrawerLayout drawerLayout;
    private AppBarConfiguration mAppBarConfiguration;

    FragmentTransaction m_fTrans;
    private CTownFragment m_townFragment;
    private CDateFragment m_dateFragment;
    private CNumberPersonFragment m_numberPersonFragment;

    public static CClient m_client;

    /*public static Socket m_socket;
    private static final int SERVERPORT = 3500;
    private static final String SERVER_IP = "192.168.1.4";*/


    /*class ClientThread implements Runnable {

        @Override
        public void run() {
            try {
                InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
                m_socket = new Socket(serverAddr, SERVERPORT);

            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(m_layuot);

        m_townFragment = new CTownFragment();
        m_dateFragment = new CDateFragment();
        m_numberPersonFragment = new CNumberPersonFragment();

        /*TabLayout tabLayout = (TabLayout) findViewById(R.id.mainTabLayout);
        ViewPager viewPager = (ViewPager) findViewById(R.id.mainViewPager);

        viewPager.setAdapter(new CPagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);*/

        DrawerLayout drawer = findViewById(R.id.drawerLayout);
        NavigationView navigationView = findViewById(R.id.navigationView);

        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.menu_search).setDrawerLayout(drawer).build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // new Thread(new ClientThread()).start();

        m_client = new CClient();
    }

    public void onClick(View v) {
        m_fTrans = getFragmentManager().beginTransaction();
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
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }
}