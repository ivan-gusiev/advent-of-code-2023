package util

class SubGrid2D<T>(val parent: Grid2D<T>, val origin: IntPoint2D, override val height: Int, override val width: Int) :
    Grid2D<T> {

    override fun get(yCoord: Int, xCoord: Int): T {
        val target = origin + IntPoint2D(yCoord, xCoord)
        return parent[target]
    }

    override fun set(yCoord: Int, xCoord: Int, value: T) {
        val target = origin + IntPoint2D(yCoord, xCoord)
        parent[target] = value
    }

    override fun toString(): String {
        return stringRepresentation()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SubGrid2D<*>

        if (parent != other.parent) return false
        if (origin != other.origin) return false
        if (height != other.height) return false
        if (width != other.width) return false

        return true
    }

    override fun hashCode(): Int {
        var result = parent.hashCode()
        result = 31 * result + origin.hashCode()
        result = 31 * result + height
        result = 31 * result + width
        return result
    }

}