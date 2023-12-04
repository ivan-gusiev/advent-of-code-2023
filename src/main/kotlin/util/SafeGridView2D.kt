package util

class SafeGridView2D<T>(val grid: Grid2D<T>) {
    operator fun get(coord: IntPoint2D): T? {
        return if (grid.contains(coord)) grid[coord] else null
    }

    operator fun get(y: Int, x: Int): T? {
        return get(IntPoint2D(y, x))
    }

    operator fun set(coord: IntPoint2D, value: T) {
        if (grid.contains(coord)) grid[coord] = value
    }

    operator fun set(y: Int, x: Int, value: T) {
        grid[IntPoint2D(y, x)] = value
    }
}
