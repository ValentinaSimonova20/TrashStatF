package com.startandroid.trashstatf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);//кнопка меню
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState == null){ //если ни один пункт меню не выбран, то показывается статистика
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new StatisticsFragment()).commit();
        navigationView.setCheckedItem(R.id.nav_stat);}
    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //переключатель между фрагментами
        switch (item.getItemId()) {
            case R.id.nav_addPack:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new AddPackFragment()).commit();
                break;
            case R.id.nav_advice:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new AdviceFragment()).commit();
                break;
            case R.id.nav_graph:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new GraphFragment()).commit();
                break;
            case R.id.nav_pass:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new PassFragment()).commit();
                break;
            case R.id.nav_stat:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new StatisticsFragment()).commit();
                break;
            case R.id.nav_reg:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new RegisterFragment()).commit();

        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
    }

