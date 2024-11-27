package com.example.xepartnerapp.features.driver.menu.personal_info

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.xepartnerapp.databinding.ActivityUpdatePersonalInfoDriverBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import androidx.appcompat.app.AlertDialog
import com.example.xepartnerapp.R

class UpdatePersonalInfoDriverActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdatePersonalInfoDriverBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var driversCollection: CollectionReference
    private var driverID: String = ""

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdatePersonalInfoDriverBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestore = FirebaseFirestore.getInstance()
        driversCollection = firestore.collection("Drivers")
        driverID = intent.getStringExtra("user_ID").toString()

        auth = FirebaseAuth.getInstance()

        binding.backButton.setOnClickListener {
            super.onBackPressed()
        }

        binding.ivEditAvatar.setOnClickListener {
            Toast.makeText(this, getString(R.string.FeatureInDevelop), Toast.LENGTH_SHORT).show()
        }

        binding.uploadVehicleImageButton.setOnClickListener {
            Toast.makeText(this, getString(R.string.FeatureInDevelop), Toast.LENGTH_SHORT).show()
        }

        binding.updateButton.setOnClickListener {
            updateData()
        }

        fetchAndSetUpData()
    }

    private fun fetchAndSetUpData() {
        driversCollection.document(driverID).get().addOnSuccessListener { document ->
            if (document != null) {
                binding.emailValue.setText(document.getString("email"))
                binding.licenseValue.setText(document.getString("license"))
                binding.vehicleColorValue.setText(document.getString("vehicle_Color"))
            }
        }
    }

    private fun updateData() {
        val email = binding.emailValue.text.toString()
        val license = binding.licenseValue.text.toString()
        val vehicleColor = binding.vehicleColorValue.text.toString()

        if (license.isEmpty() || vehicleColor.isEmpty()) {
            Toast.makeText(this, getString(R.string.PleaseFillAllStarFields), Toast.LENGTH_SHORT).show()
            return
        }

        driversCollection.document(driverID).update(
            mapOf(
                "email" to email,
                "license" to license,
                "vehicle_Color" to vehicleColor
            )
        ).addOnSuccessListener {
            Toast.makeText(this, getString(R.string.UpdateSuccessfully), Toast.LENGTH_SHORT).show()
            super.onBackPressed()
        }.addOnFailureListener {
            Toast.makeText(this, getString(R.string.SomethingWentWrong), Toast.LENGTH_LONG).show()
        }
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

        override fun onStop() {
            super.onStop()
            auth.signOut()
        }
    }
