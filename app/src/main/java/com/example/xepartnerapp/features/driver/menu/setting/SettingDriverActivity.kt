package com.example.xepartnerapp.features.driver.menu.setting

import android.annotation.SuppressLint
import android.content.Context
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
import com.example.xepartnerapp.databinding.ActivityDriverSettingBinding
import com.example.xepartnerapp.features.driver.booking.HomeDriverActivity
import com.example.xepartnerapp.features.driver.menu.feedbacks.PassengerFeedbackDriver
import com.example.xepartnerapp.features.driver.menu.personal_info.PersonalInfoDriverActivity
import com.example.xepartnerapp.features.driver.menu.statistics.StatisticsDriverActivity
import com.example.xepartnerapp.features.driver.menu.support.SupportDriverActivity
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.android.material.sidesheet.SideSheetBehavior
import com.google.android.material.sidesheet.SideSheetCallback
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class SettingDriverActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDriverSettingBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var passengersCollection: CollectionReference
    private lateinit var driversCollection: CollectionReference
    private lateinit var tripsCollection: CollectionReference
    private var passengerID: String = ""
    private var driverID: String = ""
    private var tripID: String = ""
    private var reasonID: String = ""

    private lateinit var sideSheetMenu: SideSheetBehavior<View>

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDriverSettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Shared Preferences
        val sharedPreferences = getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
        val isTurnOnVibrate = sharedPreferences.getBoolean("isTurnOnVibrate", true)
        binding.switchButton1.isChecked = isTurnOnVibrate

        firestore = FirebaseFirestore.getInstance()
        passengersCollection = firestore.collection("Passengers")
        driversCollection = firestore.collection("Drivers")
        tripsCollection = firestore.collection("Trips")
        driverID = intent.getStringExtra("user_ID").toString()

        // Set up sideSheetMenu
        sideSheetMenu = SideSheetBehavior.from(findViewById(R.id.sideSheetMenu))
        sideSheetMenu.state = SideSheetBehavior.STATE_HIDDEN
        sideSheetMenu.isDraggable = true

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        binding.menuButton.setOnClickListener {
            binding.dimOverlay.isVisible = true
            binding.sideSheetMenu.sideSheetMenuItem6.setTextColor(
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
                val intent = Intent(this@SettingDriverActivity, HomeDriverActivity::class.java)
                intent.putExtra("user_ID", driverID)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }
            sideSheetMenuItem2.setOnClickListener {
                sideSheetMenu.state = SideSheetBehavior.STATE_HIDDEN
                val intent = Intent(this@SettingDriverActivity, PersonalInfoDriverActivity::class.java)
                intent.putExtra("user_ID", driverID)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }
            sideSheetMenuItem3.setOnClickListener {
                sideSheetMenu.state = SideSheetBehavior.STATE_HIDDEN
                val intent = Intent(this@SettingDriverActivity, StatisticsDriverActivity::class.java)
                intent.putExtra("user_ID", driverID)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }
            sideSheetMenuItem4.setOnClickListener {
                sideSheetMenu.state = SideSheetBehavior.STATE_HIDDEN
                val intent = Intent(this@SettingDriverActivity, PassengerFeedbackDriver::class.java)
                intent.putExtra("user_ID", driverID)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }
            sideSheetMenuItem5.setOnClickListener {
                Toast.makeText(
                    this@SettingDriverActivity,
                    getString(R.string.FeatureInDevelop),
                    Toast.LENGTH_LONG
                ).show()
            }
            sideSheetMenuItem7.setOnClickListener {
                sideSheetMenu.state = SideSheetBehavior.STATE_HIDDEN
                val intent = Intent(this@SettingDriverActivity, SupportDriverActivity::class.java)
                intent.putExtra("user_ID", driverID)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }
            sideSheetMenuItem8.setOnClickListener {
                logout(currentUser)
            }
        }

        binding.overlayView1.setOnClickListener {
            Toast.makeText(this, getString(R.string.FeatureInDevelop), Toast.LENGTH_LONG).show()
        }

        binding.overlayView2.setOnClickListener {
            Toast.makeText(this, getString(R.string.FeatureInDevelop), Toast.LENGTH_LONG).show()
        }

        binding.overlayView3.setOnClickListener {
            Toast.makeText(this, getString(R.string.FeatureInDevelop), Toast.LENGTH_LONG).show()
        }

        binding.switchButton1.setOnCheckedChangeListener { _, isChecked ->
            val editor = sharedPreferences.edit()
            editor.putBoolean("isTurnOnVibrate", isChecked)
            editor.apply()
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
