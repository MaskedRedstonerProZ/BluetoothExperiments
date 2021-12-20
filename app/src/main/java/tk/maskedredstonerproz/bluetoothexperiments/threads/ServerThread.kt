package tk.maskedredstonerproz.bluetoothexperiments.threads

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.util.Log
import tk.maskedredstonerproz.bluetoothexperiments.objects.Constants.BLUETOOTH_UUID
import tk.maskedredstonerproz.bluetoothexperiments.objects.Constants.NAME
import tk.maskedredstonerproz.bluetoothexperiments.objects.Constants.TAG
import java.io.IOException

class ServerThread(private val bluetoothAdapter: BluetoothAdapter): StandardThread() {

    private val mmServerSocket: BluetoothServerSocket? by lazy(LazyThreadSafetyMode.NONE) {
        bluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(NAME, BLUETOOTH_UUID)
    }

    override fun run() {
        Log.d(TAG, "Thread Started")
        // Keep listening until exception occurs or a socket is returned.
        var shouldLoop = true
        while (shouldLoop) {
            val socket: BluetoothSocket? = try {
                mmServerSocket?.accept()
            } catch (e: IOException) {
                Log.e(TAG, "Socket's accept() method failed", e)
                shouldLoop = false
                null
            }
            socket?.also {
                manageMyConnectedSocket(it)
                mmServerSocket?.close()
                shouldLoop = false
            }
        }
    }

    // Closes the connect socket and causes the thread to finish.
    override fun cancel() {
        try {
            mmServerSocket?.close()
        } catch (e: IOException) {
            Log.e(TAG, "Could not close the connect socket", e)
        }
        Log.d(TAG, "Thread cancelled")
    }
}