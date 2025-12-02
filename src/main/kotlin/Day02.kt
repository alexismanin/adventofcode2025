package fr.amanin.aoc2025.day02

fun List<String>.parse() : Sequence<LongRange> {
    return asSequence()
        .flatMap { it.splitToSequence(',') }
        .map { it.trim() }
        .filter { it.isNotEmpty() }
        .map {
            val sepIdx = it.indexOf("-", 1)
            it.substring(0, sepIdx).toLong()..it.substring(sepIdx+1).toLong()
        }
}

fun String.findRepetition() : String? {
    for (width in 1 .. length / 2) {
        if (length % width != 0) continue
        val pattern = substring(0, width)
        val windows = width until length step width
        val repeat = windows.all { substring(it, it+width) == pattern }
        if (repeat) return pattern
    }
    return null
}

fun LongRange.findSymetricValues(): Sequence<Long> {
    val firstStr = first.toString()
    val lastStr = last.toString()

    val minHalf = if (firstStr.length %2 == 0) {
        val majorHalf = first.toString().substring(0, firstStr.length/2).toLong()
        val minorHalf = first.toString().substring(firstStr.length/2).toLong()
        if (majorHalf < minorHalf)
            majorHalf+1
        else
            majorHalf
    } else {
        "1${"0".repeat(firstStr.length/2)}".toLong()
    }

    val maxHalf = if (lastStr.length % 2 == 0) {
        val majorHalf = last.toString().substring(0, lastStr.length/2).toLong()
        val minorHalf = last.toString().substring(lastStr.length/2).toLong()
        if (minorHalf < majorHalf)
            majorHalf-1
        else
            majorHalf
    } else {
        "9".repeat(lastStr.length/2).toLong()
    }

    assert("$minHalf$minHalf".toLong() >= first) { "Invalid first half"}
    assert("$maxHalf$maxHalf".toLong() <= last) { "Invalid last half" }
    // Init candidate value to the least possible value
    return (minHalf..maxHalf).asSequence()
        .map { "$it$it".toLong() }

}

fun part1(input: List<String>) : Long {
    return input.parse()
        .flatMap { range -> range.findSymetricValues() }
        .onEach { println(it) }
        .sum()
}

fun part2(input: List<String>) : Long {
    return input.parse()
        .flatMap { range -> range }
        .map { it.toString()}
        .filter { it.findRepetition() != null}
        .onEach { println(it) }
        .map { it.toLong() }
        .sum()
}