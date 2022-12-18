package day12

import Day

class Day12 : Day {
    override fun part1(input: String): String =
        parse(input).let { input ->
            PathFinder(input.heights).findPath(
                input.start,
                { current, next -> next <= current + 1 },
                { pos -> pos == input.end },
            ).toString()
        }

    override fun part2(input: String): String =
        parse(input).let { input ->
            PathFinder(input.heights).findPath(
                input.end,
                { current, next -> current <= next + 1 },
                { pos -> input.heights[pos.y][pos.x] == 0 },
            ).toString()
        }
}

private class PathFinder(private val heights: List<List<Int>>) {
    private val visited: MutableSet<Position> = mutableSetOf()
    private val worklist: ArrayDeque<Pair<Position, Int>> = ArrayDeque()

    fun findPath(start: Position, heightCondition: (Int, Int) -> Boolean, endCondition: (Position) -> Boolean): Int {
        worklist.addLast(Pair(start, 0))
        visited.add(start)
        do {
            val (current, len) = worklist.removeFirst()
            val currentHeight = heights[current.y][current.x]
            val left = if (current.x == 0) null else Position(current.x - 1, current.y)
            val right = if (current.x + 1 == heights[0].size) null else Position(current.x + 1, current.y)
            val up = if (current.y == 0) null else Position(current.x, current.y - 1)
            val down = if (current.y + 1 == heights.size) null else Position(current.x, current.y + 1)
            for (pos in listOfNotNull(left, right, up, down)) {
                if (visited.contains(pos)) {
                    continue
                }
                if (!heightCondition(currentHeight, heights[pos.y][pos.x])) {
                    continue
                }
                if (endCondition(pos)) {
                    return len + 1
                }
                worklist.addLast(Pair(pos, len + 1))
                visited.add(pos)
            }
        } while (worklist.isNotEmpty())
        throw IllegalStateException("no path found")
    }
}

private fun parse(input: String): Input {
    var start: Position? = null
    var end: Position? = null
    val heights = input.lineSequence().withIndex().map { (y, line) ->
        line.asSequence().withIndex()
            .map { (x, char) -> when (char) {
                'S' -> { start = Position(x, y); 0 }
                'E' -> { end = Position(x, y); 26 }
                else -> char.code - 'a'.code
            } }.toList()
    }.toList()
    return Input(heights, start!!, end!!)
}

private data class Position(val x: Int, val y: Int)

private data class Input(
    val heights: List<List<Int>>,
    val start: Position,
    val end: Position,
)
