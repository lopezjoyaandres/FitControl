package com.example.fitcontroluser.ui.receiver;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import org.apache.commons.math3.stat.descriptive.moment.Skewness;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitcontroluser.R;
import com.example.fitcontroluser.Utilidades.Utilidades;
import com.example.fitcontroluser.databases.database;
import com.example.fitcontroluser.databases.models.Accelerometer;
import com.example.fitcontroluser.databases.models.Gyroscope;
import com.example.fitcontroluser.databases.models.Movement;
import com.example.fitcontroluser.databases.models.Repetition;
import com.example.fitcontroluser.ui.Adapters.rvReciverReps;
import com.example.fitcontroluser.ui.Adapters.rvRepetitionsAdapter;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class ReceiverFragment extends Fragment {

    //---------VARS-----------
    protected BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    protected AcceptThread acceptThread;
    private MyBluetoothService.ConnectedThread MBS = null;
    protected String aux = "";
    private Context mContext;
    private boolean dat;
    private boolean bount = false;
    private String movement_name = "";
    long restime = 0;
    TextView tvmovement_name, tvresttime, tvnumreps, tvnumgoodreps, tvnummbadreps, tvcomment;
    ImageButton buttonReceiver;
    ImageView imageconnect;
    VideoView ivGif;
    ImageButton buttoncorrect;
    ImageButton buttonalgorithm;
    ImageButton buttonincorrect;
    RoundedBitmapDrawable roundedDrawable;
    RecyclerView rvreciver;
    rvReciverReps adapter;
    boolean problem = false;
    int mode;
    int numRep;
    protected ProgressBar myprogressbar;

    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {


                case Utilidades.MESSAGE_READ:
                    //---------RECIVE THE MESSAGE FROM THE MBS-----------
                    aux = (String) msg.obj;
                    break;

            }
        }
    };


    @RequiresApi(api = Build.VERSION_CODES.N)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//---------IF CONTAINER OF FRAGMENTS ISN´T NULL REMOVE ALL VIEWS-----------
        if (container != null) {
            container.removeAllViews();
        }

        //---------VARS-----------
        View root = inflater.inflate(R.layout.fragment_receiver, container, false);
        mContext = getContext();
        myprogressbar = root.findViewById(R.id.ProgressBar);
        myprogressbar.setVisibility(View.GONE);

        buttonReceiver = root.findViewById(R.id.botonReceiver);
       /*buttoncorrect = root.findViewById(R.id.idcorrect);
        buttonalgorithm = root.findViewById(R.id.idalgoritmo);
        buttonincorrect = root.findViewById(R.id.idincorrect);*/

        tvcomment = root.findViewById(R.id.idcomments);
        tvnumreps = root.findViewById(R.id.numeroreps);
        tvnumreps.setText(("-"));
        tvnummbadreps = root.findViewById(R.id.malasrepes);
        tvnummbadreps.setText(("-"));
        tvnumgoodreps = root.findViewById(R.id.buenasrepes);
        tvnumgoodreps.setText(("-"));
        tvresttime = root.findViewById(R.id.idtiempordescanso);
        tvresttime.setText(("-"));
        tvmovement_name = root.findViewById(R.id.idnombremovimiento);
        tvmovement_name.setText(("-"));
        rvreciver = root.findViewById(R.id.idreciverrv);

        mode = 2;
        ivGif = root.findViewById(R.id.idgif);

        //---------CHOOSE THE MDE TO RECIVE THE REPETITIONS-----------
        //checkmode();
        imageconnect = root.findViewById(R.id.idimagenconnect);
        dat = false;

        //---------GET BITMAP FROM RESOURCE FOR PUT THE ON IMAGE-----------
        Bitmap originalBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.botononoff);

        //---------CUT THE BITMAP TO SQUARE-----------
        originalBitmap = Utilidades.cropToSquare(originalBitmap);

        //---------CREATE THE ROUNDEDBITMAP-----------
        roundedDrawable = RoundedBitmapDrawableFactory.create(mContext.getResources(), originalBitmap);
        roundedDrawable.setCornerRadius(originalBitmap.getHeight());


        //---------CHECK IF THE DEVICE HAS A SUPORT BLUETOOTH-----------
        if (bluetoothAdapter == null) {
            new AlertDialog.Builder(mContext)
                    .setTitle("Not compatible")
                    .setMessage("Your phone does not support Bluetooth")
                    .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(0);
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

        }

        //---------SEARCH A DEVICE AND RECIVE SENSOR DATA FROM DEVICE-----------
        connection();


        //---------RESTART THE CONNECTION-----------
        buttonReceiver.setOnClickListener(v -> {

            //---------PUT ALL TO INIT VALUES-----------
            buttonReceiver.setEnabled(false);
            buttonReceiver.setImageResource(android.R.color.transparent);
            tvcomment.setText("");

            tvmovement_name.setText("-");
            tvnumreps.setText("-");
            tvnumgoodreps.setText("-");
            tvnummbadreps.setText("-");
            tvresttime.setText("-");
            restime = 0;

            List<Repetition> reps = new ArrayList<>();

            adapter = new rvReciverReps(mContext, reps);
            rvreciver.setAdapter(adapter);
            rvreciver.setLayoutManager(new LinearLayoutManager(mContext.getApplicationContext()));

            //---------SEARCH A DEVICE AND RECIVE SENSOR DATA FROM DEVICE-----------
            connection();


        });


      /*  buttonincorrect.setOnClickListener(v -> {
            mode = 3;
            checkmode();

        });
        buttonalgorithm.setOnClickListener(v -> {
            mode = 2;
            checkmode();

        });
        buttoncorrect.setOnClickListener(v -> {
            mode = 1;
            checkmode();

        });*/

        return root;
    }

    public void checkmode() {
        if (mode == 1) {
            buttoncorrect.setBackgroundColor(Color.parseColor("#70FFFFFF"));
            buttonalgorithm.setBackgroundColor(Color.parseColor("#00FFFFFF"));
            buttonincorrect.setBackgroundColor(Color.parseColor("#00FFFFFF"));
        } else if (mode == 2) {
            buttoncorrect.setBackgroundColor(Color.parseColor("#00FFFFFF"));
            buttonalgorithm.setBackgroundColor(Color.parseColor("#70FFFFFF"));
            buttonincorrect.setBackgroundColor(Color.parseColor("#00FFFFFF"));
        } else {
            buttoncorrect.setBackgroundColor(Color.parseColor("#00FFFFFF"));
            buttonalgorithm.setBackgroundColor(Color.parseColor("#00FFFFFF"));
            buttonincorrect.setBackgroundColor(Color.parseColor("#70FFFFFF"));

        }
    }

    //SIRVE PARA EXTRAR TODOS LOS DATOS DE UN EJE DE LOS DATOS DEL ACELEROMETRO
    public List<Float> extraer_datos_eje(List<Accelerometer> acel, String eje) {
        List<Float> ret = new ArrayList<>();
        if (eje.equals("x")) {

            for (int i = 0; i < acel.size(); i++) {
                ret.add(acel.get(i).getX());
            }

        } else if (eje.equals("y")) {

            for (int i = 0; i < acel.size(); i++) {
                ret.add(acel.get(i).getY());
            }

        } else {
            for (int i = 0; i < acel.size(); i++) {
                ret.add(acel.get(i).getZ());
            }

        }
        return ret;
    }

    public double mean(List<Accelerometer> dataRep, String eje, String sensor, List<Gyroscope> dataRep1) {
        double mean = 0;
        switch (eje) {
            case "x":
                if (sensor.equals("Accelerometer")) {
                    for (int i = 0; i < dataRep.size(); i++) {
                        mean = +dataRep.get(i).getX();
                    }
                    mean = mean / dataRep.size();
                } else {
                    for (int i = 0; i < dataRep1.size(); i++) {
                        mean = +dataRep1.get(i).getX();
                    }
                    mean = mean / dataRep1.size();
                }

                break;
            case "y":
                if (sensor.equals("Accelerometer")) {
                    for (int i = 0; i < dataRep.size(); i++) {
                        mean = +dataRep.get(i).getY();
                    }
                    mean = mean / dataRep.size();
                } else {
                    for (int i = 0; i < dataRep1.size(); i++) {
                        mean = +dataRep1.get(i).getY();
                    }
                    mean = mean / dataRep1.size();
                }
                break;
            case "z":
                if (sensor.equals("Accelerometer")) {
                    for (int i = 0; i < dataRep.size(); i++) {
                        mean = +dataRep.get(i).getZ();
                    }
                    mean = mean / dataRep.size();
                } else {
                    for (int i = 0; i < dataRep1.size(); i++) {
                        mean = +dataRep1.get(i).getZ();
                    }
                    mean = mean / dataRep1.size();
                }
                break;


        }
        return mean;
    }

    public double variance(List<Accelerometer> dataRep, double mean, String eje, String sensor, List<Gyroscope> dataRep1) {
        double temp = 0;
        switch (eje) {
            case "x":
                if (sensor.equals("Accelerometer")) {
                    for (int i = 0; i < dataRep.size(); i++) {
                        temp += (dataRep.get(i).getX() - mean) * (dataRep.get(i).getX() - mean);
                    }
                } else {
                    for (int i = 0; i < dataRep1.size(); i++) {
                        temp += (dataRep1.get(i).getX() - mean) * (dataRep1.get(i).getX() - mean);
                    }
                }
                break;
            case "y":
                if (sensor.equals("Accelerometer")) {
                    for (int i = 0; i < dataRep.size(); i++) {
                        temp += (dataRep.get(i).getY() - mean) * (dataRep.get(i).getY() - mean);
                    }
                } else {
                    for (int i = 0; i < dataRep1.size(); i++) {
                        temp += (dataRep1.get(i).getY() - mean) * (dataRep1.get(i).getY() - mean);
                    }
                }
                break;
            case "z":
                if (sensor.equals("Accelerometer")) {
                    for (int i = 0; i < dataRep.size(); i++) {
                        temp += (dataRep.get(i).getZ() - mean) * (dataRep.get(i).getZ() - mean);
                    }
                } else {
                    for (int i = 0; i < dataRep1.size(); i++) {
                        temp += (dataRep1.get(i).getZ() - mean) * (dataRep1.get(i).getZ() - mean);
                    }
                }
                break;
        }
        if (sensor.equals("Accelerometer")) {
            temp = temp / (dataRep.size() - 1);
        } else {
            temp = temp / (dataRep1.size() - 1);
        }
        return temp;

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public double getSkewness(List<Accelerometer> dataRep, String eje, String sensor, List<Gyroscope> dataRep1) {
        double ret;
        double[] arr = null;
        Skewness ske = new Skewness();
        switch (eje) {
            case "x":
                if (sensor.equals("Accelerometer")) {
                    arr = dataRep.stream().mapToDouble(value -> Float.valueOf(value.getX())).toArray(); //via method reference
                } else {
                    arr = dataRep1.stream().mapToDouble(value -> Float.valueOf(value.getX())).toArray(); //via method reference

                }
                break;
            case "y":
                if (sensor.equals("Accelerometer")) {
                    arr = dataRep.stream().mapToDouble(value -> Float.valueOf(value.getY())).toArray(); //via method reference
                } else {
                    arr = dataRep1.stream().mapToDouble(value -> Float.valueOf(value.getY())).toArray(); //via method reference

                }
                break;
            case "z":
                if (sensor.equals("Accelerometer")) {
                    arr = dataRep.stream().mapToDouble(value -> Float.valueOf(value.getZ())).toArray(); //via method reference
                } else {
                    arr = dataRep1.stream().mapToDouble(value -> Float.valueOf(value.getZ())).toArray(); //via method reference

                }
                break;
        }


        ret = ske.evaluate(arr, 0, arr.length);
        return ret;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<Repetition> add_reps(String fecha, String nombreUsuario, int numRep) {

        List<Repetition> returnRep = new ArrayList<>();
        long inicio, fin;
        for (int i = 1; i <= numRep; i++) {
            double meanXA, meanYA, meanZA, skewnessXA, skewnessYA, skewnessZA, varianceXA, varianceYA, varianceZA;
            List<Accelerometer> dataRep = database.getInstance(mContext).getAccelerometerDAO().getReps(i);
            Log.d(TAG, "LISTA" + dataRep.size());

            inicio = dataRep.get(0).getTime();
            fin = dataRep.get(dataRep.size() - 1).getTime();
            Log.d(TAG, "add_reps: INI" + inicio);
            Log.d(TAG, "add_reps: FIN" + fin);
            Repetition rep = new Repetition(movement_name, fecha, nombreUsuario, inicio, fin, i);

            meanXA = mean(dataRep, "x", "Accelerometer", null);
            meanYA = mean(dataRep, "y", "Accelerometer", null);
            meanZA = mean(dataRep, "z", "Accelerometer", null);
            varianceXA = variance(dataRep, meanXA, "x", "Accelerometer", null);
            varianceYA = variance(dataRep, meanYA, "y", "Accelerometer", null);
            varianceZA = variance(dataRep, meanZA, "z", "Accelerometer", null);
            skewnessXA = getSkewness(dataRep, "x", "Accelerometer", null);
            skewnessYA = getSkewness(dataRep, "y", "Accelerometer", null);
            skewnessZA = getSkewness(dataRep, "z", "Accelerometer", null);
            if (mode == 1) {
                rep.add_data_acel(meanXA, meanYA, meanZA, varianceXA, varianceYA, varianceZA, skewnessXA, skewnessYA, skewnessZA, 0);
            } else if (mode == 2) {
                rep.add_data_acel(meanXA, meanYA, meanZA, varianceXA, varianceYA, varianceZA, skewnessXA, skewnessYA, skewnessZA, 1);

            } else if (mode == 3) {
                rep.add_data_acel(meanXA, meanYA, meanZA, varianceXA, varianceYA, varianceZA, skewnessXA, skewnessYA, skewnessZA, 1);

            }
            returnRep.add(rep);
        }
        load_gyro_reps(returnRep);
        for (int i = 1; i <= numRep; i++) {
            double meanXG, meanYG, meanZG, skewnessXG, skewnessYG, skewnessZG, varianceXG, varianceYG, varianceZG;
            double minXG, maxXG, minYG, maxYG, minZG, maxZG;
            List<Gyroscope> dataRep1 = database.getInstance(mContext).getGyroscopeDAO().getReps(i);
            meanXG = mean(null, "x", "Gyroscope", dataRep1);
            meanYG = mean(null, "y", "Gyroscope", dataRep1);
            meanZG = mean(null, "z", "Gyroscope", dataRep1);
            varianceXG = variance(null, meanXG, "x", "Gyroscope", dataRep1);
            varianceYG = variance(null, meanYG, "y", "Gyroscope", dataRep1);
            varianceZG = variance(null, meanZG, "z", "Gyroscope", dataRep1);
            skewnessXG = getSkewness(null, "x", "Gyroscope", dataRep1);
            skewnessYG = getSkewness(null, "y", "Gyroscope", dataRep1);
            skewnessZG = getSkewness(null, "z", "Gyroscope", dataRep1);
            minXG = minAbsolute(dataRep1, "x");
            maxXG = maxAbsolute(dataRep1, "x");
            minYG = minAbsolute(dataRep1, "y");
            maxYG = maxAbsolute(dataRep1, "y");
            minZG = minAbsolute(dataRep1, "z");
            maxZG = maxAbsolute(dataRep1, "z");
            returnRep.get(i - 1).add_data_gyr(meanXG, meanYG, meanZG, varianceXG, varianceYG, varianceZG, skewnessXG, skewnessYG, skewnessZG, minXG, minYG, minZG, maxXG, maxYG, maxZG);
        }

        return returnRep;
    }

    public void load_gyro_reps(List<Repetition> returnRep) {
        List<Gyroscope> gyrosco = database.getInstance(mContext).getGyroscopeDAO().getAll();
        for (int i = 0; i < returnRep.size(); i++) {

            long ini = returnRep.get(i).getInicio();
            long fin = returnRep.get(i).getFin();
            for (int y = 0; y < gyrosco.size(); y++) {
                if (gyrosco.get(y).getTime() >= ini && gyrosco.get(y).getTime() <= fin) {
                    Gyroscope aux2 = gyrosco.get(y);
                    aux2.setTask(i + 1);
                    database.getInstance(mContext).getGyroscopeDAO().update(aux2);
                }
            }
        }
    }

    public double maxAbsolute(List<Gyroscope> gyro, String eje) {
        double max = 0;
        if (eje.equals("x")) {


            for (int i = 0; i < gyro.size(); i++) {
                if (gyro.get(i).getX() > max) {
                    max = gyro.get(i).getX();
                }
            }
        } else if (eje.equals("y")) {
            for (int i = 0; i < gyro.size(); i++) {
                if (gyro.get(i).getY() > max) {
                    max = gyro.get(i).getY();
                }
            }
        } else {
            for (int i = 0; i < gyro.size(); i++) {
                if (gyro.get(i).getZ() > max) {
                    max = gyro.get(i).getZ();
                }
            }
        }
        return max;
    }

    public double minAbsolute(List<Gyroscope> gyro, String eje) {
        double min = 100;
        if (eje.equals("x")) {


            for (int i = 0; i < gyro.size(); i++) {
                if (gyro.get(i).getX() < min) {
                    min = gyro.get(i).getX();
                }
            }
        } else if (eje.equals("y")) {
            for (int i = 0; i < gyro.size(); i++) {
                if (gyro.get(i).getY() < min) {
                    min = gyro.get(i).getY();
                }
            }
        } else {
            for (int i = 0; i < gyro.size(); i++) {
                if (gyro.get(i).getZ() < min) {
                    min = gyro.get(i).getZ();
                }
            }
        }
        return min;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public int load_acel_reps(List<Integer> index_relatives, List<Accelerometer> acel, String eje, List<Gyroscope> gyro) throws IOException {
        List<Float> aux;
        Float ratio;
        int index = 0;
        int max;
        boolean change = false;
        Float previous = Float.valueOf(0);
        List<Integer> index_repetitions = new ArrayList<>();
        List<Integer> index_fin = new ArrayList<>();

        if (eje.equals("x")) {

            Log.d(TAG, "INDICES RELATIVOS: " + index_relatives.toString());
            aux = extraer_datos_eje(acel, "x");
            boolean aux4 = false;
            bount = false;
            int count1 = 0;
            boolean noesfin = false;
            //ADD TO INDEX START LIST
            for (int i = 0; i < index_relatives.size(); i++) {
                if (i != 0) {
                    //GET THE RATIO BETWEEN THE ACTUAL AND THE PREVIOUS
                    ratio = aux.get(index_relatives.get(i)) - previous;
                    Log.d(TAG, "saca_repes: " + ratio);

                    //IF THIS RATIO IS BIGGER THAN 2 AND YOU HAVEN´T STARTED 1 REP
                    if (ratio >= 2 && !bount) {
                        index_repetitions.add(index_relatives.get(i - 1));//MAYBE I-1
                        Log.d(TAG, "saca_repes: " + "REPETICION");
                        bount = true;
                        noesfin = false;
                    }
                    //IF YOU HAVE STARTED 1 REP
                    if (bount) {
                        //IF THE VALUE IS LESS THAN 0

                        //SEARCH THE FINAL OF THE REPETITION

                        if ( (aux.get(index_relatives.get(i)) <= 0.4) && (aux.get(index_relatives.get(i)) >=-0.4)) {
                            count1++;
                        } else {
                            count1 = 0;
                        }

                        if (count1 == 3 && !noesfin) {

                            //REST VALUES AND SAVE FINAL
                            index_fin.add(index_relatives.get(i - 2));
                            noesfin = true;
                            bount = false;
                            aux4 = false;
                            count1 = 0;
                        }


                    }
                }
                previous = aux.get(index_relatives.get(i));

            }


            //ADD DATA TO DATABASES
            max = index_repetitions.size();
            Log.d(TAG, "INDICE REPES: " + index_repetitions.toString());
            Log.d(TAG, "INDICE FIN REPES: " + index_fin.toString());
            int aux2 = 0;
            if (max > 0) {
                if (index_fin.size() == index_repetitions.size()) {

                    for (int i = 0; i < acel.size(); i++) {

                        if ((index < max)) {

                            if (i == index_repetitions.get(index)) {
                                Log.d(TAG, "ENTROO: " + index);
                                if (index != 0 && aux2 < index_fin.size()) {
                                    aux2++;
                                }
                                index++;
                            }

                        }

                        if (index_repetitions.get(0) < i) {
                            if (i < index_fin.get(aux2)) {
                                if (index != 0) {
                                    Accelerometer acel1 = acel.get(i);

                                    acel1.setTask(index);

                                    database.getInstance(mContext).getAccelerometerDAO().update(acel1);
                                }
                            } else {
                                if (aux2 != index_fin.size() - 1) {
                                    Accelerometer acel1 = acel.get(i);

                                    acel1.setTask(0);

                                    database.getInstance(mContext).getAccelerometerDAO().update(acel1);
                                } else {
                                    Accelerometer acel1 = acel.get(i);

                                    acel1.setTask(-1);

                                    database.getInstance(mContext).getAccelerometerDAO().update(acel1);
                                }
                            }

                        }


                    }
                } else {
                    Toast.makeText(mContext, "There are a problem with the repetitions try again", Toast.LENGTH_LONG).show();
                    problem = true;
                }
            } else {
                Toast.makeText(mContext, "You haven´t done any repetition", Toast.LENGTH_LONG).show();
            }
        } else if (eje.equals("y")) {


        } else {


        }
        return index_repetitions.size();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void principal_function() throws IOException {

        //ADD MOVEMENT TO DATABASE
        Calendar c = Calendar.getInstance();
        String dia = Integer.toString(c.get(Calendar.DATE));
        String month = Integer.toString(c.get(Calendar.MONTH));
        String annio = Integer.toString(c.get(Calendar.YEAR));
        String date = dia + "/" + month + "/" + annio;
        SharedPreferences preferences = mContext.getSharedPreferences("PreferenciasUsuario", Context.MODE_PRIVATE);
        String userName = preferences.getString("usuario", "usuario");

        if (database.getInstance(mContext).getMovementDAO().getCount(movement_name, userName, date) == 0) {
            Movement movement = new Movement(movement_name, userName, date, 0, 0);
            database.getInstance(mContext).getMovementDAO().insert(movement);
        }


        List<Integer> relativos = maxRelativos(database.getInstance(mContext).getAccelerometerDAO().getAll(), "x");
        numRep = load_acel_reps(relativos, database.getInstance(mContext).getAccelerometerDAO().getAll(), "x", database.getInstance(mContext).getGyroscopeDAO().getAll());

        calculateRestTime(database.getInstance(mContext).getAccelerometerDAO().getAll());
        if (!problem) {
            if (numRep > 0) {
                List<Repetition> reps = add_reps(date, userName, numRep);

                adapter = new rvReciverReps(mContext, reps);
                rvreciver.setAdapter(adapter);
                rvreciver.setLayoutManager(new LinearLayoutManager(mContext.getApplicationContext()));
                try {
                    exportarCSV();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                for (int i=0; i<reps.size();i++){


                    normalize(reps.get(i));
                }

                if (mode == 2) {
                    //decision tree
                    decisionTree(reps, "Lateral Elevations");
                }
                exportar("Repetition", reps);
                int repsGood = 0;
                int repsBad = 0;
                for (int x = 0; x < reps.size(); x++) {
                    if (reps.get(x).getRepOK()==0) {
                        repsGood++;
                    } else {
                        repsBad++;
                    }
                }
                tvnumgoodreps.setText(String.valueOf(repsGood));
                tvnummbadreps.setText(String.valueOf(repsBad));
                for (int i = 0; i < reps.size(); i++) {
                    database.getInstance(mContext).getRepetitionDAO().insert(reps.get(i));
                }
            } else {
                exportarCSV();
            }
        } else {
            String dir = Environment.getExternalStorageDirectory() + "/FitControlImages/GIF/lateralelevations.gif";
            loadGif(dir);
            tvcomment.setText("No repetition saved, you must wait between each repetition and \n" +
                    "the eccentric phase must be done slowly");
            exportarCSV();
        }


    }

    public void loadGif(String dir) {
        ivGif.setVisibility(View.VISIBLE);
        String path = "android.resource://" + mContext.getPackageName() + "/" + R.raw.lateralelevations;
        Uri uri = Uri.parse(path);
        ivGif.setMediaController(new MediaController(mContext));
        ivGif.setVideoURI(uri);
        ivGif.requestFocus();
        ivGif.start();


    }

    public void decisionTree(List<Repetition> reps, String mov) {
        switch (mov) {
            case "Lateral Elevations":
                for (int i = 0; i < reps.size(); i++) {

                    if (reps.get(i).getMaxYG() <= 0.98) {
                        //BAD YOU SHOULD FLEX LESS YOU ELBOW
                        reps.get(i).setRepOK(1);
                    } else if (reps.get(i).getMaxXG() <= 0.43) {
                        reps.get(i).setRepOK(2);
                    } else {
                        reps.get(i).setRepOK(0);
                    }

                }
                break;
        }
    }

    public void calculateRestTime(List<Accelerometer> acel) {

        boolean follow = false;
        long first = 0, last = 0;
        for (int i = 0; i < acel.size(); i++) {
            if (acel.get(i).getTask() == 0) {
                if (!follow) {
                    first = acel.get(i).getTime();
                    follow = true;
                } else {
                    last = acel.get(i).getTime();
                }

            } else {
                if (follow) {
                    restime = +(last - first);
                    follow = false;
                }
            }
        }
    }

    public static double redondearDecimales(double valorInicial, int numeroDecimales) {
        double parteEntera, resultado;
        resultado = valorInicial;
        parteEntera = Math.floor(resultado);
        resultado = (resultado - parteEntera) * Math.pow(10, numeroDecimales);
        resultado = Math.round(resultado);
        resultado = (resultado / Math.pow(10, numeroDecimales)) + parteEntera;
        return resultado;
    }

    public List<Integer> maxRelativos(List<Accelerometer> acel, String eje) {
        List<Integer> indices = new ArrayList<>();
        float anterior = 0;
        if (eje.equals("x")) {

            for (int i = 0; i < acel.size(); i++) {
                if (i != acel.size() - 1 && i != 0) {
                    if ((anterior < acel.get(i).getX()) && (acel.get(i + 1).getX() < acel.get(i).getX())) {
                        indices.add(i - 1);
                    }
                    anterior = acel.get(i).getX();


                }

            }
        } else if (eje.equals("y")) {


        } else {


        }


        return indices;
    }

    public void dividir(String a) {
        String[] first = a.split("\\%");
        if (first.length > 1) {
            dat = true;
            String[] senso = first[1].split("\\#");
            movement_name = first[0];
            if (senso.length != 0) {

                String acelero = senso[0]; //DATA OF ACCELEROMETER SENSOR
                String[] eachDataA = acelero.split("\\/");

                for (int i = 0; i < eachDataA.length; i++) {
                    String[] atributes = eachDataA[i].split("\\,");
                    if (atributes.length == 5) {

                        //ADD ROW TO DATABASE
                        Accelerometer aux = new Accelerometer(Long.parseLong(atributes[0]), Float.parseFloat(atributes[1]), Float.parseFloat(atributes[2]), Float.parseFloat(atributes[3]), Integer.parseInt(atributes[4]));
                        database.getInstance(mContext).getAccelerometerDAO().insert(aux);
                    }
                }

            }
            if (senso.length == 2) {

                String gyro = senso[1]; //DATA OF GYROSCOPE SENSOR      <-----------
                String[] eachDataG = gyro.split("\\/");

                for (int i = 0; i < eachDataG.length; i++) {
                    String[] atributes1 = eachDataG[i].split("\\,");
                    if (atributes1.length == 5) {

                        //ADD ROW TO DATABASE
                        Gyroscope aux = new Gyroscope(Long.parseLong(atributes1[0]), Float.parseFloat(atributes1[1]), Float.parseFloat(atributes1[2]), Float.parseFloat(atributes1[3]), Integer.parseInt(atributes1[4]));
                        database.getInstance(mContext).getGyroscopeDAO().insert(aux);
                    }
                }
            }
        }


    }

    @Override
    public void onPause() {


//        MBS.cancel();
        super.onPause();
    }

    private class AsyncReceiver extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            new AsyncParser().execute();
            super.onPreExecute();
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected void onPostExecute(Void aVoid) {
            new AsyncMostrar().execute();
            super.onPostExecute(aVoid);
        }

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {
            imageconnect.setImageResource(android.R.drawable.presence_online);
            tvcomment.setText("Remember to click green button in your wearable device when you finish the movements");
            return null;
        }
    }

    private class AsyncMostrar extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {

            mostrar();
            super.onPreExecute();
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected void onPostExecute(Void aVoid) {

            dividir(aux);
            if (dat) {
                try {
                    principal_function();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                dat = false;
            }

            buttonReceiver.setEnabled(true);
            buttonReceiver.setImageDrawable(roundedDrawable);
            tvmovement_name.setText(movement_name);
            if (!problem) {
                tvnumreps.setText(String.valueOf(numRep));

                double fecha = redondearDecimales(restime * Math.pow(10, -9), 2);
                Log.d(TAG, "onPostExecute: fecha" + fecha);
                String FORMAT = "%02d:%02d:%02d";
                String myTime = String.format(FORMAT,
                        //Minutes
                        TimeUnit.SECONDS.toMinutes((long) fecha) -
                                TimeUnit.HOURS.toMinutes(TimeUnit.SECONDS.toHours((long) fecha)),
                        //Seconds
                        TimeUnit.SECONDS.toSeconds((long) fecha) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes((long) fecha)),
                        //nanoSeconds
                        (long) redondearDecimales((fecha - ((int) fecha)) * 100, 0)

                );
                tvresttime.setText(myTime);


                tvcomment.setText("");
            }
            problem = false;
            super.onPostExecute(aVoid);
        }

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {

           // tvcomment.setText("Received data \n Processing data");
            return null;
        }
    }

    private class AsyncParser extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            myprogressbar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            myprogressbar.setVisibility(View.GONE);
            Toast.makeText(mContext, "Dispositivo Conectado", Toast.LENGTH_SHORT).show();

            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            //ACCEPT A DEVICE
            acceptThread = new AcceptThread(bluetoothAdapter);

            acceptThread.run();

            //MAKE A CONEXION

            MBS = new MyBluetoothService.ConnectedThread(acceptThread.getSocket(), handler);

            return null;
        }
    }

    public void normalize(Repetition rep){

        double max=0,maxVariance=0,maxMed=0,maxSkew=0;
        double mixVariance=1000,minMed=1000,mixSkew=1000;

        //MAX
        if(rep.getMaxXG()>rep.getMaxYG()){
            max=rep.getMaxXG();
        }else{
            max=rep.getMaxYG();
        }

        //MAXVARIANCE
        if(rep.getVarianzaXG()>rep.getVarianzaYG()){

            if(rep.getVarianzaXG()>rep.getVarianzaZG()) {
                maxVariance = rep.getVarianzaXG();
            }else{
                maxVariance = rep.getVarianzaZG();
            }
        }else{
            if(rep.getVarianzaYG()>rep.getVarianzaZG()) {
                maxVariance = rep.getVarianzaYG();
            }else{
                maxVariance = rep.getVarianzaZG();
            }
        }
        //MIXVARIANCE
        if(rep.getVarianzaXG()<rep.getVarianzaYG()){

            if(rep.getVarianzaXG()<rep.getVarianzaZG()) {
                mixVariance = rep.getVarianzaXG();
            }else{
                mixVariance = rep.getVarianzaZG();
            }
        }else{
            if(rep.getVarianzaYG()<rep.getVarianzaZG()) {
                mixVariance = rep.getVarianzaYG();
            }else{
                mixVariance = rep.getVarianzaZG();
            }
        }

        //MAXMEDIA
        if(rep.getMediaXG()>rep.getMediaYG()){

            if(rep.getMediaXG()>rep.getMediaZG()) {
                maxMed = rep.getMediaXG();
            }else{
                maxMed = rep.getMediaZG();
            }
        }else{
            if(rep.getMediaYG()>rep.getMediaZG()) {
                maxMed = rep.getMediaYG();
            }else{
                maxMed = rep.getMediaZG();
            }
        }

        //MIXMEDIA
        if(rep.getMediaXG()<rep.getMediaYG()){

            if(rep.getMediaXG()<rep.getMediaZG()) {
                minMed = rep.getMediaXG();
            }else{
                minMed = rep.getMediaZG();
            }
        }else{
            if(rep.getMediaYG()<rep.getMediaZG()) {
                minMed = rep.getMediaYG();
            }else{
                minMed = rep.getMediaZG();
            }
        }
        //MAXSKEWNESS
        if(rep.getSkewnessXG()>rep.getSkewnessYG()){

            if(rep.getSkewnessXG()>rep.getSkewnessZG()) {
                maxSkew = rep.getSkewnessXG();
            }else{
                maxSkew = rep.getSkewnessZG();
            }
        }else{
            if(rep.getSkewnessYG()>rep.getSkewnessZG()) {
                maxSkew = rep.getSkewnessYG();
            }else{
                maxSkew = rep.getSkewnessZG();
            }
        }

        //MINSKEW
        if(rep.getSkewnessXG()<rep.getSkewnessYG()){

            if(rep.getSkewnessXG()<rep.getSkewnessZG()) {
                mixSkew = rep.getSkewnessXG();
            }else{
                mixSkew = rep.getSkewnessZG();
            }
        }else{
            if(rep.getSkewnessYG()<rep.getSkewnessZG()) {
                mixSkew = rep.getSkewnessYG();
            }else{
                mixSkew = rep.getSkewnessZG();
            }
        }

        rep.setMaxXG((rep.getMaxXG()/max));
        rep.setMaxYG((rep.getMaxYG()/max));

        rep.setVarianzaXG(((rep.getVarianzaXG()-mixVariance)/(maxVariance-mixVariance)));
        rep.setVarianzaYG(((rep.getVarianzaYG()-mixVariance)/(maxVariance-mixVariance)));
        rep.setVarianzaZG(((rep.getVarianzaZG()-mixVariance)/(maxVariance-mixVariance)));

        rep.setMediaXG(((rep.getMediaXG()-minMed)/(maxMed-minMed)));
        rep.setMediaYG(((rep.getMediaYG()-minMed)/(maxMed-minMed)));
        rep.setMediaZG(((rep.getMediaZG()-minMed)/(maxMed-minMed)));

        rep.setSkewnessXG(((rep.getSkewnessXG()-mixSkew)/(maxSkew-mixSkew)));
        rep.setSkewnessYG(((rep.getSkewnessYG()-mixSkew)/(maxSkew-mixSkew)));
        rep.setSkewnessZG(((rep.getSkewnessZG()-mixSkew)/(maxSkew-mixSkew)));



    }
    public void connection() {

        //
        //------------------CHECK IF BLUETOOTH IS ON AND IF NOT SWITCH ON-------------------------
        //
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);
        }
//AsyncParser
        new AsyncReceiver().execute();

        Toast.makeText(mContext, "Searching device", Toast.LENGTH_SHORT).show();


    }

    public void mostrar() {
        //LISTEN FROM THE DEVICE
        MBS.run();

        MBS.cancel();

        imageconnect.setImageResource(android.R.drawable.ic_notification_overlay);
    }

    public boolean isExternalStorageWritable() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            Log.i(TAG, "isExternalStorageWritable: Yes, you can write on it");
            return true;
        } else {
            return false;
        }
    }

    //IF YOU WANT TO USE THIS FUNCTION YOU HAVE TO GIVE PERMISSION TO SAVE IN THE DEVICE
    public void exportar(String nombre, List<Repetition> reps) throws IOException {
        //listFiles();

        database bd = database.getInstance(mContext);

        if (isExternalStorageWritable()) {
            try {
                File texFile = new File(Environment.getExternalStorageDirectory(), nombre + ".csv");
                List<String[]> datos = new ArrayList<>();

                if (nombre.equals("Accelerometer")) {
                    datos.add(new String[]{"Tiempo", "x", "y", "z", "task"});
                    List<Accelerometer> acel = bd.getAccelerometerDAO().getAll();
                    for (int i = 0; i < acel.size(); i++) {
                        datos.add(new String[]{Long.toString(acel.get(i).getTime()), Float.toString(acel.get(i).getX()), Float.toString(acel.get(i).getY()), Float.toString(acel.get(i).getZ()), Integer.toString(acel.get(i).getTask())});
                    }


                }

                if (nombre.equals("Gyroscope")) {
                    datos.add(new String[]{"Tiempo", "x", "y", "z", "task"});
                    List<Gyroscope> gyr = bd.getGyroscopeDAO().getAll();
                    for (int i = 0; i < gyr.size(); i++) {
                        datos.add(new String[]{Long.toString(gyr.get(i).getTime()), Float.toString(gyr.get(i).getX()), Float.toString(gyr.get(i).getY()), Float.toString(gyr.get(i).getZ()), Integer.toString(gyr.get(i).getTask())});
                    }

                }

                if (nombre.equals("Repetition")) {
                    if (mode == 1) {
                        texFile = new File(Environment.getExternalStorageDirectory(), nombre + "Positive.csv");
                    } else if (mode == 2) {
                        texFile = new File(Environment.getExternalStorageDirectory(), nombre + ".csv");
                    } else if (mode == 3) {
                        texFile = new File(Environment.getExternalStorageDirectory(), nombre + "Negative.csv");
                    }
                    datos.add(new String[]{"MovementName", "date", "user", "start", "end", "mediaXA", "mediaYA", "mediaZA", "varianzaXA", "varianzaYA", "varianzaZA", "skewnessXA", "skewnessYA", "skewnessZA", "mediaXG", "mediaYG", "mediaZG", "varianzaXG", "varianzaYG", "varianzaZG", "skewnessXG", "skewnessYG", "skewnessZG", "minXG", "maxXG", "minYG", "maxYG", "minZG", "maxZG", "repOK"});

                    for (int i = 0; i < reps.size(); i++) {
                        //INSERT THE CORRECT DATA TO REPETITION
                        datos.add(new String[]{reps.get(i).getNameMovement(), reps.get(i).getDate(), reps.get(i).getName(), Long.toString(reps.get(i).getInicio()), Long.toString(reps.get(i).getFin()),

                                String.valueOf(reps.get(i).getMediaXA()), String.valueOf(reps.get(i).getMediaYA()), String.valueOf(reps.get(i).getMediaZA()),
                                String.valueOf(reps.get(i).getVarianzaXA()), String.valueOf(reps.get(i).getVarianzaYA()), String.valueOf(reps.get(i).getVarianzaZA()),
                                String.valueOf(reps.get(i).getSkewnessXA()), String.valueOf(reps.get(i).getSkewnessYA()), String.valueOf(reps.get(i).getSkewnessZA()),

                                String.valueOf(reps.get(i).getMediaXG()), String.valueOf(reps.get(i).getMediaYG()), String.valueOf(reps.get(i).getMediaZG()),
                                String.valueOf(reps.get(i).getVarianzaXG()), String.valueOf(reps.get(i).getVarianzaYG()), String.valueOf(reps.get(i).getVarianzaZG()),
                                String.valueOf(reps.get(i).getSkewnessXG()), String.valueOf(reps.get(i).getSkewnessYG()), String.valueOf(reps.get(i).getSkewnessZG()),

                                String.valueOf(reps.get(i).getMinXG()), String.valueOf(reps.get(i).getMaxXG()),
                                String.valueOf(reps.get(i).getMinYG()), String.valueOf(reps.get(i).getMaxYG()),
                                String.valueOf(reps.get(i).getMinZG()), String.valueOf(reps.get(i).getMaxZG()),

                                String.valueOf(reps.get(i).getRepOK())
                        });
                    }

                }

                //String archCSV = "./" + nombre + ".csv";
                CSVWriter writer = new CSVWriter(new FileWriter(texFile));

                writer.writeAll(datos);
                writer.flush();
                writer.close();


            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void exportarCSV() throws IOException {
        exportar("Accelerometer", null);
        exportar("Gyroscope", null);
        database.getInstance(mContext).getAccelerometerDAO().clearAll();
        database.getInstance(mContext).getGyroscopeDAO().clearAll();

    }


}
