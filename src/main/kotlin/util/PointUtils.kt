package util

import java.awt.Point

object PointUtils {
    fun Point.neighbours(): List<Point> {
        return listOf(
            Point(this.x - 1, this.y),
            Point(this.x + 1, this.y),
            Point(this.x, this.y - 1),
            Point(this.x, this.y + 1),
        )
    }
}