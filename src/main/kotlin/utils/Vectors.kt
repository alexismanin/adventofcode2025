package utils

interface Vector {
    val dimension: Int
    operator fun get(dim: Int): Long
    fun squaredNorm(): Long
}

interface Vector2D : Vector {
    override val dimension get() = 2
    val x: Long get() = get(0)
    val y: Long get() = get(1)

    operator fun minus(other: Vector2D): Vector2D
}

interface Vector3D : Vector {
    override val dimension get() = 3
    val x: Long get() = get(0)
    val y: Long get() = get(1)
    val z: Long get() = get(2)

    operator fun minus(other: Vector3D): Vector3D
}


private open class CompactVector(val coords: LongArray) : Vector {
    override val dimension: Int get() = coords.size
    override fun get(dim: Int) = coords[dim]
    override fun squaredNorm() = coords.sumOf { coord -> coord * coord }
}

private class CompactVector3D(coords: LongArray) : CompactVector(coords), Vector3D {

    init { require(coords.size == 3) }

    override val dimension: Int get() = 3

    override fun minus(other: Vector3D): Vector3D {
        return CompactVector3D(LongArray(3) { i -> get(i) - other.get(i) })
    }
}

private class CompactVector2D(coords: LongArray) : CompactVector(coords), Vector2D {

    init { require(coords.size == 2) }

    override val dimension: Int get() = 2

    override fun minus(other: Vector2D): Vector2D {
        return CompactVector2D(LongArray(2) { i -> get(i) - other.get(i) })
    }
}

fun Vector2D(x: Long, y: Long) : Vector2D = CompactVector2D(longArrayOf(x, y))

fun Vector3D(x: Long, y: Long, z: Long) : Vector3D = CompactVector3D(longArrayOf(x, y, z))
