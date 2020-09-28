package com.example.fitcontroluser.ui.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.fitcontroluser.R;
import com.example.fitcontroluser.databases.database;
import com.example.fitcontroluser.ui.register.RegisterMovementFragment;
import com.example.fitcontroluser.ui.register.RegisterRepetitonsFragment;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.List;

public class rvCalendarMovementsAdapter extends RecyclerView.Adapter<rvCalendarMovementsAdapter.MyViewHolder> {
    private static final String TAG = "RecyclerViewProfiles";


    public interface OnClickListener {
        void onArticleClick(String value);
    }

    public static rvCalendarMovementsAdapter.OnClickListener mListener;

    public void setClickArticleListener(rvCalendarMovementsAdapter.OnClickListener listener) {
        this.mListener = listener;
    }

    private int positionMarked = -1;

    // vars
    private List<String> movementsList;
    private Context mContext;
    private LayoutInflater mInflater;
    private String date;

    public rvCalendarMovementsAdapter(Context context, List<String> mInventoryList, String date) {
        this.movementsList = mInventoryList;
        this.mContext = context;
        this.mInflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
        this.date = date;
    }


    @NonNull
    @Override
    public rvCalendarMovementsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movement_calendar, parent, false);
        rvCalendarMovementsAdapter.MyViewHolder holder = new rvCalendarMovementsAdapter.MyViewHolder(view);

        return holder;

    }

    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(@NonNull final rvCalendarMovementsAdapter.MyViewHolder holder, final int position) {
        List<Integer> data = new ArrayList<>();
        List<PieEntry> entries = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<Integer>();
        PieDataSet set1;
        String userName;

        PieData databar;

        //---------GET USERNAME-----------
        SharedPreferences preferences = mContext.getSharedPreferences("PreferenciasUsuario", Context.MODE_PRIVATE);
        userName = preferences.getString("usuario", "usuario");

        //---------ADD COLORS TO PIE GRAPH-----------
        colors.add(Color.parseColor("#66C006"));
        colors.add(Color.parseColor("#f94a1a"));



        if (movementsList.get(position).equals("Lateral Elevations")) {
            //---------IF THE MOVEMENT IS LATERAL ELEVATIONS-----------
            holder.tvLogo.setText("Lateral Elevations");
            holder.tvLogo.setBackgroundResource(R.drawable.titleyellow);

        } else if (movementsList.get(position).equals("Curl Biceps")) {
            //---------IF THE MOVEMENT IS CURL BICEPS-----------
            holder.tvLogo.setText("Curl Biceps");
            holder.tvLogo.setBackgroundResource(R.drawable.titlered);
        }

        //---------GET TOTAL SETS OF MOVEMENT-----------
        int sets = database.getInstance(mContext).getRepetitionDAO().getSets(userName, date, movementsList.get(position));
        holder.tvSets.setText(String.valueOf(sets));

        //---------GET REPS OF MOVEMENT-----------
        int repsCorrect = database.getInstance(mContext).getRepetitionDAO().getRepetitionsByForm(userName, movementsList.get(position), date, 0);
        int repsIncorrect = database.getInstance(mContext).getRepetitionDAO().getRepetitionsByForm(userName, movementsList.get(position), date, 1)
                + database.getInstance(mContext).getRepetitionDAO().getRepetitionsByForm(userName, movementsList.get(position), date, 2);
        data.add(repsCorrect);
        data.add(repsIncorrect);
        double suma1 = repsCorrect + repsIncorrect;

        //---------LOAD DATA IN PIE GRAPHIC-----------
        for (Integer data1 : data) {
            Log.d(TAG, "onCreateView: GRAFICO");
            // turn your data into Entry objects
            entries.add(new PieEntry(data1));
        }
        set1 = new PieDataSet(entries, "Correct");
        set1.setColors(colors);
        set1.setValueTextColor(Color.parseColor("#FFFFFF"));
        set1.setValueTextSize(10);
        databar = new PieData(set1);
        holder.pcgraf.setData(databar);

        //---------SELECT PARAMETERS OF GRAPHIC-----------
        holder.pcgraf.getLegend().setEnabled(false);
        holder.pcgraf.setCenterTextSize(20);
        holder.pcgraf.setCenterTextColor(Color.parseColor("#702F3137"));
        holder.pcgraf.setData(databar);
        holder.pcgraf.setTransparentCircleColor(mContext.getResources().getColor(R.color.azul2));
        holder.pcgraf.setDrawCenterText(true);
        holder.pcgraf.setCenterText(String.valueOf(repsCorrect + repsIncorrect) + "\nReps");
        holder.pcgraf.getDescription().setEnabled(false);
        holder.pcgraf.invalidate();
        databar.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.valueOf((int) Math.floor(value));
            }
        });


        //---------ON CLICK TO SHOW PORCENT OF REPS-----------
        holder.pcgraf.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                Log.d(TAG, "onValueSelected: VALOR1" + e.getY());
                double valor = ((e.getY() / suma1) * 100);
                holder.tvporcent.setVisibility(View.VISIBLE);
                holder.tvporcent.setText(String.format("%.1f", valor) + " %");


            }

            @Override
            public void onNothingSelected() {
                holder.tvporcent.setVisibility(View.INVISIBLE);
                holder.tvporcent.setText("");
            }


        });

        //---------ON CLICK TO GO TO THE MOVEMENT SELECTED-----------
        holder.parentLayout.setOnClickListener(v -> {
            YoYo.with(Techniques.BounceIn)
                    .duration(400)
                    .repeat(0)
                    .onEnd(animator -> {
                        if (sets > 0) {
                            //---------LOAD A NEW FRAGMENT-----------
                            Fragment fragment = new RegisterRepetitonsFragment();
                            Bundle datosAEnviar = new Bundle();
                            datosAEnviar.putString("date", date);
                            datosAEnviar.putString("movement", "Lateral Elevations");
                            fragment.setArguments(datosAEnviar);
                            fragment.setArguments(datosAEnviar);
                            FragmentManager fragmentManager = ((FragmentActivity) mContext).getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }else{
                            Toast.makeText(mContext,"The "+date+" you didn´t do any rep of "+movementsList.get(position),Toast.LENGTH_LONG).show();
                        }

                    })
                    .playOn(holder.parentLayout);


        });
    }

    /**
     * MY VIEW HOLDER CLASS
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvLogo;
        TextView tvSets;
        TextView tvporcent;
        LinearLayout setslayout, titlelayout;
        PieChart pcgraf;
        LinearLayout parentLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            //---------VARS-----------
            pcgraf = itemView.findViewById(R.id.idgraf);
            parentLayout = itemView.findViewById(R.id.idparentmovcalendar);
            tvLogo = itemView.findViewById(R.id.idnamemovementcalendar);
            tvSets = itemView.findViewById(R.id.idsetsmovcalendar);
            titlelayout = itemView.findViewById(R.id.texttitle);
            setslayout = itemView.findViewById(R.id.idlayoutsets);
            tvporcent = itemView.findViewById(R.id.idporcentajerv);

        }
    }


    @Override
    public int getItemCount() {
        return movementsList.size();
    }
}
