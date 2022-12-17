package day10

import Day
import java.lang.StringBuilder
import kotlin.math.abs

class Day10 : Day {
    override fun part1(input: String): String {
        var result = 0
        var wantedNext = 20
        val instructions = parse(input)
        val cpu = Cpu()
        for (instruction in instructions) {
            val oldX = cpu.getX()
            cpu.execute(instruction)
            result += when {
                cpu.getCycles() >= wantedNext -> wantedNext.also { wantedNext += 40 } * oldX
                else -> 0
            }
            println("${cpu.getCycles()}: x=${cpu.getX()} -> $result    ($instruction)")
        }
        return result.toString()
    }

    override fun part2(input: String): String =
        Cpu().apply { parse(input).forEach { execute(it) } }
            .printCrt()
}

private fun parse(input: String): List<Instruction> =
    input.lineSequence()
        .map { it.split(" ") }
        .map { when (it[0]) {
            "addx" -> Instruction.Addx(it[1].toInt())
            "noop" -> Instruction.Noop
            else -> throw IllegalArgumentException("instruction not addx or noop: $it")
        } }
        .toList()

private sealed class Instruction {
    data class Addx(val value: Int) : Instruction()
    object Noop : Instruction()
}

private class Cpu {
    private var x: Int = 1
    private var cycles: Int = 0
    private var crt = mutableListOf<Char>()
    private var crtPosition = 0

    fun getX(): Int = this.x
    fun getCycles(): Int = this.cycles

    fun execute(instruction: Instruction) {
        when (instruction) {
            is Instruction.Addx -> {
                drawCrt()
                drawCrt()
                x += instruction.value
                cycles += 2
            }
            is Instruction.Noop -> {
                drawCrt()
                cycles += 1
            }
        }
    }

    private fun drawCrt() {
        val char = if (abs(crtPosition - x) <= 1) '#' else '.'
        crt.add(char)
        crtPosition += 1
        crtPosition %= 40
    }

    fun printCrt(): String {
        val sb = StringBuilder()
        for ((i, char) in crt.withIndex()) {
            if (i % 40 == 0) {
                sb.append('\n')
            }
            sb.append(char)
        }
        sb.append('\n')
        return sb.toString()
    }
}
