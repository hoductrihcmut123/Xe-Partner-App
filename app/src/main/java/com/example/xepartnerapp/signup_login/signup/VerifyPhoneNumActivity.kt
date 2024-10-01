package com.example.xepartnerapp.signup_login.signup

import android.content.Intent
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.xepartnerapp.R
import com.example.xepartnerapp.data.CsoData
import com.example.xepartnerapp.data.DriverData
import com.example.xepartnerapp.databinding.ActivityVerifyPhonenumBinding
import com.example.xepartnerapp.signup_login.permissions.PermissionActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import java.util.concurrent.TimeUnit

class VerifyPhoneNumActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVerifyPhonenumBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var collection: CollectionReference

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerifyPhonenumBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestore = FirebaseFirestore.getInstance()

        auth = FirebaseAuth.getInstance()
        lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

        val storedVerificationId = intent.getStringExtra("storedVerificationId")
        val isDriver = intent.getBooleanExtra("isDriver", true)

        // Driver
        val driverData = DriverData(
            firstname = intent.getStringExtra("driverFirstname"),
            lastname = intent.getStringExtra("driverLastname"),
            mobile_No = intent.getStringExtra("driverPhoneNumber"),
            password = intent.getStringExtra("driverPassword"),
            card_ID = intent.getStringExtra("driverCardID"),
            license = intent.getStringExtra("driverLicense"),
            gender = intent.getBooleanExtra("driverGender", true),
            machine_Number = intent.getStringExtra("driverMachineNumber"),
            license_Plate = intent.getStringExtra("driverLicensePlate"),
            place_Manufacture = intent.getStringExtra("driverPlaceManufacture"),
            vehicle_Color = intent.getStringExtra("driverVehicleColor"),
            vehicle_Type = intent.getStringExtra("driverVehicleType"),
            seat_Num = intent.getIntExtra("driverSeatNum", 0),
            year_Manufacture = intent.getIntExtra("driverYearManufacture", 0),
            vehicle_Brand = intent.getStringExtra("driverVehicleBrand"),
            point = 0,
            ready = false
        )

        // CSO
        val csoData = CsoData(
            firstname = intent.getStringExtra("CSOFirstname"),
            lastname = intent.getStringExtra("CSOLastname"),
            mobile_No = intent.getStringExtra("CSOPhoneNumber"),
            password = intent.getStringExtra("CSOPassword"),
            business_Organization_Name = intent.getStringExtra("CSOBusinessName"),
            email = intent.getStringExtra("CSOEmailAddress"),
            point = 0
        )

        val otpGiven = binding.idOtp
        binding.backButton.setOnClickListener {
            onBackPressed()
        }
        binding.guideContent.text = getString(
            R.string.VerificationMessage,
            if (isDriver) driverData.mobile_No else csoData.mobile_No
        )

        val resendCode = binding.resendCode
        object : CountDownTimer(59000, 1000) {
            // Callback function, fired on regular interval
            override fun onTick(millisUntilFinished: Long) {
                resendCode.setTextColor(
                    ContextCompat.getColor(
                        this@VerifyPhoneNumActivity,
                        R.color.button_background
                    )
                )
                resendCode.text = getString(R.string.ResendCodeMessage, millisUntilFinished / 1000)
                resendCode.underline()
            }

            // Callback function, fired when the time is up
            override fun onFinish() {
                resendCode.text = getString(R.string.ResendCode)
                var index = 1
                resendCode.setOnClickListener {
                    if (index > 0) {
                        index -= 1
                        Toast.makeText(
                            this@VerifyPhoneNumActivity, getString(R.string.OTPResent),
                            Toast.LENGTH_SHORT
                        ).show()

                        val number =
                            "+84${if (isDriver) driverData.mobile_No else csoData.mobile_No}"
                        val options = PhoneAuthOptions.newBuilder(auth)
                            .setPhoneNumber(number) // Phone number to verify
                            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                            .setActivity(this@VerifyPhoneNumActivity) // Activity (for callback binding)
                            .setCallbacks(callbacks)
                            .build()
                        PhoneAuthProvider.verifyPhoneNumber(options)
                    }
                    resendCode.setTextColor(
                        ContextCompat.getColor(
                            this@VerifyPhoneNumActivity,
                            R.color.sub_content
                        )
                    )
                }
            }
        }.start()

        binding.verifyButton.setOnClickListener {
            val otp = otpGiven.text.toString().trim()
            if (otp.isNotEmpty()) {
                val credential: PhoneAuthCredential = PhoneAuthProvider.getCredential(
                    storedVerificationId.toString(), otp
                )
                signInWithPhoneAuthCredential(
                    credential, driverData, csoData, isDriver
                )
            } else {
                Toast.makeText(this, getString(R.string.PleaseEnterOTP), Toast.LENGTH_SHORT).show()
            }
        }

        // Callback function for Phone Auth
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {}
            override fun onVerificationFailed(e: FirebaseException) {}
            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
            }
        }

    }

    private fun signInWithPhoneAuthCredential(
        credential: PhoneAuthCredential, driverData: DriverData, csoData: CsoData, isDriver: Boolean
    ) {
        collection = if (isDriver) {
            firestore.collection("Drivers")
        } else {
            firestore.collection("CSOs")
        }

        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val userid = collection.document().id
                    val setData: Any = if (isDriver) {
                        driverData.copy(driver_ID = userid)
                    } else {
                        csoData.copy(Cso_ID = userid)
                    }
                    collection.document(userid).set(setData)
                        .addOnSuccessListener {
                            Toast.makeText(
                                this@VerifyPhoneNumActivity,
                                getString(R.string.VerificationRegistrationSuccessful),
                                Toast.LENGTH_LONG
                            ).show()
                            val intent = Intent(
                                this@VerifyPhoneNumActivity,
                                PermissionActivity::class.java
                            )
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            intent.putExtra("isDriver", isDriver)
                            intent.putExtra("user_ID", userid)
                            startActivity(intent)
                            finish()
                        }
                        .addOnFailureListener { exception ->
                            Toast.makeText(
                                this@VerifyPhoneNumActivity,
                                "Database Error: ${exception.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    val intent = Intent(this@VerifyPhoneNumActivity, PermissionActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    intent.putExtra("isDriver", isDriver)
                    intent.putExtra("user_ID", userid)
                    startActivity(intent)
                    finish()
                } else {
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(this, getString(R.string.InvalidOTP), Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
    }

    fun TextView.underline() {
        paintFlags = paintFlags or Paint.UNDERLINE_TEXT_FLAG
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.ExitTheApp))
            .setMessage(getString(R.string.AreYouExit))
            .setNegativeButton(getString(R.string.Exit)) { _, _ ->
                super.onBackPressed() // Call the default back button action
            }
            .setPositiveButton(getString(R.string.Return), null)
            .show()
    }
}
