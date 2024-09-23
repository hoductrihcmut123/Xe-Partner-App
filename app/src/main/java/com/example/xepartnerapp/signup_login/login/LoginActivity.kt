package com.example.xepartnerapp.signup_login.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.xepartnerapp.R
import com.example.xepartnerapp.signup_login.permissions.PermissionActivity
import com.example.xepartnerapp.signup_login.signup.SignupDriverActivity
import com.example.xepartnerapp.data.PassengerData
import com.example.xepartnerapp.databinding.ActivityLoginBinding
import com.example.xepartnerapp.signup_login.forgot_password.ForgotPasswordActivity
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var passengersCollection: CollectionReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestore = FirebaseFirestore.getInstance()
        passengersCollection = firestore.collection("Passengers")

        binding.loginForgotPassword.setOnClickListener {
            startActivity(Intent(this@LoginActivity, ForgotPasswordActivity::class.java))
        }

        binding.loginButton.setOnClickListener {
            val loginPhoneNumber = binding.loginPhoneNumber.text.toString()
            val loginPassword = binding.loginPasswordChild.text.toString()

            if (loginPhoneNumber.isNotEmpty() && loginPassword.isNotEmpty()) {
                loginPassenger(loginPhoneNumber, loginPassword)
            } else {
                Toast.makeText(
                    this@LoginActivity, getString(R.string.PleaseFillAllInformation),
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        binding.loginNavSignup.setOnClickListener {
            startActivity(Intent(this@LoginActivity, SignupDriverActivity::class.java))
            finish()
        }
    }

    private fun loginPassenger(phoneNumber: String, password: String) {
        passengersCollection.whereEqualTo("mobile_No", phoneNumber)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    for (document in querySnapshot.documents) {
                        val userData = document.toObject(PassengerData::class.java)
                        if (userData != null && userData.password == password) {
                            Toast.makeText(
                                this@LoginActivity,
                                getString(R.string.CongratulationSuccessfullyLogin),
                                Toast.LENGTH_LONG
                            ).show()
                            val intent = Intent(this@LoginActivity, PermissionActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            startActivity(intent)
                            finish()
                            return@addOnSuccessListener
                        }
                    }
                }
                Toast.makeText(
                    this@LoginActivity, getString(R.string.WrongInformation),
                    Toast.LENGTH_LONG
                ).show()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(
                    this@LoginActivity, "Database Error: ${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }

    }
}
