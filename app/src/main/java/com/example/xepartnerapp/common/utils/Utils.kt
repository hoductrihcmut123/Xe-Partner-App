package com.example.xepartnerapp.common.utils

import android.Manifest
import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.LatLng
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlin.math.round
import kotlin.math.roundToInt

object Utils {
    fun Context.isCheckLocationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    fun decodePolyLine(poly: String): List<LatLng> {
        val len = poly.length
        var index = 0
        val decoded: MutableList<LatLng> = ArrayList()
        var lat = 0
        var lng = 0
        while (index < len) {
            var shift = 0
            var result = 0
            var b: Int
            do {
                b = poly[index++].code - 63
                result = result or (b and 31 shl shift)
                shift += 5
            } while (b >= 32)
            val dLat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dLat
            shift = 0
            result = 0
            do {
                b = poly[index++].code - 63
                result = result or (b and 31 shl shift)
                shift += 5
            } while (b >= 32)
            val dLng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dLng
            decoded.add(LatLng(lat.toDouble() / 100000.0, lng.toDouble() / 100000.0))
        }
        return decoded
    }

    fun Double.convertMetersToKilometers(): Double {
        return String.format(Locale.US, "%.1f", this / 1000).toDouble()
    }

    fun Int.convertSecondsToMinutes(): Int {
        return (this / 60.0).roundToInt()
    }

    fun Double.formatCurrency(): String {
        val decimalFormat = DecimalFormat("#,###")
        return "${decimalFormat.format(this)} VNĐ"
    }

    fun Double.calculateMoney(vehicleType: String): Double {
        return when (vehicleType) {
            Constants.BIKE -> this * Constants.BIKE_RATE
            Constants.CAR -> this * Constants.CAR_RATE
            Constants.MVP -> this * Constants.MVP_RATE
            else -> 0.0
        }
    }

    fun getCurrentTimeFormatted(): String {
        val currentTime = Date()
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        return dateFormat.format(currentTime)
    }

    fun getHourAndMinute(dateString: String): String {
        if (dateString.isEmpty()) return "__:__"
        val time = dateString.split(" ")[3]
        return time.substring(0, 5)
    }

    fun calculateMinutesDifference(time1: String, time2: String): Long {
        if (time1.isEmpty() || time2.isEmpty()) return 0

        val format = SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH)

        val date1 = format.parse(time1)
        val date2 = format.parse(time2)

        val differenceInMillis = (date2?.time ?: 0) - (date1?.time ?: 0)

        val minutes = TimeUnit.MILLISECONDS.toMinutes(differenceInMillis).toDouble()
        return round(minutes).toLong()
    }

    fun getLastThreeChars(input: String): String {
        return if (input.length >= 3) {
            input.takeLast(3)
        } else {
            input
        }
    }

    fun extractDate(input: String): String? {
        val inputFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH)
        val outputFormat = SimpleDateFormat("MMM dd yyyy", Locale.ENGLISH)
        return try {
            val date = inputFormat.parse(input)
            date?.let { outputFormat.format(it) }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun valueAnimate(duration: Int, listener: AnimatorUpdateListener?): ValueAnimator? {
        val valueAnimate = ValueAnimator.ofFloat(0f, 200f)
        valueAnimate.duration = duration.toLong()
        valueAnimate.addUpdateListener(listener)
        valueAnimate.repeatCount = ValueAnimator.INFINITE
        valueAnimate.repeatMode = ValueAnimator.RESTART
        valueAnimate.start()
        return valueAnimate
    }

    fun vibrateCustomPattern(context: Context, times: Int, duration: Long, interval: Long) {
        val vibrator = ContextCompat.getSystemService(context, Vibrator::class.java)
        val pattern = LongArray(times * 2) { i ->
            if (i % 2 == 0) interval else duration
        }
        pattern[0] = 0

        if (vibrator != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createWaveform(pattern, -1))
            } else {
                vibrator.vibrate(pattern, -1)
            }
        }
    }

    fun String.extractMonthYear(): String {
        val parts = this.split(" ")
        return if (parts.size >= 6) {
            "${parts[1]}${parts.last()}"
        } else {
            ""
        }
    }

    fun String.extractDay(): String {
        val parts = this.split(" ")
        return if (parts.size >= 6) {
            parts[2]
        } else {
            ""
        }
    }

    fun String.extractTime(): String {
        val parts = this.split(" ")
        return if (parts.size >= 6) {
            parts[3]
        } else {
            ""
        }
    }
}