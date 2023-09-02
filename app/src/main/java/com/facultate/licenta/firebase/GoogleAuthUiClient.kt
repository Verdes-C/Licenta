package com.facultate.licenta.firebase

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.util.Log
import com.facultate.licenta.R
import com.facultate.licenta.hilt.interfaces.FirebaseRepository
import com.facultate.licenta.model.UserData
import com.facultate.licenta.redux.Actions
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException
import javax.inject.Inject

class GoogleAuthUiClient @Inject constructor(
    private val auth: FirebaseAuth,
    private val action: Actions,
    private val repository: FirebaseRepository,
    private val context: Context,
    private val oneTapClient: SignInClient,
) {
    suspend fun signIn(): IntentSender? {
        val result = try {
            oneTapClient.beginSignIn(
                buildSIgnInRequest()
            ).await()
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            null
        }
        return result?.pendingIntent?.intentSender
    }

    suspend fun signInWithIntent(intent: Intent): SignInResult {
        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)

        return try {
            val authResult = auth.signInWithCredential(googleCredentials).await()
            val user = authResult.user

            Log.d("GOOGLE", "check email ${user?.email}")

            var userData = user?.email?.let {
                repository.retrieveUserData(email = user.email!!)
            }?.also { userData ->
                Log.d("GOOGLE", "userData is not null -> ${userData}")
                action.updateUserData(userData = userData)
            } ?: UserData(email = user!!.email as String).also { userData ->
                Log.d("GOOGLE", "retrieveUserData is  null ${user.email}")
                Log.d("GOOGLE", "retrieveUserData is  null ${userData}")
                repository.updateUserData(userData = userData)
                action.updateUserData(userData = userData)
            }

            SignInResult(
                data = user?.run {
                    GoogleUserData(
                        userId = uid,
                        email = email,
                        profilePictureUrl = photoUrl?.toString()
                    )
                },
                errorMessage = null
            )
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            SignInResult(data = null, errorMessage = e.message)
        }
    }


    suspend fun signOut() {
        try {
            oneTapClient.signOut().addOnSuccessListener {
            }
            auth.signOut()
            action.signOut()
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
        }
    }

    fun getSignedInUser(): GoogleUserData? = auth.currentUser?.run {
        GoogleUserData(
            userId = uid,
            email = email,
            profilePictureUrl = photoUrl?.toString()
        )
    }

    private fun buildSIgnInRequest(): BeginSignInRequest {
        return BeginSignInRequest.Builder()
            .setGoogleIdTokenRequestOptions(
                GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(context.getString(R.string.google_web_client_id))
                    .build()
            ).setAutoSelectEnabled(true)
            .build()
    }

}