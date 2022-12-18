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
    fun part1(input: String): String
    fun part2(input: String): String
}

inline fun <reified T : Enum<*>> enumValueOrNull(name: String): T? =
    T::class.java.enumConstants.firstOrNull { it.name.equals(name, ignoreCase = true) }

fun main(args: Array<String>) {
    val day = args.getOrNull(0)?.let { enumValueOrNull<DayEnum>(it) } ?: usage()
    val prefix = "src/main/kotlin/${day.toString().lowercase()}"
    val filename = when (args.getOrNull(1)) {
        "--sample" -> "$prefix/sample.txt"
        "--sample2" -> "$prefix/sample2.txt"
        else -> "$prefix/input.txt"
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
        else -> TODO()
    }

    val part1 = dayObject.part1(input)
    val part2 = dayObject.part2(input)
    println("part1: $part1")
    println("part2: $part2")
}
