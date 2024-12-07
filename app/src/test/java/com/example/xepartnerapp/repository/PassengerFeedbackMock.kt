package com.example.xepartnerapp.repository

import com.example.xepartnerapp.data.PassengerFeedback

object PassengerFeedbackMock {

    // Dữ liệu mock cho một PassengerFeedback
    fun getPassengerFeedbackMockData(): PassengerFeedback {
        return PassengerFeedback(
            passengerFeedback_ID = "feedback123",
            driver_ID = "driver123",
            passenger_ID = "passenger123",
            reportPassengerReason = "Unruly behavior",
            reportPassengerReasonDetail = "The passenger was rude and uncooperative during the trip",
            passengerFeedbackTime = "2024-12-06 14:30:00"
        )
    }

    // Tạo mock dữ liệu cho PassengerFeedback
    fun getPassengerFeedbackDocument(passengerFeedbackID: String): PassengerFeedback {
        return getPassengerFeedbackMockData()
    }
}
