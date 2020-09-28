package com.FitControl.ui.chooseDevice;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.wear.widget.WearableRecyclerView;

import com.FitControl.R;
import com.FitControl.ui.Adapters.rvDeviceAdapter;
import com.FitControl.ui.chooseMovement.ChooseMovement;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ChooseDevice extends WearableActivity {
    private static final String TAG = "ChooseDevice";
    private WearableRecyclerView rvDevice;
    private rvDeviceAdapter adapter;
    protected BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private List<BluetoothDevice> listOfDevice;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_device);
        rvDevice = findViewById(R.id.idwereablerv);
        rellenar();
    }

    public void rellenar() {

        //
        //------------------FIND THE CORRECT DEVICE TO CONNECT-------------------------
        //

        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        if (!(pairedDevices.size() == 0)) {
            listOfDevice = new ArrayList(pairedDevices);
            adapter = new rvDeviceAdapter(this, listOfDevice);
            rvDevice.setAdapter(adapter);
            rvDevice.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            adapter.setClickArticleListener(new rvDeviceAdapter.OnClickListener() {

                @Override
                public void onArticleClick(int value) {

                    SharedPreferences prefs =
                            getSharedPreferences("PreferenciasDevice", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("device", listOfDevice.get(value).getAddress());
                    editor.commit();
                    Intent myIntent = new Intent(ChooseDevice.this, ChooseMovement.class);
                    startActivity(myIntent);
                }
            });
        } else {
            Toast.makeText(this, "There are no Bluetooth Devices to connect", Toast.LENGTH_LONG).show();
        }
    }
}
