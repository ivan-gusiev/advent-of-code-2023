
import util.AocDay
import util.AocInput

class Day1: Runner {
    val TEST_INPUT = """
        1abc2
        pqr3stu8vwx
        a1b2c3d4e5f
        treb7uchet
    """.trimIndent()

    val TEST_INPUT_2 = """
        5eight82sixtwonev
    """.trimIndent()

    override fun run() {
        val day = AocDay("2023".toInt(), "1".toInt())
        val lines = AocInput.lines(day)
        //val lines = TEST_INPUT_2.split('\n')
        var result = 0

        for (line in lines) {
            val number = firstDigitFromTheLeft(line) + firstDigitFromTheRight(line)
            println("$line, $number")
            result += number.toInt()
        }
        println(result)
    }

    private fun firstDigitFromTheLeft(input: String): String {
        val matches = ArrayList<MatchResult>();
        var startIndex = 0;

        while (startIndex < input.length) {
            val match = DIGIT_REGEX.find(input, startIndex) ?: break
            startIndex = match.range.first + 1
            matches.add(match)
        }

        return digitizeDigit(matches.first())
    }

    private fun firstDigitFromTheRight(input: String): String {
        val matches = ArrayList<MatchResult>();
        var startIndex = 0;

        while (startIndex < input.length) {
            val match = DIGIT_REGEX.find(input, startIndex) ?: break
            startIndex = match.range.first + 1
            matches.add(match)
        }

        return digitizeDigit(matches.last())
    }

    private val DIGIT_REGEX = Regex("one|two|three|four|five|six|seven|eight|nine|1|2|3|4|5|6|7|8|9")
    private fun digitizeDigit(input: MatchResult): String {
        return input.value.replace("one", "1")
            .replace("two", "2")
            .replace("three", "3")
            .replace("four", "4")
            .replace("five", "5")
            .replace("six", "6")
            .replace("seven", "7")
            .replace("eight", "8")
            .replace("nine", "9")
    }
}