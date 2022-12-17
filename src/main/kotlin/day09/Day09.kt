package day09

import Day
import kotlin.math.max
import kotlin.math.min

class Day09: Day {
    override fun part1(input: String): String =
        State(1).apply { applyMoves(parse(input)) }.numVisitedTailPositions().toString()

    override fun part2(input: String): String =
        State(9).apply { applyMoves(parse(input)) }.numVisitedTailPositions().toString()
}

fun parse(input: String): List<Move> =
    input.lineSequence()
        .map { it.split(" ") }
        .map { (dir, count) -> when (dir) {
            "L" -> Move(Direction.LEFT, count.toInt())
            "R" -> Move(Direction.RIGHT, count.toInt())
            "U" -> Move(Direction.UP, count.toInt())
            "D" -> Move(Direction.DOWN, count.toInt())
            else -> throw IllegalArgumentException("direction is not LRUD but $dir")
        } }
        .toList()

enum class Direction {
    LEFT, RIGHT, UP, DOWN
}
data class Move(val direction: Direction, val count: Int)

data class Position(var x: Int, var y: Int)


class State(private val knotCount: Int) {
    private val head = Position(0, 0)
    private val knots = Array(knotCount) { Position(0, 0) }
    private val visitedTailPositions = hashSetOf(this.knots.last())
    private var minX = 0
    private var maxX = 0
    private var minY = 0
    private var maxY = 0

    private val MOVE_TABLE = arrayOf(
        arrayOf(Pair(-1, -1), Pair(-1, -1), Pair( 0, -1), Pair( 1, -1), Pair( 1, -1)),
        arrayOf(Pair(-1, -1), Pair( 0,  0), Pair( 0,  0), Pair( 0,  0), Pair( 1, -1)),
        arrayOf(Pair(-1,  0), Pair( 0,  0), Pair( 0,  0), Pair( 0,  0), Pair( 1,  0)),
        arrayOf(Pair(-1,  1), Pair( 0,  0), Pair( 0,  0), Pair( 0,  0), Pair( 1,  1)),
        arrayOf(Pair(-1,  1), Pair(-1,  1), Pair( 0,  1), Pair( 1,  1), Pair( 1,  1)),
    )

    fun applyMoves(moves: List<Move>) = moves.forEach { this.applyMove(it) }
    fun numVisitedTailPositions() = this.visitedTailPositions.size
    fun debugMoves(moves: List<Move>) {
        val firstPass = State(this.knotCount).apply { applyMoves(moves) }
        this.minX = firstPass.minX
        this.maxX = firstPass.maxX
        this.minY = firstPass.minY
        this.maxY = firstPass.maxY
        draw()
        for (move in moves) {
            applyMove(move)
            draw()
        }
    }

    private fun applyMove(move: Move) {
        for (i in 0 until move.count) {
            this.moveHead(move.direction)
            this.minX = min(this.minX, this.head.x)
            this.maxX = max(this.maxX, this.head.x)
            this.minY = min(this.minY, this.head.y)
            this.maxY = max(this.maxY, this.head.y)
            this.moveKnots()
            visitedTailPositions.add(this.knots.last().copy())
        }
    }
    private fun moveHead(dir: Direction) = when (dir) {
        Direction.LEFT -> this.head.x -= 1
        Direction.RIGHT -> this.head.x += 1
        Direction.UP -> this.head.y -= 1
        Direction.DOWN -> this.head.y += 1
    }
    private fun moveKnots() {
        var last = this.head
        for (current in this.knots) {
            val dx = last.x - current.x
            val dy = last.y - current.y
            val (x, y) = MOVE_TABLE[dy + 2][dx + 2]
            current.x += x
            current.y += y
            last = current
        }
    }

    private fun draw() {
        val array: Array<Array<Char>> = Array(maxY - minY + 1) { Array(maxX - minX + 1) { '.' } }
        array[0 - minY][0 - minX] = 's'
        for ((i, knot) in knots.withIndex().reversed()) {
            array[knot.y - minY][knot.x - minX] = (i + 1).toString()[0]
        }
        array[head.y - minY][head.x - minX] = 'H'
        for (line in array) {
            for (char in line) {
                print(char)
            }
            println()
        }
        println()
    }
}
