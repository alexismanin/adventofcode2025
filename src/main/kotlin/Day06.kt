package fr.amanin.aoc2025.day06

import fr.amanin.aoc2025.day06.Operation.*

enum class Operation {
    `+`, `*`
}

operator fun Operation.invoke(v1: Long, v2: Long) : Long = when (this) {
    `+` -> v1 + v2
    `*` -> v1 * v2
}

class Problem(val operator: Operation, val operands: LongArray)

fun Problem.solve(): Long = operands.reduce(operator::invoke)

class Worksheet(val problems: List<Problem>)
fun Worksheet.grandTotal() = problems.sumOf(Problem::solve)

fun parseSimple(input: List<String>): Worksheet {
    val tokens = input.map { line -> line.trim().split(Regex("\\s+")) }
    val nbOperands = tokens.size -1
    val nbProblems = tokens[0].size
    val problems = ArrayList<Problem>(nbProblems)
    for (i in 0..<nbProblems) {
        val operands = LongArray(nbOperands) { j ->
            tokens[j][i].toLong()
        }
        val operator = Operation.valueOf(tokens.last()[i])
        problems.add(Problem(operator, operands))
    }
    return Worksheet(problems)
}

fun parseCephalopod(input: List<String>): Worksheet {
    fun String.indexOfAll(chars: List<Char>) : IntArray {
        val buffer = mutableListOf<Int>()
        for (ci in 0..<length) {
            if (get(ci) in chars) {
                buffer.add(ci)
            }
        }
        return buffer.toIntArray()
    }

    val operatorIndices : IntArray = input.last().indexOfAll(Operation.entries.map { it.name[0] })
    val tokens = input.map { line ->
        val lineTokens = ArrayList<String>(operatorIndices.size)
        for (i in 0..<operatorIndices.lastIndex) {
            lineTokens.add(line.substring(operatorIndices[i], operatorIndices[i + 1]-1))
        }
        lineTokens.add(line.substring(operatorIndices.last()))
        lineTokens
    }
    val nbProblems = tokens[0].size
    assert(tokens.subList(0, tokens.size-1).all { token -> token.size == nbProblems }) { "Token parsing incorrect" }
    val problems = ArrayList<Problem>(nbProblems)
    val operandMaxLength = tokens.size -1
    for (i in 0..<nbProblems) {
        val nbOperands = if (i < nbProblems-1) operatorIndices[i+1]  - operatorIndices[i] - 1
                         else tokens.maxOf { tokenLine -> tokenLine[i].length }
        val operands = LongArray(nbOperands) { j ->
            val rawOperand = CharArray(operandMaxLength) { k ->
                val cell = tokens[k][i]
                if (cell.length <= j) ' ' else cell[j]
            }
            String(rawOperand).trim().toLong()
        }
        val operator = Operation.valueOf(tokens.last()[i].trim())
        problems.add(Problem(operator, operands))
    }
    return Worksheet(problems)
}

fun part1(input: List<String>) = parseSimple(input).grandTotal()
fun part2(input: List<String>) = parseCephalopod(input).grandTotal()
