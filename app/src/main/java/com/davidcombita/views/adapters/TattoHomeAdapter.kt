package com.davidcombita.views.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.davidcombita.R
import com.davidcombita.data.models.TattoResponse
import com.squareup.picasso.Picasso

class TattoHomeAdapter : RecyclerView.Adapter<TattoViewHolder>() {

    private lateinit var tattoList: List<TattoResponse>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TattoViewHolder {
        return TattoViewHolder(
            LayoutInflater.from(parent.context)
            .inflate(R.layout.item_target_tattoo, parent, false))
    }

    override fun onBindViewHolder(holder: TattoViewHolder, position: Int) {
        if(tattoList.isNotEmpty()){
            holder.tittle.text = tattoList[position].style
            Picasso.get().load(tattoList[position].resource[0].urlImage).error(R.drawable.tatto1)
                .into(holder.image)
        }
    }


    override fun getItemCount(): Int = 3

    @SuppressLint("NotifyDataSetChanged")
    fun setList(tattoList:List<TattoResponse>){
        this.tattoList = tattoList
        notifyDataSetChanged()
    }
}

class TattoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    var image: ImageView = itemView.findViewById(R.id.imageView_imageTatto)
    var tittle: TextView = itemView.findViewById(R.id.textView_tittletatto)
}