package com.github.rezita.homelearning.data


sealed class SimpleRepositoryResult<T : Any?> {
    class Downloading<T : Any> : SimpleRepositoryResult<T>() {
        override fun equals(other: Any?): Boolean = other is Downloading<*>
        override fun hashCode(): Int = javaClass.hashCode()
    }

    data class Downloaded<T : Any>(val data: List<T>) : SimpleRepositoryResult<T>()
    data class DownloadingError<T : Any>(val message: String) : SimpleRepositoryResult<T>()
}

sealed class NormalRepositoryResult<T : Any?> {
    class Downloading<T : Any> : NormalRepositoryResult<T>() {
        override fun equals(other: Any?): Boolean = other is Downloading<*>
        override fun hashCode(): Int = javaClass.hashCode()
    }

    data class Downloaded<T : Any>(val data: List<T>) : NormalRepositoryResult<T>()

    data class DownloadingError<T : Any>(val message: String) : NormalRepositoryResult<T>()


    data class Uploading<T : Any>(val data: List<T>) : NormalRepositoryResult<T>()
    data class UploadError<T : Any>(val data: List<T>, val message: String) :
        NormalRepositoryResult<T>()

    class Uploaded<T : Any>(val data: List<T>, val message: String) :
        NormalRepositoryResult<T>()
}

sealed class ComplexRepositoryResult<T : Any?, U : Any?> {
    class Downloading<T : Any, U : Any> :
        ComplexRepositoryResult<T, U>() {
        override fun equals(other: Any?): Boolean = other is Downloading<*, *>
        override fun hashCode(): Int = javaClass.hashCode()
    }

    data class Downloaded<T : Any, U : Any>(val downloaded: List<T>, val uploadable: List<U>) :
        ComplexRepositoryResult<T, U>()

    data class DownloadingError<T : Any, U : Any>(val message: String) :
        ComplexRepositoryResult<T, U>()

    data class Uploading<T : Any, U : Any>(val downloaded: List<T>, val uploadable: List<U>) :
        ComplexRepositoryResult<T, U>()

    data class UploadError<T : Any, U : Any>(
        val downloaded: List<T>,
        val uploadable: List<U>,
        val message: String
    ) : ComplexRepositoryResult<T, U>()

    data class Uploaded<T : Any, U : Any>(
        val downloaded: List<T>,
        val uploadable: List<U>,
        val message: String
    ) : ComplexRepositoryResult<T, U>()
}