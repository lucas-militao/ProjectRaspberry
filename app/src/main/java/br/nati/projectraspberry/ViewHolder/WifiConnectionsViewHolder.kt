package br.nati.projectraspberry.ViewHolder

import android.net.wifi.ScanResult
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_wifi.view.*

class WifiConnectionsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bindView(scanResult: ScanResult) {

        itemView.txt_ssid.text = scanResult.SSID
        itemView.txt_bssid.text = scanResult.BSSID
        itemView.txt_level.text = scanResult.level.toString()
        itemView.txt_frequency.text = scanResult.frequency.toString()

    }

}