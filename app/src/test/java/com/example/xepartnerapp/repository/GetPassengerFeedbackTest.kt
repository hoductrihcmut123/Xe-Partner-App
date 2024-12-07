package com.example.xepartnerapp.repository

import com.example.xepartnerapp.data.PassengerFeedback
import org.junit.Assert.assertEquals
import org.junit.Test

class GetPassengerFeedbackTest {

    @Test
    fun `test passenger feedback data`() {
        // Arrange
        val mockPassengerFeedback = PassengerFeedbackMock.getPassengerFeedbackMockData()

        // Act: Bạn có thể gọi các hàm logic xử lý dữ liệu này
        val feedbackID = mockPassengerFeedback.passengerFeedback_ID
        val driverID = mockPassengerFeedback.driver_ID
        val passengerID = mockPassengerFeedback.passenger_ID
        val reportReason = mockPassengerFeedback.reportPassengerReason

        // Assert: Kiểm tra các giá trị
        assertEquals("feedback123", feedbackID)
        assertEquals("driver123", driverID)
        assertEquals("passenger123", passengerID)
        assertEquals("Unruly behavior", reportReason)
    }

    @Test
    fun `test empty passenger feedback data`() {
        // Arrange
        val emptyPassengerFeedback = PassengerFeedback()

        // Act: Kiểm tra dữ liệu trống
        val feedbackID = emptyPassengerFeedback.passengerFeedback_ID
        val driverID = emptyPassengerFeedback.driver_ID
        val passengerID = emptyPassengerFeedback.passenger_ID

        // Assert: Kiểm tra các giá trị mặc định
        assertEquals("", feedbackID)
        assertEquals("", driverID)
        assertEquals("", passengerID)
    }
}
