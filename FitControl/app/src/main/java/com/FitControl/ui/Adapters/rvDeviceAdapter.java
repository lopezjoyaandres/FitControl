package com.FitControl.ui.Adapters;


import android.bluetooth.BluetoothDevice;
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

public class rvDeviceAdapter extends RecyclerView.Adapter<rvDeviceAdapter.MyViewHolder> {

    private static final String TAG = "RecyclerViewDevice";

    public interface OnClickListener{
        void onArticleClick(int value);
    }

    public static rvDeviceAdapter.OnClickListener mListener;

    public void setClickArticleListener( rvDeviceAdapter.OnClickListener listener){
        this.mListener = listener;
    }

    // vars
    private List<BluetoothDevice> deviceList;
    private Context mContext;
    private LayoutInflater mInflater ;

    public rvDeviceAdapter(Context context , List<BluetoothDevice> mInventoryList) {
        this.deviceList = mInventoryList;
        this.mContext = context;
        this.mInflater =(LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_device, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        holder.tvName.setText(deviceList.get(position).getName());
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mListener.onArticleClick(position);

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

            tvName = itemView.findViewById(R.id.tvName);
            parentLayout = itemView.findViewById(R.id.parentLayout);


        }
    }




    @Override
    public int getItemCount() {
        return deviceList.size();
    }
}
