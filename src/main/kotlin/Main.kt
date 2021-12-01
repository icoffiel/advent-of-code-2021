fun main() {
    println("Welcome to Advent of Code 2021!")

    val days = listOf(
        Day01()
    )

    days.forEach { it.run() }
}