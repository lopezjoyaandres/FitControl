package com.FitControl.ui.main;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.FitControl.R;
import com.FitControl.tables.DataSensor;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends WearableActivity implements SensorEventListener {
    private static final String TAG = "MainActivity";
    private TextView mTextView;
    private MyBluetoothService.ConnectedThread MBS=null;
    protected BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    protected BluetoothSocket sock;
    protected ConnectThread conex;
    protected BluetoothDevice device;
    protected ArrayList<DataSensor> dataAccelerometer;
    protected ArrayList<DataSensor> dataMagnetometer;
    protected ArrayList<DataSensor> dataGyroscope;
    private SensorManager sensorManager;
    protected Sensor accelerometerSensor ;
    protected Sensor magnetometerSensor;
    protected Sensor gyroscopeSensor ;
    private boolean start ;
    private int rep;
    private float total;
    private boolean vuelta;
    private float anterior;
    private boolean activo;
    private Handler h;
    private String mac;
    private boolean isend;
    private ImageButton play_pause;
    private ImageButton send;
    private TextView text;
    private int contador;


    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener((SensorEventListener) this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mTextView = (TextView) findViewById(R.id.text);
        start = false;
        isend=false;
        dataAccelerometer=new ArrayList<DataSensor>();
        dataGyroscope=new ArrayList<DataSensor>();
        dataMagnetometer=new ArrayList<DataSensor>();
        rep=-1;
        activo=false;
        vuelta=false;
        anterior=0;
        text=findViewById(R.id.count);
        send=findViewById(R.id.idsend);
        h=new Handler();
        play_pause=findViewById(R.id.Buttonplay);

        text.setText("");

        // Enables Always-on
        setAmbientEnabled();
        //Toast.makeText(MainActivity.this, (int) System.currentTimeMillis(), Toast.LENGTH_SHORT).show();
        play_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncCount().execute();

            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviar();
                Toast.makeText(MainActivity.this,"Data send", Toast.LENGTH_SHORT).show();
                finish();

            }
        });
//-------------------------------------------------------------------------------------------------------------


        //
        //------------------CHECK IF DEVICE CAN SUPPORT BLUETOOTH-------------------------
        //

        if (bluetoothAdapter == null) {
            new AlertDialog.Builder(this)
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
//-------------------------------------------------------------------------------------------------------------

        //
        //------------------CHECK IF BLUETOOTH IS ON AND IF NOT SWITCH ON-------------------------
        //

        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);
        }
//-------------------------------------------------------------------------------------------------------------

        //
        //------------------CHECK SENSORS-------------------------
        //

            //ACCELEROMETER                         TYPE_LINEAR_ACCELERATION
        if (sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION) != null){
            // Success! There's an accelerometer.                       TYPE_LINEAR_ACCELERATION
            accelerometerSensor=sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        } else {
            // Failure! No accelerometer.
            new AlertDialog.Builder(this)
                    .setTitle("Not compatible")
                    .setMessage("Your device have not got accelerometer")
                    .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(0);
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

        }

        //GYROSCOPE
        if (sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE) != null){
            // Success! There's a gyroscope.
            gyroscopeSensor=sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        } else {
            // Failure! No accelerometer.
            new AlertDialog.Builder(this)
                    .setTitle("Not compatible")
                    .setMessage("Your device have not got gyroscope")
                    .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(0);
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

        }

//-------------------------------------------------------------------------------------------------------------

        //
        //------------------FIND THE CORRECT DEVICE TO CONNECT-------------------------
        //

        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        SharedPreferences preferencias =getSharedPreferences("PreferenciasDevice", Context.MODE_PRIVATE);
        mac=preferencias.getString("device", "device");

        if (pairedDevices.size() > 0) {
            // There are paired devices. Get the name and address of each paired device.
            for (BluetoothDevice device1 : pairedDevices) {
                String deviceHardwareAddress = device1.getAddress(); // MAC address
                if(deviceHardwareAddress.equals(mac)){
                    device=device1;
                }


            }
        }

