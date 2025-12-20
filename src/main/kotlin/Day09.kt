package fr.amanin.aoc2025.day09

import utils.Vector2D
import kotlin.math.abs

typealias Point = Vector2D

private fun parse(input: List<String>): List<Point> = input.map { line ->
    val (v1, v2) = line.split(",")
    Vector2D(v1.toLong(), v2.toLong())
}

fun part1(input: List<String>): Long {
    val pts = parse(input)
    var maxArea = 0L
    for (i in 0..<pts.size) {
        for (j in i..<pts.size) {
            val diagonal = pts[i] - pts[j]
            val area = (abs(diagonal.x) + 1) * (abs(diagonal.y)+1)
            if (area > maxArea) maxArea = area
        }
    }
    return maxArea
}

fun part2(input: List<String>): Int { TODO() }