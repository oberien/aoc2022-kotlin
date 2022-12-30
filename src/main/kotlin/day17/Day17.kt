package day17

import Day
import java.text.DecimalFormat
import kotlin.math.max

class Day17 : Day {
    override fun part1(input: String): String {
        val steam = parse(input).cycle().iterator()
        val pieceList = listOf(PieceTypes.LINE_HORIZONTAL, PieceTypes.PLUS, PieceTypes.CORNER, PieceTypes.LINE_VERTICAL, PieceTypes.O)
        val pieces = pieceList.cycle().iterator()
        val field = Field()
        for (i in 0 until 2022) {
            field.spawn(pieces.next())
//            println(field)
            field.moveCurrentToStop(steam)
        }
        return field.height.toString()
    }

    override fun part2(input: String): String {
        return ""
    }
}

private fun parse(input: String): List<Direction> =
    input.asSequence()
        .map { when (it) {
            '>' -> Direction.Right
            '<' -> Direction.Left
            else -> throw IllegalArgumentException("illegal character $it in input")
        } }.toList()

private enum class Direction {
    Left,
    Right,
}

private class Field {
    val stoppedPieces = mutableListOf<Piece>()
    val width = 7
    var height = 0
    var currentPiece: Piece? = null

    fun spawn(piece: Array<Array<Boolean>>) {
        if (currentPiece != null) {
            throw IllegalStateException("current piece not stopped yet")
        }

        currentPiece = Piece(2, height + 3, piece)
    }

    fun moveCurrentToStop(steam: Iterator<Direction>) {
        if (currentPiece == null) {
            throw IllegalStateException("moveToStop called without currentPiece")
        }

        while (moveCurrent(steam) != MoveResult.STOPPED) {
//            println(this)
        }

        height = max(height, currentPiece!!.y + currentPiece!!.array.size)
        stoppedPieces.add(currentPiece!!)
        currentPiece = null
    }

    fun moveCurrent(steam: Iterator<Direction>): MoveResult {
        when (steam.next()) {
            Direction.Left -> currentPiece!!.moveLeft(this)
            Direction.Right -> currentPiece!!.moveRight(this)
        }
        return currentPiece!!.moveDown(this)
    }

    fun isFree(spaces: List<Pair<Int, Int>>): Boolean =
        stoppedPieces.all { piece -> spaces.none { piece.occupies(it.first, it.second) } }

    override fun toString(): String {
        val field = Array(height + 3 + 4) { Array(width) { '.' } }
        for ((i, piece) in stoppedPieces.withIndex()) {
            piece.draw(field, "ABCDEFGHIJKLMNOPQRSTUVWXYZ"[i % 26])
        }
        currentPiece?.draw(field, '@')
        return field.withIndex().joinToString("\n") { (i, it) -> "%4d ".format(height + 3 + 4 - i - 1) + it.joinToString("") } + "\n"
    }
}

private object PieceTypes {
    val LINE_HORIZONTAL = arrayOf(arrayOf(true, true, true, true))
    val PLUS = arrayOf(
        arrayOf(false, true, false),
        arrayOf(true, true, true),
        arrayOf(false, true, false),
    )
    val CORNER = arrayOf(
        arrayOf(false, false, true),
        arrayOf(false, false, true),
        arrayOf(true, true, true),
    )
    val LINE_VERTICAL = arrayOf(
        arrayOf(true),
        arrayOf(true),
        arrayOf(true),
        arrayOf(true),
    )
    val O = arrayOf(
        arrayOf(true, true),
        arrayOf(true, true),
    )
}

private enum class MoveResult {
    STOPPED,
    UNSTOPPED,
}

private class Piece(
    var x: Int,
    var y: Int,
    val array: Array<Array<Boolean>>,
) {
    val width = array[0].size
    val height = array.size

    fun moveDown(field: Field): MoveResult {
//        println("moveDown $x:$y")
        if (this.y == 0) {
            return MoveResult.STOPPED
        }
        val list = mutableListOf<Pair<Int, Int>>()
        for (x in array[0].indices) {
            val y = array.reversed().map { it[x] }.indexOf(true)
            list.add(Pair(this.x + x, this.y - 1 + y))
        }
        return if (field.isFree(list)) {
            this.y -= 1
            MoveResult.UNSTOPPED
        } else {
            MoveResult.STOPPED
        }
    }
    fun moveLeft(field: Field) {
//        println("moveLeft $x:$y")
        if (this.x == 0) {
            return
        }
        val list = mutableListOf<Pair<Int, Int>>()
        for (y in array.indices) {
            val x = array[y].indexOf(true)
            list.add(Pair(this.x + x - 1, this.y + (height - 1 - y)))
        }
        if (field.isFree(list)) {
            this.x -= 1
        }
    }
    fun moveRight(field: Field) {
//        println("moveRight $x:$y")
        if (this.x + width == 7) {
            return
        }
        val list = mutableListOf<Pair<Int, Int>>()
        for (y in array.indices) {
            val x = array[y].reversed().indexOf(true)
            list.add(Pair(this.x + width - 1 - x + 1, this.y + (height - 1 - y)))
        }
        if (field.isFree(list)) {
            this.x += 1
        }
    }
    fun occupies(x: Int, y: Int): Boolean {
        val occupies = array.getOrNull(height - 1 - (y - this.y))?.getOrNull(x - this.x) ?: false
//        println("${this.x}:${this.y}w${width}h${height} occupies: $x:$y indexes ${x - this.x}:${height - 1 - (y - this.y)}: $occupies")
        return occupies
    }

    fun draw(field: Array<Array<Char>>, char: Char) {
        for (y in array.indices) {
            for (x in array[y].indices) {
                if (array[y][x]) {
                    field[field.size - this.y - height + y][this.x + x] = char
                }
            }
        }
    }
}

fun <T : Any> List<T>.cycle(): Sequence<T> {
    var i = 0
    return generateSequence { this[i++ % this.size] }
}

