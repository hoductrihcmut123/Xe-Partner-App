package com.example.xepartnerapp.features.driver.menu.statistics

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.appcompat.app.AppCompatActivity
import com.example.xepartnerapp.R
import com.example.xepartnerapp.common.adapter.DriverDailyAdapter
import com.example.xepartnerapp.common.utils.Constants
import com.example.xepartnerapp.common.utils.Utils.convertMetersToKilometers
import com.example.xepartnerapp.common.utils.Utils.convertSecondsToMinutes
import com.example.xepartnerapp.common.utils.Utils.extractDay
import com.example.xepartnerapp.common.utils.Utils.formatCurrency
import com.example.xepartnerapp.data.DriverMonthlyDailyData
import com.example.xepartnerapp.data.DriverMonthlyDailyStatistics
import com.example.xepartnerapp.databinding.ActivityStatisticsDailyBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class StatisticsDailyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStatisticsDailyBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var driversCollection: CollectionReference
    private var driverID: String = ""
    private var monthData: DriverMonthlyDailyData? = null

    // Daily Statistics
    private var dailyStatisticsList = mutableListOf<DriverMonthlyDailyStatistics>()

    // Adapter
    private val driverDailyAdapter: DriverDailyAdapter by lazy { DriverDailyAdapter(this) }

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStatisticsDailyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenHeight = displayMetrics.heightPixels / resources.displayMetrics.density
        settingRecyclerViewHeight(screenHeight)

        firestore = FirebaseFirestore.getInstance()
        driversCollection = firestore.collection("Drivers")
        monthData = intent.getParcelableExtra("MONTH_DATA")
        driverID = monthData?.tripDataList?.get(0)?.driver_ID ?: ""

        // Set up feedback adapter
        binding.recyclerViewDaily.adapter = driverDailyAdapter

        auth = FirebaseAuth.getInstance()

        binding.backButton.setOnClickListener {
            super.onBackPressed()
        }
        binding.title.text = monthData?.title

        setUpData()
    }

    private fun setUpData() {
        val dayMap = mutableMapOf<String, DriverMonthlyDailyData>()

        for (tripData in monthData?.tripDataList!!) {
            val day = tripData.bookingTime?.extractDay()
            if (day != null) {
                if (dayMap.containsKey(day)) {
                    dayMap[day]?.tripDataList = dayMap[day]?.tripDataList?.plus(tripData)!!
                } else {
                    dayMap[day] =
                        DriverMonthlyDailyData(getString(R.string.DayStatistics, day), listOf(tripData))
                }
            }
        }
        val sortedDayMap = dayMap.toSortedMap(compareByDescending { it })
        for (dayData in sortedDayMap.values) {
            statisticDailyData(dayData)
        }
        driverDailyAdapter.updateData(dailyStatisticsList)
    }

    private fun statisticDailyData(dayData: DriverMonthlyDailyData) {
        val driverDailyStatistics = DriverMonthlyDailyStatistics(
            monthOrDay = dayData.title,
            totalRequest = dayData.tripDataList.size,

            tripSuccess = dayData.tripDataList.count {
                it.status == Constants.ARRIVE || it.status == Constants.COMPLETED
            },

            totalCancel = dayData.tripDataList.count {
                it.status == Constants.PASSENGER_CANCEL || it.status == Constants.DRIVER_CANCEL
                        || it.status == Constants.DRIVER_CANCEL_EMERGENCY
            },

            driverCancel = dayData.tripDataList.count {
                it.status == Constants.DRIVER_CANCEL
                        || it.status == Constants.DRIVER_CANCEL_EMERGENCY
            },

            passengerCancel = dayData.tripDataList.count { it.status == Constants.PASSENGER_CANCEL },

            totalDistance = dayData.tripDataList.filter {
                it.status == Constants.ARRIVE || it.status == Constants.COMPLETED
            }
                .sumByDouble { it.distance ?: 0.0 }
                .convertMetersToKilometers(),

            totalDuration = dayData.tripDataList.filter {
                it.status == Constants.ARRIVE || it.status == Constants.COMPLETED
            }
                .sumBy { it.duration ?: 0 }
                .convertSecondsToMinutes().toLong(),

            totalIncome = dayData.tripDataList.filter {
                it.status == Constants.ARRIVE || it.status == Constants.COMPLETED
            }
                .sumByDouble { it.price ?: 0.0 }
                .formatCurrency(),

            monthOrDayData = dayData
        )

        dailyStatisticsList.add(driverDailyStatistics)
    }

    private fun settingRecyclerViewHeight(screenHeight: Float) {
        if (screenHeight < 700) {
            binding.recyclerViewDaily.layoutParams.height =
                (575 * resources.displayMetrics.density).toInt()
        }
    }

    override fun onStop() {
        super.onStop()
        auth.signOut()
    }
}
