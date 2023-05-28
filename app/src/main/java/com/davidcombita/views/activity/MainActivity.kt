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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.davidcombita.R
import com.davidcombita.utils.SharedPreferenceHelper
import com.davidcombita.viewmodels.InventaryViewModel
import com.davidcombita.viewmodels.MainViewModel
import com.davidcombita.views.adapters.TattoHomeAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var adapter: TattoHomeAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var sh: SharedPreferenceHelper
    private lateinit var auth: FirebaseAuth

    @Inject
    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sh = SharedPreferenceHelper(this)
        auth = FirebaseAuth.getInstance()

        val buttonInventary = findViewById<Button>(R.id.buttonInventary)
        val logout = findViewById<ImageButton>(R.id.imageButton_logout)
        val progress = findViewById<ProgressBar>(R.id.progressBar)
        recyclerView = findViewById(R.id.recyclerView_tattos)
        adapter = TattoHomeAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        lifecycleScope.launch{
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED){
                viewModel.tatto.collect{ info ->
                    if(info.error){
                        Toast.makeText(this@MainActivity,
                            "Error al traer la información", Toast.LENGTH_LONG).show()
                    }else{
                        progress.visibility = if (info.loading) View.VISIBLE else View.GONE
                        adapter.setList(info.tattoInfo)
                    }
                }
            }
        }

        buttonInventary.setOnClickListener {
            startActivity(Intent(this@MainActivity,InventaryActivity::class.java ))
        }

        logout.setOnClickListener {
            auth.signOut()
            sh.saveIsLoggoutIn()
            startActivity(Intent(this@MainActivity,LoginActivity::class.java ))
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getTattos()
    }
}