package com.FitControl.ui.chooseMovement;

import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.wear.widget.WearableRecyclerView;

import com.FitControl.R;
import com.FitControl.ui.Adapters.rvMovementAdapter;
import com.FitControl.ui.main.MainActivity;

import java.util.ArrayList;

public class ChooseMovement extends WearableActivity {
    private static final String TAG = "ChooseMovement";
    private WearableRecyclerView rvMovement;
    private rvMovementAdapter adapter;
    private ArrayList<String> names;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onArticleClick: "+"PUES ESTOY EN EL CREATE DEL MOVEMENT");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_movement);
        rvMovement=findViewById(R.id.idweremovement);
        rellenar();
    }
    public void rellenar() {

        //
        //------------------FIND THE CORRECT DEVICE TO CONNECT-------------------------
        //


        names= new ArrayList<>();
        names.add("Lateral Elevations");

        adapter=new rvMovementAdapter(this,names);
        rvMovement.setAdapter(adapter);
        rvMovement.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter.setClickArticleListener(new rvMovementAdapter.OnClickListener() {

            @Override
            public void onArticleClick(String value) {

                Intent myIntent = new Intent(ChooseMovement.this, MainActivity.class);
                myIntent.putExtra("movement", value);
                startActivity(myIntent);
            }
        });

    }
}

