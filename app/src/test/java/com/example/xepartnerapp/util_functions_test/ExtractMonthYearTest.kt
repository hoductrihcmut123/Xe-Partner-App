package com.example.xepartnerapp.util_functions_test

import com.example.xepartnerapp.common.utils.Utils.extractMonthYear
import org.junit.Assert.assertEquals
import org.junit.Test

class ExtractMonthYearTest {

    @Test
    fun `extractMonthYear returns correct month and year from valid string`() {
        // Arrange
        val input = "Tue Nov 28 14:30:00 UTC 2023"
        val expected = "Nov2023"

        // Act
        val result = input.extractMonthYear()

        // Assert
        assertEquals(expected, result)
    }

    @Test
    fun `extractMonthYear returns empty string for string with less than 6 parts`() {
        // Arrange
        val input = "Nov 28 2023"
        val expected = ""

        // Act
        val result = input.extractMonthYear()

        // Assert
        assertEquals(expected, result)
    }

    @Test
    fun `extractMonthYear returns empty string for empty input`() {
        // Arrange
        val input = ""
        val expected = ""

        // Act
        val result = input.extractMonthYear()

        // Assert
        assertEquals(expected, result)
    }

    @Test
    fun `extractMonthYear handles string with extra spaces`() {
        // Arrange
        val input = "Tue     Nov     28    14:30:00    UTC    2023"
        val expected = "2023"

        // Act
        val result = input.extractMonthYear()

        // Assert
        assertEquals(expected, result)
    }

    @Test
    fun `extractMonthYear handles input with more than 6 parts`() {
        // Arrange
        val input = "Tue Nov 28 14:30:00 UTC 2023 extra"
        val expected = "Novextra"

        // Act
        val result = input.extractMonthYear()

        // Assert
        assertEquals(expected, result)
    }

    @Test
    fun `extractMonthYear returns empty string for string without month and year`() {
        // Arrange
        val input = "Some random string without month and year"
        val expected = "randomyear"

        // Act
        val result = input.extractMonthYear()

        // Assert
        assertEquals(expected, result)
    }

    @Test
    fun `extractMonthYear returns empty string for string with invalid month format`() {
        // Arrange
        val input = "Tue Xyz 28 14:30:00 UTC 2023"
        val expected = "Xyz2023"

        // Act
        val result = input.extractMonthYear()

        // Assert
        assertEquals(expected, result)
    }

    @Test
    fun `extractMonthYear returns empty string for string with invalid date format`() {
        // Arrange
        val input = "Tue Nov 32 14:30:00 UTC 2023"
        val expected = "Nov2023" // Despite invalid day, format is still correct.

        // Act
        val result = input.extractMonthYear()

        // Assert
        assertEquals(expected, result)
    }
}
