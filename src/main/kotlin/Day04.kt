package fr.amanin.aoc2025.day04

import fr.amanin.aoc2025.utils.Grid
import fr.amanin.aoc2025.utils.textGrid
import kotlin.math.min

const val debug = true

val paperRoll = '@'

fun Grid<Char>.countPaperRollsAdjacentTo(cursor: Grid.Cursor<Char>) : Int {
    val startCol = if (cursor.column == 0) 0 else cursor.column - 1
    val startRow = if (cursor.row == 0) 0 else cursor.row - 1
    val endCol = min(cursor.column + 2, width)
    val endRow = min(cursor.row + 2, height)
    return asIndexedSequence(startCol, startRow, endCol - startCol, endRow - startRow)
        .filter { it.value == paperRoll }
        .count() - (if (cursor.value == paperRoll) 1 else 0)
}

fun part1(input: List<String>): Int {
    val completeGrid = textGrid(input)
    val debugState = if (debug) completeGrid.copy() else null
    val result = completeGrid.asIndexedSequence()
        .filter { it.value == paperRoll }
        .filter { cursor ->
            val isAccessible = completeGrid.countPaperRollsAdjacentTo(cursor) < 4
            if (debug && isAccessible) debugState?.set(cursor.column, cursor.row, 'x')
            isAccessible
        }
        .count()
    if (debug) {
        println("Result:")
        println(debugState)
    }
    return result
}

fun part2(input: List<String>): Long {
    val completeGrid = textGrid(input)
    var totalRemoved = 0L
    var removed: Long
    var pass = 0
    do {
        removed = completeGrid.asIndexedSequence()
            .filter { cursor -> cursor.value == paperRoll }
            .filter { cursor ->
                if (completeGrid.countPaperRollsAdjacentTo(cursor) < 4) {
                    completeGrid[cursor.column, cursor.row] = 'x'
                    true
                } else false
            }
            .count().toLong()
        if (debug) {
            println("Pass ${++pass}: removed $removed rolls")
            println(completeGrid)
            println()
        }
        totalRemoved += removed
    } while (removed > 0)
    return totalRemoved
}
