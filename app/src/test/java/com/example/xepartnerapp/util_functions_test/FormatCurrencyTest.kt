package com.example.xepartnerapp.util_functions_test

import com.example.xepartnerapp.common.utils.Utils.formatCurrency
import org.junit.Assert.assertEquals
import org.junit.Test

class FormatCurrencyTest {

    @Test
    fun `formatCurrency formats whole number correctly`() {
        // Arrange
        val amount = 1000000.0
        val expectedResult = "1,000,000 VNĐ"

        // Act
        val result = amount.formatCurrency()

        // Assert
        assertEquals(expectedResult, result)
    }

    @Test
    fun `formatCurrency formats decimal number correctly`() {
        // Arrange
        val amount = 1234.56
        val expectedResult = "1,235 VNĐ" // Decimal part ignored

        // Act
        val result = amount.formatCurrency()

        // Assert
        assertEquals(expectedResult, result)
    }

    @Test
    fun `formatCurrency formats small number`() {
        // Arrange
        val amount = 0.0
        val expectedResult = "0 VNĐ"

        // Act
        val result = amount.formatCurrency()

        // Assert
        assertEquals(expectedResult, result)
    }

    @Test
    fun `formatCurrency formats negative number`() {
        // Arrange
        val amount = -500000.0
        val expectedResult = "-500,000 VNĐ"

        // Act
        val result = amount.formatCurrency()

        // Assert
        assertEquals(expectedResult, result)
    }

    @Test
    fun `formatCurrency formats large number`() {
        // Arrange
        val amount = 1000000000.0
        val expectedResult = "1,000,000,000 VNĐ"

        // Act
        val result = amount.formatCurrency()

        // Assert
        assertEquals(expectedResult, result)
    }

    @Test
    fun `formatCurrency formats single digit number`() {
        // Arrange
        val amount = 7.0
        val expectedResult = "7 VNĐ"

        // Act
        val result = amount.formatCurrency()

        // Assert
        assertEquals(expectedResult, result)
    }

    @Test
    fun `formatCurrency handles number less than 1000`() {
        // Arrange
        val amount = 999.0
        val expectedResult = "999 VNĐ"

        // Act
        val result = amount.formatCurrency()

        // Assert
        assertEquals(expectedResult, result)
    }

    @Test
    fun `formatCurrency rounds decimals correctly`() {
        // Arrange
        val amount = 1234.499
        val expectedResult = "1,234 VNĐ" // Rounded down

        // Act
        val result = amount.formatCurrency()

        // Assert
        assertEquals(expectedResult, result)
    }

    @Test
    fun `formatCurrency rounds decimals up`() {
        // Arrange
        val amount = 1234.501
        val expectedResult = "1,235 VNĐ" // Rounded up

        // Act
        val result = amount.formatCurrency()

        // Assert
        assertEquals(expectedResult, result)
    }

    @Test
    fun `formatCurrency handles edge case zero`() {
        // Arrange
        val amount = 0.0
        val expectedResult = "0 VNĐ"

        // Act
        val result = amount.formatCurrency()

        // Assert
        assertEquals(expectedResult, result)
    }

    @Test
    fun `formatCurrency handles very small number`() {
        // Arrange
        val amount = 0.1
        val expectedResult = "0 VNĐ" // Decimal part should be ignored

        // Act
        val result = amount.formatCurrency()

        // Assert
        assertEquals(expectedResult, result)
    }

    @Test
    fun `formatCurrency handles large decimal number`() {
        // Arrange
        val amount = 1234567.891
        val expectedResult = "1,234,568 VNĐ" // Decimal part should be ignored

        // Act
        val result = amount.formatCurrency()

        // Assert
        assertEquals(expectedResult, result)
    }

    @Test
    fun `formatCurrency formats number with trailing zeros`() {
        // Arrange
        val amount = 123456.000
        val expectedResult =
            "123,456 VNĐ" // Trailing zeros after the decimal part should be ignored

        // Act
        val result = amount.formatCurrency()

        // Assert
        assertEquals(expectedResult, result)
    }

    @Test
    fun `formatCurrency formats very large number correctly`() {
        // Arrange
        val amount = 1234567890123.0
        val expectedResult = "1,234,567,890,123 VNĐ" // Large number format

        // Act
        val result = amount.formatCurrency()

        // Assert
        assertEquals(expectedResult, result)
    }

    @Test
    fun `formatCurrency formats number with one decimal place correctly`() {
        // Arrange
        val amount = 1234.7
        val expectedResult = "1,235 VNĐ" // Decimal part rounded off

        // Act
        val result = amount.formatCurrency()

        // Assert
        assertEquals(expectedResult, result)
    }

    @Test
    fun `formatCurrency handles extremely large number correctly`() {
        // Arrange
        val amount = 9999999999999.0
        val expectedResult = "9,999,999,999,999 VNĐ" // Extremely large number

        // Act
        val result = amount.formatCurrency()

        // Assert
        assertEquals(expectedResult, result)
    }

    @Test
    fun `formatCurrency formats small positive number correctly`() {
        // Arrange
        val amount = 0.99
        val expectedResult = "1 VNĐ" // Rounded up

        // Act
        val result = amount.formatCurrency()

        // Assert
        assertEquals(expectedResult, result)
    }

    @Test
    fun `formatCurrency handles negative number with decimal correctly`() {
        // Arrange
        val amount = -12345.67
        val expectedResult = "-12,346 VNĐ" // Negative number, decimal rounded off

        // Act
        val result = amount.formatCurrency()

        // Assert
        assertEquals(expectedResult, result)
    }
}
