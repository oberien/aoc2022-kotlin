package day05

import Day
import java.lang.StringBuilder
import java.util.Stack

class Day05: Day {
    override fun part1(input: String): String {
        val split = input.split("\n\n");
        val crates = Crates(split[0])
        val moves = Moves(split[1])
        for (move in moves.moves) {
            crates.applyMovePart1(move)
        }
        return crates.result()
    }

    override fun part2(input: String): String {
        val split = input.split("\n\n");
        val crates = Crates(split[0])
        val moves = Moves(split[1])
        for (move in moves.moves) {
            crates.applyMovePart2(move)
        }
        return crates.result()
    }
}

private class Crates(crates: String) {
    private val stacks: MutableList<Stack<Char>> = ArrayList()

    init {
        val lines = crates.lines()
        val numStacks = lines.last().splitToSequence(Regex("\\s+"))
            .filter { it.isNotEmpty() }
            .map { it.toInt() }
            .max()
        for (i in 0 until numStacks) {
            this.stacks.add(Stack());
        }
        for (line in lines.asReversed().asSequence().drop(1)) {
            for (i in 0 until numStacks) {
                when (val c = line.getOrNull(1 + i*4)) {
                    ' ', null -> {}
                    else -> this.stacks[i].push(c)
                }
            }
        }
    }

    fun applyMovePart1(move: Move) {
        for (i in 0 until move.count) {
            val crate = this.stacks[move.from].pop()
            this.stacks[move.to].push(crate)
        }
    }

    fun applyMovePart2(move: Move) {
        val from = this.stacks[move.from]
        val to = this.stacks[move.to]
        for (char in from.asSequence().drop(from.size - move.count)) {
            to.push(char)
        }
        for (i in 0 until move.count) {
            from.pop()
        }
    }

    fun result(): String {
        val builder = StringBuilder()
        for (stack in this.stacks) {
            builder.append(stack.lastElement())
        }
        return builder.toString()
    }
}

private class Moves(moves: String) {
    val moves: MutableList<Move> = ArrayList()

    init {
        val regex = Regex("move (\\d+) from (\\d+) to (\\d+)")
        for (moveString in moves.lineSequence()) {
            val groups = regex.find(moveString)!!.groups
            val move = Move(
                groups[1]!!.value.toInt(),
                groups[2]!!.value.toInt() - 1,
                groups[3]!!.value.toInt() - 1,
            )
            this.moves.add(move)
        }
    }
}

private data class Move(
    val count: Int,
    val from: Int,
    val to: Int,
)
