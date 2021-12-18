package tk.maskedredstonerproz.bluetoothexperiments.threads

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.util.Log
import tk.maskedredstonerproz.bluetoothexperiments.objects.Constants.BLUETOOTH_UUID
import tk.maskedredstonerproz.bluetoothexperiments.objects.Constants.TAG
import java.io.IOException

class ClientThread(private val device: BluetoothDevice): StandardThread() {

    private val mmSocket: BluetoothSocket? by lazy(LazyThreadSafetyMode.NONE) {
        device.createRfcommSocketToServiceRecord(BLUETOOTH_UUID)
    }

    override fun run() {

        Log.d(TAG, "Thread Started")

        if (mmSocket == null) {
            cancel()
        }

        mmSocket?.let { socket ->
            // Connect to the remote device through the socket. This call blocks
            // until it succeeds or throws an exception.
            Log.d(TAG, "Connection attempted")
            try {
                socket.connect()
                manageMyConnectedSocket(socket)
            } catch (e: IOException) {
                Log.d(TAG, "Connection Failed")
            }
        }
    }

    // Closes the client socket and causes the thread to finish.
    override fun cancel() {
        try {
            mmSocket?.close()
        } catch (e: IOException) {
            Log.e(TAG, "Could not close the client socket", e)
        }
        Log.d(TAG, "Thread cancelled")
    }


}