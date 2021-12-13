package util

import java.io.File

object FileUtil {
    fun getInput(file: String, path: String = "src/main/resources/inputs") = File("$path/$file").readLines()
    fun getInputString(file: String, path: String = "src/main/resources/inputs") = File("$path/$file").readText()
}