//-------------------------------------------------------------------------------------------------------------

        //
        //------------------FIND THE CORRECT THREAD TO CONNECT-------------------------
        //
        if(device!=null) {
            conex = new ConnectThread(device);
            conex.run(bluetoothAdapter);

            //INITIALIZE BLUETOOTH SERVICE
            MBS = new MyBluetoothService.ConnectedThread(conex.getSocket());
            Toast.makeText(MainActivity.this,"Connected to "+device.getName(), Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this,"Bluetooth device unselected",Toast.LENGTH_SHORT).show();
        }

    }
    private class AsyncCount extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            contador=3;
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.d(TAG, "doInBackground: SEGUNDO"+start);

                if (!start) {
                    start = true;
                } else {
                    start = false;
                }

            if (!start) {
                text.setText("");
                play_pause.setImageResource(R.drawable.play);

            } else {
                text.setText("GO!");
                play_pause.setImageResource(R.drawable.pause);
            }

                Log.d(TAG, "doInBackground: TERCERO" + start);


            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            //ACCEPT A DEVICE
            Log.d(TAG, "doInBackground: PRIMERO"+start);
            if(!activo && !start) {
                 while (contador > 0) {

                    h.post(new Runnable() {
                        @Override
                        public void run() {
                            text.setText(String.valueOf(contador));
                        }
                    });

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                contador--;
                activo = true;

                }
                 activo=false;
            }


            return null;
        }
    }
    public void enviar(){
        byte[] bytes;
        String movem=getIntent().getStringExtra("movement");
        //ACCELEROMETER
        String aux="";
        aux+=movem+"%";
        for (int i=0;i<dataAccelerometer.size();i++) {
            aux+=dataAccelerometer.get(i).toString();
        }
        aux = aux.substring(0, aux.length() - 1);
        aux+="#";
        bytes = aux.getBytes();
        MBS.write(bytes);

        //MAGNETOMETER
     /*   aux="";
        for (int i=0;i<dataMagnetometer.size();i++) {
            aux+=dataMagnetometer.get(i).toString();
        }
        aux+="#";
        bytes = aux.getBytes();
        MBS.write(bytes);
*/

        //GYROSCOPE
        aux="";
        for (int i=0;i<dataGyroscope.size();i++) {
            aux+=dataGyroscope.get(i).toString();
        }
        aux = aux.substring(0, aux.length() - 1);
        bytes = aux.getBytes();
        MBS.write(bytes);
        //Toast.makeText(MainActivity.this,"enviado el primero", Toast.LENGTH_SHORT).show();
        MBS.cancel();
        rep=-1;
        dataAccelerometer=new ArrayList<>();
        dataGyroscope=new ArrayList<>();
    }

   /* public void enviar1(){
        if(!start) {
            start = true;
        }else{
            start = false;
        }
    }*/
    @Override
    protected void onDestroy() {
        sensorManager.unregisterListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION));
        sensorManager.unregisterListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE));
        super.onDestroy();
        // Don't forget to unregister the ACTION_FOUND receiver.
        //unregisterReceiver(receiver);
    }
    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, gyroscopeSensor, SensorManager.SENSOR_DELAY_GAME);
        //sensorManager.registerListener(this, magnetometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if(start) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];


            if (event.sensor.getStringType().equals(Sensor.STRING_TYPE_GYROSCOPE) ) {

                DataSensor au = new DataSensor(event.timestamp, x, y, z, rep);
                dataGyroscope.add(au);
            }                                           //STRING_TYPE_LINEAR_ACCELERATION
            if (event.sensor.getStringType().equals(Sensor.STRING_TYPE_LINEAR_ACCELERATION)) {


                DataSensor au1 = new DataSensor(event.timestamp, x, y, z, rep);
                dataAccelerometer.add(au1);
            }
           /* if (event.sensor.getStringType() == Sensor.STRING_TYPE_MAGNETIC_FIELD) {
                DataSensor au2 = new DataSensor(event.timestamp, x, y, z, 1);
                dataMagnetometer.add(au2);
            }*/
        }
    }
    /*public boolean isMaxRelative(int pos,ArrayList<DataSensor> array){

    }*/
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
