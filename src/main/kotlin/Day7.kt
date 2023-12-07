import util.AocDay
import util.AocInput
import util.AocSequence

typealias Day7InputType = List<String>;

class Day7 : Runner {
    val TEST_INPUT: String = """
        32T3K 765
        T55J5 684
        KK677 28
        KTJJT 220
        QQQJA 483
    """.trimIndent()

    override fun run() {
        val day = AocDay("2023".toInt(), "7".toInt())
        val lines = AocInput.lines(day)
        //val lines = TEST_INPUT.split("\n")
        part1(lines)
        part2(lines)
    }

    private fun part1(input: Day7InputType) {
        val handsWithBids = input.map(HandWithBid::parse)
        val sorted = handsWithBids.sortedBy { hwb -> hwb.hand }

        // equal to sorted.indices but 1-indexed
        val ranks = (1..sorted.size).toList()
        println(ranks.zip(sorted).sumOf { (rank, hwb) -> rank * hwb.bid })
    }

    private fun part2(input: Day7InputType) {
        val handsWithBids = input.map(HandWithBid::parse)
        val sorted = handsWithBids.sortedWith(HandWithBidComparator())

        // equal to sorted.indices but 1-indexed
        val ranks = (1..sorted.size).toList()
        sorted.zip(ranks).forEach { (hwb, rank) -> println("$hwb is ${hwb.hand.jokerType()} -> $rank") }
        println(ranks.zip(sorted).sumOf { (rank, hwb) -> rank * hwb.bid })
    }

    data class Card private constructor(val value: Char) : Comparable<Card> {
        companion object {
            //const val VALUES = "23456789TJQKA" //<- uncomment for part 1
            const val VALUES = "J23456789TQKA"

            fun of(value: Char): Card {
                if (VALUES.contains(value)) {
                    return Card(value)
                } else {
                    throw IllegalArgumentException("Invalid card value: $value")
                }
            }

            fun all(): List<Card> {
                return VALUES.map(Card::of)
            }
        }

        private fun index(): Int {
            return VALUES.indexOf(value)
        }

        override fun compareTo(other: Card): Int {
            return index().compareTo(other.index())
        }

        override fun toString(): String {
            return value.toString()
        }
    }

    enum class HandType {
        HIGH_CARD,
        PAIR,
        TWO_PAIR,
        THREE_OF_A_KIND,
        FULL_HOUSE,
        FOUR_OF_A_KIND,
        FIVE_OF_A_KIND
    }

    data class Hand(val cards: List<Card>) : Comparable<Hand> {
        companion object {
            fun parse(input: String): Hand {
                val cards = input.map(Card::of)
                return Hand(cards)
            }
        }

        fun type(): HandType {
            val counts = AocSequence.spectrum(cards)
            return when (counts.size) {
                1 -> HandType.FIVE_OF_A_KIND
                2 -> {
                    if (counts.values.contains(4)) {
                        HandType.FOUR_OF_A_KIND
                    } else {
                        HandType.FULL_HOUSE
                    }
                }

                3 -> {
                    if (counts.values.contains(3)) {
                        HandType.THREE_OF_A_KIND
                    } else {
                        HandType.TWO_PAIR
                    }
                }

                4 -> HandType.PAIR
                5 -> HandType.HIGH_CARD
                else -> throw IllegalStateException("Invalid hand: $this")
            }
        }

        // calculates type, but now joker can be any card
        // and we should take the best type
        fun jokerType(): HandType {
            val jokerIndices = cards.indices.filter { i -> cards[i] == Card.of('J') }
            val selfString = this.toString()

            Card.all()
                .map { parse(selfString.replace("J", it.toString())) }
                .maxBy { it.type() }
                .let { best ->
                    return best.type()
                }
        }

        override fun compareTo(other: Hand): Int {
            val typeComparison = type().compareTo(other.type())
            if (typeComparison != 0) {
                return typeComparison
            }

            for (i in cards.indices) {
                val comparison = cards[i].compareTo(other.cards[i])
                if (comparison != 0) {
                    return comparison
                }
            }

            return 0
        }

        override fun toString(): String {
            return cards.joinToString("")
        }
    }

    data class HandWithBid(val hand: Hand, val bid: Int) {
        companion object {
            fun parse(input: String): HandWithBid {
                val parts = input.split(" ")
                val hand = Hand.parse(parts[0])
                val bid = parts[1].toInt()
                return HandWithBid(hand, bid)
            }
        }
    }

    class HandWithBidComparator : Comparator<HandWithBid> {
        override fun compare(a: HandWithBid, b: HandWithBid): Int {
            val handA = a.hand
            val handB = b.hand

            val typeComparison = handA.jokerType().compareTo(handB.jokerType())
            if (typeComparison != 0) {
                return typeComparison
            }

            for (i in handA.cards.indices) {
                val comparison = handA.cards[i].compareTo(handB.cards[i])
                if (comparison != 0) {
                    return comparison
                }
            }

            return 0
        }
    }
}