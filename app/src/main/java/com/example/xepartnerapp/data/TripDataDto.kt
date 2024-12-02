package com.example.xepartnerapp.data

import com.google.firebase.firestore.GeoPoint
import android.os.Parcel
import android.os.Parcelable

data class TripDataDto(
    val trip_ID :String? = null,
    val originLat: Double? = null,
    val originLng: Double? = null,
    val originAddress: String? = null,
    val destinationLat: Double? = null,
    val destinationLng: Double? = null,
    val destinationAddress: String? = null,
    val vehicleType: String? = null,
    val paymentType: String? = null,
    val price: Double? = null,
    val distance: Double? = null,
    val duration: Int? = null,
    val bookingTime: String? = null,
    val startTime: String? = null,
    val endTime: String? = null,
    val status: String? = null,
    val passenger_ID: String? = null,
    val driver_ID: String? = null,
    val reason_ID: String? = null,
    val reason: ReasonDataDto? = null
) : Parcelable {

    constructor(parcel: Parcel) : this(
        trip_ID = parcel.readString(),
        originLat = parcel.readValue(Double::class.java.classLoader) as? Double,
        originLng = parcel.readValue(Double::class.java.classLoader) as? Double,
        originAddress = parcel.readString(),
        destinationLat = parcel.readValue(Double::class.java.classLoader) as? Double,
        destinationLng = parcel.readValue(Double::class.java.classLoader) as? Double,
        destinationAddress = parcel.readString(),
        vehicleType = parcel.readString(),
        paymentType = parcel.readString(),
        price = parcel.readValue(Double::class.java.classLoader) as? Double,
        distance = parcel.readValue(Double::class.java.classLoader) as? Double,
        duration = parcel.readValue(Int::class.java.classLoader) as? Int,
        bookingTime = parcel.readString(),
        startTime = parcel.readString(),
        endTime = parcel.readString(),
        status = parcel.readString(),
        passenger_ID = parcel.readString(),
        driver_ID = parcel.readString(),
        reason_ID = parcel.readString(),
        reason = parcel.readParcelable(ReasonDataDto::class.java.classLoader)
    )


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(trip_ID)
        parcel.writeValue(originLat)
        parcel.writeValue(originLng)
        parcel.writeString(originAddress)
        parcel.writeValue(destinationLat)
        parcel.writeValue(destinationLng)
        parcel.writeString(destinationAddress)
        parcel.writeString(vehicleType)
        parcel.writeString(paymentType)
        parcel.writeValue(price)
        parcel.writeValue(distance)
        parcel.writeValue(duration)
        parcel.writeString(bookingTime)
        parcel.writeString(startTime)
        parcel.writeString(endTime)
        parcel.writeString(status)
        parcel.writeString(passenger_ID)
        parcel.writeString(driver_ID)
        parcel.writeString(reason_ID)
        parcel.writeParcelable(reason, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField
        val CREATOR = object : Parcelable.Creator<TripDataDto> {
            override fun createFromParcel(parcel: Parcel): TripDataDto {
                return TripDataDto(parcel)
            }

            override fun newArray(size: Int): Array<TripDataDto?> {
                return arrayOfNulls(size)
            }
        }
    }

    fun toOriginGeoPoint(): GeoPoint? {
        return if (originLat != null && originLng != null) {
            GeoPoint(originLat, originLng)
        } else {
            null
        }
    }

    fun toDestinationGeoPoint(): GeoPoint? {
        return if (destinationLat != null && destinationLng != null) {
            GeoPoint(destinationLat, destinationLng)
        } else {
            null
        }
    }
}
