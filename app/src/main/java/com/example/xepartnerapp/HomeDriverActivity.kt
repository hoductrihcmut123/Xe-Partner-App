package com.example.xepartnerapp

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.Point
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.xepartnerapp.common.adapter.PassengerFeedbackAdapter
import com.example.xepartnerapp.common.utils.Constants
import com.example.xepartnerapp.common.utils.Constants.DESIRED_NUM_OF_SPINS
import com.example.xepartnerapp.common.utils.Constants.DESIRED_SECOND_PER_ONE_FULL_360_SPIN
import com.example.xepartnerapp.common.utils.Constants.EFFECT_DURATION
import com.example.xepartnerapp.common.utils.Utils
import com.example.xepartnerapp.common.utils.Utils.calculateMinutesDifference
import com.example.xepartnerapp.common.utils.Utils.convertMetersToKilometers
import com.example.xepartnerapp.common.utils.Utils.convertSecondsToMinutes
import com.example.xepartnerapp.common.utils.Utils.formatCurrency
import com.example.xepartnerapp.common.utils.Utils.getHourAndMinute
import com.example.xepartnerapp.common.utils.Utils.isCheckLocationPermission
import com.example.xepartnerapp.common.utils.Utils.vibrateCustomPattern
import com.example.xepartnerapp.common.utils.showLocationPermissionDialog
import com.example.xepartnerapp.common.utils.showLocationRequestDialog
import com.example.xepartnerapp.data.PassengerFeedback
import com.example.xepartnerapp.databinding.ActivityHomeDriverBinding
import com.firebase.geofire.GeoFireUtils
import com.firebase.geofire.GeoLocation
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.Circle
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.libraries.places.api.Places
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import douglasspgyn.com.github.circularcountdown.CircularCountdown
import douglasspgyn.com.github.circularcountdown.listener.CircularListener
import org.json.JSONArray
import org.json.JSONException
import java.util.Date

class HomeDriverActivity : AppCompatActivity(), OnMapReadyCallback {
    private var mGoogleMap: GoogleMap? = null
    private var driverID: String? = null
    private var tripId: String? = null

    private lateinit var binding: ActivityHomeDriverBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private lateinit var locationCallback: LocationCallback
    private var currentLocation: Location? = null

    private var currentLatLng: LatLng? = null
    private var distance: Double = 0.0
    private var duration: Int = 0

    // Trip data
    private var tripOriginLatLng: LatLng? = null
    private var tripOriginAddress: String? = null
    private var tripDestinationLatLng: LatLng? = null
    private var tripDestinationAddress: String? = null
    private var tripPaymentType: String? = null
    private var tripPrice: Double = 0.0
    private var tripDuration: Double = 0.0
    private var tripDistance: Double = 0.0
    private var tripBookingTime: String? = null
    private var passengerID: String? = null
    private var passengerPhone: String? = null
    private var tripReasonID: String? = null
    private var tripStartTime: String? = null
    private var tripEndTime: String? = null

    private var listenerTrip: ListenerRegistration? = null

    private var isReady: Boolean? = null
    private var skipTime: Int = 5

    private lateinit var bottomSheetBookingInfo: BottomSheetBehavior<View>
    private lateinit var bottomSheetIsComing: BottomSheetBehavior<View>
    private lateinit var bottomSheetDriverOnGoing: BottomSheetBehavior<View>
    private lateinit var bottomSheetCancellation: BottomSheetBehavior<View>
    private lateinit var bottomSheetCancellationEmergency: BottomSheetBehavior<View>
    private lateinit var bottomSheetReceipt: BottomSheetBehavior<View>
    private lateinit var bottomSheetPassengerPersonalInfo: BottomSheetBehavior<View>

    // Adapter
    private val passengerFeedbackAdapter: PassengerFeedbackAdapter by lazy { PassengerFeedbackAdapter() }

    // Effect
    private var lastUserCircle: Circle? = null
    private var lastPulseAnimator: ValueAnimator? = null

    // Spinning animation
    private var animator: ValueAnimator? = null

    @SuppressLint("MissingPermission")
    @OptIn(ExperimentalAnimationApi::class, ExperimentalPagerApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeDriverBinding.inflate(layoutInflater)
        setContentView(binding.root)

        driverID = intent.getStringExtra("user_ID")
        firestore = FirebaseFirestore.getInstance()

        Places.initialize(applicationContext, BuildConfig.MAP_KEY)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment)
                as SupportMapFragment
        mapFragment.getMapAsync(this)

        mFusedLocationClient = this.let { LocationServices.getFusedLocationProviderClient(it) }

