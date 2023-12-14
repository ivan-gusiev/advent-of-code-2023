package util

interface Grid2D<T> {
    val height: Int
    val width: Int

    operator fun get(yCoord: Int, xCoord: Int): T

    operator fun get(coord: IntPoint2D): T {
        return get(coord.y, coord.x)
    }

    operator fun get(yCoord: IntRange, xCoord: IntRange): SubGrid2D<T> {
        return SubGrid2D(this, IntPoint2D(yCoord.first, xCoord.first), yCoord.count(), xCoord.count())
    }

    operator fun set(yCoord: Int, xCoord: Int, value: T)

    operator fun set(coord: IntPoint2D, value: T) {
        set(coord.y, coord.x, value)
    }

    fun <U> mapWithIndex(transform: (IntPoint2D) -> U): ArrayGrid2D<U> {
        return CalculatedGrid2D(height, width, transform).let(ArrayGrid2D.Factory::clone)
    }

    fun <U> map(transform: (T) -> U): ArrayGrid2D<U> {
        return mapWithIndex { coord -> transform(this[coord]) }
    }

    fun enumerate(): Iterable<IntPoint2D> {
        return IntPoint2D.box(0, 0, height, width)
    }

    fun contains(coord: IntPoint2D): Boolean {
        return coord.y >= 0 && coord.x >= 0 && // no negative
                coord.y < height && coord.x < width // no out of bounds
    }

    fun neighbors(coord: IntPoint2D): Iterable<IntPoint2D> {
        val candidates = IntPoint2D.box(coord.y - 1, coord.x - 1, 3, 3)
        return candidates.filter { pt ->
            pt.x >= 0 && pt.y >= 0 && // no negative
                    pt.x < width && pt.y < height // no out of bounds
                    && (pt.x != coord.x || pt.y != coord.y)
        } // no middle
    }

    fun stringRepresentation(): String {
        return (0..<height).joinToString("\n") { y ->
            (0..<width)
                .map { x -> this[y, x] }
                .joinToString("")
        }
    }

    fun safe(): SafeGridView2D<T> {
        return SafeGridView2D(this)
    }

    fun find(predicate: (T) -> Boolean): IntPoint2D? {
        for (y in 0..<height) {
            for (x in 0..<width) {
                if (predicate(this[y, x])) {
                    return IntPoint2D(y, x)
                }
            }
        }
        return null
    }

    fun row(y: Int): Sequence<T> {
        return sequence {
            for (x in 0..<width) {
                yield(this@Grid2D[y, x])
            }
        }
    }

    fun rows(): Sequence<Sequence<T>> {
        return sequence {
            for (y in 0..<height) {
                yield(row(y))
            }
        }
    }

    fun col(x: Int): Sequence<T> {
        return sequence {
            for (y in 0..<height) {
                yield(this@Grid2D[y, x])
            }
        }
    }

    fun cols(): Sequence<Sequence<T>> {
        return sequence {
            for (x in 0..<width) {
                yield(col(x))
            }
        }
    }

    fun pairwiseEquals(other: Grid2D<T>): Boolean {
        return height == other.height && width == other.width && enumerate().all { coord ->
            this[coord] == other[coord]
        }
    }
}