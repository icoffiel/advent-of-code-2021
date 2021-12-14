package util

import java.awt.Point

object StringUtils {
    fun String.toPoint(): Point {
        val (one, two) = this.split(",")
        return Point(one.toInt(), two.toInt())
    }

    fun String.toIntList(): List<Int> =
        this
            .toList()
            .map { Character.getNumericValue(it) }
}