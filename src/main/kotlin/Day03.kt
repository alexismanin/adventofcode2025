package fr.amanin.aoc2025.day03

typealias Bank= IntArray

fun List<String>.parse(): Sequence<Bank> {
    return asSequence()
        .map { line ->
            Bank(line.length) { i ->
                line[i].digitToInt()
            }
        }
}

fun Bank.selectOptimalCouple() : Pair<Int, Int> {
    var firstBest = 0
    var secondBest = 1
    val lastIdx = size-1
    for (i in 1..<lastIdx) {
        val current = this[i]
        if (current > this[firstBest]) {
            firstBest = i
            secondBest = i+1
        } else if (current > this[secondBest]) secondBest = i
    }
    if (lastIdx > secondBest && this.last() > this[secondBest]) { secondBest = lastIdx }
    return firstBest to secondBest
}

/**
 * Select index of this array containing max value
 *
 * @param startIdx Index to start search on.
 * @param endIdx Index to stop search at.
 */
fun Bank.maxIdx(startIdx: Int, endIdx: Int): Int {
    var max = get(startIdx)
    var maxIdx = startIdx
    for (i in startIdx+1..endIdx) {
        val current = get(i)
        if (current > max) {
            max = current
            maxIdx = i
        }
    }
    return maxIdx
}

fun Bank.selectOptimalSequence(n: Int): IntArray {
    assert(n > 0)
    val bestIndices = IntArray(n) { it }
    bestIndices[0] = maxIdx(0, size-n)
    for (b in 1 until bestIndices.size) {
        bestIndices[b] = maxIdx(bestIndices[b-1]+1, size - (n-b))
    }
    return bestIndices
}

fun part1(input: List<String>): Long {
    return input.parse()
        .map { it to it.selectOptimalCouple() }
        .map { (bank, indices) -> "${bank[indices.first]}${bank[indices.second]}".toLong() }
        .sum()
}

fun part2(input: List<String>): Long {
    return input.parse()
        .map { it to it.selectOptimalSequence(12) }
        .map { (bank, indices) -> indices.map {i -> bank[i]}.joinToString("").toLong() }
        .sum()
}