package com.example.fitcontroluser.ui.register;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.fitcontroluser.R;
import com.example.fitcontroluser.databases.database;
import com.example.fitcontroluser.databases.models.Repetition;
import com.example.fitcontroluser.ui.Adapters.rvRepetitionsAdapter;

import java.util.List;


public class RegisterRepetitonsFragment extends Fragment {
String date;
String movement;
RecyclerView rvReps;
List<Repetition> mrepetitions;
String nombreUsuario;
LinearLayout parentLayout;
TextView tvName;
    private rvRepetitionsAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_register_repetitons, container, false);
        Bundle datosRecuperados = getArguments();
        if (datosRecuperados == null) {
            // No hay datos, manejar excepción
            return root;
        }

        SharedPreferences preferencias =root.getContext().getSharedPreferences("PreferenciasUsuario", Context.MODE_PRIVATE);
        nombreUsuario=preferencias.getString("usuario", "usuario");
        date = datosRecuperados.getString("date");
        tvName=root.findViewById(R.id.idnamemovcalendarrep);
        parentLayout=root.findViewById(R.id.idparentlayoutcalendar);
        movement = datosRecuperados.getString("movement");
        movement = datosRecuperados.getString("movement");
        if(movement.equals("Lateral Elevations")){
            tvName.setText("Lateral Elevations");
            parentLayout.setBackgroundResource(R.drawable.noteyellow);
        }else if(movement.equals("Curl Biceps")){
            tvName.setText("Curl Biceps");
            parentLayout.setBackgroundResource(R.drawable.notered);
        }
        rvReps=root.findViewById(R.id.idrvrepetitions);
        mrepetitions= database.getInstance(root.getContext()).getRepetitionDAO().getAllRepetitionsBy(nombreUsuario,date,movement);
        adapter=new rvRepetitionsAdapter(root.getContext(),mrepetitions);
        rvReps.setAdapter(adapter);
        rvReps.setLayoutManager(new LinearLayoutManager(root.getContext().getApplicationContext()));
        //rvReps.setLayoutManager(new GridLayoutManager(root.getContext(),2));

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                Fragment fragment = new RegisterMovementFragment();
                Bundle datosAEnviar = new Bundle();
                datosAEnviar.putString("date", date);


                fragment.setArguments(datosAEnviar);


                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        };

        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
        return root;
    }
}
