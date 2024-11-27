package com.example.xepartnerapp.features.driver.menu.feedbacks

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.example.xepartnerapp.MainActivity
import com.example.xepartnerapp.R
import com.example.xepartnerapp.common.adapter.DriverFeedbackAdapter
import com.example.xepartnerapp.data.DriverFeedback
import com.example.xepartnerapp.databinding.ActivityPassengerFeedbackDriverBinding
import com.example.xepartnerapp.features.driver.booking.HomeDriverActivity
import com.example.xepartnerapp.features.driver.menu.personal_info.PersonalInfoDriverActivity
import com.example.xepartnerapp.features.driver.menu.setting.SettingDriverActivity
import com.example.xepartnerapp.features.driver.menu.statistics.StatisticsDriverActivity
import com.example.xepartnerapp.features.driver.menu.support.SupportDriverActivity
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.android.material.sidesheet.SideSheetBehavior
import com.google.android.material.sidesheet.SideSheetCallback
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.taosif7.android.ringchartlib.RingChart
import com.taosif7.android.ringchartlib.models.RingChartData
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.DecimalFormat

class PassengerFeedbackDriver : AppCompatActivity() {

    private lateinit var binding: ActivityPassengerFeedbackDriverBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var driversCollection: CollectionReference
    private var driverID: String = ""

    private lateinit var sideSheetMenu: SideSheetBehavior<View>

    // Adapter
    private val driverFeedbackAdapter: DriverFeedbackAdapter by lazy { DriverFeedbackAdapter() }

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPassengerFeedbackDriverBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestore = FirebaseFirestore.getInstance()
        driversCollection = firestore.collection("Drivers")
        driverID = intent.getStringExtra("user_ID").toString()

        // Set up sideSheetMenu
        sideSheetMenu = SideSheetBehavior.from(findViewById(R.id.sideSheetMenu))
        sideSheetMenu.state = SideSheetBehavior.STATE_HIDDEN
        sideSheetMenu.isDraggable = true

        // Set up feedback adapter
        binding.recyclerViewFeedback.adapter = driverFeedbackAdapter

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        binding.menuButton.setOnClickListener {
            binding.dimOverlay.isVisible = true
            binding.sideSheetMenu.sideSheetMenuItem4.setTextColor(
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
                val intent = Intent(this@PassengerFeedbackDriver, HomeDriverActivity::class.java)
                intent.putExtra("user_ID", driverID)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }
            sideSheetMenuItem2.setOnClickListener {
                sideSheetMenu.state = SideSheetBehavior.STATE_HIDDEN
                val intent =
                    Intent(this@PassengerFeedbackDriver, PersonalInfoDriverActivity::class.java)
                intent.putExtra("user_ID", driverID)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }
            sideSheetMenuItem3.setOnClickListener {
                sideSheetMenu.state = SideSheetBehavior.STATE_HIDDEN
                val intent =
                    Intent(this@PassengerFeedbackDriver, StatisticsDriverActivity::class.java)
                intent.putExtra("user_ID", driverID)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }
            sideSheetMenuItem5.setOnClickListener {
                Toast.makeText(
                    this@PassengerFeedbackDriver,
                    getString(R.string.FeatureInDevelop),
                    Toast.LENGTH_LONG
                ).show()
            }
            sideSheetMenuItem6.setOnClickListener {
                sideSheetMenu.state = SideSheetBehavior.STATE_HIDDEN
                val intent = Intent(this@PassengerFeedbackDriver, SettingDriverActivity::class.java)
                intent.putExtra("user_ID", driverID)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }
            sideSheetMenuItem7.setOnClickListener {
                sideSheetMenu.state = SideSheetBehavior.STATE_HIDDEN
                val intent = Intent(this@PassengerFeedbackDriver, SupportDriverActivity::class.java)
                intent.putExtra("user_ID", driverID)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }
            sideSheetMenuItem8.setOnClickListener {
                logout(currentUser)
            }
        }

        setUpProfile()
        setUpFeedbacks()
    }

    private fun setUpProfile() {
        // Set up Ring Chart Feedback
        var starRatio = 0.0
        binding.feedbackRingChart.setLayoutMode(RingChart.renderMode.MODE_OVERLAP)
        val blueFeedback = RingChartData(
            (starRatio / 5).toFloat(),
            ContextCompat.getColor(this, R.color.button_background),
            ""
        )
        val dataListPointFeedback: ArrayList<RingChartData?> =
            object : ArrayList<RingChartData?>() {
                init {
                    add(blueFeedback)
                }
            }
        binding.feedbackRingChart.setData(dataListPointFeedback)

        // Get data from Firestore
        driversCollection.document(driverID).get().addOnSuccessListener { document ->
            if (document.exists()) {
                starRatio = calculateRateAverage(document)
                val point = document.getDouble("point") ?: 0
                binding.tvRatingAverage.text = starRatio.toString()
                dataListPointFeedback[0]?.value = (starRatio / 5).toFloat()
                binding.feedbackRingChart.updateData(dataListPointFeedback)
                binding.tvPoint.setDecimalFormat(DecimalFormat("###,###,###"))
                    .setAnimationDuration(2700).countAnimation(0, point.toInt())
            }
        }

        // Set up Ring Chart Point
        binding.pointRingChart.setLayoutMode(RingChart.renderMode.MODE_OVERLAP)
        val blue200 = RingChartData(0.2f, ContextCompat.getColor(this, R.color.blue_200), "")
        val gray = RingChartData(0.4f, ContextCompat.getColor(this, R.color.text_color), "")
        val red = RingChartData(0.6f, ContextCompat.getColor(this, R.color.refuse_background), "")
        val blue = RingChartData(0.8f, ContextCompat.getColor(this, R.color.button_background), "")
        val dataListPoint: ArrayList<RingChartData?> = object : ArrayList<RingChartData?>() {
            init {
                add(blue200)
                add(gray)
                add(red)
                add(blue)
            }
        }
        binding.pointRingChart.setData(dataListPoint)

        // Start animation
        binding.feedbackRingChart.startAnimateLoading()
        binding.pointRingChart.startAnimateLoading()

        // Stop animation after 2.5s
        lifecycleScope.launch {
            delay(2500)
            binding.feedbackRingChart.stopAnimateLoading()
            binding.pointRingChart.stopAnimateLoading()
        }
    }

    private fun setUpFeedbacks() {
        firestore.collection("DriverFeedbacks")
            .whereEqualTo("driver_ID", driverID)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    binding.tvNotHaveReview.isVisible = true
                    binding.recyclerViewFeedback.isVisible = false
                } else {
                    binding.tvNotHaveReview.isVisible = false
                    binding.recyclerViewFeedback.isVisible = true
                }
                driverFeedbackAdapter.updateData(documents.toObjects(DriverFeedback::class.java))
            }
            .addOnFailureListener { exception ->
                Log.w("Firestore", "Error getting documents: ", exception)
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

    @SuppressLint("DefaultLocale")
    private fun calculateRateAverage(document: DocumentSnapshot): Double {
        val rateStarNum = document.getDouble("rateStarNum") ?: 0.0
        val rateAverage = if (rateStarNum != 0.0) {
            document.getDouble("totalStar")?.div(rateStarNum) ?: 5.0
        } else {
            5.0
        }
        val formattedRate = String.format("%.1f", rateAverage).replace(",", ".")
        return formattedRate.toDoubleOrNull() ?: 5.0
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
