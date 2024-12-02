package com.example.xepartnerapp.features.driver.menu.statistics

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.appcompat.app.AppCompatActivity
import com.example.xepartnerapp.common.adapter.DriverTripOverviewAdapter
import com.example.xepartnerapp.data.DriverMonthlyDailyData
import com.example.xepartnerapp.data.TripDataDto
import com.example.xepartnerapp.databinding.ActivityTripsInDayBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class TripsInDayActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTripsInDayBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var driversCollection: CollectionReference
    private var driverID: String = ""
    private var dayData: DriverMonthlyDailyData? = null

    // Trip overview
    private var tripOverviewList = mutableListOf<TripDataDto>()

    // Adapter
    private val tripOverviewAdapter: DriverTripOverviewAdapter by lazy {
        DriverTripOverviewAdapter(
            this
        )
    }

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTripsInDayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenHeight = displayMetrics.heightPixels / resources.displayMetrics.density
        settingRecyclerViewHeight(screenHeight)

        firestore = FirebaseFirestore.getInstance()
        driversCollection = firestore.collection("Drivers")
        dayData = intent.getParcelableExtra("DAY_DATA")
        driverID = dayData?.tripDataList?.get(0)?.driver_ID ?: ""

        // Set up feedback adapter
        binding.recyclerViewTripInDay.adapter = tripOverviewAdapter

        auth = FirebaseAuth.getInstance()

        binding.backButton.setOnClickListener {
            super.onBackPressed()
        }
        binding.title.text = dayData?.title

        setUpData()
    }

    private fun setUpData() {
        tripOverviewList = (dayData?.tripDataList ?: mutableListOf()).toMutableList()
        tripOverviewAdapter.updateData(tripOverviewList.sortedByDescending { it.bookingTime })
    }

    private fun settingRecyclerViewHeight(screenHeight: Float) {
        if (screenHeight < 700) {
            binding.recyclerViewTripInDay.layoutParams.height =
                (575 * resources.displayMetrics.density).toInt()
        }
    }

    override fun onStop() {
        super.onStop()
        auth.signOut()
    }
}
