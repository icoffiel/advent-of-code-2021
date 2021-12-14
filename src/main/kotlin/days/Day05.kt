package days

import util.FileUtils.getInput
import util.Line
import util.StringUtils.toPoint

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
            .filter { it.horizontalAndVerticalOnly() }
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