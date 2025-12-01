package fr.amanin.aoc2025.day01

val dialStartPosition = 50
val dialRange = 0..99

enum class Direction(val symbol: Char) {
    left('L'),
    right('R');

    companion object {
        fun of (symbol: Char): Direction {
            return requireNotNull(entries.firstOrNull { it.symbol == symbol }) {
                "Unknown direction '$symbol'"
            }
        }
    }
}

data class RotationInstruction(val direction: Direction, val amount: Int)

fun RotationInstruction(description: String): RotationInstruction {
    return RotationInstruction(
        Direction.of(description[0]),
        description.substring(1).toInt()
    )
}

fun part1(input: List<String>): Int {

    fun RotationInstruction.rotate(currentPosition: Int) : Int {
        require(currentPosition in dialRange) { "Invalid starting position: $currentPosition" }
        val dialSpan = (dialRange.last - dialRange.first + 1)
        val amount = amount % dialSpan
        val effect = when (direction) {
            Direction.left -> currentPosition - amount
            Direction.right -> currentPosition + amount
        }

        return if (effect < dialRange.first) {
            dialRange.last - (dialRange.first - effect - 1)
        } else {
            effect % dialSpan
        }
    }

    return input.asSequence()
        .map(::RotationInstruction)
        .runningFold(dialStartPosition) { pos, instruction -> instruction.rotate(pos) }
        .filter { it == 0 }
        .count()
}

fun part2(input: List<String>) : Int {
    data class RotationOutcome(val passedThroughZero: Int, val position: Int)
    fun RotationInstruction.rotate(currentPosition: Int) : RotationOutcome {
        require(currentPosition in dialRange) { "Invalid starting position: $currentPosition" }
        val dialSpan = (dialRange.last - dialRange.first + 1)
        var passedThroughZero = amount / dialSpan
        val amount = amount % dialSpan
        val effect = when (direction) {
            Direction.left if currentPosition == 0 -> dialSpan - amount
            Direction.left -> currentPosition - amount
            Direction.right -> currentPosition + amount
        }

        val position = if (effect in dialRange) {
            effect
        } else {
            val position = if (effect < dialRange.first) {
                dialRange.last - (dialRange.first - effect - 1)
            } else {
                dialRange.first + effect % dialSpan
            }
            if (position != 0) passedThroughZero += 1
            position
        }
        return RotationOutcome(passedThroughZero, position)
    }

    return input.asSequence()
        .map(::RotationInstruction)
        .runningFold(RotationOutcome(0, dialStartPosition)) { outcome, instruction -> instruction.rotate(outcome.position) }
        .map { it.passedThroughZero + (if (it.position == 0) 1 else 0) }
        .onEach { println(it) }
        .sum()
}

