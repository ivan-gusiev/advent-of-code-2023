package util

class InjectionGrid2D<T, U>(val transform: (IntPoint2D, T) -> U, val source: Grid2D<T>) : Grid2D<U> {
    override val height: Int = source.height
    override val width: Int = source.width

    override fun get(yCoord: Int, xCoord: Int): U {
        return transform(IntPoint2D(yCoord, xCoord), source[yCoord, xCoord])
    }

    override fun set(yCoord: Int, xCoord: Int, value: U) {
        throw RuntimeException("Cannot set values on injection grid")
    }

    override fun toString(): String {
        return stringRepresentation()
    }
}