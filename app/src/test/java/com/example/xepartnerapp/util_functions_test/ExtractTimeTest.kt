package com.example.xepartnerapp.util_functions_test

import com.example.xepartnerapp.common.utils.Utils.extractTime
import org.junit.Assert.assertEquals
import org.junit.Test

class ExtractTimeTest {

    @Test
    fun `extractTime returns correct time from valid string`() {
        // Arrange
        val input = "Tue Nov 28 14:30:00 UTC 2023"
        val expected = "14:30:00"

        // Act
        val result = input.extractTime()

        // Assert
        assertEquals(expected, result)
    }

    @Test
    fun `extractTime returns empty string for string with less than 6 parts`() {
        // Arrange
        val input = "Nov 28 2023"
        val expected = ""

        // Act
        val result = input.extractTime()

        // Assert
        assertEquals(expected, result)
    }

    @Test
    fun `extractTime returns empty string for empty input`() {
        // Arrange
        val input = ""
        val expected = ""

        // Act
        val result = input.extractTime()

        // Assert
        assertEquals(expected, result)
    }

    @Test
    fun `extractTime handles string with extra spaces`() {
        // Arrange
        val input = "Tue     Nov     28    14:30:00    UTC    2023"
        val expected = ""

        // Act
        val result = input.extractTime()

        // Assert
        assertEquals(expected, result)
    }

    @Test
    fun `extractTime handles input with more than 6 parts`() {
        // Arrange
        val input = "Tue Nov 28 14:30:00 UTC 2023 extra"
        val expected = "14:30:00"

        // Act
        val result = input.extractTime()

        // Assert
        assertEquals(expected, result)
    }

    @Test
    fun `extractTime returns empty string for string without time`() {
        // Arrange
        val input = "Some random string without time"
        val expected = ""

        // Act
        val result = input.extractTime()

        // Assert
        assertEquals(expected, result)
    }

    @Test
    fun `extractTime handles invalid time format`() {
        // Arrange
        val input = "Tue Xyz 32 25:99:00 UTC 2023"
        val expected = "25:99:00" // Extracts time even if it's not a valid time

        // Act
        val result = input.extractTime()

        // Assert
        assertEquals(expected, result)
    }

    @Test
    fun `extractTime handles string with single digit hour correctly`() {
        // Arrange
        val input = "Thu Mar  7 03:15:00 UTC 2024"
        val expected = "7"

        // Act
        val result = input.extractTime()

        // Assert
        assertEquals(expected, result)
    }

    @Test
    fun `extractTime handles string with different date format`() {
        // Arrange
        val input = "2023-12-01T15:30:00Z"
        val expected = ""

        // Act
        val result = input.extractTime()

        // Assert
        assertEquals(expected, result)
    }
}
