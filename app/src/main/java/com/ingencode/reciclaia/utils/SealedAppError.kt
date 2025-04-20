package com.ingencode.reciclaia.utils

/**
Created with ❤ by jesusmarvaz on 2025-01-12.
 */

sealed interface ISealedError

sealed class SealedApiError (override val cause: Throwable? = null, val statusCode: Int? = null) : Throwable(), ISealedError {
    class WrongResponseData(cause: Throwable?, statusCode: Int?): SealedApiError(cause, statusCode)
    class HttpError(cause: Throwable?, statusCode: Int?) : SealedApiError(cause, statusCode)
    class ServerError(cause: Throwable?, statusCode: Int?) : SealedApiError(cause, statusCode)
    class UnableToGetTokenError(cause: Throwable?, statusCode: Int?) : SealedApiError(cause, statusCode)
    class TokenError(cause: Throwable?, statusCode: Int?) : SealedApiError(cause, statusCode)
    class NotAuthorizedError(cause: Throwable?, statusCode: Int?) : SealedApiError(cause, statusCode)
    class RefreshTokenError(cause: Throwable?, statusCode: Int?): SealedApiError(cause, statusCode)
    class TimeLimitPassed(cause: Throwable?, statusCode: Int?): SealedApiError(cause, statusCode)
}

sealed class SealedAppError(val message: String? = null) : ISealedError {
    class DefaultError(message: String? = null): SealedAppError(message)
    class WrongFormData(): SealedAppError()
    class ConnectivityError() : SealedAppError()
    class ProblemSavingImagesLocally(message: String? = null): SealedAppError(message)
    class LocalRepositoryError(message: String? = null): SealedAppError(message)
}