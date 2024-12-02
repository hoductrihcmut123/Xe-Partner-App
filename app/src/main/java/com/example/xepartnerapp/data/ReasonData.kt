package com.example.xepartnerapp.data

import com.google.firebase.firestore.DocumentReference

data class ReasonData(
    val reason_ID: String = "",
    val passengerCancelReason: String = "",
    val driverCancelReason: String = "",
    val driverCancelEmergency: String = "",
    val driverCancelEmergencyDetail: String = "",
    val feedbackPassengerRef: DocumentReference? = null,
    val feedbackDriverRef: DocumentReference? = null
)