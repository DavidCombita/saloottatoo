package com.davidcombita.views.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.davidcombita.R
import com.davidcombita.data.models.TattoResponse
import com.davidcombita.views.adapters.GalleryAdapter
import java.io.Serializable


class DetailTattooActivity : AppCompatActivity() {

    private lateinit var btnReservar: Button
    private lateinit var textStyle: TextView
    private lateinit var textPrice: TextView
    private lateinit var textDetail: TextView
    private lateinit var gallery: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_tatto)

        val tattoInfo = intent.serializable<TattoResponse>("infoDetailTatto")
        gallery = findViewById(R.id.recyclerView_images_tattos)
        textDetail = findViewById(R.id.textView_detail_info)
        textPrice = findViewById(R.id.textView_detailPrice)
        textStyle = findViewById(R.id.textView_detail_style)
        btnReservar = findViewById(R.id.button_reservar)
        val back = findViewById<ImageView>(R.id.imageButton_back)
        back.setOnClickListener { onBackPressed() }

        if(tattoInfo != null){
            Log.e("Se obtiene la info---", "----------")
            gallery.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            gallery.adapter = GalleryAdapter(tattoInfo.resource)
            textDetail.text = tattoInfo.descriptionTatto
            textPrice.text = "$"+tattoInfo.price
            textStyle.text = tattoInfo.style
        }else{
            Log.e("Error al obtener la info----------", "----------")
            Toast.makeText(this, "Error al ver la info, intentelo más tarde",
                Toast.LENGTH_SHORT).show()
            onBackPressed()
        }

        btnReservar.setOnClickListener {
            val intent = Intent(this@DetailTattooActivity, ReservActivity::class.java)
            intent.putExtra("infoReservaTatto", tattoInfo )
            startActivity(intent)
        }
    }

    inline fun <reified T : Serializable> Bundle.serializable(key: String) = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializable(key, T::class.java)
        else -> @Suppress("DEPRECATION") getSerializable(key) as? T
    }

    inline fun <reified T : Serializable> Intent.serializable(key: String): T? = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializableExtra(key, T::class.java)
        else -> @Suppress("DEPRECATION") getSerializableExtra(key) as? T
    }
}