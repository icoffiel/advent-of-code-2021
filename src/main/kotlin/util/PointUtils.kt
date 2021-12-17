package util

import java.awt.Point

object PointUtils {
    fun Point.neighbours(includeDiagonals: Boolean = false): List<Point> {
        val horizontalNeighbours = listOf(
            Point(this.x - 1, this.y),
            Point(this.x + 1, this.y),
        )

        val verticalNeighbours = listOf(
            Point(this.x, this.y - 1),
            Point(this.x, this.y + 1),
        )

        val diagonalNeighbours = listOf(
            Point(this.x - 1, this.y - 1),
            Point(this.x - 1, this.y + 1),
            Point(this.x + 1, this.y - 1),
            Point(this.x + 1, this.y + 1),
        )

        val combinedList = if (includeDiagonals) {
            horizontalNeighbours + verticalNeighbours + diagonalNeighbours
        } else {
            horizontalNeighbours + verticalNeighbours
        }

        return combinedList
    }
}