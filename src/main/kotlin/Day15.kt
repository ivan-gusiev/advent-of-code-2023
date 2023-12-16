
import util.AocDay
import util.AocInput
import kotlin.streams.toList

typealias Day15InputType = List<String>

class Day15 : Runner {
    val TEST_VALUE = """rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7"""

    companion object {
        fun hashString(str: String): Int  {
            return str.chars().toList().fold(0) { acc, c ->
                (acc + c) * 17 % 256
            }
        }
    }

    override fun run() {
        val day = AocDay("2023".toInt(), "15".toInt())
        val lines = AocInput.string(day)
        //val lines = TEST_VALUE
        val parts = lines.trim().split(",")
        println(parts)
        part1(parts)
        part2(parts)
    }

    private fun part1(input: Day15InputType) {
        println(input.sumOf(::hashString))
    }

    private fun part2(input: Day15InputType) {
        val boxes = ArrayList(List(256) { LensBox.new() })
        val actions = input.map(BoxAction.Companion::parse)

        actions.forEachIndexed { i, action ->
            val box = boxes[action.boxIndex()]
            when (action) {
                is BoxAction.Remove -> box.remove(action.label)
                is BoxAction.Add -> box.add(action.lens)
            }
            println("After $i (${input[i]}):")
            printBoxes(boxes)
            println()
        }
        println(boxes.mapIndexed { index, lensBox -> lensBox.power(index) }.sum())
    }

    private fun printBoxes(boxes: List<LensBox>) {
        for ((i, box) in boxes.mapIndexed(::Pair)) {
            if (box.empty()) continue
            println("Box $i: $box")
        }
    }

    data class Lens(val label: String, val focalLength: Int) {
        fun power(boxId: Int, lensId: Int): Int = focalLength * (boxId + 1) * (lensId + 1)

        override fun toString(): String {
            return "[$label $focalLength]"
        }
    }

    data class LensBox(val lenses: ArrayList<Lens>) {
        companion object {
            fun new(): LensBox {
                return LensBox(ArrayList())
            }
        }

        fun empty(): Boolean {
            return lenses.isEmpty()
        }

        fun add(lens: Lens) {
            val index = lenses.indexOfFirst { it.label == lens.label }
            when (index) {
                -1 -> lenses.add(lens)
                else -> lenses[index] = lens
            }
        }

        fun remove(label: String) {
            lenses.removeIf { it.label == label }
        }

        fun power(boxId: Int): Int =
            lenses.mapIndexed { index, lens -> lens.power(boxId, index) }.sum()


        override fun toString(): String {
            return lenses.joinToString(" ")
        }
    }

    sealed class BoxAction {
        companion object {
            fun parse(str: String): BoxAction {
                if (str.endsWith("-")) return Remove(str.dropLast(1))

                val parts = str.split("=")
                return Add(Lens(parts[0], parts[1].toInt()))
            }
        }

        data class Remove(val label: String) : BoxAction()
        data class Add(val lens: Lens) : BoxAction()

        fun boxIndex(): Int {
            return when (this) {
                is Remove -> hashString(label)
                is Add -> hashString(lens.label)
            }
        }
    }
}