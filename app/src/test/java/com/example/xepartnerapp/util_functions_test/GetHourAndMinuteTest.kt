package com.example.xepartnerapp.util_functions_test

import com.example.xepartnerapp.common.utils.Utils.getHourAndMinute
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test

class GetHourAndMinuteTest {

    @Test
    fun `getHourAndMinute returns placeholder for empty input`() {
        // Arrange
        val dateString = ""
        val expectedOutput = "__:__"

        // Act
        val result = getHourAndMinute(dateString)

        // Assert
        assertEquals(expectedOutput, result)
    }

    @Test
    fun `getHourAndMinute handles input with extra spaces`() {
        // Arrange
        val dateString = "2024-12-06   14:45:00  "
        val expectedOutput = "14:45"

        // Act
        val result = getHourAndMinute(dateString)

        // Assert
        assertEquals(expectedOutput, result)
    }

    @Test
    fun `getHourAndMinute handles input without time`() {
        // Arrange
        val dateString = "2024-12-06"
        try {
            // Act
            getHourAndMinute(dateString)
            fail("Expected exception for invalid input")
        } catch (e: Exception) {
            // Assert
            assertTrue(e is IndexOutOfBoundsException)
        }
    }

    @Test
    fun `getHourAndMinute handles input with time in a different position`() {
        // Arrange
        val dateString = "10:30:00 2024-12-06"
        try {
            // Act
            getHourAndMinute(dateString)
            fail("Expected exception for invalid input")
        } catch (e: Exception) {
            // Assert
            assertTrue(e is IndexOutOfBoundsException)
        }
    }

    @Test
    fun `getHourAndMinute handles input with partial time`() {
        // Arrange
        val dateString = "2024-12-06 10:3"
        try {
            // Act
            getHourAndMinute(dateString)
            fail("Expected exception for invalid time format")
        } catch (e: Exception) {
            // Assert
            assertTrue(e is IndexOutOfBoundsException)
        }
    }

    @Test
    fun `getHourAndMinute handles input with incorrect splitting structure`() {
        // Arrange
        val dateString = "InvalidDateTimeString"
        try {
            // Act
            getHourAndMinute(dateString)
            fail("Expected exception for invalid splitting structure")
        } catch (e: Exception) {
            // Assert
            assertTrue(e is IndexOutOfBoundsException)
        }
    }

    @Test
    fun `getHourAndMinute handles input with special characters`() {
        // Arrange
        val dateString = "2024-12-06 @ 10:30:00"
        try {
            // Act
            getHourAndMinute(dateString)
            fail("Expected exception for invalid input")
        } catch (e: Exception) {
            // Assert
            assertTrue(e is IndexOutOfBoundsException)
        }
    }
}
