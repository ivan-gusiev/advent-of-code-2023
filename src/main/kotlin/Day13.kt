import util.*
import kotlin.math.min

typealias Day13InputType = List<ArrayGrid2D<Char>>;

class Day13 : Runner {
    val TEST_INPUT: String = """
        #.##..##.
        ..#.##.#.
        ##......#
        ##......#
        ..#.##.#.
        ..##..##.
        #.#.##.#.

        #...##..#
        #....#..#
        ..##..###
        #####.##.
        #####.##.
        ..##..###
        #....#..#
    """.trimIndent()
//
//    val TEST_INPUT: String = """
//        .###.......
//        ####.......
//        .#......#.#
//        ......##..#
//        #.#..#..#..
//        .#..##.##..
//        .#..####...
//        .####.#.#..
//        .#.####....
//        ##.#.##..#.
//        ##.#.##..#.
//    """.trimIndent()

    override fun run() {
        val day = AocDay("2023".toInt(), "13".toInt())
        val lines = AocInput.lines(day)
        //val lines = TEST_INPUT.split("\n")
        val patterns = AocSequence.split(lines, "").map(ArrayGrid2D.Factory::charsOfLines)
        part1(patterns)
        part2(patterns)
    }

    private fun part1(input: Day13InputType) {
        println(input.map(::score).sum())
    }

    private fun score(grid: Grid2D<Char>): Int {
        return allReflections(grid).totalScore()
    }

    data class AllReflections(val reflections: Set<Pair<Int, Boolean>>) {
        fun totalScore(): Int {
            return reflections.sumOf { (score, isVertical) -> score * if (isVertical) 100 else 1 }
        }

        operator fun minus(other: AllReflections): AllReflections {
            return AllReflections(reflections - other.reflections)
        }

        override fun toString(): String {
            return reflections.joinToString(", ") { (score, isVertical) -> "${score}x${if (isVertical) "V" else "H"}" }
        }
    }

    private fun allReflections(grid: Grid2D<Char>): AllReflections {
        val horizontal = reflectionsHorizontal(grid)
        val vertical = reflectionsVertical(grid)
        return AllReflections(horizontal.map { it to false }.toSet() + vertical.map { it to true }.toSet())
    }

    private fun reflectionsHorizontal(grid: Grid2D<Char>): List<Int> {
        val result = ArrayList<Int>()

        for (divider in 1..<grid.width) {
            val rightSpace = grid.width - divider
            val maxSpace = min(divider, rightSpace)
            val leftEdge = divider - maxSpace
            val leftSub = SubGrid2D(grid, IntPoint2D(0, leftEdge), grid.height, maxSpace)
            val rightSub = SubGrid2D(grid, IntPoint2D(0, divider), grid.height, maxSpace)

            if (leftSub.pairwiseEquals(CalculatedGrid2D.reflectHorizontally(rightSub))) {
                result.add(divider)
            }
        }

        return result
    }

    private fun reflectionsVertical(grid: Grid2D<Char>): List<Int> {
        val result = ArrayList<Int>()

        for (divider in 1..<grid.height) {
            val downSpace = grid.height - divider
            val maxSpace = min(divider, downSpace)
            val upEdge = divider - maxSpace
            val upSub = SubGrid2D(grid, IntPoint2D(upEdge, 0), maxSpace, grid.width)
            val downSub = SubGrid2D(grid, IntPoint2D(divider, 0), maxSpace, grid.width)

            if (upSub.pairwiseEquals(CalculatedGrid2D.reflectVertically(downSub))) {
                result.add(divider)
            }
        }

        return result
    }

    private fun part2(input: Day13InputType) {
        println(input.map(::scorePart2).sum())
    }

    private fun scorePart2(grid: Grid2D<Char>): Int {
        val allReflections = allReflections(grid)
        val different = different(grid, allReflections) - allReflections
        println("before: $allReflections, after: $different")
        return different.totalScore()
    }

    private fun different(grid: Grid2D<Char>, noFix: AllReflections): AllReflections {
        return grid.enumerate().map { coord ->
            val fixed = fixMirror(grid, coord)
            allReflections(fixed)
        }.first { it.totalScore() > 0 && it != noFix }
    }

    private fun fixMirror(grid: Grid2D<Char>, coordToFix: IntPoint2D): Grid2D<Char> {
        return CalculatedGrid2D(grid.height, grid.width) { coord ->
            if (coord == coordToFix) {
                changeCell(grid[coord])
            } else {
                grid[coord]
            }
        }
    }

    private fun changeCell(cell: Char): Char {
        return when (cell) {
            '.' -> '#'
            '#' -> '.'
            else -> throw RuntimeException("Unknown cell type: $cell")
        }
    }

}