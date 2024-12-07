package com.example.xepartnerapp.util_functions_test

import com.example.xepartnerapp.common.utils.Utils.decodePolyLine
import com.google.android.gms.maps.model.LatLng
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class DecodePolyLineTest {

    @Test
    fun `decodePolyLine handles empty string`() {
        // Arrange
        val encodedPolyline = ""
        val expectedResult = emptyList<LatLng>()

        // Act
        val result = decodePolyLine(encodedPolyline)

        // Assert
        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun `decodePolyLine handles invalid input`() {
        // Arrange
        val invalidPolyline = "invalid"

        try {
            // Act
            decodePolyLine(invalidPolyline)
            // Assert: Expect exception to be thrown
            assert(false) { "Expected an exception to be thrown for invalid input" }
        } catch (e: Exception) {
            assertThat(e).isInstanceOf(IndexOutOfBoundsException::class.java)
        }
    }

    @Test
    fun `decodePolyLine handles edge case with one valid character`() {
        // Arrange
        val encodedPolyline = "A"
        try {
            // Act
            decodePolyLine(encodedPolyline)
            // Assert: Expect exception to be thrown
            assert(false) { "Expected an exception to be thrown for invalid input" }
        } catch (e: Exception) {
            assertThat(e).isInstanceOf(IndexOutOfBoundsException::class.java)
        }
    }

    @Test
    fun `decodePolyLine handles edge case with one valid A character`() {
        // Arrange
        val encodedPolyline = "A"
        try {
            // Act
            decodePolyLine(encodedPolyline)
            // Assert: Expect exception to be thrown
            assert(false) { "Expected an exception to be thrown for invalid input" }
        } catch (e: Exception) {
            assertThat(e).isInstanceOf(IndexOutOfBoundsException::class.java)
        }
    }

    @Test
    fun `decodePolyLine handles edge case with one valid B character`() {
        // Arrange
        val encodedPolyline = "B"
        try {
            // Act
            decodePolyLine(encodedPolyline)
            // Assert: Expect exception to be thrown
            assert(false) { "Expected an exception to be thrown for invalid input" }
        } catch (e: Exception) {
            assertThat(e).isInstanceOf(IndexOutOfBoundsException::class.java)
        }
    }

    @Test
    fun `decodePolyLine handles edge case with one valid E character`() {
        // Arrange
        val encodedPolyline = "E"
        try {
            // Act
            decodePolyLine(encodedPolyline)
            // Assert: Expect exception to be thrown
            assert(false) { "Expected an exception to be thrown for invalid input" }
        } catch (e: Exception) {
            assertThat(e).isInstanceOf(IndexOutOfBoundsException::class.java)
        }
    }
}
