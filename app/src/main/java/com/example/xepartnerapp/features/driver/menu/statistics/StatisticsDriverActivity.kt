package com.example.xepartnerapp.features.driver.menu.statistics

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.example.xepartnerapp.MainActivity
import com.example.xepartnerapp.R
import com.example.xepartnerapp.common.adapter.DriverMonthlyAdapter
import com.example.xepartnerapp.common.utils.Constants
import com.example.xepartnerapp.common.utils.Utils.convertMetersToKilometers
import com.example.xepartnerapp.common.utils.Utils.convertSecondsToMinutes
import com.example.xepartnerapp.common.utils.Utils.extractMonthYear
import com.example.xepartnerapp.common.utils.Utils.formatCurrency
import com.example.xepartnerapp.data.DriverMonthlyDailyData
import com.example.xepartnerapp.data.DriverMonthlyDailyStatistics
import com.example.xepartnerapp.data.ReasonDataDto
import com.example.xepartnerapp.data.TripDataDto
import com.example.xepartnerapp.databinding.ActivityDriverStatisticsBinding
import com.example.xepartnerapp.features.driver.booking.HomeDriverActivity
import com.example.xepartnerapp.features.driver.menu.feedbacks.PassengerFeedbackDriver
import com.example.xepartnerapp.features.driver.menu.personal_info.PersonalInfoDriverActivity
import com.example.xepartnerapp.features.driver.menu.setting.SettingDriverActivity
import com.example.xepartnerapp.features.driver.menu.support.SupportDriverActivity
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.android.material.sidesheet.SideSheetBehavior
import com.google.android.material.sidesheet.SideSheetCallback
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class StatisticsDriverActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDriverStatisticsBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var driversCollection: CollectionReference
    private lateinit var tripsCollection: CollectionReference
    private var driverID: String = ""

    // Monthly data
    private var januaryThisYear: DriverMonthlyDailyData = DriverMonthlyDailyData()
    private var februaryThisYear: DriverMonthlyDailyData = DriverMonthlyDailyData()
    private var marchThisYear: DriverMonthlyDailyData = DriverMonthlyDailyData()
    private var aprilThisYear: DriverMonthlyDailyData = DriverMonthlyDailyData()
    private var mayThisYear: DriverMonthlyDailyData = DriverMonthlyDailyData()
    private var juneThisYear: DriverMonthlyDailyData = DriverMonthlyDailyData()
    private var julyThisYear: DriverMonthlyDailyData = DriverMonthlyDailyData()
    private var augustThisYear: DriverMonthlyDailyData = DriverMonthlyDailyData()
    private var septemberThisYear: DriverMonthlyDailyData = DriverMonthlyDailyData()
    private var octoberThisYear: DriverMonthlyDailyData = DriverMonthlyDailyData()
    private var novemberThisYear: DriverMonthlyDailyData = DriverMonthlyDailyData()
    private var decemberThisYear: DriverMonthlyDailyData = DriverMonthlyDailyData()
    private var januaryNextYear: DriverMonthlyDailyData = DriverMonthlyDailyData()

    // Monthly Statistics
    private var monthlyStatisticsList = mutableListOf<DriverMonthlyDailyStatistics>()

    // Adapter
    private val driverMonthlyAdapter: DriverMonthlyAdapter by lazy { DriverMonthlyAdapter(this) }

    private lateinit var sideSheetMenu: SideSheetBehavior<View>

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDriverStatisticsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenHeight = displayMetrics.heightPixels / resources.displayMetrics.density
        settingRecyclerViewHeight(screenHeight)

        firestore = FirebaseFirestore.getInstance()
        driversCollection = firestore.collection("Drivers")
        tripsCollection = firestore.collection("Trips")
        driverID = intent.getStringExtra("user_ID").toString()

        // Set up sideSheetMenu
        sideSheetMenu = SideSheetBehavior.from(findViewById(R.id.sideSheetMenu))
        sideSheetMenu.state = SideSheetBehavior.STATE_HIDDEN
        sideSheetMenu.isDraggable = true

        // Set up feedback adapter
        binding.recyclerViewMonthly.adapter = driverMonthlyAdapter

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        binding.menuButton.setOnClickListener {
            binding.dimOverlay.isVisible = true
            binding.sideSheetMenu.sideSheetMenuItem3.setTextColor(
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
                val intent = Intent(this@StatisticsDriverActivity, HomeDriverActivity::class.java)
                intent.putExtra("user_ID", driverID)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }
            sideSheetMenuItem2.setOnClickListener {
                sideSheetMenu.state = SideSheetBehavior.STATE_HIDDEN
                val intent =
                    Intent(this@StatisticsDriverActivity, PersonalInfoDriverActivity::class.java)
                intent.putExtra("user_ID", driverID)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }
            sideSheetMenuItem4.setOnClickListener {
                sideSheetMenu.state = SideSheetBehavior.STATE_HIDDEN
                val intent =
                    Intent(this@StatisticsDriverActivity, PassengerFeedbackDriver::class.java)
                intent.putExtra("user_ID", driverID)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }
            sideSheetMenuItem5.setOnClickListener {
                Toast.makeText(
                    this@StatisticsDriverActivity,
                    getString(R.string.FeatureInDevelop),
                    Toast.LENGTH_LONG
                ).show()
            }
            sideSheetMenuItem6.setOnClickListener {
                sideSheetMenu.state = SideSheetBehavior.STATE_HIDDEN
                val intent =
                    Intent(this@StatisticsDriverActivity, SettingDriverActivity::class.java)
                intent.putExtra("user_ID", driverID)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }
            sideSheetMenuItem7.setOnClickListener {
                sideSheetMenu.state = SideSheetBehavior.STATE_HIDDEN
                val intent =
                    Intent(this@StatisticsDriverActivity, SupportDriverActivity::class.java)
                intent.putExtra("user_ID", driverID)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }
            sideSheetMenuItem8.setOnClickListener {
                logout(currentUser)
            }
        }

        initMonthlyTitle()
        setUpData()
    }

    private fun initMonthlyTitle() {
        januaryThisYear = DriverMonthlyDailyData(title = getString(R.string.JanuaryThisYear))
        februaryThisYear = DriverMonthlyDailyData(title = getString(R.string.FebruaryThisYear))
        marchThisYear = DriverMonthlyDailyData(title = getString(R.string.MarchThisYear))
        aprilThisYear = DriverMonthlyDailyData(title = getString(R.string.AprilThisYear))
        mayThisYear = DriverMonthlyDailyData(title = getString(R.string.MayThisYear))
        juneThisYear = DriverMonthlyDailyData(title = getString(R.string.JuneThisYear))
        julyThisYear = DriverMonthlyDailyData(title = getString(R.string.JulyThisYear))
        augustThisYear = DriverMonthlyDailyData(title = getString(R.string.AugustThisYear))
        septemberThisYear = DriverMonthlyDailyData(title = getString(R.string.SeptemberThisYear))
        octoberThisYear = DriverMonthlyDailyData(title = getString(R.string.OctoberThisYear))
        novemberThisYear = DriverMonthlyDailyData(title = getString(R.string.NovemberThisYear))
        decemberThisYear = DriverMonthlyDailyData(title = getString(R.string.DecemberThisYear))
        januaryNextYear = DriverMonthlyDailyData(title = getString(R.string.JanuaryNextYear))
    }

    private fun setUpData() {
        tripsCollection.whereEqualTo("driver_ID", driverID)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    var tripData = document.toObject(TripDataDto::class.java)
                    tripsCollection.document(document.id).collection("Reason")
                        .get()
                        .addOnSuccessListener { reasonDocument ->
                            val reasonsData = ReasonDataDto(
                                reason_ID = reasonDocument.documents[0].id,
                                passengerCancelReason = reasonDocument.documents[0]
                                    .getString("passengerCancelReason") ?: "",
                                driverCancelReason = reasonDocument.documents[0]
                                    .getString("driverCancelReason") ?: "",
                                driverCancelEmergency = reasonDocument.documents[0]
                                    .getString("driverCancelEmergency") ?: "",
                                driverCancelEmergencyDetail = reasonDocument.documents[0]
                                    .getString("driverCancelEmergencyDetail") ?: "",
                                feedbackPassengerRef = reasonDocument.documents[0]
                                    .getDocumentReference("feedbackPassengerRef").toString(),
                                feedbackDriverRef = reasonDocument.documents[0]
                                    .getDocumentReference("feedbackDriverRef").toString()
                            )
                            tripData = tripData.copy(reason = reasonsData)
                        }

                    when (tripData.bookingTime?.extractMonthYear()) {
                        "Jan2024" -> januaryThisYear.tripDataList += tripData
                        "Feb2024" -> februaryThisYear.tripDataList += tripData
                        "Mar2024" -> marchThisYear.tripDataList += tripData
                        "Apr2024" -> aprilThisYear.tripDataList += tripData
                        "May2024" -> mayThisYear.tripDataList += tripData
                        "Jun2024" -> juneThisYear.tripDataList += tripData
                        "Jul2024" -> julyThisYear.tripDataList += tripData
                        "Aug2024" -> augustThisYear.tripDataList += tripData
                        "Sep2024" -> septemberThisYear.tripDataList += tripData
                        "Oct2024" -> octoberThisYear.tripDataList += tripData
                        "Nov2024" -> novemberThisYear.tripDataList += tripData
                        "Dec2024" -> decemberThisYear.tripDataList += tripData
                        "Jan2025" -> januaryNextYear.tripDataList += tripData
                    }
                }

                if (januaryThisYear.tripDataList.isNotEmpty()) {
                    statisticMonthlyData(januaryThisYear)
                }
                if (februaryThisYear.tripDataList.isNotEmpty()) {
                    statisticMonthlyData(februaryThisYear)
                }
                if (marchThisYear.tripDataList.isNotEmpty()) {
                    statisticMonthlyData(marchThisYear)
                }
                if (aprilThisYear.tripDataList.isNotEmpty()) {
                    statisticMonthlyData(aprilThisYear)
                }
                if (mayThisYear.tripDataList.isNotEmpty()) {
                    statisticMonthlyData(mayThisYear)
                }
                if (juneThisYear.tripDataList.isNotEmpty()) {
                    statisticMonthlyData(juneThisYear)
                }
                if (julyThisYear.tripDataList.isNotEmpty()) {
                    statisticMonthlyData(julyThisYear)
                }
                if (augustThisYear.tripDataList.isNotEmpty()) {
                    statisticMonthlyData(augustThisYear)
                }
                if (septemberThisYear.tripDataList.isNotEmpty()) {
                    statisticMonthlyData(septemberThisYear)
                }
                if (octoberThisYear.tripDataList.isNotEmpty()) {
                    statisticMonthlyData(octoberThisYear)
                }
                if (novemberThisYear.tripDataList.isNotEmpty()) {
                    statisticMonthlyData(novemberThisYear)
                }
                if (decemberThisYear.tripDataList.isNotEmpty()) {
                    statisticMonthlyData(decemberThisYear)
                }
                if (januaryNextYear.tripDataList.isNotEmpty()) {
                    statisticMonthlyData(januaryNextYear)
                }
                driverMonthlyAdapter.updateData(monthlyStatisticsList)
            }
            .addOnFailureListener { exception ->
                Log.w("Firestore", "Error getting documents: ", exception)
            }
    }

    private fun statisticMonthlyData(monthData: DriverMonthlyDailyData) {
        val driverMonthlyStatistics = DriverMonthlyDailyStatistics(
            monthOrDay = monthData.title,
            totalRequest = monthData.tripDataList.size,

            tripSuccess = monthData.tripDataList.count {
                it.status == Constants.ARRIVE || it.status == Constants.COMPLETED
            },

            totalCancel = monthData.tripDataList.count {
                it.status == Constants.PASSENGER_CANCEL || it.status == Constants.DRIVER_CANCEL
                        || it.status == Constants.DRIVER_CANCEL_EMERGENCY
            },

            driverCancel = monthData.tripDataList.count {
                it.status == Constants.DRIVER_CANCEL
                        || it.status == Constants.DRIVER_CANCEL_EMERGENCY
            },

            passengerCancel = monthData.tripDataList.count { it.status == Constants.PASSENGER_CANCEL },

            totalDistance = monthData.tripDataList.filter {
                it.status == Constants.ARRIVE || it.status == Constants.COMPLETED
            }
                .sumByDouble { it.distance ?: 0.0 }
                .convertMetersToKilometers(),

            totalDuration = monthData.tripDataList.filter {
                it.status == Constants.ARRIVE || it.status == Constants.COMPLETED
            }
                .sumBy { it.duration ?: 0 }
                .convertSecondsToMinutes().toLong(),

            totalIncome = monthData.tripDataList.filter {
                it.status == Constants.ARRIVE || it.status == Constants.COMPLETED
            }
                .sumByDouble { it.price ?: 0.0 }
                .formatCurrency(),

            monthOrDayData = monthData
        )

        monthlyStatisticsList.add(0, driverMonthlyStatistics)
    }

    private fun settingRecyclerViewHeight(screenHeight: Float) {
        if (screenHeight < 700) {
            binding.recyclerViewMonthly.layoutParams.height = (575 * resources.displayMetrics.density).toInt()
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
