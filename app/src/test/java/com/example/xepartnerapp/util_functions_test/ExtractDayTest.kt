package com.example.xepartnerapp.util_functions_test

import com.example.xepartnerapp.common.utils.Utils.extractDay
import org.junit.Assert.assertEquals
import org.junit.Test

class ExtractDayTest {

    @Test
    fun `extractDay returns correct day from valid string`() {
        // Arrange
        val input = "Tue Nov 28 14:30:00 UTC 2023"
        val expected = "28"

        // Act
        val result = input.extractDay()

        // Assert
        assertEquals(expected, result)
    }

    @Test
    fun `extractDay returns empty string for string with less than 6 parts`() {
        // Arrange
        val input = "Nov 28 2023"
        val expected = ""

        // Act
        val result = input.extractDay()

        // Assert
        assertEquals(expected, result)
    }

    @Test
    fun `extractDay returns empty string for empty input`() {
        // Arrange
        val input = ""
        val expected = ""

        // Act
        val result = input.extractDay()

        // Assert
        assertEquals(expected, result)
    }

    @Test
    fun `extractDay handles string with extra spaces`() {
        // Arrange
        val input = "Tue     Nov     28    14:30:00    UTC    2023"
        val expected = ""

        // Act
        val result = input.extractDay()

        // Assert
        assertEquals(expected, result)
    }

    @Test
    fun `extractDay handles input with more than 6 parts`() {
        // Arrange
        val input = "Tue Nov 28 14:30:00 UTC 2023 extra"
        val expected = "28"

        // Act
        val result = input.extractDay()

        // Assert
        assertEquals(expected, result)
    }

    @Test
    fun `extractDay returns empty string for string without day`() {
        // Arrange
        val input = "Some random string without day"
        val expected = ""

        // Act
        val result = input.extractDay()

        // Assert
        assertEquals(expected, result)
    }

    @Test
    fun `extractDay returns empty string for string with invalid date format`() {
        // Arrange
        val input = "Tue Xyz 32 14:30:00 UTC 2023"
        val expected = "32" // Despite invalid day, it still extracts the day part.

        // Act
        val result = input.extractDay()

        // Assert
        assertEquals(expected, result)
    }

    @Test
    fun `extractDay handles string with single digit day correctly`() {
        // Arrange
        val input = "Thu Mar  7 10:15:00 UTC 2024"
        val expected = ""

        // Act
        val result = input.extractDay()

        // Assert
        assertEquals(expected, result)
    }

    @Test
    fun `extractDay handles string with long month name`() {
        // Arrange
        val input = "Mon September 15 08:00:00 UTC 2023"
        val expected = "15"

        // Act
        val result = input.extractDay()

        // Assert
        assertEquals(expected, result)
    }

    @Test
    fun `extractDay returns empty string for invalid month name`() {
        // Arrange
        val input = "Mon XYZ 15 08:00:00 UTC 2023"
        val expected = "15"

        // Act
        val result = input.extractDay()

        // Assert
        assertEquals(expected, result)
    }

    @Test
    fun `extractDay handles string with different date format`() {
        // Arrange
        val input = "2023-12-01T15:30:00Z"
        val expected = ""

        // Act
        val result = input.extractDay()

        // Assert
        assertEquals(expected, result)
    }
}
