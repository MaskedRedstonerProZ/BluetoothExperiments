package tk.maskedredstonerproz.bluetoothexperiments.viewmodels

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import tk.maskedredstonerproz.bluetoothexperiments.objects.Constants
import tk.maskedredstonerproz.bluetoothexperiments.threads.ClientThread
import tk.maskedredstonerproz.bluetoothexperiments.threads.ServerThread

@SuppressLint("StaticFieldLeak")
class BluetoothViewModel(private val context: Activity): ViewModel() {

    init {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.S) {
            val permissions = arrayOf(
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.BLUETOOTH_SCAN
            )
            checkPermissions(permissions)
        }
    }

    val bluetoothAdapter = (provideBluetoothAdapter() ?: kotlin.run { Toast.makeText(context, "Bluetooth not supported!!", Toast.LENGTH_LONG).show() }) as BluetoothAdapter

    private val _nameState = mutableStateOf("")
    val nameState: State<String> = _nameState

    private val _deviceState = mutableStateOf<BluetoothDevice?>(null)
    val deviceState: State<BluetoothDevice?> = _deviceState

    private val _isServerState = mutableStateOf(false)
    val isServerState: State<Boolean> = _isServerState

    fun setIsServerState(value: Boolean) {
        _isServerState.value = value
    }

    fun setDeviceState(device: BluetoothDevice?) {
        _deviceState.value = device
    }

    fun setNameState(name: String) {
        _nameState.value = name
    }

    private val serverThread = ServerThread(bluetoothAdapter)
    private lateinit var clientThread: ClientThread

    fun connect() {
        if (isServerState.value) {
            connectAsServer()
        } else connectAsClient()
    }

    private fun connectAsClient() {
        clientThread = deviceState.value?.let { ClientThread(it) }!!
        clientThread.run()
    }

    private fun connectAsServer() {
        serverThread.run()
    }

    fun disconnect() {
        if (serverThread.isAlive) {
            serverThread.cancel()
        } else clientThread.cancel()
    }

    private fun provideBluetoothAdapter(): BluetoothAdapter? {
        val hasBluetooth = context.packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)
        val bluetoothManager: BluetoothManager? = if (hasBluetooth) {
            context.getSystemService(AppCompatActivity.BLUETOOTH_SERVICE) as BluetoothManager
        } else null
        return bluetoothManager?.adapter ?: BluetoothAdapter.getDefaultAdapter()
    }

    private fun checkPermissions(permissions: Array<String>) {
        for (i in permissions.indices) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    permissions[i]
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    context,
                    arrayOf(permissions[i]),
                    Constants.PERMISSION_CHECK_REQUEST_CODE
                )
            } else {
                Toast.makeText(context, "Permission already granted", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

}