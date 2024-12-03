package yu.know

import arrow.core.Either
import java.io.File
import java.net.URL


fun toFile(resource: URL): File {
    return File(resource.toURI())
}

fun loadResource(file: String): Either<Error, URL> {
    val resource = object {}::class.java.classLoader.getResource(file)
    return if (resource == null) Either.Left(Error("File not found")) else Either.Right(resource)
}

data class Error(val message: String)