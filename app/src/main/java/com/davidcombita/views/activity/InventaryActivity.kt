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
import com.davidcombita.viewmodels.InventaryViewModel
import com.davidcombita.viewmodels.MainViewModel
import com.davidcombita.views.adapters.InventaryAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class InventaryActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModel: InventaryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inventary)

        val progress = findViewById<ProgressBar>(R.id.progressBar)
        val back = findViewById<ImageButton>(R.id.imageButton_back)
        val recycler = findViewById<RecyclerView>(R.id.recyclerView)
        val add = findViewById<Button>(R.id.button_add_product)

        val adapter = InventaryAdapter()
        recycler.adapter = adapter

        back.setOnClickListener { onBackPressed() }
        add.setOnClickListener { startActivity(Intent(this@InventaryActivity, AddMaterialActivity::class.java)) }

        lifecycleScope.launch{
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.material.collect{ info ->
                    if(info.error){
                        Toast.makeText(this@InventaryActivity,
                            "Error al traer la información", Toast.LENGTH_LONG).show()
                    }else{
                        progress.visibility = if (info.loading) View.VISIBLE else View.GONE
                        adapter.setList(info.materialInfo)
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getInventary()
    }
}