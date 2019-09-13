package com.demomvvm.school.Activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.SystemClock;

import com.demomvvm.school.SchoolApplication;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.demomvvm.school.R;
import com.demomvvm.school.databinding.ActivityMainBinding;
import com.demomvvm.school.fragment.ProfileFragment;
import com.demomvvm.school.fragment.StoreListFragment;
import com.demomvvm.school.util.Preferences;
import com.demomvvm.school.util.Utils;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {


    private SharedPreferences.OnSharedPreferenceChangeListener listener;

    private String fromDtaeCalc = "";
    private String toDateCalc = "";
    private String cityName = "";

    private int MAX_CLICK_INTERVAL = 1500;
    private long mLastClickTime = 0;
    private ActivityMainBinding binding;

    private TextView txtDrawerUserName;
    private TextView txtDrawerUserEmail;


    public static String getDateTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        return simpleDateFormat.format(date);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setLanguage(this, Preferences.readString(this, Preferences.SELECTED_LANGUAGE_PREFIX, "en"));
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        initComponents();
        FirebaseApp.initializeApp(MainActivity.this);


    }

    @Override
    public void initComponents() {

        SchoolApplication.getmInstance().setActivity(MainActivity.this);


        binding.rlLogout.setOnClickListener(this);
        binding.navView.setNavigationItemSelectedListener(this);

        setUpDrawerSync();
        initToolbar();

        setUpHeaderData();
        userChangeListner();

        Preferences.getPreferences(MainActivity.this).registerOnSharedPreferenceChangeListener(listener);
        rateUsDialog();
        homeFragmentCall();

    }

    private void initToolbar() {


        setSupportActionBar(binding.toolbar);
        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
            actionBar.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");

        }

    }


    private void userChangeListner() {

        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                // Implementation
                if ((key.equals(Preferences.USER_NAME)) || (key.equals(Preferences.USER_EMAIL_ID))) {
                    txtDrawerUserName.setText(Preferences.readString(MainActivity.this, Preferences.USER_NAME, ""));
                    txtDrawerUserEmail.setText(Preferences.readString(MainActivity.this, Preferences.USER_EMAIL_ID, ""));
                }
            }
        };

    }

    private void setUpHeaderData() {

        if (Preferences.readBoolean(getApplicationContext(), Preferences.SELECTEDLOCATIONORCITY, false)) {
            cityName = Preferences.readString(this, Preferences.SELECTED_CURRENT_CITY, "");
        } else {
            cityName = Preferences.readString(this, Preferences.SELECTED_CITY, "");
        }


        View header = binding.navView.getHeaderView(0);

        txtDrawerUserName = (TextView) header.findViewById(R.id.txtDrawerUserName);
        txtDrawerUserEmail = (TextView) header.findViewById(R.id.txtDrawerUserEmail);


        if (!Preferences.readString(MainActivity.this, Preferences.USER_ID, "").equals("")) {
            txtDrawerUserName.setText(Preferences.readString(MainActivity.this, Preferences.USER_NAME, ""));
            txtDrawerUserEmail.setText(Preferences.readString(MainActivity.this, Preferences.USER_EMAIL_ID, ""));

            binding.navView.getMenu().clear();
            binding.navView.inflateMenu(R.menu.activity_main_drawer);
        } else {
            txtDrawerUserName.setText(getString(R.string.user_name));
            txtDrawerUserEmail.setText(getString(R.string.welcome_guest));
            binding.navView.getMenu().clear();
            binding.navView.inflateMenu(R.menu.activity_main_drawer_logout);
        }

    }

    private void setUpDrawerSync() {


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                Utils.hideKeyboard(MainActivity.this);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                Utils.hideKeyboard(MainActivity.this);
            }
        };

        //Setting the actionbarToggle to drawer layout
        binding.drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();


    }

    private void rateUsDialog() {


//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
//        if (prev != null) {
//            ft.remove(prev);
//        }
//        ft.addToBackStack(null);
//        RateUsDialogFragment rateUsDialogFragment = new RateUsDialogFragment();
//        int daysDiff = getDateDifferent();
//        String dialogStatus = Preferences.readString(MainActivity.this, Preferences.RATE_US_DIALOG_STATUS, "");
//        if (toDateCalc.isEmpty()) {
//            rateUsDialogFragment.show(ft, "dialog");
//        }
//        if (((daysDiff > 1) && (Integer.parseInt(dialogStatus) == 1)) || (daysDiff == 1)) {
//            rateUsDialogFragment.show(ft, "dialog");
//        }
    }

    public int getDateDifferent() {
        int days = 0;

        int startDate = 0;
        int endDate = 0;
        fromDtaeCalc = getDateTime() + "";
        toDateCalc = Preferences.readString(MainActivity.this, Preferences.RATE_US_DIALOG_SHOW_LAST_DATE, "");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date Date1 = null, Date2 = null;
        try {
            Date1 = sdf.parse(toDateCalc);
            Date2 = sdf.parse(fromDtaeCalc);
            days = (int) ((Date2.getTime() - Date1.getTime()) / (24 * 60 * 60 * 1000));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return days;
    }

    @Override
    public void onClick(View v) {


        if (SystemClock.elapsedRealtime() - mLastClickTime < MAX_CLICK_INTERVAL) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();

        switch (v.getId()) {
            case R.id.toolbar:
//                Intent locationIntent = new Intent(MainActivity.this, SelectLocationActivity.class);
//                startActivity(locationIntent);
                break;

            case R.id.rlLogout:
                onLogOutClicked();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (binding.searchView.isSearchOpen()) {
            binding.searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            binding.toolbar.setOnClickListener(this);
            homeFragmentCall();

        } else if (id == R.id.nav_profile) {

            binding.toolbar.setOnClickListener(null);
            changeFragment(new ProfileFragment(), false);
            changeTitle(false, getString(R.string.Profile));


        } else if (id == R.id.nav_offers) {
            binding.toolbar.setOnClickListener(null);
            changeFragment(new StoreListFragment(), false);
            changeTitle(false, getString(R.string.Offers));

        } else if (id == R.id.nav_booking) {
            binding.toolbar.setOnClickListener(null);
            changeFragment(new StoreListFragment(), false);
            changeTitle(false, getString(R.string.Bookings));

        } else if (id == R.id.nav_setting) {
            binding.toolbar.setOnClickListener(null);
            changeFragment(new StoreListFragment(), false);
            changeTitle(false, getString(R.string.action_settings));


        } else if (id == R.id.nav_notification) {
            binding.toolbar.setOnClickListener(null);
            // changeFragment(new NotificationListFragment(), false);
            changeTitle(false, getString(R.string.notification));


        }
        else if (id == R.id.nav_about) {
            binding.toolbar.setOnClickListener(null);
            changeFragment(new StoreListFragment(), false);
            changeTitle(false, getString(R.string.AboutUs));


        } else if (id == R.id.nav_feedback) {
            binding.toolbar.setOnClickListener(null);
            changeFragment(new StoreListFragment(), false);
            changeTitle(false, getString(R.string.feedback));


        } else if (id == R.id.nav_login) {
            binding.toolbar.setOnClickListener(null);
            Preferences.writeString(this, Preferences.IS_SKIPED, "");
            startActivity(new Intent(this, LoginActivity.class));

        }


        Utils.hideKeyboard(MainActivity.this);
        binding.drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    public void homeFragmentCall() {

        binding.toolbar.setOnClickListener(this);
        changeFragment(new StoreListFragment(), false);


        if (Preferences.readBoolean(MainActivity.this, Preferences.SELECTEDLOCATIONORCITY, false)) {

            changeTitle(true, Preferences.readString(this, Preferences.SELECTED_CURRENT_CITY, ""));
        } else {
            changeTitle(true, Preferences.readString(this, Preferences.SELECTED_CITY, ""));
        }


    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        int id = item.getItemId();

        if (id == android.R.id.home) {

            if (getFragmentManager().getBackStackEntryCount() > 0) {
                getFragmentManager().popBackStack();
            } else {
                binding.drawerLayout.openDrawer(GravityCompat.START);
            }

        }

        return super.onOptionsItemSelected(item);


    }


    public void changeTitle(boolean isComp, String title) {


        getSupportActionBar().setTitle("" + title);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        binding.toolbar.setTitle("" + title);
        binding.txtTitle.setText("" + title);

        if (isComp) {
            binding.txtTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_location_edit, 0);
        } else {
            binding.txtTitle.setCompoundDrawables(null, null, null, null);
        }

    }


    public void changeTitleBack(boolean isComp, String title) {

        getSupportActionBar().setTitle("" + title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        binding.toolbar.setTitle("" + title);
        binding.txtTitle.setText("" + title);

        if (isComp) {
            binding.txtTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_location_edit, 0);
        } else {
            binding.txtTitle.setCompoundDrawables(null, null, null, null);
        }


        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);


    }

    private void performLogout() {


        SchoolApplication.getmInstance().savePreferenceDataBoolean(getString(R.string.preferances_islogin), false);

        Preferences.writeString(MainActivity.this, Preferences.USER_ID, "");
        Preferences.writeString(MainActivity.this, Preferences.SELECTED_LOCATION_ID, "");
        Preferences.writeString(MainActivity.this, Preferences.SELECTED_CATEGORIES_ID, "");
        Preferences.writeString(MainActivity.this, Preferences.SELECTED_CITY_LATITUDE, "");
        Preferences.writeString(MainActivity.this, Preferences.SELECTED_CITY_LONGITUDE, "");
        Preferences.writeString(MainActivity.this, Preferences.LOGIN_TYPE, "");
        Preferences.writeString(MainActivity.this, Preferences.DEVICE_ID, "");
        Preferences.writeString(MainActivity.this, Preferences.DEVICE_TOKEN, "");
        try {
            FirebaseInstanceId.getInstance().deleteInstanceId();
            FirebaseInstanceId.getInstance().getToken();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void changeFragment(Fragment fragment, boolean isAddtoBackstack) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content_main, fragment, fragment.getClass().getSimpleName());
        if (isAddtoBackstack)
            transaction.addToBackStack(fragment.getClass().getSimpleName());
        transaction.commit();


    }

    public void addFragment(Fragment fragment, boolean isAddtoBackstack) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.content_main, fragment, fragment.getClass().getSimpleName());
        if (isAddtoBackstack)
            transaction.addToBackStack(fragment.getClass().getSimpleName());
        transaction.commit();


    }


    public void onLogOutClicked() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setMessage(R.string.logout_confirm)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        performLogout();
                        dialogInterface.dismiss();

                    }


                }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });
        builder.show();
    }

}
