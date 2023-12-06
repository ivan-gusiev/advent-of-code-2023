import util.AocDay
import util.AocInput
import java.math.BigInteger

typealias Day6InputType = List<String>;

class Day6 : Runner {
    var TEST_INPUT = """
        Time:      7  15   30
        Distance:  9  40  200
    """.trimIndent()

    data class TimeDistance(val time: BigInteger, val distance: BigInteger) {
        companion object {
            fun parsePart1(input: List<String>): List<TimeDistance> {
                val lists = input.map { s ->
                    s.replace("Time:", "")
                        .replace("Distance:", "")
                        .trim()
                }.map { s ->
                    s.split(Regex("\\s+")).map(String::toBigInteger)
                }
                return lists[0].zip(lists[1]).map { (time, distance) ->
                    TimeDistance(time, distance)
                }
            }

            fun parsePart2(input: List<String>): TimeDistance {
                val lists = input.map { s ->
                    s.replace("Time:", "")
                        .replace("Distance:", "")
                        .trim()
                }.map { s ->
                    s.replace(Regex("\\s+"), "").toBigInteger()
                }
                return TimeDistance(lists[0], lists[1])
            }
        }
    }

    override fun run() {
        val day = AocDay("2023".toInt(), "6".toInt())
        val lines = AocInput.lines(day)
        //val lines = TEST_INPUT.split("\n")
        part1(lines)
        part2(lines)
    }

    private fun part1(lines: Day6InputType) {
        val input = TimeDistance.parsePart1(lines)
        var result = 1
        for (record in input) {
            val winnerCount = winnerCount(record)
            println("$record -> $winnerCount")
            result *= winnerCount
        }
        println(result)
    }

    private fun part2(lines: Day6InputType) {
        val input = TimeDistance.parsePart2(lines)
        println(winnerCount(input))
    }

    private fun winnerCount(record: TimeDistance): Int {
        val range = sequence<BigInteger> {
            var cur = BigInteger.ZERO
            while (cur < record.time) {
                yield(cur)
                cur += BigInteger.ONE
            }
        }

        return range.map { time: BigInteger -> TimeDistance(time, travelDistance(time, record.time)) }
            .count { it.distance > record.distance }
    }

    private fun travelDistance(startupMs: BigInteger, totalMs: BigInteger): BigInteger {
        val travelMs = totalMs - startupMs
        val result = startupMs * travelMs
        return if (result < BigInteger.ZERO) {
            BigInteger.ZERO
        } else {
            result
        }
    }
}