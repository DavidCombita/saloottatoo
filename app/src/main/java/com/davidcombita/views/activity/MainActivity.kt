package com.davidcombita.views.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.davidcombita.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonInventary = findViewById<Button>(R.id.buttonInventary)
        buttonInventary.setOnClickListener {
            startActivity(Intent(this@MainActivity,InventaryActivity::class.java ))
        }
    }
}