package tk.maskedredstonerproz.bluetoothexperiments.threads

import android.bluetooth.BluetoothSocket
import android.util.Log
import tk.maskedredstonerproz.bluetoothexperiments.objects.Constants.TAG

abstract class StandardThread: Thread() {

    abstract override fun run()

    abstract fun cancel()

    open fun manageMyConnectedSocket(socket: BluetoothSocket) {
        Log.d(TAG, "Socket $socket connected successfully")
    }
}