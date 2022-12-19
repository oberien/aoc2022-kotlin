package day13

import Day

class Day13 : Day {
    override fun part1(input: String): String =
        parse(input, false).asSequence().withIndex()
            .filter { (_, pair) -> pair.first < pair.second }
            .map { it.index + 1 }
            .sum()
            .toString()

    override fun part2(input: String): String {
        val divider1 = Value.Vec(listOf(Value.Vec(listOf(Value.Integer(2)))))
        val divider2 = Value.Vec(listOf(Value.Vec(listOf(Value.Integer(6)))))
        val sorted = parse(input, false)
            .flatMap { (a, b) -> listOf(a, b) }
            .toMutableList()
            .apply { add(divider1); add(divider2) }
            .sorted()
        val index1 = sorted.indexOf(divider1) + 1
        val index2 = sorted.indexOf(divider2) + 1

        return "${index1 * index2}"
    }
}

private fun parse(input: String, debug: Boolean): List<Pair<Value, Value>> =
    input.splitToSequence("\n\n")
        .map {
            val (a, b) = it.split("\n")
            Pair(Value.parse(a, 0, debug).second, Value.parse(b, 0, debug).second)
        }.toList()

private sealed class Value : Comparable<Value> {
    data class Integer(val x: Int) : Value() {
        override fun toString(): String = this.x.toString()
    }
    data class Vec(val x: List<Value>) : Value() {
        override fun toString(): String = this.x.toString()
    }

    override fun compareTo(other: Value): Int {
        if (this is Value.Integer && other is Value.Integer) {
            println("int vs int: ${this.x} vs ${other.x}")
            return this.x.compareTo(other.x)
        }
        if (this is Value.Vec && other is Value.Vec) {
            println("list vs list: $this vs $other")
            var i = 0
            while (true) {
                val left = this.x.getOrNull(i)
                val right = other.x.getOrNull(i)
                when (Pair(left, right)) {
                    Pair(null, null) -> return 0
                    Pair(null, right) -> { println("left ran out"); return -1 }
                    Pair(left, null) -> { println("right ran out"); return 1 }
                }
                val cmp = left!!.compareTo(right!!)
                when {
                    cmp < 0 -> return -1
                    cmp == 0 -> {}
                    cmp > 0 -> return 1
                }
                i += 1
            }
        }
        val (left, right) = if (this is Value.Vec && other is Value.Integer) {
            println("list vs int: $this vs $other")
            Pair(this, Value.Vec(listOf(other)))
        } else {
            assert(this is Value.Integer && other is Value.Vec)
            println("int vs list: $this vs $other")
            Pair(Value.Vec(listOf(this)), other)
        }
        return left.compareTo(right)
    }

    companion object {
        @Suppress("NAME_SHADOWING")
        fun parse(expr: String, index: Int, debug: Boolean): Pair<Int, Value> {
            var index = yeetWhitespaces(expr, index, debug)
            val tryInt = parseInt(expr, index)
            if (tryInt != null) {
                if (debug) println("$index: found int ${tryInt.second}")
                return Pair(tryInt.first, Value.Integer(tryInt.second))
            }
            assert(expr[index] == '[')
            index += 1

            if (debug) println("$index: trying list")
            val list = mutableListOf<Value>()
            while (true) {
                index = yeetWhitespaces(expr, index, debug)
                if (expr[index] == ']') {
                    return Pair(index + 1, Value.Vec(list))
                }
                if (expr[index] == ',') {
                    index = yeetWhitespaces(expr, index + 1, debug)
                }
                val (idx, value) = Value.parse(expr, index, debug)
                if (debug) println("$index: found value $value")
                index = idx
                list.add(value)
            }
        }
    }
}

object Regexes {
    var NUM_REGEX = Regex("\\d+")
}

fun parseInt(expr: String, index: Int): Pair<Int, Int>? {
    val match = Regexes.NUM_REGEX.matchAt(expr, index) ?: return null
    return Pair(index + match.groupValues[0].length, match.groupValues[0].toInt())
}

@Suppress("NAME_SHADOWING")
fun yeetWhitespaces(expr: String, index: Int, debug: Boolean): Int {
    var index = index
    while (expr[index].isWhitespace()) {
        if (debug) println("$index: yeetWhitespace")
        index += 1
    }
    return index
}

fun assert(cond: Boolean) {
    if (!cond) {
        throw AssertionError()
    }
}
