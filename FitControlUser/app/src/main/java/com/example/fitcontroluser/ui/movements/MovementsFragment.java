package com.example.fitcontroluser.ui.movements;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fitcontroluser.R;
import com.example.fitcontroluser.ui.Adapters.rvMovementsAdapter;

import java.util.ArrayList;
import java.util.List;


public class MovementsFragment extends Fragment {

    //---------VARS-----------
    List<String> movementsName = new ArrayList<>();
    private rvMovementsAdapter adapter;
    private RecyclerView rvMovements;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //---------IF CONTAINER OF FRAGMENTS ISN´T NULL REMOVE ALL VIEWS-----------
        if (container != null) {
            container.removeAllViews();
        }

        //---------VARS-----------
        View root = inflater.inflate(R.layout.fragment_movements, container, false);
        rvMovements = root.findViewById(R.id.idrvhome);

        //---------ADD ALL MOVEMENTS TO ARRAYLIST-----------
        movementsName.add("Lateral Elevations");
        movementsName.add("Curl Biceps");


        adapter = new rvMovementsAdapter(root.getContext(), movementsName);
        rvMovements.setAdapter(adapter);
        rvMovements.setLayoutManager(new LinearLayoutManager(root.getContext(), LinearLayoutManager.VERTICAL, false));


        return root;
    }
}