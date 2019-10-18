package br.nati.projectraspberry.adapter

import android.net.wifi.ScanResult
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.nati.projectraspberry.R
import br.nati.projectraspberry.ViewHolder.WifiConnectionsViewHolder

class WifiConnectionsAdapter : RecyclerView.Adapter<WifiConnectionsViewHolder>(){

    private var scanResults: List<ScanResult> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WifiConnectionsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_wifi, parent, false)
        return WifiConnectionsViewHolder(view)
    }

    override fun getItemCount(): Int = scanResults.size

    override fun onBindViewHolder(holder: WifiConnectionsViewHolder, position: Int) {
        holder.bindView(scanResults[position])
    }

    fun updateList(list: List<ScanResult>) {
        scanResults = list
        notifyDataSetChanged()
    }

}