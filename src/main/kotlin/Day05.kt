package fr.amanin.aoc2025.day05

import kotlin.math.max
import kotlin.math.min

fun sortAndMerge(ranges: List<LongRange>): List<LongRange> {
    val ranges = ranges.sortedWith(Comparator.comparing(LongRange::first).thenComparing(LongRange::last))
    val optimized = mutableListOf(ranges[0])
    for (i in 1 until ranges.size) {
        val candidate = ranges[i]
        val searchIdx = optimized.binarySearch { range ->
            if (range.last < candidate.first) -1
            else if (candidate.last < range.first) 1
            else 0
        }
        if (searchIdx < 0) optimized.add(-searchIdx-1, candidate)
        else optimized[searchIdx] = min(candidate.first, optimized[searchIdx].first)..max(candidate.last, optimized[searchIdx].last)
    }
    assert(optimized.windowed(2).all { (r1, r2) -> r1.last < r2.first }) { "Ranges are not properly merged and ordered" }
    return optimized
}

class FreshIngredientIndex(freshRanges : List<LongRange>) {

    val ranges = sortAndMerge(freshRanges)

    fun isFresh(ingredient: Long) : Boolean {
        val idx = ranges.binarySearch { range ->
            if (range.last < ingredient) -1
            else if (ingredient < range.first) 1
            else 0
        }
        return idx >= 0
    }
}

class IngredientDb(val fresh : FreshIngredientIndex, val available: LongArray)

fun parse(input: List<String>) : IngredientDb {
    val freshRanges = mutableListOf<LongRange>()
    var i = 0
    var line: String
    while (i < input.size) {
        line = input[i++]
        if (line.isEmpty()) break
        val (start, end) = line.split("-")
        freshRanges.add(start.toLong()..end.toLong())
    }

    val availableIds = LongArray(input.size - i) { idx -> input[i+idx].toLong() }
    return IngredientDb(FreshIngredientIndex(freshRanges), availableIds)
}

fun part1(input: List<String>): Long {
    val db = parse(input)
    var found = 0L
    for (id in db.available) {
        if (db.fresh.isFresh(id)) {
            found++
        }
    }
    return found
}

fun part2(input: List<String>): Long {
    val ranges = input.asSequence()
        .takeWhile { line -> line.isNotEmpty() }
        .map { line ->
            val (first, last) = line.split("-")
            first.toLong()..last.toLong()
        }
        .toList()
        .let { sortAndMerge(it) }

    val countPerRange = LongArray(ranges.size) { i -> ranges[i].last - ranges[i].first + 1 }
    return countPerRange.sum()
}
