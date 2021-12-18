package tk.maskedredstonerproz.bluetoothexperiments.threads

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.util.Log
import tk.maskedredstonerproz.bluetoothexperiments.objects.Constants.BLUETOOTH_UUID
import tk.maskedredstonerproz.bluetoothexperiments.objects.Constants.TAG
import java.io.IOException

class ClientThread(private val device: BluetoothDevice?): StandardThread() {

    private val mmSocket: BluetoothSocket? by lazy(LazyThreadSafetyMode.NONE) {
        device?.createRfcommSocketToServiceRecord(BLUETOOTH_UUID)
    }

    override fun run() {
        Log.d(TAG, "Thread Started")

        mmSocket?.let { socket ->
            // Connect to the remote device through the socket. This call blocks
            // until it succeeds or throws an exception.
            Log.d(TAG, "Connection attempted")
            socket.connect()

            // The connection attempt succeeded. Perform work associated with
            // the connection in a separate thread.
            manageMyConnectedSocket(socket)
        }
    }

    // Closes the client socket and causes the thread to finish.
    override fun cancel() {
        try {
            mmSocket?.close()
        } catch (e: IOException) {
            Log.e(TAG, "Could not close the client socket", e)
        }
    }


}