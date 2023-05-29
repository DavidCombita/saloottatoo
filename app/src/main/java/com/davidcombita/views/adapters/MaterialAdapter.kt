package com.davidcombita.views.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.davidcombita.R
import com.davidcombita.data.models.MaterialsTatto

class MaterialAdapter: RecyclerView.Adapter<MaterialAdapter.MaterialViewHolder>() {

    private lateinit var material: List<MaterialsTatto>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MaterialViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_materil_reserva, parent, false)
        return MaterialViewHolder(view)
    }

    override fun onBindViewHolder(holder: MaterialViewHolder, position: Int) {
        holder.textPrice.text = material[position].nameProduct
        holder.textDetail.text = "Materiales"
        selectImage(position, holder)
    }

    override fun getItemCount(): Int {
        return material.size
    }

    fun setList(material: List<MaterialsTatto>){
        this.material = material
        notifyDataSetChanged()
    }

    private fun selectImage(
        position: Int,
        holder: MaterialViewHolder
    ) {
        when (material[position].idCategory) {
            1L -> {
                holder.icon.setImageResource(R.drawable.icon_tint_red)
            }

            2L -> {
                holder.icon.setImageResource(R.drawable.icon_machine)
            }

            3L -> {
                holder.icon.setImageResource(R.drawable.icon_more)
            }

            4L -> {
                holder.icon.setImageResource(R.drawable.icon_config)
            }

            5L -> {
                holder.icon.setImageResource(R.drawable.icon_paper)
            }

            else -> {
                holder.icon.setImageResource(R.drawable.icon_more)
            }
        }
    }

    class MaterialViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textPrice: TextView = itemView.findViewById(R.id.textView_detailPrice)
        var textDetail: TextView = itemView.findViewById(R.id.textView_detail_style)
        var icon: ImageView = itemView.findViewById(R.id.imageView2)
    }
}