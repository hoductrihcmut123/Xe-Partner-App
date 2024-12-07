package com.example.xepartnerapp.data_test

import com.example.xepartnerapp.data.DriverData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class DriverDataTest {

    // Test với giá trị hợp lệ cho tất cả các thuộc tính
    @Test
    fun `test constructor with valid input`() {
        // Arrange
        val driverData = DriverData(
            driver_ID = "D001",
            firstname = "John",
            lastname = "Doe",
            email = "john.doe@example.com",
            gender = true,
            mobile_No = "0987654321",
            point = 150,
            password = "securePass123",
            avatar_Link = "http://example.com/avatar.jpg",
            vehicle_Link = "http://example.com/vehicle.jpg",
            card_ID = "123456",
            license = "A1234567",
            classify = "Truck",
            machine_Number = "M123456",
            license_Plate = "ABC1234",
            place_Manufacture = "Japan",
            vehicle_Color = "Red",
            vehicle_Line = "Sedan",
            seat_Num = 4,
            year_Manufacture = 2020,
            vehicle_Brand = "Toyota",
            ready = true,
            totalStar = 5,
            rateStarNum = 10,
            completeTripNum = 50,
            totalDistance = 1000,
            momoPhone = "0988888888"
        )

        // Assert: Kiểm tra giá trị các thuộc tính
        assertEquals("D001", driverData.driver_ID)
        assertEquals("John", driverData.firstname)
        assertEquals("Doe", driverData.lastname)
        assertEquals("john.doe@example.com", driverData.email)
        assertEquals(true, driverData.gender)
        assertEquals("0987654321", driverData.mobile_No)
        assertEquals(150, driverData.point)
        assertEquals("securePass123", driverData.password)
        assertEquals("http://example.com/avatar.jpg", driverData.avatar_Link)
        assertEquals("http://example.com/vehicle.jpg", driverData.vehicle_Link)
        assertEquals("123456", driverData.card_ID)
        assertEquals("A1234567", driverData.license)
        assertEquals("Truck", driverData.classify)
        assertEquals("M123456", driverData.machine_Number)
        assertEquals("ABC1234", driverData.license_Plate)
        assertEquals("Japan", driverData.place_Manufacture)
        assertEquals("Red", driverData.vehicle_Color)
        assertEquals("Sedan", driverData.vehicle_Line)
        assertEquals(4, driverData.seat_Num)
        assertEquals(2020, driverData.year_Manufacture)
        assertEquals("Toyota", driverData.vehicle_Brand)
        assertEquals(true, driverData.ready)
        assertEquals(5, driverData.totalStar)
        assertEquals(10, driverData.rateStarNum)
        assertEquals(50, driverData.completeTripNum)
        assertEquals(1000, driverData.totalDistance)
        assertEquals("0988888888", driverData.momoPhone)
    }

    // Test với thông tin `mobile_No` bị thiếu
    @Test
    fun `test missing mobile_No`() {
        // Arrange
        val driverData = DriverData(
            driver_ID = "D002",
            firstname = "Jane",
            lastname = "Smith",
            mobile_No = null
        )

        // Assert: Kiểm tra `mobile_No` có phải là null
        assertNull(driverData.mobile_No)
    }

    // Test với thông tin `vehicle_Color` bị thiếu
    @Test
    fun `test missing vehicle_Color`() {
        // Arrange
        val driverData = DriverData(
            driver_ID = "D003",
            firstname = "Alice",
            lastname = "Johnson",
            vehicle_Color = null
        )

        // Assert: Kiểm tra `vehicle_Color` có phải là null
        assertNull(driverData.vehicle_Color)
    }

    // Test với điểm = 0
    @Test
    fun `test zero points`() {
        // Arrange
        val driverData = DriverData(
            driver_ID = "D004",
            firstname = "Bob",
            lastname = "Williams",
            point = 0
        )

        // Assert: Kiểm tra điểm là 0
        assertEquals(0, driverData.point)
    }

    // Test với giá trị `null` cho `ready`
    @Test
    fun `test null ready`() {
        // Arrange
        val driverData = DriverData(
            driver_ID = "D005",
            firstname = "Charlie",
            lastname = "Brown",
            ready = null
        )

        // Assert: Kiểm tra giá trị `ready` là null
        assertNull(driverData.ready)
    }

    // Test với giá trị `null` cho `totalStar`
    @Test
    fun `test null totalStar`() {
        // Arrange
        val driverData = DriverData(
            driver_ID = "D006",
            firstname = "David",
            lastname = "Lee",
            totalStar = null
        )

        // Assert: Kiểm tra `totalStar` là null
        assertNull(driverData.totalStar)
    }

    // Test với việc sao chép đối tượng và thay đổi một thuộc tính
    @Test
    fun `test copy with modified property`() {
        // Arrange
        val driverData = DriverData(
            driver_ID = "D007",
            firstname = "Eve",
            lastname = "Davis",
            email = "eve.davis@example.com"
        )

        // Act: Sao chép và thay đổi email
        val copiedDriverData = driverData.copy(email = "new.eve@example.com")

        // Assert: Kiểm tra email mới
        assertEquals("new.eve@example.com", copiedDriverData.email)
        assertEquals("Eve", copiedDriverData.firstname)
        assertEquals("Davis", copiedDriverData.lastname)
    }

    // Test với `vehicle_Brand` không hợp lệ
    @Test
    fun `test invalid vehicle_Brand`() {
        // Arrange
        val driverData = DriverData(
            driver_ID = "D008",
            vehicle_Brand = "InvalidBrand123"
        )

        // Assert: Kiểm tra giá trị `vehicle_Brand` có hợp lệ
        assertEquals("InvalidBrand123", driverData.vehicle_Brand)
    }

    // Test với tất cả các thuộc tính là null
    @Test
    fun `test all null values`() {
        // Arrange
        val driverData = DriverData(
            driver_ID = null,
            firstname = null,
            lastname = null,
            email = null,
            gender = null,
            mobile_No = null,
            point = null,
            password = null,
            avatar_Link = null,
            vehicle_Link = null,
            card_ID = null,
            license = null,
            classify = null,
            machine_Number = null,
            license_Plate = null,
            place_Manufacture = null,
            vehicle_Color = null,
            vehicle_Line = null,
            seat_Num = null,
            year_Manufacture = null,
            vehicle_Brand = null,
            ready = null,
            totalStar = null,
            rateStarNum = null,
            completeTripNum = null,
            totalDistance = null,
            momoPhone = null
        )

        // Assert: Kiểm tra tất cả các thuộc tính là null
        assertNull(driverData.driver_ID)
        assertNull(driverData.firstname)
        assertNull(driverData.lastname)
        assertNull(driverData.email)
        assertNull(driverData.gender)
        assertNull(driverData.mobile_No)
        assertNull(driverData.point)
        assertNull(driverData.password)
        assertNull(driverData.avatar_Link)
        assertNull(driverData.vehicle_Link)
        assertNull(driverData.card_ID)
        assertNull(driverData.license)
        assertNull(driverData.classify)
        assertNull(driverData.machine_Number)
        assertNull(driverData.license_Plate)
        assertNull(driverData.place_Manufacture)
        assertNull(driverData.vehicle_Color)
        assertNull(driverData.vehicle_Line)
        assertNull(driverData.seat_Num)
        assertNull(driverData.year_Manufacture)
        assertNull(driverData.vehicle_Brand)
        assertNull(driverData.ready)
        assertNull(driverData.totalStar)
        assertNull(driverData.rateStarNum)
        assertNull(driverData.completeTripNum)
        assertNull(driverData.totalDistance)
        assertNull(driverData.momoPhone)
    }
}
