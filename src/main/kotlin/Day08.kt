package fr.amanin.aoc2025.day08

import kotlin.math.min

data class Point(val x: Long, val y: Long, val z: Long) {
    operator fun minus(other: Point): Point = Point(x - other.x, y - other.y, z - other.z)
    fun squareNorm(): Long = x * x + y * y + z * z
}

fun Point.squaredDistanceTo(other: Point) = (this - other).squareNorm()

data class Input(val pts: List<Point>, val iterations: Int = 1000)

fun parse(input: List<String>) : Input {
    val count = if (input[0].matches(Regex("\\d+"))) input[0].toInt() else 0
    val input = if (count > 0) input.subList(1, input.size) else input
    val pts = input.map { line ->
        val tokens = line.split(",")
        require(tokens.size == 3)
        Point(tokens[0].toLong(), tokens[1].toLong(), tokens[2].toLong())
    }

    return if (count == 0) Input(pts) else Input(pts, count)
}

fun part1(input: List<String>): Long = part1BrutForce(input)

fun part1BrutForce(input: List<String>): Long {
    val (pts, iterations) = parse(input)
    data class Distance(val p1: Point, val p2: Point, val squareDist: Long = p1.squaredDistanceTo(p2))
    val allDistances = ArrayList<Distance>(pts.size * (pts.size - 1))
    for (i in pts.indices) {
        for (j in (i+1) ..< pts.size) {
            allDistances.add(Distance(pts[i], pts[j]))
        }
    }
    allDistances.sortBy { it.squareDist }
    println("Iterations: $iterations. Number of couples: ${allDistances.size}")
    val circuits = ArrayList<MutableSet<Point>>(100)
    for (i in 0..<(min(iterations, allDistances.size))) {
        val (p1, p2, _) = allDistances[i]
        var added = false
        for (circuit in circuits) {
            if (p1 in circuit) {
                circuit.add(p2)
            } else if (p2 in circuit) {
                circuit.add(p1)
            } else {
                continue
            }
            added = true
            break
        }

        if (!added) circuits.add(mutableSetOf(p1, p2))
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
