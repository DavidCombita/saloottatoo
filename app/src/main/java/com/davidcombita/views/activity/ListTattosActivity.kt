package com.davidcombita.views.activity

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.davidcombita.R
import com.davidcombita.viewmodels.MainViewModel
import com.davidcombita.views.adapters.TattoHomeAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ListTattosActivity : AppCompatActivity() {

    private lateinit var adapter: TattoHomeAdapter

    @Inject
    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_tattos)

        val back = findViewById<ImageButton>(R.id.imageButton_back)
        var recyclerView = findViewById<RecyclerView>(R.id.recyclerView_tattos)
        val progress = findViewById<ProgressBar>(R.id.progressBar)

        adapter = TattoHomeAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        viewModel.getTattos()

        lifecycleScope.launch{
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED){
                viewModel.tatto.collect{ info ->
                    if(info.error){
                        Toast.makeText(this@ListTattosActivity,
                            "Error al traer la información, " +
                                    "contactese con el desarrollador", Toast.LENGTH_LONG).show()
                        recyclerView.visibility = View.VISIBLE
                        progress.visibility = View.GONE
                    }else{
                        if (info.loading){
                            recyclerView.visibility = View.GONE
                            progress.visibility = View.VISIBLE
                        }else{
                            recyclerView.visibility = View.VISIBLE
                            progress.visibility = View.GONE
                        }
                        adapter.setList(info.tattoInfo)
                    }
                }
            }
        }

        back.setOnClickListener {
            onBackPressed()
        }
    }
}