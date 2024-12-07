package com.example.xepartnerapp.data_test

import com.example.xepartnerapp.data.ReportIssueData
import org.junit.Assert.assertEquals
import org.junit.Test

class ReportIssueDataTest {

    // Test với giá trị hợp lệ cho tất cả các thuộc tính
    @Test
    fun `test constructor with valid input`() {
        // Arrange
        val reportIssueData = ReportIssueData(
            reportIssue_ID = "RI001",
            trip_ID = "T001",
            driver_ID = "D001",
            passenger_ID = "P001",
            reportIssue = "Late arrival",
            reportIssueDetail = "Driver arrived 30 minutes late",
            reportTime = "2024-12-01 08:30:00"
        )

        // Assert
        assertEquals("RI001", reportIssueData.reportIssue_ID)
        assertEquals("T001", reportIssueData.trip_ID)
        assertEquals("D001", reportIssueData.driver_ID)
        assertEquals("P001", reportIssueData.passenger_ID)
        assertEquals("Late arrival", reportIssueData.reportIssue)
        assertEquals("Driver arrived 30 minutes late", reportIssueData.reportIssueDetail)
        assertEquals("2024-12-01 08:30:00", reportIssueData.reportTime)
    }

    // Test với giá trị mặc định
    @Test
    fun `test default values`() {
        // Arrange
        val reportIssueData = ReportIssueData()

        // Assert
        assertEquals("", reportIssueData.reportIssue_ID)
        assertEquals("", reportIssueData.trip_ID)
        assertEquals("", reportIssueData.driver_ID)
        assertEquals("", reportIssueData.passenger_ID)
        assertEquals("", reportIssueData.reportIssue)
        assertEquals("", reportIssueData.reportIssueDetail)
        assertEquals("", reportIssueData.reportTime)
    }

    // Test sao chép đối tượng và thay đổi `reportIssue`
    @Test
    fun `test copy with modified reportIssue`() {
        // Arrange
        val reportIssueData = ReportIssueData(reportIssue = "Initial issue")

        // Act
        val updatedReportIssueData = reportIssueData.copy(reportIssue = "Updated issue")

        // Assert
        assertEquals("Updated issue", updatedReportIssueData.reportIssue)
        assertEquals("", updatedReportIssueData.reportIssue_ID)
    }

    // Test với `reportIssueDetail` rỗng
    @Test
    fun `test reportIssueDetail is empty`() {
        // Arrange
        val reportIssueData = ReportIssueData(reportIssueDetail = "")

        // Assert
        assertEquals("", reportIssueData.reportIssueDetail)
    }

    // Test với giá trị ngẫu nhiên
    @Test
    fun `test random values`() {
        // Arrange
        val reportIssueData = ReportIssueData(
            reportIssue_ID = "RANDOM01",
            trip_ID = "T999",
            driver_ID = "D555",
            passenger_ID = "P666",
            reportIssue = "Aggressive driving",
            reportIssueDetail = "Driver was speeding excessively",
            reportTime = "2024-12-02 15:45:00"
        )

        // Assert
        assertEquals("RANDOM01", reportIssueData.reportIssue_ID)
        assertEquals("T999", reportIssueData.trip_ID)
        assertEquals("D555", reportIssueData.driver_ID)
        assertEquals("P666", reportIssueData.passenger_ID)
        assertEquals("Aggressive driving", reportIssueData.reportIssue)
        assertEquals("Driver was speeding excessively", reportIssueData.reportIssueDetail)
        assertEquals("2024-12-02 15:45:00", reportIssueData.reportTime)
    }

    // Test với `reportTime` không đúng định dạng
    @Test
    fun `test invalid reportTime format`() {
        // Arrange
        val reportIssueData = ReportIssueData(reportTime = "Invalid Time Format")

        // Assert
        assertEquals("Invalid Time Format", reportIssueData.reportTime)
    }

    // Test với `reportIssue_ID` là chuỗi rỗng
    @Test
    fun `test empty reportIssue_ID`() {
        // Arrange
        val reportIssueData = ReportIssueData(reportIssue_ID = "")

        // Assert
        assertEquals("", reportIssueData.reportIssue_ID)
    }

    // Test với chuỗi rất dài cho `reportIssueDetail`
    @Test
    fun `test long reportIssueDetail`() {
        // Arrange
        val longDetail = "A".repeat(1000)
        val reportIssueData = ReportIssueData(reportIssueDetail = longDetail)

        // Assert
        assertEquals(longDetail, reportIssueData.reportIssueDetail)
    }

    // Test với `trip_ID` là chuỗi rỗng
    @Test
    fun `test empty trip_ID`() {
        // Arrange
        val reportIssueData = ReportIssueData(trip_ID = "")

        // Assert
        assertEquals("", reportIssueData.trip_ID)
    }
}
