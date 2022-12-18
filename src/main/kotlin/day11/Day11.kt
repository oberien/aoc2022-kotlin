package day11

import Day
import java.math.BigInteger

class Day11 : Day {
    override fun part1(input: String): String {
        val monkeys = parse(input)
        val numInspections = runRounds(monkeys, 20, 3.toBigInteger(), false)
        numInspections.sortDescending()
        return (numInspections[0] * numInspections[1]).toString()
    }

    override fun part2(input: String): String {
        val monkeys = parse(input)
        val numInspections = runRounds(monkeys, 10000, 1.toBigInteger(), false)
        numInspections.sortDescending()
        return (numInspections[0].toLong() * numInspections[1].toLong()).toString()
    }
}

private fun parse(input: String): List<Monkey> =
    input.splitToSequence("\n\n").map(Monkey::parse).toList()

private fun runRounds(monkeys: List<Monkey>, numRounds: Int, divisor: BigInteger, debug: Boolean): Array<Int> {
    val modulo = monkeys.asSequence().map { it.test.divisibleBy }.reduce { a, b -> a * b }
    val numInspections = Array(monkeys.size) { 0 }

    for (round in 1..numRounds) {
        for ((i, monkey) in monkeys.withIndex()) {
            if (i != monkey.num.num) {
                throw IllegalStateException("i != monkeyNum")
            }
            numInspections[i] += monkey.items.size
            var item: Item?
            while (run { item = monkey.items.removeFirstOrNull(); item } != null) {
                val worryLevelBefore = item!!.worryLevel
                val worryLevelInc = monkey.operation.apply(worryLevelBefore)
                val worryLevel = (worryLevelInc / divisor) % modulo
                val newMonkey = monkey.test.test(worryLevel)
                monkeys[newMonkey.num].items.addLast(Item(worryLevel))
                if (debug) println("Monkey $i inspects $worryLevelBefore -> $worryLevelInc -> $worryLevel throws to ${newMonkey.num}")
            }
        }
        println("$round: ${numInspections.joinToString(", ")}")
    }
    return numInspections
}

private data class Monkey(
    val num: MonkeyNum,
    val items: ArrayDeque<Item>,
    val operation: Operation,
    val test: Test,
) {
    companion object {
        fun parse(monkey: String): Monkey =
        monkey.split("\n").let { lines ->
            Monkey(
                num = MonkeyNum.parse(lines[0]),
                items = Item.parse(lines[1]),
                operation = Operation.parse(lines[2]),
                test = Test.parse(lines[3], lines[4], lines[5]),
            )
        }
    }
}

private sealed class Operation {
    data class Mul(val a: Value, val b: Value) : Operation() {
        override fun apply(old: BigInteger): BigInteger = a.get(old) * b.get(old)
    }

    data class Plus(val a: Value, val b: Value) : Operation() {
        override fun apply(old: BigInteger): BigInteger = a.get(old) + b.get(old)
    }

    abstract fun apply(old: BigInteger): BigInteger

    companion object {
        fun parse(line: String): Operation =
            Regexes.OPERATION_REGEX.matchEntire(line)!!.groupValues.let {
                val a = Value.parse(it[1])
                val b = Value.parse(it[3])
                when (it[2]) {
                    "*" -> Operation.Mul(a, b)
                    "+" -> Operation.Plus(a, b)
                    else -> throw IllegalArgumentException("operation not * or +")
                }
            }
    }
}

private sealed class Value {
    data class Constant(val a: BigInteger) : Value() {
        override fun get(old: BigInteger): BigInteger = a
    }
    object Old : Value() {
        override fun get(old: BigInteger): BigInteger = old
    }

    abstract fun get(old: BigInteger): BigInteger

    companion object {
        fun parse(value: String): Value =
            when (value) {
                "old" -> Value.Old
                else -> Value.Constant(value.toInt().toBigInteger())
            }
    }
}

private data class Test(
    val divisibleBy: BigInteger,
    val ifTrueMonkey: MonkeyNum,
    val ifFalseMonkey: MonkeyNum,
) {
    fun test(num: BigInteger): MonkeyNum = if (num % this.divisibleBy == 0.toBigInteger()) ifTrueMonkey else ifFalseMonkey

    companion object {
        fun parse(testLine: String, ifTrueLine: String, ifFalseLine: String): Test =
            Test(
                divisibleBy = Regexes.TEST_REGEX.matchEntire(testLine)!!.groupValues[1].toInt().toBigInteger(),
                ifTrueMonkey = MonkeyNum(Regexes.IF_TRUE_REGEX.matchEntire(ifTrueLine)!!.groupValues[1].toInt()),
                ifFalseMonkey = MonkeyNum(Regexes.IF_FALSE_REGEX.matchEntire(ifFalseLine)!!.groupValues[1].toInt()),
            )
    }
}

private data class MonkeyNum(val num: Int) {
    companion object {
        fun parse(line: String): MonkeyNum =
            MonkeyNum(Regexes.MONKEY_REGEX.matchEntire(line)!!.groupValues[1].toInt())
    }
}
private data class Item(val worryLevel: BigInteger) {
    companion object {
        fun parse(line: String): ArrayDeque<Item> =
            ArrayDeque<Item>().apply { this.addAll(
                Regexes.ITEM_REGEX.findAll(line)
                    .flatMap { it.groupValues }
                    .map { println(it); it }
                    .map { Item(it.toInt().toBigInteger()) }
            ) }
    }
}

object Regexes {
    val MONKEY_REGEX = Regex("Monkey (\\d+):")
    val ITEM_REGEX = Regex("\\d+")
    val OPERATION_REGEX = Regex("  Operation: new = (old|\\d+) ([*+]) (old|\\d+)")
    val TEST_REGEX = Regex("  Test: divisible by (\\d+)")
    val IF_TRUE_REGEX = Regex("    If true: throw to monkey (\\d+)")
    val IF_FALSE_REGEX = Regex("    If false: throw to monkey (\\d+)")
}
