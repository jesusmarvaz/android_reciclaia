package com.ingencode.reciclaia.common

import android.content.Context
import com.ingencode.bcd.utils.BcdError


/**
Created with ❤ by jesusmarvaz on 2025-01-12.
 */

sealed interface SealedUIStatus {
    object Idle : SealedUIStatus
    class Loading(val message: String) : SealedUIStatus
    class Error(val error: SealedError) : SealedUIStatus
    class Success(val data: Success) : SealedUIStatus
}

sealed class SealedResult<out T> {
    data class ResultSuccess<T>(val data: T) : SealedResult<T>()
    data class ResultError(val error: SealedError) : SealedResult<Nothing>()
}

sealed interface ISealedError {
    fun classifyError(t: Throwable, c: Context) : SealedError {
        return if (t is SealedError) return t
        else if (!c.isAnyNetworkActive()) BcdError.ConnectivityError(c.getString(R.string.check_internet_connection))
        else if (t is HttpException) {
            val stringDTO: String? = t.response()?.errorBody()?.string()
            try {
                val errorDto: ErrorDTO? = stringDTO?.let {
                    Gson().fromJson(JsonParser.parseString(stringDTO), ErrorDTO::class.java)
                }
                val message = errorDto?.message ?: messageDefault
                val code = errorDto?.code
                when (errorDto?.type) {
                    //TODO complete with each type
                    "SERVER_ERROR" -> BcdError.ServerError(message, t, code)
                    "UNABLE_GET_TOKEN" -> BcdError.UnableToGetTokenError(message, t, code)
                    "TOKEN_ERROR", "498" -> BcdError.TokenError(message, t, code)
                    "NOT_AUTHORIZED" -> BcdError.NotAuthorizedError(message, t, code)
                    "REFRESH_ERROR" -> BcdError.RefreshTokenError(message, t, code)
                    "LOGIN_ERROR" -> BcdError.LoginError(message, t, code)
                    "EMAIL_REGISTERED" -> BcdError.SignUpError(message, t, code)
                    "NO_SUBSCRIPTION_FOUND" -> BcdError.NoSubscription(message, t, code)
                    "TIME_LIMIT_PASSED" -> BcdError.TimeLimitPassed(message, t, code)
                    "BOOKS_LIMIT_EXCEEDED" -> BcdError.BooksLimitExceeded(message, t, code)
                    else -> {
                        when (t.code()) {
                            498 -> BcdError.TokenError(message, t, t.code())
                            else -> BcdError.BcdHttpError(message, t, t.code())
                        }
                        when (message) {
                            "Not authorized" -> BcdError.NotAuthorizedError(message, t.cause, t.code())
                            else -> BcdError.BcdHttpError(message, t.cause, t.code())
                        }
                    }
                }
            } catch (e: JsonParseException) {
                when (t.code()) {
                    498 -> BcdError.TokenError(t.message ?: messageDefault, t, t.code())
                    else -> BcdError.BcdHttpError(t.message ?: messageDefault, t, t.code())
                }
            }
        } else {
            BcdError.DefaultError(String.format(c.getString(R.string.generic_error_pattern), t?.message ?: messageDefault), t, null)
        }
    }
}

sealed class SealedError (override val message: String, override val cause: Throwable? = null, val statusCode: Int? = null) : Throwable(), ISealedError {

}