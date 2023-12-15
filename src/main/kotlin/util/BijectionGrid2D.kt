package util

import kotlin.math.max

class BijectionGrid2D<T>(override val height: Int, override val width: Int, val getter: (IntPoint2D) -> T, val setter: (IntPoint2D, T) -> Unit) : Grid2D<T> {
    companion object {
        private val ROTATION_TRANSFORMS: List<(Grid2D<*>, IntPoint2D) -> IntPoint2D> = listOf(
            { _, (y, x) -> IntPoint2D(y, x) },
            { grid, (y, x) -> IntPoint2D(grid.height - x - 1, y) },
            { grid, (y, x) -> IntPoint2D(grid.height - y - 1, grid.width - x - 1) },
            { grid, (y, x) -> IntPoint2D(x, grid.width - y  - 1) }
        )

        fun <T> coordinateTransform(base: Grid2D<T>, transform: (IntPoint2D) -> IntPoint2D): BijectionGrid2D<T> {
            val topLeft = transform(IntPoint2D(0, 0))
            val bottomRight = transform(IntPoint2D(base.height - 1, base.width - 1))
            val newBottomRight = IntPoint2D(max(topLeft.y, bottomRight.y), max(topLeft.x, bottomRight.x))

            return BijectionGrid2D(newBottomRight.y + 1, newBottomRight.x + 1, { (y, x) ->
                base[transform(IntPoint2D(y, x))]
            }) { (y, x), value ->
                base[transform(IntPoint2D(y, x))] = value
            }
        }

        fun <T> rotateClockwise(base: Grid2D<T>, times: Int): BijectionGrid2D<T> {
            val globalTransform = ROTATION_TRANSFORMS[times % 4]
            val transform = { point: IntPoint2D -> globalTransform(base, point) }
            return coordinateTransform(base, transform)
        }
    }

    override fun get(yCoord: Int, xCoord: Int): T = getter(IntPoint2D(yCoord, xCoord))

    override fun set(yCoord: Int, xCoord: Int, value: T) = setter(IntPoint2D(yCoord, xCoord), value)

    override fun toString(): String = stringRepresentation()
}