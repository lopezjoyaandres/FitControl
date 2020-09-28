package com.example.fitcontroluser.ui.receiver;

import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.os.Handler;
import android.widget.Toast;

import com.example.fitcontroluser.Utilidades.Utilidades;

public class MyBluetoothService {
    private static final String TAG = "MY_APP_DEBUG_TAG";


    public static class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        private byte[] mmBuffer; // mmBuffer store for the stream
        private Handler handler; // handler that gets info from Bluetooth service

        public ConnectedThread(BluetoothSocket socket, Handler hand) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            handler = hand;
            // Get the input and output streams; using temp objects because
            // member streams are final.
            try {

                tmpIn = socket.getInputStream();

            } catch (IOException e) {
                Log.e(TAG, "Error occurred when creating input stream", e);
            }
            try {
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "Error occurred when creating output stream", e);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {

            mmBuffer = new byte[1024];
            int numBytes; // bytes returned from read()
            String aux = "";
            // Keep listening to the InputStream until an exception occurs.
            while (true) {
                try {
                    // Read from the InputStream.
                    //CHECK MMBUFFER
                    numBytes = mmInStream.read(mmBuffer);
                    String readMessage = new String(mmBuffer, 0, numBytes);
                    aux += readMessage;
                    //------------------------------
                } catch (IOException e) {
                    Log.d(TAG, "Input stream was disconnected", e);
                    Message readMsg = handler.obtainMessage(
                            Utilidades.MESSAGE_READ, 0, -1,
                            aux);

                    readMsg.sendToTarget();
                    break;
                }
            }
        }


        // Call this method from the main activity to shut down the connection.
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the connect socket", e);
            }
        }
    }
}
