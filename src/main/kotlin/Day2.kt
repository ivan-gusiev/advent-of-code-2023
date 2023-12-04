import util.AocDay
import util.AocInput
import kotlin.math.max

typealias InputType = List<String>;

class Day2 : Runner {
    data class CubeSet(val red: Int, val green: Int, val blue: Int) {
        companion object {
            fun parse(input: String): CubeSet {
                var red = 0;
                var green = 0;
                var blue = 0;
                val pieces = input.split(",").map(String::trim)
                pieces
                    .map { piece -> piece.split(" ").map(String::trim) }
                    .forEach { words ->
                        val value = words[0].toInt()
                        when (words[1]) {
                            "red" -> red += value
                            "green" -> green += value
                            "blue" -> blue += value
                        }
                    }
                return CubeSet(red, green, blue)
            }
        }

        fun includes(other: CubeSet): Boolean {
            return this.red >= other.red && this.green >= other.green && this.blue >= other.blue
        }

        fun leastCommonCubes(other: CubeSet): CubeSet {
            return CubeSet(
                max(this.red, other.red),
                max(this.green, other.green),
                max(this.blue, other.blue)
            )
        }

        fun power(): Int {
            return red * green * blue
        }
    }

    data class Game(val id: Int, val sets: List<CubeSet>) {
        companion object {
            fun parse(input: String): Game {
                val parts = input.replace("Game ", "").split(":")
                val index = parts[0].toInt()
                val setStrings = parts[1].split(";").map(CubeSet::parse)
                return Game(index, setStrings)
            }
        }

        fun possible(bag: CubeSet): Boolean {
            return sets.all(bag::includes)
        }

        fun leastCubesPossible(): CubeSet {
            return sets.reduce(CubeSet::leastCommonCubes)
        }
    }

    val TEST_INPUT = """
        Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
        Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
        Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
        Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
        Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green
    """.trimIndent()

    override fun run() {
        val day = AocDay("2023".toInt(), "2".toInt())
        val lines = AocInput.lines(day)
        //val lines = TEST_INPUT.split("\n")
        //part1(lines)
        part2(lines)
    }

    fun part1(input: InputType) {
        val targetBag = CubeSet(12, 13, 14)
        val games = input.map(Game::parse)
        val possibleIds = games.map { g ->
            if (g.possible(targetBag)) {
                g.id
            } else {
                0
            }
        }
        games.forEach { g -> println("${g.possible(targetBag)}: $g") }
        println(possibleIds.sum())
    }

    fun part2(input: InputType) {
        val games = input.map(Game::parse)
        games.forEach { g -> println("${g.leastCubesPossible().power()}: $g") }
        println(games.map { g -> g.leastCubesPossible().power() }.sum())
    }
}
