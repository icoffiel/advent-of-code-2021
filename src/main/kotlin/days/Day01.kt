package days

import util.FileUtils.getInput

fun main() {
    Day01().run()
}

class Day01: Day {
    override fun run() {
        val input = getInput("Day01.txt")
        val measurements = input
            .map { it.trim() }
            .map { it.toInt() }

        println("Results for Day One:")
        println("Part One: ${partOne(measurements)}")
        println("Part Two: ${partTwo(measurements)}")
    }

    private fun partOne(measurements: List<Int>): Int {
        return measurements
            .zipWithNext { first, second -> if (first < second) 1 else 0 }
            .sum()
    }

    private fun partTwo(measurements: List<Int>): Int {
        return measurements
            .windowed(3) { it.sum() }
            .zipWithNext { first, second -> if (first < second) 1 else 0 }
            .sum()
    }
}