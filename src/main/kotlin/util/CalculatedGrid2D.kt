package util

class CalculatedGrid2D<T>(override val height: Int, override val width: Int, val transform: (IntPoint2D) -> T) :
    Grid2D<T> {
    companion object {
        fun <T> reflectHorizontally(other: Grid2D<T>): CalculatedGrid2D<T> {
            return CalculatedGrid2D(other.height, other.width) { (y, x) ->
                other[y, other.width - x - 1]
            }
        }

        fun <T> reflectVertically(other: Grid2D<T>): CalculatedGrid2D<T> {
            return CalculatedGrid2D(other.height, other.width) { (y, x) ->
                other[other.height - y - 1, x]
            }
        }

        fun <T> transpose(other: Grid2D<T>): CalculatedGrid2D<T> {
            return CalculatedGrid2D(other.width, other.height) { (y, x) ->
                other[x, y]
            }
        }
    }

    override fun get(yCoord: Int, xCoord: Int): T {
        return transform(IntPoint2D(yCoord, xCoord))
    }

    override fun set(yCoord: Int, xCoord: Int, value: T) {
        throw RuntimeException("Cannot set values on injection grid")
    }

    override fun toString(): String {
        return stringRepresentation()
    }
}