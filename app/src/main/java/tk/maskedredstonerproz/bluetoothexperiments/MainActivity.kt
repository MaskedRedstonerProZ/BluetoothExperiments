package tk.maskedredstonerproz.bluetoothexperiments

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import tk.maskedredstonerproz.bluetoothexperiments.objects.Contracts
import tk.maskedredstonerproz.bluetoothexperiments.ui.theme.BluetoothExperimentsTheme
import tk.maskedredstonerproz.bluetoothexperiments.viewmodels.BluetoothViewModel

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bluetoothViewModel = BluetoothViewModel(this@MainActivity)
        setContent {
            BluetoothExperimentsTheme {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        val bluetoothEnableLauncher = rememberLauncherForActivityResult(Contracts.BluetoothEnableContract()) { }
                        Button(onClick = {
                            if (!bluetoothViewModel.bluetoothAdapter.isEnabled) {
                                bluetoothEnableLauncher.launch(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
                            }
                        }) {
                            Text(text = stringResource(id = R.string.enable))
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(onClick = { bluetoothViewModel.connect() }) {
                            Text(text = stringResource(id = R.string.connect))
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(onClick = {
                            bluetoothViewModel.bluetoothAdapter.bondedDevices?.forEach { device ->
                                bluetoothViewModel.setDeviceState(
                                    if (device.name.toString() == bluetoothViewModel.nameState.value) {
                                        device
                                    } else null
                                )
                            }
                        }) {
                            Text(text = stringResource(id = R.string.discover_devices))
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(onClick = { bluetoothViewModel.disconnect() }) {
                            Text(text = stringResource(id = R.string.disconnect))
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(onClick = {
                            bluetoothViewModel.bluetoothAdapter.disable()
                        }) {
                            Text(text = stringResource(id = R.string.disable))
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Checkbox(
                            checked = bluetoothViewModel.isServerState.value,
                            onCheckedChange = {
                                bluetoothViewModel.setIsServerState(it)
                            }
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        TextField(value = bluetoothViewModel.nameState.value, onValueChange = { bluetoothViewModel.setNameState(it) })
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Text(text = bluetoothViewModel.deviceState.value?.name ?: "null", color = Color(0xFFEEEEEE))
                    }
                }
            }
        }
    }
}