import day01.Day01
import day02.Day02
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
    fun part1(input: String): Int
    fun part2(input: String): Int
}

inline fun <reified T : Enum<*>> enumValueOrNull(name: String): T? =
    T::class.java.enumConstants.firstOrNull { it.name.equals(name, ignoreCase = true) }

fun main(args: Array<String>) {
    val day = args.getOrNull(0)?.let { enumValueOrNull<DayEnum>(it) } ?: usage()
    val prefix = "src/main/kotlin/${day.toString().lowercase()}"
    val filename = when (args.getOrNull(1)) {
        "--sample" -> "$prefix/sample.txt"
        else -> "$prefix/input.txt"
    }

    println("loading file $filename");
    val input = File(filename).readText()

    val dayObject = when (day) {
        DayEnum.DAY01 -> Day01()
        DayEnum.DAY02 -> Day02()
        else -> TODO()
    }

    val part1 = dayObject.part1(input)
    val part2 = dayObject.part2(input)
    println("part1: $part1")
    println("part2: $part2")
}
