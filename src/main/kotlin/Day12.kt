import util.AocDay
import util.AocSequence.Companion.tap

typealias Day12InputType = List<Day12.SpringConditions>;

class Day12 : Runner {
    val TEST_INPUT: String = """
        ???.### 1,1,3
        .??..??...?##. 1,1,3
        ?#?#?#?#?#?#?#? 1,3,1,6
        ????.#...#... 4,1,1
        ????.######..#####. 1,6,5
        ?###???????? 3,2,1
    """.trimIndent()

    override fun run() {
        val day = AocDay("2023".toInt(), "12".toInt())
        //val lines = AocInput.lines(day)
        val lines = TEST_INPUT.split("\n")
        val conditions = lines.map(SpringConditions::fromInput)
        part1(conditions)
        part2(conditions)
    }

    data class SpringConditions(val record: String, val runs: List<Int>) {
        companion object {
            fun fromInput(input: String): SpringConditions {
                val (record, runs) = input.split(" ")
                val runsList = runs.split(",").map(String::toInt)
                return SpringConditions(record, runsList)
            }
        }

        fun isValid(): Boolean {
            require(!record.contains('?'))
            val clusters = record.split(".").filter { it.isNotEmpty() }
            val counts = clusters.map { it.length }
            return runs == counts
        }

        fun arrangements(): Sequence<SpringConditions> {
            val wildcard = record.indices.find { record[it] == '?' } ?: return sequenceOf(this)
            val dot = record.replaceRange(wildcard, wildcard + 1, ".")
            val hash = record.replaceRange(wildcard, wildcard + 1, "#")

            return sequence {
                yieldAll(SpringConditions(dot, runs).arrangements())
                yieldAll(SpringConditions(hash, runs).arrangements())
            }
        }

        fun validArrangementsCount(): Int {
            return arrangements().filter { it.isValid() }.count()
        }

        operator fun times(other: Int): SpringConditions {
            return SpringConditions(record.repeat(other), List(other) { runs }.flatten())
        }
    }

    private fun part1(input: Day12InputType) {
        println(input.asSequence().map(SpringConditions::validArrangementsCount).tap(::println).sum())
    }

    private fun part2(input: Day12InputType) {
        println(input.asSequence().map { it * 5 }.map(SpringConditions::validArrangementsCount).tap(::println).sum())
    }
}