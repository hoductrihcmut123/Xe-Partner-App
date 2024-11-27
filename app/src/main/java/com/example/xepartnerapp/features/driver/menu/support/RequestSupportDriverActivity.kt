package com.example.xepartnerapp.features.driver.menu.support

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.xepartnerapp.R
import com.example.xepartnerapp.data.DriverSupport
import com.example.xepartnerapp.databinding.ActivityDriverRequestSupportBinding
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Date

class RequestSupportDriverActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDriverRequestSupportBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var driverSupportCollection: CollectionReference
    private var driverID: String = ""

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDriverRequestSupportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestore = FirebaseFirestore.getInstance()
        driverSupportCollection = firestore.collection("DriverSupports")
        driverID = intent.getStringExtra("user_ID").toString()

        binding.backButton.setOnClickListener {
            onBackPressed()
        }
        binding.sendFeedbackButton.setOnClickListener {
            val feedbackContent = binding.etFeedback.text.toString()
            if (feedbackContent.isNotEmpty()) {
                val id = driverSupportCollection.document().id
                val passengerSupport = DriverSupport(
                    driverSupport_ID = id,
                    driver_ID = driverID,
                    supportContent = feedbackContent,
                    createTime = Date().toString()
                )
                driverSupportCollection.document(id).set(passengerSupport)
                    .addOnSuccessListener {
                        binding.etFeedback.setText("")
                        Toast.makeText(this, getString(R.string.RequestSend), Toast.LENGTH_SHORT)
                            .show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(
                            this,
                            getString(R.string.SomethingWentWrong),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            } else {
                Toast.makeText(this, getString(R.string.PleaseWriteContent), Toast.LENGTH_SHORT).show()
            }
        }
    }
}
