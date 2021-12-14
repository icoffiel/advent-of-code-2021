package days

import util.FileUtils.getInput

fun main() {
    Day04().run()
}

class Day04 : Day {
    override fun run() {
        val input = getInput("Day04.txt")
        val sanitizedInput = input
            .map { it.trim() }
            .filter { it.isNotBlank() }
        val drawNumbers = sanitizedInput.first().split(",")

        println("Results for Day Four:")
        println("Part One: ${partOne(sanitizedInput, drawNumbers)}")
        println("Part Two: ${partTwo(sanitizedInput, drawNumbers)}")
    }

    private fun partOne(sanitizedInput: List<String>, drawNumbers: List<String>): Int {

        var boards = sanitizedInput.toBoards()

        drawNumbers.forEach { number ->
            boards = boards
                .map { board -> board.mark(number) }

            val firstWinningBoard = boards.firstOrNull { it.isWinningBoard() }
            if (firstWinningBoard != null) {
                return firstWinningBoard.score(number.toInt())
            }
        }

        return 0
    }

    private fun partTwo(sanitizedInput: List<String>, drawNumbers: List<String>): Int {
        var boards = sanitizedInput.toBoards().toMutableList()
        val winningBoards = linkedSetOf<WinningBoard>()

        drawNumbers.forEach { number ->
            boards = boards
                .map { board -> board.mark(number) }
                .toMutableList()

            val currentWinningBoards = boards
                .filter { it.isWinningBoard() }
                .map { WinningBoard(it, number.toInt()) }

            currentWinningBoards
                .forEach{ boards.remove(it.board)}

            if (currentWinningBoards.isNotEmpty()) {
                winningBoards.addAll(currentWinningBoards)
            }
        }

        val lastWinningBoard = winningBoards.last()

        return lastWinningBoard.board.score(lastWinningBoard.winningNumber)
    }
}

fun List<String>.toBoards() = drop(1)
    .chunked(5)
    .map { it.toBoard() }

typealias Row = List<BoardValues>
typealias Column = List<Row>

data class WinningBoard(val board: Board, val winningNumber: Int)

data class Board(val data: Column) {
    fun mark(number: String): Board {
        val updatedData = data
            .map {
                it.map { item ->
                    if (item.number == number) {
                        item.copy(marked = true)
                    } else {
                        item
                    }
                }
            }
        return copy(data = updatedData)
    }

    fun isWinningBoard(): Boolean {
        data.forEach { if (it.all { boardValues -> boardValues.marked }) return true } // row is all marked

        for (i in 0 until data[0].size) {
            val allColumnsMarked = data
                .map { it[i] }
                .all { boardValues -> boardValues.marked }

            if (allColumnsMarked) {
                return true
            }
        }

        return false
    }

    fun score(number: Int): Int {
        val unmarkedScore = data
            .flatten()
            .filter { !it.marked }
            .sumOf { it.number.toInt() }

        return unmarkedScore * number
    }
}

private fun List<String>.toBoard(): Board {
    val data = this
        .map { it.split(" ") }
        .map { it.toBoardValue() }

    return Board(data)
}

private fun List<String>.toBoardValue(): List<BoardValues> {
    return this
        .filter { number -> number.isNotEmpty() }
        .map { number -> BoardValues(number) }
}

data class BoardValues(val number: String, val marked: Boolean = false)