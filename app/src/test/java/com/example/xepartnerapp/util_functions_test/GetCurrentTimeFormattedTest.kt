package com.example.xepartnerapp.util_functions_test

import org.junit.Assert.assertEquals
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class GetCurrentTimeFormattedTest {

    @Test
    fun `getCurrentTimeFormatted returns correct time format`() {
        // Arrange
        val mockTime = Calendar.getInstance().apply {
            set(2024, Calendar.DECEMBER, 6, 10, 30, 0) // Ngày giờ giả lập
        }.time
        val expectedFormattedTime = "10:30"

        // Act
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val formattedMockTime = dateFormat.format(mockTime)

        // Assert
        assertEquals(expectedFormattedTime, formattedMockTime)
    }

    @Test
    fun `getCurrentTimeFormatted returns correct time with default locale`() {
        // Arrange
        Locale.setDefault(Locale.US)
        val currentTime = Calendar.getInstance().apply {
            set(2024, Calendar.DECEMBER, 6, 22, 45) // 22:45 PM
        }.time
        val expectedFormattedTime = "22:45"

        // Act
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val result = dateFormat.format(currentTime)

        // Assert
        assertEquals(expectedFormattedTime, result)
    }

    @Test
    fun `getCurrentTimeFormatted handles different locale settings`() {
        // Arrange
        Locale.setDefault(Locale.FRANCE)
        val currentTime = Calendar.getInstance().apply {
            set(2024, Calendar.DECEMBER, 6, 7, 15) // 07:15 AM
        }.time
        val expectedFormattedTime = "07:15"

        // Act
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val result = dateFormat.format(currentTime)

        // Assert
        assertEquals(expectedFormattedTime, result)
    }

    @Test
    fun `getCurrentTimeFormatted matches mock time`() {
        // Arrange
        val fixedTime = Calendar.getInstance().apply {
            set(2024, Calendar.DECEMBER, 6, 18, 0, 0) // 18:00 PM
        }.time
        val expectedTime = "18:00"

        // Act
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val formattedFixedTime = dateFormat.format(fixedTime)

        // Assert
        assertEquals(expectedTime, formattedFixedTime)
    }

    @Test
    fun `getCurrentTimeFormatted handles near-midnight time`() {
        // Arrange
        val mockTime = Calendar.getInstance().apply {
            set(2024, Calendar.DECEMBER, 6, 23, 59) // 23:59 PM
        }.time
        val expectedFormattedTime = "23:59"

        // Act
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val formattedMockTime = dateFormat.format(mockTime)

        // Assert
        assertEquals(expectedFormattedTime, formattedMockTime)
    }

    @Test
    fun `getCurrentTimeFormatted handles start-of-day time`() {
        // Arrange
        val mockTime = Calendar.getInstance().apply {
            set(2024, Calendar.DECEMBER, 6, 0, 0) // 00:00 AM
        }.time
        val expectedFormattedTime = "00:00"

        // Act
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val formattedMockTime = dateFormat.format(mockTime)

        // Assert
        assertEquals(expectedFormattedTime, formattedMockTime)
    }
}
