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
    }

    operator fun plus(rhs: IntPoint2D): IntPoint2D {
        return IntPoint2D(y + rhs.y, x + rhs.x)
    }
}