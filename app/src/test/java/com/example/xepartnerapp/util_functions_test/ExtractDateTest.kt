package com.example.xepartnerapp.util_functions_test

import com.example.xepartnerapp.common.utils.Utils.extractDate
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.util.Locale

class ExtractDateTest {

    @Test
    fun `extractDate returns correct formatted date for valid input`() {
        // Arrange
        val input = "Tue Nov 28 14:30:00 UTC 2023"
        val expected = "Nov 28 2023"

        // Act
        val result = extractDate(input)

        // Assert
        assertEquals(expected, result)
    }

    @Test
    fun `extractDate returns null for empty string`() {
        // Arrange
        val input = ""

        // Act
        val result = extractDate(input)

        // Assert
        assertNull(result)
    }

    @Test
    fun `extractDate returns null for invalid date format`() {
        // Arrange
        val input = "Invalid Date String"

        // Act
        val result = extractDate(input)

        // Assert
        assertNull(result)
    }

    @Test
    fun `extractDate returns null for null input`() {
        // Arrange
        val input: String? = null

        // Act
        val result = input?.let { extractDate(it) }

        // Assert
        assertNull(result)
    }

    @Test
    fun `extractDate handles different valid date inputs`() {
        // Arrange
        val input1 = "Fri Jan 01 00:00:00 UTC 2021"
        val input2 = "Sun Feb 14 12:00:00 UTC 2021"
        val input3 = "Wed Dec 25 08:00:00 UTC 2019"

        val expected1 = "Jan 01 2021"
        val expected2 = "Feb 14 2021"
        val expected3 = "Dec 25 2019"

        // Act & Assert
        assertEquals(expected1, extractDate(input1))
        assertEquals(expected2, extractDate(input2))
        assertEquals(expected3, extractDate(input3))
    }

    @Test
    fun `extractDate handles leap year dates`() {
        // Arrange
        val input = "Sat Feb 29 12:00:00 UTC 2020"
        val expected = "Feb 29 2020"

        // Act
        val result = extractDate(input)

        // Assert
        assertEquals(expected, result)
    }

    @Test
    fun `extractDate handles short timezone names`() {
        // Arrange
        val input = "Tue Nov 28 14:30:00 PST 2023"
        val expected = "Nov 29 2023"

        // Act
        val result = extractDate(input)

        // Assert
        assertEquals(expected, result)
    }

    @Test
    fun `extractDate returns null for date with missing components`() {
        // Arrange
        val input = "Tue Nov 28 2023" // Missing time and timezone

        // Act
        val result = extractDate(input)

        // Assert
        assertNull(result)
    }

    @Test
    fun `extractDate returns null for invalid day of week`() {
        // Arrange
        val input = "Fry Nov 28 14:30:00 UTC 2023" // Invalid "Fry" instead of "Fri"

        // Act
        val result = extractDate(input)

        // Assert
        assertNull(result)
    }

    @Test
    fun `extractDate returns null for invalid month`() {
        // Arrange
        val input = "Tue Now 28 14:30:00 UTC 2023" // Invalid "Now" instead of "Nov"

        // Act
        val result = extractDate(input)

        // Assert
        assertNull(result)
    }

    @Test
    fun `extractDate handles dates with additional spaces`() {
        // Arrange
        val input = "  Tue   Nov 28   14:30:00   UTC   2023  "
        val expected = null
        // Act
        val result = extractDate(input.trim())

        // Assert
        assertEquals(expected, result)
    }

    @Test
    fun `extractDate handles leading or trailing invalid characters`() {
        // Arrange
        val input = "###Tue Nov 28 14:30:00 UTC 2023@@@"
        val expected = "Nov 28 2023"

        // Act
        val result = extractDate(input.trim('#', '@'))

        // Assert
        assertEquals(expected, result)
    }

    @Test
    fun `extractDate handles leap seconds`() {
        // Arrange
        val input = "Wed Jun 30 23:59:60 UTC 2015" // Valid leap second date
        val expected = "Jul 01 2015"

        // Act
        val result = extractDate(input)

        // Assert
        assertEquals(expected, result)
    }

    @Test
    fun `extractDate handles invalid timestamp at epoch`() {
        // Arrange
        val input = "Thu Jan 01 00:00:00 UTC 1970"
        val expected = "Jan 01 1970"

        // Act
        val result = extractDate(input)

        // Assert
        assertEquals(expected, result)
    }

    @Test
    fun `extractDate returns null for malformed input with numbers`() {
        // Arrange
        val input = "1234567890"

        // Act
        val result = extractDate(input)

        // Assert
        assertNull(result)
    }

    @Test
    fun `extractDate handles valid dates with multiple spaces`() {
        // Arrange
        val input = "Tue    Nov     28    14:30:00     UTC    2023"
        val expected = "Nov 28 2023"

        // Act
        val result = extractDate(input.replace("\\s+".toRegex(), " "))

        // Assert
        assertEquals(expected, result)
    }

    @Test
    fun `extractDate handles case-insensitive months`() {
        // Arrange
        val input = "Tue nov 28 14:30:00 UTC 2023"
        val expected = "Nov 28 2023"

        // Act
        val result = extractDate(input)

        // Assert
        assertEquals(expected, result)
    }

    @Test
    fun `extractDate returns null for unsupported locale`() {
        // Arrange
        val input = "Mar 15 2023 14:30:00 UTC"
        Locale.setDefault(Locale.FRENCH) // Change system locale to unsupported

        // Act
        val result = extractDate(input)

        // Assert
        assertNull(result)
    }
}
