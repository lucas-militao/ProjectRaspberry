package br.nati.projectraspberry.activity

import android.Manifest
import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.nati.projectraspberry.R
import br.nati.projectraspberry.adapter.WifiConnectionsAdapter
import br.nati.projectraspberry.util.BluetoothConnection
import kotlinx.android.synthetic.main.activity_data.*
import java.util.*

@Suppress("DEPRECATION")
class DataActivity : AppCompatActivity() {

    lateinit var wifiConnectionsAdapter: WifiConnectionsAdapter

    companion object {
        lateinit var m_progress: ProgressDialog
        var m_myUUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
        var m_bluetoothSocket: BluetoothSocket? = null
        var m_isConnected: Boolean = false
        lateinit var m_bluetoothAdapter: BluetoothAdapter
        lateinit var m_address: String
        lateinit var bluetoothConnection: BluetoothConnection

        private const val PERMISSION_REQUEST_CODE_ACCESS_COARSE_LOCATION = 120
    }

    private val wifiManager: WifiManager
        get() = this.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

    private var wifiReceiverRegistered: Boolean = false

    private val wifiReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val results = wifiManager.scanResults
            if (results != null) {
                wifiConnectionsAdapter.updateList(results)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data)
        m_address = intent.getStringExtra(DevicesActivity.EXTRA_ADDRESS)
        setupView()

        bluetoothConnection = BluetoothConnection(this)
        bluetoothConnection.execute()

        if (!wifiManager.isWifiEnabled) {
            Toast.makeText(this, R.string.prompt_enabling_wifi, Toast.LENGTH_SHORT).show()
            wifiManager.isWifiEnabled = true
        }
    }

    private fun startScanning() {
        if (checkPermissions()) {
            registerReceiver(wifiReceiver, IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))
            wifiReceiverRegistered = true
        }
    }

    private fun checkPermissions(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                    PERMISSION_REQUEST_CODE_ACCESS_COARSE_LOCATION
            )
            return false
        } else {
            return true
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE_ACCESS_COARSE_LOCATION -> {
                startScanning()
            }
        }
    }

    private fun setupView() {
        wifiConnectionsAdapter = WifiConnectionsAdapter()
        wifi_list.adapter = wifiConnectionsAdapter

        send_button.setOnClickListener {
            bluetoothConnection.sendCommand(data_field.text.toString())
        }

        scan_button.setOnClickListener {
            startScanning()
        }
    }
}