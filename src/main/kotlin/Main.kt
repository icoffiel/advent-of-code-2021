import days.Day01
import days.Day02

fun main() {
    println("Welcome to Advent of Code 2021!")
    println()

    val days = listOf(
        Day01(),
        Day02(),
    )

    days.forEach {
        it.run()
        println()
    }
}