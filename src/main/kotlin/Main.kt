import days.*

fun main() {
    println("Welcome to Advent of Code 2021!")
    println()

    val days = listOf(
        Day01(),
        Day02(),
        Day03(),
        Day04(),
        Day05(),
        Day06(),
        Day07(),
        Day08(),
        Day09(),
        Day10(),
        Day11()
    )

    days.forEach {
        it.run()
        println()
    }
}