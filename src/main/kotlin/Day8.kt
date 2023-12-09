import util.AocArithm
import util.AocArithm.Companion.lcm
import util.AocDay
import util.AocInput
import java.math.BigInteger

typealias Day8InputType = Day8.Input;

class Day8 : Runner {
    val TEST_INPUT = """
        RL
        
        AAA = (BBB, CCC)
        BBB = (DDD, EEE)
        CCC = (ZZZ, GGG)
        DDD = (DDD, DDD)
        EEE = (EEE, EEE)
        GGG = (GGG, GGG)
        ZZZ = (ZZZ, ZZZ)
    """.trimIndent()

    val TEST_INPUT_2 = """
        LR

        11A = (11B, XXX)
        11B = (XXX, 11Z)
        11Z = (11B, XXX)
        22A = (22B, XXX)
        22B = (22C, 22C)
        22C = (22Z, 22Z)
        22Z = (22B, 22B)
        XXX = (XXX, XXX)
    """.trimIndent()

    override fun run() {
        val day = AocDay("2023".toInt(), "8".toInt())
        val lines = AocInput.lines(day)
        //val lines = TEST_INPUT_2.split("\n")
        val input = Input.parse(lines)
        //part1(input)
        part2(input)
    }

    private fun part1(input: Day8InputType) {
        val nodeDictionary = input.nodes.associateBy { it.source }
        val instructionsSource = sequence {
            while (true) {
                yieldAll(input.instructions.iterator())
            }
        }

        val start = "AAA"

        var current = "AAA"
        var steps = 0
        val instructions = instructionsSource.iterator()

        while (true) {
            val instruction = instructions.next()
            val node = nodeDictionary[current]!!
            current = if (instruction == 'R') node.right else node.left
            steps++
            if (current == "ZZZ") {
                break
            }
        }
        println("Steps to get to $current: $steps")
    }

    private fun part2(input: Day8InputType) {
        //println(input)
        val nodeDictionary = input.nodes.associateBy { it.source }

        val starts = nodeDictionary.keys.filter { it.endsWith("A") }
        val cycles = starts.map { describeCycle(input.instructions, nodeDictionary, it) }
        val zs = cycles.map { it.third[0] }.map { BigInteger.valueOf(it.toLong()) }
        println(zs.reduce(AocArithm::lcm))
    }

    fun describeCycle(instructionsString: String, nodeDictionary: Map<String, InputNode>, start: String): Triple<Int, Int, List<Int>> {
        val instructionSequence = sequence {
            while (true) {
                yieldAll(instructionsString.indices)
            }
        }
        var current = start
        var steps = 0
        var cycleStart = 0
        val instructions = instructionSequence.iterator()
        val zs = ArrayList<Int>()
        val seen = HashMap<Pair<String, Int>, Int>()

        while (true) {
            val instructionIndex = instructions.next()
            val instruction = instructionsString[instructionIndex]
            val node = nodeDictionary[current]!!
            //println("$steps: $node <- $instruction")
            current = if (instruction == 'R') node.right else node.left
            steps++

            val state = Pair(current, instructionIndex)
            if (seen.containsKey(state)) {
                cycleStart = seen[state]!!
                break
            } else {
                seen[state] = steps
            }

            if (current.endsWith("Z")) {
                zs.add(steps)
            }
        }
        return Triple(steps, cycleStart, zs)
    }

    data class InputNode(val source: String, val left: String, val right: String) {
        companion object {
            fun parse(input: String): InputNode {
                val parts = input.replace(" = ", " ")
                    .replace("(", "").replace(")", "")
                    .replace(",", "").split(" ");

                return InputNode(parts[0], parts[1], parts[2])
            }
        }

        override fun toString(): String {
            return "$source = ($left, $right)"
        }
    }

    data class Input(val instructions: String, val nodes: List<InputNode>) {
        companion object {
            fun parse(input: List<String>): Input {
                val instructions = input[0]
                val nodes = input.drop(2).map(InputNode::parse)
                return Input(instructions, nodes)
            }
        }

        override fun toString(): String {
            return listOf(instructions, "").plus(nodes).joinToString("\n")
        }
    }
}