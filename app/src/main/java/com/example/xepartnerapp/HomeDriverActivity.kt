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
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.xepartnerapp.common.utils.Constants.DESIRED_NUM_OF_SPINS
import com.example.xepartnerapp.common.utils.Constants.DESIRED_SECOND_PER_ONE_FULL_360_SPIN
import com.example.xepartnerapp.common.utils.Constants.EFFECT_DURATION
import com.example.xepartnerapp.common.utils.Utils
import com.example.xepartnerapp.common.utils.Utils.isCheckLocationPermission
import com.example.xepartnerapp.common.utils.showLocationPermissionDialog
import com.example.xepartnerapp.databinding.ActivityHomeDriverBinding
import com.firebase.geofire.GeoFireUtils
import com.firebase.geofire.GeoLocation
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
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
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.suke.widget.SwitchButton
import org.json.JSONArray
import org.json.JSONException

class HomeDriverActivity : AppCompatActivity(), OnMapReadyCallback {
    private var mGoogleMap: GoogleMap? = null

    private lateinit var binding: ActivityHomeDriverBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private lateinit var locationCallback: LocationCallback

    private var originLatLng: LatLng? = null
    private var originAddress: String? = null
    private var destinationLatLng: LatLng? = null
    private var destinationAddress: String? = null
    private var distance: Double = 0.0
    private var duration: Int = 0

    private var paymentType: String? = null
    private var isReady: Boolean? = null
    private var skipTime: Int = 15

    private lateinit var bottomSheetBookingInfo: BottomSheetBehavior<View>

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

        binding.currentLocationButton.setOnClickListener {
            if (this@HomeDriverActivity.isCheckLocationPermission()) {
                mGoogleMap?.clear()
                mFusedLocationClient?.lastLocation
                    ?.addOnSuccessListener { location: Location? ->
                        if (location != null) {
                            originLatLng = LatLng(location.latitude, location.longitude)
                        }
                        originLatLng?.let { originLatLng -> zoomOnMap(originLatLng, 16f) }
                        mGoogleMap!!.isMyLocationEnabled = true
                    }
            } else {
                showLocationPermissionDialog()
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

        binding.switchButton.setOnCheckedChangeListener { _, isChecked ->
            isReady = isChecked
            if (isChecked) {
                binding.title.text = getString(R.string.ReadyAccept)
            } else {
                binding.title.text = getString(R.string.NotAccepting)
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

    private fun updateLocationHandle(location: Location?) {
        zoomOnMap(LatLng(location!!.latitude, location.longitude), 16f)

        if (isReady == true) {
            skipTime -= 1
            if (skipTime == 0) {
                val lat = location.latitude
                val lng = location.longitude
                val hash = GeoFireUtils.getGeoHashForLocation(GeoLocation(lat, lng))
                val updates: MutableMap<String, Any> = mutableMapOf(
                    "geohash" to hash,
                    "lat" to lat,
                    "lng" to lng,
                )
                val ref = firestore.collection("Drivers").document("GsTVdwClHtRd1pzP5Rqq")
                ref.update(updates)
                skipTime = 15
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
                        .fillColor(ContextCompat.getColor(this@HomeDriverActivity, R.color.blue_200))
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

    private fun resetValue() {
        mGoogleMap?.clear()
        destinationLatLng = null
        destinationAddress = null
        originLatLng = null
        originAddress = null
        distance = 0.0
        duration = 0
        paymentType = null
    }
}
