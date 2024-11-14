package com.example.xepartnerapp.signup_login.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.xepartnerapp.HomeDriverActivity
import com.example.xepartnerapp.R
import com.example.xepartnerapp.common.utils.Constants
import com.example.xepartnerapp.databinding.ActivitySignupDriverBinding
import com.example.xepartnerapp.signup_login.login.LoginActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import java.util.concurrent.TimeUnit

class SignupDriverActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupDriverBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var driversCollection: CollectionReference

    private lateinit var auth: FirebaseAuth
    lateinit var storedVerificationId: String
    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks


    private lateinit var lastname: String
    private lateinit var firstname: String
    private lateinit var phoneNumber: String
    private lateinit var password: String
    private lateinit var cardID: String
    private lateinit var license: String
    private var gender: Boolean? = null
    private var classify: String? = null
    private lateinit var machineNumber: String
    private lateinit var licensePlate: String
    private lateinit var placeManufacture: String
    private lateinit var vehicleColor: String
    private lateinit var vehicleLine: String
    private var seatNum: Int? = null
    private var yearManufacture: Int? = null
    private lateinit var vehicleBrand: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupDriverBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestore = FirebaseFirestore.getInstance()
        driversCollection = firestore.collection("Drivers")
        auth = FirebaseAuth.getInstance()

        val currentUser = auth.currentUser
        if (currentUser != null) {
            startActivity(Intent(this@SignupDriverActivity, HomeDriverActivity::class.java))
            finish()
        }

        binding.radioGroupGender.setOnCheckedChangeListener { _, checkedId ->
            gender = when (checkedId) {
                R.id.radioMale -> true  // true cho Male
                R.id.radioFemale -> false // false cho Female
                else -> null
            }
        }
        binding.radioGroupClassify.setOnCheckedChangeListener { _, checkedId ->
            classify = when (checkedId) {
                R.id.radioBike -> Constants.BIKE
                R.id.radioCar -> Constants.CAR
                R.id.radioMvp -> Constants.MVP
                else -> null
            }
        }

        binding.signupButton.setOnClickListener {
            lastname = binding.signupLastname.text.toString()
            firstname = binding.signupFirstname.text.toString()
            phoneNumber = binding.signupPhoneNumber.text.toString()
            password = binding.signupPasswordChild.text.toString()
            cardID = binding.signupIDCard.text.toString()
            license = binding.signupLicense.text.toString()
            machineNumber = binding.signupMachineNumber.text.toString()
            licensePlate = binding.signupLicensePlate.text.toString()
            placeManufacture = binding.signupPlaceManufacture.text.toString()
            vehicleColor = binding.signupColor.text.toString()
            vehicleLine = binding.signupLine.text.toString()
            seatNum = binding.signupSeatNum.text.toString().toIntOrNull()
            yearManufacture = binding.signupYearManufacture.text.toString().toIntOrNull()
            vehicleBrand = binding.signupVehicleBrand.text.toString()

            if (lastname.isNotEmpty() && firstname.isNotEmpty() && phoneNumber.isNotEmpty()
                && password.isNotEmpty() && cardID.isNotEmpty() && license.isNotEmpty()
                && gender != null && classify != null && machineNumber.isNotEmpty() && licensePlate.isNotEmpty()
                && placeManufacture.isNotEmpty() && vehicleColor.isNotEmpty() && vehicleLine.isNotEmpty()
                && seatNum != null && yearManufacture != null && vehicleBrand.isNotEmpty()) {
                signupDriver(phoneNumber)
            } else {
                Toast.makeText(
                    this@SignupDriverActivity, getString(R.string.PleaseFillAllInformation),
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        binding.signupNavLogin.setOnClickListener {
            startActivity(Intent(this@SignupDriverActivity, LoginActivity::class.java))
            finish()
        }

        // Callback function for Phone Auth
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                startActivity(Intent(this@SignupDriverActivity, HomeDriverActivity::class.java))
                finish()
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(
                    this@SignupDriverActivity,
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

                val intent = Intent(this@SignupDriverActivity, VerifyPhoneNumActivity::class.java)
                intent.putExtra("storedVerificationId", storedVerificationId)
                intent.putExtra("driverLastname", lastname)
                intent.putExtra("driverFirstname", firstname)
                intent.putExtra("driverPhoneNumber", phoneNumber)
                intent.putExtra("driverPassword", password)
                intent.putExtra("driverCardID", cardID)
                intent.putExtra("driverLicense", license)
                intent.putExtra("driverGender", gender)
                intent.putExtra("driverClassify", classify)
                intent.putExtra("driverMachineNumber", machineNumber)
                intent.putExtra("driverLicensePlate", licensePlate)
                intent.putExtra("driverPlaceManufacture", placeManufacture)
                intent.putExtra("driverVehicleColor", vehicleColor)
                intent.putExtra("driverVehicleLine", vehicleLine)
                intent.putExtra("driverSeatNum", seatNum)
                intent.putExtra("driverYearManufacture", yearManufacture)
                intent.putExtra("driverVehicleBrand", vehicleBrand)
                intent.putExtra("isDriver", true)
                startActivity(intent)
            }
        }
    }

    private fun signupDriver(phoneNumber: String) {
        driversCollection.whereEqualTo("mobile_No", phoneNumber)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.isEmpty) {
                    val number = "+84$phoneNumber"
                    val options = PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(number) // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this@SignupDriverActivity) // Activity (for callback binding)
                        .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
                        .build()
                    PhoneAuthProvider.verifyPhoneNumber(options)
                } else {
                    Toast.makeText(
                        this@SignupDriverActivity, getString(R.string.AccountExists),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(
                    this@SignupDriverActivity, "Database Error: ${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}
