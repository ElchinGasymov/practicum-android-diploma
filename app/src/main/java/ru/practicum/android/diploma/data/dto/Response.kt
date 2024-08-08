package ru.practicum.android.diploma.data.dto

const val RESULT_CODE_NO_INTERNET = -1
const val RESULT_CODE_SUCCESS = 200
const val RESULT_CODE_BAD_REQUEST = 400
const val RESULT_CODE_NOT_FOUND = 404
const val RESULT_CODE_SERVER_ERROR = 500
val CLIENT_ERROR_RANGE = 400..499
val SERVER_ERROR_RANGE = 500..599

open class Response {
    var resultCode = 0
}
