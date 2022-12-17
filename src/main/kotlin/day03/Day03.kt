package day03

import Day
import java.lang.Exception

class Day03 : Day {
    override fun part1(input: String): String =
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
            .toString()

    override fun part2(input: String): String =
        input.lineSequence()
            .filter { it.isNotEmpty() }
            .chunked(3)
            .map { (a, b, c) -> a.asIterable().intersect(b.toSet()).intersect(c.toSet()).single() }
            .map { when {
                ('a'..'z').contains(it) -> it - 'a' + 1
                ('A'..'Z').contains(it) -> it - 'A' + 27
                else -> throw Exception("unreachable")
            } }
            .sum()
            .toString()
}
