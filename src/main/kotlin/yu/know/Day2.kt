package yu.know

import arrow.core.Either
import java.io.File
import kotlin.math.sign

fun main() {
    loadResource("day2.txt").map(::toFile).map(::count)
        .let {
            when (it) {
                is Either.Left -> println("Error: ${it.value.message}")
                is Either.Right -> println(
                    """
                        Safe reports: ${it.value.first}
                        Safe with one skip reports: ${it.value.second}
                    """.trimIndent()
                )
            }
        }
}

private fun count(file: File): Pair<Int, Int> {
    val lines = file.readLines()
    val values = lines
        .map { line -> line.split(" ").map { it.toInt() } }
    return Pair(values.sumOf { isSafe(it) }, values.sumOf { isSafeWithOneSkip(it) })
}

private fun isSafe(values: List<Int>): Int {
    val sign = (values[0] - values[1]).sign
    for (i in 0..<values.lastIndex) {
        val isSafe = isSafe(values[i], values[i + 1], sign)
        if (!isSafe) return 0
    }
    return 1;
}

private fun isSafeWithOneSkip(values: List<Int>): Int {
    val sign = ((values[0] - values[1]).sign + (values[1] - values[2]).sign + (values[2] - values[3]).sign).sign
    var skipped = false
    var saved: Int? = null
    for (i in 0..<values.lastIndex) {
        val isSafe = isSafe(saved ?: values[i], values[i + 1], sign)
        if (!isSafe) {
            if (skipped) return 0
            saved = when {
                i == 0 && isSafe(values[1], values[2], sign) -> null
                i > 0 && isSafe(values[i - 1], values[i + 1], sign) -> null
                i == values.lastIndex - 1 -> null
                i + 1 < values.lastIndex
                        && isSafe(values[i], values[i + 2], sign) -> values[i]

                else -> return 0
            }
            skipped = true
        } else {
            saved = null
        }
    }
    return 1;
}

private fun isSafe(first: Int, second: Int, sign: Int): Boolean = (first - second) * sign in 1..3
