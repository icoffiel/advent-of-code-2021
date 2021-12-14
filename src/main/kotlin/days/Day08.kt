package days

import util.FileUtils.getInput

fun main() {
    Day08().run()
}

class Day08 : Day {
    override fun run() {
        val input = getInput("Day08.txt")
        val sanitizedInput = input.map { it.trim() }

        println("Results for Day Eight:")
        println("Part One: ${partOne(sanitizedInput)}")
        println("Part Two: ${partTwo(sanitizedInput)}")
    }

    private fun partOne(sanitizedInput: List<String>): Int {
        val inputs = sanitizedInput
            .map {
                val (signal, output) = it.split("|")
                Input(signal.splitNotBlank(" "), output.splitNotBlank(" "))
            }

        return inputs.sumOf { countDigitsInOutput(it) }
    }

    private fun partTwo(sanitizedInput: List<String>): Int {
        val inputs = sanitizedInput
            .map {
                val (signal, output) = it.split("|")
                Input(signal.splitNotBlank(" "), output.splitNotBlank(" "))
            }

        return inputs.sumOf { input ->
            val numbers = input.signals.toNumbers()
            input.output.decode(numbers)
        }
    }
}

data class Input(val signals: List<String>, val output: List<String>)

fun getUniqueSegments(input: Input): List<String> {
    return input.signals
        .groupBy { it.length }
        .filter { it.value.size == 1 }
        .flatMap { it.value }
}

fun countDigitsInOutput(input: Input): Int {
    val uniqueSegments = getUniqueSegments(input)
        .map { it.sorted() }
    return input
        .output
        .count { uniqueSegments.contains(it.sorted()) }
}

fun String.sorted(): String {
    return this
        .toList()
        .sorted()
        .joinToString("")
}

fun String.splitNotBlank(delimiter: String): List<String> {
    return this.split(delimiter).filter { it.isNotBlank() }
}

fun List<String>.toNumbers(): Map<String, Int> {
    val one = this.first { it.length == 2 }
    val four = this.first { it.length == 4 }
    val seven = this.first { it.length == 3 }
    val eight = this.first { it.length == 7 }

    val nine = this.first { it.length == 6 && it.toSet() intersect four.toSet() == four.toSet() }
    val three = this.first { it.length == 5 && it.toSet() intersect one.toSet() == one.toSet() }
    val zero = this.first { it.length == 6 && it != nine && it.toSet() intersect seven.toSet() == seven.toSet() }
    val six = this.first { it.length == 6 && it != nine && it != zero }
    val five = this.first { it.length == 5 && it != three && six.toSet() intersect it.toSet() == it.toSet() }
    val two = this.first { it.length == 5 && it != three && it != five }

    return mapOf(
        zero.sorted() to 0,
        one.sorted() to 1,
        two.sorted() to 2,
        three.sorted() to 3,
        four.sorted() to 4,
        five.sorted() to 5,
        six.sorted() to 6,
        seven.sorted() to 7,
        eight.sorted() to 8,
        nine.sorted() to 9,
    )
}

fun List<String>.decode(numbers: Map<String, Int>): Int =
    this
        .map { numbers[it.sorted()] }
        .joinToString("")
        .toInt()