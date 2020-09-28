package com.example.fitcontroluser.ui.register;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.fitcontroluser.R;
import com.example.fitcontroluser.Utilidades.Utilidades;
import com.example.fitcontroluser.databases.database;
import com.example.fitcontroluser.databases.models.Repetition;
import com.example.fitcontroluser.ui.home.HomeFragment;
import com.example.fitcontroluser.ui.main.login;
import com.example.fitcontroluser.ui.movements.MovementsRepFragment;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.components.YAxis.AxisDependency;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class RegisterFragment extends Fragment implements OnChartValueSelectedListener{
    ImageButton aniomas,aniomenos,mesmas,mesmenos;
    TextView tvanio,tvmes;
    int anio;
    LinearLayout layoutinfo;
    BarChart grafico;
    int maxdias;
    LinearLayout mainlayout;
    Context mContext;
    String fechaTotal;
    String mes1;
    int numMes;
    boolean info;
    Button buttonmovement;
    BarDataSet set1;
    List<BarEntry> entries;
    List<PairMovement> data;
    ArrayList<IBarDataSet> idata1;
    BarData databar;
    String nombreUsuario;
    TextView tvdate,tvreps,tvgoodreps,tvbadreps;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }
        View root = inflater.inflate(R.layout.fragment_register, container, false);
        aniomas=root.findViewById(R.id.idmasanio);
        aniomenos=root.findViewById(R.id.idmenosanio);
        tvanio=root.findViewById(R.id.idanio);
        mContext=root.getContext();
        mesmas=root.findViewById(R.id.idmasmes);
        layoutinfo=root.findViewById(R.id.idlayoutinfo);


        mesmenos=root.findViewById(R.id.idmenosmes);
        tvmes=root.findViewById(R.id.idmes);
        grafico=root.findViewById(R.id.idgrafica);
        buttonmovement=root.findViewById(R.id.idogomovements);

        info=false;
        tvdate=root.findViewById(R.id.idfechaclick);
        tvreps=root.findViewById(R.id.idrepsclick);
        tvgoodreps=root.findViewById(R.id.idgoodrepsclick);
        tvbadreps=root.findViewById(R.id.idbadrepsclick);

        grafico.setOnChartValueSelectedListener(new OnChartValueSelectedListener()
        {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                int val=(int) e.getX();
                int rOK=0,rNOK=0;
                fechaTotal=val+"/"+numMes+"/"+anio;
                List<Repetition> listR= database.getInstance(mContext).getRepetitionDAO().getAllRepetitionsBy(nombreUsuario,fechaTotal);
                tvdate.setText(fechaTotal);
                tvreps.setText(String.valueOf(listR.size()));

                for(int i=0;i<listR.size();i++){
                    if(listR.get(i).getRepOK()==0){
                        rOK++;
                    }else{
                        rNOK++;
                    }
                }
                tvbadreps.setText(String.valueOf(rNOK));
                tvgoodreps.setText(String.valueOf(rOK));
                buttonmovement.setEnabled(true);
                info=true;

            }

            @Override
            public void onNothingSelected() {
                tvdate.setText("");
                tvreps.setText("");
                tvbadreps.setText("");
                tvgoodreps.setText("");
                buttonmovement.setEnabled(false);
                info=false;
            }
        });
        Calendar c = Calendar.getInstance();
        String dia = Integer.toString(c.get(Calendar.DATE));
        String mes = Integer.toString(c.get(Calendar.MONTH));
        String annio = Integer.toString(c.get(Calendar.YEAR));



        SharedPreferences preferencias =root.getContext().getSharedPreferences("PreferenciasUsuario", Context.MODE_PRIVATE);
        nombreUsuario=preferencias.getString("usuario", "usuario");
        anio=Integer.valueOf(annio);
        tvanio.setText(String.valueOf(anio));
        Log.d(TAG, "onCreateView: ELMES"+ mes);
        numMes=Integer.valueOf(mes);
        Log.d(TAG, "onCreateView: ELMES"+ numMes);
        changeNumberMonth();

        tvmes.setText(mes1);

        maxdias= Utilidades.numeroDeDiasMes(mes1,anio);
        //Points to be in the graph...
        Log.d(TAG, "onCreateView: DIAS"+maxdias);
        data = new ArrayList<>();//CREAR STRUCT CON DOS ARGUMENTOS
        for (int i=1;i<=maxdias;i++){
            String fecha=i+"/"+numMes+"/"+anio;
            int aux4=database.getInstance(root.getContext()).getRepetitionDAO().getCountReps(nombreUsuario,fecha);
            data.add(new PairMovement(i,aux4));
        }


        entries = new ArrayList<>();

        for (PairMovement data1 : data) {
            Log.d(TAG, "onCreateView: GRAFICO");
            // turn your data into Entry objects
            entries.add(new BarEntry(data1.getDay(), data1.getReps()));
        }
        grafico.setDragEnabled(true);
        grafico.setScaleEnabled(true);
        grafico.getAxisLeft().setDrawGridLines(false);
        grafico.getAxisLeft().setGranularity(1.0f);
        grafico.getAxisLeft().setGranularityEnabled(true);
        grafico.getAxisRight().setDrawGridLines(false);
        grafico.getAxisRight().setEnabled(false);
        grafico.getXAxis().setDrawGridLines(false);
        grafico.getXAxis().setGranularity(1.0f);
        grafico.getXAxis().setGranularityEnabled(true);
        grafico.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        grafico.getDescription().setEnabled(false);
        grafico.getLegend().setEnabled(false);


        set1= new BarDataSet(entries,"Datos1");

        set1.setColors(ColorTemplate.COLORFUL_COLORS);
        //set1.setColor(getResources().getColor(R.color.colorPrimary));
        set1.setDrawValues(false);

        idata1=new ArrayList<>();
        idata1.add(set1);

        databar=new BarData(idata1);
        databar.setBarWidth(1f);

        grafico.setData(databar);
        grafico.invalidate();



        aniomas.setOnClickListener((View v) -> {
           anio++;
           tvanio.setText(String.valueOf(anio));
            maxdias= Utilidades.numeroDeDiasMes(mes1,anio);
            //Points to be in the graph...

            data = new ArrayList<>();
            for (int i=1;i<=maxdias;i++){
                String fecha=i+"/"+numMes+"/"+anio;
                int aux4=database.getInstance(root.getContext()).getRepetitionDAO().getCountReps(nombreUsuario,fecha);
                data.add(new PairMovement(i,aux4));
            }



            entries = new ArrayList<>();
            for (PairMovement data1 : data) {
                Log.d(TAG, "onCreateView: GRAFICO");
                // turn your data into Entry objects
                entries.add(new BarEntry(data1.getDay(), data1.getReps()));
            }
            set1= new BarDataSet(entries,"Datos1");

            set1.setColors(ColorTemplate.COLORFUL_COLORS);
            //set1.setColor(getResources().getColor(R.color.colorPrimary));
            set1.setDrawValues(false);

            idata1=new ArrayList<>();
            idata1.add(set1);

            databar=new BarData(idata1);
            databar.setBarWidth(1f);

            grafico.setData(databar);
            grafico.invalidate();
        });
        mesmenos.setOnClickListener(v -> {
            numMes--;
            changeNumberMonth();
            tvmes.setText(mes1);
            maxdias= Utilidades.numeroDeDiasMes(mes1,anio);
            //Points to be in the graph...

            data = new ArrayList<>();
            for (int i=1;i<=maxdias;i++){
                String fecha=i+"/"+numMes+"/"+anio;
                int aux4=database.getInstance(root.getContext()).getRepetitionDAO().getCountReps(nombreUsuario,fecha);
                data.add(new PairMovement(i,aux4));
            }



            entries = new ArrayList<>();
            for (PairMovement data1 : data) {
                Log.d(TAG, "onCreateView: GRAFICO");
                // turn your data into Entry objects
                entries.add(new BarEntry(data1.getDay(), data1.getReps()));
            }
            set1= new BarDataSet(entries,"Datos1");

            set1.setColors(ColorTemplate.COLORFUL_COLORS);
            //set1.setColor(getResources().getColor(R.color.colorPrimary));
            set1.setDrawValues(false);

            idata1=new ArrayList<>();
            idata1.add(set1);

            databar=new BarData(idata1);
            databar.setBarWidth(1f);

            grafico.setData(databar);
            grafico.invalidate();
        });

        mesmas.setOnClickListener(v -> {
            numMes++;
            changeNumberMonth();
            tvmes.setText(mes1);
            maxdias= Utilidades.numeroDeDiasMes(mes1,anio);
            //Points to be in the graph...

            data = new ArrayList<>();
            for (int i=1;i<=maxdias;i++){
                String fecha=i+"/"+numMes+"/"+anio;
                int aux4=database.getInstance(root.getContext()).getRepetitionDAO().getCountReps(nombreUsuario,fecha);
                data.add(new PairMovement(i,aux4));
            }



            entries = new ArrayList<>();
            for (PairMovement data1 : data) {
                Log.d(TAG, "onCreateView: GRAFICO");
                // turn your data into Entry objects
                entries.add(new BarEntry(data1.getDay(), data1.getReps()));
            }
            set1= new BarDataSet(entries,"Datos1");

            set1.setColors(ColorTemplate.COLORFUL_COLORS);
            //set1.setColor(getResources().getColor(R.color.colorPrimary));
            set1.setDrawValues(false);

            idata1=new ArrayList<>();
            idata1.add(set1);

            databar=new BarData(idata1);
            databar.setBarWidth(1f);

            grafico.setData(databar);
            grafico.invalidate();
        });
        aniomenos.setOnClickListener(v -> {
            anio--;
            tvanio.setText(String.valueOf(anio));
            maxdias= Utilidades.numeroDeDiasMes(mes1,anio);
            //Points to be in the graph...

            data = new ArrayList<>();
            for (int i=1;i<=maxdias;i++){
                String fecha=i+"/"+numMes+"/"+anio;
                int aux4=database.getInstance(root.getContext()).getRepetitionDAO().getCountReps(nombreUsuario,fecha);
                data.add(new PairMovement(i,aux4));
            }



            entries = new ArrayList<>();
            for (PairMovement data1 : data) {
                Log.d(TAG, "onCreateView: GRAFICO");
                // turn your data into Entry objects
                entries.add(new BarEntry(data1.getDay(), data1.getReps()));
            }
            set1= new BarDataSet(entries,"Datos1");

            set1.setColors(ColorTemplate.COLORFUL_COLORS);
            //set1.setColor(getResources().getColor(R.color.colorPrimary));
            set1.setDrawValues(false);

            idata1=new ArrayList<>();
            idata1.add(set1);

            databar=new BarData(idata1);
            databar.setBarWidth(1f);

            grafico.setData(databar);
            grafico.invalidate();

        });

        buttonmovement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.BounceIn)
                        .duration(400)
                        .repeat(0)
                        .onEnd(animator -> {
                            Bundle datosAEnviar = new Bundle();
                            datosAEnviar.putString("date", fechaTotal);

                            Fragment fragment = new RegisterMovementFragment();
                            fragment.setArguments(datosAEnviar);
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();

                        })
                        .playOn(buttonmovement);


            }
        });

        layoutinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if(!info){
                  Toast.makeText(mContext,"You must select a day into the graphic",Toast.LENGTH_LONG).show();
              }

            }
        });
        return root;
    }

    public void changeNumberMonth(){
        switch (numMes) {
            case 0:
                mes1="January";
                break;
            case 1:
                mes1="February";
                break;
            case 2:
                mes1="March";
                break;
            case 3:
                mes1="April";
                break;
            case 4:
                mes1="May";
                break;
            case 5:
                mes1="June";
                break;
            case 6:
                mes1="July";
                break;
            case 7:
                mes1="August";
                break;
            case 8:
                mes1="September";
                break;
            case 9:
                mes1="October";
                break;
            case 10:
                mes1="November";
                break;
            case 11:
                mes1="December";
                break;
        }

    }


    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}
