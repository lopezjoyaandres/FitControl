package com.example.fitcontroluser.ui.register;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.fitcontroluser.R;
import com.example.fitcontroluser.databases.database;
import com.example.fitcontroluser.ui.Adapters.rvCalendarMovementsAdapter;
import com.example.fitcontroluser.ui.Adapters.rvMovementsAdapter;
import com.example.fitcontroluser.ui.Adapters.rvMovementsRepAdapter;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class RegisterMovementFragment extends Fragment {

    String date;
    String nombreUsuario;

   Context mContext;
    TextView tvdatemov;
    RecyclerView rvMovementsRep;
    rvCalendarMovementsAdapter adapter;
    List<String> nombre_movements=new ArrayList<>();


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_register_movements, container, false);
        mContext=root.getContext();
        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                Fragment fragment = new RegisterFragment();

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        };

        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

        // The callback can be enabled or disabled here or in handleOnBackPressed()
        Bundle datosRecuperados = getArguments();
        if (datosRecuperados == null) {
            // No hay datos, manejar excepción
            return root;
        }
        date = datosRecuperados.getString("date");
        SharedPreferences preferencias =root.getContext().getSharedPreferences("PreferenciasUsuario", Context.MODE_PRIVATE);
        nombreUsuario=preferencias.getString("usuario", "usuario");

        tvdatemov=root.findViewById(R.id.idnamemovecalendar);
        tvdatemov.setText(date);
        rvMovementsRep=root.findViewById(R.id.idrvmovementcalendar);
        nombre_movements.add("Lateral Elevations");
        nombre_movements.add("Curl Biceps");
        adapter=new rvCalendarMovementsAdapter(root.getContext(),nombre_movements,date);
        rvMovementsRep.setAdapter(adapter);

        rvMovementsRep.setLayoutManager(new GridLayoutManager(mContext,2));


        return root;
    }


}
