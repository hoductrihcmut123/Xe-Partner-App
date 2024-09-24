package com.example.xepartnerapp.signup_login.forgot_password

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.xepartnerapp.R
import com.example.xepartnerapp.signup_login.signup.SignupDriverActivity
import com.example.xepartnerapp.databinding.ActivityForgotPasswordBinding
import com.example.xepartnerapp.signup_login.signup.SignupCsoActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import java.util.concurrent.TimeUnit

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var collection: CollectionReference

    private lateinit var auth: FirebaseAuth
    lateinit var storedVerificationId: String
    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    private lateinit var fpPhoneNumber: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        val isDriver = intent.getBooleanExtra("isDriver", true)

        binding.backButton.setOnClickListener {
            onBackPressed()
        }

        binding.fpButton.setOnClickListener {

            fpPhoneNumber = binding.fpPhoneNumber.text.toString()

            if (fpPhoneNumber.isNotEmpty()) {
                fpPassword(fpPhoneNumber, isDriver)
            } else {
                Toast.makeText(
                    this@ForgotPasswordActivity, getString(R.string.PleaseEnterYourPhoneNumber),
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        binding.fpNavSignup.setOnClickListener {
            if (isDriver) {
                startActivity(Intent(this@ForgotPasswordActivity, SignupDriverActivity::class.java))
            } else {
                startActivity(Intent(this@ForgotPasswordActivity, SignupCsoActivity::class.java))
            }
            finish()
        }

        // Callback function for Phone Auth
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                val intent = Intent(this@ForgotPasswordActivity, NewPasswordActivity::class.java)
                intent.putExtra("isDriver", isDriver)
                startActivity(intent)
                finish()
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(
                    this@ForgotPasswordActivity,
                    getString(R.string.AnErrorOccurred),
                    Toast.LENGTH_SHORT
                )
                    .show()
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                Log.d("TAG", "onCodeSent:$verificationId")
                storedVerificationId = verificationId
                resendToken = token

                val intent =
                    Intent(this@ForgotPasswordActivity, VerifyPhoneNumFPActivity::class.java)
                intent.putExtra("storedVerificationId", storedVerificationId)
                intent.putExtra("fpPhoneNumber", fpPhoneNumber)
                intent.putExtra("isDriver", isDriver)
                startActivity(intent)
            }
        }
    }

    private fun fpPassword(phoneNumber: String, isDriver: Boolean) {
        collection = if (isDriver) {
            firestore.collection("Drivers")
        } else {
            firestore.collection("CSOs")
        }
        collection.whereEqualTo("mobile_No", phoneNumber)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val number = "+84$phoneNumber"
                    val options = PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(number) // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this@ForgotPasswordActivity) // Activity (for callback binding)
                        .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
                        .build()
                    PhoneAuthProvider.verifyPhoneNumber(options)
                } else {
                    Toast.makeText(
                        this@ForgotPasswordActivity, getString(R.string.AccountDoesNotExist),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(
                    this@ForgotPasswordActivity, "Database Error: ${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}
