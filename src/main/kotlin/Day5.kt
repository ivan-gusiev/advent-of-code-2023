import util.AocDay
import util.AocInput
import util.AocSequence
import kotlin.math.min

typealias Day5InputType = List<List<String>>;

class Day5 : Runner {
    // constant that holds the test input
    val TEST_INPUT: String = """
        seeds: 79 14 55 13
        
        seed-to-soil map:
        50 98 2
        52 50 48
        
        soil-to-fertilizer map:
        0 15 37
        37 52 2
        39 0 15
        
        fertilizer-to-water map:
        49 53 8
        0 11 42
        42 0 7
        57 7 4
        
        water-to-light map:
        88 18 7
        18 25 70
        
        light-to-temperature map:
        45 77 23
        81 45 19
        68 64 13
        
        temperature-to-humidity map:
        0 69 1
        1 0 69
        
        humidity-to-location map:
        60 56 37
        56 93 4
    """.trimIndent()

    override fun run() {
        val day = AocDay("2023".toInt(), "5".toInt())
        val lines = AocInput.lines(day)
        //val lines = TEST_INPUT.split("\n")
        val blocks = AocSequence.split(lines, "")


        part1(blocks)
        part2(blocks)
    }

    private fun part1(input: Day5InputType) {
        val seeds = parseSeedsPart1(input[0].single())
        val minLocation = getMinLocation(input, seeds.asSequence())
        println("Part 1 Result: $minLocation")
    }

    private fun part2(input: Day5InputType) {
        val seeds = parseSeedsPart2(input[0].single())
        val minLocation = getMinLocation(input, seeds)
        println("Part 2 Result: $minLocation")
    }

    data class FoodMapping(val destinationStart: Long, val sourceStart: Long, val length: Long) {
        companion object {
            fun parse(input: String): FoodMapping {
                val parts = input.split(" ")
                val destinationStart = parts[0].toLong()
                val sourceStart = parts[1].toLong()
                val length = parts[2].toLong()
                return FoodMapping(destinationStart, sourceStart, length)
            }
        }

        override fun toString(): String {
            return "$sourceStart -> $destinationStart ($length)"
        }

        fun map(source: Long): Long {
            return destinationStart + (source - sourceStart)
        }
    }

    data class FoodMap(val destination: String, val source: String, val mappings: List<FoodMapping>) {
        companion object {
            fun parse(input: List<String>): FoodMap {
                val header = input[0]
                    .replace(" map:", "")
                    .split("-to-")
                val source = header[0]
                val destination = header[1]

                val mappings = input.drop(1).map(FoodMapping::parse)

                return FoodMap(destination, source, mappings)
            }
        }

        override fun toString(): String {
            return "$source -> $destination\n" + mappings.joinToString("\n") { "  $it" }
        }

        fun selectMapping(source: Long): FoodMapping {
            return mappings.find { it.sourceStart <= source && source < it.sourceStart + it.length } ?: FoodMapping(
                0,
                0,
                Long.MAX_VALUE
            )
        }
    }

    private fun parseSeedsPart1(input: String): List<Long> {
        return input
            .replace("seeds: ", "")
            .trim()
            .split(" ")
            .map(String::toLong)
    }

    private fun parseSeedsPart2(input: String): Sequence<Long> = sequence {
        val seeds = parseSeedsPart1(input)
        val pairs = seeds.chunked(2)
        var pairCount = 0

        for (pair in pairs) {
            pairCount++
            val start = pair[0]
            val count = pair[1]
            println("processing $pairCount/${pairs.count()} [$start, ${start + count}], count=$count")
            for (i in start..start + count) {
                yield(i)
            }
        }
    }

    private fun getMinLocation(input: Day5InputType, seeds: Sequence<Long>): Long {
        val maps = input.drop(1).map(FoodMap.Companion::parse)
        val mapMap = maps.associateBy(FoodMap::source)

        val seedName = "seed"
        val targetName = "location"
        var minLocation = Long.MAX_VALUE
        var progress = 0

        for (seed in seeds) {
            progress++
            if (progress % 10_000_000 == 0) {
                println("${progress / 1_000_000}M seeds processed")
            }

            var current = seed
            var currentName = seedName

            while (true) {
                val map = mapMap[currentName]!!
                val mapping = map.selectMapping(current)
                current = mapping.map(current)
                currentName = map.destination
                if (currentName == targetName) {
                    minLocation = min(minLocation, current)
                    break
                }
            }
        }
        return minLocation
    }
}