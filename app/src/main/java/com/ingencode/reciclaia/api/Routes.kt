package com.ingencode.reciclaia.api

/**
Created with ❤ by Jesús Martín (jesusmarvaz@gmail.com) on 2025-02-03.
 */

object Routes {
    const val BASE_URL = "https://jesusmarvaz.hopto.org/apis/reciclaia/"

    object PathParams {
        const val ID = "id"
    }

    object Headers {
        const val TOKEN_HEADER = "authorization"
    }

    object ApisEndpoints {
        object TestApi {
            const val TEST = "test"
            const val TESTDB = "testdb"
        }
    }
}