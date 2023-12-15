
import util.*

typealias Day14InputType = ArrayGrid2D<Char>

class Day14 : Runner {
    val TEST_INPUT: String = """
        O....#....
        O.OO#....#
        .....##...
        OO.#O....O
        .O.....O#.
        O.#..O.#.#
        ..O..#O..O
        .......O..
        #....###..
        #OO..#....
    """.trimIndent()

    override fun run() {
        val day = AocDay("2023".toInt(), "14".toInt())
        val lines = AocInput.lines(day)
        //val lines = TEST_INPUT.split("\n")
        val grid = ArrayGrid2D.charsOfLines(lines)
        part1(grid)
        part2(grid)
    }

    private fun part1(input: Day14InputType) {
        fallUp(input)
        println(load(input))
    }

    private fun load(grid: Grid2D<Char>): Int {
        return grid.enumerate().filter { grid[it] == 'O' }.sumOf { grid.height - it.y }
    }

    private fun fallUp(grid: Grid2D<Char>) {
        for (x in 0..<grid.width) {
            val col = SubGrid2D(grid, IntPoint2D(0, x), grid.height, 1)
            val squareIndices = col.enumerate().filter { col[it] == '#' }.map { it.y }
            val boundaries = listOf(-1) + squareIndices + listOf(grid.height)
            val ranges = boundaries.zipWithNext().map { (a, b) -> a + 1 ..< b }
            for (range in ranges) {
                val pts = range.map { IntPoint2D(it, 0) }
                val roundCount = pts.count { col[it] == 'O' }
                range.last - range.first + 1 - roundCount
                pts.forEachIndexed { index, intPoint2D ->
                    col[intPoint2D] = if (index < roundCount) 'O' else '.'
                }
            }
        }
    }

    private fun part2(input: Day14InputType) {
        idToGrid[0] = input
        var curId = 0
        repeat(1_000_000_000) { i ->
            curId = memoizedSpinCycle(curId)
            if (i % 100_000 == 0) println("${i / 10_000_000.0}%")
        }
        println(load(idToGrid[curId]!!))
    }

    fun spinCycle(input: Grid2D<Char>): Grid2D<Char> {
        val grid = ArrayGrid2D.clone(input)
        fallUp(grid)
        fallUp(BijectionGrid2D.rotateClockwise(grid, 1))
        fallUp(BijectionGrid2D.rotateClockwise(grid, 2))
        fallUp(BijectionGrid2D.rotateClockwise(grid, 3))
        return grid
    }


    private val idToNext = mutableMapOf<Int, Int>()
    private val idToGrid = mutableMapOf<Int, Grid2D<Char>>()
    private val gridToId = mutableMapOf<Grid2D<Char>, Int>()
    private fun memoizedSpinCycle(id: Int): Int {
        if (idToNext.contains(id)) return idToNext[id]!!
        val grid = idToGrid[id]!!
        val next = spinCycle(grid)
        if (next in gridToId) {
            val nextId = gridToId[next]!!
            idToNext[id] = nextId
            idToGrid[nextId] = next
            return nextId
        } else {
            val nextId = idToGrid.size
            idToNext[id] = nextId
            idToGrid[nextId] = next
            gridToId[next] = nextId
            return nextId
        }
    }
}