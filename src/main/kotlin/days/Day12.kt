package days

import util.FileUtils.getInput

fun main() {
    Day12().run()
}

class Day12 : Day {
    override fun run() {
        val input = getInput("Day12.txt")
        val sanitizedInput = input.map { it.trim() }

        println("Results for Day Twelve:")
        println("Part One: ${partOne(sanitizedInput)}")
        println("Part Two: ${partTwo(sanitizedInput)}")
    }

    private fun partOne(sanitizedInput: List<String>): Int {
        val graph: Graph = Graph.fromInput(sanitizedInput)
        val setOfPathsFound = mutableSetOf<List<String>>()

        graph.visitNodes("start", "end", setOfPathsFound, smallToBigCave = null)

        return setOfPathsFound.count()
    }

    private fun partTwo(sanitizedInput: List<String>): Int {
        val graph: Graph = Graph.fromInput(sanitizedInput)
        val setOfPathsFound = mutableSetOf<List<String>>()
        val smallCaves = sanitizedInput
            .flatMap { it.split("-") }
            .filter { !it.isLargeCave() }
            .filter { it !in listOf("start", "end") }
            .distinct()

        smallCaves
            .forEach { cave ->
                graph.visitNodes("start", "end", setOfPathsFound, cave)
            }

        return setOfPathsFound.count()
    }
}

class Graph {
    private val vertices = mutableMapOf<String, MutableList<String>>()

    companion object Factory {
        fun fromInput(sanitizedInput: List<String>): Graph {
            val graph = Graph()
            sanitizedInput
                .map { it.split("-") }
                .forEach { (start, end) -> graph.addEdge(start, end) }
            return graph
        }
    }

    fun addEdge(source: String, destination: String) {
        vertices
            .getOrPut(source) { mutableListOf() }
            .add(destination)

        vertices
            .getOrPut(destination) { mutableListOf() }
            .add(source)
    }

    fun visitNodes(start: String, end: String, setOfPathsFound: MutableSet<List<String>>, smallToBigCave: String?, pathList: List<String> = listOf(start)) {
        if (start == end) {
            setOfPathsFound += pathList
            return
        }

        val nodesToCheck =
            vertices.getValue(start)
                .filter { (it.isLargeCave() || it !in pathList) || (it == smallToBigCave && pathList.count { it == smallToBigCave } <= 1) }

        for (node in nodesToCheck) {
            visitNodes(node, end, setOfPathsFound, smallToBigCave, pathList + node)
        }
    }

    override fun toString(): String {
        return vertices.toString()
    }
}

private fun String.isLargeCave(): Boolean = this.uppercase() == this
