package util

interface Grid2D<T> {
    val height: Int
    val width: Int

    operator fun get(yCoord: Int, xCoord: Int): T

    operator fun get(coord: IntPoint2D): T {
        return get(coord.y, coord.x)
    }

    operator fun set(yCoord: Int, xCoord: Int, value: T)

    operator fun set(coord: IntPoint2D, value: T) {
        set(coord.y, coord.x, value)
    }

    fun <U> map(transform: (IntPoint2D, T) -> U): ArrayGrid2D<U> {
        return InjectionGrid2D(transform, this).let(ArrayGrid2D.Factory::clone)
    }

    fun <U> map(transform: (T) -> U): ArrayGrid2D<U> {
        return map { _, value -> transform(value) }
    }

    fun enumerate(): Iterable<IntPoint2D> {
        return IntPoint2D.box(0, 0, height, width)
    }

    fun neighbors(coord: IntPoint2D): Iterable<IntPoint2D> {
        val candidates = IntPoint2D.box(coord.y - 1, coord.x - 1, 3, 3)
        return candidates.filter { pt -> pt.x >= 0 && pt.y >= 0 && // no negative
                pt.x < width && pt.y < height // no out of bounds
                && (pt.x != coord.x || pt.y != coord.y) } // no middle
    }
}