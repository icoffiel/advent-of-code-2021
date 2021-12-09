package days

import util.FileUtil.getInput
import java.awt.Point

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
        val cave = sanitizedInput
            .map { it.toIntList() }

        val columns = cave.size
        val rows = cave[0].size

        val lowPointValues = (0 until columns)
            .map { y ->
                (0 until rows)
                    .mapNotNull { x ->
                        val pointToCheck = Point(x, y)
                        if (cave.isLowPoint(pointToCheck)) {
                            cave[pointToCheck.y][pointToCheck.x]
                        } else {
                            null
                        }
                    }
            }

        return lowPointValues
            .flatten()
            .sumOf { it + 1 }
    }

    private fun partTwo(sanitizedInput: List<String>): Int {
        val cave = sanitizedInput
            .map { it.toIntList() }

        val columns = cave.size
        val rows = cave[0].size

        val lowPointCoordinates = (0 until columns)
            .map { y ->
                (0 until rows)
                    .mapNotNull { x ->
                        val pointToCheck = Point(x, y)
                        if (cave.isLowPoint(pointToCheck)) {
                            pointToCheck
                        } else {
                            null
                        }
                    }
            }
            .flatten()

        val basins = lowPointCoordinates
            .map { cave.basin(it) }

        return basins
            .map { it.size }
            .sorted()
            .reversed()
            .take(3)
            .reduce { acc, size -> acc * size }
    }
}

fun String.toIntList(): List<Int> =
    this
        .toList()
        .map { Character.getNumericValue(it) }

fun List<List<Int>>.isLowPoint(coOrdinate: Point): Boolean {
    return validSurroundingPoints(coOrdinate)
        .map { this[it.y][it.x] }
        .none { it <= this[coOrdinate.y][coOrdinate.x] }
}

private fun List<List<Int>>.validSurroundingPoints(coOrdinate: Point): List<Point> {
    val columns = this.size
    val rows = this[0].size

    return listOf(
        Point(coOrdinate.x - 1, coOrdinate.y),
        Point(coOrdinate.x + 1, coOrdinate.y),
        Point(coOrdinate.x, coOrdinate.y - 1),
        Point(coOrdinate.x, coOrdinate.y + 1),
    )
        .filter { it.x in 0 until rows }
        .filter { it.y in 0 until columns }
}

fun List<List<Int>>.basin(coOrdinate: Point): List<Point> {
    val foundPoints = mutableSetOf(coOrdinate)
    var currentPoints = listOf(coOrdinate)

    while (true) {

        val newPoints = currentPoints
            .map { this.validSurroundingPoints(it) }
            .flatten()
            .filter { !foundPoints.contains(it) }
            .filter { this[it.y][it.x] != 9 }

        // can check at this point for any new points and if 0 return

        val currentSize = foundPoints.size
        foundPoints.addAll(newPoints)

        if (currentSize == foundPoints.size) {
            return foundPoints.toList()
        }

        currentPoints = newPoints
    }
}