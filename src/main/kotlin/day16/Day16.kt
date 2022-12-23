package day16

import Day
import kotlin.math.max

class Day16 : Day {
    override fun part1(input: String): String {
        val graph = parse(input)
        return dfsPart1(Memoization(), graph, graph.nodes["AA"]!!, 30, setOf()).toString()
    }

    override fun part2(input: String): String {
        return ""
    }
}

private class Memoization() {
    val memoization = HashMap<String, Int>()

    fun insert(current: Valve, timeLeft: Int, turnedValves: Set<Valve>, max: Int) {
        memoization[hash(current, timeLeft, turnedValves)] = max
    }

    fun get(current: Valve, timeLeft: Int, turnedValves: Set<Valve>): Int? =
        memoization[hash(current, timeLeft, turnedValves)]

    fun hash(current: Valve, timeLeft: Int, turnedValves: Set<Valve>): String {
        val turnedHash = turnedValves.asSequence().map { it.name }.sorted().joinToString("")
        return "${current.name}:$timeLeft:$turnedHash"
    }
}

private fun dfsPart1(memoization: Memoization, graph: Graph, current: Valve, timeLeft: Int, turnedValves: Set<Valve>): Int {
    if (timeLeft <= 1) {
        return 0
    }
    val memoized = memoization.get(current, timeLeft, turnedValves)
    if (memoized != null) {
        return memoized
    }

    var max = 0
    if (!turnedValves.contains(current) && current.rate > 0) {
        val turned = HashSet<Valve>()
        turned.addAll(turnedValves)
        turned.add(current)
        val released = (timeLeft - 1) * current.rate
        val maxRest = dfsPart1(memoization, graph, current, timeLeft - 1, turned)
        max = released + maxRest
    }
    for (path in current.paths) {
        val maxRest = dfsPart1(memoization, graph, graph.nodes[path]!!, timeLeft - 1, turnedValves)
        max = max(max, maxRest)
    }
    memoization.insert(current, timeLeft, turnedValves, max)
    return max
}

private fun parse(input: String): Graph =
    Graph(input.lineSequence()
        .map {
            val line = Regexes.LINE_REGEX.matchEntire(it)!!
            val paths = Regexes.VALVE_REGEX.findAll(line.groupValues[3]).map { it.value }.toList()
            Valve(line.groupValues[1], line.groupValues[2].toInt(), paths)
        }.map { Pair(it.name, it) }
        .toMap(HashMap()))

private data class Graph(
    val nodes: HashMap<String, Valve>
)

private data class Valve(
    val name: String,
    val rate: Int,
    val paths: List<String>,
)

private object Regexes {
    val LINE_REGEX = Regex("Valve ([A-Z]{2}) has flow rate=(\\d+); tunnel(.*)$")
    val VALVE_REGEX = Regex("[A-Z]{2}")
}
