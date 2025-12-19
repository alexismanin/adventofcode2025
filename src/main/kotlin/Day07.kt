package fr.amanin.aoc2025.day07

import fr.amanin.aoc2025.utils.textGrid
import utils.AsciiAnimator
import java.io.File
import java.util.TreeMap

fun part1(input: List<String>) : Long {
    val anim = AsciiAnimator()
    val scorePadding = 5
    try {
        val grid = textGrid(input.map { it+("0".padStart(scorePadding, ' ')) })
        anim.add(grid.copy())
        val start = grid.asIndexedSequence().first { cursor -> cursor.value == 'S' }
        var beamCols = listOf(start.column)
        var nbSplit = 0L
        for (rowIdx in start.row + 1..<grid.height) {
            val nextBeamCols = mutableSetOf<Int>()
            for (beamCol in beamCols) {
                when (grid[beamCol, rowIdx]) {
                    '.' -> {
                        grid[beamCol, rowIdx] = '|'
                        nextBeamCols.add(beamCol)
                    }

                    '^' -> {
                        nbSplit++
                        if (beamCol > 0) {
                            if (grid[beamCol - 1, rowIdx] == '^') throw UnsupportedOperationException("TODO: chained ^")
                            nextBeamCols.add(beamCol - 1)
                        }
                        if (beamCol < grid.width - scorePadding - 1) {
                            if (grid[beamCol + 1, rowIdx] == '^') throw UnsupportedOperationException("TODO: chained ^")
                            nextBeamCols.add(beamCol + 1)
                        }
                    }

                    else -> throw IllegalStateException("Unexpected token: ${grid[beamCol, rowIdx]} at column $beamCol in row $rowIdx")
                }
            }

            beamCols = nextBeamCols.sorted()

            for (c in beamCols) grid[c, rowIdx] = '|'
            val paddedScore = "$nbSplit".padStart(scorePadding, ' ')
            for (i in 0..<scorePadding) {
                grid[grid.width - scorePadding + i, rowIdx] = paddedScore[i]
            }
            anim.add(grid.copy())
        }
        return nbSplit
    } finally {
        File("./anim-day7-part1.svg").writeText(anim.exportSvg(4u, debug = false))
    }
}

fun part2(input: List<String>) : Long {
    val anim = AsciiAnimator()
    val scorePadding = 15
    try {
        val grid = textGrid(input.map { it+("0".padStart(scorePadding, ' ')) })
        anim.add(grid.copy())
        val start = grid.asIndexedSequence().first { cursor -> cursor.value == 'S' }
        var beamTimelines = TreeMap<Int, Long>()
        beamTimelines[start.column] = 1
        for (rowIdx in start.row + 1..<grid.height) {
            val nextBeamTimelines = TreeMap<Int, Long>()
            for ((beamCol, nbPossibleBeams) in beamTimelines) {
                when (grid[beamCol, rowIdx]) {
                    '.', '|' -> {
                        nextBeamTimelines.merge(beamCol, nbPossibleBeams, Long::plus)
                    }

                    '^' -> {
                        if (beamCol > 0) {
                            if (grid[beamCol - 1, rowIdx] == '^') throw UnsupportedOperationException("TODO: chained ^")
                            nextBeamTimelines.merge(beamCol-1, nbPossibleBeams, Long::plus)
                        }
                        if (beamCol < grid.width - scorePadding - 1) {
                            if (grid[beamCol + 1, rowIdx] == '^') throw UnsupportedOperationException("TODO: chained ^")
                            nextBeamTimelines.merge(beamCol+1, nbPossibleBeams, Long::plus)
                        }
                    }

                    else -> throw IllegalStateException("Unexpected token: ${grid[beamCol, rowIdx]} at column $beamCol in row $rowIdx")
                }
            }

            beamTimelines = nextBeamTimelines

            for (c in beamTimelines.keys) grid[c, rowIdx] = '|'
            val currentScore = "${beamTimelines.values.sum()}"
            check (currentScore.length <= scorePadding) { "Score si too high to be printed in grid !" }
            val paddedScore = currentScore.padStart(scorePadding, padChar = ' ')
            for (i in 0..<scorePadding) {
                grid[grid.width - scorePadding + i, rowIdx] = paddedScore[i]
            }
            anim.add(grid.copy())
        }
        return beamTimelines.values.sum()
    } finally {
        File("./anim-day7-part2.svg").writeText(anim.exportSvg(4u, debug = false))
    }
}