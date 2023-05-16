package com.example.roffuapp.sealed

sealed class DataResponse<T>(
    var data: T? = null,
    var error: com.example.roffuapp.sealed.Error? = null,
) {
    class Success<T>(data: T) : DataResponse<T>(data = data)
    class Error<T>(error: com.example.roffuapp.sealed.Error) : DataResponse<T>(error = error)
}