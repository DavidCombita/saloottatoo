package com.davidcombita.views.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import com.davidcombita.R
import com.davidcombita.data.models.Material
import com.davidcombita.utils.CustomAdapter
import com.davidcombita.viewmodels.InventaryViewModel
import com.davidcombita.viewmodels.MainViewModel
import com.davidcombita.views.adapters.InventaryAdapter
import com.davidcombita.views.adapters.OnItemClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class InventaryActivity : AppCompatActivity(), OnItemClickListener {

    @Inject
    lateinit var viewModel: InventaryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inventary)

        val progress = findViewById<ProgressBar>(R.id.progressBar)
        val back = findViewById<ImageButton>(R.id.imageButton_back)
        val recycler = findViewById<RecyclerView>(R.id.recyclerView)
        val add = findViewById<Button>(R.id.button_add_product)

        val adapter = InventaryAdapter(this)
        adapter.setOnItemClickListener(this)
        recycler.adapter = adapter

        back.setOnClickListener { onBackPressed() }
        add.setOnClickListener { startActivity(Intent(this@InventaryActivity, AddMaterialActivity::class.java)) }

        lifecycleScope.launch{
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED){
                viewModel.material.collect{ info ->
                    if(info.error){
                        Toast.makeText(this@InventaryActivity,
                            "Error al traer la información", Toast.LENGTH_LONG).show()
                    }else{
                        progress.visibility = if (info.loading) View.VISIBLE else View.GONE
                        Toast.makeText(this@InventaryActivity,
                            "Materias Actualizadas", Toast.LENGTH_SHORT).show()
                        adapter.setList(info.materialInfo)
                    }
                }
            }
        }

        lifecycleScope.launch{
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED){
                viewModel.updateMateria.collect{ info ->
                    if(info){
                        viewModel.getInventary()
                        viewModel.updateNewMaterial()
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getInventary()
    }

    override fun onItemClickUpdate(material: Material) {
        viewModel.updateMaterial(material)
    }
}