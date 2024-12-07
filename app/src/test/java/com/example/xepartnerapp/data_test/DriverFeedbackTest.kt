package com.example.xepartnerapp.data_test

import com.example.xepartnerapp.data.DriverFeedback
import org.junit.Assert.assertEquals
import org.junit.Test

class DriverFeedbackTest {

    // Test với giá trị hợp lệ cho tất cả các thuộc tính
    @Test
    fun `test constructor with valid input`() {
        // Arrange
        val driverFeedback = DriverFeedback(
            driverFeedback_ID = "DF001",
            driver_ID = "D001",
            passenger_ID = "P001",
            star = 5,
            feedback = "Great service!",
            reportDriverReason = "Speeding",
            reportDriverReasonDetail = "Driver was speeding in a residential area",
            driverFeedbackTime = "Mon Dec 06 12:00:00 GMT 2024"
        )

        // Assert: Kiểm tra giá trị các thuộc tính
        assertEquals("DF001", driverFeedback.driverFeedback_ID)
        assertEquals("D001", driverFeedback.driver_ID)
        assertEquals("P001", driverFeedback.passenger_ID)
        assertEquals(5, driverFeedback.star)
        assertEquals("Great service!", driverFeedback.feedback)
        assertEquals("Speeding", driverFeedback.reportDriverReason)
        assertEquals(
            "Driver was speeding in a residential area",
            driverFeedback.reportDriverReasonDetail
        )
        assertEquals("Mon Dec 06 12:00:00 GMT 2024", driverFeedback.driverFeedbackTime)
    }

    // Test với giá trị `star` bằng 0
    @Test
    fun `test zero star`() {
        // Arrange
        val driverFeedback = DriverFeedback(
            driverFeedback_ID = "DF002",
            star = 0
        )

        // Assert: Kiểm tra `star` bằng 0
        assertEquals(0, driverFeedback.star)
    }

    // Test với giá trị `star` ngoài phạm vi hợp lệ (0 đến 5)
    @Test
    fun `test invalid star value`() {
        // Arrange
        val driverFeedback = DriverFeedback(
            driverFeedback_ID = "DF003",
            star = 6
        )

        // Assert: Kiểm tra `star` có vượt quá giá trị hợp lệ
        assertEquals(6, driverFeedback.star)
    }

    // Test với `driverFeedbackTime` là chuỗi rỗng
    @Test
    fun `test empty driverFeedbackTime`() {
        // Arrange
        val driverFeedback = DriverFeedback(
            driverFeedback_ID = "DF004",
            driverFeedbackTime = ""
        )

        // Assert: Kiểm tra `driverFeedbackTime` là chuỗi rỗng
        assertEquals("", driverFeedback.driverFeedbackTime)
    }

    // Test với tất cả các thuộc tính là giá trị mặc định (rỗng hoặc 0)
    @Test
    fun `test all default values`() {
        // Arrange
        val driverFeedback = DriverFeedback()

        // Assert: Kiểm tra tất cả các thuộc tính có giá trị mặc định
        assertEquals("", driverFeedback.driverFeedback_ID)
        assertEquals("", driverFeedback.driver_ID)
        assertEquals("", driverFeedback.passenger_ID)
        assertEquals(0, driverFeedback.star)
        assertEquals("", driverFeedback.feedback)
        assertEquals("", driverFeedback.reportDriverReason)
        assertEquals("", driverFeedback.reportDriverReasonDetail)
        assertEquals("", driverFeedback.driverFeedbackTime)
    }

    // Test với thông tin `feedback` rỗng
    @Test
    fun `test empty feedback`() {
        // Arrange
        val driverFeedback = DriverFeedback(
            driverFeedback_ID = "DF005",
            feedback = ""
        )

        // Assert: Kiểm tra `feedback` là chuỗi rỗng
        assertEquals("", driverFeedback.feedback)
    }

    // Test với thông tin `reportDriverReasonDetail` dài
    @Test
    fun `test long reportDriverReasonDetail`() {
        // Arrange
        val driverFeedback = DriverFeedback(
            driverFeedback_ID = "DF006",
            reportDriverReasonDetail = "The driver was driving erratically, speeding through red lights and cutting off other drivers, making the ride very unsafe."
        )

        // Assert: Kiểm tra `reportDriverReasonDetail` có đúng với giá trị đã cho
        assertEquals(
            "The driver was driving erratically, speeding through red lights and cutting off other drivers, making the ride very unsafe.",
            driverFeedback.reportDriverReasonDetail
        )
    }

    // Test sao chép đối tượng và thay đổi một thuộc tính
    @Test
    fun `test copy with modified property`() {
        // Arrange
        val driverFeedback = DriverFeedback(
            driverFeedback_ID = "DF007",
            star = 4,
            feedback = "Good service."
        )

        // Act: Sao chép và thay đổi `feedback`
        val copiedDriverFeedback = driverFeedback.copy(feedback = "Excellent service!")

        // Assert: Kiểm tra `feedback` mới
        assertEquals("Excellent service!", copiedDriverFeedback.feedback)
        assertEquals(4, copiedDriverFeedback.star)
    }

    // Test với các thuộc tính `reportDriverReason` và `reportDriverReasonDetail` trống
    @Test
    fun `test empty reportDriverReason and reportDriverReasonDetail`() {
        // Arrange
        val driverFeedback = DriverFeedback(
            driverFeedback_ID = "DF010",
            reportDriverReason = "",
            reportDriverReasonDetail = ""
        )

        // Assert: Kiểm tra `reportDriverReason` và `reportDriverReasonDetail` đều là chuỗi rỗng
        assertEquals("", driverFeedback.reportDriverReason)
        assertEquals("", driverFeedback.reportDriverReasonDetail)
    }
}
