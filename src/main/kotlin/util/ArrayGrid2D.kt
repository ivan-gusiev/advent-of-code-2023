package util

class ArrayGrid2D<T>(override val height: Int, override val width: Int, init: () -> T) : Grid2D<T> {
    companion object Factory {
        fun ints(height: Int, width: Int): ArrayGrid2D<Int> {
            return ArrayGrid2D(height, width) { 0 }
        }

        fun doubles(height: Int, width: Int): ArrayGrid2D<Double> {
            return ArrayGrid2D(height, width) { 0.0 }
        }

        fun strings(height: Int, width: Int): ArrayGrid2D<String> {
            return ArrayGrid2D(height, width) { "" }
        }

        fun charsOfLines(lines: List<String>): ArrayGrid2D<Char> {
            val grid = ArrayGrid2D(lines.size, lines[0].length) { '.' }
            lines.forEachIndexed { y, line ->
                line.forEachIndexed { x, char ->
                    grid[y, x] = char
                }
            }
            return grid
        }

        fun <T> clone(grid: Grid2D<T>): ArrayGrid2D<T> {
            val target = ArrayGrid2D<T>(grid.height, grid.width) { grid[0, 0] }
            for (y in 0..<grid.height) {
                for (x in 0..<grid.width) {
                    target[y, x] = grid[y, x]
                }
            }
            return target
        }
    }

    private val items: ArrayList<ArrayList<T>> = ArrayList(height)

    init {
        for (y in 0..<height) {
            val row = ArrayList<T>(width)
            for (x in 0..<width) {
                row.add(init())
            }
            items.add(row)
        }
    }

    override operator fun get(yCoord: Int, xCoord: Int): T {
        return items[yCoord][xCoord]
    }

    override operator fun set(yCoord: Int, xCoord: Int, value: T) {
        items[yCoord][xCoord] = value
    }

    override fun toString(): String {
        return stringRepresentation()
    }
}