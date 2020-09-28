package com.example.fitcontroluser.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.fragment.app.Fragment;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.fitcontroluser.R;
import com.example.fitcontroluser.Utilidades.Utilidades;
import com.example.fitcontroluser.databases.database;
import com.example.fitcontroluser.databases.models.Profile;
import com.example.fitcontroluser.ui.Adapters.rvMovementsAdapter;
import com.example.fitcontroluser.ui.main.index;
import com.example.fitcontroluser.ui.main.login;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class HomeFragment extends Fragment {

    //---------VARS-----------
    private PieChart pclateralelevations;
    private PieChart pctotal;
    private ImageView iv1;
    private List<Integer> datatotal;
    private String userName;
    private Context mContext;
    private LinearLayout layoutReps,layoutLevel,layoutMostFrequent;
    private TextView tvrepetotal, tvtotallvl, tvrepebadtotal, tvbestmovement, tvNote, tvporcent;
    private List<Pair<String, Integer>> listmayor = new ArrayList<>();
    private int listrep;
    private int listrep1;
    private int mayor = 0;
    private boolean anyone;
    private String mostfrequent;
    private List<PieEntry> entriestotal;
    private PieDataSet settotal;
    private ArrayList<Integer> colors = new ArrayList<Integer>();
    private PieData databartotal;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        //---------IF CONTAINER OF FRAGMENTS ISN´T NULL REMOVE ALL VIEWS-----------
        if (container != null) {
            container.removeAllViews();
        }

        //---------VARS-----------
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        mContext = root.getContext();
        pclateralelevations = root.findViewById(R.id.idlateralelevations);
        pctotal = root.findViewById(R.id.idtotalreps);
        tvrepetotal = root.findViewById(R.id.idtotalrepetitions);
        tvbestmovement = root.findViewById(R.id.idtotalbestmovementrepetitions);
        tvporcent = root.findViewById(R.id.idporcentaje);
        tvtotallvl = root.findViewById(R.id.idlvltotal);
        tvNote = root.findViewById(R.id.idtotalnoterepetitions);
        iv1 = root.findViewById(R.id.idimage1);
        layoutReps=root.findViewById(R.id.idlayoutrep);
        layoutLevel=root.findViewById(R.id.idlayoutlevel);
        layoutMostFrequent=root.findViewById(R.id.idlayoutmostfrequent);

        //---------GET USERNAME-----------
        SharedPreferences preferencias = root.getContext().getSharedPreferences("PreferenciasUsuario", Context.MODE_PRIVATE);
        userName = preferencias.getString("usuario", "usuario");

        //---------ADD COLORS TO PIE GRAPH-----------
        colors.add(Color.parseColor("#B52C10"));
        colors.add(Color.parseColor("#66C006"));

        datatotal = new ArrayList<>();
        anyone = true;
        //---------GET PROFILE OF USERNAME-----------
        Profile per = database.getInstance(root.getContext()).getProfileDAO().getMemberById(userName);


        //!!-----------PUT THE ROUNDED USERIAMGE----------------!!

        //---------CONVERT USERIMAGE TO BITMAP-----------
        Bitmap originalBitmap = null;
        try {
            originalBitmap = Utilidades.getBitmapFromUri(Uri.parse(per.getImage()), root.getContext());
        } catch (IOException e) {
            e.printStackTrace();
        }

        //---------CUT THE BITMAP TO SQUARE-----------
        originalBitmap = Utilidades.cropToSquare(originalBitmap);

        //---------CREATE THE ROUNDEDBITMAP-----------
        RoundedBitmapDrawable roundedDrawable = RoundedBitmapDrawableFactory.create(getResources(), originalBitmap);

        //---------SET THE ROUNDEDBITMAP-----------
        roundedDrawable.setCornerRadius(originalBitmap.getHeight());
        iv1.setImageDrawable(roundedDrawable);

        //---------GET REPS OF MOVEMENT-----------
        int repstotalCorrect = database.getInstance(root.getContext()).getRepetitionDAO().getAllRepetitions(userName, 0);
        int repstotalIncorrect = database.getInstance(root.getContext()).getRepetitionDAO().getAllRepetitions(userName, 1)
                + database.getInstance(root.getContext()).getRepetitionDAO().getAllRepetitions(userName, 2);
        datatotal.add(repstotalIncorrect);
        datatotal.add(repstotalCorrect);
        tvrepetotal.setText(String.valueOf(repstotalCorrect + repstotalIncorrect));
        double suma = repstotalCorrect + repstotalIncorrect;

        //---------PUT ALL MOVEMENTS TO GET THE MOST FREQUENT-----------
        listrep = database.getInstance(root.getContext()).getRepetitionDAO().getAllRepetitions(userName, "Lateral Elevations");
        listrep1 = database.getInstance(root.getContext()).getRepetitionDAO().getAllRepetitions(userName, "Curl Biceps");

        //---------CREATE A PAIR TO ADD TO ARRAYLIST-----------
        Pair<String, Integer> aux = new Pair<>("Lateral Elevations", listrep);
        listmayor.add(aux);
        aux = new Pair<>("Curl Biceps", listrep1);
        listmayor.add(aux);

        //---------SEARCH THE MOST FREQUENT MOVEMENT-----------
        for (int i = 0; i < listmayor.size(); i++) {
            if (listmayor.get(i).second > mayor) {
                mostfrequent = listmayor.get(i).first;
                anyone = false;
            }
        }

        //---------IF THERE IS NONE SET A WHITE SPACE-----------
        if (anyone) {
            tvbestmovement.setText("");
        } else {
            tvbestmovement.setText(mostfrequent);
        }

        //---------TAKE ADVICE TO GIVE IT TO THE USER-----------
        if (repstotalCorrect / suma <= 0.25) {
            tvNote.setText("Your technique is quite improvable, you have to check it.");
        } else if (repstotalCorrect / suma <= 0.5) {
            tvNote.setText("Your technique isn't so bad ,but you can improve it even more.");
        } else if (repstotalCorrect / suma <= 0.75) {
            tvNote.setText("Your technique is good but it can improve more.");
        } else if (repstotalCorrect / suma <= 1) {
            tvNote.setText("Congratulations, your technique is very good. Keep it up.");
        }


        //---------SELECT THE LEVEL OF THE MOVEMENT-----------
        if (suma <= 100) {
            tvtotallvl.setTextColor(mContext.getResources().getColor(R.color.begginer));
            tvtotallvl.setText("Beginner");
        } else if (suma <= 300) {
            double percent=(double) repstotalCorrect / suma;
            if (percent >= 0.4) {
                tvtotallvl.setTextColor(mContext.getResources().getColor(R.color.advance));
                tvtotallvl.setText("Advanced");
            } else {
                tvtotallvl.setTextColor(mContext.getResources().getColor(R.color.begginer));
                tvtotallvl.setText("Beginner");
            }
        } else if (suma <= 500) {
            double percent=(double) repstotalCorrect / suma;
            if (percent > 0.65) {
                tvtotallvl.setTextColor(mContext.getResources().getColor(R.color.experienced));
                tvtotallvl.setText("Experienced");
            } else {
                tvtotallvl.setTextColor(mContext.getResources().getColor(R.color.advance));
                tvtotallvl.setText("Advanced");
            }
        } else if (suma > 500) {
            double percent=(double) repstotalCorrect / suma;
            if (percent > 0.75) {
                tvtotallvl.setTextColor(mContext.getResources().getColor(R.color.expert));
                tvtotallvl.setText("Expert");
            } else {
                tvtotallvl.setTextColor(mContext.getResources().getColor(R.color.experienced));
                tvtotallvl.setText("Experienced");
            }
        }

        entriestotal = new ArrayList<>();

        //---------LOAD DATA IN PIE GRAPHIC-----------
        for (Integer data1 : datatotal) {
            Log.d(TAG, "onCreateView: GRAFICO");
            // turn your data into Entry objects
            entriestotal.add(new PieEntry(data1));
        }
        settotal = new PieDataSet(entriestotal, "Correct");
        settotal.setValueFormatter(new PercentFormatter());
        settotal.setValueTextColor(Color.parseColor("#FFFFFF"));
        settotal.setValueTextSize(20);
        settotal.setColors(colors);
        databartotal = new PieData(settotal);
        databartotal.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.valueOf((int) Math.floor(value));
            }
        });
        pctotal.setData(databartotal);

        //---------SELECT PARAMETERS OF GRAPHIC-----------
        pctotal.getLegend().setEnabled(false);
        pctotal.setCenterTextSize(20);
        pctotal.setCenterTextColor(getResources().getColor(R.color.colorPrimary));
        pctotal.setHoleColor(android.R.color.transparent);
        pctotal.setTransparentCircleColor(getResources().getColor(R.color.azul2));
        pctotal.setDrawCenterText(true);
        pctotal.getDescription().setEnabled(false);
        pctotal.invalidate();

        //---------ON CLICK TO SHOW PORCENT OF REPS-----------
        pctotal.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                tvporcent.setVisibility(View.VISIBLE);
                Log.d(TAG, "onValueSelected: VALOR1" + e.getY());
                double valor = ((e.getY() / suma) * 100);

                tvporcent.setText(String.format("%.1f", valor) + " %");


            }

            @Override
            public void onNothingSelected() {

                tvporcent.setVisibility(View.INVISIBLE);
                tvporcent.setText("");
            }


        });

        //---------ON CLICK TO SHOW A TOAST-----------
        layoutMostFrequent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.Bounce)
                        .duration(500)
                        .repeat(0)
                        .onEnd(animator -> {
                            Toast.makeText(mContext,mostfrequent+" is the most frequent movement in this profile.",Toast.LENGTH_LONG).show();

                        })
                        .playOn(layoutMostFrequent);
            }
        });
        //---------ON CLICK TO SHOW A TOAST-----------
        layoutLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                YoYo.with(Techniques.Bounce)
                        .duration(500)
                        .repeat(0)
                        .onEnd(animator -> {
                            //---------SELECT THE LEVEL OF THE MOVEMENT-----------
                            if (suma <= 100) {
                                Toast.makeText(mContext,"You are a begginer, if you want level up you must do min 100 reps ,40% of them with good form.",Toast.LENGTH_LONG).show();
                            } else if (suma <= 300) {
                                double percent=(double) repstotalCorrect / suma;
                                if (percent >= 0.4) {
                                    Toast.makeText(mContext,"You are advanced,if you want level up you must do min 300 reps ,65% of them with good form.",Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(mContext,"You are a begginer, if you want level up you must do min 300 reps or increase the percentage of correct repetitions to 40%.",Toast.LENGTH_LONG).show();
                                }
                            } else if (suma <= 500) {
                                double percent=(double) repstotalCorrect / suma;
                                if (percent > 0.65) {
                                    Toast.makeText(mContext,"You are experienced,if you want level up you must do min 500 reps ,75% of them with good form.",Toast.LENGTH_LONG).show();

                                } else {
                                    Toast.makeText(mContext,"You are advanced, if you want level up you must do min 500 reps or increase the percentage of correct repetitions to 65%.",Toast.LENGTH_LONG).show();
                                }
                            } else if (suma > 500) {
                                double percent=(double) repstotalCorrect / suma;
                                if (percent > 0.75) {
                                    Toast.makeText(mContext,"You are expert, the max level.",Toast.LENGTH_LONG).show();

                                } else {
                                    Toast.makeText(mContext,"You are experienced, if you want level up you must increase the percentage of correct repetitions to 75%.",Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                        .playOn(layoutLevel);

            }
        });
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                Intent intent = new Intent(root.getContext(), index.class);

                startActivityForResult(intent, 0);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
        return root;
    }
}
