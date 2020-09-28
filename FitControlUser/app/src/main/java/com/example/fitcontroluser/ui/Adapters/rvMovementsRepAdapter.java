package com.example.fitcontroluser.ui.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.fitcontroluser.R;
import com.example.fitcontroluser.databases.models.Repetition;

import java.util.List;

public class rvMovementsRepAdapter extends RecyclerView.Adapter<rvMovementsRepAdapter.MyViewHolder> {

    public interface OnClickListener {
        void onArticleClick(String value);
    }

    public static rvMovementsRepAdapter.OnClickListener mListener;

    public void setClickArticleListener(rvMovementsRepAdapter.OnClickListener listener) {
        this.mListener = listener;
    }

    private int positionMarked = -1;

    // vars
    private List<Repetition> repsList;
    private Context mContext;
    private LayoutInflater mInflater;


    public rvMovementsRepAdapter(Context context, List<Repetition> mInventoryList) {
        this.repsList = mInventoryList;
        this.mContext = context;
        this.mInflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);

    }


    @NonNull
    @Override
    public rvMovementsRepAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movement_reps, parent, false);
        rvMovementsRepAdapter.MyViewHolder holder = new rvMovementsRepAdapter.MyViewHolder(view);

        return holder;

    }

    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(@NonNull final rvMovementsRepAdapter.MyViewHolder holder, final int position) {


        holder.tvDate.setText(repsList.get(position).getDate());
        holder.tvNumber.setText(String.valueOf(position + 1));

        //---------SELECT THE CORRECT IMAGE TO REPETITIONS-----------
        if (repsList.get(position).getRepOK()==0) {
            holder.ivOK.setImageResource(R.drawable.correct);

        } else {
            holder.ivOK.setImageResource(R.drawable.incorrect);

        }
        holder.parentLayout.setOnClickListener(v -> {
            YoYo.with(Techniques.BounceIn)
                    .duration(400)
                    .repeat(0)
                    .onEnd(animator -> {
                        if((repsList.get(position).getRepOK()==0)){
                            Toast.makeText(mContext, mContext.getResources().getString(R.string.lateralelevation0),Toast.LENGTH_LONG).show();
                        }else if((repsList.get(position).getRepOK()==1)){
                            Toast.makeText(mContext, mContext.getResources().getString(R.string.lateralelevation1),Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(mContext, mContext.getResources().getString(R.string.lateralelevation2),Toast.LENGTH_LONG).show();
                        }

                    })
                    .playOn(holder.parentLayout);

        });

    }

    /**
     * MY VIEW HOLDER CLASS
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView ivOK;
        TextView tvDate, tvNumber;
        LinearLayout parentLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            //---------VARS-----------
            tvDate = itemView.findViewById(R.id.iddaterep);
            tvNumber = itemView.findViewById(R.id.idnumberrep);
            parentLayout = itemView.findViewById(R.id.parentLayoutcolor);
            ivOK = itemView.findViewById(R.id.idrepokrep);

        }
    }


    @Override
    public int getItemCount() {
        return repsList.size();
    }
}
