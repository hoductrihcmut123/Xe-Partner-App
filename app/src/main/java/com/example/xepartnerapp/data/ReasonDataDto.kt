package com.example.xepartnerapp.data

import com.google.firebase.firestore.DocumentReference
import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.FirebaseFirestore

data class ReasonDataDto(
    val reason_ID: String = "",
    val passengerCancelReason: String = "",
    val driverCancelReason: String = "",
    val driverCancelEmergency: String = "",
    val driverCancelEmergencyDetail: String = "",
    val feedbackPassengerRef: String? = null,
    val feedbackDriverRef: String? = null
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(reason_ID)
        parcel.writeString(passengerCancelReason)
        parcel.writeString(driverCancelReason)
        parcel.writeString(driverCancelEmergency)
        parcel.writeString(driverCancelEmergencyDetail)
        parcel.writeString(feedbackPassengerRef)
        parcel.writeString(feedbackDriverRef)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField
        val CREATOR = object : Parcelable.Creator<ReasonDataDto> {
            override fun createFromParcel(parcel: Parcel): ReasonDataDto {
                return ReasonDataDto(parcel)
            }

            override fun newArray(size: Int): Array<ReasonDataDto?> {
                return arrayOfNulls(size)
            }
        }
    }

    fun toFeedbackPassengerRef(): DocumentReference? {
        return feedbackPassengerRef?.let { FirebaseFirestore.getInstance().document(it) }
    }

    fun toFeedbackDriverRef(): DocumentReference? {
        return feedbackDriverRef?.let { FirebaseFirestore.getInstance().document(it) }
    }
}
