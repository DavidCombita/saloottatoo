package com.davidcombita.views.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import com.davidcombita.R
import com.davidcombita.utils.SharedPreferenceHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var sh: SharedPreferenceHelper
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sh = SharedPreferenceHelper(this)
        auth = FirebaseAuth.getInstance()
        val buttonInventary = findViewById<Button>(R.id.buttonInventary)
        val logout = findViewById<ImageButton>(R.id.imageButton_logout)

        buttonInventary.setOnClickListener {
            startActivity(Intent(this@MainActivity,InventaryActivity::class.java ))
        }

        logout.setOnClickListener {
            auth.signOut()
            sh.saveIsLoggoutIn()
            startActivity(Intent(this@MainActivity,LoginActivity::class.java ))
        }
    }
}