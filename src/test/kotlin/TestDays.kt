package fr.amanin.aoc2025

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class TestDays {

    @ParameterizedTest(name="Day {0} part {1} should return {2}")
    @CsvSource(
        "1, 1, 3",
        "1, 2, 6"
    )
    fun testDay(dayIdx: Int, partIdx: Int, expectedOutput: Long) {
        val result = runDay(dayIdx, partIdx, Thread.currentThread().contextClassLoader)
        assertEquals(expectedOutput, result.toLong())
    }
}
