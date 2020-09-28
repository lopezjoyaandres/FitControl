package com.example.fitcontroluser.ui.main;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.fitcontroluser.R;
import com.example.fitcontroluser.Utilidades.Utilidades;
import com.example.fitcontroluser.databases.database;
import com.example.fitcontroluser.databases.models.Profile;
import com.example.fitcontroluser.ui.dialog.DatePickerFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
//import com.example.fitcontroluser.conexionSQLiteHelper;

public class login extends AppCompatActivity {

    //---------VARS-----------
    private static final String TAG = "login";
    private Button buttonSignIn;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 2;
    private FloatingActionButton buttonImage;
    ImageView imageView;
    Uri path;
    Bitmap bitmap;
    String imageString;
    Context mContext;
    private EditText user;
    private EditText year;
    private ProgressBar mybar;
    private Spinner sex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //---------VARS-----------
        mContext = this;
        buttonSignIn = findViewById(R.id.buttonSignIn);
        buttonImage = findViewById(R.id.buttonimagen);
        user = findViewById(R.id.iduser);
        year = findViewById(R.id.idyear);
        sex = findViewById(R.id.idsex);
        mybar = findViewById(R.id.idprogresslogin);
        mybar.setVisibility(View.GONE);
        imageView = findViewById(R.id.loginImage1);
        imageView.setImageResource((R.drawable.login));


        year.setOnClickListener(this::onClick);

        //---------ON CLICK TO SELECT USER IMAGE-----------
        buttonImage.setOnClickListener(v -> {

            //---------FUNCTION TO SELECT FROM YOUR GALERY A PHOTO-----------
            loadImage();


        });

        //---------ON CLICK TO CHECK THE INFORMATION OF FORMULARY AND SAVE THE PROFILE IF ALL IS OKEY-----------
        buttonSignIn.setOnClickListener(v -> {
            //---------ERROR MESSAGE FROM FORMULARY -----------
            if (user.getText().toString().trim().equals("")) {
                user.setError("First name is required!");
                user.setHint("You must put a username");
            } else {
                bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                mContext = v.getContext();

                //---------CHECK PREMISSION TO WRITE-----------
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Permission is not granted
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.
                    } else {
                        // No explanation needed; request the permission
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);


                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                } else {
                    // Permission has already been granted
                    new AsyncProgress().execute();
                }
            }


        });
    }

    //---------SHOW A CALENDARY TO SELECT A DATE-----------
    private void showDatePickerDialog() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year1, int month, int day) {
                // +1 because January is zero
                final String selectedDate = day + " / " + (month + 1) + " / " + year1;
                year.setText(selectedDate);
            }
        });

        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.idyear:
                showDatePickerDialog();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    new AsyncProgress().execute();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    private class AsyncProgress extends AsyncTask<Profile, View, Void> {
        @Override
        protected void onPreExecute() {

            //---------MAKE VISIBLE A PROGRESS BAR-----------
            mybar.setVisibility(View.VISIBLE);
            Toast.makeText(login.this, "Creating profile", Toast.LENGTH_SHORT).show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            //---------MAKE INVISIBLE A PROGRESS BAR AND START A NEW ACTIVITY-----------
            mybar.setVisibility(View.GONE);
            Intent intent = new Intent(mContext, index.class);
            startActivityForResult(intent, 0);
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Profile... voids) {

            //---------SAVE IMAGE IN LOCAL AREA AND ADD A PROFILE-----------
            imageString = saveImage().toString();
            @SuppressLint("WrongThread") Profile aux = new Profile(user.getText().toString(), imageString, year.getText().toString(), sex.getSelectedItem().toString());
            database.getInstance(login.this).getProfileDAO().insert(aux);
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
            File directorioImagenes = new File(file, user.getText() + ".jpg");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            //--------IF YOU HAVE PERMISSION YOU CAN USE THE PHOTO-----------
            Bitmap originalBitmap = null;
            path = data.getData();
            imageView.setImageURI(path);
            originalBitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            originalBitmap = Utilidades.cropToSquare(originalBitmap);
            imageView.setImageBitmap(originalBitmap);


        }
    }


}
