package day04

import Day

class Day04 : Day {
    override fun part1(input: String): String =
        parse(input)
            .count { (a, b) -> a.contains(b) || b.contains(a) }
            .toString()

    override fun part2(input: String): String =
        parse(input)
            .count { (a, b) -> a.overlaps(b) }
            .toString()

    private fun parse(input: String): Sequence<Pair<ClosedRange<Int>, ClosedRange<Int>>> =
        input.lineSequence().filter { it.isNotEmpty() }
            .map { it.split(",") }
            .map { (first, second) -> Pair(first.split("-"), second.split("-")) }
            .map { (a, b) -> Pair(a[0].toInt()..a[1].toInt(), b[0].toInt()..b[1].toInt()) }
}

private fun ClosedRange<Int>.contains(other: ClosedRange<Int>) =
    this.contains(other.start) && this.contains(other.endInclusive)

private fun ClosedRange<Int>.overlaps(other: ClosedRange<Int>) =
    other.start <= this.start && this.start <= other.endInclusive
    || other.start <= this.endInclusive && this.endInclusive <= other.endInclusive
    || this.start <= other.start && other.start <= this.endInclusive
    || this.start <= other.endInclusive && other.endInclusive <= this.start
