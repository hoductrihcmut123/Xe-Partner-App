package com.example.xepartnerapp.data_test

import com.example.xepartnerapp.data.PassengerFeedback
import org.junit.Assert.assertEquals
import org.junit.Test

class PassengerFeedbackTest {

    // Test với giá trị hợp lệ cho tất cả các thuộc tính
    @Test
    fun `test constructor with valid input`() {
        // Arrange
        val passengerFeedback = PassengerFeedback(
            passengerFeedback_ID = "PF001",
            driver_ID = "D001",
            passenger_ID = "P001",
            reportPassengerReason = "Late arrival",
            reportPassengerReasonDetail = "Passenger was late by 15 minutes",
            passengerFeedbackTime = "2024-12-06T14:00:00"
        )

        // Assert: Kiểm tra giá trị các thuộc tính
        assertEquals("PF001", passengerFeedback.passengerFeedback_ID)
        assertEquals("D001", passengerFeedback.driver_ID)
        assertEquals("P001", passengerFeedback.passenger_ID)
        assertEquals("Late arrival", passengerFeedback.reportPassengerReason)
        assertEquals(
            "Passenger was late by 15 minutes",
            passengerFeedback.reportPassengerReasonDetail
        )
        assertEquals("2024-12-06T14:00:00", passengerFeedback.passengerFeedbackTime)
    }

    // Test với `reportPassengerReason` rỗng
    @Test
    fun `test reportPassengerReason is empty`() {
        // Arrange
        val passengerFeedback = PassengerFeedback(
            passengerFeedback_ID = "PF002",
            reportPassengerReason = ""
        )

        // Assert: Kiểm tra `reportPassengerReason` là chuỗi rỗng
        assertEquals("", passengerFeedback.reportPassengerReason)
    }

    // Test với `reportPassengerReasonDetail` rỗng
    @Test
    fun `test reportPassengerReasonDetail is empty`() {
        // Arrange
        val passengerFeedback = PassengerFeedback(
            passengerFeedback_ID = "PF003",
            reportPassengerReasonDetail = ""
        )

        // Assert: Kiểm tra `reportPassengerReasonDetail` là chuỗi rỗng
        assertEquals("", passengerFeedback.reportPassengerReasonDetail)
    }

    // Test với `passengerFeedbackTime` rỗng
    @Test
    fun `test passengerFeedbackTime is empty`() {
        // Arrange
        val passengerFeedback = PassengerFeedback(
            passengerFeedback_ID = "PF004",
            passengerFeedbackTime = ""
        )

        // Assert: Kiểm tra `passengerFeedbackTime` là chuỗi rỗng
        assertEquals("", passengerFeedback.passengerFeedbackTime)
    }

    // Test với tất cả các thuộc tính là giá trị mặc định
    @Test
    fun `test all default values`() {
        // Arrange
        val passengerFeedback = PassengerFeedback()

        // Assert: Kiểm tra tất cả các thuộc tính có giá trị mặc định
        assertEquals("", passengerFeedback.passengerFeedback_ID)
        assertEquals("", passengerFeedback.driver_ID)
        assertEquals("", passengerFeedback.passenger_ID)
        assertEquals("", passengerFeedback.reportPassengerReason)
        assertEquals("", passengerFeedback.reportPassengerReasonDetail)
        assertEquals("", passengerFeedback.passengerFeedbackTime)
    }

    // Test sao chép đối tượng và thay đổi `reportPassengerReason`
    @Test
    fun `test copy with modified reportPassengerReason`() {
        // Arrange
        val passengerFeedback = PassengerFeedback(
            passengerFeedback_ID = "PF005",
            reportPassengerReason = "Initial reason"
        )

        // Act: Sao chép và thay đổi `reportPassengerReason`
        val updatedPassengerFeedback =
            passengerFeedback.copy(reportPassengerReason = "Updated reason")

        // Assert: Kiểm tra giá trị `reportPassengerReason` mới
        assertEquals("Updated reason", updatedPassengerFeedback.reportPassengerReason)
        assertEquals("PF005", updatedPassengerFeedback.passengerFeedback_ID)
    }

    // Test với `reportPassengerReasonDetail` rất dài
    @Test
    fun `test long reportPassengerReasonDetail`() {
        // Arrange
        val longDetail = "A".repeat(1000)
        val passengerFeedback = PassengerFeedback(
            passengerFeedback_ID = "PF006",
            reportPassengerReasonDetail = longDetail
        )

        // Assert: Kiểm tra `reportPassengerReasonDetail` là giá trị rất dài
        assertEquals(longDetail, passengerFeedback.reportPassengerReasonDetail)
    }

    // Test với `passengerFeedbackTime` có định dạng hợp lệ
    @Test
    fun `test valid passengerFeedbackTime`() {
        // Arrange
        val validTime = "2024-12-06T15:00:00"
        val passengerFeedback = PassengerFeedback(
            passengerFeedback_ID = "PF007",
            passengerFeedbackTime = validTime
        )

        // Assert: Kiểm tra `passengerFeedbackTime` là chuỗi hợp lệ
        assertEquals(validTime, passengerFeedback.passengerFeedbackTime)
    }

    // Test với `passengerFeedbackTime` không hợp lệ
    @Test
    fun `test invalid passengerFeedbackTime`() {
        // Arrange
        val invalidTime = "Invalid time format"
        val passengerFeedback = PassengerFeedback(
            passengerFeedback_ID = "PF008",
            passengerFeedbackTime = invalidTime
        )

        // Assert: Kiểm tra `passengerFeedbackTime` có giá trị không hợp lệ
        assertEquals(invalidTime, passengerFeedback.passengerFeedbackTime)
    }

    // Test với các giá trị ngẫu nhiên
    @Test
    fun `test random values`() {
        // Arrange
        val passengerFeedback = PassengerFeedback(
            passengerFeedback_ID = "RANDOM01",
            driver_ID = "DRIVER01",
            passenger_ID = "PASSENGER01",
            reportPassengerReason = "Random reason.",
            reportPassengerReasonDetail = "Detailed random reason.",
            passengerFeedbackTime = "2024-12-07T12:34:56"
        )

        // Assert: Kiểm tra giá trị ngẫu nhiên
        assertEquals("RANDOM01", passengerFeedback.passengerFeedback_ID)
        assertEquals("DRIVER01", passengerFeedback.driver_ID)
        assertEquals("PASSENGER01", passengerFeedback.passenger_ID)
        assertEquals("Random reason.", passengerFeedback.reportPassengerReason)
        assertEquals("Detailed random reason.", passengerFeedback.reportPassengerReasonDetail)
        assertEquals("2024-12-07T12:34:56", passengerFeedback.passengerFeedbackTime)
    }
}
