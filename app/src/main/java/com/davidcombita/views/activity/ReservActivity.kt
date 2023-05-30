package com.davidcombita.views.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.davidcombita.R
import com.davidcombita.data.models.TattoResponse
import com.davidcombita.utils.SharedPreferenceHelper
import com.davidcombita.viewmodels.ReservaViewModel
import com.davidcombita.views.adapters.GalleryAdapter
import com.davidcombita.views.adapters.MaterialAdapter
import com.davidcombita.views.fragments.DatePickerFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.Serializable
import javax.inject.Inject

@AndroidEntryPoint
class ReservActivity : AppCompatActivity() {

    private var tattoInfo: TattoResponse? = null
    private lateinit var adapter: MaterialAdapter
    private lateinit var txtStyle: TextView
    private lateinit var editNumbers: EditText
    private lateinit var gallery: RecyclerView
    private lateinit var datePicker: EditText
    private lateinit var materials: RecyclerView

    @Inject
    lateinit var viewModel: ReservaViewModel

    private lateinit var sh: SharedPreferenceHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reserv)

        sh = SharedPreferenceHelper(this)

        datePicker = findViewById(R.id.etPlannedDate)
        editNumbers = findViewById(R.id.edittext_numSessions)
        txtStyle = findViewById(R.id.textview_style)
        gallery = findViewById(R.id.recyclerView_images_tattos)
        materials = findViewById(R.id.recyclerView_material_tatto)
        val layour = findViewById<NestedScrollView>(R.id.scrollable)
        val progress = findViewById<ProgressBar>(R.id.progressBar)
        val back = findViewById<ImageButton>(R.id.imageButton_back)
        val reserva = findViewById<Button>(R.id.button_reservar)

        tattoInfo = intent.serializable<TattoResponse>("infoReservaTatto")

        if (tattoInfo != null) {
            gallery.layoutManager = LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL,
                false
            )
            gallery.adapter = GalleryAdapter(tattoInfo!!.resource)
            txtStyle.text = tattoInfo!!.style
            viewModel.getMaterialsTattos(tattoInfo!!.idTattoo)
            adapter = MaterialAdapter()
            materials.adapter = adapter
            materials.layoutManager = LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL,
                false
            )
        } else {
            Toast.makeText(
                this, "Error al traer los datos, intente más tarde",
                Toast.LENGTH_SHORT
            ).show()
            onBackPressed()
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.material.collect { info ->
                    if (info.error) {
                        Toast.makeText(
                            this@ReservActivity,
                            "Error al traer la información", Toast.LENGTH_LONG
                        ).show()
                    } else {
                        if (info.loading) {
                            layour.visibility = View.GONE
                            progress.visibility = View.VISIBLE
                        } else {
                            layour.visibility = View.VISIBLE
                            progress.visibility = View.GONE
                        }
                        adapter.setList(info.tattoMaterialInfo)
                    }
                }
            }
        }

        datePicker.setOnClickListener {
            showDatePickerDialog()
        }

        back.setOnClickListener {
            onBackPressed()
        }

        reserva.setOnClickListener {
            if(datePicker.text.toString().isEmpty()){
                Toast.makeText(this@ReservActivity, "Ingrese una fecha",
                    Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(editNumbers.text.toString().isEmpty()){
                Toast.makeText(this@ReservActivity, "Ingrese un numero de telefono",
                    Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.sendReserva("+57"+editNumbers.text.toString(), datePicker.text.toString(),
                tattoInfo?.style?: "blackWork", tattoInfo?.size?: "5x5", sh.getUserName(), sh.getUserEmail(),
                tattoInfo!!.idTattoo)
            Toast.makeText(this@ReservActivity, "Reservado, pronto nuestro tatuador se contactara contigo",
            Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@ReservActivity, MainActivity::class.java))
        }
    }

    private fun showDatePickerDialog() {
        val newFragment = DatePickerFragment.newInstance { _, year, month, day ->
            val selectedDate =  year.toString() + "-"+ (month + 1) +"-"+day.toString()
            datePicker.setText(selectedDate)
        }
        newFragment.show(supportFragmentManager, "datePicker")
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