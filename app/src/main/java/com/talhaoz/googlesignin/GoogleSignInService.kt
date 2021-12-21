package com.talhaoz.googlesignin

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class GoogleSignInService(private val context: Context){

    var mGoogleSignInClient : GoogleSignInClient? = null
        private set

    lateinit var firebaseAuth : FirebaseAuth

    fun initGoogleClient(){
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(context,gso)
        firebaseAuth = Firebase.auth
    }

    fun checkIfUserSignedIn() : GoogleSignInAccount? {
        return GoogleSignIn.getLastSignedInAccount(context)
    }
}