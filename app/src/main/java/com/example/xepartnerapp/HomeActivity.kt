package com.example.xepartnerapp

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Point
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
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
import com.example.xepartnerapp.common.utils.Constants
import com.example.xepartnerapp.common.utils.Constants.DESIRED_NUM_OF_SPINS
import com.example.xepartnerapp.common.utils.Constants.DESIRED_SECOND_PER_ONE_FULL_360_SPIN
import com.example.xepartnerapp.common.utils.Constants.EFFECT_DURATION
import com.example.xepartnerapp.common.utils.Utils
import com.example.xepartnerapp.common.utils.Utils.calculateMoney
import com.example.xepartnerapp.common.utils.Utils.convertMetersToKilometers
import com.example.xepartnerapp.common.utils.Utils.convertSecondsToMinutes
import com.example.xepartnerapp.common.utils.Utils.formatCurrency
import com.example.xepartnerapp.common.utils.Utils.getCurrentTimeFormatted
import com.example.xepartnerapp.common.utils.Utils.isCheckLocationPermission
import com.example.xepartnerapp.common.utils.showLocationPermissionDialog
import com.example.xepartnerapp.databinding.ActivityHomeBinding
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
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
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.auth.FirebaseAuth
import org.json.JSONArray
import org.json.JSONException

class HomeActivity : AppCompatActivity(), OnMapReadyCallback {
    private var mGoogleMap: GoogleMap? = null

    private lateinit var autocompleteFragment: AutocompleteSupportFragment

    private lateinit var binding: ActivityHomeBinding
    private lateinit var auth: FirebaseAuth

    private var mFusedLocationClient: FusedLocationProviderClient? = null

    private var originLatLng: LatLng? = null
    private var originAddress: String? = null
    private var destinationLatLng: LatLng? = null
    private var destinationAddress: String? = null
    private var showedDirection: Boolean = false
    private var distance: Double = 0.0
    private var duration: Int = 0

    private var vehicleType: String? = null
    private var paymentType: String? = null

    private lateinit var bottomSheetVehicleType: BottomSheetBehavior<View>
    private lateinit var bottomSheetPaymentType: BottomSheetBehavior<View>
    private lateinit var bottomSheetBookingInfo: BottomSheetBehavior<View>

    // Effect
    private var lastUserCircle: Circle? = null
    private var lastPulseAnimator: ValueAnimator? = null

    // Spinning animation
    private var animator: ValueAnimator? = null

    private val mapLongClickListener = GoogleMap.OnMapLongClickListener { location ->
        mGoogleMap?.clear()
        originLatLng = LatLng(location.latitude, location.longitude)
        addDefaultMarker(location)
        findAddress(location)
    }

    private val markerClickListener = GoogleMap.OnMarkerClickListener { marker ->
        marker.remove()
        false
    }

