package com.davidcombita.views.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.davidcombita.R
import com.davidcombita.data.models.TattoResponse
import com.davidcombita.views.activity.DetailTattooActivity
import com.squareup.picasso.Picasso
import java.io.Serializable

class TattoListAdapter (val context: Context): RecyclerView.Adapter<TattoListViewHolder>() {

    private lateinit var tattoList: List<TattoResponse>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TattoListViewHolder {
        return TattoListViewHolder(
            LayoutInflater.from(parent.context)
            .inflate(R.layout.item_target_tattoo, parent, false))
    }

    override fun onBindViewHolder(holder: TattoListViewHolder, position: Int) {
        if(tattoList.isNotEmpty()){
            holder.tittle.text = tattoList[position].style
            Picasso.get().load(tattoList[position].resource[0].urlImage).error(R.drawable.tatto1)
                .into(holder.image)

            holder.itemView.setOnClickListener {
                val intent = Intent(context, DetailTattooActivity::class.java)
                intent.putExtra("infoDetailTatto", tattoList[position])
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int = this.tattoList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setList(tattoList:List<TattoResponse>){
        this.tattoList = tattoList
        notifyDataSetChanged()
    }
}

class TattoListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    var image: ImageView = itemView.findViewById(R.id.imageView_imageTatto)
    var tittle: TextView = itemView.findViewById(R.id.textView_tittletatto)
}