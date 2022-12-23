package day16

import Day
import kotlin.math.max

class Day16 : Day {
    override fun part1(input: String): String {
        val graph = parse(input)
        return dfsPart1(Memoization(), graph, graph.nodes["AA"]!!, 30, setOf()).toString()
    }

    override fun part2(input: String): String {
        // runtime with this approach is insane
        //  9 takes   4.5s
        // 10 takes  21s
        // 11 takes 142s
        // -> exponential growth
        // estimated time for 26 = 5^17 = 25.000 years
        val graph = parse(input)
        return dfsPart2(Memoization(), graph, graph.nodes["AA"]!!, graph.nodes["AA"]!!, 8, setOf()).toString()
    }
}

private class Memoization() {
    val memoization = HashMap<String, Int>()

    fun insert(player1: Valve, player2: Valve?, timeLeft: Int, turnedValves: Set<Valve>, max: Int) {
        memoization[hash(player1, player2, timeLeft, turnedValves)] = max
    }

    fun get(player1: Valve, player2: Valve?, timeLeft: Int, turnedValves: Set<Valve>): Int? =
        memoization[hash(player1, player2, timeLeft, turnedValves)]

    fun hash(player1: Valve, player2: Valve?, timeLeft: Int, turnedValves: Set<Valve>): String {
        val playerHash = if (player2 != null) {
            listOf(player1.name, player2.name).sorted().joinToString("")
        } else {
            player1.name
        }
        val turnedHash = turnedValves.asSequence().map { it.name }.sorted().joinToString("")
        return "${playerHash}:$timeLeft:$turnedHash"
    }
}

private fun dfsPart1(memoization: Memoization, graph: Graph, current: Valve, timeLeft: Int, turnedValves: Set<Valve>): Int {
    if (timeLeft <= 1) {
        return 0
    }
    val memoized = memoization.get(current, null, timeLeft, turnedValves)
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
    memoization.insert(current, null, timeLeft, turnedValves, max)
    return max
}

private fun dfsPart2(memoization: Memoization, graph: Graph, player1: Valve, player2: Valve, timeLeft: Int, turnedValves: Set<Valve>): Int {
    if (timeLeft <= 1) {
        return 0
    }
    val memoized = memoization.get(player1, player2, timeLeft, turnedValves)
    if (memoized != null) {
        return memoized
    }

    val valvePossibilities = mutableSetOf(Pair(false, false), Pair(true, false), Pair(false, true), Pair(true, true))
    if (turnedValves.contains(player1)) {
        valvePossibilities.remove(Pair(true, false))
        valvePossibilities.remove(Pair(true, true))
    }
    if (turnedValves.contains(player2)) {
        valvePossibilities.remove(Pair(false, true))
        valvePossibilities.remove(Pair(true, true))
    }
    if (player1 == player2) {
        valvePossibilities.remove(Pair(true, true))
    }

    var max = 0

    val released1 = (timeLeft - 1) * player1.rate
    val released2 = (timeLeft - 1) * player2.rate
    for (possibility in valvePossibilities) {
        when (possibility) {
            Pair(true, true) -> {
                val turned = HashSet(turnedValves).apply { add(player1); add(player2) }
                val released = released1 + released2
                val maxRest = dfsPart2(memoization, graph, player1, player2, timeLeft - 1, turned)
                max = max(max, released + maxRest)
            }
            Pair(true, false) -> {
                val turned = HashSet(turnedValves).apply { add(player1) }
                for (path in player2.paths) {
                    max = max(max, released1 + dfsPart2(memoization, graph, player1, graph.nodes[path]!!, timeLeft - 1, turned))
                }
            }
            Pair(false, true) -> {
                val turned = HashSet(turnedValves).apply { add(player2) }
                for (path in player1.paths) {
                    max = max(max, released2 + dfsPart2(memoization, graph, graph.nodes[path]!!, player2, timeLeft - 1, turned))
                }
            }
            Pair(false, false) -> {
                for (path1 in player1.paths) {
                    for (path2 in player2.paths) {
                        max = max(max, dfsPart2(memoization, graph, graph.nodes[path1]!!, graph.nodes[path2]!!, timeLeft - 1, turnedValves))
                    }
                }
            }
        }
    }

    memoization.insert(player1, player2, timeLeft, turnedValves, max)
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
