package com.nigoote.utb_leave_app;

import static com.nigoote.utb_leave_app.LoginActivity.SHARED_PREFERENCES_NAME;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nigoote.utb_leave_app.fragments.HomeFragment;
import com.nigoote.utb_leave_app.fragments.MyLeavesFragment;
import com.nigoote.utb_leave_app.fragments.ProfileFragment;
import com.nigoote.utb_leave_app.fragments.RequestFragment;

public class HomeActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar2);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Home");
//        end of toolBar


        BottomNavigationView bottomNav = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new HomeFragment()).addToBackStack(null).commit();


    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

//        shared pref data
                    sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
                    String str_name = sharedPreferences.getString("names", "");
                    String str_userid = sharedPreferences.getString("id", "");
                    switch (item.getItemId()){
                        case R.id.nav_home:
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                    new HomeFragment().newInstance(str_name,str_userid)).addToBackStack(null).commit();
                            break;

                        case R.id.nav_request:
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                    new RequestFragment().newInstance(str_name,str_userid)).addToBackStack(null).commit();
                            break;

                        case R.id.nav_myRequest:
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                    new MyLeavesFragment().newInstance(str_name,str_userid)).addToBackStack(null).commit();
                            break;
                    }
                    return true;
                }

            };

//    optionMenu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_menu,menu);
        return true;
    }

//    onOptionClicks

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_logout:
                SharedPreferences sharedpreferences = getApplicationContext().getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.clear();
                editor.apply();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                return true;
            case R.id.nav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ProfileFragment()).addToBackStack(null).commit();
        }
        return super.onOptionsItemSelected(item);
    }

//    end of OptionMenu

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0){
            super.onBackPressed();
        }else {
            getSupportFragmentManager().popBackStack();
        }
    }
}