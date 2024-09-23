package com.example.xepartnerapp.signup_login.forgot_password

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.xepartnerapp.R
import com.example.xepartnerapp.signup_login.signup.SignupDriverActivity
import com.example.xepartnerapp.databinding.ActivityNewPasswordBinding
import com.example.xepartnerapp.signup_login.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class NewPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewPasswordBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var passengersCollection: CollectionReference

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestore = FirebaseFirestore.getInstance()
        passengersCollection = firestore.collection("Passengers")

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        val fpPhoneNumber = intent.getStringExtra("fpPhoneNumber")

        binding.newPasswordButton.setOnClickListener {
            val newPassword = binding.newPasswordChild.text.toString()
            val verifyPassword = binding.verifyPasswordChild.text.toString()

            if (newPassword.isNotEmpty() && verifyPassword.isNotEmpty() && newPassword == verifyPassword) {
                if (currentUser != null) {
                    auth.signOut()
                }
                if (fpPhoneNumber != null) {
                    changePassword(fpPhoneNumber, newPassword)
                }
            } else if (newPassword.isNotEmpty() && verifyPassword.isNotEmpty() && newPassword != verifyPassword) {
                Toast.makeText(
                    this@NewPasswordActivity, getString(R.string.PasswordNotMatch),
                    Toast.LENGTH_LONG
                ).show()
            } else {
                Toast.makeText(
                    this@NewPasswordActivity, getString(R.string.PleaseFillAllInformation),
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        binding.loginNavSignup.setOnClickListener {
            if (currentUser != null) {
                auth.signOut()
            }
            startActivity(Intent(this@NewPasswordActivity, SignupDriverActivity::class.java))
            finish()
        }
    }

    private fun changePassword(phoneNumber: String, password: String) {
        passengersCollection
            .whereEqualTo("mobile_No", phoneNumber)
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot.documents) {
                    val passengerData = mapOf("password" to password)
                    passengersCollection.document(document.id).update(passengerData)
                        .addOnSuccessListener {
                            Toast.makeText(
                                this@NewPasswordActivity,
                                getString(R.string.PasswordChangedSuccessfully),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(
                                this@NewPasswordActivity,
                                getString(R.string.SomethingWentWrong),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    startActivity(
                        Intent(
                            this@NewPasswordActivity,
                            LoginActivity::class.java
                        )
                    )
                    finish()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(
                    this@NewPasswordActivity,
                    "Error: ${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

}
