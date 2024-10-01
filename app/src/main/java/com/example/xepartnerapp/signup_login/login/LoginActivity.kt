package com.example.xepartnerapp.signup_login.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.xepartnerapp.R
import com.example.xepartnerapp.signup_login.permissions.PermissionActivity
import com.example.xepartnerapp.signup_login.signup.SignupDriverActivity
import com.example.xepartnerapp.data.DriverData
import com.example.xepartnerapp.databinding.ActivityLoginBinding
import com.example.xepartnerapp.signup_login.forgot_password.ForgotPasswordActivity
import com.example.xepartnerapp.signup_login.signup.SignupCsoActivity
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var collection: CollectionReference

    private var userType: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestore = FirebaseFirestore.getInstance()

        binding.radioGroupUserType.setOnCheckedChangeListener { _, checkedId ->
            userType = when (checkedId) {
                R.id.radioDriver -> true  // true cho Driver
                R.id.radioCSO -> false // false cho CSO
                else -> null
            }
        }

        binding.loginForgotPassword.setOnClickListener {
            if (userType != null) {
                val intent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
                intent.putExtra("isDriver", userType)
                startActivity(intent)
            } else {
                Toast.makeText(
                    this@LoginActivity, getString(R.string.PleaseSelectUserType),
                    Toast.LENGTH_LONG
                ).show()
            }

        }

        binding.loginButton.setOnClickListener {
            val loginPhoneNumber = binding.loginPhoneNumber.text.toString()
            val loginPassword = binding.loginPasswordChild.text.toString()

            if (loginPhoneNumber.isNotEmpty() && loginPassword.isNotEmpty() && userType != null) {
                loginUser(loginPhoneNumber, loginPassword, userType!!)
            } else {
                Toast.makeText(
                    this@LoginActivity, getString(R.string.PleaseFillAllInformation),
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        binding.loginNavSignup.setOnClickListener {
            if (userType != null) {
                if (userType as Boolean) {
                    startActivity(Intent(this@LoginActivity, SignupDriverActivity::class.java))
                } else {
                    startActivity(Intent(this@LoginActivity, SignupCsoActivity::class.java))
                }
                finish()
            } else {
                Toast.makeText(
                    this@LoginActivity, getString(R.string.PleaseSelectUserType),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun loginUser(phoneNumber: String, password: String, isDriver: Boolean) {
        collection = if (isDriver) {
            firestore.collection("Drivers")
        } else {
            firestore.collection("CSOs")
        }
        collection.whereEqualTo("mobile_No", phoneNumber)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    for (document in querySnapshot.documents) {
                        val userData = document.toObject(DriverData::class.java)
                        if (userData != null && userData.password == password) {
                            Toast.makeText(
                                this@LoginActivity,
                                getString(R.string.CongratulationSuccessfullyLogin),
                                Toast.LENGTH_LONG
                            ).show()
                            val intent = Intent(this@LoginActivity, PermissionActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            intent.putExtra("isDriver", isDriver)
                            intent.putExtra("user_ID", userData.driver_ID)
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
