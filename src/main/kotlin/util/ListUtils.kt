package util

import util.StringUtils.toIntList

object ListUtils {
    fun List<String>.toIntGrid() = this.map { it.toIntList() }
}