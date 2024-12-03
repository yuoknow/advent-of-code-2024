package yu.know

import arrow.core.Either
import java.io.File
import kotlin.math.absoluteValue

fun main() {
    loadResource("day3.txt").map(::toFile).map(::count)
        .let {
            when (it) {
                is Either.Left -> println("Error: ${it.value.message}")
                is Either.Right -> println(
                    """
                        Sum of multiplications result: ${it.value.first}
                        Enhanced sum of multiplications: ${it.value.second}
                    """.trimIndent()
                )
            }
        }
}

private fun count(file: File): Pair<Int, Int> {
    val regex = Regex("(mul\\()(\\d{1,3})(,)(\\d{1,3})(\\))")
    val lines = file.readLines()
    val mulSum = lines.asSequence()
        .flatMap { regex.findAll(it) }
        .map { toInt(it.groups[2]) * toInt(it.groups[4]) }
        .sum()
    val enhancedRegex =
        Regex("((?<dont>don't\\(\\))|(?<do>do\\(\\)|\\n)|(mul\\()(?<first>\\d{1,3})(,)(?<second>\\d{1,3})(\\)))")

    val enhancedMulSum = lines.asSequence()
        .flatMap { enhancedRegex.findAll(it) }
        .map {
            when {
                it.groups["dont"] != null -> -1
                it.groups["do"] != null -> 0
                else -> toInt(it.groups["first"]) * toInt(it.groups["second"])
            }
        }.reduce { acc, num ->
            when {
                acc > 0 && num == -1 -> acc * -1
                acc < 0 && num == 0 -> acc * -1
                acc > 0 -> acc + num
                else -> acc
            }
        }

    return Pair(mulSum, enhancedMulSum.absoluteValue)
}

private fun toInt(matchGroup: MatchGroup?): Int {
    return matchGroup?.value?.toInt() ?: 0
}
