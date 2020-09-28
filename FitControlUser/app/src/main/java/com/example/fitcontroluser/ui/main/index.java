package com.example.fitcontroluser.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.fitcontroluser.R;
//import com.example.fitcontroluser.conexionSQLiteHelper;
import com.example.fitcontroluser.databases.database;
import com.example.fitcontroluser.databases.models.Profile;

import com.example.fitcontroluser.ui.Adapters.rvProfileAdapter;

import java.util.List;

public class index extends AppCompatActivity {

    //---------VARS-----------
    private static final String TAG = "index";
    private RecyclerView rvProfiles;
    private rvProfileAdapter adapter;
    private ImageButton buttonAddProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //---------VARS-----------
        setContentView(R.layout.activity_index);
        rvProfiles = findViewById(R.id.rvProfile);
        buttonAddProfile = findViewById(R.id.buttonAddProfile);

        //---------FILL THE RECYCLE VIEW WITH ALL PROFILES-----------
        fill();


        //---------START A NEW ACTIVITY-----------
        buttonAddProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.BounceIn)
                        .duration(400)
                        .repeat(0)
                        .onEnd(animator -> {

                            Intent intent = new Intent(v.getContext(), login.class);

                            startActivityForResult(intent, 0);
                        })
                        .playOn(buttonAddProfile);

            }
        });
    }

    public void fill() {

        //---------GET ALL PROFILES-----------
        List<Profile> mprofiles = database.getInstance(index.this).getProfileDAO().getAll();
        Log.d(TAG, "rellenar: " + mprofiles.size());

        //---------INITIALIZE AN ADAPTER AND RECYCLER VIEW----------
        adapter = new rvProfileAdapter(this, mprofiles);
        rvProfiles.setAdapter(adapter);
        rvProfiles.setLayoutManager(new GridLayoutManager(this, 2));

        //---------ON CLICK TO SET THE USERNAME IN PREFERANCES-----------
        adapter.setClickArticleListener(new rvProfileAdapter.OnClickListener() {
            @Override
            public void onArticleClick(String value) {
                Log.d(TAG, "onArticleClick: " + value);
                SharedPreferences prefs = getSharedPreferences("PreferenciasUsuario", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("usuario", value);
                editor.commit();
            }
        });

    }


}
