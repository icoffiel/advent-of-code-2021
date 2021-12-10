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
        val cave = sanitizedInput.map { it.toIntList() }

        val lowPointValues =
            cave.indices
                .flatMap { y ->
                    cave[0].indices
                        .mapNotNull { x ->
                            val pointToCheck = Point(x, y)
                            if (cave.isLowPoint(pointToCheck)) {
                                cave[pointToCheck]
                            } else {
                                null
                            }
                        }
                }

        return lowPointValues.sumOf { it + 1 }
    }

    private fun partTwo(sanitizedInput: List<String>): Int {
        val cave = sanitizedInput.map { it.toIntList() }

        val lowPointCoordinates =
            cave.indices
                .flatMap { y ->
                    cave[0].indices
                        .mapNotNull { x ->
                            val pointToCheck = Point(x, y)
                            if (cave.isLowPoint(pointToCheck)) {
                                pointToCheck
                            } else {
                                null
                            }
                        }
                }

        val basins = lowPointCoordinates.map { cave.basin(it) }

        return basins
            .map { it.size }
            .sorted()
            .takeLast(3)
            .reduce { acc, size -> acc * size }
    }
}

fun String.toIntList(): List<Int> =
    this
        .toList()
        .map { Character.getNumericValue(it) }

fun List<List<Int>>.isLowPoint(coOrdinate: Point): Boolean {
    return validSurroundingPoints(coOrdinate)
        .map { this[it] }
        .none { it <= this[coOrdinate] }
}

private fun List<List<Int>>.validSurroundingPoints(coOrdinate: Point): List<Point> {
    return coOrdinate
        .neighbours()
        .filter { it in this }
}

operator fun List<List<Int>>.contains(point: Point): Boolean =
    point.y in this.indices && point.x in this[point.y].indices

operator fun List<List<Int>>.get(point: Point): Int = this[point.y][point.x]

fun Point.neighbours(): List<Point> {
    return listOf(
        Point(this.x - 1, this.y),
        Point(this.x + 1, this.y),
        Point(this.x, this.y - 1),
        Point(this.x, this.y + 1),
    )
}

fun List<List<Int>>.basin(coOrdinate: Point): List<Point> {
    val boundaryHeight = 9
    val foundPoints = mutableSetOf(coOrdinate)
    var currentPoints = listOf(coOrdinate)

    while (true) {

        val newPoints = currentPoints
            .map { this.validSurroundingPoints(it) }
            .flatten()
            .filter { !foundPoints.contains(it) }
            .filter { this[it] != boundaryHeight }

        if (newPoints.isEmpty()) {
            return foundPoints.toList()
        }

        foundPoints.addAll(newPoints)
        currentPoints = newPoints
    }
}