    @SuppressLint("MissingPermission")
    @OptIn(ExperimentalAnimationApi::class, ExperimentalPagerApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Places.initialize(applicationContext, BuildConfig.MAP_KEY)
        autocompleteFragment = supportFragmentManager.findFragmentById(R.id.autocomplete_fragment)
                as AutocompleteSupportFragment
        autocompleteFragment.setPlaceFields(
            listOf(
                Place.Field.ID,
                Place.Field.ADDRESS,
                Place.Field.LAT_LNG
            )
        )
        autocompleteFragment.setCountries("VN")
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onError(p0: Status) {}

            override fun onPlaceSelected(place: Place) {
                mGoogleMap?.clear()
                val address = place.address
                val id = place.id
                val latLng = place.latLng!!
                val marker = addDefaultMarker(latLng)
                if (marker != null) {
                    marker.title = address
                }
                if (marker != null) {
                    marker.snippet = id
                }

                if (destinationLatLng == null) {
                    destinationLatLng = latLng
                    destinationAddress = address
                    with(binding) {
                        subTitle.text = getString(R.string.Destination, destinationAddress)
                        subTitle.isVisible = true
                        title.text = getString(R.string.SelectPickUpPoint)
                        title.isVisible = true
                        chooseLocationButton.isVisible = true
                        menuButton.isVisible = false
                        backButton.isVisible = true
                    }
                } else {
                    originLatLng = latLng
                    originAddress = address
                }
                zoomOnMap(latLng)
            }
        })

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment)
                as SupportMapFragment
        mapFragment.getMapAsync(this)

        mFusedLocationClient = this.let { LocationServices.getFusedLocationProviderClient(it) }

        // Set up bottomSheetVehicleType
        bottomSheetVehicleType = BottomSheetBehavior.from(findViewById(R.id.bottomSheetVehicleType))
        bottomSheetVehicleType.peekHeight = 0
        bottomSheetVehicleType.state = BottomSheetBehavior.STATE_COLLAPSED
        bottomSheetVehicleType.isDraggable = false

        // Set up bottomSheetPaymentType
        bottomSheetPaymentType = BottomSheetBehavior.from(findViewById(R.id.bottomSheetPaymentType))
        bottomSheetPaymentType.peekHeight = 0
        bottomSheetPaymentType.state = BottomSheetBehavior.STATE_COLLAPSED
        bottomSheetPaymentType.isDraggable = false

        // Set up bottomSheetBookingInfo
        bottomSheetBookingInfo = BottomSheetBehavior.from(findViewById(R.id.bottomSheetBookingInfo))
        bottomSheetBookingInfo.peekHeight = 0
        bottomSheetBookingInfo.state = BottomSheetBehavior.STATE_COLLAPSED
        bottomSheetBookingInfo.isDraggable = false

        binding.currentLocationButton.setOnClickListener {
            if (this@HomeActivity.isCheckLocationPermission()) {
                mGoogleMap?.clear()
                mFusedLocationClient?.lastLocation
                    ?.addOnSuccessListener { location: Location? ->
                        if (location != null) {
                            originLatLng = LatLng(location.latitude, location.longitude)
                        }
                        originLatLng?.let { originLatLng -> zoomOnMap(originLatLng, 18f) }
                        originLatLng?.let { originLatLng -> addDefaultMarker(originLatLng) }
                        originLatLng?.let { originLatLng ->
                            findAddress(
                                originLatLng,
                                isCurrentLocation = true
                            )
                        }
                    }
            } else {
                showLocationPermissionDialog()
            }
        }

        binding.chooseLocationButton.setOnClickListener {
            if (originLatLng != null && originAddress != null) {
                with(binding) {
                    subTitle.text =
                        getString(R.string.Destination_Pickup, originAddress, destinationAddress)
                    subTitle.isVisible = true
                    title.text = getString(R.string.VehicleOptions)
                    title.isVisible = true
                    chooseLocationButton.isVisible = false
                    currentLocationButton.isVisible = false
                    searchView.isVisible = false
                }
                direction(originLatLng!!, destinationLatLng!!)
                showedDirection = true
                mGoogleMap?.setOnMapLongClickListener(null)
                mGoogleMap?.setOnMarkerClickListener(null)
                bottomSheetVehicleType.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        binding.backButton.setOnClickListener {
            resetValue()
        }

        binding.menuButton.setOnClickListener {
            if (currentUser != null) {
                auth.signOut()
            }
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        with(binding.bottomSheetVehicleType) {
            cardBike.setOnClickListener {
                vehicleType = Constants.BIKE
                bottomSheetVehicleType.state = BottomSheetBehavior.STATE_COLLAPSED
                binding.title.text = getString(R.string.PaymentOptions)
                bottomSheetPaymentType.state = BottomSheetBehavior.STATE_EXPANDED
            }
            cardCar.setOnClickListener {
                vehicleType = Constants.CAR
                bottomSheetVehicleType.state = BottomSheetBehavior.STATE_COLLAPSED
                binding.title.text = getString(R.string.PaymentOptions)
                bottomSheetPaymentType.state = BottomSheetBehavior.STATE_EXPANDED
            }
            cardMvp.setOnClickListener {
                vehicleType = Constants.MVP
                bottomSheetVehicleType.state = BottomSheetBehavior.STATE_COLLAPSED
                binding.title.text = getString(R.string.PaymentOptions)
                bottomSheetPaymentType.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }

        with(binding.bottomSheetPaymentType) {
            cardCash.setOnClickListener {
                paymentType = Constants.CASH
                cardCash.cardElevation = 12f
                cardMomo.cardElevation = 0f
            }
            cardMomo.setOnClickListener {
                paymentType = Constants.MOMO
                cardMomo.elevation = 12f
                cardCash.elevation = 0f
            }
            bookingButton.setOnClickListener {
                if (paymentType != null) {
                    setUpUIWhenBooking()

                    mGoogleMap?.clear()
                    // Tilt
                    val cameraPos = CameraPosition.Builder().target(originLatLng!!)
                        .tilt(45f)
                        .zoom(16f)
                        .build()
                    mGoogleMap?.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPos))

                    // Start animation
                    addMarkerWithPulseAnimation()
                }
            }
        }

        with(binding.bottomSheetBookingInfo) {
            cancelButton.setOnClickListener {
                resetValue()
                bottomSheetBookingInfo.state = BottomSheetBehavior.STATE_COLLAPSED

                animator?.cancel()
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
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mGoogleMap = googleMap

        mGoogleMap!!.setMaxZoomPreference(20.0f)
        mGoogleMap!!.setMapStyle(
            MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style)
        )
        mGoogleMap?.setOnMapLongClickListener(mapLongClickListener)
        mGoogleMap?.setOnMarkerClickListener(markerClickListener)
    }

    private fun addDefaultMarker(position: LatLng): Marker? {
        return mGoogleMap?.addMarker(
            MarkerOptions()
                .position(position)
                .title("Custom Default Marker")
                .draggable(false)
                .icon(
                    BitmapDescriptorFactory.fromBitmap(
                        Bitmap.createScaledBitmap(
                            BitmapFactory.decodeResource(resources, R.drawable.marker_icon),
                            130,
                            130,
                            false
                        )
                    )
                )
        )
    }

    private fun addStartedMarker(position: LatLng): Marker? {
        return mGoogleMap?.addMarker(
            MarkerOptions()
                .position(position)
                .title("Custom Started Marker")
                .draggable(false)
                .icon(
                    BitmapDescriptorFactory.fromBitmap(
                        Bitmap.createScaledBitmap(
                            BitmapFactory.decodeResource(resources, R.drawable.started_marker_icon),
                            50,
                            50,
                            false
                        )
                    )
                )
        )
    }

    private fun zoomOnMap(latLng: LatLng, zoomRate: Float = 14f) {
        val newLatLngZoom = CameraUpdateFactory.newLatLngZoom(latLng, zoomRate)
        mGoogleMap?.animateCamera(newLatLngZoom)
    }

    private fun addMarkerWithPulseAnimation() {
        originLatLng?.let { addDefaultMarker(it) }
        addPulsatingEffect(originLatLng)
    }

    private fun addPulsatingEffect(originLatLng: LatLng?) {
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
                        .center(originLatLng!!)
                        .radius(animatedRadius)
                        .strokeColor(Color.WHITE)
                        .fillColor(ContextCompat.getColor(this@HomeActivity, R.color.blue_200))
                )
            } else {
                lastUserCircle!!.radius = animatedRadius
            }
        }

        // Start rotating camera
        startMapCameraSpinningAnimation(mGoogleMap?.cameraPosition?.target)
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
                        .zoom(16f)
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
                                setVehicleTypeValue()

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
                        addStartedMarker(origin)
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

    private fun findAddress(latLng: LatLng, isCurrentLocation: Boolean = false) {
        val url = Uri.parse("https://maps.googleapis.com/maps/api/geocode/json")
            .buildUpon()
            .appendQueryParameter("latlng", "${latLng.latitude},${latLng.longitude}")
            .appendQueryParameter("key", BuildConfig.MAP_KEY)
            .toString()

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            { response ->
                val results = response.getJSONArray("results")
                if (results.length() > 0) {
                    originAddress = results.getJSONObject(0).getString("formatted_address")
                    with(binding) {
                        title.text = originAddress
                        title.isVisible = true
                        if (isCurrentLocation) {
                            subTitle.text = getString(R.string.YourLocation)
                            subTitle.isVisible = true
                        } else {
                            subTitle.isVisible = false
                        }
                    }
                    Log.d("Geocode", "Address: $originAddress")
                } else {
                    Log.d("Geocode", "Address not found")
                }
            },
            { error ->
                error.printStackTrace()
                Log.e("Geocode", "Lỗi: ${error.message}")
            }
        )
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(jsonObjectRequest)
    }

    private fun setVehicleTypeValue() {
        binding.bottomSheetVehicleType.moneyBike.text =
            distance.convertMetersToKilometers().calculateMoney(Constants.BIKE).formatCurrency()
        binding.bottomSheetVehicleType.moneyCar.text =
            distance.convertMetersToKilometers().calculateMoney(Constants.CAR).formatCurrency()
        binding.bottomSheetVehicleType.moneyMvp.text =
            distance.convertMetersToKilometers().calculateMoney(Constants.MVP).formatCurrency()
        binding.bottomSheetVehicleType.TvEstimateTime.text =
            getString(R.string.TripTime, duration.convertSecondsToMinutes().toString())
    }

    private fun resetValue() {
        if (destinationLatLng != null && !showedDirection) {
            with(binding) {
                subTitle.text = ""
                subTitle.isVisible = false
                title.text = ""
                title.isVisible = false
                chooseLocationButton.isVisible = false
                menuButton.isVisible = true
                backButton.isVisible = false
            }
        } else {
            with(binding) {
                subTitle.text = ""
                subTitle.isVisible = false
                title.text = ""
                title.isVisible = false
                chooseLocationButton.isVisible = false
                currentLocationButton.isVisible = true
                searchView.isVisible = true
                menuButton.isVisible = true
                backButton.isVisible = false
                bottomSheetPaymentType.cardMomo.elevation = 1f
                bottomSheetPaymentType.cardCash.elevation = 1f
            }
            showedDirection = false
            bottomSheetVehicleType.state = BottomSheetBehavior.STATE_COLLAPSED
            bottomSheetPaymentType.state = BottomSheetBehavior.STATE_COLLAPSED
            mGoogleMap?.setOnMapLongClickListener(mapLongClickListener)
            mGoogleMap?.setOnMarkerClickListener(markerClickListener)
            mGoogleMap?.setPadding(0, 0, 60, 0)
        }
        mGoogleMap?.clear()
        destinationLatLng = null
        destinationAddress = null
        originLatLng = null
        originAddress = null
        distance = 0.0
        duration = 0
        vehicleType = null
        paymentType = null
    }

    private fun setUpUIWhenBooking() {
        with(binding) {
            bottomSheetPaymentType.cardMomo.elevation = 1f
            bottomSheetPaymentType.cardCash.elevation = 1f
            this@HomeActivity.bottomSheetPaymentType.state = BottomSheetBehavior.STATE_COLLAPSED

            title.isVisible = false
            subTitle.isVisible = false
            backButton.isVisible = false

            this@HomeActivity.bottomSheetBookingInfo.state = BottomSheetBehavior.STATE_EXPANDED
            with(bottomSheetBookingInfo) {
                tvTimeBooking.text = getCurrentTimeFormatted()
                tvOriginAddress.text = originAddress
                tvDestinationAddress.text = destinationAddress
                when (paymentType) {
                    Constants.CASH -> tvPaymentType.text = getString(R.string.Cash)
                    Constants.MOMO -> tvPaymentType.text = getString(R.string.Momo)
                }
            }
        }
    }
}
