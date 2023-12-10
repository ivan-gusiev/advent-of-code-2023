package util

data class IntPoint2D(val y: Int, val x: Int) {
    companion object Factory {
        fun box(y: Int, x: Int, height: Int, width: Int): Iterable<IntPoint2D> {
            return (y..<y + height)
                .flatMap { y ->
                    (x..<x + width)
                        .map { x -> IntPoint2D(y, x) }
                }
        }

        val UP = IntPoint2D(-1, 0)
        val DOWN = IntPoint2D(1, 0)
        val LEFT = IntPoint2D(0, -1)
        val RIGHT = IntPoint2D(0, 1)
    }

    operator fun plus(rhs: IntPoint2D): IntPoint2D {
        return IntPoint2D(y + rhs.y, x + rhs.x)
    }

    operator fun minus(rhs: IntPoint2D): IntPoint2D {
        return IntPoint2D(y - rhs.y, x - rhs.x)
    }
}