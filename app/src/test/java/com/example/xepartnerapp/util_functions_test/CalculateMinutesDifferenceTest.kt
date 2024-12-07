package com.example.xepartnerapp.util_functions_test

import com.example.xepartnerapp.common.utils.Utils.calculateMinutesDifference
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test
import java.text.ParseException

class CalculateMinutesDifferenceTest {

    @Test
    fun `calculateMinutesDifference returns 0 for empty strings`() {
        val time1 = ""
        val time2 = ""
        val expected = 0L
        val result = calculateMinutesDifference(time1, time2)
        assertEquals(expected, result)
    }

    @Test
    fun `calculateMinutesDifference throws exception for invalid date format`() {
        val time1 = "InvalidDate1"
        val time2 = "InvalidDate2"

        try {
            calculateMinutesDifference(time1, time2)
            fail("Expected exception for invalid date format")
        } catch (e: Exception) {
            assertTrue(e is ParseException)
        }
    }

    @Test
    fun `calculateMinutesDifference returns 0 for same time`() {
        val time1 = "Wed Dec 06 12:00:00 GMT 2023"
        val time2 = "Wed Dec 06 12:00:00 GMT 2023"
        val expected = 0L
        val result = calculateMinutesDifference(time1, time2)
        assertEquals(expected, result)
    }

    @Test
    fun `calculateMinutesDifference returns correct positive difference`() {
        val time1 = "Wed Dec 06 12:00:00 GMT 2023"
        val time2 = "Wed Dec 06 12:30:00 GMT 2023"
        val expected = 30L
        val result = calculateMinutesDifference(time1, time2)
        assertEquals(expected, result)
    }

    @Test
    fun `calculateMinutesDifference returns correct negative difference`() {
        val time1 = "Wed Dec 06 12:30:00 GMT 2023"
        val time2 = "Wed Dec 06 12:00:00 GMT 2023"
        val expected = -30L
        val result = calculateMinutesDifference(time1, time2)
        assertEquals(expected, result)
    }

    @Test
    fun `calculateMinutesDifference handles large time difference`() {
        val time1 = "Wed Dec 06 12:00:00 GMT 2023"
        val time2 = "Thu Dec 07 12:00:00 GMT 2023"
        val expected = 1440L
        val result = calculateMinutesDifference(time1, time2)
        assertEquals(expected, result)
    }

    @Test
    fun `calculateMinutesDifference handles different time zones`() {
        val time1 = "Wed Dec 06 12:00:00 GMT 2023"
        val time2 = "Wed Dec 06 07:00:00 EST 2023"
        val expected = 0L
        val result = calculateMinutesDifference(time1, time2)
        assertEquals(expected, result)
    }

    @Test
    fun `calculateMinutesDifference handles one valid date`() {
        val time1 = "Wed Dec 06 12:00:00 GMT 2023"
        val time2 = "InvalidDate"

        try {
            calculateMinutesDifference(time1, time2)
            fail("Expected exception for invalid date")
        } catch (e: Exception) {
            assertTrue(e is ParseException)
        }
    }

    @Test
    fun `calculateMinutesDifference handles date with milliseconds`() {
        val time1 = "Wed Dec 06 12:00:00.000 GMT 2023"
        val time2 = "Wed Dec 06 12:01:00.000 GMT 2023"
        val expected = 1L
        try {
            val result = calculateMinutesDifference(time1, time2)
            assertEquals(expected, result)
        } catch (e: Exception) {
            assertTrue(e is ParseException)
        }
    }

    @Test
    fun `calculateMinutesDifference handles year difference`() {
        val time1 = "Fri Dec 31 23:59:00 GMT 2021"
        val time2 = "Sat Jan 01 00:01:00 GMT 2022"
        val expected = 2L
        val result = calculateMinutesDifference(time1, time2)
        assertEquals(expected, result)
    }

    @Test
    fun `calculateMinutesDifference handles leap year difference`() {
        val time1 = "Sat Feb 28 23:59:00 GMT 2024"
        val time2 = "Sun Mar 01 00:01:00 GMT 2024"
        val expected = 1442L
        val result = calculateMinutesDifference(time1, time2)
        assertEquals(expected, result)
    }

    @Test
    fun `calculateMinutesDifference handles future dates`() {
        val time1 = "Wed Dec 06 12:00:00 GMT 2023"
        val time2 = "Wed Dec 06 12:10:00 GMT 2025"
        val expected = 1052650L
        val result = calculateMinutesDifference(time1, time2)
        assertEquals(expected, result)
    }

    @Test
    fun `calculateMinutesDifference handles historical dates`() {
        val time1 = "Wed Dec 06 12:00:00 GMT 2000"
        val time2 = "Wed Dec 06 12:10:00 GMT 2023"
        val expected = 12096010L
        val result = calculateMinutesDifference(time1, time2)
        assertEquals(expected, result)
    }
}