        val locationRequest = LocationRequest.create().apply {
            interval = 20000
            fastestInterval = 10000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            smallestDisplacement = 100f
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                for (location in locationResult.locations) {
                    updateLocationHandle(location)
                }
            }
        }

        mFusedLocationClient!!.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null
        )

        // Set up bottomSheetBookingInfo
        bottomSheetBookingInfo = BottomSheetBehavior.from(findViewById(R.id.bottomSheetBookingInfo))
        bottomSheetBookingInfo.peekHeight = 0
        bottomSheetBookingInfo.state = BottomSheetBehavior.STATE_COLLAPSED
        bottomSheetBookingInfo.isDraggable = false

        // Set up bottomSheetIsComing
        bottomSheetIsComing = BottomSheetBehavior.from(findViewById(R.id.bottomSheetIsComing))
        bottomSheetIsComing.peekHeight = 0
        bottomSheetIsComing.state = BottomSheetBehavior.STATE_COLLAPSED
        bottomSheetIsComing.isDraggable = false

        // Set up bottomSheetDriverOnGoing
        bottomSheetDriverOnGoing =
            BottomSheetBehavior.from(findViewById(R.id.bottomSheetDriverOnGoing))
        bottomSheetDriverOnGoing.peekHeight = 0
        bottomSheetDriverOnGoing.state = BottomSheetBehavior.STATE_COLLAPSED
        bottomSheetDriverOnGoing.isDraggable = false

        // Set up bottomSheetCancellation
        bottomSheetCancellation =
            BottomSheetBehavior.from(findViewById(R.id.bottomSheetCancellation))
        bottomSheetCancellation.peekHeight = 0
        bottomSheetCancellation.state = BottomSheetBehavior.STATE_COLLAPSED
        bottomSheetCancellation.isDraggable = false

        // Set up bottomSheetCancellationEmergency
        bottomSheetCancellationEmergency =
            BottomSheetBehavior.from(findViewById(R.id.bottomSheetCancellationEmergency))
        bottomSheetCancellationEmergency.peekHeight = 0
        bottomSheetCancellationEmergency.state = BottomSheetBehavior.STATE_COLLAPSED
        bottomSheetCancellationEmergency.isDraggable = false

        // Set up bottomSheetReceipt
        bottomSheetReceipt = BottomSheetBehavior.from(findViewById(R.id.bottomSheetReceipt))
        bottomSheetReceipt.peekHeight = 0
        bottomSheetReceipt.state = BottomSheetBehavior.STATE_COLLAPSED
        bottomSheetReceipt.isDraggable = false

        // Set up bottomSheetPassengerPersonalInfo
        bottomSheetPassengerPersonalInfo =
            BottomSheetBehavior.from(findViewById(R.id.bottomSheetPassengerPersonalInfo))
        bottomSheetPassengerPersonalInfo.peekHeight = 0
        bottomSheetPassengerPersonalInfo.state = BottomSheetBehavior.STATE_COLLAPSED
        bottomSheetPassengerPersonalInfo.isDraggable = false

        // Set up feedback adapter
        binding.bottomSheetPassengerPersonalInfo.recyclerViewReport.adapter =
            passengerFeedbackAdapter

        binding.currentLocationButton.setOnClickListener {
            val locationDeviceRequest = LocationRequest.create().apply {
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }
            val builder =
                LocationSettingsRequest.Builder().addLocationRequest(locationDeviceRequest)
            val client = LocationServices.getSettingsClient(this)
            val task = client.checkLocationSettings(builder.build())

            task.addOnSuccessListener {
                if (this@HomeDriverActivity.isCheckLocationPermission()) {
                    mGoogleMap?.clear()
                    mFusedLocationClient?.lastLocation
                        ?.addOnSuccessListener { location: Location? ->
                            if (location != null) {
                                currentLatLng = LatLng(location.latitude, location.longitude)
                                currentLocation = location
                            } else {
                                Toast.makeText(
                                    this,
                                    getString(R.string.WaitSecondToDetermineLocation),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            currentLatLng?.let { originLatLng -> zoomOnMap(originLatLng, 16f) }
                            mGoogleMap!!.isMyLocationEnabled = true
                        }
                } else {
                    showLocationPermissionDialog()
                }
            }
            task.addOnFailureListener {
                binding.switchButton.isChecked = false
                showLocationRequestDialog()
            }
        }

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        binding.backButton.setOnClickListener {
            // TODO implement if need
        }

        binding.menuButton.setOnClickListener {
            if (currentUser != null) {
                auth.signOut()
            }
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        binding.switchButton.setOnCheckedChangeListener { _, isChecked ->
            if (currentLocation != null) {
                isReady = isChecked
                val ref = driverID?.let { firestore.collection("Drivers").document(it) }
                if (isChecked) {
                    binding.title.text = getString(R.string.ReadyAccept)
                    val lat = currentLocation?.latitude
                    val lng = currentLocation?.longitude
                    val hash = GeoFireUtils.getGeoHashForLocation(GeoLocation(lat!!, lng!!))
                    ref?.update(
                        mapOf(
                            "geohash" to hash,
                            "current_Lat" to lat,
                            "current_Lng" to lng,
                            "ready" to true
                        )
                    )
                } else {
                    binding.title.text = getString(R.string.NotAccepting)
                    ref?.update(
                        mapOf(
                            "geohash" to FieldValue.delete(),
                            "current_Lat" to FieldValue.delete(),
                            "current_Lng" to FieldValue.delete(),
                            "ready" to false
                        )
                    )
                }
            } else {
                checkLocationRequest()
            }
        }

        binding.refuseButton.setOnClickListener {
            val ref = driverID?.let { firestore.collection("Drivers").document(it) }
            ref?.update(
                mapOf(
                    "trip_ID" to FieldValue.delete()
                )
            )
            val tripRef = tripId?.let { it1 -> firestore.collection("Trips").document(it1) }
            tripRef?.update(
                mapOf(
                    "status" to Constants.REFUSE
                )
            )
            resetValue()

            binding.bottomSheetBookingInfo.circularCountdown.stop()
            binding.title.isVisible = true
            binding.switchButton.isVisible = true
            binding.menuButton.isVisible = true
            bottomSheetBookingInfo.state = BottomSheetBehavior.STATE_COLLAPSED
            binding.refuseButton.isVisible = false
            tripId = null

            animator?.cancel()
            mGoogleMap?.setPadding(0, 0, 0, 0)
            mGoogleMap?.moveCamera(
                CameraUpdateFactory.newCameraPosition(
                    CameraPosition.Builder().target(mGoogleMap?.cameraPosition!!.target)
                        .tilt(0f)
                        .zoom(14f)
                        .build()
                )
            )
        }

        with(binding.bottomSheetIsComing) {
            callButton.setOnClickListener {
                val phoneIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$passengerPhone"))
                startActivity(phoneIntent)
            }
            chatButton.setOnClickListener {
                val smsIntent = Intent(Intent.ACTION_VIEW, Uri.parse("sms:$passengerPhone"))
                startActivity(smsIntent)
            }
            arrivedButton.setOnClickListener {
                val currentLocation =
                    GeoLocation(currentLatLng!!.latitude, currentLatLng!!.longitude)
                val tripOriginLocation =
                    GeoLocation(tripOriginLatLng!!.latitude, tripOriginLatLng!!.longitude)
                val distanceToOrigin =
                    GeoFireUtils.getDistanceBetween(currentLocation, tripOriginLocation)
                if (distanceToOrigin <= Constants.MAX_DISTANCE_TO_ORIGIN_FOR_PICKUP) {
                    val tripRef = tripId?.let { it1 -> firestore.collection("Trips").document(it1) }
                    tripRef?.update(
                        mapOf(
                            "status" to Constants.PICK_UP_POINT
                        )
                    )
                    passengerBoardButton.isVisible = true
                }
            }
            passengerBoardButton.setOnClickListener {
                tripStartTime = Date().toString()
                val tripRef = tripId?.let { it1 -> firestore.collection("Trips").document(it1) }
                tripRef?.update(
                    mapOf(
                        "status" to Constants.GOING,
                        "startTime" to tripStartTime
                    )
                )
                binding.bottomSheetIsComing.passengerBoardButton.isVisible = false
                bottomSheetIsComing.state = BottomSheetBehavior.STATE_COLLAPSED
                binding.title.text = getString(R.string.OnGoing)

                animator?.cancel()
                direction(tripOriginLatLng!!, tripDestinationLatLng!!)
                when (tripPaymentType) {
                    Constants.CASH -> binding.bottomSheetDriverOnGoing.tvPaymentType2.text =
                        getString(R.string.Cash)

                    Constants.MOMO -> binding.bottomSheetDriverOnGoing.tvPaymentType2.text =
                        getString(R.string.Momo)
                }
                binding.bottomSheetDriverOnGoing.tvPrice2.text = tripPrice.formatCurrency()
                binding.bottomSheetDriverOnGoing.tvEstimateTime.text = getString(
                    R.string.EstimatedTime,
                    tripDuration.toInt().convertSecondsToMinutes()
                )
                binding.bottomSheetDriverOnGoing.tvTravelDistance.text = getString(
                    R.string.TravelDistance,
                    tripDistance.convertMetersToKilometers().toInt()
                )
                bottomSheetDriverOnGoing.state = BottomSheetBehavior.STATE_EXPANDED
            }
            cancelTripButton.setOnClickListener {
                val ref = driverID?.let { firestore.collection("Drivers").document(it) }
                ref?.update(
                    mapOf(
                        "trip_ID" to FieldValue.delete()
                    )
                )
                val tripRef = tripId?.let { it1 -> firestore.collection("Trips").document(it1) }
                tripRef?.update(
                    mapOf(
                        "status" to Constants.DRIVER_CANCEL
                    )
                )
                resetValue()

                binding.title.text = getString(R.string.ReadyAccept)
                binding.switchButton.isVisible = true
                binding.menuButton.isVisible = true
                binding.bottomSheetIsComing.passengerBoardButton.isVisible = false
                bottomSheetIsComing.state = BottomSheetBehavior.STATE_COLLAPSED
                bottomSheetCancellation.state = BottomSheetBehavior.STATE_EXPANDED

                animator?.cancel()
                mGoogleMap?.setPadding(0, 0, 0, 0)
                mGoogleMap?.moveCamera(
                    CameraUpdateFactory.newCameraPosition(
                        CameraPosition.Builder().target(mGoogleMap?.cameraPosition!!.target)
                            .tilt(0f)
                            .zoom(14f)
                            .build()
                    )
                )
            }
            passengerInfoLayout.setOnClickListener {
                with(binding.bottomSheetPassengerPersonalInfo) {
                    firestore.collection("Passengers").document(passengerID!!).get()
                        .addOnSuccessListener { document ->
                            tvPassengerNamePersonalInfo.text = buildString {
                                append(document.getString("lastname"))
                                append(" ")
                                append(document.getString("firstname"))
                            }
                            tvBookingTripNumValue.text =
                                document.getLong("bookingTripNum").toString()
                            tvReportedNumValue.text =
                                document.getLong("reportPassengerNum").toString()
                            when (document.getBoolean("gender")) {
                                true -> tvGenderValue.text = getString(R.string.Male)
                                false -> tvGenderValue.text = getString(R.string.Female)
                                else -> tvGenderValue.text = getString(R.string.NoInformation)
                            }
                        }
                }
                firestore.collection("PassengerFeedbacks")
                    .whereEqualTo("passenger_ID", passengerID)
                    .get()
                    .addOnSuccessListener { documents ->
                        if (documents.isEmpty) {
                            binding.bottomSheetPassengerPersonalInfo.tvNotHaveReview.isVisible = true
                            binding.bottomSheetPassengerPersonalInfo.recyclerViewReport.isVisible = false
                        } else {
                            binding.bottomSheetPassengerPersonalInfo.tvNotHaveReview.isVisible = false
                            binding.bottomSheetPassengerPersonalInfo.recyclerViewReport.isVisible = true
                        }
                        passengerFeedbackAdapter.updateData(documents.toObjects(PassengerFeedback::class.java))
                        bottomSheetIsComing.state = BottomSheetBehavior.STATE_COLLAPSED
                        bottomSheetPassengerPersonalInfo.state = BottomSheetBehavior.STATE_EXPANDED
                    }
                    .addOnFailureListener { exception ->
                        Log.w("Firestore", "Error getting documents: ", exception)
                    }
            }
        }

        with(binding.bottomSheetPassengerPersonalInfo) {
            backButtonPersonalInfo.setOnClickListener {
                bottomSheetPassengerPersonalInfo.state = BottomSheetBehavior.STATE_COLLAPSED
                bottomSheetIsComing.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }

        with(binding.bottomSheetCancellation) {
            var cancelReason = ""
            radioGroupReasons.setOnCheckedChangeListener { _, checkId ->
                cancelReason = when (checkId) {
                    R.id.radioReason1 -> getString(R.string.CancellationReason1)
                    R.id.radioReason2 -> getString(R.string.CancellationReason2)
                    R.id.radioReason3 -> getString(R.string.CancellationReason3)
                    R.id.radioReason4 -> getString(R.string.CancellationReason4)
                    R.id.radioReason5 -> getString(R.string.CancellationReason5)
                    else -> ""
                }
            }
            sendCancellationButton.setOnClickListener {
                if (cancelReason.isNotEmpty()) {
                    bottomSheetCancellation.state = BottomSheetBehavior.STATE_COLLAPSED
                    firestore.collection("Trips").document(tripId!!).collection("Reason")
                        .document(tripReasonID!!)
                        .update(
                            mapOf(
                                "driverCancelReason" to cancelReason
                            )
                        )
                    radioGroupReasons.clearCheck()
                    tripId = null
                    tripReasonID = null
                } else {
                    Toast.makeText(
                        this@HomeDriverActivity,
                        getString(R.string.PleaseChooseReason),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        with(binding.bottomSheetDriverOnGoing) {
            arriveDestinationButton.setOnClickListener {
                binding.title.isVisible = false
                mGoogleMap?.clear()
                bottomSheetDriverOnGoing.state = BottomSheetBehavior.STATE_COLLAPSED

                tripEndTime = Date().toString()
                val exactDuration = calculateMinutesDifference(tripStartTime!!, tripEndTime!!)
                val tripRef = tripId?.let { it1 -> firestore.collection("Trips").document(it1) }
                tripRef?.update(
                    mapOf(
                        "status" to Constants.ARRIVE,
                        "endTime" to tripEndTime,
                        "exactDuration" to exactDuration
                    )
                )

                binding.bottomSheetReceipt.tvTimeBookingReceipt.text =
                    getHourAndMinute(tripStartTime!!)
                binding.bottomSheetReceipt.tvTimeCompleteReceipt.text =
                    getHourAndMinute(tripEndTime!!)
                binding.bottomSheetReceipt.tvOriginAddressReceipt.text = tripOriginAddress
                binding.bottomSheetReceipt.tvDestinationAddressReceipt.text = tripDestinationAddress
                when (tripPaymentType) {
                    Constants.CASH -> binding.bottomSheetReceipt.tvPaymentTypeReceipt.text =
                        getString(R.string.Cash)

                    Constants.MOMO -> binding.bottomSheetReceipt.tvPaymentTypeReceipt.text =
                        getString(R.string.Momo)
                }
                binding.bottomSheetReceipt.tvPriceReceipt.text = tripPrice.formatCurrency()
                binding.bottomSheetReceipt.tvBookingTimeReceipt.text = tripBookingTime
                binding.bottomSheetReceipt.tvTravelDistanceReceipt.text = getString(
                    R.string.TravelDistance,
                    tripDistance.convertMetersToKilometers().toInt()
                )
                binding.bottomSheetReceipt.TvExactTimeReceipt.text = getString(
                    R.string.ReceiptTime,
                    exactDuration
                )
                bottomSheetReceipt.state = BottomSheetBehavior.STATE_EXPANDED
            }
            cancelButton.setOnClickListener {
                val ref = driverID?.let { firestore.collection("Drivers").document(it) }
                ref?.update(
                    mapOf(
                        "trip_ID" to FieldValue.delete()
                    )
                )
                val tripRef = tripId?.let { it1 -> firestore.collection("Trips").document(it1) }
                tripRef?.update(
                    mapOf(
                        "status" to Constants.DRIVER_CANCEL_EMERGENCY
                    )
                )
                resetValue(needDeletePassengerId = false)

                binding.title.text = getString(R.string.ReadyAccept)
                binding.switchButton.isVisible = true
                binding.menuButton.isVisible = true
                bottomSheetDriverOnGoing.state = BottomSheetBehavior.STATE_COLLAPSED
                bottomSheetCancellationEmergency.state = BottomSheetBehavior.STATE_EXPANDED

                animator?.cancel()
                mGoogleMap?.setPadding(0, 0, 0, 0)
                mGoogleMap?.moveCamera(
                    CameraUpdateFactory.newCameraPosition(
                        CameraPosition.Builder().target(mGoogleMap?.cameraPosition!!.target)
                            .tilt(0f)
                            .zoom(14f)
                            .build()
                    )
                )
            }
        }

        with(binding.bottomSheetCancellationEmergency) {
            var cancelEmergencyReason = ""
            radioGroupEmergencyReasons.setOnCheckedChangeListener { _, checkId ->
                cancelEmergencyReason = when (checkId) {
                    R.id.radioEmergencyReason1 -> getString(R.string.EmergencyReason1)
                    R.id.radioEmergencyReason2 -> getString(R.string.EmergencyReason2)
                    R.id.radioEmergencyReason3 -> getString(R.string.EmergencyReason3)
                    R.id.radioEmergencyReason4 -> getString(R.string.EmergencyReason4)
                    R.id.radioEmergencyReason5 -> getString(R.string.EmergencyReason5)
                    else -> ""
                }
            }
            sendCancellationEmergencyButton.setOnClickListener {
                if (cancelEmergencyReason.isNotEmpty()) {
                    val emergencyDetail = etFeedback.text.toString()
                    firestore.collection("Trips").document(tripId!!).collection("Reason")
                        .document(tripReasonID!!)
                        .update(
                            mapOf(
                                "driverCancelEmergency" to cancelEmergencyReason,
                                "driverCancelEmergencyDetail" to emergencyDetail
                            )
                        )

                    if (cancelEmergencyReason == getString(R.string.EmergencyReason2)) {
                        val passengerFeedbackId =
                            firestore.collection("PassengerFeedbacks").document().id
                        firestore.collection("Passengers").document(passengerID!!).update(
                            mapOf(
                                "reportPassengerNum" to FieldValue.increment(1)
                            )
                        )
                        val passengerFeedbackData = PassengerFeedback(
                            passengerFeedback_ID = passengerFeedbackId,
                            driver_ID = driverID!!,
                            passenger_ID = passengerID!!,
                            reportPassengerReason = cancelEmergencyReason,
                            reportPassengerReasonDetail = emergencyDetail,
                            passengerFeedbackTime = Date().toString()
                        )
                        firestore.collection("PassengerFeedbacks").document(passengerFeedbackId)
                            .set(passengerFeedbackData)
                        firestore.collection("Trips").document(tripId!!).collection("Reason")
                            .document(tripReasonID!!)
                            .update(
                                mapOf(
                                    "feedbackPassengerRef" to firestore.collection("PassengerFeedbacks")
                                        .document(passengerFeedbackId)
                                )
                            )
                    }

                    Toast.makeText(
                        this@HomeDriverActivity,
                        getString(R.string.TripCanceled),
                        Toast.LENGTH_SHORT
                    ).show()
                    bottomSheetCancellationEmergency.state = BottomSheetBehavior.STATE_COLLAPSED
                    etFeedback.text.clear()
                    radioGroupEmergencyReasons.clearCheck()
                    tripId = null
                    passengerID = null
                    tripReasonID = null
                } else {
                    Toast.makeText(
                        this@HomeDriverActivity,
                        getString(R.string.PleaseChooseReason),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        with(binding.bottomSheetReceipt) {
            completeReceiptButton.setOnClickListener {
                val tripRef = tripId?.let { it1 -> firestore.collection("Trips").document(it1) }
                tripRef?.update(
                    mapOf(
                        "status" to Constants.COMPLETED
                    )
                )
                val driverRef = driverID?.let { firestore.collection("Drivers").document(it) }
                driverRef?.update(
                    mapOf(
                        "completeTripNum" to FieldValue.increment(1),
                        "totalDistance" to FieldValue.increment(
                            tripDistance.convertMetersToKilometers().toLong()
                        ),
                        "point" to FieldValue.increment(
                            tripDistance.convertMetersToKilometers().toLong()
                        ),
                        "trip_ID" to FieldValue.delete()
                    )
                )
                val passengerRef =
                    passengerID?.let { firestore.collection("Passengers").document(it) }
                passengerRef?.update(
                    mapOf(
                        "bookingTripNum" to FieldValue.increment(1),
                        "point" to FieldValue.increment(Math.round(tripPrice / 1000.0))
                    )
                )
                resetValue()

                binding.title.text = getString(R.string.ReadyAccept)
                binding.title.isVisible = true
                binding.switchButton.isVisible = true
                binding.menuButton.isVisible = true
                bottomSheetReceipt.state = BottomSheetBehavior.STATE_COLLAPSED
                tripId = null
                tripReasonID = null

                animator?.cancel()
                mGoogleMap?.setPadding(0, 0, 0, 0)
                mGoogleMap?.moveCamera(
                    CameraUpdateFactory.newCameraPosition(
                        CameraPosition.Builder().target(mGoogleMap?.cameraPosition!!.target)
                            .tilt(0f)
                            .zoom(14f)
                            .build()
                    )
                )
            }
        }

        listenToDriverCollection()
    }

    private fun listenToTripCollection() {
        val tripRef = firestore.collection("Trips").document(tripId!!)
        listenerTrip = tripRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w("FirestoreListener", "Listen failed.", e)
                return@addSnapshotListener
            }
            if (snapshot != null && snapshot.exists()) {
                val status = snapshot.getString("status")
                if (status == Constants.PASSENGER_CANCEL) {
                    vibrateCustomPattern(this, times = 1, duration = 350L, interval = 500L)
                    val ref = driverID?.let { firestore.collection("Drivers").document(it) }
                    ref?.update(
                        mapOf(
                            "trip_ID" to FieldValue.delete()
                        )
                    )
                    resetValue()

                    binding.title.text = getString(R.string.ReadyAccept)
                    binding.switchButton.isVisible = true
                    binding.bottomSheetIsComing.passengerBoardButton.isVisible = false
                    bottomSheetIsComing.state = BottomSheetBehavior.STATE_COLLAPSED
                    tripId = null

                    animator?.cancel()
                    mGoogleMap?.setPadding(0, 0, 0, 0)
                    mGoogleMap?.moveCamera(
                        CameraUpdateFactory.newCameraPosition(
                            CameraPosition.Builder().target(mGoogleMap?.cameraPosition!!.target)
                                .tilt(0f)
                                .zoom(14f)
                                .build()
                        )
                    )
                }
            } else {
                Log.d("FirestoreListener", "Document does not exist.")
            }
        }
    }

    private fun listenToDriverCollection() {
        val docRef = firestore.collection("Drivers").document(driverID!!)
        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w("FirestoreListener", "Listen failed.", e)
                return@addSnapshotListener
            }
            if (snapshot != null && snapshot.exists() && tripId == null) {
                tripId = snapshot.getString("trip_ID")
                if (tripId != null) {
                    vibrateCustomPattern(this, times = 2, duration = 250L, interval = 400L)
                    val tripRef = firestore.collection("Trips").document(tripId!!)
                    tripRef.get().addOnSuccessListener { document ->
                        if (document.exists()) {
                            passengerID = document.getString("passenger_ID")
                            tripBookingTime = document.getString("bookingTime")
                            tripOriginAddress = document.getString("originAddress")
                            tripDestinationAddress = document.getString("destinationAddress")
                            tripPaymentType = document.getString("paymentType")
                            tripPrice = document.getDouble("price")!!
                            tripDuration = document.getDouble("duration")!!
                            tripDistance = document.getDouble("distance")!!
                            tripOriginLatLng = document.getGeoPoint("originLatLng")?.let {
                                LatLng(it.latitude, it.longitude)
                            }
                            tripDestinationLatLng = document.getGeoPoint("destinationLatLng")?.let {
                                LatLng(it.latitude, it.longitude)
                            }
                            tripReasonID = document.getString("reason_ID")

                            binding.bottomSheetBookingInfo.tvTimeBooking.text =
                                getHourAndMinute(tripBookingTime!!)
                            binding.bottomSheetBookingInfo.tvOriginAddress.text = tripOriginAddress
                            binding.bottomSheetBookingInfo.tvDestinationAddress.text =
                                tripDestinationAddress

                            val paymentType = tripPaymentType
                            when (paymentType) {
                                Constants.CASH -> binding.bottomSheetBookingInfo.tvPaymentType.text =
                                    getString(R.string.Cash)

                                Constants.MOMO -> binding.bottomSheetBookingInfo.tvPaymentType.text =
                                    getString(R.string.Momo)
                            }
                            binding.bottomSheetBookingInfo.tvPrice.text = tripPrice.formatCurrency()
                            binding.bottomSheetBookingInfo.tvDuration.text = getString(
                                R.string.Duration,
                                tripDuration.toInt().convertSecondsToMinutes()
                            )
                            binding.bottomSheetBookingInfo.tvDistance.text = getString(
                                R.string.Distance,
                                tripDistance.convertMetersToKilometers().toInt()
                            )

                            binding.bottomSheetBookingInfo.circularCountdown.create(
                                16,
                                16,
                                CircularCountdown.TYPE_SECOND
                            )
                                .listener(object : CircularListener {
                                    override fun onTick(progress: Int) {
                                    }

                                    override fun onFinish(newCycle: Boolean, cycleCount: Int) {
                                        if (cycleCount > 1 && cycleCount % 2 == 0) {
                                            binding.bottomSheetBookingInfo.circularCountdown.stop()
                                            tripRef.update(
                                                mapOf(
                                                    "status" to Constants.ACCEPT,
                                                    "driver_ID" to driverID
                                                )
                                            )
                                            setUpWhenTripAccepted()
                                        }
                                    }
                                })
                                .start()

                            // Tilt
                            val cameraPos = CameraPosition.Builder().target(tripOriginLatLng!!)
                                .tilt(45f)
                                .zoom(16f)
                                .build()
                            mGoogleMap?.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPos))

                            // Set up camera bounds
                            val bounds: LatLngBounds = LatLngBounds.builder()
                                .include(tripOriginLatLng!!)
                                .include(mGoogleMap?.cameraPosition?.target ?: tripOriginLatLng!!)
                                .build()
                            val point = Point()
                            windowManager.defaultDisplay.getSize(point)
                            mGoogleMap?.setPadding(60, 0, 60, 1000)
                            mGoogleMap?.animateCamera(
                                CameraUpdateFactory.newLatLngBounds(
                                    bounds,
                                    point.x,
                                    200,
                                    60
                                )
                            )

                            // Start animation
                            addMarkerWithPulseAnimation(tripOriginLatLng!!)
                        }
                    }
                    binding.title.isVisible = false
                    binding.switchButton.isVisible = false
                    binding.menuButton.isVisible = false
                    bottomSheetBookingInfo.state = BottomSheetBehavior.STATE_EXPANDED
                    binding.refuseButton.isVisible = true
                } else {
                    resetValue()
                    binding.bottomSheetBookingInfo.circularCountdown.stop()
                    binding.title.isVisible = true
                    binding.switchButton.isVisible = true
                    binding.menuButton.isVisible = true
                    bottomSheetBookingInfo.state = BottomSheetBehavior.STATE_COLLAPSED
                    binding.refuseButton.isVisible = false
                    tripId = null

                    animator?.cancel()
                    mGoogleMap?.setPadding(0, 0, 0, 0)
                    mGoogleMap?.moveCamera(
                        CameraUpdateFactory.newCameraPosition(
                            CameraPosition.Builder().target(mGoogleMap?.cameraPosition!!.target)
                                .tilt(0f)
                                .zoom(14f)
                                .build()
                        )
                    )
                }
            } else {
                Log.d("FirestoreListener", "Document does not exist.")
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun setUpWhenTripAccepted() {
        firestore.collection("Passengers").document(passengerID!!).get()
            .addOnSuccessListener { document ->
                passengerPhone = document.getString("mobile_No")
                val passengerName =
                    document.getString("lastname") + " " + document.getString("firstname")
                binding.bottomSheetIsComing.tvPassengerName.text = passengerName
            }

        bottomSheetBookingInfo.state = BottomSheetBehavior.STATE_COLLAPSED
        binding.refuseButton.isVisible = false
        binding.menuButton.isVisible = false
        binding.title.isVisible = true
        binding.title.text = getString(R.string.IsComing)
        animator?.cancel()
        mGoogleMap?.moveCamera(
            CameraUpdateFactory.newCameraPosition(
                CameraPosition.Builder().target(mGoogleMap?.cameraPosition!!.target)
                    .tilt(0f)
                    .zoom(14f)
                    .build()
            )
        )

        // Re-locate current location
        mFusedLocationClient?.lastLocation
            ?.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    currentLatLng = LatLng(location.latitude, location.longitude)
                    currentLocation = location
                }
                currentLatLng?.let { originLatLng -> zoomOnMap(originLatLng, 15f) }
                mGoogleMap!!.isMyLocationEnabled = true
                currentLatLng?.let { direction(it, tripOriginLatLng!!) }
            }
        bottomSheetIsComing.state = BottomSheetBehavior.STATE_EXPANDED

        listenToTripCollection()
    }


    private fun updateLocationHandle(location: Location?) {
        zoomOnMap(LatLng(location!!.latitude, location.longitude), 16f)
        currentLocation = location
        if (isReady == true) {
            skipTime -= 1
            if (skipTime == 0) {
                val lat = location.latitude
                val lng = location.longitude
                val hash = GeoFireUtils.getGeoHashForLocation(GeoLocation(lat, lng))
                val updates: MutableMap<String, Any> = mutableMapOf(
                    "geohash" to hash,
                    "current_Lat" to lat,
                    "current_Lng" to lng,
                )
                val ref = driverID?.let { firestore.collection("Drivers").document(it) }
                ref?.update(updates)
                // TODO Change if need
                skipTime = 2
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mGoogleMap = googleMap

        mGoogleMap?.setMaxZoomPreference(20.0f)
        mGoogleMap?.setMapStyle(
            MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style)
        )
    }

    private fun addDefaultMarker(position: LatLng): Marker? {
        return mGoogleMap?.addMarker(
            MarkerOptions()
                .position(position)
                .title("Custom Default Marker")
                .draggable(false)
        )
    }

    private fun zoomOnMap(latLng: LatLng, zoomRate: Float = 14f) {
        val newLatLngZoom = CameraUpdateFactory.newLatLngZoom(latLng, zoomRate)
        mGoogleMap?.animateCamera(newLatLngZoom)
    }

    private fun addMarkerWithPulseAnimation(latLng: LatLng) {
        addDefaultMarker(latLng)
        addPulsatingEffect(latLng)
    }

    private fun addPulsatingEffect(latLng: LatLng?, isStartSpinningAnimate: Boolean = true) {
        lastPulseAnimator?.cancel()
        if (lastUserCircle != null) {
            lastUserCircle?.remove()
            lastUserCircle = null
        }

        lastPulseAnimator = Utils.valueAnimate(EFFECT_DURATION) { p0 ->
            val animatedRadius = p0.animatedValue.toString().toDouble()
            if (lastUserCircle == null) {
                lastUserCircle = mGoogleMap!!.addCircle(
                    CircleOptions()
                        .center(latLng!!)
                        .radius(animatedRadius)
                        .strokeColor(Color.WHITE)
                        .fillColor(
                            ContextCompat.getColor(
                                this@HomeDriverActivity,
                                R.color.blue_200
                            )
                        )
                )
            } else {
                lastUserCircle!!.radius = animatedRadius
            }
        }

        // Start rotating camera
        if (isStartSpinningAnimate) {
            startMapCameraSpinningAnimation(latLng)
        }
    }

    private fun startMapCameraSpinningAnimation(target: LatLng?) {
        animator?.cancel()
        animator = ValueAnimator.ofFloat(0f, (DESIRED_NUM_OF_SPINS * 360).toFloat())
        animator?.duration =
            (DESIRED_NUM_OF_SPINS * DESIRED_SECOND_PER_ONE_FULL_360_SPIN * 1000).toLong()
        animator?.repeatCount = ValueAnimator.INFINITE
        animator?.repeatMode = ValueAnimator.RESTART
        animator?.interpolator = LinearInterpolator()
        animator?.startDelay = 100
        animator?.addUpdateListener { valueAnimator ->
            val newBearingValue = valueAnimator.animatedValue as Float
            mGoogleMap?.moveCamera(
                CameraUpdateFactory.newCameraPosition(
                    CameraPosition.Builder()
                        .target(target!!)
                        .zoom(15f)
                        .tilt(45f)
                        .bearing(newBearingValue)
                        .build()
                )
            )
        }
        animator?.start()
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

    override fun onDestroy() {
        animator?.end()
        super.onDestroy()
    }

    private fun direction(origin: LatLng, destination: LatLng, mode: String = "driving") {
        mGoogleMap?.clear()
        val requestQueue = Volley.newRequestQueue(this)
        val url = Uri.parse("https://maps.googleapis.com/maps/api/directions/json")
            .buildUpon()
            .appendQueryParameter(
                "destination",
                "${destination.latitude}, ${destination.longitude}"
            )
            .appendQueryParameter("origin", "${origin.latitude}, ${origin.longitude}")
            .appendQueryParameter("mode", mode)
            .appendQueryParameter("key", BuildConfig.MAP_KEY)
            .toString()

        Log.d("DirectionsRequest", "URL: $url")

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,  // Method: GET, POST, PUT, DELETE
            url,
            null,
            { response ->
                Log.d("DirectionsResponse", "Response: ${response.toString(2)}")
                try {
                    val status = response.getString("status")
                    if (status.equals("OK")) {
                        val routes: JSONArray = response.getJSONArray("routes")

                        var points: ArrayList<LatLng>
                        var polylineOptions: PolylineOptions? = null

                        for (i in 0 until routes.length()) {
                            points = ArrayList()
                            polylineOptions = PolylineOptions()
                            val legs: JSONArray = routes.getJSONObject(i).getJSONArray("legs")

                            for (j in 0 until legs.length()) {
                                distance = legs.getJSONObject(j).getJSONObject("distance")
                                    .getString("value").toDouble()
                                duration = legs.getJSONObject(j).getJSONObject("duration")
                                    .getString("value").toInt()

                                val steps: JSONArray = legs.getJSONObject(j).getJSONArray("steps")

                                for (k in 0 until steps.length()) {
                                    val polyline = steps.getJSONObject(k).getJSONObject("polyline")
                                        .getString("points")
                                    val list: List<LatLng> = Utils.decodePolyLine(polyline)

                                    for (l in list.indices) {
                                        val position = LatLng(list[l].latitude, list[l].longitude)
                                        points.add(position)
                                    }
                                }
                            }
                            polylineOptions.addAll(points)
                            polylineOptions.width(15F)
                            polylineOptions.color(
                                ContextCompat.getColor(
                                    this,
                                    R.color.button_background
                                )
                            )
                            polylineOptions.geodesic(true)
                        }
                        if (polylineOptions != null) {
                            mGoogleMap?.addPolyline(polylineOptions)
                        }
                        addDefaultMarker(destination)

                        val bounds: LatLngBounds = LatLngBounds.builder()
                            .include(origin)
                            .include(destination)
                            .build()
                        val point = Point()
                        windowManager.defaultDisplay.getSize(point)
                        mGoogleMap?.setPadding(60, 0, 60, 600)
                        mGoogleMap?.animateCamera(
                            CameraUpdateFactory.newLatLngBounds(
                                bounds,
                                point.x,
                                200,
                                60
                            )
                        )
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            { error: VolleyError ->
                error.printStackTrace()
            }
        )
        val retryPolicy = DefaultRetryPolicy(
            30000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        jsonObjectRequest.setRetryPolicy(retryPolicy)
        requestQueue.add(jsonObjectRequest)
    }

    private fun checkLocationRequest() {
        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val client = LocationServices.getSettingsClient(this)
        val task = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener {
            binding.switchButton.isChecked = false
            Toast.makeText(
                this,
                getString(R.string.WaitSecondToDetermineLocation),
                Toast.LENGTH_SHORT
            ).show()
        }
        task.addOnFailureListener {
            binding.switchButton.isChecked = false
            showLocationRequestDialog()
        }
    }

    private fun resetValue(needDeletePassengerId: Boolean = true) {
        if (needDeletePassengerId) {
            passengerID = null
        }
        mGoogleMap?.clear()
        tripDestinationLatLng = null
        tripDestinationAddress = null
        currentLatLng = null
        tripOriginAddress = null
        distance = 0.0
        duration = 0
        tripPaymentType = null
        tripBookingTime = null
        tripPrice = 0.0
        tripDuration = 0.0
        tripDistance = 0.0
        tripOriginLatLng = null
        passengerPhone = null
        tripStartTime = null
        tripEndTime = null
        listenerTrip?.remove()
    }
}
