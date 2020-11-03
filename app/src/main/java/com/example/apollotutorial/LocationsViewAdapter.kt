package com.example.apollotutorial

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class LocationsViewAdapter(
    private var values: List<LocationGroupQuery.Location>
) : RecyclerView.Adapter<LocationsViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values.get(position)
        val idViewString = "Address #${position}"
        holder.idView.text = idViewString
        holder.contentView.text = "latitude: ${item.latitude} \n longitude: ${item.longitude}"
    }

    override fun getItemCount(): Int = values.size

    fun update(list: List<LocationGroupQuery.Location>){
        values = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val idView: TextView = view.findViewById(R.id.item_number)
        val contentView: TextView = view.findViewById(R.id.content)

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }
}