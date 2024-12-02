package com.example.xepartnerapp.features.driver.menu.statistics

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.xepartnerapp.R
import com.example.xepartnerapp.common.utils.Constants
import com.example.xepartnerapp.common.utils.Utils.calculateMinutesDifference
import com.example.xepartnerapp.common.utils.Utils.convertMetersToKilometers
import com.example.xepartnerapp.common.utils.Utils.convertSecondsToMinutes
import com.example.xepartnerapp.common.utils.Utils.formatCurrency
import com.example.xepartnerapp.common.utils.Utils.getHourAndMinute
import com.example.xepartnerapp.data.DriverFeedback
import com.example.xepartnerapp.data.ReasonData
import com.example.xepartnerapp.data.ReportIssueData
import com.example.xepartnerapp.data.TripDataDto
import com.example.xepartnerapp.databinding.ActivityTripDetailBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Date

class TripDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTripDetailBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var driversCollection: CollectionReference
    private var driverID: String = ""
    private var tripData: TripDataDto? = null
    private var isReportButtonDisabled = false

    private lateinit var bottomSheetReportIssue: BottomSheetBehavior<View>

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTripDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenHeight = displayMetrics.heightPixels / resources.displayMetrics.density
        settingRecyclerViewHeight(screenHeight)

        firestore = FirebaseFirestore.getInstance()
        driversCollection = firestore.collection("Drivers")
        tripData = intent.getParcelableExtra("TRIP_DATA")
        driverID = tripData?.driver_ID ?: ""

        auth = FirebaseAuth.getInstance()

        // Set up bottomSheetReportIssue
        bottomSheetReportIssue = BottomSheetBehavior.from(findViewById(R.id.bottomSheetReportIssue))
        bottomSheetReportIssue.peekHeight = 0
        bottomSheetReportIssue.state = BottomSheetBehavior.STATE_COLLAPSED
        bottomSheetReportIssue.isDraggable = false

        binding.backButton.setOnClickListener {
            super.onBackPressed()
        }

        checkHasReportedIssue()

        binding.reportIssueButton.setOnClickListener {
            if (!isReportButtonDisabled) {
                bottomSheetReportIssue.state = BottomSheetBehavior.STATE_EXPANDED
                binding.backButton.isVisible = false
                binding.reportIssueButton.isVisible = false
            } else {
                Toast.makeText(
                    this@TripDetailActivity,
                    getString(R.string.ReportOnceTime),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        with(binding.bottomSheetReportIssue) {
            var reportIssueReason = ""
            radioGroupReportIssueReasons.setOnCheckedChangeListener { _, checkId ->
                reportIssueReason = when (checkId) {
                    R.id.radioReportIssueReason1 -> getString(R.string.ReportIssueReason1)
                    R.id.radioReportIssueReason2 -> getString(R.string.ReportIssueReason2)
                    R.id.radioReportIssueReason3 -> getString(R.string.ReportIssueReason3)
                    R.id.radioReportIssueReason4 -> getString(R.string.ReportIssueReason4)
                    R.id.radioReportIssueReason5 -> getString(R.string.ReportIssueReason5)
                    else -> ""
                }
            }
            sendReportIssueButton.setOnClickListener {
                if (reportIssueReason.isNotEmpty()) {
                    val reportIssueDetail = etFeedback.text.toString()

                    val reportIssueId = firestore.collection("ReportIssues").document().id
                    val reportIssueData = ReportIssueData(
                        reportIssue_ID = reportIssueId,
                        trip_ID = tripData?.trip_ID!!,
                        driver_ID = driverID,
                        reportIssue = reportIssueReason,
                        reportIssueDetail = reportIssueDetail,
                        reportTime = Date().toString()
                    )
                    firestore.collection("ReportIssues").document(reportIssueId)
                        .set(reportIssueData)
                        .addOnSuccessListener {
                            Toast.makeText(
                                this@TripDetailActivity,
                                getString(R.string.ReportIssueSuccess),
                                Toast.LENGTH_SHORT
                            ).show()
                            isReportButtonDisabled = true
                            bottomSheetReportIssue.state = BottomSheetBehavior.STATE_COLLAPSED
                            etFeedback.text.clear()
                            radioGroupReportIssueReasons.clearCheck()
                            binding.backButton.isVisible = true
                            binding.reportIssueButton.isVisible = true
                        }
                } else {
                    Toast.makeText(
                        this@TripDetailActivity,
                        getString(R.string.PleaseChooseReason),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            backButtonReportIssue.setOnClickListener {
                bottomSheetReportIssue.state = BottomSheetBehavior.STATE_COLLAPSED
                etFeedback.text.clear()
                radioGroupReportIssueReasons.clearCheck()
                binding.backButton.isVisible = true
                binding.reportIssueButton.isVisible = true
            }
        }

        setUpData()
    }

    private fun checkHasReportedIssue() {
        firestore.collection("ReportIssues").whereEqualTo("trip_ID", tripData?.trip_ID)
            .whereEqualTo("driver_ID", driverID).get().addOnSuccessListener { querySnapshot ->
                isReportButtonDisabled = !querySnapshot.isEmpty
            }
    }

    private fun setUpData() {
        binding.tvTimeStartDetail.text = getHourAndMinute(tripData?.startTime ?: "")
        binding.tvTimeEndDetail.text = getHourAndMinute(tripData?.endTime ?: "")
        binding.tvOriginAddressDetail.text = tripData?.originAddress
        binding.tvDestinationAddressDetail.text = tripData?.destinationAddress

        binding.tvStatusDetailValue.text = when (tripData?.status) {
            Constants.NEW -> getString(R.string.StatusNew)
            Constants.WAITING -> getString(R.string.StatusWaiting)
            Constants.ACCEPT -> getString(R.string.StatusAccept)
            Constants.REFUSE -> getString(R.string.StatusRefuse)
            Constants.PICK_UP_POINT -> getString(R.string.StatusPickUpPoint)
            Constants.GOING -> getString(R.string.StatusGoing)
            Constants.PASSENGER_CANCEL -> getString(R.string.StatusPassengerCancel)
            Constants.DRIVER_CANCEL -> getString(R.string.StatusDriverCancel)
            Constants.DRIVER_CANCEL_EMERGENCY -> getString(R.string.StatusDriverCancelEmergency)
            Constants.ARRIVE -> getString(R.string.StatusArrive)
            Constants.COMPLETED -> getString(R.string.StatusCompleted)
            else -> ""
        }
        binding.tvStatusDetailValue.setTextColor(
            when (tripData?.status) {
                Constants.ARRIVE -> getColor(R.color.button_background)
                Constants.COMPLETED -> getColor(R.color.button_background)
                else -> getColor(R.color.refuse_background)
            }
        )

        binding.tvBookingTimeDetail.text = tripData?.bookingTime
        binding.tvTravelDistanceDetail.text = getString(
            R.string.TravelDistance,
            tripData?.distance?.convertMetersToKilometers()?.toInt() ?: 0
        )
        binding.TvEstimateTimeDetail.text = getString(
            R.string.EstimatedTime,
            tripData?.duration?.convertSecondsToMinutes()
        )
        val exactDuration =
            calculateMinutesDifference(tripData?.startTime ?: "", tripData?.endTime ?: "")
        binding.TvExactTimeDetail.text = getString(
            R.string.ExactTimeDetail,
            exactDuration
        )

        firestore.collection("Trips").document(tripData?.trip_ID!!).collection("Reason")
            .document(tripData?.reason_ID!!).get().addOnSuccessListener { reasonDocument ->
                if (reasonDocument.exists()) {
                    val reasonData = reasonDocument.toObject(ReasonData::class.java)
                    binding.layoutFeedback.isVisible = true

                    reasonData?.feedbackDriverRef?.get()?.addOnSuccessListener { document ->
                        if (document.exists()) {
                            val driverFeedback = document.toObject(DriverFeedback::class.java)

                            if (driverFeedback?.star != 0) {
                                binding.tvStarValue.text = driverFeedback?.star.toString()
                            } else {
                                binding.tvStarValue.text = getString(R.string.EmptyStar)
                            }

                            if (driverFeedback?.feedback?.isNotEmpty() == true) {
                                binding.tvCommentValue.text = driverFeedback.feedback
                                binding.layoutComment.isVisible = true
                            } else {
                                binding.layoutComment.isVisible = false
                            }

                            if (driverFeedback?.reportDriverReason?.isNotEmpty() == true) {
                                binding.tvReportValue.text = driverFeedback.reportDriverReason
                                binding.layoutReport.isVisible = true
                            } else {
                                binding.layoutReport.isVisible = false
                            }

                            if (driverFeedback?.reportDriverReasonDetail?.isNotEmpty() == true) {
                                binding.tvReportDetailValue.text =
                                    driverFeedback.reportDriverReasonDetail
                                binding.layoutReportDetail.isVisible = true
                            } else {
                                binding.layoutReportDetail.isVisible = false
                            }
                        }
                    }

                    if (reasonData?.driverCancelReason?.isNotEmpty() == true) {
                        binding.tvDriverCancelValue.text = reasonData.driverCancelReason
                        binding.layoutDriverCancel.isVisible = true
                    } else {
                        binding.layoutDriverCancel.isVisible = false
                    }

                    if (reasonData?.passengerCancelReason?.isNotEmpty() == true) {
                        binding.tvPassengerCancelValue.text = reasonData.passengerCancelReason
                        binding.layoutPassengerCancel.isVisible = true
                    } else {
                        binding.layoutPassengerCancel.isVisible = false
                    }

                    if (reasonData?.driverCancelEmergency?.isNotEmpty() == true) {
                        binding.tvDriverCancelEmergencyValue.text = reasonData.driverCancelEmergency
                        binding.layoutDriverCancelEmergency.isVisible = true
                    } else {
                        binding.layoutDriverCancelEmergency.isVisible = false
                    }

                    if (reasonData?.driverCancelEmergencyDetail?.isNotEmpty() == true) {
                        binding.tvDriverCancelEmergencyDetailValue.text =
                            reasonData.driverCancelEmergencyDetail
                        binding.layoutDriverCancelEmergencyDetail.isVisible = true
                    } else {
                        binding.layoutDriverCancelEmergencyDetail.isVisible = false
                    }
                }
            }

        when (tripData?.paymentType) {
            Constants.CASH -> binding.tvPaymentType.text = getString(R.string.Cash)
            Constants.MOMO -> binding.tvPaymentType.text = getString(R.string.Momo)
        }
        binding.tvPrice.text = tripData?.price?.formatCurrency()
    }

    private fun settingRecyclerViewHeight(screenHeight: Float) {
        if (screenHeight < 700) {
            binding.layoutFeedback.layoutParams.height =
                (80 * resources.displayMetrics.density).toInt()
        }
    }

    override fun onStop() {
        super.onStop()
        auth.signOut()
    }
}
