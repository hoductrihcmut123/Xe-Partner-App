package com.example.xepartnerapp.util_functions_test

import com.example.xepartnerapp.common.utils.Utils.convertMetersToKilometers
import org.junit.Assert.assertEquals
import org.junit.Test

class ConvertMetersToKilometersTest {
    @Test
    fun `convertMetersToKilometers handles zero meters`() {
        // Arrange
        val meters = 0.0
        val expectedKilometers = 0.0

        // Act
        val result = meters.convertMetersToKilometers()

        // Assert
        assertEquals(expectedKilometers, result, 0.0)
    }

    @Test
    fun `convertMetersToKilometers converts meters to kilometers correctly`() {
        // Arrange
        val meters = 1500.0
        val expectedKilometers = 1.5

        // Act
        val result = meters.convertMetersToKilometers()

        // Assert
        assertEquals(expectedKilometers, result, 0.0)
    }

    @Test
    fun `convertMetersToKilometers rounds to one decimal place`() {
        // Arrange
        val meters = 1234.0
        val expectedKilometers = 1.2

        // Act
        val result = meters.convertMetersToKilometers()

        // Assert
        assertEquals(expectedKilometers, result, 0.0)
    }

    @Test
    fun `convertMetersToKilometers handles large input values`() {
        // Arrange
        val meters = 12345678.0
        val expectedKilometers = 12345.7

        // Act
        val result = meters.convertMetersToKilometers()

        // Assert
        assertEquals(expectedKilometers, result, 0.0)
    }

    @Test
    fun `convertMetersToKilometers handles very small input values`() {
        // Arrange
        val meters = 1.0
        val expectedKilometers = 0.0

        // Act
        val result = meters.convertMetersToKilometers()

        // Assert
        assertEquals(expectedKilometers, result, 0.0)
    }

    @Test
    fun `convertMetersToKilometers handles negative input`() {
        // Arrange
        val meters = -1500.0
        val expectedKilometers = -1.5

        // Act
        val result = meters.convertMetersToKilometers()

        // Assert
        assertEquals(expectedKilometers, result, 0.0)
    }

    @Test
    fun `convertMetersToKilometers handles very large input values`() {
        // Arrange
        val meters = Double.MAX_VALUE
        val expectedKilometers = Double.MAX_VALUE / 1000

        // Act
        val result = meters.convertMetersToKilometers()

        // Assert
        assertEquals(expectedKilometers, result, 0.0)
    }

    @Test
    fun `convertMetersToKilometers handles very small positive input`() {
        // Arrange
        val meters = 0.00001
        val expectedKilometers = 0.0

        // Act
        val result = meters.convertMetersToKilometers()

        // Assert
        assertEquals(expectedKilometers, result, 0.0)
    }

    @Test
    fun `convertMetersToKilometers handles very small negative input`() {
        // Arrange
        val meters = -0.00001
        val expectedKilometers = 0.0

        // Act
        val result = meters.convertMetersToKilometers()

        // Assert
        assertEquals(expectedKilometers, result, 0.0)
    }

    @Test
    fun `convertMetersToKilometers handles exactly 1 kilometer`() {
        // Arrange
        val meters = 1000.0
        val expectedKilometers = 1.0

        // Act
        val result = meters.convertMetersToKilometers()

        // Assert
        assertEquals(expectedKilometers, result, 0.0)
    }

    @Test
    fun `convertMetersToKilometers handles exactly 10 kilometers`() {
        // Arrange
        val meters = 10000.0
        val expectedKilometers = 10.0

        // Act
        val result = meters.convertMetersToKilometers()

        // Assert
        assertEquals(expectedKilometers, result, 0.0)
    }

    @Test
    fun `convertMetersToKilometers handles fractional kilometers`() {
        // Arrange
        val meters = 1234.56
        val expectedKilometers = 1.2

        // Act
        val result = meters.convertMetersToKilometers()

        // Assert
        assertEquals(expectedKilometers, result, 0.0)
    }

    @Test
    fun `convertMetersToKilometers handles input near boundary condition`() {
        // Arrange
        val meters = 999.9
        val expectedKilometers = 1.0

        // Act
        val result = meters.convertMetersToKilometers()

        // Assert
        assertEquals(expectedKilometers, result, 0.0)
    }

    @Test
    fun `convertMetersToKilometers handles input slightly above boundary`() {
        // Arrange
        val meters = 1000.1
        val expectedKilometers = 1.0

        // Act
        val result = meters.convertMetersToKilometers()

        // Assert
        assertEquals(expectedKilometers, result, 0.0)
    }

    @Test
    fun `convertMetersToKilometers handles input with large decimals`() {
        // Arrange
        val meters = 123456789.987654321
        val expectedKilometers = 123456.8

        // Act
        val result = meters.convertMetersToKilometers()

        // Assert
        assertEquals(expectedKilometers, result, 0.0)
    }
}
