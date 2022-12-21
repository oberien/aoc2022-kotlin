package day14

import Day
import kotlin.math.max
import kotlin.math.min

class Day14 : Day {
    override fun part1(input: String): String =
        parse(input).run {
            var num = 0
            while (this.step() != StepResult.FellOff) {
                num += 1
            }
            num.toString()
        }

    override fun part2(input: String): String {
        var map = parse(input)
        var num = 0
        map = map.addFloor()
        while (map.map[map.sandSource] != Tile.Sand) {
            if (map.step() == StepResult.FellOff) {
                throw IllegalStateException("FellOff with Floor")
            }
            num += 1
        }
        return num.toString()
    }
}

private fun parse(input: String): Map {
    var minX = 500
    var maxX = 500
    var minY = 0
    var maxY = 0
    val map = HashMap<Pair<Int, Int>, Tile>()
    input.lineSequence()
        .flatMap { it.split(" -> ").windowed(2) }
        .forEach {
            val (from, to) = it
            val (fromx, fromy) = from.split(",").map(String::toInt)
            val (tox, toy) = to.split(",").map(String::toInt)
            for (y in fromy toward toy) {
                for (x in fromx toward tox) {
                    minX = min(minX, x)
                    maxX = max(maxX, x)
                    minY = min(minY, y)
                    maxY = max(maxY, y)
                    map[Pair(x, y)] = Tile.Rock
                }
            }
        }
    return Map(map, minX, maxX, minY, maxY)
}

private enum class Tile {
    Sand,
    Rock,
}

private enum class StepResult {
    Landed,
    FellOff,
}

private data class Map(
    val map: HashMap<Pair<Int, Int>, Tile>,
    val minX: Int,
    val maxX: Int,
    val minY: Int,
    val maxY: Int,
) {
    val sandSource = Pair(500, 0)

    /**
     * For part2 a floor needs to be added
     */
    fun addFloor(): Map {
        val y = maxY + 2
        val yrange = maxY - minY
        for (x in minX - yrange .. maxX + yrange) {
            map[Pair(x, y)] = Tile.Rock
        }
        return Map(map, minX - yrange, maxX + yrange, minY, maxY + 2)
    }

    fun step(): StepResult {
        var (x, y) = sandSource
        while (true) {
            val positions = listOf(Pair(x, y+1), Pair(x-1, y+1), Pair(x+1, y+1))
            var moved = false
            for (newPos in positions) {
                when (map[newPos]) {
                    Tile.Sand, Tile.Rock -> continue
                    null -> {
                        x = newPos.first
                        y = newPos.second
                        moved = true
                        break
                    }
                }
            }
            if (y > maxY) {
                return StepResult.FellOff
            }
            if (!moved) {
                map[Pair(x, y)] = Tile.Sand
                return StepResult.Landed
            }
        }
    }

    fun display(): String {
        val sb = StringBuilder()
        for (y in minY..maxY) {
            for (x in minX..maxX) {
                val char = when (map[Pair(x, y)]) {
                    null -> '.'
                    Tile.Sand -> 'o'
                    Tile.Rock -> '#'
                }
                sb.append(char)
            }
            sb.appendLine()
        }
        return sb.toString()
    }
}

private infix fun Int.toward(to: Int): IntProgression {
    val step = if (this > to) -1 else 1
    return IntProgression.fromClosedRange(this, to, step)
}
