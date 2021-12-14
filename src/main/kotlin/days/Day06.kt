package days

import util.FileUtils.getInputString

fun main() {
    Day06().run()
}

class Day06 : Day {
    override fun run() {
        val input = getInputString("Day06.txt")
        val sanitizedInput = input.trim()

        val initState = sanitizedInput.split(",").map { it.toInt() }

        println("Results for Day Six:")
        println("Part One: ${partOne(initState)}")
        println("Part Two: ${partTwo(initState)}")
    }

    private fun partOne(initState: List<Int>): Long {
        return calculateNumberOfFish(initState, 80)
    }

    private fun partTwo(initState: List<Int>): Long {
        return calculateNumberOfFish(initState, 256)
    }
}

private fun calculateNumberOfFish(initState: List<Int>, days: Int): Long {
    val fish = MutableList(9) { 0L }
    initState.forEach { fish[it]++ }

    repeat(days) {
        val fishToSpawn = fish[0]

        (1..8).forEach { fish[it -1] = fish[it] }
        fish[6] += fishToSpawn
        fish[8] = fishToSpawn
    }

    return fish.sum()
}