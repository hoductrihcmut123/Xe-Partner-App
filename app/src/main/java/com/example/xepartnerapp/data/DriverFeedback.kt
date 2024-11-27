package com.example.xepartnerapp.data

data class DriverFeedback(
    val driverFeedback_ID: String = "",
    val driver_ID: String = "",
    val passenger_ID: String = "",
    val star: Int = 0,
    val feedback: String = "",
    val reportDriverReason: String = "",
    val reportDriverReasonDetail: String = "",
    val driverFeedbackTime: String = ""
)
