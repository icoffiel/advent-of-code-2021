package days

import util.FileUtil.getInput
import java.awt.Point
import java.lang.IllegalArgumentException

fun main() {
    Day05().run()
}

class Day05 : Day {
    override fun run() {
        val input = getInput("Day05.txt")
        val sanitizedInput = input
            .map { it.trim() }

        val lines = sanitizedInput
            .flatMap { it.split(" -> ") }
            .chunked(2)
            .map { Line(it[0].toPoint(), it[1].toPoint()) }

        val maxX = lines
            .map { listOf(it.start.x, it.end.x).maxOrNull() ?: 0 }
            .maxOrNull() ?: 0

        val maxY = lines
            .map { listOf(it.start.y, it.end.y).maxOrNull() ?: 0 }
            .maxOrNull() ?: 0

        println("Results for Day Five:")
        println("Part One: ${partOne(lines, maxX, maxY)}")
        println("Part Two: ${partTwo(lines, maxX, maxY)}")
    }

    private fun partOne(lines: List<Line>, maxX: Int, maxY: Int): Int {
        val diagram: Array<Array<Int>> = Array(maxY + 1) {
            Array(maxX + 1) { 0 }
        }

        lines
            .filter { it.onlyHorizontalAndVertical() }
            .flatMap { it.toIndividualPoints() }
            .forEach { diagram[it.y][it.x]++ }

        return diagram
            .flatMap { it.toList() }
            .count { it > 1 }
    }

    private fun partTwo(lines: List<Line>, maxX: Int, maxY: Int): Int {
        val diagram: Array<Array<Int>> = Array(maxY + 1) {
            Array(maxX + 1) { 0 }
        }

        lines
            .flatMap { it.toIndividualPoints() }
            .forEach { diagram[it.y][it.x]++ }

        return diagram
            .flatMap { it.toList() }
            .count { it > 1 }
    }
}

fun Array<Array<Int>>.printMatrix() {
    forEach {
        it.forEach { elem -> if (elem == 0) System.out.format("%2s", ".") else System.out.format("%2s", elem) }
        println()
    }
}

data class Line(val start: Point, val end: Point) {
    fun toIndividualPointsHorizontalVertical(): List<Point> {
        return if (start.x != end.x) {
            val xPoints = listOf(start.x, end.x)
            val min = xPoints.minOrNull() ?: throw IllegalArgumentException()
            val max = xPoints.maxOrNull() ?: throw IllegalArgumentException()
            (min..max).map { diff -> Point(diff, start.y) }
        } else {
            val yPoints = listOf(start.y, end.y)
            val min = yPoints.minOrNull() ?: throw IllegalArgumentException()
            val max = yPoints.maxOrNull() ?: throw IllegalArgumentException()
            (min..max).map { diff -> Point(start.x, diff) }
        }
    }

    fun toIndividualPointsIncDiagonal(): List<Point> {
        val xCoords = (start.x toward end.x).map { it }
        val yCoords = (start.y toward end.y).map { it }

        return xCoords
            .zip(yCoords)
            .map { (x, y) -> Point(x, y) }
    }

    fun toIndividualPoints(): List<Point> {
        return if (start.x != end.x && start.y != end.y) {
            toIndividualPointsIncDiagonal()
        } else {
            toIndividualPointsHorizontalVertical()
        }
    }

    fun onlyHorizontalAndVertical() = start.x == end.x || start.y == end.y
}

fun String.toPoint(): Point {
    val (one, two) = this.split(",")
    return Point(one.toInt(), two.toInt())
}

private infix fun Int.toward(to: Int): IntProgression {
    val step = if (this > to) -1 else 1
    return IntProgression.fromClosedRange(this, to, step)
}