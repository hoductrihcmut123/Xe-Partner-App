package com.example.xepartnerapp.data

import android.os.Parcel
import android.os.Parcelable

data class DriverMonthlyDailyData(
    val title: String = "",
    var tripDataList: List<TripDataDto> = listOf()
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.createTypedArrayList(TripDataDto.CREATOR) ?: listOf()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeTypedList(tripDataList)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField
        val CREATOR = object : Parcelable.Creator<DriverMonthlyDailyData> {
            override fun createFromParcel(parcel: Parcel): DriverMonthlyDailyData {
                return DriverMonthlyDailyData(parcel)
            }

            override fun newArray(size: Int): Array<DriverMonthlyDailyData?> {
                return arrayOfNulls(size)
            }
        }
    }
}
