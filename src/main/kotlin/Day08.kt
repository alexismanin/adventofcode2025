package fr.amanin.aoc2025.day08

import utils.Vector3D
import kotlin.math.min

typealias Point = Vector3D

fun Point.squaredDistanceTo(other: Point) = (this - other).squaredNorm()

data class Junction(val p1: Point, val p2: Point, val squareDist: Long = p1.squaredDistanceTo(p2))

typealias Circuit = MutableSet<Point>

data class Input(val pts: List<Point>, val iterations: Int = 1000)

fun parse(input: List<String>) : Input {
    val count = if (input[0].matches(Regex("\\d+"))) input[0].toInt() else 0
    val input = if (count > 0) input.subList(1, input.size) else input
    val pts = input.map { line ->
        val tokens = line.split(",")
        require(tokens.size == 3)
        Vector3D(tokens[0].toLong(), tokens[1].toLong(), tokens[2].toLong())
    }

    return if (count == 0) Input(pts) else Input(pts, count)
}

fun part1(input: List<String>): Long = solve(input, false)
fun part2(input: List<String>): Long = solve(input, true)

fun solve(input: List<String>, isPart2: Boolean): Long {
    val (pts, iterations) = parse(input).let { if (isPart2) it.copy(iterations = Integer.MAX_VALUE) else it }

    val allJunctions = ArrayList<Junction>(pts.size * (pts.size - 1))
    for (i in pts.indices) {
        for (j in (i+1) ..< pts.size) {
            allJunctions.add(Junction(pts[i], pts[j]))
        }
    }
    allJunctions.sortBy { it.squareDist }
    println("Iterations: $iterations. Number of couples: ${allJunctions.size}")
    val circuits = ArrayList<Circuit>(100)
    for (i in 0..<(min(iterations, allJunctions.size))) {
        val (p1, p2, _) = allJunctions[i]
        var added = false
        var searchConnected : Circuit? = null
        for (circuit in circuits) {
            if (p1 in circuit) {
                if (searchConnected == null) {
                    circuit.add(p2)
                    searchConnected = circuit
                } else {
                    searchConnected.addAll(circuit)
                    circuit.clear()
                    added = true
                    break
                }
            } else if (p2 in circuit) {
                if (searchConnected == null) {
                    circuit.add(p1)
                    searchConnected = circuit
                } else {
                    searchConnected.addAll(circuit)
                    circuit.clear()
                    added = true
                    break
                }
            } else {
                continue
            }
            added = true
        }

        if (!added) circuits.add(mutableSetOf(p1, p2))
        else {
            circuits.removeIf { circuit -> circuit.isEmpty() }
            if (isPart2 && circuits.any { it.size == pts.size }) {
                return p1.x * p2.x
            }
        }
    }

    println("Number of circuits before optimisation: ${circuits.size}")
    var movesHappened : Boolean
    var optiIterations = 0
    do {
        optiIterations++
        movesHappened = false
        for (i in circuits.indices) {
            for (j in (i + 1)..<circuits.size) {
                val c2 = circuits[j]
                if (c2.isEmpty()) continue
                val c1 = circuits[i]
                if (c1.any { p -> p in c2 }) {
                    c1.addAll(c2)
                    c2.clear()
                    movesHappened = true
                }
            }
        }
    } while (movesHappened)

    println("${circuits.size} circuits remains after $optiIterations passes of optimisation. ")
    circuits.sortByDescending { it.size }
    return circuits.take(3).fold(1) { acc, data -> acc * data.size }
}
