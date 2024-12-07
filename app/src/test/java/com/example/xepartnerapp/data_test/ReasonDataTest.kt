package com.example.xepartnerapp.data_test

import com.example.xepartnerapp.data.ReasonData
import com.google.firebase.firestore.DocumentReference
import junit.framework.TestCase.assertNotNull
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.mockito.Mockito.mock

class ReasonDataTest {

    // Test với giá trị hợp lệ cho tất cả các thuộc tính
    @Test
    fun `test constructor with valid input`() {
        // Arrange
        val feedbackPassengerRef = mock(DocumentReference::class.java)
        val feedbackDriverRef = mock(DocumentReference::class.java)
        val reasonData = ReasonData(
            reason_ID = "R001",
            passengerCancelReason = "Passenger decided not to travel",
            driverCancelReason = "Driver's car broke down",
            driverCancelEmergency = "Family emergency",
            driverCancelEmergencyDetail = "Driver's family member was hospitalized",
            feedbackPassengerRef = feedbackPassengerRef,
            feedbackDriverRef = feedbackDriverRef
        )

        // Assert
        assertEquals("R001", reasonData.reason_ID)
        assertEquals("Passenger decided not to travel", reasonData.passengerCancelReason)
        assertEquals("Driver's car broke down", reasonData.driverCancelReason)
        assertEquals("Family emergency", reasonData.driverCancelEmergency)
        assertEquals(
            "Driver's family member was hospitalized",
            reasonData.driverCancelEmergencyDetail
        )
        assertEquals(feedbackPassengerRef, reasonData.feedbackPassengerRef)
        assertEquals(feedbackDriverRef, reasonData.feedbackDriverRef)
    }

    // Test với giá trị mặc định
    @Test
    fun `test default values`() {
        // Arrange
        val reasonData = ReasonData()

        // Assert
        assertEquals("", reasonData.reason_ID)
        assertEquals("", reasonData.passengerCancelReason)
        assertEquals("", reasonData.driverCancelReason)
        assertEquals("", reasonData.driverCancelEmergency)
        assertEquals("", reasonData.driverCancelEmergencyDetail)
        assertNull(reasonData.feedbackPassengerRef)
        assertNull(reasonData.feedbackDriverRef)
    }

    // Test với `reason_ID` rỗng
    @Test
    fun `test reason_ID is empty`() {
        // Arrange
        val reasonData = ReasonData(reason_ID = "")

        // Assert
        assertEquals("", reasonData.reason_ID)
    }

    // Test với `driverCancelEmergencyDetail` rỗng
    @Test
    fun `test driverCancelEmergencyDetail is empty`() {
        // Arrange
        val reasonData = ReasonData(driverCancelEmergencyDetail = "")

        // Assert
        assertEquals("", reasonData.driverCancelEmergencyDetail)
    }

    // Test sao chép đối tượng và thay đổi `passengerCancelReason`
    @Test
    fun `test copy with modified passengerCancelReason`() {
        // Arrange
        val reasonData = ReasonData(reason_ID = "R002", passengerCancelReason = "Initial reason")

        // Act
        val updatedReasonData = reasonData.copy(passengerCancelReason = "Updated reason")

        // Assert
        assertEquals("R002", updatedReasonData.reason_ID)
        assertEquals("Updated reason", updatedReasonData.passengerCancelReason)
    }

    // Test với `feedbackPassengerRef` và `feedbackDriverRef` là `null`
    @Test
    fun `test null references`() {
        // Arrange
        val reasonData = ReasonData(
            feedbackPassengerRef = null,
            feedbackDriverRef = null
        )

        // Assert
        assertNull(reasonData.feedbackPassengerRef)
        assertNull(reasonData.feedbackDriverRef)
    }

    // Test với `feedbackPassengerRef` và `feedbackDriverRef` không null
    @Test
    fun `test non-null references`() {
        // Arrange
        val feedbackPassengerRef = mock(DocumentReference::class.java)
        val feedbackDriverRef = mock(DocumentReference::class.java)
        val reasonData = ReasonData(
            feedbackPassengerRef = feedbackPassengerRef,
            feedbackDriverRef = feedbackDriverRef
        )

        // Assert
        assertNotNull(reasonData.feedbackPassengerRef)
        assertNotNull(reasonData.feedbackDriverRef)
    }

    // Test với các giá trị ngẫu nhiên
    @Test
    fun `test random values`() {
        // Arrange
        val reasonData = ReasonData(
            reason_ID = "RANDOM01",
            passengerCancelReason = "Random passenger reason",
            driverCancelReason = "Random driver reason",
            driverCancelEmergency = "Random emergency",
            driverCancelEmergencyDetail = "Detailed random emergency"
        )

        // Assert
        assertEquals("RANDOM01", reasonData.reason_ID)
        assertEquals("Random passenger reason", reasonData.passengerCancelReason)
        assertEquals("Random driver reason", reasonData.driverCancelReason)
        assertEquals("Random emergency", reasonData.driverCancelEmergency)
        assertEquals("Detailed random emergency", reasonData.driverCancelEmergencyDetail)
    }

    // Test với `driverCancelReason` rất dài
    @Test
    fun `test long driverCancelReason`() {
        // Arrange
        val longReason = "A".repeat(1000)
        val reasonData = ReasonData(driverCancelReason = longReason)

        // Assert
        assertEquals(longReason, reasonData.driverCancelReason)
    }

    // Test với `passengerCancelReason` rỗng
    @Test
    fun `test passengerCancelReason is empty`() {
        // Arrange
        val reasonData = ReasonData(passengerCancelReason = "")

        // Assert
        assertEquals("", reasonData.passengerCancelReason)
    }
}
