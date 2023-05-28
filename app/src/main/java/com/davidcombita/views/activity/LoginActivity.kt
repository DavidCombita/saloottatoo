package com.davidcombita.views.activity

import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import com.davidcombita.R
import com.davidcombita.utils.SharedPreferenceHelper
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import java.lang.Exception


class LoginActivity : AppCompatActivity() {

    private lateinit var loginFacebookButton: ImageButton
    private lateinit var google: LinearLayoutCompat
    private lateinit var signInRequest: BeginSignInRequest
    private val RC_SIGN_IN = 200
    private lateinit var auth: FirebaseAuth
    private lateinit var oneTapClient: SignInClient
    private lateinit var sh: SharedPreferenceHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth =FirebaseAuth.getInstance()
        sh = SharedPreferenceHelper(this)

        google = findViewById(R.id.linearLayoutCompat_login_google)
        loginFacebookButton = findViewById(R.id.imagebutton_logingoogle)

        oneTapClient = Identity.getSignInClient(this)
        signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(getString(R.string.default_web_client_id))
                    .setFilterByAuthorizedAccounts(false)
                    .build())
            .build()

        google.setOnClickListener { signInWithGoogle() }
        loginFacebookButton.setOnClickListener { signInWithGoogle() }
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

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.i("SEND", "Se envio el usuario y se guardo"+account.email)
                } else {
                    Log.e("ERROR_SAVE_USER", "Error al autenticar con Firebase: " + task.exception!!.message)
                }
            }
    }
}