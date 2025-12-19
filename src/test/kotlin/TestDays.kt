package fr.amanin.aoc2025

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class TestDays {

    @ParameterizedTest(name="Day {0} part {1} should return {2}")
    @CsvSource(
        "1, 1, 3",
        "1, 2, 6",
        "2, 1, 1227775554",
        "2, 2, 4174379265",
        "3, 1, 357",
        "3, 2, 3121910778619",
        "4, 1, 13",
        "4, 2, 43",
        "5, 1, 3",
        "5, 2, 14",
        "6, 1, 4277556",
        "6, 2, 3263827",
        "7, 1, 21",
        "7, 2, 40",
    )
    fun testDay(dayIdx: Int, partIdx: Int, expectedOutput: Long) {
        val result = runDay(dayIdx, partIdx, Thread.currentThread().contextClassLoader)
        assertEquals(expectedOutput, result.toLong())
    }
}
