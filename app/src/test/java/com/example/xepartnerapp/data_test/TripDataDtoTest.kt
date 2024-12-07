package com.example.xepartnerapp.data_test

import com.example.xepartnerapp.data.ReasonDataDto
import com.example.xepartnerapp.data.TripDataDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class TripDataDtoTest {

    @Test
    fun `test constructor with valid values`() {
        // Arrange
        val reasonData = ReasonDataDto(
            reason_ID = "R001",
            passengerCancelReason = "Change of plans",
            driverCancelReason = "Vehicle issue"
        )
        val tripData = TripDataDto(
            trip_ID = "T001",
            originLat = 10.12345,
            originLng = 20.54321,
            originAddress = "123 Origin St",
            destinationLat = 30.54321,
            destinationLng = 40.12345,
            destinationAddress = "456 Destination Ave",
            vehicleType = "Car",
            paymentType = "CreditCard",
            price = 250000.0,
            distance = 15.0,
            duration = 30,
            bookingTime = "2024-12-05T10:00:00",
            startTime = "2024-12-05T10:15:00",
            endTime = "2024-12-05T10:45:00",
            status = "Completed",
            passenger_ID = "P001",
            driver_ID = "D001",
            reason_ID = "R001",
            reason = reasonData
        )

        // Assert
        assertEquals("T001", tripData.trip_ID)
        tripData.originLat?.let { assertEquals(10.12345, it, 0.0) }
        tripData.originLng?.let { assertEquals(20.54321, it, 0.0) }
        assertEquals("123 Origin St", tripData.originAddress)
        tripData.destinationLat?.let { assertEquals(30.54321, it, 0.0) }
        tripData.destinationLng?.let { assertEquals(40.12345, it, 0.0) }
        assertEquals("456 Destination Ave", tripData.destinationAddress)
        assertEquals("Car", tripData.vehicleType)
        assertEquals("CreditCard", tripData.paymentType)
        tripData.price?.let { assertEquals(250000.0, it, 0.0) }
        tripData.distance?.let { assertEquals(15.0, it, 0.0) }
        assertEquals(30, tripData.duration)
        assertEquals("2024-12-05T10:00:00", tripData.bookingTime)
        assertEquals("2024-12-05T10:15:00", tripData.startTime)
        assertEquals("2024-12-05T10:45:00", tripData.endTime)
        assertEquals("Completed", tripData.status)
        assertEquals("P001", tripData.passenger_ID)
        assertEquals("D001", tripData.driver_ID)
        assertEquals("R001", tripData.reason_ID)
        assertEquals(reasonData, tripData.reason)
    }

    @Test
    fun `test constructor with default values`() {
        // Arrange
        val tripData = TripDataDto()

        // Assert
        assertNull(tripData.trip_ID)
        assertNull(tripData.originLat)
        assertNull(tripData.originLng)
        assertNull(tripData.originAddress)
        assertNull(tripData.destinationLat)
        assertNull(tripData.destinationLng)
        assertNull(tripData.destinationAddress)
        assertNull(tripData.vehicleType)
        assertNull(tripData.paymentType)
        assertNull(tripData.price)
        assertNull(tripData.distance)
        assertNull(tripData.duration)
        assertNull(tripData.bookingTime)
        assertNull(tripData.startTime)
        assertNull(tripData.endTime)
        assertNull(tripData.status)
        assertNull(tripData.passenger_ID)
        assertNull(tripData.driver_ID)
        assertNull(tripData.reason_ID)
        assertNull(tripData.reason)
    }

    @Test
    fun `test copy with modified values`() {
        // Arrange
        val tripData = TripDataDto(trip_ID = "T001", status = "Ongoing")

        // Act
        val updatedTripData = tripData.copy(status = "Cancelled")

        // Assert
        assertEquals("T001", updatedTripData.trip_ID)
        assertEquals("Cancelled", updatedTripData.status)
        assertNull(updatedTripData.reason)
    }

    @Test
    fun `test distance and duration defaults`() {
        // Arrange
        val tripData = TripDataDto(distance = 0.0, duration = 0)

        // Assert
        tripData.distance?.let { assertEquals(0.0, it, 0.0) }
        assertEquals(0, tripData.duration)
    }

    @Test
    fun `test reason is null when not provided`() {
        // Arrange
        val tripData = TripDataDto(reason_ID = "R001")

        // Assert
        assertEquals("R001", tripData.reason_ID)
        assertNull(tripData.reason)
    }

    @Test
    fun `test vehicleType with invalid data`() {
        // Arrange
        val tripData = TripDataDto(vehicleType = "InvalidType")

        // Assert
        assertEquals("InvalidType", tripData.vehicleType)
    }

    @Test
    fun `test reasonData nested validation`() {
        // Arrange
        val reasonData = ReasonDataDto(
            reason_ID = "R002",
            passengerCancelReason = "Unexpected delay",
            driverCancelReason = "Health emergency"
        )
        val tripData = TripDataDto(reason = reasonData)

        // Assert
        assertEquals("R002", tripData.reason?.reason_ID)
        assertEquals("Unexpected delay", tripData.reason?.passengerCancelReason)
        assertEquals("Health emergency", tripData.reason?.driverCancelReason)
    }
}
