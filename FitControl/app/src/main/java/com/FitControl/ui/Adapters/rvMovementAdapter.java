package com.FitControl.ui.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.FitControl.R;

import java.util.List;

public class rvMovementAdapter extends RecyclerView.Adapter<rvMovementAdapter.MyViewHolder> {

    private static final String TAG = "RecyclerViewMovement";

    public interface OnClickListener{
        void onArticleClick(String value);
    }

    public static rvMovementAdapter.OnClickListener mListener;

    public void setClickArticleListener( rvMovementAdapter.OnClickListener listener){
        this.mListener = listener;
    }
    private int positionMarked = -1;

    // vars
    private List<String> movementList;
    private Context mContext;
    private LayoutInflater mInflater ;

    public rvMovementAdapter(Context context , List<String> mInventoryList) {
        this.movementList = mInventoryList;
        this.mContext = context;
        this.mInflater =(LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movement, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        holder.tvName.setText(movementList.get(position));





        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String movementName = holder.tvName.getText().toString();
                //mListener.onArticleClick(deviceName);
                mListener.onArticleClick(movementName);
                // Toast.makeText(mContext, "nombre "+holder.tvName.getText().toString(), Toast.LENGTH_SHORT).show();
               /* Intent myIntent = new Intent(v.getContext(), main.class);
                myIntent.putExtra("device", deviceName);
                mContext.startActivity(myIntent);*/
            }
        });


    }
    /**
     * MY VIEW HOLDER CLASS
     *///caja chica
    public class MyViewHolder extends RecyclerView.ViewHolder{


        TextView tvName;
        LinearLayout parentLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvNameMovement);


            parentLayout = itemView.findViewById(R.id.layoutmovement);


        }
    }




    @Override
    public int getItemCount() {
        return movementList.size();
    }
}
