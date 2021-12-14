package days

import util.FileUtils.getInput

fun main() {
    Day02().run()
}

class Day02: Day {
    override fun run() {
        val input = getInput("Day02.txt")
        val commands = input
            .map { it.trim() }
            .map { it.split(" ") }
            .map { (command, value) -> Command(DIRECTION.valueOf(command.uppercase()), value.toInt()) }

        println("Results for Day Two:")
        println("Part One: ${partOne(commands)}")
        println("Part Two: ${partTwo(commands)}")
    }

    private fun partOne(commands: List<Command>): Int {
        val submarine = commands.fold(Submarine()) { sub, command -> sub.executeCommand(command)}
        return submarine.horizontal * submarine.depth
    }

    private fun partTwo(commands: List<Command>): Int {
        val submarine = commands.fold(Submarine()) { sub, command -> sub.executeCommandWithAim(command)}
        return submarine.horizontal * submarine.depth
    }
}

enum class DIRECTION {
    FORWARD,
    DOWN,
    UP
}

data class Command(val direction: DIRECTION, val value: Int)

data class Submarine(val horizontal: Int = 0, val depth: Int = 0, val aim: Int = 0) {
    fun executeCommand(command: Command): Submarine {
        return when(command.direction) {
            DIRECTION.FORWARD -> copy(horizontal = horizontal + command.value)
            DIRECTION.DOWN -> copy(depth = depth + command.value)
            DIRECTION.UP -> copy(depth = depth - command.value)
        }
    }

    fun executeCommandWithAim(command: Command): Submarine {
        return when(command.direction) {
            DIRECTION.FORWARD -> copy(horizontal = horizontal + command.value, depth = depth + aim * command.value)
            DIRECTION.DOWN -> copy(aim = aim + command.value)
            DIRECTION.UP -> copy(aim = aim - command.value)
        }
    }
}