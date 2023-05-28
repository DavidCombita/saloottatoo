package com.davidcombita.views.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.davidcombita.R
import com.davidcombita.data.models.Material

class InventaryAdapter : RecyclerView.Adapter<InventaryViewHolder>() {

    private lateinit var inventaryList: List<Material>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InventaryViewHolder {
        return InventaryViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.item_material, parent, false))
    }

    override fun onBindViewHolder(holder: InventaryViewHolder, position: Int) {
        holder.tittle.text = inventaryList[position].nameProduct
        holder.numQuantity.text = inventaryList[position].units.toString()
        selectImage(position, holder)
    }

    private fun selectImage(
        position: Int,
        holder: InventaryViewHolder
    ) {
        when (inventaryList[position].idCategory) {
            1L -> {
                holder.image.setImageResource(R.drawable.icon_tint_red)
            }

            2L -> {
                holder.image.setImageResource(R.drawable.icon_machine)
            }

            3L -> {
                holder.image.setImageResource(R.drawable.icon_more)
            }

            4L -> {
                holder.image.setImageResource(R.drawable.icon_config)
            }

            5L -> {
                holder.image.setImageResource(R.drawable.icon_paper)
            }

            else -> {
                holder.image.setImageResource(R.drawable.icon_more)
            }
        }
    }

    override fun getItemCount(): Int = inventaryList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setList(inventaryList:List<Material>){
        this.inventaryList = inventaryList
        notifyDataSetChanged()
    }
}

class InventaryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    var tittle: TextView = itemView.findViewById(R.id.textView_nameproduct)
    var numQuantity: TextView = itemView.findViewById(R.id.textView_numQuantity)
    var image: ImageView = itemView.findViewById(R.id.imageview_icon_category)
}