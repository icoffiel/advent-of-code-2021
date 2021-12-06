package days

import util.FileUtil.getInputString

fun main() {
    Day06().run()
}

class Day06 : Day {
    override fun run() {
        val input = getInputString("Day06.txt")
        val sanitizedInput = input.trim()

        val initState = sanitizedInput.split(",")

        println("Results for Day Five:")
        println("Part One: ${partOne(initState)}")
        println("Part Two: ${partTwo(initState)}")
    }

    private fun partOne(initState: List<String>): Int {
        val initialLaternFish = initState
            .map { Lanternfish(it.toInt()) }

        println("Initial State: $initialLaternFish")

        val result = (0..79)
            .fold(initialLaternFish) { prev, i ->
                val newState = calculateNewState(prev)
                println("After ${i + 1} day(s): ${newState.map { it.timer }.joinToString(",")}")

                newState
            }

        return result.size
    }

    private fun partTwo(initState: List<String>): Int {
        val initialLaternFish = initState
            .map { Lanternfish(it.toInt()) }

        println("Initial State: $initialLaternFish")

        val result = (0..255)
            .fold(initialLaternFish) { prev, i ->
                val newState = calculateNewState(prev)
//                println("After ${i + 1} day(s): ${newState.map { it.timer }.joinToString(",")}")

                newState
            }

        return result.size
    }
}

fun calculateNewState(currentState: List<Lanternfish>): List<Lanternfish> {

    val updatedCurrentFish = currentState
        .map { it.tick() }

    val newFishNeeded = updatedCurrentFish.count { it.timer == 6 && !it.new }

    val newFish = if (newFishNeeded > 0) {
        (0 until newFishNeeded)
            .map { Lanternfish(new = true) }
    } else {
        listOf()
    }

    return updatedCurrentFish + newFish
}

data class Lanternfish(val timer: Int = 8, val new: Boolean = false) {
    fun tick(): Lanternfish {
        val newTimer = if (timer == 0) 6 else timer - 1
        val isNew = if(timer == 0) false else new
        return copy(timer = newTimer, new = isNew)
    }
}