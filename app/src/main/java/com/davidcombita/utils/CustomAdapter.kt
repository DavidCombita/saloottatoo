package com.davidcombita.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.davidcombita.data.models.Categories

class CustomAdapter(context: Context, items: List<Categories>) : ArrayAdapter<Categories>(context, 0, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var itemView = convertView
        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(android.R.layout.simple_spinner_item, parent, false)
        }
        val item = getItem(position)

        val text = itemView!!.findViewById<TextView>(android.R.id.text1)
        text.setText(item!!.nameCategory)

        return itemView
    }
}