package days

import util.FileUtil.getInput
import java.util.*

fun main() {
    Day10().run()
}

class Day10 : Day {
    override fun run() {
        val input = getInput("Day10.txt")
        val sanitizedInput = input.map { it.trim() }

        println("Results for Day Ten:")
        println("Part One: ${partOne(sanitizedInput)}")
        println("Part Two: ${partTwo(sanitizedInput)}")
    }

    private fun partOne(sanitizedInput: List<String>): Int {
        val corruptedStringsCount = sanitizedInput
            .filter { it.isCorrupted() }
            .mapNotNull { it.corruptedBracket() }
            .groupingBy { it }.eachCount()

        return corruptedStringsCount
            .map { (char, count) -> closingBracketScoring.getValue(char) * count }
            .sum()
    }

    private fun partTwo(sanitizedInput: List<String>): Long {
        val incompleteStrings = sanitizedInput
            .filter { it.isNotCorrupted() }
            .map { it.incompleteBrackets() }

        val scores = incompleteStrings
            .map { it.score() }

        return scores
            .sorted()
            .middleValueOrNull() ?: throw IllegalStateException("No middle value found")
    }
}

fun <E> List<E>.middleValueOrNull(): E? {
    return if (this.size % 2 == 0) {
        null
    } else {
        this[size / 2]
    }
}

fun String.isOpeningBracket(): Boolean = this in mapOfBrackets.keys

fun String.isNotCorrupted(): Boolean = !this.isCorrupted()
fun String.isCorrupted(): Boolean = this.corruptedBracket() != null

fun String.corruptedBracket(): String? {
    this
        .asSequence()
        .map { it.toString() }
        .fold(Stack<String>()) { stack, char ->
            if (char.isOpeningBracket()) {
                stack.also { it.push(char) }
            } else {
                val possibleMatchingBracket = stack.pop()
                if (possibleMatchingBracket to char !in mapOfBrackets) {
                    return char
                }
                stack
            }
        }

    return null
}

fun String.incompleteBrackets(): List<String> {
    val unclosedChars = this
        .toList()
        .map { it.toString() }
        .fold(Stack<String>()) { stack, char ->
            if (char.isOpeningBracket()) {
                stack.also { it.push(char) }
            } else {
                stack.also { it.pop() }
            }
        }

    return unclosedChars
        .map { mapOfBrackets.getValue(it) }
        .reversed()
}

fun List<String>.score(): Long =
    this
        .fold(0L) { acc, char ->
            acc * 5 + incompleteBracketScoring.getValue(char)
        }

operator fun <R, S> Map<R, S>.contains(pair: Pair<R, S>): Boolean {
    return this[pair.first] == pair.second
}

val closingBracketScoring = mapOf(
    ")" to 3,
    "]" to 57,
    "}" to 1197,
    ">" to 25137,
)

val mapOfBrackets = mapOf(
    "(" to ")",
    "[" to "]",
    "{" to "}",
    "<" to ">",
)

val incompleteBracketScoring = mapOf(
    ")" to 1,
    "]" to 2,
    "}" to 3,
    ">" to 4,
)