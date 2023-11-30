package util

class SubGrid2D<T>(val parent: Grid2D<T>, val origin: IntPoint2D, override val height: Int, override val width: Int): Grid2D<T> {
    override fun get(yCoord: Int, xCoord: Int): T {
        val target = origin + IntPoint2D(yCoord, xCoord)
        return parent[target]
    }

    override fun set(yCoord: Int, xCoord: Int, value: T) {
        val target = origin + IntPoint2D(yCoord, xCoord)
        parent[target] = value
    }
}