import util.*
import util.IntPoint2D.Factory.DOWN
import util.IntPoint2D.Factory.LEFT
import util.IntPoint2D.Factory.RIGHT
import util.IntPoint2D.Factory.UP
import kotlin.math.abs
import kotlin.math.min

typealias Day10InputType = ArrayGrid2D<Day10.PipeType>;

class Day10 : Runner {

    val TEST_INPUT = """
        FF7FSF7F7F7F7F7F---7
        L|LJ||||||||||||F--J
        FL-7LJLJ||||||LJL-77
        F--JF--7||LJLJ7F7FJ-
        L---JF-JLJ.||-FJLJJ7
        |F|F-JF---7F7-L7L|7|
        |FFJF7L7F-JF7|JL---7
        7-L-JL7||F7|L7F-7F7|
        L.L7LFJ|||||FJL7||LJ
        L7JLJL-JLJLJL--JLJ.L
    """.trimIndent()

    override fun run() {
        val day = AocDay("2023".toInt(), "10".toInt())
        val lines = AocInput.lines(day)
        //val lines = TEST_INPUT.split("\n")
        val grid = ArrayGrid2D.charsOfLines(lines)
        val pipeGrid = grid.map(PipeType::fromInput)
        part1(pipeGrid)
        part2(pipeGrid)
    }

    private fun part1(grid: Day10InputType) {
        val lengths = findPath(grid).map(Pair<PipeType, Int>::second)

        val max = lengths.enumerate().filter { lengths[it] != Int.MAX_VALUE }.maxBy { lengths[it] }
        println("$max -> ${lengths[max]}")
    }

    private fun part2(input: Day10InputType) {
        val path = findPath(input).map(Pair<PipeType, Int>::first)
        val innerCells = findInnerCells(path)
        println(input)
        println(path)
        println(innerCells)
        println(innerCells.enumerate().map { innerCells[it] }.filter { it == PipeType.INSIDE }.count())
    }

    private fun findInnerCells(path: Grid2D<PipeType>): Grid2D<PipeType> {
        fun findInnerCellsInRow(path: Grid2D<PipeType>, y: Int) {
            var inInner = false
            var upDown = 0

            for (x in 0..<path.width) {
                val pipe = path[y, x]

                if (pipe == PipeType.HORIZONTAL) {
                    continue
                }

                if (pipe == PipeType.VERTICAL) {
                    inInner = !inInner
                    continue
                }

                if (pipe != PipeType.NONE) {
                    upDown += pipe.upDown()
                    print(upDown)

                    if (upDown == 0) {
                        continue
                    }
                    if (abs(upDown) == 2) {
                        inInner = !inInner
                        upDown = 0
                        continue
                    }
                } else {
                    path[y, x] = if (inInner) PipeType.INSIDE else PipeType.OUTSIDE
                }
            }
        }

        val result = ArrayGrid2D.clone(path)
        for (y in 0..<result.height) {
            findInnerCellsInRow(result, y)
        }
        return result
    }

    private fun findPath(pipeGrid: Grid2D<PipeType>): Grid2D<Pair<PipeType, Int>> {
        val startCoord = pipeGrid.find { it == PipeType.START }!!
        val loopCandidates = pipeGrid.neighbors(startCoord).filter { pipeGrid[it] != PipeType.NONE }
        val loopStarts = loopCandidates.filter { candidate ->
            val candidateType = pipeGrid[candidate]
            val connections = candidateType.connections()
            connections.any { candidate + it == startCoord }
        }
        val results = pipeGrid.map { _ -> Pair(PipeType.NONE, Int.MAX_VALUE) }
        results[startCoord] = Pair(PipeType.connect(loopStarts.map { it - startCoord })!!, 0)

        loopStarts.forEach { loopStart ->
            var previous = startCoord
            var current = loopStart
            var steps = 0
            while (true) {
                steps++
                val next = next(pipeGrid, previous, current)

                if (current != startCoord) {
                    results[current] = Pair(pipeGrid[current], min(results[current].second, steps))
                }

                if (next == startCoord) {
                    break
                }
                previous = current
                current = next
            }
        }

        return results
    }


    private fun next(grid: Grid2D<PipeType>, previous: IntPoint2D, current: IntPoint2D): IntPoint2D {
        val currentType = grid[current]
        val connections = currentType.connections()
        val nextCandidates = connections.map { current + it }
        val next = nextCandidates.firstOrNull { it != previous && grid[it] != PipeType.NONE }
        return next ?: throw IllegalStateException("No next pipe found")
    }

    enum class PipeType {
        NONE,
        START,
        HORIZONTAL,
        VERTICAL,
        NORTH_EAST,
        NORTH_WEST,
        SOUTH_EAST,
        SOUTH_WEST,
        INSIDE,
        OUTSIDE;

        companion object {
            private val CONNECTIONS: Map<Set<IntPoint2D>, PipeType> = hashMapOf(
                setOf(UP, DOWN) to VERTICAL,
                setOf(LEFT, RIGHT) to HORIZONTAL,
                setOf(UP, RIGHT) to NORTH_EAST,
                setOf(UP, LEFT) to NORTH_WEST,
                setOf(DOWN, LEFT) to SOUTH_WEST,
                setOf(DOWN, RIGHT) to SOUTH_EAST
            )


            fun connect(points: List<IntPoint2D>): PipeType? {
                return CONNECTIONS[points.toSet()]
            }

            fun fromInput(char: Char): PipeType {
                return when (char) {
                    '.' -> NONE
                    'S' -> START
                    '|' -> VERTICAL
                    '-' -> HORIZONTAL
                    'L' -> NORTH_EAST
                    'J' -> NORTH_WEST
                    '7' -> SOUTH_WEST
                    'F' -> SOUTH_EAST
                    else -> throw IllegalArgumentException("Unknown pipe type: $char")
                }
            }
        }

        fun toInput(): Char {
            return when (this) {
                NONE -> '.'
                START -> 'S'
                VERTICAL -> '|'
                HORIZONTAL -> '-'
                NORTH_EAST -> 'L'
                NORTH_WEST -> 'J'
                SOUTH_WEST -> '7'
                SOUTH_EAST -> 'F'
                INSIDE -> 'I'
                OUTSIDE -> 'O'
            }
        }

        fun toUnicode(): String {
            return when (this) {
                NONE -> "·"
                START -> "*"
                VERTICAL -> "│"
                HORIZONTAL -> "─"
                NORTH_EAST -> "└"
                NORTH_WEST -> "┘"
                SOUTH_WEST -> "┐"
                SOUTH_EAST -> "┌"
                INSIDE -> "I"
                OUTSIDE -> "O"
            }
        }

        fun connections(): List<IntPoint2D> {
            return when (this) {
                VERTICAL -> listOf(UP, DOWN)
                HORIZONTAL -> listOf(LEFT, RIGHT)
                NORTH_EAST -> listOf(UP, RIGHT)
                NORTH_WEST -> listOf(UP, LEFT)
                SOUTH_WEST -> listOf(DOWN, LEFT)
                SOUTH_EAST -> listOf(DOWN, RIGHT)
                else -> emptyList()
            }
        }

        fun upDown(): Int {
            return when (this) {
                NORTH_EAST -> -1
                NORTH_WEST -> 1
                SOUTH_WEST -> -1
                SOUTH_EAST -> 1
                else -> 0
            }
        }

        override fun toString(): String = this.toUnicode()
    }


}