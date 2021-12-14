package util

import util.IntUtils.toward
import java.awt.Point

data class Line(val start: Point, val end: Point) {
    private fun toIndividualPointsHorizontalVertical(): List<Point> {
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

    private fun toIndividualPointsIncDiagonal(): List<Point> {
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

    fun horizontalAndVerticalOnly() = start.x == end.x || start.y == end.y
}
