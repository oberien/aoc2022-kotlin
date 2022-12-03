import day01.Day01
import java.io.File
import kotlin.system.exitProcess

private fun usage(): Nothing {
    println("usage: `./gradlew run --args '<day> <--sample>'`\nEx. `./gradlew run --args 'day01 --sample'`")
    exitProcess(1)
}

enum class Day {
    Day01, Day02, Day03, Day04, Day05, Day06, Day07, Day08, Day09, Day10,
    Day11, Day12, Day13, Day14, Day15, Day16, Day17, Day18, Day19, Day20,
    Day21, Day22, Day23, Day24, Day25,
}

inline fun <reified T : Enum<*>> enumValueOrNull(name: String): T? =
    T::class.java.enumConstants.firstOrNull { it.name.equals(name, ignoreCase = true) }

fun main(args: Array<String>) {
    val day = args.getOrNull(0)?.let { enumValueOrNull<Day>(it) } ?: usage()
    val prefix = "src/main/kotlin/${day.toString().lowercase()}"
    val filename = when (args.getOrNull(1)) {
        "--sample" -> "$prefix/sample.txt"
        else -> "$prefix/input.txt"
    }

    println("loading file $filename");
    val input = File(filename).readText()

    val dayObject = when (day) {
        Day.Day01 -> Day01()
        else -> TODO()
    }

    val part1 = dayObject.part1(input)
    val part2 = dayObject.part2(input)
    println("part1: $part1")
    println("part2: $part2")
}
