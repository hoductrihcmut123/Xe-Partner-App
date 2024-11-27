package com.example.xepartnerapp.features.driver.menu.personal_info

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.example.xepartnerapp.MainActivity
import com.example.xepartnerapp.R
import com.example.xepartnerapp.common.utils.Constants
import com.example.xepartnerapp.databinding.ActivityDriverPersonalInfoBinding
import com.example.xepartnerapp.features.driver.booking.HomeDriverActivity
import com.example.xepartnerapp.features.driver.menu.feedbacks.PassengerFeedbackDriver
import com.example.xepartnerapp.features.driver.menu.setting.SettingDriverActivity
import com.example.xepartnerapp.features.driver.menu.statistics.StatisticsDriverActivity
import com.example.xepartnerapp.features.driver.menu.support.SupportDriverActivity
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.android.material.sidesheet.SideSheetBehavior
import com.google.android.material.sidesheet.SideSheetCallback
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class PersonalInfoDriverActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDriverPersonalInfoBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var driversCollection: CollectionReference
    private var driverID: String = ""

    private lateinit var sideSheetMenu: SideSheetBehavior<View>

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDriverPersonalInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestore = FirebaseFirestore.getInstance()
        driversCollection = firestore.collection("Drivers")
        driverID = intent.getStringExtra("user_ID").toString()

        // Set up sideSheetMenu
        sideSheetMenu = SideSheetBehavior.from(findViewById(R.id.sideSheetMenu))
        sideSheetMenu.state = SideSheetBehavior.STATE_HIDDEN
        sideSheetMenu.isDraggable = true

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        binding.menuButton.setOnClickListener {
            binding.dimOverlay.isVisible = true
            binding.sideSheetMenu.sideSheetMenuItem2.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.button_background
                )
            )
            sideSheetMenu.state = SideSheetBehavior.STATE_EXPANDED
        }

        val sideSheetCallback = object : SideSheetCallback() {
            override fun onStateChanged(sideSheet: View, newState: Int) {}

            override fun onSlide(sideSheet: View, slideOffset: Float) {
                binding.dimOverlay.alpha = (slideOffset * 1.5).toFloat()
                if (slideOffset == 0f) {
                    binding.dimOverlay.isVisible = false
                }
            }
        }
        sideSheetMenu.addCallback(sideSheetCallback)

        with(binding.sideSheetMenu) {
            sideSheetMenuItem1.setOnClickListener {
                sideSheetMenu.state = SideSheetBehavior.STATE_HIDDEN
                val intent = Intent(this@PersonalInfoDriverActivity, HomeDriverActivity::class.java)
                intent.putExtra("user_ID", driverID)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }
            sideSheetMenuItem3.setOnClickListener {
                sideSheetMenu.state = SideSheetBehavior.STATE_HIDDEN
                val intent =
                    Intent(this@PersonalInfoDriverActivity, StatisticsDriverActivity::class.java)
                intent.putExtra("user_ID", driverID)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }
            sideSheetMenuItem4.setOnClickListener {
                sideSheetMenu.state = SideSheetBehavior.STATE_HIDDEN
                val intent =
                    Intent(this@PersonalInfoDriverActivity, PassengerFeedbackDriver::class.java)
                intent.putExtra("user_ID", driverID)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }
            sideSheetMenuItem5.setOnClickListener {
                Toast.makeText(
                    this@PersonalInfoDriverActivity,
                    getString(R.string.FeatureInDevelop),
                    Toast.LENGTH_LONG
                ).show()
            }
            sideSheetMenuItem6.setOnClickListener {
                sideSheetMenu.state = SideSheetBehavior.STATE_HIDDEN
                val intent =
                    Intent(this@PersonalInfoDriverActivity, SettingDriverActivity::class.java)
                intent.putExtra("user_ID", driverID)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }
            sideSheetMenuItem7.setOnClickListener {
                sideSheetMenu.state = SideSheetBehavior.STATE_HIDDEN
                val intent =
                    Intent(this@PersonalInfoDriverActivity, SupportDriverActivity::class.java)
                intent.putExtra("user_ID", driverID)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }
            sideSheetMenuItem8.setOnClickListener {
                logout(currentUser)
            }
        }

        binding.editButton.setOnClickListener {
            val intent = Intent(this, UpdatePersonalInfoDriverActivity::class.java)
            intent.putExtra("user_ID", driverID)
            startActivity(intent)
        }
    }

    override fun onResume() {
        fetchDriverInfoData()
        super.onResume()
    }

    private fun fetchDriverInfoData() {
        driversCollection.document(driverID).get().addOnSuccessListener { document ->
            if (document != null) {
                binding.tvNamePersonalInfo.text = buildString {
                    append(document.getString("lastname"))
                    append(" ")
                    append(document.getString("firstname"))
                }
                binding.tvPhoneValue.text = document.getString("mobile_No")
                binding.tvIDCardValue.text = document.getString("card_ID")
                binding.tvLicenseValue.text = document.getString("license")
                binding.tvGenderValue.text = when (document.getBoolean("gender")) {
                    true -> getString(R.string.Male)
                    false -> getString(R.string.Female)
                    else -> ""
                }
                binding.tvCompleteTripNumberValue.text =
                    document.getLong("completeTripNum").toString()
                binding.tvTotalDistanceValue.text =
                    getString(R.string.DistancePI, document.getDouble("totalDistance").toString())
                binding.tvClassifyValue.text = when (document.getString("classify")) {
                    Constants.BIKE -> getString(R.string.Bike)
                    Constants.CAR -> getString(R.string.Car)
                    Constants.MVP -> getString(R.string.MVP)
                    else -> ""
                }
                binding.tvMachineNumberValue.text = document.getString("machine_Number")
                binding.tvLicensePlateValue.text = document.getString("license_Plate")
                binding.tvPlaceOfManufactureValue.text = document.getString("place_Manufacture")
                binding.tvColorValue.text = document.getString("vehicle_Color")
                binding.tvLineValue.text = document.getString("vehicle_Line")
                binding.tvSeatNumberValue.text = document.getLong("seat_Num").toString()
                binding.tvYearOfManufactureValue.text =
                    document.getLong("year_Manufacture").toString()
                binding.tvBrandValue.text = document.getString("vehicle_Brand")

                if (document.getString("email")?.isNotEmpty() == true) {
                    binding.tvEmailValue.text = document.getString("email")
                    binding.llEmail.isVisible = true
                    binding.separateViewEmail.isVisible = true
                } else {
                    binding.llEmail.isVisible = false
                    binding.separateViewEmail.isVisible = false
                }
            }
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

    @OptIn(ExperimentalAnimationApi::class, ExperimentalPagerApi::class)
    private fun logout(currentUser: FirebaseUser?) {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.LogoutTitle))
            .setMessage(getString(R.string.AreYouSureLogout))
            .setNegativeButton(getString(R.string.Logout)) { _, _ ->
                if (currentUser != null) {
                    auth.signOut()
                }
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            .setPositiveButton(getString(R.string.Cancel), null)
            .show()
    }
}
