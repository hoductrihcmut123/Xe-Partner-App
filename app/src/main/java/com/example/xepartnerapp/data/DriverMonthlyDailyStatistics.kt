package com.example.xepartnerapp.data

import android.os.Parcel
import android.os.Parcelable

data class DriverMonthlyDailyStatistics(
    val monthOrDay: String = "",
    val totalRequest: Int = 0,
    val tripSuccess: Int = 0,
    val totalCancel: Int = 0,
    val driverCancel: Int = 0,
    val passengerCancel: Int = 0,
    val totalDistance: Double = 0.0,
    val totalDuration: Long = 0,
    val totalIncome: String = "",
    val monthOrDayData: DriverMonthlyDailyData
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readDouble(),
        parcel.readLong(),
        parcel.readString() ?: "",
        parcel.readParcelable(DriverMonthlyDailyData::class.java.classLoader) ?: DriverMonthlyDailyData()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(monthOrDay)
        parcel.writeInt(totalRequest)
        parcel.writeInt(tripSuccess)
        parcel.writeInt(totalCancel)
        parcel.writeInt(driverCancel)
        parcel.writeInt(passengerCancel)
        parcel.writeDouble(totalDistance)
        parcel.writeLong(totalDuration)
        parcel.writeString(totalIncome)
        parcel.writeParcelable(monthOrDayData, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField
        val CREATOR = object : Parcelable.Creator<DriverMonthlyDailyStatistics> {
            override fun createFromParcel(parcel: Parcel): DriverMonthlyDailyStatistics {
                return DriverMonthlyDailyStatistics(parcel)
            }

            override fun newArray(size: Int): Array<DriverMonthlyDailyStatistics?> {
                return arrayOfNulls(size)
            }
        }
    }
}
