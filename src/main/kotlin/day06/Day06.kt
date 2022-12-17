package day06

import Day

class Day06 : Day {
    override fun part1(input: String): String =
        (input.asSequence().windowed(4).withIndex()
            .first { (_, v) -> v[0] != v[1] && v[0] != v[2] && v[0] != v[3] && v[1] != v[2] && v[1] != v[3] && v[2] != v[3] }
            .index + 4).toString()

    override fun part2(input: String): String =
        (input.asSequence().windowed(14).withIndex()
            .first { (_, v) -> v.toSet().size == 14 }
            .index + 14).toString()
}
