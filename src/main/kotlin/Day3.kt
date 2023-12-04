import util.*

typealias Day3InputType = ArrayGrid2D<Char>;

class Day3 : Runner {
    val TEST_INPUT: String = """
        467..114..
        ...*......
        ..35..633.
        ......#...
        617*......
        .....+.58.
        ..592.....
        ......755.
        ...${'$'}.*....
        .664.598..
    """.trimIndent()

    override fun run() {
        val day = AocDay("2023".toInt(), "3".toInt())
        val lines = AocInput.lines(day)
        //val lines  = TEST_INPUT.split("\n")
        val grid = ArrayGrid2D(lines.size, lines[0].length) { '.' }
        lines.forEachIndexed { y, line ->
            line.forEachIndexed { x, char ->
                grid[y, x] = char
            }
        }
        part1(grid)
        println("---")
        part2(grid)
    }

    private fun part1(grid: Day3InputType) {
        val candidates = grid.enumerate()
            .filter { grid[it].isSymbol }
            .flatMap { grid.neighbors(it) }
            .distinct()

        val numbers = candidates.mapNotNull { detectNumber(grid, it) }.distinct()
        println(numbers.map(Grid2D<Char>::toString).map(String::toInt).sum())
    }

    private fun part2(grid: Day3InputType) {
        // find all gear candidates, i.e. * symbols
        var gearLike = grid.enumerate()
            .filter { grid[it] == '*' }
            .toSet()

        val ratios = gearLike.mapNotNull { gearRatio(grid, it) }

        println(ratios.sum())
    }

    // returns either null, or a number with one digit in specified position
    private fun detectNumber(grid: Grid2D<Char>, position: IntPoint2D): Grid2D<Char>? {
        if (!grid[position].isDigit()) {
            return null
        }

        var start = position
        var end = position

        val left = IntPoint2D(0, -1)
        val right = IntPoint2D(0, 1)

        val safe = grid.safe()
        while (safe[start + left]?.isDigit() == true) {
            start += left
        }
        while (safe[end + right]?.isDigit() == true) {
            end += right
        }

        return grid[start.y..end.y, start.x..end.x]
    }

    // checks whether a character is one of the following: *=/%#&$-@+
    private val Char.isSymbol: Boolean
        get() = "*=/%#&\$-@+".contains(this)

    // if strictly two distinct numbers are adjacent to a gear,
    // return the product of their integer representations
    fun gearRatio(grid: Grid2D<Char>, gear: IntPoint2D): Int? {
        val neighbors = grid.neighbors(gear)
        val numbers = neighbors.mapNotNull { detectNumber(grid, it) }.distinct()
        if (numbers.size != 2) {
            return null
        }
        return numbers.map(Grid2D<Char>::toString).map(String::toInt).reduce(Int::times)
    }
}