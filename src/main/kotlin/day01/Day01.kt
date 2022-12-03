package day01

private fun elvesCalories(input: String) =
    input.split("\n\n")
        .asSequence()
        .map { elf -> elf.split("\n").filter(String::isNotEmpty).sumOf(String::toInt) }

class Day01 {
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
}
