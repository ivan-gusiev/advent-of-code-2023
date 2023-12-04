package template

import Runner
import util.AocDay
import util.AocInput

typealias Day0InputType = List<String>;

@Suppress("unused")
class Day0 : Runner {
    override fun run() {
        val day = AocDay("TemplateYear".toInt(), "TemplateDay".toInt())
        val lines = AocInput.lines(day)
        part1(lines)
        part2(lines)
    }

    private fun part1(input: Day0InputType) {

    }

    private fun part2(input: Day0InputType) {

    }
}