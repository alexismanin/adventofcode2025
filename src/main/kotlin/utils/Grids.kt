package fr.amanin.aoc2025.utils


interface Grid<out V> {
    data class Cursor<out V>(val column: Int, val row: Int, val value: V)

    val width: Int
    val height: Int
    operator fun get(column: Int, row: Int): V
    fun copy(startCol: Int = 0, startRow: Int = 0, width: Int = this.width, height: Int = this.height): Grid<V>

    fun asSequence(): Sequence<V>

    fun asIndexedSequence(startCol: Int = 0, startRow: Int = 0, width: Int = this.width, height: Int = this.height): Sequence<Cursor<V>> = sequence {
        for (i in startRow..<(startRow+height)) {
            for (j in startCol..<(startCol+width)) {
                yield(Cursor(j, i, get(column = j, row = i)))
            }
        }
    }
}

interface MutableGrid<V> : Grid<V> {
    operator fun set(column: Int, row: Int, value: V)
    override fun copy(startCol: Int, startRow: Int, width: Int, height: Int): MutableGrid<V>
}

private fun Grid<Any>.requireInBounds(column: Int, row: Int) {
    require(column in 0 ..< width) { "Column $column out of bounds (0..<$width)" }
    require(row in 0 ..< height) { "Row $row out of bounds (0..<$height)" }
}

private class TextGrid(override val width: Int, override val height: Int, private val rows: Array<CharArray>) : MutableGrid<Char> {
    override fun get(column: Int, row: Int): Char {
        requireInBounds(column, row)
        return rows[row][column]
    }

    override fun set(column: Int, row: Int, value: Char) {
        rows[row][column] = value
    }

    override fun copy(startCol: Int, startRow: Int, width: Int, height: Int): MutableGrid<Char> {
        requireInBounds(startCol, startRow)
        requireInBounds(startCol + width-1, startRow + height-1)
        val copy = Array(height) { i ->
            val entireRow = rows[startRow + i]
            entireRow.copyOfRange(startCol, startCol + width)
        }

        return TextGrid(width, height, copy)
    }

    override fun toString() = rows.joinToString(separator = "\n") { String(it) }

    override fun asSequence() = sequence {
        for (row in rows) {
            yieldAll(row.asSequence())
        }
    }
}

fun textGrid(rows: List<String>, padIfNeeded : Boolean = false): MutableGrid<Char> {
    if (padIfNeeded) TODO("Implement padding: check widest row any copy others, adding padding to the right")
    require(rows.isNotEmpty()) { "Rows must not be empty" }
    val width = rows[0].length
    require(rows.all { row -> row.length == width }) { "All rows must have the same width" }
    return TextGrid(width, rows.size, Array(rows.size) { i -> rows[i].toCharArray() })
}
