package com.example.xepartnerapp.util_functions_test

import com.example.xepartnerapp.common.utils.Utils.convertSecondsToMinutes
import org.junit.Assert.assertEquals
import org.junit.Test

class ConvertSecondsToMinutesTest {

    @Test
    fun `convertSecondsToMinutes handles zero seconds`() {
        // Arrange
        val seconds = 0
        val expectedMinutes = 0

        // Act
        val result = seconds.convertSecondsToMinutes()

        // Assert
        assertEquals(expectedMinutes, result)
    }

    @Test
    fun `convertSecondsToMinutes handles exactly 60 seconds`() {
        // Arrange
        val seconds = 60
        val expectedMinutes = 1

        // Act
        val result = seconds.convertSecondsToMinutes()

        // Assert
        assertEquals(expectedMinutes, result)
    }

    @Test
    fun `convertSecondsToMinutes handles exactly 120 seconds`() {
        // Arrange
        val seconds = 120
        val expectedMinutes = 2

        // Act
        val result = seconds.convertSecondsToMinutes()

        // Assert
        assertEquals(expectedMinutes, result)
    }

    @Test
    fun `convertSecondsToMinutes handles less than 60 seconds`() {
        // Arrange
        val seconds = 59
        val expectedMinutes = 1

        // Act
        val result = seconds.convertSecondsToMinutes()

        // Assert
        assertEquals(expectedMinutes, result)
    }

    @Test
    fun `convertSecondsToMinutes handles more than 60 but less than 120 seconds`() {
        // Arrange
        val seconds = 90
        val expectedMinutes = 2

        // Act
        val result = seconds.convertSecondsToMinutes()

        // Assert
        assertEquals(expectedMinutes, result)
    }

    @Test
    fun `convertSecondsToMinutes handles large number of seconds`() {
        // Arrange
        val seconds = 3600
        val expectedMinutes = 60

        // Act
        val result = seconds.convertSecondsToMinutes()

        // Assert
        assertEquals(expectedMinutes, result)
    }

    @Test
    fun `convertSecondsToMinutes handles odd seconds value`() {
        // Arrange
        val seconds = 75
        val expectedMinutes = 1

        // Act
        val result = seconds.convertSecondsToMinutes()

        // Assert
        assertEquals(expectedMinutes, result)
    }

    @Test
    fun `convertSecondsToMinutes handles 1 second`() {
        // Arrange
        val seconds = 1
        val expectedMinutes = 0

        // Act
        val result = seconds.convertSecondsToMinutes()

        // Assert
        assertEquals(expectedMinutes, result)
    }

    @Test
    fun `convertSecondsToMinutes handles 59 seconds`() {
        // Arrange
        val seconds = 59
        val expectedMinutes = 1

        // Act
        val result = seconds.convertSecondsToMinutes()

        // Assert
        assertEquals(expectedMinutes, result)
    }

    @Test
    fun `convertSecondsToMinutes handles 61 seconds`() {
        // Arrange
        val seconds = 61
        val expectedMinutes = 1

        // Act
        val result = seconds.convertSecondsToMinutes()

        // Assert
        assertEquals(expectedMinutes, result)
    }

    @Test
    fun `convertSecondsToMinutes handles 119 seconds`() {
        // Arrange
        val seconds = 119
        val expectedMinutes = 2

        // Act
        val result = seconds.convertSecondsToMinutes()

        // Assert
        assertEquals(expectedMinutes, result)
    }

    @Test
    fun `convertSecondsToMinutes handles very small negative value`() {
        // Arrange
        val seconds = -1
        val expectedMinutes = 0

        // Act
        val result = seconds.convertSecondsToMinutes()

        // Assert
        assertEquals(expectedMinutes, result)
    }

    @Test
    fun `convertSecondsToMinutes handles negative value exactly divisible by 60`() {
        // Arrange
        val seconds = -120
        val expectedMinutes = -2

        // Act
        val result = seconds.convertSecondsToMinutes()

        // Assert
        assertEquals(expectedMinutes, result)
    }

    @Test
    fun `convertSecondsToMinutes handles large positive value`() {
        // Arrange
        val seconds = 86400 // 1 day in seconds
        val expectedMinutes = 1440

        // Act
        val result = seconds.convertSecondsToMinutes()

        // Assert
        assertEquals(expectedMinutes, result)
    }

    @Test
    fun `convertSecondsToMinutes handles large negative value`() {
        // Arrange
        val seconds = -86400 // -1 day in seconds
        val expectedMinutes = -1440

        // Act
        val result = seconds.convertSecondsToMinutes()

        // Assert
        assertEquals(expectedMinutes, result)
    }

    @Test
    fun `convertSecondsToMinutes handles exactly zero`() {
        // Arrange
        val seconds = 0
        val expectedMinutes = 0

        // Act
        val result = seconds.convertSecondsToMinutes()

        // Assert
        assertEquals(expectedMinutes, result)
    }

    @Test
    fun `convertSecondsToMinutes handles edge case rounding up`() {
        // Arrange
        val seconds = 89 // Should round to 1 minute
        val expectedMinutes = 1

        // Act
        val result = seconds.convertSecondsToMinutes()

        // Assert
        assertEquals(expectedMinutes, result)
    }

    @Test
    fun `convertSecondsToMinutes handles edge case rounding down`() {
        // Arrange
        val seconds = 31 // Should round to 1 minute
        val expectedMinutes = 1

        // Act
        val result = seconds.convertSecondsToMinutes()

        // Assert
        assertEquals(expectedMinutes, result)
    }
}
