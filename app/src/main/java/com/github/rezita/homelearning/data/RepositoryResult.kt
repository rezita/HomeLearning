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

sealed class RepositoryResult<out R> {
    data class Success<out T>(val data: T) : RepositoryResult<T>()
    data class Error(val message: String) : RepositoryResult<Nothing>()
}