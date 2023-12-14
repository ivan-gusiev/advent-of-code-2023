import util.AocDay
import util.AocInput
import util.AocSequence.Companion.tap

typealias Day9InputType = List<List<Int>>;

class Day9 : Runner {
    val TEST_INPUT: String = """
        0 3 6 9 12 15
        1 3 6 10 15 21
        10 13 16 21 30 45
    """.trimIndent()

    override fun run() {
        val day = AocDay("2023".toInt(), "9".toInt())
        val lines = AocInput.lines(day)
        //val lines = TEST_INPUT.split("\n")
        val series = lines.map { it.split(" ").map(String::toInt) }
        part1(series)
        part2(series)
    }

    private fun part1(input: Day9InputType) {
        println(input.map(::predict).sum())
    }

    private fun part2(input: Day9InputType) {
        println(input.map(::retrodict).tap(::println).sum())
    }

    private fun predict(input: List<Int>): Int {
        val differences = input.differences()
        val last = input.last()
        return if (allZero(differences)) {
            last
        } else {
            last + predict(differences)
        }
    }

    // does the same as predict, but for the first element instead of the last
    private fun retrodict(input: List<Int>): Int {
        val differences = input.differences()
        val first = input.first()
        return if (allZero(differences)) {
            first
        } else {
            first - retrodict(differences)
        }
    }

    private fun allZero(input: List<Int>): Boolean {
        return input.all { it == 0 }
    }

    private fun List<Int>.differences(): List<Int> {
        return this.zipWithNext { a, b -> b - a }
    }
}