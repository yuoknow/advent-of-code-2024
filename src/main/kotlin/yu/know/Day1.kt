package yu.know

import arrow.core.Either
import java.io.File
import java.net.URL
import kotlin.math.abs

fun main() {
    loadResource("day1.txt").map(::toFile).map(::count)
        .let {
            when (it) {
                is Either.Left -> println("Error: ${it.value.message}")
                is Either.Right -> println(
                    """
                        Total distance: ${it.value.first}
                        Similarity score: ${it.value.second}
                    """.trimIndent()
                )
            }
        }
}

private fun count(file: File): Pair<Int, Int> {
    val leftArr = mutableListOf<Int>()
    val rightArr = mutableListOf<Int>()
    file.readLines().map {
        val (left, _, _, right) = it.split(" ")
        leftArr.add(left.toInt())
        rightArr.add(right.toInt())
    }

    val totalDistance = leftArr.sorted().zip(rightArr.sorted()).sumOf { (left, right) -> abs(left - right) }
    val similarityScore = leftArr.groupingBy { it }.eachCount()
        .map { left -> rightArr.count { right -> left.key == right } * left.key * left.value }
        .sum()

    return Pair(totalDistance, similarityScore)
}

private fun toFile(resource: URL): File {
    return File(resource.toURI())
}

private fun loadResource(file: String): Either<Error, URL> {
    val resource = object {}::class.java.classLoader.getResource(file)
    return if (resource == null) Either.Left(Error("File not found")) else Either.Right(resource)
}

private data class Error(val message: String)