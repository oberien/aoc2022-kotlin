package day03

import Day
import java.lang.Exception

class Day03: Day {
    override fun part1(input: String): Int =
        input.lineSequence()
            .filter { it.isNotEmpty() }
            .map { Pair(it.substring(0 until it.length/2), it.substring(it.length/2 until it.length)) }
            .map { (a, b) -> Pair(a.asSequence().toSet(), b) }
            .map { (set, b) -> b.asSequence().filter { set.contains(it) }.first() }
            .map { when {
                ('a'..'z').contains(it) -> it - 'a' + 1
                ('A'..'Z').contains(it) -> it - 'A' + 27
                else -> throw Exception("unreachable")
            } }
            .sum()

    override fun part2(input: String): Int =
        input.lineSequence()
            .filter { it.isNotEmpty() }
            .chunked(3)
            .map { (a, b, c) -> a.asSequence().distinct().plus(b.asSequence().distinct()).plus(c.asSequence().distinct()) }
            .map {
                val map = HashMap<Char, Int>()
                for (c in it) {
                    map[c] = (map[c] ?: 0) + 1
                }
                map.asSequence().maxBy { (_, count) -> count }.key
            }
            .map { when {
                ('a'..'z').contains(it) -> it - 'a' + 1
                ('A'..'Z').contains(it) -> it - 'A' + 27
                else -> throw Exception("unreachable")
            } }
            .sum()
}
