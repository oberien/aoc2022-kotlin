package day08

import Day
import kotlin.math.max

class Day08 : Day {
    override fun part1(input: String): String {
        val trees = parse(input)

        var numVisible = 0
        for (y in trees.indices) {
            for (x in trees[0].indices) {
                val height = trees[y][x].height
                var visible = false
                visible = visible || (0 until x).asSequence().map { trees[y][it].height }.all { it < height }
                visible = visible || (x+1 until trees[0].size).asSequence().map { trees[y][it].height }.all { it < height }
                visible = visible || (0 until y).asSequence().map { trees[it][x].height }.all { it < height }
                visible = visible || (y+1 until trees.size).asSequence().map { trees[it][x].height }.all { it < height }
                numVisible += visible.toInt()
            }
        }
        return numVisible.toString()
    }

    override fun part2(input: String): String {
        val trees = parse(input)
        var max = 0
        for (y in trees.indices) {
            for (x in trees[0].indices) {
                val spaceLeft = x
                val spaceRight = trees[0].size - x - 1
                val spaceUp = y
                val spaceDown = trees.size - y - 1
                val tree = trees[y][x]
                val height = tree.height
                val left = (x-1 downTo 0).asSequence().map { trees[y][it].height }.indexOfFirst { it >= height }.takeIf { it >= 0 }?.let { it + 1 } ?: spaceLeft
                val right = (x+1 until trees[0].size).asSequence().map { trees[y][it].height }.indexOfFirst { it >= height }.takeIf { it >= 0 }?.let { it + 1 } ?: spaceRight
                val up = (y-1 downTo 0).asSequence().map { trees[it][x].height }.indexOfFirst { it >= height }.takeIf { it >= 0 }?.let { it + 1 } ?: spaceUp
                val down = (y+1 until trees.size).asSequence().map { trees[it][x].height }.indexOfFirst { it >= height }.takeIf { it >= 0 }?.let { it + 1 } ?: spaceDown
                print("${left}:${right}:${up}:${down} ")
                max = max(max, left * right * up * down)
            }
            println()
        }
        return max.toString()
    }
}

private fun parse(input: String): List<List<Tree>> =
    input.lineSequence()
        .map { it.asSequence().map { num -> Tree(num.digitToInt()) }.toList() }
        .toList()

data class Tree(val height: Int)

private fun Boolean.toInt() = if (this) 1 else 0
