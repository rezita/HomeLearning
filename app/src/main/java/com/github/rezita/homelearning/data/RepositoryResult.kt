package com.github.rezita.homelearning.data


sealed class SimpleRepositoryResult<T : Any?>(
    val data: List<T> = emptyList(),
    val message: String = ""
) {
    class Downloading<T : Any> : SimpleRepositoryResult<T>() {
        override fun equals(other: Any?): Boolean = other is Downloading<*>
        override fun hashCode(): Int = javaClass.hashCode()
    }

    class Downloaded<T : Any>(data: List<T>) : SimpleRepositoryResult<T>(data = data)
    class DownloadingError<T : Any>(message: String) : SimpleRepositoryResult<T>(message = message)
}

sealed class NormalRepositoryResult<T : Any?>(
    val data: List<T> = emptyList(),
    val message: String = ""
) {
    class Downloading<T : Any>() : NormalRepositoryResult<T>() {
        override fun equals(other: Any?): Boolean = other is Downloading<*>
        override fun hashCode(): Int = javaClass.hashCode()
    }

    class Downloaded<T : Any>(data: List<T>) : NormalRepositoryResult<T>(data = data)
    class DownloadingError<T : Any>(message: String) : NormalRepositoryResult<T>(message = message)


    class Uploading<T : Any>(data: List<T>) : NormalRepositoryResult<T>(data = data)
    class UploadError<T : Any>(data: List<T>, message: String) :
        NormalRepositoryResult<T>(data = data, message = message)

    class Uploaded<T : Any>(data: List<T>, message: String) :
        NormalRepositoryResult<T>(data = data, message = message)
}

sealed class ComplexRepositoryResult<T : Any?, U : Any?>(
    val downloaded: List<T> = emptyList(),
    val uploadable: List<U> = emptyList(),
    val message: String = ""
) {
    class Downloading<T : Any, U : Any>() :
        ComplexRepositoryResult<T, U>() {
        override fun equals(other: Any?): Boolean = other is Downloading<*, *>
        override fun hashCode(): Int = javaClass.hashCode()
    }

    class Downloaded<T : Any, U : Any>(downloaded: List<T>, uploadable: List<U>) :
        ComplexRepositoryResult<T, U>(downloaded = downloaded, uploadable = uploadable)

    class DownloadingError<T : Any, U : Any>(message: String) :
        ComplexRepositoryResult<T, U>(message = message)

    class Uploading<T : Any, U : Any>(downloaded: List<T>, uploadable: List<U>) :
        ComplexRepositoryResult<T, U>(downloaded = downloaded, uploadable = uploadable)

    class UploadError<T : Any, U : Any>(
        downloaded: List<T>,
        uploadable: List<U>,
        message: String
    ) : ComplexRepositoryResult<T, U>(
        downloaded = downloaded,
        uploadable = uploadable,
        message = message
    )

    class Uploaded<T : Any, U : Any>(
        downloaded: List<T>,
        uploadable: List<U>,
        message: String
    ) : ComplexRepositoryResult<T, U>(
        downloaded = downloaded,
        uploadable = uploadable,
        message = message
    )
}