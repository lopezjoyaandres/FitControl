package com.example.fitcontroluser.ui.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.fitcontroluser.R;
import com.example.fitcontroluser.Utilidades.Utilidades;
import com.example.fitcontroluser.databases.database;
import com.example.fitcontroluser.databases.models.Profile;
import com.example.fitcontroluser.ui.home.HomeFragment;
import com.example.fitcontroluser.ui.movements.MovementsFragment;
import com.example.fitcontroluser.ui.receiver.ReceiverFragment;
import com.example.fitcontroluser.ui.register.RegisterFragment;
import com.example.fitcontroluser.ui.register.RegisterRepetitonsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.ByteArrayInputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;

public class main extends AppCompatActivity {

    //---------VARS-----------
    private BottomNavigationView navmain;
    FragmentContainerView container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //---------VARS-----------
        Toolbar toolbar = findViewById(R.id.toolbar);
        container = findViewById(R.id.nav_host_fragment);
        setSupportActionBar(toolbar);
        navmain = findViewById(R.id.id_navmain);

        //---------ON CLICK TO REDIRECT THE ACTIONS FROM THE MENU-----------
        navmain.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_home:

                        //---------LOAD A NEW FRAGMENT-----------
                        Fragment fragment = new HomeFragment();
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        break;
                    case R.id.nav_reciver:

                        //---------LOAD A NEW FRAGMENT-----------
                        Fragment fragment1 = new ReceiverFragment();
                        FragmentManager fragmentManager1 = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
                        fragmentTransaction1.replace(R.id.nav_host_fragment, fragment1);
                        fragmentTransaction1.addToBackStack(null);
                        fragmentTransaction1.commit();
                        break;
                    case R.id.nav_register:

                        //---------LOAD A NEW FRAGMENT-----------
                        Fragment fragment2 = new RegisterFragment();
                        FragmentManager fragmentManager2 = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction2 = fragmentManager2.beginTransaction();
                        fragmentTransaction2.replace(R.id.nav_host_fragment, fragment2);
                        fragmentTransaction2.addToBackStack(null);
                        fragmentTransaction2.commit();
                        break;

                    case R.id.nav_movements:

                        //---------LOAD A NEW FRAGMENT-----------
                        Fragment fragment3 = new MovementsFragment();
                        FragmentManager fragmentManager3 = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction3 = fragmentManager3.beginTransaction();
                        fragmentTransaction3.replace(R.id.nav_host_fragment, fragment3);
                        fragmentTransaction3.addToBackStack(null);
                        fragmentTransaction3.commit();
                        break;
                }
                return true;
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //---------MENU OPTIONS TO GO TO THE PROFILE-----------
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem down = menu.findItem(R.id.action_settings);
        down.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {


                Intent intent = new Intent(main.this, myProfile.class);
                startActivityForResult(intent, 1);
                return false;
            }
        });


        return true;
    }


}
