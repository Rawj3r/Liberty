package com.equidais.mybeacon.controller.main;


import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.equidais.mybeacon.R;

import com.equidais.mybeacon.controller.common.BaseActivity;
import com.equidais.mybeacon.controller.common.BaseFragment;
import com.sensoro.cloud.SensoroManager;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    public static final int REQUEST_ENABLE_BT = 3;

    TabLayout tabLayout;
    Toolbar toolbar;
    ViewPager viewPager;
    private int[] imgresID = {
            R.drawable.month_view,
            R.drawable.records,
            R.drawable.phone_icon,
            R.drawable.messaging
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new CustomAdapter(getSupportFragmentManager(), getApplication()));

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
        });

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle(" ");
        toolbar.setSubtitle(" ");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);

        setNav();
        setUpTabIcons();
        beaconTest();

    }

    private void setNav() {

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        );

        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView view = (NavigationView) findViewById(R.id.nav_view);
        view.setNavigationItemSelectedListener(this);
    }

    private void setUpTabIcons() {
        tabLayout.getTabAt(0).setIcon(imgresID[0]);
        tabLayout.getTabAt(1).setIcon(imgresID[0]);
        tabLayout.getTabAt(2).setIcon(imgresID[1]);
        tabLayout.getTabAt(3).setIcon(imgresID[3]);
        tabLayout.getTabAt(4).setIcon(imgresID[2]);
    }


    private void beaconTest(){
        final SensoroManager sensoroManager = SensoroManager.getInstance(this);

        if (sensoroManager.isBluetoothEnabled()){

        }else {
            Intent bluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(bluetoothIntent, REQUEST_ENABLE_BT);
        }
    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.det){
            Intent intent = new Intent(this, DetailsActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);

        return true;

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private class CustomAdapter extends FragmentPagerAdapter {

        private String frags[] = {"Week", "Month", "Total", "Messages", "Contact us"};

        public CustomAdapter(FragmentManager fm, Context context) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position){
                case 0:
                    return new WeekFragment();
                case 1:
                    return new MonthFragment();
                case 2:
                    return new TotalFragment();
                case 3:
                    return new MessagingFragment();
                case 4:
                    return new ContactUs();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return frags.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return frags[position];
        }
    }

}
