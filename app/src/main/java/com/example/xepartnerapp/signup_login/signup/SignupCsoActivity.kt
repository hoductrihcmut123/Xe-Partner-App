package com.example.xepartnerapp.signup_login.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.xepartnerapp.HomeCsoActivity
import com.example.xepartnerapp.R
import com.example.xepartnerapp.databinding.ActivitySignupCsoBinding
import com.example.xepartnerapp.signup_login.login.LoginActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import java.util.concurrent.TimeUnit

class SignupCsoActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupCsoBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var csoCollection: CollectionReference

    private lateinit var auth: FirebaseAuth
    lateinit var storedVerificationId: String
    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks


    private lateinit var lastname: String
    private lateinit var firstname: String
    private lateinit var phoneNumber: String
    private lateinit var password: String
    private lateinit var businessName: String
    private lateinit var emailAddress: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupCsoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestore = FirebaseFirestore.getInstance()
        csoCollection = firestore.collection("CSOs")
        auth = FirebaseAuth.getInstance()

        val currentUser = auth.currentUser
        if (currentUser != null) {
            startActivity(Intent(this@SignupCsoActivity, HomeCsoActivity::class.java))
            finish()
        }

        binding.signupButton.setOnClickListener {
            lastname = binding.signupLastname.text.toString()
            firstname = binding.signupFirstname.text.toString()
            phoneNumber = binding.signupPhoneNumber.text.toString()
            password = binding.signupPasswordChild.text.toString()
            businessName = binding.signupBusinessOrganization.text.toString()
            emailAddress = binding.signupEmailAddress.text.toString()

            if (lastname.isNotEmpty() && firstname.isNotEmpty() && phoneNumber.isNotEmpty()
                && password.isNotEmpty() && businessName.isNotEmpty() && emailAddress.isNotEmpty()) {
                signupCSO(phoneNumber)
            } else {
                Toast.makeText(
                    this@SignupCsoActivity, getString(R.string.PleaseFillAllInformation),
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        binding.signupNavLogin.setOnClickListener {
            startActivity(Intent(this@SignupCsoActivity, LoginActivity::class.java))
            finish()
        }

        // Callback function for Phone Auth
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                startActivity(Intent(this@SignupCsoActivity, HomeCsoActivity::class.java))
                finish()
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(
                    this@SignupCsoActivity,
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

                val intent = Intent(this@SignupCsoActivity, VerifyPhoneNumActivity::class.java)
                intent.putExtra("storedVerificationId", storedVerificationId)
                intent.putExtra("CSOLastname", lastname)
                intent.putExtra("CSOFirstname", firstname)
                intent.putExtra("CSOPhoneNumber", phoneNumber)
                intent.putExtra("CSOPassword", password)
                intent.putExtra("CSOBusinessName", businessName)
                intent.putExtra("CSOEmailAddress", emailAddress)
                intent.putExtra("isDriver", false)
                startActivity(intent)
            }
        }
    }

    private fun signupCSO(phoneNumber: String) {
        csoCollection.whereEqualTo("mobile_No", phoneNumber)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.isEmpty) {
                    val number = "+84$phoneNumber"
                    val options = PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(number) // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this@SignupCsoActivity) // Activity (for callback binding)
                        .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
                        .build()
                    PhoneAuthProvider.verifyPhoneNumber(options)
                } else {
                    Toast.makeText(
                        this@SignupCsoActivity, getString(R.string.AccountExists),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(
                    this@SignupCsoActivity, "Database Error: ${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}
