package com.example.fitcontroluser.ui.main;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.fitcontroluser.R;
import com.example.fitcontroluser.Utilidades.Utilidades;
import com.example.fitcontroluser.databases.database;
import com.example.fitcontroluser.databases.models.Profile;
import com.example.fitcontroluser.ui.dialog.DatePickerFragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class myProfile extends AppCompatActivity {

    //---------VARS-----------
    private static final String TAG = "myProfile";
    private ImageButton backButton;
    private ImageButton changeBoton;
    private ProgressBar mybar;
    private ImageView photo;
    private TextView usuario;
    private EditText anios;
    private Spinner gender;
    private Context mContext;
    private String userName;
    private Button buttonUpdate;
    private ImageView buttonDelete;
    private int pos;
    private Uri path;
    private Bitmap bitmap;
    private String imageString;
    private boolean imageChanged;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        //---------VARS-----------
        mContext=this;
        backButton = findViewById(R.id.botonBack);
        buttonDelete = findViewById(R.id.buttonDelete);
        usuario = findViewById(R.id.userProfile);
        anios = findViewById(R.id.yearsProfile);
        gender = findViewById(R.id.sexProfile);
        buttonUpdate = findViewById(R.id.bottonUpdate);
        photo = findViewById(R.id.loginImage);
        mybar = findViewById(R.id.idprogressupdate);
        mybar.setVisibility(View.GONE);
        imageChanged = false;
        changeBoton = findViewById(R.id.changeimage);
        anios.setOnClickListener(this::onClick);

        //---------GET USERNAME-----------
        SharedPreferences preferencias = getSharedPreferences("PreferenciasUsuario", Context.MODE_PRIVATE);
        userName = preferencias.getString("usuario", "usuario");
        usuario.setText(userName, TextView.BufferType.EDITABLE);

        //---------GET PROFILE THROUGH USERNAME-----------
        Profile per = database.getInstance(this).getProfileDAO().getMemberById(userName);

        //---------PUT DATE OF BIRTHDAY FROM PROFILE DATA-----------
        anios.setText(per.getYear(), TextView.BufferType.EDITABLE);

        //---------GET GENDER ARRAY, SELECT THE CORRECT AND PUT THE GENDER-----------
        Resources res = getResources();
        String[] gender = res.getStringArray(R.array.gender);
        for (int i = 0; i < gender.length; i++) {
            if (per.getSex().equals(gender[i])) {
                pos = i;
            }
        }
        this.gender.setSelection(pos);

        //---------CONVERT URI TO BITMAP-----------
        Bitmap originalBitmap = null;
        try {
            originalBitmap = Utilidades.getBitmapFromUri(Uri.parse(per.getImage()), this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //---------CUT THE BITMAP TO SQUARE-----------
        originalBitmap = Utilidades.cropToSquare(originalBitmap);
        photo.setImageBitmap(originalBitmap);

        //---------ON CLICK BACK BUTTON TO RETURN TO THE MAIN -----------
        backButton.setOnClickListener(v -> {
            YoYo.with(Techniques.BounceIn)
                    .duration(400)
                    .repeat(0)
                    .onEnd(animator -> {
                        Intent intent = new Intent(v.getContext(), main.class);

                        startActivityForResult(intent, 0);
                    })
                    .playOn(backButton);


        });

        //---------ON CLICK BACK BUTTON TO DELETE PROFILE -----------
        buttonDelete.setOnClickListener(v -> {
            YoYo.with(Techniques.BounceIn)
                    .duration(400)
                    .repeat(0)
                    .onEnd(animator -> {
                        AlertDialog.Builder builder =
                                new AlertDialog.Builder(this);

                        builder.setMessage("You are going to delete this profile, ¿Are you sure?")
                                .setTitle("Confirmation")
                                .setPositiveButton("YES", new DialogInterface.OnClickListener()  {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Log.i("Dialogos", "Confirmacion Aceptada.");
                                        database.getInstance(mContext).getRepetitionDAO().deleteReps(userName);
                                        database.getInstance(mContext).getMovementDAO().deleteMovements(userName);
                                        database.getInstance(mContext).getProfileDAO().delete(per);
                                        File file = new File(Environment.getExternalStorageDirectory()
                                                + File.separator + "FitControlImages" + File.separator + userName + ".jpg");
                                        file.delete();
                                        Intent intent = new Intent(mContext, index.class);

                                        startActivityForResult(intent, 0);
                                        dialog.cancel();
                                    }
                                })
                                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Log.i("Dialogos", "Confirmacion Cancelada.");
                                        dialog.cancel();
                                    }
                                });
                        builder.show();


                    })
                    .playOn(buttonDelete);


        });

        //---------ON CLICK BUTTON TO PUT A NEW IMAGE-----------
        changeBoton.setOnClickListener(v -> {
            YoYo.with(Techniques.BounceIn)
                    .duration(400)
                    .repeat(0)
                    .onEnd(animator -> {
                        //---------FUNCTION TO SELECT AN IMAGEN FROM THE GALLERY-----------
                        loadImage();
                        imageChanged = true;
                    })
                    .playOn(changeBoton);

        });

        //---------ON CLICK BUTTON TO SAVE CHANGES---------
        buttonUpdate.setOnClickListener(v -> {
            YoYo.with(Techniques.BounceIn)
                    .duration(400)
                    .repeat(0)
                    .onEnd(animator -> {
                        mContext = v.getContext();
                        bitmap = ((BitmapDrawable) photo.getDrawable()).getBitmap();
                        if (imageChanged) {
                            //---------IF YOU HAVE CHANGED THE IMAGE-----------
                            new AsyncUpdate().execute();
                            imageChanged = false;
                        } else {
                            Profile aux = new Profile(usuario.getText().toString(), per.getImage(), anios.getText().toString(), this.gender.getSelectedItem().toString());
                            database.getInstance(mContext).getProfileDAO().update(aux);
                            Toast.makeText(myProfile.this, "Profile Changed", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .playOn(buttonUpdate);


        });
    }

    private void showDatePickerDialog() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year1, int month, int day) {
                // +1 because January is zero
                final String selectedDate = day + " / " + (month + 1) + " / " + year1;
                anios.setText(selectedDate);
            }
        });

        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.yearsProfile:
                showDatePickerDialog();
                break;
        }
    }

    private class AsyncUpdate extends AsyncTask<Profile, View, Void> {
        @Override
        protected void onPreExecute() {

            //---------MAKE VISIBLE A PROGRESS BAR-----------
            mybar.setVisibility(View.VISIBLE);
            buttonUpdate.setEnabled(false);
            Toast.makeText(myProfile.this, "Updating profile", Toast.LENGTH_SHORT).show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            //---------MAKE INVISIBLE A PROGRESS BAR-----------
            mybar.setVisibility(View.GONE);
            buttonUpdate.setEnabled(true);
            Toast.makeText(myProfile.this, "Profile Changed", Toast.LENGTH_SHORT).show();
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Profile... voids) {

            //---------SAVE IMAGE IN LOCAL AREA AND ADD A PROFILE-----------
            imageString = saveImage().toString();
            @SuppressLint("WrongThread") Profile aux = new Profile(usuario.getText().toString(), imageString, anios.getText().toString(), gender.getSelectedItem().toString());

            database.getInstance(mContext).getProfileDAO().update(aux);

            return null;
        }
    }

    //!!!---------THIS FUNCTION SAVE AN IMAGE IN LOCAL AREA AND RETURNS A URI WITH THE PATH OF IMAGE-----------!!!
    private Uri saveImage() {
        OutputStream fileOutStream = null;
        Uri uri = null;
        try {
            File file = new File(Environment.getExternalStorageDirectory()
                    + File.separator + "FitControlImages" + File.separator);
            file.mkdirs();
            File directorioImagenes = new File(file, usuario.getText() + ".jpg");
            uri = Uri.fromFile(directorioImagenes);
            fileOutStream = new FileOutputStream(directorioImagenes);
        } catch (Exception e) {
            Log.e("ERROR!", e.getMessage());
        }

        try {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutStream);
            fileOutStream.flush();
            fileOutStream.close();
        } catch (Exception e) {
            Log.e("ERROR!", e.getMessage());
        }
        return uri;
    }

    public void loadImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(intent.createChooser(intent, "Choose application"), 11);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            //--------IF YOU HAVE PERMISSION YOU CAN USE THE PHOTO-----------
            Bitmap originalBitmap = null;
            path = data.getData();
            photo.setImageURI(path);
            originalBitmap = ((BitmapDrawable) photo.getDrawable()).getBitmap();
            originalBitmap = Utilidades.cropToSquare(originalBitmap);
            photo.setImageBitmap(originalBitmap);


        }
    }

}
