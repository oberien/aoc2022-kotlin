import day01.Day01
import day02.Day02
import day03.Day03
import day04.Day04
import day05.Day05
import day06.Day06
import day07.Day07
import day08.Day08
import day09.Day09
import day10.Day10
import day11.Day11
import day12.Day12
import day13.Day13
import day14.Day14
import day15.Day15
import day16.Day16
import day17.Day17
import day18.Day18
import java.io.File
import kotlin.system.exitProcess

private fun usage(): Nothing {
    println("usage: `./gradlew run --args '<day> <--sample>'`\nEx. `./gradlew run --args 'day01 --sample'`")
    exitProcess(1)
}

enum class DayEnum {
    DAY01, DAY02, DAY03, DAY04, DAY05, DAY06, DAY07, DAY08, DAY09, DAY10,
    DAY11, DAY12, DAY13, DAY14, DAY15, DAY16, DAY17, DAY18, DAY19, DAY20,
    DAY21, DAY22, DAY23, DAY24, DAY25,
}

interface Day {
    fun part1Sample(input: String, sample: Boolean): String = part1(input)
    fun part1(input: String): String
    fun part2Sample(input: String, sample: Boolean): String = part2(input)
    fun part2(input: String): String
}

inline fun <reified T : Enum<*>> enumValueOrNull(name: String): T? =
    T::class.java.enumConstants.firstOrNull { it.name.equals(name, ignoreCase = true) }

fun main(args: Array<String>) {
    val day = args.getOrNull(0)?.let { enumValueOrNull<DayEnum>(it) } ?: usage()
    val prefix = "src/main/kotlin/${day.toString().lowercase()}"
    val (filename, sample) = when (args.getOrNull(1)) {
        "--sample" -> Pair("$prefix/sample.txt", true)
        "--sample2" -> Pair("$prefix/sample2.txt", true)
        else -> Pair("$prefix/input.txt", false)
    }

    println("loading file $filename");
    val input = File(filename).readText()

    val dayObject = when (day) {
        DayEnum.DAY01 -> Day01()
        DayEnum.DAY02 -> Day02()
        DayEnum.DAY03 -> Day03()
        DayEnum.DAY04 -> Day04()
        DayEnum.DAY05 -> Day05()
        DayEnum.DAY06 -> Day06()
        DayEnum.DAY07 -> Day07()
        DayEnum.DAY08 -> Day08()
        DayEnum.DAY09 -> Day09()
        DayEnum.DAY10 -> Day10()
        DayEnum.DAY11 -> Day11()
        DayEnum.DAY12 -> Day12()
        DayEnum.DAY13 -> Day13()
        DayEnum.DAY14 -> Day14()
        DayEnum.DAY15 -> Day15()
        DayEnum.DAY16 -> Day16()
        DayEnum.DAY17 -> Day17()
        DayEnum.DAY18 -> Day18()
        else -> TODO()
    }

    val part1 = dayObject.part1Sample(input, sample)
    println("part1: $part1")
    val part2 = dayObject.part2Sample(input, sample)
    println("part2: $part2")
}
