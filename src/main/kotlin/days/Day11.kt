package days

import util.FileUtils.getInput
import util.GridUtils.neighbours
import util.GridUtils.get
import util.ListUtils.toIntGrid
import java.awt.Point
import kotlin.concurrent.fixedRateTimer

fun main() {
    Day11().run()
}

class Day11 : Day {
    override fun run() {
        val input = getInput("Day11.txt")
        val sanitizedInput = input.map { it.trim() }

        println("Results for Day Eleven:")
        println("Part One: ${partOne(sanitizedInput)}")
        println("Part Two: ${partTwo(sanitizedInput)}")
    }

    private fun partOne(sanitizedInput: List<String>): Int {
        val initialGrid = sanitizedInput.toIntGrid()

        println("Before any steps:")
        initialGrid.print()

        val result = (0..99)
            .fold(State(initialGrid, emptyList())) { state, stage ->
                val (grid, alreadyFlashed) = state

                val plusOneGrid = grid.addOneToAll()
                val flashPoints = plusOneGrid.flashPoints()

                val resetGrid = plusOneGrid.resetFlashedToZero()

                val innerState = flashPointSequence(InnerState(resetGrid, flashPoints, flashPoints)).lastOrNull()

                val gridOutcome = innerState?.grid ?: resetGrid

                println("After step ${stage + 1}:")
                gridOutcome.print()

                State(gridOutcome, alreadyFlashed + (innerState?.alreadyFlashed ?: emptyList()))
            }

        return result.alreadyFlashed.count()
    }

    private fun partTwo(sanitizedInput: List<String>): Int {
        val initialGrid = sanitizedInput.toIntGrid()

        println("Before any steps:")
        initialGrid.print()

        val result = generateSequence(OuterState(initialGrid, emptyList(), 1)) { state ->
            val (grid, alreadyFlashed, stage) = state

            if (grid.synchronized()) {
                null
            } else {

                val plusOneGrid = grid.addOneToAll()
                val flashPoints = plusOneGrid.flashPoints()

                val resetGrid = plusOneGrid.resetFlashedToZero()

                val innerState = flashPointSequence(InnerState(resetGrid, flashPoints, flashPoints)).lastOrNull()

                val gridOutcome = innerState?.grid ?: resetGrid

                println("After step ${stage + 1}:")
                gridOutcome.print()

                OuterState(gridOutcome, alreadyFlashed + (innerState?.alreadyFlashed ?: emptyList()), stage + 1)
            }
        }
            .onEach { it.grid.synchronized() }
            .takeWhile { !it.grid.synchronized() }
            .last()

        return result.stage
    }
}

data class OuterState(val grid: List<List<Int>>, val alreadyFlashed: List<Point>, val stage: Int)
data class State(val grid: List<List<Int>>, val alreadyFlashed: List<Point>)
data class InnerState(val grid: List<List<Int>>, val flashPoints: List<Point>, val alreadyFlashed: List<Point>)

fun flashPointSequence(state: InnerState): Sequence<InnerState> {
    return generateSequence(state) { innerState ->
        val (grid, flashPoints, alreadyFlashed) = innerState

        if (flashPoints.isEmpty()) {
            null
        } else {

            val newGrid = flashPoints.fold(grid) { previousGrid, point ->
                previousGrid.flashNeighbours(point)
            }

            val newFlashPoints = newGrid.flashPoints()
            val resetGrid = newGrid.resetFlashedToZero()

            InnerState(resetGrid, newFlashPoints, alreadyFlashed + newFlashPoints)
        }
    }
}

fun List<List<Int>>.addOneToAll(): List<List<Int>> =
    this
        .map { column ->
            column.map { row ->
                row + 1
            }
        }

fun List<List<Int>>.addOneToPoint(point: Point): List<List<Int>> {
    return this
        .mapIndexed { y, column ->
            column.mapIndexed { x, row ->
                if (Point(x, y) == point) {
                    row + 1
                } else {
                    row
                }
            }
        }
}

fun List<List<Int>>.flashNeighbours(point: Point): List<List<Int>> {
    val neighbours = this.neighbours(point, includeDiagonals = true)

    return neighbours.fold(this) { grid, pointToUpdate ->
        if (grid[pointToUpdate] == 0) {
            grid
        } else {
            grid.addOneToPoint(pointToUpdate)
        }
    }
}

fun List<List<Int>>.resetFlashedToZero(): List<List<Int>> {
    return this
        .map { column ->
            column.map { row ->
                if (row > 9) {
                    0
                } else {
                    row
                }
            }
        }
}

fun List<List<Int>>.flashPoints(): List<Point> {
    return this
        .flatMapIndexed { y, column ->
            column.mapIndexedNotNull { x, value ->
                if (value > 9) {
                    Point(x, y)
                } else {
                    null
                }
            }
        }

}

fun List<List<Int>>.synchronized(): Boolean {
    return this.flatten().none { it != 0 }
}

fun List<List<Int>>.print() {
    this
        .forEach { column ->
            column.forEach { row ->
                System.out.format("%5s", row)
            }
            println()
        }
}