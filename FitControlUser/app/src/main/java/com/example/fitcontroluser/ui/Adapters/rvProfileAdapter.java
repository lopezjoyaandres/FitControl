package com.example.fitcontroluser.ui.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.fitcontroluser.R;
import com.example.fitcontroluser.Utilidades.Utilidades;
import com.example.fitcontroluser.databases.models.Profile;

import com.example.fitcontroluser.ui.main.index;
import com.example.fitcontroluser.ui.main.main;
import com.example.fitcontroluser.ui.register.RegisterRepetitonsFragment;

import java.io.ByteArrayInputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class rvProfileAdapter extends RecyclerView.Adapter<rvProfileAdapter.MyViewHolder> {

    private static final String TAG = "RecyclerViewProfiles";

    public interface OnClickListener {
        void onArticleClick(String value);
    }

    public static rvProfileAdapter.OnClickListener mListener;

    public void setClickArticleListener(rvProfileAdapter.OnClickListener listener) {
        this.mListener = listener;
    }

    private int positionMarked = -1;

    //---------VARS-----------
    private List<Profile> profilesList;
    private Context mContext;
    private LayoutInflater mInflater;

    public rvProfileAdapter(Context context, List<Profile> mInventoryList) {
        this.profilesList = mInventoryList;
        this.mContext = context;
        this.mInflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_profile, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;

    }

    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {


        holder.tvName.setText(profilesList.get(position).getName());

        //!!-----------PUT THE ROUNDED USERIAMGE----------------!!

        //---------CONVERT USERIMAGE TO BITMAP-----------
        Bitmap originalBitmap = null;
        try {
            originalBitmap = Utilidades.getBitmapFromUri(Uri.parse(profilesList.get(position).getImage()), mContext);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //---------CUT THE BITMAP TO SQUARE-----------
        originalBitmap = Utilidades.cropToSquare(originalBitmap);

        //---------CREATE THE ROUNDEDBITMAP-----------
        RoundedBitmapDrawable roundedDrawable = RoundedBitmapDrawableFactory.create(mContext.getResources(), originalBitmap);

        //---------SET THE ROUNDEDBITMAP-----------
        roundedDrawable.setCornerRadius(originalBitmap.getHeight());
        holder.ivProfile.setImageDrawable(roundedDrawable);


        //---------ONCLICK TO GO TO THE USER HOME-----------
        holder.parentLayout.setOnClickListener(v -> {
            YoYo.with(Techniques.BounceIn)
                    .duration(400)
                    .repeat(0)
                    .onEnd(animator -> {
                        String userName = holder.tvName.getText().toString();
                        mListener.onArticleClick(userName);

                        //---------START NEW ACTIVITY-----------
                        Intent myIntent = new Intent(v.getContext(), main.class);
                        mContext.startActivity(myIntent);


                    })
                    .playOn(holder.parentLayout);

        });


    }

    /**
     * MY VIEW HOLDER CLASS
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView ivProfile;
        TextView tvName;
        LinearLayout parentLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            //---------VARS-----------
            tvName = itemView.findViewById(R.id.tvName);
            parentLayout = itemView.findViewById(R.id.parentLayout);
            ivProfile = itemView.findViewById(R.id.imageProfile);

        }
    }


    @Override
    public int getItemCount() {
        return profilesList.size();
    }
}

