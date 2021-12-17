package util

import util.PointUtils.neighbours
import java.awt.Point

object GridUtils {

    operator fun <E> List<List<E>>.contains(point: Point): Boolean =
        point.y in this.indices && point.x in this[point.y].indices

    operator fun <E> List<List<E>>.get(point: Point): E = this[point.y][point.x]

    fun List<List<Int>>.lowPoints(): List<Point> {
        return this.indices
            .flatMap { y ->
                this[0].indices
                    .mapNotNull { x ->
                        val pointToCheck = Point(x, y)
                        if (this.isLowPoint(pointToCheck)) {
                            pointToCheck
                        } else {
                            null
                        }
                    }
            }
    }

    fun List<List<Int>>.basin(coOrdinate: Point, boundaryHeight: Int = 9): List<Point> {
        val foundPoints = mutableSetOf(coOrdinate)
        var currentPoints = listOf(coOrdinate)

        while (true) {

            val newPoints = currentPoints
                .map { this.neighbours(it) }
                .flatten()
                .filter { !foundPoints.contains(it) }
                .filter { this[it] != boundaryHeight }

            if (newPoints.isEmpty()) {
                return foundPoints.toList()
            }

            foundPoints.addAll(newPoints)
            currentPoints = newPoints
        }
    }

    fun <E> List<List<E>>.neighbours(coOrdinate: Point, includeDiagonals: Boolean = false): List<Point> {
        return coOrdinate
            .neighbours(includeDiagonals)
            .filter { it in this }
    }

    private fun List<List<Int>>.isLowPoint(coOrdinate: Point): Boolean {
        return neighbours(coOrdinate)
            .map { this[it] }
            .none { it <= this[coOrdinate] }
    }

}