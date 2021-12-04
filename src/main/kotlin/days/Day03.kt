package days

import util.FileUtil.getInput

fun main() {
    Day03().run()
}

class Day03 : Day {
    override fun run() {
        val input = getInput("Day03.txt")
        val sanitizedInput = input.map { it.trim() }

        println("Results for Day Two:")
        println("Part One: ${partOne(sanitizedInput)}")
        println("Part Two: ${partTwo(sanitizedInput)}")
    }

    private fun partOne(sanitizedInput: List<String>): Int {
        val transposedList = sanitizedInput
            .toMatrix()
            .toList()

        val mostCommonBits = transposedList.mostCommonBits()

        val leastCommonBits = transposedList
            .map { it.groupingBy { key -> key }.eachCount().minByOrNull { count -> count.value } }
            .joinToString(separator = "") { it?.key.toString() }

        return mostCommonBits.toInt(2) * leastCommonBits.toInt(2)
    }

    private fun partTwo(sanitizedInput: List<String>): Int {

        var mostCommonFiltered = sanitizedInput
        var count = 0

        while (mostCommonFiltered.size != 1) {
            val transposedList = mostCommonFiltered
                .toMatrix()
                .toList()
            val mostCommonBits = transposedList.mostCommonBits()

            mostCommonFiltered = mostCommonFiltered.filter { it[count] == mostCommonBits[count] }
            count++
        }

        var leastCommonFiltered = sanitizedInput
        var anotherCount = 0

        while (leastCommonFiltered.size != 1) {
            val transposedList = leastCommonFiltered
                .toMatrix()
                .toList()
            val leastCommonBits = transposedList.leastCommonBits()

            leastCommonFiltered = leastCommonFiltered.filter { it[anotherCount] == leastCommonBits[anotherCount] }
            anotherCount++
        }

        return mostCommonFiltered[0].toInt(2) * leastCommonFiltered[0].toInt(2)
    }
}

private fun Array<CharArray>.toList(): List<String> {
    return map { String(it) }.toList()
}

private fun List<String>.toMatrix(): Array<CharArray> {
    val matrix: Array<CharArray> = map { it.toCharArray() }.toTypedArray()
    val transposedMatrix: Array<CharArray> = Array(matrix[0].size) { CharArray(matrix.size) }

    transposedMatrix.indices.forEach { i ->
        transposedMatrix[i].indices.forEach { j ->
            transposedMatrix[i][j] = matrix[j][i]
        }
    }

    return transposedMatrix
}

private fun List<String>.mostCommonBits() =
    map { it.calculateCommon('1', maxByValueOrNull()) }
        .joinToString(separator = "") { it.key.toString() }

private fun maxByValueOrNull() =
    { inputMap: Map<Char, Int> -> inputMap.maxByOrNull { entry: Map.Entry<Char, Int> -> entry.value }?.value }


private fun List<String>.leastCommonBits() =
    map { it.calculateCommon('0', minByValueOrNull()) }
        .joinToString(separator = "") { it.key.toString() }

private fun minByValueOrNull() =
    { inputMap: Map<Char, Int> -> inputMap.minByOrNull { entry -> entry.value }?.value }

private fun String.calculateCommon(
    filterBy: Char,
    apply: (input: Map<Char, Int>) -> Int?
): Map.Entry<Char, Int> {
    val keyCountMap =
        groupingBy { key -> key }
            .eachCount()

    val valueFound = apply(keyCountMap)

    val valuesFoundMap = keyCountMap.filter { it.value == valueFound }

    return when {
        valuesFoundMap.size > 1 -> valuesFoundMap.entries.first { it.key == filterBy }
        else -> valuesFoundMap.entries.first()
    }
}