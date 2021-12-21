package com.talhaoz.googlesignin

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.talhaoz.googlesignin.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var signInService : GoogleSignInService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        signIn()
    }

    private fun signIn() {
        signInService = GoogleSignInService(this)
        if(signInService.checkIfUserSignedIn()!=null){
            signInSuccess()
        }

        binding.signInButton.setOnClickListener {
            signInService.initGoogleClient()
            signInService.mGoogleSignInClient?.let {
                val signInIntent: Intent = it.signInIntent
                resultLauncher.launch(signInIntent)
            }
        }
    }

    private fun signInSuccess() {
        Toast.makeText(this, "Google Sign In success", Toast.LENGTH_SHORT).show()
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                if (task.isSuccessful) {
                    try {
                        // Google Sign In was successful, authenticate with Firebase
                        val account = task.getResult(ApiException::class.java)!!
                        firebaseAuthWithGoogle(account.idToken!!)
                    } catch (e: ApiException) {
                        //Google Sign In failed, update UI appropriately
                        Toast.makeText(this, "Google Sign In failed", Toast.LENGTH_SHORT).show()

                    }
                }
            }
        }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        signInService.firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = signInService.firebaseAuth.currentUser
                    Toast.makeText(this, "successs", Toast.LENGTH_SHORT).show()
                    //send user to main
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this, "failed to sign in", Toast.LENGTH_SHORT).show()
                }
            }
    }
}