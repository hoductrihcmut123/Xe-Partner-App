package com.example.xepartnerapp.data_test

import com.example.xepartnerapp.data.DriverSupport
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class DriverSupportTest {

    // Test với giá trị hợp lệ cho tất cả các thuộc tính
    @Test
    fun `test constructor with valid input`() {
        // Arrange
        val driverSupport = DriverSupport(
            driverSupport_ID = "DS001",
            driver_ID = "D001",
            supportContent = "Need help with vehicle maintenance.",
            supportImgUrl = "https://example.com/support-image.jpg",
            createTime = "2024-12-06T14:00:00"
        )

        // Assert: Kiểm tra giá trị các thuộc tính
        assertEquals("DS001", driverSupport.driverSupport_ID)
        assertEquals("D001", driverSupport.driver_ID)
        assertEquals("Need help with vehicle maintenance.", driverSupport.supportContent)
        assertEquals("https://example.com/support-image.jpg", driverSupport.supportImgUrl)
        assertEquals("2024-12-06T14:00:00", driverSupport.createTime)
    }

    // Test với `supportImgUrl` là null
    @Test
    fun `test supportImgUrl is null`() {
        // Arrange
        val driverSupport = DriverSupport(
            driverSupport_ID = "DS002",
            supportImgUrl = null
        )

        // Assert: Kiểm tra `supportImgUrl` là null
        assertNull(driverSupport.supportImgUrl)
    }

    // Test với `supportContent` rỗng
    @Test
    fun `test supportContent is empty`() {
        // Arrange
        val driverSupport = DriverSupport(
            driverSupport_ID = "DS003",
            supportContent = ""
        )

        // Assert: Kiểm tra `supportContent` là chuỗi rỗng
        assertEquals("", driverSupport.supportContent)
    }

    // Test với `createTime` rỗng
    @Test
    fun `test createTime is empty`() {
        // Arrange
        val driverSupport = DriverSupport(
            driverSupport_ID = "DS004",
            createTime = ""
        )

        // Assert: Kiểm tra `createTime` là chuỗi rỗng
        assertEquals("", driverSupport.createTime)
    }

    // Test với tất cả các thuộc tính là giá trị mặc định
    @Test
    fun `test all default values`() {
        // Arrange
        val driverSupport = DriverSupport()

        // Assert: Kiểm tra tất cả các thuộc tính có giá trị mặc định
        assertEquals("", driverSupport.driverSupport_ID)
        assertEquals("", driverSupport.driver_ID)
        assertEquals("", driverSupport.supportContent)
        assertNull(driverSupport.supportImgUrl)
        assertEquals("", driverSupport.createTime)
    }

    // Test sao chép đối tượng và thay đổi `supportContent`
    @Test
    fun `test copy with modified supportContent`() {
        // Arrange
        val driverSupport = DriverSupport(
            driverSupport_ID = "DS005",
            supportContent = "Initial content."
        )

        // Act: Sao chép và thay đổi `supportContent`
        val updatedDriverSupport = driverSupport.copy(supportContent = "Updated content.")

        // Assert: Kiểm tra giá trị `supportContent` mới
        assertEquals("Updated content.", updatedDriverSupport.supportContent)
        assertEquals("DS005", updatedDriverSupport.driverSupport_ID)
    }

    // Test với `supportContent` rất dài
    @Test
    fun `test long supportContent`() {
        // Arrange
        val longContent = "A".repeat(1000)
        val driverSupport = DriverSupport(
            driverSupport_ID = "DS006",
            supportContent = longContent
        )

        // Assert: Kiểm tra `supportContent` là giá trị rất dài
        assertEquals(longContent, driverSupport.supportContent)
    }

    // Test với `supportImgUrl` là một URL hợp lệ
    @Test
    fun `test valid supportImgUrl`() {
        // Arrange
        val validUrl = "https://example.com/valid-image.jpg"
        val driverSupport = DriverSupport(
            driverSupport_ID = "DS007",
            supportImgUrl = validUrl
        )

        // Assert: Kiểm tra `supportImgUrl` có giá trị hợp lệ
        assertEquals(validUrl, driverSupport.supportImgUrl)
    }

    // Test với `createTime` có định dạng ISO-8601 hợp lệ
    @Test
    fun `test valid createTime`() {
        // Arrange
        val validTime = "2024-12-06T15:00:00"
        val driverSupport = DriverSupport(
            driverSupport_ID = "DS008",
            createTime = validTime
        )

        // Assert: Kiểm tra `createTime` là chuỗi hợp lệ
        assertEquals(validTime, driverSupport.createTime)
    }

    // Test với `createTime` không hợp lệ
    @Test
    fun `test invalid createTime`() {
        // Arrange
        val invalidTime = "Invalid time format"
        val driverSupport = DriverSupport(
            driverSupport_ID = "DS009",
            createTime = invalidTime
        )

        // Assert: Kiểm tra `createTime` có giá trị không hợp lệ
        assertEquals(invalidTime, driverSupport.createTime)
    }

    // Test với các giá trị ngẫu nhiên
    @Test
    fun `test random values`() {
        // Arrange
        val driverSupport = DriverSupport(
            driverSupport_ID = "RANDOM01",
            driver_ID = "DRIVER01",
            supportContent = "Random content.",
            supportImgUrl = "https://random.url/random-image.jpg",
            createTime = "2024-12-07T12:34:56"
        )

        // Assert: Kiểm tra giá trị ngẫu nhiên
        assertEquals("RANDOM01", driverSupport.driverSupport_ID)
        assertEquals("DRIVER01", driverSupport.driver_ID)
        assertEquals("Random content.", driverSupport.supportContent)
        assertEquals("https://random.url/random-image.jpg", driverSupport.supportImgUrl)
        assertEquals("2024-12-07T12:34:56", driverSupport.createTime)
    }
}
