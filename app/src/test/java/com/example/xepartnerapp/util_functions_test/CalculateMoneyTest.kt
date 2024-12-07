package com.example.xepartnerapp.util_functions_test

import com.example.xepartnerapp.common.utils.Constants
import com.example.xepartnerapp.common.utils.Utils.calculateMoney
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CalculateMoneyTest {

    @Test
    fun `calculateMoney returns correct value for BIKE type`() {
        // Arrange
        val amount = 1000.0
        val vehicleType = Constants.BIKE
        val expectedResult = amount * Constants.BIKE_RATE

        // Act
        val result = amount.calculateMoney(vehicleType)

        // Assert
        assertEquals(expectedResult, result, 0.0)
    }

    @Test
    fun `calculateMoney returns correct value for CAR type`() {
        // Arrange
        val amount = 2000.0
        val vehicleType = Constants.CAR
        val expectedResult = amount * Constants.CAR_RATE

        // Act
        val result = amount.calculateMoney(vehicleType)

        // Assert
        assertEquals(expectedResult, result, 0.0)
    }

    @Test
    fun `calculateMoney returns correct value for MVP type`() {
        // Arrange
        val amount = 1500.0
        val vehicleType = Constants.MVP
        val expectedResult = amount * Constants.MVP_RATE

        // Act
        val result = amount.calculateMoney(vehicleType)

        // Assert
        assertEquals(expectedResult, result, 0.0)
    }

    @Test
    fun `calculateMoney returns zero for unknown vehicle type`() {
        // Arrange
        val amount = 1000.0
        val vehicleType = "UNKNOWN"
        val expectedResult = 0.0

        // Act
        val result = amount.calculateMoney(vehicleType)

        // Assert
        assertEquals(expectedResult, result, 0.0)
    }

    @Test
    fun `calculateMoney with zero amount returns zero`() {
        // Arrange
        val amount = 0.0
        val vehicleType = Constants.BIKE
        val expectedResult = 0.0

        // Act
        val result = amount.calculateMoney(vehicleType)

        // Assert
        assertEquals(expectedResult, result, 0.0)
    }

    @Test
    fun `calculateMoney with negative amount returns correct negative value for CAR type`() {
        // Arrange
        val amount = -500.0
        val vehicleType = Constants.CAR
        val expectedResult = amount * Constants.CAR_RATE

        // Act
        val result = amount.calculateMoney(vehicleType)

        // Assert
        assertEquals(expectedResult, result, 0.0)
    }

    @Test
    fun `calculateMoney returns zero for null vehicle type`() {
        // Arrange
        val amount = 1000.0
        val vehicleType: String? = null
        val expectedResult = 0.0

        // Act
        val result = amount.calculateMoney(vehicleType ?: "")

        // Assert
        assertEquals(expectedResult, result, 0.0)
    }

    @Test
    fun `calculateMoney returns zero for empty vehicle type`() {
        // Arrange
        val amount = 1000.0
        val vehicleType = ""
        val expectedResult = 0.0

        // Act
        val result = amount.calculateMoney(vehicleType)

        // Assert
        assertEquals(expectedResult, result, 0.0)
    }

    @Test
    fun `calculateMoney handles large amount correctly for BIKE`() {
        // Arrange
        val amount = Double.MAX_VALUE
        val vehicleType = Constants.BIKE
        val expectedResult = amount * Constants.BIKE_RATE

        // Act
        val result = amount.calculateMoney(vehicleType)

        // Assert
        assertEquals(expectedResult, result, 1.0) // Dung sai là 1.0 do giới hạn số học
    }

    @Test
    fun `calculateMoney handles small amount correctly for CAR`() {
        // Arrange
        val amount = 0.0001
        val vehicleType = Constants.CAR
        val expectedResult = amount * Constants.CAR_RATE

        // Act
        val result = amount.calculateMoney(vehicleType)

        // Assert
        assertEquals(expectedResult, result, 0.0)
    }

    @Test
    fun `calculateMoney handles edge case of 1_0 correctly for MVP`() {
        // Arrange
        val amount = 1.0
        val vehicleType = Constants.MVP
        val expectedResult = amount * Constants.MVP_RATE

        // Act
        val result = amount.calculateMoney(vehicleType)

        // Assert
        assertEquals(expectedResult, result, 0.0)
    }

    @Test
    fun `calculateMoney handles Double_MAX_VALUE and CAR correctly`() {
        // Arrange
        val amount = Double.MAX_VALUE
        val vehicleType = Constants.CAR

        // Act
        val result = amount.calculateMoney(vehicleType)

        // Assert
        assertTrue(result.isFinite())
    }
}
