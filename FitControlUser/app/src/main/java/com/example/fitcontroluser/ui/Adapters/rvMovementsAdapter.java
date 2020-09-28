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
import com.example.fitcontroluser.databases.models.Repetition;
import com.example.fitcontroluser.ui.movements.MovementsRepFragment;
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

public class rvMovementsAdapter extends RecyclerView.Adapter<rvMovementsAdapter.MyViewHolder> {
    private static final String TAG = "RecyclerViewProfiles";


    public interface OnClickListener {
        void onArticleClick(String value);
    }

    public static rvMovementsAdapter.OnClickListener mListener;

    public void setClickArticleListener(rvMovementsAdapter.OnClickListener listener) {
        this.mListener = listener;
    }

    private int positionMarked = -1;

    // vars
    private List<String> movementsList;
    private Context mContext;
    private LayoutInflater mInflater;

    public rvMovementsAdapter(Context context, List<String> mInventoryList) {
        this.movementsList = mInventoryList;
        this.mContext = context;
        this.mInflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);

    }


    @NonNull
    @Override
    public rvMovementsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movement, parent, false);
        rvMovementsAdapter.MyViewHolder holder = new rvMovementsAdapter.MyViewHolder(view);

        return holder;

    }

    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(@NonNull final rvMovementsAdapter.MyViewHolder holder, final int position) {
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

            //---------IF THE MOVEMENT IS LATERAL ELEVATIONS SELECT YELLOW-----------
            holder.tvLogo.setText("Lateral      Elevations");
            holder.tvLogo.setBackgroundResource(R.drawable.titleyellow);
            holder.lastreplayout.setBackgroundResource(R.drawable.noteyellow);
            holder.lvllayout.setBackgroundResource(R.drawable.noteyellow);
            holder.setslayout.setBackgroundResource(R.drawable.noteyellow);
        } else if (movementsList.get(position).equals("Curl Biceps")) {

            //---------IF THE MOVEMENT IS CURL BICEPS SELECT RED-----------
            holder.tvLogo.setText("Curl      Biceps");
            holder.tvLogo.setBackgroundResource(R.drawable.titlered);
            holder.lastreplayout.setBackgroundResource(R.drawable.notered);
            holder.lvllayout.setBackgroundResource(R.drawable.notered);
            holder.setslayout.setBackgroundResource(R.drawable.notered);
        }

        //---------GET THE LAST REPETITION-----------
        List<Repetition> repList = database.getInstance(mContext).getRepetitionDAO().getAllRepetitionsByM(userName, movementsList.get(position));
        if (repList.size() > 0) {
            holder.tvLastRep.setText(repList.get(repList.size() - 1).getDate());
        }

        //---------GET TOTAL SETS OF MOVEMENT-----------
        int sets = database.getInstance(mContext).getRepetitionDAO().getSets(userName, movementsList.get(position));
        holder.tvSets.setText(String.valueOf(sets));

        //---------GET REPS OF MOVEMENT-----------
        int repsCorrect = database.getInstance(mContext).getRepetitionDAO().getRepetitionsByForm(userName, movementsList.get(position), 0);
        Log.d(TAG, "onBindViewHolder: CORR"+repsCorrect);
        int repsIncorrect = database.getInstance(mContext).getRepetitionDAO().getRepetitionsByForm(userName, movementsList.get(position), 1)
                + database.getInstance(mContext).getRepetitionDAO().getRepetitionsByForm(userName, movementsList.get(position), 2);
        data.add(repsCorrect);
        data.add(repsIncorrect);
        int sum1 = repsCorrect + repsIncorrect;

        //---------SELECT THE LEVEL OF THE MOVEMENT-----------
        Log.d(TAG, "onBindViewHolder: "+sum1);

        if (sum1 <= 100) {

            holder.tvlvl.setTextColor(mContext.getResources().getColor(R.color.begginer));
            holder.tvlvl.setText("Beginner");
        } else if (sum1 <= 300) {

            double percent=(double) repsCorrect / sum1;
            Log.d(TAG, "onBindViewHolder: RESU"+percent);
            if (percent >= 0.4) {
                holder.tvlvl.setTextColor(mContext.getResources().getColor(R.color.advance));
                holder.tvlvl.setText("Advanced");
            } else {
                holder.tvlvl.setTextColor(mContext.getResources().getColor(R.color.begginer));
                holder.tvlvl.setText("Beginner");
            }
        } else if (sum1 <= 500) {
            double percent=(double)repsCorrect / sum1;
            if (percent > 0.65) {
                holder.tvlvl.setTextColor(mContext.getResources().getColor(R.color.experienced));
                holder.tvlvl.setText("Experienced");
            } else {
                holder.tvlvl.setTextColor(mContext.getResources().getColor(R.color.advance));
                holder.tvlvl.setText("Advanced");
            }
        } else if (sum1 > 500) {
            double percent=(double)repsCorrect / sum1;
            if (percent > 0.75) {
                holder.tvlvl.setTextColor(mContext.getResources().getColor(R.color.expert));
                holder.tvlvl.setText("Expert");
            } else {
                holder.tvlvl.setTextColor(mContext.getResources().getColor(R.color.experienced));
                holder.tvlvl.setText("Experienced");
            }
        }

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
        databar.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.valueOf((int) Math.floor(value));
            }
        });
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





        //---------ON CLICK TO SHOW PORCENT OF REPS-----------
        holder.pcgraf.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                holder.tvporcent.setVisibility(View.VISIBLE);
                Log.d(TAG, "onValueSelected: VALOR1" + e.getY());
                double valor = ((e.getY() / sum1) * 100);

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
            YoYo.with(Techniques.SlideInRight)
                    .duration(400)
                    .repeat(0)
                    .onEnd(animator -> {
                        if (sets > 0) {
                            //---------LOAD A NEW FRAGMENT-----------
                            String movementrep = movementsList.get(position);
                            Fragment fragment = new MovementsRepFragment();
                            Bundle datosAEnviar = new Bundle();
                            datosAEnviar.putString("movement", movementrep);
                            fragment.setArguments(datosAEnviar);
                            fragment.setArguments(datosAEnviar);
                            FragmentManager fragmentManager = ((FragmentActivity) mContext).getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }else{

                            Toast.makeText(mContext,"You haven´t done any rep of this movement",Toast.LENGTH_LONG).show();

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
        TextView tvLastRep, tvlvl, tvporcent;
        LinearLayout setslayout, lvllayout, lastreplayout;
        PieChart pcgraf;
        RelativeLayout parentLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            //---------VARS-----------
            pcgraf = itemView.findViewById(R.id.idlateralelevations);
            parentLayout = itemView.findViewById(R.id.parentMovement);
            tvLogo = itemView.findViewById(R.id.idmovementHomeLogo);
            tvSets = itemView.findViewById(R.id.idsets);
            tvLastRep = itemView.findViewById(R.id.idlastrep);
            tvlvl = itemView.findViewById(R.id.idlevel);
            lvllayout = itemView.findViewById(R.id.idlayoutlvl);
            lastreplayout = itemView.findViewById(R.id.idlayoutlastrep);
            setslayout = itemView.findViewById(R.id.idlayoutsets);
            tvporcent = itemView.findViewById(R.id.idporcentajerv);

        }
    }


    @Override
    public int getItemCount() {
        return movementsList.size();
    }
}
