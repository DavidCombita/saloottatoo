package com.davidcombita.views.activity

import android.content.Intent
import android.content.IntentSender
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.lifecycle.lifecycleScope
import com.davidcombita.R
import com.davidcombita.utils.SharedPreferenceHelper
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.recaptcha.Recaptcha
import com.google.android.recaptcha.RecaptchaAction
import com.google.android.recaptcha.RecaptchaClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.launch
import java.io.IOException


class LoginActivity : AppCompatActivity() {

    private var progressNun1: Int = 0
    private var progressNun2: Int = 0
    private var progressNun3: Int = 0

    private var currentIndex: Int=0
    private val SITE_KEY = "6LfeekYmAAAAANVdywlRTb5Aq7WJ3ba_af5nTVsP"

    private lateinit var loginFacebookButton: ImageButton
    private lateinit var imagelogin: ImageView
    private lateinit var progress1: ProgressBar
    private lateinit var progress2: ProgressBar
    private lateinit var progress3: ProgressBar

    private lateinit var google: LinearLayoutCompat
    private lateinit var signInRequest: BeginSignInRequest
    private val RC_SIGN_IN = 200
    private lateinit var auth: FirebaseAuth
    private lateinit var oneTapClient: SignInClient
    private lateinit var sh: SharedPreferenceHelper
    private lateinit var recaptchaClient: RecaptchaClient
    private val handler = Handler()

    private val imageList = listOf(R.drawable.tatto1, R.drawable.tatoo2, R.drawable.tatto3) // Lista de IDs de recursos de las imágenes

    private val imageRunnable: Runnable = object : Runnable {
        override fun run() {
            imagelogin.setImageResource(imageList[currentIndex])

            currentIndex = (currentIndex + 1) % imageList.size

            handler.postDelayed(this, 5000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth =FirebaseAuth.getInstance()
        sh = SharedPreferenceHelper(this)

        google = findViewById(R.id.linearLayoutCompat_login_google)
        loginFacebookButton = findViewById(R.id.imagebutton_logingoogle)
        imagelogin = findViewById(R.id.imageView_login)

        progress1 = findViewById(R.id.progressBar1)
        progress2 = findViewById(R.id.progressBar2)
        progress3 = findViewById(R.id.progressBar3)

        handler.postDelayed(imageRunnable, 5000)

        oneTapClient = Identity.getSignInClient(this)
        signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(getString(R.string.default_web_client_id))
                    .setFilterByAuthorizedAccounts(false)
                    .build())
            .build()

        configCaptcha()

        google.setOnClickListener {  executeLoginAction() }
        loginFacebookButton.setOnClickListener {  executeLoginAction() }
    }

    override fun onResume() {
        super.onResume()
        if(sh.isUserLogin()){
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(imageRunnable)
    }

    private fun configCaptcha() {
        lifecycleScope.launch {
            Recaptcha.getClient(application, SITE_KEY)
                .onSuccess { client ->
                    recaptchaClient = client
                    Toast.makeText(this@LoginActivity, "OK", Toast.LENGTH_SHORT).show()
                }
                .onFailure { exception ->
                    Log.e("Error-----catpcha", exception.message.toString())
                    Toast.makeText(this@LoginActivity, "ERROR", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun executeLoginAction() {
        lifecycleScope.launch {
            recaptchaClient
                .execute(RecaptchaAction.LOGIN)
                .onSuccess { token ->
                    Log.e("Error-----catpcha", token)
                    Toast.makeText(this@LoginActivity, "CAPTCHA PROTECT OK", Toast.LENGTH_SHORT).show()
                    signInWithGoogle()
                }
                .onFailure { exception ->
                    Toast.makeText(this@LoginActivity, "ERROR CON CAPTCHA", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun signInWithGoogle() {
        oneTapClient.beginSignIn(signInRequest)
            .addOnSuccessListener(this) { result ->
                try {
                    startIntentSenderForResult(
                        result.pendingIntent.intentSender, RC_SIGN_IN,
                        null, 0, 0, 0, null)
                } catch (e: IntentSender.SendIntentException) {
                    Log.e("Error show ui", e.message.toString())
                }
            }
            .addOnFailureListener(this) { e ->
                Log.d("Error----------------", e.localizedMessage)
            }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RC_SIGN_IN -> {
                try {
                    val credential = oneTapClient.getSignInCredentialFromIntent(data)
                    val idToken = credential.googleIdToken
                    when {
                        idToken != null -> {
                            val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                            auth.signInWithCredential(firebaseCredential)
                                .addOnCompleteListener(this) { task ->
                                    if (task.isSuccessful) {
                                        saveAndGetInfo()
                                    } else {
                                        Log.w("Error upload", "signInWithCredential:failure", task.exception)
                                    }
                                }
                        }
                        else -> {
                            Log.d("Error-----", "No ID token!")
                        }
                    }
                } catch (e: ApiException) {
                    Log.d("Error-----exception", e.message.orEmpty())
                    when (e.statusCode) {
                        CommonStatusCodes.CANCELED -> {
                            Log.d("Exception---", "One-tap dialog was closed.")
                        }
                        CommonStatusCodes.NETWORK_ERROR -> {
                            Log.d("Exception---", "One-tap encountered a network error.")
                            // Try again or just ignore.
                        }
                        else -> {
                            Log.d("Exception else", "Couldn't get credential from result." +
                                    " (${e.localizedMessage})")
                        }
                    }
                }
            }
        }
    }

    private fun saveAndGetInfo() {
        try {
            Log.d("Get Info---------", "signInWithCredential:success")
            val user = auth.currentUser
            user!!.let {
                sh.saveIsLoggedIn(it.displayName!!, it.email!!, it.uid)
            }
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        } catch (e: Exception) {
            Toast.makeText(
                this@LoginActivity, "Error al inciar sesión, " +
                        "intentelo más tarde", Toast.LENGTH_SHORT
            ).show()
            Log.e("Error get info user", e.message.toString())
        }
    }

}