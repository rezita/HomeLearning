package com.github.rezita.homelearning.utils

class RemoteError(val message: String){

}

class UploadError(message: String) : Exception(message){

}