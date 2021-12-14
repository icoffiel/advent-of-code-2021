package days

import util.FileUtils.getInputString
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import kotlin.math.absoluteValue

fun main() {
    Day07().run()
}

class Day07 : Day {
    override fun run() {
        val input = getInputString("Day07.txt")
        val sanitizedInput = input.trim()

        val initState = sanitizedInput.split(",").map { it.toInt() }

        println("Results for Day Seven:")
        println("Part One: ${partOne(initState)}")
        println("Part Two: ${partTwo(initState)}")
    }

    private fun partOne(initState: List<Int>): Int {
        val median = median(initState)

        return initState
            .map { it - median }
            .sumOf { it.absoluteValue }
    }

    private fun partTwo(initState: List<Int>): Int {

        val (min, max) = initState.minAndMax()

        val positionFuelCodePair = (min..max)
            .map {
                val fuelCost = initState.sumOf { state ->
                    if (it < state) consecutiveSum(it, state) else consecutiveSum(
                        state,
                        it
                    )
                }

                Pair(it, fuelCost)
            }
            .minByOrNull { (_, fuelCost) -> fuelCost } ?: throw IllegalStateException()

        return positionFuelCodePair.second
    }
}

/**
 * Taken from https://rosettacode.org/wiki/Averages/Median#Kotlin
 * Modified to use Ints
 */
fun median(l: List<Int>) = l.sorted().let { (it[it.size / 2] + it[(it.size - 1) / 2]) / 2 }

fun consecutiveSum(min: Int, max: Int): Int {
    return (0..(max - min))
        .sumOf { it }
}

fun List<Int>.minAndMax(): Pair<Int, Int> {
    val min = this.minOrNull() ?: throw IllegalArgumentException()
    val max = this.maxOrNull() ?: throw IllegalArgumentException()
    return Pair(min, max)
}