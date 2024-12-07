package com.example.xepartnerapp.util_functions_test

import com.example.xepartnerapp.common.utils.Utils.getLastThreeChars
import org.junit.Assert.assertEquals
import org.junit.Test

class GetLastThreeCharsTest {

    @Test
    fun `getLastThreeChars returns last three characters for input with more than three characters`() {
        // Arrange
        val input = "abcdef"
        val expected = "def"

        // Act
        val result = getLastThreeChars(input)

        // Assert
        assertEquals(expected, result)
    }

    @Test
    fun `getLastThreeChars returns full input if input length is exactly three`() {
        // Arrange
        val input = "abc"
        val expected = "abc"

        // Act
        val result = getLastThreeChars(input)

        // Assert
        assertEquals(expected, result)
    }

    @Test
    fun `getLastThreeChars returns full input if input length is less than three`() {
        // Arrange
        val input = "ab"
        val expected = "ab"

        // Act
        val result = getLastThreeChars(input)

        // Assert
        assertEquals(expected, result)
    }

    @Test
    fun `getLastThreeChars returns empty string for empty input`() {
        // Arrange
        val input = ""
        val expected = ""

        // Act
        val result = getLastThreeChars(input)

        // Assert
        assertEquals(expected, result)
    }

    @Test
    fun `getLastThreeChars handles special characters`() {
        // Arrange
        val input = "a$%"
        val expected = "a$%"

        // Act
        val result = getLastThreeChars(input)

        // Assert
        assertEquals(expected, result)
    }

    @Test
    fun `getLastThreeChars handles spaces in input`() {
        // Arrange
        val input = "hello world"
        val expected = "rld"

        // Act
        val result = getLastThreeChars(input)

        // Assert
        assertEquals(expected, result)
    }

    @Test
    fun `getLastThreeChars handles input with exactly three spaces`() {
        // Arrange
        val input = "   "
        val expected = "   "

        // Act
        val result = getLastThreeChars(input)

        // Assert
        assertEquals(expected, result)
    }

    @Test
    fun `getLastThreeChars handles input with non-ASCII characters`() {
        // Arrange
        val input = "你好世界"
        val expected = "好世界"

        // Act
        val result = getLastThreeChars(input)

        // Assert
        assertEquals(expected, result)
    }

    @Test
    fun `getLastThreeChars handles input with emojis`() {
        // Arrange
        val input = "abc😀😃😄"
        val expected = "😀😃😄"

        // Act
        val result = getLastThreeChars(input)

        // Assert
        assertEquals(expected, result)
    }

    @Test
    fun `getLastThreeChars handles single-character input`() {
        // Arrange
        val input = "a"
        val expected = "a"

        // Act
        val result = getLastThreeChars(input)

        // Assert
        assertEquals(expected, result)
    }

    @Test
    fun `getLastThreeChars handles two-character input`() {
        // Arrange
        val input = "ab"
        val expected = "ab"

        // Act
        val result = getLastThreeChars(input)

        // Assert
        assertEquals(expected, result)
    }
}
