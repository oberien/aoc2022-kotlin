package day02

import Day
import java.lang.IllegalArgumentException

class Day02 : Day {
    override fun part1(input: String): String =
        input.lineSequence()
            .filter { it.isNotEmpty() }
            .map { it.split(" ") }
            .map { (opponent, me) -> Pair(RockPaperScissors.fromString(opponent), RockPaperScissors.fromString(me)) }
            .map { (opponent, me) -> me.score() + playAgainst(me, opponent).score() }
            .sum()
            .toString()

    override fun part2(input: String): String =
        input.lineSequence()
            .filter { it.isNotEmpty() }
            .map { it.split(" ") }
            .map { (opponent, result) -> Pair(RockPaperScissors.fromString(opponent), RPSResult.fromString(result)) }
            .map { (opponent, result) -> result.score() + calculateMe(opponent, result).score() }
            .sum()
            .toString()
}

private interface Score {
    fun score(): Int
}

private enum class RPSResult: Score {
    WIN, LOSS, DRAW;

    override fun score() =
        when (this) {
            WIN -> 6
            LOSS -> 0
            DRAW -> 3
        }

    companion object {
        fun fromString(input: String) =
            when (input) {
                "X" -> LOSS
                "Y" -> DRAW
                "Z" -> WIN
                else -> throw Exception("RPSResult argument isn't XYZ")
            }
    }
}

private enum class RockPaperScissors: Score {
    ROCK, PAPER, SCISSORS;

    override fun score() =
        when (this) {
            ROCK -> 1
            PAPER -> 2
            SCISSORS -> 3
        }


    companion object {
        fun fromString(input: String) =
            when (input) {
                "A", "X" -> ROCK
                "B", "Y" -> PAPER
                "C", "Z" -> SCISSORS
                else -> throw IllegalArgumentException("RockPaperScissors argument isn't ABCXYZ")
            }
    }
}

private fun playAgainst(me: RockPaperScissors, other: RockPaperScissors) =
    when (Pair(me, other)) {
        Pair(RockPaperScissors.ROCK, RockPaperScissors.ROCK) -> RPSResult.DRAW
        Pair(RockPaperScissors.ROCK, RockPaperScissors.PAPER) -> RPSResult.LOSS
        Pair(RockPaperScissors.ROCK, RockPaperScissors.SCISSORS) -> RPSResult.WIN
        Pair(RockPaperScissors.PAPER, RockPaperScissors.ROCK) -> RPSResult.WIN
        Pair(RockPaperScissors.PAPER, RockPaperScissors.PAPER) -> RPSResult.DRAW
        Pair(RockPaperScissors.PAPER, RockPaperScissors.SCISSORS) -> RPSResult.LOSS
        Pair(RockPaperScissors.SCISSORS, RockPaperScissors.ROCK) -> RPSResult.LOSS
        Pair(RockPaperScissors.SCISSORS, RockPaperScissors.PAPER) -> RPSResult.WIN
        Pair(RockPaperScissors.SCISSORS, RockPaperScissors.SCISSORS) -> RPSResult.DRAW
        else -> throw Exception("unreachable")
    }

private fun calculateMe(other: RockPaperScissors, result: RPSResult) =
    when (Pair(other, result)) {
        Pair(RockPaperScissors.ROCK, RPSResult.WIN) -> RockPaperScissors.PAPER
        Pair(RockPaperScissors.ROCK, RPSResult.LOSS) -> RockPaperScissors.SCISSORS
        Pair(RockPaperScissors.ROCK, RPSResult.DRAW) -> RockPaperScissors.ROCK
        Pair(RockPaperScissors.PAPER, RPSResult.WIN) -> RockPaperScissors.SCISSORS
        Pair(RockPaperScissors.PAPER, RPSResult.LOSS) -> RockPaperScissors.ROCK
        Pair(RockPaperScissors.PAPER, RPSResult.DRAW) -> RockPaperScissors.PAPER
        Pair(RockPaperScissors.SCISSORS, RPSResult.WIN) -> RockPaperScissors.ROCK
        Pair(RockPaperScissors.SCISSORS, RPSResult.LOSS) -> RockPaperScissors.PAPER
        Pair(RockPaperScissors.SCISSORS, RPSResult.DRAW) -> RockPaperScissors.SCISSORS
        else -> throw Exception("unreachable")
    }
