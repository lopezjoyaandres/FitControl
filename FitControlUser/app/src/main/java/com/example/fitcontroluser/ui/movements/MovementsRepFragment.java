package com.example.fitcontroluser.ui.movements;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
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
import com.example.fitcontroluser.ui.Adapters.rvMovementsRepAdapter;

import java.util.ArrayList;
import java.util.List;


public class MovementsRepFragment extends Fragment {

    //---------VARS-----------
    String namemovement;
    LinearLayout parentLayout;
    TextView tvName;
    Context mContext;
    List<Repetition> reps = new ArrayList<>();
    RecyclerView rvMovementsRep;
    rvMovementsRepAdapter adapter;
    String userName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //---------VARS-----------
        View root = inflater.inflate(R.layout.fragment_movements_rep, container, false);
        mContext = root.getContext();
        parentLayout = root.findViewById(R.id.layoutmovrep);
        rvMovementsRep = root.findViewById(R.id.idrvmovrepetitions);
        tvName = root.findViewById(R.id.idnamemovrep);

        //---------GET USERNAME-----------
        SharedPreferences preferencias = mContext.getSharedPreferences("PreferenciasUsuario", Context.MODE_PRIVATE);
        userName = preferencias.getString("usuario", "usuario");

        //---------GET THE ARGUMENTS FROM THE PREVIOUS FRAGMENT-----------
        Bundle datosRecuperados = getArguments();
        if (datosRecuperados == null) {
            // No hay datos, manejar excepción
            return root;
        }
        namemovement = datosRecuperados.getString("movement");

        if (namemovement.equals("Lateral Elevations")) {
            tvName.setText("Lateral Elevations");
            parentLayout.setBackgroundResource(R.drawable.noteyellow);
        } else if (namemovement.equals("Curl Biceps")) {
            tvName.setText("Curl Biceps");
            parentLayout.setBackgroundResource(R.drawable.notered);
        }
        //---------GET ALL REPS WITH USERNAME AND NAMEMOVEMENT-----------
        reps = database.getInstance(mContext).getRepetitionDAO().getAllRepetitionsByM(userName, namemovement);

        //--------ADAPTER AND RECYCLEVIEW-----------
        adapter = new rvMovementsRepAdapter(root.getContext(), reps);
        rvMovementsRep.setAdapter(adapter);
        rvMovementsRep.setLayoutManager(new LinearLayoutManager(root.getContext(), LinearLayoutManager.VERTICAL, false));
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                //---------LOAD A PREVIOUS FRAGMENT-----------
                Fragment fragment3 = new MovementsFragment();
                FragmentManager fragmentManager3 = ((FragmentActivity) mContext).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction3 = fragmentManager3.beginTransaction();
                fragmentTransaction3.replace(R.id.nav_host_fragment, fragment3);
                fragmentTransaction3.addToBackStack(null);
                fragmentTransaction3.commit();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
        return root;
    }
}