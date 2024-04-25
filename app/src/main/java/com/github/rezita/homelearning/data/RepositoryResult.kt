package com.github.rezita.homelearning.data

sealed class RepositoryResult<out R> {
    data class Success<out T>(val data: T) : RepositoryResult<T>()
    data class Error(val message: String) : RepositoryResult<Nothing>()
}