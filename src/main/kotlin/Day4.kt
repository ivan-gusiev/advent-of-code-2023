import util.AocDay
import util.AocInput

typealias Day4InputType = List<Day4.ScratchCard>;

class Day4 : Runner {
    val TEST_INPUT: String = """
        Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
        Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19
        Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1
        Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83
        Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36
        Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11
    """.trimIndent()

    override fun run() {
        val day = AocDay("2023".toInt(), "4".toInt())
        val lines = AocInput.lines(day)
        //val lines = TEST_INPUT.split("\n")
        val cards = lines.map(ScratchCard::parse)

        part1(cards)
        part2(cards)
    }

    private fun part1(input: Day4InputType) {
        val scores = input.map(ScratchCard::part1Score)
        println(scores.sum())
    }

    private fun part2(input: Day4InputType) {
        val calculator = ScoreCalculator(input)
        val totalScore = input
            .map { x -> calculator.scoreByIndex(x.id) }
            .reduce(CardDistribution::plus)
            .ids
            .reduce(Int::plus)

        println(totalScore)
    }

    class ScoreCalculator(private val cards: List<ScratchCard>) {
        private val distributionByIndexCache = mutableMapOf<Int, CardDistribution>()

        private fun getByIndex(i: Int): ScratchCard {
            return cards[i - 1]
        }

        private fun calculateDistribution(i: Int): CardDistribution {
            val nums = IntArray(cards.size)
            val card = getByIndex(i)
            val winnerIds = card.winnerIds()

            nums[i - 1] = 1
            var distro = CardDistribution(nums)
            winnerIds.forEach { distro += scoreByIndex(it) }
            return distro
        }

        fun scoreByIndex(i: Int): CardDistribution {
            return distributionByIndexCache.getOrPut(i) {
                calculateDistribution(i)
            }
        }
    }

    data class CardDistribution(val ids: IntArray) {
        operator fun plus(other: CardDistribution): CardDistribution {
            return CardDistribution(ids.zip(other.ids) { a, b -> a + b }.toIntArray())
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as CardDistribution

            return ids.contentEquals(other.ids)
        }

        override fun hashCode(): Int {
            return ids.contentHashCode()
        }

        override fun toString(): String {
            return ids.joinToString(",")
        }
    }

    data class ScratchCard(val id: Int, val winningNumbers: List<Int>, val scratchNumbers: List<Int>) {
        companion object {
            // parses text like a line of from TEST_INPUT and returns a ScratchCard
            fun parse(input: String): ScratchCard {
                val parts = input.replace("Card ", "").split(":")
                val id = parts[0].trim().toInt()
                val numbers = parts[1].split("|").map { it.trim() }
                val winningNumbers = numbers[0].split(Regex("\\s+")).map(String::toInt)
                val scratchNumbers = numbers[1].split(Regex("\\s+")).map(String::toInt)
                return ScratchCard(id, winningNumbers, scratchNumbers)
            }
        }

        private fun winningCount(): Int {
            return scratchNumbers.count { it in winningNumbers }
        }

        // return 0 if winningCount() == 0, and 2^i, where i is the number of winning numbers
        fun part1Score(): Int {
            val count = winningCount()
            if (count == 0) return 0

            return 1 shl (count - 1)
        }

        fun winnerIds(): List<Int> {
            return (id + 1..id + winningCount()).toList()
        }

        // returns the representation of ScratchCard as a String, like TEST_INPUT
        override fun toString(): String {
            return "Card $id: ${winningNumbers.joinToString(" ")} | ${scratchNumbers.joinToString(" ")}"
        }
    }
}