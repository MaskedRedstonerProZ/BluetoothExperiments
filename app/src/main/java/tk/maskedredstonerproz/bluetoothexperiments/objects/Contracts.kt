package tk.maskedredstonerproz.bluetoothexperiments.objects

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.result.contract.ActivityResultContract
import tk.maskedredstonerproz.bluetoothexperiments.objects.Constants.TAG

object Contracts {

    class BluetoothEnableContract: ActivityResultContract<Intent, Unit>() {

        override fun createIntent(context: Context, input: Intent): Intent {
            return input
        }

        override fun parseResult(resultCode: Int, intent: Intent?) {
            if (resultCode != Activity.RESULT_OK) {
                Log.d(TAG, "Error")
            }
        }
    }
}