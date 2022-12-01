import java.io.File

fun main(args: Array<String>) {
    println("Hello World!")

//    val input = File("sample.txt").readText()
    val input = File("input.txt").readText()
    val part1 = part1(input)
    val part2 = part2(input)
    println("part1: $part1")
    println("part2: $part2")
}

fun elvesCalories(input: String) =
    input.split("\n\n")
        .asSequence()
        .map { elf -> elf.split("\n").filter(String::isNotEmpty).sumOf(String::toInt) }

fun part1(input: String) = elvesCalories(input).max()
fun part2(input: String): Int {
    val (a, b, c) = elvesCalories(input).fold(Triple(0, 0, 0)) { acc, i ->
        val (a, b, c) = acc
        if (i >= c) {
            Triple(b, c, i)
        } else if (i >= b) {
            Triple(b, i, c)
        } else if (i >= a) {
            Triple(i, b, c)
        } else {
            acc
        }
    }
    return a + b + c
}