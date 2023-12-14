import util.AocDay
import util.AocInput
import util.ArrayGrid2D
import util.IntPoint2D
import java.math.BigInteger

typealias Day11InputType = ArrayGrid2D<Boolean>;

class Day11 : Runner {
    val TEST_INPUT: String = """
        ...#......
        .......#..
        #.........
        ..........
        ......#...
        .#........
        .........#
        ..........
        .......#..
        #...#.....
    """.trimIndent()

    override fun run() {
        val day = AocDay("2023".toInt(), "11".toInt())
        val lines = AocInput.lines(day)
        //val lines = TEST_INPUT.split("\n")
        val grid = ArrayGrid2D.charsOfLines(lines).map { it == '#' }
        part1(grid)
        part2(grid)
    }

    private fun part1(input: Day11InputType) {
        val grid = expand(input)
        val galaxies = grid.enumerate().filter { grid[it] }
        var total = sumOfDistances(galaxies)

        println(total)
    }

    private fun part2(grid: Day11InputType) {
        val galaxies = grid.enumerate().filter { grid[it] }
        val (rowsToExpand, colsToExpand) = expandingRowsAndCols(grid)
        val timeElapsed = 1_000_000 - 1
        val expandedGalaxies = galaxies.map { coord ->
            val (row, col) = coord
            val rowsExpanded = rowsToExpand.count { it <= row }
            val colsExpanded = colsToExpand.count { it <= col }
            IntPoint2D(row + rowsExpanded * timeElapsed, col + colsExpanded * timeElapsed)
        }

        println(sumOfDistances(expandedGalaxies))
    }

    private fun expandingRowsAndCols(grid: Day11InputType): Pair<List<Int>, List<Int>> {
        fun vectorsToExpand(vector: Sequence<Sequence<Boolean>>) = vector
            .mapIndexed { index, row -> Pair(index, row) }
            .filter { it.second.all { x -> !x } }
            .map { it.first }
            .toList()
            .reversed()

        val rowsToExpand = vectorsToExpand(grid.rows())
        val colsToExpand = vectorsToExpand(grid.cols())

        return Pair(rowsToExpand, colsToExpand)
    }

    private fun expand(grid: Day11InputType): Day11InputType {
        val rowsAndColsToExpand = expandingRowsAndCols(grid)
        var result = ArrayGrid2D.clone(grid)
        val rowsToExpand = rowsAndColsToExpand.first
        val colsToExpand = rowsAndColsToExpand.second

        for (row in rowsToExpand) {
            result = ArrayGrid2D.insertRow(result, row, List(result.width) { false })
        }

        for (col in colsToExpand) {
            result = ArrayGrid2D.insertColumn(result, col, List(result.height) { false })
        }

        return result
    }


    private fun sumOfDistances(galaxies: List<IntPoint2D>): BigInteger {
        var total = BigInteger.ZERO

        for (l in 0..<galaxies.size) {
            for (r in l + 1..<galaxies.size) {
                total += BigInteger.valueOf(galaxies[r].manhattanDistanceTo(galaxies[l]).toLong())
            }
        }
        return total
    }

}