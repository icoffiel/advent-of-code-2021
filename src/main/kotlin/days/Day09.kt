package days

import util.FileUtils.getInput
import util.GridUtils.get
import util.GridUtils.basin
import util.GridUtils.lowPoints
import util.ListUtils.toIntGrid
import util.StringUtils.toIntList

fun main() {
    Day09().run()
}

class Day09 : Day {
    override fun run() {
        val input = getInput("Day09.txt")
        val sanitizedInput = input.map { it.trim() }

        println("Results for Day Nine:")
        println("Part One: ${partOne(sanitizedInput)}")
        println("Part Two: ${partTwo(sanitizedInput)}")
    }

    private fun partOne(sanitizedInput: List<String>): Int {
        val cave = sanitizedInput.toIntGrid()

        val lowPointValues =
            cave
                .lowPoints()
                .map { cave[it] }

        return lowPointValues.sumOf { it + 1 }
    }

    private fun partTwo(sanitizedInput: List<String>): Int {
        val cave = sanitizedInput.map { it.toIntList() }

        val basins =
            cave
                .lowPoints()
                .map { cave.basin(it) }

        return basins
            .map { it.size }
            .sorted()
            .takeLast(3)
            .reduce { acc, size -> acc * size }
    }
}

