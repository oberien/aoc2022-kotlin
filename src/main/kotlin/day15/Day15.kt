package day15

import Day
import kotlin.math.abs

class Day15 : Day {
    override fun part1Sample(input: String, sample: Boolean): String {
        val sensors = parse(input)
        val maxDistance = sensors.maxOf { it.distance }
        val minX = sensors.asSequence().flatMap { listOf(it.posX, it.beaconX) }.min() - maxDistance
        val maxX = sensors.asSequence().flatMap { listOf(it.posX, it.beaconX) }.max() + maxDistance
        val y = if (sample) 10 else 2_000_000
        return (minX..maxX).asSequence()
            .filter { x ->
                sensors.none { it.beaconX == x && it.beaconY == y }
                    && sensors.any { it.contains(x, y) }
            }.count().toString()
    }

    override fun part1(input: String): String = part1Sample(input, false)
    override fun part2(input: String): String = part2Sample(input, false)

    override fun part2Sample(input: String, sample: Boolean): String {
        val sensors = parse(input)
        val max = if (sample) 20 else 4_000_000

        for (y in 0..max) {
            var x = 0
            while (x <= max) {
                val maxDist = sensors.maxOf { it.restDistance(x, y) }
                when {
                    maxDist < 0 -> return (4_000_000L * x + y).toString()
                    maxDist == 0 -> x += 1
                    else -> x += maxDist
                }
            }
        }
        throw IllegalArgumentException("no position found")
    }
}

private fun parse(input: String): List<Sensor> =
    input.lineSequence()
        .map {
            val res = Regexes.LINE_REGEX.matchEntire(it)!!
            Sensor(
                res.groupValues[1].toInt(),
                res.groupValues[2].toInt(),
                res.groupValues[3].toInt(),
                res.groupValues[4].toInt(),
            )
        }.toList()

private data class Sensor(
    val posX: Int,
    val posY: Int,
    val beaconX: Int,
    val beaconY: Int,
) {
    val distance = abs(beaconX - posX) + abs(beaconY - posY)

    fun contains(x: Int, y: Int): Boolean =
        abs(x - posX) + abs(y - posY) <= distance

    fun restDistance(x: Int, y: Int): Int =
        distance - (abs(x - posX) + abs(y - posY))
}

private object Regexes {
    val LINE_REGEX = Regex("Sensor at x=(-?\\d+), y=(-?\\d+): closest beacon is at x=(-?\\d+), y=(-?\\d+)")
}
