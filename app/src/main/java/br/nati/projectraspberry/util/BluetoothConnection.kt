package br.nati.projectraspberry.util

import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import android.widget.Toast
import br.nati.projectraspberry.activity.DataActivity
import java.io.IOException

@Suppress("DEPRECATION")
class BluetoothConnection(c: Context) : AsyncTask<Void, Void, String>() {

    private var connectSuccess: Boolean = true
    private val context: Context

    init {
        this.context = c
    }

    override fun onPreExecute() {
        super.onPreExecute()
        DataActivity.m_progress = ProgressDialog.show(context, "Conectando... ", "Aguarde...")
    }

    override fun doInBackground(vararg p0: Void?): String? {
        try {
            if (DataActivity.m_bluetoothSocket == null || !DataActivity.m_isConnected) {
                DataActivity.m_bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
                val device: BluetoothDevice = DataActivity.m_bluetoothAdapter.getRemoteDevice(DataActivity.m_address)
                DataActivity.m_bluetoothSocket = device.createInsecureRfcommSocketToServiceRecord(DataActivity.m_myUUID)
                BluetoothAdapter.getDefaultAdapter().cancelDiscovery()
                DataActivity.m_bluetoothSocket!!.connect()
            }
        } catch (e: IOException) {
            connectSuccess = false
            e.printStackTrace()
        }
        return null
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        if (!connectSuccess) {
            Log.i("data", "couldn't connect")
            Toast.makeText(context, "Tentativa de conexão falhou", Toast.LENGTH_SHORT).show()
        } else {
            DataActivity.m_isConnected = true
            Toast.makeText(context, "Dispositivo conectado", Toast.LENGTH_SHORT).show()
        }
        DataActivity.m_progress.dismiss()
    }

    fun sendCommand(input: String) {
        if (DataActivity.m_bluetoothSocket != null) {
            try{
                DataActivity.m_bluetoothSocket!!.outputStream.write(input.toByteArray())
            } catch(e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun disconnect() {
        if (DataActivity.m_bluetoothSocket != null) {
            try {
                DataActivity.m_bluetoothSocket!!.close()
                DataActivity.m_bluetoothSocket = null
                DataActivity.m_isConnected = false
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

    }
}